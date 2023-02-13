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

package wilos.hibernate.spem2.activity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.spem2.activity.Activity;

/**
 * ActivityDao manage requests from the system to store Activities to the
 * database
 * 
 * @author garwind
 * @author deder
 */
public class ActivityDao extends HibernateDaoSupport {

    /**
     * Saves or updates an Activity
     * 
     * @param _activity The Activity to be saved or updated
     */
    public String saveOrUpdateActivity(Activity _activity) {
	if (_activity != null) {
	    this.getHibernateTemplate().saveOrUpdate(_activity);
	    return _activity.getId();
	}
	return "";
    }

    /**
     * Returns a list of all the Activities
     * 
     * @return A list of all the Activities
     */
    public List<Activity> getAllActivities() {
	List<Activity> activities = new ArrayList<Activity>();
	for (Object obj : this.getHibernateTemplate().loadAll(Activity.class)) {
	    Activity act = (Activity) obj;
	    activities.add(act);
	}
	return activities;
    }

    /**
     * Returns the Activity which has the specified ID
     * 
     * @param _id The wanted Activity's ID
     * @return The wanted Activity
     */
    public Activity getActivity(String _id) {
	if (!_id.equals(""))
	    return (Activity) this.getHibernateTemplate().get(Activity.class, _id);
	return null;
    }

    /**
     * Returns an Activity  with the given guid If there are many activities with
     * the same guid, it returns the first
     * 
     * @param _guid the given guidance id
     * @return the wanted Activity
     */
    public Activity getActivityFromGuid(String _guid) {
	if (!_guid.equals("")) {
	    List activities = this.getHibernateTemplate().find("from Activity a where a.guid=?", _guid);
	    if (activities.size() > 0)
		return (Activity) activities.get(0);
	}
	return null;
    }

    /**
     * Returns the Activity which has the given prefix
     * If there are many Activities with the same prefix, it returns the first
     * 
     * @param _prefix The wanted Activity's prefix
     * @return The wanted Activity
     */
    public Activity getActivityFromPrefix(String _prefix) {
	if (!_prefix.equals("")) {
	    List actvities = this.getHibernateTemplate().find("from Activity a where a.prefix=?", _prefix);
	    if (actvities.size() > 0)
		return (Activity) actvities.get(0);
	}
	return null;
    }

    /**
     * Deletes the Activity
     * 
     * @param _activity The Activity to be deleted
     */
    public void deleteActivity(Activity _activity) {
	this.getHibernateTemplate().delete(_activity);
    }
}
