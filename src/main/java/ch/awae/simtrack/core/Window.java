package ch.awae.simtrack.core;

import java.awt.Dimension;

public interface Window {

	Dimension getCanvasSize();

	void setTitle(String title);

	Input getInput();

}
