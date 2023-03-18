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
package wilos.test.hibernate.spem2.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.spem2.task.TaskDefinitionDao;
import wilos.model.spem2.task.TaskDefinition;
import wilos.test.TestConfiguration;

/**
 * Unit test for TaskDefinitionDao
 * 
 * @author eperico
 * 
 */
public class TaskDefinitionDaoTest {

    private TaskDefinitionDao taskDefinitionDao = (TaskDefinitionDao) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("TaskDefinitionDao");;

    private TaskDefinition taskDefinition = null;

    /**
     * attributes from Element
     */
    public static final String ID = "thisId";

    public static final String NAME = "thisTask";

    public static final String DESCRIPTION = "taskDefinition description";

    @Before
    public void setUp() {

	// Create empty TaskDefinition
	this.taskDefinition = new TaskDefinition();
	this.taskDefinition.setName(NAME);
	this.taskDefinition.setDescription(DESCRIPTION);
    }

    @After
    public void tearDown() {
	this.taskDefinition = null;
    }

    @Test
    public void testSaveTaskDefinition() {
	// Rk: the setUp method is called here.

	// Save the taskDefinition with the method to test.
	String id = this.taskDefinitionDao.saveOrUpdateTaskDefinition(this.taskDefinition);

	// Check the saving.
	TaskDefinition taskTmp = (TaskDefinition) this.taskDefinitionDao.getHibernateTemplate().load(
		TaskDefinition.class, id);
	assertNotNull(taskTmp);

	this.taskDefinitionDao.deleteTaskDefinition(this.taskDefinition);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetAllTaskDefinitions() {
	// Rk: the setUp method is called here.

	// Save the taskDefinition into the database.
	this.taskDefinitionDao.getHibernateTemplate().saveOrUpdate(this.taskDefinition);

	// Look if this taskDefinition is also into the database and look if the
	// size of
	// the set is >= 1.
	List<TaskDefinition> taskDefinitions = this.taskDefinitionDao.getAllTaskDefinitions();
	assertNotNull(taskDefinitions);
	assertTrue(taskDefinitions.size() >= 1);

	this.taskDefinitionDao.deleteTaskDefinition(this.taskDefinition);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetTask() {
	// Rk: the setUp method is called here

	// Save the taskDefinition into the database.
	String id = this.taskDefinitionDao.saveOrUpdateTaskDefinition(this.taskDefinition);

	// Test the method getTask with an existing taskDefinition.
	TaskDefinition taskTmp = this.taskDefinitionDao.getTaskDefinition(id);
	assertNotNull(taskTmp);
	assertEquals("Name", taskTmp.getName(), NAME);
	assertEquals("Description", taskTmp.getDescription(), DESCRIPTION);

	// Test the method getTask with an unexisting taskDefinition.
	this.taskDefinitionDao.getHibernateTemplate().delete(taskDefinition);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testDeleteTask() {
	// Rk: the setUp method is called here.

	// Save the taskDefinition into the database.
	String id = this.taskDefinitionDao.saveOrUpdateTaskDefinition(this.taskDefinition);

	// Test the method deleteTask with an acitivity existing into the db.
	this.taskDefinitionDao.deleteTaskDefinition(this.taskDefinition);

	// See if this.task is now absent in the db.
	TaskDefinition taskTmp = (TaskDefinition) this.taskDefinitionDao.getHibernateTemplate().get(
		TaskDefinition.class, id);
	assertNull(taskTmp);

	// Rk: the tearDown method is called here.
    }
}
