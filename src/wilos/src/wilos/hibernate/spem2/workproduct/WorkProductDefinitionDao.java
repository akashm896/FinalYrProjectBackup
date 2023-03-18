/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

import wilos.model.spem2.workproduct.WorkProductDefinition;

/**
 * 
 * WorkProductDefinitionDao manage requests from the system to store WorkProductDefinitions into the database
 * 
 */
public class WorkProductDefinitionDao extends HibernateDaoSupport {
    
	/**
     * Saves or updates a WorkProductDefinition
     * 
     * @param _workProductDefinition the WorkProductDefinition to be saved or updated
     */
    public String saveOrUpdateWorkProductDefinition(
	    WorkProductDefinition _workProductDefinition) {
	if (_workProductDefinition != null) {
	    this.getHibernateTemplate().saveOrUpdate(_workProductDefinition);
	    return _workProductDefinition.getId();
	}
	return "";
    }
    
    /**
     * Returns a list of all the WorkProductDefinitions
     * 
     * @return A list of all the WorkProductDefinitions
     */
    public List<WorkProductDefinition> getAllWorkProductDefinitions() {
	List<WorkProductDefinition> workProductDefinitions = new ArrayList<WorkProductDefinition>();
	for (Object obj : this.getHibernateTemplate().loadAll(
		WorkProductDefinition.class)) {
	    WorkProductDefinition wpd = (WorkProductDefinition) obj;
	    workProductDefinitions.add(wpd);
	}
	return workProductDefinitions;
    }

    /**
     * Returns the WorkProductDefinition which has the specified ID
     * 
     * @param _id The wanted WorkProductDefinition's ID
     * @return The wanted WorkProductDefinition
     */
    public WorkProductDefinition getWorkProductDefinition(String _id) {
	if (!_id.equals(""))
	    return (WorkProductDefinition) this.getHibernateTemplate().get(
		    WorkProductDefinition.class, _id);
	return null;
    }

    /**
     * Deletes a WorkProductDefinition
     * 
     * @param _workProductDefinition The WorkProductDefinition to be deleted
     */
    public void deleteWorkProductDefinition(
	    WorkProductDefinition _workProductDefinition) {
	this.getHibernateTemplate().delete(_workProductDefinition);
    }

}
