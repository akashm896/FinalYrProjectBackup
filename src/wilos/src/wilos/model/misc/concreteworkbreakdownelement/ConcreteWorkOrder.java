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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 
 * @author Sebastien
 * 
 */
public class ConcreteWorkOrder implements Cloneable {

	private ConcreteWorkOrderId concreteWorkOrderId;

	private String concreteLinkType;

	private ConcreteWorkBreakdownElement concretePredecessor;

	private ConcreteWorkBreakdownElement concreteSuccessor;

	private String projectId;

	/**
	 * Default class constructor
	 * 
	 */
	public ConcreteWorkOrder() {
		this.concreteWorkOrderId = null;
		this.concretePredecessor = null;
		this.concreteSuccessor = null;
		this.concreteLinkType = "";
		this.projectId = "";
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the class.
	 * 
	 * @param obj
	 *            the Object to be compare to the ConcreteWorkOrder
	 * @return true if the specified Object is the same, false otherwise
	 */
	@Override
	public boolean equals(Object _obj) {
		if (_obj instanceof ConcreteWorkOrder == false) {
			return false;
		}
		if (this == _obj) {
			return true;
		}
		ConcreteWorkOrder concreteWorkOrder = (ConcreteWorkOrder) _obj;
		return new EqualsBuilder().append(this.concreteWorkOrderId,
				concreteWorkOrder.concreteWorkOrderId).append(
				this.concreteLinkType, concreteWorkOrder.concreteLinkType)
				.isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of ConcreteWorkOrder
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.concreteWorkOrderId)
				.toHashCode();
	}

	/**
	 * Returns a copy of the current instance of ConcreteWorkOrder
	 * 
	 * @return a copy of the ConcreteWorkOrder
	 * @throws CloneNotSupportedException
	 */
	@Override
	public ConcreteWorkOrder clone() throws CloneNotSupportedException {
		ConcreteWorkOrder concreteWorkOrder = new ConcreteWorkOrder();
		concreteWorkOrder.copy(this);
		return concreteWorkOrder;
	}

	/**
	 * Copy the values of the specified ConcreteWorkOrder into the current
	 * instance of the class.
	 * 
	 * @param _concreteWorkOrder
	 *            the ConcreteWorkOrder to copy
	 */
	protected void copy(final ConcreteWorkOrder _concreteWorkOrder)
			throws CloneNotSupportedException {
		this.concretePredecessor = _concreteWorkOrder.getConcretePredecessor();
		this.concreteSuccessor = _concreteWorkOrder.getConcreteSuccessor();
		this.concreteLinkType = _concreteWorkOrder.getConcreteLinkType();
		this.projectId = _concreteWorkOrder.getProjectId();
		this.concreteWorkOrderId = _concreteWorkOrder.getConcreteWorkOrderId()
				.clone();
	}

	/**
	 * Returns the ConcreteWorkBreakdownElement predecessor assigned to the
	 * ConcreteWorkOrder
	 * 
	 * @return the concretePredecessor attribute
	 */
	public ConcreteWorkBreakdownElement getConcretePredecessor() {
		return this.concretePredecessor;
	}

	/**
	 * Sets the ConcreteWorkBreakdownElement predecessor assigned to the
	 * ConcreteWorkOrder
	 * 
	 * @param concretePredecessor
	 *            the concretePredecessor to set
	 */
	public void setConcretePredecessor(
			ConcreteWorkBreakdownElement _concretePredecessor) {
		this.concretePredecessor = _concretePredecessor;
	}

	/**
	 * Returns the ConcreteWorkBreakdownElement successor assigned to the
	 * ConcreteWorkOrder
	 * 
	 * @return the concreteSuccessor
	 */
	public ConcreteWorkBreakdownElement getConcreteSuccessor() {
		return this.concreteSuccessor;
	}

	/**
	 * Sets the ConcreteWorkBreakdownElement successor assigned to the
	 * ConcreteWorkOrder
	 * 
	 * @param concreteSuccessor
	 *            the concreteSuccessor to set
	 */
	public void setConcreteSuccessor(
			ConcreteWorkBreakdownElement concreteSuccessor) {
		this.concreteSuccessor = concreteSuccessor;
	}

	
	/**
	 * Returns the String that symbolizes the type of the concrete link of the
	 * ConcreteWorkOrder
	 * 
	 * @return the concrete link
	 */
	public String getConcreteLinkType() {
		return this.concreteLinkType;
	}

	/**
	 * Sets the ConcreteWorkBreakdownElement successor assigned to the
	 * ConcreteWorkOrder
	 * 
	 * @param concreteSuccessor
	 *            the concreteSuccessor to set
	 */
	public void setConcreteLinkType(String _concreteLinkType) {
		this.concreteLinkType = _concreteLinkType;
	}

	/**
	 * Returns the ConcreteWorkOrderId identifying the ConcreteWorkOrder
	 * 
	 * @return the concreteWorkOrderId
	 */
	public ConcreteWorkOrderId getConcreteWorkOrderId() {
		return concreteWorkOrderId;
	}

	/**
	 * Sets the ConcreteWorkOrderId identifying the ConcreteWorkOrder
	 * 
	 * @param concreteWorkOrderId
	 *            the concreteWorkOrderId to set
	 */
	public void setConcreteWorkOrderId(ConcreteWorkOrderId concreteWorkOrderId) {
		this.concreteWorkOrderId = concreteWorkOrderId;
	}

	/**
	 * Returns the identifier of the Project link to the ConcreteWorkOrder
	 * 
	 * @return the projectId
	 */
	public String getProjectId() {
		return this.projectId;
	}

	/**
	 * Sets the identifier of the Project link to the ConcreteWorkOrder
	 * 
	 * @param _projectId
	 *            the projectId to set
	 */
	public void setProjectId(String _projectId) {
		this.projectId = _projectId;
	}

}
