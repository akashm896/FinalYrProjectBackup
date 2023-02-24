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

package wilos.hibernate.spem2.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.spem2.task.TaskDefinition;

/**
 * TaskDefinitionDao manage requests from the system to store TaskDefinitions into the database
 * 
 * @author eperico
 * 
 */
public class TaskDefinitionDao extends HibernateDaoSupport {
    /**
     * Saves or updates a TaskDefinition
     * 
     * @param _taskDefinition The TaskDefinition to be saved or updated
     */
    public String saveOrUpdateTaskDefinition(TaskDefinition _taskDefinition) {
	if (_taskDefinition != null) {
	    this.getHibernateTemplate().saveOrUpdate(_taskDefinition);
	    return _taskDefinition.getId();
	}
	return "";
    }

    /**
     * Returns a list of all the TaskDefinitions
     * 
     * @return A list of all the TaskDefinitions
     */
    public List<TaskDefinition> getAllTaskDefinitions() {
	List<TaskDefinition> taskDefinitions = new ArrayList<TaskDefinition>();
	for (Object obj : this.getHibernateTemplate().loadAll(TaskDefinition.class)) {
	    TaskDefinition td = (TaskDefinition) obj;
	    taskDefinitions.add(td);
	}
	return taskDefinitions;
    }

    /**
     * Returns the TaskDefinition which has the specified ID
     * 
     * @param _id The wanted TaskDefinition's ID
     * @return The wanted TaskDefinition
     */
    public TaskDefinition getTaskDefinition(String _id) {
	if (!_id.equals(""))
	    return (TaskDefinition) this.getHibernateTemplate().get(TaskDefinition.class, _id);
	return null;
    }

    /**
     * Deletes a TaskDefinition
     * 
     * @param _roleDescriptor The TaskDefinition to be deleted
     */
    public void deleteTaskDefinition(TaskDefinition _roleDescriptor) {
	this.getHibernateTemplate().delete(_roleDescriptor);
    }
}
