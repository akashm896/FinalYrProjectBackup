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
package wilos.test.hibernate.spem2.iteration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.spem2.iteration.IterationDao;
import wilos.model.spem2.iteration.Iteration;
import wilos.test.TestConfiguration;

/**
 * 
 * @author Soosuske
 * 
 */
public class IterationDaoTest {
    private IterationDao iterationDao = (IterationDao) TestConfiguration.getInstance().getApplicationContext().getBean(
	    "IterationDao");;

    private Iteration iteration = null;

    public static final String NAME = "thisIteration";

    public static final String DESCRIPTION = "iteration description";

    public static final String PREFIX = "prefix";

    public static final Boolean IS_OPTIONAL = true;

    public static final Boolean HAS_MULTIPLE_OCCURENCES = true;

    public static final Boolean IS_EVEN_DRIVEN = true;

    public static final Boolean IS_ON_GOING = true;

    public static final Boolean IS_PLANNED = true;

    public static final Boolean IS_REPEATABLE = true;

    @Before
    public void setUp() {

	this.iteration = new Iteration();
	// Add properties to the iteration.
	this.iteration.setName(NAME);
	this.iteration.setDescription(DESCRIPTION);
	this.iteration.setPrefix(PREFIX);
	this.iteration.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
	this.iteration.setIsEvenDriven(IS_EVEN_DRIVEN);
	this.iteration.setIsOngoing(IS_ON_GOING);
	this.iteration.setIsOptional(IS_OPTIONAL);
	this.iteration.setIsPlanned(IS_PLANNED);
	this.iteration.setIsRepeatable(IS_REPEATABLE);
    }

    @After
    public void tearDown() {
	this.iteration = null;
    }

    @Test
    public final void testSaveOrUpdateIteration() {
	// Rk: the setUp method is called here.

	// Save the iteration with the method to test.
	this.iterationDao.saveOrUpdateIteration(this.iteration);

	// Check the saving.
	String id = this.iteration.getId();
	Iteration iterationTmp = (Iteration) this.iterationDao.getIteration(id);
	assertNotNull(iterationTmp);

	this.iterationDao.deleteIteration(this.iteration);

	// Rk: the tearDown method is called here.
    }

    @Test
    public final void testGetAllIterations() {
	// Rk: the setUp method is called here.

	// Save the iteration with the method to test.
	this.iterationDao.saveOrUpdateIteration(this.iteration);

	// Look if this iteration is also into the database and look if the size
	// of the set is >= 1.
	List<Iteration> iterations = this.iterationDao.getAllIterations();
	assertNotNull(iterations);
	assertTrue(iterations.size() >= 1);

	this.iterationDao.deleteIteration(this.iteration);

	// Rk: the tearDown method is called here.
    }

    @Test
    public final void testGetIteration() {
	// Rk: the setUp method is called here.

	// Save the iteration into the database.
	this.iterationDao.saveOrUpdateIteration(this.iteration);
	String id = this.iteration.getId();

	// Test the method getIteration with an existing iteration.
	Iteration iterationTmp = this.iterationDao.getIteration(id);
	assertNotNull(iterationTmp);
	assertEquals("Name", iterationTmp.getName(), NAME);
	assertEquals("Description", iterationTmp.getDescription(), DESCRIPTION);
	assertEquals("Prefix", iterationTmp.getPrefix(), PREFIX);
	assertEquals("HasMultipleOccurences", iterationTmp.getHasMultipleOccurrences(), HAS_MULTIPLE_OCCURENCES);
	assertEquals("IsEvenDriven", iterationTmp.getIsEvenDriven(), IS_EVEN_DRIVEN);
	assertEquals("IsOnGoing", iterationTmp.getIsOngoing(), IS_ON_GOING);
	assertEquals("IsOptional", iterationTmp.getIsOptional(), IS_OPTIONAL);
	assertEquals("IsPlanned", iterationTmp.getIsPlanned(), IS_PLANNED);
	assertEquals("IsRepeatale", iterationTmp.getIsRepeatable(), IS_REPEATABLE);

	// Test the method getIteration with an unexisting iteration.
	this.iterationDao.deleteIteration(this.iteration);

	// Rk: the tearDown method is called here.
    }

    @Test
    public final void testDeleteIteration() {
	// Rk: the setUp method is called here.

	// Save the iteration into the database.
	this.iterationDao.saveOrUpdateIteration(this.iteration);
	String id = this.iteration.getId();

	// Test the method deleteIteration with an iteration existing into the
	// db.
	this.iterationDao.deleteIteration(this.iteration);

	// See if this.iteration is now absent in the db.
	Iteration iterationTmp = (Iteration) this.iterationDao.getIteration(id);
	assertNull(iterationTmp);

	// Rk: the tearDown method is called here.
    }
}
