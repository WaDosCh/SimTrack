package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Stroke;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Input;
import ch.awae.simtrack.scene.game.controller.PathFinding;
import ch.awae.simtrack.scene.game.model.position.Edge;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.scene.game.view.ViewPort;

public class PathFindingTool extends GameTool {

	private Logger logger = LogManager.getLogger(getClass());
	private TileCoordinate start;
	private Edge startEdge;
	private TileCoordinate end;
	private Edge endEdge;
	private PathFinding pathFinder;
	private Stack<TileEdgeCoordinate> path;

	public PathFindingTool(Editor editor) {
		super(editor, true);

		this.pathFinder = scene.getPathfinder();
		this.startEdge = Edge.RIGHT;
		this.endEdge = Edge.RIGHT;

		onPress(Input.MOUSE_LEFT, this::updateOrigin);
		onPress(Input.MOUSE_RIGHT, this::updateTarget);
	}

	private void updateOrigin() {
		this.start = mouseTile;
		this.startEdge = this.startEdge.getNeighbour(true);
		if (this.start != null) {
			logger.debug("Start:" + new TileEdgeCoordinate(this.start, this.startEdge));
		}
		updatePath();
	}

	private void updateTarget() {
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

	private final static Stroke borderStroke = new BasicStroke(6);
	private final static int hexSideHalf = (int) (50 / Math.sqrt(3));

	@Override
	public void render(Graphics g) {
		ViewPort viewPort = this.scene.getViewPort();
		if (this.path != null) {
			g.setColor(Color.red);
			g.setStroke(new BasicStroke(12));
			for (int i = 0; i < this.path.size() - 1; i++) {
				TileCoordinate tile1 = this.path.get(i).tile;
				TileCoordinate tile2 = this.path.get(i + 1).tile;
				Point p1 = viewPort.toScreenCoordinate(tile1.toSceneCoordinate());
				Point p2 = viewPort.toScreenCoordinate(tile2.toSceneCoordinate());

				g.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
		}

		if (start != null) {
			g.push();
			g.setStroke(borderStroke);
			viewPort.focusHex(start, g);
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
			viewPort.focusHex(end, g);
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
