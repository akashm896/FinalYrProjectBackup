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
package wilos.test.hibernate.spem2.breakdownelement;

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

import wilos.hibernate.spem2.breakdownelement.BreakdownElementDao;
import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.breakdownelement.BreakdownElement;
import wilos.test.TestConfiguration;

/**
 * Unit test for BreakdownElementDao
 * 
 * @author deder
 */
public class BreakdownElementDaoTest {

    private BreakdownElementDao breakdownElementDao = (BreakdownElementDao) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("BreakdownElementDao");;

    private BreakdownElement breakdownElement = null;

    public static final String NAME = "thisBde";

    public static final String DESCRIPTION = "bde description";

    public static final String PREFIX = "prefix";

    public static final Boolean IS_OPTIONAL = true;

    public static final Boolean HAS_MULTIPLE_OCCURENCES = true;

    public static final Boolean IS_PLANNED = true;

    public Set<Activity> superActivities = new HashSet<Activity>();

    @Before
    public void setUp() {

	// Create empty BreakdownElement
	this.breakdownElement = new BreakdownElement();
	this.breakdownElement.setName(NAME);
	this.breakdownElement.setDescription(DESCRIPTION);
	this.breakdownElement.setPrefix(PREFIX);
	this.breakdownElement.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
	this.breakdownElement.setIsOptional(IS_OPTIONAL);
	this.breakdownElement.setIsPlanned(IS_PLANNED);
    }

    @After
    public void tearDown() {
	this.breakdownElement = null;
    }

    @Test
    public void testSaveOrUpdateBreakdownElement() {
	// Rk: the setUp method is called here.

	// Save the activity with the method to test.
	this.breakdownElementDao.saveOrUpdateBreakdownElement(this.breakdownElement);

	// Check the saving.
	String id = this.breakdownElement.getId();
	BreakdownElement bdeTmp = (BreakdownElement) this.breakdownElementDao.getHibernateTemplate().load(
		BreakdownElement.class, id);
	assertNotNull(bdeTmp);
	
	this.breakdownElementDao.deleteBreakdownElement(this.breakdownElement);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetAllBreakdownElements() {
	// Rk: the setUp method is called here.

	// Save the activity into the database.
	this.breakdownElementDao.saveOrUpdateBreakdownElement(this.breakdownElement);

	// Look if this bde is also into the database and look if the size of
	// the set is >= 1.
	List<BreakdownElement> bdes = this.breakdownElementDao.getAllBreakdownElements();
	assertNotNull(bdes);
	assertTrue(bdes.size() >= 1);
	
	this.breakdownElementDao.deleteBreakdownElement(this.breakdownElement);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetBreakdownElement() {

	// Save the breakdownElement into the database.
	String id = this.breakdownElementDao.saveOrUpdateBreakdownElement(this.breakdownElement);

	// Test the method getBreakdownElement with an existing
	// breakdownElement.
	BreakdownElement bdeTmp = this.breakdownElementDao.getBreakdownElement(id);
	assertNotNull(bdeTmp);
	assertEquals("Name", bdeTmp.getName(), NAME);
	assertEquals("Description", bdeTmp.getDescription(), DESCRIPTION);
	assertEquals("Prefix", bdeTmp.getPrefix(), PREFIX);
	assertEquals("HasMultipleOccurences", bdeTmp.getHasMultipleOccurrences(), HAS_MULTIPLE_OCCURENCES);
	assertEquals("IsOptional", bdeTmp.getIsOptional(), IS_OPTIONAL);
	assertEquals("IsPlanned", bdeTmp.getIsPlanned(), IS_PLANNED);

	// Test the method getBreakdownElement with an unexisting
	// breakdownElement.
	this.breakdownElementDao.deleteBreakdownElement(this.breakdownElement);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testDeleteBreakdownElement() {
	// Rk: the setUp method is called here.

	// Save the BreakdownElement into the database.
	String id = this.breakdownElementDao.saveOrUpdateBreakdownElement(this.breakdownElement);

	// Test the method deleteBreakdownElement with an BreakdownElement
	// existing into the db.
	this.breakdownElementDao.deleteBreakdownElement(this.breakdownElement);

	// See if this.breakdownElement is now absent in the db.
	BreakdownElement bdeTmp = (BreakdownElement) this.breakdownElementDao.getHibernateTemplate().get(
		BreakdownElement.class, id);
	assertNull(bdeTmp);

	// Rk: the tearDown method is called here.
    }
}
