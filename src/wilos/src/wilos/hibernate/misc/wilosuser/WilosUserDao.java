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

package wilos.hibernate.misc.wilosuser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.misc.wilosuser.WilosUser;

/**
 * WilosUserDao manage requests from the system to store WilosUser into the
 * database.
 */
public class WilosUserDao extends HibernateDaoSupport {

    /**
     * Saves or updates a WilosUser
     * 
     * @param _wilosuser The WilosUser to be saved or updated
     */
    public String saveOrUpdateWilosUser(WilosUser _wilosUser) {
	if (_wilosUser != null) {
	    this.getHibernateTemplate().saveOrUpdate(_wilosUser);
	    return _wilosUser.getId();
	}
	return "";
    }

    /**
     * Returns a list of all the WilosUsers
     * 
     * @return A list of all the WilosUsers
     */
    public List<WilosUser> getAllWilosUsers() {
	List<WilosUser> wilosUsers = new ArrayList<WilosUser>();
	for (Object obj : this.getHibernateTemplate().loadAll(WilosUser.class)) {
	    WilosUser wo = (WilosUser) obj;
	    wilosUsers.add(wo);
	}
	return wilosUsers;
    }

    /**
     * Returns the WilosUser which have the specified ID
     * 
     * @param _id The wanted WilosUser's ID
     * @return The wanted WilosUser
     */
    public WilosUser getWilosUser(String _login) {
		List wilosUsers = this.getHibernateTemplate().find("from WilosUser wu where wu.login=?", _login);
		if (wilosUsers.size() > 0) {
			return (WilosUser) wilosUsers.get(0);
		} else {
		    return null;
		}
    }
    
    /**
     * Returns the WilosUser which have the specified ID
     * 
     * @param _id The wanted WilosUser's ID
     * @return The wanted WilosUser
     */
    public WilosUser getWilosUserById(String _id) {
		List wilosUsers = this.getHibernateTemplate().find("from WilosUser wu where wu.id=?", _id);
		if (wilosUsers.size() > 0) {
		    return (WilosUser) wilosUsers.get(0);
		} else {
		    return null;
		}
    }
    
    /**
     * Get user by his login
     * @param _login the login
     * @return user
     */
    public WilosUser getUserByLogin(String _login) {
	
		List wilosUsers = this.getHibernateTemplate().find("from WilosUser wu where wu.login=?", _login);
		if (wilosUsers.size() > 0) {
		    return (WilosUser) wilosUsers.get(0);
		} else {
		    return null;
		}
    }
    
    /**
     * Get user by his email
     * @param _email the email
     * @return user
     */
    public WilosUser getUserByEmail(String _email) {

	List wilosUsers = this.getHibernateTemplate().find(
		"from WilosUser wu where wu.emailAddress=?", _email);
	if (wilosUsers.size() > 0) {
	    return (WilosUser) wilosUsers.get(0);
	} else {
	    return null;
	}
    }

    /**
     * Returns the list of WilosUser in function of the role
     * @param _id the role of users
     * @return the list of wilos user
     */
    public List<WilosUser> getWilosUserByRole(String _id){
    	return this.getHibernateTemplate().find("from WilosUser wu where wu.role_id=?", _id);
    }

    /**
     * Deletes the WilosUser
     * 
     * @param _wilosuser The WilosUser to be deleted
     */
    public void deleteWilosUser(WilosUser _wilosuser) {
    	this.getHibernateTemplate().delete(_wilosuser);
    }
}
