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

package wilos.test.business.services.spem2.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.SortedSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.misc.project.ProjectService;
import wilos.business.services.misc.wilosuser.ProcessManagerService;
import wilos.business.services.spem2.process.ProcessManagementService;
import wilos.business.services.spem2.process.ProcessService;
import wilos.model.misc.project.Project;
import wilos.model.misc.wilosuser.WilosUser;
import wilos.model.spem2.breakdownelement.BreakdownElement;
import wilos.model.spem2.process.Process;
import wilos.test.TestConfiguration;

public class ProcessManagementServiceTest {

	public static File pathScrum2008 = new File("processes" + File.separator
			+ "scrum2008.xml");
	
	private ProjectService projectS;
	private ProcessManagementService pms;
	private ProcessService processService;
	private Process process;
	private Project project;
	private WilosUser processManager;
	private ProcessManagerService processManagerService;

	public ProcessManagementServiceTest() {
		this.projectS = (ProjectService) TestConfiguration.getInstance()
				.getApplicationContext().getBean("ProjectService");
		this.pms = (ProcessManagementService) TestConfiguration.getInstance()
				.getApplicationContext().getBean("ProcessManagementService");
		this.processService = (ProcessService) TestConfiguration
			.getInstance().getApplicationContext().getBean("ProcessService");
		this.processManagerService = (ProcessManagerService) TestConfiguration
			.getInstance().getApplicationContext().getBean("ProcessManagerService");
	}

	@Before
	public void setUp() {
		this.process = new Process();
		this.process.setName("TestProcess");
		this.process.setDescription("description test");
	}

	@After
	public void tearDown() {
		
	}

	@Test
	public void testHasBeenInstanciated() {
		this.project = new Project();
		this.projectS.getProjectDao().saveOrUpdateProject(this.project);
		this.process.addProject(this.project);
		this.pms.getProcessDao().saveOrUpdateProcess(this.process);
		assertTrue(this.pms.hasBeenInstanciated(this.process.getId()));
		this.projectS.getProjectDao().deleteProject(this.project);
		this.pms.getProcessDao().deleteProcess(this.process);
	}

	@Test
	public void testRemoveProcess()
	{
		//parse process
		this.process = processService.importProcessFromEpfComposer(pathScrum2008);
		//save process
		//create a process manager to make test
		this.processManager = new WilosUser();
		this.processManager.setLogin("My login 32");
		this.processManager.setName("My name");
		this.processManager.setPassword("My password");
		this.processManagerService.saveProcessManager(this.processManager);
		String id = this.processService.saveProcess(this.process, this.processManager.getId());
		//new processes list
		SortedSet<BreakdownElement> lst_after_save = this.processService.getAllBreakdownElements(this.process);
		List<Process> lst_after_save_process = this.processService.getAllProcesses();
		assertNotNull(lst_after_save);
		assertNotNull(lst_after_save_process);
		this.pms.removeProcess(this.process.getId());
		List<Process> lst_after_delete = this.processService.getAllProcesses();
		assertEquals(lst_after_delete.size(), lst_after_save_process.size()-1);
		this.processManagerService.deleteProcessManager(processManager);
	}
}
