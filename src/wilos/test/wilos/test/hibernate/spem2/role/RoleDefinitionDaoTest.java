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

import wilos.hibernate.spem2.role.RoleDefinitionDao;
import wilos.model.spem2.role.RoleDefinition;
import wilos.test.TestConfiguration;

/**
 * Unit test for RoleDefinitionDao
 * 
 * @author Soosuske
 */
public class RoleDefinitionDaoTest {

    private RoleDefinitionDao roleDefinitionDao = (RoleDefinitionDao) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("RoleDefinitionDao");

    private RoleDefinition roleDefinition = null;

    public static final String NAME = "thisRole";

    public static final String DESCRIPTION = "roleDefinition description";

    @Before
    public void setUp() {

	// Create empty RoleDefinition
	this.roleDefinition = new RoleDefinition();
	this.roleDefinition.setName(NAME);
	this.roleDefinition.setDescription(DESCRIPTION);
    }

    @After
    public void tearDown() {

	this.roleDefinition = null;
    }

    @Test
    public void testSaveOrUpdateRole() {
	// Rk: the setUp method is called here.

	// Save the roleDefinition with the method to test.
	String id = this.roleDefinitionDao.saveOrUpdateRoleDefinition(this.roleDefinition);

	RoleDefinition roleTmp = (RoleDefinition) this.roleDefinitionDao.getHibernateTemplate().load(
		RoleDefinition.class, id);
	assertNotNull(roleTmp);

	this.roleDefinitionDao.deleteRoleDefinition(this.roleDefinition);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetAllRole() {
	// Rk: the setUp method is called here.
	// Save the roleDefinition into the database.
	this.roleDefinitionDao.getHibernateTemplate().saveOrUpdate(this.roleDefinition);

	// Look if this roleDefinition is also into the database and look if the
	// size of
	// the set is >= 1.
	List<RoleDefinition> roleDefinitions = this.roleDefinitionDao.getAllRoleDefinitions();
	assertNotNull(roleDefinitions);
	assertTrue(roleDefinitions.size() >= 1);

	this.roleDefinitionDao.deleteRoleDefinition(this.roleDefinition);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetRole() {
	// Rk: the setUp method is called here.

	// Save the roleDefinition into the database.
	String id = this.roleDefinitionDao.saveOrUpdateRoleDefinition(this.roleDefinition);

	// Test the method getActivity with an existing activity.
	RoleDefinition roleTmp = this.roleDefinitionDao.getRoleDefinition(id);
	assertNotNull(roleTmp);
	assertEquals("Name", roleTmp.getName(), NAME);
	assertEquals("Description", roleTmp.getDescription(), DESCRIPTION);

	// Test the method getRole with an unexisting roleDefinition.
	this.roleDefinitionDao.getHibernateTemplate().delete(roleDefinition);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testDeleteRole() {
	// Rk: the setUp method is called here.

	// Save the roleDefinition into the database.
	String id = this.roleDefinitionDao.saveOrUpdateRoleDefinition(this.roleDefinition);

	// Test the method deleteRole with an roleDefinition existing into the
	// db.
	this.roleDefinitionDao.deleteRoleDefinition(this.roleDefinition);

	// See if this.role is now absent in the db.
	RoleDefinition roleTmp = (RoleDefinition) this.roleDefinitionDao.getHibernateTemplate().get(
		RoleDefinition.class, id);
	assertNull(roleTmp);

	// Rk: the tearDown method is called here.
    }
}
