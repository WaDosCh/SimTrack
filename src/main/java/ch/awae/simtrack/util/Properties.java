package ch.awae.simtrack.util;

import java.awt.Color;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Properties {
	private final java.util.Properties props;

	public String getString(String id) {
		return props.getProperty(id);
	}

	public int getInt(String id) {
		return Integer.parseInt(getString(id));
	}

	public Color getColor(String id) {
		String v = getString(id);
		return Color.decode(v);
	}

}
