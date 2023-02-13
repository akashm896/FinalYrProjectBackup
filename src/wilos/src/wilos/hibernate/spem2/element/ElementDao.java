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

package wilos.hibernate.spem2.element;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.spem2.element.Element;

/**
 * ElementDao manage requests from the system to store Elements into the database.
 * 
 * @author deder
 */
public class ElementDao extends HibernateDaoSupport {

    /**
     * Saves or updates an Element
     * 
     * @param _element The Element to be saved or updated
     */
    public String saveOrUpdateElement(Element _element) {
	if (_element != null) {
	    this.getHibernateTemplate().saveOrUpdate(_element);
	    return _element.getId();
	}
	return "";
    }

    /**
     * Returns a list of all the Elements
     * 
     * @return A list of all the Elements
     */
    public List<Element> getAllElements() {
	List<Element> elements = new ArrayList<Element>();
	for (Object obj : this.getHibernateTemplate().loadAll(Element.class)) {
	    Element e = (Element) obj;
	    elements.add(e);
	}
	return elements;
    }

    /**
     * Returns the Element which has the specified ID
     * 
     * @param _id The wanted Element's ID
     * @return The wanted Element
     */
    public Element getElement(String _id) {
	if (!_id.equals(""))
	    return (Element) this.getHibernateTemplate().get(Element.class, _id);
	return null;
    }

    /**
     * Deletes an Element
     * 
     * @param _element The Element to be deleted
     */
    public void deleteElement(Element _element) {
	this.getHibernateTemplate().delete(_element);
    }
}
