package ch.awae.simtrack.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for accessing resource files
 * 
 * @author Andreas Wälchli
 */
public final class Resource {

	private final static ClassLoader loader = Resource.class.getClassLoader();

	private final static Logger logger = LogManager.getLogger(Resource.class);

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

	public static BufferedImage getImage(String id) {
		try (InputStream stream = asStream(id)) {
			return ImageIO.read(stream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static HashMap<String, Properties> propertyCache = new HashMap<>();

	public static Properties getProperties(String id) {
		if (propertyCache.containsKey(id)) {
			logger.info("loading '" + id + "' from cache");
			return propertyCache.get(id);
		} else {
			java.util.Properties props = new java.util.Properties();
			try (InputStream stream = asStream(id)) {
				props.load(stream);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			logger.info("loading '" + id + "' with " + props.size() + " entries:");
			for (Entry<Object, Object> entry : props.entrySet()) {
				logger.info("  " + entry.getKey() + "\t= " + entry.getValue());
			}
			Properties p = new Properties(props);
			propertyCache.put(id, p);
			return p;
		}
	}

}
