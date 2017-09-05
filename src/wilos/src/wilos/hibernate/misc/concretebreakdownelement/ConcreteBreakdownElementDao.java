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
package wilos.hibernate.misc.concretebreakdownelement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
/**
 * ConcreteBreakdownElementDao manages requests from the system to store
 * ConcreteBreakdownElement to the database.
 */
public class ConcreteBreakdownElementDao extends HibernateDaoSupport {
    /**
     * Saves or updates a ConcreteBreakdownElement
     * 
     * @param _cbde The ConcreteBreakdownElement to be saved
     */
    public String saveOrUpdateConcreteBreakdownElement(ConcreteBreakdownElement _cbde) {
	if (_cbde != null) {
	    this.getHibernateTemplate().saveOrUpdate(_cbde);
	    return _cbde.getId();
	}
	return "";
    }
    /**
     * Returns a list of all the ConcreteBreakdownElements
     * 
     * @return A list of all the ConcreteBreakdownElements
     */
    public List<ConcreteBreakdownElement> getAllConcreteBreakdownElements() {
	List<ConcreteBreakdownElement> concreteBreakdownElements = new ArrayList<ConcreteBreakdownElement>();
	for (Object obj : this.getHibernateTemplate().loadAll(ConcreteBreakdownElement.class)) {
	    ConcreteBreakdownElement cbde = (ConcreteBreakdownElement) obj;
	    concreteBreakdownElements.add(cbde);
	}
	return concreteBreakdownElements;
    }
    /**
     * Returns the ConcreteBreakdownElements which has the ID _id.
     * 
     * @param _id The wanted ID
     * @return The wanted ConcreteBreakdownElement
     */
    public ConcreteBreakdownElement getConcreteBreakdownElement(String _id) {
	if (!_id.equals(""))
	    return (ConcreteBreakdownElement) this.getHibernateTemplate().get(ConcreteBreakdownElement.class, _id);
	return null;
    }
    /**
     * Deletes the ConcreteBreakdownElement.
     * 
     * @param _cbde The ConcreteBreakdownElement to be deleted
     */
    public void deleteConcreteBreakdownElement(ConcreteBreakdownElement _cbde) {
	this.getHibernateTemplate().delete(_cbde);
    }

    /*public List<ConcreteBreakdownElement> getAllConcreteBreakdownElementsFromProject(String _projectId) {
	List crds = this.getHibernateTemplate().find("from ConcreteBreakdownElement cbde where cbde.project.id=?",
		_projectId);
	return crds;
    }*/
}