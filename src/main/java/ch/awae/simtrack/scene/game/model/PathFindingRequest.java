package ch.awae.simtrack.scene.game.model;

import java.util.Stack;
import java.util.function.Consumer;

import ch.awae.simtrack.scene.game.model.entity.Entity;
import ch.awae.simtrack.scene.game.model.position.TileEdgeCoordinate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PathFindingRequest {

	public final Entity entity;
	public final TileEdgeCoordinate start;
	public final TileEdgeCoordinate end;
	public final PathFindingOptions options;
	public final Consumer<Stack<TileEdgeCoordinate>> pathAcceptor;
	public final Runnable noPathFound;

}
