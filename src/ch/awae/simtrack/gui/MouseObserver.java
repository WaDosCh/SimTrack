package ch.awae.simtrack.gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import ch.awae.simtrack.Global;
import ch.awae.simtrack.model.position.TileCoordinate;

public class MouseObserver implements MouseMotionListener {

	public Point mouse;
	public TileCoordinate mouseHex = new TileCoordinate(0, 0);

	public MouseObserver(Window w) {
		w.getContentPane().addMouseMotionListener(this);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		this.mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point nextP = e.getPoint();
		if (nextP != null)
			this.mouse = nextP;
		TileCoordinate tile = Global.port.getHexPos(Global.port
				.getSceneCoordinate(this.mouse));
		if (tile != null)
			this.mouseHex = tile;

	}

}
