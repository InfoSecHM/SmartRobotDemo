package filters.core.actions;


import filters.core.Domain;
import filters.core.State;

import java.util.ArrayList;
import java.util.List;

public abstract class Action {

	/**
	 * The name of the action that can uniquely identify it
	 */
	protected String name;

	/**
	 * The domain with which this action is associated
	 */
	protected Domain domain;

	/**
	 * An observer that will be notified of an actions results every time it is
	 * executed. By default no observer is specified.
	 */
	protected List<ActionObserver> actionObservers = new ArrayList<ActionObserver>();

	public Action() {
		// should not be called directly, but may be useful for subclasses of
		// Action
	}

	public Action(String name, Domain domain) {
		this.name = name;
		this.domain = domain;
		this.domain.addAction(this);
	}

	/**
	 * Returns the name of the action
	 *
	 * @return the name of the action
	 */
	public final String getName() {
		return name;
	}



	public State performAction(State s) {

		State resultState = s.copy();

		resultState = performActionHelper(resultState);

		for (ActionObserver observer : this.actionObservers) {
			observer.actionEvent(s, resultState, getName());
		}

		return resultState;

	}



	protected abstract State performActionHelper(State s);

	@Override
	public boolean equals(Object obj) {
		Action op = (Action) obj;
		if (op.name.equals(name))
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

}
