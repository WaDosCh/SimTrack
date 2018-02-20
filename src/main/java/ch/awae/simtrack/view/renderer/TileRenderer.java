package ch.awae.simtrack.view.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Map.Entry;

import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.tile.IDestinationTrackTile;
import ch.awae.simtrack.model.tile.ITile;
import ch.awae.simtrack.model.tile.ITrackTile;
import ch.awae.simtrack.model.tile.TileType;
import ch.awae.simtrack.util.Properties;
import ch.awae.simtrack.util.Resource;
import ch.awae.simtrack.view.Graphics;
import ch.awae.simtrack.view.IGameView;
import ch.awae.simtrack.view.ViewPort;

/**
 * Renderer for track rendering
 * 
 * @author Andreas Wälchli
 * @version 2.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public class TileRenderer implements IRenderer {
	private static final Color bedColour;
	private static final Color bgColour;
	private static final Color waterColor;
	private static final Color railColour;
	private static final Stroke arrowStroke;

	static {
		Properties props = Resource.getProperties("renderer.properties");
		
		bedColour = props.getColor("railbedColor");
		bgColour = props.getColor("grassColor");
		waterColor = props.getColor("waterColor");
		railColour = props.getColor("railColor");
		arrowStroke = new BasicStroke(props.getInt("arrowStroke"));
	}


	private final static int hexSideHalf = 1 + (int) (50 / Math.sqrt(3));
	private final static int[][] hexEdges = { { 0, -50, -50, 0, 50, 50 },
			{ 2 * hexSideHalf, hexSideHalf, -hexSideHalf, -2 * hexSideHalf, -hexSideHalf, hexSideHalf } };
	
	@Override
	public void render(Graphics g, IGameView view) {
		ViewPort port = view.getViewPort();
		Graphics.Stack stack = g.getStack();
		for (Entry<TileCoordinate, ITile> pair : view.getModel().getTiles()) {
			g.setStack(stack);
			TileCoordinate pos = pair.getKey();
			ITile tile = pair.getValue();
			if (!port.isVisible(pos))
				continue;
			port.focusHex(pos, g);
			g.setColor(bgColour);
			g.fillPolygon(hexEdges[0], hexEdges[1], 6);

			// decide how to work
			TileType type = tile.getType();
			switch (type == null ? TileType.UNKNOWN : type) {
				case TRACK:
					renderTrack(g, (ITrackTile) tile);
					break;
				case OBSTACLE:
					renderObstacle(g);
					break;
				// unknown tile
				default:
					renderUnknown(g);
			}

		}
		g.setStack(stack);
	}

	private void renderUnknown(Graphics2D g2) {
		g2.setColor(Color.RED.darker());
		g2.fillPolygon(hexEdges[0], hexEdges[1], 6);
		g2.setColor(Color.BLACK);
		g2.scale(5, 5);
		g2.drawString("?", -2, 5);
	}

	private void renderObstacle(Graphics2D g2) {
		g2.setColor(waterColor);
		g2.fillOval(10 - 20, 10 - 20, 40, 40);
		g2.fillOval(-10 - 20, 10 - 20, 40, 40);
		g2.fillOval(0 - 20, -10 - 20, 40, 40);
	}

	private void renderTrack(Graphics2D g2, ITrackTile tile) {
		TrackRenderUtil.renderRails(g2, bedColour, railColour, tile.getRailPaths());
		if (tile instanceof IDestinationTrackTile) {
			IDestinationTrackTile dest = (IDestinationTrackTile) tile;
			g2.rotate(Math.PI / 3 * tile.getRailPaths()[0]._1.ordinal());
			g2.setColor(railColour);
			g2.setStroke(arrowStroke);
			g2.translate(30, 0);
			if (dest.isTrainDestination())
				g2.scale(-1, 1);
			g2.drawLine(0, 0, -10, -10);
			g2.drawLine(0, 0, -10, 10);
			g2.drawLine(0, -10, 10, 0);
			g2.drawLine(0, 10, 10, 0);
		}
	}
}
