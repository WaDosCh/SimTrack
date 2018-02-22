package ch.awae.simtrack.scene.game.model.entity;

import java.util.List;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.PathFindingOptions;
import ch.awae.simtrack.scene.game.model.PathFindingRequest;
import ch.awae.simtrack.scene.game.model.position.*;
import ch.judos.generic.data.DynamicList;
import ch.judos.generic.data.RandomJS;

public class Train implements Entity {

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
		this.speed = 2;
		this.trainElements = new DynamicList<>(firstElement);
		this.reservedTiles = new DynamicList<>();

		// TODO: add moooarr power
		for (int i = 0; i < 5; i++) {
			this.trainElements
					.add(RandomJS.getObject(TrainElementConfiguration.wagon1, TrainElementConfiguration.wagon2));
		}
	}

	public List<TrainElementConfiguration> getElements() {
		return this.trainElements;
	}

	public int getTrainLength() {
		return this.trainElements.stream().mapToInt(element -> element.getLength()).sum();
	}

	@Override
	public void tick(Game g) {
		if (this.path == null && this.pathFindingOptions != null) {
			searchPath(g.getModel());
		}
		if (this.path != null) {
			move(g.getModel());
		}
	}

	private void move(Model model) {
		if (this.currentTilePath == null) {
			createNextTilePath();
		}

		this.progressedDistance += this.speed;
		if (this.progressedDistance >= this.currentTilePath.getPathLength()) {
			if (this.path.size() > 0) {
				this.progressedDistance -= this.currentTilePath.getPathLength();
				createNextTilePath();
			} else if (this.progressedDistance > getTrainLength() + this.currentTilePath.getPathLength()) {
				this.path = null;
				model.removeEntity(this);
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
		while (historyIndex >= 0) {
			TilePathCoordinate tilePath = this.reservedTiles.get(historyIndex);
			progressedOnTile += tilePath.getPathLength();
			if (progressedOnTile >= 0)
				return tilePath.getPositionOnPath(progressedOnTile);
			historyIndex--;
		}
		return null;
	}

	private void searchPath(Model model) {
		PathFindingRequest request = new PathFindingRequest(this, this.currentTileTargetEdge, null,
				this.pathFindingOptions, (path) -> {
					this.path = path;
					logger.info("Train found path to: " + path.firstElement());
				});
		model.getPathFindingQueue().add(request);
		this.pathFindingOptions = null;
	}

}
