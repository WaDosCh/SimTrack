package ch.awae.simtrack.controller;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.vecmath.Vector2f;

import ch.awae.simtrack.Global;
import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.view.ARenderer;

public class Navigator implements ITool {

	@Override
	public void load(Object[] args) throws IllegalStateException {
	}

	@Override
	public void unload() {
		// no action required
	}

	private final static int BORDER = 20;
	private final static int MOVE_SPEED = 10;

	@Override
	public void tick() {
		Point mouse = Global.mouse.position();
		Keyboard k = Global.keyboard;
		if (mouse == null)
			return;
		int dx = 0, dy = 0;
		if (mouse.x < BORDER || k.keysOr(KeyEvent.VK_A, KeyEvent.VK_LEFT))
			dx = 1;
		if (mouse.y < BORDER || k.keysOr(KeyEvent.VK_W, KeyEvent.VK_UP))
			dy = 1;
		if (mouse.x > Global.ScreenW - BORDER
				|| k.keysOr(KeyEvent.VK_D, KeyEvent.VK_RIGHT))
			dx = -1;
		if (mouse.y > Global.ScreenH - BORDER
				|| k.keysOr(KeyEvent.VK_S, KeyEvent.VK_DOWN))
			dy = -1;
		dx *= MOVE_SPEED;
		dy *= MOVE_SPEED;
		Global.port.moveScene(dx, dy);

		double amount = Global.mouse.getScroll();
		Global.port.zoom((int) (amount * deltaZoom), mouse.x, mouse.y);
	}

	@Override
	public String getToolName() {
		return null;
	}

	@Override
	public ARenderer getRenderer() {
		return null;
	}

	private final static int deltaZoom = 1;

}
