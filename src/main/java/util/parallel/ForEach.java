package util.parallel;

import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * Class to generate a parallelized version of the for each loop.
 * @param <E> elements to iterate over.
 * @param <V> processed element type result.
 */
class ForEach<E, V> implements Function<Function<E, V>, TaskHandler<V>> {
	// Source elements
	private Iterable<E> elements;

	// Executor used to invoke concurrent tasks
	// By default it uses as many threads as processors available
	private final ExecutorService executor;

	public ForEach(Iterable<E> elements, ExecutorService executorService) {
		this.elements = elements;
		this.executor = executorService;
	}

	/**
	 * Encapsulates the ForEach instance into a Callable that retrieves a TaskHandler with the invoked tasks. Example:
	 * <pre>
	 *     {@code Collection<Double> numbers = new Collection<Double>(...);
	 *       Callable<TaskHandler<V>> forEach = new ForEach<Double, String>(numbers)
	 *              .prepare(new F<Double, String>() {
	 *                  String apply(Double e) {
	 *                      return e.toString();
	 *                  }
	 *              });
	 *     forEach.call().values();
	 *     }
	 * </pre>
	 * @param f
	 * @return
	 */
	public Callable<TaskHandler<V>> prepare(final Function<E, V> f) {
		return new Callable<TaskHandler<V>>() {
			public TaskHandler<V> call() {
				return apply(f);
			}
		};
	}

	public TaskHandler<V> apply(Function<E, V> f) {
		return new TaskHandler<V>(executor, map(elements, f));
	}

	private Iterable<Callable<V>> map(final Iterable<E> elements, final Function<E, V> f) {
		return new Iterable<Callable<V>>() {
			public Iterator<Callable<V>> iterator() {
				return new Iterator<Callable<V>>() {
					Iterator<E> it = elements.iterator();
					public boolean hasNext() {
						return it.hasNext();
					}
					public Callable<V> next() {
						final E e = it.next();
						return new Callable<V>() {
							public V call() throws Exception {
								return f.apply(e);
							}
						};
					}
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}
}