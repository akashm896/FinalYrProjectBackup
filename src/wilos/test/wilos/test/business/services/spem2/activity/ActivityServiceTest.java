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
package wilos.test.business.services.spem2.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.spem2.activity.ActivityService;
import wilos.hibernate.misc.concreteactivity.ConcreteActivityDao;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.breakdownelement.BreakdownElement;
import wilos.test.TestConfiguration;

public class ActivityServiceTest {

    private ActivityService activityService = (ActivityService) TestConfiguration.getInstance().getApplicationContext()
	    .getBean("ActivityService");

    private ConcreteActivityDao concreteActivityDao = (ConcreteActivityDao) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("ConcreteActivityDao");

    private Activity activity;

    private Activity bde;

    public static final String PREFIX = "prefix";

    public static final Boolean IS_OPTIONAL = true;

    private String activityId;

    @Before
    public void setUp() {

	this.bde = new Activity();
	this.bde.setName("My bde name");
	
	this.activityService.saveActivity(this.bde);
	
	this.activity = new Activity();
	this.activity.setPrefix(PREFIX);
	this.activity.setIsOptional(IS_OPTIONAL);
	this.activity.addBreakdownElement(this.bde);
    }

    @After
    public void tearDown() {
	this.activityService.deleteActivity(this.bde);
	this.bde = null;
	this.activity = null;
    }

    @Test
    public void testStorageOfAnActivityInATransaction() {

	this.activityId = this.activityService.saveActivity(this.activity);
	this.activityService.saveActivity(this.bde);

	Activity retrievedAct = this.activityService.getActivity(this.activityId);
	assertNotNull("not null", retrievedAct);

	SortedSet<BreakdownElement> bdes = this.activityService.getAllBreakdownElements(retrievedAct);
	assertEquals("equal bdes", this.activity.getBreakdownElements(), bdes);
	
	this.activity.removeBreakdownElement(this.bde);
	this.activityService.saveActivity(this.bde);

	this.activityService.deleteActivity(this.activity);
    }

    @Test
    public void testThatAllActivitiesAreRetrieved() {

	int nb = this.activityService.getAllActivities().size();

	this.activityId = this.activityService.saveActivity(this.activity);
	this.activityService.saveActivity(this.bde);

	Activity act1 = new Activity();
	act1.setName("Your name");
	act1.setDescription("Your description");

	this.activityService.saveActivity(act1);
	
	Activity act2 = new Activity();
	act2.setName("Its name");
	act2.setDescription("Its description");

	this.activityService.saveActivity(act2);

	List<Activity> activities = this.activityService.getAllActivities();

	assertNotNull("not null", activities);
	assertEquals("number of activities", 3, activities.size() - nb);

	this.activityService.deleteActivity(act2);
	this.activityService.deleteActivity(act1);
	
	this.activity.removeBreakdownElement(this.bde);
	this.activityService.saveActivity(this.bde);
	this.activityService.deleteActivity(this.activity);
    }

    @Test
    public void testGetAllConcreteActivities() {
	// Rk: the setUp method is called here.
	
	this.activityService.saveActivity(this.activity);
	
	ConcreteActivity cact = new ConcreteActivity();
	cact.setConcreteName("cname");

	this.activity.addConcreteActivity(cact);
	this.concreteActivityDao.saveOrUpdateConcreteActivity(cact);
	this.activityService.saveActivity(this.activity);
	
	// Look if this activity is also into the database.
	Set<ConcreteActivity> activityTmp = this.activityService.getAllConcreteActivities(this.activity);
	assertNotNull(activityTmp);
	assertEquals(activityTmp.size(), 1);
	
	this.concreteActivityDao.deleteConcreteActivity(cact);
	this.activityService.deleteActivity(this.activity);

	// Rk: the tearDown method is called here.
    }

}
