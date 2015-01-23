package ch.awae.simtrack.view;

import ch.awae.simtrack.model.IModel;

/**
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public interface IView {

	public void setModel(IModel model);

	public void moveScene(int dx, int dy);

	public void zoom(float dzoom, int fixX, int fixY);

	public void setScreenDimensions(int width, int height);

	public void renderView();

	public void setEditorRenderer(IRenderer renderer);

	public IModel getModel();

	public int getHorizontalScreenSize();

	public int getVerticalScreenSize();

	public IViewPort getViewPort();

}
