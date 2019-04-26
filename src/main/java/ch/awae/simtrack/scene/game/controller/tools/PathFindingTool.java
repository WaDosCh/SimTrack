package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Stroke;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.controller.Editor;
import ch.awae.simtrack.scene.game.controller.PathFinding;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.model.position.Edge;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileEdgeCoordinate;

public class PathFindingTool extends GameTool {

	private final static Stroke borderStroke = new BasicStroke(6);
	private final static int hexSideHalf = (int) (50 / Math.sqrt(3));

	private Logger logger = LogManager.getLogger(getClass());
	private TileCoordinate start;
	private Edge startEdge;
	private TileCoordinate end;
	private Edge endEdge;
	private PathFinding pathFinder;
	private Stack<TileEdgeCoordinate> path;

	public PathFindingTool(Editor editor, ViewPortNavigator viewPort, PathFinding pathFinder) {
		super(editor, viewPort, true);

		this.pathFinder = pathFinder;
		this.startEdge = Edge.RIGHT;
		this.endEdge = Edge.RIGHT;
	}

	@Override
	public void handleInput(InputEvent event) {
		if (event.isPressActionAndConsume(InputAction.SELECT)) {
			TileCoordinate mouseTile = this.getMouseSceneCoordinate(event.getCurrentMousePosition()).toTileCoordinate();
			updateOrigin(mouseTile);
		} else if (event.isPressActionAndConsume(InputAction.SELECT2)) {
			TileCoordinate mouseTile = this.getMouseSceneCoordinate(event.getCurrentMousePosition()).toTileCoordinate();
			updateTarget(mouseTile);
		} else
			super.handleInput(event);
	}

	private void updateOrigin(TileCoordinate mouseTile) {
		this.start = mouseTile;
		this.startEdge = this.startEdge.getNeighbour(true);
		if (this.start != null) {
			logger.debug("Start:" + new TileEdgeCoordinate(this.start, this.startEdge));
		}
		updatePath();
	}

	private void updateTarget(TileCoordinate mouseTile) {
		this.end = mouseTile;
		this.endEdge = this.endEdge.getNeighbour(true);
		if (this.end != null)
			logger.debug("Ende: " + new TileEdgeCoordinate(this.end, this.endEdge));
		updatePath();
	}

	private void updatePath() {
		if (this.start != null && this.end != null) {
			this.path = this.pathFinder.findPath(new TileEdgeCoordinate(this.start, this.startEdge),
					new TileEdgeCoordinate(this.end, this.endEdge));
		}
	}

	@Override
	public void render(Graphics g) {
		if (this.path != null) {
			g.setColor(Color.red);
			g.setStroke(new BasicStroke(12));
			for (int i = 0; i < this.path.size() - 1; i++) {
				TileCoordinate tile1 = this.path.get(i).tile;
				TileCoordinate tile2 = this.path.get(i + 1).tile;
				Point p1 = this.viewPort.toScreenCoordinate(tile1.toSceneCoordinate());
				Point p2 = this.viewPort.toScreenCoordinate(tile2.toSceneCoordinate());

				g.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
		}

		if (start != null) {
			g.push();
			g.setStroke(borderStroke);
			this.viewPort.focusHex(start, g);
			g.setColor(Color.GREEN);
			double angle = Math.PI / 3;
			for (int i = 0; i < 6; i++) {
				g.drawLine(50, -hexSideHalf, 50, hexSideHalf);
				g.rotate(angle);
			}
			for (int i = 0; i < startEdge.ordinal(); i++) {
				g.rotate(angle);
			}
			g.fillOval(30, -20, 40, 40);
			g.pop();
		}

		if (end != null) {
			g.push();
			g.setStroke(borderStroke);
			this.viewPort.focusHex(end, g);
			g.setColor(Color.ORANGE);
			double angle = Math.PI / 3;
			for (int i = 0; i < 6; i++) {
				g.drawLine(50, -hexSideHalf, 50, hexSideHalf);
				g.rotate(angle);
			}
			for (int i = 0; i < endEdge.ordinal(); i++) {
				g.rotate(angle);
			}
			g.fillOval(30, -20, 40, 40);
			g.pop();
		}

	}

}
