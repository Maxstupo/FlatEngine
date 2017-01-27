package com.github.maxstupo.flatengine;

/**
 * This interface is used for generic events within the flat engine library. However it's mostly used with GUI objects.
 * 
 * @author Maxstupo
 * @param <E>
 *            the executor that invoked this listener.
 * @param <I>
 *            the object that resulted from the triggered action.
 * @param <A>
 *            the object that caused this event to trigger.
 */
public interface IEventListener<E, I, A> {

    /**
     * Invoked when an action occurs.
     * 
     * @param executor
     *            The object that invoked this method.
     * @param actionItem
     *            The given action from the executor.
     * @param action
     *            The action that triggered this event.
     * 
     * 
     */
    public void onEvent(E executor, I actionItem, A action);
}
