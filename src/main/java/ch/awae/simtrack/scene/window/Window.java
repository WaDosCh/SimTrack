package ch.awae.simtrack.scene.window;

import ch.awae.simtrack.controller.input.Input;

public interface Window {

	int getCanvasWidth();

	int getCanvasHeight();

	Buffer getBuffer();
	
	void setTitle(String title);
	
	Input getInput();

}
