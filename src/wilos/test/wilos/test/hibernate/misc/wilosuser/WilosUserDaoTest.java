/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Sebastien BALARD <sbalard@wilos-project.org>
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

package wilos.test.hibernate.misc.wilosuser;

import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.misc.wilosuser.WilosUserDao;
import wilos.model.misc.wilosuser.Participant;
import wilos.model.misc.wilosuser.WilosUser;
import wilos.test.TestConfiguration;

public class WilosUserDaoTest extends TestCase {

    private WilosUserDao wud = (WilosUserDao) TestConfiguration.getInstance()
	    .getApplicationContext().getBean("WilosUserDao");

    private WilosUser wu;

    @Before
    public void setUp() {
	this.wu = new Participant();
	this.wu.setLogin("loginWU");
	this.wu.setName("nameWU");
    }

    @After
    public void tearDown() {
	this.wu = null;
    }

    @Test
    public void testSaveOrUpdateWilosUser() {
	this.wud.saveOrUpdateWilosUser(this.wu);

	WilosUser wuTmp = this.wud.getWilosUser("loginWU");
	assertNotNull(wuTmp);
	assertTrue(this.wu.getLogin().equals(wuTmp.getLogin()));
	assertTrue(this.wu.getName().equals(wuTmp.getName()));
	this.wud.deleteWilosUser(this.wu);
    }

    @Test
    public void testGetAllWilosUsers() {
	this.wud.saveOrUpdateWilosUser(this.wu);

	List<WilosUser> setTmp = this.wud.getAllWilosUsers();
	assertNotNull(setTmp);
	assertTrue(setTmp.size() >= 1);
	this.wud.deleteWilosUser(this.wu);
    }

    @Test
    public void testGetWilosUser() {
	this.wud.saveOrUpdateWilosUser(this.wu);

	WilosUser wuTmp = this.wud.getWilosUser("loginWU");
	assertNotNull(wuTmp);
	assertEquals(wuTmp.getLogin(), "loginWU");
	assertEquals(wuTmp.getName(), "nameWU");
	this.wud.deleteWilosUser(this.wu);
    }

    @Test
    public void testGetWilosUserById() {
	String id = this.wud.saveOrUpdateWilosUser(this.wu);
	WilosUser wuTmp = this.wud.getWilosUserById(id);
	assertNotNull(wuTmp);
	assertEquals(wuTmp.getId(), id);
	assertEquals(wuTmp.getLogin(), "loginWU");
	assertEquals(wuTmp.getName(), "nameWU");
	this.wud.deleteWilosUser(this.wu);
    }
    

    @Test
    public void testGetWilosUserByRole() {
	//wu de role participant
	this.wu.setRole_id("0");
	//based participant list
	List<WilosUser> lst_base = this.wud.getWilosUserByRole("0");
	// save participant
	this.wud.saveOrUpdateWilosUser(this.wu);
	List<WilosUser> lst_tmp = this.wud.getWilosUserByRole("0");
	assertNotNull(lst_tmp);
	assertEquals(lst_tmp.size(), lst_base.size() + 1);
	// clear database
	this.wud.deleteWilosUser(this.wu);

	/*//wu de role project director
	this.wu.setRole_id("1");
	//based project director list
	lst_base = this.wud.getWilosUserByRole("1");
	// save participant
	this.wud.saveOrUpdateWilosUser(this.wu);
	lst_tmp = this.wud.getWilosUserByRole("1");
	assertNotNull(lst_tmp);
	assertEquals(lst_tmp.size(), lst_base.size()+1);
	// clear database
	this.wud.deleteWilosUser(this.wu);*/
    }

    @Test
    public void testDeleteWilosUser() {
	this.wud.saveOrUpdateWilosUser(this.wu);
	this.wud.deleteWilosUser(this.wu);

	WilosUser wuTmp = this.wud.getWilosUser("loginWU");
	assertNull(wuTmp);
    }

    @Test
    public void testGetUserByLogin() {
	//Create a user
	WilosUser wu1 = new WilosUser();
	wu1.setLogin("wu1");
	wu1.setName("wu1");
	wu1.setFirstname("wu1");
	wu1.setEmailAddress("wu1@wu1.com");
	wu1.setNewPassword("passwd");
	wu1.setRole_id("0");
	this.wud.saveOrUpdateWilosUser(wu1);
	//get the user by his login
	WilosUser wures = this.wud.getUserByLogin("wu1");
	//verify if the method is ok		
	assertNotNull(wures);
	assertEquals(wures.getEmailAddress(), wu1.getEmailAddress());
	assertEquals(wures.getLogin(), wu1.getLogin());
	assertEquals(wures.getName(), wu1.getName());
	assertEquals(wures.getFirstname(), wu1.getFirstname());
	assertEquals(wures.getPassword(), wu1.getPassword());

	// clear the created user for the test
	this.wud.deleteWilosUser(wu1);
    }
    
    @Test
    public void testGetUserByEmail() {
	//Create a user
	WilosUser wu1 = new WilosUser();
	wu1.setLogin("wu1");
	wu1.setName("wu1");
	wu1.setFirstname("wu1");
	wu1.setEmailAddress("wu1@wu1.com");
	wu1.setNewPassword("passwd");
	wu1.setRole_id("0");
	this.wud.saveOrUpdateWilosUser(wu1);
	//get the user by his login
	WilosUser wures = this.wud.getUserByEmail("wu1@wu1.com");
	//verify if the method is ok		
	assertNotNull(wures);
	assertEquals(wures.getEmailAddress(), wu1.getEmailAddress());
	assertEquals(wures.getLogin(), wu1.getLogin());
	assertEquals(wures.getName(), wu1.getName());
	assertEquals(wures.getFirstname(), wu1.getFirstname());
	assertEquals(wures.getPassword(), wu1.getPassword());

	// clear the created user for the test
	this.wud.deleteWilosUser(wu1);
    }

}
