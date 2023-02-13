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
package wilos.test.hibernate.spem2.activity;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.spem2.activity.ActivityDao;
import wilos.model.spem2.activity.Activity;
import wilos.test.TestConfiguration;

public class ActivityDaoTest {

    private ActivityDao activityDao = (ActivityDao) TestConfiguration.getInstance().getApplicationContext().getBean(
	    "ActivityDao");

    private Activity activity = null;

    public static final String NAME = "thisActivity";

    public static final String DESCRIPTION = "activity description";

    public static final String PREFIX = "prefix";

    public static final Boolean IS_OPTIONAL = true;

    public static final Boolean HAS_MULTIPLE_OCCURENCES = true;

    public static final Boolean IS_EVEN_DRIVEN = true;

    public static final Boolean IS_ON_GOING = true;

    public static final Boolean IS_PLANNED = true;

    public static final Boolean IS_REPEATABLE = true;

    @Before
    public void setUp() {

	this.activity = new Activity();
	this.activity.setName(NAME);
	this.activity.setDescription(DESCRIPTION);
	this.activity.setPrefix(PREFIX);
	this.activity.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
	this.activity.setIsEvenDriven(IS_EVEN_DRIVEN);
	this.activity.setIsOngoing(IS_ON_GOING);
	this.activity.setIsOptional(IS_OPTIONAL);
	this.activity.setIsPlanned(IS_PLANNED);
	this.activity.setIsRepeatable(IS_REPEATABLE);
    }

    @After
    public void tearDown() {
	this.activity = null;
    }

    @Test
    public void testSaveOrUpdateActivity() {
	// Rk: the setUp method is called here.

	// Save the activity with the method to test.
	String id = this.activityDao.saveOrUpdateActivity(this.activity);

	Activity activityTmp = (Activity) this.activityDao.getActivity(id);
	assertNotNull(activityTmp);

	this.activityDao.deleteActivity(this.activity);
	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetAllActivities() {
	// Rk: the setUp method is called here.

	// Save the activity with the method to test.
	this.activityDao.saveOrUpdateActivity(this.activity);

	// Look if this activity is also into the database and look if the size
	// of the set is >= 1.
	List<Activity> activities = this.activityDao.getAllActivities();
	assertNotNull(activities);
	assertTrue(activities.size() >= 1);
	
	this.activityDao.deleteActivity(this.activity);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetActivity() {

	// Save the activity into the database.
	String id = this.activityDao.saveOrUpdateActivity(this.activity);

	// Test the method getActivity with an existing activity.
	Activity activityTmp = this.activityDao.getActivity(id);
	assertNotNull(activityTmp);
	assertEquals("Name", activityTmp.getName(), NAME);
	assertEquals("Description", activityTmp.getDescription(), DESCRIPTION);
	assertEquals("Prefix", activityTmp.getPrefix(), PREFIX);
	assertEquals("HasMultipleOccurences", activityTmp.getHasMultipleOccurrences(), HAS_MULTIPLE_OCCURENCES);
	assertEquals("IsEvenDriven", activityTmp.getIsEvenDriven(), IS_EVEN_DRIVEN);
	assertEquals("IsOnGoing", activityTmp.getIsOngoing(), IS_ON_GOING);
	assertEquals("IsOptional", activityTmp.getIsOptional(), IS_OPTIONAL);
	assertEquals("IsPlanned", activityTmp.getIsPlanned(), IS_PLANNED);
	assertEquals("IsRepeatale", activityTmp.getIsRepeatable(), IS_REPEATABLE);

	// Test the method getActivity with an unexisting activity.
	this.activityDao.deleteActivity(this.activity);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testDeleteActivity() {
	// Rk: the setUp method is called here.

	// Save the activity into the database.
	String id = this.activityDao.saveOrUpdateActivity(this.activity);

	// Test the method deleteActivity with an activity existing into the db.
	this.activityDao.deleteActivity(this.activity);

	// See if this.activity is now absent in the db.
	Activity activityTmp = (Activity) this.activityDao.getActivity(id);
	assertNull(activityTmp);

	// Rk: the tearDown method is called here.
    }
}
