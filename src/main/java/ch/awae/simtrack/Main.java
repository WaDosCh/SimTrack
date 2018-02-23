package ch.awae.simtrack;

import javax.swing.SwingUtilities;

import ch.awae.simtrack.core.Controller;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.ModelFactory;
import ch.awae.simtrack.util.MacKeyboardHack;
import ch.awae.simtrack.window.BasicWindow;

@SuppressWarnings("javadoc")
public class Main {

	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "True");
		System.setProperty("sun.java2d.accthreshold", "0");

		if (System.getProperty("os.name").equals("Mac OS X"))
			MacKeyboardHack.applyHack();

		SwingUtilities.invokeLater(Main::init);
	}

	private static void init() {

		BasicWindow window = new BasicWindow(1200, 800);
		Controller controller = new Controller(window);
		controller.start();

		Model model = ModelFactory.getDefaultModel();
		Game scene = new Game(controller, model);

		controller.loadScene(scene);
	}

}