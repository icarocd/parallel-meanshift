package util.parallel;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Provides some useful methods to handle the execution of a collection of tasks.
 * @param <V> value of the task
 */
class TaskHandler<V> {
	private Collection<Future<V>> runningTasks = new LinkedList<Future<V>>();
	ExecutorService executorService;

	public TaskHandler(ExecutorService executor, Iterable<Callable<V>> tasks) {
		this.executorService = executor;
		for (Callable<V> task : tasks) {
			runningTasks.add(executor.submit(task));
		}
	}

	/**
	 * This function is equivalent to {@link ExecutorService#awaitTermination(long, TimeUnit)}
	 * @see ExecutorService#awaitTermination(long, TimeUnit)
	 */
	public boolean wait(long timeout, TimeUnit unit) throws InterruptedException {
		return this.executorService.awaitTermination(timeout, unit);
	}

	/**
	 * Retrieves the result of the transformation of each element (the value of each Future).
	 * This function blocks until all tasks are terminated.
	 * @return a collection with the results of the elements transformation
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public Collection<V> values() throws InterruptedException, ExecutionException {
		Collection<V> results = new LinkedList<V>();
		for (Future<V> future : this.runningTasks) {
			V result = future.get();
			if (result != null) {
				results.add(result);
			}
		}
		return results;
	}
}