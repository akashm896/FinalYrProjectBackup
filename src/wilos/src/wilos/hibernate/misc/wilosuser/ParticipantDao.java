/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) Sebastien BALARD <sbalard@wilos-project.org>
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

package wilos.hibernate.misc.wilosuser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.misc.wilosuser.Participant;

/**
 * ParticipantDao manage requests from the system to store Participant into the
 * database.
 */
public class ParticipantDao extends HibernateDaoSupport {

	/**
	 * Saves or updates a Participant
	 * 
	 * @param _participant
	 *            The Participant to be saved or updated
	 */
	public String saveOrUpdateParticipant(Participant _participant) {
		if (_participant != null) {
			this.getHibernateTemplate().saveOrUpdate(_participant);
			return _participant.getId();
		}
		return "";
	}

	/**
	 * Returns a list of all the Participant
	 * 
	 * @return A list of all the Participant
	 */
	public List<Participant> getAllParticipants() {
		List<Participant> participants = new ArrayList<Participant>();
		for (Object obj : this.getHibernateTemplate()
				.loadAll(Participant.class)) {
			Participant p = (Participant) obj;
			participants.add(p);
		}
		return participants;
	}

	/**
	 * Returns the Participant which has the specified ID
	 * 
	 * @param _id
	 *            The wanted Participant's ID
	 * @return The wanted Participant
	 */
	public Participant getParticipantById(String _id) {
		if (_id != null) {
			if (!_id.equals(""))
				return (Participant) this.getHibernateTemplate().get(
						Participant.class, _id);
		}

		return null;
	}

	/**
	 * Returns the Participant which has the specified login
	 * 
	 * @param _login
	 *            The wanted Participant's login
	 * @return The wanted Participant
	 */
	public Participant getParticipant(String _login) {
		List participants = this.getHibernateTemplate().find(
				"from Participant p where p.login=?", _login);
		if (participants.size() > 0) {
			return (Participant) participants.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Deletes the Participant
	 * 
	 * @param _participant
	 *            The Participant to be deleted
	 */
	public void deleteParticipant(Participant _participant) {
		this.getHibernateTemplate().delete(_participant);
	}
}
