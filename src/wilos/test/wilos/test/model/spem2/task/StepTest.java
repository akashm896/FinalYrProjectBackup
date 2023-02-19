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
package wilos.test.model.spem2.task;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.model.spem2.task.Step;
import wilos.model.spem2.task.TaskDefinition;

public class StepTest {

	private Step step;

	public static final String IDEPF = "idEPF";

	public static final String NAME = "name";

	public static final String DESCRIPTION = "description";

	@Before
	public void setUp() {
		this.step = new Step();
		this.step.setDescription(DESCRIPTION);
		this.step.setName(NAME);
		this.step.setGuid(IDEPF);
	}

	@After
	public void tearDown() {
		// None.
	}

	@Test
	public final void testClone() {
		// Rk: the setUp method is called here.

		try {
			assertEquals(this.step.clone(), this.step);
		} catch (CloneNotSupportedException e) {
			fail("Error CloneNotSupportedException in the testClone method");
		}

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testHashCode() {
		Step stp = new Step();
		stp.setDescription(DESCRIPTION);
		stp.setName(NAME);
		stp.setGuid(IDEPF);

		assertNotNull(this.step.hashCode());
		assertNotNull(stp.hashCode());
		assertEquals(this.step.hashCode(), stp.hashCode());
	}

	@Test
	public void testEquals() {

		// Assert if it's equal by references.
		assertTrue("By references", this.step.equals(this.step));

		// Assert if it's equal field by field.
		Step step = new Step();
		step.setDescription(DESCRIPTION);
		step.setName(NAME);
		step.setGuid(IDEPF);

		assertTrue("Field by field", this.step.equals(step));

		// Assert if it's not equal.
		Step step2 = new Step();
		step2.setDescription("description2");
		step2.setName("name2");
		step2.setGuid("idEPF2");

		assertFalse("Not equals", this.step.equals(step2));
	}

	@Test
	public void testAddToTaskDefinition() {
		TaskDefinition taskDefinition = new TaskDefinition();
		taskDefinition.setDescription(DESCRIPTION);
		taskDefinition.setName(NAME);
		taskDefinition.setGuid(IDEPF);

		this.step.addTaskDefinition(taskDefinition);

		assertNotNull(this.step.getTaskDefinition());
		assertFalse(taskDefinition.getSteps().isEmpty());
	}

	@Test
	public void testRemoveFromTaskDefinition() {
		TaskDefinition taskDefinition = new TaskDefinition();
		taskDefinition.setDescription(DESCRIPTION);
		taskDefinition.setName(NAME);
		taskDefinition.setGuid(IDEPF);

		this.step.addTaskDefinition(taskDefinition);
		this.step.removeTaskDefinition(taskDefinition);

		assertNull(this.step.getTaskDefinition());
		assertTrue(taskDefinition.getSteps().isEmpty());
	}

	@Test
	public void testCompare() {
		TaskDefinition taskDefinition = new TaskDefinition();
		taskDefinition.setDescription(DESCRIPTION);
		taskDefinition.setName(NAME);
		taskDefinition.setGuid(IDEPF);
		this.step.addTaskDefinition(taskDefinition);
		assertEquals(0, this.step.compareTo(this.step));

		Step stepTmp = new Step();
		stepTmp.setName("otherName");
		assertEquals(-1, this.step.compareTo(stepTmp));
	}
}
