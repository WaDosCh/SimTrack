package ch.awae.simtrack.core;

public interface Window {

	int getCanvasWidth();

	int getCanvasHeight();

	Buffer getBuffer();

	void setTitle(String title);

	Input getInput();

}
