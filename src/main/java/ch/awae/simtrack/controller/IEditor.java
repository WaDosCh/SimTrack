package ch.awae.simtrack.controller;

public interface IEditor {

	/**
	 * loads a tool and unloads the current one. If the new tool cannot be
	 * loaded, the current one will not be unloaded and stays active.
	 * 
	 * @param name
	 *            the identifier string of the new tool
	 * @param args
	 *            additional arguments to hand over to the new tool
	 * @return {@code true} if the tool switch was successful
	 */
	public boolean loadTool(String name, Object[] args);
}
