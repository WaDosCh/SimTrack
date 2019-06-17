package ch.awae.simtrack.scene.game.model.tile;

import java.awt.Color;

import ch.awae.simtrack.scene.game.model.Goods;
import ch.awae.simtrack.scene.game.view.renderer.TileRenderer;
import ch.awae.simtrack.window.Graphics;

public class Factory implements Tile, Updatable {

	private static final long serialVersionUID = -1227945674781926168L;
	private Goods goods;
	private int maxStorage;
	private int stored;

	public Factory(Goods produce) {
		this.goods = produce;
		this.maxStorage = 25;
		this.stored = 0;
	}

	@Override
	public void update() {
		if (this.stored < this.maxStorage)
			this.stored++;
	}

	@Override
	public long getUpdateEveryTicks() {
		return 90;
	}

	@Override
	public void render(TileRenderer renderer, Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(-35, -35, 70, 70);
		g.setColor(this.goods.getColor());
		int x = -35 + 3;
		int y = -35 + 3;
		for (int i = 1; i <= this.stored; i++) {
			g.fillRect(x, y, 10, 10);
			x += 13;
			if (i % 5 == 0) {
				x = -35 + 3;
				y += 13;
			}
		}

	}

}
