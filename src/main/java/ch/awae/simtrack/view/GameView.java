package ch.awae.simtrack.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.model.Model;
import ch.awae.simtrack.view.renderer.Renderer;

/**
 * the game view implementation
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class GameView {

	private Model model;
	private ViewPort viewPort;
	private int screenX, screenY;
	private Runnable delegate;

	private Logger logger = LogManager.getLogger();

	private List<Renderer> renderers = new ArrayList<>();
	private Renderer editorRenderer = (r, v) -> {
		// void
	};

	/**
	 * instantiates a new game view
	 * 
	 * @param model
	 * @param screenX
	 * @param screenY
	 */
	GameView(Model model, int screenX, int screenY) {
		this.model = model;
		this.screenX = screenX;
		this.screenY = screenY;
		this.viewPort = new ViewPort(this);
	}

	/**
	 * sets the list of renderers to be used to render this view
	 * 
	 * @param renderers
	 */
	void setRenderers(List<Renderer> renderers) {
		this.renderers = renderers;
	}

	/**
	 * moves the view by the given amount
	 * 
	 * @param dx
	 * @param dy
	 */
	public void moveScene(int dx, int dy) {
		this.viewPort.moveScene(dx, dy);
	}

	/**
	 * zooms the view by the given amount at the given point. the given point
	 * remains stationary while zooming.
	 * 
	 * @param dzoom
	 * @param fixX
	 * @param fixY
	 */
	public void zoom(float dzoom, int fixX, int fixY) {
		this.viewPort.zoom((int) (100 * dzoom), fixX, fixY);
	}

	/**
	 * renders the view onto the graphics instance
	 * 
	 * @param graphics
	 *            the graphics instance to render onto
	 */
	void render(Graphics graphics) {
		Graphics.Stack stack = graphics.getStack();

		this.renderers.forEach(r -> {
			try {
				r.render(graphics, this);
			} catch (Exception ex) {
				logger.error("Rendering failed for " + r.getClass().getSimpleName(), ex);
			}
			graphics.setStack(stack);
		});

		this.editorRenderer.render(graphics, this);
		graphics.setStack(stack);
	}

	/**
	 * sets the rendering surface dimensions. All values are provided in pixels
	 * 
	 * @param width
	 * @param height
	 */
	public void setScreenDimensions(int width, int height) {
		this.screenX = width;
		this.screenY = height;
		this.viewPort.init();
	}

	public void renderView() {
		this.delegate.run();
	}

	/**
	 * sets the delegate responsible for enforcing the rendering
	 * 
	 * @param delegate
	 */
	void setRenderingDelegate(Runnable delegate) {
		this.delegate = delegate;
	}

	public void setEditorRenderer(Renderer renderer) {
		this.editorRenderer = renderer;
	}

	public Model getModel() {
		return this.model;
	}

	public int getHorizontalScreenSize() {
		return this.screenX;
	}

	public int getVerticalScreenSize() {
		return this.screenY;
	}

	public ViewPort getViewPort() {
		return this.viewPort;
	}

	public void setModel(Model model) {
		this.model = model;
	}

}
