/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Mathieu BENOIT <mathieu-benoit@hotmail.fr>
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

package wilos.model.spem2.breakdownelement;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.element.Element;

/**
 * 
 * BreakdownElement is an abstract generalization for any type of
 * {@link Element} that is part of a breakdown structure. It defines a set of
 * properties available to all of its specializations.
 * <p />
 * It's an element of the SPEM2 specification of the OMG organization
 * (http://www.omg.org/).
 * 
 */
public class BreakdownElement extends Element implements Cloneable,
		Comparable<BreakdownElement> {

	private String presentationName;

	private String prefix;

	private Boolean isPlanned;

	private Boolean hasMultipleOccurrences;

	private Boolean isOptional;

	private Set<Activity> superActivities;

	private Set<ConcreteBreakdownElement> concreteBreakdownElements;

	/**
	 * Constructor.
	 * 
	 */
	public BreakdownElement() {
		super();
		this.presentationName = "";
		this.prefix = "";
		this.isOptional = false;
		this.isPlanned = true;
		this.hasMultipleOccurrences = false;
		this.superActivities = new HashSet<Activity>();
		this.concreteBreakdownElements = new HashSet<ConcreteBreakdownElement>();
	}

	/**
	 * Returns a copy of the current instance of BreakdownElement
	 * 
	 * @return a copy of the BreakdownElement
	 * @throws CloneNotSupportedException
	 */
	@Override
	public BreakdownElement clone() throws CloneNotSupportedException {
		BreakdownElement breakdownElement = new BreakdownElement();
		breakdownElement.copy(this);
		return breakdownElement;
	}

	/**
	 * Copy the values of the specified BreakdownElement into the current
	 * instance of the class.
	 * 
	 * @param _breakdownElement
	 *            The BreakdownElement to copy.
	 */
	protected void copy(final BreakdownElement _breakdownElement) {
		super.copy(_breakdownElement);
		this.presentationName = _breakdownElement.presentationName;
		this.prefix = _breakdownElement.prefix;
		this.hasMultipleOccurrences = _breakdownElement.hasMultipleOccurrences;
		this.isPlanned = _breakdownElement.isPlanned;
		this.isOptional = _breakdownElement.isOptional;
		this.concreteBreakdownElements.addAll(_breakdownElement
				.getConcreteBreakdownElements());
		this.superActivities.addAll(_breakdownElement.getSuperActivities());
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the BreakdownElement.
	 * 
	 * @param obj
	 *            the Object to be compare to the BreakdownElement
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof BreakdownElement == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		BreakdownElement breakdownElement = (BreakdownElement) obj;
		return new EqualsBuilder().appendSuper(super.equals(breakdownElement))
				.append(this.presentationName,
						breakdownElement.presentationName).append(this.prefix,
						breakdownElement.prefix).append(this.isPlanned,
						breakdownElement.isPlanned).append(
						this.hasMultipleOccurrences,
						breakdownElement.hasMultipleOccurrences).append(
						this.isOptional, breakdownElement.isOptional).append(
						this.concreteBreakdownElements,
						breakdownElement.concreteBreakdownElements).append(
						this.superActivities, breakdownElement.superActivities)
				.isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of BreakdownElement
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(this.presentationName).append(this.prefix).append(
						this.hasMultipleOccurrences).append(this.isOptional)
				.append(this.isPlanned).toHashCode();
	}

	/*
	 * relation between BreakdownElement and ConcreteBreakdownElement.
	 * 
	 */

	/**
	 * Adds a relation between the current instance of BreakdownElement
	 * and a specified ConcreteBreakdownElement.
	 * 
	 * @param _concreteBreakdownElement the ConcreteBreakdownElement to link
	 */
	public void addConcreteBreakdownElement(
			ConcreteBreakdownElement _concreteBreakdownElement) {
		this.concreteBreakdownElements.add(_concreteBreakdownElement);
		_concreteBreakdownElement.addBreakdownElement(this);
	}

	/**
	 * Removes the relation between the current instance of BreakdownElement
	 * and a specified ConcreteBreakdownElement.
	 * 
	 * @param _concreteBreakdownElement the ConcreteBreakdownElement to unlink
	 */
	public void removeConcreteBreakdownElement(
			ConcreteBreakdownElement _concreteBreakdownElement) {
		_concreteBreakdownElement.removeBreakdownElement(this);
		this.concreteBreakdownElements.remove(_concreteBreakdownElement);
	}

	/**
	 * Adds a relation between the current instance of BreakdownElement
	 * and all the specified ConcreteBreakdownElement.
	 * 
	 * @param _concreteBreakdownElements the Collection of ConcreteBreakdownElement to link
	 */
	public void addAllConcreteBreakdownElements(
			Set<ConcreteBreakdownElement> _concreteBreakdownElements) {
		for (ConcreteBreakdownElement cbde : _concreteBreakdownElements) {
			cbde.addBreakdownElement(this);
		}
	}

	/**
	 * Remove the relation between the current instance of BreakdownElement
	 * and all the related ConcreteBreakdownElement.
	 */
	public void removeAllConcreteBreakdownElements() {
		for (ConcreteBreakdownElement cbde : this
				.getConcreteBreakdownElements())
			cbde.setBreakdownElement(null);
		this.getConcreteBreakdownElements().clear();
	}

	/**
	 * Add an Activity to the activities collection of a BreakdownElement.
	 * 
	 * @param _superActivity
	 *            The activity to add
	 */
	public void addSuperActivity(Activity _superActivity) {
		this.getSuperActivities().add(_superActivity);
		_superActivity.getBreakdownElements().add(this);
	}

	/**
	 * Add an activity collection to the activity collection of a
	 * breakdownelement.
	 * 
	 * @param _superActivities
	 *            The set of Activity to add.
	 */
	public void addAllSuperActivities(Set<Activity> _superActivities) {
		for (Activity activity : _superActivities) {
			activity.addBreakdownElement(this);
		}
	}

	/**
	 * Remove from a breakdownelement one of these activities.
	 * 
	 * @param _superActivity
	 *            The Activity to remove.
	 */
	public void removeSuperActivity(Activity _superActivity) {
		_superActivity.getBreakdownElements().remove(this);
		this.getSuperActivities().remove(_superActivity);
	}

	/**
	 * Remove from a breakdownelement all its activities.
	 * 
	 */
	public void removeAllSuperActivities() {
		for (Activity activity : this.getSuperActivities())
			activity.getBreakdownElements().remove(this);
		this.getSuperActivities().clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(BreakdownElement _arg0) {
		if (_arg0 != null) {
			return (this.getInsertionOrder() - _arg0.getInsertionOrder());
		} else
			return 0;
	}

	/**
	 * Getter of presentationName.
	 * 
	 * @return the presentationName
	 */
	public String getPresentationName() {
		return this.presentationName;
	}

	/**
	 * Setter of presentationName.
	 * 
	 * @param _presentationName
	 *            The presentationName to set.
	 */
	public void setPresentationName(String _presentationName) {
		this.presentationName = _presentationName;
	}

	/**
	 * Getter of hasMultipleOccurrences.
	 * 
	 * @return the hasMultipleOccurrences.
	 */
	public Boolean getHasMultipleOccurrences() {
		return this.hasMultipleOccurrences;
	}

	/**
	 * Setter of hasMultipleOccurrences.
	 * 
	 * @param _hasMultipleOccurrences
	 *            The hasMultipleOccurrences to set.
	 */
	public void setHasMultipleOccurrences(Boolean _hasMultipleOccurrences) {
		this.hasMultipleOccurrences = _hasMultipleOccurrences;
	}

	/**
	 * Getter of isOptional.
	 * 
	 * @return the isOptional.
	 */
	public Boolean getIsOptional() {
		return this.isOptional;
	}

	/**
	 * Setter of isOptional.
	 * 
	 * @param _isOptional
	 *            The isOptional to set.
	 */
	public void setIsOptional(Boolean _isOptional) {
		this.isOptional = _isOptional;
	}

	/**
	 * Getter of isPlanned.
	 * 
	 * @return the isPlanned.
	 */
	public Boolean getIsPlanned() {
		return this.isPlanned;
	}

	/**
	 * Setter of isPlanned.
	 * 
	 * @param _isPlanned
	 *            The isPlanned to set.
	 */
	public void setIsPlanned(Boolean _isPlanned) {
		this.isPlanned = _isPlanned;
	}

	/**
	 * Getter of prefix.
	 * 
	 * @return the prefix.
	 */
	public String getPrefix() {
		return this.prefix;
	}

	/**
	 * Setter of prefix.
	 * 
	 * @param _prefix
	 *            The prefix to set.
	 */
	public void setPrefix(String _prefix) {
		this.prefix = _prefix;
	}

	/**
	 * Getter of activities.
	 * 
	 * @return the activities.
	 */
	public Set<Activity> getSuperActivities() {
		return this.superActivities;
	}

	/**
	 * Setter of activities.
	 * 
	 * @param _superActivities
	 *            The activities to set.
	 */
	@SuppressWarnings("unused")
	public void setSuperActivities(Set<Activity> _superActivities) {
		this.superActivities = _superActivities;
	}

	/**
	 * Getter of concreteBreakdownElements.
	 * 
	 * @return the concreteBreakdownElements.
	 */
	public Set<ConcreteBreakdownElement> getConcreteBreakdownElements() {
		return this.concreteBreakdownElements;
	}

	/**
	 * Setter of concreteBreakdownElements.
	 * 
	 * @param _superActivities
	 *            The activities to set.
	 */
	@SuppressWarnings("unused")
	private void setConcreteBreakdownElements(
			Set<ConcreteBreakdownElement> _concreteBreakdownElements) {
		this.concreteBreakdownElements = _concreteBreakdownElements;
	}
}
