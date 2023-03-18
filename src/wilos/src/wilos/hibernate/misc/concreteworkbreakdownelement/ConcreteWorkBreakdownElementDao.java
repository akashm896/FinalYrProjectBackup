/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

package wilos.hibernate.misc.concreteworkbreakdownelement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;

/**
 * @author Sylvain
 * 
 * ConcreteWorkBreakdownElementDao manage requests from the system to store
 * WorkBreakdownElements to the database
 * 
 */

public class ConcreteWorkBreakdownElementDao extends HibernateDaoSupport {
    /**
     * Saves or updates a ConcreteWorkBreakdownElement
     * 
     * @param _concreteWorkBreakdownElement The ConcreteWorkBreakdownElement to be saved or updated
     */
    public String saveOrUpdateConcreteWorkBreakdownElement(
	    ConcreteWorkBreakdownElement _concreteworkBreakdownElement) {
	if (_concreteworkBreakdownElement != null) {
	    this.getHibernateTemplate().saveOrUpdate(
		    _concreteworkBreakdownElement);
	    return _concreteworkBreakdownElement.getId();
	}
	return "";
    }

    /**
     * Returns a list of all the ConcreteWorkBreakdownElements
     * 
     * @return A list of all the ConcreteWorkBreakdownElements
     */
    public List<ConcreteWorkBreakdownElement> getAllConcreteWorkBreakdownElements() {
	List<ConcreteWorkBreakdownElement> concreteWorkBreakdownElement = new ArrayList<ConcreteWorkBreakdownElement>();
	for (Object obj : this.getHibernateTemplate().loadAll(
		ConcreteWorkBreakdownElement.class)) {
	    ConcreteWorkBreakdownElement wbde = (ConcreteWorkBreakdownElement) obj;
	    concreteWorkBreakdownElement.add(wbde);
	}
	return concreteWorkBreakdownElement;
    }

    /**
     * Returns the ConcreteWorkBreakdownElement which has the specified ID
     * 
     * @param _id The wanted ConcreteWorkBreakdownElement ID
     * @return The wanted ConcreteWorkBreakdownElement
     */
    public ConcreteWorkBreakdownElement getConcreteWorkBreakdownElement(
	    String _id) {
	if (!_id.equals(""))
	    return (ConcreteWorkBreakdownElement) this.getHibernateTemplate()
		    .get(ConcreteWorkBreakdownElement.class, _id);
	return null;
    }

    /**
     * Deletes the ConcreteWorkBreakdownElement
     * 
     * @param _concreteWorkBreakdownElement The ConcreteWorkBreakdownElement to be deleted
     */
    public void deleteConcreteWorkBreakdownElement(
	    ConcreteWorkBreakdownElement _concreteworkBreakdownElement) {
	this.getHibernateTemplate().delete(_concreteworkBreakdownElement);
    }
}
