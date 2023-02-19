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
package wilos.test.model.spem2.element;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.model.spem2.element.Element;

public class ElementTest {

	private Element element;

	public static final String IDEPF = "idEPF";

	public static final String NAME = "name";

	public static final String DESCRIPTION = "description";

	@Before
	public void setUp() throws Exception {
		this.element = new Element();
		this.element.setDescription(DESCRIPTION);
		this.element.setName(NAME);
		this.element.setName(IDEPF);
	}

	@After
	public void tearDown() throws Exception {
		//None.
	}

	@Test
	public void testHashCode() {

		Element elt = new Element();
		elt.setDescription(DESCRIPTION);
		elt.setName(NAME);
		elt.setName(IDEPF);

		assertNotNull(this.element.hashCode());
		assertNotNull(elt.hashCode());
		assertEquals(this.element.hashCode(),elt.hashCode());
	}

	@Test
	public void testClone() {
		try {
			assertTrue(this.element.equals(this.element.clone()));
		} catch (CloneNotSupportedException e) {
			fail("Error CloneNotSupportedException in the testClone method");
		}
	}

	@Test
	public void testEquals() {
		Element elt = new Element();
		elt.setDescription(DESCRIPTION);
		elt.setName(NAME);
		elt.setName(IDEPF);

		assertTrue(this.element.equals(elt));
	}

}