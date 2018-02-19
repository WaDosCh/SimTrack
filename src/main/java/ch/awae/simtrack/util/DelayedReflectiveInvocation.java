package ch.awae.simtrack.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DelayedReflectiveInvocation {

	private Object instance;
	private Method method;
	private Object[] parameters;

	public Object invoke() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return method.invoke(instance, parameters);
	}

}
