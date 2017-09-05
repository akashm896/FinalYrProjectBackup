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

import wilos.model.spem2.task.TaskDescriptor;

/**
 * 
 * @author toine
 */
public class TaskDescriptorTO extends TaskDescriptor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5257048255787493710L;

	/** Creates a new instance of TaskDescriptorTO */
	public TaskDescriptorTO() {
	}

	public TaskDescriptorTO(TaskDescriptor myTaskDescriptor) {
		this.setName(myTaskDescriptor.getName());
		this.setPrefix(myTaskDescriptor.getPrefix());
		this.setGuid(myTaskDescriptor.getGuid());
		this.setDescription(myTaskDescriptor.getDescription());
		this.setPresentationName(myTaskDescriptor.getPresentationName());
		this.setInsertionOrder(myTaskDescriptor.getInsertionOrder());
		this.setMainDescription(myTaskDescriptor.getMainDescription());
		this.setKeyConsiderations(myTaskDescriptor.getKeyConsiderations());

		if (myTaskDescriptor.getTaskDefinition() != null)
			this.setTaskDefinition(new TaskDefinitionTO(myTaskDescriptor
					.getTaskDefinition()));
		if (this.getTaskDefinition() != null
				&& this.getDescription().length() == 0)
			this.setDescription(this.getTaskDefinition().getDescription());

		/*
		 * for (ConcreteTaskDescriptor ct :
		 * myTaskDescriptor.getConcreteTaskDescriptors()) {
		 * this.addConcreteTaskDescriptor(new ConcreteTaskDescriptorTO(ct)); }
		 */

	}

	// TaskDefinition t = new TaskDefinition().
	// TaskDescriptor t = new TaskDescriptor().get

}
