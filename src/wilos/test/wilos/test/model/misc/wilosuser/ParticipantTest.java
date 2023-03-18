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

package wilos.test.model.misc.wilosuser;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.project.Project;
import wilos.model.misc.wilosuser.Participant;

public class ParticipantTest {

	private Participant participant1;

	private Participant participant2;

	private final static String LOGIN = "john";

	private final static String LOGIN2 = "cathy";

	private final static String NAME = "georges";

	private final static String NAME2 = "willis";

	private final static String FIRSTNAME = "johnny";

	private final static String FIRSTNAME2 = "catherine";

	private final static String PASS = "pass";

	private final static String PASS2 = "pass2";

	@Before
	public void setUp() {
		participant1 = new Participant();
		participant2 = new Participant();
	}

	@After
	public void tearDown() {
		//None.
	}

	@Test
	public void testEqualsObject() {
		participant1.setLogin(LOGIN);
		participant1.setFirstname(FIRSTNAME);
		participant1.setName(NAME);
		participant1.setPassword(PASS);
		participant2.setLogin(LOGIN);
		participant2.setFirstname(FIRSTNAME);
		participant2.setName(NAME);
		participant2.setPassword(PASS);
		assertTrue(participant1.equals(participant2));
		/* Login test */
		participant2.setLogin(LOGIN2);
		assertFalse(participant1.equals(participant2));
		/* Name test */
		participant2.setLogin(LOGIN);
		participant2.setName(NAME2);
		assertFalse(participant1.equals(participant2));
		/* FirstName test */
		participant2.setName(NAME);
		participant2.setFirstname(FIRSTNAME2);
		assertFalse(participant1.equals(participant2));
		/* Password test */
		participant2.setFirstname(FIRSTNAME);
		participant2.setPassword(PASS2);
		assertFalse(participant1.equals(participant2));
	}

	@Test
	public void testGetConcreteRoleDescriptors() {
		ConcreteRoleDescriptor crd = new ConcreteRoleDescriptor();
		participant1.addConcreteRoleDescriptor(crd);
		assertNotNull(participant1.getConcreteRoleDescriptors());
		assertEquals(crd,
				participant1.getConcreteRoleDescriptors().toArray()[0]);
	}

	@Test
	public void testAddConcreteRoleDescriptor() {
		ConcreteRoleDescriptor crd = new ConcreteRoleDescriptor();
		participant1.addConcreteRoleDescriptor(crd);
		assertNotNull(participant1.getConcreteRoleDescriptors());
		assertEquals(crd,
				participant1.getConcreteRoleDescriptors().toArray()[0]);
		assertEquals(crd.getParticipant(), participant1);
	}

	@Test
	public void testRemoveConcreteRoleDescriptor() {
		ConcreteRoleDescriptor crd = new ConcreteRoleDescriptor();
		participant1.addConcreteRoleDescriptor(crd);
		assertNotNull(participant1.getConcreteRoleDescriptors());
		participant1.removeConcreteRoleDescriptor(crd);
		assertTrue(participant1.getConcreteRoleDescriptors().isEmpty());
		assertEquals(crd.getParticipant(), null);
	}

	@Test
	public void testRemoveAllConcreteRoleDescriptors() {
		ConcreteRoleDescriptor crd = new ConcreteRoleDescriptor();
		ConcreteRoleDescriptor crd2 = new ConcreteRoleDescriptor();
		participant1.addConcreteRoleDescriptor(crd);
		participant1.addConcreteRoleDescriptor(crd2);
		assertNotNull(participant1.getConcreteRoleDescriptors());
		participant1.removeAllConcreteRoleDescriptors();
		assertTrue(participant1.getConcreteRoleDescriptors().isEmpty());
		assertEquals(null, crd.getParticipant());
		assertEquals(null, crd2.getParticipant());
	}

	@Test
	public void testAddToProject() {
		Project project = new Project();
		participant1.addAffectedProject(project);
		assertNotNull(participant1.getAffectedProjectList());
		assertEquals(project,
				participant1.getAffectedProjectList().toArray()[0]);
		assertEquals(project.getParticipants().toArray()[0], participant1);
	}

	@Test
	public void testRemoveFromProject() {
		Project project = new Project();
		participant1.addAffectedProject(project);
		assertNotNull(participant1.getAffectedProjectList());
		participant1.removeAffectedProject(project);
		assertTrue(participant1.getAffectedProjectList().isEmpty());
		assertTrue(project.getParticipants().isEmpty());
	}

	@Test
	public void testRemoveAllProject() {
		Project project = new Project();
		Project project2 = new Project();
		participant1.addAffectedProject(project);
		participant1.addAffectedProject(project2);
		assertNotNull(participant1.getAffectedProjectList());
		participant1.removeAllAffectedProjects();
		assertTrue(participant1.getAffectedProjectList().isEmpty());
		assertTrue(project.getParticipants().isEmpty());
		assertTrue(project2.getParticipants().isEmpty());
	}

	@Test
	public void testRemoveFromAllProject() {
		Project project = new Project();
		Project project2 = new Project();
		participant1.addAffectedProject(project);
		participant1.addAffectedProject(project2);
		assertNotNull(participant1.getAffectedProjectList());
		participant1.removeAllAffectedProjects();
		assertTrue(participant1.getAffectedProjectList().isEmpty());
		assertTrue(project.getParticipants().isEmpty());
		assertTrue(project2.getParticipants().isEmpty());
	}

	@Test
	public void testGetAffectedProjectList() {
		Project project = new Project();
		participant1.addAffectedProject(project);
		assertNotNull(participant1.getAffectedProjectList());
		assertEquals(project,
				participant1.getAffectedProjectList().toArray()[0]);
	}

	@Test
	public void testAddManagedProject() {
		Project project = new Project();
		participant1.addManagedProject(project);
		assertNotNull(participant1.getManagedProjects());
		assertEquals(project, participant1.getManagedProjects().toArray()[0]);
		assertEquals(project.getProjectManager(), participant1);
	}

	@Test
	public void testRemoveManagedProject() {
		Project project = new Project();
		participant1.addAffectedProject(project);
		assertNotNull(participant1.getManagedProjects());
		participant1.removeAffectedProject(project);
		assertTrue(participant1.getManagedProjects().isEmpty());
		assertNull(project.getProjectManager());
	}

	@Test
	public void testRemoveAllManagedProjects() {
		Project project = new Project();
		Project project2 = new Project();
		participant1.addManagedProject(project);
		participant1.addManagedProject(project2);
		assertNotNull(participant1.getManagedProjects());
		participant1.removeAllManagedProjects();
		assertTrue(participant1.getManagedProjects().isEmpty());
		assertEquals(null, project.getProjectManager());
		assertEquals(null, project2.getProjectManager());
	}

	@Test
	public void testGetManagedProjects() {
		Project project = new Project();
		participant1.addManagedProject(project);
		assertNotNull(participant1.getManagedProjects());
		assertEquals(project, participant1.getManagedProjects().toArray()[0]);
	}

}
