package ch.awae.simtrack.controller.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashSet;

import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.controller.tools.DebugTools.Option;
import ch.awae.simtrack.model.position.SceneCoordinate;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.util.Resource;
import ch.awae.simtrack.view.Design;
import ch.awae.simtrack.view.IGameView;
import ch.awae.simtrack.view.IRenderer;
import ch.awae.simtrack.view.IViewPort;

public class DebugToolsRenderer implements IRenderer {

	private HashSet<Option> showing;
	private String[] inputGuideText;
	private Mouse mouse;
	private IGameView gameView;

	public DebugToolsRenderer(HashSet<Option> showing, Mouse mouse) {
		this.showing = showing;
		this.mouse = mouse;
		this.inputGuideText = Resource.getText("inputKeys.txt");
	}

	@Override
	public void render(Graphics2D g, IGameView view) {
		if (this.showing.contains(Option.InputGuide))
			renderUserGuide(g, view);
		if (this.showing.contains(Option.Coordinates))
			renderCoordinate(g, view);
	}

	private void renderCoordinate(Graphics2D g, IGameView view) {
		Point screenPos = this.mouse.getScreenPosition();
		TileCoordinate tilePos = this.mouse.getTileCoordinate();
		IViewPort viewPort = this.gameView.getViewPort();
		SceneCoordinate scenePos = viewPort.toSceneCoordinate(screenPos);

		g.setColor(Design.almostTransparent);
		int y = screenPos.y + 10;
		g.fillRect(screenPos.x + 10, y, 400, 100);
		g.setColor(Design.grayBorder);
		g.drawRect(screenPos.x + 10, y, 400, 100);
		g.setColor(Color.black);
		g.setFont(Design.text);
		y += Design.text.getSize();
		g.drawString(screenPositionToString(screenPos), screenPos.x + 20, y);
		y += Design.text.getSize();
		g.drawString(scenePos.toString(), screenPos.x + 20, y);
		y += Design.text.getSize();
		if (tilePos == null)
			g.drawString("Tile: null", screenPos.x + 20, y);
		else
			g.drawString(tilePos.toString(), screenPos.x + 20, y);
		y += Design.text.getSize();
		g.drawString(screenSizeToString(), screenPos.x + 20, y);
		y += Design.text.getSize();
		g.drawString(zoomToString(), screenPos.x + 20, y);
	}

	public String zoomToString() {
		return "Zoom: " + this.gameView.getViewPort().getZoom();
	}

	public String screenPositionToString(Point screenPos) {
		return "ScreenPos: [x=" + screenPos.x + ", y=" + screenPos.y + "]";
	}

	public String screenSizeToString() {
		Point pos = this.gameView.getViewPort().getScreenDimensions();
		return "ScreenSize: [w=" + pos.x + ", h=" + pos.y + "]";
	}

	private void renderUserGuide(Graphics2D g, IGameView view) {
		int w = view.getHorizontalScreenSize();
		int h = view.getVerticalScreenSize();
		g.setColor(new Color(255, 255, 255, 225));
		g.fillRect(50, 50, w - 100, h - 100 - Design.toolbarHeight);
		g.setColor(new Color(128, 128, 128, 255));
		g.drawRect(50, 50, w - 100, h - 100 - Design.toolbarHeight);

		int y = 80;
		g.setColor(Color.black);
		g.setFont(Design.text);
		for (String s : this.inputGuideText) {
			g.drawString(s, 60, y);
			y += Design.text.getSize();
		}
	}

}
