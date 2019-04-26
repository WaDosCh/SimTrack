package ch.awae.simtrack.core;

import java.awt.Image;
import java.util.function.Consumer;

public interface SceneController {

	public Scene loadScene(Class<? extends Scene> scene, Object... arg);

	public void requestSnapshot(Consumer<Image> callback);
}
