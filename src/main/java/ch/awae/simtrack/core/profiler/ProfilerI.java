package ch.awae.simtrack.core.profiler;

import ch.awae.simtrack.core.BaseRenderer;
import ch.awae.simtrack.core.NamedComponent;
import ch.awae.simtrack.core.input.InputHandler;

public interface ProfilerI extends BaseRenderer, InputHandler {

	public void startSample(NamedComponent controller);
	
	public void endSample(NamedComponent controller);
	
	public void startFrame();
	
	public void endFrame();
	
	/**
	 * @return return the data in string representation of the last sample period
	 */
	public String[] getProfilerOutput();
}
