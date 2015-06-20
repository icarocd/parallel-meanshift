package util.parallel;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import util.MathUtils;

/**
 * <p>
 * This class contains some useful classes and methods to parallelize code in an easy and fluent way. Parallel class is
 * self-contained to enable an easier integration and reuse in different java projects. Simple example:
 * </p>
 *
 * <pre>
 *     {@code Parallel.ForEach(elements, new Parallel.Action<String>() {
 *              public void doAction(String s) {
 *                  System.out.println("Processing element " + s + " on thread " + Thread.currentThread().getName());
 *              }
 *            });
 *     }
 * </pre>
 * <p>
 * The ForEach function can be used as a parallel map function to transform a collection of elements in parallel. For example,
 * suppose we want to convert a list of words to upper case:
 * </p>
 *
 * <pre>
 * {
 * 	&#064;code
 * 	Collection&lt;String&gt; upperCaseWords = new ForEach&lt;Integer, String&gt;(elements).apply(new Function&lt;String&gt;() {
 * 		public Integer apply(String element) {
 * 			return element.toUpperCase();
 * 		}
 * 	}).values();
 * }
 * </pre>
 */
public final class Parallel {

	private Parallel() {
		throw new RuntimeException("Use Parallel static methods");
	}

	public static <A, V> Collection<V> ForEach(Iterable<A> elements, Function<A, V> task) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		Collection<V> values = ForEach(elements, executorService, task);
		executorService.shutdown();
		return values;
	}

	public static <A, V> Collection<V> ForEach(Iterable<A> elements, int threads, Function<A, V> task) {
		ExecutorService executorService = Executors.newFixedThreadPool(threads);
		Collection<V> values = ForEach(elements, executorService, task);
		executorService.shutdown();
		return values;
	}

	public static <A, V> Collection<V> ForEach(Iterable<A> elements, ExecutorService executorService, Function<A, V> task) {
		try {
			TaskHandler<V> loop = new ForEach<A, V>(elements, executorService).apply(task);
			return loop.values();
		} catch (Exception e) {
			throw new RuntimeException("ForEach method exception. " + e.getMessage());
		}
	}

	public static void For(final int from, final int to, final Action<Integer> action) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		For(from, to, executorService, action);
		executorService.shutdown();
	}

	public static void For(final int from, final int to, ExecutorService executorService, final Action<Integer> action) {
		ForEach(MathUtils.rangeIterable(from, to), executorService, action);
	}

	public static void For(final long from, final long to, final Action<Long> action) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		For(from, to, executorService, action);
		executorService.shutdown();
	}

	public static void For(final long from, final long to, ExecutorService executorService, final Action<Long> action) {
		ForEach(MathUtils.rangeIterable(from, to), executorService, action);
	}
}
