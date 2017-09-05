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
package wilos.test.hibernate.misc.concreteiteration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.misc.concreteiteration.ConcreteIterationDao;
import wilos.model.misc.concreteiteration.ConcreteIteration;
import wilos.test.TestConfiguration;

/**
 * 
 * @author Almiriad
 * 
 */
public class ConcreteIterationDaoTest {

    private ConcreteIterationDao concreteIterationDao = (ConcreteIterationDao) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("ConcreteIterationDao");

    private ConcreteIteration concreteIteration;

    @Before
    public void setUp() {

	// Create empty ConcreteIteration
	this.concreteIteration = new ConcreteIteration();
	this.concreteIteration.setConcreteName("My concrete name");
    }

    @After
    public void tearDown() {

	this.concreteIteration = null;
    }

    @Test
    public final void testSaveOrUpdateConcreteIteration() {
	// Rk: the setUp method is called here.

	// Save the ConcreteIteration with the method to test.
	this.concreteIterationDao.saveOrUpdateConcreteIteration(this.concreteIteration);

	// Check the saving.
	String id = this.concreteIteration.getId();
	ConcreteIteration ConcreteIterationTmp = (ConcreteIteration) this.concreteIterationDao.getConcreteIteration(id);
	assertNotNull(ConcreteIterationTmp);

	// Delete the data stored in the database
	this.concreteIterationDao.deleteConcreteIteration(this.concreteIteration);
	// Rk: the tearDown method is called here.
    }

    @Test
    public final void testGetAllConcreteIterations() {
	// Rk: the setUp method is called here.

	// Save the ConcreteIteration with the method to test.
	this.concreteIterationDao.saveOrUpdateConcreteIteration(this.concreteIteration);

	// Look if this ConcreteIteration is also into the database and look if
	// the size of the set is >= 1.
	List<ConcreteIteration> ConcreteIterations = this.concreteIterationDao.getAllConcreteIterations();
	assertNotNull(ConcreteIterations);
	assertTrue(ConcreteIterations.size() >= 1);

	// Delete the data stored in the database
	this.concreteIterationDao.deleteConcreteIteration(this.concreteIteration);
	// Rk: the tearDown method is called here.
    }

    @Test
    public final void testGetConcreteIteration() {
	// Rk: the setUp method is called here.

	// Save the ConcreteIteration into the database.
	this.concreteIterationDao.saveOrUpdateConcreteIteration(this.concreteIteration);
	String id = this.concreteIteration.getId();

	// Test the method getConcreteIteration with an existing
	// ConcreteIteration.
	ConcreteIteration concreteIterationTmp = this.concreteIterationDao.getConcreteIteration(id);
	assertNotNull(concreteIterationTmp);
	assertEquals("Name", concreteIterationTmp.getConcreteName(), this.concreteIteration.getConcreteName());

	// Test the method getConcreteIteration with an unexisting
	// ConcreteIteration.
	this.concreteIterationDao.deleteConcreteIteration(this.concreteIteration);

	// Rk: the tearDown method is called here.
    }

    @Test
    public final void testDeleteConcreteIteration() {
	// Rk: the setUp method is called here.

	// Save the ConcreteIteration into the database.
	this.concreteIterationDao.saveOrUpdateConcreteIteration(this.concreteIteration);
	String id = this.concreteIteration.getId();

	// Test the method deleteConcreteIteration with an ConcreteIteration
	// existing into the db.
	this.concreteIterationDao.deleteConcreteIteration(this.concreteIteration);

	// See if this.ConcreteIteration is now absent in the db.
	ConcreteIteration concreteIterationTmp = (ConcreteIteration) this.concreteIterationDao.getConcreteIteration(id);
	assertNull(concreteIterationTmp);

	// Rk: the tearDown method is called here.
    }
}
