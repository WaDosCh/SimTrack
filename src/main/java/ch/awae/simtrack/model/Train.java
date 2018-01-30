package ch.awae.simtrack.model;

import java.util.Stack;
import java.util.function.Consumer;

import ch.awae.simtrack.model.position.TileEdgeCoordinate;

public class Train implements IEntity {

	private static final long serialVersionUID = -559986357702522674L;

	private TileEdgeCoordinate position;
	private PathFindingOptions pathFindingOptions;
	private Stack<TileEdgeCoordinate> path;

	private int timer;

	public Train(TileEdgeCoordinate start, PathFindingOptions pathFindingOptions) {
		this.position = start;
		this.pathFindingOptions = pathFindingOptions;
		this.timer = 0;
	}

	@Override
	public void tick(Consumer<PathFindingRequest> pathFinding) {
		if (this.path == null && this.pathFindingOptions != null) {
			PathFindingRequest request = new PathFindingRequest(this, this.position, null, this.pathFindingOptions,
					(path) -> this.path = path);
			pathFinding.accept(request);
			this.pathFindingOptions = null;
		}
		if (this.path != null) {
			if (this.timer++ % 10 == 0) {
				this.position = this.path.pop();
				if (this.path.size() == 0)
					this.path = null;
			}
		}
	}

	public TileEdgeCoordinate getPosition() {
		return this.position;
	}

}
