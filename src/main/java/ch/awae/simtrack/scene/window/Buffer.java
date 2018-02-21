package ch.awae.simtrack.scene.window;

import ch.awae.simtrack.view.Graphics;

public interface Buffer {

	void swapBuffer();

	void clearBuffer();

	Graphics getGraphics();

}
