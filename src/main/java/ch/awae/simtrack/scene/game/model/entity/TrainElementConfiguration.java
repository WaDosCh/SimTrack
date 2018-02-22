package ch.awae.simtrack.scene.game.model.entity;

import java.awt.image.BufferedImage;

import ch.awae.simtrack.util.Resource;
import lombok.Getter;

public enum TrainElementConfiguration {

	locomotive1("locomotive1", 20, 80,100),
	wagon1("wagon1", 15, 60, 74),
	wagon2("wagon2", 15, 65, 80);

	private @Getter int firstAxle;
	private @Getter int secondAxle;
	private transient @Getter BufferedImage image;
	private @Getter int length;
	private String imageName;

	TrainElementConfiguration(String imageName, int firstAxle, int secondAxle, int length) {
		this.firstAxle = firstAxle;
		this.secondAxle = secondAxle;
		this.length = length;
		this.imageName = imageName;
		loadImage();
	}

	private void loadImage() {
		image = Resource.getImage(imageName + ".png");
	}

}
