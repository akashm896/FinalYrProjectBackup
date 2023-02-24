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
package wilos.test.model.misc.concreteworkproduct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;
import wilos.model.spem2.workproduct.WorkProductDescriptor;

public class ConcreteWorkProductTest {

    private ConcreteWorkProductDescriptor concreteWorkProductDescriptor;

    private static final String NAME = "Concrete Name";

    @Before
    public void setUp() {
	this.concreteWorkProductDescriptor = new ConcreteWorkProductDescriptor();
	this.concreteWorkProductDescriptor.setConcreteName(NAME);
    }

    @After
    public void tearDown() {
	// None
    }

    @Test
    public void testClone() {
	try {
	    assertEquals(this.concreteWorkProductDescriptor, this.concreteWorkProductDescriptor
		    .clone());
	} catch (CloneNotSupportedException e) {
	    fail("Error CloneNotSupportedException in the testClone method");
	}
    }

    @Test
    public void testHashCode() {

	ConcreteWorkProductDescriptor tmp = new ConcreteWorkProductDescriptor();
	tmp.setConcreteName(NAME);

	assertNotNull(this.concreteWorkProductDescriptor.hashCode());
	assertNotNull(tmp.hashCode());
	assertEquals(this.concreteWorkProductDescriptor.hashCode(), tmp.hashCode());
    }

    @Test
    public void testEqualsObject() {

	ConcreteWorkProductDescriptor tmp = new ConcreteWorkProductDescriptor();
	tmp.setConcreteName(NAME);

	assertTrue(this.concreteWorkProductDescriptor.equals(tmp));

	ConcreteWorkProductDescriptor act = new ConcreteWorkProductDescriptor();
	act.setConcreteName("name2");

	assertFalse(this.concreteWorkProductDescriptor.equals(act));
    }

    @Test
    public void testAddConcreteResponsibleRole() {
	ConcreteRoleDescriptor role = new ConcreteRoleDescriptor();
	role.setConcreteName("Test Role");

	assertEquals(null, this.concreteWorkProductDescriptor
		.getResponsibleConcreteRoleDescriptor());
	assertTrue(role.getConcreteWorkProductDescriptors().isEmpty());

	this.concreteWorkProductDescriptor.addResponsibleConcreteRoleDescriptor(role);
	assertEquals(role, this.concreteWorkProductDescriptor
		.getResponsibleConcreteRoleDescriptor());
	assertEquals(1, role.getConcreteWorkProductDescriptors().size());
    }

    @Test
    public void testRemoveResponsibleConcreteRoleDescriptor() {
	ConcreteRoleDescriptor role = new ConcreteRoleDescriptor();
	role.setConcreteName("Test Role");
	this.concreteWorkProductDescriptor.setResponsibleConcreteRoleDescriptor(role);

	assertEquals(role, this.concreteWorkProductDescriptor
		.getResponsibleConcreteRoleDescriptor());
	this.concreteWorkProductDescriptor
		.removeResponsibleConcreteRoleDescriptor(role);
	assertEquals(null, this.concreteWorkProductDescriptor
		.getResponsibleConcreteRoleDescriptor());
	assertTrue(role.getConcreteWorkProductDescriptors().isEmpty());
    }
    
    @Test
    public void testAddWorkProductDescriptor(){
	WorkProductDescriptor wpd = new WorkProductDescriptor();
	assertEquals(null,this.concreteWorkProductDescriptor.getWorkProductDescriptor());
	
	this.concreteWorkProductDescriptor.addWorkProductDescriptor(wpd);
	assertEquals(wpd, this.concreteWorkProductDescriptor.getWorkProductDescriptor());
    }
    
    @Test
    public void testRemoveWorkProductDescriptor(){
	WorkProductDescriptor wpd = new WorkProductDescriptor();	
	this.concreteWorkProductDescriptor.addWorkProductDescriptor(wpd);
	assertNotNull(this.concreteWorkProductDescriptor.getWorkProductDescriptor());
	
	this.concreteWorkProductDescriptor.removeWorkProductDescriptor(wpd);
	assertEquals(null, this.concreteWorkProductDescriptor.getWorkProductDescriptor());
    }
}
