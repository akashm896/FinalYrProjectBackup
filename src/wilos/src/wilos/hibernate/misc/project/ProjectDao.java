/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) Sebastien BALARD <sbalard@wilos-project.org>
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

package wilos.hibernate.misc.project;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.misc.project.Project;

/**
 * ProjectDao manage requests from the system to store Project into the
 * database.
 */
public class ProjectDao extends HibernateDaoSupport {
	/**
	 * Saves or updates a Project
	 * 
	 * @param _project The Project to be saved or updated
	 */
	public String saveOrUpdateProject(Project _project) {
		if (_project != null) {
			this.getHibernateTemplate().saveOrUpdate(_project);
			return _project.getId();
		}
		return "";
	}

	/**
	 * Returns a list of all the Projects
	 * 
	 * @return A list of all the Projects
	 */
	public List<Project> getAllProjects() {
		List<Project> projects = new ArrayList<Project>();
		for (Object obj : this.getHibernateTemplate().loadAll(Project.class)) {
			Project p = (Project) obj;
			projects.add(p);
		}
		return projects;
	}

	/**
	 * Returns the Project which has the specified ID
	 * 
	 * @param _id The wanted Project's ID
	 * @return The wanted Project
	 */
	public Project getProject(String _id) {
		if (!_id.equals(""))
			return (Project) this.getHibernateTemplate().get(Project.class, _id);
		return null;
	}

	/**
	 * Deletes the Project
	 * 
	 * @param _project The Project to be deleted
	 */
	public void deleteProject(Project _project) {
		this.getHibernateTemplate().delete(_project);
	}
	
}
