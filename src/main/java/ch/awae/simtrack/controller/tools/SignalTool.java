package ch.awae.simtrack.controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.OnLoad;
import ch.awae.simtrack.controller.SimpleEventDrivenTool;
import ch.awae.simtrack.controller.input.Action;
import ch.awae.simtrack.model.entity.Signal;
import ch.awae.simtrack.model.entity.Signal.Type;
import ch.awae.simtrack.model.position.Edge;
import ch.awae.simtrack.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.view.Graphics;
import ch.judos.generic.data.geometry.PointD;

public class SignalTool extends SimpleEventDrivenTool {

	private final static Stroke borderStroke = new BasicStroke(6);

	private boolean oneway;
	private TileEdgeCoordinate position;
	private PointD center, mouse;

	public SignalTool(Editor editor) {
		super(editor, UnloadAction.UNLOAD);

		onTick(this::updatePosition);
		onPress(Action.ST_BUILD_SIGNAL, this::buildSignal);
		onPress(Action.ST_DELETE_SIGNAL, this::deleteSignal);
	}

	private void updatePosition() {
		center = mouseTile.toSceneCoordinate().getPointD();
		mouse = mouseScene.getPointD();
		double angle = center.getAngleTo(mouse);
		if (angle < 0)
			angle += 2 * Math.PI;

		int sector = (int) (6 * angle / Math.PI);
		Edge edge = Edge.byIndex(((sector + 1) / 2) % 6);
		position = mouseTile.getEdge(edge);
	}

	private void buildSignal() {
		model.setSignalAt(position, new Signal(position, oneway ? Type.ONE_WAY : Type.TWO_WAY));
	}

	private void deleteSignal() {
		model.removeSignalAt(position);
	}

	@OnLoad
	public void load(boolean oneway) {
		this.oneway = oneway;
	}

	@Override
	public void render(Graphics g) {
		viewPort.focusHex(position.getTile(), g);
		g.setStroke(borderStroke);
		g.setColor(Color.CYAN);
		double angle = position.getEdge().getAngleOut();
		g.rotate(angle);
		g.fillOval(41, 8, 18, 18);
		g.drawLine(-40, 0, 40, 0);
		g.drawLine(30, -10, 40, 0);
		g.drawLine(30, 10, 40, 0);
	}

}
