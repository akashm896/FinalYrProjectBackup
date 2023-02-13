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

package wilos.hibernate.spem2.workbreakdownelement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.spem2.workbreakdownelement.WorkBreakdownElement;

/**
 * @author Sebastien
 * 
 * WorkBreakdownElementDao manage requests from the system to store WorkBreakdownElements into the
 * database
 * 
 */
public class WorkBreakdownElementDao extends HibernateDaoSupport {

    /**
     * Saves or updates a WorkBreakdownElement
     * 
     * @param _workBreakdownElement The WorkBreakdownElement to be saved or updated
     */
    public String saveOrUpdateWorkBreakdownElement(WorkBreakdownElement _workBreakdownElement) {
	if (_workBreakdownElement != null) {
	    this.getHibernateTemplate().saveOrUpdate(_workBreakdownElement);
	    return _workBreakdownElement.getId();
	}
	return "";
    }

    /**
     * Returns a list of all the WorkBreakdownElements
     * 
     * @return A list of all the WorkBreakdownElements
     */
    public List<WorkBreakdownElement> getAllWorkBreakdownElements() {
	List<WorkBreakdownElement> workBreakdownElements = new ArrayList<WorkBreakdownElement>();
	for (Object obj : this.getHibernateTemplate().loadAll(WorkBreakdownElement.class)) {
	    WorkBreakdownElement wbde = (WorkBreakdownElement) obj;
	    workBreakdownElements.add(wbde);
	}
	return workBreakdownElements;
    }

    /**
     * Returns the WorkBreakdownElement which has the specified ID
     * 
     * @param _id The wanted WorkBreakdownElement's ID
     * @return The wanted WorkBreakdownElement
     */
    public WorkBreakdownElement getWorkBreakdownElement(String _id) {
	if (!_id.equals(""))
	    return (WorkBreakdownElement) this.getHibernateTemplate().get(WorkBreakdownElement.class, _id);
	return null;
    }

    /**
     * Deletes a WorkBreakdownElement
     * 
     * @param _workBreakdownElement The WorkBreakdownElement to be deleted
     */
    public void deleteWorkBreakdownElement(WorkBreakdownElement _workBreakdownElement) {
	this.getHibernateTemplate().delete(_workBreakdownElement);
    }
}
