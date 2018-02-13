package ch.awae.simtrack;

import javax.swing.SwingUtilities;

import ch.awae.simtrack.controller.ControllerFactory;
import ch.awae.simtrack.controller.IController;
import ch.awae.simtrack.gui.GUI;
import ch.awae.simtrack.model.IModel;
import ch.awae.simtrack.model.ModelFactory;
import ch.awae.simtrack.view.IGameView;
import ch.awae.simtrack.view.ViewFactory;

/**
 * Main Class.
 * 
 * Will be replaced by a more elaborate high-logic system.
 * 
 * @author Andreas Wälchli
 * @version 1.3, 2015-01-23
 * @since SimTrack 0.2.1
 */
@SuppressWarnings("javadoc")
public class Main {

	private static void init() {
		GUI gui = new GUI(1200, 800);
		IModel model = ModelFactory.getModel(25, 13, 10);
		IGameView gameView = ViewFactory.createGameView(model, gui);
		IController c = ControllerFactory.buildGameController(model, gameView, gui, 50, 50);
		c.startView();
		c.start();
	}

	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "True");
		System.setProperty("sun.java2d.accthreshold", "0");

		SwingUtilities.invokeLater(Main::init);
	}

}