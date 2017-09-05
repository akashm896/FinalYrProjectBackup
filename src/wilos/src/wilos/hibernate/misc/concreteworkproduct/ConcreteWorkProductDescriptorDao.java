/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
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

package wilos.hibernate.misc.concreteworkproduct;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;

/**
 * 
 * ConcreteWorkProductDescriptorDao manage requests from the system to store
 * ConcreteWorkProductDescriptors to the database
 * 
 */
public class ConcreteWorkProductDescriptorDao extends HibernateDaoSupport {

	/**
     * Saves or updates a ConcreteWorkProductDescriptor
     * 
     * @param _concreteWorkOrder The ConcreteWorkProductDescriptor to be saved or updated
     * @return The ConcreteWorkProductDescriptor's ID, if the save has been done successfully
     */
    public String saveOrUpdateConcreteWorkProductDescriptor(
	    ConcreteWorkProductDescriptor _concreteWorkProductdescriptor) {
	if (_concreteWorkProductdescriptor != null) {
	    this.getHibernateTemplate().saveOrUpdate(
		    _concreteWorkProductdescriptor);
	    return _concreteWorkProductdescriptor.getId();
	}
	return "";
    }

    /**
	 * Returns a list of all the ConcreteWorkProductDescriptor
	 * 
	 * @return A list of all the ConcreteWorkProductDescriptor
	 */
    public List<ConcreteWorkProductDescriptor> getAllConcreteWorkProductDescriptors() {
	List<ConcreteWorkProductDescriptor> concreteWorkProductDescriptors = new ArrayList<ConcreteWorkProductDescriptor>();
	for (Object obj : this.getHibernateTemplate().loadAll(
		ConcreteWorkProductDescriptor.class)) {
	    ConcreteWorkProductDescriptor cwpd = (ConcreteWorkProductDescriptor) obj;
	    concreteWorkProductDescriptors.add(cwpd);
	}
	return concreteWorkProductDescriptors;
    }

    /**
	 * Returns the ConcreteWorkProductDescriptor which has the specified ID
	 * 
	 * @param _id The wanted ConcreteWorkProductDescriptor ID
	 * @return The wanted ConcreteWorkProductDescriptor
	 */
    public ConcreteWorkProductDescriptor getConcreteWorkProductDescriptor(
	    String _id) {
	if (!_id.equals(""))
	    return (ConcreteWorkProductDescriptor) this.getHibernateTemplate()
		    .get(ConcreteWorkProductDescriptor.class, _id);
	return null;
    }

    /**
	 * Deletes the ConcreteWorkProductDescriptor
	 * 
	 * @param _concreteWorkProductdescriptor The ConcreteWorkProductDescriptor to be deleted
	 */
    public void deleteConcreteWorkProductDescriptor(
	    ConcreteWorkProductDescriptor _concreteWorkProductdescriptor) {
	this.getHibernateTemplate().delete(_concreteWorkProductdescriptor);
    }

    /**
     * Returns all the ConcreteWorkProductDescriptor of the project which has the specified ID
     * 
     * @param _projectId The wanted Project ID
     * @return A list of ConcreteWorkProductDescriptor
     */
    public List<ConcreteWorkProductDescriptor> getAllConcreteWorkProductDescriptorsForProject(
	    String _projectId) {
	List<ConcreteWorkProductDescriptor> concreteWorkProductDescriptors = new ArrayList<ConcreteWorkProductDescriptor>();
	for (Object obj : this
		.getHibernateTemplate()
		.find(
			"from ConcreteWorkProductDescriptor cwpd where cwpd.project.id=?",
			_projectId)) {
	    ConcreteWorkProductDescriptor cwpd = (ConcreteWorkProductDescriptor) obj;
	    concreteWorkProductDescriptors.add(cwpd);
	}
	return concreteWorkProductDescriptors;
    }

    /**
     * Returns all the ConcreteWorkProductDescriptor corresponding to the WorkProductDescriptor which has the specified ID
     * 
     * @param _workProductId The wanted WorkProductDescriptor ID
     * @return A list of ConcreteWorkProductDescriptor
     */
    public List<ConcreteWorkProductDescriptor> getAllConcreteWorkProductDescriptorsForAWorkProductDescriptor(
	    String _workProductId) {
	List<ConcreteWorkProductDescriptor> concreteWorkProductDescriptors = new ArrayList<ConcreteWorkProductDescriptor>();
	for (Object obj : this
		.getHibernateTemplate()
		.find(
			"from ConcreteWorkProductDescriptor cwpd where cwpd.workProductDescriptor.id=?",
			_workProductId)) {
	    ConcreteWorkProductDescriptor cwpd = (ConcreteWorkProductDescriptor) obj;
	    concreteWorkProductDescriptors.add(cwpd);
	}
	return concreteWorkProductDescriptors;
    }
}
