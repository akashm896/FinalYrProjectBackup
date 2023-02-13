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
package wilos.test.hibernate.spem2.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.spem2.process.ProcessDao;
import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.process.Process;
import wilos.test.TestConfiguration;

/**
 * @author deder
 * 
 */
public class ProcessDaoTest {

	private ProcessDao processDao = null;
	private Process process = null;
	public static final String NAME = "thisProcess";
	public static final String DESCRIPTION = "process description";
	public static final String PREFIX = "prefix";
	public static final Boolean IS_OPTIONAL = true;
	public static final Boolean HAS_MULTIPLE_OCCURENCES = true;
	public static final Boolean IS_EVEN_DRIVEN = true;
	public static final Boolean IS_ON_GOING = true;
	public static final Boolean IS_PLANNED = true;
	public static final Boolean IS_REPEATABLE = true;

	@Before
	public void setUp() {

		// Get the ActivityDao Singleton for managing Activity data
		this.processDao = (ProcessDao) TestConfiguration.getInstance()
				.getApplicationContext().getBean("ProcessDao");

		// Create empty Activity
		this.process = new Process();
		this.process.setName(NAME);
		this.process.setDescription(DESCRIPTION);
		this.process.setPrefix(PREFIX);
		this.process.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
		this.process.setIsEvenDriven(IS_EVEN_DRIVEN);
		this.process.setIsOngoing(IS_ON_GOING);
		this.process.setIsOptional(IS_OPTIONAL);
		this.process.setIsPlanned(IS_PLANNED);
		this.process.setIsRepeatable(IS_REPEATABLE);
	}

	@After
	public void tearDown() {
		this.process = null;
	}

	@Test
	public void testSaveOrUpdateProcess() {
		// Rk: the setUp method is called here.

		// Save the activity with the method to test.
		this.processDao.saveOrUpdateProcess(this.process);

		// Check the saving.
		String id = this.process.getId();
		Activity activityTmp = (Activity) this.processDao.getProcess(id);
		assertNotNull(activityTmp);
		
		this.processDao.deleteProcess(this.process);

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testGetAllProcesses() {
		// Rk: the setUp method is called here.
		this.processDao.saveOrUpdateProcess(this.process);
		// Look if this activity is also into the database and look if the size
		// of the set is >= 1.
		List<Process> processes = this.processDao.getAllProcesses();
		assertNotNull(processes);
		assertTrue(processes.size() >= 1);
		this.processDao.deleteProcess(this.process);
		// Rk: the tearDown method is called here.
	}

	@Test
	public void testGetProcess() {
		// Rk: the setUp method is called here.

		// Save the activity into the database.
		this.processDao.saveOrUpdateProcess(this.process);
		String id = this.process.getId();
		// Test the method getProcess with this id.
		Process processTmp = this.processDao.getProcess(id);
		assertNotNull(processTmp);
		assertEquals("Name", processTmp.getName(), NAME);
		assertEquals("Description", processTmp.getDescription(), DESCRIPTION);
		assertEquals("Prefix", processTmp.getPrefix(), PREFIX);
		assertEquals("HasMultipleOccurences", processTmp
				.getHasMultipleOccurrences(), HAS_MULTIPLE_OCCURENCES);
		assertEquals("IsEvenDriven", processTmp.getIsEvenDriven(),
				IS_EVEN_DRIVEN);
		assertEquals("IsOnGoing", processTmp.getIsOngoing(), IS_ON_GOING);
		assertEquals("IsOptional", processTmp.getIsOptional(), IS_OPTIONAL);
		assertEquals("IsPlanned", processTmp.getIsPlanned(), IS_PLANNED);
		assertEquals("IsRepeatale", processTmp.getIsRepeatable(), IS_REPEATABLE);
		
		this.processDao.deleteProcess(this.process);

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testGetProcessFromGuid()
	{
		// Save the activity into the database.
		this.process.setGuid("GuidTest");
		this.processDao.saveOrUpdateProcess(this.process);
		// Test the method getProcess with this guid
		String guid = this.process.getGuid();
		assertTrue(guid.equals("GuidTest"));
		Process processTmpGuid = this.processDao.getProcessFromGuid(guid);
		assertNotNull(processTmpGuid);
		assertEquals(processTmpGuid.getGuid(), guid);
		assertEquals("Name", processTmpGuid.getName(), NAME);
		assertEquals("Description", processTmpGuid.getDescription(), DESCRIPTION);
		assertEquals("Prefix", processTmpGuid.getPrefix(), PREFIX);
		assertEquals("HasMultipleOccurences", processTmpGuid
				.getHasMultipleOccurrences(), HAS_MULTIPLE_OCCURENCES);
		assertEquals("IsEvenDriven", processTmpGuid.getIsEvenDriven(),
				IS_EVEN_DRIVEN);
		assertEquals("IsOnGoing", processTmpGuid.getIsOngoing(), IS_ON_GOING);
		assertEquals("IsOptional", processTmpGuid.getIsOptional(), IS_OPTIONAL);
		assertEquals("IsPlanned", processTmpGuid.getIsPlanned(), IS_PLANNED);
		assertEquals("IsRepeatale", processTmpGuid.getIsRepeatable(), IS_REPEATABLE);
		this.processDao.deleteProcess(this.process);
	}
	
	@Test
	public void testDeleteProcess() {
		// Rk: the setUp method is called here.

		// Save the activity into the database.
		this.processDao.saveOrUpdateProcess(this.process);
		String id = this.process.getId();

		// Test the method deleteActivity with an activity existing into the db.
		this.processDao.deleteProcess(this.process);

		// See if this.activity is now absent in the db.
		Activity activityTmp = (Activity) this.processDao.getProcess(id);
		assertNull(activityTmp);

		// Rk: the tearDown method is called here.
	}

}
