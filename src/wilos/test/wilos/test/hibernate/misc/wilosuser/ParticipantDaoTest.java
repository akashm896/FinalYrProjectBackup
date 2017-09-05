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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.misc.wilosuser.ParticipantDao;
import wilos.model.misc.wilosuser.Participant;
import wilos.test.TestConfiguration;

public class ParticipantDaoTest {

    private ParticipantDao participantDao = (ParticipantDao) TestConfiguration.getInstance().getApplicationContext()
	    .getBean("ParticipantDao");


    @Before
    public void setUp() {
	
    }

    @After
    public void tearDown() {
	
    }

    @Test
    public void testSaveOrUpdateParticipant() {
	Participant participant = new Participant();
	participant.setLogin("My login 36");
	participant.setName("My name");
	participant.setFirstname("My first name");
	participant.setEmailAddress("My email");
	participantDao.saveOrUpdateParticipant(participant);
	Participant participantTmp = (Participant) this.participantDao.getParticipant("My login 36");
	assertNotNull(participantTmp);
	assertTrue(participant.getLogin().equals(participantTmp.getLogin()));
	assertTrue(participant.getName().equals(participantTmp.getName()));
	this.participantDao.deleteParticipant(participant);
    }

    @Test
    public void testGetAllParticipants() {
	Participant participant = new Participant();
	participant.setLogin("My login 36");
	participant.setName("My name");
	participant.setFirstname("My first name");
	participant.setEmailAddress("My email");
	this.participantDao.saveOrUpdateParticipant(participant);
	// Check the saving.
	List<Participant> participantsTmp = this.participantDao.getAllParticipants();
	assertNotNull(participantsTmp);
	assertTrue(participantsTmp.size() >= 1);
	this.participantDao.deleteParticipant(participant);
    }

    @Test
    public void testGetParticipant() {
	Participant participant = new Participant();
	participant.setLogin("My login 36");
	participant.setName("My name");
	participant.setFirstname("My first name");
	participant.setEmailAddress("My email");
	
	String id = this.participantDao.saveOrUpdateParticipant(participant);
	Participant participantTmp = this.participantDao.getParticipantById(id);

	assertNotNull(participantTmp);
	assertTrue(participantTmp.getLogin().equals(participant.getLogin()));
	assertTrue(participantTmp.getFirstname().equals(participant.getFirstname()));
	assertTrue(participantTmp.getEmailAddress().equals(participant.getEmailAddress()));

	this.participantDao.deleteParticipant(participant);
    }

    @Test
    public void testDeleteParticipant() {
	Participant participant = new Participant();
	participant.setLogin("My login 36");
	participant.setName("My name");
	participant.setFirstname("My first name");
	participant.setEmailAddress("My email");
	
	String id = this.participantDao.saveOrUpdateParticipant(participant);
	this.participantDao.deleteParticipant(participant);
	Participant participantTmp = this.participantDao.getParticipantById(id);
	assertNull(participantTmp);
    }
}
