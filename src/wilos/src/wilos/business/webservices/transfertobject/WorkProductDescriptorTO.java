/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2007-2008 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

import wilos.model.spem2.workproduct.WorkProductDescriptor;

/**
 * 
 * 
 */
public class WorkProductDescriptorTO extends WorkProductDescriptor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6666666666666666666L;

	/** Creates a new instance of WorkProductDescriptorTO */
	public WorkProductDescriptorTO() {
	}

	public WorkProductDescriptorTO(WorkProductDescriptor myWorkProductDescriptor) {
		this.setName(myWorkProductDescriptor.getName());
		this.setPrefix(myWorkProductDescriptor.getPrefix());
		this.setPresentationName(myWorkProductDescriptor.getPresentationName());
		this.setGuid(myWorkProductDescriptor.getGuid());
		this.setDescription(myWorkProductDescriptor.getDescription());

		this.setMainDescription(myWorkProductDescriptor.getMainDescription());
		this.setKeyConsiderations(myWorkProductDescriptor.getKeyConsiderations());
		this.setInsertionOrder(myWorkProductDescriptor.getInsertionOrder());

		
		if (myWorkProductDescriptor.getWorkProductDefinition() != null)
			this.setWorkProductDefinition(new WorkProductDefinitionTO(myWorkProductDescriptor
					.getWorkProductDefinition()));
		if (this.getWorkProductDefinition() != null
				&& this.getDescription().length() == 0)
			this.setDescription(this.getWorkProductDefinition().getDescription());
	}

}
