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

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.model.spem2.guide.Guidance;
import wilos.model.spem2.task.Step;
import wilos.model.spem2.task.TaskDefinition;
import wilos.model.spem2.task.TaskDescriptor;

public class TaskDefinitionTest {

	private TaskDefinition taskDefinition;

	public static final String NAME = "name";

	public static final String DESCRIPTION = "description";

	@Before
	public void setUp() {
		this.taskDefinition = new TaskDefinition();
		this.taskDefinition.setDescription(DESCRIPTION);
		this.taskDefinition.setName(NAME);
	}

	@After
	public void tearDown() {
		// None.
	}

	@Test
	public void testHashCode() {
		assertNotNull(this.taskDefinition.hashCode());
	}

	@Test
	public void testEquals() {

		// Assert if it's equal by references.
		assertTrue("By references", this.taskDefinition
				.equals(this.taskDefinition));

		// Assert if it's equal field by field.
		TaskDefinition td1 = null;
		try {
			td1 = this.taskDefinition.clone();
		} catch (CloneNotSupportedException e) {
			fail("Error CloneNotSupportedException in the testEquals method");
		}
		assertTrue("Field by field", this.taskDefinition.equals(td1));

		// Assert if it's not equal.
		TaskDescriptor td2 = new TaskDescriptor();
		td2.setIsRepeatable(false);
		td2.setPrefix("prefixFalse");
		assertFalse("Not equals", this.taskDefinition.equals(td2));
	}

	@Test
	public void testAddStep() {
		Step step = new Step();
		step.setDescription(DESCRIPTION);
		step.setName(NAME);

		this.taskDefinition.addStep(step);

		assertTrue(this.taskDefinition.getSteps().size() == 1);
		assertNotNull(step.getTaskDefinition());
	}

	@Test
	public void testAddTaskDescriptor() {
		TaskDescriptor taskDescriptor = new TaskDescriptor();
		taskDescriptor.setDescription(DESCRIPTION);
		taskDescriptor.setName(NAME);

		this.taskDefinition.addTaskDescriptor(taskDescriptor);

		assertTrue(this.taskDefinition.getTaskDescriptors().size() == 1);
		assertNotNull(taskDescriptor.getTaskDefinition());
	}

	@Test
	public void testAddToAllSteps() {
		Step step1 = new Step();
		step1.setDescription("description1");
		step1.setName("name1");

		Step step2 = new Step();
		step2.setDescription("description2");
		step2.setName("name2");

		SortedSet<Step> set = new TreeSet<Step>();
		set.add(step1);
		set.add(step2);

		this.taskDefinition.addAllSteps(set);

		assertFalse(this.taskDefinition.getSteps().isEmpty());
		assertEquals(2, this.taskDefinition.getSteps().size());
		assertNotNull(step1.getTaskDefinition());
		assertNotNull(step2.getTaskDefinition());

		// check that the set is almost sorted.
		assertEquals(this.taskDefinition.getSteps().first(), step1);
		assertEquals(this.taskDefinition.getSteps().last(), step2);
	}

	@Test
	public void testAddToAllTaskDesciptors() {
		TaskDescriptor td1 = new TaskDescriptor();
		td1.setDescription("description1");
		td1.setName("name1");

		TaskDescriptor td2 = new TaskDescriptor();
		td2.setDescription("description2");
		td2.setName("name2");

		Set<TaskDescriptor> set = new HashSet<TaskDescriptor>();
		set.add(td1);
		set.add(td2);

		this.taskDefinition.addAllTaskDesciptors(set);

		assertFalse(this.taskDefinition.getTaskDescriptors().isEmpty());
		assertEquals(2, this.taskDefinition.getTaskDescriptors().size());
		assertNotNull(td1.getTaskDefinition());
		assertNotNull(td2.getTaskDefinition());
	}

	@Test
	public void testRemoveStep() {
		Step step = new Step();
		step.setDescription(DESCRIPTION);
		step.setName(NAME);

		this.taskDefinition.removeStep(step);

		assertTrue(this.taskDefinition.getSteps().isEmpty());
		assertNull(step.getTaskDefinition());
	}

	@Test
	public void testRemoveTaskDescriptor() {
		TaskDescriptor taskDescriptor = new TaskDescriptor();
		taskDescriptor.setDescription(DESCRIPTION);
		taskDescriptor.setName(NAME);

		this.taskDefinition.removeTaskDescriptor(taskDescriptor);

		assertTrue(this.taskDefinition.getTaskDescriptors().isEmpty());
		assertNull(taskDescriptor.getTaskDefinition());
	}

	@Test
	public void testRemoveAllSteps() {
		Step step1 = new Step();
		step1.setDescription(DESCRIPTION);
		step1.setName("otherName");

		Step step2 = new Step();
		step2.setDescription(DESCRIPTION);
		step2.setName(NAME);

		SortedSet<Step> set = new TreeSet<Step>();
		set.add(step1);
		set.add(step2);

		this.taskDefinition.addAllSteps(set);
		assertTrue(this.taskDefinition.getSteps().size() == 2);
		assertNotNull(step1.getTaskDefinition());
		assertNotNull(step2.getTaskDefinition());

		this.taskDefinition.removeAllSteps();
		assertTrue(this.taskDefinition.getSteps().isEmpty());
		assertNull(step1.getTaskDefinition());
		assertNull(step2.getTaskDefinition());
	}

	@Test
	public void testRemoveAllTaskDescriptors() {
		TaskDescriptor td1 = new TaskDescriptor();
		td1.setDescription(DESCRIPTION);
		td1.setName(NAME);

		TaskDescriptor td2 = new TaskDescriptor();
		td2.setDescription(DESCRIPTION);
		td2.setName("otherName");

		Set<TaskDescriptor> set = new HashSet<TaskDescriptor>();
		set.add(td1);
		set.add(td2);

		this.taskDefinition.addAllTaskDesciptors(set);
		assertTrue(this.taskDefinition.getTaskDescriptors().size() == 2);
		assertNotNull(td1.getTaskDefinition());
		assertNotNull(td2.getTaskDefinition());

		this.taskDefinition.removeAllTaskDescriptors();
		assertTrue(this.taskDefinition.getTaskDescriptors().isEmpty());
		assertNull(td1.getTaskDefinition());
		assertNull(td2.getTaskDefinition());
	}

	@Test
	public void testAddGuidance() {
		Guidance guidance = new Guidance();
		guidance.setName("name");

		this.taskDefinition.addGuidance(guidance);

		assertTrue(this.taskDefinition.getGuidances().size() == 1);
		assertTrue(guidance.getTaskDefinitions().contains(this.taskDefinition));
	}

	@Test
	public void testaddAllGuidances() {
		Guidance g1 = new Guidance();
		g1.setName("name1");

		Guidance g2 = new Guidance();
		g2.setName("name2");

		Set<Guidance> set = new HashSet<Guidance>();
		set.add(g1);
		set.add(g2);

		this.taskDefinition.addAllGuidances(set);

		assertFalse(this.taskDefinition.getGuidances().isEmpty());
		assertEquals(2, this.taskDefinition.getGuidances().size());
		assertTrue(g1.getTaskDefinitions().contains(this.taskDefinition));
		assertTrue(g2.getTaskDefinitions().contains(this.taskDefinition));
	}

	@Test
	public void testRemoveGuidance() {
		Guidance guidance = new Guidance();
		guidance.setName("name");

		this.taskDefinition.removeGuidance(guidance);

		assertTrue(this.taskDefinition.getGuidances().isEmpty());
		assertFalse(guidance.getTaskDefinitions().contains(this.taskDefinition));
	}

	@Test
	public void testRemoveAllGuidances() {
		Guidance g1 = new Guidance();
		g1.setName("name1");

		Guidance g2 = new Guidance();
		g2.setName("name2");

		Set<Guidance> set = new HashSet<Guidance>();
		set.add(g1);
		set.add(g2);

		this.taskDefinition.addAllGuidances(set);
		assertTrue(this.taskDefinition.getGuidances().size() == 2);
		assertTrue(g1.getTaskDefinitions().contains(this.taskDefinition));
		assertTrue(g2.getTaskDefinitions().contains(this.taskDefinition));

		this.taskDefinition.removeAllGuidances();
		assertTrue(this.taskDefinition.getGuidances().isEmpty());
		assertFalse(g1.getTaskDefinitions().contains(this.taskDefinition));
		assertFalse(g2.getTaskDefinitions().contains(this.taskDefinition));
	}
}
