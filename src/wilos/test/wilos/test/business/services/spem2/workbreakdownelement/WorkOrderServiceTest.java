/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2007 Sebastien BALARD <sbalard@wilos-project.org>
 * Copyright (C) 2007 Nicolas CASTEL <ncastel@wilos-project.org>
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

package wilos.test.business.services.spem2.workbreakdownelement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.spem2.workbreakdownelement.WorkOrderService;
import wilos.hibernate.spem2.workbreakdownelement.WorkBreakdownElementDao;
import wilos.model.spem2.workbreakdownelement.WorkBreakdownElement;
import wilos.model.spem2.workbreakdownelement.WorkOrder;
import wilos.model.spem2.workbreakdownelement.WorkOrderId;
import wilos.test.TestConfiguration;

/**
 * @author Sebastien BALARD
 *
 */
public class WorkOrderServiceTest {

    private WorkOrderService workOrderService = (WorkOrderService) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("WorkOrderService");

    private WorkBreakdownElementDao workBreakdownElementDao = (WorkBreakdownElementDao) TestConfiguration.getInstance()
    .getApplicationContext().getBean("WorkBreakdownElementDao");

    private WorkBreakdownElement workBreakdownElement1 = null;

    private WorkBreakdownElement workBreakdownElement2 = null;

    private String wbde1Id = "";

    private String wbde2Id = "";

    @Before
    public void setUp() {

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
	this.workBreakdownElement1 = null;
	this.workBreakdownElement2 = null;
    }

    @Test
    public void testStorageOfAWorkOrderInATransaction() {

	this.wbde1Id = this.workBreakdownElementDao.saveOrUpdateWorkBreakdownElement(this.workBreakdownElement1);
	this.wbde2Id = this.workBreakdownElementDao.saveOrUpdateWorkBreakdownElement(this.workBreakdownElement2);

	WorkOrderId workOrderId = this.workOrderService.saveWorkOrder(this.wbde1Id, this.wbde2Id, "startToFinish");

	WorkBreakdownElement retrievedWorkBreakdownElement1 = this.workBreakdownElementDao.getWorkBreakdownElement(this.wbde1Id);
	assertNotNull("not null", retrievedWorkBreakdownElement1);

	Set<WorkOrder> workOrdersP = this.workOrderService.getAllWorkOrders(retrievedWorkBreakdownElement1);
	assertEquals("equal workOrders", this.workOrderService.getSuccessors(retrievedWorkBreakdownElement1), workOrdersP);

	WorkBreakdownElement retrievedWorkBreakdownElement2 = this.workBreakdownElementDao.getWorkBreakdownElement(this.wbde2Id);
	assertNotNull("not null", retrievedWorkBreakdownElement2);

	Set<WorkOrder> workOrdersS = this.workOrderService.getAllWorkOrders(retrievedWorkBreakdownElement2);
	assertEquals("equal workOrders", this.workOrderService.getPredecessors(retrievedWorkBreakdownElement2), workOrdersS);

	WorkOrder workOrder = this.workOrderService.getWorkOrder(workOrderId);
	this.workOrderService.deleteWorkOrder(workOrder);
	this.workBreakdownElementDao.deleteWorkBreakdownElement(this.workBreakdownElement2);
	this.workBreakdownElementDao.deleteWorkBreakdownElement(this.workBreakdownElement1);
    }

}
