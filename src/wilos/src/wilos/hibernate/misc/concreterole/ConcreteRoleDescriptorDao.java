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

package wilos.hibernate.misc.concreterole;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;

/**
 * ConcreteRoleDescriptorDao manage requests from the system to store
 * ConcreteRoleDescriptorDao to the database 
 */
public class ConcreteRoleDescriptorDao extends HibernateDaoSupport {

	/**
	 * Saves or updates a ConcreteRoleDescriptorDao
	 * 
	 * @param _concreteRoledescriptor The ConcreteRoleDescriptor to be saved or updated
	 */
	public String saveOrUpdateConcreteRoleDescriptor(ConcreteRoleDescriptor _concreteRoledescriptor) {
		if (_concreteRoledescriptor != null) {
			this.getHibernateTemplate().saveOrUpdate(_concreteRoledescriptor);
			return _concreteRoledescriptor.getId();
		}
		return "";
	}

	/**
	 * Returns a list of all the ConcreteRoleDescriptors
	 * 
	 * @return A list of all the ConcreteRoleDescriptors
	 */
	public List<ConcreteRoleDescriptor> getAllConcreteRoleDescriptors() {
		List<ConcreteRoleDescriptor> concreteRoleDescriptors = new ArrayList<ConcreteRoleDescriptor>();
		for (Object obj : this.getHibernateTemplate().loadAll(ConcreteRoleDescriptor.class)) {
			ConcreteRoleDescriptor crd = (ConcreteRoleDescriptor) obj;
			concreteRoleDescriptors.add(crd);
		}
		return concreteRoleDescriptors;
	}

	/**
	 * Returns the ConcreteRoleDescriptor which has the ID _id
	 * 
	 * @param _id The wanted ID
	 * @return The wanted ConcreteRoleDescriptor
	 */
	public ConcreteRoleDescriptor getConcreteRoleDescriptor(String _id) {
		if (!_id.equals(""))
			return (ConcreteRoleDescriptor) this.getHibernateTemplate().get(ConcreteRoleDescriptor.class, _id);
		return null;
	}

	/**
	 * Deletes the ConcreteRoleDescriptor
	 * 
	 * @param _concreteRoledescriptor
	 */
	public void deleteConcreteRoleDescriptor(ConcreteRoleDescriptor _concreteRoledescriptor) {
		this.getHibernateTemplate().delete(_concreteRoledescriptor);
	}

	/**
	 * Returns all the ConcreteRoleDescriptors of the project which has the specified ID
	 * 
	 * @param _projectId The wanted Project ID
	 * @return A list of ConcreteRoleDescriptor
	 */
	public List<ConcreteRoleDescriptor> getAllConcreteRoleDescriptorsForProject(String _projectId) {
		List<ConcreteRoleDescriptor> concreteRoleDescriptors = new ArrayList<ConcreteRoleDescriptor>();
		for (Object obj : this.getHibernateTemplate().find("from ConcreteRoleDescriptor crd where crd.project.id=?",
				_projectId)) {
			ConcreteRoleDescriptor crd = (ConcreteRoleDescriptor) obj;
			concreteRoleDescriptors.add(crd);
		}
		return concreteRoleDescriptors;
	}

	/**
	 * Returns a list of ConcreteTaskDescriptor corresponding to 
	 * the ConcreteRoleDescriptor which has the specified ID
	 * 
	 * @param _concreteRoleDescriptorId The wanted ConcreteRoleDescriptor ID
	 * @return A list of ConcreteTaskDescriptor
	 */
	public List<ConcreteTaskDescriptor> getMainConcreteTaskDescriptorsForConcreteRoleDescriptor(
			String _concreteRoleDescriptorId) {
		List<ConcreteTaskDescriptor> concreteTaskDescriptors = new ArrayList<ConcreteTaskDescriptor>();
		for (Object obj : this.getHibernateTemplate().find(
				"from ConcreteTaskDescriptor ctd where ctd.mainConcreteRoleDescriptor.id=?", _concreteRoleDescriptorId)) {
			ConcreteTaskDescriptor ctd = (ConcreteTaskDescriptor) obj;
			concreteTaskDescriptors.add(ctd);
		}
		return concreteTaskDescriptors;
	}

	/**
	 * Returns a list of ConcreteRoleDescriptor corresponding to the 
	 * RoleDescriptor which has the specified ID
	 * 
	 * @param _roleId The wanted RoleDescriptor ID
	 * @return A list of ConcreteRoleDescriptor
	 */
	public List<ConcreteRoleDescriptor> getAllConcreteRoleDescriptorsForARoleDescriptor(String _roleId) {
		List<ConcreteRoleDescriptor> concreteRoleDescriptors = new ArrayList<ConcreteRoleDescriptor>();
		for (Object obj : this.getHibernateTemplate().find("from ConcreteRoleDescriptor ctd where ctd.roleDescriptor.id=?",
				_roleId)) {
			ConcreteRoleDescriptor crd = (ConcreteRoleDescriptor) obj;
			concreteRoleDescriptors.add(crd);
		}
		return concreteRoleDescriptors;
	}

	/* public List<ConcreteTaskDescriptor> getPrimaryConcreteTaskDescriptors(String _roleId) {
     List ctds = this.getHibernateTemplate().find(
     "from ConcreteTaskDescriptor ctd join ctd.concreteRoleDescriptors crds where crds.id=?", _roleId);
     List<ConcreteTaskDescriptor> listCtds = new ArrayList<ConcreteTaskDescriptor>();
     if (ctds.get(0) instanceof List) {
     for (Object o : (ArrayList) ctds.get(0)) {
     if (o instanceof ConcreteTaskDescriptor) {
     ConcreteTaskDescriptor bde = (ConcreteTaskDescriptor) o;
     listCtds.add(bde);
     }
     }
     }
     return listCtds;
     }*/

	/*public String getConcreteRoleDescriptorName(String _concreteRoleDescriptorId) {
     Query query = this.getSession().createQuery(
     "select crd.concreteName from ConcreteRoleDescriptor crd where crd.id='" + _concreteRoleDescriptorId
     + "'");
     List results = query.list();
     if (results.size() == 1)
     return (String) results.get(0);
     return null;
     }*/
}
