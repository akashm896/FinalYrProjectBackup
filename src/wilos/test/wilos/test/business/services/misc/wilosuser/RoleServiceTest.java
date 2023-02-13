/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2008-2009 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

package wilos.test.business.services.misc.wilosuser;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.misc.wilosuser.RoleService;
import wilos.hibernate.misc.wilosuser.WilosUserDao;
import wilos.model.misc.wilosuser.WilosUser;
import wilos.test.TestConfiguration;

public class RoleServiceTest {

	private RoleService roleService = (RoleService) TestConfiguration
			.getInstance().getApplicationContext().getBean("RoleService");
	private WilosUserDao wud = (WilosUserDao) TestConfiguration
	.getInstance().getApplicationContext().getBean("WilosUserDao");

	private WilosUser wu;
	
	@Before
    public void setUp() {
		wu = new WilosUser();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetRoleUser()
    {
    	List<WilosUser> lst = this.roleService.getRoleUser(wud.getAllWilosUsers());
    	assertNotNull (lst);
    	this.wud.saveOrUpdateWilosUser(wu);
    	List<WilosUser> lstTmp = this.roleService.getRoleUser(wud.getAllWilosUsers());
    	assertNotNull (lstTmp);
    	assertTrue(lstTmp.size()>lst.size());
    	this.wud.deleteWilosUser(wu);
    }
    
    @Test
    public void testGetARoleForAnUser(){
    	String role = this.roleService.getARoleForAnUser("0");
    	assertNotNull(role);
    	assertEquals(role, "participant");
    }
	
}
