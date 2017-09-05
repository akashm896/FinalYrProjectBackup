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

import wilos.hibernate.spem2.task.StepDao;
import wilos.model.spem2.task.Step;
import wilos.model.spem2.task.TaskDefinition;
import wilos.test.TestConfiguration;

/**
 * Test for DAO That manage Step objets
 * 
 * @author garwind
 * 
 */
public class StepDaoTest {

    private StepDao stepDao = (StepDao) TestConfiguration.getInstance().getApplicationContext().getBean("StepDao");;

    private Step step = null;

    /**
     * attributes from Element
     */
    public static final String ID = "thisId";

    public static final String NAME = "thisStep";

    public static final String DESCRIPTION = "step";

    @Before
    public void setUp() {

	// Create empty TaskDefinition
	this.step = new Step();
	this.step.setName(NAME);
	this.step.setDescription(DESCRIPTION);
    }

    @After
    public void tearDown() {
	this.step = null;
    }

    @Test
    public void testSaveOrUpdateStep() {
	// Rk: the setUp method is called here.

	// Save the task with the method to test.
	String id = this.stepDao.saveOrUpdateStep(this.step);

	// Check the saving.
	TaskDefinition taskTmp = (TaskDefinition) this.stepDao.getHibernateTemplate().load(TaskDefinition.class, id);
	assertNotNull(taskTmp);

	this.stepDao.deleteStep(this.step);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetAllSteps() {
	// Rk: the setUp method is called here.

	// Save the task into the database.
	this.stepDao.getHibernateTemplate().saveOrUpdate(this.step);

	// Look if this task is also into the database and look if the size of
	// the set is >= 1.
	List<Step> steps = this.stepDao.getAllSteps();
	assertNotNull(steps);
	assertTrue(steps.size() >= 1);

	this.stepDao.deleteStep(this.step);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetStep() {
	// Rk: the setUp method is called here.

	// Save the task into the database.
	String id = this.stepDao.saveOrUpdateStep(this.step);

	// Test the method getTask with an existing task.
	Step sectionTmp = this.stepDao.getStep(id);
	assertNotNull(sectionTmp);
	assertEquals("Name", sectionTmp.getName(), NAME);
	assertEquals("Description", sectionTmp.getDescription(), DESCRIPTION);

	// Test the method getTask with an unexisting task.
	this.stepDao.getHibernateTemplate().delete(step);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testDeleteTask() {
	// Rk: the setUp method is called here.

	// Save the task into the database.
	String id = this.stepDao.saveOrUpdateStep(this.step);

	// Test the method deleteTask with an acitivity existing into the db.
	this.stepDao.deleteStep(this.step);

	// See if the step is now absent in the db.
	Step stepTmp = (Step) this.stepDao.getStep(id);
	assertNull(stepTmp);

	// Rk: the tearDown method is called here.
    }
}
