package ch.awae.simtrack;

import javax.swing.SwingUtilities;

import ch.awae.simtrack.controller.GameController;
import ch.awae.simtrack.scene.Window;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.ModelFactory;
import ch.awae.simtrack.scene.game.view.GameView;
import ch.awae.simtrack.util.MacKeyboardHack;
import ch.awae.simtrack.window.MainWindow;

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

		Window window = new MainWindow(1200, 800);
		Model model = ModelFactory.getModel(25, 13, 10);
		GameView scene = new GameView(model, window);

		new GameController(window, scene).start();
	}

	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "True");
		System.setProperty("sun.java2d.accthreshold", "0");

		if (System.getProperty("os.name").equals("Mac OS X"))
			MacKeyboardHack.applyHack();

		SwingUtilities.invokeLater(Main::init);
	}

}