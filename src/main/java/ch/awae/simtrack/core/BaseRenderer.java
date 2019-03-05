package ch.awae.simtrack.core;

@FunctionalInterface
public interface BaseRenderer<T extends Scene<T>> extends BaseComponent<T> {

	void render(Graphics graphics, T scene);

	
	/**
	 * @param name
	 * @param renderer
	 * @return a named version of the renderer
	 */
	public static <X extends Scene<X>> BaseRenderer<X> getNamed(String name, BaseRenderer<X> renderer) {
		return new BaseRenderer<X>() {

			@Override
			public void render(Graphics graphics, X scene) {
				renderer.render(graphics, scene);
			}

			@Override
			public String getName() {
				return name;
			}
		};
	}

}
