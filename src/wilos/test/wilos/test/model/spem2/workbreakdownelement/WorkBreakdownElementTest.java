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
package wilos.test.model.spem2.workbreakdownelement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.model.spem2.workbreakdownelement.WorkBreakdownElement;
import wilos.model.spem2.workbreakdownelement.WorkOrder;

public class WorkBreakdownElementTest {

    private WorkBreakdownElement workBreakdownElement;

    @Before
    public void setUp() {
	this.workBreakdownElement = new WorkBreakdownElement();
    }

    @After
    public void tearDown() {
	this.workBreakdownElement = null;
    }

    @Test
    public void testThatWorkBreakdownElementIsEmptyByDefault() {
	assertTrue("id", this.workBreakdownElement.getId().equals(""));
	assertTrue("guid", this.workBreakdownElement.getGuid().equals(""));
	assertTrue("name", this.workBreakdownElement.getName().equals(""));
	assertEquals("isRepeatable", false, this.workBreakdownElement.getIsRepeatable());
    }

    @Test
    public void testThatAWorkBreakdownElementEqualsAnother() {

	this.workBreakdownElement.setGuid("My guid");
	this.workBreakdownElement.setName("My name");
	this.workBreakdownElement.setIsRepeatable(true);

	WorkBreakdownElement workBreakdownElement1 = new WorkBreakdownElement();
	workBreakdownElement1.setGuid("My guid");
	workBreakdownElement1.setName("My name");
	workBreakdownElement1.setIsRepeatable(true);

	assertTrue("equal", this.workBreakdownElement.equals(workBreakdownElement1));

	WorkBreakdownElement workBreakdownElement2 = new WorkBreakdownElement();
	workBreakdownElement2.setGuid("Your guid");
	workBreakdownElement2.setName("Your name");
	workBreakdownElement2.setIsRepeatable(true);

	assertFalse("not equal", this.workBreakdownElement.equals(workBreakdownElement2));

	workBreakdownElement2 = null;
	workBreakdownElement1 = null;
    }

    @Test
    public void testThatWorkBreakdownElementHashCodeIsValide() {

	this.workBreakdownElement.setGuid("My guid");
	this.workBreakdownElement.setName("My name");
	this.workBreakdownElement.setIsRepeatable(true);

	WorkBreakdownElement workBreakdownElement1 = new WorkBreakdownElement();
	workBreakdownElement1.setGuid("My guid");
	workBreakdownElement1.setName("My name");
	workBreakdownElement1.setIsRepeatable(true);

	assertNotNull(this.workBreakdownElement.hashCode());
	assertNotNull(workBreakdownElement1.hashCode());
	assertEquals("equal", this.workBreakdownElement.hashCode(), workBreakdownElement1.hashCode());

	workBreakdownElement1 = null;
    }

    @Test
    public void testThatAPredecessorIsCorrectlyAddedToPredecessorsCollection() {

	this.workBreakdownElement.setGuid("My guid");
	this.workBreakdownElement.setName("My name");
	this.workBreakdownElement.setIsRepeatable(true);

	WorkOrder workOrder = new WorkOrder();
	workOrder.setLinkType("startToFinish");

	this.workBreakdownElement.addPredecessor(workOrder);

	assertEquals("size of predeccessors collection in the workBreakdownElement", 1, this.workBreakdownElement
		.getPredecessors().size());
	assertFalse("workBreakdownElement in the workOrder", workOrder.getSuccessor() == null);

	assertTrue("workOrders contains the workOrder", this.workBreakdownElement.getPredecessors().contains(workOrder));
	assertEquals("workBreakdownElement : code", this.workBreakdownElement.getGuid(), workOrder.getSuccessor()
		.getGuid());
	assertEquals("workBreakdownElement : name", this.workBreakdownElement.getName(), workOrder.getSuccessor()
		.getName());
	assertEquals("workBreakdownElement : isRepeatable", this.workBreakdownElement.getIsRepeatable(), workOrder
		.getSuccessor().getIsRepeatable());

	workOrder = null;
    }

    @Test
    public void testThatAsuccessorIsCorrectlyAddedToSuccessorsCollection() {

	this.workBreakdownElement.setGuid("My guid");
	this.workBreakdownElement.setName("My name");
	this.workBreakdownElement.setIsRepeatable(true);

	WorkOrder workOrder = new WorkOrder();
	workOrder.setLinkType("startToFinish");

	this.workBreakdownElement.addSuccessor(workOrder);

	assertEquals("size of successors collection in the workBreakdownElement", 1, this.workBreakdownElement
		.getSuccessors().size());
	assertFalse("workBreakdownElement in the workOrder", workOrder.getPredecessor() == null);

	assertTrue("workOrders contains the workOrder", this.workBreakdownElement.getSuccessors().contains(workOrder));
	assertEquals("workBreakdownElement : code", this.workBreakdownElement.getGuid(), workOrder.getPredecessor()
		.getGuid());
	assertEquals("workBreakdownElement : name", this.workBreakdownElement.getName(), workOrder.getPredecessor()
		.getName());
	assertEquals("workBreakdownElement : isRepeatable", this.workBreakdownElement.getIsRepeatable(), workOrder
		.getPredecessor().getIsRepeatable());

	workOrder = null;
    }

    @Test
    public void testThatAPredecessorIsCorrectlyRemovedFromPredecessorsCollection() {

	WorkOrder workOrder = new WorkOrder();
	workOrder.setLinkType("startToFinish");

	this.workBreakdownElement.addPredecessor(workOrder);
	this.workBreakdownElement.removePredecessor(workOrder);

	assertEquals("size of predecessors collection in the workBreakdownElement", 0, this.workBreakdownElement
		.getPredecessors().size());
	assertTrue("workBreakdownElement of workOrder is null", workOrder.getSuccessor() == null);

	workOrder = null;
    }

    @Test
    public void testThatASuccessorIsCorrectlyRemovedFromSuccessorsCollection() {

	WorkOrder workOrder = new WorkOrder();
	workOrder.setLinkType("startToFinish");

	this.workBreakdownElement.addPredecessor(workOrder);
	this.workBreakdownElement.removePredecessor(workOrder);

	assertEquals("size of successors collection in the workBreakdownElement", 0, this.workBreakdownElement
		.getPredecessors().size());
	assertTrue("workBreakdownElement of workOrder is null", workOrder.getSuccessor() == null);

	workOrder = null;
    }

    @Test
    public void testThatAPredecessorsCollectionIsCorrectlyAddedToPredecessorsCollection() {

	this.workBreakdownElement.setGuid("My guid");
	this.workBreakdownElement.setName("My name");
	this.workBreakdownElement.setIsRepeatable(true);

	Set<WorkOrder> workOrders = new HashSet<WorkOrder>();

	WorkOrder workOrder1 = new WorkOrder();
	workOrder1.setLinkType("startToFinish");

	WorkOrder workOrder2 = new WorkOrder();
	workOrder2.setLinkType("startToStart");

	workOrders.add(workOrder1);
	workOrders.add(workOrder2);

	this.workBreakdownElement.addAllPredecessors(workOrders);

	assertEquals("size of predecessors collection in the workBreakdownElement", 2, this.workBreakdownElement
		.getPredecessors().size());
	assertFalse("workBreakdownElement in the workOrder1", workOrder1.getSuccessor() == null);
	assertFalse("workBreakdownElement in the workOrder2", workOrder2.getSuccessor() == null);

	assertTrue("workOrders contains the e1", this.workBreakdownElement.getPredecessors().contains(workOrder1));
	assertTrue("workOrders contains the e2", this.workBreakdownElement.getPredecessors().contains(workOrder2));
	assertEquals("workBreakdownElement in workorder1 : code", this.workBreakdownElement.getGuid(), workOrder1
		.getSuccessor().getGuid());
	assertEquals("workBreakdownElement in workorder1 : name", this.workBreakdownElement.getName(), workOrder1
		.getSuccessor().getName());
	assertEquals("workBreakdownElement in workorder1 : isRepeatable", this.workBreakdownElement.getIsRepeatable(),
		workOrder1.getSuccessor().getIsRepeatable());
	assertEquals("workBreakdownElement in workorder2 : code", this.workBreakdownElement.getGuid(), workOrder2
		.getSuccessor().getGuid());
	assertEquals("workBreakdownElement in workorder2 : name", this.workBreakdownElement.getName(), workOrder2
		.getSuccessor().getName());
	assertEquals("workBreakdownElement in workorder2 : isRepeatable", this.workBreakdownElement.getIsRepeatable(),
		workOrder2.getSuccessor().getIsRepeatable());

	workOrder2 = null;
	workOrder1 = null;
	workOrders = null;
    }

    @Test
    public void testThatASuccessorsCollectionIsCorrectlyAddedToSuccessorsCollection() {

	this.workBreakdownElement.setGuid("My guid");
	this.workBreakdownElement.setName("My name");
	this.workBreakdownElement.setIsRepeatable(true);

	Set<WorkOrder> workOrders = new HashSet<WorkOrder>();

	WorkOrder workOrder1 = new WorkOrder();
	workOrder1.setLinkType("startToFinish");

	WorkOrder workOrder2 = new WorkOrder();
	workOrder2.setLinkType("startToStart");

	workOrders.add(workOrder1);
	workOrders.add(workOrder2);

	this.workBreakdownElement.addAllSuccessors(workOrders);

	assertEquals("size of successors collection in the workBreakdownElement", 2, this.workBreakdownElement
		.getSuccessors().size());
	assertFalse("workBreakdownElement in the workOrder1", workOrder1.getPredecessor() == null);
	assertFalse("workBreakdownElement in the workOrder2", workOrder2.getPredecessor() == null);

	assertTrue("workOrders contains the e1", this.workBreakdownElement.getSuccessors().contains(workOrder1));
	assertTrue("workOrders contains the e2", this.workBreakdownElement.getSuccessors().contains(workOrder2));
	assertEquals("workBreakdownElement in workorder1 : code", this.workBreakdownElement.getGuid(), workOrder1
		.getPredecessor().getGuid());
	assertEquals("workBreakdownElement in workorder1 : name", this.workBreakdownElement.getName(), workOrder1
		.getPredecessor().getName());
	assertEquals("workBreakdownElement in workorder1 : isRepeatable", this.workBreakdownElement.getIsRepeatable(),
		workOrder1.getPredecessor().getIsRepeatable());
	assertEquals("workBreakdownElement in workorder2 : code", this.workBreakdownElement.getGuid(), workOrder2
		.getPredecessor().getGuid());
	assertEquals("workBreakdownElement in workorder2 : name", this.workBreakdownElement.getName(), workOrder2
		.getPredecessor().getName());
	assertEquals("workBreakdownElement in workorder2 : isRepeatable", this.workBreakdownElement.getIsRepeatable(),
		workOrder2.getPredecessor().getIsRepeatable());

	workOrder2 = null;
	workOrder1 = null;
	workOrders = null;
    }

    @Test
    public void testThatAPredecessorsCollectionIsCorrectlyRemovedFromPredecessorsCollection() {

	this.workBreakdownElement.setGuid("My guid");
	this.workBreakdownElement.setName("My name");
	this.workBreakdownElement.setIsRepeatable(true);

	Set<WorkOrder> workOrders = new HashSet<WorkOrder>();

	WorkOrder workOrder1 = new WorkOrder();
	workOrder1.setLinkType("startToFinish");

	WorkOrder workOrder2 = new WorkOrder();
	workOrder2.setLinkType("startToStart");

	workOrders.add(workOrder1);
	workOrders.add(workOrder2);

	this.workBreakdownElement.addAllPredecessors(workOrders);
	this.workBreakdownElement.removeAllPredecessors();

	assertEquals("predecessors contains the workOrder", 0, this.workBreakdownElement.getPredecessors().size());
	assertTrue("workBreakdownElement in the e1", workOrder1.getSuccessor() == null);
	assertTrue("workBreakdownElement in the e2", workOrder2.getSuccessor() == null);

	workOrder2 = null;
	workOrder1 = null;
	workOrders = null;
    }
    
    @Test
    public void testThatASuccessorsCollectionIsCorrectlyRemovedFromSuccessorsCollection() {

	this.workBreakdownElement.setGuid("My guid");
	this.workBreakdownElement.setName("My name");
	this.workBreakdownElement.setIsRepeatable(true);

	Set<WorkOrder> workOrders = new HashSet<WorkOrder>();

	WorkOrder workOrder1 = new WorkOrder();
	workOrder1.setLinkType("startToFinish");

	WorkOrder workOrder2 = new WorkOrder();
	workOrder2.setLinkType("startToStart");

	workOrders.add(workOrder1);
	workOrders.add(workOrder2);

	this.workBreakdownElement.addAllSuccessors(workOrders);
	this.workBreakdownElement.removeAllSuccessors();

	assertEquals("predecessors contains the workOrder", 0, this.workBreakdownElement.getSuccessors().size());
	assertTrue("workBreakdownElement in the e1", workOrder1.getPredecessor() == null);
	assertTrue("workBreakdownElement in the e2", workOrder2.getPredecessor() == null);

	workOrder2 = null;
	workOrder1 = null;
	workOrders = null;
    }
}
