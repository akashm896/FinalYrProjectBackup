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

package wilos.test.business.services.spem2.guide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.spem2.activity.ActivityService;
import wilos.business.services.spem2.guide.GuidanceService;
import wilos.hibernate.spem2.role.RoleDefinitionDao;
import wilos.hibernate.spem2.task.TaskDefinitionDao;
import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.guide.Guidance;
import wilos.model.spem2.role.RoleDefinition;
import wilos.model.spem2.task.TaskDefinition;
import wilos.test.TestConfiguration;

public class GuidanceServiceTest {

    private GuidanceService guidanceService = (GuidanceService) TestConfiguration.getInstance().getApplicationContext()
	    .getBean("GuidanceService");

    private ActivityService activityService = (ActivityService) TestConfiguration.getInstance().getApplicationContext()
	    .getBean("ActivityService");

    private TaskDefinitionDao taskDefinitionDao = (TaskDefinitionDao) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("TaskDefinitionDao");

    private RoleDefinitionDao roleDefinitionDao = (RoleDefinitionDao) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("RoleDefinitionDao");

    private Guidance guidance;

    private String guidanceId;

    private Activity activity;

    private RoleDefinition roleDefinition;

    private TaskDefinition taskDefinition;

    @Before
    public void setUp() {

	this.roleDefinition = new RoleDefinition();
	this.roleDefinition.setName("My name");
	this.roleDefinitionDao.saveOrUpdateRoleDefinition(this.roleDefinition);

	this.taskDefinition = new TaskDefinition();
	this.taskDefinition.setName("My name");
	this.taskDefinitionDao.saveOrUpdateTaskDefinition(this.taskDefinition);

	this.activity = new Activity();
	this.activity.setName("My name");
	this.activityService.saveActivity(this.activity);

	this.guidance = new Guidance();
	this.guidance.setGuid("my guid");
    }

    @After
    public void tearDown() {
	this.taskDefinitionDao.deleteTaskDefinition(this.taskDefinition);
	this.roleDefinitionDao.deleteRoleDefinition(this.roleDefinition);
	this.activityService.deleteActivity(this.activity);
	this.roleDefinition = null;
	this.taskDefinition = null;
	this.guidance = null;
    }

    @Test
    public void testStorageOfARoleDescriptorInATransaction() {

	this.guidance.addRoleDefinition(this.roleDefinition);
	this.guidance.addTaskDefinition(this.taskDefinition);
	this.guidance.addActivity(this.activity);
	this.guidanceId = this.guidanceService.saveGuidance(this.guidance);
	this.taskDefinitionDao.saveOrUpdateTaskDefinition(this.taskDefinition);
	this.roleDefinitionDao.saveOrUpdateRoleDefinition(this.roleDefinition);
	this.activityService.saveActivity(this.activity);

	Guidance retrievedRd = this.guidanceService.getGuidance(this.guidanceId);
	assertNotNull("not null", retrievedRd);

	Set<TaskDefinition> taskDefinitions = this.guidanceService.getTaskDefinitions(retrievedRd);
	assertEquals("size of taskDefinitions", 1, taskDefinitions.size());

	Set<RoleDefinition> roleDefinitions = this.guidanceService.getRoleDefinitions(retrievedRd);
	assertEquals("size of roleDefinitions", 1, roleDefinitions.size());
	
	Set<Activity> activities = this.guidanceService.getActivities(retrievedRd);
	assertEquals("size of activities", 1, activities.size());

	this.guidance.removeRoleDefinition(this.roleDefinition);
	this.guidance.removeTaskDefinition(this.taskDefinition);
	this.guidance.removeActivity(this.activity);

	this.taskDefinitionDao.saveOrUpdateTaskDefinition(this.taskDefinition);
	this.roleDefinitionDao.saveOrUpdateRoleDefinition(this.roleDefinition);
	this.activityService.saveActivity(this.activity);
	this.guidanceService.saveGuidance(this.guidance);

	this.guidanceService.deleteGuidance(this.guidance);
    }
    
    @Test
    public void testThatAllGuidancesAreRetrieved() {
	
	int nb = this.guidanceService.getAllGuidances().size();

	this.guidance.addRoleDefinition(this.roleDefinition);
	this.guidance.addTaskDefinition(this.taskDefinition);
	this.guidance.addActivity(this.activity);
	this.guidanceId = this.guidanceService.saveGuidance(this.guidance);
	this.taskDefinitionDao.saveOrUpdateTaskDefinition(this.taskDefinition);
	this.roleDefinitionDao.saveOrUpdateRoleDefinition(this.roleDefinition);
	this.activityService.saveActivity(this.activity);
	
	Guidance g1 = new Guidance();
	g1.setName("Your name");
	this.guidanceService.saveGuidance(g1);

	Guidance g2 = new Guidance();
	g2.setName("Its name");
	this.guidanceService.saveGuidance(g2);
	
	List<Guidance> guidances = this.guidanceService.getAllGuidances();
	assertNotNull("not null", guidances);
	assertEquals("number of guidances", 3, guidances.size() - nb);

	this.guidance.removeRoleDefinition(this.roleDefinition);
	this.guidance.removeTaskDefinition(this.taskDefinition);
	this.guidance.removeActivity(this.activity);

	this.taskDefinitionDao.saveOrUpdateTaskDefinition(this.taskDefinition);
	this.roleDefinitionDao.saveOrUpdateRoleDefinition(this.roleDefinition);
	this.activityService.saveActivity(this.activity);

	this.guidanceService.deleteGuidance(this.guidance);
    }

}
