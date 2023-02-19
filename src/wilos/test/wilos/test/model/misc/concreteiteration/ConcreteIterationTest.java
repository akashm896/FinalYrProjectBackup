/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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
package wilos.test.model.misc.concreteiteration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.model.misc.concreteiteration.ConcreteIteration;
import wilos.model.spem2.iteration.Iteration;
import wilos.utils.Constantes.State;

public class ConcreteIterationTest {

	private ConcreteIteration concreteIteration;

	public static final String CONCRETENAME = "concreteName";
	
	public static final String PREFIX = "prefix" ;

	public static final Boolean IS_OPTIONAL = true ;

	@Before
	public void setUp() {
		this.concreteIteration = new ConcreteIteration();
		this.concreteIteration.setConcreteName(CONCRETENAME);
	}

	@After
	public void tearDown() {
		//None
	}

	@Test
	public void testHashCode() {
		
		// Rk: the setUp method is called here.

		ConcreteIteration tmp = new ConcreteIteration();
		tmp.setConcreteName(CONCRETENAME);
		
		Iteration iteration = new Iteration();
		iteration.setPrefix(PREFIX) ;
		iteration.setIsOptional(IS_OPTIONAL) ;
		
		tmp.setIteration(iteration);
		this.concreteIteration.setIteration(iteration);

		assertNotNull(this.concreteIteration.hashCode()) ;
		assertNotNull(tmp.hashCode()) ;
		assertEquals(this.concreteIteration.hashCode(), tmp.hashCode()) ;

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testEqualsObject() {
		
		// Rk: the setUp method is called here.

		ConcreteIteration tmp = new ConcreteIteration();
		tmp.setConcreteName(CONCRETENAME);
		
		Iteration iteration = new Iteration();
		iteration.setPrefix(PREFIX) ;
		iteration.setIsOptional(IS_OPTIONAL) ;
		
		tmp.setIteration(iteration);
		this.concreteIteration.setIteration(iteration);

		assertNotNull(this.concreteIteration) ;
		assertNotNull(tmp) ;
		assertTrue(this.concreteIteration.equals(tmp)) ;

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testClone() {
		
		// Rk: the setUp method is called here.
		
		ConcreteIteration tmp = null;
		
		Iteration iteration = new Iteration();
		iteration.setPrefix(PREFIX) ;
		iteration.setIsOptional(IS_OPTIONAL) ;
		
		this.concreteIteration.setIteration(iteration);
		
		try{
			tmp = this.concreteIteration.clone();
		}
		catch(CloneNotSupportedException e){
			fail("Error CloneNotSupportedException in the testClone method") ;
		}
		
		assertNotNull(tmp);
		assertEquals(tmp, this.concreteIteration) ;
		

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testAddIteration() {

		// Rk: the setUp method is called here.
		
		Iteration iteration = new Iteration();
		iteration.setPrefix(PREFIX) ;
		iteration.setIsOptional(IS_OPTIONAL) ;

		this.concreteIteration.addIteration(iteration);

	assertEquals(this.concreteIteration.getIteration(), iteration);
	assertTrue(iteration.getConcreteIterations().contains(
		this.concreteIteration));
	assertEquals(this.concreteIteration.getState(), State.CREATED);

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testRemoveIteration() {

		// Rk: the setUp method is called here.
		
		Iteration iteration = new Iteration();
		iteration.setPrefix(PREFIX) ;
		iteration.setIsOptional(IS_OPTIONAL) ;

		this.concreteIteration.addIteration(iteration);
		this.concreteIteration.removeIteration(iteration);
		
		assertNull(this.concreteIteration.getIteration()) ;
		assertFalse(iteration.getConcreteIterations().contains(this.concreteIteration)) ;

		// Rk: the tearDown method is called here.
	}

}
