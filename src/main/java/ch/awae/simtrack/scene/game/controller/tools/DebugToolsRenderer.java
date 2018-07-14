package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Graphics.GraphicsStack;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.controller.tools.DebugTools.Option;
import ch.awae.simtrack.scene.game.model.entity.Train;
import ch.awae.simtrack.scene.game.model.position.SceneCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.view.Design;
import ch.awae.simtrack.scene.game.view.ViewPort;
import ch.awae.simtrack.scene.game.view.renderer.Renderer;
import ch.awae.simtrack.util.Resource;
import ch.awae.utils.functional.T2;

public class DebugToolsRenderer implements Renderer {

	private HashSet<ch.awae.simtrack.scene.game.controller.tools.DebugTools.Option> showing;
	private String[] inputGuideText;
	private Game gameView;
	private DebugTools tools;

	public DebugToolsRenderer(HashSet<Option> showing, DebugTools tools) {
		this.showing = showing;
		this.tools = tools;
		this.inputGuideText = Resource.getText("inputKeys.txt");
	}

	@Override
	public void render(Graphics g, Game view) {
		if (this.showing.contains(Option.InputGuide))
			renderUserGuide(g, view);
		if (this.showing.contains(Option.Coordinates))
			renderCoordinate(g, view);
		if (showing.contains(Option.Reservations))
			renderBlocked(g, view);
	}

	private void renderBlocked(Graphics g, Game view) {
		GraphicsStack stack = g.getStack();
		g.push();
		HashMap<TileCoordinate, T2<Train, Integer>> tileReservations = view.getModel().getTileReservations();
		for (Entry<TileCoordinate, T2<Train, Integer>> t : tileReservations.entrySet()) {
			view.getViewPort().focusHex(t.getKey(), g);
			g.setFont(g.getFont().deriveFont((float) 20.0));
			String txt = t.getValue()._1.getId() + "";
			for (int i = 1; i < t.getValue()._2; i++)
				txt += "+";
			int w = g.getFontMetrics().stringWidth(txt);
			int h = g.getFontMetrics().getAscent();
			g.setColor(Color.BLACK);
			g.fillRect((-w/2)-3, (-h/2)-3, w+6, h+6);
			g.setColor(Color.WHITE);
			g.drawString(txt, -(w/2), (h/2)-1);
			g.peek();
		}
		g.setStack(stack);
	}

	private void renderCoordinate(Graphics2D g, Game view) {
		Point screenPos = tools.getMousePosition();
		TileCoordinate tilePos = tools.getMouseTile();
		gameView = view;
		ViewPort viewPort = this.gameView.getViewPort();
		SceneCoordinate scenePos = viewPort.toSceneCoordinate(screenPos);

		if (screenPos.y > viewPort.getScreenDimensions().y - Design.toolbarHeight)
			screenPos.y -= 150;
		if (screenPos.x > viewPort.getScreenDimensions().x - 400)
			screenPos.x -= 400;
		g.setColor(Design.almostOpaque);
		int y = screenPos.y + 10;
		g.fillRect(screenPos.x + 10, y, 400, 130);
		g.setColor(Design.grayBorder);
		g.drawRect(screenPos.x + 10, y, 400, 130);
		g.setColor(Color.black);
		g.setFont(Design.textFont);
		y += Design.textFont.getSize();
		g.drawString(screenPositionToString(screenPos), screenPos.x + 20, y);
		y += Design.textFont.getSize();
		g.drawString(scenePos.toString(), screenPos.x + 20, y);
		y += Design.textFont.getSize();
		if (tilePos == null)
			g.drawString("Tile: null", screenPos.x + 20, y);
		else
			g.drawString(tilePos.toString(), screenPos.x + 20, y);
		y += Design.textFont.getSize();
		g.drawString(screenSizeToString(view), screenPos.x + 20, y);
		y += Design.textFont.getSize();
		g.drawString(zoomToString(view), screenPos.x + 20, y);
		y += Design.textFont.getSize();
		g.drawString("scroll: " + view.getViewPort().getSceneCorner(), screenPos.x + 20, y);
	}

	public String zoomToString(Game view) {
		DecimalFormat f = new DecimalFormat("#.00");
		return "Zoom: " + f.format(view.getViewPort().getZoom()) + " -> "
				+ f.format(view.getViewPort().getTargetZoom());
	}

	public String screenPositionToString(Point screenPos) {
		return "ScreenPos: [x=" + screenPos.x + ", y=" + screenPos.y + "]";
	}

	public String screenSizeToString(Game view) {
		Point pos = view.getViewPort().getScreenDimensions();
		return "ScreenSize: [w=" + pos.x + ", h=" + pos.y + "]";
	}

	private void renderUserGuide(Graphics2D g, Game view) {
		Dimension size = view.getScreenSize();
		g.setColor(new Color(255, 255, 255, 225));
		g.fillRect(50, 50, size.width - 100, size.height - 100 - Design.toolbarHeight);
		g.setColor(new Color(128, 128, 128, 255));
		g.drawRect(50, 50, size.width - 100, size.height - 100 - Design.toolbarHeight);

		int y = 80;
		g.setColor(Color.black);
		g.setFont(Design.textFont);
		for (String s : this.inputGuideText) {
			g.drawString(s, 60, y);
			y += Design.textFont.getSize();
		}
	}

}
