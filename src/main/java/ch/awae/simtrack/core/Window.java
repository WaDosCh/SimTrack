package ch.awae.simtrack.core;

import java.awt.Dimension;

import ch.awae.simtrack.core.input.InputController;

public interface Window {

	Dimension getScreenSize();

	InputController getInput();

}
