package ch.awae.simtrack.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.cache.Cache;

import lombok.SneakyThrows;

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
		try (InputStream stream = asStream("config/" + id)) {
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

	private static Cache<String, BufferedImage> imageCache = CollectionUtil.softValueCache();

	@SneakyThrows(ExecutionException.class)
	public static BufferedImage getImage(String id) {
		return imageCache.get(id, () -> {
			logger.debug("loading image '" + id + "'");
			try (InputStream stream = asStream("graphics/" + id)) {
				return ImageIO.read(stream);
			}
		});
	}

	private static Cache<String, Properties> propertyCache = CollectionUtil.softValueCache();

	public static Properties getConfigProperties(String id) {
		return getProperties(id, "config/");
	}

	@SneakyThrows(ExecutionException.class)
	public static Properties getProperties(String id, String path) {
		return propertyCache.get(id, () -> {
			java.util.Properties props = new java.util.Properties();
			try (InputStream stream = asStream(path + id)) {
				props.load(stream);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			logger.debug("loading '" + id + "' with " + props.size() + " entries");
			for (Entry<Object, Object> entry : props.entrySet()) {
				logger.trace("  " + entry.getKey() + "\t= " + entry.getValue());
			}
			return new Properties(props);
		});
	}

}
