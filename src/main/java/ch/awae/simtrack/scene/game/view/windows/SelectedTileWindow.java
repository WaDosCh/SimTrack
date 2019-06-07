package ch.awae.simtrack.scene.game.view.windows;

import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.core.ui.WindowComponent;
import ch.awae.simtrack.scene.game.controller.event.EventListener;
import ch.awae.simtrack.scene.game.controller.event.EventType;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.tile.Tile;
import ch.awae.simtrack.scene.game.view.Design;

public class SelectedTileWindow extends WindowComponent implements EventListener {

	private TileCoordinate tileCoord;
	private Tile tile;
	private Model model;

	public SelectedTileWindow(Model model, InputController input) {
		super(Design.textFont, input);
		this.model = model;
		this.tileCoord = model.getSelectedTile();
		this.tile = model.getTileAt(this.tileCoord);

		this.title = "Tile at " + this.tileCoord.u + " / " + this.tileCoord.v;
		this.model.getEventBus().subscribe(EventType.SELECTION_CHANGED, this);
		this.addComponent(new Label("You selected something"));
	}

	@Override
	public void eventOccured(EventType type, Object... args) {
		this.dispose();
		this.model.getEventBus().unsubscribe(this);
	}

}
