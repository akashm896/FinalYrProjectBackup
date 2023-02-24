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

import wilos.hibernate.spem2.workproduct.WorkProductDefinitionDao;
import wilos.model.spem2.workproduct.WorkProductDefinition;
import wilos.test.TestConfiguration;

/**
 * Unit test for WorkProductDefinitionDao
 * 
 * 
 */
public class WorkProductDefinitionDaoTest {

    private WorkProductDefinitionDao workProductDefinitionDao = (WorkProductDefinitionDao) TestConfiguration
	    .getInstance().getApplicationContext().getBean(
		    "WorkProductDefinitionDao");

    private WorkProductDefinition workProductDefinition = null;

    public static final String NAME = "thisWorkProduct";

    public static final String DESCRIPTION = "workProductDefinition description";

    @Before
    public void setUp() {

	// Create empty WorkProductDefinition
	this.workProductDefinition = new WorkProductDefinition();
	this.workProductDefinition.setName(NAME);
	this.workProductDefinition.setDescription(DESCRIPTION);
    }

    @After
    public void tearDown() {

	this.workProductDefinition = null;
    }

    @Test
    public void testSaveOrUpdateWorkProduct() {
	// Rk: the setUp method is called here.

	// Save the workProductDefinition with the method to test.
	String id = this.workProductDefinitionDao
		.saveOrUpdateWorkProductDefinition(this.workProductDefinition);

	WorkProductDefinition workProductTmp = (WorkProductDefinition) this.workProductDefinitionDao
		.getHibernateTemplate().load(WorkProductDefinition.class, id);
	assertNotNull(workProductTmp);

	this.workProductDefinitionDao
		.deleteWorkProductDefinition(this.workProductDefinition);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetAllWorkProduct() {
	// Rk: the setUp method is called here.
	// Save the workProductDefinition into the database.
	this.workProductDefinitionDao.getHibernateTemplate().saveOrUpdate(
		this.workProductDefinition);

	// Look if this workProductDefinition is also into the database and look
	// if the
	// size of
	// the set is >= 1.
	List<WorkProductDefinition> workProductDefinitions = this.workProductDefinitionDao
		.getAllWorkProductDefinitions();
	assertNotNull(workProductDefinitions);
	assertTrue(workProductDefinitions.size() >= 1);

	this.workProductDefinitionDao
		.deleteWorkProductDefinition(this.workProductDefinition);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetWorkProduct() {
	// Rk: the setUp method is called here.

	// Save the workProductDefinition into the database.
	String id = this.workProductDefinitionDao
		.saveOrUpdateWorkProductDefinition(this.workProductDefinition);

	// Test the method getActivity with an existing activity.
	WorkProductDefinition workProductTmp = this.workProductDefinitionDao
		.getWorkProductDefinition(id);
	assertNotNull(workProductTmp);
	assertEquals("Name", workProductTmp.getName(), NAME);
	assertEquals("Description", workProductTmp.getDescription(),
		DESCRIPTION);

	// Test the method getWorkProduct with an unexisting
	// workProductDefinition.
	this.workProductDefinitionDao.getHibernateTemplate().delete(
		workProductDefinition);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testDeleteWorkProduct() {
	// Rk: the setUp method is called here.

	// Save the workProductDefinition into the database.
	String id = this.workProductDefinitionDao
		.saveOrUpdateWorkProductDefinition(this.workProductDefinition);

	// Test the method deleteWorkProduct with an workProductDefinition
	// existing into the
	// db.
	this.workProductDefinitionDao
		.deleteWorkProductDefinition(this.workProductDefinition);

	// See if this.workProduct is now absent in the db.
	WorkProductDefinition workProductTmp = (WorkProductDefinition) this.workProductDefinitionDao
		.getHibernateTemplate().get(WorkProductDefinition.class, id);
	assertNull(workProductTmp);

	// Rk: the tearDown method is called here.
    }
}
