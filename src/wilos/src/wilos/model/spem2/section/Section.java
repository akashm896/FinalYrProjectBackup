/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Mathieu BENOIT <mathieu-benoit@hotmail.fr>
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

package wilos.model.spem2.section;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.spem2.checklist.CheckList;
import wilos.model.spem2.element.Element;

/**
 * 
 * A Section is a special {@link Element} that represents structural subsections
 * of a Content Description's sectionDescription attribute. It is used for
 * either large scale documentation of Elements organized into sections as well
 * as to flexibly add new Sections to Elements using contribution variability
 * added to the Section concept.
 * <p />
 * It's an element of the SPEM2 specification of the OMG organization
 * (http://www.omg.org/).
 * 
 */
public class Section extends Element {

	/* the attached CheckList */
	private CheckList checklist;

	/**
	 * Class constructor
	 */
	public Section() {
		super();
	}

	/**
	 * Copy the values of the specified Section into the current instance of the
	 * class.
	 * 
	 * @param _section
	 *            the Section to copy
	 */
	protected void copy(final Section _section) {
		super.copy(_section);
		this.setChecklist(_section.getChecklist());
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the Section.
	 * 
	 * @param obj
	 *            the Object to be compare to the Section
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Section == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}

		Section checklist = (Section) obj;
		return new EqualsBuilder().appendSuper(super.equals(checklist)).append(
				this.getChecklist(), checklist.getChecklist()).isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of Section
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.toHashCode();
	}

	/**
	 * Returns the CheckList the Section belongs to
	 * 
	 * @return the related CheckList
	 */
	public CheckList getChecklist() {
		return checklist;
	}

	/**
	 * Assigns the Section to the specified CheckList
	 * 
	 * @param checklist
	 *            the CheckList to assigned
	 */
	public void setChecklist(CheckList checklist) {
		this.checklist = checklist;
	}

	/**
	 * Adds the Section to the specified CheckList
	 * 
	 * @param _checklist
	 *            the CheckList to be related to
	 */
	public void addCheckList(CheckList _checklist) {
		_checklist.getSections().add(this);
	}

	/**
	 * Removes the Section from the specified CheckList
	 * 
	 * @param _checklist
	 *            the CheckList to be unlinked to
	 */
	public void removeCheckList(CheckList _checklist) {
		_checklist.getSections().remove(this);
	}

}
