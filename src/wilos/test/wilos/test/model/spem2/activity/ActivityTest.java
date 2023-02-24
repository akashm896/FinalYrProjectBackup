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
package wilos.test.model.spem2.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.breakdownelement.BreakdownElement;
import wilos.model.spem2.guide.Guidance;

public class ActivityTest {

	private Activity activity;

	@Before
	public void setUp() {
		this.activity = new Activity();
		this.activity.setGuid("idEPF1");
		this.activity.setName("name1");
		this.activity.setDescription("description1");
		this.activity.setPrefix("prefix1");
		this.activity.setIsOptional(true);
		this.activity.setIsPlanned(false);
		this.activity.setHasMultipleOccurrences(false);
		this.activity.setIsEvenDriven(true);
		this.activity.setIsOngoing(false);
		this.activity.setIsRepeatable(true);
	}

	@After
	public void tearDown() {
		// None.
	}

	@Test
	public void testClone() {
		try {
			assertEquals(this.activity, this.activity.clone());
		} catch (CloneNotSupportedException e) {
			fail("Error CloneNotSupportedException in the testClone method");
		}
	}

	@Test
	public void testHashCode() {

		Activity tmp = new Activity();
		tmp.setGuid("idEPF1");
		tmp.setName("name1");
		tmp.setDescription("description1");
		tmp.setPrefix("prefix1");
		tmp.setIsOptional(true);
		tmp.setIsPlanned(false);
		tmp.setHasMultipleOccurrences(false);
		tmp.setIsEvenDriven(true);
		tmp.setIsOngoing(false);
		tmp.setIsRepeatable(true);

		assertNotNull(this.activity.hashCode());
		assertNotNull(tmp.hashCode());
		assertEquals(this.activity.hashCode(), tmp.hashCode());
	}

	@Test
	public void testEqualsObject() {

		Activity tmp = new Activity();
		tmp.setGuid("idEPF1");
		tmp.setName("name1");
		tmp.setDescription("description1");
		tmp.setPrefix("prefix1");
		tmp.setIsOptional(true);
		tmp.setIsPlanned(false);
		tmp.setHasMultipleOccurrences(false);
		tmp.setIsEvenDriven(true);
		tmp.setIsOngoing(false);
		tmp.setIsRepeatable(true);

		assertTrue(this.activity.equals(tmp));

		Activity act = new Activity();
		act.setGuid("idEPF2");
		act.setName("name2");
		act.setDescription("description2");
		act.setPrefix("prefix2");
		act.setIsOptional(true);
		act.setIsPlanned(false);
		act.setHasMultipleOccurrences(false);
		act.setIsEvenDriven(true);
		act.setIsOngoing(false);
		act.setIsRepeatable(true);

		assertFalse(this.activity.equals(act));
	}

	@Test
	public void testAddBreakdownElement() {

		BreakdownElement breakdownElement = new BreakdownElement();
		breakdownElement.setGuid("idEPF1");
		breakdownElement.setName("name1");
		breakdownElement.setDescription("description1");
		breakdownElement.setPrefix("prefix1");
		breakdownElement.setIsOptional(true);
		breakdownElement.setIsPlanned(false);
		breakdownElement.setHasMultipleOccurrences(false);

		this.activity.addBreakdownElement(breakdownElement);

		assertFalse(this.activity.getBreakdownElements().isEmpty());
		assertFalse(breakdownElement.getSuperActivities().isEmpty());
		assertTrue(this.activity.getBreakdownElements().size() == 1);
		assertTrue(breakdownElement.getSuperActivities().size() == 1);
		assertTrue(this.activity.getBreakdownElements().contains(
				breakdownElement));
		assertTrue(breakdownElement.getSuperActivities()
				.contains(this.activity));
	}

	@Test
	public void testAddAllBreakdownElement() {

		BreakdownElement breakdownElement = new BreakdownElement();
		breakdownElement.setGuid("idEPF1");
		breakdownElement.setName("name1");
		breakdownElement.setDescription("description1");
		breakdownElement.setPrefix("prefix1");
		breakdownElement.setIsOptional(true);
		breakdownElement.setIsPlanned(false);
		breakdownElement.setHasMultipleOccurrences(false);

		BreakdownElement tmp = new BreakdownElement();
		tmp.setGuid("idEPF2");
		tmp.setName("name2");
		tmp.setDescription("description1");
		tmp.setPrefix("prefix1");
		tmp.setIsOptional(true);
		tmp.setIsPlanned(false);
		tmp.setHasMultipleOccurrences(false);

		SortedSet<BreakdownElement> set = new TreeSet<BreakdownElement>();
		breakdownElement.setInsertionOrder(1);
		set.add(breakdownElement);
		set.add(tmp);

		this.activity.addAllBreakdownElements(set);

		assertFalse("bdes vides", this.activity.getBreakdownElements()
				.isEmpty());
		assertTrue("bdes != 2", this.activity.getBreakdownElements().size() == 2);
		assertFalse("brk acts vide", breakdownElement.getSuperActivities()
				.isEmpty());
		assertTrue("brk acts = 1",
				breakdownElement.getSuperActivities().size() == 1);
		assertFalse("tmp acts vide", tmp.getSuperActivities().isEmpty());
		assertTrue("tmp acts = 1", tmp.getSuperActivities().size() == 1);
	}

	@Test
	public void testRemoveBreakdownElement() {
		BreakdownElement breakdownElement = new BreakdownElement();
		breakdownElement.setGuid("idEPF1");
		breakdownElement.setName("name1");
		breakdownElement.setDescription("description1");
		breakdownElement.setPrefix("prefix1");
		breakdownElement.setIsOptional(true);
		breakdownElement.setIsPlanned(false);
		breakdownElement.setHasMultipleOccurrences(false);

		this.activity.addBreakdownElement(breakdownElement);
		this.activity.removeBreakdownElement(breakdownElement);

		assertTrue(this.activity.getBreakdownElements().isEmpty());
		assertTrue(breakdownElement.getSuperActivities().isEmpty());
	}

	@Test
	public void testRemoveAllBreakdownElements() {

		BreakdownElement breakdownElement = new BreakdownElement();
		breakdownElement.setGuid("idEPF1");
		breakdownElement.setName("name1");
		breakdownElement.setDescription("description1");
		breakdownElement.setPrefix("prefix1");
		breakdownElement.setIsOptional(true);
		breakdownElement.setIsPlanned(false);
		breakdownElement.setHasMultipleOccurrences(false);

		BreakdownElement tmp = new BreakdownElement();
		tmp.setGuid("idEPF2");
		tmp.setName("name2");
		tmp.setDescription("description1");
		tmp.setPrefix("prefix1");
		tmp.setIsOptional(true);
		tmp.setIsPlanned(false);
		tmp.setHasMultipleOccurrences(false);

		SortedSet<BreakdownElement> set = new TreeSet<BreakdownElement>();
		breakdownElement.setInsertionOrder(1);
		set.add(tmp);	
		set.add(breakdownElement);
		assertTrue(set.size() == 2);

		this.activity.addAllBreakdownElements(set);
		this.activity.removeAllBreakdownElements();

		assertTrue("bdes", this.activity.getBreakdownElements().isEmpty());
		assertTrue("bde.acts", breakdownElement.getSuperActivities().isEmpty());
		assertTrue("tmp.acts", tmp.getSuperActivities().isEmpty());
	}

	@Test
	public void testAddConcreteActivity() {
		ConcreteActivity concreteActivity = new ConcreteActivity();
		concreteActivity.setConcreteName("name");

		this.activity.addConcreteActivity(concreteActivity);

		assertTrue(this.activity.getConcreteActivities().size() == 1);
		assertNotNull(concreteActivity.getActivity());
	}

	@Test
	public void testaddAllConcreteActivity() {
		ConcreteActivity ca1 = new ConcreteActivity();
		ca1.setConcreteName("name1");

		ConcreteActivity ca2 = new ConcreteActivity();
		ca2.setConcreteName("name2");

		Set<ConcreteActivity> set = new HashSet<ConcreteActivity>();
		set.add(ca1);
		set.add(ca2);

		this.activity.addAllConcreteActivities(set);

		assertFalse(this.activity.getConcreteActivities().isEmpty());
		assertEquals(2, this.activity.getConcreteActivities().size());
		assertNotNull(ca1.getActivity());
		assertNotNull(ca2.getActivity());
	}

	@Test
	public void testRemoveConcreteActivity() {
		ConcreteActivity concreteActivity = new ConcreteActivity();
		concreteActivity.setConcreteName("name");

		this.activity.removeConcreteActivity(concreteActivity);

		assertTrue(this.activity.getConcreteActivities().isEmpty());
		assertNull(concreteActivity.getActivity());
	}

	@Test
	public void testRemoveAllConcreteActivities() {
		ConcreteActivity ca1 = new ConcreteActivity();
		ca1.setConcreteName("name1");

		ConcreteActivity ca2 = new ConcreteActivity();
		ca2.setConcreteName("name2");

		Set<ConcreteActivity> set = new HashSet<ConcreteActivity>();
		set.add(ca1);
		set.add(ca2);

		this.activity.addAllConcreteActivities(set);
		assertTrue(this.activity.getConcreteActivities().size() == 2);
		assertNotNull(ca1.getActivity());
		assertNotNull(ca2.getActivity());

		this.activity.removeAllConcreteActivities();
		assertTrue(this.activity.getConcreteActivities().isEmpty());
		assertNull(ca1.getActivity());
		assertNull(ca2.getActivity());
	}

	@Test
	public void testAddGuidance() {
		Guidance guidance = new Guidance();
		guidance.setName("name");

		this.activity.addGuidance(guidance);

		assertTrue(this.activity.getGuidances().size() == 1);
		assertNotNull(guidance.getActivities());
		// derniere maj relation avec guidance
		assertTrue(guidance.getActivities().contains(this.activity));
	}

	@Test
	public void testaddAllGuidances() {
		Guidance g1 = new Guidance();
		g1.setName("name1");

		Guidance g2 = new Guidance();
		g2.setName("name2");

		Set<Guidance> set = new HashSet<Guidance>();
		set.add(g1);
		set.add(g2);

		this.activity.addAllGuidances(set);

		assertFalse(this.activity.getGuidances().isEmpty());
		assertEquals(2, this.activity.getGuidances().size());
		// derniere maj relation avec guidance
		assertTrue(g1.getActivities().contains(this.activity));
		assertTrue(g2.getActivities().contains(this.activity));
	}

	@Test
	public void testRemoveGuidance() {
		Guidance guidance = new Guidance();
		guidance.setName("name");

		this.activity.removeGuidance(guidance);

		assertTrue(this.activity.getGuidances().isEmpty());
		// Derniere maj relation guidance
		assertFalse(guidance.getActivities().contains(this.activity));
	}

	@Test
	public void testRemoveAllGuidances() {
		Guidance g1 = new Guidance();
		g1.setName("name1");

		Guidance g2 = new Guidance();
		g2.setName("name2");

		Set<Guidance> set = new HashSet<Guidance>();
		set.add(g1);
		set.add(g2);

		this.activity.addAllGuidances(set);
		assertTrue(this.activity.getGuidances().size() == 2);
		assertTrue(g1.getActivities().contains(this.activity));
		assertTrue(g2.getActivities().contains(this.activity));

		this.activity.removeAllGuidances();
		assertTrue(this.activity.getGuidances().isEmpty());
		assertFalse(g1.getActivities().contains(this.activity));
		assertFalse(g2.getActivities().contains(this.activity));
	}
	
	/*@Test
	public void testaddOutputWorkProductDescriptor() {
	    WorkProductDescriptor wpd = new WorkProductDescriptor();
		wpd.setName("wpd");

		this.activity.addOutputWorkProductDescriptor(wpd);
		assertTrue(this.activity.getOutputWorkProductDescriptors().contains(wpd));
		assertTrue(this.activity.getOutputWorkProductDescriptors().size() == 1);		
	}
	
	@Test
	public void testaddInputWorkProductDescriptor() {
	    WorkProductDescriptor wpd = new WorkProductDescriptor();
		wpd.setName("wpd");

		this.activity.addInputWorkProductDescriptor(wpd);
		assertTrue(this.activity.getInputWorkProductDescriptors().contains(wpd));
		assertTrue(this.activity.getInputWorkProductDescriptors().size() == 1);		
	}
	
	@Test
	public void testremoveOutputWorkProductDescriptor() {
	    WorkProductDescriptor wpd = new WorkProductDescriptor();
		wpd.setName("wpd");
		
		this.activity.addOutputWorkProductDescriptor(wpd);
		assertTrue(this.activity.getOutputWorkProductDescriptors().contains(wpd));
				
		this.activity.removeOutputWorkProductDescriptor(wpd);
		assertTrue(this.activity.getOutputWorkProductDescriptors().size() == 0);		
	}
	
	@Test
	public void testremoveInputWorkProductDescriptor() {
	    WorkProductDescriptor wpd = new WorkProductDescriptor();
		wpd.setName("wpd");
		
		this.activity.addInputWorkProductDescriptor(wpd);
		assertTrue(this.activity.getInputWorkProductDescriptors().contains(wpd));
				
		this.activity.removeInputWorkProductDescriptor(wpd);
		assertTrue(this.activity.getInputWorkProductDescriptors().size() == 0);		
	}*/
}
