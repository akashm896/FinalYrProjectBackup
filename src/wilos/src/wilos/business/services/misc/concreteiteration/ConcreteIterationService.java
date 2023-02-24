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

package wilos.business.services.misc.concreteiteration;

import java.util.HashSet;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.hibernate.misc.concreteiteration.ConcreteIterationDao;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concreteiteration.ConcreteIteration;

@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class ConcreteIterationService {

	public ConcreteIterationDao concreteIterationDao;

	/**
	 * Allows to get the concreteIteration with the id
	 * 
	 * @param _concreteIterationId
	 * @return the concreteIteration
	 */
	public ConcreteIteration getConcreteIteration(String _concreteIterationId) {
		return this.getConcreteIterationDao().getConcreteIteration(
				_concreteIterationId);
	}

	/**
	 * Allows to save the concreteIteration
	 * 
	 * @param _concreteIteration
	 */
	public void saveConcreteIteration(ConcreteIteration _concreteIteration) {
		this.concreteIterationDao
				.saveOrUpdateConcreteIteration(_concreteIteration);
	}

	/**
	 * Allows to get the set of all concreteBreakdownElement
	 * 
	 * @param _concreteIteration
	 * @return the set of all concreteBreakdownElement
	 */
	public Set<ConcreteBreakdownElement> getAllConcreteBreakdownElements(
			ConcreteIteration _concreteIteration) {
		Set<ConcreteBreakdownElement> tmp = new HashSet<ConcreteBreakdownElement>();

		this.getConcreteIterationDao().getSessionFactory().getCurrentSession()
				.saveOrUpdate(_concreteIteration);
		for (ConcreteBreakdownElement element : _concreteIteration
				.getConcreteBreakdownElements()) {
			tmp.add(element);
		}
		return tmp;
	}

	/**
	 * Allows to get the concreteIterationDao
	 * 
	 * @return the concreteIterationDao
	 */
	public ConcreteIterationDao getConcreteIterationDao() {
		return concreteIterationDao;
	}

	/**
	 * Allows to set the concreteIterationDao
	 * 
	 * @param concreteIterationDao
	 * 
	 */
	public void setConcreteIterationDao(
			ConcreteIterationDao concreteIterationDao) {
		this.concreteIterationDao = concreteIterationDao;
	}
}
