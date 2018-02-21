package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.input.Input;
import ch.awae.simtrack.scene.Graphics;
import ch.awae.simtrack.scene.game.model.entity.Signal;
import ch.awae.simtrack.scene.game.model.tile.TransformableTrackTile;
import ch.awae.simtrack.scene.game.model.tile.track.TrackProvider;
import ch.awae.simtrack.scene.game.view.GameView;
import ch.awae.simtrack.scene.game.view.renderer.Renderer;
import ch.awae.simtrack.scene.game.view.renderer.TrackRenderUtil;
import ch.awae.simtrack.util.T2;

/**
 * Track tool-bar used for track selection while editing the board
 * 
 * @author Andreas Wälchli
 * @version 1.5, 2015-01-26
 * @since SimTrack 0.2.2 (0.2.1)
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

	/**
	 * creates a new track-bar instance
	 * 
	 * @param editor
	 *            the editor owning the build tool
	 */
	public ToolBar(Editor<GameView> editor) {
		super(editor, UnloadAction.IGNORE);

		// ######### BINDINGS ##########
		// bind bulldoze
		bind(() -> editor.loadTool(BuildTool.class), (g, v) -> {
			g.setColor(Color.RED);
			g.setStroke(this.xstr);
			g.drawLine(-25, -25, 25, 25);
			g.drawLine(-25, 25, 25, -25);
		});
		// bind track tiles
		for (int i = 0; i < TrackProvider.getTileCount(); i++) {
			if (i != 3 && i != 4 && i != 8) {
				TransformableTrackTile tile = TrackProvider.getTileInstance(i);
				bind(() -> editor.loadTool(BuildTool.class, tile), (g, v) -> {
					g.scale(0.8, 0.8);
					TrackRenderUtil.renderRails(g, this.rbeds, this.rails, tile.getRailPaths());
				});
			}
		}
		// signal
		bind(() -> editor.loadTool(SignalTool.class), (g, v) -> {
			g.setColor(Color.RED);
			g.setStroke(this.xstr);
			g.drawLine(-25, -25, 25, 25);
			g.drawLine(-25, 25, 25, -25);
			g.setColor(Color.BLACK);
			g.setFont(g.getFont().deriveFont(20f));
			g.drawString("Signal", -30, 5);
		});
		bind(() -> editor.loadTool(SignalTool.class, Signal.Type.TWO_WAY), (g, v) -> {
			g.setColor(Color.MAGENTA);
			g.fillOval(-25, -25, 50, 50);
			g.setColor(Color.BLACK);
			g.setFont(g.getFont().deriveFont(20f));
			g.drawString("Signal", -30, 5);
		});
		bind(() -> editor.loadTool(SignalTool.class, Signal.Type.ONE_WAY), (g, v) -> {
			g.setColor(Color.PINK);
			g.fillOval(-25, -25, 50, 50);
			g.setColor(Color.BLACK);
			g.setFont(g.getFont().deriveFont(20f));
			g.drawString("Signal", -30, 5);
		});

		onPress(KeyEvent.VK_MINUS, () -> select(0));
		onPress(KeyEvent.VK_1, () -> select(1));
		onPress(KeyEvent.VK_2, () -> select(2));
		onPress(KeyEvent.VK_3, () -> select(3));
		onPress(KeyEvent.VK_4, () -> select(4));
		onPress(KeyEvent.VK_5, () -> select(5));
		onPress(KeyEvent.VK_6, () -> select(6));
		onPress(KeyEvent.VK_7, () -> select(7));
		onPress(KeyEvent.VK_8, () -> select(8));
		onPress(KeyEvent.VK_9, () -> select(9));
		onPress(KeyEvent.VK_0, () -> select(10));

		onTick(this::updateByMouse);
		ifMet(() -> index >= 0).onPress(Input.MOUSE_LEFT, this::select);
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

	public void updateByMouse() {
		this.index = -1;
		Point p = mousePosition;
		if (p == null) {
			p = new Point(0, 0);
		}
		p = p.getLocation();
		p.x -= controller.getHorizontalScreenSize() / 2;
		p.x += 550;
		p.y -= controller.getVerticalScreenSize();
		p.y += 100;
		if (p.x < 0 || p.y < 0)
			return;
		int index = p.x / 100;
		if (index > 10)
			return;
		this.index = index;
	}

	@Override
	public void render(Graphics g, GameView view) {
		g.translate(view.getHorizontalScreenSize() / 2 - 500, view.getVerticalScreenSize() - 50);
		g.setStroke(new BasicStroke(4));
		for (int i = 0; i < 11; i++) {
			// box
			g.setStroke(this.bdst);
			g.setColor(i == index ? this.hover : this.bgcol);
			g.fillRect(-50, -50, 100, 100);
			g.setColor(this.bdcol);
			g.drawRect(-50, -50, 100, 100);
			if (bindings.size() > i)
				bindings.get(i)._2.renderSafe(g, view);
			g.translate(100, 0);
		}
	}

}
