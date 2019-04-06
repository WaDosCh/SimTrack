package ch.awae.simtrack;

import javax.swing.SwingUtilities;

import ch.awae.simtrack.core.Controller;
import ch.awae.simtrack.core.GameWindow;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.scene.menu.Menu;
import ch.awae.simtrack.util.MacKeyboardHack;
import ch.awae.simtrack.window.ResizableWindow;

public class Main {

	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "True");
		System.setProperty("sun.java2d.accthreshold", "0");

		if (System.getProperty("os.name").equals("Mac OS X"))
			MacKeyboardHack.applyHack();

		SwingUtilities.invokeLater(Main::init);
	}

	private static void init() {
		
		InputController input = new InputController();
//		GameWindow window = new NativeFullscreen(input);
		GameWindow window = new ResizableWindow(1200, 800, input);
		
		Controller controller = new Controller(window);
		controller.start();
		controller.loadScene(Menu.class);
		
	}

}