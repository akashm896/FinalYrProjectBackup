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

package wilos.test.business.services.misc.wilosuser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.misc.wilosuser.LoginService;
import wilos.hibernate.misc.wilosuser.ParticipantDao;
import wilos.model.misc.wilosuser.Participant;
import wilos.model.misc.wilosuser.WilosUser;
import wilos.test.TestConfiguration;

public class LoginServiceTest {

	private LoginService loginService = (LoginService) TestConfiguration
			.getInstance().getApplicationContext().getBean("LoginService");

	private ParticipantDao participantDao = (ParticipantDao) TestConfiguration
			.getInstance().getApplicationContext().getBean("ParticipantDao");

	private Participant p;


	@Before
	public void setUp() {
		this.p = new Participant();
		this.p.setLogin("login");
		this.p.setPassword("passwd");
	}

	@After
	public void tearDown() {
		this.p = null;
	}

	@Test
	public void testGetAuthentifiedUser() {
		// test for a participant

		this.participantDao.saveOrUpdateParticipant(this.p);
		WilosUser w = this.loginService.getAuthentifiedUser("login", "passwd");
		assertNotNull(w);
		assertTrue(w.getLogin().equals("login"));
		assertTrue(w.getPassword().equals("passwd"));
		assertTrue(w instanceof Participant);
		this.participantDao.deleteParticipant(this.p);
	}

	@Test
	public void testLoginExist() {
		this.participantDao.saveOrUpdateParticipant(this.p);
		boolean trouve = this.loginService.loginExist("login");
		assertTrue(trouve);
		this.participantDao.deleteParticipant(this.p);
	}

	@Test
	public void testIsParticipant() {
		this.participantDao.saveOrUpdateParticipant(this.p);
		assertTrue(this.loginService.isParticipant(p));


		this.participantDao.deleteParticipant(this.p);
	}

}
