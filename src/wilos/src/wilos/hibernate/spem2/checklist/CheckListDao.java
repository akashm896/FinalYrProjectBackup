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

package wilos.hibernate.spem2.checklist;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.spem2.checklist.CheckList;

/**
 * CheckListDao manage requests from the system to store CheckLists into the database.
 */
public class CheckListDao extends HibernateDaoSupport {

    /**
     * Saves or updates a CheckList
     * 
     * @param _checklist The CheckList to be saved or updated
     */
    public String saveOrUpdateCheckList(CheckList _checklist) {
	if (_checklist != null) {
	    this.getHibernateTemplate().saveOrUpdate(_checklist);
	    return _checklist.getId();
	}
	return "";
    }

    /**
     * Returns a list of all the CheckLists
     * 
     * @return A list of all the CheckLists
     */
    @SuppressWarnings("unchecked")
    public List<CheckList> getAllCheckLists() {
	List<CheckList> checkLists = new ArrayList<CheckList>();
	for (Object obj : this.getHibernateTemplate().loadAll(CheckList.class)) {
	    CheckList cl = (CheckList) obj;
	    checkLists.add(cl);
	}
	return checkLists;
    }

    /**
     * Returns the CheckList which has the specified ID
     * 
     * @param _id The wanted CheckList's ID
     * @return The wanted CheckList
     */
    public CheckList getCheckList(String _id) {
	if (!_id.equals(""))
	    return (CheckList) this.getHibernateTemplate().get(CheckList.class, _id);
	return null;
    }

    /**
     * Deletes a CheckList
     * 
     * @param _checklist The CheckList to be deleted
     */
    public void deleteCheckList(CheckList _checklist) {
	this.getHibernateTemplate().delete(_checklist);
    }
}
