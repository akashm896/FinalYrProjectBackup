/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) Sebastien BALARD <sbalard@wilos-project.org>
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
package wilos.test.business.services.misc.concreterole;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.misc.concreterole.ConcreteRoleDescriptorService;
import wilos.hibernate.misc.concreteactivity.ConcreteActivityDao;
import wilos.hibernate.misc.concretetask.ConcreteTaskDescriptorDao;
import wilos.hibernate.misc.project.ProjectDao;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.project.Project;
import wilos.test.TestConfiguration;

public class ConcreteRoleDescriptorServiceTest {

    private ConcreteRoleDescriptor mainConcreteRoleDescriptor;

    private ConcreteRoleDescriptorService concreteRoleDescriptorService = (ConcreteRoleDescriptorService) TestConfiguration
	    .getInstance().getApplicationContext().getBean("ConcreteRoleDescriptorService");

    private ConcreteTaskDescriptorDao concreteTaskDescriptorDao = (ConcreteTaskDescriptorDao) TestConfiguration
	    .getInstance().getApplicationContext().getBean("ConcreteTaskDescriptorDao");

    private ProjectDao projectDao = (ProjectDao) TestConfiguration.getInstance().getApplicationContext().getBean(
	    "ProjectDao");
    
    private ConcreteActivityDao concreteActivityDao = (ConcreteActivityDao) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("ConcreteActivityDao");

    @Before
    public void setUp() {
	this.mainConcreteRoleDescriptor = new ConcreteRoleDescriptor();
	this.mainConcreteRoleDescriptor.setConcreteName("My concrete name");
    }

    @After
    public void tearDown() {
	this.mainConcreteRoleDescriptor = null;
    }

    @Test
    public void testGetSuperConcreteActivities() {
	// Rk: the setUp method is called here.

	//init.
	ConcreteActivity ca = new ConcreteActivity();
	ca.setConcreteName("My conrete activity name");
	this.concreteActivityDao.saveOrUpdateConcreteActivity(ca);

	this.mainConcreteRoleDescriptor.addSuperConcreteActivity(ca);
	String crdId = this.concreteRoleDescriptorService.getConcreteRoleDescriptorDao().saveOrUpdateConcreteRoleDescriptor(
		this.mainConcreteRoleDescriptor);
	this.concreteActivityDao.saveOrUpdateConcreteActivity(ca);

	//test itself.
	List<ConcreteActivity> list = this.concreteRoleDescriptorService.getSuperConcreteActivities(crdId);
	assertTrue("list size", list.size()>=1);

	//clean.
	this.mainConcreteRoleDescriptor.removeSuperConcreteActivity(ca);
	this.concreteActivityDao.saveOrUpdateConcreteActivity(ca);
	this.concreteRoleDescriptorService.getConcreteRoleDescriptorDao().deleteConcreteRoleDescriptor(this.mainConcreteRoleDescriptor);
	this.concreteActivityDao.deleteConcreteActivity(ca);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetAllConcreteRoleDescriptorsForProject() {
	// Rk: the setUp method is called here.

	Project project = new Project();
	project.setConcreteName("project");
	this.projectDao.saveOrUpdateProject(project);

	Project project2 = new Project();
	project2.setConcreteName("project2");
	this.projectDao.saveOrUpdateProject(project2);

	this.mainConcreteRoleDescriptor.setProject(project);

	ConcreteRoleDescriptor ctdTmp = new ConcreteRoleDescriptor();
	ctdTmp.setConcreteName("pouet");
	ctdTmp.setProject(project);

	ConcreteRoleDescriptor ctdTmp2 = new ConcreteRoleDescriptor();
	ctdTmp2.setConcreteName("pouet2");
	ctdTmp2.setProject(project2);

	this.concreteRoleDescriptorService.getConcreteRoleDescriptorDao().saveOrUpdateConcreteRoleDescriptor(
		this.mainConcreteRoleDescriptor);
	this.concreteRoleDescriptorService.getConcreteRoleDescriptorDao().saveOrUpdateConcreteRoleDescriptor(ctdTmp);
	this.concreteRoleDescriptorService.getConcreteRoleDescriptorDao().saveOrUpdateConcreteRoleDescriptor(ctdTmp2);

	List<ConcreteRoleDescriptor> list = this.concreteRoleDescriptorService
		.getAllConcreteRoleDescriptorsForProject(project.getId());

	assertNotNull(list);
	assertTrue(list.size() == 2);

	// clean.
	this.concreteRoleDescriptorService.getConcreteRoleDescriptorDao().deleteConcreteRoleDescriptor(
		this.mainConcreteRoleDescriptor);
	this.concreteRoleDescriptorService.getConcreteRoleDescriptorDao().deleteConcreteRoleDescriptor(ctdTmp);
	this.concreteRoleDescriptorService.getConcreteRoleDescriptorDao().deleteConcreteRoleDescriptor(ctdTmp2);

	this.projectDao.deleteProject(project);
	this.projectDao.deleteProject(project2);
	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetAllConcreteTaskDescriptorsForConcreteRoleDescriptor() {
	// Rk: the setUp method is called here.
	
	this.concreteRoleDescriptorService.getConcreteRoleDescriptorDao().saveOrUpdateConcreteRoleDescriptor(
		this.mainConcreteRoleDescriptor);

	ConcreteTaskDescriptor ctdTmp = new ConcreteTaskDescriptor();
	ctdTmp.setConcreteName("pouet");
	this.concreteTaskDescriptorDao.saveOrUpdateConcreteTaskDescriptor(ctdTmp);

	ConcreteTaskDescriptor ctdTmp2 = new ConcreteTaskDescriptor();
	ctdTmp2.setConcreteName("pouet2");
	this.concreteTaskDescriptorDao.saveOrUpdateConcreteTaskDescriptor(ctdTmp2);

	this.mainConcreteRoleDescriptor.addPrimaryConcreteTaskDescriptor(ctdTmp);
	this.mainConcreteRoleDescriptor.addPrimaryConcreteTaskDescriptor(ctdTmp2);

	this.concreteRoleDescriptorService.getConcreteRoleDescriptorDao().saveOrUpdateConcreteRoleDescriptor(
		this.mainConcreteRoleDescriptor);

	List<ConcreteTaskDescriptor> list = this.concreteRoleDescriptorService
		.getAllConcreteTaskDescriptorsForConcreteRoleDescriptor(this.mainConcreteRoleDescriptor);

	assertNotNull(list);
	assertTrue(list.size() == 2);

	// clean.
	this.mainConcreteRoleDescriptor.removePrimaryConcreteTaskDescriptor(ctdTmp);
	this.mainConcreteRoleDescriptor.removePrimaryConcreteTaskDescriptor(ctdTmp2);
	this.concreteRoleDescriptorService.getConcreteRoleDescriptorDao().saveOrUpdateConcreteRoleDescriptor(
		this.mainConcreteRoleDescriptor);
	this.concreteTaskDescriptorDao.deleteConcreteTaskDescriptor(ctdTmp);
	this.concreteTaskDescriptorDao.deleteConcreteTaskDescriptor(ctdTmp2);
	this.concreteRoleDescriptorService.getConcreteRoleDescriptorDao().deleteConcreteRoleDescriptor(
		this.mainConcreteRoleDescriptor);
	// Rk: the tearDown method is called here.
    }

    @Test
    public void testSaveConcreteRoleDescriptor() {
	ConcreteTaskDescriptor ctdTmp = new ConcreteTaskDescriptor();
	ctdTmp.setConcreteName("pouet");
	this.concreteTaskDescriptorDao.saveOrUpdateConcreteTaskDescriptor(ctdTmp);

	ConcreteTaskDescriptor ctdTmp2 = new ConcreteTaskDescriptor();
	ctdTmp2.setConcreteName("pouet2");
	this.concreteTaskDescriptorDao.saveOrUpdateConcreteTaskDescriptor(ctdTmp2);

	this.mainConcreteRoleDescriptor.addPrimaryConcreteTaskDescriptor(ctdTmp);
	this.mainConcreteRoleDescriptor.addPrimaryConcreteTaskDescriptor(ctdTmp2);

	this.concreteRoleDescriptorService.saveConcreteRoleDescriptor(this.mainConcreteRoleDescriptor);

	String id = this.mainConcreteRoleDescriptor.getId();

	ConcreteRoleDescriptor crdtmp = this.concreteRoleDescriptorService.getConcreteRoleDescriptorDao()
		.getConcreteRoleDescriptor(id);

	assertNotNull(crdtmp);
	assertNotNull(crdtmp.getPrimaryConcreteTaskDescriptors());
	
	//clean
	this.mainConcreteRoleDescriptor.removePrimaryConcreteTaskDescriptor(ctdTmp);
	this.mainConcreteRoleDescriptor.removePrimaryConcreteTaskDescriptor(ctdTmp2);
	this.concreteTaskDescriptorDao.deleteConcreteTaskDescriptor(ctdTmp);
	this.concreteTaskDescriptorDao.deleteConcreteTaskDescriptor(ctdTmp2);
	this.concreteRoleDescriptorService.getConcreteRoleDescriptorDao().deleteConcreteRoleDescriptor(
		this.mainConcreteRoleDescriptor);
    }
}
