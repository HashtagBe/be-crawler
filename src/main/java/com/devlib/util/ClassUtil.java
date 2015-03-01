package com.devlib.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 工具类。提供一些反射、对Class对象进行操作的方法
 * 
 * @author wenjian.liang
 */
public class ClassUtil {

	/**
	 * 按字段名返回其“add方法”的名字
	 * 
	 * @param fieldName
	 * @return
	 */
	public static String toAdderName(final String fieldName) {
		return "add" + Character.toUpperCase(fieldName.charAt(0))
				+ fieldName.substring(1);
	}

	/**
	 * 按字段名返回其“set方法”的名字
	 * 
	 * @param fieldName
	 * @return
	 */
	public static String toSetterName(final String fieldName) {
		return "set" + Character.toUpperCase(fieldName.charAt(0))
				+ fieldName.substring(1);
	}

	/**
	 * 根据对象的Class和字段的Class获取这个字段的“add方法”
	 * 
	 * @param clazz
	 * @param fieldClass
	 * @return
	 */
	public static Method getAdder(final Class<?> clazz,
			final Class<?> fieldClass) {
		return findMethodIgnoreCaseAndArgsTypesAssigned(clazz.getMethods(),
				toAdderName(fieldClass.getSimpleName()), fieldClass);
	}

	/**
	 * 根据对象的Class和字段的Class获取这个字段的“set方法”，将忽略方法名字的大小写，并且返回的方法所使用的参数可能不是精确匹配：
	 * 这个参数的类型可能是指定字段类型的父类
	 * 
	 * @param clazz
	 * @param fieldClass
	 * @return
	 */
	public static Method getSetterIgnoreNameCaseAndTypeAssigned(
			final Class<?> clazz, final Class<?> fieldClass) {// ignore case and
																// type
		return findMethodIgnoreCaseAndArgsTypesAssigned(clazz.getMethods(),
				toSetterName(fieldClass.getSimpleName()), fieldClass);
	}

	/**
	 * 在一堆方法中寻找带有指定名字（忽略大小写）和参数类型列表的方法。方法参数类型并非精确匹配，
	 * 方法接收的某个可能是传入的参数类型数组中对应位置的类型的父类。
	 * 
	 * @param methods
	 * @param methodName
	 * @param argsClasses
	 * @return
	 */
	public static Method findMethodIgnoreCaseAndArgsTypesAssigned(
			final Method[] methods, final String methodName,
			final Class<?>... argsClasses) {
		Method method = null;
		for (final Method m : methods) {
			final String name = m.getName();
			final Class<?>[] argTypes = m.getParameterTypes();
			if (argsClasses.length != argTypes.length) {
				continue;
			}
			if (name.equalsIgnoreCase(methodName)) {
				if (Arrays.equals(argTypes, argsClasses)) {
					return m;
				}
				int i = 0;
				for (final Class<?> argType : argTypes) {
					if (argType.isAssignableFrom(argsClasses[i])) {
						++i;
					} else {
						break;
					}
				}
				if (i == argsClasses.length) {
					method = m;
				}
			}
		}
		return method;
	}

	/**
	 * 获取某个指定的单参数方法
	 * 
	 * @param clazz
	 * @param argClass
	 * @param methodName
	 */
	public static Method getSingleArgMethod(final Class<?> clazz,
			final Class<?> argClass, final String methodName) {
		try {
			return clazz.getMethod(methodName, argClass);
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
		} catch (final SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 反射出一个实例
	 * 
	 * @param className
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static Object newInstance(final String className)
			throws IllegalArgumentException {
		try {
			return Class.forName(className).newInstance();
		} catch (final InstantiationException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("class " + className
					+ " cannot be create", e);
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("class " + className
					+ " cannot be create", e);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("class " + className
					+ " cannot be create", e);
		}
	}

	/**
	 * 尝试把一个字段“放”到一个对象中去。如果该对象没有这个字段或者没有对应的方法，则什么也不会发生
	 * 
	 * @param object
	 * @param fieldValue
	 */
	public static void setField(final Object object, final Object fieldValue) {
		final Class<?> childClass = fieldValue.getClass();

		final Class<?> parentClass = object.getClass();
		final Method setter = ClassUtil.getSetterIgnoreNameCaseAndTypeAssigned(
				parentClass, childClass);
		if (setter != null) {
			putFieldToObject(setter, object, fieldValue);
		}

		final Method adder = ClassUtil.getAdder(parentClass, childClass);
		if (adder != null) {
			putFieldToObject(adder, object, fieldValue);
		}
	}

	/**
	 * 尝试把一个字段通过一个方法放到一个对象中去。
	 * 
	 * @param method
	 * @param object
	 * @param fieldValue
	 */
	public static void putFieldToObject(final Method method,
			final Object object, final Object fieldValue) {
		try {
			method.invoke(object, fieldValue);
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
