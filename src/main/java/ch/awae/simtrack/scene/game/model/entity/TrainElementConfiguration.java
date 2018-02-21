package ch.awae.simtrack.scene.game.model.entity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;

import ch.awae.simtrack.util.Resource;
import ch.awae.simtrack.util.serial.CustomDeserialization;
import lombok.Getter;

public class TrainElementConfiguration implements CustomDeserialization {

	private static final long serialVersionUID = 1L;

	public static final TrainElementConfiguration locomotive1 = new TrainElementConfiguration("locomotive1", 20, 80,
			100);
	public static final TrainElementConfiguration wagon1 = new TrainElementConfiguration("wagon1", 15, 60, 74);
	public static final TrainElementConfiguration wagon2 = new TrainElementConfiguration("wagon2", 15, 65, 80);

	private @Getter int firstAxle;
	private @Getter int secondAxle;
	private transient @Getter BufferedImage image;
	private @Getter int length;
	private String imageName;

	public TrainElementConfiguration(String imageName, int firstAxle, int secondAxle, int length) {
		this.firstAxle = firstAxle;
		this.secondAxle = secondAxle;
		this.length = length;
		this.imageName = imageName;
		loadImage();
	}

	private void loadImage() {
		image = Resource.getImage(imageName + ".png");
	}

	@Override
	public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		loadImage();
	}

}
