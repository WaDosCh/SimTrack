package ch.awae.simtrack.scene.game.view.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.text.DecimalFormat;

import ch.awae.simtrack.core.BaseRenderer;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.position.SceneCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.view.Design;

public class CoordinateInformationRenderer implements BaseRenderer {

	private InputController input;
	private ViewPortNavigator viewPort;
	private Model model;

	public CoordinateInformationRenderer(InputController input, ViewPortNavigator viewPort, Model model) {
		this.input = input;
		this.viewPort = viewPort;
		this.model = model;
	}

	@Override
	public void render(Graphics g) {
		if (!this.model.getDebugOptions().getShowCoordinates().get())
			return;
		Point screenPos = this.input.getMousePosition().getLocation();
		SceneCoordinate scenePos = this.viewPort.toSceneCoordinate(screenPos);
		TileCoordinate tilePos = scenePos.toTileCoordinate();

		String screenPosStr = screenPositionToString(screenPos);
		if (screenPos.y > viewPort.getScreenSize().height - Design.toolbarHeight)
			screenPos.y -= 150;
		if (screenPos.x > viewPort.getScreenSize().width - 400)
			screenPos.x -= 400;
		g.setColor(Design.panelBackground);
		int y = screenPos.y + 10;
		g.fillRect(screenPos.x + 10, y, 400, 130);
		g.setColor(Design.grayBorder);
		g.drawRect(screenPos.x + 10, y, 400, 130);
		g.setColor(Color.black);
		g.setFont(Design.textFont);
		y += Design.textFont.getSize();
		g.drawString(screenPosStr, screenPos.x + 20, y);
		y += Design.textFont.getSize();
		g.drawString(scenePos.toString(), screenPos.x + 20, y);
		y += Design.textFont.getSize();
		if (tilePos == null)
			g.drawString("Tile: null", screenPos.x + 20, y);
		else
			g.drawString(tilePos.toString(), screenPos.x + 20, y);
		y += Design.textFont.getSize();
		g.drawString(screenSizeToString(), screenPos.x + 20, y);
		y += Design.textFont.getSize();
		g.drawString(zoomToString(), screenPos.x + 20, y);
		y += Design.textFont.getSize();
		g.drawString("scroll: " + this.viewPort.getSceneCorner(), screenPos.x + 20, y);
	}

	public String zoomToString() {
		DecimalFormat f = new DecimalFormat("#.00");
		return "Zoom: " + f.format(this.viewPort.getZoom()) + " -> " + f.format(this.viewPort.getTargetZoom());
	}

	public String screenPositionToString(Point screenPos) {
		return "ScreenPos: [x=" + screenPos.x + ", y=" + screenPos.y + "]";
	}

	public String screenSizeToString() {
		Dimension pos = this.viewPort.getScreenSize();
		return "ScreenSize: [w=" + pos.width + ", h=" + pos.height + "]";
	}

}
