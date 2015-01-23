package ch.awae.simtrack.model;

/**
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public interface IEntity {

	public void tick();

	public void update(IModel model);

}
