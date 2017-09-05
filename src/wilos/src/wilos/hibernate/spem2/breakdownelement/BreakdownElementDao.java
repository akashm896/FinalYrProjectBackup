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

package wilos.hibernate.spem2.breakdownelement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.spem2.breakdownelement.BreakdownElement;

/**
 * BreakdownElementDao manage requests from the system to store BreakdownElement to the database.
 * 
 * @author deder
 */
public class BreakdownElementDao extends HibernateDaoSupport {

    /**
     * Saves or updates a BreakdownElement
     * 
     * @param _bde The BreakdownElement to be saved or updated
     */
    public String saveOrUpdateBreakdownElement(BreakdownElement _bde) {
	if (_bde != null) {
	    this.getHibernateTemplate().saveOrUpdate(_bde);
	    return _bde.getId();
	}
	return "";
    }

    /**
     * Returns a list of all the BreakdownElements
     * 
     * @return A list of all the BreakdownElements
     */
    @SuppressWarnings("unchecked")
    public List<BreakdownElement> getAllBreakdownElements() {
	List<BreakdownElement> bdes = new ArrayList<BreakdownElement>();
	for (Object obj : this.getHibernateTemplate().loadAll(BreakdownElement.class)) {
	    BreakdownElement bde = (BreakdownElement) obj;
	    bdes.add(bde);
	}
	return bdes;
    }

    /**
     * Returns the BreakdownElements which has the specified ID
     * 
     * @param _id The wanted BreakdownElement's ID
     * @return The wanted BreakdownElement
     */
    public BreakdownElement getBreakdownElement(String _id) {
	if (!_id.equals(""))
	    return (BreakdownElement) this.getHibernateTemplate().get(BreakdownElement.class, _id);
	return null;
    }

    /**
     * Deletes the BreakdownElement
     * 
     * @param _bde The BreakdownElement to be deleted
     */
    public void deleteBreakdownElement(BreakdownElement _bde) {
	this.getHibernateTemplate().delete(_bde);
    }
}
