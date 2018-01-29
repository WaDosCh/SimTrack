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

	public void setModel(IModel model);

}
