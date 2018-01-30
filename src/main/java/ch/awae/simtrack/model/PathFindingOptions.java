package ch.awae.simtrack.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PathFindingOptions {
	public Type type;

	public enum Type {
		RandomTarget;
	}
}
