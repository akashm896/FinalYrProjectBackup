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

package wilos.test.business.services.spem2.workproduct;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.spem2.workproduct.WorkProductDescriptorService;
import wilos.hibernate.spem2.workproduct.WorkProductDefinitionDao;
import wilos.model.spem2.workproduct.WorkProductDefinition;
import wilos.model.spem2.workproduct.WorkProductDescriptor;
import wilos.test.TestConfiguration;

public class WorkProductDescriptorServiceTest {

    private WorkProductDescriptor workProductDescriptor;

    private WorkProductDefinition workProductDefinition;

    private WorkProductDescriptorService workProductDescriptorService = (WorkProductDescriptorService) TestConfiguration
	    .getInstance().getApplicationContext().getBean(
		    "WorkProductDescriptorService");

    private WorkProductDefinitionDao workProductDefinitionDao = (WorkProductDefinitionDao) TestConfiguration
	    .getInstance().getApplicationContext().getBean(
		    "WorkProductDefinitionDao");

    private String workProductDescriptorId;

    @Before
    public void setUp() {

	this.workProductDefinition = new WorkProductDefinition();
	this.workProductDefinition.setName("My name");
	this.workProductDefinitionDao
		.saveOrUpdateWorkProductDefinition(this.workProductDefinition);

	// None
	this.workProductDescriptor = new WorkProductDescriptor();
	this.workProductDescriptor.setName("My name");
    }

    @After
    public void tearDown() {
	this.workProductDefinitionDao
		.deleteWorkProductDefinition(this.workProductDefinition);
	this.workProductDefinition = null;
	this.workProductDescriptor = null;

    }

    @Test
    public void testStorageOfAnWorkProductDescriptorInATransaction() {

	this.workProductDescriptor
		.addWorkProductDefinition(this.workProductDefinition);
	this.workProductDescriptorId = this.workProductDescriptorService
		.saveWorkProductDescriptor(this.workProductDescriptor);
	this.workProductDefinitionDao
		.saveOrUpdateWorkProductDefinition(this.workProductDefinition);

	WorkProductDescriptor retrievedWPd = this.workProductDescriptorService
		.getWorkProductDescriptor(this.workProductDescriptorId);
	assertNotNull("not null", retrievedWPd);

	this.workProductDescriptor
		.setWorkProductDefinition(this.workProductDefinition);

	this.workProductDefinitionDao
		.saveOrUpdateWorkProductDefinition(this.workProductDefinition);

	this.workProductDescriptorService
		.deleteWorkProductDescriptor(this.workProductDescriptor);
    }

    
}
