package ch.awae.simtrack.scene.game.model;

import java.awt.Point;
import java.io.Serializable;

import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.judos.generic.data.geometry.PointD;

public class ViewPortData implements Serializable {

	private static final long serialVersionUID = -5142493835945681531L;
	
	public double zoom = ViewPortNavigator.ZOOM_DEFAULT;
	public double targetZoom = ViewPortNavigator.ZOOM_DEFAULT;

	public PointD sceneCorner = new PointD(0, 0);
	public Point focusedPointForZoom;

}
