package ch.awae.simtrack.scene.game.view;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.scene.Scene;
import ch.awae.simtrack.scene.Window;
import ch.awae.simtrack.scene.game.controller.Navigator;
import ch.awae.simtrack.scene.game.controller.PathFinding;
import ch.awae.simtrack.scene.game.controller.tools.BuildTool;
import ch.awae.simtrack.scene.game.controller.tools.DebugTools;
import ch.awae.simtrack.scene.game.controller.tools.FreeTool;
import ch.awae.simtrack.scene.game.controller.tools.InGameMenu;
import ch.awae.simtrack.scene.game.controller.tools.PathFindingTool;
import ch.awae.simtrack.scene.game.controller.tools.SignalTool;
import ch.awae.simtrack.scene.game.controller.tools.ToolBar;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.view.renderer.BackgroundRenderer;
import ch.awae.simtrack.scene.game.view.renderer.EntityRenderer;
import ch.awae.simtrack.scene.game.view.renderer.HexGridRenderer;
import ch.awae.simtrack.scene.game.view.renderer.SignalRenderer;
import ch.awae.simtrack.scene.game.view.renderer.TileRenderer;
import lombok.Getter;

/**
 * the game view implementation
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class GameView extends Scene<GameView> {

	private @Getter PathFinding pathfinder;
	private Model model;
	private ViewPort viewPort;
	private Editor<GameView> editor;

	public boolean drawHex = true;

	private ToolBar trackbar;
	private DebugTools debugTools;

	/**
	 * instantiates a new game view
	 * 
	 * @param model
	 * @param screenX
	 * @param screenY
	 */
	public GameView(Model model, Window window) {
		super(window);
		this.model = model;
		this.viewPort = new ViewPort(this);
		this.editor = new Editor<GameView>(this);
		this.trackbar = new ToolBar(editor);
		this.debugTools = new DebugTools(editor);
		this.pathfinder = new PathFinding(model);

		editor.addTool(new FreeTool(editor));
		editor.addTool(new BuildTool(editor));
		editor.addTool(new PathFindingTool(editor));
		editor.addTool(new InGameMenu(editor));
		editor.addTool(new SignalTool(editor));

		addRenderer(new BackgroundRenderer());
		addRenderer(new TileRenderer());
		addRenderer(new HexGridRenderer());
		addRenderer(new SignalRenderer());
		addRenderer(new EntityRenderer());
		addRenderer(editor);
		addRenderer(debugTools.getRenderer());
		addRenderer(trackbar);

		addTicker(new Navigator(this, input));
		addTicker(trackbar);
		addTicker(editor);
		addTicker(debugTools);
		addTicker(s -> viewPort.tick());
		addTicker(s -> model.tick());
		addTicker(pathfinder);
	}

	/**
	 * moves the view by the given amount
	 * 
	 * @param dx
	 * @param dy
	 */
	public void moveScene(int dx, int dy) {
		this.viewPort.moveScene(dx, dy);
	}

	/**
	 * zooms the view by the given amount at the given point. the given point
	 * remains stationary while zooming.
	 * 
	 * @param dzoom
	 * @param fixX
	 * @param fixY
	 */
	public void zoom(float dzoom, int fixX, int fixY) {
		this.viewPort.zoom((int) (100 * dzoom), fixX, fixY);
	}

	public Model getModel() {
		return this.model;
	}

	public int getHorizontalScreenSize() {
		return width;
	}

	public int getVerticalScreenSize() {
		return height;
	}

	public ViewPort getViewPort() {
		return this.viewPort;
	}

	public void loadModel(Model model) {
		this.model = model;
		this.model.load();
		this.pathfinder.setModel(model);
	}

	public void toggleHex() {
		this.drawHex = !this.drawHex;
	}

}
