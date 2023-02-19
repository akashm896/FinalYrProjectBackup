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
package wilos.test.model.spem2.breakdownelement;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.breakdownelement.BreakdownElement;

public class BreakdownElementTest {

	private BreakdownElement breakdownElement ;

	@Before
	public void setUp() throws Exception {
		this.breakdownElement = new BreakdownElement() ;
		this.breakdownElement.setGuid("idEPF1") ;
		this.breakdownElement.setName("name1") ;
		this.breakdownElement.setDescription("description1") ;
		this.breakdownElement.setPrefix("prefix1") ;
		this.breakdownElement.setIsOptional(true) ;
		this.breakdownElement.setIsPlanned(false) ;
		this.breakdownElement.setHasMultipleOccurrences(false) ;
	}

	@After
	public void tearDown() throws Exception {
		//None.
	}

	@Test
	public void testClone() {
		try{
			assertEquals(this.breakdownElement, this.breakdownElement.clone()) ;
		}
		catch(CloneNotSupportedException e){
			fail("Error CloneNotSupportedException in the testClone method") ;
		}
	}

	@Test
	public void testHashCode() {
		// Rk: the setUp method is called here.

		BreakdownElement bdeTmp1 = new BreakdownElement() ;
		bdeTmp1.setGuid("idEPF1") ;
		bdeTmp1.setName("name1") ;
		bdeTmp1.setDescription("description1") ;
		bdeTmp1.setPrefix("prefix1") ;
		bdeTmp1.setIsOptional(true) ;
		bdeTmp1.setIsPlanned(false) ;
		bdeTmp1.setHasMultipleOccurrences(false) ;

		assertNotNull(this.breakdownElement.hashCode()) ;
		assertNotNull(bdeTmp1.hashCode()) ;
		assertEquals(this.breakdownElement.hashCode(), bdeTmp1.hashCode()) ;

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testEquals() {
		// Rk: the setUp method is called here.

		// Assert if it's equal by references.
		assertTrue("By references", this.breakdownElement.equals(this.breakdownElement)) ;

		// Assert if it's equal field by field.
		BreakdownElement bdeTmp1 = new BreakdownElement() ;
		bdeTmp1.setGuid("idEPF1") ;
		bdeTmp1.setName("name1") ;
		bdeTmp1.setDescription("description1") ;
		bdeTmp1.setPrefix("prefix1") ;
		bdeTmp1.setIsOptional(true) ;
		bdeTmp1.setIsPlanned(false) ;
		bdeTmp1.setHasMultipleOccurrences(false) ;
		assertTrue("Field by field", this.breakdownElement.equals(bdeTmp1)) ;

		// Assert if it's not equal.
		BreakdownElement bdeTmp2 = new BreakdownElement() ;
		bdeTmp2.setGuid("idEPF2") ;
		bdeTmp2.setName("name2") ;
		bdeTmp2.setDescription("description2") ;
		bdeTmp2.setPrefix("prefix2") ;
		bdeTmp2.setIsOptional(true) ;
		bdeTmp2.setIsPlanned(false) ;
		bdeTmp2.setHasMultipleOccurrences(false) ;
		assertFalse("Not equals", this.breakdownElement.equals(bdeTmp2)) ;

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testAddToActivity() {
		// Rk: the setUp method is called here.

		assertTrue("Empty (begin)", this.breakdownElement.getSuperActivities().isEmpty()) ;

		Activity activity1 = new Activity() ;
		activity1.setGuid("idEPF") ;
		activity1.setName("name1") ;
		activity1.setDescription("description1") ;
		activity1.setPrefix("prefix1") ;
		activity1.setIsOptional(true) ;
		activity1.setIsPlanned(false) ;
		activity1.setHasMultipleOccurrences(false) ;
		activity1.setIsEvenDriven(true) ;
		activity1.setIsOngoing(false) ;
		activity1.setIsRepeatable(true) ;
		this.breakdownElement.addSuperActivity(activity1) ;
		assertTrue("acts.size == 1", this.breakdownElement.getSuperActivities().size() == 1) ;
		assertTrue("bdes.size1 == 1", activity1.getBreakdownElements().size() == 1) ;

		Activity activity2 = new Activity() ;
		activity2.setGuid("idEPF") ;
		activity2.setName("name2") ;
		activity2.setDescription("description2") ;
		activity2.setPrefix("prefix2") ;
		activity2.setIsOptional(true) ;
		activity2.setIsPlanned(false) ;
		activity2.setHasMultipleOccurrences(false) ;
		activity2.setIsEvenDriven(true) ;
		activity2.setIsOngoing(false) ;
		activity2.setIsRepeatable(true) ;
		this.breakdownElement.addSuperActivity(activity2) ;
		assertTrue("acts.size ==  2", this.breakdownElement.getSuperActivities().size() == 2) ;
		assertTrue("bdes.size2 ==  1", activity2.getBreakdownElements().size() == 1) ;

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testAddToAllActivities() {
		// Rk: the setUp method is called here.

		Activity activity1 = new Activity() ;
		activity1.setGuid("idEPF") ;
		activity1.setName("name1") ;
		activity1.setDescription("description1") ;
		activity1.setPrefix("prefix1") ;
		activity1.setIsOptional(true) ;
		activity1.setIsPlanned(false) ;
		activity1.setHasMultipleOccurrences(false) ;
		activity1.setIsEvenDriven(true) ;
		activity1.setIsOngoing(false) ;
		activity1.setIsRepeatable(true) ;

		Activity activity2 = new Activity() ;
		activity2.setGuid("idEPF") ;
		activity2.setName("name2") ;
		activity2.setDescription("description2") ;
		activity2.setPrefix("prefix2") ;
		activity2.setIsOptional(true) ;
		activity2.setIsPlanned(false) ;
		activity2.setHasMultipleOccurrences(false) ;
		activity2.setIsEvenDriven(true) ;
		activity2.setIsOngoing(false) ;
		activity2.setIsRepeatable(true) ;

		Set<Activity> activities = new HashSet<Activity>() ;
		activities.add(activity1) ;
		activities.add(activity2) ;

		this.breakdownElement.addAllSuperActivities(activities) ;

		assertTrue("acts.size ==  2", this.breakdownElement.getSuperActivities().size() == 2) ;
		assertTrue("bdes1.size == 1", activity1.getBreakdownElements().size() == 1) ;
		assertTrue("bdes2.size == 1", activity2.getBreakdownElements().size() == 1) ;

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testRemoveFromActivity() {
		// Rk: the setUp method is called here.

		Activity activity = new Activity() ;
		activity.setGuid("idEPF") ;
		activity.setName("name1") ;
		activity.setDescription("description1") ;
		activity.setPrefix("prefix1") ;
		activity.setIsOptional(true) ;
		activity.setIsPlanned(false) ;
		activity.setHasMultipleOccurrences(false) ;
		activity.setIsEvenDriven(true) ;
		activity.setIsOngoing(false) ;
		activity.setIsRepeatable(true) ;
		this.breakdownElement.addSuperActivity(activity) ;
		this.breakdownElement.removeSuperActivity(activity) ;

		assertTrue(this.breakdownElement.getSuperActivities().isEmpty()) ;
		assertTrue(activity.getBreakdownElements().isEmpty()) ;

		// Rk: the tearDown method is called here.
	}

	@Test
	public void testRemoveFromAllActivities() {
		// Rk: the setUp method is called here.

		Activity activity = new Activity() ;
		activity.setGuid("idEPF") ;
		activity.setName("name1") ;
		activity.setDescription("description1") ;
		activity.setPrefix("prefix1") ;
		activity.setIsOptional(true) ;
		activity.setIsPlanned(false) ;
		activity.setHasMultipleOccurrences(false) ;
		activity.setIsEvenDriven(true) ;
		activity.setIsOngoing(false) ;
		activity.setIsRepeatable(true) ;

		Activity activity2 = new Activity() ;
		activity2.setGuid("idEPF") ;
		activity2.setName("name2") ;
		activity2.setDescription("description2") ;
		activity2.setPrefix("prefix2") ;
		activity2.setIsOptional(true) ;
		activity2.setIsPlanned(false) ;
		activity2.setHasMultipleOccurrences(false) ;
		activity2.setIsEvenDriven(true) ;
		activity2.setIsOngoing(false) ;
		activity2.setIsRepeatable(true) ;

		Set<Activity> set = new HashSet<Activity>() ;
		set.add(activity) ;
		set.add(activity2) ;

		this.breakdownElement.addAllSuperActivities(set) ;
		this.breakdownElement.removeAllSuperActivities() ;

		assertTrue(this.breakdownElement.getSuperActivities().isEmpty()) ;
		assertTrue(activity.getBreakdownElements().isEmpty()) ;
		assertTrue(activity2.getBreakdownElements().isEmpty()) ;

		// Rk: the tearDown method is called here.
	}
}
