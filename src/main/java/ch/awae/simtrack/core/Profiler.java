package ch.awae.simtrack.core;

import java.util.List;
import java.util.function.Supplier;

import lombok.Getter;

public final class Profiler {

	private final int sampleCount;
	private final StringSupplier[] rendererNames;
	private final StringSupplier[] tickerNames;
	private final long[] rendererSamples;
	private final long[] tickerSamples;
	private long lastFrameTime;
	private long frameActiveDuration;
	private long frameDuration;
	private long currentFrameStart;
	private int frame;

	private @Getter String digest = "no data yet";

	public static interface StringSupplier extends Supplier<String> {
	};

	public Profiler(int sampleCount, List<StringSupplier> tickers, List<StringSupplier> renderers) {
		this.sampleCount = sampleCount;
		this.rendererNames = new StringSupplier[renderers.size() + 1];
		this.tickerNames = new StringSupplier[tickers.size()];
		this.rendererSamples = new long[renderers.size() + 1];
		this.tickerSamples = new long[tickers.size()];
		for (int i = 0; i < renderers.size(); i++) {
			rendererNames[i + 1] = renderers.get(i);
		}
		for (int i = 0; i < tickers.size(); i++) {
			tickerNames[i] = tickers.get(i);
		}
		rendererNames[0] = () -> "Frame Buffer";
	}

	private void reset() {
		for (int i = 0; i < rendererSamples.length; i++)
			rendererSamples[i] = 0;
		for (int i = 0; i < tickerSamples.length; i++)
			tickerSamples[i] = 0;
		frameActiveDuration = 0;
		frameDuration = 0;
		frame = 0;
	}

	public void startFrame() {
		frame++;
		currentFrameStart = System.currentTimeMillis();
		frameDuration += currentFrameStart - lastFrameTime;
		lastFrameTime = currentFrameStart;
	}

	private boolean currentSampleRenderer;
	private long currentSampleStart;
	private int currentSampleIndex;

	public void startSample(boolean renderer, int index) {
		currentSampleRenderer = renderer;
		currentSampleStart = System.currentTimeMillis();
		currentSampleIndex = renderer ? index + 1 : index;
	}

	public void endSample() {
		long time = System.currentTimeMillis() - currentSampleStart;
		(currentSampleRenderer ? rendererSamples : tickerSamples)[currentSampleIndex] += time;
	}

	public void endFrame() {
		frameActiveDuration += System.currentTimeMillis() - currentFrameStart;
		if (frame == sampleCount) {
			digest();
			reset();
		}
	}

	private void digest() {
		StringBuilder sb = new StringBuilder();
		sb.append(1000 / (frameDuration / frame) + " FPS\n");
		sb.append((frameActiveDuration / frame) + "ms (" + ((100 * frameActiveDuration) / frameDuration) + "% load)\n");
		sb.append("\n");
		{ // tickers
			long sum = 0;
			for (long x : tickerSamples)
				sum += x;
			sb.append("Tickers: " + (sum / frame) + "ms\n");
			for (int i = 0; i < tickerSamples.length; i++) {
				sb.append(" - " + tickerNames[i].get() + ": " + (tickerSamples[i] / frame) + "ms\n");
			}
		}
		sb.append("\n");
		{ // renderers
			long sum = 0;
			for (long x : rendererSamples)
				sum += x;
			sb.append("Renderers: " + (sum / frame) + "ms\n");
			for (int i = 0; i < rendererSamples.length; i++) {
				sb.append(" - " + rendererNames[i].get() + ": " + (rendererSamples[i] / frame) + "ms\n");
			}
		}
		digest = sb.toString();
	}

}
