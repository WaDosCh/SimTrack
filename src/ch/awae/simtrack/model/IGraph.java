package ch.awae.simtrack.model;

import java.util.List;

/**
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-23
 * @since SimTrack 0.2.1
 *
 * @param <E>
 *            the node type
 * @param <K>
 *            the edge type
 */
public interface IGraph<E, K> {

	public List<E> getNeighbours(E node);

	public double getCost(E start, E end);

	public void removeEdge(E start, E end);

	public K getEdge(E start, E end);

	public void addEdge(E start, E end, K edge);

}
