package ch.awae.simtrack.scene.game.model;

import java.awt.Color;

public enum Goods {

	WOOD(new Color(225, 130, 50)),
	IRON(new Color(100, 200, 230));

	private Color color;

	private Goods(Color c) {
		this.color = c;
	}

	public Color getColor() {
		return this.color;
	}
}
