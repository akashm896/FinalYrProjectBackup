/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
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

package wilos.test.hibernate.misc.concreteworkbreakdownelement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.misc.project.ProjectService;
import wilos.hibernate.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElementDao;
import wilos.hibernate.misc.concreteworkbreakdownelement.ConcreteWorkOrderDao;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkOrder;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkOrderId;
import wilos.model.misc.project.Project;
import wilos.test.TestConfiguration;
import wilos.utils.Constantes;

/**
 * @author Sebastien BALARD
 * 
 */
public class ConcreteWorkOrderDaoTest {

    private ConcreteWorkOrderDao concreteWorkOrderDao = (ConcreteWorkOrderDao) TestConfiguration
	    .getInstance().getApplicationContext().getBean(
		    "ConcreteWorkOrderDao");

    private ConcreteWorkBreakdownElementDao concreteWorkBreakdownElementDao = (ConcreteWorkBreakdownElementDao) TestConfiguration
	    .getInstance().getApplicationContext().getBean(
		    "ConcreteWorkBreakdownElementDao");

    private ProjectService projectService = (ProjectService) TestConfiguration
	    .getInstance().getApplicationContext().getBean("ProjectService");

    private ConcreteWorkOrder concreteWorkOrder = null;

    private ConcreteWorkBreakdownElement concreteWorkBreakdownElement1 = null;

    private ConcreteWorkBreakdownElement concreteWorkBreakdownElement2 = null;

    private Date date;

    @Before
    public void setUp() {

	try {
	    date = Constantes.DATE_FORMAT.parse("18/01/2007 10:00");
	} catch (ParseException e) {
	    e.printStackTrace();
	}

	this.concreteWorkOrder = new ConcreteWorkOrder();
	this.concreteWorkOrder.setConcreteLinkType("startToStart");

	this.concreteWorkBreakdownElement1 = new ConcreteWorkBreakdownElement();
	this.concreteWorkBreakdownElement1.setConcreteName("My concrete name");
	this.concreteWorkBreakdownElement1.setPlannedTime(25.0f);
	this.concreteWorkBreakdownElement1.setPlannedFinishingDate(date);

	this.concreteWorkBreakdownElement2 = new ConcreteWorkBreakdownElement();
	this.concreteWorkBreakdownElement2
		.setConcreteName("Your concrete name");
	this.concreteWorkBreakdownElement2.setPlannedTime(4.0f);
	this.concreteWorkBreakdownElement2.setPlannedFinishingDate(date);
    }

    @After
    public void tearDown() {
	this.concreteWorkOrder = null;
	this.concreteWorkBreakdownElement1 = null;
	this.concreteWorkBreakdownElement2 = null;
    }

    @Test
    public void testStorageOfANullConcreteWorkOrder() {

	ConcreteWorkOrder concreteWorkOrder = null;

	ConcreteWorkOrderId id = this.concreteWorkOrderDao
		.saveOrUpdateConcreteWorkOrder(concreteWorkOrder);
	ConcreteWorkOrder retrievedConcreteWorkOrder = this.concreteWorkOrderDao
		.getConcreteWorkOrder(id);

	assertNull("null", retrievedConcreteWorkOrder);
    }

    @Test
    public void testStorageOfAConcreteWorkOrder() {

	Project p = new Project();

	p.setConcreteName("Wilos");
	p.setDescription("projet de test");
	p.setIsFinished(true);
	this.projectService.getProjectDao().saveOrUpdateProject(p);

	String cwbde1Id = this.concreteWorkBreakdownElementDao
		.saveOrUpdateConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement1);
	String cwbde2Id = this.concreteWorkBreakdownElementDao
		.saveOrUpdateConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement2);

	ConcreteWorkOrderId concreteWorkOrderId = new ConcreteWorkOrderId();
	concreteWorkOrderId.setConcretePredecessorId(cwbde1Id);
	concreteWorkOrderId.setConcreteSuccessorId(cwbde2Id);
	this.concreteWorkOrder.setConcreteWorkOrderId(concreteWorkOrderId);
	this.concreteWorkOrder
		.setConcretePredecessor(this.concreteWorkBreakdownElement1);
	this.concreteWorkOrder
		.setConcreteSuccessor(this.concreteWorkBreakdownElement2);
	this.concreteWorkOrder.setProjectId(p.getId());

	ConcreteWorkOrderId id = this.concreteWorkOrderDao
		.saveOrUpdateConcreteWorkOrder(this.concreteWorkOrder);

	ConcreteWorkOrder retrievedConcreteWorkOrder = this.concreteWorkOrderDao
		.getConcreteWorkOrder(id);
	retrievedConcreteWorkOrder.setConcreteLinkType("startToStart");

	assertEquals("equal id", this.concreteWorkOrder,
		retrievedConcreteWorkOrder);

	this.concreteWorkOrderDao
		.deleteConcreteWorkOrder(this.concreteWorkOrder);
	this.concreteWorkBreakdownElementDao
		.deleteConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement2);
	this.concreteWorkBreakdownElementDao
		.deleteConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement1);
	
	this.projectService.deleteProject(p.getId());
    }

    @Test
    public void testDeletionOfAConcreteWorkOrder() {

	Project p = new Project();
	p.setConcreteName("Wilos");
	p.setDescription("projet de test");
	p.setIsFinished(true);
	this.projectService.getProjectDao().saveOrUpdateProject(p);
	
	String cwbde1Id = this.concreteWorkBreakdownElementDao
		.saveOrUpdateConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement1);
	String cwbde2Id = this.concreteWorkBreakdownElementDao
		.saveOrUpdateConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement2);

	ConcreteWorkOrderId concreteWorkOrderId = new ConcreteWorkOrderId();
	concreteWorkOrderId.setConcretePredecessorId(cwbde1Id);
	concreteWorkOrderId.setConcreteSuccessorId(cwbde2Id);
	this.concreteWorkOrder.setConcreteWorkOrderId(concreteWorkOrderId);

	this.concreteWorkOrder.setProjectId(p.getId());
	
	ConcreteWorkOrderId id = this.concreteWorkOrderDao
		.saveOrUpdateConcreteWorkOrder(this.concreteWorkOrder);
	this.concreteWorkOrderDao
		.deleteConcreteWorkOrder(this.concreteWorkOrder);
	ConcreteWorkOrder retrievedConcreteWorkOrder = this.concreteWorkOrderDao
		.getConcreteWorkOrder(id);

	assertNull("null", retrievedConcreteWorkOrder);

	this.concreteWorkBreakdownElementDao
		.deleteConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement2);
	this.concreteWorkBreakdownElementDao
		.deleteConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement1);
	
	this.projectService.deleteProject(p.getId());
    }

    @Test
    public void testRetrievalOfAllConcreteWorkOrders() {

	Project p = new Project();
	p.setConcreteName("Wilos");
	p.setDescription("projet de test");
	p.setIsFinished(true);
	this.projectService.getProjectDao().saveOrUpdateProject(p);
	
	Project p1 = new Project();
	p1.setConcreteName("Wilos2");
	p1.setDescription("projet de test 2");
	p1.setIsFinished(true);
	this.projectService.getProjectDao().saveOrUpdateProject(p1);
	
	int nb = this.concreteWorkOrderDao.getAllConcreteWorkOrders().size();

	String cwbde1Id = this.concreteWorkBreakdownElementDao
		.saveOrUpdateConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement1);
	
	String cwbde2Id = this.concreteWorkBreakdownElementDao
		.saveOrUpdateConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement2);

	ConcreteWorkOrderId concreteWorkOrderId = new ConcreteWorkOrderId();
	concreteWorkOrderId.setConcretePredecessorId(cwbde1Id);
	concreteWorkOrderId.setConcreteSuccessorId(cwbde2Id);
	this.concreteWorkOrder.setConcreteWorkOrderId(concreteWorkOrderId);

	this.concreteWorkBreakdownElement1
		.addConcreteSuccessor(this.concreteWorkOrder);
	this.concreteWorkBreakdownElement2
		.addConcretePredecessor(this.concreteWorkOrder);

	this.concreteWorkOrder.setProjectId(p1.getId());
	
	this.concreteWorkOrderDao
		.saveOrUpdateConcreteWorkOrder(this.concreteWorkOrder);

	ConcreteWorkOrder concreteWorkOrder1 = new ConcreteWorkOrder();
	concreteWorkOrder1.setConcreteLinkType("startToFinish");

	ConcreteWorkBreakdownElement concreteWorkBreakdownElement = new ConcreteWorkBreakdownElement();
	concreteWorkBreakdownElement.setConcreteName("My concrete name");
	concreteWorkBreakdownElement.setPlannedTime(25.0f);
	concreteWorkBreakdownElement.setPlannedFinishingDate(date);

	String cwbde3Id = this.concreteWorkBreakdownElementDao
		.saveOrUpdateConcreteWorkBreakdownElement(concreteWorkBreakdownElement);

	ConcreteWorkOrderId concreteWorkOrder1Id = new ConcreteWorkOrderId();
	concreteWorkOrder1Id.setConcretePredecessorId(cwbde1Id);
	concreteWorkOrder1Id.setConcreteSuccessorId(cwbde3Id);
	concreteWorkOrder1.setConcreteWorkOrderId(concreteWorkOrder1Id);
	concreteWorkOrder1.setProjectId(p.getId());
	
	this.concreteWorkBreakdownElement1
		.addConcreteSuccessor(concreteWorkOrder1);
	concreteWorkBreakdownElement.addConcretePredecessor(concreteWorkOrder1);

	this.concreteWorkOrderDao
		.saveOrUpdateConcreteWorkOrder(concreteWorkOrder1);

	List<ConcreteWorkOrder> concreteWorkOrders = this.concreteWorkOrderDao
		.getAllConcreteWorkOrders();
	assertNotNull("list not null", concreteWorkOrders);
	assertEquals("size", 2, concreteWorkOrders.size() - nb);

	this.concreteWorkOrderDao.deleteConcreteWorkOrder(concreteWorkOrder1);
	this.concreteWorkBreakdownElementDao
		.deleteConcreteWorkBreakdownElement(concreteWorkBreakdownElement);

	this.concreteWorkOrderDao
		.deleteConcreteWorkOrder(this.concreteWorkOrder);
	this.concreteWorkBreakdownElementDao
		.deleteConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement2);
	this.concreteWorkBreakdownElementDao
		.deleteConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement1);

	concreteWorkOrderId = null;
	concreteWorkOrder1 = null;
	concreteWorkOrder1Id = null;
	
	this.projectService.deleteProject(p1.getId());
	this.projectService.deleteProject(p.getId());
    }

}
