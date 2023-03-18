/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

package wilos.test.business.services.misc.project;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.misc.project.ProjectService;
import wilos.hibernate.misc.concreteactivity.ConcreteActivityDao;
import wilos.hibernate.misc.concretetask.ConcreteTaskDescriptorDao;
import wilos.hibernate.spem2.role.RoleDescriptorDao;
import wilos.hibernate.spem2.task.TaskDescriptorDao;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.project.Project;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.test.TestConfiguration;

public class ProjectServiceTest {

	private ProjectService ps;

	private Project p;

	private Project p2;

	private RoleDescriptorDao roleDescriptordao;

	private TaskDescriptorDao taskDescriptordao;

	private ConcreteTaskDescriptorDao concreteTaskDescriptorDao;

	private ConcreteActivityDao concreteActivityDao;

	public ProjectServiceTest() {
		this.ps = (ProjectService) TestConfiguration.getInstance()
				.getApplicationContext().getBean("ProjectService");
		this.roleDescriptordao = (RoleDescriptorDao) TestConfiguration
				.getInstance().getApplicationContext().getBean(
						"RoleDescriptorDao");
		this.taskDescriptordao = (TaskDescriptorDao) TestConfiguration
				.getInstance().getApplicationContext().getBean(
						"TaskDescriptorDao");
		this.concreteTaskDescriptorDao = (ConcreteTaskDescriptorDao) TestConfiguration
				.getInstance().getApplicationContext().getBean(
						"ConcreteTaskDescriptorDao");
		this.concreteActivityDao = (ConcreteActivityDao) TestConfiguration
				.getInstance().getApplicationContext().getBean(
						"ConcreteActivityDao");

	}

	@Before
	public void setUp() {

		p = new Project();
		p.setConcreteName("Wilos");
		p.setDescription("projet de test");
		p.setIsFinished(true);
		this.ps.getProjectDao().saveOrUpdateProject(p);

		p2 = new Project();
		p2.setConcreteName("Wilos2");
		p2.setDescription("projet de test2");
		p2.setIsFinished(false);
		this.ps.getProjectDao().saveOrUpdateProject(p2);


	}

	@After
	public void tearDown() {
		// Delete the tmp Project from the database.
		this.ps.deleteProject(this.p.getId());
		this.ps.deleteProject(this.p2.getId());
		//p3 is deleted on the test testDeleteProject
	}


	@Test
	public void testSaveProject() {
		this.ps.saveProject(this.p);
		Project ProjectTmp = (Project) this.ps.getProjectDao().getProject(
				this.p.getId());
		assertNotNull(ProjectTmp);
		assertEquals(ProjectTmp.getConcreteName(), "Wilos");
		assertEquals(ProjectTmp.getDescription(), "projet de test");
	}

	@Test
	public void testDeleteProject() {
		Project p3 = new Project();
		p3.setConcreteName("Wilos3");
		p3.setDescription("projet de test3");
		p3.setIsFinished(false);
		this.ps.saveProject(p3);
		Project ProjectTmp = (Project) this.ps.getProjectDao().getProject(
				p3.getId());
		assertNotNull(ProjectTmp);
		this.ps.deleteProject(p3.getId());
		ProjectTmp = (Project) this.ps.getProjectDao().getProject(
				p3.getId());
		assertNull(ProjectTmp);
	}

	@Test
	public void testProjectExists() {
		// Test for an existing project
		this.ps.saveProject(this.p);
		assertTrue(this.ps.projectExist("Wilos"));

		// Test for a non-existing project
		assertFalse(this.ps.projectExist("poulou"));
	}



	@Test
	public void testGetUnfinishedProjects() {
		System.out.println("\n********");
		long startTime = System.currentTimeMillis();

		Set<Project> unfProjects = this.ps.getUnfinishedProjects();

		long endTime = System.currentTimeMillis();
		System.out.println("Time taken: " + (endTime - startTime)*1.0/1000 + " seconds");
		System.out.println("Num rows: " + unfProjects.size());
		System.out.println("********");

		for (Project project : unfProjects) {
			assertFalse(project.getIsFinished());
		}
	}

	@Test
	public void testCreateTask() {
		Project project = new Project();
		project.setConcreteName("ProjetTest");
		this.ps.saveProject(project);

		RoleDescriptor roleDescriptor = new RoleDescriptor();
		roleDescriptor.setGuid("Mon guid");
		this.roleDescriptordao.saveOrUpdateRoleDescriptor(roleDescriptor);

		ConcreteActivity cact = new ConcreteActivity();
		cact.setConcreteName("cact");
		cact.setProject(project);
		this.concreteActivityDao.saveOrUpdateConcreteActivity(cact);

		assertTrue(this.ps.createTask("nom", "descr", project, roleDescriptor,
				cact, false));

		ConcreteActivity cact2 = new ConcreteActivity();
		cact2.setConcreteName("cact");
		cact2.addSuperConcreteActivity(cact);
		cact2.setProject(project);
		this.concreteActivityDao.saveOrUpdateConcreteActivity(cact2);
		assertTrue(this.ps.createTask("nom", "descr", project, roleDescriptor,
				cact, true));

		//clean
		//delete task created
		List<ConcreteTaskDescriptor> lctd = this.concreteTaskDescriptorDao.getAllConcreteTaskDescriptorsForProject(project.getId());
		for(ConcreteTaskDescriptor ctd : lctd)
		{
			this.concreteTaskDescriptorDao.deleteConcreteTaskDescriptor(ctd);
		}
		//delete activity
		this.concreteActivityDao.deleteConcreteActivity(cact2);
		this.concreteActivityDao.deleteConcreteActivity(cact);
		//delete the project
		this.ps.deleteProject(project.getId());
	}
}
