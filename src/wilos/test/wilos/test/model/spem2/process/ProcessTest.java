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
package wilos.test.model.spem2.process;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.model.misc.project.Project;
import wilos.model.misc.wilosuser.WilosUser;
import wilos.model.spem2.process.Process;

public class ProcessTest {

	private Process process;

	public static final String PREFIX = "prefix";

	public static final Boolean IS_OPTIONAL = true;

	public static final String NAME = "thisRoleDescriptor";

	public static final String NAME2 = "name1";

	public static final String DESCRIPTION = "roleDescriptor description";

	@Before
	public void setUp() {
		this.process = new Process();
		this.process.setPrefix(PREFIX);
		this.process.setIsOptional(IS_OPTIONAL);
	}

	@After
	public void tearDown() {
		// None.
	}

	@Test
	public final void testHashCode() {
		// Rk: the setUp method is called here.

		Process proc = new Process();
		proc.setPrefix(PREFIX);
		proc.setIsOptional(IS_OPTIONAL);

		assertNotNull(this.process.hashCode());
		assertNotNull(proc.hashCode());
		assertEquals(this.process.hashCode(), proc.hashCode());

		// Rk: the tearDown method is called here.
	}

	@Test
	public final void testEquals() {
		// Rk: the setUp method is called here.

		// Assert if it's equal by references.
		assertTrue("By references", this.process.equals(this.process));

		// Assert if it's equal field by field.
		Process processTmp1 = null;
		try {
			processTmp1 = this.process.clone();
		} catch (CloneNotSupportedException e) {
			fail("Error CloneNotSupportedException in the testEquals method");
		}
		assertTrue("Field by field", this.process.equals(processTmp1));

		// Assert if it's not equal.
		Process procTmp2 = new Process();
		procTmp2.setPrefix("prefixFalse");
		procTmp2.setIsOptional(true);
		assertFalse("Not equals", this.process.equals(procTmp2));

		// Rk: the tearDown method is called here.
	}

	@Test
	public final void testClone() {
		// Rk: the setUp method is called here.

		try {
			assertEquals(this.process.clone(), this.process);
		} catch (CloneNotSupportedException e) {
			fail("Error CloneNotSupportedException in the testClone method");
		}

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testRemoveProjects() {
		Project proj = new Project();
		proj.setConcreteName(NAME);
		proj.setDescription(DESCRIPTION);

		this.process.addProject(proj);
		assertFalse(this.process.getProjects().isEmpty());

		this.process.removeProject(proj);
		assertTrue(this.process.getProjects().isEmpty());

	}

	@Test
	public void testAddProject() {
		Project proj = new Project();
		proj.setConcreteName(NAME);
		proj.setDescription(DESCRIPTION);

		this.process.addProject(proj);
		assertFalse(this.process.getProjects().isEmpty());
		assertTrue(this.process.getProjects().size() == 1);

	}

	@Test
	public void testRemoveAllProject() {
		Project proj = new Project();
		proj.setConcreteName(NAME);
		proj.setDescription(DESCRIPTION);

		Project tmp = new Project();
		tmp.setConcreteName(NAME2);
		// tmp.setDescription(DESCRIPTION);

		Set<Project> set = new HashSet<Project>();
		set.add(proj);
		set.add(tmp);

		this.process.addAllProjects(set);
		assertNotNull(proj.getProcess());
		assertNotNull(tmp.getProcess());
		assertTrue(this.process.getProjects().size() == set.size());

		this.process.removeAllProjects();
		assertNull(proj.getProcess());
		assertNull(tmp.getProcess());
		assertTrue(this.process.getProjects().isEmpty());
	}

	@Test
	public void testAddToAllProjects() {
		Project proj = new Project();
		proj.setConcreteName(NAME);
		proj.setDescription(DESCRIPTION);

		Project tmp = new Project();
		tmp.setConcreteName(NAME2);
		tmp.setDescription(DESCRIPTION);

		Set<Project> set = new HashSet<Project>();
		set.add(proj);
		set.add(tmp);

		this.process.addAllProjects(set);

		assertFalse(this.process.getProjects().isEmpty());
		assertTrue(this.process.getProjects().size() == 2);
		assertNotNull(proj.getProcess());
		assertNotNull(tmp.getProcess());
	}

	@Test
	public void testAddToProcessManager() {
		WilosUser processManager = new WilosUser();
		processManager.setName(NAME);
		processManager.setId("99");
		this.process.addProcessManager(processManager);
		assertNotNull(this.process.getProcessManager());
		assertTrue(processManager.getProcessesManaged().size() == 1);
	}

	@Test
	public void testRemoveFromProcessManager() {
		WilosUser processManager = new WilosUser();
		processManager.setName(NAME);
		processManager.setId("99");

		this.process.addProcessManager(processManager);
		assertNotNull("null", this.process.getProcessManager());
		assertTrue("empty", processManager.getProcessesManaged().size() == 1);

		this.process.removeProcessManager(processManager);
		assertNull("null", this.process.getProcessManager());
		assertTrue("empty", processManager.getProcessesManaged().isEmpty());
	}

}
