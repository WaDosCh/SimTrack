package ch.awae.simtrack.gui;

import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.function.Consumer;

import ch.awae.simtrack.controller.IGUIControllerHookup;
import ch.awae.simtrack.view.IGUIHookProvider;

public class GUI implements IGUIControllerHookup, IGUIHookProvider {

	private Window window;
	private Surface surface;

	public GUI(int x, int y) {
		this.window = new Window(x, y);
		this.surface = this.window.getSurface();
	}

	@Override
	public Runnable getRenderDelegate() {
		return this.window::repaint;
	}

	@Override
	public void hookComponentRenderer(Consumer<Graphics2D> consumer) {
		this.surface.setRenderingHook(consumer);
	}

	@Override
	public int getScreenWidth() {
		return this.surface.getWidth();
	}

	@Override
	public int getScreenHeight() {
		return this.surface.getHeight();
	}

	@Override
	public Consumer<MouseAdapter> getMouseHookup() {
		return (adap) -> {
			this.surface.addMouseListener(adap);
			this.surface.addMouseMotionListener(adap);
			this.surface.addMouseWheelListener(adap);
		};
	}

	@Override
	public Consumer<KeyAdapter> getKeyboardHookup() {
		return this.window::addKeyListener;
	}

}
