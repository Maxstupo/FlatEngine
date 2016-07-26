package com.github.maxstupo.flatengine;

/**
 * 
 * @author Maxstupo
 *
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
