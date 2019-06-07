package ch.awae.simtrack.scene.game.view.windows;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;

import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.ui.WindowComponent;
import ch.awae.simtrack.scene.game.controller.Editor;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.controller.tools.BuildTool;
import ch.awae.simtrack.scene.game.controller.tools.SignalTool;
import ch.awae.simtrack.scene.game.model.entity.Signal;
import ch.awae.simtrack.scene.game.model.tile.track.ConstructionTrackTile;
import ch.awae.simtrack.scene.game.model.tile.track.TrackProvider;
import ch.awae.simtrack.scene.game.view.Design;
import ch.awae.simtrack.scene.game.view.ToolBarButton;
import ch.awae.simtrack.scene.game.view.renderer.TrackRenderUtil;
import ch.awae.simtrack.window.Graphics;

/**
 * Track tool-bar used for track selection while editing the board
 */
public class ToolBar extends WindowComponent {

	private Color bdcol = new Color(0.0f, 0.0f, 0.0f);
	private Stroke bdst = new BasicStroke(2);
	private Color bgcol = new Color(1.0f, 1.0f, 1.0f);
	private Color hover = new Color(0.8f, 0.8f, 0.8f);
	private Color rails = new Color(0.2f, 0.2f, 0.2f);
	private Color rbeds = new Color(0.6f, 0.6f, 0.6f);
	private Stroke xstr = new BasicStroke(6);
	private Font buttonFont = new Font(Design.textFont.getName(),Font.BOLD, Design.textFont.getSize()+4);

	private Editor editor;
	private ViewPortNavigator viewPort;

	/**
	 * creates a new track-bar instance
	 * 
	 * @param editor the editor owning the build tool
	 */
	public ToolBar(Editor editor, InputController input, ViewPortNavigator viewPort) {
		super(Design.textFont, input);
		this.editor = editor;
		this.viewPort = viewPort;

		this.isHeadless = true;
		this.isMovable = false;
		this.content.setVertical(false);

		initMenu();
	}

	private void initMenu() {
		int toolbarHotkeyNumber = 0;
		addComponent(new ToolBarButton(this.input, () -> this.editor.loadTool(BuildTool.class)) {
			@Override
			protected void renderIcon(Graphics g) {
				g.setColor(Color.RED);
				g.setStroke(xstr);
				g.drawLine(-25, -25, 25, 25);
				g.drawLine(-25, 25, 25, -25);
			}
		}.setInputAction(InputAction.getToolbarActionByNumber(toolbarHotkeyNumber++)));
		// track tiles
		for (ConstructionTrackTile tile : TrackProvider.getTiles()) {
			if (tile.isShowInToolbar()) {
				addComponent(new ToolBarButton(this.input, () -> this.editor.loadTool(BuildTool.class, tile)) {
					@Override
					protected void renderIcon(Graphics g) {
						g.translate(0, 15);
						TrackRenderUtil.renderRails(g, rbeds, rails, tile.getPaths());
						g.translate(0, -15);
						g.setColor(Color.BLACK);
						g.setFont(buttonFont);
						g.drawString(tile.getBuildCost()+"$", -22, -34);
					}
				}.setInputAction(InputAction.getToolbarActionByNumber(toolbarHotkeyNumber++)));
			}
		}
		// signal
		addComponent(new ToolBarButton(this.input, () -> this.editor.loadTool(SignalTool.class)) {
			@Override
			protected void renderIcon(Graphics g) {
				g.setColor(Color.RED);
				g.setStroke(xstr);
				g.drawLine(-25, -25, 25, 25);
				g.drawLine(-25, 25, 25, -25);
				g.setColor(Color.BLACK);
				g.setFont(buttonFont);
				g.drawString("Signal", -27, -34);
			}
		}.setInputAction(InputAction.getToolbarActionByNumber(toolbarHotkeyNumber++)));
		addComponent(new ToolBarButton(this.input, () -> this.editor.loadTool(SignalTool.class, Signal.Type.TWO_WAY)) {
			@Override
			protected void renderIcon(Graphics g) {
				g.setColor(Color.BLACK);
				g.fillOval(-30, -30, 60, 60);
				g.setColor(Color.RED);
				g.fillOval(-23, -23, 46, 46);
				g.setColor(Color.BLACK);
				g.setFont(buttonFont);
				g.drawString("Two-Way", -40, -34);
			}
		}.setInputAction(InputAction.getToolbarActionByNumber(toolbarHotkeyNumber++)));
		addComponent(new ToolBarButton(this.input, () -> this.editor.loadTool(SignalTool.class, Signal.Type.ONE_WAY)) {
			@Override
			protected void renderIcon(Graphics g) {
				g.setColor(Color.BLACK);
				g.fillRect(-30, -30, 60, 60);
				g.setColor(Color.RED);
				g.fillOval(-23, -23, 46, 46);
				g.setColor(Color.BLACK);
				g.setFont(buttonFont);
				g.drawString("One-Way", -40, -34);
			}
		}.setInputAction(InputAction.getToolbarActionByNumber(toolbarHotkeyNumber++)));
	}

}
