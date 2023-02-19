/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) Sebastien BALARD <sbalard@wilos-project.org>
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

package wilos.business.services.misc.concretebreakdownelement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.hibernate.misc.concretebreakdownelement.ConcreteBreakdownElementDao;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;

@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class ConcreteBreakdownElementService {

	private ConcreteBreakdownElementDao concreteBreakdownElementDao;

	/**
	 * Allows to save all the first sons of the concreteBreakdownElement for a
	 * concreteActivity
	 * 
	 * @param _concreteActivity
	 */
	public void saveAllFirstSonsConcreteBreakdownElementsForConcreteActivity(
			ConcreteActivity _concreteActivity) {
		for (ConcreteBreakdownElement cbde : _concreteActivity
				.getConcreteBreakdownElements()) {
			this.concreteBreakdownElementDao
					.saveOrUpdateConcreteBreakdownElement(cbde);
		}
	}

	/**
	 * Allows to get the concreteBreakdownElementDao
	 * 
	 * @return the concreteBreakdownElementDao
	 */
	public ConcreteBreakdownElementDao getConcreteBreakdownElementDao() {
		return concreteBreakdownElementDao;
	}

	/**
	 * Setter of concreteBreakdownElementDao.
	 * 
	 * @param concreteBreakdownElementDao
	 *            The concreteBreakdownElementDao to set.
	 */
	public void setConcreteBreakdownElementDao(
			ConcreteBreakdownElementDao concreteBreakdownElementDao) {
		this.concreteBreakdownElementDao = concreteBreakdownElementDao;
	}

	/**
	 * Allows to get the list of the super concreteActivties
	 * 
	 * @param _cbde
	 * @return the list of the super concreteActivties
	 */
	public List<ConcreteActivity> getSuperConcreteActivitiesUnderList(
			ConcreteBreakdownElement _cbde) {
		this.concreteBreakdownElementDao.getSessionFactory()
				.getCurrentSession().saveOrUpdate(_cbde);
		List<ConcreteActivity> tmp = new ArrayList<ConcreteActivity>();
		for (ConcreteActivity cact : _cbde.getSuperConcreteActivities()) {
			tmp.add(cact);
		}
		return tmp;
	}
}
