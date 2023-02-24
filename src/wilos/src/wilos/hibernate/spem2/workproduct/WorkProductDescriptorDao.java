/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Mathieu BENOIT <mbenoit@wilos-project.org>
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

package wilos.hibernate.spem2.workproduct;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.spem2.workproduct.WorkProductDescriptor;

/**
 * WorkProductDescriptorDao manage requests from the system to store WorkProductDescriptor to the database
 *  
 */
public class WorkProductDescriptorDao extends HibernateDaoSupport {

    /**
     * Saves or updates a WorkProductDescriptor
     * 
     * @param _WorkProductDescriptor The WorkProductDescriptor to be saved or updated
     */
    public String saveOrUpdateWorkProductDescriptor(WorkProductDescriptor _workproductDescriptor) {
	if (_workproductDescriptor != null) {
	    this.getHibernateTemplate().saveOrUpdate(_workproductDescriptor);
	    return _workproductDescriptor.getId();
	}
	return "";
    }

    /**
     * Returns a list of all the WorkProductDescriptors
     * 
     * @return A list of all the WorkProductDescriptors
     */
    public List<WorkProductDescriptor> getAllWorkProductDescriptors() {
	List<WorkProductDescriptor> workProductDescriptors = new ArrayList<WorkProductDescriptor>();
	for (Object obj : this.getHibernateTemplate().loadAll(WorkProductDescriptor.class)) {
	    WorkProductDescriptor wpd = (WorkProductDescriptor) obj;
	    workProductDescriptors.add(wpd);
	}
	return workProductDescriptors;
    }

    /**
     * Returns the WorkProductDescriptor which has the specified ID
     * 
     * @param _id The wanted WorkProductDescriptor's ID
     * @return The wanted WorkProductDescriptor
     */
    public WorkProductDescriptor getWorkProductDescriptor(String _id) {
	if (!_id.equals(""))
	    return (WorkProductDescriptor) this.getHibernateTemplate().get(WorkProductDescriptor.class, _id);
	return null;
    }

    /**
     * Deletes a WorkProductDescriptor
     * 
     * @param _workproductDescriptor The WorkProductDescriptor to be deleted
     */
    public void deleteWorkProductDescriptor(WorkProductDescriptor _workproductDescriptor) {
	this.getHibernateTemplate().delete(_workproductDescriptor);
    }
}
