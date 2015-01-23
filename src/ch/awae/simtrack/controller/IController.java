package ch.awae.simtrack.controller;

import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.model.IModel;
import ch.awae.simtrack.view.IView;

public interface IController {

	public IModel getModel();

	public IView getView();

	public void setFPS(int fps);

	public void setTPS(int tps);

	public void start();

	public void stop();
	
	public void startView();
	public void stopView();

	public Mouse getMouse();

	public Keyboard getKeyboard();
}
