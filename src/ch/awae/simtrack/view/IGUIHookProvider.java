package ch.awae.simtrack.view;

import java.awt.Graphics2D;
import java.util.function.Consumer;

public interface IGUIHookProvider {

	public Runnable getRenderDelegate();

	public void hookComponentRenderer(Consumer<Graphics2D> consumer);

	public int getScreenWidth();

	public int getScreenHeight();

}
