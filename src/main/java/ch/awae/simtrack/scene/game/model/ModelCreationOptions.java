package ch.awae.simtrack.scene.game.model;

import java.awt.Dimension;

public class ModelCreationOptions {

	public Dimension size;
	public int connectionCount;
	public int startingMoney;
	public int bulldozeCost;
	
	public ModelCreationOptions() {
		this.size = new Dimension(20, 14);
		this.connectionCount = 10;
		this.startingMoney = 60;
		this.bulldozeCost = 2;
	}
}
