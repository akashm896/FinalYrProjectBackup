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

package wilos.business.services.spem2.task;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.hibernate.spem2.task.TaskDefinitionDao;
import wilos.model.spem2.guide.Guidance;
import wilos.model.spem2.task.Step;
import wilos.model.spem2.task.TaskDefinition;

@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class TaskDefinitionService {
	
	private TaskDefinitionDao taskDefinitionDao;
	
	public Set<Guidance> getGuidances(TaskDefinition _tdef) {
		this.taskDefinitionDao.getSessionFactory().getCurrentSession().saveOrUpdate(_tdef);
		Set<Guidance> tmp = new HashSet<Guidance>();
		for (Guidance g : _tdef.getGuidances()) {
			tmp.add(g);
		}
		return tmp;
	}
	
	public SortedSet<Step> getSteps(TaskDefinition _tdef) {
		this.taskDefinitionDao.getSessionFactory().getCurrentSession().saveOrUpdate(_tdef);
		SortedSet<Step> tmp = new TreeSet<Step>();
		for (Step s : _tdef.getSteps()) {
			tmp.add(s);
		}
		return tmp;
	}

	/**
	 * @return the taskDefinitionDao
	 */
	public TaskDefinitionDao getTaskDefinitionDao() {
		return this.taskDefinitionDao;
	}

	/**
	 * @param _taskDefinitionDao the taskDefinitionDao to set
	 */
	public void setTaskDefinitionDao(TaskDefinitionDao _taskDefinitionDao) {
		this.taskDefinitionDao = _taskDefinitionDao;
	}

}
