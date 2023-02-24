/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
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
package wilos.test.business.services.misc.concreteworkproduct;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.misc.concreteworkproduct.ConcreteWorkProductDescriptorService;
import wilos.business.services.misc.wilosuser.ParticipantService;
import wilos.business.services.spem2.workproduct.WorkProductDescriptorService;
import wilos.hibernate.misc.concreteactivity.ConcreteActivityDao;
import wilos.hibernate.misc.concreterole.ConcreteRoleDescriptorDao;
import wilos.hibernate.misc.concretetask.ConcreteTaskDescriptorDao;
import wilos.hibernate.misc.concreteworkproduct.ConcreteWorkProductDescriptorDao;
import wilos.hibernate.misc.project.ProjectDao;
import wilos.hibernate.misc.wilosuser.ParticipantDao;
import wilos.hibernate.spem2.role.RoleDescriptorDao;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;
import wilos.model.misc.project.Project;
import wilos.model.misc.wilosuser.Participant;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.model.spem2.workproduct.WorkProductDescriptor;
import wilos.test.TestConfiguration;

public class ConcreteWorkProductDescriptorServiceTest {

    private ConcreteWorkProductDescriptor mainConcreteWorkProductDescriptor;

    private ConcreteWorkProductDescriptorService concreteWorkProductDescriptorService = (ConcreteWorkProductDescriptorService) TestConfiguration
	    .getInstance().getApplicationContext().getBean(
		    "ConcreteWorkProductDescriptorService");
    
    private WorkProductDescriptorService workProductDescriptorService = (WorkProductDescriptorService) TestConfiguration
    .getInstance().getApplicationContext().getBean(
	    "WorkProductDescriptorService");
    
    private ParticipantService participantService = (ParticipantService) TestConfiguration
    .getInstance().getApplicationContext().getBean(
	    "ParticipantService");

    private ProjectDao projectDao = (ProjectDao) TestConfiguration
	    .getInstance().getApplicationContext().getBean("ProjectDao");

    private ConcreteActivityDao concreteActivityDao = (ConcreteActivityDao) TestConfiguration
	    .getInstance().getApplicationContext().getBean(
		    "ConcreteActivityDao");

    private ParticipantDao participantdao = (ParticipantDao) TestConfiguration
	    .getInstance().getApplicationContext().getBean("ParticipantDao");

    private ConcreteWorkProductDescriptorDao concreteWorkProductDescriptorDao = (ConcreteWorkProductDescriptorDao) TestConfiguration
	    .getInstance().getApplicationContext().getBean(
		    "ConcreteWorkProductDescriptorDao");

    private RoleDescriptorDao roleDescriptordao = (RoleDescriptorDao) TestConfiguration
	    .getInstance().getApplicationContext().getBean("RoleDescriptorDao");

    private ConcreteRoleDescriptorDao concreteRoleDescriptorDao = (ConcreteRoleDescriptorDao) TestConfiguration
	    .getInstance().getApplicationContext().getBean(
		    "ConcreteRoleDescriptorDao");

    @Before
    public void setUp() {
	this.mainConcreteWorkProductDescriptor = new ConcreteWorkProductDescriptor();
	this.mainConcreteWorkProductDescriptor
		.setConcreteName("My concrete name");
    }

    @After
    public void tearDown() {
	this.mainConcreteWorkProductDescriptor = null;
    }

    @Test
    public void testGetSuperConcreteActivities() {
	// Rk: the setUp method is called here.

	// init.
	ConcreteActivity ca = new ConcreteActivity();
	ca.setConcreteName("My conrete activity name");
	this.concreteActivityDao.saveOrUpdateConcreteActivity(ca);

	this.mainConcreteWorkProductDescriptor.addSuperConcreteActivity(ca);
	String cwpdId = this.concreteWorkProductDescriptorService
		.getConcreteWorkProductDescriptorDao()
		.saveOrUpdateConcreteWorkProductDescriptor(
			this.mainConcreteWorkProductDescriptor);
	this.concreteActivityDao.saveOrUpdateConcreteActivity(ca);

	// test itself.
	List<ConcreteActivity> list = this.concreteWorkProductDescriptorService
		.getSuperConcreteActivities(cwpdId);
	assertTrue("list size", list.size() >= 1);

	// clean.
	this.mainConcreteWorkProductDescriptor.removeSuperConcreteActivity(ca);
	this.concreteActivityDao.saveOrUpdateConcreteActivity(ca);
	this.concreteWorkProductDescriptorService
		.getConcreteWorkProductDescriptorDao()
		.deleteConcreteWorkProductDescriptor(
			this.mainConcreteWorkProductDescriptor);
	this.concreteActivityDao.deleteConcreteActivity(ca);

	// Rk: the tearDown method is called here.
    }

    @Test
    public void testGetAllConcreteWorkProductDescriptorsForProject() {
	// Rk: the setUp method is called here.

	Project project = new Project();
	project.setConcreteName("project");
	this.projectDao.saveOrUpdateProject(project);

	Project project2 = new Project();
	project2.setConcreteName("project2");
	this.projectDao.saveOrUpdateProject(project2);

	this.mainConcreteWorkProductDescriptor.setProject(project);

	ConcreteWorkProductDescriptor cwpdTmp = new ConcreteWorkProductDescriptor();
	cwpdTmp.setConcreteName("pouet");
	cwpdTmp.setProject(project);

	ConcreteWorkProductDescriptor cwpdTmp2 = new ConcreteWorkProductDescriptor();
	cwpdTmp2.setConcreteName("pouet2");
	cwpdTmp2.setProject(project2);

	this.concreteWorkProductDescriptorService
		.getConcreteWorkProductDescriptorDao()
		.saveOrUpdateConcreteWorkProductDescriptor(
			this.mainConcreteWorkProductDescriptor);
	this.concreteWorkProductDescriptorService
		.getConcreteWorkProductDescriptorDao()
		.saveOrUpdateConcreteWorkProductDescriptor(cwpdTmp);
	this.concreteWorkProductDescriptorService
		.getConcreteWorkProductDescriptorDao()
		.saveOrUpdateConcreteWorkProductDescriptor(cwpdTmp2);

	List<ConcreteWorkProductDescriptor> list = this.concreteWorkProductDescriptorService
		.getAllConcreteWorkProductDescriptorsForProject(project.getId());

	assertNotNull(list);
	assertTrue(list.size() == 2);

	// clean.
	this.concreteWorkProductDescriptorService
		.getConcreteWorkProductDescriptorDao()
		.deleteConcreteWorkProductDescriptor(
			this.mainConcreteWorkProductDescriptor);
	this.concreteWorkProductDescriptorService
		.getConcreteWorkProductDescriptorDao()
		.deleteConcreteWorkProductDescriptor(cwpdTmp);
	this.concreteWorkProductDescriptorService
		.getConcreteWorkProductDescriptorDao()
		.deleteConcreteWorkProductDescriptor(cwpdTmp2);

	this.projectDao.deleteProject(project);
	this.projectDao.deleteProject(project2);
	// Rk: the tearDown method is called here.
    }

    @Test
    public void testSaveConcreteWorkProductDescriptor() {

	this.concreteWorkProductDescriptorService
		.saveConcreteWorkProductDescriptor(this.mainConcreteWorkProductDescriptor);

	String id = this.mainConcreteWorkProductDescriptor.getId();

	ConcreteWorkProductDescriptor cwpdtmp = this.concreteWorkProductDescriptorService
		.getConcreteWorkProductDescriptorDao()
		.getConcreteWorkProductDescriptor(id);

	assertNotNull(cwpdtmp);

    }

    @Test
    public void testAffectedConcreteWorkProductDescriptor() {

	this.concreteWorkProductDescriptorService
		.saveConcreteWorkProductDescriptor(this.mainConcreteWorkProductDescriptor);

	Participant monpar = new Participant();
	monpar.setLogin("login test");
	this.participantdao.saveOrUpdateParticipant(monpar);

	this.mainConcreteWorkProductDescriptor = new ConcreteWorkProductDescriptor();
	this.mainConcreteWorkProductDescriptor.setConcreteName("Mon guid");
	this.concreteWorkProductDescriptorDao
		.saveOrUpdateConcreteWorkProductDescriptor(this.mainConcreteWorkProductDescriptor);

	RoleDescriptor roleDescriptor = new RoleDescriptor();
	roleDescriptor.setGuid("Mon guid");
	this.roleDescriptordao.saveOrUpdateRoleDescriptor(roleDescriptor);

	ConcreteRoleDescriptor concreteRoleDescriptor = new ConcreteRoleDescriptor();
	concreteRoleDescriptor.setConcreteName("Mon concrete name");
	this.concreteRoleDescriptorDao
		.saveOrUpdateConcreteRoleDescriptor(concreteRoleDescriptor);

	concreteRoleDescriptor.addParticipant(monpar);
	this.participantdao.saveOrUpdateParticipant(monpar);

	concreteRoleDescriptor.addRoleDescriptor(roleDescriptor);
	
	this.roleDescriptordao.saveOrUpdateRoleDescriptor(roleDescriptor);
	this.concreteRoleDescriptorDao
		.saveOrUpdateConcreteRoleDescriptor(concreteRoleDescriptor);
	
	this.mainConcreteWorkProductDescriptor.setResponsibleConcreteRoleDescriptor(concreteRoleDescriptor);
	
	WorkProductDescriptor wpd = new WorkProductDescriptor();
	
	wpd.setGuid("truc");
	
	this.workProductDescriptorService.saveWorkProductDescriptor(wpd);
	
	wpd.setResponsibleRoleDescriptor(roleDescriptor);
	
	this.mainConcreteWorkProductDescriptor.setWorkProductDescriptor(wpd);
	
	
	this.concreteWorkProductDescriptorDao
		.saveOrUpdateConcreteWorkProductDescriptor(this.mainConcreteWorkProductDescriptor);

	this.participantService.saveConcreteWorkProductDescriptorForAParticipant(monpar, this.mainConcreteWorkProductDescriptor);
	
	this.participantdao.saveOrUpdateParticipant(monpar);
	
	this.concreteWorkProductDescriptorService.affectedConcreteWorkProductDescriptor(this.mainConcreteWorkProductDescriptor, monpar);

	ConcreteRoleDescriptor res = this.mainConcreteWorkProductDescriptor.getResponsibleConcreteRoleDescriptor();

	assertTrue(res.equals(concreteRoleDescriptor));

	concreteRoleDescriptor.removeParticipant(monpar);
	this.participantdao.saveOrUpdateParticipant(monpar);

	concreteRoleDescriptor.removeRoleDescriptor(roleDescriptor);
	this.mainConcreteWorkProductDescriptor.removeResponsibleConcreteRoleDescriptor(concreteRoleDescriptor);
	this.roleDescriptordao.saveOrUpdateRoleDescriptor(roleDescriptor);
	this.concreteRoleDescriptorDao
		.saveOrUpdateConcreteRoleDescriptor(concreteRoleDescriptor);
	this.concreteWorkProductDescriptorService
		.saveConcreteWorkProductDescriptor(this.mainConcreteWorkProductDescriptor);

	// cleanup used objects
	this.roleDescriptordao.deleteRoleDescriptor(roleDescriptor);
	this.concreteRoleDescriptorDao
		.deleteConcreteRoleDescriptor(concreteRoleDescriptor);
	this.concreteWorkProductDescriptorService
		.removeConcreteWorkProductDescriptor(mainConcreteWorkProductDescriptor);
	this.workProductDescriptorService.deleteWorkProductDescriptor(wpd);
	this.participantService.deleteParticipant(monpar.getId());
	
    }
}
