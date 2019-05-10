package ch.awae.simtrack.scene.game.view.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.position.Edge;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.tile.EnvironmentTile;
import ch.awae.simtrack.scene.game.model.tile.EnvironmentTile.EnvironmentType;
import ch.awae.simtrack.scene.game.model.tile.Tile;
import ch.awae.simtrack.scene.game.model.tile.track.BorderTrackTile;
import ch.awae.simtrack.scene.game.model.tile.track.TrackTile;
import ch.awae.simtrack.util.Properties;
import ch.awae.simtrack.util.Resource;
import ch.awae.simtrack.window.Graphics;

public class TileRenderer implements Renderer {
	private static final Color bedColor;
	private static final Color waterColor;
	private static final Color railColor;
	private static final Stroke arrowStroke;
	private static BufferedImage tileGrass;
	private static BufferedImage tileWater;

	static {
		Properties props = Resource.getConfigProperties("renderer.properties");

		bedColor = props.getColor("railbedColor");
		waterColor = props.getColor("waterColor");
		railColor = props.getColor("railColor");
		arrowStroke = new BasicStroke(props.getInt("arrowStroke"));

		reloadGraphics();
	}

	public static void reloadGraphics() {
		tileGrass = Resource.getImage("tile-grass.png");
		tileWater = Resource.getImage("tile-water.png");
	}

	private Model model;
	private ViewPortNavigator viewPort;
	private TileCoordinate currentRenderPosition;

	public TileRenderer(ViewPortNavigator viewPort, Model model) {
		this.viewPort = viewPort;
		this.model = model;
	}

	@Override
	public void render(Graphics g) {
		Graphics.GraphicsStack stack = g.getStack();

		TileCoordinate topLeftTile = this.viewPort.toSceneCoordinate(new Point(0, 0)).toTileCoordinate();
		TileCoordinate topRightTile = this.viewPort.toSceneCoordinate(new Point(this.viewPort.getScreenSize().width, 0))
				.toTileCoordinate();
		TileCoordinate bottomTile = this.viewPort.toSceneCoordinate(new Point(0, this.viewPort.getScreenSize().height))
				.toTileCoordinate();

		int tileU = topLeftTile.u;
		int tileV = topLeftTile.v - 1;
		int tileULeft = 1;

		while (true) {
			renderTile(g, new TileCoordinate(tileU, tileV));

			tileU++;
			if (tileU == topRightTile.u + 1) {
				tileULeft++;
				tileU = topLeftTile.u - tileULeft / 2;
				tileV++;
				if (tileV == bottomTile.v + 2) {
					break;
				}
			}

			g.setStack(stack);
		}
		g.setStack(stack);
	}

	private void renderTile(Graphics g, TileCoordinate tileCoord) {
		this.currentRenderPosition = tileCoord;
		if (!this.viewPort.isVisible(this.currentRenderPosition))
			return;
		Tile tile = this.model.getTileAt(tileCoord);
		this.viewPort.focusHex(this.currentRenderPosition, g);
		if (tile == null || !(tile instanceof EnvironmentTile)) {
			renderGrass(g);
		}
		if (tile != null) {
			tile.render(this, g);
		}
	}

	public void renderTileBorder(Graphics g, int width) {
		g.setStroke(new BasicStroke(width));
		g.drawHex();
	}

	private void renderUnknown(Graphics g) {
		g.setColor(Color.RED.darker());
		g.fillHex();
		g.setColor(Color.BLACK);
		g.scale(5, 5);
		g.drawString("?", -2, 5);
	}

	public void renderWater(Graphics g) {
		if (this.model.getDebugOptions().getRenderSoftware().get()) {
			renderWaterSoft(g);
			return;
		}
		g.scale(0.5, 0.5);
		g.drawImage(tileWater, -100, -116, null);
	}

	private void renderGrass(Graphics g) {
		if (this.model.getDebugOptions().getRenderSoftware().get()) {
			return;
		}
		g.push();
		g.scale(0.5, 0.5);
		g.drawImage(tileGrass, -100, -116, null);
		g.pop();
	}

	public void renderWaterSoft(Graphics g) {
		g.setColor(waterColor);
		final int hexSideHalf = TileCoordinate.TILE_SIDE_HEIGHT_HALF;
		g.fillOval(-hexSideHalf, -hexSideHalf, 2 * hexSideHalf, 2 * hexSideHalf);
		g.push();
		for (Edge edge : Edge.values()) {
			Tile tile = this.model.getTileAt(this.currentRenderPosition.getNeighbour(edge));
			if (tile != null && (tile instanceof EnvironmentTile)) {
				EnvironmentTile etile = (EnvironmentTile) tile;
				if (etile.getType() == EnvironmentType.WATER) {
					g.rotate(edge.getAngleOut());
					g.fillRect(0, -hexSideHalf, 55, 2 * hexSideHalf);
					g.peek();
				}
			}
		}
		g.pop();
	}

	public void renderTrack(Graphics g, TrackTile tile) {
		renderTrack(g, tile, bedColor, railColor);
	}

	public void renderTrack(Graphics g, TrackTile tile, Color bedColor, Color railColor) {
		TrackRenderUtil.renderRails(g, bedColor, railColor, tile.getPaths());
		if (tile instanceof BorderTrackTile) {
			BorderTrackTile dest = (BorderTrackTile) tile;
			g.rotate(Math.PI / 3 * tile.getPaths()[0].edge1.ordinal());
			g.setColor(railColor);
			g.setStroke(arrowStroke);
			g.translate(30, 0);
			if (dest.isTrainDestination())
				g.scale(-1, 1);
			g.drawLine(0, 0, -10, -10);
			g.drawLine(0, 0, -10, 10);
			g.drawLine(0, -10, 10, 0);
			g.drawLine(0, 10, 10, 0);
		}
	}
}
