package ch.awae.simtrack.scene.game.model;

import java.io.Serializable;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PathFindingOptions implements Serializable {

	private static final long serialVersionUID = -433056803149757704L;

	public final Type type;

	public int searchAgainInTicks = 0;

	public enum Type {
		RandomTarget;
	}
}
