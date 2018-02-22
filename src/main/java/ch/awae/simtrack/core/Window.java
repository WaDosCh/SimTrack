package ch.awae.simtrack.core;

import java.awt.Image;
import java.util.function.Consumer;

public interface Window {

	int getCanvasWidth();

	int getCanvasHeight();

	void setTitle(String title);

	Input getInput();
	
	void takeSnapshot(Consumer<Image> callback);

}
