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

import wilos.model.spem2.role.RoleDescriptor;

/**
 * 
 * @author toine
 */
public class RoleDescriptorTO extends RoleDescriptor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3467847737413616456L;

	/** Creates a new instance of RoleDescriptorTO */
	public RoleDescriptorTO() {
	}

	public RoleDescriptorTO(RoleDescriptor myRoleDescriptor) {
		this.setName(myRoleDescriptor.getName());
		this.setPrefix(myRoleDescriptor.getPrefix());
		this.setPresentationName(myRoleDescriptor.getPresentationName());
		this.setGuid(myRoleDescriptor.getGuid());
		this.setDescription(myRoleDescriptor.getDescription());

		this.setMainDescription(myRoleDescriptor.getMainDescription());
		this.setKeyConsiderations(myRoleDescriptor.getKeyConsiderations());
		this.setInsertionOrder(myRoleDescriptor.getInsertionOrder());

		/*
		 * for (TaskDescriptor td : myRoleDescriptor.getPrimaryTasks()) {
		 * this.addPrimaryTask(new TaskDescriptorTO(td)); }
		 */

		/*
		 * for (TaskDescriptor td : myRoleDescriptor.getAdditionalTasks()) {
		 * this.addAdditionalTask(new TaskDescriptorTO(td)); }
		 */

		if (myRoleDescriptor.getRoleDefinition() != null)
			this.setRoleDefinition(new RoleDefinitionTO(myRoleDescriptor
					.getRoleDefinition()));
		if (this.getRoleDefinition() != null
				&& this.getDescription().length() == 0)
			this.setDescription(this.getRoleDefinition().getDescription());
	}

}
