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
		// special case for colors with alpha value
		if (v.length() == 9) {
			int alpha = Integer.parseInt(v.substring(7), 16);
			v = v.substring(0, 7);
			Color c = Color.decode(v);
			return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
		}
		return Color.decode(v);
	}

}
