package util.parallel;


/**
 * Action class can be used to define a concurrent task that does not return any value after processing the element.
 * @param <E> Element processed within the action
 */
public abstract class Action<E> implements Function<E, Void> {

	/** This method is final and cannot be overridden. It applies the action implemented by {@link Action#doAction(Object)} */
	public final Void apply(E element) {
		doAction(element);
		return null;
	}

	/** Defines the action that will be applied over the element. Every action must implement this method */
	public abstract void doAction(E element);
}