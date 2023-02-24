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

import wilos.model.spem2.checklist.CheckList;
import wilos.model.spem2.section.Section;

public class CheckListTO extends CheckList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2651484866248124962L;

	/** Creates a new instance of CheckList */
	public CheckListTO() {

	}

	public CheckListTO(CheckList myCheckList) {
		this.setName(myCheckList.getName());
		this.setGuid(myCheckList.getGuid());
		this.setDescription(myCheckList.getDescription());
		this.setType(myCheckList.getType());
		this.setPresentationName(myCheckList.getPresentationName());
		this.setAttachment(myCheckList.getAttachment());

		this.setMainDescription(myCheckList.getMainDescription());
		this.setKeyConsiderations(myCheckList.getKeyConsiderations());

		// set the sections
		Set<Section> sections = new HashSet<Section>();
		for (Section s : myCheckList.getSections()) {
			sections.add(new SectionTO(s));
		}
		this.setSections(sections);
	}
}
