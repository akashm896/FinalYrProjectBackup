/*
 * 
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Mathieu BENOIT <mathieu-benoit@hotmail.fr>
 * Copyright (C) 2007-2008 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not,
 * write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA. 
 */

package wilos.model.misc.concreteactivity;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.project.Project;
import wilos.model.spem2.activity.Activity;

/**
 * 
 * A ConcreteActivity is a specific {@link Activity} for a {@link Project}.
 * 
 */
public class ConcreteActivity extends ConcreteWorkBreakdownElement implements
		Cloneable {

	private SortedSet<ConcreteBreakdownElement> concreteBreakdownElements;

	private Activity activity;

	/**
	 * Class constructor.
	 * 
	 */
	public ConcreteActivity() {
		super();
		this.concreteBreakdownElements = new TreeSet<ConcreteBreakdownElement>();
	}

	/**
	 * Compares an object in parameter with the current instance of the class
	 * and returns true if both those objects are equals.
	 * 
	 * @param obj
	 *            the object to be compare with the current instance of
	 *            ConcreteActivity
	 * @return true if the object is the same or has the same values as the
	 *         current instance of ConcreteActivity, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof ConcreteActivity == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		ConcreteActivity concreteActivity = (ConcreteActivity) obj;
		return new EqualsBuilder().appendSuper(super.equals(concreteActivity))
				.append(this.activity, concreteActivity.activity).append(
						this.concreteBreakdownElements,
						concreteActivity.concreteBreakdownElements).isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of ConcreteActivity
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(this.activity).toHashCode();
	}

	/**
	 * Returns a copy of the current instance of ConcreteActivity.
	 * 
	 * @return a copy of the ConcreteActivity
	 * @throws CloneNotSupportedException
	 */
	public ConcreteActivity clone() throws CloneNotSupportedException {
		ConcreteActivity concreteActivity = new ConcreteActivity();
		concreteActivity.copy(this);
		return concreteActivity;
	}

	/**
	 * Copy the values of the specified ConcreteActivity into the current
	 * instance of the class.
	 * 
	 * @param _concreteActivity
	 *            the concreteActivity to copy.
	 */
	protected void copy(final ConcreteActivity _concreteActivity) {
		super.copy(_concreteActivity);
		this.activity = _concreteActivity.getActivity();
		this.concreteBreakdownElements.addAll(_concreteActivity
				.getConcreteBreakdownElements());
	}

	/*
	 * Relation between ConcreteActivity and ConcreteBreakdownElement
	 */

	/**
	 * Adds a ConcreteBreakdownElement to the Collection of the current
	 * instance. Defines the relation between ConcreteActivity and
	 * ConcreteBreakdownElement.
	 * 
	 * @param _concreteBeakdownElement
	 *            the concreteBreakdownElement to be add
	 */

	public void addConcreteBreakdownElement(
			ConcreteBreakdownElement _concreteBeakdownElement) {
		this.getConcreteBreakdownElements().add(_concreteBeakdownElement);
		_concreteBeakdownElement.getSuperConcreteActivities().add(this);
	}

	/**
	 * Adds a Set of ConcreteBreakdownElement to the current instance. Defines
	 * for them all the relation between ConcreteActivity and
	 * ConcreteBreakdownElement.
	 * 
	 * @param _concreteBreakdownElements
	 *            the Set of ConcreteBreakdownElement to add
	 */
	public void addAllConcreteBreakdownElements(
			Set<ConcreteBreakdownElement> _concreteBreakdownElements) {
		for (ConcreteBreakdownElement cbde : _concreteBreakdownElements) {
			cbde.addSuperConcreteActivity(this);
		}
	}

	/**
	 * Removes from the instance Collection a specified
	 * ConcreteBreakdownElement. Deletes the link between the ConcreteActivity
	 * and the ConcreteBreakdownElement.
	 * 
	 * @param _concreteBreakdownElement
	 *            the ConcreteBreakdownElement to be remove
	 */
	public void removeConcreteBreakdownElement(
			ConcreteBreakdownElement _concreteBreakdownElement) {
		_concreteBreakdownElement.getSuperConcreteActivities().remove(this);
		this.getConcreteBreakdownElements().remove(_concreteBreakdownElement);
	}

	/**
	 * Removes all the ConcreteBreakdownElement from the Collection of the
	 * current instance.
	 */
	public void removeAllConcreteBreakdownElements() {
		for (ConcreteBreakdownElement bde : this.getConcreteBreakdownElements())
			bde.getSuperConcreteActivities().remove(this);
		this.getConcreteBreakdownElements().clear();
	}

	/*
	 * Relation between ConcreteActivity and Activity
	 * 
	 */

	/**
	 * Specifies the Activity related to the current ConcreteActivity. Adds that
	 * ConcreteActivity to the Activity's Collection.
	 * 
	 * @param _activity
	 *            the Activity related to the current ConcreteActivity
	 */
	public void addActivity(Activity _activity) {
		this.activity = _activity;
		_activity.addConcreteActivity(this);
	}

	/**
	 * Remove the relation between the specified Activity and the
	 * ConcreteActivity.
	 * 
	 * @param _activity
	 *            the activity to be unlinked with the ConcreteActivity
	 */
	public void removeActivity(Activity _activity) {
		this.activity = null;
		_activity.removeConcreteActivity(this);
	}

	/*
	 * Getter & Setter.
	 * 
	 */

	/**
	 * Returns a SortedSet of ConcreteBreakdownElement related to the
	 * ConcreteActivity.
	 * 
	 * @return the SortedSet of ConcreteBreakdownElement
	 */
	public SortedSet<ConcreteBreakdownElement> getConcreteBreakdownElements() {
		return concreteBreakdownElements;
	}

	/**
	 * Initialize the ConcreteActivity's Collection of ConcreteBreakdownElement
	 * with the specified SortedSet.
	 * 
	 * @param concreteBreakdownElements
	 *            the SortedSet to be assigned to the ConcreteActivity
	 */
	public void setConcreteBreakdownElements(
			SortedSet<ConcreteBreakdownElement> concreteBreakdownElements) {
		this.concreteBreakdownElements = concreteBreakdownElements;
	}

	/**
	 * Returns the Activity that is in relation with the ConcreteActivity.
	 * 
	 * @return the Activity related to the ConcreteActivity
	 */
	public Activity getActivity() {
		return activity;
	}

	/**
	 * Initialize the ConcreteActivity's Activity with the specified one.
	 * 
	 * @param activity
	 *            the Activity to be assigned to the ConcreteActivity
	 */
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
}
