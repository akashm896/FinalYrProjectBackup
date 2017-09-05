package wilos.test.business.services.misc.wilosuser;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.misc.wilosuser.WilosUserService;
import wilos.hibernate.misc.wilosuser.WilosUserDao;
import wilos.model.misc.wilosuser.WilosUser;
import wilos.test.TestConfiguration;

public class WilosUserServiceTest {

	private WilosUserService wus = (WilosUserService) TestConfiguration
		.getInstance().getApplicationContext().getBean("WilosUserService");
	
	private WilosUserDao wud = (WilosUserDao) TestConfiguration
		.getInstance().getApplicationContext().getBean("WilosUserDao");

	private WilosUser wu1, wu2, wu3;
	
	@Before
    public void setUp() {
		
    }

    @After
    public void tearDown() {
    }
    
	@Test
	public void testGetUser() {
		List<WilosUser> lstdao = this.wud.getAllWilosUsers();
		List<WilosUser> lstService = this.wus.getUser();
		assertNotNull (lstdao);
		assertNotNull(lstService);
		assertTrue (lstdao.size() == lstService.size());
		for (int i = 0; i<lstService.size();i++)
			assertTrue(lstdao.get(i).getId().toString()
						.equals(lstService.get(i).getId().toString()));
	}

	@Test
	public void testSaveWilosUser() {
		this.wu1 = new WilosUser();
		this.wu1.setLogin("wu1");
		this.wu1.setName("wu1");
		this.wu1.setFirstname("wu1");
		this.wu1.setEmailAddress("wu1@wu1.com");
		this.wu1.setNewPassword("");
		this.wu1.setPassword("passwd");
		//insert without password encoding
		this.wus.saveWilosUser(this.wu1);
		this.wu2 = this.wud.getWilosUser("wu1");
		assertTrue(this.wu2.getLogin().toString()
					.equals(this.wu1.getLogin().toString()));
		assertTrue(this.wu2.getPassword().toString().equals("passwd"));
		this.wud.deleteWilosUser(this.wu1);
		
		this.wu2=null;
		
		//insert
		this.wu3 = new WilosUser();
		this.wu3.setLogin("wu3");
		this.wu3.setName("wu3");
		this.wu3.setFirstname("wu3");
		this.wu3.setEmailAddress("wu3@wu3.com");
		this.wu3.setNewPassword("passwd");
		this.wu3.setPassword("passwd");
		this.wus.saveWilosUser(this.wu3);
		this.wu2 = this.wud.getWilosUser("wu3");
		assertTrue(this.wu2.getLogin().toString()
					.equals(this.wu3.getLogin().toString()));
		assertTrue(this.wu2.getPassword().toString()
					.equals(this.wu3.getPassword().toString()));
		assertFalse(this.wu2.getPassword().toString().equals("passwd"));
		this.wud.deleteWilosUser(this.wu3);
	}

	@Test
	public void testDeleteWilosuser() {
		this.wu1 = new WilosUser();
		this.wu1.setLogin("wu1");
		this.wu1.setName("wu1");
		this.wu1.setFirstname("wu1");
		this.wu1.setEmailAddress("wu1@wu1.com");
		this.wu1.setNewPassword("");
		
		this.wus.saveWilosUser(this.wu1);
		this.wu2 = this.wud.getWilosUser("wu1");
		assertNotNull(this.wu2);
		this.wud.deleteWilosUser(this.wu1);
		this.wu2 = this.wud.getWilosUser("wu1");
		assertNull(this.wu2);
	}

	@Test
	public void testGetSimpleUser() {
		this.wu1 = new WilosUser();
		this.wu1.setLogin("wu1");
		this.wu1.setName("wu1");
		this.wu1.setFirstname("wu1");
		this.wu1.setEmailAddress("wu1@wu1.com");
		this.wu1.setNewPassword("");
		this.wus.saveWilosUser(this.wu1);
		
		String id = this.wud.getWilosUser("wu1").getId();
		this.wu2 = this.wus.getSimpleUser(id);
		assertNotNull(this.wu2);
		assertTrue(this.wu2.getId().toString().equals(id));
		assertTrue(this.wu2.getName().toString().equals("wu1"));
		assertTrue(this.wu2.getLogin().toString().equals("wu1"));
		assertTrue(this.wu2.getFirstname().toString().equals("wu1"));
		
		this.wud.deleteWilosUser(this.wu1);		
	}

	@Test
	public void testGetUserByRole() {
		//participant list
		List<WilosUser> lst_wus_base = this.wus.getUserByRole("0");
		//participant
		this.wu1 = new WilosUser();
		this.wu1.setLogin("wu1");
		this.wu1.setName("wu1");
		this.wu1.setFirstname("wu1");
		this.wu1.setEmailAddress("wu1@wu1.com");
		this.wu1.setNewPassword("passwd");
		this.wu1.setRole_id("0");
		this.wus.saveWilosUser(this.wu1);
		//new participant list
		List<WilosUser> lst_wus = this.wus.getUserByRole("0");
		
		assertNotNull(lst_wus);
		assertEquals(lst_wus.size(), lst_wus_base.size()+1);
		
		// clear data base
		this.wud.deleteWilosUser(this.wu1);
	}
	
	@Test
	public void testGetUserByLogin() {

		//Create a user
		this.wu1 = new WilosUser();
		this.wu1.setLogin("wu1");
		this.wu1.setName("wu1");
		this.wu1.setFirstname("wu1");
		this.wu1.setEmailAddress("wu1@wu1.com");
		this.wu1.setNewPassword("passwd");
		this.wu1.setRole_id("0");
		String keyGenerated = this.wu1.generateNewPassword();
		this.wu1.setKeyPassword(keyGenerated);
		this.wus.saveWilosUser(this.wu1);		
		//get the user by his login
		WilosUser wures = this.wus.getUserByLogin("wu1");
		//verify if the method is ok		
		assertNotNull(wures);
		assertEquals(wures.getEmailAddress(), wu1.getEmailAddress());
		assertEquals(wures.getLogin(), wu1.getLogin());
		assertEquals(wures.getName(), wu1.getName());
		assertEquals(wures.getFirstname(), wu1.getFirstname());
		assertEquals(wures.getPassword(), wu1.getPassword());
		assertEquals(wures.getKeyPassword(), wu1.getKeyPassword());
		
		// clear the created user for the test
		this.wud.deleteWilosUser(this.wu1);
	}
	
	@Test
	public void testGetUserByEmail() {

		//Create a user
		this.wu1 = new WilosUser();
		this.wu1.setLogin("wu1");
		this.wu1.setName("wu1");
		this.wu1.setFirstname("wu1");
		this.wu1.setEmailAddress("wu1@wu1.com");
		this.wu1.setNewPassword("passwd");
		this.wu1.setRole_id("0");
		String keyGenerated = this.wu1.generateNewPassword();
		this.wu1.setKeyPassword(keyGenerated);
		this.wus.saveWilosUser(this.wu1);		
		//get the user by his email
		WilosUser wures = this.wus.getUserByEmail("wu1@wu1.com");
		//verify if the method is ok		
		assertNotNull(wures);
		assertEquals(wures.getEmailAddress(), wu1.getEmailAddress());
		assertEquals(wures.getLogin(), wu1.getLogin());
		assertEquals(wures.getName(), wu1.getName());
		assertEquals(wures.getFirstname(), wu1.getFirstname());
		assertEquals(wures.getPassword(), wu1.getPassword());
		assertEquals(wures.getKeyPassword(), wu1.getKeyPassword());
		
		// clear the created user for the test
		this.wud.deleteWilosUser(this.wu1);
	}
	
}
