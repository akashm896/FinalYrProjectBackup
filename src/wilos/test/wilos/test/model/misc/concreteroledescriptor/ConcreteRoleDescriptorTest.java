/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007-2008 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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
package wilos.test.model.misc.concreteroledescriptor;


import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;
import wilos.model.misc.wilosuser.Participant;
import wilos.model.spem2.role.RoleDescriptor;

public class ConcreteRoleDescriptorTest {

	private ConcreteRoleDescriptor concreteRoleDescriptor ;

public static final String CONCRETENAME = "concreteName";

	public static final String PREFIX = "prefix" ;

	public static final Boolean IS_OPTIONAL = true ;

	@Before
	public void setUp() {
		this.concreteRoleDescriptor = new ConcreteRoleDescriptor() ;
		this.concreteRoleDescriptor.setConcreteName(CONCRETENAME);
	}

	@After
	public void tearDown() {
		//None
	}

	@Test
	public void testHashCode() {

		// Rk: the setUp method is called here.

		ConcreteRoleDescriptor tmp = new ConcreteRoleDescriptor();
		tmp.setConcreteName(CONCRETENAME);

		RoleDescriptor roleDescriptor = new RoleDescriptor();
		roleDescriptor.setPrefix(PREFIX) ;
		roleDescriptor.setIsOptional(IS_OPTIONAL) ;

		tmp.setRoleDescriptor(roleDescriptor);
		this.concreteRoleDescriptor.setRoleDescriptor(roleDescriptor);

		assertNotNull(this.concreteRoleDescriptor.hashCode()) ;
		assertNotNull(tmp.hashCode()) ;
		assertEquals(this.concreteRoleDescriptor.hashCode(), tmp.hashCode()) ;

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testEqualsObject() {

		// Rk: the setUp method is called here.

		ConcreteRoleDescriptor tmp = new ConcreteRoleDescriptor();
		tmp.setConcreteName(CONCRETENAME);

		RoleDescriptor roleDescriptor = new RoleDescriptor();
		roleDescriptor.setPrefix(PREFIX) ;
		roleDescriptor.setIsOptional(IS_OPTIONAL) ;

		tmp.setRoleDescriptor(roleDescriptor);
		this.concreteRoleDescriptor.setRoleDescriptor(roleDescriptor);

		assertNotNull(this.concreteRoleDescriptor) ;
		assertNotNull(tmp) ;
		assertTrue(this.concreteRoleDescriptor.equals(tmp)) ;

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testClone() {

		// Rk: the setUp method is called here.

		ConcreteRoleDescriptor tmp = null;

		RoleDescriptor roleDescriptor = new RoleDescriptor();
		roleDescriptor.setPrefix(PREFIX) ;
		roleDescriptor.setIsOptional(IS_OPTIONAL) ;

		this.concreteRoleDescriptor.setRoleDescriptor(roleDescriptor);

		try{
			tmp = this.concreteRoleDescriptor.clone();
		}
		catch(CloneNotSupportedException e){
			fail("Error CloneNotSupportedException in the testClone method") ;
		}

		assertNotNull(tmp);
		assertEquals(tmp, this.concreteRoleDescriptor) ;


		// Rk: the tearDown method is called here.
	}

	@Test
	public void testAddParticipant() {
		// Rk: the setUp method is called here.

		Participant participant = new Participant();
		participant.setName("testAddParticipant");
		this.concreteRoleDescriptor.addParticipant(participant);
		assertNotNull(this.concreteRoleDescriptor.getParticipant());
		assertEquals(this.concreteRoleDescriptor.getParticipant(), participant) ;

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testRemoveParticipant() {
		// Rk: the setUp method is called here.

		Participant participant = new Participant();
		participant.setFirstname("cocolapin");

		this.concreteRoleDescriptor.addParticipant(participant);
		assertNotNull(this.concreteRoleDescriptor.getParticipant());
		assertEquals(this.concreteRoleDescriptor.getParticipant(), participant);

		this.concreteRoleDescriptor.removeParticipant(participant);
		assertNull(this.concreteRoleDescriptor.getParticipant());
		//assertTrue(roleDescriptor.getParticipant().size() == 0);

		this.concreteRoleDescriptor.removeParticipant(participant) ;
		assertNull(this.concreteRoleDescriptor.getParticipant());

		// Rk: the tearDown method is called here.
	}

	
	@Test
	public void testAddRoleDescriptor() {
		// Rk: the setUp method is called here.

		RoleDescriptor roleDescriptor = new RoleDescriptor();
		this.concreteRoleDescriptor.addRoleDescriptor(roleDescriptor);
		assertNotNull(this.concreteRoleDescriptor.getRoleDescriptor());
		assertEquals(this.concreteRoleDescriptor.getRoleDescriptor(), roleDescriptor) ;

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testRemoveRoleDescriptor() {
		// Rk: the setUp method is called here.

		RoleDescriptor roleDescriptor = new RoleDescriptor();

		this.concreteRoleDescriptor.addRoleDescriptor(roleDescriptor);
		assertNotNull(this.concreteRoleDescriptor.getRoleDescriptor());
		assertEquals(this.concreteRoleDescriptor.getRoleDescriptor(), roleDescriptor);

		this.concreteRoleDescriptor.removeRoleDescriptor(roleDescriptor);
		assertNull(this.concreteRoleDescriptor.getRoleDescriptor());
		assertTrue(roleDescriptor.getConcreteRoleDescriptors().size() == 0);

		this.concreteRoleDescriptor.removeRoleDescriptor(roleDescriptor) ;
		assertNull(this.concreteRoleDescriptor.getRoleDescriptor());

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testAddConcreteTaskDescriptor() {
		// Rk: the setUp method is called here.

		ConcreteTaskDescriptor concreteTaskDescriptor = new ConcreteTaskDescriptor();
		this.concreteRoleDescriptor.addPrimaryConcreteTaskDescriptor(concreteTaskDescriptor);

		assertNotNull(this.concreteRoleDescriptor.getPrimaryConcreteTaskDescriptors());
		assertTrue(this.concreteRoleDescriptor.getPrimaryConcreteTaskDescriptors().size() == 1);

		assertEquals(concreteTaskDescriptor.getMainConcreteRoleDescriptor(), this.concreteRoleDescriptor);

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testRemoveConcreteTaskDescriptor() {
		// Rk: the setUp method is called here.

		ConcreteTaskDescriptor concreteTaskDescriptor = new ConcreteTaskDescriptor();

		this.concreteRoleDescriptor.addPrimaryConcreteTaskDescriptor(concreteTaskDescriptor);
		assertNotNull(this.concreteRoleDescriptor.getPrimaryConcreteTaskDescriptors());
		assertNotNull(this.concreteRoleDescriptor.getPrimaryConcreteTaskDescriptors().contains(concreteTaskDescriptor));
		assertEquals(concreteTaskDescriptor.getMainConcreteRoleDescriptor(), this.concreteRoleDescriptor);

		this.concreteRoleDescriptor.removePrimaryConcreteTaskDescriptor(concreteTaskDescriptor);
		assertTrue(this.concreteRoleDescriptor.getPrimaryConcreteTaskDescriptors().size() == 0);

		assertNull(concreteTaskDescriptor.getMainConcreteRoleDescriptor());

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testAddAllConcreteTaskDescriptors() {
		// Rk: the setUp method is called here.

		Set<ConcreteTaskDescriptor> concreteTaskDescriptors = new HashSet<ConcreteTaskDescriptor>();
		ConcreteTaskDescriptor ctd1 = new ConcreteTaskDescriptor() ;
		ctd1.setConcreteName("ctd1") ;
		ConcreteTaskDescriptor ctd2 = new ConcreteTaskDescriptor() ;
		ctd2.setConcreteName("ctd2") ;	// only to specify that ctd1 and ctd2 are not equals
		concreteTaskDescriptors.add(ctd1) ;
		concreteTaskDescriptors.add(ctd2) ;
		for (ConcreteTaskDescriptor ctd : concreteTaskDescriptors) {
			ctd.addMainConcreteRoleDescriptor(this.concreteRoleDescriptor);
		}

		this.concreteRoleDescriptor.addAllPrimaryConcreteTaskDescriptors(concreteTaskDescriptors) ;

		assertNotNull(this.concreteRoleDescriptor.getPrimaryConcreteTaskDescriptors()) ;
		assertTrue(this.concreteRoleDescriptor.getPrimaryConcreteTaskDescriptors().size() == 2) ;
		assertTrue(this.concreteRoleDescriptor.getPrimaryConcreteTaskDescriptors().contains(ctd1)) ;
		assertTrue(this.concreteRoleDescriptor.getPrimaryConcreteTaskDescriptors().contains(ctd2)) ;

		assertNotNull(ctd1.getMainConcreteRoleDescriptor()) ;
		assertEquals(ctd1.getMainConcreteRoleDescriptor(), this.concreteRoleDescriptor) ;
		assertNotNull(ctd2.getMainConcreteRoleDescriptor()) ;
		assertEquals(ctd2.getMainConcreteRoleDescriptor(), this.concreteRoleDescriptor) ;

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testRemoveAllConcreteTaskDescriptors() {
		// Rk: the setUp method is called here.

		Set<ConcreteTaskDescriptor> concreteTaskDescriptors = new HashSet<ConcreteTaskDescriptor>();
		ConcreteTaskDescriptor ctd1 = new ConcreteTaskDescriptor() ;
		ctd1.setConcreteName("ctd1") ;
		ConcreteTaskDescriptor ctd2 = new ConcreteTaskDescriptor() ;
		ctd2.setConcreteName("ctd2") ;	// only to specify that ctd1 and ctd2 are not "equals()"
		concreteTaskDescriptors.add(ctd1) ;
		concreteTaskDescriptors.add(ctd2) ;
		for (ConcreteTaskDescriptor ctd : concreteTaskDescriptors) {
			ctd.addMainConcreteRoleDescriptor(this.concreteRoleDescriptor);
		}

		// see above for tests checking addAllConcreteTaskDescriptors
	this.concreteRoleDescriptor
		.addAllPrimaryConcreteTaskDescriptors(concreteTaskDescriptors);

	this.concreteRoleDescriptor.removeAllPrimaryConcreteTaskDescriptors();

	assertNull(ctd1.getMainConcreteRoleDescriptor());
	assertNull(ctd2.getMainConcreteRoleDescriptor());
	assertTrue(this.concreteRoleDescriptor
		.getPrimaryConcreteTaskDescriptors().size() == 0);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testAddConcreteWorkProductDescriptor() {
	// Rk: the setUp method is called here.

	ConcreteWorkProductDescriptor product = new ConcreteWorkProductDescriptor();
	assertTrue(this.concreteRoleDescriptor
		.getConcreteWorkProductDescriptors().isEmpty());
	this.concreteRoleDescriptor.addConcreteWorkProductDescriptor(product);
	assertFalse(this.concreteRoleDescriptor
		.getConcreteWorkProductDescriptors().isEmpty());
	assertTrue(this.concreteRoleDescriptor
		.getConcreteWorkProductDescriptors().size() == 1);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testRemoveConcreteWorkProductDescriptor() {
	ConcreteWorkProductDescriptor product = new ConcreteWorkProductDescriptor();
	assertTrue(this.concreteRoleDescriptor.getConcreteWorkProductDescriptors().isEmpty());
	this.concreteRoleDescriptor.addConcreteWorkProductDescriptor(product);

	assertFalse(this.concreteRoleDescriptor.getConcreteWorkProductDescriptors().isEmpty());
	assertEquals(1,this.concreteRoleDescriptor.getConcreteWorkProductDescriptors().size());

	this.concreteRoleDescriptor.removeConcreteWorkProductDescriptor(product);
	assertTrue(this.concreteRoleDescriptor.getConcreteWorkProductDescriptors().isEmpty());
    }
}
