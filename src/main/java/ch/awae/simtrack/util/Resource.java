package ch.awae.simtrack.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.json.JsonObject;
import javax.json.spi.JsonProvider;

/**
 * Utility class for accessing resource files
 * 
 * @author Andreas Wälchli
 */
public final class Resource {

	private final static ClassLoader loader = Resource.class.getClassLoader();

	/**
	 * Provides a resource as an input stream
	 * 
	 * @param id
	 *            the resource name. may not be null
	 * @return the resource as an input stream
	 * @throws NullPointerException
	 *             the id is null
	 * @throws IllegalArgumentException
	 *             no resource exists for the given id
	 */
	public static InputStream asStream(final String id) {
		InputStream stream = loader.getResourceAsStream(id);
		if (stream == null)
			throw new IllegalArgumentException("resource not found: " + id);
		return stream;
	}

	/**
	 * Loads a JSON object from an resource
	 * 
	 * @param id
	 *            the resource name. may not be null
	 * @return the loaded JSON object
	 */
	public static JsonObject getJSON(final String id) {
		try (InputStream stream = asStream(id)) {
			return JsonProvider.provider().createReader(stream).readObject();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String[] getText(String id) {
		try (InputStream stream = asStream(id)) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			ArrayList<String> text = new ArrayList<>();
			reader.lines().forEach(text::add);
			return text.toArray(new String[text.size()]);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
