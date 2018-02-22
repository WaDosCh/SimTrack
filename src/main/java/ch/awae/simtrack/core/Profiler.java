package ch.awae.simtrack.core;

import java.util.List;

import lombok.Getter;

public final class Profiler {

	private final int sampleCount;
	private final String[] renderer_names;
	private final String[] ticker_names;
	private final long[] renderer_samples;
	private final long[] ticker_samples;
	private long lastFrameTime;
	private long frameActiveDuration;
	private long frameDuration;
	private long currentFrameStart;
	private int frame;

	private @Getter String digest = "no data yet";

	public Profiler(int sampleCount, List<String> tickers, List<String> renderers) {
		this.sampleCount = sampleCount;
		this.renderer_names = new String[renderers.size()];
		this.ticker_names = new String[tickers.size()];
		this.renderer_samples = new long[renderers.size()];
		this.ticker_samples = new long[renderers.size()];
		for (int i = 0; i < renderers.size(); i++) {
			renderer_names[i] = renderers.get(i);
		}
		for (int i = 0; i < tickers.size(); i++) {
			ticker_names[i] = renderers.get(i);
		}
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
		currentSampleIndex = index;
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
		sb.append((frameDuration / frame) + " FPS\n");
		sb.append(((100 * frameActiveDuration) / frameDuration) + "% load\n");
		sb.append("\n");
		{ // tickers
			long sum = 0;
			for (long x : ticker_samples)
				sum += x;
			sb.append("Tickers: " + (sum / frame) + "ms\n");
			for (int i = 0; i < ticker_samples.length; i++) {
				sb.append(" - " + ticker_names[i] + ": " + (ticker_samples[i] / frame) + "ms\n");
			}
		}
		{ // renderers
			long sum = 0;
			for (long x : renderer_samples)
				sum += x;
			sb.append("Renderers: " + (sum / frame) + "ms\n");
			for (int i = 0; i < renderer_samples.length; i++) {
				sb.append(" - " + renderer_names[i] + ": " + (renderer_samples[i] / frame) + "ms\n");
			}
		}

	}

}
