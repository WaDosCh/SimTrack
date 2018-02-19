package ch.awae.simtrack.view;

import java.util.ArrayList;

import ch.awae.simtrack.model.IModel;
import ch.awae.simtrack.view.renderer.EntityRenderer;
import ch.awae.simtrack.view.renderer.HexGridRenderer;
import ch.awae.simtrack.view.renderer.IRenderer;
import ch.awae.simtrack.view.renderer.TileRenderer;

/**
 * factory for view instance
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-26
 * @since SimTrack 0.2.1
 */
public class ViewFactory {

	/**
	 * creates a new game view instance
	 * 
	 * @param model
	 * @param hooker
	 * @return a new game view instance
	 */
	public static GameView createGameView(IModel model, IGUIHookProvider hooker) {
		GameView v = new GameView(model, hooker.getScreenWidth(), hooker.getScreenHeight());
		v.setRenderingDelegate(hooker.getRenderDelegate());
		hooker.hookComponentRenderer(v::render);
		ArrayList<IRenderer> rends = new ArrayList<>();
		rends.add(new TileRenderer());
		rends.add(new HexGridRenderer());
		rends.add(new EntityRenderer());
		v.setRenderers(rends);
		return v;
	}
}
