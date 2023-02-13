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
package wilos.test.hibernate.misc.concretetaskdescriptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import wilos.hibernate.misc.concretetask.ConcreteTaskDescriptorDao;
import wilos.hibernate.spem2.task.TaskDescriptorDao;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.test.TestConfiguration;
import wilos.utils.Constantes;

/**
 * 
 * @author Sebastien
 * 
 * 
 */
public class ConcreteTaskDescriptorDaoTest {

    private ConcreteTaskDescriptorDao concreteTaskDescriptorDao = (ConcreteTaskDescriptorDao) TestConfiguration
	    .getInstance().getApplicationContext().getBean("ConcreteTaskDescriptorDao");

    private ConcreteTaskDescriptor concreteTaskDescriptor = null;

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

    /**
     * attributes from ConcreteTaskDescriptor
     */
    public static final String CONCRETENAME = "concreteName";

    public static final float PLANNEDTIME = 15.5f;

    public static final float REMAININGTIME = 14.8f;

    public static final float ACCOMPLISHEDTIME = 4.7f;

    Date date = null;

    /**
     * 
     *
     */
    public ConcreteTaskDescriptorDaoTest() {
	try {
	    date = Constantes.DATE_FORMAT.parse("18/01/2007 10:00");
	} catch (ParseException e) {
	    e.printStackTrace();
	}
    }

    @Before
    public void setUp() {

	// Create empty TaskDescriptor
	this.concreteTaskDescriptor = new ConcreteTaskDescriptor();
	this.concreteTaskDescriptor.setConcreteName(CONCRETENAME);
	this.concreteTaskDescriptor.setPlannedTime(PLANNEDTIME);
	this.concreteTaskDescriptor.setPlannedFinishingDate(date);
    }

    @After
    public void tearDown() {
	this.concreteTaskDescriptor = null;
    }

    @Test
    public void testSaveOrUpdateConcreteTaskDescriptor() {
	// Rk: the setUp method is called here.

	// Save the taskDescriptor with the method to test.
	this.concreteTaskDescriptorDao.saveOrUpdateConcreteTaskDescriptor(this.concreteTaskDescriptor);

	// Check the saving.
	String id = concreteTaskDescriptor.getId();
	ConcreteTaskDescriptor taskDescriptorTmp = (ConcreteTaskDescriptor) this.concreteTaskDescriptorDao
		.getHibernateTemplate().load(ConcreteTaskDescriptor.class, id);
	assertNotNull(taskDescriptorTmp);

	// Delete the data stored in the database
	this.concreteTaskDescriptorDao.deleteConcreteTaskDescriptor(this.concreteTaskDescriptor);
	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetAllConcreteTaskDescriptors() {
	// Rk: the setUp method is called here.

	// Save the taskDescriptor into the database.
	this.concreteTaskDescriptorDao.saveOrUpdateConcreteTaskDescriptor(this.concreteTaskDescriptor);

	// Look if this taskDescriptor is also into the database and look if the
	// size of the set is >= 1.
	List<ConcreteTaskDescriptor> concretetaskDescriptors = this.concreteTaskDescriptorDao
		.getAllConcreteTaskDescriptors();
	assertNotNull(concretetaskDescriptors);
	assertTrue(concretetaskDescriptors.size() >= 1);

	// Delete the data stored in the database
	this.concreteTaskDescriptorDao.deleteConcreteTaskDescriptor(this.concreteTaskDescriptor);
	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetConcreteTaskDescriptor() {

	ApplicationContext ctx = TestConfiguration.getInstance().getApplicationContext();
	TaskDescriptorDao taskDescriptorDao = (TaskDescriptorDao) ctx.getBean("TaskDescriptorDao");

	TaskDescriptor taskDescriptor = new TaskDescriptor();
	// Rk: the setUp method is called here.

	// Add prooperties to the taskDescriptor.
	taskDescriptor = new TaskDescriptor();
	taskDescriptor.setName(NAME);
	taskDescriptor.setDescription(DESCRIPTION);

	taskDescriptor.setPrefix(PREFIX);
	taskDescriptor.setIsPlanned(IS_PLANNED);
	taskDescriptor.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
	taskDescriptor.setIsOptional(IS_OPTIONAL);

	taskDescriptor.setIsRepeatable(IS_REPEATABLE);
	taskDescriptor.setIsOngoing(IS_ON_GOING);
	taskDescriptor.setIsEvenDriven(IS_EVEN_DRIVEN);

	taskDescriptorDao.saveOrUpdateTaskDescriptor(taskDescriptor);
	String id_taskDescriptor = taskDescriptor.getId();
	TaskDescriptor taskDescriptorTmp = taskDescriptorDao.getTaskDescriptor(id_taskDescriptor);

	this.concreteTaskDescriptor.setTaskDescriptor(taskDescriptorTmp);

	// Save the taskDescriptor into the database.
	this.concreteTaskDescriptorDao.saveOrUpdateConcreteTaskDescriptor(this.concreteTaskDescriptor);
	String id = this.concreteTaskDescriptor.getId();

	// Test the method getTaskDescriptor with an existing taskDescriptor.
	ConcreteTaskDescriptor concreteTaskDescriptorTmp = this.concreteTaskDescriptorDao.getConcreteTaskDescriptor(id);
	assertNotNull(concreteTaskDescriptorTmp);
	assertEquals("ConcreteName", concreteTaskDescriptorTmp.getConcreteName(), CONCRETENAME);
	assertEquals("PlannedTime", concreteTaskDescriptorTmp.getPlannedTime(), PLANNEDTIME);
	assertEquals("TaskDescriptor", concreteTaskDescriptorTmp.getTaskDescriptor().getId(), id_taskDescriptor);

	// Test the method getTaskDescriptor with an unexisting taskDescriptor.
	this.concreteTaskDescriptorDao.deleteConcreteTaskDescriptor(this.concreteTaskDescriptor);
	concreteTaskDescriptorTmp = this.concreteTaskDescriptorDao.getConcreteTaskDescriptor(id);
	assertNull(concreteTaskDescriptorTmp);

	taskDescriptorDao.deleteTaskDescriptor(taskDescriptorTmp);
	// Rk: the tearDown method is called here.
    }

    @Test
    public void testDeleteConcreteTaskDescriptor() {
	// Rk: the setUp method is called here.

	// Save the taskDescriptor into the database.
	this.concreteTaskDescriptorDao.saveOrUpdateConcreteTaskDescriptor(this.concreteTaskDescriptor);
	String id = this.concreteTaskDescriptor.getId();

	// Test the method deleteTaskDescriptor with an acitivity existing into
	// the db.
	this.concreteTaskDescriptorDao.deleteConcreteTaskDescriptor(this.concreteTaskDescriptor);

	// See if this.taskDescriptor is now absent in the db.
	ConcreteTaskDescriptor concreteTaskDescriptorTmp = (ConcreteTaskDescriptor) this.concreteTaskDescriptorDao
		.getConcreteTaskDescriptor(id);
	assertNull(concreteTaskDescriptorTmp);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetAllConcreteTaskDescriptorsForProject() {
	// Rk: the setUp method is called here.

	// Save the taskDescriptor into the database.
	this.concreteTaskDescriptorDao.getHibernateTemplate().saveOrUpdate(this.concreteTaskDescriptor);
	String id = this.concreteTaskDescriptor.getId();

	// Test the method deleteTaskDescriptor with an acitivity existing into
	// the db.
	this.concreteTaskDescriptorDao.deleteConcreteTaskDescriptor(this.concreteTaskDescriptor);

	// See if this.taskDescriptor is now absent in the db.
	ConcreteTaskDescriptor concreteTaskDescriptorTmp = (ConcreteTaskDescriptor) this.concreteTaskDescriptorDao
		.getConcreteTaskDescriptor(id);
	assertNull(concreteTaskDescriptorTmp);

	// Rk: the tearDown method is called here.
    }

}
