package ch.awae.simtrack.view.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import ch.awae.simtrack.Global;
import ch.awae.simtrack.controller.TrackBar;
import ch.awae.simtrack.model.TrackTile;
import ch.awae.simtrack.view.ARenderer;

public class TrackBarRenderer extends ARenderer {

	private TrackBar bar;
	private Color bgcol = new Color(1.0f, 1.0f, 1.0f);
	private Color bdcol = new Color(0.0f, 0.0f, 0.0f);
	private Color hover = new Color(0.8f, 0.8f, 0.8f);
	private Color rails = new Color(0.2f, 0.2f, 0.2f);
	private Color rbeds = new Color(0.6f, 0.6f, 0.6f);
	private Stroke bdst = new BasicStroke(2);
	private Stroke rlst = new BasicStroke(4);
	private Stroke xstr = new BasicStroke(6);

	public TrackBarRenderer(TrackBar bar) {
		this.bar = bar;
	}

	@Override
	public void render(Graphics2D g) {
		g.translate(Global.ScreenW / 2 - 500, Global.ScreenH - 50);
		g.setStroke(new BasicStroke(4));
		ArrayList<TrackTile> tracks = this.bar.getTracks();
		for (int i = -1; i < tracks.size(); i++) {
			// box
			g.setStroke(this.bdst);
			g.setColor(i + 1 == this.bar.getIndex() ? this.hover : this.bgcol);
			g.fillRect(-50, -50, 100, 100);
			g.setColor(this.bdcol);
			g.drawRect(-50, -50, 100, 100);
			if (i < 0) {
				g.setColor(Color.RED);
				g.setStroke(this.xstr);
				g.drawLine(-25, -25, 25, 25);
				g.drawLine(-25, 25, 25, -25);
			} else {
				// rail
				g.setColor(this.rbeds);
				g.setStroke(this.rlst);
				AffineTransform Tx = g.getTransform();
				tracks.get(i).renderBed(g);
				g.setTransform(Tx);
				g.setColor(this.rails);
				tracks.get(i).renderRail(g);
				g.setTransform(Tx);
			}
			g.translate(100, 0);
		}
	}

}
