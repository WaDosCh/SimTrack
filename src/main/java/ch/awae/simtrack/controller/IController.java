package ch.awae.simtrack.controller;

import ch.awae.simtrack.controller.input.Input;
import ch.awae.simtrack.model.IModel;
import ch.awae.simtrack.view.IGameView;

/**
 * The basic controller interface
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-26
 * @since SimTrack 0.2.2
 */
public interface IController {

	/**
	 * retrieves the controller-associated model
	 * 
	 * @return the model
	 */
	public IModel getModel();

	/**
	 * retrieves the controller-associated view
	 * 
	 * @return the view
	 */
	public IGameView getGameView();

	/**
	 * sets the rendering frequency
	 * 
	 * @param fps
	 *            the new frequency given as frames-per-second
	 */
	public void setFPS(int fps);

	/**
	 * sets the update frequency
	 * 
	 * @param tps
	 *            the new tick frequency given as ticks-per-second
	 */
	public void setTPS(int tps);

	/**
	 * starts the tick loop
	 */
	public void start();

	/**
	 * stops the tick loop
	 */
	public void stop();

	/**
	 * starts the rendering loop
	 */
	public void startView();

	/**
	 * stops the rendering loop
	 */
	public void stopView();

	public Input getInput();

	public PathFinding getPathfinder();

	public void setWindowTitle(String string);

	public void loadModel(IModel model);

}
