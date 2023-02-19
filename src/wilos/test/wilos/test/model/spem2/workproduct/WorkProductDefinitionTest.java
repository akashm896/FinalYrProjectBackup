package wilos.test.model.spem2.workproduct;

import java.util.HashSet;
import java.util.Set;

import wilos.model.spem2.guide.Guidance;
import wilos.model.spem2.workproduct.WorkProductDescriptor;
import wilos.model.spem2.workproduct.WorkProductDefinition;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;


public class WorkProductDefinitionTest extends TestCase {
	
	private WorkProductDefinition workProductDefinition;
	
	public static final String NAME = "name";
	
	public static final String NAME2 = "name1";

	public static final String DESCRIPTION = "workProductDescriptor description";

	public static final String DESCRIPTION2 = "description";
	
	@Before
	public void setUp() {
		this.workProductDefinition = new WorkProductDefinition();
		this.workProductDefinition.setDescription(DESCRIPTION);
		this.workProductDefinition.setName(NAME);
	}

	@After
	public void tearDown() {
		// None.
	}

	public final void testClone() {
		// Rk: the setUp method is called here.

		try {
			assertEquals(this.workProductDefinition.clone(), this.workProductDefinition);
		} catch (CloneNotSupportedException e) {
			fail("Error CloneNotSupportedException in the testClone method");
		}

		// Rk: the tearDown method is called here.
	}
	
	@Test
	public void testHashCode() {

		WorkProductDefinition wpd = new WorkProductDefinition();
		wpd.setDescription(DESCRIPTION);
		wpd.setName(NAME);

		assertNotNull(this.workProductDefinition.hashCode());
		assertNotNull(wpd.hashCode());
		assertEquals(this.workProductDefinition.hashCode(), wpd.hashCode());
	}

	
	@Test
	public void testEquals() {
		WorkProductDefinition tmp = null;
		try {
			tmp = this.workProductDefinition.clone();
		} catch (CloneNotSupportedException e) {
			fail("Error CloneNotSupportedException in the testEquals method");
		}
		assertTrue(this.workProductDefinition.equals(tmp));

		WorkProductDefinition workProduct = new WorkProductDefinition();
		workProduct.setName("name2");
		workProduct.setDescription(DESCRIPTION);

		assertFalse(this.workProductDefinition.equals(workProduct));
	}
	
	@Test
	public void testAddWorkProductDescriptor() {
		WorkProductDescriptor workproduct = new WorkProductDescriptor();
		workproduct.setName(NAME);
		workproduct.setDescription(DESCRIPTION);

		this.workProductDefinition.addWorkProductDescriptor(workproduct);
		assertFalse(this.workProductDefinition.getWorkProductDescriptors().isEmpty());
		assertTrue(this.workProductDefinition.getWorkProductDescriptors().size() == 1);
	}
	
	@Test
	public void testRemoveWorkProductDescriptor() {
		WorkProductDescriptor workproduct = new WorkProductDescriptor();
		workproduct.setName(NAME);
		workproduct.setDescription(DESCRIPTION);

		this.workProductDefinition.addWorkProductDescriptor(workproduct);
		assertFalse(this.workProductDefinition.getWorkProductDescriptors().isEmpty());

		this.workProductDefinition.removeWorkProductDescriptor(workproduct);
		assertTrue(this.workProductDefinition.getWorkProductDescriptors().isEmpty());
	}
	
	@Test
	public void testRemoveAllWorkProductDescriptors() {
		WorkProductDescriptor workproduct = new WorkProductDescriptor();
		workproduct.setName(NAME);
		workproduct.setDescription(DESCRIPTION);

		WorkProductDescriptor tmp = new WorkProductDescriptor();
		tmp.setName(NAME2);
		tmp.setDescription(DESCRIPTION);

		Set<WorkProductDescriptor> set = new HashSet<WorkProductDescriptor>();
		set.add(workproduct);
		set.add(tmp);

		this.workProductDefinition.addAllWorkProductDescriptors(set);
		assertNotNull(workproduct.getWorkProductDefinition());
		assertNotNull(tmp.getWorkProductDefinition());
		assertTrue(this.workProductDefinition.getWorkProductDescriptors().size() == set
				.size());

		this.workProductDefinition.removeAllWorkProductDescriptors();
		assertNull(workproduct.getWorkProductDefinition());
		assertNull(tmp.getWorkProductDefinition());
		assertTrue(this.workProductDefinition.getWorkProductDescriptors().isEmpty());
	}
	
	@Test
	public void testAddToAllWorkProductDescriptors() {
		WorkProductDescriptor workproduct = new WorkProductDescriptor();
		workproduct.setName(NAME2);
		workproduct.setDescription(DESCRIPTION2);

		WorkProductDescriptor tmp = new WorkProductDescriptor();
		tmp.setName(NAME);
		tmp.setDescription(DESCRIPTION);

		Set<WorkProductDescriptor> set = new HashSet<WorkProductDescriptor>();
		set.add(workproduct);
		set.add(tmp);

		this.workProductDefinition.addAllWorkProductDescriptors(set);

		assertFalse(this.workProductDefinition.getWorkProductDescriptors().isEmpty());
		assertTrue(this.workProductDefinition.getWorkProductDescriptors().size() == 2);
		assertNotNull(workproduct.getWorkProductDefinition());
		assertNotNull(tmp.getWorkProductDefinition());
	}
	
	@Test
	public void testAddGuidance() {
		Guidance guidance = new Guidance();
		guidance.setName("name");

		this.workProductDefinition.addGuidance(guidance);

		assertTrue(this.workProductDefinition.getGuidances().size() == 1);
		assertTrue(guidance.getWorkProductDefinitions().contains(this.workProductDefinition));
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

		this.workProductDefinition.addAllGuidances(set);

		assertFalse(this.workProductDefinition.getGuidances().isEmpty());
		assertEquals(2, this.workProductDefinition.getGuidances().size());
		assertTrue(g1.getWorkProductDefinitions().contains(this.workProductDefinition));
		assertTrue(g2.getWorkProductDefinitions().contains(this.workProductDefinition));
	}
	
	@Test
	public void testRemoveGuidance() {
		Guidance guidance = new Guidance();
		guidance.setName("name");

		this.workProductDefinition.removeGuidance(guidance);

		assertTrue(this.workProductDefinition.getGuidances().isEmpty());
		assertFalse(guidance.getWorkProductDefinitions().contains(this.workProductDefinition));
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

		this.workProductDefinition.addAllGuidances(set);
		assertTrue(this.workProductDefinition.getGuidances().size() == 2);
		assertTrue(g1.getWorkProductDefinitions().contains(this.workProductDefinition));
		assertTrue(g2.getWorkProductDefinitions().contains(this.workProductDefinition));

		this.workProductDefinition.removeAllGuidances();
		assertTrue(this.workProductDefinition.getGuidances().isEmpty());
		assertFalse(g1.getWorkProductDefinitions().contains(this.workProductDefinition));
		assertFalse(g2.getWorkProductDefinitions().contains(this.workProductDefinition));
	}
}
