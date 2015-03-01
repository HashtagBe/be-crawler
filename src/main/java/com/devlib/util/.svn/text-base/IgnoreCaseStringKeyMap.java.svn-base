package com.devlib.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 一个以String对象为key的map，而且忽略其大小写。
 * 
 * @author wenjian.liang
 * @param <V>
 */
public class IgnoreCaseStringKeyMap<V> implements Map<String, V> {
	private final Map<String, V> map;

	public IgnoreCaseStringKeyMap() {
		map = new HashMap<String, V>();
	}

	public IgnoreCaseStringKeyMap(final Map<String, V> map) {
		super();
		this.map = map;
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean containsKey(final Object key) {
		if (!(key instanceof String)) {
			return false;
		}
		return map.containsKey(transformKey((String) key));
	}

	public boolean containsValue(final Object value) {
		return map.containsValue(value);
	}

	public V get(final Object key) {
		if (!(key instanceof String)) {
			return null;
		}
		return map.get(transformKey((String) key));
	}

	public V put(final String key, final V value) {
		return map.put(key, value);
	}

	public V remove(final Object key) {
		if (!(key instanceof String)) {
			return null;
		}
		return map.remove(transformKey((String) key));
	}

	public void putAll(final Map<? extends String, ? extends V> m) {
		for (final Entry<? extends String, ? extends V> e : m.entrySet()) {
			map.put(transformKey(e.getKey()), e.getValue());
		}
	}

	public void clear() {
		map.clear();
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	public Collection<V> values() {
		return map.values();
	}

	public Set<Entry<String, V>> entrySet() {
		return map.entrySet();
	}

	@Override
	public boolean equals(final Object o) {
		return map.equals(o);
	}

	@Override
	public int hashCode() {
		return map.hashCode();
	}

	private String transformKey(final String key) {
		return key.toLowerCase();
	}
}