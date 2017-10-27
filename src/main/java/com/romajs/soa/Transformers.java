package com.romajs.soa;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Transformers {

	static class TransformerKey<K, V> {

		private Class<K> keyClazz;
		private Class<V> valueClass;

		public TransformerKey(Class<K> keyClazz, Class<V> valueClass) {
			this.keyClazz = keyClazz;
			this.valueClass = valueClass;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			TransformerKey that = (TransformerKey) o;

			if (!keyClazz.equals(that.keyClazz)) return false;
			return valueClass.equals(that.valueClass);
		}

		@Override
		public int hashCode() {
			int result = keyClazz.hashCode();
			result = 31 * result + valueClass.hashCode();
			return result;
		}
	}

	static Map<TransformerKey, Function> transformers = new HashMap<>();

	public static <K, V> void register(Class<K> keyClazz, Class<V> valueClass, Function<K, V> function) {
		transformers.put(new TransformerKey(keyClazz,valueClass), function);
	}

	public static <K, V> V transform(K object, Class<V> valueClass) {
		if(object == null) {
			return null;
		}
		return (V) transformers.get(new TransformerKey(object.getClass(), valueClass)).apply(object);
	}
}
