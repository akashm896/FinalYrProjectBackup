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
package wilos.test.hibernate.misc.concretebreakdownelement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.misc.concretebreakdownelement.ConcreteBreakdownElementDao;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.spem2.activity.Activity;
import wilos.test.TestConfiguration;

/**
 * Unit test for ConcreteConcreteBreakdownElementDao
 * 
 * @author nanawel
 */
public class ConcreteBreakdownElementDaoTest {

    private ConcreteBreakdownElementDao concreteBreakdownElementDao = (ConcreteBreakdownElementDao) TestConfiguration
	    .getInstance().getApplicationContext().getBean("ConcreteBreakdownElementDao");

    private ConcreteBreakdownElement concreteBreakdownElement = null;

    public Set<Activity> superActivities = new HashSet<Activity>();

    @Before
    public void setUp() {

	// Creates empty BreakdownElement
	this.concreteBreakdownElement = new ConcreteBreakdownElement();
	this.concreteBreakdownElement.setConcreteName("My concrete name");
    }

    @After
    public void tearDown() {
	this.concreteBreakdownElement = null;
    }

    @Test
    public void testSaveOrUpdateConcreteBreakdownElement() {
	// Rk: the setUp method is called here.

	// Saves the activity with the method to test.
	this.concreteBreakdownElementDao.saveOrUpdateConcreteBreakdownElement(this.concreteBreakdownElement);

	// Checks the saving.
	String id = this.concreteBreakdownElement.getId();
	ConcreteBreakdownElement cbdeTmp = (ConcreteBreakdownElement) this.concreteBreakdownElementDao
		.getHibernateTemplate().load(ConcreteBreakdownElement.class, id);
	assertNotNull(cbdeTmp);

	// Delete the data stored in the database
	this.concreteBreakdownElementDao.deleteConcreteBreakdownElement(this.concreteBreakdownElement);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetAllConcreteBreakdownElements() {
	// Rk: the setUp method is called here.

	// Saves the activity into the database.
	this.concreteBreakdownElementDao.saveOrUpdateConcreteBreakdownElement(this.concreteBreakdownElement);

	// Looks if this bde is also into the database and looks if the size of
	// the set is >= 1.
	List<ConcreteBreakdownElement> cbdes = this.concreteBreakdownElementDao.getAllConcreteBreakdownElements();
	assertNotNull(cbdes);
	assertTrue(cbdes.size() >= 1);

	// Delete the data stored in the database
	this.concreteBreakdownElementDao.deleteConcreteBreakdownElement(this.concreteBreakdownElement);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetBreakdownElement() {
	// Rk: the setUp method is called here.

	// Saves the concreteBreakdownElement into the database.
	this.concreteBreakdownElementDao.saveOrUpdateConcreteBreakdownElement(this.concreteBreakdownElement);
	String id = this.concreteBreakdownElement.getId();

	// Tests the method getBreakdownElement with an existing
	// concreteBreakdownElement.
	ConcreteBreakdownElement cbdeTmp = this.concreteBreakdownElementDao.getConcreteBreakdownElement(id);
	assertNotNull(cbdeTmp);
	assertEquals("Name", cbdeTmp.getConcreteName(), this.concreteBreakdownElement.getConcreteName());

	// Tests the method getConcreteBreakdownElement with an unexisting
	// concreteBreakdownElement.
	this.concreteBreakdownElementDao.deleteConcreteBreakdownElement(this.concreteBreakdownElement);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testDeleteConcreteBreakdownElement() {
	// Rk: the setUp method is called here.

	// Saves the ConcreteBreakdownElement into the database.
	this.concreteBreakdownElementDao.saveOrUpdateConcreteBreakdownElement(this.concreteBreakdownElement);
	String id = this.concreteBreakdownElement.getId();

	// Tests the method deleteConcreteBreakdownElement with an
	// ConcreteBreakdownElement
	// existing into the db.
	this.concreteBreakdownElementDao.deleteConcreteBreakdownElement(this.concreteBreakdownElement);

	// Looks if this.concreteBreakdownElement is now absent in the db.
	ConcreteBreakdownElement cbdeTmp = (ConcreteBreakdownElement) this.concreteBreakdownElementDao
		.getConcreteBreakdownElement(id);
	assertNull(cbdeTmp);

	// Rk: the tearDown method is called here.
    }
}
