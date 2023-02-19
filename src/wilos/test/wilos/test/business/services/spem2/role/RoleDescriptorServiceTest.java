/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Mathieu BENOIT <mathieu-benoit@hotmail.fr>
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

package wilos.test.business.services.spem2.role;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.spem2.role.RoleDescriptorService;
import wilos.hibernate.spem2.role.RoleDefinitionDao;
import wilos.hibernate.spem2.task.TaskDefinitionDao;
import wilos.hibernate.spem2.task.TaskDescriptorDao;
import wilos.model.spem2.role.RoleDefinition;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.model.spem2.task.TaskDefinition;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.test.TestConfiguration;

public class RoleDescriptorServiceTest {

    private RoleDescriptor roleDescriptor;

    private RoleDefinition roleDefinition;

    private TaskDefinition taskDefinition;

    private TaskDescriptor primary;

    private TaskDescriptor additional;

    private RoleDescriptorService roleDescriptorService = (RoleDescriptorService) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("RoleDescriptorService");

    private TaskDefinitionDao taskDefinitionDao = (TaskDefinitionDao) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("TaskDefinitionDao");

    private TaskDescriptorDao taskDescriptorDao = (TaskDescriptorDao) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("TaskDescriptorDao");

    private RoleDefinitionDao roleDefinitionDao = (RoleDefinitionDao) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("RoleDefinitionDao");

    private String roleDescriptorId;

    @Before
    public void setUp() {

	this.roleDefinition = new RoleDefinition();
	this.roleDefinition.setName("My name");
	this.roleDefinitionDao.saveOrUpdateRoleDefinition(this.roleDefinition);

	this.taskDefinition = new TaskDefinition();
	this.taskDefinition.setName("My name");
	this.taskDefinitionDao.saveOrUpdateTaskDefinition(this.taskDefinition);

	this.primary = new TaskDescriptor();
	this.primary.setName("primary");
	this.primary.addTaskDefinition(this.taskDefinition);
	this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.primary);

	this.additional = new TaskDescriptor();
	this.additional.setName("additional");
	this.additional.addTaskDefinition(this.taskDefinition);
	this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.additional);

	this.taskDefinitionDao.saveOrUpdateTaskDefinition(this.taskDefinition);
	// None
	this.roleDescriptor = new RoleDescriptor();
	this.roleDescriptor.setName("My name");
    }

    @After
    public void tearDown() {
	this.taskDescriptorDao.deleteTaskDescriptor(this.additional);
	this.taskDescriptorDao.deleteTaskDescriptor(this.primary);
	this.taskDefinitionDao.deleteTaskDefinition(this.taskDefinition);
	this.roleDefinitionDao.deleteRoleDefinition(this.roleDefinition);
	this.roleDefinition = null;
	this.taskDefinition = null;
	this.roleDescriptor = null;

    }

    @Test
    public void testStorageOfAnRoleDescriptorInATransaction() {

	this.roleDescriptor.addRoleDefinition(this.roleDefinition);
	this.roleDescriptor.addAdditionalTask(this.additional);
	this.roleDescriptor.addPrimaryTask(this.primary);
	this.roleDescriptorId = this.roleDescriptorService.saveRoleDescriptor(this.roleDescriptor);
	this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.additional);
	this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.primary);
	this.roleDefinitionDao.saveOrUpdateRoleDefinition(this.roleDefinition);

	RoleDescriptor retrievedRd = this.roleDescriptorService.getRoleDescriptor(this.roleDescriptorId);
	assertNotNull("not null", retrievedRd);

	Set<TaskDescriptor> primaries = this.roleDescriptorService.getPrimaryTasks(retrievedRd);
	assertEquals("size of primaries", 1, primaries.size());

	Set<TaskDescriptor> additionals = this.roleDescriptorService.getAdditionalTasks(retrievedRd);
	assertEquals("size of additionals", 1, additionals.size());

	this.roleDescriptor.setRoleDefinition(this.roleDefinition);
	this.roleDescriptor.removeAdditionalTask(this.additional);
	this.roleDescriptor.removePrimaryTask(this.primary);

	this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.additional);
	this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.primary);
	this.roleDefinitionDao.saveOrUpdateRoleDefinition(this.roleDefinition);

	this.roleDescriptorService.deleteRoleDescriptor(this.roleDescriptor);
    }

    @Test
    public void testThatAllRoleDescriptorsAreRetrieved() {

	int nb = this.roleDescriptorService.getAllRoleDescriptors().size();

	this.roleDescriptor.addRoleDefinition(this.roleDefinition);
	this.roleDescriptor.addAdditionalTask(this.additional);
	this.roleDescriptor.addPrimaryTask(this.primary);
	this.roleDescriptorId = this.roleDescriptorService.saveRoleDescriptor(this.roleDescriptor);
	this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.additional);
	this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.primary);
	this.roleDefinitionDao.saveOrUpdateRoleDefinition(this.roleDefinition);

	RoleDescriptor act1 = new RoleDescriptor();
	act1.setName("Your name");
	act1.setDescription("Your description");

	this.roleDescriptorService.saveRoleDescriptor(act1);

	RoleDescriptor act2 = new RoleDescriptor();
	act2.setName("Its name");
	act2.setDescription("Its description");

	this.roleDescriptorService.saveRoleDescriptor(act2);

	List<RoleDescriptor> roleDescriptors = this.roleDescriptorService.getAllRoleDescriptors();

	assertNotNull("not null", roleDescriptors);
	assertEquals("number of roleDescriptors", 3, roleDescriptors.size() - nb);

	this.roleDescriptorService.deleteRoleDescriptor(act2);
	this.roleDescriptorService.deleteRoleDescriptor(act1);

	this.roleDescriptor.setRoleDefinition(this.roleDefinition);
	this.roleDescriptor.removeAdditionalTask(this.additional);
	this.roleDescriptor.removePrimaryTask(this.primary);

	this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.additional);
	this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.primary);
	this.roleDefinitionDao.saveOrUpdateRoleDefinition(this.roleDefinition);

	this.roleDescriptorService.deleteRoleDescriptor(this.roleDescriptor);
    }
}
