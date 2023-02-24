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

package wilos.hibernate.spem2.iteration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.spem2.iteration.Iteration;

/**
 * IterationDao manage requests from the system to store Iterations into the
 * database
 * 
 * @author soosuske
 */
public class IterationDao extends HibernateDaoSupport {

    /**
     * Saves or updates an Iteration
     * 
     * @param _iteration The Iteration to be saved or updated
     */
    public String saveOrUpdateIteration(Iteration _iteration) {
	if (_iteration != null) {
	    this.getHibernateTemplate().saveOrUpdate(_iteration);
	    return _iteration.getId();
	}
	return "";
    }

    /**
     * Returns a list of all the Iterations
     * 
     * @return A list of all the Iterations
     */
    public List<Iteration> getAllIterations() {
	List<Iteration> iterations = new ArrayList<Iteration>();
	for (Object obj : this.getHibernateTemplate().loadAll(Iteration.class)) {
	    Iteration ph = (Iteration) obj;
	    iterations.add(ph);
	}
	return iterations;
    }

    /**
     * Returns the Iteration which has the specified ID
     * 
     * @param _id The wanted Iteration's ID
     * @return The wanted Iteration
     */
    public Iteration getIteration(String _id) {
	if (!_id.equals(""))
	    return (Iteration) this.getHibernateTemplate().get(Iteration.class, _id);
	return null;
    }

    /**
     * Deletes the Iteration
     * 
     * @param _iteration The Iteration to be deleted
     */
    public void deleteIteration(Iteration _iteration) {
	this.getHibernateTemplate().delete(_iteration);
    }
}
