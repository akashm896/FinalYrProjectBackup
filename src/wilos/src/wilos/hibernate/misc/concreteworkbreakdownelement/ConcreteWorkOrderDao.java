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
package wilos.hibernate.misc.concreteworkbreakdownelement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkOrder;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkOrderId;

/**
 * @author Sebastien BALARD
 * 
 * ConcreteWorkOrderDao manage requests from the system to store
 * ConcreteWorkOrders to the database
 * 
 */
public class ConcreteWorkOrderDao extends HibernateDaoSupport {
    /**
     * Saves or updates a ConcreteWorkOrder
     * 
     * @param _concreteWorkOrder The ConcreteWorkOrder to be saved or updated
     * @return The ConcreteWorkOrder's ID, if the save has been done successfully
     */
    public ConcreteWorkOrderId saveOrUpdateConcreteWorkOrder(
	    ConcreteWorkOrder _concreteWorkOrder) {
	if (_concreteWorkOrder != null) {
	    this.getHibernateTemplate().saveOrUpdate(_concreteWorkOrder);
	    return _concreteWorkOrder.getConcreteWorkOrderId();
	}
	return null;
    }

    /**
	 * Returns the ConcreteWorkOrder which has the specified ID
	 * 
	 * @param _id The wanted ConcreteWorkOrder ID
	 * @return The wanted ConcreteWorkOrder
	 */
    public ConcreteWorkOrder getConcreteWorkOrder(ConcreteWorkOrderId _id) {
	if (_id != null)
	    return (ConcreteWorkOrder) this.getHibernateTemplate().get(
		    ConcreteWorkOrder.class, _id);
	return null;
    }

    /**
	 * Deletes the ConcreteWorkOrder
	 * 
	 * @param _concreteactivity The ConcreteWorkOrder to be deleted
	 */
    public void deleteConcreteWorkOrder(ConcreteWorkOrder _concreteWorkOrder) {
	this.getHibernateTemplate().delete(_concreteWorkOrder);
    }

    /**
	 * Returns a list of all the ConcreteWorkOrder
	 * 
	 * @return A list of all the ConcreteWorkOrder
	 */
    public List<ConcreteWorkOrder> getAllConcreteWorkOrders() {
	List<ConcreteWorkOrder> concreteWorkOrders = new ArrayList<ConcreteWorkOrder>();
	for (Object obj : this.getHibernateTemplate().loadAll(
		ConcreteWorkOrder.class)) {
	    ConcreteWorkOrder cwo = (ConcreteWorkOrder) obj;
	    concreteWorkOrders.add(cwo);
	}
	return concreteWorkOrders;
    }

    /**
     * Returns all the ConcreteWorkOrder of the project which has the specified ID
     * 
     * @param _projectId The wanted Project ID
     * @return A list of ConcreteWorkOrder
     */
    public List<ConcreteWorkOrder> getAllConcreteWorkOrdersFromProject(
	    String _projectId) {
	List<ConcreteWorkOrder> concreteWorkOrders = new ArrayList<ConcreteWorkOrder>();
	if (!_projectId.equals("")) {
	    List tmp = this.getHibernateTemplate().find(
		    "from ConcreteWorkOrder c where c.projectId=?", _projectId);
	    for (Object obj : tmp) {
		ConcreteWorkOrder cwo = (ConcreteWorkOrder) obj;
		concreteWorkOrders.add(cwo);
	    }
	}
	return concreteWorkOrders;
    }
}