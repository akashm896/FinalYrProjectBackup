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

package wilos.hibernate.spem2.guide;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.spem2.guide.Guidance;

/**
 * GuidanceDao manage requests from the system to store Guidances into the database.
 */
public class GuidanceDao extends HibernateDaoSupport {

    /**
     * Saves or updates a Guidance
     * 
     * @param _guidance The Guidance to be saved or updated
     */
    public String saveOrUpdateGuidance(Guidance _guidance) {
	if (_guidance != null) {
	    this.getHibernateTemplate().saveOrUpdate(_guidance);
	    return _guidance.getId();
	}
	return "";
    }

    /**
     * Returns the Guidance with the given guid
     * If there are many process with the same guid, it returns the first
     * 
     * @param _guid the given guidance id
     * @return the wanted guidance
     */
    public Guidance getGuidanceFromGuid(String _guid) {
	if (!_guid.equals("")) {
	    List guidances = this.getHibernateTemplate().find("from Guidance g where g.guid=?", _guid);
	    if (guidances.size() > 0)
		return (Guidance) guidances.get(0);
	}
	return null;
    }

    /**
     * Returns a list of all the Guidances
     * 
     * @return A list of all the Guidances
     */
    public List<Guidance> getAllGuidances() {
	List<Guidance> guidances = new ArrayList<Guidance>();
	for (Object obj : this.getHibernateTemplate().loadAll(Guidance.class)) {
	    Guidance ph = (Guidance) obj;
	    guidances.add(ph);
	}
	return guidances;
    }

    /**
     * Returns the Guidance which has the specified ID
     * 
     * @param _id The wanted Guidance's ID
     * @return The wanted Guidance
     */
    public Guidance getGuidance(String _id) {
	if (!_id.equals(""))
	    return (Guidance) this.getHibernateTemplate().get(Guidance.class, _id);
	return null;
    }

    /**
     * Deletes a Guidance
     * 
     * @param _guidance The Guidance to be deleted
     */
    public void deleteGuidance(Guidance _guidance) {
	this.getHibernateTemplate().delete(_guidance);
    }
}
