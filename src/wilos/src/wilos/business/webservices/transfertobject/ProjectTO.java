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

import wilos.model.misc.project.Project;

public class ProjectTO extends Project implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5796969689713723162L;

	public ProjectTO() {
	}

	public ProjectTO(Project _myproject) {
		this.setProcess(new ProcessTO(_myproject.getProcess()));

		this.setDescription(_myproject.getDescription());
		this.setCreationDate(_myproject.getCreationDate());
		this.setLaunchingDate(_myproject.getLaunchingDate());
		this.setIsFinished(_myproject.getIsFinished());
		this.setId(_myproject.getId());
		this.setConcreteName(_myproject.getConcreteName());
		this.setPlannedStartingDate(_myproject.getPlannedStartingDate());
		this.setPlannedFinishingDate(_myproject.getPlannedFinishingDate());
		this.setPlannedTime(_myproject.getPlannedTime());
	}
}
