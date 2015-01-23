package ch.awae.simtrack.model.track;

import java.util.ArrayList;

import ch.awae.simtrack.model.ITile;
import ch.awae.simtrack.model.position.TileCoordinate;

public class TrackProvider {

	private static ArrayList<ITile> tiles;

	static {
		tiles = new ArrayList<>();
		// register tiles
		tiles.add(new StraightRail(new TileCoordinate(0, 0)));
		tiles.add(new CurvedRail(new TileCoordinate(0, 0)));
		tiles.add(new StraightCrossing(new TileCoordinate(0, 0)));
		tiles.add(new StraightCurvedCrossing(new TileCoordinate(0, 0)));
		tiles.add(new CurvedCrossing(new TileCoordinate(0, 0)));
		tiles.add(new BasicTurnout(new TileCoordinate(0, 0)));
		tiles.add(new ThreeWayTurnout(new TileCoordinate(0, 0)));
		tiles.add(new WyeSwitch(new TileCoordinate(0, 0)));
		tiles.add(new SingleSlip(new TileCoordinate(0, 0)));
		tiles.add(new DoubleSlip(new TileCoordinate(0, 0)));
	}

	public static ITile getTileInstance(int tileID) {
		return tiles.get(tileID).cloneTile();
	}

	public static int getTileCount() {
		return tiles.size();
	}

}
