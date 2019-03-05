package ch.awae.simtrack.core;

public interface BaseTicker<T extends Scene<T>> extends NamedComponent {

	void tick(T scene);

	/**
	 * @param name
	 * @param ticker
	 * @return a named version of the ticker
	 */
	public static <X extends Scene<X>> BaseTicker<X> getNamed(String name, BaseTicker<X> ticker) {
		return new BaseTicker<X>() {

			@Override
			public void tick(X scene) {
				ticker.tick(scene);
			}

			@Override
			public String getName() {
				return name;
			}
		};
	}
}
