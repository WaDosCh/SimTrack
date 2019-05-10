package ch.awae.simtrack.core.ui;

import java.awt.Dimension;

import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.window.Graphics;

public class Spacer extends BaseComponent {

	private int preferedWidth;
	private int preferedHeight;

	public Spacer(int width, int height) {
		this.preferedWidth = width;
		this.preferedHeight = height;
	}

	@Override
	public Dimension getPreferedDimension() {
		return new Dimension(preferedWidth, preferedHeight);
	}

	@Override
	public void handleInput(InputEvent event) {
	}

	@Override
	public void render(Graphics graphics) {
	}

}
