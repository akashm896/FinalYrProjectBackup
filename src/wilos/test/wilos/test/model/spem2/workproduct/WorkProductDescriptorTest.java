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
package wilos.test.model.spem2.workproduct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;
import wilos.model.spem2.workproduct.WorkProductDefinition;
import wilos.model.spem2.workproduct.WorkProductDescriptor;

public class WorkProductDescriptorTest {

    private WorkProductDescriptor workProductDescriptor;

    /**
     * attributes from Element
     */
    public static final String NAME = "thisWorkProductDescriptor";

    public static final String DESCRIPTION = "workProductDescriptor description";

    /**
     * attributes from BreakdownElement
     */
    public static final String PREFIX = "prefix";

    public static final Boolean IS_PLANNED = true;

    public static final Boolean HAS_MULTIPLE_OCCURENCES = true;

    public static final Boolean IS_OPTIONAL = true;

    @Before
    public void setUp() {
	this.workProductDescriptor = new WorkProductDescriptor();
	this.workProductDescriptor.setDescription(DESCRIPTION);
	this.workProductDescriptor.setName(NAME);
	this.workProductDescriptor.setPrefix(PREFIX);
	this.workProductDescriptor.setIsOptional(IS_OPTIONAL);
	this.workProductDescriptor
		.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
	this.workProductDescriptor.setIsPlanned(IS_PLANNED);
    }

    @After
    public void tearDown() {
	// None
    }

    @Test
    public void testHashCode() {

	WorkProductDescriptor wpd = new WorkProductDescriptor();
	wpd.setDescription(DESCRIPTION);
	wpd.setName(NAME);
	wpd.setPrefix(PREFIX);
	wpd.setIsOptional(IS_OPTIONAL);
	wpd.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
	wpd.setIsPlanned(IS_PLANNED);

	assertNotNull(this.workProductDescriptor.hashCode());
	assertNotNull(wpd.hashCode());
	assertEquals(this.workProductDescriptor.hashCode(), wpd.hashCode());
    }

    @Test
    public void testClone() {
	try {
	    assertEquals(this.workProductDescriptor, this.workProductDescriptor
		    .clone());
	} catch (CloneNotSupportedException e) {
	    fail("Error CloneNotSupportedException in the testClone method");
	}
    }

    @Test
    public void testEquals() {
	WorkProductDescriptor wpd = new WorkProductDescriptor();
	wpd.setDescription(DESCRIPTION);
	wpd.setName(NAME);
	wpd.setPrefix(PREFIX);
	wpd.setIsOptional(IS_OPTIONAL);
	wpd.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
	wpd.setIsPlanned(IS_PLANNED);

	assertTrue(this.workProductDescriptor.equals(wpd));

	WorkProductDescriptor wpdf = new WorkProductDescriptor();
	wpdf.setDescription("description");
	wpdf.setName("name");
	wpdf.setPrefix("suffix");
	wpdf.setIsOptional(false);
	wpdf.setHasMultipleOccurrences(false);
	wpdf.setIsPlanned(false);

	assertFalse(this.workProductDescriptor.equals(wpdf));
    }

    @Test
    public void testAddWorkProductDefinition() {
	WorkProductDefinition wp = new WorkProductDefinition();
	wp.setName(NAME);
	wp.setDescription(DESCRIPTION);

	this.workProductDescriptor.addWorkProductDefinition(wp);
	assertNotNull(this.workProductDescriptor.getWorkProductDefinition());
	assertNotNull(wp.getWorkProductDescriptors());
	assertTrue(wp.getWorkProductDescriptors().size() == 1);
    }

    @Test
    public void testRemoveWorkProductDefinition() {
	WorkProductDefinition wp = new WorkProductDefinition();
	wp.setName(NAME);
	wp.setDescription(DESCRIPTION);

	this.workProductDescriptor.addWorkProductDefinition(wp);
	assertNotNull(this.workProductDescriptor.getWorkProductDefinition());

	// assertEquals()
	assertTrue(wp.getWorkProductDescriptors().size() == 1);

	this.workProductDescriptor.removeWorkProductDefinition(wp);
	assertNull(this.workProductDescriptor.getWorkProductDefinition());
	assertTrue(wp.getWorkProductDescriptors().isEmpty());
    }

    @Test
    public void testAddConcreteWorkProductDescriptor() {
	ConcreteWorkProductDescriptor cwp = new ConcreteWorkProductDescriptor();
	cwp.setConcreteName(NAME);
	this.workProductDescriptor.addConcreteWorkProductDescriptor(cwp);
	assertTrue(this.workProductDescriptor
		.getConcreteWorkProductDescriptors().contains(cwp));
    }

    @Test
    public void testRemoveConcreteWorkProductDescriptor() {
	ConcreteWorkProductDescriptor cwp = new ConcreteWorkProductDescriptor();
	cwp.setConcreteName(NAME);
	this.workProductDescriptor.removeConcreteWorkProductDescriptor(cwp);
	assertFalse(this.workProductDescriptor
		.getConcreteWorkProductDescriptors().contains(cwp));
    }
}
