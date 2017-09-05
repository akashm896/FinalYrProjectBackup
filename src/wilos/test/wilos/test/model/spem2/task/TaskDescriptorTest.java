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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.model.spem2.role.RoleDescriptor;
import wilos.model.spem2.task.TaskDefinition;
import wilos.model.spem2.task.TaskDescriptor;

public class TaskDescriptorTest {

	private TaskDescriptor taskDescriptor;

	/**
	 * attributes from Element
	 */
	public static final String NAME = "thisTaskDescriptor";

	public static final String DESCRIPTION = "taskDescriptor description";

	/**
	 * attributes from BreakdownElement
	 */
	public static final String PREFIX = "prefix";

	public static final Boolean IS_PLANNED = true;

	public static final Boolean HAS_MULTIPLE_OCCURENCES = true;

	public static final Boolean IS_OPTIONAL = true;

	/**
	 * attributes from WorkBreakdownElement
	 */
	public static final Boolean IS_REPEATABLE = true;

	public static final Boolean IS_ON_GOING = true;

	public static final Boolean IS_EVEN_DRIVEN = true;

	@Before
	public void setUp() {
		this.taskDescriptor = new TaskDescriptor();
		this.taskDescriptor.setDescription(NAME);
		this.taskDescriptor.setDescription(DESCRIPTION);
		this.taskDescriptor.setPrefix(PREFIX);
		this.taskDescriptor.setIsPlanned(IS_PLANNED);
		this.taskDescriptor.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
		this.taskDescriptor.setIsOptional(IS_OPTIONAL);
		this.taskDescriptor.setIsRepeatable(IS_REPEATABLE);
		this.taskDescriptor.setIsOngoing(IS_ON_GOING);
		this.taskDescriptor.setIsEvenDriven(IS_EVEN_DRIVEN);
	}

	@After
	public void tearDown() {
		// None.
	}

	@Test
	public void testHashCode() {
		assertNotNull(this.taskDescriptor.hashCode());
	}

	@Test
	public void testEquals() {

		// Assert if it's equal by references.
		assertTrue("By references", this.taskDescriptor
				.equals(this.taskDescriptor));

		// Assert if it's equal field by field.
		TaskDescriptor td1 = null;
		try {
			td1 = this.taskDescriptor.clone();
		} catch (CloneNotSupportedException e) {
			fail("Error with CloneNotSupportedException in the testEquals method");
		}
		assertTrue("Field by field", this.taskDescriptor.equals(td1));

		// Assert if it's not equal.
		TaskDescriptor td2 = new TaskDescriptor();
		td2.setDescription(DESCRIPTION);
		td2.setIsRepeatable(false);
		td2.setPrefix("prefixFalse");
		assertFalse("Not equals", this.taskDescriptor.equals(td2));
	}

	@Test
	public void testAddToTaskDefinition() {
		TaskDefinition taskDefinition = new TaskDefinition();
		taskDefinition.setDescription(DESCRIPTION);
		taskDefinition.setName(NAME);

		this.taskDescriptor.addTaskDefinition(taskDefinition);

		assertNotNull(this.taskDescriptor.getTaskDefinition());
		assertTrue(taskDefinition.getTaskDescriptors().size() == 1);
	}

	@Test
	public void testRemoveFromTaskDefinition() {
		TaskDefinition taskDefinition = new TaskDefinition();
		taskDefinition.setDescription(DESCRIPTION);
		taskDefinition.setName(NAME);

		this.taskDescriptor.addTaskDefinition(taskDefinition);
		assertNotNull("null", this.taskDescriptor.getTaskDefinition());
		assertTrue("empty", taskDefinition.getTaskDescriptors().size() == 1);

		this.taskDescriptor.removeTaskDefinition(taskDefinition);
		assertNull("null", this.taskDescriptor.getTaskDefinition());
		assertTrue("empty", taskDefinition.getTaskDescriptors().isEmpty());
	}

	@Test
	public void testAddToMainRole() {
		RoleDescriptor roleDescriptor = new RoleDescriptor();
		roleDescriptor.setName(NAME);
		roleDescriptor.setDescription(DESCRIPTION);
		roleDescriptor.setPrefix(PREFIX);
		roleDescriptor.setIsPlanned(IS_PLANNED);
		roleDescriptor.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
		roleDescriptor.setIsOptional(IS_OPTIONAL);

		this.taskDescriptor.addMainRole(roleDescriptor);

		assertNotNull(this.taskDescriptor.getMainRole());
		assertTrue(roleDescriptor.getPrimaryTasks().size() == 1);
	}

	@Test
	public void testRemoveMainRole() {
		RoleDescriptor roleDescriptor = new RoleDescriptor();
		roleDescriptor.setName(NAME);
		roleDescriptor.setDescription(DESCRIPTION);
		roleDescriptor.setPrefix(PREFIX);
		roleDescriptor.setIsPlanned(IS_PLANNED);
		roleDescriptor.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
		roleDescriptor.setIsOptional(IS_OPTIONAL);

		this.taskDescriptor.addMainRole(roleDescriptor);
		assertNotNull(this.taskDescriptor.getMainRole());
		assertTrue(roleDescriptor.getPrimaryTasks().size() == 1);

		this.taskDescriptor.removeMainRole(roleDescriptor);
		assertNull(this.taskDescriptor.getMainRole());
		assertTrue(roleDescriptor.getPrimaryTasks().isEmpty());
	}

	@Test
	public void testAddToRoleDescriptor() {
		RoleDescriptor roleDescriptor = new RoleDescriptor();
		roleDescriptor.setName(NAME);
		roleDescriptor.setDescription(DESCRIPTION);
		roleDescriptor.setPrefix(PREFIX);
		roleDescriptor.setIsPlanned(IS_PLANNED);
		roleDescriptor.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
		roleDescriptor.setIsOptional(IS_OPTIONAL);

		this.taskDescriptor.addAdditionalRole(roleDescriptor);

		assertTrue(this.taskDescriptor.getAdditionalRoles().size() == 1);
		assertTrue(roleDescriptor.getAdditionalTasks().size() == 1);
	}

	@Test
	public void testRemoveFromRoleDescriptor() {
		RoleDescriptor roleDescriptor = new RoleDescriptor();
		roleDescriptor.setName(NAME);
		roleDescriptor.setDescription(DESCRIPTION);
		roleDescriptor.setPrefix(PREFIX);
		roleDescriptor.setIsPlanned(IS_PLANNED);
		roleDescriptor.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
		roleDescriptor.setIsOptional(IS_OPTIONAL);

		this.taskDescriptor.addAdditionalRole(roleDescriptor);
		assertTrue(this.taskDescriptor.getAdditionalRoles().size() == 1);
		assertTrue(roleDescriptor.getAdditionalTasks().size() == 1);

		this.taskDescriptor.removeAdditionalRole(roleDescriptor);
		assertTrue(this.taskDescriptor.getAdditionalRoles().isEmpty());
		assertTrue(roleDescriptor.getAdditionalTasks().isEmpty());
	}

	@Test
	public void testRemoveFromAllRoleDescriptors() {
		RoleDescriptor rd1 = new RoleDescriptor();
		rd1.setName("otherName");
		rd1.setDescription("otherDescription");
		rd1.setPrefix(PREFIX);
		rd1.setIsPlanned(IS_PLANNED);
		rd1.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
		rd1.setIsOptional(IS_OPTIONAL);

		RoleDescriptor rd2 = new RoleDescriptor();
		rd2.setName(NAME);
		rd2.setDescription(DESCRIPTION);
		rd2.setPrefix(PREFIX);
		rd2.setIsPlanned(IS_PLANNED);
		rd2.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
		rd2.setIsOptional(IS_OPTIONAL);

		Set<RoleDescriptor> roledescriptors = new HashSet<RoleDescriptor>();
		roledescriptors.add(rd1);
		roledescriptors.add(rd2);

		this.taskDescriptor.addAllAdditionalRoles(roledescriptors);
		assertTrue(rd1.getAdditionalTasks().size() == 1);
		assertTrue(rd2.getAdditionalTasks().size() == 1);
		assertTrue(this.taskDescriptor.getAdditionalRoles().size() == 2);

		this.taskDescriptor.removeAllAdditionalRoles();
		assertTrue(rd1.getAdditionalTasks().isEmpty());
		assertTrue(rd2.getAdditionalTasks().isEmpty());
		assertTrue(this.taskDescriptor.getAdditionalRoles().isEmpty());
	}
}
