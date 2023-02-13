/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
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
package wilos.test.hibernate.spem2.workproduct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.spem2.workproduct.WorkProductDescriptorDao;
import wilos.model.spem2.workproduct.WorkProductDescriptor;
import wilos.test.TestConfiguration;

/**
 * Unit test for WorkProductDescriptorDao
 * 
 * @author Soosuske.
 */
public class WorkProductDescriptorDaoTest {

    private WorkProductDescriptorDao workProductDescriptorDao = (WorkProductDescriptorDao) TestConfiguration
	    .getInstance().getApplicationContext().getBean(
		    "WorkProductDescriptorDao");

    private WorkProductDescriptor workProductDescriptor = null;

    public static final String NAME = "thisWorkProductDescriptor";

    public static final String DESCRIPTION = "workProductDescriptor description";

    public static final String PREFIX = "prefix";

    public static final Boolean IS_OPTIONAL = true;

    public static final Boolean HAS_MULTIPLE_OCCURENCES = true;

    public static final Boolean IS_PLANNED = true;

    @Before
    public void setUp() {

	// Create empty workProductDescriptor
	this.workProductDescriptor = new WorkProductDescriptor();
	this.workProductDescriptor.setName(NAME);
	this.workProductDescriptor.setDescription(DESCRIPTION);
	this.workProductDescriptor.setPrefix(PREFIX);
	this.workProductDescriptor
		.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
	this.workProductDescriptor.setIsOptional(IS_OPTIONAL);
	this.workProductDescriptor.setIsPlanned(IS_PLANNED);
    }

    @After
    public void tearDown() {

	this.workProductDescriptor = null;
    }

    @Test
    public void testSaveOrUpdateWorkProductDescriptor() {
	// Rk: the setUp method is called here.

	// Save the workProductDescriptor with the method to test.
	String id = this.workProductDescriptorDao
		.saveOrUpdateWorkProductDescriptor(this.workProductDescriptor);

	WorkProductDescriptor workProductDescriptorTmp = (WorkProductDescriptor) this.workProductDescriptorDao
		.getHibernateTemplate().load(WorkProductDescriptor.class, id);
	assertNotNull(workProductDescriptorTmp);

	this.workProductDescriptorDao
		.deleteWorkProductDescriptor(this.workProductDescriptor);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetAllWorkProductDescriptor() {
	// Rk: the setUp method is called here.

	// Save the workProductDescriptor into the database.
	this.workProductDescriptorDao.getHibernateTemplate().saveOrUpdate(
		this.workProductDescriptor);

	// Look if this workProductDescriptor is also into the database and look
	// if the
	// size of the set is >= 1.
	List<WorkProductDescriptor> workProductDescriptors = this.workProductDescriptorDao
		.getAllWorkProductDescriptors();
	assertNotNull(workProductDescriptors);
	assertTrue(workProductDescriptors.size() >= 1);

	this.workProductDescriptorDao
		.deleteWorkProductDescriptor(this.workProductDescriptor);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetWorkProductDescriptor() {
	// Rk: the setUp method is called here.

	// Save the workProductDescriptor into the database.
	String id = this.workProductDescriptorDao
		.saveOrUpdateWorkProductDescriptor(this.workProductDescriptor);

	// Test the method getWorkProductDescriptor with an existing
	// workProductDescriptor.
	WorkProductDescriptor workProductDescriptorTmp = this.workProductDescriptorDao
		.getWorkProductDescriptor(id);
	assertNotNull(workProductDescriptorTmp);
	assertEquals("Name", workProductDescriptorTmp.getName(), NAME);
	assertEquals("Description", workProductDescriptorTmp.getDescription(),
		DESCRIPTION);
	assertEquals("Prefix", workProductDescriptorTmp.getPrefix(), PREFIX);
	assertEquals("HasMultipleOccurences", workProductDescriptorTmp
		.getHasMultipleOccurrences(), HAS_MULTIPLE_OCCURENCES);
	assertEquals("IsOptional", workProductDescriptorTmp.getIsOptional(),
		IS_OPTIONAL);
	assertEquals("IsPlanned", workProductDescriptorTmp.getIsPlanned(),
		IS_PLANNED);

	// Test the method getWorkProductDescriptor with an unexisting
	// workProductDescriptor.
	this.workProductDescriptorDao.getHibernateTemplate().delete(
		workProductDescriptor);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testDeleteWorkProductDescriptor() {
	// Rk: the setUp method is called here.

	// Save the workProductDescriptor into the database.
	String id = this.workProductDescriptorDao
		.saveOrUpdateWorkProductDescriptor(this.workProductDescriptor);

	// Test the method deleteWorkProductDescriptor with an
	// workProductDescriptor existing
	// into the db.
	this.workProductDescriptorDao
		.deleteWorkProductDescriptor(this.workProductDescriptor);

	// See if this.workProductDescriptor is now absent in the db.
	WorkProductDescriptor workProductDescriptorTmp = (WorkProductDescriptor) this.workProductDescriptorDao
		.getHibernateTemplate().get(WorkProductDescriptor.class, id);
	assertNull(workProductDescriptorTmp);

	// Rk: the tearDown method is called here.
    }
}
