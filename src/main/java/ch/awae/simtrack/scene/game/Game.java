package ch.awae.simtrack.scene.game;

import java.awt.Dimension;
import java.util.concurrent.atomic.AtomicBoolean;

import ch.awae.simtrack.core.Scene;
import ch.awae.simtrack.core.SceneController;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.DesktopComponent;
import ch.awae.simtrack.core.ui.LayoutPositioning.PositionH;
import ch.awae.simtrack.core.ui.LayoutPositioning.PositionV;
import ch.awae.simtrack.scene.game.controller.Editor;
import ch.awae.simtrack.scene.game.controller.PathFinding;
import ch.awae.simtrack.scene.game.controller.TrainController;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.controller.event.EventType;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.view.renderer.MapRenderer;
import ch.awae.simtrack.scene.game.view.renderer.TileRenderer;
import ch.awae.simtrack.scene.game.view.windows.DebugToolsView;
import ch.awae.simtrack.scene.game.view.windows.InGameMenu;
import ch.awae.simtrack.scene.game.view.windows.InputGuideView;
import ch.awae.simtrack.scene.game.view.windows.PlayerInfoView;
import ch.awae.simtrack.scene.game.view.windows.SelectedTileWindow;
import ch.awae.simtrack.scene.game.view.windows.ToolBar;

public class Game extends Scene {

	private PathFinding pathfinder;
	private Window window;
	private Model model;
	private ViewPortNavigator viewPortNavigator;
	private Editor editor;

	private TrainController trainController;
	private DesktopComponent ui;
	private InputController input;

	public Game(SceneController sceneController, Model modelToLoad, Window window, InputController input) {
		super(sceneController);
		this.window = window;
		this.input = input;
		this.model = modelToLoad;
		this.model.load();
		this.viewPortNavigator = new ViewPortNavigator(this.model, this.window.getScreenSize(), input);
		this.pathfinder = new PathFinding(this.model);
		this.trainController = new TrainController(this.model);
		this.editor = new Editor(this.model, this.viewPortNavigator, input, this.pathfinder);
		this.ui = new DesktopComponent(input);
		setupUI();

		addRenderer(new MapRenderer(this.input, this.model, this.viewPortNavigator));
		addRenderer(this.editor);
		addRenderer(this.ui);

		addTicker(this.editor);
		addTicker(this.viewPortNavigator);
		addTicker(this.model);
		addTicker(this.pathfinder);
		addTicker(this.trainController);
	}

	private void setupUI() {
		this.ui.layout(0, 0, this.window.getScreenSize().width, this.window.getScreenSize().height);
		DebugToolsView debugWindow = new DebugToolsView(this.editor, this.trainController, this.input,
				this.sceneController, this.model);
		this.ui.addWindow(debugWindow, PositionH.LEFT, PositionV.TOP);

		InputGuideView inputGuide = new InputGuideView(this.input, this.model);
		this.ui.addWindow(inputGuide);

		PlayerInfoView infoView = new PlayerInfoView(this.model, this.input);
		this.ui.addWindow(infoView, PositionH.CENTER, PositionV.TOP);

		ToolBar toolbar = new ToolBar(this.editor, input, this.viewPortNavigator);
		this.ui.addWindow(toolbar, PositionH.CENTER, PositionV.BOTTOM);

		this.model.getEventBus().subscribe(EventType.SELECTION_CHANGED, (type, args) -> {
			if (args != null && args.length > 0 && args[0] != null) {
				this.ui.addWindow(new SelectedTileWindow(this.model, input), PositionH.RIGHT, PositionV.TOP);
			}
		});
	}

	@Override
	public void preTick(long millis) {
		this.model.getClock().tick(millis);
	}

	@Override
	public void screenResized(Dimension screenSize) {
		this.viewPortNavigator.setScreenSize(screenSize);
		this.ui.layout(0, 0, screenSize.width, screenSize.height);
		TileRenderer.reloadGraphics();
	}

	@Override
	public void handleInput(InputEvent event) {
		this.ui.handleInput(event);
		if (!event.isConsumed)
			this.viewPortNavigator.handleInput(event);
		if (!event.isConsumed)
			this.editor.handleInput(event);
		if (!event.isConsumed)
			checkIfGameMenuShouldOpen(event);
	}

	private void checkIfGameMenuShouldOpen(InputEvent event) {
		if (event.isPressActionAndConsume(InputAction.DROP_TOOL)) {
			InGameMenu ingameMenu = new InGameMenu(input, this.model, this.sceneController, this.ui);
			this.ui.addWindow(ingameMenu);
		}
	}

	private void toggleInputGuide() {
		AtomicBoolean inputGuide = this.model.getDebugOptions().getShowInputGuide();
		inputGuide.set(!inputGuide.get());
	}

}
