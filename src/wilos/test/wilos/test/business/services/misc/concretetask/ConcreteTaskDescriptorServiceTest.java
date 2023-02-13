/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Sebastien BALARD <sbalard@wilos-project.org>
 * Copyright (C) 2007 Nicolas CASTEL <ncastel@wilos-project.org>
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
package wilos.test.business.services.misc.concretetask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hibernate.LazyInitializationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.misc.concreteactivity.ConcreteActivityService;
import wilos.business.services.misc.concreterole.ConcreteRoleDescriptorService;
import wilos.business.services.misc.concretetask.ConcreteTaskDescriptorService;
import wilos.business.services.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElementService;
import wilos.business.services.misc.concreteworkbreakdownelement.ConcreteWorkOrderService;
import wilos.business.services.misc.wilosuser.ParticipantService;
import wilos.business.services.spem2.role.RoleDescriptorService;
import wilos.business.services.spem2.task.TaskDescriptorService;
import wilos.hibernate.misc.concreterole.ConcreteRoleDescriptorDao;
import wilos.hibernate.misc.concretetask.ConcreteTaskDescriptorDao;
import wilos.hibernate.misc.project.ProjectDao;
import wilos.hibernate.misc.wilosuser.ParticipantDao;
import wilos.hibernate.spem2.role.RoleDescriptorDao;
import wilos.hibernate.spem2.task.TaskDescriptorDao;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.project.Project;
import wilos.model.misc.wilosuser.Participant;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.model.spem2.workbreakdownelement.WorkOrder;
import wilos.test.TestConfiguration;
import wilos.utils.Constantes.State;

public class ConcreteTaskDescriptorServiceTest {

	private ConcreteTaskDescriptorService concreteTaskDescriptorService = (ConcreteTaskDescriptorService) TestConfiguration
			.getInstance().getApplicationContext().getBean(
					"ConcreteTaskDescriptorService");

	private ConcreteRoleDescriptorService concreteRoleDescriptorService = (ConcreteRoleDescriptorService) TestConfiguration
			.getInstance().getApplicationContext().getBean(
					"ConcreteRoleDescriptorService");

	private TaskDescriptorService taskDescriptorService = (TaskDescriptorService) TestConfiguration
			.getInstance().getApplicationContext().getBean(
					"TaskDescriptorService");
	
	private RoleDescriptorService roleDescriptorService = (RoleDescriptorService) TestConfiguration
	.getInstance().getApplicationContext().getBean(
			"RoleDescriptorService");

	private ParticipantService participantService = (ParticipantService) TestConfiguration
			.getInstance().getApplicationContext().getBean(
					"ParticipantService");

	private ConcreteWorkBreakdownElementService concreteWorkBreakdownElementService = (ConcreteWorkBreakdownElementService) TestConfiguration
			.getInstance().getApplicationContext().getBean(
					"ConcreteWorkBreakdownElementService");

	private ConcreteWorkOrderService concreteWorkOrderService = (ConcreteWorkOrderService) TestConfiguration
			.getInstance().getApplicationContext().getBean(
					"ConcreteWorkOrderService");

	private ConcreteActivityService concreteActivityService = (ConcreteActivityService) TestConfiguration
			.getInstance().getApplicationContext().getBean(
					"ConcreteActivityService");

	private ConcreteTaskDescriptor concreteTaskDescriptor;

	private ConcreteTaskDescriptorDao concreteTaskDescriptorDao = (ConcreteTaskDescriptorDao) TestConfiguration
			.getInstance().getApplicationContext().getBean(
					"ConcreteTaskDescriptorDao");

	private ProjectDao projectDao = (ProjectDao) TestConfiguration
			.getInstance().getApplicationContext().getBean("ProjectDao");

	private ParticipantDao participantdao = (ParticipantDao) TestConfiguration
			.getInstance().getApplicationContext().getBean("ParticipantDao");

	private TaskDescriptorDao taskDescriptordao = (TaskDescriptorDao) TestConfiguration
			.getInstance().getApplicationContext().getBean("TaskDescriptorDao");

	private RoleDescriptorDao roleDescriptordao = (RoleDescriptorDao) TestConfiguration
			.getInstance().getApplicationContext().getBean("RoleDescriptorDao");

	private ConcreteRoleDescriptorDao concreteRoleDescriptorDao = (ConcreteRoleDescriptorDao) TestConfiguration
			.getInstance().getApplicationContext().getBean(
					"ConcreteRoleDescriptorDao");

	private TaskDescriptor taskDescriptor;

	@Before
	public void setUp() {

		this.concreteTaskDescriptor = new ConcreteTaskDescriptor();
		this.concreteTaskDescriptor.setConcreteName("My name");
		this.concreteTaskDescriptor.setAccomplishedTime(3);
	}

	@After
	public void tearDown() {
		this.concreteTaskDescriptor = null;
	}

	@Test
	public void testGetConcreteTaskDescriptorsForProject() {
		// Rk: the setUp method is called here.

	    this.concreteTaskDescriptor.setAccomplishedTime(111);
		this.concreteTaskDescriptorService
				.getConcreteTaskDescriptorDao()
				.saveOrUpdateConcreteTaskDescriptor(this.concreteTaskDescriptor);

		Project project = new Project();
		project.setConcreteName("project");
		this.projectDao.saveOrUpdateProject(project);

		ConcreteTaskDescriptor ctdTmp = new ConcreteTaskDescriptor();
		ctdTmp.setAccomplishedTime(112);
		ctdTmp.setProject(project);

		ConcreteTaskDescriptor ctdTmp2 = new ConcreteTaskDescriptor();
		ctdTmp2.setAccomplishedTime(113);
		ctdTmp2.setProject(project);

		this.concreteTaskDescriptorService.getConcreteTaskDescriptorDao()
				.saveOrUpdateConcreteTaskDescriptor(ctdTmp);
		this.concreteTaskDescriptorService.getConcreteTaskDescriptorDao()
				.saveOrUpdateConcreteTaskDescriptor(ctdTmp2);

		List<ConcreteTaskDescriptor> list = this.concreteTaskDescriptorService
				.getAllConcreteTaskDescriptorsForProject(project.getId());

		assertNotNull(list);
		assertTrue(list.size() == 2);

		// cleaning up used objects
		this.concreteTaskDescriptorService.getConcreteTaskDescriptorDao()
				.deleteConcreteTaskDescriptor(ctdTmp);
		this.concreteTaskDescriptorService.getConcreteTaskDescriptorDao()
				.deleteConcreteTaskDescriptor(ctdTmp2);
		this.projectDao.deleteProject(project);

		this.concreteTaskDescriptorService.getConcreteTaskDescriptorDao()
				.deleteConcreteTaskDescriptor(this.concreteTaskDescriptor);

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testStartConcreteTaskDescriptor() {
		// Rk: the setUp method is called here.
	    this.concreteTaskDescriptor.setAccomplishedTime(222);
		this.concreteTaskDescriptorService
				.getConcreteTaskDescriptorDao()
				.saveOrUpdateConcreteTaskDescriptor(this.concreteTaskDescriptor);

		ConcreteActivity concreteActivity = new ConcreteActivity();
		concreteActivity.setConcreteName("activity name");
		concreteActivity
				.addConcreteBreakdownElement(this.concreteTaskDescriptor);
		this.concreteActivityService.getConcreteActivityDao()
				.saveOrUpdateConcreteActivity(concreteActivity);

		// Change the state of the concretetaskdescriptor.
		this.concreteTaskDescriptorService
				.startConcreteTaskDescriptor(this.concreteTaskDescriptor);

		String taskId = this.concreteTaskDescriptor.getId();
		String activityId = concreteActivity.getId();

		// Get this concreteTaskDescriptor.
		ConcreteTaskDescriptor tmpConcreteTaskDescriptor = this.concreteTaskDescriptorService
				.getConcreteTaskDescriptor(taskId);

		// Get the super concreteActivity.
		ConcreteActivity tmpConcreteActivity = this.concreteActivityService
				.getConcreteActivity(activityId);

		assertNotNull(tmpConcreteTaskDescriptor);
		assertEquals(tmpConcreteTaskDescriptor.getState(), State.STARTED);

		assertNotNull(tmpConcreteActivity);
		assertEquals(tmpConcreteActivity.getState(), State.STARTED);

		this.concreteTaskDescriptorService.getConcreteTaskDescriptorDao()
				.deleteConcreteTaskDescriptor(this.concreteTaskDescriptor);

		this.concreteActivityService.getConcreteActivityDao()
				.deleteConcreteActivity(concreteActivity);

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testSuspendConcreteTaskDescriptor() {
		// Rk: the setUp method is called here.
	    this.concreteTaskDescriptor.setAccomplishedTime(333);
		this.concreteTaskDescriptorService
				.getConcreteTaskDescriptorDao()
				.saveOrUpdateConcreteTaskDescriptor(this.concreteTaskDescriptor);

		// Change the state of the concretetaskdescriptor.
		this.concreteTaskDescriptorService
				.suspendConcreteTaskDescriptor(this.concreteTaskDescriptor);

		this.concreteTaskDescriptorDao
				.saveOrUpdateConcreteTaskDescriptor(this.concreteTaskDescriptor);
		String id = this.concreteTaskDescriptor.getId();

		// Get this concreteTaskDescriptor.
		ConcreteTaskDescriptor tmpConcreteTaskDescriptor = this.concreteTaskDescriptorService
				.getConcreteTaskDescriptor(id);

		assertNotNull(tmpConcreteTaskDescriptor);
		assertEquals(tmpConcreteTaskDescriptor.getState(), State.SUSPENDED);
		//clean
		this.concreteTaskDescriptorService.getConcreteTaskDescriptorDao()
				.deleteConcreteTaskDescriptor(this.concreteTaskDescriptor);

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testFinishConcreteTaskDescriptor() {
		// Rk: the setUp method is called here.
	    this.concreteTaskDescriptor.setAccomplishedTime(444);
		this.concreteTaskDescriptorService.saveConcreteTaskDescriptor(this.concreteTaskDescriptor);

		ConcreteActivity concreteActivity = new ConcreteActivity();
		concreteActivity.setConcreteName("activity name");
		concreteActivity
				.addConcreteBreakdownElement(this.concreteTaskDescriptor);
		this.concreteActivityService.getConcreteActivityDao()
				.saveOrUpdateConcreteActivity(concreteActivity);

		// Change the state of the concretetaskdescriptor.
		this.concreteTaskDescriptorService
				.finishConcreteTaskDescriptor(this.concreteTaskDescriptor);

		String taskId = this.concreteTaskDescriptor.getId();
		String activityId = concreteActivity.getId();

		// Get this concreteTaskDescriptor.
		ConcreteTaskDescriptor tmpConcreteTaskDescriptor = this.concreteTaskDescriptorService
				.getConcreteTaskDescriptor(taskId);

		// Get the super concreteActivity.
		ConcreteActivity tmpConcreteActivity = this.concreteActivityService
				.getConcreteActivity(activityId);

		assertNotNull(tmpConcreteTaskDescriptor);
		assertEquals(tmpConcreteTaskDescriptor.getState(), State.FINISHED);

		assertNotNull(tmpConcreteActivity);
		assertEquals(tmpConcreteActivity.getState(), State.FINISHED);

		//clean
		this.concreteTaskDescriptorService.getConcreteTaskDescriptorDao()
				.deleteConcreteTaskDescriptor(this.concreteTaskDescriptor);

		this.concreteActivityService.getConcreteActivityDao()
				.deleteConcreteActivity(concreteActivity);

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testAffectedConcreteTaskDescriptor() {
	    this.concreteTaskDescriptor.setAccomplishedTime(555);
		this.concreteTaskDescriptorService.saveConcreteTaskDescriptor(this.concreteTaskDescriptor);

		Participant monpar = new Participant();
		monpar.setName("testAffectedConcreteTaskDescriptor");
		this.participantdao.saveOrUpdateParticipant(monpar);

		this.taskDescriptor = new TaskDescriptor();
		this.taskDescriptor.setGuid("Mon guid");
		this.taskDescriptordao.saveOrUpdateTaskDescriptor(this.taskDescriptor);

		RoleDescriptor roleDescriptor = new RoleDescriptor();
		roleDescriptor.setGuid("Mon guid");
		this.roleDescriptordao.saveOrUpdateRoleDescriptor(roleDescriptor);

		ConcreteRoleDescriptor concreteRoleDescriptor = new ConcreteRoleDescriptor();
		concreteRoleDescriptor.setConcreteName("Mon concrete name");
		this.concreteRoleDescriptorDao
				.saveOrUpdateConcreteRoleDescriptor(concreteRoleDescriptor);

		concreteRoleDescriptor.addParticipant(monpar);
		this.participantdao.saveOrUpdateParticipant(monpar);

		concreteRoleDescriptor.addRoleDescriptor(roleDescriptor);
		this.taskDescriptor.addMainRole(roleDescriptor);

		this.concreteTaskDescriptor.addTaskDescriptor(this.taskDescriptor);
		this.concreteTaskDescriptor
				.addMainConcreteRoleDescriptor(concreteRoleDescriptor);

		this.taskDescriptordao.saveOrUpdateTaskDescriptor(this.taskDescriptor);
		this.roleDescriptordao.saveOrUpdateRoleDescriptor(roleDescriptor);
		this.concreteRoleDescriptorDao
				.saveOrUpdateConcreteRoleDescriptor(concreteRoleDescriptor);
		this.concreteTaskDescriptorService
				.getConcreteTaskDescriptorDao()
				.saveOrUpdateConcreteTaskDescriptor(this.concreteTaskDescriptor);

		this.concreteTaskDescriptorService.affectedConcreteTaskDescriptor(
				concreteTaskDescriptor, monpar);

		ConcreteRoleDescriptor res = concreteTaskDescriptor
				.getMainConcreteRoleDescriptor();
		assertTrue(res.equals(concreteRoleDescriptor));

		concreteRoleDescriptor.removeParticipant(monpar);
		this.participantdao.saveOrUpdateParticipant(monpar);

		concreteRoleDescriptor.removeRoleDescriptor(roleDescriptor);
		this.taskDescriptor.removeMainRole(roleDescriptor);
		this.concreteTaskDescriptor.removeTaskDescriptor(this.taskDescriptor);
		this.concreteTaskDescriptor
				.removeMainConcreteRoleDescriptor(concreteRoleDescriptor);

		this.taskDescriptordao.saveOrUpdateTaskDescriptor(this.taskDescriptor);
		this.roleDescriptordao.saveOrUpdateRoleDescriptor(roleDescriptor);
		this.concreteRoleDescriptorDao
				.saveOrUpdateConcreteRoleDescriptor(concreteRoleDescriptor);
		this.concreteTaskDescriptorService
				.getConcreteTaskDescriptorDao()
				.saveOrUpdateConcreteTaskDescriptor(this.concreteTaskDescriptor);

		// cleanup used objects
		this.participantdao.deleteParticipant(monpar);
		this.taskDescriptordao.deleteTaskDescriptor(this.taskDescriptor);
		this.roleDescriptordao.deleteRoleDescriptor(roleDescriptor);
		this.concreteRoleDescriptorDao
				.deleteConcreteRoleDescriptor(concreteRoleDescriptor);
		this.concreteTaskDescriptorService.getConcreteTaskDescriptorDao()
				.deleteConcreteTaskDescriptor(this.concreteTaskDescriptor);

	}

	@Test
	public void testRemoveConcreteTaskDescriptor() {
	    this.concreteTaskDescriptor.setAccomplishedTime(666);
	  
		this.concreteTaskDescriptorService
				.getConcreteTaskDescriptorDao()
				.saveOrUpdateConcreteTaskDescriptor(this.concreteTaskDescriptor);

		Participant monpar = new Participant();
		monpar.setName("testRemoveConcreteTaskDescriptor");
		this.participantdao.saveOrUpdateParticipant(monpar);

		this.taskDescriptor = new TaskDescriptor();
		this.taskDescriptor.setPresentationName("lolo 1");
		this.taskDescriptordao.saveOrUpdateTaskDescriptor(this.taskDescriptor);

		RoleDescriptor roleDescriptor = new RoleDescriptor();
		roleDescriptor.setPresentationName("lolo2");
		this.roleDescriptordao.saveOrUpdateRoleDescriptor(roleDescriptor);

		ConcreteRoleDescriptor concreteRoleDescriptor = new ConcreteRoleDescriptor();
		this.concreteRoleDescriptorDao
				.saveOrUpdateConcreteRoleDescriptor(concreteRoleDescriptor);

		concreteRoleDescriptor.addParticipant(monpar);
		this.participantdao.saveOrUpdateParticipant(monpar);
		this.concreteRoleDescriptorDao
				.saveOrUpdateConcreteRoleDescriptor(concreteRoleDescriptor);

		concreteRoleDescriptor.addRoleDescriptor(roleDescriptor);
		this.taskDescriptor.addMainRole(roleDescriptor);
		this.roleDescriptordao.saveOrUpdateRoleDescriptor(roleDescriptor);

		// predecessor task
		ConcreteTaskDescriptor ctdPred = new ConcreteTaskDescriptor();
		ctdPred.setAccomplishedTime(888);
		this.concreteTaskDescriptorService.getConcreteTaskDescriptorDao().saveOrUpdateConcreteTaskDescriptor(ctdPred);
		this.concreteWorkOrderService.saveConcreteWorkOrder(ctdPred.getId(),
				this.concreteTaskDescriptor.getId(), WorkOrder.FINISH_TO_START,
				null);
		ConcreteWorkBreakdownElement cwbdePred = this.concreteWorkBreakdownElementService
				.getConcreteWorkBreakdownElementDao()
				.getConcreteWorkBreakdownElement(ctdPred.getId());
		assertNotNull("not null", cwbdePred);
		assertEquals(1, this.concreteWorkOrderService.getConcreteSuccessors(
				cwbdePred).size());
	
		// successor task
		ConcreteTaskDescriptor ctdSucc = new ConcreteTaskDescriptor();
		ctdSucc.setAccomplishedTime(999);
		this.concreteTaskDescriptorService.getConcreteTaskDescriptorDao().saveOrUpdateConcreteTaskDescriptor(ctdSucc);
		this.concreteWorkOrderService.saveConcreteWorkOrder(
				this.concreteTaskDescriptor.getId(), ctdSucc.getId(),
				WorkOrder.FINISH_TO_START, null);
		ConcreteWorkBreakdownElement cwbdeSucc = this.concreteWorkBreakdownElementService
				.getConcreteWorkBreakdownElementDao()
				.getConcreteWorkBreakdownElement(ctdSucc.getId());
		assertNotNull("not null", cwbdeSucc);
		assertEquals(1, this.concreteWorkOrderService.getConcretePredecessors(
				cwbdeSucc).size());		
		
		this.concreteTaskDescriptor.addTaskDescriptor(this.taskDescriptor);
		this.taskDescriptordao.saveOrUpdateTaskDescriptor(this.taskDescriptor);

		this.concreteTaskDescriptor
				.addMainConcreteRoleDescriptor(concreteRoleDescriptor);
		this.concreteRoleDescriptorDao
				.saveOrUpdateConcreteRoleDescriptor(concreteRoleDescriptor);
		this.concreteTaskDescriptorService
				.getConcreteTaskDescriptorDao()
				.saveOrUpdateConcreteTaskDescriptor(this.concreteTaskDescriptor);

		ConcreteWorkBreakdownElement retrievedCwbde = this.concreteWorkBreakdownElementService
				.getConcreteWorkBreakdownElementDao()
				.getConcreteWorkBreakdownElement(
						this.concreteTaskDescriptor.getId());
				
		assertEquals(1, this.concreteWorkOrderService.getConcretePredecessors(
				retrievedCwbde).size());

		assertEquals(1, this.concreteWorkOrderService.getConcreteSuccessors(
				retrievedCwbde).size());

		assertEquals(1, this.taskDescriptorService.getConcreteTaskDescriptors(
				this.taskDescriptor).size());

		assertEquals(1, this.concreteRoleDescriptorService
				.getPrimaryConcreteTaskDescriptors(concreteRoleDescriptor)
				.size());

		this.concreteTaskDescriptorService
				.removeConcreteTaskDescriptor(this.concreteTaskDescriptor);

		assertEquals(0, this.concreteRoleDescriptorService
				.getPrimaryConcreteTaskDescriptors(concreteRoleDescriptor)
				.size());

		this.taskDescriptor.removeMainRole(roleDescriptor);
				
		this.concreteRoleDescriptorDao
				.saveOrUpdateConcreteRoleDescriptor(concreteRoleDescriptor);		

		// cleanup used objects
		this.participantService.deleteParticipant(monpar.getId());	
		
		this.taskDescriptorService.getTaskDescriptorDao().deleteTaskDescriptor(this.taskDescriptor);
	
		this.concreteTaskDescriptorService.getConcreteTaskDescriptorDao().deleteConcreteTaskDescriptor(ctdSucc);
		
		this.concreteTaskDescriptorService.getConcreteTaskDescriptorDao().deleteConcreteTaskDescriptor(ctdPred);		
				
		this.concreteRoleDescriptorService.getConcreteRoleDescriptorDao()
				.deleteConcreteRoleDescriptor(concreteRoleDescriptor);
		
	}

	@Test
	public void testDissociateConcreteTaskDescriptor() {
	    this.concreteTaskDescriptor.setAccomplishedTime(1000);
		this.concreteTaskDescriptorService
				.getConcreteTaskDescriptorDao()
				.saveOrUpdateConcreteTaskDescriptor(this.concreteTaskDescriptor);

		Participant parti = new Participant();
		parti.setName("bob");
		this.participantdao.saveOrUpdateParticipant(parti);

		this.concreteTaskDescriptorDao
				.saveOrUpdateConcreteTaskDescriptor(this.concreteTaskDescriptor);

		ConcreteRoleDescriptor crd = new ConcreteRoleDescriptor();
		crd.addPrimaryConcreteTaskDescriptor(this.concreteTaskDescriptor);
		crd.addParticipant(parti);
		this.concreteRoleDescriptorDao.saveOrUpdateConcreteRoleDescriptor(crd);

		parti.addConcreteRoleDescriptor(crd);
		this.participantdao.saveOrUpdateParticipant(parti);

		this.concreteTaskDescriptor.setMainConcreteRoleDescriptor(crd);
		this.concreteTaskDescriptorDao
				.saveOrUpdateConcreteTaskDescriptor(this.concreteTaskDescriptor);

		this.concreteTaskDescriptorService
				.dissociateConcreteTaskDescriptor(this.concreteTaskDescriptor);

		assertTrue(crd.getPrimaryConcreteTaskDescriptors().size() == 0);
		
		//clean
		this.concreteTaskDescriptorService.getConcreteTaskDescriptorDao()
				.deleteConcreteTaskDescriptor(this.concreteTaskDescriptor);
		
		this.concreteRoleDescriptorDao.deleteConcreteRoleDescriptor(crd);
		
		this.participantdao.deleteParticipant(parti);
	}
}
