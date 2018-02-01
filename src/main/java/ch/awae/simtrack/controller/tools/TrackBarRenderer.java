package ch.awae.simtrack.controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

import ch.awae.simtrack.model.tile.ITrackTile;
import ch.awae.simtrack.model.track.TrackProvider;
import ch.awae.simtrack.view.IGameView;
import ch.awae.simtrack.view.renderer.IRenderer;
import ch.awae.simtrack.view.renderer.TrackRenderUtil;

/**
 * Track tool-bar renderer
 * 
 * @author Andreas Wälchli
 * @version 1.4, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class TrackBarRenderer implements IRenderer {

	private TrackBar bar;
	private Color bdcol = new Color(0.0f, 0.0f, 0.0f);
	private Stroke bdst = new BasicStroke(2);
	private Color bgcol = new Color(1.0f, 1.0f, 1.0f);
	private Color hover = new Color(0.8f, 0.8f, 0.8f);
	private Color rails = new Color(0.2f, 0.2f, 0.2f);
	private Color rbeds = new Color(0.6f, 0.6f, 0.6f);
	private Stroke xstr = new BasicStroke(6);

	private ArrayList<ITrackTile> tiles;

	/**
	 * creates a new renderer instance
	 * 
	 * @param bar
	 *            the track-bar rendered by this renderer
	 */
	TrackBarRenderer(TrackBar bar) {
		this.bar = bar;
		this.tiles = new ArrayList<>();
		for (int i = 0; i < TrackProvider.getTileCount(); i++) {
			this.tiles.add(TrackProvider.getTileInstance(i));
		}
	}

	@Override
	public void render(Graphics2D g, IGameView view) {
		g.translate(view.getHorizontalScreenSize() / 2 - 500, view.getVerticalScreenSize() - 50);
		g.setStroke(new BasicStroke(4));
		for (int i = -1; i < this.tiles.size(); i++) {
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
				g.scale(0.8, 0.8);
				// rail
				TrackRenderUtil.renderRails(g, this.rbeds, this.rails, this.tiles.get(i).getRailPaths());

				g.scale(1.25, 1.25);
			}
			g.translate(100, 0);
		}
	}

}
