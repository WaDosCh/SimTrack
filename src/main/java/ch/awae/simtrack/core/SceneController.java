package ch.awae.simtrack.core;

public interface SceneController {

	public <T extends Scene<T>> void loadScene(Class<T> scene, Object... arg);

}
