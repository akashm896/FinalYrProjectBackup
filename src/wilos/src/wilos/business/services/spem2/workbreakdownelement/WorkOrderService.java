/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
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
import wilos.hibernate.misc.concreteworkbreakdownelement.ConcreteWorkOrderDao;
import wilos.hibernate.spem2.workbreakdownelement.WorkBreakdownElementDao;
import wilos.hibernate.spem2.workbreakdownelement.WorkOrderDao;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkOrder;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkOrderId;
import wilos.model.spem2.workbreakdownelement.WorkBreakdownElement;
import wilos.model.spem2.workbreakdownelement.WorkOrder;
import wilos.model.spem2.workbreakdownelement.WorkOrderId;

@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class WorkOrderService {

	private WorkOrderDao workOrderDao;

	private WorkBreakdownElementDao workBreakdownElementDao;
	
	private ConcreteWorkOrderDao concreteWorkOrderDao;
	
	private ConcreteWorkBreakdownElementDao concreteWorkBreakdownElementDao;

	/**
	 * 
	 * @param _predecessorId
	 * @param _successorId
	 * @param _string
	 * @return
	 */
	public WorkOrderId saveWorkOrder(String _predecessorId,
			String _successorId, String _string) {

		WorkBreakdownElement workBreakdownElement1 = this.workBreakdownElementDao
				.getWorkBreakdownElement(_predecessorId);
		WorkBreakdownElement workBreakdownElement2 = this.workBreakdownElementDao
				.getWorkBreakdownElement(_successorId);

		WorkOrder workOrder = new WorkOrder();
		workOrder.setLinkType(_string);

		WorkOrderId workOrderId = new WorkOrderId();
		workOrderId.setPredecessorId(_predecessorId);
		workOrderId.setSuccessorId(_successorId);
		workOrder.setWorkOrderId(workOrderId);

		workBreakdownElement2.addPredecessor(workOrder);
		workBreakdownElement1.addSuccessor(workOrder);

		this.workOrderDao.saveOrUpdateWorkOrder(workOrder);
		this.workBreakdownElementDao
				.saveOrUpdateWorkBreakdownElement(workBreakdownElement1);
		this.workBreakdownElementDao
				.saveOrUpdateWorkBreakdownElement(workBreakdownElement2);

		return workOrderId;
	}
	
	/**
	 * Allow to instanciate one concreteWorkOrder
	 * @param _pred the ConcreteWorkBreakdownElement predecessor
	 * @param _succ the ConcreteWorkBreakdownElement successor
	 * @param _linkType
	 */
	public void workOrderInstanciation(ConcreteWorkBreakdownElement _pred, ConcreteWorkBreakdownElement _succ, String _linkType) {
		
		// creation of the identification key of the concreteWorkOrder
		ConcreteWorkOrderId concreteWorkOrderId = new ConcreteWorkOrderId();
		concreteWorkOrderId.setConcretePredecessorId(_pred.getId());
		concreteWorkOrderId.setConcreteSuccessorId(_succ.getId());
		
		// creation of the relative concreteWorkOrder
		ConcreteWorkOrder concreteWorkOrder = new ConcreteWorkOrder();
		concreteWorkOrder.setConcreteWorkOrderId(concreteWorkOrderId);
		// auto maintenance
		_pred.addConcreteSuccessor(concreteWorkOrder);
		_succ.addConcretePredecessor(concreteWorkOrder);
		concreteWorkOrder.setConcreteLinkType(_linkType);
		
		this.concreteWorkOrderDao.saveOrUpdateConcreteWorkOrder(concreteWorkOrder);
		this.concreteWorkBreakdownElementDao.saveOrUpdateConcreteWorkBreakdownElement(_pred);
		this.concreteWorkBreakdownElementDao.saveOrUpdateConcreteWorkBreakdownElement(_succ);
		
	}

	/**
	 * 
	 * @param _workBreakdownElement
	 * @return
	 */
	public Set<WorkOrder> getAllWorkOrders(
			WorkBreakdownElement _workBreakdownElement) {
		Set<WorkOrder> tmp = new HashSet<WorkOrder>();
		this.workOrderDao.getSessionFactory().getCurrentSession().saveOrUpdate(
				_workBreakdownElement);
		for (WorkOrder wo : _workBreakdownElement.getPredecessors()) {
			tmp.add(wo);
		}
		for (WorkOrder wo : _workBreakdownElement.getSuccessors()) {
			tmp.add(wo);
		}
		return tmp;
	}
	
	/**
	 * @param _wo
	 * @return
	 */
	public WorkBreakdownElement getPredecessor(WorkOrder _wo) {
		this.workOrderDao.getSessionFactory().getCurrentSession().saveOrUpdate(_wo);
		return _wo.getPredecessor();
	}

	/**
	 * 
	 * @param _workBreakdownElement
	 * @return
	 */
	public Set<WorkOrder> getPredecessors(
			WorkBreakdownElement _workBreakdownElement) {
		Set<WorkOrder> tmp = new HashSet<WorkOrder>();
		this.workOrderDao.getSessionFactory().getCurrentSession().saveOrUpdate(
				_workBreakdownElement);
		for (WorkOrder wo : _workBreakdownElement.getPredecessors()) {
			tmp.add(wo);
		}
		return tmp;
	}
	
	/**
	 * @param _wo
	 * @return
	 */
	public WorkBreakdownElement getSuccessor(WorkOrder _wo) {
		this.workOrderDao.getSessionFactory().getCurrentSession().saveOrUpdate(_wo);
		return _wo.getSuccessor();
	}

	/**
	 * 
	 * @param _workBreakdownElement
	 * @return
	 */
	public Set<WorkOrder> getSuccessors(
			WorkBreakdownElement _workBreakdownElement) {
		Set<WorkOrder> tmp = new HashSet<WorkOrder>();
		this.workOrderDao.getSessionFactory().getCurrentSession().saveOrUpdate(
				_workBreakdownElement);
		for (WorkOrder wo : _workBreakdownElement.getSuccessors()) {
			tmp.add(wo);
		}
		return tmp;
	}
	
	/**
	 * @param _predId
	 * @param _succId
	 * @return
	 */
	public String getLinkType(String _predId, String _succId) {
		WorkOrderId woi = new WorkOrderId();
		woi.setPredecessorId(_predId);
		woi.setSuccessorId(_succId);
		WorkOrder wo = this.workOrderDao.getWorkOrder(woi);
		return wo.getLinkType();
	}

	/**
	 * 
	 * @param _workOrderId
	 * @return
	 */
	public WorkOrder getWorkOrder(WorkOrderId _workOrderId) {
		return this.workOrderDao.getWorkOrder(_workOrderId);
	}

	/**
	 * 
	 * @param _workOrder
	 */
	public void deleteWorkOrder(WorkOrder _workOrder) {
		this.workOrderDao.deleteWorkOrder(_workOrder);
	}

	/**
	 * 
	 * @return
	 */
	public WorkOrderDao getWorkOrderDao() {
		return this.workOrderDao;
	}

	/**
	 * @param _workOrderDao
	 *            the workOrderDao to set
	 */
	public void setWorkOrderDao(WorkOrderDao _workOrderDao) {
		this.workOrderDao = _workOrderDao;
	}

	/**
	 * @return the workBreakdownElementDao
	 */
	public WorkBreakdownElementDao getWorkBreakdownElementDao() {
		return this.workBreakdownElementDao;
	}

	/**
	 * @param _workBreakdownElementDao
	 *            the workBreakdownElementDao to set
	 */
	public void setWorkBreakdownElementDao(
			WorkBreakdownElementDao _workBreakdownElementDao) {
		this.workBreakdownElementDao = _workBreakdownElementDao;
	}

	/**
	 * @return the concreteWorkOrderDao
	 */
	public ConcreteWorkOrderDao getConcreteWorkOrderDao() {
		return this.concreteWorkOrderDao;
	}

	/**
	 * @param _concreteWorkOrderDao the concreteWorkOrderDao to set
	 */
	public void setConcreteWorkOrderDao(ConcreteWorkOrderDao _concreteWorkOrderDao) {
		this.concreteWorkOrderDao = _concreteWorkOrderDao;
	}

	/**
	 * @return the concreteWorkBreakdownElementDao
	 */
	public ConcreteWorkBreakdownElementDao getConcreteWorkBreakdownElementDao() {
		return this.concreteWorkBreakdownElementDao;
	}

	/**
	 * @param _concreteWorkBreakdownElementDao the concreteWorkBreakdownElementDao to set
	 */
	public void setConcreteWorkBreakdownElementDao(
			ConcreteWorkBreakdownElementDao _concreteWorkBreakdownElementDao) {
		this.concreteWorkBreakdownElementDao = _concreteWorkBreakdownElementDao;
	}

}
