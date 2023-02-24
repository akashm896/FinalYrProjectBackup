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

package wilos.test.business.services.spem2.section;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.spem2.checklist.CheckListService;
import wilos.business.services.spem2.section.SectionService;
import wilos.model.spem2.checklist.CheckList;
import wilos.model.spem2.section.Section;
import wilos.test.TestConfiguration;

public class SectionServiceTest {

    private SectionService sectionService = (SectionService) TestConfiguration.getInstance().getApplicationContext()
	    .getBean("SectionService");

    private CheckListService checkListService = (CheckListService) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("CheckListService");

    private Section section;

    private CheckList checkList;

    public static final String guid = "guident";

    public static final String name = "le_nom";

    public static final String description = "la_description";

    private String sectionId;

    @Before
    public void setUp() {

	this.checkList = new CheckList();
	this.checkList.setName("my name");

	this.checkListService.saveCheckList(this.checkList);

	this.section = new Section();
	this.section.setGuid("My guid");
    }

    @After
    public void tearDown() {
	this.checkListService.deleteCheckList(this.checkList);
	this.checkList = null;
	this.section = null;
    }

    @Test
    public void testStorageOfAnSectionInATransaction() {

	this.section.addCheckList(this.checkList);
	this.sectionId = this.sectionService.saveSection(this.section);
	this.checkListService.saveCheckList(this.checkList);

	Section retrievedSection = this.sectionService.getSection(this.sectionId);
	assertNotNull("not null", retrievedSection);

	this.section.removeCheckList(this.checkList);
	this.checkListService.saveCheckList(this.checkList);

	this.sectionService.deleteSection(this.section);
    }
    
    @Test
    public void testThatAllSectionsAreRetrieved() {

	int nb = this.sectionService.getAllSections().size();

	this.sectionId = this.sectionService.saveSection(this.section);

	Section s1 = new Section();
	s1.setName("Your name");
	s1.setDescription("Your description");

	this.sectionService.saveSection(s1);

	Section s2 = new Section();
	s2.setName("Its name");
	s2.setDescription("Its description");

	this.sectionService.saveSection(s2);

	List<Section> sectiones = this.sectionService.getAllSections();

	assertNotNull("not null", sectiones);
	assertEquals("number of sectiones", 3, sectiones.size() - nb);

	this.sectionService.deleteSection(s2);
	this.sectionService.deleteSection(s1);
	this.sectionService.deleteSection(this.section);
	s2 = null;
	s1 = null;
    }

}
