package ch.awae.simtrack;

import javax.swing.SwingUtilities;

import ch.awae.simtrack.core.Controller;
import ch.awae.simtrack.core.GameWindow;
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
		
//		GameWindow window = new NativeFullscreen();
		GameWindow window = new ResizableWindow(1200, 800);
		
		Controller controller = new Controller(window);
		controller.start();
		Menu scene = new Menu(controller, window);
		
		controller.loadScene(scene);
	}

}