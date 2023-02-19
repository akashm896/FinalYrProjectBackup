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

package wilos.business.services.spem2.workbreakdownelement;

import java.util.HashSet;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.hibernate.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElementDao;
import wilos.hibernate.spem2.workbreakdownelement.WorkBreakdownElementDao;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.project.Project;
import wilos.model.spem2.workbreakdownelement.WorkBreakdownElement;
import wilos.model.spem2.workbreakdownelement.WorkOrder;

@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class WorkBreakdownElementService {

	private ConcreteWorkBreakdownElementDao concreteWorkBreakdownElementDao;

	private WorkBreakdownElementDao workBreakdownElementDao;

	/**
	 * 
	 * @param _project
	 * @param _wbde
	 */
	public void workBreakdownElementInstanciation(Project _project,
			WorkBreakdownElement _wbde) {

		ConcreteWorkBreakdownElement cwbe = new ConcreteWorkBreakdownElement();

		if (_wbde.getPresentationName() == null)
			cwbe.setConcreteName(_wbde.getName());
		else
			cwbe.setConcreteName(_wbde.getPresentationName());

		cwbe.addBreakdownElement(_wbde);
		cwbe.setProject(_project);

		this.concreteWorkBreakdownElementDao
				.saveOrUpdateConcreteWorkBreakdownElement(cwbe);
	}

	/**
	 * 
	 * @param _wbde
	 * @return
	 */
	public Set<WorkOrder> getPredecessors(WorkBreakdownElement _wbde) {
		this.workBreakdownElementDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_wbde);
		Set<WorkOrder> tmp = new HashSet<WorkOrder>();
		for (WorkOrder wo : _wbde.getPredecessors()) {
			tmp.add(wo);
		}
		return tmp;
	}

	/**
	 * 
	 * @param _wbde
	 * @return
	 */
	public Set<WorkOrder> getSuccessors(WorkBreakdownElement _wbde) {
		this.workBreakdownElementDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_wbde);
		Set<WorkOrder> tmp = new HashSet<WorkOrder>();
		for (WorkOrder wo : _wbde.getSuccessors()) {
			tmp.add(wo);
		}
		return tmp;
	}

	/**
	 * @param _wbde
	 * @return
	 */
	public Set<ConcreteWorkBreakdownElement> getAllConcreteWorkBreakdownElementsFromWorkBreakdownElement(
			WorkBreakdownElement _wbde) {

		this.workBreakdownElementDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_wbde);
		Set<ConcreteWorkBreakdownElement> tmp = new HashSet<ConcreteWorkBreakdownElement>();
		for (ConcreteWorkBreakdownElement cwbde : _wbde
				.getConcreteWorkBreakdownElements()) {
			tmp.add(cwbde);
		}
		return tmp;
	}
	
	/**
	 * @param _predecessor
	 * @param _successor
	 * @return
	 */
	public boolean canInstanciateConcreteWorkOrder(
			WorkBreakdownElement _predecessor, WorkBreakdownElement _successor) {

		this.workBreakdownElementDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_predecessor);
		this.workBreakdownElementDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_predecessor);
		
		for (ConcreteWorkBreakdownElement cPred : _predecessor.getConcreteWorkBreakdownElements()) {
			if (cPred.getConcreteSuccessors().size() < _successor.getConcreteWorkBreakdownElements().size())
				return true;
			else
				return false;
		}
		
		return true;

	}

	/**
	 * @return the concreteBreakdownElementDao
	 */
	public ConcreteWorkBreakdownElementDao getConcreteWorkBreakdownElementDao() {
		return concreteWorkBreakdownElementDao;
	}

	/**
	 * @param concreteBreakdownElementDao
	 *            the concreteBreakdownElementDao to set
	 */
	public void setConcreteWorkBreakdownElementDao(
			ConcreteWorkBreakdownElementDao concreteWorkBreakdownElementDao) {
		this.concreteWorkBreakdownElementDao = concreteWorkBreakdownElementDao;
	}

	public WorkBreakdownElementDao getWorkBreakdownElementDao() {
		return workBreakdownElementDao;
	}

	public void setWorkBreakdownElementDao(
			WorkBreakdownElementDao workBreakdownElementDao) {
		this.workBreakdownElementDao = workBreakdownElementDao;
	}

}
