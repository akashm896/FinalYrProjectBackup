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

package wilos.business.webservices.transfertobject;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;
import wilos.model.misc.wilosuser.Participant;

/**
 * 
 * @author toine
 */
public class ParticipantTO extends Participant implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -175514417997688236L;

    /** Creates a new instance of ParticipantTO */
    public ParticipantTO() {

    }

    public ParticipantTO(Participant myParticipant) {
	this.setName(myParticipant.getName());
	this.setLogin(myParticipant.getLogin());
	this.setPassword(myParticipant.getPassword());
	this.setFirstname(myParticipant.getFirstname());
	this.setEmailAddress(myParticipant.getEmailAddress());
	this.setRole_id(myParticipant.getRole_id());
	this.setDeleted(myParticipant.getDeleted());
	this.setKeyPassword(myParticipant.getKeyPassword());

	Set<ConcreteRoleDescriptor> concreteRoleDescriptors = new HashSet<ConcreteRoleDescriptor>();
	for (ConcreteRoleDescriptor crd : myParticipant
		.getConcreteRoleDescriptors()) {
	    concreteRoleDescriptors.add(new ConcreteRoleDescriptorTO(crd));
	}
	this.setConcreteRoleDescriptors(concreteRoleDescriptors);

	Set<ConcreteWorkProductDescriptor> concreteWorkProductDescriptors = new HashSet<ConcreteWorkProductDescriptor>();
	for (ConcreteWorkProductDescriptor cwpd : myParticipant
		.getConcreteWorkProductDescriptors()) {
	    concreteWorkProductDescriptors
		    .add(new ConcreteWorkProductDescriptorTO(cwpd));
	}
	this.setConcreteWorkProductDescriptors(concreteWorkProductDescriptors);

    }

}
