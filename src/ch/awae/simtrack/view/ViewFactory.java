package ch.awae.simtrack.view;

import java.util.ArrayList;

import ch.awae.simtrack.model.IModel;
import ch.awae.simtrack.view.renderer.HexGridRenderer;
import ch.awae.simtrack.view.renderer.TrackRenderer;

public class ViewFactory {

	public static IView createGameView(IModel model, IGUIHookProvider hooker) {
		GameView v = new GameView(model, hooker.getScreenWidth(),
				hooker.getScreenHeight());
		v.setRenderingDelegate(hooker.getRenderDelegate());
		hooker.hookComponentRenderer(v::render);
		ArrayList<IRenderer> rends = new ArrayList<>();
		rends.add(new TrackRenderer());
		rends.add(new HexGridRenderer());
		v.setRenderers(rends);
		return v;
	}
}
