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

	public static final double maxSpeed = 5;

	private PathFindingOptions pathFindingOptions;

	/**
	 * stack of target edges where the next signal could be located at before
	 * the train leaves a tile
	 */
	private Stack<TileEdgeCoordinate> path;

	/**
	 * distance progressed to reach next edge
	 */
	private double progressedDistance;
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

	private int id;
	private static int idCounter = 1;

	/**
	 * number of tiles this train is allowed to enter before he has to make a
	 * new reservation
	 */
	private int amountOfTilesAheadReserved;

	public Train(Model model, TileEdgeCoordinate start, PathFindingOptions pathFindingOptions,
			TrainElementConfiguration firstElement) {
		this.trainElements = new DynamicList<>(firstElement);
		this.reservedTiles = new DynamicList<>();
		this.amountOfTilesAheadReserved = 0;
		this.speed = 5;
		this.id = idCounter++;

		setStartingPosition(model, start, pathFindingOptions);
		addStarterHistory();

		// TODO: add moooarr power
		for (int i = 0; i < 5; i++) {
			this.trainElements
					.add(RandomJS.getObject(TrainElementConfiguration.wagon1, TrainElementConfiguration.wagon2));
		}
	}
	
	/**
	 * @return
	 */
	private double getAccelerationPerSecond() {
		//TODO: implement value based on power of locomotives and weight of wagons
		return 0.5;
	}
	
	private double getTimeToFullStop() {
		return this.speed / getAccelerationPerSecond();
	}
	
	private double getBreakingDistance() {
		return Math.pow(this.speed, 2) / (2*this.getAccelerationPerSecond());
	}

	private void setStartingPosition(Model model, TileEdgeCoordinate start, PathFindingOptions pathFindingOptions) {
		this.currentTileTargetEdge = start;
		this.pathFindingOptions = pathFindingOptions;
		this.currentTilePath = new TilePathCoordinate(start.tile,
				new TilePath(start.getEdge().getOpposite(), start.getEdge()));
		this.progressedDistance = 0;

		model.reserveTiles(this, new DynamicList<TileEdgeCoordinate>(this.currentTileTargetEdge));
	}

	private void addStarterHistory() {
		// add some starter tiles to the history in order for the train to draw
		// correctly when entering the map
		TileEdgeCoordinate start = this.currentTileTargetEdge;
		TileCoordinate earlier = start.tile.getNeighbour(start.edge.getOpposite());

		this.reservedTiles.add(new TilePathCoordinate(earlier, new TilePath(start.edge.getOpposite(), start.edge)));
		this.reservedTiles.add(new TilePathCoordinate(start.tile, new TilePath(start.edge.getOpposite(), start.edge)));
	}

	@Override
	public String toString() {
		return "Train" + this.id;
	}

	public int getId() {
		return id;
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
			if (this.pathFindingOptions.searchAgainInTicks == 0) {
				searchPath(g.getModel());
			}
			if (this.pathFindingOptions.searchAgainInTicks == -300) {
				logger.info(this + " path request is starving...");
			}
			this.pathFindingOptions.searchAgainInTicks--;
		}
		if (this.path != null) {
			move(g.getModel());
		}
	}

	private void move(Model model) {
		// this.speed = MathJS.clamp(this.amountOfTilesAheadReserved, 2, 6);

		if (this.currentTilePath == null) {
			if (!createNextTilePath(model))
				return;
		}

		this.progressedDistance += this.speed;
		if (this.progressedDistance >= this.currentTilePath.getPathLength()) {
			if (this.path.size() > 0) {
				double length = this.currentTilePath.getPathLength();
				if (createNextTilePath(model)) {
					this.progressedDistance -= length;
				} else {
					this.progressedDistance = length;
				}
			} else if (this.progressedDistance > getTrainLength() + 50 + this.currentTilePath.getPathLength()) {
				this.path = null;
				logger.info(this + " has reached its destination");
				model.removeEntity(this);
			}
		}

		if (this.reservedTiles.size() > 0) {
			double sumHistory = this.reservedTiles.stream().mapToDouble(tile -> tile.getPathLength()).sum();
			sumHistory -= this.currentTilePath.getPathLength() - this.progressedDistance;
			double sumNewHistory = sumHistory - this.reservedTiles.get(0).getPathLength();
			if (getTrainLength() + 50 <= sumNewHistory) {
				TilePathCoordinate tile = this.reservedTiles.remove(0);
				model.releaseTile(this, tile.getTile());
			}
		}
	}

	/**
	 * @param model
	 * @return true if the train can enter a new tile
	 */
	private boolean createNextTilePath(Model model) {
		if (this.amountOfTilesAheadReserved == 0) {
			this.amountOfTilesAheadReserved = model.reserveTiles(this, this.path);
			if (this.amountOfTilesAheadReserved == 0)
				return false;
		}
		Edge nextStartEdge = this.currentTileTargetEdge.edge.getOpposite();

		this.currentTileTargetEdge = this.path.pop();
		this.amountOfTilesAheadReserved--;
		TilePath nextPath = new TilePath(nextStartEdge, this.currentTileTargetEdge.edge);
		this.currentTilePath = new TilePathCoordinate(this.currentTileTargetEdge.tile, nextPath);
		this.reservedTiles.add(this.currentTilePath);
		return true;
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
					this.pathFindingOptions = null;
					logger.info(this + " found path to: " + path.firstElement());
				}, () -> {
					this.pathFindingOptions.searchAgainInTicks = 60;
					logger.info("No path found for " + this + " trying again later");
				});
		model.getPathFindingQueue().add(request);
	}

}
