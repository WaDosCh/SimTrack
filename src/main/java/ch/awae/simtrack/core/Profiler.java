package ch.awae.simtrack.core;

import java.util.List;
import java.util.function.Supplier;

import lombok.Getter;

public final class Profiler {

	private final int sampleCount;
	private final StringSupplier[] renderer_names;
	private final StringSupplier[] ticker_names;
	private final long[] renderer_samples;
	private final long[] ticker_samples;
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
		this.renderer_names = new StringSupplier[renderers.size() + 1];
		this.ticker_names = new StringSupplier[tickers.size()];
		this.renderer_samples = new long[renderers.size() + 1];
		this.ticker_samples = new long[tickers.size()];
		for (int i = 0; i < renderers.size(); i++) {
			renderer_names[i + 1] = renderers.get(i);
		}
		for (int i = 0; i < tickers.size(); i++) {
			ticker_names[i] = tickers.get(i);
		}
		renderer_names[0] = () -> "Frame Buffer";
	}

	private void reset() {
		for (int i = 0; i < renderer_samples.length; i++)
			renderer_samples[i] = 0;
		for (int i = 0; i < ticker_samples.length; i++)
			ticker_samples[i] = 0;
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
		(currentSampleRenderer ? renderer_samples : ticker_samples)[currentSampleIndex] += time;
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
			for (long x : ticker_samples)
				sum += x;
			sb.append("Tickers: " + (sum / frame) + "ms\n");
			for (int i = 0; i < ticker_samples.length; i++) {
				sb.append(" - " + ticker_names[i].get() + ": " + (ticker_samples[i] / frame) + "ms\n");
			}
		}
		sb.append("\n");
		{ // renderers
			long sum = 0;
			for (long x : renderer_samples)
				sum += x;
			sb.append("Renderers: " + (sum / frame) + "ms\n");
			for (int i = 0; i < renderer_samples.length; i++) {
				sb.append(" - " + renderer_names[i].get() + ": " + (renderer_samples[i] / frame) + "ms\n");
			}
		}
		digest = sb.toString();
	}

}
