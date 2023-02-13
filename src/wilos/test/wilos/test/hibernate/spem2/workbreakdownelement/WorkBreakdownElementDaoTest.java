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

package wilos.test.hibernate.spem2.workbreakdownelement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.spem2.workbreakdownelement.WorkBreakdownElementDao;
import wilos.model.spem2.workbreakdownelement.WorkBreakdownElement;
import wilos.test.TestConfiguration;

/**
 * @author Sebastien
 * 
 * Unit test for WorkBreakdownElementDao
 * 
 */
public class WorkBreakdownElementDaoTest {

    private WorkBreakdownElementDao workBreakdownElementDao = (WorkBreakdownElementDao) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("WorkBreakdownElementDao");

    private WorkBreakdownElement workBreakdownElement = null;

    @Before
    public void setUp() {
	this.workBreakdownElement = new WorkBreakdownElement();
	this.workBreakdownElement.setGuid("My guid");
	this.workBreakdownElement.setName("My name");
	this.workBreakdownElement.setIsRepeatable(true);
    }

    @After
    public void tearDown() {
	this.workBreakdownElement = null;
    }

    @Test
    public void testStorageOfANullWorkBreakdownElement() {

	WorkBreakdownElement workBreakdownElement = null;

	String id = this.workBreakdownElementDao.saveOrUpdateWorkBreakdownElement(workBreakdownElement);
	WorkBreakdownElement retrievedWorkBreakdownElement = this.workBreakdownElementDao.getWorkBreakdownElement(id);

	assertNull("null", retrievedWorkBreakdownElement);
    }

    @Test
    public void testStorageOfAWorkBreakdownElement() {

	String id = this.workBreakdownElementDao.saveOrUpdateWorkBreakdownElement(this.workBreakdownElement);
	WorkBreakdownElement retrievedWorkBreakdownElement = this.workBreakdownElementDao.getWorkBreakdownElement(id);

	assertEquals("equal guid", this.workBreakdownElement.getGuid(), retrievedWorkBreakdownElement.getGuid());
	assertEquals("equal name", this.workBreakdownElement.getName(), retrievedWorkBreakdownElement.getName());
	assertEquals("equal desciption", this.workBreakdownElement.getIsRepeatable(), retrievedWorkBreakdownElement
		.getIsRepeatable());
	assertNotSame("same object", this.workBreakdownElement, retrievedWorkBreakdownElement);

	this.workBreakdownElementDao.deleteWorkBreakdownElement(this.workBreakdownElement);
    }

    @Test
    public void testDeletionOfAWorkBreakdownElement() {

	String id = this.workBreakdownElementDao.saveOrUpdateWorkBreakdownElement(this.workBreakdownElement);
	this.workBreakdownElementDao.deleteWorkBreakdownElement(this.workBreakdownElement);
	WorkBreakdownElement retrievedWorkBreakdownElement = this.workBreakdownElementDao.getWorkBreakdownElement(id);

	assertNull("null", retrievedWorkBreakdownElement);
    }

    @Test
    public void testRetrievalOfAllWorkBreakdownElements() {

	int nb = this.workBreakdownElementDao.getAllWorkBreakdownElements().size();

	WorkBreakdownElement workBreakdownElement1 = new WorkBreakdownElement();
	workBreakdownElement1.setGuid("Your guid");
	workBreakdownElement1.setName("Your name");
	workBreakdownElement1.setIsRepeatable(true);

	WorkBreakdownElement workBreakdownElement2 = new WorkBreakdownElement();
	workBreakdownElement1.setGuid("its guid");
	workBreakdownElement1.setName("its name");
	workBreakdownElement1.setIsRepeatable(true);

	this.workBreakdownElementDao.saveOrUpdateWorkBreakdownElement(this.workBreakdownElement);
	this.workBreakdownElementDao.saveOrUpdateWorkBreakdownElement(workBreakdownElement1);
	this.workBreakdownElementDao.saveOrUpdateWorkBreakdownElement(workBreakdownElement2);

	List<WorkBreakdownElement> workBreakdownElements = this.workBreakdownElementDao.getAllWorkBreakdownElements();
	assertNotNull("list not null", workBreakdownElements);
	assertEquals("size", 3, workBreakdownElements.size() - nb);

	this.workBreakdownElementDao.deleteWorkBreakdownElement(workBreakdownElement2);
	this.workBreakdownElementDao.deleteWorkBreakdownElement(workBreakdownElement1);
	this.workBreakdownElementDao.deleteWorkBreakdownElement(this.workBreakdownElement);

	workBreakdownElement2 = null;
	workBreakdownElement1 = null;

    }

}
