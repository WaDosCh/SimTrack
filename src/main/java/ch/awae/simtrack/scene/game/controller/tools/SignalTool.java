package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.OnLoad;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.controller.Action;
import ch.awae.simtrack.scene.game.model.entity.Signal;
import ch.awae.simtrack.scene.game.model.entity.Signal.Type;
import ch.awae.simtrack.scene.game.model.position.Edge;
import ch.awae.simtrack.scene.game.model.position.TileEdgeCoordinate;
import ch.judos.generic.data.geometry.PointD;

public class SignalTool extends GameTool {

	private final static Stroke borderStroke = new BasicStroke(6);

	private boolean bulldoze;
	private Type type;
	private boolean valid;
	private TileEdgeCoordinate position;
	private PointD center, mouse;

	public SignalTool(Editor<Game> editor) {
		super(editor, GameTool.UnloadAction.UNLOAD);

		onTick(this::updatePosition);
		ifNot(() -> bulldoze).onTick(this::checkPlace);
		ifMet(() -> bulldoze).onTick(this::checkDelete);

		ifNot(() -> bulldoze).onPress(Action.ST_BUILD_SIGNAL, this::buildSignal);
		ifMet(() -> bulldoze).onPress(Action.ST_BUILD_SIGNAL, this::deleteSignal);
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

	@OnLoad
	public void loadBulldoze() {
		this.bulldoze = true;
	}

	@OnLoad
	public void load(Type type) {
		this.bulldoze = false;
		this.type = type;
	}

	@Override
	public void render(Graphics g, Game scene) {
		scene.getViewPort().focusHex(position.getTile(), g);
		g.setStroke(borderStroke);
		double angle = position.getEdge().getAngleOut();
		g.rotate(angle);

		if (bulldoze) {
			g.setColor(Color.RED);
			g.drawOval(40, 7, 20, 20);
		} else {
			g.setColor(type == Type.ONE_WAY ? Color.PINK : Color.MAGENTA);
			g.drawOval(41, 8, 18, 18);
			g.setColor(valid ? Color.BLACK : Color.RED);
			g.drawLine(-40, 0, 40, 0);
			g.drawLine(30, -10, 40, 0);
			g.drawLine(30, 10, 40, 0);
		}
	}

}