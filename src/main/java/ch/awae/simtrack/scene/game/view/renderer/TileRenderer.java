package ch.awae.simtrack.scene.game.view.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Map.Entry;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.position.Edge;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.tile.Tile;
import ch.awae.simtrack.scene.game.model.tile.TileType;
import ch.awae.simtrack.scene.game.model.tile.track.BorderTrackTile;
import ch.awae.simtrack.scene.game.model.tile.track.TrackTile;
import ch.awae.simtrack.util.Properties;
import ch.awae.simtrack.util.Resource;

/**
 * Renderer for track rendering
 * 
 * @author Andreas Wälchli
 * @version 2.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public class TileRenderer implements Renderer {
	private static final Color bedColour;
	private static final Color bgColour;
	private static final Color waterColor;
	private static final Color railColour;
	private static final Stroke arrowStroke;

	static {
		Properties props = Resource.getConfigProperties("renderer.properties");

		bedColour = props.getColor("railbedColor");
		bgColour = props.getColor("grassColor");
		waterColor = props.getColor("waterColor");
		railColour = props.getColor("railColor");
		arrowStroke = new BasicStroke(props.getInt("arrowStroke"));
	}

	private final static int hexSideHalf = 1 + (int) (50 / Math.sqrt(3));
	private final static int[][] hexEdges = { { 0, -50, -50, 0, 50, 50 },
			{ 2 * hexSideHalf, hexSideHalf, -hexSideHalf, -2 * hexSideHalf, -hexSideHalf, hexSideHalf } };
	private Model model;
	private ViewPortNavigator viewPort;

	public TileRenderer(ViewPortNavigator viewPort, Model model) {
		this.viewPort = viewPort;
		this.model = model;
	}
	
	
	@Override
	public void render(Graphics g) {
		Graphics.GraphicsStack stack = g.getStack();
		for (Entry<TileCoordinate, Tile> pair : this.model.getTiles()) {
			g.setStack(stack);
			TileCoordinate pos = pair.getKey();
			Tile tile = pair.getValue();
			if (!this.viewPort.isVisible(pos))
				continue;
			this.viewPort.focusHex(pos, g);
			g.setColor(bgColour);
			g.fillPolygon(hexEdges[0], hexEdges[1], 6);

			// decide how to work
			TileType type = tile.getType();
			switch (type == null ? TileType.UNKNOWN : type) {
				case TRACK:
					renderTrack(g, (TrackTile) tile);
					break;
				case WATER:
					renderWater(g, model, pos);
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

	private void renderWater(Graphics g, Model model, TileCoordinate pos) {
		g.setColor(waterColor);
		g.fillOval(-hexSideHalf, -hexSideHalf, 2 * hexSideHalf, 2 * hexSideHalf);
		g.push();
		for (Edge edge : Edge.values()) {
			Tile tile = model.getTileAt(pos.getNeighbour(edge));
			if (tile != null && tile.getType() == TileType.WATER) {
				g.rotate(edge.getAngleOut());
				g.fillRect(0, -hexSideHalf, 55, 2 * hexSideHalf);
				g.peek();
			}
		}
		g.pop();
	}

	private void renderTrack(Graphics2D g2, TrackTile tile) {
		TrackRenderUtil.renderRails(g2, bedColour, railColour, tile.getPaths());
		if (tile instanceof BorderTrackTile) {
			BorderTrackTile dest = (BorderTrackTile) tile;
			g2.rotate(Math.PI / 3 * tile.getPaths()[0]._1.ordinal());
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
