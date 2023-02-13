/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
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

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * 
 * @author Sebastien BALARD
 * 
 */
public class ConcreteWorkOrderId implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private String concretePredecessorId;

    private String concreteSuccessorId;

    private String concreteLinkTypeId;

    /**
	 * Class constructor
	 * 
	 */
    public ConcreteWorkOrderId() {
	this.concretePredecessorId = "";
	this.concreteSuccessorId = "";
	this.concreteLinkTypeId = "";
    }

    /**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the class.
	 * 
	 * @param obj
	 *            the Object to be compare to the ConcreteWorkOrderId
	 * @return true if the specified Object is the same, false otherwise
	 */
    public boolean equals(Object _obj) {
	if (_obj instanceof ConcreteWorkOrderId == false) {
	    return false;
	}
	if (this == _obj) {
	    return true;
	}
	ConcreteWorkOrderId concreteWorkOrderId = (ConcreteWorkOrderId) _obj;
	return new EqualsBuilder().append(this.concretePredecessorId,
		concreteWorkOrderId.concretePredecessorId).append(
		this.concreteSuccessorId,
		concreteWorkOrderId.concreteSuccessorId).append(this.concreteLinkTypeId,
		concreteWorkOrderId.concreteLinkTypeId).isEquals();
    }

    /**
	 * Returns a copy of the current instance of ConcreteWorkOrderId
	 * 
	 * @return a copy of the ConcreteWorkOrderId
	 * @throws CloneNotSupportedException
	 */
    @Override
    public ConcreteWorkOrderId clone() throws CloneNotSupportedException {
	ConcreteWorkOrderId concreteWorkOrderId = new ConcreteWorkOrderId();
	concreteWorkOrderId.copy(this);
	return concreteWorkOrderId;
    }

    /**
	 * Copy the values of the specified ConcreteWorkOrderId into the current
	 * instance of the class.
	 * 
	 * @param _concreteWorkOrderId
	 *            the ConcreteWorkOrderId to copy
	 */
    protected void copy(final ConcreteWorkOrderId _concreteWorkOrderId) {
	this.concretePredecessorId = _concreteWorkOrderId
		.getConcretePredecessorId();
	this.concreteSuccessorId = _concreteWorkOrderId
		.getConcreteSuccessorId();
	this.concreteLinkTypeId = _concreteWorkOrderId.concreteLinkTypeId;
    }

    /**
     * Returns the identifier of the concrete predecessor assigned to the
	 * ConcreteWorkOrderId
	 * 
     * @return the concretePredecessorId
     */
    public String getConcretePredecessorId() {
	return this.concretePredecessorId;
    }

    /**
     * Sets the identifier of the concrete predecessor assigned to the
	 * ConcreteWorkOrderId
	 * 
     * @param _concretePredecessorId
     *                the concretePredecessorId to set
     */
    public void setConcretePredecessorId(String _concretePredecessorId) {
	this.concretePredecessorId = _concretePredecessorId;
    }

    /**
     * Returns the identifier of the concrete successor assigned to the
	 * ConcreteWorkOrderId
	 * 
     * @return the concreteSuccessorId
     */
    public String getConcreteSuccessorId() {
	return this.concreteSuccessorId;
    }

    /**
     * Sets the identifier of the concrete successor assigned to the
	 * ConcreteWorkOrderId
	 * 
     * @param _concreteSuccessorId
     *                the concreteSuccessorId to set
     */
    public void setConcreteSuccessorId(String _concreteSuccessorId) {
	this.concreteSuccessorId = _concreteSuccessorId;
    }

    /**
     * Returns the identifier of the concrete link type assigned to the
	 * ConcreteWorkOrderId
	 * 
     * @return the linkType
     */
    public String getConcreteLinkTypeId() {
        return concreteLinkTypeId;
    }

    /**
     * Sets the identifier of the concrete link type assigned to the
	 * ConcreteWorkOrderId
	 * 
     * @param _linkType the linkType to set
     */
    public void setConcreteLinkTypeId(String _linkType) {
        concreteLinkTypeId = _linkType;
    }
}
