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

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkOrder;
import wilos.utils.Constantes;
import wilos.utils.Constantes.State;

public class ConcreteWorkBreakdownElementTest {

    private ConcreteWorkBreakdownElement concreteWorkBreakdownElement;

    private Date date;

    @Before
    public void setUp() {
	this.concreteWorkBreakdownElement = new ConcreteWorkBreakdownElement();
	this.concreteWorkBreakdownElement.setConcreteName("test");
	try {
	    date = Constantes.DATE_FORMAT.parse("18/01/2007 10:00");
	} catch (ParseException e) {
	    e.printStackTrace();
	}
    }

    @After
    public void tearDown() {
	this.concreteWorkBreakdownElement = null;
    }

    @Test
    public void testThatConcreteWorkBreakdownElementIsEmptyByDefault() {
	assertTrue("id", this.concreteWorkBreakdownElement.getId().equals(""));
	assertTrue("concrete name", this.concreteWorkBreakdownElement
		.getConcreteName().equals("test"));
	assertTrue("planned time", this.concreteWorkBreakdownElement
		.getPlannedTime() == 0.0f);
	assertTrue("planned finish date", this.concreteWorkBreakdownElement
		.getPlannedFinishingDate() == null);
	assertTrue("state", this.concreteWorkBreakdownElement.getState()
		.equals(State.CREATED));
    }

    @Test
    public void testThatAConcreteWorkBreakdownElementEqualsAnother() {

	this.concreteWorkBreakdownElement.setConcreteName("My concrete name");
	this.concreteWorkBreakdownElement.setPlannedTime(25.0f);
	this.concreteWorkBreakdownElement.setPlannedFinishingDate(date);

	ConcreteWorkBreakdownElement concreteWorkBreakdownElement1 = new ConcreteWorkBreakdownElement();
	concreteWorkBreakdownElement1.setConcreteName("My concrete name");
	concreteWorkBreakdownElement1.setPlannedTime(25.0f);
	concreteWorkBreakdownElement1.setPlannedFinishingDate(date);

	assertTrue("equal", this.concreteWorkBreakdownElement.equals(concreteWorkBreakdownElement1));

	ConcreteWorkBreakdownElement concreteWorkBreakdownElement2 = new ConcreteWorkBreakdownElement();
	concreteWorkBreakdownElement2.setConcreteName("Your concrete name");
	concreteWorkBreakdownElement2.setPlannedTime(8.0f);
	concreteWorkBreakdownElement2.setPlannedFinishingDate(date);

	assertFalse("not equal", this.concreteWorkBreakdownElement.equals(concreteWorkBreakdownElement2));

	concreteWorkBreakdownElement2 = null;
	concreteWorkBreakdownElement1 = null;
    }

    @Test
    public void testThatConcreteWorkBreakdownElementHashCodeIsValide() {

	this.concreteWorkBreakdownElement.setConcreteName("My concrete name");
	this.concreteWorkBreakdownElement.setPlannedTime(25.0f);
	this.concreteWorkBreakdownElement.setPlannedFinishingDate(date);

	ConcreteWorkBreakdownElement concreteWorkBreakdownElement1 = new ConcreteWorkBreakdownElement();
	concreteWorkBreakdownElement1.setConcreteName("My concrete name");
	concreteWorkBreakdownElement1.setPlannedTime(25.0f);
	concreteWorkBreakdownElement1.setPlannedFinishingDate(date);

	assertNotNull(this.concreteWorkBreakdownElement.hashCode());
	assertNotNull(concreteWorkBreakdownElement1.hashCode());
	assertEquals("equal", this.concreteWorkBreakdownElement.hashCode(), concreteWorkBreakdownElement1.hashCode());

	concreteWorkBreakdownElement1 = null;
    }

    @Test
    public void testThatAConcretePredecessorIsCorrectlyAddedToConcretePredecessorsCollection() {

	this.concreteWorkBreakdownElement.setConcreteName("My concrete name");
	this.concreteWorkBreakdownElement.setPlannedTime(25.0f);
	this.concreteWorkBreakdownElement.setPlannedFinishingDate(date);

	ConcreteWorkOrder concreteWorkOrder = new ConcreteWorkOrder();
	concreteWorkOrder.setConcreteLinkType("startToFinish");

	this.concreteWorkBreakdownElement.addConcretePredecessor(concreteWorkOrder);

	assertEquals("size of predeccessors collection in the concreteWorkBreakdownElement", 1,
		this.concreteWorkBreakdownElement.getConcretePredecessors().size());
	assertFalse("concreteWorkBreakdownElement in the concreteWorkOrder",
		concreteWorkOrder.getConcreteSuccessor() == null);

	assertTrue("concreteWorkOrders contains the concreteWorkOrder", this.concreteWorkBreakdownElement
		.getConcretePredecessors().contains(concreteWorkOrder));
	assertEquals("concreteWorkBreakdownElement : concrete name", this.concreteWorkBreakdownElement
		.getConcreteName(), concreteWorkOrder.getConcreteSuccessor().getConcreteName());
	assertEquals("concreteWorkBreakdownElement : planned time", this.concreteWorkBreakdownElement.getPlannedTime(),
		concreteWorkOrder.getConcreteSuccessor().getPlannedTime());
	assertEquals("concreteWorkBreakdownElement : planned finishing date",
		this.concreteWorkBreakdownElement.getPlannedFinishingDate(),
		concreteWorkOrder.getConcreteSuccessor()
			.getPlannedFinishingDate());
	assertEquals("concreteWorkBreakdownElement : state",
		this.concreteWorkBreakdownElement.getState(), concreteWorkOrder
			.getConcreteSuccessor().getState());

	concreteWorkOrder = null;
    }

    @Test
    public void testThatAConcreteSuccessorIsCorrectlyAddedToConcreteSuccessorsCollection() {

	this.concreteWorkBreakdownElement.setConcreteName("My concrete name");
	this.concreteWorkBreakdownElement.setPlannedTime(25.0f);
	this.concreteWorkBreakdownElement.setPlannedFinishingDate(date);

	ConcreteWorkOrder concreteWorkOrder = new ConcreteWorkOrder();
	concreteWorkOrder.setConcreteLinkType("startToFinish");

	this.concreteWorkBreakdownElement.addConcreteSuccessor(concreteWorkOrder);

	assertEquals("size of concreteSuccessors collection in the concreteWorkBreakdownElement", 1,
		this.concreteWorkBreakdownElement.getConcreteSuccessors().size());
	assertFalse("concreteWorkBreakdownElement in the concreteWorkOrder",
		concreteWorkOrder.getConcretePredecessor() == null);

	assertTrue("concreteWorkOrders contains the concreteWorkOrder", this.concreteWorkBreakdownElement
		.getConcreteSuccessors().contains(concreteWorkOrder));
	assertEquals("concreteWorkBreakdownElement : concrete name", this.concreteWorkBreakdownElement
		.getConcreteName(), concreteWorkOrder.getConcretePredecessor().getConcreteName());
	assertEquals("concreteWorkBreakdownElement : planned time", this.concreteWorkBreakdownElement.getPlannedTime(),
		concreteWorkOrder.getConcretePredecessor().getPlannedTime());
	assertEquals("concreteWorkBreakdownElement : planned finishing date",
		this.concreteWorkBreakdownElement.getPlannedFinishingDate(),
		concreteWorkOrder.getConcretePredecessor()
			.getPlannedFinishingDate());
	assertEquals("concreteWorkBreakdownElement : state",
		this.concreteWorkBreakdownElement.getState(), concreteWorkOrder
			.getConcretePredecessor().getState());

	concreteWorkOrder = null;
    }

    @Test
    public void testThatAConcretePredecessorIsCorrectlyRemovedFromConcretePredecessorsCollection() {

	ConcreteWorkOrder concreteWorkOrder = new ConcreteWorkOrder();
	concreteWorkOrder.setConcreteLinkType("startToFinish");

	this.concreteWorkBreakdownElement.addConcretePredecessor(concreteWorkOrder);
	this.concreteWorkBreakdownElement.removeConcretePredecessor(concreteWorkOrder);

	assertEquals("size of concretePredecessors collection in the concreteWorkBreakdownElement", 0,
		this.concreteWorkBreakdownElement.getConcretePredecessors().size());
	assertTrue("concreteWorkBreakdownElement of concreteWorkOrder is null", concreteWorkOrder
		.getConcreteSuccessor() == null);

	concreteWorkOrder = null;
    }

    @Test
    public void testThatAConcreteSuccessorIsCorrectlyRemovedFromConcreteSuccessorsCollection() {

	ConcreteWorkOrder concreteWorkOrder = new ConcreteWorkOrder();
	concreteWorkOrder.setConcreteLinkType("startToFinish");

	this.concreteWorkBreakdownElement.addConcretePredecessor(concreteWorkOrder);
	this.concreteWorkBreakdownElement.removeConcretePredecessor(concreteWorkOrder);

	assertEquals("size of concreteSuccessors collection in the concreteWorkBreakdownElement", 0,
		this.concreteWorkBreakdownElement.getConcretePredecessors().size());
	assertTrue("concreteWorkBreakdownElement of concreteWorkOrder is null", concreteWorkOrder
		.getConcreteSuccessor() == null);

	concreteWorkOrder = null;
    }

    @Test
    public void testThatAConcretePredecessorsCollectionIsCorrectlyAddedToConcretePredecessorsCollection() {

	this.concreteWorkBreakdownElement.setConcreteName("My concrete name");
	this.concreteWorkBreakdownElement.setPlannedTime(25.0f);
	this.concreteWorkBreakdownElement.setPlannedFinishingDate(date);

	Set<ConcreteWorkOrder> concreteWorkOrders = new HashSet<ConcreteWorkOrder>();

	ConcreteWorkOrder concreteWorkOrder1 = new ConcreteWorkOrder();
	concreteWorkOrder1.setConcreteLinkType("startToFinish");

	ConcreteWorkOrder concreteWorkOrder2 = new ConcreteWorkOrder();
	concreteWorkOrder2.setConcreteLinkType("startToStart");

	concreteWorkOrders.add(concreteWorkOrder1);
	concreteWorkOrders.add(concreteWorkOrder2);

	this.concreteWorkBreakdownElement.addAllConcretePredecessors(concreteWorkOrders);

	assertEquals("size of concretePredecessors collection in the concreteWorkBreakdownElement", 2,
		this.concreteWorkBreakdownElement.getConcretePredecessors().size());
	assertFalse("concreteWorkBreakdownElement in the concreteWorkOrder1",
		concreteWorkOrder1.getConcreteSuccessor() == null);
	assertFalse("concreteWorkBreakdownElement in the concreteWorkOrder2",
		concreteWorkOrder2.getConcreteSuccessor() == null);

	assertTrue("concreteWorkOrders contains the concreteWorkOrder1",
		this.concreteWorkBreakdownElement.getConcretePredecessors()
			.contains(concreteWorkOrder1));
	assertTrue("concreteWorkOrders contains the concreteWorkOrder2",
		this.concreteWorkBreakdownElement.getConcretePredecessors()
			.contains(concreteWorkOrder2));
	assertEquals("concreteWorkBreakdownElement : concrete name",
		this.concreteWorkBreakdownElement.getConcreteName(),
		concreteWorkOrder1.getConcreteSuccessor().getConcreteName());
	assertEquals("concreteWorkBreakdownElement : planned time",
		this.concreteWorkBreakdownElement.getPlannedTime(),
		concreteWorkOrder1.getConcreteSuccessor().getPlannedTime());
	assertEquals("concreteWorkBreakdownElement : planned finishing date",
		this.concreteWorkBreakdownElement.getPlannedFinishingDate(),
		concreteWorkOrder1.getConcreteSuccessor()
			.getPlannedFinishingDate());
	assertEquals("concreteWorkBreakdownElement : state",
		this.concreteWorkBreakdownElement.getState(),
		concreteWorkOrder1.getConcreteSuccessor().getState());

	assertEquals("concreteWorkBreakdownElement : concrete name",
		this.concreteWorkBreakdownElement.getConcreteName(),
		concreteWorkOrder2.getConcreteSuccessor().getConcreteName());
	assertEquals("concreteWorkBreakdownElement : planned time",
		this.concreteWorkBreakdownElement.getPlannedTime(),
		concreteWorkOrder2.getConcreteSuccessor().getPlannedTime());
	assertEquals("concreteWorkBreakdownElement : planned finishing date",
		this.concreteWorkBreakdownElement.getPlannedFinishingDate(),
		concreteWorkOrder2.getConcreteSuccessor()
			.getPlannedFinishingDate());
	assertEquals("concreteWorkBreakdownElement : state",
		this.concreteWorkBreakdownElement.getState(),
		concreteWorkOrder2.getConcreteSuccessor().getState());

	concreteWorkOrder2 = null;
	concreteWorkOrder1 = null;
	concreteWorkOrders = null;
    }

    @Test
    public void testThatAConcreteSuccessorsCollectionIsCorrectlyAddedToConcreteSuccessorsCollection() {

	this.concreteWorkBreakdownElement.setConcreteName("My concrete name");
	this.concreteWorkBreakdownElement.setPlannedTime(25.0f);
	this.concreteWorkBreakdownElement.setPlannedFinishingDate(date);

	Set<ConcreteWorkOrder> concreteWorkOrders = new HashSet<ConcreteWorkOrder>();

	ConcreteWorkOrder concreteWorkOrder1 = new ConcreteWorkOrder();
	concreteWorkOrder1.setConcreteLinkType("startToFinish");

	ConcreteWorkOrder concreteWorkOrder2 = new ConcreteWorkOrder();
	concreteWorkOrder2.setConcreteLinkType("startToStart");

	concreteWorkOrders.add(concreteWorkOrder1);
	concreteWorkOrders.add(concreteWorkOrder2);

	this.concreteWorkBreakdownElement.addAllConcreteSuccessors(concreteWorkOrders);

	assertEquals("size of concreteSuccessors collection in the concreteWorkBreakdownElement", 2,
		this.concreteWorkBreakdownElement.getConcreteSuccessors().size());
	assertFalse("concreteWorkBreakdownElement in the concreteWorkOrder1", concreteWorkOrder1
		.getConcretePredecessor() == null);
	assertFalse("concreteWorkBreakdownElement in the concreteWorkOrder2", concreteWorkOrder2
		.getConcretePredecessor() == null);

	assertTrue("concreteWorkOrders contains the concreteWorkOrder1", this.concreteWorkBreakdownElement
		.getConcreteSuccessors().contains(concreteWorkOrder1));
	assertTrue("concreteWorkOrders contains the concreteWorkOrder2", this.concreteWorkBreakdownElement
		.getConcreteSuccessors().contains(concreteWorkOrder2));
	assertEquals("concreteWorkBreakdownElement : concrete name", this.concreteWorkBreakdownElement
		.getConcreteName(), concreteWorkOrder1.getConcretePredecessor().getConcreteName());
	assertEquals("concreteWorkBreakdownElement : planned time", this.concreteWorkBreakdownElement.getPlannedTime(),
		concreteWorkOrder1.getConcretePredecessor().getPlannedTime());
	assertEquals("concreteWorkBreakdownElement : planned finishing date",
		this.concreteWorkBreakdownElement.getPlannedFinishingDate(),
		concreteWorkOrder1.getConcretePredecessor()
			.getPlannedFinishingDate());
	assertEquals("concreteWorkBreakdownElement : state",
		this.concreteWorkBreakdownElement.getState(),
		concreteWorkOrder1.getConcretePredecessor().getState());

	assertEquals("concreteWorkBreakdownElement : concrete name",
		this.concreteWorkBreakdownElement.getConcreteName(),
		concreteWorkOrder2.getConcretePredecessor().getConcreteName());
	assertEquals("concreteWorkBreakdownElement : planned time",
		this.concreteWorkBreakdownElement.getPlannedTime(),
		concreteWorkOrder2.getConcretePredecessor().getPlannedTime());
	assertEquals("concreteWorkBreakdownElement : planned finishing date",
		this.concreteWorkBreakdownElement.getPlannedFinishingDate(),
		concreteWorkOrder2.getConcretePredecessor()
			.getPlannedFinishingDate());
	assertEquals("concreteWorkBreakdownElement : state",
		this.concreteWorkBreakdownElement.getState(),
		concreteWorkOrder2.getConcretePredecessor().getState());

	concreteWorkOrder2 = null;
	concreteWorkOrder1 = null;
	concreteWorkOrders = null;
    }

    @Test
    public void testThatAConcretePredecessorsCollectionIsCorrectlyRemovedFromConcretePredecessorsCollection() {

	this.concreteWorkBreakdownElement.setConcreteName("My concrete name");
	this.concreteWorkBreakdownElement.setPlannedTime(25.0f);
	this.concreteWorkBreakdownElement.setPlannedFinishingDate(date);

	Set<ConcreteWorkOrder> concreteWorkOrders = new HashSet<ConcreteWorkOrder>();

	ConcreteWorkOrder concreteWorkOrder1 = new ConcreteWorkOrder();
	concreteWorkOrder1.setConcreteLinkType("startToFinish");

	ConcreteWorkOrder concreteWorkOrder2 = new ConcreteWorkOrder();
	concreteWorkOrder2.setConcreteLinkType("startToStart");

	concreteWorkOrders.add(concreteWorkOrder1);
	concreteWorkOrders.add(concreteWorkOrder2);

	this.concreteWorkBreakdownElement.addAllConcretePredecessors(concreteWorkOrders);
	this.concreteWorkBreakdownElement.removeAllConcretePredecessors();

	assertEquals("concretePredecessors contains the concreteWorkOrder", 0, this.concreteWorkBreakdownElement
		.getConcretePredecessors().size());
	assertTrue("concreteWorkBreakdownElement in the concreteWorkOrder1",
		concreteWorkOrder1.getConcreteSuccessor() == null);
	assertTrue("concreteWorkBreakdownElement in the concreteWorkOrder2",
		concreteWorkOrder2.getConcreteSuccessor() == null);

	concreteWorkOrder2 = null;
	concreteWorkOrder1 = null;
	concreteWorkOrders = null;
    }

    @Test
    public void testThatAConcreteSuccessorsCollectionIsCorrectlyRemovedFromConcreteSuccessorsCollection() {

	this.concreteWorkBreakdownElement.setConcreteName("My concrete name");
	this.concreteWorkBreakdownElement.setPlannedTime(25.0f);
	this.concreteWorkBreakdownElement.setPlannedFinishingDate(date);

	Set<ConcreteWorkOrder> concreteWorkOrders = new HashSet<ConcreteWorkOrder>();

	ConcreteWorkOrder concreteWorkOrder1 = new ConcreteWorkOrder();
	concreteWorkOrder1.setConcreteLinkType("startToFinish");

	ConcreteWorkOrder concreteWorkOrder2 = new ConcreteWorkOrder();
	concreteWorkOrder2.setConcreteLinkType("startToStart");

	concreteWorkOrders.add(concreteWorkOrder1);
	concreteWorkOrders.add(concreteWorkOrder2);

	this.concreteWorkBreakdownElement.addAllConcreteSuccessors(concreteWorkOrders);
	this.concreteWorkBreakdownElement.removeAllConcreteSuccessors();

	assertEquals("concretePredecessors contains the concreteWorkOrder", 0, this.concreteWorkBreakdownElement
		.getConcreteSuccessors().size());
	assertTrue("concreteWorkBreakdownElement in the concreteWorkOrder1", concreteWorkOrder1
		.getConcretePredecessor() == null);
	assertTrue("concreteWorkBreakdownElement in the concreteWorkOrder2", concreteWorkOrder2
		.getConcretePredecessor() == null);

	concreteWorkOrder2 = null;
	concreteWorkOrder1 = null;
	concreteWorkOrders = null;
    }
}
