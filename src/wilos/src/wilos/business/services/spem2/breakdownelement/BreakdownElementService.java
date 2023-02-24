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

package wilos.business.services.spem2.breakdownelement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.hibernate.misc.concretebreakdownelement.ConcreteBreakdownElementDao;
import wilos.hibernate.spem2.breakdownelement.BreakdownElementDao;
import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.breakdownelement.BreakdownElement;

/**
 * BreakdownElement is a transactional class that manages operations about
 * breakdownelement,
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
@SuppressWarnings("unused")
public class BreakdownElementService {

	private ConcreteBreakdownElementDao concreteBreakdownElementDao;

	private BreakdownElementDao breakdownElementDao;

	/**
	 * 
	 * @param _act
	 * @return
	 */
	public List<Activity> getSuperActivitiesUnderList(BreakdownElement _bde) {
		this.breakdownElementDao.getSessionFactory().getCurrentSession().saveOrUpdate(_bde);
		List<Activity> tmp = new ArrayList<Activity>();
		for (Activity act : _bde.getSuperActivities()) {
			tmp.add(act);
		}
		return tmp;
	}
	
	/**
	 * 
	 * @param _act
	 * @return
	 */
	public Set<Activity> getSuperActivities(BreakdownElement _bde) {
		this.breakdownElementDao.getSessionFactory().getCurrentSession().saveOrUpdate(_bde);
		Set<Activity> tmp = new HashSet<Activity>();
		for (Activity act : _bde.getSuperActivities()) {
			tmp.add(act);
		}
		return tmp;
	}

	/**
	 * Getter of concreteBreakdownElementDao
	 * 
	 * @return the concreteBreakdownElementDao
	 */
	public ConcreteBreakdownElementDao getConcreteBreakdownElementDao() {
		return concreteBreakdownElementDao;
	}

	/**
	 * Setter of concreteBreakdownElementDao
	 * 
	 * @param concreteBreakdownElementDao
	 *            the concreteBreakdownElementDao to set
	 */
	public void setConcreteBreakdownElementDao(
			ConcreteBreakdownElementDao concreteBreakdownElementDao) {
		this.concreteBreakdownElementDao = concreteBreakdownElementDao;
	}

	public BreakdownElementDao getBreakdownElementDao() {
		return breakdownElementDao;
	}

	public void setBreakdownElementDao(BreakdownElementDao breakdownElementDao) {
		this.breakdownElementDao = breakdownElementDao;
	}

}
