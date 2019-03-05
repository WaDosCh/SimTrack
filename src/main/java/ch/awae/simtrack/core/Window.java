package ch.awae.simtrack.core;

import java.awt.Dimension;

public interface Window {

	//TODO: rename to getScreenSize() like in Game
	Dimension getCanvasSize();

	Input getInput();

}
