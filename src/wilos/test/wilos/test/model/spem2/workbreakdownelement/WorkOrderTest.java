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

package wilos.test.model.spem2.workbreakdownelement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.model.spem2.workbreakdownelement.WorkOrder;
import wilos.model.spem2.workbreakdownelement.WorkOrderId;

/**
 * @author Sebastien BALARD
 *
 */
public class WorkOrderTest {

    private WorkOrderId workOrderId;

    private WorkOrder workOrder;

    @Before
    public void setUp() {
	this.workOrderId = new WorkOrderId();
	this.workOrder = new WorkOrder();
    }

    @After
    public void tearDown() {
	this.workOrder = null;
	this.workOrderId = null;
    }

    @Test
    public void testThatWorkOrderIsEmptyByDefault() {
	assertTrue("predecessor id", this.workOrderId.getPredecessorId().equals(""));
	assertTrue("successor id", this.workOrderId.getSuccessorId().equals(""));

	assertTrue("work order id", this.workOrder.getWorkOrderId() == null);
	assertTrue("predecessor", this.workOrder.getPredecessor() == null);
	assertTrue("successor", this.workOrder.getSuccessor() == null);
    }

    @Test
    public void testThatAWorkOrderEqualsAnother() {

	this.workOrderId.setPredecessorId("p1");
	this.workOrderId.setSuccessorId("s1");

	this.workOrder.setWorkOrderId(this.workOrderId);

	WorkOrder wo1 = new WorkOrder();
	wo1.setWorkOrderId(this.workOrderId);

	assertTrue("equal", this.workOrder.equals(wo1));

	WorkOrder wo2 = new WorkOrder();

	assertFalse("not equal", this.workOrder.equals(wo2));
    }

    @Test
    public void testThatWorkOrderHashCodeIsValide() {

	this.workOrderId.setPredecessorId("p1");
	this.workOrderId.setSuccessorId("s1");

	this.workOrder.setWorkOrderId(this.workOrderId);

	WorkOrder wo1 = new WorkOrder();
	wo1.setWorkOrderId(this.workOrderId);

	assertNotNull(this.workOrder.hashCode());
	assertNotNull(wo1.hashCode());
	assertEquals("equal", this.workOrder.hashCode(), wo1.hashCode());

	wo1 = null;
    }

}
