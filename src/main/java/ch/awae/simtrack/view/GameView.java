package ch.awae.simtrack.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ch.awae.simtrack.model.IModel;
import ch.awae.simtrack.view.Graphics.GraphicsContext;
import ch.awae.simtrack.view.renderer.IRenderer;

/**
 * the game view implementation
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class GameView implements IGameView {

	private IModel model;
	private ViewPort viewPort;
	private int screenX, screenY;
	private Runnable delegate;

	private List<IRenderer> renderers = new ArrayList<>();
	private IRenderer editorRenderer = (r, v) -> {
		// void
	};

	/**
	 * instantiates a new game view
	 * 
	 * @param model
	 * @param screenX
	 * @param screenY
	 */
	GameView(IModel model, int screenX, int screenY) {
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
	void setRenderers(List<IRenderer> renderers) {
		this.renderers = renderers;
	}

	@Override
	public void moveScene(int dx, int dy) {
		this.viewPort.moveScene(dx, dy);
	}

	@Override
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
		Stack<GraphicsContext> stack = graphics.getStack();

		this.renderers.forEach(r -> {
			r.render(graphics, this);
			graphics.setStack(stack);
		});

		this.editorRenderer.render(graphics, this);
		graphics.setStack(stack);
	}

	@Override
	public void setScreenDimensions(int width, int height) {
		this.screenX = width;
		this.screenY = height;
		this.viewPort.init();
	}

	@Override
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

	@Override
	public void setEditorRenderer(IRenderer renderer) {
		this.editorRenderer = renderer;
	}

	@Override
	public IModel getModel() {
		return this.model;
	}

	@Override
	public int getHorizontalScreenSize() {
		return this.screenX;
	}

	@Override
	public int getVerticalScreenSize() {
		return this.screenY;
	}

	@Override
	public ViewPort getViewPort() {
		return this.viewPort;
	}

	@Override
	public void setModel(IModel model) {
		this.model = model;
	}

}
