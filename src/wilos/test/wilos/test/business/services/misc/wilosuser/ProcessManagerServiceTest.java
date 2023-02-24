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

package wilos.test.business.services.misc.wilosuser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.misc.wilosuser.ProcessManagerService;
import wilos.model.misc.wilosuser.WilosUser;
import wilos.test.TestConfiguration;
import wilos.utils.Security;

public class ProcessManagerServiceTest {

    private ProcessManagerService processManagerService;

    private WilosUser processManager1;

    private WilosUser processManager2;

    public final Log logger = LogFactory.getLog(this.getClass());

    public ProcessManagerServiceTest() {
	this.processManagerService = (ProcessManagerService) TestConfiguration.getInstance().getApplicationContext()
		.getBean("ProcessManagerService");
    }

    @Before
    public void setUp() {
	this.processManager1 = new WilosUser();
	this.processManager1.setLogin("john");
	this.processManager1.setName("georges");
	this.processManager1.setFirstname("georges");
	this.processManager1.setPassword("pass");
	// this.processManager1.setProcessesManaged(null);

	this.processManager2 = new WilosUser();
	this.processManager2.setName("jose");
	this.processManager2.setFirstname("jose");
	this.processManager2.setLogin("ter");
	this.processManager2.setPassword("bouh");
	// this.processManager2.setProcessesManaged(null);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSaveProcessManager() {
	
	this.processManagerService.saveProcessManager(processManager1);
	this.processManagerService.saveProcessManager(processManager2);
	
	WilosUser ProcessManagerTmp = (WilosUser) this.processManagerService.getWilosUserDao().getWilosUser("john");
	assertNotNull(ProcessManagerTmp);
	assertEquals(ProcessManagerTmp.getName(), "georges");
	assertEquals(ProcessManagerTmp.getLogin(), "john");
	assertEquals(ProcessManagerTmp.getPassword(), Security.encode("pass"));
	
	this.processManagerService.deleteProcessManager(this.processManager1);
	this.processManagerService.deleteProcessManager(this.processManager2);
    }
}
