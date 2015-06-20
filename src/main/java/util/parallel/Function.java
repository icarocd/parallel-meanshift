package util.parallel;

/**
 * First-order function interface
 * @param <E> Element to transform
 * @param <V> Result of the transformation
 */
public interface Function<E, V> {
	/**
	 * Apply a function over the element e.
	 */
	V apply(E e);
}