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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 
 * WorkOrder represents a relationship between two Breakdown Elements in which
 * one Breakdown Elements depends on the start or finish of another Breakdown
 * Elements in order to begin or end. The WorkOrder class defines predecessor
 * and successor relations amongst Breakdown Elements. This information is in
 * particular critical for planning applications. 
 * <p />
 * It's an element of the SPEM2 specification of the OMG
 *         organization (http://www.omg.org/).
 * 
 */
public class WorkOrder {

	//TODO [Wilos1] remove this constant (already defined in wilos.utils.Constantes)
	public final static String START_TO_START = "startToStart";
	//TODO [Wilos1] remove this constant (already defined in wilos.utils.Constantes)
	public final static String START_TO_FINISH = "startToFinish";
	//TODO [Wilos1] remove this constant (already defined in wilos.utils.Constantes)
	public final static String FINISH_TO_START = "finishToStart";
	//TODO [Wilos1] remove this constant (already defined in wilos.utils.Constantes)
	public final static String FINISH_TO_FINISH = "finishToFinish";
	//TODO [Wilos1] move this array to wilos.utils.Constantes
	public final static String[] linkTypes = { START_TO_START, START_TO_FINISH,
			FINISH_TO_START, FINISH_TO_FINISH };

	private WorkOrderId workOrderId;

	private WorkBreakdownElement predecessor;

	private WorkBreakdownElement successor;

	private String linkType;

	/**
	 * Class constructor
	 */
	public WorkOrder() {
		this.workOrderId = null;
		this.predecessor = null;
		this.successor = null;
		this.linkType = "";
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the WorkOrder.
	 * 
	 * @param obj
	 *            the Object to be compare to the WorkOrder
	 * @return true if the specified Object is the same, false otherwise
	 */
	@Override
	public boolean equals(Object _obj) {
		if (_obj instanceof WorkOrder == false) {
			return false;
		}
		if (this == _obj) {
			return true;
		}
		WorkOrder workOrder = (WorkOrder) _obj;
		return new EqualsBuilder().append(this.workOrderId,
				workOrder.workOrderId)
				.append(this.linkType, workOrder.linkType).isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of WorkOrder
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.workOrderId)
				.toHashCode();
	}

	/**
	 * Gets the WorkOrder's link type
	 * 
	 * @return the linkType
	 */
	public String getLinkType() {
		return this.linkType;
	}

	/**
	 * Sets the WorkOrder's link type
	 * 
	 * @param _linkType
	 *            the linkType to set
	 */
	public void setLinkType(String _linkType) {
		this.linkType = _linkType;
	}

	/**
	 * Gets the WorkOrder's predecessor
	 * 
	 * @return the predecessor
	 */
	public WorkBreakdownElement getPredecessor() {
		return this.predecessor;
	}

	/**
	 * Sets the WorkOrder's predecessor with the specified one
	 * 
	 * @param _predecessor
	 *            the predecessor to set
	 */
	public void setPredecessor(WorkBreakdownElement _predecessor) {
		this.predecessor = _predecessor;
	}

	/**
	 * Gets the WorkOrder's successor
	 * 
	 * @return the successor
	 */
	public WorkBreakdownElement getSuccessor() {
		return this.successor;
	}

	/**
	 * Sets the WorkOrder's successor
	 * 
	 * @param _successor
	 *            the successor to set
	 */
	public void setSuccessor(WorkBreakdownElement _successor) {
		this.successor = _successor;
	}

	/**
	 * Gets the WorkOrder's identifier
	 * 
	 * @return the workOrderId
	 */
	public WorkOrderId getWorkOrderId() {
		return this.workOrderId;
	}

	/**
	 * Sets the WorkOrder's identifier with the specified one
	 * 
	 * @param _workOrderId
	 *            the workOrderId to set
	 */
	public void setWorkOrderId(WorkOrderId _workOrderId) {
		this.workOrderId = _workOrderId;
	}

}
