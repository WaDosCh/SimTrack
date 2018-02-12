package ch.awae.simtrack.model.entity;

import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

import ch.awae.simtrack.controller.Log;
import ch.awae.simtrack.model.PathFindingOptions;
import ch.awae.simtrack.model.PathFindingRequest;
import ch.awae.simtrack.model.position.SceneCoordinate;
import ch.awae.simtrack.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.model.position.TilePath;
import ch.judos.generic.data.DynamicList;

public class Train implements IEntity {

	private static final long serialVersionUID = -559986357702522674L;

	private TileEdgeCoordinate currentTileEdge;
	private PathFindingOptions pathFindingOptions;
	private Stack<TileEdgeCoordinate> path;

	private double progressedDistance; // distance progressed to reach next edge
	private double speed;
	private TilePath currentTilePath;
	private DynamicList<TrainElementConfiguration> trainElements;

	public Train(TileEdgeCoordinate start, PathFindingOptions pathFindingOptions,
			TrainElementConfiguration firstElement) {
		this.currentTileEdge = start;
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
		TileEdgeCoordinate nextStartTileEdge = this.currentTileEdge.getOppositeDirection();
		this.currentTileEdge = this.path.pop();
		this.currentTilePath = new TilePath(nextStartTileEdge.edge, this.currentTileEdge.edge);
	}

	public TileEdgeCoordinate getHeadPosition() {
		return this.currentTileEdge;
	}

	public SceneCoordinate getNicePosition() {
		if (this.currentTilePath == null) {
			return this.currentTileEdge.getSceneCoordinate();
		} else {
			SceneCoordinate tilePos = this.currentTileEdge.getTile().getSceneCoordinate();
			SceneCoordinate delta = this.currentTilePath.getPosition(this.progressedDistance);
			return new SceneCoordinate(tilePos.s + delta.s, tilePos.t + delta.t);
		}
	}

	private void searchPath(Consumer<PathFindingRequest> pathFinding) {
		PathFindingRequest request = new PathFindingRequest(this, this.currentTileEdge, null, this.pathFindingOptions,
				(path) -> {
					this.path = path;
					Log.info("Train found path to: " + path.firstElement());
				});
		pathFinding.accept(request);
		this.pathFindingOptions = null;
	}

}
