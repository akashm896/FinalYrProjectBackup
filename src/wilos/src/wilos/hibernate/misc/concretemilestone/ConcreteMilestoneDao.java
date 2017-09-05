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
package wilos.hibernate.misc.concretemilestone;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.misc.concretemilestone.ConcreteMilestone;

/**
 * ConcreteMilestoneDao manage requests from the system to store Concrete
 * Milestones to the database
 */
public class ConcreteMilestoneDao extends HibernateDaoSupport {
    /**
     * Saves or updates a ConcreteMilestone
     * 
     * @param _concretemilestone The ConcreteMilestone to be saved or updated
     */
    public String saveOrUpdateConcreteMilestone(
	    ConcreteMilestone _concretemilestone) {
	if (_concretemilestone != null) {
	    this.getHibernateTemplate().saveOrUpdate(_concretemilestone);
	    return _concretemilestone.getId();
	}
	return "";
    }

    /**
     * Returns a list of all the ConcreteMilestones
     * 
     * @return A list of all the ConcreteMilestone
     */
    public List<ConcreteMilestone> getAllConcreteActivities() {
	List<ConcreteMilestone> concreteActivities = new ArrayList<ConcreteMilestone>();
	for (Object obj : this.getHibernateTemplate().loadAll(
		ConcreteMilestone.class)) {
	    ConcreteMilestone ca = (ConcreteMilestone) obj;
	    concreteActivities.add(ca);
	}
	return concreteActivities;
    }

    /**
	 * Tests if there are one or more existing ConcreteMilestones that have the given ID
	 * 
	 * @param _id The wanted ID
	 * @return True or false
	 */
    public boolean existsConcreteMilestone(String _id) {
	List concreteactvities = this.getHibernateTemplate().find(
		"from ConcreteMilestone a where a.id=?", _id);
	return (concreteactvities.size() > 0);
    }

    /**
     * Returns the ConcreteMilestone which has the ID _id
     * 
     * @param _id The wanted ID
     * @return The wanted Concrete Milestone
     */
    public ConcreteMilestone getConcreteMilestone(String _id) {
	if (!_id.equals(""))
	    return (ConcreteMilestone) this.getHibernateTemplate().get(
		    ConcreteMilestone.class, _id);
	return null;
    }

    /**
	 * Returns the ConcreteMilestone which has the given prefix
	 * If there are many ConcreteMilestones with the same prefix, it returns the first of them
	 * 
	 * @param _prefix The wanted prefix
	 * @return The wanted ConcreteMilestone
	 */
    public ConcreteMilestone getConcreteMilestoneFromPrefix(String _prefix) {
	List concreteactvities = this.getHibernateTemplate().find(
		"from ConcreteMilestone a where a.prefix=?", _prefix);
	if (concreteactvities.size() > 0)
	    return (ConcreteMilestone) concreteactvities.get(0);
	else
	    return null;
    }

    /**
     * Deletes the ConcreteMilestone
     * 
     * @param _concretemilestone The ConcreteMilestone to be deleted
     */
    public void deleteConcreteMilestone(ConcreteMilestone _concretemilestone) {
	this.getHibernateTemplate().delete(_concretemilestone);
    }
}