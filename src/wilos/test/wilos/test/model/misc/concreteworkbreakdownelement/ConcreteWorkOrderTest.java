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

package wilos.test.model.misc.concreteworkbreakdownelement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkOrder;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkOrderId;

/**
 * @author Sebastien BALARD
 *
 */
public class ConcreteWorkOrderTest {

    private ConcreteWorkOrderId concreteWorkOrderId;

    private ConcreteWorkOrder concreteWorkOrder;

    @Before
    public void setUp() {
	this.concreteWorkOrderId = new ConcreteWorkOrderId();
	this.concreteWorkOrder = new ConcreteWorkOrder();
    }

    @After
    public void tearDown() {
	this.concreteWorkOrder = null;
	this.concreteWorkOrderId = null;
    }

    @Test
    public void testThatConcreteWorkOrderIsEmptyByDefault() {
	assertTrue("concrete predecessor id", this.concreteWorkOrderId.getConcretePredecessorId().equals(""));
	assertTrue("concrete successor id", this.concreteWorkOrderId.getConcreteSuccessorId().equals(""));

	assertTrue("concrete workorder id", this.concreteWorkOrder.getConcreteWorkOrderId() == null);
	assertTrue("concrete predecessor", this.concreteWorkOrder.getConcretePredecessor() == null);
	assertTrue("concrete successor", this.concreteWorkOrder.getConcreteSuccessor() == null);
    }

    @Test
    public void testThatAConcreteWorkOrderEqualsAnother() {

	this.concreteWorkOrderId.setConcretePredecessorId("cp1");
	this.concreteWorkOrderId.setConcreteSuccessorId("cs1");

	this.concreteWorkOrder.setConcreteWorkOrderId(this.concreteWorkOrderId);

	ConcreteWorkOrder cwo1 = new ConcreteWorkOrder();
	cwo1.setConcreteWorkOrderId(this.concreteWorkOrderId);

	assertTrue("equal", this.concreteWorkOrder.equals(cwo1));

	ConcreteWorkOrder cwo2 = new ConcreteWorkOrder();

	assertFalse("not equal", this.concreteWorkOrder.equals(cwo2));
    }

    @Test
    public void testThatConcreteWorkOrderHashCodeIsValide() {

	this.concreteWorkOrderId.setConcretePredecessorId("cp1");
	this.concreteWorkOrderId.setConcreteSuccessorId("cs1");

	this.concreteWorkOrder.setConcreteWorkOrderId(this.concreteWorkOrderId);

	ConcreteWorkOrder cwo1 = new ConcreteWorkOrder();
	cwo1.setConcreteWorkOrderId(this.concreteWorkOrderId);

	assertNotNull(this.concreteWorkOrder.hashCode());
	assertNotNull(cwo1.hashCode());
	assertEquals("equal", this.concreteWorkOrder.hashCode(), cwo1.hashCode());

	cwo1 = null;
    }

}
