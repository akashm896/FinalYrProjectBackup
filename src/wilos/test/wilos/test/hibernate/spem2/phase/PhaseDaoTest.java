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
package wilos.test.hibernate.spem2.phase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.spem2.phase.PhaseDao;
import wilos.model.spem2.phase.Phase;
import wilos.test.TestConfiguration;

/**
 * 
 * @author Soosuske
 * 
 */
public class PhaseDaoTest {
    
    private PhaseDao phaseDao = (PhaseDao) TestConfiguration.getInstance().getApplicationContext().getBean("PhaseDao");

    private Phase phase = null;

    public static final String NAME = "thisPhase";

    public static final String DESCRIPTION = "phase description";

    public static final String PREFIX = "prefix";

    public static final Boolean IS_OPTIONAL = true;

    public static final Boolean HAS_MULTIPLE_OCCURENCES = true;

    public static final Boolean IS_EVEN_DRIVEN = true;

    public static final Boolean IS_ON_GOING = true;

    public static final Boolean IS_PLANNED = true;

    public static final Boolean IS_REPEATABLE = true;

    @Before
    public void setUp() {

	// Create empty phase
	this.phase = new Phase();
	// Add properties to the activity.
	this.phase.setName(NAME);
	this.phase.setDescription(DESCRIPTION);
	this.phase.setPrefix(PREFIX);
	this.phase.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
	this.phase.setIsEvenDriven(IS_EVEN_DRIVEN);
	this.phase.setIsOngoing(IS_ON_GOING);
	this.phase.setIsOptional(IS_OPTIONAL);
	this.phase.setIsPlanned(IS_PLANNED);
	this.phase.setIsRepeatable(IS_REPEATABLE);

    }

    @After
    public void tearDown() {
	this.phase = null;
    }

    @Test
    public final void testSaveOrUpdatePhase() {
	// Rk: the setUp method is called here.

	// Save the activity with the method to test.
	this.phaseDao.saveOrUpdatePhase(this.phase);

	// Check the saving.
	String id = this.phase.getId();
	Phase phaseTmp = (Phase) this.phaseDao.getPhase(id);
	assertNotNull(phaseTmp);
	
	this.phaseDao.deletePhase(this.phase);

	// Rk: the tearDown method is called here.
    }

    @Test
    public final void testGetAllPhases() {
	// Rk: the setUp method is called here.

	// Save the activity with the method to test.
	this.phaseDao.saveOrUpdatePhase(this.phase);

	// Look if this activity is also into the database and look if the size
	// of the set is >= 1.
	List<Phase> phases = this.phaseDao.getAllPhases();
	assertNotNull(phases);
	assertTrue(phases.size() >= 1);
	
	this.phaseDao.deletePhase(this.phase);

	// Rk: the tearDown method is called here.
    }

    @Test
    public final void testGetPhase() {
	// Rk: the setUp method is called here.

	// Save the activity into the database.
	this.phaseDao.saveOrUpdatePhase(this.phase);
	String id = this.phase.getId();

	// Test the method getActivity with an existing activity.
	Phase phasesTmp = this.phaseDao.getPhase(id);
	assertNotNull(phasesTmp);
	assertEquals("Name", phasesTmp.getName(), NAME);
	assertEquals("Description", phasesTmp.getDescription(), DESCRIPTION);
	assertEquals("Prefix", phasesTmp.getPrefix(), PREFIX);
	assertEquals("HasMultipleOccurences", phasesTmp.getHasMultipleOccurrences(), HAS_MULTIPLE_OCCURENCES);
	assertEquals("IsEvenDriven", phasesTmp.getIsEvenDriven(), IS_EVEN_DRIVEN);
	assertEquals("IsOnGoing", phasesTmp.getIsOngoing(), IS_ON_GOING);
	assertEquals("IsOptional", phasesTmp.getIsOptional(), IS_OPTIONAL);
	assertEquals("IsPlanned", phasesTmp.getIsPlanned(), IS_PLANNED);
	assertEquals("IsRepeatale", phasesTmp.getIsRepeatable(), IS_REPEATABLE);
	
	this.phaseDao.deletePhase(this.phase);

	// Rk: the tearDown method is called here.
    }

    @Test
    public final void testDeletePhase() {
	// Rk: the setUp method is called here.

	// Save the activity into the database.
	this.phaseDao.saveOrUpdatePhase(this.phase);
	String id = this.phase.getId();

	// Test the method deleteActivity with an activity existing into the db.
	this.phaseDao.deletePhase(this.phase);

	// See if this.activity is now absent in the db.
	Phase phaseTmp = (Phase) this.phaseDao.getPhase(id);
	assertNull(phaseTmp);

    }
}
