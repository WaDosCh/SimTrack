package ch.awae.simtrack.scene.game.controller.mapgen;

import java.awt.Dimension;

public class ModelCreationOptions {

	public Dimension size = new Dimension(20, 14);
	public int connectionCount = 10;
	public int startingMoney = 60;
	public int bulldozeCost = 2;
	public int startingFactories = 4; // per 100 tiles

	public int getArea() {
		return this.size.width * this.size.height;
	}

}
