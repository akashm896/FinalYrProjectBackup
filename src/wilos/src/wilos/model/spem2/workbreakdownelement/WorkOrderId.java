/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
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

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;

public class WorkOrderId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String predecessorId;

	private String successorId;

	// Constructors

	/**
	 * Default constructor
	 */
	public WorkOrderId() {
		this.predecessorId = "";
		this.successorId = "";
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the WorkOrderId.
	 * 
	 * @param obj
	 *            the Object to be compare to the WorkOrderId
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object _obj) {
		if (_obj instanceof WorkOrderId == false) {
			return false;
		}
		if (this == _obj) {
			return true;
		}
		WorkOrderId workOrderId = (WorkOrderId) _obj;
		return new EqualsBuilder().append(this.predecessorId,
				workOrderId.predecessorId).append(this.successorId,
				workOrderId.successorId).isEquals();
	}
	
	

	/**
	 * Returns the characteristics of the WorkOrderId in a String form
	 * 
	 * @return the WorkOrderId's characteristics
	 */
	@Override
	public String toString() {
		return this.predecessorId + "-" +this.successorId;
	}

	/**
	 * Returns the identifier of the WorkOrderId's predecessor
	 * 
	 * @return the predecessorId
	 */
	public String getPredecessorId() {
		return this.predecessorId;
	}

	/**
	 * Sets the identifier of the WorkOrderId's predecessor
	 * 
	 * @param _predecessorId
	 *            the predecessorId to set
	 */
	public void setPredecessorId(String _predecessorId) {
		this.predecessorId = _predecessorId;
	}

	/**
	 * Returns the identifier of the WorkOrderId's successor
	 * 
	 * @return the successorId
	 */
	public String getSuccessorId() {
		return this.successorId;
	}

	/**
	 * Sets the identifier of the WorkOrderId's successor
	 * 
	 * @param _successorId
	 *            the successorId to set
	 */
	public void setSuccessorId(String _successorId) {
		this.successorId = _successorId;
	}

}
