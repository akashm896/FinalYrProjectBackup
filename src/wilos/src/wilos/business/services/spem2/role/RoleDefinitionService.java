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

package wilos.business.services.spem2.role;

import java.util.HashSet;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.hibernate.spem2.role.RoleDefinitionDao;
import wilos.model.spem2.guide.Guidance;
import wilos.model.spem2.role.RoleDefinition;
import wilos.model.spem2.role.RoleDescriptor;

/**
 * @author Sebastien
 * 
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class RoleDefinitionService {

	private RoleDefinitionDao roleDefinitionDao;

	public Set<Guidance> getGuidances(RoleDefinition _rdef) {
		this.roleDefinitionDao.getSessionFactory().getCurrentSession().saveOrUpdate(_rdef);
		Set<Guidance> tmp = new HashSet<Guidance>();
		for (Guidance g : _rdef.getGuidances()) {
			tmp.add(g);
		}
		return tmp;
	}

	public Set<RoleDescriptor> getRoleDescriptors(RoleDefinition _rdef) {
		this.roleDefinitionDao.getSessionFactory().getCurrentSession().saveOrUpdate(_rdef);
		Set<RoleDescriptor> tmp = new HashSet<RoleDescriptor>();
		for (RoleDescriptor rd : _rdef.getRoleDescriptors()) {
			tmp.add(rd);
		}
		return tmp;
	}

	/**
	 * @return the roleDefinitionDao
	 */
	public RoleDefinitionDao getRoleDefinitionDao() {
		return this.roleDefinitionDao;
	}

	/**
	 * @param _roleDefinitionDao
	 *            the roleDefinitionDao to set
	 */
	public void setRoleDefinitionDao(RoleDefinitionDao _roleDefinitionDao) {
		this.roleDefinitionDao = _roleDefinitionDao;
	}
}
