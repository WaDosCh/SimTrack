package controller;

import java.util.ArrayList;

import ch.awae.simtrack.model.Map;
import ch.awae.simtrack.model.track.BorderTrackTile;

/**
 * This interface describes the utility that spawns the border connections when
 * creating a new game
 * 
 * @author Andreas Wälchli
 * @version 1.1 (2015-01-16)
 * @since SimTrack 0.0.1 (2015-01-16)
 */
public interface IBorderConnectionSpawner {

	/**
	 * spawns border connections for the map.
	 * 
	 * @param map
	 *            the map to spawn for
	 * @return the border connections. Duplicate positions are allowed but will
	 *         be ignored.
	 */
	public ArrayList<BorderTrackTile> spawnConnections(Map map);
}
