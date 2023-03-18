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

import wilos.model.spem2.task.TaskDescriptor;

/**
 * TaskDescriptorDao manage requests from the system to store TaskDescriptors into the database
 * 
 * @author eperico
 * 
 */
public class TaskDescriptorDao extends HibernateDaoSupport {

    /**
     * Saves or updates a TaskDescriptor
     * 
     * @param _taskdescriptor The TaskDescriptor to be saved or updated
     */
    public String saveOrUpdateTaskDescriptor(TaskDescriptor _taskdescriptor) {
	if (_taskdescriptor != null) {
	    this.getHibernateTemplate().saveOrUpdate(_taskdescriptor);
	    return _taskdescriptor.getId();
	}
	return "";
    }

    /**
     * Returns a list of all the TaskDescriptors
     * 
     * @return A list of all the TaskDescriptors
     */
    public List<TaskDescriptor> getAllTaskDescriptors() {
	List<TaskDescriptor> taskDescriptors = new ArrayList<TaskDescriptor>();
	for (Object obj : this.getHibernateTemplate().loadAll(TaskDescriptor.class)) {
	    TaskDescriptor td = (TaskDescriptor) obj;
	    taskDescriptors.add(td);
	}
	return taskDescriptors;
    }

    /**
     * Returns the TaskDescriptor which has the specified ID
     * 
     * @param _id The wanted TaskDescriptor's ID
     * @return The wanted TaskDescriptor
     */
    public TaskDescriptor getTaskDescriptor(String _id) {
	if (!_id.equals(""))
	    return (TaskDescriptor) this.getHibernateTemplate().get(TaskDescriptor.class, _id);
	return null;
    }

    /**
     * Deletes a TaskDescriptor
     * 
     * @param _taskdescriptor The TaskDescriptor to be deleted
     */
    public void deleteTaskDescriptor(TaskDescriptor _taskdescriptor) {
	this.getHibernateTemplate().delete(_taskdescriptor);
    }
}
