package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.entity.Signal;
import ch.awae.simtrack.scene.game.model.entity.Signal.Type;
import ch.awae.simtrack.scene.game.model.position.Edge;
import ch.awae.simtrack.scene.game.model.position.TileEdgeCoordinate;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.PointD;

public class SignalTool extends GameTool {

	private final static Stroke borderStroke = new BasicStroke(6);

	private boolean bulldoze;
	private Type type;
	private boolean valid;
	private TileEdgeCoordinate position;
	private PointD center, mouse;
	private Model model;

	public SignalTool(Editor editor, Model model) {
		super(editor, true);
		this.model = model;

		onTick(this::updatePosition);
		ifNot(() -> bulldoze).onTick(this::checkPlace);
		ifMet(() -> bulldoze).onTick(this::checkDelete);

		ifNot(() -> bulldoze).onPress(InputAction.ST_BUILD_SIGNAL, this::buildSignal);
		ifMet(() -> bulldoze).onPress(InputAction.ST_BUILD_SIGNAL, this::deleteSignal);
		onPress(InputAction.ST_DELETE_SIGNAL, this::deleteSignal);
	}

	private void updatePosition() {
		center = mouseTile.toSceneCoordinate().getPointD();
		mouse = mouseScene.getPointD();
		Angle angle = center.getAAngleTo(mouse);
		int sector = (int) angle.getDegree() / 60;
		Edge edge = Edge.byIndex(sector);
		position = mouseTile.getEdge(edge);
	}

	private void checkPlace() {
		valid = model.canPlaceSignal(position, type);
	}

	private void checkDelete() {
		valid = model.canRemoveSignalAt(position);
	}

	private void buildSignal() {
		if (valid)
			model.setSignalAt(position, new Signal(position, type));
	}

	private void deleteSignal() {
		if (valid)
			model.removeSignalAt(position);
	}

	@Override
	public void loadTool(Object... args) {
		if (args.length == 1 && args[0].getClass() == Type.class) {
			this.bulldoze = false;
			this.type = (Type) args[0];
		} else if (args.length == 0) {
			this.bulldoze = true;
		} else {
			throw new RuntimeException("Invalid args to load tool");
		}
	}

	@Override
	public void render(Graphics g) {
		this.scene.getViewPort().focusHex(position.getTile(), g);
		g.setStroke(borderStroke);
		double angle = position.getEdge().getAngleOut();
		g.rotate(angle);

		if (bulldoze) {
			g.setColor(Color.RED);
			g.drawOval(33, 12, 16, 16);
		} else {
			g.setColor(valid ? Color.DARK_GRAY : Color.RED);
			if (type == Type.ONE_WAY)
				g.fillRect(35, 14, 12, 12);
			else
				g.fillOval(35, 14, 12, 12);
			g.drawLine(-40, 0, 40, 0);
			g.drawLine(30, -10, 40, 0);
			g.drawLine(30, 10, 40, 0);
		}
	}

}
