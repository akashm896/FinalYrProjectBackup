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
package wilos.test.hibernate.misc.concreterole;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.misc.concreterole.ConcreteRoleDescriptorDao;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.test.TestConfiguration;

/**
 * 
 * @author Almiriad
 * 
 */
public class ConcreteRoleDescriptorDaoTest {

    private ConcreteRoleDescriptorDao concreteRoleDescriptorDao = (ConcreteRoleDescriptorDao) TestConfiguration.getInstance()
	.getApplicationContext().getBean("ConcreteRoleDescriptorDao");

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

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testSaveOrUpdateConcreteRoleDescriptor() {
	// Rk: the setUp method is called here.
	ConcreteRoleDescriptor concreteRoleDescriptor = new ConcreteRoleDescriptor();
	concreteRoleDescriptor.setConcreteName("My concrete name");
	// Save the roleDescriptor with the method to test.
	String id = this.concreteRoleDescriptorDao.saveOrUpdateConcreteRoleDescriptor(concreteRoleDescriptor);

	ConcreteRoleDescriptor roleDescriptorTmp = (ConcreteRoleDescriptor) this.concreteRoleDescriptorDao
		.getHibernateTemplate().get(ConcreteRoleDescriptor.class, id);
	assertNotNull(roleDescriptorTmp);
	assertEquals(roleDescriptorTmp.getConcreteName(), concreteRoleDescriptor.getConcreteName());

	// Delete the data stored in the database
	this.concreteRoleDescriptorDao.deleteConcreteRoleDescriptor(concreteRoleDescriptor);
	// Rk: the tearDown method is called here.
    }
    
    @Test
    public void testGetConcreteRoleDescriptor() {

	ConcreteRoleDescriptor concreteRoleDescriptor = new ConcreteRoleDescriptor();
	concreteRoleDescriptor.setConcreteName("My concrete name");
	
	// Saves the concreteBreakdownElement into the database.
	this.concreteRoleDescriptorDao.saveOrUpdateConcreteRoleDescriptor(concreteRoleDescriptor);
	String id = concreteRoleDescriptor.getId();

	// Tests the method getBreakdownElement with an existing
	// concreteBreakdownElement.
	ConcreteBreakdownElement cbdeTmp = this.concreteRoleDescriptorDao.getConcreteRoleDescriptor(id);
	assertNotNull(cbdeTmp);
	assertEquals("concreteName", cbdeTmp.getConcreteName(), concreteRoleDescriptor.getConcreteName());

	// Delete the data stored in the database
	this.concreteRoleDescriptorDao.deleteConcreteRoleDescriptor(concreteRoleDescriptor);
	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetAllConcreteRoleDescriptors() {
	// Rk: the setUp method is called here.

	ConcreteRoleDescriptor concreteRoleDescriptor = new ConcreteRoleDescriptor();
	concreteRoleDescriptor.setConcreteName("My concrete name");
	
	// Save the roleDescriptor into the database.
	this.concreteRoleDescriptorDao.getHibernateTemplate().saveOrUpdate(concreteRoleDescriptor);

	// Look if this roleDescriptor is also into the database and look if the
	// size of the set is >= 1.
	List<ConcreteRoleDescriptor> concreteRoleDescriptors = this.concreteRoleDescriptorDao
		.getAllConcreteRoleDescriptors();
	assertNotNull(concreteRoleDescriptors);
	assertTrue(concreteRoleDescriptors.size() >= 1);

	// Delete the data stored in the database
	this.concreteRoleDescriptorDao.deleteConcreteRoleDescriptor(concreteRoleDescriptor);
	// Rk: the tearDown method is called here.
    }

    @Test
    public void testDeleteConcreteRoleDescriptor() {
	// Rk: the setUp method is called here.
	ConcreteRoleDescriptor concreteRoleDescriptor = new ConcreteRoleDescriptor();
	concreteRoleDescriptor.setConcreteName("My concrete name");
	// Save the taskDescriptor into the database.
	this.concreteRoleDescriptorDao.getHibernateTemplate().saveOrUpdate(concreteRoleDescriptor);
	String id = concreteRoleDescriptor.getId();

	// Test the method deleteTaskDescriptor with an acitivity existing into
	// the db.
	this.concreteRoleDescriptorDao.deleteConcreteRoleDescriptor(concreteRoleDescriptor);

	// See if this.taskDescriptor is now absent in the db.
	ConcreteRoleDescriptor concreteRoleDescriptorTmp = (ConcreteRoleDescriptor) this.concreteRoleDescriptorDao
		.getHibernateTemplate().get(ConcreteRoleDescriptor.class, id);
	assertNull(concreteRoleDescriptorTmp);
    }
}
