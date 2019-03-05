package ch.awae.simtrack.core;

@FunctionalInterface
public interface BaseRenderer extends NamedComponent {

	void render(Graphics graphics);

}
