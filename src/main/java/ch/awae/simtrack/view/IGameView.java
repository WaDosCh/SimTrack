/*
 * SimTrack - Railway Planning and Simulation Game
 * Copyright (C) 2015 Andreas Wälchli
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.awae.simtrack.view;

import ch.awae.simtrack.model.IModel;

/**
 * The base interface for any view instance
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-26
 * @since SimTrack 0.2.1
 */
public interface IGameView {

	/**
	 * sets the model associated with the view
	 * 
	 * @param model
	 */
	public void setModel(IModel model);

	/**
	 * moves the view by the given amount
	 * 
	 * @param dx
	 * @param dy
	 */
	public void moveScene(int dx, int dy);

	/**
	 * zooms the view by the given amount at the given point. the given point
	 * remains stationary while zooming.
	 * 
	 * @param dzoom
	 * @param fixX
	 * @param fixY
	 */
	public void zoom(float dzoom, int fixX, int fixY);

	/**
	 * sets the rendering surface dimensions. All values are provided in pixels
	 * 
	 * @param width
	 * @param height
	 */
	public void setScreenDimensions(int width, int height);

	/**
	 * renders the views
	 */
	public void renderView();

	/**
	 * sets the renderer for the controller-internal rendering
	 * 
	 * @param renderer
	 */
	public void setEditorRenderer(IRenderer renderer);

	/**
	 * provides the model associated with the view
	 * 
	 * @return the view-associated model
	 */
	public IModel getModel();

	/**
	 * returns the pixel width of the drawing surface
	 * 
	 * @return the surface width
	 */
	public int getHorizontalScreenSize();

	/**
	 * returns the pixel height of the drawing surface
	 * 
	 * @return the surface height
	 */
	public int getVerticalScreenSize();

	/**
	 * provides the view viewport
	 * 
	 * @return the viewport
	 */
	public IViewPort getViewPort();

}
