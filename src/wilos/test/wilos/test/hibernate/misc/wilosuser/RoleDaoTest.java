package wilos.test.hibernate.misc.wilosuser;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.misc.wilosuser.RoleDao;
import wilos.model.misc.wilosuser.Role;
import wilos.test.TestConfiguration;

public class RoleDaoTest {

	private RoleDao rd = (RoleDao) TestConfiguration
			.getInstance().getApplicationContext().getBean("RoleDao");
	
	@Before
    public void setUp() {
	
    }

    @After
    public void tearDown() {
    }
    
    
    @Test
	public void testGetRole() {
    	List<Role> lst = this.rd.getRole();
    	assertNotNull (lst);
    	assertTrue(lst.size()>0);
	}
    
    @Test
    public void testGetARoleById(){
    	Role r = this.rd.getARoleById("0");
    	assertNotNull (r);

    }

}
