/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

package wilos.test.business.services.misc.wilosuser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.misc.project.ProjectService;
import wilos.business.services.misc.wilosuser.ParticipantService;
import wilos.model.misc.project.Project;
import wilos.model.misc.wilosuser.Participant;
import wilos.test.TestConfiguration;

public class ParticipantServiceTest {

    private ParticipantService participantService = (ParticipantService) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("ParticipantService");

    private ProjectService projectService = (ProjectService) TestConfiguration.getInstance().getApplicationContext()
	    .getBean("ProjectService");

    private Project p1;

    private Project p2;

    @Before
    public void setUp() {
	this.p1 = new Project();
	this.p1.setConcreteName("projectTestPS1");
	this.projectService.saveProject(p1);

	this.p2 = new Project();
	this.p2.setConcreteName("projectTestPS2");
	this.projectService.saveProject(p2);

	
    }

    @After
    public void tearDown() {
	this.projectService.deleteProject(this.p2.getId());
	this.projectService.deleteProject(this.p1.getId());
    }

    @Test
    public void testSaveParticipant() {

	Participant participant = new Participant();
	participant.setLogin("My login 66");
	participant.setName("My name");
	participant.setPassword("My password");
	participantService.saveParticipant(participant);
	
	participantService.saveParticipant(participant);
	
	Participant ParticipantTmp = (Participant) this.participantService.getParticipantDao().getParticipant(
		"My login 66 ");

	assertNotNull(ParticipantTmp);
	assertEquals(participant.getName(), ParticipantTmp.getName());

	this.participantService.deleteParticipant(participant.getId());
    }
    
    @Test
    public void testSaveProjectsForAParticipant(){
    	//create a participant
    	Participant parti = new Participant();
    	parti.setLogin("Mylogin2");
    	parti.setName("My name");
    	parti.setPassword("My password");
    	this.participantService.saveParticipant(parti);
        
    	// create project
    	Project project3 = new Project();
    	project3.setConcreteName("projectTestPS4");
    	this.projectService.saveProject(project3);
    	Map<String, Boolean> listProject = new HashMap<String, Boolean>();
    	listProject.put(project3.getId(),true);
    	// affect participant to the project
    	this.participantService.saveProjectsForAParticipant(parti,
				listProject);
    	// test if participant is affected
    	List<Project> listAffectedProject = 
    		this.participantService.getAllAffectedProjectsForParticipant(parti);
    	boolean isP3 = false;
    	    	
    	for(Project project : listAffectedProject)  {
    		
    		if(project.getId().equals(project3.getId())){
    			isP3 = true;
    		}
    		else{
    			isP3 = false;
    			break;
    		}
    	}
    	assertEquals(true,isP3);
    	// unaffect participant
    	listProject = new HashMap<String, Boolean>();
    	listProject.put(project3.getId(),false);
    	this.participantService.saveProjectsForAParticipant(parti, listProject);
    	// test if participant is unaffected
    	listAffectedProject = 
    		this.participantService.getAllAffectedProjectsForParticipant(parti);
    	isP3 = false;
    	    	
    	for(Project project : listAffectedProject)  {
    		
    		if(project.getId().equals(project3.getId())){
    			isP3 = true;
    			break;
    		}
    	}
    	assertEquals(false,isP3);
    	// delete project
    	this.projectService.deleteProject(project3.getId());
    	// delete participant
    	this.participantService.deleteParticipant(parti.getId());
    }
}
