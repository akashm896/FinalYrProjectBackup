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

package wilos.test.hibernate.spem2.guide;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.spem2.guide.GuidanceDao;
import wilos.model.spem2.guide.Guidance;
import wilos.test.TestConfiguration;

public class GuidanceDaoTest {

    public static final String guid = "le_guid";

    public static final String name = "le_nom";

    public static final String description = "la_description";

    private GuidanceDao guidanceDAO = (GuidanceDao) TestConfiguration.getInstance().getApplicationContext().getBean(
	    "GuidanceDao");

    private Guidance guidance;

    @Before
    public void setUp() {

	// Create empty Activity.
	this.guidance = new Guidance();
	this.guidance.setDescription(description);
	this.guidance.setName(name);
	this.guidance.setGuid(guid);
	this.guidance.setInsertionOrder(1);
	
    }

    @After
    public void tearDown() {
	this.guidance = null;
    }

    @Test
    public void testSaveOrUpdateGuidance() {
	// Rk: the setUp method is called here.

	// Save the guideline with the method to test.
	this.guidanceDAO.saveOrUpdateGuidance(this.guidance);

	// Check the saving.
	String id = this.guidance.getId();
	Guidance guidanceTmp = (Guidance) this.guidanceDAO.getGuidance(id);
	assertNotNull(guidanceTmp);
	
	this.guidanceDAO.deleteGuidance(this.guidance);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetAllGuidances() {
	// Rk: the setUp method is called here.

	// Save the guideline with the method to test.
	this.guidanceDAO.saveOrUpdateGuidance(this.guidance);

	// Look if this guideline is also into the database and look if the size
	// of the set is >= 1.
	List<Guidance> guidances = this.guidanceDAO.getAllGuidances();
	assertNotNull(guidances);
	assertTrue(guidances.size() >= 1);
	
	this.guidanceDAO.deleteGuidance(this.guidance);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetGuidance() {
	// Rk: the setUp method is called here.

	// Save the guideline into the database.
	this.guidanceDAO.saveOrUpdateGuidance(this.guidance);
	String id = guidance.getId();

	// Test the method getGuideline with an existing guideline.
	Guidance guidanceTmp = this.guidanceDAO.getGuidance(id);
	assertNotNull(guidanceTmp);
	assertEquals("GUID", guidanceTmp.getGuid(), guid);
	assertEquals("Name", guidanceTmp.getName(), name);
	assertEquals("Name", guidanceTmp.getDescription(), description);

	// Test the method getGuideline with an unexisting guideline.
	this.guidanceDAO.deleteGuidance(this.guidance);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testDeleteGuidance() {
	// Rk: the setUp method is called here.

	// Save the guideline into the database.
	this.guidanceDAO.saveOrUpdateGuidance(this.guidance);
	String id = this.guidance.getId();

	// Test the method deleteGuideline with an guideline existing into the
	// db.
	this.guidanceDAO.deleteGuidance(this.guidance);

	// See if this.guideline is now absent in the db.
	Guidance guidanceTmp = (Guidance) this.guidanceDAO.getGuidance(id);
	assertNull(guidanceTmp);

	// Rk: the tearDown method is called here.

    }
}
