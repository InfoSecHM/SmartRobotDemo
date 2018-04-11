package filters.core.actions;


import filters.core.State;

public interface ActionObserver {

	/**
	 * 
	 * @param oldState
	 * @param newState
	 * @param actionName TODO
	 */
	public void actionEvent(State oldState, State newState, String actionName);
}
