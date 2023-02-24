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

package wilos.test.business.services.spem2.checklist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.spem2.checklist.CheckListService;
import wilos.model.spem2.checklist.CheckList;
import wilos.test.TestConfiguration;

public class CheckListServiceTest {

    private CheckListService checkListService = (CheckListService) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("CheckListService");;

    private CheckList checkList;

    private String checkListId;

    @Before
    public void setUp() {

	this.checkList = new CheckList();
	this.checkList.setName("My name");
    }

    @After
    public void tearDown() {
	this.checkList = null;
    }

    @Test
    public void testStorageOfAnCheckListInATransaction() {

	this.checkListId = this.checkListService.saveCheckList(this.checkList);

	CheckList retrievedCheckList = this.checkListService.getCheckListDao().getCheckList(this.checkListId);
	assertNotNull("not null", retrievedCheckList);

	this.checkListService.deleteCheckList(this.checkList);
    }

    @Test
    public void testThatAllCheckListesAreRetrieved() {

	int nb = this.checkListService.getAllCheckLists().size();

	this.checkListId = this.checkListService.saveCheckList(this.checkList);

	CheckList c1 = new CheckList();
	c1.setName("Your name");
	c1.setDescription("Your description");

	this.checkListService.saveCheckList(c1);

	CheckList c2 = new CheckList();
	c2.setName("Its name");
	c2.setDescription("Its description");

	this.checkListService.saveCheckList(c2);

	List<CheckList> checkListes = this.checkListService.getAllCheckLists();
	assertNotNull("not null", checkListes);
	assertEquals("number of checkListes", 3, checkListes.size() - nb);

	this.checkListService.deleteCheckList(c2);
	this.checkListService.deleteCheckList(c1);
	this.checkListService.deleteCheckList(this.checkList);
	c2 = null;
	c1 = null;
    }
}
