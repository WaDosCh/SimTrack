package ch.awae.simtrack.core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.scene.game.Game;
import lombok.Getter;

public abstract class EventDrivenTool implements Tool, BaseRenderer {

	private List<Runnable> drivers = new ArrayList<>();
	protected final InputController input;
	protected final Editor editor;
	protected final Game scene;
	protected @Getter Point mousePosition = new Point(0, 0);
	protected final Logger logger = LogManager.getLogger(getClass());

	public EventDrivenTool(Editor editor) {
		this.input = editor.getInput();
		this.editor = editor;
		this.scene = editor.getScene();
	}

	@Override
	public void tick() {
		for (Runnable r : drivers) {
			r.run();
		}
	}

	@Override
	public BaseRenderer getRenderer() {
		return this;
	}

}
