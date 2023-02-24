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

package wilos.test.hibernate.misc.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.misc.project.ProjectDao;
import wilos.model.misc.project.Project;
import wilos.test.TestConfiguration;
import wilos.utils.Constantes;

public class ProjectDaoTest {

    private ProjectDao projectDao = (ProjectDao) TestConfiguration.getInstance().getApplicationContext().getBean(
	    "ProjectDao");

    private Project project;
    Date date = null;

    @Before
    public void setUp() {

	try {
	    date = Constantes.DATE_FORMAT.parse("18/01/2007 10:00");
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	
	this.project = new Project();
	this.project.setConcreteName("testProject");
	this.project.setDescription("testDesc");
    }

    @After
    public void tearDown() {
	this.project = null;
    }

    @Test
    public void testStorageOfANullProject() {

	Project project = null;

	String id = this.projectDao.saveOrUpdateProject(project);
	Project retrievedProject = this.projectDao.getProject(id);

	assertNull("null", retrievedProject);
	
	this.projectDao.deleteProject(this.project);
    }

    @Test
    public void testStorageOfAProject() {

	String id = this.projectDao.saveOrUpdateProject(this.project);
	Project retrievedProject = this.projectDao.getProject(id);

	assertEquals("project : concrete name", this.project.getConcreteName(), retrievedProject.getConcreteName());
	assertEquals("project : planned time", this.project.getPlannedTime(), retrievedProject.getPlannedTime());
	assertEquals("project : planned finishing date", this.project.getPlannedFinishingDate(), retrievedProject
		.getPlannedFinishingDate());
	assertNotSame("same object", this.project, retrievedProject);

	this.projectDao.deleteProject(this.project);
    }

    @Test
    public void testDeletionOfAProject() {

	String id = this.projectDao.saveOrUpdateProject(this.project);
	this.projectDao.deleteProject(this.project);
	Project retrievedProject = this.projectDao.getProject(id);

	assertNull("null", retrievedProject);
    }

    @Test
    public void testRetrievalOfAllProjects() {

	int nb = this.projectDao.getAllProjects().size();

	Project project1 = new Project();
	project1.setConcreteName("My concrete name");
	project1.setPlannedTime(25.0f);
	project1.setPlannedFinishingDate(date);

	Project project2 = new Project();
	project2.setConcreteName("Your concrete name");
	project2.setPlannedTime(7.0f);
	project2.setPlannedFinishingDate(date);

	this.projectDao.saveOrUpdateProject(this.project);
	this.projectDao.saveOrUpdateProject(project1);
	this.projectDao.saveOrUpdateProject(project2);

	List<Project> projects = this.projectDao.getAllProjects();
	assertNotNull("list not null", projects);
	assertEquals("size", 3, projects.size() - nb);

	this.projectDao.deleteProject(project2);
	this.projectDao.deleteProject(project1);
	this.projectDao.deleteProject(this.project);

	project2 = null;
	project1 = null;

    }

}
