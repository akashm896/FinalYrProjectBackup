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

package wilos.hibernate.misc.dailyremainingtime;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.misc.dailyremainingtime.DailyRemainingTime;

/**
 * 
 * DailyRemainingTimeDao manage requests from the system to store
 * DailyRemainingTimes to the database
 * 
 */
public class DailyRemainingTimeDao extends HibernateDaoSupport {

	/**
     * Saves or updates a DailyRemainingTime
     * 
     * @param _dailyRemainingTime The DailyRemainingTime to be saved or updated
     * @return The DailyRemainingTime's ID, if the save has been done successfully
     */
    public String saveOrUpdateDailyRemainingTime(
	    DailyRemainingTime _dailyRemainingTime) {
	if (_dailyRemainingTime != null) {
	    this.getHibernateTemplate().saveOrUpdate(_dailyRemainingTime);
	    return _dailyRemainingTime.getId();
	}
	return null;
    }

    /**
	 * Returns the DailyRemainingTime which has the specified ID
	 * 
	 * @param _id The wanted DailyRemainingTime ID
	 * @return The wanted DailyRemainingTime
	 */
    public Integer getDailyRemainingTime(Integer _id) {
	if (_id != null)
	    return (Integer) this.getHibernateTemplate().get(
		    DailyRemainingTime.class, _id);
	return null;
    }

    /**
	 * Deletes the DailyRemainingTime
	 * 
	 * @param _dailyRemainingTime The DailyRemainingTime to be deleted
	 */
    public void deleteDailyRemainingTime(DailyRemainingTime _dailyRemainingTime) {
	this.getHibernateTemplate().delete(_dailyRemainingTime);
    }

    /**
	 * Returns a list of all the DailyRemainingTime
	 * 
	 * @return A list of all the DailyRemainingTime
	 */
    public List<DailyRemainingTime> getDailyRemainingTime() {
	List<DailyRemainingTime> DailyRemainingTimes = new ArrayList<DailyRemainingTime>();
	for (Object obj : this.getHibernateTemplate().loadAll(
		DailyRemainingTime.class)) {
	    DailyRemainingTime drt = (DailyRemainingTime) obj;
	    DailyRemainingTimes.add(drt);
	}
	return DailyRemainingTimes;
    }
}
