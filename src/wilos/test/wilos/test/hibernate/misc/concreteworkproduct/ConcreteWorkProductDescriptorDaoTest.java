/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Sebastien BALARD <sbalard@wilos-project.org>
 * Copyright (C) 2007-2008 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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
package wilos.test.hibernate.misc.concreteworkproduct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.misc.concreteworkproduct.ConcreteWorkProductDescriptorDao;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;
import wilos.test.TestConfiguration;

/**
 * 
 * 
 * 
 */
public class ConcreteWorkProductDescriptorDaoTest {

    private ConcreteWorkProductDescriptorDao concreteWorkProductDescriptorDao = (ConcreteWorkProductDescriptorDao) TestConfiguration
	    .getInstance().getApplicationContext().getBean(
		    "ConcreteWorkProductDescriptorDao");

    private ConcreteWorkProductDescriptor concreteWorkProductDescriptor = null;

    /**
     * attributes from Project
     */

    public static final String PROJECT_NAME = "projectname";

    public static final Date CREATION_DATE = new Date();

    public static final Date LAUNCHING_DATE = new Date();

    public static final boolean IS_FINISHED = false;

    /**
     * attributes from Participant
     */

    public static final String PARTICIPANT_NAME = "participantname";

    public static final String PARTICIPANT_FIRSTNAME = "participantfirstname";

    public static final String EMAIL = "email";

    public static final String LOGIN = "login";

    public static final String PASSWORD = "password";

    @Before
    public void setUp() {

	// Create empty TaskDescriptor
	this.concreteWorkProductDescriptor = new ConcreteWorkProductDescriptor();
	this.concreteWorkProductDescriptor.setConcreteName("My concrete name");
    }

    @After
    public void tearDown() {

	this.concreteWorkProductDescriptor = null;
    }

    @Test
    public void testSaveOrUpdateConcreteWorkProductDescriptor() {
	// Rk: the setUp method is called here.

	// Save the workProductDescriptor with the method to test.
	String id = this.concreteWorkProductDescriptorDao
		.saveOrUpdateConcreteWorkProductDescriptor(this.concreteWorkProductDescriptor);

	ConcreteWorkProductDescriptor workProductDescriptorTmp = (ConcreteWorkProductDescriptor) this.concreteWorkProductDescriptorDao
		.getHibernateTemplate().get(
			ConcreteWorkProductDescriptor.class, id);
	assertNotNull(workProductDescriptorTmp);
	assertEquals(workProductDescriptorTmp.getConcreteName(),
		this.concreteWorkProductDescriptor.getConcreteName());

	// Delete the data stored in the database
	this.concreteWorkProductDescriptorDao
		.deleteConcreteWorkProductDescriptor(this.concreteWorkProductDescriptor);
	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetConcreteWorkProductDescriptor() {

	// Saves the concreteBreakdownElement into the database.
	this.concreteWorkProductDescriptorDao
		.saveOrUpdateConcreteWorkProductDescriptor(this.concreteWorkProductDescriptor);
	String id = this.concreteWorkProductDescriptor.getId();

	// Tests the method getBreakdownElement with an existing
	// concreteBreakdownElement.
	ConcreteBreakdownElement cbdeTmp = this.concreteWorkProductDescriptorDao
		.getConcreteWorkProductDescriptor(id);
	assertNotNull(cbdeTmp);
	assertEquals("concreteName", cbdeTmp.getConcreteName(),
		this.concreteWorkProductDescriptor.getConcreteName());

	// Delete the data stored in the database
	this.concreteWorkProductDescriptorDao
		.deleteConcreteWorkProductDescriptor(this.concreteWorkProductDescriptor);
	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetAllConcreteWorkProductDescriptors() {
	// Rk: the setUp method is called here.

	// Save the workProductDescriptor into the database.
	this.concreteWorkProductDescriptorDao.getHibernateTemplate()
		.saveOrUpdate(this.concreteWorkProductDescriptor);

	// Look if this workProductDescriptor is also into the database and look
	// if the
	// size of the set is >= 1.
	List<ConcreteWorkProductDescriptor> concreteWorkProductDescriptors = this.concreteWorkProductDescriptorDao
		.getAllConcreteWorkProductDescriptors();
	assertNotNull(concreteWorkProductDescriptors);
	assertTrue(concreteWorkProductDescriptors.size() >= 1);

	// Delete the data stored in the database
	this.concreteWorkProductDescriptorDao
		.deleteConcreteWorkProductDescriptor(this.concreteWorkProductDescriptor);
	// Rk: the tearDown method is called here.
    }

    @Test
    public void testDeleteConcreteWorkProductDescriptor() {
	// Rk: the setUp method is called here.

	// Save the taskDescriptor into the database.
	this.concreteWorkProductDescriptorDao.getHibernateTemplate()
		.saveOrUpdate(this.concreteWorkProductDescriptor);
	String id = this.concreteWorkProductDescriptor.getId();

	// Test the method deleteTaskDescriptor with an acitivity existing into
	// the db.
	this.concreteWorkProductDescriptorDao
		.deleteConcreteWorkProductDescriptor(this.concreteWorkProductDescriptor);

	// See if this.taskDescriptor is now absent in the db.
	ConcreteWorkProductDescriptor concreteWorkProductDescriptorTmp = (ConcreteWorkProductDescriptor) this.concreteWorkProductDescriptorDao
		.getHibernateTemplate().get(
			ConcreteWorkProductDescriptor.class, id);
	assertNull(concreteWorkProductDescriptorTmp);

	
	// Rk: the tearDown method is called here.
    }
}
