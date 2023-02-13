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
package wilos.test.hibernate.spem2.role;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.spem2.role.RoleDescriptorDao;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.test.TestConfiguration;

/**
 * Unit test for RoleDescriptorDao
 * 
 * @author Soosuske.
 */
public class RoleDescriptorDaoTest {

    private RoleDescriptorDao roleDescriptorDao = (RoleDescriptorDao) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("RoleDescriptorDao");

    private RoleDescriptor roleDescriptor = null;

    public static final String NAME = "thisRoleDescriptor";

    public static final String DESCRIPTION = "roleDescriptor description";

    public static final String PREFIX = "prefix";

    public static final Boolean IS_OPTIONAL = true;

    public static final Boolean HAS_MULTIPLE_OCCURENCES = true;

    public static final Boolean IS_PLANNED = true;

    @Before
    public void setUp() {

	// Create empty roleDescriptor
	this.roleDescriptor = new RoleDescriptor();
	this.roleDescriptor.setName(NAME);
	this.roleDescriptor.setDescription(DESCRIPTION);
	this.roleDescriptor.setPrefix(PREFIX);
	this.roleDescriptor.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
	this.roleDescriptor.setIsOptional(IS_OPTIONAL);
	this.roleDescriptor.setIsPlanned(IS_PLANNED);
    }

    @After
    public void tearDown() {

	this.roleDescriptor = null;
    }

    @Test
    public void testSaveOrUpdateRoleDescriptor() {
	// Rk: the setUp method is called here.

	// Save the roleDescriptor with the method to test.
	String id = this.roleDescriptorDao.saveOrUpdateRoleDescriptor(this.roleDescriptor);

	RoleDescriptor roleDescriptorTmp = (RoleDescriptor) this.roleDescriptorDao.getHibernateTemplate().load(
		RoleDescriptor.class, id);
	assertNotNull(roleDescriptorTmp);

	this.roleDescriptorDao.deleteRoleDescriptor(this.roleDescriptor);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetAllRoleDescriptor() {
	// Rk: the setUp method is called here.

	// Save the roleDescriptor into the database.
	this.roleDescriptorDao.getHibernateTemplate().saveOrUpdate(this.roleDescriptor);

	// Look if this roleDescriptor is also into the database and look if the
	// size of the set is >= 1.
	List<RoleDescriptor> roleDescriptors = this.roleDescriptorDao.getAllRoleDescriptors();
	assertNotNull(roleDescriptors);
	assertTrue(roleDescriptors.size() >= 1);

	this.roleDescriptorDao.deleteRoleDescriptor(this.roleDescriptor);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetRoleDescriptor() {
	// Rk: the setUp method is called here.

	// Save the roleDescriptor into the database.
	String id = this.roleDescriptorDao.saveOrUpdateRoleDescriptor(this.roleDescriptor);

	// Test the method getRoleDescriptor with an existing roleDescriptor.
	RoleDescriptor roleDescriptorTmp = this.roleDescriptorDao.getRoleDescriptor(id);
	assertNotNull(roleDescriptorTmp);
	assertEquals("Name", roleDescriptorTmp.getName(), NAME);
	assertEquals("Description", roleDescriptorTmp.getDescription(), DESCRIPTION);
	assertEquals("Prefix", roleDescriptorTmp.getPrefix(), PREFIX);
	assertEquals("HasMultipleOccurences", roleDescriptorTmp.getHasMultipleOccurrences(), HAS_MULTIPLE_OCCURENCES);
	assertEquals("IsOptional", roleDescriptorTmp.getIsOptional(), IS_OPTIONAL);
	assertEquals("IsPlanned", roleDescriptorTmp.getIsPlanned(), IS_PLANNED);

	// Test the method getRoleDescriptor with an unexisting roleDescriptor.
	this.roleDescriptorDao.getHibernateTemplate().delete(roleDescriptor);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testDeleteRoleDescriptor() {
	// Rk: the setUp method is called here.

	// Save the roleDescriptor into the database.
	String id = this.roleDescriptorDao.saveOrUpdateRoleDescriptor(this.roleDescriptor);

	// Test the method deleteRoleDescriptor with an roleDescriptor existing
	// into the db.
	this.roleDescriptorDao.deleteRoleDescriptor(this.roleDescriptor);

	// See if this.roleDescriptor is now absent in the db.
	RoleDescriptor roleDescriptorTmp = (RoleDescriptor) this.roleDescriptorDao.getHibernateTemplate().get(
		RoleDescriptor.class, id);
	assertNull(roleDescriptorTmp);

	// Rk: the tearDown method is called here.
    }
}
