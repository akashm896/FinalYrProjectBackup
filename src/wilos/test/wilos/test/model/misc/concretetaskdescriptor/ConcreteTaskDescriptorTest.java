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
package wilos.test.model.misc.concretetaskdescriptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.utils.Constantes;
import wilos.utils.Constantes.State;

public class ConcreteTaskDescriptorTest {

	private ConcreteTaskDescriptor concreteTaskDescriptor ;

	private Date date;

	public static final String CONCRETE_NAME = "Concrete name" ;

	public static final int ACCOMPLISHED_TIME = 15 ;

	public static final int PLANNED_TIME = 24 ;

	public static final String PLANNED_FINISHING_DATE_STRING = "18/01/2007 10:00" ;


	public ConcreteTaskDescriptorTest(){
		try {
			date = Constantes.DATE_FORMAT.parse(PLANNED_FINISHING_DATE_STRING);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void setUp() {
		this.concreteTaskDescriptor = new ConcreteTaskDescriptor() ;
		this.concreteTaskDescriptor.setConcreteName(CONCRETE_NAME);
		this.concreteTaskDescriptor.setAccomplishedTime(ACCOMPLISHED_TIME);
		this.concreteTaskDescriptor.setPlannedTime(PLANNED_TIME);
		this.concreteTaskDescriptor.setPlannedFinishingDate(this.date);
	}

	@After
	public void tearDown() {
		//None
	}

	@Test
	public void testClone () {
		try{
			assertEquals(this.concreteTaskDescriptor, this.concreteTaskDescriptor.clone()) ;
		}
		catch(CloneNotSupportedException e){
			fail("Error CloneNotSupportedException in the testClone method") ;
		}
	}

	@Test
	public void testHashCode() {
		ConcreteTaskDescriptor ctd = new ConcreteTaskDescriptor();
		ctd.setConcreteName(CONCRETE_NAME);
		ctd.setAccomplishedTime(ACCOMPLISHED_TIME);
		ctd.setPlannedTime(PLANNED_TIME);
		ctd.setPlannedFinishingDate(this.date);

		assertNotNull(this.concreteTaskDescriptor.hashCode());
		assertNotNull(ctd.hashCode());
		assertEquals(this.concreteTaskDescriptor.hashCode(),ctd.hashCode());
	}

	@Test
	public void testAddTaskDescriptor() {
		TaskDescriptor taskDescriptor = new TaskDescriptor();
		this.concreteTaskDescriptor.addTaskDescriptor(taskDescriptor);
		assertNotNull(this.concreteTaskDescriptor.getTaskDescriptor());
		assertEquals(this.concreteTaskDescriptor.getTaskDescriptor(), taskDescriptor);
		assertEquals(this.concreteTaskDescriptor.getState(), State.CREATED);
		assertEquals(this.concreteTaskDescriptor.getPlannedFinishingDate(), this.date);
		assertTrue(taskDescriptor.getConcreteTaskDescriptors().size() == 1);
		System.out.println(this.concreteTaskDescriptor.getState());
		System.out.println(this.concreteTaskDescriptor.getPlannedFinishingDate());
	}

	@Test
	public void testRemoveTaskDescriptor() {
		TaskDescriptor taskDescriptor = new TaskDescriptor();

		this.concreteTaskDescriptor.addTaskDescriptor(taskDescriptor);
		assertNotNull(this.concreteTaskDescriptor.getTaskDescriptor());
		assertEquals(this.concreteTaskDescriptor.getTaskDescriptor(), taskDescriptor);

		this.concreteTaskDescriptor.removeTaskDescriptor(taskDescriptor);
		assertNull(this.concreteTaskDescriptor.getTaskDescriptor());
		assertTrue(taskDescriptor.getConcreteTaskDescriptors().size() == 0);
	}
	
	@Test
	public void testaddConcreteRoleDescriptor()
	{
		ConcreteRoleDescriptor crd = new ConcreteRoleDescriptor();
		this.concreteTaskDescriptor.addMainConcreteRoleDescriptor(crd);
		
		assertTrue(this.concreteTaskDescriptor.getMainConcreteRoleDescriptor() == crd);
		assertTrue(crd.getPrimaryConcreteTaskDescriptors().size() == 1);
	}

}
