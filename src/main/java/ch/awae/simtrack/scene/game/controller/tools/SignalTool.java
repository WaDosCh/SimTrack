package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Stroke;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.entity.Signal;
import ch.awae.simtrack.scene.game.model.entity.Signal.Type;
import ch.awae.simtrack.scene.game.model.position.Edge;
import ch.awae.simtrack.scene.game.model.position.SceneCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
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
	private InputController input;

	public SignalTool(Editor editor, Model model, InputController input) {
		super(editor, true);
		this.model = model;
		this.input = input;
	}
	
	@Override
	public void tick() {
		super.tick();
		updatePosition();
		if (this.bulldoze)
			checkDelete();
		else
			checkPlace();
	}
	
	@Override
	public void handleInput(InputEvent event) {
		if (event.isPressActionAndConsume(InputAction.ST_BUILD_SIGNAL)) {
			if (this.bulldoze)
				deleteSignal();
			else
				buildSignal();
			return;
		}
		if (event.isPressActionAndConsume(InputAction.ST_DELETE_SIGNAL)) {
			deleteSignal();
			return;
		}
		super.handleInput(event);
	}

	private void updatePosition() {
		Point mousePos = this.input.getMousePosition();
		SceneCoordinate mouseScene = this.getMouseSceneCoordinate(mousePos);
		TileCoordinate mouseTile = mouseScene.toTileCoordinate();
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
