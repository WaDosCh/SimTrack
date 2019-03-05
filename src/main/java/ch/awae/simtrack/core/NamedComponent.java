package ch.awae.simtrack.core;

public interface NamedComponent {

	default String getName() {
		return this.getClass().getSimpleName();
	}
	
}
