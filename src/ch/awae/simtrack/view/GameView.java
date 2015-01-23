package ch.awae.simtrack.view;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import ch.awae.simtrack.model.IModel;

class GameView implements IView {

	private IModel model;
	private ViewPort port;
	private int screenX, screenY;
	private Runnable delegate;

	private List<IRenderer> renderers = new ArrayList<>();
	private IRenderer editorRenderer = (r, v) -> {
		// void
	};

	public GameView(IModel model, int screenX, int screenY) {
		this.model = model;
		this.screenX = screenX;
		this.screenY = screenY;
		this.port = new ViewPort(this);
	}

	void setRenderers(List<IRenderer> renderers) {
		this.renderers = renderers;
	}

	@Override
	public void setModel(IModel model) {
		this.model = model;
		this.port.init();
	}

	@Override
	public void moveScene(int dx, int dy) {
		this.port.moveScene(dx, dy);
	}

	@Override
	public void zoom(float dzoom, int fixX, int fixY) {
		this.port.zoom((int) (100 * dzoom), fixX, fixY);
	}

	void render(Graphics2D graphics) {
		this.renderers.forEach(r -> r.render((Graphics2D) graphics.create(),
				this));
		this.editorRenderer.render((Graphics2D) graphics.create(), this);
	}

	@Override
	public void setScreenDimensions(int width, int height) {
		this.screenX = width;
		this.screenY = height;
		this.port.init();
	}

	@Override
	public void renderView() {
		this.delegate.run();
	}

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
	public IViewPort getViewPort() {
		return this.port;
	}

}
