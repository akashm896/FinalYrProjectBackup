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

package wilos.hibernate.spem2.role;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.spem2.role.RoleDefinition;

/**
 * RoleDefinitionDao manage requests from the system to store RoleDefinition to the database
 * 
 * @author soosuske
 */
public class RoleDefinitionDao extends HibernateDaoSupport {

    /**
     * Saves or updates a RoleDefinition
     * 
     * @param _roleDefinition The RoleDefinition to be saved or updated
     */
    public String saveOrUpdateRoleDefinition(RoleDefinition _roleDefinition) {
	if (_roleDefinition != null) {
	    this.getHibernateTemplate().saveOrUpdate(_roleDefinition);
	    return _roleDefinition.getId();
	}
	return "";
    }

    /**
     * Returns a list of all the RoleDefinitions
     * 
     * @return A list of all the RoleDefinitions
     */
    public List<RoleDefinition> getAllRoleDefinitions() {
	List<RoleDefinition> roleDefinitions = new ArrayList<RoleDefinition>();
	for (Object obj : this.getHibernateTemplate().loadAll(RoleDefinition.class)) {
	    RoleDefinition rd = (RoleDefinition) obj;
	    roleDefinitions.add(rd);
	}
	return roleDefinitions;
    }

    /**
     * Returns the RoleDefinition which has the specified ID
     * 
     * @param _id The wanted RoleDefinition's ID
     * @return The wanted RoleDefinition
     */
    public RoleDefinition getRoleDefinition(String _id) {
	if (!_id.equals(""))
	    return (RoleDefinition) this.getHibernateTemplate().get(RoleDefinition.class, _id);
	return null;
    }

    /**
     * Deletes the RoleDefinition
     * 
     * @param _roleDefinition The RoleDefinition to be deleted
     */
    public void deleteRoleDefinition(RoleDefinition _roleDefinition) {
	this.getHibernateTemplate().delete(_roleDefinition);
    }
}
