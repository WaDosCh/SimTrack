package ch.awae.simtrack;

import javax.swing.SwingUtilities;

import ch.awae.simtrack.core.Controller;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.ModelFactory;
import ch.awae.simtrack.util.MacKeyboardHack;
import ch.awae.simtrack.window.BasicWindow;
import ch.awae.simtrack.window.NativeFullscreen;

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
		
		//Controller controller = new Controller(new NativeFullscreen());
		Controller controller = new Controller(new BasicWindow(1200, 800));
		
		controller.start();

		Model model = ModelFactory.getModel(23, 15, 15);
		Game scene = new Game(controller, model);

		controller.loadScene(scene);
	}

}