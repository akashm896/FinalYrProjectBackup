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

package wilos.test.hibernate.spem2.workbreakdownelement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.spem2.workbreakdownelement.WorkBreakdownElementDao;
import wilos.hibernate.spem2.workbreakdownelement.WorkOrderDao;
import wilos.model.spem2.workbreakdownelement.WorkBreakdownElement;
import wilos.model.spem2.workbreakdownelement.WorkOrder;
import wilos.model.spem2.workbreakdownelement.WorkOrderId;
import wilos.test.TestConfiguration;

/**
 * @author Sebastien BALARD
 *
 */
public class WorkOrderDaoTest {

    private WorkOrderDao workOrderDao = (WorkOrderDao) TestConfiguration.getInstance().getApplicationContext().getBean(
	    "WorkOrderDao");

    private WorkBreakdownElementDao workBreakdownElementDao = (WorkBreakdownElementDao) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("WorkBreakdownElementDao");

    private WorkOrder workOrder = null;

    private WorkBreakdownElement workBreakdownElement1 = null;

    private WorkBreakdownElement workBreakdownElement2 = null;

    @Before
    public void setUp() {

	this.workOrder = new WorkOrder();
	this.workOrder.setLinkType("startToStart");

	this.workBreakdownElement1 = new WorkBreakdownElement();
	this.workBreakdownElement1.setGuid("My guid");
	this.workBreakdownElement1.setName("My name");
	this.workBreakdownElement1.setIsRepeatable(true);

	this.workBreakdownElement2 = new WorkBreakdownElement();
	this.workBreakdownElement2.setGuid("Your guid");
	this.workBreakdownElement2.setName("Your name");
	this.workBreakdownElement2.setIsRepeatable(false);
    }

    @After
    public void tearDown() {
	this.workOrder = null;
	this.workBreakdownElement1 = null;
	this.workBreakdownElement2 = null;
    }

    @Test
    public void testStorageOfANullWorkOrder() {

	WorkOrder workOrder = null;

	WorkOrderId id = this.workOrderDao.saveOrUpdateWorkOrder(workOrder);
	WorkOrder retrievedWorkOrder = this.workOrderDao.getWorkOrder(id);

	assertNull("null", retrievedWorkOrder);
    }

    @Test
    public void testStorageOfAWorkOrder() {

	String wbde1Id = this.workBreakdownElementDao.saveOrUpdateWorkBreakdownElement(this.workBreakdownElement1);
	String wbde2Id = this.workBreakdownElementDao.saveOrUpdateWorkBreakdownElement(this.workBreakdownElement2);

	WorkOrderId workOrderId = new WorkOrderId();
	workOrderId.setPredecessorId(wbde1Id);
	workOrderId.setSuccessorId(wbde2Id);
	this.workOrder.setWorkOrderId(workOrderId);

	WorkOrderId id = this.workOrderDao.saveOrUpdateWorkOrder(this.workOrder);
	WorkOrder retrievedWorkOrder = this.workOrderDao.getWorkOrder(id);

	assertEquals("equal id", this.workOrder, retrievedWorkOrder);

	this.workOrderDao.deleteWorkOrder(this.workOrder);
	this.workBreakdownElementDao.deleteWorkBreakdownElement(this.workBreakdownElement2);
	this.workBreakdownElementDao.deleteWorkBreakdownElement(this.workBreakdownElement1);
    }

    @Test
    public void testDeletionOfAWorkOrder() {

	String wbde1Id = this.workBreakdownElementDao.saveOrUpdateWorkBreakdownElement(this.workBreakdownElement1);
	String wbde2Id = this.workBreakdownElementDao.saveOrUpdateWorkBreakdownElement(this.workBreakdownElement2);

	WorkOrderId workOrderId = new WorkOrderId();
	workOrderId.setPredecessorId(wbde1Id);
	workOrderId.setSuccessorId(wbde2Id);
	this.workOrder.setWorkOrderId(workOrderId);

	WorkOrderId id = this.workOrderDao.saveOrUpdateWorkOrder(this.workOrder);
	this.workOrderDao.deleteWorkOrder(this.workOrder);
	WorkOrder retrievedWorkOrder = this.workOrderDao.getWorkOrder(id);

	assertNull("null", retrievedWorkOrder);

	this.workBreakdownElementDao.deleteWorkBreakdownElement(this.workBreakdownElement2);
	this.workBreakdownElementDao.deleteWorkBreakdownElement(this.workBreakdownElement1);
    }

    @Test
    public void testRetrievalOfAllWorkOrders() {

	int nb = this.workOrderDao.getAllWorkOrders().size();

	String wbde1Id = this.workBreakdownElementDao.saveOrUpdateWorkBreakdownElement(this.workBreakdownElement1);
	String wbde2Id = this.workBreakdownElementDao.saveOrUpdateWorkBreakdownElement(this.workBreakdownElement2);

	WorkOrderId workOrderId = new WorkOrderId();
	workOrderId.setPredecessorId(wbde1Id);
	workOrderId.setSuccessorId(wbde2Id);
	this.workOrder.setWorkOrderId(workOrderId);
	
	this.workBreakdownElement1.addSuccessor(this.workOrder);
	this.workBreakdownElement2.addPredecessor(this.workOrder);

	this.workOrderDao.saveOrUpdateWorkOrder(this.workOrder);

	WorkOrder workOrder1 = new WorkOrder();
	workOrder1.setLinkType("startToFinish");

	WorkBreakdownElement workBreakdownElement = new WorkBreakdownElement();
	workBreakdownElement.setGuid("Its guid");
	workBreakdownElement.setName("Its name");
	workBreakdownElement.setIsRepeatable(false);

	String wbde3Id = this.workBreakdownElementDao.saveOrUpdateWorkBreakdownElement(workBreakdownElement);

	WorkOrderId workOrder1Id = new WorkOrderId();
	workOrder1Id.setPredecessorId(wbde1Id);
	workOrder1Id.setSuccessorId(wbde3Id);
	workOrder1.setWorkOrderId(workOrder1Id);
	
	this.workBreakdownElement1.addSuccessor(workOrder1);
	workBreakdownElement.addPredecessor(workOrder1);

	this.workOrderDao.saveOrUpdateWorkOrder(workOrder1);

	List<WorkOrder> workOrders = this.workOrderDao.getAllWorkOrders();
	assertNotNull("list not null", workOrders);
	assertEquals("size", 2, workOrders.size() - nb);

	this.workOrderDao.deleteWorkOrder(workOrder1);
	this.workBreakdownElementDao.deleteWorkBreakdownElement(workBreakdownElement);

	this.workOrderDao.deleteWorkOrder(this.workOrder);
	this.workBreakdownElementDao.deleteWorkBreakdownElement(this.workBreakdownElement2);
	this.workBreakdownElementDao.deleteWorkBreakdownElement(this.workBreakdownElement1);

	workOrderId = null;
	workOrder1 = null;
	workOrder1Id = null;
    }

}
