package ch.awae.simtrack.scene;

public interface BaseTicker<T extends Scene<T>> {

	void tick(T scene);

}
