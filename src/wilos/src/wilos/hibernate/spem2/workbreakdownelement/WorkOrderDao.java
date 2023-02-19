/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2007 Sebastien BALARD <sbalard@wilos-project.org>
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

package wilos.hibernate.spem2.workbreakdownelement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.spem2.workbreakdownelement.WorkOrder;
import wilos.model.spem2.workbreakdownelement.WorkOrderId;

/**
 * @author Sebastien
 * 
 * WorkOrderDao manage requests from the system to store WorkOrders into the database
 * 
 */
public class WorkOrderDao extends HibernateDaoSupport {

    /**
     * Saves or updates a WorkOrder
     * 
     * @param _workOrder the WorkOrder to be saved or updated
     */
    public WorkOrderId saveOrUpdateWorkOrder(WorkOrder _workOrder) {
	if (_workOrder != null) {
	    this.getHibernateTemplate().saveOrUpdate(_workOrder);
	    return _workOrder.getWorkOrderId();
	}
	return null;
    }

    /**
     * Returns a list of all the WorkOrders
     * 
     * @return A list of all the WorkOrders
     */
    public List<WorkOrder> getAllWorkOrders() {
	List<WorkOrder> workOrders = new ArrayList<WorkOrder>();
	for (Object obj : this.getHibernateTemplate().loadAll(WorkOrder.class)) {
	    WorkOrder e = (WorkOrder) obj;
	    workOrders.add(e);
	}
	return workOrders;
    }

    /**
     * Returns the WorkOrder which has the specified ID
     * 
     * @param _id The wanted WorkOrder's ID
     * @return The wanted WorkOrder
     */
    public WorkOrder getWorkOrder(WorkOrderId _id) {
	if (_id != null)
	    return (WorkOrder) this.getHibernateTemplate().get(WorkOrder.class, _id);
	return null;
    }

    /**
     * Deletes a WorkOrder
     * 
     * @param _workOrder The WorkOrder to be deleted
     */
    public void deleteWorkOrder(WorkOrder _workOrder) {
	this.getHibernateTemplate().delete(_workOrder);
    }
}
