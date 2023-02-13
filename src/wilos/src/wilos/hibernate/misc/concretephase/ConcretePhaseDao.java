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

package wilos.hibernate.misc.concretephase;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.misc.concretephase.ConcretePhase;

/**
 * 
 * @author Soosuske
 * 
 */
public class ConcretePhaseDao extends HibernateDaoSupport {
    /**
     * Saves or updates a ConcretePhase
     * 
     * @param _concretePhase The ConcretePhase to be saved or updated
     */
    public String saveOrUpdateConcretePhase(ConcretePhase _concretePhase) {
	if (_concretePhase != null) {
	    this.getHibernateTemplate().saveOrUpdate(_concretePhase);
	    return _concretePhase.getId();
	}
	return "";
    }

    /**
     * Returns a list of all the ConcretePhases
     * 
     * @return A list of all the ConcretePhases
     */
    public List<ConcretePhase> getAllConcretePhases() {
	List<ConcretePhase> concretePhases = new ArrayList<ConcretePhase>();
	for (Object obj : this.getHibernateTemplate().loadAll(ConcretePhase.class)) {
	    ConcretePhase cp = (ConcretePhase) obj;
	    concretePhases.add(cp);
	}
	return concretePhases;
    }

    /**
     * Returns the Concrete Phase which has the ID _id
     * 
     * @param _id The wanted ID
     * @return The wanted ConcretePhase
     */
    public ConcretePhase getConcretePhase(String _id) {
	if (!_id.equals(""))
	    return (ConcretePhase) this.getHibernateTemplate().get(ConcretePhase.class, _id);
	return null;
    }

    /*public List<BreakdownElement> getBreakdownElementsFromPhase(String _concretePhaseId) {
	List bdes = this.getHibernateTemplate().find(
		"from BreakdownElement bde join bde.superActivities s where s.id=?", _concretePhaseId);
	List<BreakdownElement> listBdes = new ArrayList<BreakdownElement>();
	if (bdes.get(0) instanceof List) {
	    for (Object o : (ArrayList) bdes.get(0)) {
		if (o instanceof BreakdownElement) {
		    BreakdownElement bde = (BreakdownElement) o;
		    listBdes.add(bde);
		}
	    }
	}
	return listBdes;
    }*/

    /**
     * Deletes the ConcretePhase
     * 
     * @param _concretePhase The ConcretePhase to be deleted
     */
    public void deleteConcretePhase(ConcretePhase _concretePhase) {
	this.getHibernateTemplate().delete(_concretePhase);
    }

    /*public String getConcretePhaseName(String _concretePhaseId) {
	Query query = this.getSession().createQuery(
		"select cp.concreteName from ConcretePhase cp where cp.id='" + _concretePhaseId + "'");
	List results = query.list();
	if (results.size() == 1)
	    return (String) results.get(0);
	return null;
    }*/
}
