/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

package wilos.hibernate.misc.concretetask;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.misc.concretetask.ConcreteTaskDescriptor;

/**
 * ConcreteTaskDescriptorDao manage requests from the system to store
 * ConcreteTaskDescriptorDao to the database
 *
 * @author mat, seb
 *
 */
public class ConcreteTaskDescriptorDao extends HibernateDaoSupport {

    /**
     * Saves or updates a ConcreteTaskDescriptor
     *
     * @param _concreteTaskdescriptor The ConcreteTaskDescriptor to be saved or updated
     */
    public String saveOrUpdateConcreteTaskDescriptor(ConcreteTaskDescriptor _concreteTaskDescriptor) {
	if (_concreteTaskDescriptor != null) {
	    this.getHibernateTemplate().saveOrUpdate(_concreteTaskDescriptor);
	    return _concreteTaskDescriptor.getId();
	}
	return "";
    }

    /**
	 * Returns a list of all the ConcreteTaskDescriptor
	 * 
	 * @return A list of all the ConcreteTaskDescriptor
	 */
    public List<ConcreteTaskDescriptor> getAllConcreteTaskDescriptors() {
	List<ConcreteTaskDescriptor> concreteTaskDescriptors = new ArrayList<ConcreteTaskDescriptor>();
	for (Object obj : this.getHibernateTemplate().loadAll(ConcreteTaskDescriptor.class)) {
	    ConcreteTaskDescriptor ctd = (ConcreteTaskDescriptor) obj;
	    concreteTaskDescriptors.add(ctd);
	}
	return concreteTaskDescriptors;
    }

    /**
     * Returns the ConcreteTaskDescriptor which has the specified ID
     *
     * @param _id The wanted ConcreteTaskDescriptor ID
     * @return The wanted ConcreteTaskDescriptor
     */
    public ConcreteTaskDescriptor getConcreteTaskDescriptor(String _id) {
	if(_id == null)
	    return null;
	if (!_id.equals(""))
	    return (ConcreteTaskDescriptor) this.getHibernateTemplate().get(ConcreteTaskDescriptor.class, _id);
	return null;
    }

    /**
     * Deletes the ConcreteTaskDescriptor
     *
     * @param _concreteTaskdescriptor The ConcreteTaskDescriptor to be deleted
     */
    public void deleteConcreteTaskDescriptor(ConcreteTaskDescriptor _concreteTaskdescriptor) {
	this.getHibernateTemplate().delete(_concreteTaskdescriptor);
    }

    /**
     * Returns all the ConcreteTaskDescriptors of the project which has the specified ID
     * 
     * @param _projectId The wanted Project ID
     * @return A list of ConcreteTaskDescriptor
     */
    public List<ConcreteTaskDescriptor> getAllConcreteTaskDescriptorsForProject(String _projectId) {
	List<ConcreteTaskDescriptor> concreteTaskDescriptors = new ArrayList<ConcreteTaskDescriptor>();
	for (Object obj : this.getHibernateTemplate().find("from ConcreteTaskDescriptor ctd where ctd.project.id=?",
		_projectId)) {
	    ConcreteTaskDescriptor ctd = (ConcreteTaskDescriptor) obj;
	    concreteTaskDescriptors.add(ctd);
	}
	return concreteTaskDescriptors;
    }
}
