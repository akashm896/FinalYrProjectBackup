/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Sebastien BALARD <sbalard@wilos-project.org>
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

package wilos.model.spem2.workbreakdownelement;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.spem2.breakdownelement.BreakdownElement;

/**
 * 
 * A WorkBreakdownElement is a special {@link BreakdownElement} that provides
 * specific properties for Breakdown Elements that represent or refer to Work
 * Definitions.
 * <p />
 * It's an element of the SPEM2 specification of the OMG organization
 * (http://www.omg.org/).
 * 
 */
public class WorkBreakdownElement extends BreakdownElement implements Cloneable {

	private Boolean isRepeatable;

	private Boolean isOngoing;

	private Boolean isEvenDriven;

	private Set<WorkOrder> predecessors;

	private Set<WorkOrder> successors;

	private Set<ConcreteWorkBreakdownElement> concreteWorkBreakdownElements;

	/**
	 * Class constructor.
	 */
	public WorkBreakdownElement() {
		super();
		this.isEvenDriven = false;
		this.isOngoing = false;
		this.isRepeatable = false;
		this.predecessors = new HashSet<WorkOrder>();
		this.successors = new HashSet<WorkOrder>();
		this.concreteWorkBreakdownElements = new HashSet<ConcreteWorkBreakdownElement>();
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the WorkBreakdownElement.
	 * 
	 * @param obj
	 *            the Object to be compare to the WorkBreakdownElement
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof BreakdownElement == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		WorkBreakdownElement workBreakdownElement = (WorkBreakdownElement) obj;
		return new EqualsBuilder().appendSuper(
				super.equals(workBreakdownElement)).append(this.isEvenDriven,
				workBreakdownElement.isEvenDriven).append(this.isOngoing,
				workBreakdownElement.isOngoing).append(this.isRepeatable,
				workBreakdownElement.isRepeatable).append(this.successors,
				workBreakdownElement.successors).append(this.predecessors,
				workBreakdownElement.predecessors).append(
				this.concreteWorkBreakdownElements,
				workBreakdownElement.concreteWorkBreakdownElements).isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of WorkBreakdownElement
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(this.isEvenDriven).append(this.isOngoing).append(
						this.isRepeatable).toHashCode();
	}

	/**
	 * Returns a copy of the current instance of WorkBreakdownElement
	 * 
	 * @return a copy of the WorkBreakdownElement
	 * @throws CloneNotSupportedException
	 */
	@Override
	public WorkBreakdownElement clone() throws CloneNotSupportedException {
		WorkBreakdownElement workBreakdownElement = new WorkBreakdownElement();
		workBreakdownElement.copy(this);
		return workBreakdownElement;
	}

	/**
	 * Copy the values of the specified WorkBreakdownElement into the current
	 * instance of the class.
	 * 
	 * @param _workBreakdownElement
	 *            the WorkBreakdownElement to copy
	 */
	protected void copy(final WorkBreakdownElement _workBreakdownElement) {
		super.copy(_workBreakdownElement);
		this.isEvenDriven = _workBreakdownElement.getIsEvenDriven();
		this.isOngoing = _workBreakdownElement.getIsOngoing();
		this.isRepeatable = _workBreakdownElement.getIsRepeatable();
		this.concreteWorkBreakdownElements = _workBreakdownElement
				.getConcreteWorkBreakdownElements();
		this.predecessors.addAll(_workBreakdownElement.getPredecessors());
		this.successors.addAll(_workBreakdownElement.getSuccessors());

	}

	/*
	 * relation between WorkBreakdownElement and WorkOrder.
	 * 
	 */

	/**
	 * Add a successor to the successors collection of a workOrder
	 * 
	 * @param _workOrder
	 */
	public void addSuccessor(WorkOrder _workOrder) {
		this.successors.add(_workOrder);
		_workOrder.setPredecessor(this);
	}

	/**
	 * Remove from a WorkBreakdownElement one of its successor.
	 * 
	 * @param _workOrder
	 *            The successor to remove.
	 */
	public void removeSuccessor(WorkOrder _workOrder) {
		_workOrder.setPredecessor(null);
		this.successors.remove(_workOrder);
	}

	/**
	 * Add a successors collection into the WorkOrder successors collection.
	 * 
	 * @param _workOrder
	 *            The set of successors to add.
	 */
	public void addAllSuccessors(Set<WorkOrder> _workOrder) {
		for (WorkOrder wo : _workOrder) {
			this.addSuccessor(wo);
		}
	}

	/**
	 * Remove from an WorkBreakdownElement all its WorkOrder successors.
	 * 
	 */
	public void removeAllSuccessors() {
		for (WorkOrder wo : this.getSuccessors())
			wo.setPredecessor(null);
		this.getSuccessors().clear();
	}

	/**
	 * Add a predecessor into the WorkOrder predecessors collection.
	 * 
	 * @param _workOrder
	 *            The predecessor to add.
	 */
	public void addPredecessor(WorkOrder _workOrder) {
		this.predecessors.add(_workOrder);
		_workOrder.setSuccessor(this);
	}

	/**
	 * Remove from a WorkBreakdownElement one of its predecessor.
	 * 
	 * @param _workOrder
	 *            The predecessor to remove.
	 */
	public void removePredecessor(WorkOrder _workOrder) {
		_workOrder.setSuccessor(null);
		this.predecessors.remove(_workOrder);
	}

	/**
	 * Add a predecessor collection into the WorkOrder predecessors collection.
	 * 
	 * @param _workOrder
	 *            The set of predecessors to add.
	 */
	public void addAllPredecessors(Set<WorkOrder> _workOrder) {
		for (WorkOrder wo : _workOrder) {
			this.addPredecessor(wo);
		}
	}

	/**
	 * Remove from an WorkBreakdownElement all its WorkOrder successors.
	 * 
	 */
	public void removeAllPredecessors() {
		for (WorkOrder wo : this.getPredecessors())
			wo.setSuccessor(null);
		this.getPredecessors().clear();
	}

	/*
	 * relation between WorkBreakdownElement and ConcreteWorkBreakdownElement.
	 * 
	 */

	/**
	 * Adds a relation between the WorkBreakdownElement and the specified
	 * ConcreteWorkBreakdownElement
	 * 
	 * @param _concreteWorkBreakdownElement
	 *            the ConcreteWorkBreakdownElement to relate to the
	 *            WorkBreakdownElement
	 */
	public void addConcreteWorkBreakdownElement(
			ConcreteWorkBreakdownElement _concreteWorkBreakdownElement) {
		this.concreteWorkBreakdownElements.add(_concreteWorkBreakdownElement);
		_concreteWorkBreakdownElement.addWorkBreakdownElement(this);
	}

	/**
	 * Removes the relation between the WorkBreakdownElement and the specified
	 * ConcreteWorkBreakdownElement
	 * 
	 * @param _concreteWorkBreakdownElement
	 *            the ConcreteWorkBreakdownElement to remaove
	 */
	public void removeConcreteWorkBreakdownElement(
			ConcreteWorkBreakdownElement _concreteWorkBreakdownElement) {
		_concreteWorkBreakdownElement.removeWorkBreakdownElement(this);
		this.concreteWorkBreakdownElements
				.remove(_concreteWorkBreakdownElement);
	}

	/**
	 * Adds the WorkBreakdownElement to the Collection of
	 * ConcreteWorkBreakdownElement in parameter
	 * 
	 * @param _concreteWorkBreakdownElements
	 */
	public void addAllConcreteWorkBreakdownElements(
			Set<ConcreteWorkBreakdownElement> _concreteWorkBreakdownElements) {
		for (ConcreteWorkBreakdownElement cwbde : _concreteWorkBreakdownElements) {
			cwbde.addWorkBreakdownElement(this);
		}
	}

	/**
	 * Removes the WorkBreakdownElement to all the related
	 * ConcreteWorkBreakdownElement
	 */
	public void removeAllConcreteWorkBreakdownElements() {
		for (ConcreteWorkBreakdownElement cwbde : this
				.getConcreteWorkBreakdownElements())
			cwbde.setWorkBreakdownElement(null);
		this.getConcreteWorkBreakdownElements().clear();
	}

	/**
	 * Getter of the attribute isEvenDriven.
	 * 
	 * @return the isEvenDriven.
	 */
	public Boolean getIsEvenDriven() {
		return this.isEvenDriven;
	}

	/**
	 * Setter of the attribute isEvenDriven.
	 * 
	 * @param _isEvenDriven
	 *            The isEvenDriven to set.
	 */
	public void setIsEvenDriven(Boolean _isEvenDriven) {
		this.isEvenDriven = _isEvenDriven;
	}

	/**
	 * Getter of the attribute isOngoing.
	 * 
	 * @return the isOngoing.
	 */
	public Boolean getIsOngoing() {
		return this.isOngoing;
	}

	/**
	 * Setter of the attribute isOngoing.
	 * 
	 * @param _isOngoing
	 *            The isOngoing to set.
	 */
	public void setIsOngoing(Boolean _isOngoing) {
		this.isOngoing = _isOngoing;
	}

	/**
	 * Getter of the attribute isRepeatable.
	 * 
	 * @return the isRepeatable.
	 */
	public Boolean getIsRepeatable() {
		return this.isRepeatable;
	}

	/**
	 * Setter of the attribute isRepeatable.
	 * 
	 * @param _isRepeatable
	 *            The isRepeatable to set.
	 */
	public void setIsRepeatable(Boolean _isRepeatable) {
		this.isRepeatable = _isRepeatable;
	}

	/**
	 * Getter of the WorkBreakdownElement's predecessors
	 * 
	 * @return the predecessors of the WorkBreakdownElement
	 */
	public Set<WorkOrder> getPredecessors() {
		return predecessors;
	}

	/**
	 * Setter of the WorkBreakdownElement's predecessors
	 * 
	 * @param predecessors
	 *            the predecessors to set
	 */
	public void setPredecessors(Set<WorkOrder> _predecessors) {
		this.predecessors = _predecessors;
	}

	/**
	 * Getter of the WorkBreakdownElement's successors
	 * 
	 * @return the successors
	 */
	public Set<WorkOrder> getSuccessors() {
		return successors;
	}

	/**
	 * Setter of the WorkBreakdownElement's successors
	 * 
	 * @param successors
	 *            the successors to set
	 */
	public void setSuccessors(Set<WorkOrder> _successors) {
		this.successors = _successors;
	}

	/**
	 * Returns the Collection of ConcreteWorkBreakdownElement related to the
	 * WorkBreakdownElement
	 * 
	 * @return all the ConcreteWorkBreakdownElement related to the
	 *         WorkBreakdownElement
	 */
	public Set<ConcreteWorkBreakdownElement> getConcreteWorkBreakdownElements() {
		return this.concreteWorkBreakdownElements;
	}

	/**
	 * Sets the Collection of ConcreteWorkBreakdownElement related to the
	 * WorkBreakdownElement
	 * 
	 * @param _concreteWorkBreakdownElements
	 *            the Collection to set
	 */
	public void setConcreteWorkBreakdownElements(
			Set<ConcreteWorkBreakdownElement> _concreteWorkBreakdownElements) {
		this.concreteWorkBreakdownElements = _concreteWorkBreakdownElements;
	}
}
