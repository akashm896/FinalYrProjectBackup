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

package wilos.test.hibernate.spem2.checklist;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.spem2.checklist.CheckListDao;
import wilos.model.spem2.checklist.CheckList;
import wilos.test.TestConfiguration;

public class CheckListDaoTest {

    public static final String guid = "le_guid";

    public static final String name = "le_nom";

    public static final String description = "la_description";

    private CheckListDao checklistDAO = (CheckListDao) TestConfiguration.getInstance().getApplicationContext().getBean(
	    "CheckListDao");

    private CheckList checklist;

    @Before
    public void setUp() {
	this.checklist = new CheckList();

	// Add properties to the checklist.
	this.checklist.setGuid(guid);
	this.checklist.setName(name);
	this.checklist.setDescription(description);
    }

    @After
    public void tearDown() {
	this.checklist = null;
    }

    @Test
    public void testSaveOrUpdateCheckList() {

	// Save the checklist with the method to test.
	String id = this.checklistDAO.saveOrUpdateCheckList(this.checklist);

	// Check the saving.
	CheckList checklistTmp = (CheckList) this.checklistDAO.getCheckList(id);
	assertNotNull(checklistTmp);

	this.checklistDAO.deleteCheckList(this.checklist);
    }

    @Test
    public void testGetAllGuidances() {
	// Save the checklist with the method to test.
	this.checklistDAO.saveOrUpdateCheckList(this.checklist);

	// Look if this checklist is also into the database and look if the size
	// of the set is >= 1.
	List<CheckList> checklists = this.checklistDAO.getAllCheckLists();
	assertNotNull(checklists);
	assertTrue(checklists.size() >= 1);

	this.checklistDAO.deleteCheckList(this.checklist);
    }

    @Test
    public void testGetCheckList() {

	// Save the checklist into the database.
	String id = this.checklistDAO.saveOrUpdateCheckList(this.checklist);

	// Test the method getCheckList with an existing checklist.
	CheckList checklistTmp = this.checklistDAO.getCheckList(id);
	assertNotNull(checklistTmp);
	assertEquals("GUID", checklistTmp.getGuid(), guid);
	assertEquals("Name", checklistTmp.getName(), name);
	assertEquals("Name", checklistTmp.getDescription(), description);

	this.checklistDAO.deleteCheckList(this.checklist);
    }

    @Test
    public void testDeleteCheckList() {
	// Save the checklist into the database.
	String id = this.checklistDAO.saveOrUpdateCheckList(this.checklist);

	// Test the method deleteCheckList with an checklist existing into the
	// db.
	this.checklistDAO.deleteCheckList(this.checklist);

	// See if this.checklist is now absent in the db.
	CheckList checklistTmp = (CheckList) this.checklistDAO.getCheckList(id);
	assertNull(checklistTmp);
    }
}
