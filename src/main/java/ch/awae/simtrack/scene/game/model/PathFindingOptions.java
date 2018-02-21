package ch.awae.simtrack.scene.game.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PathFindingOptions {
	public Type type;

	public enum Type {
		RandomTarget;
	}
}
