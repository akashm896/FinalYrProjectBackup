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

package wilos.business.services.spem2.workproduct;

import java.util.HashSet;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.hibernate.spem2.workproduct.WorkProductDefinitionDao;
import wilos.model.spem2.guide.Guidance;
import wilos.model.spem2.workproduct.WorkProductDefinition;
import wilos.model.spem2.workproduct.WorkProductDescriptor;

/**
 * 
 * 
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class WorkProductDefinitionService {

	private WorkProductDefinitionDao workProductDefinitionDao;

	public Set<Guidance> getGuidances(WorkProductDefinition _wpdef) {
		this.workProductDefinitionDao.getSessionFactory().getCurrentSession().saveOrUpdate(_wpdef);
		Set<Guidance> tmp = new HashSet<Guidance>();
		for (Guidance g : _wpdef.getGuidances()) {
			tmp.add(g);
		}
		return tmp;
	}

	public Set<WorkProductDescriptor> getWorkProductDescriptors(WorkProductDefinition _rdef) {
		this.workProductDefinitionDao.getSessionFactory().getCurrentSession().saveOrUpdate(_rdef);
		Set<WorkProductDescriptor> tmp = new HashSet<WorkProductDescriptor>();
		for (WorkProductDescriptor wpd : _rdef.getWorkProductDescriptors()) {
			tmp.add(wpd);
		}
		return tmp;
	}

	/**
	 * @return the workProductDefinitionDao
	 */
	public WorkProductDefinitionDao getWorkProductDefinitionDao() {
		return this.workProductDefinitionDao;
	}

	/**
	 * @param _workProductDefinitionDao
	 *            the workProductDefinitionDao to set
	 */
	public void setWorkProductDefinitionDao(WorkProductDefinitionDao _workProductDefinitionDao) {
		this.workProductDefinitionDao = _workProductDefinitionDao;
	}
}
