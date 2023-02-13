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
package wilos.test.model.spem2.iteration;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.model.misc.concreteiteration.ConcreteIteration;
import wilos.model.spem2.iteration.Iteration;

public class IterationTest {

	private Iteration iteration;

	public static final String PREFIX = "prefix";

	public static final Boolean IS_OPTIONAL = true;

	public static final String CONCRETENAME = "ConcreteName1";

	public static final String CONCRETENAME2 = "ConcreteName2";

	@Before
	public void setUp() throws Exception {
		this.iteration = new Iteration();
		this.iteration.setPrefix(PREFIX);
		this.iteration.setIsOptional(IS_OPTIONAL);
	}

	@After
	public void tearDown() throws Exception {
		//None.
	}

	@Test
	public final void testHashCode() {
		// Rk: the setUp method is called here.

		Iteration iter = new Iteration();
		iter.setPrefix(PREFIX);
		iter.setIsOptional(IS_OPTIONAL);

		assertNotNull(this.iteration.hashCode());
		assertNotNull(iter.hashCode());
		assertEquals(this.iteration.hashCode(), iter.hashCode());

		// Rk: the tearDown method is called here.
	}

	@Test
	public final void testEquals() {
		// Rk: the setUp method is called here.

		// Assert if it's equal by references.
		assertTrue("By references", this.iteration.equals(this.iteration));

		// Assert if it's equal field by field.
		Iteration iterationTmp1 = null;
		try {
			iterationTmp1 = this.iteration.clone();
		} catch (CloneNotSupportedException e) {
			fail("Error CloneNotSupportedException in the testEquals method");
		}
		assertTrue("Field by field", this.iteration.equals(iterationTmp1));

		// Assert if it's not equal.
		Iteration iterTmp2 = new Iteration();
		iterTmp2.setPrefix("prefixFalse");
		iterTmp2.setIsOptional(true);
		assertFalse("Not equals", this.iteration.equals(iterTmp2));

		// Rk: the tearDown method is called here.
	}

	@Test
	public final void testClone() {
		// Rk: the setUp method is called here.

		try {
			assertEquals(this.iteration.clone(), this.iteration);
		} catch (CloneNotSupportedException e) {
			fail("Error CloneNotSupportedException in the testClone method");
		}

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testAddConcreteIteration() {
		ConcreteIteration concreteIteration = new ConcreteIteration();
		concreteIteration.setConcreteName(CONCRETENAME);

		this.iteration.addConcreteIteration(concreteIteration);

		assertTrue(this.iteration.getConcreteIterations().size() == 1);
		assertNotNull(concreteIteration.getIteration());
	}

	@Test
	public void testAddToAllConcreteIteration() {
		ConcreteIteration concreteIteration1 = new ConcreteIteration();
		concreteIteration1.setConcreteName(CONCRETENAME);

		ConcreteIteration concreteIteration2 = new ConcreteIteration();
		concreteIteration2.setConcreteName(CONCRETENAME2);

		Set<ConcreteIteration> set = new HashSet<ConcreteIteration>();
		set.add(concreteIteration1);
		set.add(concreteIteration2);

		this.iteration.addAllConcreteIterations(set);

		assertFalse(this.iteration.getConcreteIterations().isEmpty());
		assertTrue(this.iteration.getConcreteIterations().size() == 2);
		assertNotNull(concreteIteration1.getIteration());
		assertNotNull(concreteIteration2.getIteration());
	}

	@Test
	public void testRemoveConcreteIteration() {
		ConcreteIteration concreteIteration = new ConcreteIteration();
		concreteIteration.setConcreteName(CONCRETENAME);

		this.iteration.removeConcreteIteration(concreteIteration);

		assertTrue(this.iteration.getConcreteIterations().isEmpty());
		assertNull(concreteIteration.getIteration());
	}

	@Test
	public void testRemoveAllConcreteIterations() {
		ConcreteIteration concreteIteration1 = new ConcreteIteration();
		concreteIteration1.setConcreteName(CONCRETENAME);

		ConcreteIteration concreteIteration2 = new ConcreteIteration();
		concreteIteration2.setConcreteName(CONCRETENAME2);

		Set<ConcreteIteration> set = new HashSet<ConcreteIteration>();
		set.add(concreteIteration1);
		set.add(concreteIteration2);

		this.iteration.addAllConcreteIterations(set);

		assertTrue(this.iteration.getConcreteIterations().size() == 2);
		assertNotNull(concreteIteration1.getIteration());
		assertNotNull(concreteIteration2.getIteration());

		this.iteration.removeAllConcreteIterations();
		assertTrue(this.iteration.getConcreteIterations().isEmpty());
		assertNull(concreteIteration1.getIteration());
		assertNull(concreteIteration2.getIteration());
	}
}
