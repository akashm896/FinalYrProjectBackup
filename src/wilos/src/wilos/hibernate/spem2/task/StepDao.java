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

package wilos.hibernate.spem2.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.spem2.task.Step;

/**
 * A Step is a Section and Work Definition that is used to organize Tasks into parts or subunits of
 * work
 * 
 * StepDao manage requests from the system to store Steps into the database
 * 
 * @author garwind
 */
public class StepDao extends HibernateDaoSupport {

    /**
     * Saves or updates a Step
     * 
     * @param _step The Step to be saved or updated
     */
    public String saveOrUpdateStep(Step _step) {
	if (_step != null) {
	    this.getHibernateTemplate().saveOrUpdate(_step);
	    return _step.getId();
	}
	return "";
    }

    /**
     * Returns a list of all the Steps
     * 
     * @return A list of all the Steps
     */
    public List<Step> getAllSteps() {
	List<Step> steps = new ArrayList<Step>();
	for (Object obj : this.getHibernateTemplate().loadAll(Step.class)) {
	    Step s = (Step) obj;
	    steps.add(s);
	}
	return steps;
    }

    /**
     * Returns the Step which has the specified ID
     * 
     * @param _id The wanted Step's ID
     * @return The wanted Step
     */
    public Step getStep(String _id) {
	if (!_id.equals(""))
	    return (Step) this.getHibernateTemplate().get(Step.class, _id);
	return null;
    }

    /**
     * Deletes a Step
     * 
     * @param _step The Step be deleted
     */
    public void deleteStep(Step _step) {
	this.getHibernateTemplate().delete(_step);
    }
}
