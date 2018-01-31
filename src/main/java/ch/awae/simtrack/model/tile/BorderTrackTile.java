package ch.awae.simtrack.model.tile;

import ch.awae.simtrack.model.position.Edge;
import ch.awae.simtrack.model.position.TilePath;

/**
 * Implementation for the border track pieces. They do not contain any paths.
 * 
 * @author Andreas Wälchli
 * @version 2.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public class BorderTrackTile extends BasicTrackTile implements IDestinationTrackTile {

	private static final long serialVersionUID = -2434920987381887298L;
	private final Edge edge;
	private final boolean isOutput;

	private final static BorderTrackTile[] INSTANCE;

	static {
		INSTANCE = new BorderTrackTile[12];
		for (Edge e : Edge.values()) {
			INSTANCE[e.ordinal()] = new BorderTrackTile(e, false);
			INSTANCE[e.ordinal() + 6] = new BorderTrackTile(e, true);
		}
	}

	public static BorderTrackTile getInstance(Edge edge, boolean isOutput) {
		return INSTANCE[edge.ordinal() + (isOutput ? 6 : 0)];
	}

	private BorderTrackTile(Edge edge, boolean isOutput) {
		this.edge = edge;
		this.isOutput = isOutput;
	}

	@Override
	public float getTravelCost() {
		return 1;
	}

	@Override
	public TilePath[] getRailPaths() {
		return new TilePath[] { new TilePath(edge, edge.getOpposite()) };
	}

	@Override
	public TilePath[] getPaths() {
		return new TilePath[] {
				isOutput ? new TilePath(edge, edge.getOpposite()) : new TilePath(edge.getOpposite(), edge) };
	}

	@Override
	public boolean connectsAt(Edge edge) {
		return edge == this.edge;
	}

	@Override
	public boolean isTrainSpawner() {
		return !isOutput;
	}

}
