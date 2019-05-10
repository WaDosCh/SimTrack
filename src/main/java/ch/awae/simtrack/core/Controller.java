package ch.awae.simtrack.core;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.profiler.Profiler;
import ch.awae.simtrack.core.profiler.ProfilerI;
import ch.awae.simtrack.window.GameWindow;
import ch.awae.simtrack.window.Graphics;
import ch.awae.simtrack.window.Graphics.GraphicsStack;
import ch.judos.generic.graphics.ImageUtils;
import lombok.Getter;

public class Controller implements SceneController {

	private HighPrecisionClock gameClock;
	protected GameWindow window;
	protected @Getter final InputController input;
	protected Scene currentScene = null;
	protected final Logger logger = LogManager.getLogger();

	private ProfilerI profiler;
	private boolean screenshotRequested = false;

	private long startOfLastTick = System.currentTimeMillis();
	private SceneFactory sceneFactory;

	public Controller(GameWindow window) {
		this.window = window;
		this.input = window.getInput();
		this.sceneFactory = new SceneFactory(this, window);
		this.gameClock = new HighPrecisionClock(60, this::tick, "Game Loop");

		profiler = new Profiler();
	}

	public void start() {
		this.gameClock.start();
	}

	public void stop() {
		this.gameClock.stop();
	}

	/**
	 * Main loop of the game, rendering, updating everything
	 */
	private void tick() {

		long newStart = System.currentTimeMillis();
		long deltaT = newStart - startOfLastTick;
		startOfLastTick = newStart;

		this.profiler.startFrame();

		this.profiler.startSample(this.input);

		handleAllInputEvents();

		this.profiler.endSample(this.input);

		// setup screenshot buffers if required
		BufferedImage snapshot = null;
		Graphics snapshotGraphics = null;
		if (this.screenshotRequested) {
			snapshot = new BufferedImage(window.getScreenSize().width, window.getScreenSize().height,
					BufferedImage.TYPE_INT_RGB);
			snapshotGraphics = new Graphics(snapshot.createGraphics());
			snapshotGraphics.setBackground(GameWindow.BG_COLOR);
			snapshotGraphics.clearRect(0, 0, window.getScreenSize().width, window.getScreenSize().height);
			snapshotGraphics.clipRect(0, 0, window.getScreenSize().width, window.getScreenSize().height);
		}

		window.flipFrame();
		Graphics graphics = window.getGraphics();
	    
		// clip makes everyone aware of the available screen size
		graphics.clipRect(0, 0, window.getScreenSize().width, window.getScreenSize().height);
		if (this.currentScene == null)
			return;

		if (this.window.resized()) {
			this.currentScene.screenResized(this.window.getScreenSize());
		}

		for (BaseRenderer renderer : this.currentScene.getRenderers()) {
			profiler.startSample(renderer);
			GraphicsStack stack = graphics.getStack();
			renderer.render(graphics);
			graphics.setStack(stack);
			if (snapshotGraphics != null) {
				GraphicsStack stack2 = snapshotGraphics.getStack();
				renderer.render(snapshotGraphics);
				snapshotGraphics.setStack(stack2);
			}
			profiler.endSample(renderer);
		}

		this.currentScene.preTick(deltaT);

		for (BaseTicker ticker : this.currentScene.getTickers()) {
			profiler.startSample(ticker);
			ticker.tick();
			profiler.endSample(ticker);
		}

		this.profiler.render(graphics);
		if (snapshotGraphics != null) {
			this.profiler.render(snapshotGraphics);
		}

		if (snapshot != null) {
			// this assertion is here to stop my compiler from complaining about
			// potential null pointers (which is actually impossible, but
			// hey...)
			assert (snapshotGraphics != null);
			snapshotGraphics.dispose();
			saveScreenshot(snapshot);
		}

		profiler.endFrame();
	}

	private void handleAllInputEvents() {
		for (InputEvent event : this.input.popAllEvents()) {
			if (event.isPressActionAndConsume(InputAction.QUIT_GAME))
				System.exit(0);
			if (event.isPressActionAndConsume(InputAction.TAKE_SCREENSHOT)) {
				requestScreenshot();
				continue;
			}
			this.input.handleInput(event);
			if (event.isConsumed)
				continue;
			this.profiler.handleInput(event);
			if (event.isConsumed)
				continue;
			this.currentScene.handleInput(event);
			if (event.isConsumed)
				continue;
			if (event.isPress())
				logger.debug("unhandled: " + event);
		}
	}

	private void saveScreenshot(Image screenshot) {
		ImageWriter writer = null;
		FileImageOutputStream outputStream = null;
		try {
			// bufferedImage
			BufferedImage buffer = ImageUtils.toBufferedImage(screenshot);

			// define screenshot quality
			JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
			jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			jpegParams.setCompressionQuality(0.95f);

			writer = ImageIO.getImageWritersByFormatName("jpg").next();
			outputStream = new FileImageOutputStream(new File("screenshot.jpg"));
			writer.setOutput(outputStream);
			// writes the file with given compression level
			// from your JPEGImageWriteParam instance
			writer.write(null, new IIOImage(buffer, null, null), jpegParams);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				writer.dispose();
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
				}
			}
		}
		this.screenshotRequested = false;
	}

	@Override
	public Scene loadScene(Class<? extends Scene> sceneClass, Object... args) {
		Scene scene = this.sceneFactory.createScene(sceneClass, args);
		if (scene != null) {
			if (this.currentScene != null) {
				logger.trace("unloading scene " + this.currentScene);
				this.currentScene.unloadScene();
			}

			logger.trace("loading scene " + scene);
			scene.loadScene();
			this.currentScene = scene;
		}
		return this.currentScene;
	}

	@Override
	public void requestScreenshot() {
		this.screenshotRequested = true;
	}

}
