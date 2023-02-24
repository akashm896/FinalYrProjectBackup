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

import wilos.hibernate.misc.concretetask.ConcreteTaskDescriptorDao;
import wilos.hibernate.spem2.task.TaskDescriptorDao;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.test.TestConfiguration;

/**
 * Unit test for TaskDescriptorDao
 *
 * @author eperico
 *
 */
public class TaskDescriptorDaoTest {

    private TaskDescriptorDao taskDescriptorDao = (TaskDescriptorDao) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("TaskDescriptorDao");

    private ConcreteTaskDescriptorDao concreteTaskDescriptorDao = (ConcreteTaskDescriptorDao) TestConfiguration
	    .getInstance().getApplicationContext().getBean("ConcreteTaskDescriptorDao");

    private TaskDescriptor taskDescriptor = null;

    /**
     * attributes from Element
     */
    public static final String ID = "thisId";

    public static final String NAME = "thisTaskDescriptor";

    public static final String DESCRIPTION = "taskDescriptor description";

    /**
     * attributes from BreakdownElement
     */
    public static final String PREFIX = "prefix";

    public static final Boolean IS_PLANNED = true;

    public static final Boolean HAS_MULTIPLE_OCCURENCES = true;

    public static final Boolean IS_OPTIONAL = true;

    /**
     * attributes from WorkBreakdownElement
     */
    public static final Boolean IS_REPEATABLE = true;

    public static final Boolean IS_ON_GOING = true;

    public static final Boolean IS_EVEN_DRIVEN = true;

    @Before
    public void setUp() {

	// Create empty TaskDescriptor
	this.taskDescriptor = new TaskDescriptor();
	this.taskDescriptor.setName(NAME);
	this.taskDescriptor.setDescription(DESCRIPTION);

	this.taskDescriptor.setPrefix(PREFIX);
	this.taskDescriptor.setIsPlanned(IS_PLANNED);
	this.taskDescriptor.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
	this.taskDescriptor.setIsOptional(IS_OPTIONAL);

	this.taskDescriptor.setIsRepeatable(IS_REPEATABLE);
	this.taskDescriptor.setIsOngoing(IS_ON_GOING);
	this.taskDescriptor.setIsEvenDriven(IS_EVEN_DRIVEN);
    }

    @After
    public void tearDown() {
	this.taskDescriptor = null;
    }

    @Test
    public void testSaveOrUpdateTaskDescriptor() {
	// Rk: the setUp method is called here.

	// Save the taskDescriptor with the method to test.
	String id = this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.taskDescriptor);

	// Check the saving.
	TaskDescriptor taskDescriptorTmp = (TaskDescriptor) this.taskDescriptorDao.getHibernateTemplate().load(
		TaskDescriptor.class, id);
	assertNotNull(taskDescriptorTmp);
	
	this.taskDescriptorDao.deleteTaskDescriptor(this.taskDescriptor);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetAllTaskDescriptors() {
	// Rk: the setUp method is called here.

	// Save the taskDescriptor into the database.
	this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.taskDescriptor);

	// Look if this taskDescriptor is also into the database and look if the
	// size of the set is >= 1.
	List<TaskDescriptor> taskDescriptors = this.taskDescriptorDao.getAllTaskDescriptors();
	assertNotNull(taskDescriptors);
	assertTrue(taskDescriptors.size() >= 1);
	
	this.taskDescriptorDao.deleteTaskDescriptor(this.taskDescriptor);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetTaskDescriptor() {
	// Rk: the setUp method is called here.
	
	// Save the taskDescriptor into the database.
	String id = this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.taskDescriptor);

	ConcreteTaskDescriptor concreteTaskDescriptor = new ConcreteTaskDescriptor();
	concreteTaskDescriptor.setConcreteName("My name");
	
	this.concreteTaskDescriptorDao.saveOrUpdateConcreteTaskDescriptor(concreteTaskDescriptor);

	this.taskDescriptor.addConcreteTaskDescriptor(concreteTaskDescriptor);
	this.concreteTaskDescriptorDao.saveOrUpdateConcreteTaskDescriptor(concreteTaskDescriptor);
	
	this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.taskDescriptor);

	// Test the method getTaskDescriptor with an existing taskDescriptor.
	TaskDescriptor taskDescriptorTmp = this.taskDescriptorDao.getTaskDescriptor(id);
	assertNotNull(taskDescriptorTmp);
	assertEquals("Name", taskDescriptorTmp.getName(), NAME);
	assertEquals("Description", taskDescriptorTmp.getDescription(), DESCRIPTION);
	assertEquals("Prefix", taskDescriptorTmp.getPrefix(), PREFIX);
	assertEquals("IsPlanned", taskDescriptorTmp.getIsPlanned(), IS_PLANNED);
	assertEquals("HasMultipleOccurences", taskDescriptorTmp.getHasMultipleOccurrences(), HAS_MULTIPLE_OCCURENCES);
	assertEquals("IsOptional", taskDescriptorTmp.getIsOptional(), IS_OPTIONAL);
	assertEquals("IsRepeatable", taskDescriptorTmp.getIsRepeatable(), IS_REPEATABLE);
	assertEquals("IsOnGoing", taskDescriptorTmp.getIsOngoing(), IS_ON_GOING);
	assertEquals("IsEvenDriven", taskDescriptorTmp.getIsEvenDriven(), IS_EVEN_DRIVEN);

	// clean temporary data
	this.concreteTaskDescriptorDao.deleteConcreteTaskDescriptor(concreteTaskDescriptor);
	this.taskDescriptorDao.deleteTaskDescriptor(this.taskDescriptor);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testDeleteTaskDescriptor() {
	// Rk: the setUp method is called here.

	// Save the taskDescriptor into the database.
	String id = this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.taskDescriptor);

	// Test the method deleteTaskDescriptor with an activity existing into
	// the db.
	this.taskDescriptorDao.deleteTaskDescriptor(this.taskDescriptor);

	// See if this.taskDescriptor is now absent in the db.
	TaskDescriptor taskDescriptorTmp = (TaskDescriptor) this.taskDescriptorDao.getHibernateTemplate().get(
		TaskDescriptor.class, id);
	assertNull(taskDescriptorTmp);

	// Rk: the tearDown method is called here.
    }
}
