package ch.awae.simtrack.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.wados.util.Shutdown;

/**
 * macOS does some weird stuff with non-standard keyboard behaviour: By default
 * on macOS holding a key does not always repeat it, but can produce a popup
 * menu with special characters. While this does not appear in java applications
 * it still screw up AWT keyboard events such that this key is then basically
 * stuck in the pressed state. There is no reliable way to "unstuck" a key or to
 * avoid this issue altogether.
 * 
 * This Hack modifies system configurations before AWT is initialised disabling
 * the default keyboard behaviour. During application shutdown the hack is then
 * again reverted.
 * 
 * This is currently the only way to get things to work.
 * 
 * @author Andreas Wälchli
 * @see https://stackoverflow.com/questions/41393525
 */
public class MacKeyboardHack {

	private static Logger logger = LogManager.getLogger();
	private static Shutdown shutdown = Shutdown.getDefaultShutdown();

	public static void applyHack() {

		logger.info("Applying Keyboard Hack for Mac OS X ...");

		try {
			try {
				Process exec = Runtime.getRuntime().exec("defaults read NSGlobalDomain ApplePressAndHoldEnabled");
				BufferedReader r = new BufferedReader(new InputStreamReader(exec.getInputStream()));
				boolean needsHack = r.readLine().equals("1");

				if (needsHack)
					logger.info("Keyboard Hack is required");
				else {
					logger.info("Keyboard Hack is not required");
					return;
				}
			} catch (NullPointerException npe) {
				logger.warn("Keyboard Hack is not applicable. Ignore if not running macOS 10.12");
				return;
			}

			logger.info("Hacking...");
			Runtime.getRuntime().exec("defaults write NSGlobalDomain ApplePressAndHoldEnabled -bool false").waitFor();
			shutdown.add(() -> {
				try {
					logger.info("Reverting Keyboard Hack...");
					Runtime.getRuntime().exec("defaults write NSGlobalDomain ApplePressAndHoldEnabled -bool true")
							.waitFor();
				} catch (
						IOException
						| InterruptedException e) {
					e.printStackTrace();
				}
			});
			logger.info("Hack completed");

		} catch (
				IOException
				| InterruptedException e) {
			e.printStackTrace();
		}
	}

}
