/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Sebastien BALARD <sbalard@wilos-project.org>
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

package wilos.model.misc.concreteworkbreakdownelement;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.project.Project;
import wilos.model.spem2.workbreakdownelement.WorkBreakdownElement;
import wilos.utils.Constantes.State;

/**
 * 
 * A ConcreteWorkBreakdownElement is a specific {@link WorkBreakdownElement} for
 * a {@link Project}.
 * 
 */
public class ConcreteWorkBreakdownElement extends ConcreteBreakdownElement
		implements Cloneable {

	private boolean isInUsed;

	private String state;

	private Date plannedStartingDate;

	private Date plannedFinishingDate;

	private Date plannedUserFinishingDate;

	private float plannedUserTime;

	private float plannedTime;

	private WorkBreakdownElement workBreakdownElement;

	private Set<ConcreteWorkOrder> concretePredecessors;

	private Set<ConcreteWorkOrder> concreteSuccessors;

	/**
	 * Class constructor
	 */
	public ConcreteWorkBreakdownElement() {
		super();
		this.isInUsed = true;
		this.state = State.CREATED;
		this.plannedFinishingDate = null;
		this.plannedStartingDate = null;
		this.plannedTime = 0.0f;
		this.plannedUserFinishingDate = null;
		this.plannedUserTime = 0.0f;
		this.concretePredecessors = new HashSet<ConcreteWorkOrder>();
		this.concreteSuccessors = new HashSet<ConcreteWorkOrder>();
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the class.
	 * 
	 * @param obj
	 *            the Object to be compare to the ConcreteWorkBreakdownElement
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof ConcreteWorkBreakdownElement == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		ConcreteWorkBreakdownElement concreteworkBreakdownElement = (ConcreteWorkBreakdownElement) obj;
		return new EqualsBuilder().appendSuper(
				super.equals(concreteworkBreakdownElement)).append(
				this.isInUsed, concreteworkBreakdownElement.isInUsed).append(
				this.state, concreteworkBreakdownElement.state).append(
				this.plannedFinishingDate,
				concreteworkBreakdownElement.plannedFinishingDate).append(
				this.plannedStartingDate,
				concreteworkBreakdownElement.plannedStartingDate).append(
				this.plannedTime, concreteworkBreakdownElement.plannedTime)
				.append(this.concretePredecessors,
						concreteworkBreakdownElement.concretePredecessors)
				.append(this.concreteSuccessors,
						concreteworkBreakdownElement.concreteSuccessors)
				.append(this.workBreakdownElement,
						concreteworkBreakdownElement.workBreakdownElement)
				.append(this.plannedUserFinishingDate,
						concreteworkBreakdownElement.plannedUserFinishingDate)
				.append(this.plannedUserTime,
						concreteworkBreakdownElement.plannedUserTime)
				.isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of
	 *         ConcreteWorkBreakdownElement
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(this.isInUsed).append(this.state).append(
						this.plannedFinishingDate).append(
						this.plannedStartingDate).append(this.plannedTime)
				.append(this.plannedUserFinishingDate).append(
						this.plannedUserTime).append(this.workBreakdownElement)
				.toHashCode();
	}

	/**
	 * Returns a copy of the current instance of ConcreteWorkBreakdownElement
	 * 
	 * @return a copy of the ConcreteWorkBreakdownElement
	 * @throws CloneNotSupportedException
	 */
	@Override
	public ConcreteWorkBreakdownElement clone()
			throws CloneNotSupportedException {
		ConcreteWorkBreakdownElement concreteworkBreakdownElement = new ConcreteWorkBreakdownElement();
		concreteworkBreakdownElement.copy(this);
		return concreteworkBreakdownElement;
	}

	/**
	 * Copy the values of the specified ConcreteWorkBreakdownElement into the
	 * current instance of the class.
	 * 
	 * @param _concreteWorkBreakdownElement
	 *            the ConcreteWorkBreakdownElement to copy
	 */
	protected void copy(
			final ConcreteWorkBreakdownElement _concreteWorkBreakdownElement) {
		super.copy(_concreteWorkBreakdownElement);
		this.isInUsed = _concreteWorkBreakdownElement.getIsInUsed();
		this.state = _concreteWorkBreakdownElement.getState();
		this.workBreakdownElement = _concreteWorkBreakdownElement
				.getWorkBreakdownElement();
		this.plannedFinishingDate = _concreteWorkBreakdownElement
				.getPlannedFinishingDate();
		this.plannedStartingDate = _concreteWorkBreakdownElement
				.getPlannedStartingDate();
		this.plannedUserFinishingDate = _concreteWorkBreakdownElement
				.getPlannedUserFinishingDate();
		this.plannedUserTime = _concreteWorkBreakdownElement
				.getPlannedUserTime();
		this.plannedTime = _concreteWorkBreakdownElement.getPlannedTime();
		this.concretePredecessors.addAll(_concreteWorkBreakdownElement
				.getConcretePredecessors());
		this.concreteSuccessors.addAll(_concreteWorkBreakdownElement
				.getConcreteSuccessors());
	}

	/*
	 * Relation between WorkBreakdownElement and ConcreteWorkBreakdownElement.
	 * 
	 */

	/**
	 * Adds a relation between the ConcreteWorkBreakdownElement and the
	 * specified WorkBreakdownElement.
	 * 
	 * @param _workbreakdownElement
	 *            the WorkBreakdownElement to relate to the
	 *            ConcreteWorkBreakdownElement
	 */
	public void addWorkBreakdownElement(
			WorkBreakdownElement _workbreakdownElement) {
		this.setWorkBreakdownElement(_workbreakdownElement);
		_workbreakdownElement.getConcreteWorkBreakdownElements().add(this);
	}

	/**
	 * Removes the relation between the ConcreteWorkBreakdownElement and the
	 * specified WorkBreakdownElement.
	 * 
	 * @param _workbreakdownElement
	 *            the WorkBreakdownElement to unlink with the
	 *            ConcreteWorkBreakdownElement
	 */
	public void removeWorkBreakdownElement(
			WorkBreakdownElement _workbreakdownElement) {
		_workbreakdownElement.getConcreteWorkBreakdownElements().remove(this);
		this.workBreakdownElement = null;
	}

	/*
	 * Relation between ConcreteWorkBreakdownElement and ConcreteWorkOrder.
	 * 
	 */

	/**
	 * Add a concreteSuccesor to the concreteSuccessors collection of a
	 * concreteWorkOrder
	 * 
	 * @param _concreteWorkOrder
	 */
	public void addConcreteSuccessor(ConcreteWorkOrder _concreteWorkOrder) {
		this.concreteSuccessors.add(_concreteWorkOrder);
		_concreteWorkOrder.setConcretePredecessor(this);
	}

	/**
	 * Remove from a ConcreteWbde one of these concreteSuccessor.
	 * 
	 * @param _concreteWorkOrder
	 *            The concreteSuccessor to remove.
	 */
	public void removeConcreteSuccessor(ConcreteWorkOrder _concreteWorkOrder) {
		_concreteWorkOrder.setConcretePredecessor(null);
		this.concreteSuccessors.remove(_concreteWorkOrder);
	}

	/**
	 * Add a concreteSuccessors collection into the ConcreteWorkOrder successors
	 * collection.
	 * 
	 * @param _concreteWorkOrder
	 *            The set of concreteSuccessors to add.
	 */
	public void addAllConcreteSuccessors(
			Set<ConcreteWorkOrder> _concreteWorkOrder) {
		for (ConcreteWorkOrder cwo : _concreteWorkOrder) {
			this.addConcreteSuccessor(cwo);
		}
	}

	/**
	 * Remove from a ConcreteWbde all its ConcreteWorkOrder successors.
	 * 
	 */
	public void removeAllConcreteSuccessors() {
		for (ConcreteWorkOrder cwo : this.getConcreteSuccessors())
			cwo.setConcretePredecessor(null);
		this.getConcreteSuccessors().clear();
	}

	/**
	 * Add a concretePredecessor into the ConcreteWorkOrder predecessors
	 * collection.
	 * 
	 * @param _concreteWorkOrder
	 *            The concretePredecessor to add.
	 */
	public void addConcretePredecessor(ConcreteWorkOrder _concreteWorkOrder) {
		this.concretePredecessors.add(_concreteWorkOrder);
		_concreteWorkOrder.setConcreteSuccessor(this);
	}

	/**
	 * Remove from a ConcreteWbde one of these concretePredecessor.
	 * 
	 * @param _concreteWorkOrder
	 *            The concretePredecessor to remove.
	 */
	public void removeConcretePredecessor(ConcreteWorkOrder _concreteWorkOrder) {
		_concreteWorkOrder.setConcreteSuccessor(null);
		this.concretePredecessors.remove(_concreteWorkOrder);
	}

	/**
	 * Add a concretePredecessor collection into the ConcreteWorkOrder
	 * predecessors collection.
	 * 
	 * @param _concreteWorkOrder
	 *            The set of concretePredecessors to add.
	 */
	public void addAllConcretePredecessors(
			Set<ConcreteWorkOrder> _concreteWorkOrder) {
		for (ConcreteWorkOrder cwo : _concreteWorkOrder) {
			this.addConcretePredecessor(cwo);
		}
	}

	/**
	 * Remove from an ConcreteWbde all its ConcreteWorkOrder successors.
	 * 
	 */
	public void removeAllConcretePredecessors() {
		for (ConcreteWorkOrder cwo : this.getConcretePredecessors())
			cwo.setConcreteSuccessor(null);
		this.getConcretePredecessors().clear();
	}

	/**
	 * Returns the WorkBreakdownElement assigned to the
	 * ConcreteWorkBreakdownElement
	 * 
	 * @return the WorkBreakdownElement of the ConcreteWorkBreakdownElement
	 */
	public WorkBreakdownElement getWorkBreakdownElement() {
		return workBreakdownElement;
	}

	/**
	 * Initializes the WorkBreakdownElement of the ConcreteWorkBreakdownElement.
	 * 
	 * @param _workbreakdownelement
	 *            the WorkBreakdownElement to set
	 */
	public void setWorkBreakdownElement(
			WorkBreakdownElement _workbreakdownelement) {
		this.workBreakdownElement = _workbreakdownelement;
	}

	/**
	 * Returns the Date where the ConcreteWorkBreakdownElement was planned to
	 * finish
	 * 
	 * @return the planned finishing Date for the ConcreteWorkBreakdownElement
	 */
	public Date getPlannedFinishingDate() {
		return plannedFinishingDate;
	}

	/**
	 * Initializes the planned finishing Date of the
	 * ConcreteWorkBreakdownElement.
	 * 
	 * @param plannedFinishingDate
	 *            the planned finish Date to set
	 */
	public void setPlannedFinishingDate(Date plannedFinishingDate) {
		this.plannedFinishingDate = plannedFinishingDate;
	}

	/**
	 * Returns the Date where the ConcreteWorkBreakdownElement was planned to
	 * start
	 * 
	 * @return the planned starting Date for the ConcreteWorkBreakdownElement
	 */
	public Date getPlannedStartingDate() {
		return plannedStartingDate;
	}

	/**
	 * Initializes the planned starting Date of the
	 * ConcreteWorkBreakdownElement.
	 * 
	 * @param plannedStartingDate
	 *            the planned starting Date to set
	 */
	public void setPlannedStartingDate(Date plannedStartingDate) {
		this.plannedStartingDate = plannedStartingDate;
	}

	/**
	 * Returns the time planned for the ConcreteWorkBreakdownElement
	 * 
	 * @return the time planned for the ConcreteWorkBreakdownElement
	 */
	public float getPlannedTime() {
		return plannedTime;
	}

	/**
	 * Initializes the planned time for the ConcreteWorkBreakdownElement.
	 * 
	 * @param plannedTime
	 *            the planned time to set
	 */
	public void setPlannedTime(float plannedTime) {
		this.plannedTime = plannedTime;
	}

	/**
	 * Returns the Collection of predecessors for the
	 * ConcreteWorkBreakdownElement
	 * 
	 * @return the concretePredecessors
	 */
	public Set<ConcreteWorkOrder> getConcretePredecessors() {
		return concretePredecessors;
	}

	/**
	 * Initializes the predecessors of the ConcreteWorkBreakdownElement.
	 * 
	 * @param concretePredecessors
	 *            the concretePredecessors to set
	 */
	public void setConcretePredecessors(
			Set<ConcreteWorkOrder> _concretePredecessors) {
		this.concretePredecessors = _concretePredecessors;
	}

	/**
	 * Returns the Collection of successors for the ConcreteWorkBreakdownElement
	 * 
	 * @return the concreteSuccessors
	 */
	public Set<ConcreteWorkOrder> getConcreteSuccessors() {
		return concreteSuccessors;
	}

	/**
	 * Initializes the successors of the ConcreteWorkBreakdownElement.
	 * 
	 * @param concreteSuccessors
	 *            the concreteSuccessors to set
	 */
	public void setConcreteSuccessors(Set<ConcreteWorkOrder> _concreteSuccessors) {
		this.concreteSuccessors = _concreteSuccessors;
	}

	/**
	 * Defines whether the ConcreteWorkBreakdownElement is used or not
	 * 
	 * @return the isInUsed attribute
	 */
	public boolean getIsInUsed() {
		return isInUsed;
	}

	/**
	 * Initializes the isInUsed attribute with the parameter.
	 * 
	 * @param isInUsed
	 *            the isInUsed to set
	 */
	public void setIsInUsed(boolean _isInUsed) {
		this.isInUsed = _isInUsed;
	}

	/**
	 * Returns the state of the ConcreteWorkBreakdownElement
	 * 
	 * @return the String that symbolize the state of the
	 *         ConcreteWorkBreakdownElement
	 */
	public String getState() {
		return state;
	}

	/**
	 * Initializes the state of the ConcreteWorkBreakdownElement
	 * 
	 * @param state
	 *            the state to give to the ConcreteWorkBreakdownElement
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Returns the Date that the user planned for the
	 * ConcreteWorkBreakdownElement to finish.
	 * 
	 * @return the user planned finishing Date for the
	 *         ConcreteWorkBreakdownElement
	 */
	public Date getPlannedUserFinishingDate() {
		if (this.plannedUserFinishingDate == null) {
			this.plannedUserFinishingDate = this.getPlannedFinishingDate();
		}
		return this.plannedUserFinishingDate;
	}

	/**
	 * Initializes the Date the user planned for the
	 * ConcreteWorkBreakdownElement to finish.
	 * 
	 * @param plannedUserFinishingDate
	 *            the user planned finishing Date for the
	 *            ConcreteWorkBreakdownElement
	 */
	public void setPlannedUserFinishingDate(Date plannedUserFinishingDate) {
		this.plannedUserFinishingDate = plannedUserFinishingDate;
	}

	/**
	 * Returns the time the user planned for the ConcreteWorkBreakdownElement.
	 * 
	 * @return the value of the user planned time for the
	 *         ConcreteWorkBreakdownElement
	 */
	public float getPlannedUserTime() {
		if (this.plannedUserTime == 0.0) {
			this.plannedUserTime = this.getPlannedTime();
		}
		return this.plannedUserTime;
	}

	/**
	 * Initializes the value of the time the user planned for the
	 * ConcreteWorkBreakdownElement.
	 * 
	 * @param plannedUserTime
	 *            the user planned time for the ConcreteWorkBreakdownElement
	 */
	public void setPlannedUserTime(float plannedUserTime) {
		this.plannedUserTime = plannedUserTime;
	}

	/**
	 * Returns the ConcreteActivity related to the ConcreteWorkBreakdownElement.
	 * 
	 * @return the ConcreteActivity for the ConcreteWorkBreakdownElement
	 */
	public ConcreteActivity getSuperConcreteActivity() {

		ConcreteActivity superConcreteActivity = null;

		Set<ConcreteActivity> superConcreteActivities = this
				.getSuperConcreteActivities();
		if (superConcreteActivities.iterator().hasNext()) {
			superConcreteActivity = superConcreteActivities.iterator().next();
		}

		return superConcreteActivity;
	}
}
