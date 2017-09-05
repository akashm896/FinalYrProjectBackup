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

package wilos.hibernate.spem2.phase;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.spem2.phase.Phase;

/**
 * MilestoneDao manage requests from the system to store Milestones into the database
 * 
 * @author Soosuske
 * 
 */
public class PhaseDao extends HibernateDaoSupport {
    /**
     * Saves or updates a Phase
     * 
     * @param _phase The Phase to be saved or updated
     */
    public String saveOrUpdatePhase(Phase _phase) {
	if (_phase != null) {
	    this.getHibernateTemplate().saveOrUpdate(_phase);
	    return _phase.getId();
	}
	return "";
    }

    /**
     * Returns a list of all the Phases
     * 
     * @return A list of all the Phases
     */
    public List<Phase> getAllPhases() {
	List<Phase> phases = new ArrayList<Phase>();
	for (Object obj : this.getHibernateTemplate().loadAll(Phase.class)) {
	    Phase ph = (Phase) obj;
	    phases.add(ph);
	}
	return phases;
    }

    /**
     * Returns the Phase which has the specified ID
     * 
     * @param _id The wanted Phase's ID
     * @return The wanted Phase
     */
    public Phase getPhase(String _id) {
	if (!_id.equals(""))
	    return (Phase) this.getHibernateTemplate().get(Phase.class, _id);
	return null;
    }

    /**
     * Deletes the Phase
     * 
     * @param _phase The Phase to be deleted
     */
    public void deletePhase(Phase _phase) {
	this.getHibernateTemplate().delete(_phase);
    }
}
