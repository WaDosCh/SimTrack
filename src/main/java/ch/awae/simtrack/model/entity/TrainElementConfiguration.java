package ch.awae.simtrack.model.entity;

import java.awt.image.BufferedImage;

import ch.awae.simtrack.util.Resource;
import lombok.Getter;

public class TrainElementConfiguration {

	public static final TrainElementConfiguration locomotive1 = new TrainElementConfiguration("locomotive1", 20, 80,
			100);
	public static final TrainElementConfiguration wagon1 = new TrainElementConfiguration("wagon1", 15, 60, 74);

	private @Getter int firstAxle;
	private @Getter int secondAxle;
	private @Getter BufferedImage image;
	private @Getter int length;

	public TrainElementConfiguration(String imageName, int firstAxle, int secondAxle, int length) {
		this.firstAxle = firstAxle;
		this.secondAxle = secondAxle;
		this.length = length;
		this.image = Resource.getImage(imageName + ".png");
	}
}
