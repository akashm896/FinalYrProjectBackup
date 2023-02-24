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

package wilos.hibernate.spem2.milestone;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.spem2.milestone.Milestone;

/**
 * MilestoneDao manage requests from the system to store Milestones into the
 * database
 * 
 */
public class MilestoneDao extends HibernateDaoSupport {

    /**
     * Saves or updates a Milestone
     * 
     * @param _milestone The Milestone to be saved or updated
     */
    public String saveOrUpdateMilestone(Milestone _milestone) {
	if (_milestone != null) {
	    this.getHibernateTemplate().saveOrUpdate(_milestone);
	    return _milestone.getId();
	}
	return "";
    }

    /**
     * Returns a list of all the Milestones
     * 
     * @return A list of all the Milestones
     */
    public List<Milestone> getAllMilestones() {
	List<Milestone> milestones = new ArrayList<Milestone>();
	for (Object obj : this.getHibernateTemplate().loadAll(Milestone.class)) {
	    Milestone ms = (Milestone) obj;
	    milestones.add(ms);
	}
	return milestones;
    }

    /**
     * Returns the Milestone which has the specified ID
     * 
     * @param _id The wanted Milestone's ID
     * @return The wanted Milestone
     */
    public Milestone getMilestone(String _id) {
	if (!_id.equals(""))
	    return (Milestone) this.getHibernateTemplate().get(Milestone.class,
		    _id);
	return null;
    }

    /**
     * Return a milestone with the given guid If there are many milestones with
     * the same guid, it returns the first
     * 
     * @param _guid the given guidance id
     * @return the wanted Milestone
     */
    public Milestone getMilestoneFromGuid(String _guid) {
	if (!_guid.equals("")) {
	    List milestones = this.getHibernateTemplate().find(
		    "from Milestone a where a.guid=?", _guid);
	    if (milestones.size() > 0)
		return (Milestone) milestones.get(0);
	}
	return null;
    }

    /**
     * Returns the Milestone which has the specified prefix
     * If there are many Milestones with the same prefix, it returns the first
     * 
     * @param _prefix The wanted Milestone's prefix
     * @return The wanted Milestone
     */
    public Milestone getMilestoneFromPrefix(String _prefix) {
	if (!_prefix.equals("")) {
	    List milestones = this.getHibernateTemplate().find(
		    "from Milestone a where a.prefix=?", _prefix);
	    if (milestones.size() > 0)
		return (Milestone) milestones.get(0);
	}
	return null;
    }

    /**
     * Deletes the Milestone
     * 
     * @param _milestone The Milestone to be deleted
     */
    public void deleteMilestone(Milestone _milestone) {
	this.getHibernateTemplate().delete(_milestone);
    }
}
