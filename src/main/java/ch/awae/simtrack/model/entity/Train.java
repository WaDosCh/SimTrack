package ch.awae.simtrack.model.entity;

import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

import ch.awae.simtrack.controller.Log;
import ch.awae.simtrack.model.PathFindingOptions;
import ch.awae.simtrack.model.PathFindingRequest;
import ch.awae.simtrack.model.position.*;
import ch.judos.generic.data.DynamicList;

public class Train implements IEntity {

	private static final long serialVersionUID = -559986357702522674L;

	private PathFindingOptions pathFindingOptions;
	private Stack<TileEdgeCoordinate> path;

	private double progressedDistance; // distance progressed to reach next edge
	private double speed;

	/**
	 * The edge to be reached next. The train can never get past this edge (if
	 * it would the next tileEdge in the path is assigned to this var). For
	 * spawning the train spawns behind this edge as if it were waiting for the
	 * signal on this tileEdge to turn green.
	 */
	private TileEdgeCoordinate nextTileTargetEdge;
	private TilePathCoordinate currentTilePath;
	private DynamicList<TrainElementConfiguration> trainElements;

	public Train(TileEdgeCoordinate start, PathFindingOptions pathFindingOptions,
			TrainElementConfiguration firstElement) {
		this.nextTileTargetEdge = start;
		this.pathFindingOptions = pathFindingOptions;
		this.progressedDistance = 0.;
		this.speed = 5;
		this.trainElements = new DynamicList<>(firstElement);
	}

	public List<TrainElementConfiguration> getElements() {
		return this.trainElements;
	}

	@Override
	public void tick(Consumer<PathFindingRequest> pathFinding) {
		if (this.path == null && this.pathFindingOptions != null) {
			searchPath(pathFinding);
		}
		if (this.path != null) {
			move();
		}
	}

	private void move() {
		if (this.currentTilePath == null) {
			createNextTilePath();
		}

		this.progressedDistance += this.speed;
		if (this.progressedDistance >= this.currentTilePath.getPathLength()) {
			this.progressedDistance -= this.currentTilePath.getPathLength();
			if (this.path.size() > 0) {
				createNextTilePath();
			} else {
				this.progressedDistance = this.currentTilePath.getPathLength();
				this.path = null;
			}
		}
	}

	private void createNextTilePath() {
		Edge nextStartEdge = this.currentTilePath.getPath()._2.getOpposite();
		this.nextTileTargetEdge = this.path.pop();
		TilePath nextPath = new TilePath(nextStartEdge, this.nextTileTargetEdge.edge);
		this.currentTilePath = new TilePathCoordinate(this.nextTileTargetEdge.tile, nextPath);
	}

	public TileEdgeCoordinate getHeadPosition() {
		return this.nextTileTargetEdge;
	}

	public SceneCoordinate getNicePosition() {
		if (this.currentTilePath == null)
			return this.nextTileTargetEdge.getSceneCoordinate();
		else
			return this.currentTilePath.getPositionOnPath(this.progressedDistance);
	}

	private void searchPath(Consumer<PathFindingRequest> pathFinding) {
		PathFindingRequest request = new PathFindingRequest(this, this.nextTileTargetEdge, null,
				this.pathFindingOptions, (path) -> {
					this.path = path;
					Log.info("Train found path to: " + path.firstElement());
				});
		pathFinding.accept(request);
		this.pathFindingOptions = null;
	}

}
