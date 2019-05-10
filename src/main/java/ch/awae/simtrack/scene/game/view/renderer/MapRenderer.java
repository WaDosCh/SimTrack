package ch.awae.simtrack.scene.game.view.renderer;

import java.util.ArrayList;

import ch.awae.simtrack.core.BaseRenderer;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.window.Graphics;

public class MapRenderer implements BaseRenderer {

	protected ArrayList<BaseRenderer> renderers;

	public MapRenderer(InputController input, Model model, ViewPortNavigator viewPortNavigator) {
		this.renderers = new ArrayList<BaseRenderer>();

		addRenderer(new BackgroundRenderer());
		addRenderer(new TileRenderer(viewPortNavigator, model));
		addRenderer(new HexGridRenderer(viewPortNavigator, model));
		addRenderer(new SignalRenderer(viewPortNavigator, model));
		addRenderer(new EntityRenderer(viewPortNavigator, model));
		addRenderer(new TileReservationRenderer(viewPortNavigator, model));
		addRenderer(new CoordinateInformationRenderer(input, viewPortNavigator, model));
	}

	protected void addRenderer(BaseRenderer renderer) {
		this.renderers.add(renderer);
	}

	@Override
	public void render(Graphics graphics) {
		for (BaseRenderer renderer : this.renderers) {
			renderer.render(graphics);
		}
	}

}
