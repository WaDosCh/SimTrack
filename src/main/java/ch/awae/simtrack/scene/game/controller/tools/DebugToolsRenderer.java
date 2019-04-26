package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Graphics.GraphicsStack;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.controller.tools.DebugTools.Option;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.entity.Train;
import ch.awae.simtrack.scene.game.model.position.SceneCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.view.Design;
import ch.awae.simtrack.scene.game.view.renderer.Renderer;
import ch.awae.simtrack.util.Resource;
import ch.awae.utils.functional.T2;

public class DebugToolsRenderer implements Renderer {

	private HashMap<Option, AtomicBoolean> showing;
	private String[] inputGuideText;
	private DebugTools tools;
	private ViewPortNavigator viewPort;
	private Model model;
	private Window window;
	private InputController input;

	public DebugToolsRenderer(HashMap<Option, AtomicBoolean> showing, DebugTools tools, ViewPortNavigator viewPort,
			Window window, InputController input) {
		this.showing = showing;
		this.tools = tools;
		this.input = input;
		this.inputGuideText = Resource.getText("inputKeys.txt");
		this.model = tools.model;
		this.viewPort = viewPort;
		this.window = window;
	}

	@Override
	public void render(Graphics g) {
		if (this.showing.get(Option.InputGuide).get())
			renderUserGuide(g);
		if (this.showing.get(Option.Coordinates).get())
			renderCoordinate(g);
		if (showing.get(Option.Reservations).get())
			renderBlocked(g);
	}

	private void renderBlocked(Graphics g) {
		GraphicsStack stack = g.getStack();
		g.push();
		HashMap<TileCoordinate, T2<Train, Integer>> tileReservations = this.model.getTileReservations();
		for (Entry<TileCoordinate, T2<Train, Integer>> t : tileReservations.entrySet()) {
			this.viewPort.focusHex(t.getKey(), g);
			g.setFont(g.getFont().deriveFont((float) 20.0));
			String txt = t.getValue()._1.getId() + "";
			for (int i = 1; i < t.getValue()._2; i++)
				txt += "+";
			int w = g.getFontMetrics().stringWidth(txt);
			int h = g.getFontMetrics().getAscent();
			g.setColor(Color.BLACK);
			g.fillRect((-w / 2) - 3, (-h / 2) - 3, w + 6, h + 6);
			g.setColor(Color.WHITE);
			g.drawString(txt, -(w / 2), (h / 2) - 1);
			g.peek();
		}
		g.setStack(stack);
	}

	private void renderCoordinate(Graphics2D g) {
		
		Point screenPos = this.input.getMousePosition();
		SceneCoordinate scenePos = tools.getMouseSceneCoordinate(screenPos);
		TileCoordinate tilePos = scenePos.toTileCoordinate();

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
		g.drawString(screenPositionToString(screenPos), screenPos.x + 20, y);
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

	private void renderUserGuide(Graphics2D g) {
		Dimension size = this.window.getScreenSize();
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
