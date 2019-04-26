package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.controller.Editor;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.model.entity.Signal;
import ch.awae.simtrack.scene.game.model.tile.TransformableTrackTile;
import ch.awae.simtrack.scene.game.model.tile.track.TrackProvider;
import ch.awae.simtrack.scene.game.view.renderer.Renderer;
import ch.awae.simtrack.scene.game.view.renderer.TrackRenderUtil;
import ch.awae.utils.functional.T2;

/**
 * Track tool-bar used for track selection while editing the board
 */
public class ToolBar extends GameTool {

	private int index;

	private Color bdcol = new Color(0.0f, 0.0f, 0.0f);
	private Stroke bdst = new BasicStroke(2);
	private Color bgcol = new Color(1.0f, 1.0f, 1.0f);
	private Color hover = new Color(0.8f, 0.8f, 0.8f);
	private Color rails = new Color(0.2f, 0.2f, 0.2f);
	private Color rbeds = new Color(0.6f, 0.6f, 0.6f);
	private Stroke xstr = new BasicStroke(6);

	private ArrayList<T2<Runnable, Renderer>> bindings = new ArrayList<>();

	private InputController input;

	/**
	 * creates a new track-bar instance
	 * 
	 * @param editor the editor owning the build tool
	 */
	public ToolBar(Editor editor, InputController input, ViewPortNavigator viewPort) {
		super(editor, viewPort, false);
		this.input = input;

		// ######### BINDINGS ##########
		// bind bulldoze
		bind(() -> editor.loadTool(BuildTool.class), (g) -> {
			g.setColor(Color.RED);
			g.setStroke(this.xstr);
			g.drawLine(-25, -25, 25, 25);
			g.drawLine(-25, 25, 25, -25);
		});
		// bind track tiles
		for (int i = 0; i < TrackProvider.getTileCount(); i++) {
			if (i != 3 && i != 4 && i != 8) {
				TransformableTrackTile tile = TrackProvider.getTileInstance(i);
				bind(() -> editor.loadTool(BuildTool.class, tile), (g) -> {
					g.scale(0.8, 0.8);
					TrackRenderUtil.renderRails(g, this.rbeds, this.rails, tile.getRailPaths());
				});
			}
		}
		// signal
		bind(() -> editor.loadTool(SignalTool.class), (g) -> {
			g.setColor(Color.RED);
			g.setStroke(this.xstr);
			g.drawLine(-25, -25, 25, 25);
			g.drawLine(-25, 25, 25, -25);
			g.setColor(Color.BLACK);
			g.setFont(g.getFont().deriveFont(20f));
			g.drawString("Signal", -30, 5);
		});
		bind(() -> editor.loadTool(SignalTool.class, Signal.Type.TWO_WAY), (g) -> {
			g.setColor(Color.BLACK);
			g.fillOval(-30, -30, 60, 60);
			g.setColor(Color.RED);
			g.fillOval(-23, -23, 46, 46);
		});
		bind(() -> editor.loadTool(SignalTool.class, Signal.Type.ONE_WAY), (g) -> {
			g.setColor(Color.BLACK);
			g.fillRect(-30, -30, 60, 60);
			g.setColor(Color.RED);
			g.fillOval(-23, -23, 46, 46);
		});
	}

	@Override
	public void handleInput(InputEvent event) {
		if (this.index >= 0 && event.isPressActionConsumeAndRun(InputAction.SELECT, this::select))
			return;
		if (event.isPressActionConsumeAndRun(InputAction.TOOLBAR_0, () -> select(0)))
			return;
		if (event.isPressActionConsumeAndRun(InputAction.TOOLBAR_1, () -> select(1)))
			return;
		if (event.isPressActionConsumeAndRun(InputAction.TOOLBAR_2, () -> select(2)))
			return;
		if (event.isPressActionConsumeAndRun(InputAction.TOOLBAR_3, () -> select(3)))
			return;
		if (event.isPressActionConsumeAndRun(InputAction.TOOLBAR_4, () -> select(4)))
			return;
		if (event.isPressActionConsumeAndRun(InputAction.TOOLBAR_5, () -> select(5)))
			return;
		if (event.isPressActionConsumeAndRun(InputAction.TOOLBAR_6, () -> select(6)))
			return;
		if (event.isPressActionConsumeAndRun(InputAction.TOOLBAR_7, () -> select(7)))
			return;
		if (event.isPressActionConsumeAndRun(InputAction.TOOLBAR_8, () -> select(8)))
			return;
		if (event.isPressActionConsumeAndRun(InputAction.TOOLBAR_9, () -> select(9)))
			return;
		if (event.isPressActionConsumeAndRun(InputAction.TOOLBAR_10, () -> select(10)))
			return;
		super.handleInput(event);
	}

	int getIndex() {
		return this.index;
	}

	private void bind(Runnable ru, Renderer re) {
		bindings.add(new T2<>(ru, re));
	}

	private void select(int index) {
		this.index = index;
		select();
	}

	private void select() {
		if (index < 0 || index >= bindings.size())
			return;

		bindings.get(index)._1.run();
	}

	@Override
	public void tick() {
		updateByMouse();
		super.tick();
	}

	public void updateByMouse() {
		this.index = -1;
		Point p = this.input.getMousePosition();
		if (p == null) {
			p = new Point(0, 0);
		}
		p = p.getLocation();
		p.x -= this.viewPort.getScreenSize().width / 2;
		p.x += 550;
		p.y -= this.viewPort.getScreenSize().height;
		p.y += 100;
		if (p.x < 0 || p.y < 0)
			return;
		int index = p.x / 100;
		if (index > 10)
			return;
		this.index = index;
	}

	@Override
	public void render(Graphics g) {
		Dimension screenSize = this.viewPort.getScreenSize();
		g.translate(screenSize.width / 2 - 500, screenSize.height - 50);
		g.setStroke(new BasicStroke(4));
		for (int i = 0; i < 11; i++) {
			// box
			g.setStroke(this.bdst);
			g.setColor(i == index ? this.hover : this.bgcol);
			g.fillRect(-50, -50, 100, 100);
			g.setColor(this.bdcol);
			g.drawRect(-50, -50, 100, 100);
			if (bindings.size() > i)
				bindings.get(i)._2.renderSafe(g);
			g.translate(100, 0);
		}
	}

}
