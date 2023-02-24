/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2007 Sebastien BALARD <sbalard@wilos-project.org>
 * Copyright (C) 2007-2008 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

package wilos.business.services.misc.concreteworkbreakdownelement;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.business.services.misc.stateservice.StateService;
import wilos.business.services.spem2.workbreakdownelement.WorkBreakdownElementService;
import wilos.hibernate.misc.concreteactivity.ConcreteActivityDao;
import wilos.hibernate.misc.concretemilestone.ConcreteMilestoneDao;
import wilos.hibernate.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElementDao;
import wilos.hibernate.misc.concreteworkbreakdownelement.ConcreteWorkOrderDao;
import wilos.hibernate.spem2.workbreakdownelement.WorkOrderDao;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkOrder;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkOrderId;
import wilos.utils.Constantes.State;
import wilos.utils.Constantes.WorkOrderType;

/**
 * @author Sebastien BALARD
 * 
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class ConcreteWorkOrderService {

	private ConcreteWorkOrderDao concreteWorkOrderDao;

	private WorkOrderDao workOrderDao;

	private ConcreteActivityDao concreteActivityDao;

	private StateService stateService;

	private ConcreteWorkBreakdownElementDao concreteWorkBreakdownElementDao;

	private WorkBreakdownElementService workBreakdownElementService;

	private ConcreteMilestoneDao concreteMilestoneDao;

	/**
	 * Allows to save the concreteWorkOrder
	 * 
	 * @param _concretePredecessorId
	 * @param _concreteSuccessorId
	 * @param _concreteLinkType
	 * @param _projectId
	 * @return the concreteWorkOrderId
	 */
	public ConcreteWorkOrderId saveConcreteWorkOrder(
			String _concretePredecessorId, String _concreteSuccessorId,
			String _concreteLinkType, String _projectId) {

		ConcreteWorkBreakdownElement concreteWorkBreakdownElement1 = this.concreteWorkBreakdownElementDao
				.getConcreteWorkBreakdownElement(_concretePredecessorId);
		ConcreteWorkBreakdownElement concreteWorkBreakdownElement2 = this.concreteWorkBreakdownElementDao
				.getConcreteWorkBreakdownElement(_concreteSuccessorId);

		ConcreteWorkOrder concreteWorkOrder = new ConcreteWorkOrder();
		concreteWorkOrder.setConcreteLinkType(_concreteLinkType);
		concreteWorkOrder.setProjectId(_projectId);

		ConcreteWorkOrderId concreteWorkOrderId = new ConcreteWorkOrderId();
		concreteWorkOrderId.setConcretePredecessorId(_concretePredecessorId);
		concreteWorkOrderId.setConcreteSuccessorId(_concreteSuccessorId);
		concreteWorkOrderId.setConcreteLinkTypeId(_concreteLinkType);
		concreteWorkOrder.setConcreteWorkOrderId(concreteWorkOrderId);

		concreteWorkBreakdownElement2.addConcretePredecessor(concreteWorkOrder);
		concreteWorkBreakdownElement1.addConcreteSuccessor(concreteWorkOrder);

		this.concreteWorkOrderDao
				.saveOrUpdateConcreteWorkOrder(concreteWorkOrder);
		this.concreteWorkBreakdownElementDao
				.saveOrUpdateConcreteWorkBreakdownElement(concreteWorkBreakdownElement1);
		this.concreteWorkBreakdownElementDao
				.saveOrUpdateConcreteWorkBreakdownElement(concreteWorkBreakdownElement2);

		// Check for changes on the state of the successor
		this.checkSuccessorStateAfterAddingWorkOrder(concreteWorkOrder);

		return concreteWorkOrderId;
	}

	/**
	 * Allows to update the workOrder's link type
	 * 
	 * @param _concreteWorkOrder
	 * @param _newLink
	 * @return the workOrder's link type
	 */
	public ConcreteWorkOrderId updateWorkOrderLinkType(
			ConcreteWorkOrder _concreteWorkOrder, String _newLink) {
		// no change to make
		if (_concreteWorkOrder.getConcreteLinkType().equals(_newLink)) {
			return _concreteWorkOrder.getConcreteWorkOrderId();
		}

		ConcreteWorkOrder updatedConcreteWorkOrder = null;
		try {
			updatedConcreteWorkOrder = _concreteWorkOrder.clone();
		} catch (CloneNotSupportedException e) {
		}

		this.concreteWorkOrderDao.deleteConcreteWorkOrder(_concreteWorkOrder);

		// Check for changes on the state of the successor
		this.concreteWorkOrderDao.getSessionFactory().getCurrentSession()
				.flush();
		this
				.checkSuccessorStateAfterRemovingWorkOrder(updatedConcreteWorkOrder);

		updatedConcreteWorkOrder.setConcreteLinkType(_newLink);
		updatedConcreteWorkOrder.getConcreteWorkOrderId()
				.setConcreteLinkTypeId(_newLink);

		this.concreteWorkOrderDao
				.saveOrUpdateConcreteWorkOrder(updatedConcreteWorkOrder);

		// Check for changes on the state of the successor
		this.concreteWorkOrderDao.getSessionFactory().getCurrentSession()
				.flush();
		this.checkSuccessorStateAfterAddingWorkOrder(updatedConcreteWorkOrder);

		return updatedConcreteWorkOrder.getConcreteWorkOrderId();
	}

	/**
	 * Allows to check the successor's state after adding a workOrder
	 * 
	 * @param _cwo
	 */
	private void checkSuccessorStateAfterAddingWorkOrder(ConcreteWorkOrder _cwo) {
		String predecessorId = _cwo.getConcreteWorkOrderId()
				.getConcretePredecessorId();
		String successorId = _cwo.getConcreteWorkOrderId()
				.getConcreteSuccessorId();
		ConcreteWorkBreakdownElement predecessor = this.concreteWorkBreakdownElementDao
				.getConcreteWorkBreakdownElement(predecessorId);
		ConcreteWorkBreakdownElement successor = this.concreteWorkBreakdownElementDao
				.getConcreteWorkBreakdownElement(successorId);

		String linkType = _cwo.getConcreteLinkType();

		if (successor.getState().equals(State.READY)) {
			boolean predecessorIsAtLeastStarted = predecessor.getState()
					.equals(State.STARTED)
					|| predecessor.getState().equals(State.SUSPENDED)
					|| predecessor.getState().equals(State.FINISHED);
			boolean predecessorIsFinished = predecessor.getState().equals(
					State.FINISHED);

			if ((linkType.equals(WorkOrderType.START_TO_START) && !predecessorIsAtLeastStarted)
					|| (linkType.equals(WorkOrderType.FINISH_TO_START) && !predecessorIsFinished)
					|| (this.concreteMilestoneDao
							.existsConcreteMilestone(successorId) && (linkType
							.equals(WorkOrderType.START_TO_FINISH)
							&& !predecessorIsAtLeastStarted || linkType
							.equals(WorkOrderType.FINISH_TO_FINISH)
							&& !predecessorIsFinished))) {

				this.stateService.updateStateTo(successor, State.CREATED);
			}
		}
	}

	/**
	 * Allows to check the successor's state after removing a workOrder
	 * 
	 * @param _oldCwo
	 */
	private void checkSuccessorStateAfterRemovingWorkOrder(
			ConcreteWorkOrder _oldCwo) {
		String successorId = _oldCwo.getConcreteWorkOrderId()
				.getConcreteSuccessorId();
		ConcreteWorkBreakdownElement successor = this.concreteWorkBreakdownElementDao
				.getConcreteWorkBreakdownElement(successorId);

		String linkType = _oldCwo.getConcreteLinkType();

		if (successor.getState().equals(State.CREATED)) {
			if (linkType.equals(WorkOrderType.START_TO_START)
					|| linkType.equals(WorkOrderType.FINISH_TO_START)
					|| this.concreteMilestoneDao
							.existsConcreteMilestone(successorId)) {
				this.stateService.updateStateTo(successor, State.READY);
			}
		} else if (successor.getState().equals(State.STARTED)
				&& this.concreteActivityDao.existsConcreteActivity(successorId)) {
			this.stateService.updateStateTo(successor, State.FINISHED);
		}
	}

	/**
	 * Allows to get the set of all concreteWorkOrder from the
	 * concreteBreakdownElement
	 * 
	 * @param _concreteWorkBreakdownElement
	 * @return the set of all concreteWorkOrder
	 */
	public Set<ConcreteWorkOrder> getAllConcreteWorkOrdersFromConcreteWorkBreakdownElement(
			ConcreteWorkBreakdownElement _concreteWorkBreakdownElement) {
		Set<ConcreteWorkOrder> tmp = new HashSet<ConcreteWorkOrder>();
		this.concreteWorkOrderDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_concreteWorkBreakdownElement);
		for (ConcreteWorkOrder cwo : _concreteWorkBreakdownElement
				.getConcretePredecessors()) {
			tmp.add(cwo);
		}
		for (ConcreteWorkOrder cwo : _concreteWorkBreakdownElement
				.getConcreteSuccessors()) {
			tmp.add(cwo);
		}
		return tmp;
	}

	/**
	 * Allows to get the list of all concreteWorkOrders
	 * 
	 * @return the list of all concreteWorkOrders
	 */
	public List<ConcreteWorkOrder> getAllConcreteWorkOrders() {
		return this.concreteWorkOrderDao.getAllConcreteWorkOrders();
	}

	/**
	 * Allows to get the list of all concreteWorkOrders with a project id
	 * 
	 * @param _string
	 * @return the list of all concreteWorkOrders
	 */
	public List<ConcreteWorkOrder> getAllConcreteWorkOrdersFromProject(
			String _projectId) {
		return this.concreteWorkOrderDao
				.getAllConcreteWorkOrdersFromProject(_projectId);
	}

	/**
	 * Allows to get the set of concretePredecessors
	 * 
	 * @param _concreteWorkBreakdownElement
	 * @return the set of concretePredecessors
	 */
	public Set<ConcreteWorkOrder> getConcretePredecessors(
			ConcreteWorkBreakdownElement _concreteWorkBreakdownElement) {
		Set<ConcreteWorkOrder> tmp = new HashSet<ConcreteWorkOrder>();
		this.concreteWorkOrderDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_concreteWorkBreakdownElement);
		for (ConcreteWorkOrder cwo : _concreteWorkBreakdownElement
				.getConcretePredecessors()) {
			tmp.add(cwo);
		}
		return tmp;
	}

	/**
	 * Allows to get the set of concreteSuccessors
	 * 
	 * @param _concreteWorkBreakdownElement
	 * @return the set of concreteSuccessors
	 */
	public Set<ConcreteWorkOrder> getConcreteSuccessors(
			ConcreteWorkBreakdownElement _concreteWorkBreakdownElement) {
		Set<ConcreteWorkOrder> tmp = new HashSet<ConcreteWorkOrder>();
		this.concreteWorkOrderDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_concreteWorkBreakdownElement);
		for (ConcreteWorkOrder cwo : _concreteWorkBreakdownElement
				.getConcreteSuccessors()) {
			tmp.add(cwo);
		}
		return tmp;
	}

	/**
	 * Allows to get the concreteWorkOrder
	 * 
	 * @param _concreteWorkOrderId
	 * @return the concreteWorkOrder
	 */
	public ConcreteWorkOrder getConcreteWorkOrder(
			ConcreteWorkOrderId _concreteWorkOrderId) {
		return this.concreteWorkOrderDao
				.getConcreteWorkOrder(_concreteWorkOrderId);
	}

	/**
	 * Allows to delete the concreteWorkOrder
	 * 
	 * @param _concreteWorkOrder
	 */
	public void deleteConcreteWorkOrder(ConcreteWorkOrder _concreteWorkOrder) {
		this.concreteWorkOrderDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_concreteWorkOrder);

		ConcreteWorkOrder oldConcreteWorkOrder = null;
		try {
			oldConcreteWorkOrder = _concreteWorkOrder.clone();
		} catch (CloneNotSupportedException e) {
		}

		this.concreteWorkOrderDao.deleteConcreteWorkOrder(_concreteWorkOrder);

		this.concreteWorkOrderDao.getSessionFactory().getCurrentSession()
				.flush();

		this.checkSuccessorStateAfterRemovingWorkOrder(oldConcreteWorkOrder);
	}

	/**
	 * Allows to check if the concreteWorkOrder exists
	 * 
	 * @param _selectedPredecessor
	 * @param _id
	 * @return true if the concreteWorkOrder is matched
	 */
	public boolean existsConcreteWorkOrder(String _concretePredecessorId,
			String _concreteSuccessorId, String linkType) {

		ConcreteWorkOrderId cwoi = new ConcreteWorkOrderId();
		cwoi.setConcretePredecessorId(_concretePredecessorId);
		cwoi.setConcreteSuccessorId(_concreteSuccessorId);
		cwoi.setConcreteLinkTypeId(linkType);

		ConcreteWorkOrder cwo = this.concreteWorkOrderDao
				.getConcreteWorkOrder(cwoi);
		if (cwo == null)
			return false;
		else
			return true;
	}

	/**
	 * Allows to get the concreteWorkBreakdownElementDao
	 * 
	 * @return the concreteWorkBreakdownElementDao
	 */
	public ConcreteWorkBreakdownElementDao getConcreteWorkBreakdownElementDao() {
		return concreteWorkBreakdownElementDao;
	}

	/**
	 * Allows to set the concreteWorkBreakdownElementDao
	 * 
	 * @param _concreteWorkBreakdownElementDao
	 * 
	 */
	public void setConcreteWorkBreakdownElementDao(
			ConcreteWorkBreakdownElementDao _concreteWorkBreakdownElementDao) {
		concreteWorkBreakdownElementDao = _concreteWorkBreakdownElementDao;
	}

	/**
	 * Allows to get the concreteWorkOrderDao
	 * 
	 * @return the concreteWorkOrderDao
	 */
	public ConcreteWorkOrderDao getConcreteWorkOrderDao() {
		return concreteWorkOrderDao;
	}

	/**
	 * Allows to set the concreteWorkOrderDao
	 * 
	 * @param _concreteWorkOrderDao
	 */
	public void setConcreteWorkOrderDao(
			ConcreteWorkOrderDao _concreteWorkOrderDao) {
		concreteWorkOrderDao = _concreteWorkOrderDao;
	}

	/**
	 * Allows to get the workOrderDao
	 * 
	 * @return the workOrderDao
	 */
	public WorkOrderDao getWorkOrderDao() {
		return this.workOrderDao;
	}

	/**
	 * Allows to set the workOrderDao
	 * 
	 * @param _workOrderDao
	 */
	public void setWorkOrderDao(WorkOrderDao _workOrderDao) {
		this.workOrderDao = _workOrderDao;
	}

	/**
	 * Allows to get the workBreakdownElementService
	 * 
	 * @return the workBreakdownElementService
	 */
	public WorkBreakdownElementService getWorkBreakdownElementService() {
		return this.workBreakdownElementService;
	}

	/**
	 * Allows to set the workBreakdownElementService
	 * 
	 * @param _workBreakdownElementService
	 */
	public void setWorkBreakdownElementService(
			WorkBreakdownElementService _workBreakdownElementService) {
		this.workBreakdownElementService = _workBreakdownElementService;
	}

	/**
	 * Allows to get the concreteActivityDao
	 * 
	 * @return the concreteActivityDao
	 */
	public ConcreteActivityDao getConcreteActivityDao() {
		return this.concreteActivityDao;
	}

	/**
	 * Allows to set the concreteActivityDao
	 * 
	 * @param _concreteActivityDao
	 */
	public void setConcreteActivityDao(ConcreteActivityDao _concreteActivityDao) {
		this.concreteActivityDao = _concreteActivityDao;
	}

	/**
	 * Allows to get the service state
	 * 
	 * @return the stateService
	 */
	public StateService getStateService() {
		return this.stateService;
	}

	/**
	 * Allows to set the service state
	 * 
	 * @param _stateService
	 */
	public void setStateService(StateService _stateService) {
		this.stateService = _stateService;
	}

	/**
	 * Allows to get the concreteMilestoneDao
	 * 
	 * @return the concreteMilestoneDao
	 */
	public ConcreteMilestoneDao getConcreteMilestoneDao() {
		return concreteMilestoneDao;
	}

	/**
	 * Allows to set the concreteMilestoneDao
	 * 
	 * @param _concreteMilestoneDao
	 */
	public void setConcreteMilestoneDao(
			ConcreteMilestoneDao _concreteMilestoneDao) {
		concreteMilestoneDao = _concreteMilestoneDao;
	}

}
