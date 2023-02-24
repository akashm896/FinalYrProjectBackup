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
package wilos.test.business.services.spem2.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.misc.wilosuser.ProcessManagerService;
import wilos.business.services.spem2.process.ProcessManagementService;
import wilos.business.services.spem2.process.ProcessService;
import wilos.model.misc.wilosuser.WilosUser;
import wilos.model.spem2.process.Process;
import wilos.test.TestConfiguration;

public class ProcessServiceTest {

	public static File pathOPenUP_1_0 = new File("processes" + File.separator
			+ "openup1.0.xml");
	public static File pathScrum2008 = new File("processes" + File.separator
			+ "scrum2008.xml");
	
	private ProcessService processService = (ProcessService) TestConfiguration
			.getInstance().getApplicationContext().getBean("ProcessService");
	private ProcessManagerService processManagerService = (ProcessManagerService) TestConfiguration
			.getInstance().getApplicationContext().getBean("ProcessManagerService");
	private ProcessManagementService pms = (ProcessManagementService) TestConfiguration.getInstance()
			.getApplicationContext().getBean("ProcessManagementService");
	
	private Process process;
	private WilosUser processManager;
	private String processId;

	@Before
	public void setUp() {
		//create a process manager to make test
		this.processManager = new WilosUser();
		this.processManager.setLogin("My login 24");
		this.processManager.setName("My name");
		this.processManager.setPassword("My password");
		this.processManagerService.saveProcessManager(this.processManager);
		//make process
		this.process = new Process();
		this.process.setGuid("My guid");
		this.process.addProcessManager(this.processManager);
	}

	@After
	public void tearDown() {
		this.processManagerService.deleteProcessManager(this.processManager);
		this.processManager = null;
		this.process = null;
	}

	@Test
	public void testStorageOfAnProcessInATransaction() {

		this.processId = this.processService.saveProcess(this.process);
		this.processManagerService.saveProcessManager(this.processManager);

		Process retrievedProcess = this.processService.getProcessDao()
				.getProcess(this.processId);
		assertNotNull("not null", retrievedProcess);

		this.process.removeProcessManager(this.processManager);
		// this.processManager.getProcessesManaged().clear();
		this.processManagerService.saveProcessManager(this.processManager);

		this.processService.deleteProcess(this.process);
	}

	@Test
	public void testThatAllProcessesAreRetrieved() {

		int nb = this.processService.getAllProcesses().size();

		this.processId = this.processService.saveProcess(this.process);

		Process p1 = new Process();
		p1.setName("Your name");
		p1.setDescription("Your description");

		this.processService.saveProcess(p1);

		Process p2 = new Process();
		p2.setName("Its name");
		p2.setDescription("Its description");

		this.processService.saveProcess(p2);

		List<Process> processes = this.processService.getAllProcesses();
		assertNotNull("not null", processes);
		assertEquals("number of processes", 3, processes.size() - nb);

		this.processService.deleteProcess(p2);
		this.processService.deleteProcess(p1);
		this.processService.deleteProcess(this.process);
		p2 = null;
		p1 = null;
	}

	@Test
	public final void testSaveAndDeletesProcess_Scrum2008() {
		//processes list
		List<Process> lst = this.processService.getAllProcesses();
		//parse process
		Process p = processService.importProcessFromEpfComposer(pathScrum2008);
		//save process
		String id = this.processService.saveProcess(p,this.processManager.getId());
		Process p2 = this.processService.getProcess(id);
		assertNotNull (p2);
		assertEquals(p.getId(), p2.getId());
		//new processes list
		List<Process> lst_after_save = this.processService.getAllProcesses();
		assertNotNull(lst_after_save);
		assertEquals(lst.size(), lst_after_save.size()-1);
		//delete process
		
		/*
		 * this.processService.deleteProcess(p);
		 * if this method is only used, the process is out of the database
		 *  but all breakdown elements are always on the database
		 *  ProcessManagementService.removeProcess clear all breakdown elements 
		 *  and call processService.deleteProcess
		 */
		
		this.pms.removeProcess(p.getId());

		//new processes list
		List<Process> lst_after_delete = this.processService.getAllProcesses();
		assertEquals(lst.size(), lst_after_delete.size());
	}
}
