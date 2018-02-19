package ch.awae.simtrack.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReflectionHelper<T> {

	private static final Logger logger = LogManager.getLogger();

	private static final Class<?>[][] primitiveMapping = { { byte.class, Byte.class }, { short.class, Short.class },
			{ int.class, Integer.class }, { long.class, Long.class }, { float.class, Float.class },
			{ double.class, Double.class }, { boolean.class, Boolean.class }, { char.class, Character.class } };

	private Class<? extends T> clazz;
	private T instance;

	public ReflectionHelper(Class<T> clazz) {
		this.clazz = clazz;
		this.instance = null;
	}

	@SuppressWarnings("unchecked")
	public ReflectionHelper(T instance) {
		this.clazz = (Class<T>) instance.getClass();
		this.instance = instance;
	}

	public Method findCompatibleMethod(Class<? extends Annotation> annotation, String name, Class<?>[] params)
			throws NoSuchMethodException {
		Method[] methods = clazz.getMethods();

		logger.info("searching compatible method\n"
				+ ((annotation == null) ? "" : "Annotation: " + annotation.getSimpleName() + "\n")
				+ ((name == null) ? "" : "Name:       " + name + "\n") + Arrays.deepToString(params));

		Method match = null;
		loop:
		for (Method method : methods) {
			// FILTER BY NAME
			if (name != null) {
				if (!method.getName().equals(name))
					continue loop;
			}
			// FILTER BY ANNOTATION
			if (annotation != null) {
				if (!method.isAnnotationPresent(annotation))
					continue loop;
			}
			// CHECK PARAMETER LIST LENGTH
			if (method.getParameterCount() != params.length)
				continue;
			// CHECK PARAMETER COMPATIBILITY
			Class<?>[] declaredTypes = method.getParameterTypes();
			for (int i = 0; i < declaredTypes.length; i++) {
				if (!isCompatibleType(declaredTypes[i], params[i]))
					continue loop;
			}

			logger.info("found matching method: " + method);

			// HAVE A METHOD ALREADY?
			if (match != null)
				throw new IllegalArgumentException("match is not unique");

			// PASSED ALL CHECKS
			match = method;
		}

		if (match != null)
			return match;

		throw new NoSuchMethodException();
	}

	public DelayedReflectiveInvocation findAndPrepareCompatibleMethod(Class<? extends Annotation> annotation,
			String name, Object... params) throws NoSuchMethodException {
		Class<?>[] paramTypes = new Class<?>[params.length];
		for (int i = 0; i < paramTypes.length; i++) {
			Object param = params[i];
			paramTypes[i] = param == null ? null : param.getClass();
		}
		Method method = findCompatibleMethod(annotation, name, paramTypes);
		return new DelayedReflectiveInvocation(instance, method, params);
	}

	public Object findAndInvokeCompatibleMethod(Class<? extends Annotation> annotation, String name, Object... params)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		Class<?>[] paramTypes = new Class<?>[params.length];
		for (int i = 0; i < paramTypes.length; i++) {
			Object param = params[i];
			paramTypes[i] = param == null ? null : param.getClass();
		}
		Method method = findCompatibleMethod(annotation, name, paramTypes);
		return method.invoke(this.instance, params);
	}

	/**
	 * Checks if a runtime type is compatible with a declared type.
	 * 
	 * This check covers upcasting, boxing and unboxing. Primitive conversions
	 * (e.g. int to double are not covered)
	 * 
	 * @param declared
	 * @param runtime
	 * @return
	 */
	private boolean isCompatibleType(Class<?> declared, Class<?> runtime) {
		// equal types are fine
		if (declared.equals(runtime))
			return true;

		// null parameter applicable everywhere but primitives
		if (runtime == null)
			return !declared.isPrimitive();

		// runtime type boxed
		if (declared.isPrimitive()) {
			// search unboxing pair
			for (Class<?>[] pair : primitiveMapping) {
				if (declared.equals(pair[0]))
					return runtime.equals(pair[1]);
			}
			// no matching pair
			return false;
		}

		// runtime sub-types are fine
		return declared.isAssignableFrom(runtime);
	}

}
