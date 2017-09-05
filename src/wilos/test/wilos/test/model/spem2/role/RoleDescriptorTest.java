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
package wilos.test.model.spem2.role;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.model.spem2.role.RoleDefinition;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.model.spem2.task.TaskDescriptor;

public class RoleDescriptorTest {

	private RoleDescriptor roleDescriptor = null;

	public static final String NAME = "thisRoleDescriptor";

	public static final String DESCRIPTION = "roleDescriptor description";

	public static final String PREFIX = "prefix";

	public static final Boolean IS_OPTIONAL = true;

	public static final Boolean HAS_MULTIPLE_OCCURENCES = true;

	public static final Boolean IS_PLANNED = true;

	@Before
	public void setUp() {
		this.roleDescriptor = new RoleDescriptor();
		this.roleDescriptor.setDescription(DESCRIPTION);
		this.roleDescriptor.setName(NAME);
		this.roleDescriptor.setPrefix(PREFIX);
		this.roleDescriptor.setIsOptional(IS_OPTIONAL);
		this.roleDescriptor.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
		this.roleDescriptor.setIsPlanned(IS_PLANNED);
	}

	@After
	public void tearDown() {
		// None.
	}

	@Test
	public void testHashCode() {

		RoleDescriptor rd = new RoleDescriptor();
		rd.setDescription(DESCRIPTION);
		rd.setName(NAME);
		rd.setPrefix(PREFIX);
		rd.setIsOptional(IS_OPTIONAL);
		rd.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
		rd.setIsPlanned(IS_PLANNED);

		assertNotNull(this.roleDescriptor.hashCode());
		assertNotNull(rd.hashCode());
		assertEquals(this.roleDescriptor.hashCode(), rd.hashCode());
	}

	@Test
	public final void testClone() {
		// Rk: the setUp method is called here.

		try {
			assertEquals(this.roleDescriptor.clone(), this.roleDescriptor);
		} catch (CloneNotSupportedException e) {
			fail("Error CloneNotSupportedException in the testClone method");
		}

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testEquals() {
		RoleDescriptor role = new RoleDescriptor();
		role.setDescription(DESCRIPTION);
		role.setName(NAME);
		role.setPrefix(PREFIX);
		role.setIsOptional(IS_OPTIONAL);
		role.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
		role.setIsPlanned(IS_PLANNED);

		assertTrue(this.roleDescriptor.equals(role));
	}

	@Test
	public void testAddRoleDefinition() {
		RoleDefinition role = new RoleDefinition();
		role.setName(NAME);
		role.setDescription(DESCRIPTION);

		this.roleDescriptor.addRoleDefinition(role);
		assertNotNull(this.roleDescriptor.getRoleDefinition());
		assertNotNull(role.getRoleDescriptors());
		assertTrue(role.getRoleDescriptors().size() == 1);
	}

	@Test
	public void testRemoveRoleDefinition() {
		RoleDefinition role = new RoleDefinition();
		role.setName(NAME);
		role.setDescription(DESCRIPTION);

		this.roleDescriptor.addRoleDefinition(role);
		assertNotNull(this.roleDescriptor.getRoleDefinition());
		// String id = this.ro
		System.out.println("id rdef : "
				+ this.roleDescriptor.getRoleDefinition().getId());
		// assertEquals()
		assertTrue(role.getRoleDescriptors().size() == 1);

		this.roleDescriptor.removeRoleDefinition(role);
		assertNull(this.roleDescriptor.getRoleDefinition());
		assertTrue(role.getRoleDescriptors().isEmpty());
	}

	@Test
	public void testAddPrimaryTask() {
		TaskDescriptor task = new TaskDescriptor();
		task.setName(NAME);
		task.setDescription(DESCRIPTION);

		this.roleDescriptor.addPrimaryTask(task);
		assertFalse(this.roleDescriptor.getPrimaryTasks().isEmpty());
		assertTrue(this.roleDescriptor.getPrimaryTasks().size() == 1);
		assertNotNull(task.getMainRole());
	}

	@Test
	public void testRemovePrimaryTask() {
		TaskDescriptor task = new TaskDescriptor();
		task.setName(NAME);
		task.setDescription(DESCRIPTION);

		this.roleDescriptor.addPrimaryTask(task);
		assertFalse(this.roleDescriptor.getPrimaryTasks().isEmpty());
		assertNotNull(task.getMainRole());

		this.roleDescriptor.removePrimaryTask(task);
		assertTrue(this.roleDescriptor.getPrimaryTasks().isEmpty());
		assertNull(task.getMainRole());
	}

	@Test
	public void testRemoveAllPrimaryTasks() {
		TaskDescriptor task = new TaskDescriptor();
		task.setName(NAME);
		task.setDescription(DESCRIPTION);

		TaskDescriptor tmp = new TaskDescriptor();
		tmp.setName("nom");
		tmp.setDescription(DESCRIPTION);

		Set<TaskDescriptor> set = new HashSet<TaskDescriptor>();
		set.add(task);
		set.add(tmp);

		this.roleDescriptor.addAllPrimaryTasks(set);
		assertTrue(this.roleDescriptor.getPrimaryTasks().size() == 2);
		assertNotNull(task.getMainRole());
		assertNotNull(tmp.getMainRole());

		this.roleDescriptor.removeAllPrimaryTasks();
		assertTrue(this.roleDescriptor.getPrimaryTasks().isEmpty());
		assertNull(task.getMainRole());
		assertNull(tmp.getMainRole());
	}

	@Test
	public void testAddAllPrimaryTasks() {
		TaskDescriptor task = new TaskDescriptor();
		task.setName(NAME);
		task.setDescription(DESCRIPTION);

		TaskDescriptor tmp = new TaskDescriptor();
		tmp.setName("nom");
		tmp.setDescription(DESCRIPTION);

		Set<TaskDescriptor> set = new HashSet<TaskDescriptor>();
		set.add(task);
		set.add(tmp);

		this.roleDescriptor.addAllPrimaryTasks(set);

		assertFalse(this.roleDescriptor.getPrimaryTasks().isEmpty());
		assertTrue(this.roleDescriptor.getPrimaryTasks().size() == 2);
	}

	@Test
	public void testAddAdditionnalTask() {
		TaskDescriptor task = new TaskDescriptor();
		task.setName(NAME);
		task.setDescription(DESCRIPTION);

		this.roleDescriptor.addAdditionalTask(task);

		assertFalse(this.roleDescriptor.getAdditionalTasks().isEmpty());
		assertFalse(task.getAdditionalRoles().isEmpty());
		assertTrue(this.roleDescriptor.getAdditionalTasks().size() == 1);
		assertTrue(task.getAdditionalRoles().size() == 1);
	}

	@Test
	public void testRemoveAdditionalTask() {
		TaskDescriptor task = new TaskDescriptor();
		task.setName(NAME);
		task.setDescription(DESCRIPTION);

		this.roleDescriptor.addAdditionalTask(task);
		assertFalse(this.roleDescriptor.getAdditionalTasks().isEmpty());
		assertTrue("", this.roleDescriptor.getAdditionalTasks().contains(task));
		assertFalse(task.getAdditionalRoles().isEmpty());

		this.roleDescriptor.removeAdditionalTask(task);
		assertTrue(this.roleDescriptor.getAdditionalTasks().isEmpty());
		assertTrue(task.getAdditionalRoles().isEmpty());
	}

	@Test
	public void testRemoveAllAdditionalTasks() {

		TaskDescriptor task = new TaskDescriptor();
		task.setName(NAME);
		task.setDescription(DESCRIPTION);

		TaskDescriptor tmp = new TaskDescriptor();
		tmp.setName("autreNom");
		tmp.setDescription(DESCRIPTION);

		Set<TaskDescriptor> set = new HashSet<TaskDescriptor>();
		set.add(task);
		set.add(tmp);

		this.roleDescriptor.addAllAdditionalTasks(set);
		assertTrue(this.roleDescriptor.getAdditionalTasks().size() == 2);
		assertTrue(task.getAdditionalRoles().size() == 1);
		assertTrue(tmp.getAdditionalRoles().size() == 1);

		this.roleDescriptor.removeAllAdditionalTasks();
		assertTrue(this.roleDescriptor.getAdditionalTasks().isEmpty());
		assertTrue(task.getAdditionalRoles().isEmpty());
		assertTrue(tmp.getAdditionalRoles().isEmpty());
	}
}
