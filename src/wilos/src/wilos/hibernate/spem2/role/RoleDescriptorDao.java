/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Mathieu BENOIT <mathieu-benoit@hotmail.fr>
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

import wilos.model.spem2.role.RoleDescriptor;

/**
 * RoleDefinitionDao manage requests from the system to store RoleDefinition to the database
 * 
 * @author soosuske
 */
public class RoleDescriptorDao extends HibernateDaoSupport {

    /**
     * Save or update an RoleDescriptor
     * 
     * @param _RoleDescriptor
     */
    public String saveOrUpdateRoleDescriptor(RoleDescriptor _roleDescriptor) {
	if (_roleDescriptor != null) {
	    this.getHibernateTemplate().saveOrUpdate(_roleDescriptor);
	    return _roleDescriptor.getId();
	}
	return "";
    }

    /**
     * Return a set of RoleDescriptor
     * 
     * @return
     */
    public List<RoleDescriptor> getAllRoleDescriptors() {
	List<RoleDescriptor> roleDescriptors = new ArrayList<RoleDescriptor>();
	for (Object obj : this.getHibernateTemplate().loadAll(RoleDescriptor.class)) {
	    RoleDescriptor rd = (RoleDescriptor) obj;
	    roleDescriptors.add(rd);
	}
	return roleDescriptors;
    }

    /**
     * Return the RoleDescriptor which have the id _id
     * 
     * @param _id
     * @return
     */
    public RoleDescriptor getRoleDescriptor(String _id) {
	if (!_id.equals(""))
	    return (RoleDescriptor) this.getHibernateTemplate().get(RoleDescriptor.class, _id);
	return null;
    }

    /**
     * Delete the RoleDescriptor
     * 
     * @param _RoleDescriptor
     */
    public void deleteRoleDescriptor(RoleDescriptor _roleDescriptor) {
	this.getHibernateTemplate().delete(_roleDescriptor);
    }
}
