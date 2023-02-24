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

package wilos.business.services.misc.concretephase;

import java.util.HashSet;
import java.util.Set;

import wilos.hibernate.misc.concretephase.ConcretePhaseDao;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concretephase.ConcretePhase;

public class ConcretePhaseService {

	private ConcretePhaseDao concretePhaseDao;

	/**
	 * Allows to get the concretePhase with its id
	 * 
	 * @param _concretePhaseId
	 * @return the phase which has the same concretePhaseId
	 */
	public ConcretePhase getConcretePhase(String _concretePhaseId) {
		return this.getConcretePhaseDao().getConcretePhase(_concretePhaseId);
	}

	/**
	 * Allows to save the concretePhase
	 * 
	 * @param _concretePhase
	 */
	public void saveConcretePhase(ConcretePhase _concretePhase) {
		this.concretePhaseDao.saveOrUpdateConcretePhase(_concretePhase);
	}

	/**
	 * Allows to get the set of all concreteBreakdownElement
	 * 
	 * @param _concretePhase
	 * @return the set of all concreteBreakdownElement
	 */
	public Set<ConcreteBreakdownElement> getAllConcreteBreakdownElements(
			ConcretePhase _concretePhase) {
		Set<ConcreteBreakdownElement> tmp = new HashSet<ConcreteBreakdownElement>();

		this.getConcretePhaseDao().getSessionFactory().getCurrentSession()
				.saveOrUpdate(_concretePhase);
		for (ConcreteBreakdownElement element : _concretePhase
				.getConcreteBreakdownElements()) {
			tmp.add(element);
		}
		return tmp;
	}

	/**
	 * Allows to get the concretePhaseDao
	 * 
	 * @return the concretePhaseDao
	 */
	public ConcretePhaseDao getConcretePhaseDao() {
		return concretePhaseDao;
	}

	/**
	 * Allows to set the concretePhaseDao
	 * 
	 * @param concretePhaseDao
	 *            the concretePhaseDao to set
	 */
	public void setConcretePhaseDao(ConcretePhaseDao concretePhaseDao) {
		this.concretePhaseDao = concretePhaseDao;
	}
}
