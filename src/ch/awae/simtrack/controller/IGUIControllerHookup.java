package ch.awae.simtrack.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.function.Consumer;

public interface IGUIControllerHookup {

	public Consumer<MouseAdapter> getMouseHookup();

	public Consumer<KeyAdapter> getKeyboardHookup();

}
