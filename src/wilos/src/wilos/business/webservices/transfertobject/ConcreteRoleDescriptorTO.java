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

import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;

public class ConcreteRoleDescriptorTO extends ConcreteRoleDescriptor implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6193498903419221055L;

	/** Creates a new instance of ParticipantTO */
	public ConcreteRoleDescriptorTO() {

	}

	public ConcreteRoleDescriptorTO(
			ConcreteRoleDescriptor myConcreteRoleDescriptor) {
		this.setId(myConcreteRoleDescriptor.getId());
		this.setConcreteName(myConcreteRoleDescriptor.getConcreteName());
		this.setInstanciationOrder(myConcreteRoleDescriptor.getInstanciationOrder());
		
		if (myConcreteRoleDescriptor.getRoleDescriptor() != null)
			this.setRoleDescriptor(new RoleDescriptorTO(
					myConcreteRoleDescriptor.getRoleDescriptor()));

		Set<ConcreteActivity> concreteActivitys = ConcreteActivityTO
				.getConcreteActivities(myConcreteRoleDescriptor);
		this.setSuperConcreteActivities(concreteActivitys);

		Set<ConcreteTaskDescriptor> concreteTaskDescriptors = new HashSet<ConcreteTaskDescriptor>();
		for (ConcreteTaskDescriptor ctd : myConcreteRoleDescriptor
				.getPrimaryConcreteTaskDescriptors()) {
			concreteTaskDescriptors.add(new ConcreteTaskDescriptorTO(ctd));
		}
		this.setPrimaryConcreteTaskDescriptors(concreteTaskDescriptors);

	}
}
