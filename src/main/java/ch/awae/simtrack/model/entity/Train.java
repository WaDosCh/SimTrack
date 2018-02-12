package ch.awae.simtrack.model.entity;

import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.model.PathFindingOptions;
import ch.awae.simtrack.model.PathFindingRequest;
import ch.awae.simtrack.model.position.*;
import ch.judos.generic.data.DynamicList;

public class Train implements IEntity {

	private static Logger logger = LogManager.getLogger();

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
	private TileEdgeCoordinate currentTileTargetEdge;
	private TilePathCoordinate currentTilePath;
	private DynamicList<TrainElementConfiguration> trainElements;
	private DynamicList<TilePathCoordinate> reservedTiles;

	public Train(TileEdgeCoordinate start, PathFindingOptions pathFindingOptions,
			TrainElementConfiguration firstElement) {
		this.currentTileTargetEdge = start;
		this.pathFindingOptions = pathFindingOptions;
		this.progressedDistance = 0.;
		this.speed = 5;
		this.trainElements = new DynamicList<>(firstElement);
		this.reservedTiles = new DynamicList<>();

		// TODO: add moooarr power
		// this.trainElements.add(TrainElementConfiguration.locomotive1);
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
		Edge nextStartEdge = this.currentTileTargetEdge.edge.getOpposite();
		this.currentTileTargetEdge = this.path.pop();
		TilePath nextPath = new TilePath(nextStartEdge, this.currentTileTargetEdge.edge);
		this.currentTilePath = new TilePathCoordinate(this.currentTileTargetEdge.tile, nextPath);
		this.reservedTiles.add(this.currentTilePath);
	}

	public TileEdgeCoordinate getHeadPosition() {
		return this.currentTileTargetEdge;
	}

	public SceneCoordinate getNicePosition() {
		if (this.currentTilePath == null)
			return this.currentTileTargetEdge.getSceneCoordinate();
		else
			return this.currentTilePath.getPositionOnPath(this.progressedDistance);
	}

	public SceneCoordinate getPositionWithOffset(double offset) {
		double progressedOnTile = this.progressedDistance - offset;
		if (progressedOnTile >= 0)
			return this.currentTilePath.getPositionOnPath(progressedOnTile);
		// current tilePath already checked
		int historyIndex = this.reservedTiles.size() - 2;
		while (historyIndex > 0) {
			TilePathCoordinate tilePath = this.reservedTiles.get(historyIndex);
			progressedOnTile += tilePath.getPathLength();
			if (progressedOnTile >= 0)
				return tilePath.getPositionOnPath(progressedOnTile);
			historyIndex--;
		}
		logger.info("reserved tiles: " + this.reservedTiles.size());
		return null;
	}

	private void searchPath(Consumer<PathFindingRequest> pathFinding) {
		PathFindingRequest request = new PathFindingRequest(this, this.currentTileTargetEdge, null,
				this.pathFindingOptions, (path) -> {
					this.path = path;
					logger.info("Train found path to: " + path.firstElement());
				});
		pathFinding.accept(request);
		this.pathFindingOptions = null;
	}

}
