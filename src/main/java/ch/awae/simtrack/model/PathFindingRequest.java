package ch.awae.simtrack.model;

import java.util.Stack;
import java.util.function.Consumer;

import ch.awae.simtrack.model.entity.IEntity;
import ch.awae.simtrack.model.position.TileEdgeCoordinate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PathFindingRequest {

	public final IEntity entity;
	public final TileEdgeCoordinate start;
	public final TileEdgeCoordinate end;
	public final PathFindingOptions options;
	public final Consumer<Stack<TileEdgeCoordinate>> pathAcceptor;

}
