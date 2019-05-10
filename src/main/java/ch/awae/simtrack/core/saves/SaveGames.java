package ch.awae.simtrack.core.saves;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.scene.game.model.Model;

public class SaveGames {

	private Logger logger = LogManager.getLogger();

	private File savePath = new File("saves/");

	public SaveGames() {
		if (!savePath.exists())
			savePath.mkdir();
	}

	public File[] getAvailableSaves() {
		return savePath.listFiles((folder, name) -> name.endsWith(".simtrack.save"));
	}

	public boolean saveWithName(String name, Model model) {
		model.lastSaveGameName = name;
		FileOutputStream outStream;
		try {
			outStream = new FileOutputStream(new File(savePath, name + ".simtrack.save"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		try (ObjectOutputStream out = new ObjectOutputStream(outStream)) {
			out.writeObject(model);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Model loadGame(File savedGame) throws Exception {
		logger.debug("LOAD GAME");
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(savedGame));
			return (Model) in.readObject();
		} catch (Exception e) {
			logger.error("error loading save", e);
			throw e;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}

}
