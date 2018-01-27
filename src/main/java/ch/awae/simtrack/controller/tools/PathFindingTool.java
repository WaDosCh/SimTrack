package ch.awae.simtrack.controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.util.Stack;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.ITool;
import ch.awae.simtrack.controller.Log;
import ch.awae.simtrack.controller.PathFinding;
import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.controller.input.Mouse.Button;
import ch.awae.simtrack.controller.input.Trigger;
import ch.awae.simtrack.controller.input.Trigger.Direction;
import ch.awae.simtrack.model.position.Edge;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.view.IGameView;
import ch.awae.simtrack.view.IRenderer;
import ch.awae.simtrack.view.IViewPort;

public class PathFindingTool implements ITool, IRenderer {

	private Editor editor;
	private Keyboard keyboard;
	private Mouse mouse;
	private TileCoordinate start;
	private Edge startEdge;
	private TileCoordinate end;
	private Edge endEdge;
	private PathFinding pathFinder;
	private Stack<TileEdgeCoordinate> path;
	private IViewPort viewPort;

	private Trigger ESC, M_LEFT, M_RIGHT;

	public PathFindingTool(Editor editor) {
		this.editor = editor;
		this.keyboard = editor.getController().getKeyboard();
		this.mouse = editor.getController().getMouse();
		this.pathFinder = this.editor.getController().getPathfinder();
		this.viewPort = this.editor.getController().getGameView().getViewPort();

		this.ESC = this.keyboard.trigger(Direction.ACTIVATE, KeyEvent.VK_ESCAPE);

		M_LEFT = mouse.trigger(Direction.ACTIVATE, Button.LEFT);
		M_RIGHT = mouse.trigger(Direction.DEACTIVATE, Button.RIGHT);

		this.startEdge = Edge.RIGHT;
		this.endEdge = Edge.RIGHT;
	}

	@Override
	public IRenderer getRenderer() {
		return this;
	}

	@Override
	public String getToolName() {
		return "PathFindingTool";
	}

	@Override
	public void load(Object[] args) throws IllegalStateException {
	}

	@Override
	public void tick() {
		this.ESC.test(() -> editor.loadTool("FreeHand", null));

		if (M_LEFT.test()) {
			this.start = this.mouse.getTileCoordinate();
			this.startEdge = this.startEdge.getNeighbour(true);
			Log.info("Start: ", new TileEdgeCoordinate(this.start, this.startEdge));
		}
		if (M_RIGHT.test()) {
			this.end = this.mouse.getTileCoordinate();
			this.endEdge = this.endEdge.getNeighbour(true);
			Log.info("Ende: ", new TileEdgeCoordinate(this.end, this.endEdge));
		}

		if (this.start != null && this.end != null) {
			this.path = this.pathFinder.findPath(new TileEdgeCoordinate(this.start, this.startEdge),
					new TileEdgeCoordinate(this.end, this.endEdge));
		}
	}

	private final static Stroke borderStroke = new BasicStroke(6);
	private final static int hexSideHalf = (int) (50 / Math.sqrt(3));

	@Override
	public void render(Graphics2D g, IGameView view) {
		if (this.path != null) {
			g.setColor(Color.red);
			g.setStroke(new BasicStroke(12));
			for (int i = 0; i < this.path.size() - 1; i++) {
				TileCoordinate tile1 = this.path.get(i).tile;
				TileCoordinate tile2 = this.path.get(i + 1).tile;
				Point p1 = this.viewPort.toScreenCoordinate(tile1);
				Point p2 = this.viewPort.toScreenCoordinate(tile2);

				g.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
		}

		if (start != null) {
			g.setStroke(borderStroke);
			Graphics2D g2 = view.getViewPort().focusHex(start, g);
			g2.setColor(Color.GREEN);
			double angle = Math.PI / 3;
			for (int i = 0; i < 6; i++) {
				g2.drawLine(50, -hexSideHalf, 50, hexSideHalf);
				g2.rotate(angle);
			}
			for (int i = 0; i < startEdge.ordinal(); i++) {
				g2.rotate(angle);
			}
			g2.fillOval(30, -20, 40, 40);
		}

		if (end != null) {
			g.setStroke(borderStroke);
			Graphics2D g2 = view.getViewPort().focusHex(end, g);
			g2.setColor(Color.ORANGE);
			double angle = Math.PI / 3;
			for (int i = 0; i < 6; i++) {
				g2.drawLine(50, -hexSideHalf, 50, hexSideHalf);
				g2.rotate(angle);
			}
			for (int i = 0; i < endEdge.ordinal(); i++) {
				g2.rotate(angle);
			}
			g2.fillOval(30, -20, 40, 40);
		}

	}

}
