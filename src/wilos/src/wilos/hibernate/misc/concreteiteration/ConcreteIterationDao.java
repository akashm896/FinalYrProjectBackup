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

package wilos.hibernate.misc.concreteiteration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.misc.concreteiteration.ConcreteIteration;

/**
 * ConcreteIterationDao manage requests from the system to store iterations to the
 * database
 */
public class ConcreteIterationDao extends HibernateDaoSupport {
    /**
     * Saves or updates an ConcreteIteration
     *
     * @param _ConcreteIteration The ConcreteIteration to be saved or updated
     */
    public String saveOrUpdateConcreteIteration(ConcreteIteration _concreteIteration) {
	if (_concreteIteration != null) {
	    this.getHibernateTemplate().saveOrUpdate(_concreteIteration);
	    return _concreteIteration.getId();
	}
	return "";
    }

    /**
     * Returns a list of all the ConcreteIterations
     *
     * @return A list of all the ConcreteIterations
     */
    public List<ConcreteIteration> getAllConcreteIterations() {
	List<ConcreteIteration> concreteIterations = new ArrayList<ConcreteIteration>();
	for (Object obj : this.getHibernateTemplate().loadAll(ConcreteIteration.class)) {
	    ConcreteIteration ci = (ConcreteIteration) obj;
	    concreteIterations.add(ci);
	}
	return concreteIterations;
    }

    /*public List<ConcreteBreakdownElement> getBreakdownElementsFromConcreteIteration(String _concreteIterationId) {
	List bdes = this.getHibernateTemplate().find(
		"from BreakdownElement bde join bde.superActivities s where s.id=?", _concreteIterationId);
	List<ConcreteBreakdownElement> listBdes = new ArrayList<ConcreteBreakdownElement>();
	if (bdes.get(0) instanceof List) {
	    for (Object o : (ArrayList) bdes.get(0)) {
		if (o instanceof ConcreteBreakdownElement) {
		    ConcreteBreakdownElement bde = (ConcreteBreakdownElement) o;
		    listBdes.add(bde);
		}
	    }
	}
	return listBdes;
    }*/

    /**
     * Returns the ConcreteIteration which has the ID _id
     *
     * @param _id The wanted ID
     * @return The wanted ConcreteIteration
     */
    public ConcreteIteration getConcreteIteration(String _id) {
	if (!_id.equals(""))
	    return (ConcreteIteration) this.getHibernateTemplate().get(ConcreteIteration.class, _id);
	return null;
    }

    /**
     * Deletes the ConcreteIteration
     *
     * @param _ConcreteIteration The ConcreteIteration to be deleted
     */
    public void deleteConcreteIteration(ConcreteIteration _concreteIteration) {
	this.getHibernateTemplate().delete(_concreteIteration);
    }

    /*public String getConcreteIterationName(String _concreteIterationId) {
	Query query = this.getSession().createQuery(
		"select ca.concreteName from ConcreteIteration ca where ca.id='" + _concreteIterationId + "'");
	List results = query.list();
	if (results.size() == 1)
	    return (String) results.get(0);
	return null;
    }*/
}
