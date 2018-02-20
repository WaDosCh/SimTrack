package ch.awae.simtrack.controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.controller.input.Action;
import ch.awae.simtrack.controller.input.Binding;
import ch.awae.simtrack.controller.input.Binding.EdgeProcessor;
import ch.awae.simtrack.controller.input.Binding.SkipConsumeException;
import ch.awae.simtrack.controller.input.Input;
import ch.awae.simtrack.model.Model;
import ch.awae.simtrack.model.position.SceneCoordinate;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.IViewPort;
import ch.awae.utils.logic.Logic;
import lombok.Getter;

public abstract class EventDrivenTool implements Tool {

	private List<Runnable> drivers = new ArrayList<>();
	protected final Input input;
	protected final Editor editor;
	protected final IController controller;
	protected final IViewPort viewPort;
	protected @Getter Point mousePosition = new Point(0, 0);
	protected @Getter TileCoordinate mouseTile = null;
	protected @Getter SceneCoordinate mouseScene = null;
	protected Model model = null;
	protected final Logger logger = LogManager.getLogger(getClass());
	private final Binding drop;
	private final boolean autoUnload;

	public enum UnloadAction {
		UNLOAD,
		IGNORE;
	}

	public EventDrivenTool(Editor editor, UnloadAction action) {
		this.input = editor.getInput();
		drop = input.getBinding(Action.DROP_TOOL);
		autoUnload = action == UnloadAction.UNLOAD;
		this.editor = editor;
		this.controller = editor.getController();
		this.viewPort = this.controller.getGameView().getViewPort();
		onTick(this::preTick);
	}

	private void preTick() {
		model = controller.getModel();
		mousePosition = input.getMousePosition();
		mouseScene = viewPort.toSceneCoordinate(mousePosition);
		mouseTile = mouseScene.toTileCoordinate();
	}

	/**
	 * Registers the Runnable to be executed unconditionally on every tick
	 */
	protected void onTick(Runnable r) {
		drivers.add(r);
	}

	/**
	 * Registers the Runnable to be executed on every tick where the key bound
	 * to the given action is pressed
	 * 
	 * @param action
	 * @param r
	 */
	protected void ifPressed(Action action, Runnable r) {
		Binding binding = input.getBinding(action);
		drivers.add(() -> binding.ifPressed(r));
	}

	/**
	 * Registers the Runnable to be executed on every tick where the key bound
	 * to the given key is pressed
	 * 
	 * @param keycode
	 * @param r
	 */
	protected void ifPressed(int keycode, Runnable r) {
		Binding binding = input.getBinding(keycode);
		drivers.add(() -> binding.ifPressed(r));
	}

	/**
	 * Registers the Runnable to be executed on every tick where the key bound
	 * to the given action is released
	 * 
	 * @param action
	 * @param r
	 */
	protected void ifReleased(Action action, Runnable r) {
		Binding binding = input.getBinding(action);
		drivers.add(() -> binding.ifPressed(r));
	}

	/**
	 * Registers the Runnable to be executed on every tick where the key bound
	 * to the given key is released
	 * 
	 * @param keycode
	 * @param r
	 */
	protected void ifReleased(int keycode, Runnable r) {
		Binding binding = input.getBinding(keycode);
		drivers.add(() -> binding.ifPressed(r));
	}

	/**
	 * Registers the Runnable to be executed on every tick where the key bound
	 * to the action is newly pressed. The edge is consumed.
	 * 
	 * @param action
	 * @param r
	 */
	protected void onPress(Action action, EdgeProcessor r) {
		Binding binding = input.getBinding(action);
		drivers.add(() -> {
			try {
				binding.onPress(r);
			} catch (TerminateTickException tte) {
				// if tick is terminated without SkipConsume, manually consume.
				binding.consume();
				throw tte;
			}
		});
	}

	/**
	 * Registers the Runnable to be executed on every tick where the key bound
	 * to the key is newly pressed. The edge is consumed.
	 * 
	 * @param keycode
	 * @param r
	 */
	protected void onPress(int keycode, EdgeProcessor r) {
		Binding binding = input.getBinding(keycode);
		drivers.add(() -> {
			try {
				binding.onPress(r);
			} catch (TerminateTickException tte) {
				// if tick is terminated without SkipConsume, manually consume.
				binding.consume();
				throw tte;
			}
		});
	}

	/**
	 * Registers the Runnable to be executed on every tick where the key bound
	 * to the action is newly released. The edge is consumed.
	 * 
	 * @param action
	 * @param r
	 */
	protected void onRelease(Action action, EdgeProcessor r) {
		Binding binding = input.getBinding(action);
		drivers.add(() -> {
			try {
				binding.onRelease(r);
			} catch (TerminateTickException tte) {
				// if tick is terminated without SkipConsume, manually consume.
				binding.consume();
				throw tte;
			}
		});
	}

	/**
	 * Registers the Runnable to be executed on every tick where the key bound
	 * to the key is newly released. The edge is consumed.
	 * 
	 * @param keycode
	 * @param r
	 */
	protected void onRelease(int keycode, EdgeProcessor r) {
		Binding binding = input.getBinding(keycode);
		drivers.add(() -> {
			try {
				binding.onRelease(r);
			} catch (TerminateTickException tte) {
				// if tick is terminated without SkipConsume, manually consume.
				binding.consume();
				throw tte;
			}
		});
	}

	@Override
	public void tick() {
		if (autoUnload && drop.isPressed() && drop.isEdge()) {
			drop.consume();
			editor.loadTool(null);
			logger.info("auto-unloading tool");
			return;
		}
		try {
			for (Runnable r : drivers) {
				r.run();
			}
		} catch (TerminateTickException tte) {
			logger.info("terminating tick");
		} catch (TerminateTickAndSkipConsumeException ttasce) {
			logger.info("terminating tick without consuming the last processed edge");
		}
	}

	protected void skipConsume() throws SkipConsumeException {
		throw new Binding.SkipConsumeException();
	}

	protected void terminateTick() {
		throw new TerminateTickException();
	}

	protected void terminateTickUnconsumed() {
		throw new TerminateTickAndSkipConsumeException();
	}

	protected ConditionalStep ifMet(Logic condition) {
		return new ConditionalStep(condition::evaluate);
	}

	protected ConditionalStep ifNot(Logic condition) {
		return new ConditionalStep(() -> !condition.evaluate());
	}

	// ######### INNER CLASSES #########

	protected static class TerminateTickException extends RuntimeException {
		private static final long serialVersionUID = -7131875927491205793L;
	}

	protected static class TerminateTickAndSkipConsumeException extends RuntimeException {
		private static final long serialVersionUID = -7131875927491205793L;
	}

	protected class ConditionalStep {
		private final BooleanSupplier condition;

		protected ConditionalStep(BooleanSupplier condition) {
			this.condition = condition;
		}

		public void onTick(Runnable r) {
			drivers.add(() -> {
				if (condition.getAsBoolean())
					r.run();
			});
		}

		public void ifPressed(Action action, Runnable r) {
			Binding binding = input.getBinding(action);
			onTick(() -> binding.ifPressed(r));
		}

		/**
		 * Registers the Runnable to be executed on every tick where the key
		 * bound to the given key is pressed
		 * 
		 * @param keycode
		 * @param r
		 */
		public void ifPressed(int keycode, Runnable r) {
			Binding binding = input.getBinding(keycode);
			onTick(() -> binding.ifPressed(r));
		}

		/**
		 * Registers the Runnable to be executed on every tick where the key
		 * bound to the given action is released
		 * 
		 * @param action
		 * @param r
		 */
		public void ifReleased(Action action, Runnable r) {
			Binding binding = input.getBinding(action);
			onTick(() -> binding.ifPressed(r));
		}

		/**
		 * Registers the Runnable to be executed on every tick where the key
		 * bound to the given key is released
		 * 
		 * @param keycode
		 * @param r
		 */
		public void ifReleased(int keycode, Runnable r) {
			Binding binding = input.getBinding(keycode);
			onTick(() -> binding.ifPressed(r));
		}

		/**
		 * Registers the Runnable to be executed on every tick where the key
		 * bound to the action is newly pressed. The edge is consumed.
		 * 
		 * @param action
		 * @param r
		 */
		public void onPress(Action action, EdgeProcessor r) {
			Binding binding = input.getBinding(action);
			onTick(() -> {
				try {
					binding.onPress(r);
				} catch (TerminateTickException tte) {
					// if tick is terminated without SkipConsume, manually
					// consume.
					binding.consume();
					throw tte;
				}
			});
		}

		/**
		 * Registers the Runnable to be executed on every tick where the key
		 * bound to the key is newly pressed. The edge is consumed.
		 * 
		 * @param keycode
		 * @param r
		 */
		public void onPress(int keycode, EdgeProcessor r) {
			Binding binding = input.getBinding(keycode);
			onTick(() -> {
				try {
					binding.onPress(r);
				} catch (TerminateTickException tte) {
					// if tick is terminated without SkipConsume, manually
					// consume.
					binding.consume();
					throw tte;
				}
			});
		}

		public void onRelease(Action action, EdgeProcessor r) {
			Binding binding = input.getBinding(action);
			onTick(() -> {
				try {
					binding.onRelease(r);
				} catch (TerminateTickException tte) {
					// if tick is terminated without SkipConsume, manually
					// consume.
					binding.consume();
					throw tte;
				}
			});
		}

		public void onRelease(int keycode, EdgeProcessor r) {
			Binding binding = input.getBinding(keycode);
			onTick(() -> {
				try {
					binding.onRelease(r);
				} catch (TerminateTickException tte) {
					// if tick is terminated without SkipConsume, manually
					// consume.
					binding.consume();
					throw tte;
				}
			});
		}

	}

}
