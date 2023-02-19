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

package wilos.model.spem2.checklist;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.spem2.guide.Guidance;
import wilos.model.spem2.section.Section;

/**
 * 
 * A CheckList is a specific type of {@link Guidance} that identifies a series of items
 * that need to be completed or verified. Checklists are often used in reviews
 * such as walkthroughs or inspections.
 * <p />
 * It's an element of the SPEM2 specification of the OMG
 *         organization (http://www.omg.org/).
 * 
 */
public class CheckList extends Guidance {

	/* Collection of Sections */
	Set<Section> sections;

	/**
	 * Class constructor
	 */
	public CheckList() {
		this.sections = new HashSet<Section>();
	}

	/**
	 * Returns a copy of the current instance of CheckList
	 * 
	 * @return a copy of the CheckList
	 * @throws CloneNotSupportedException
	 */
	@Override
	public CheckList clone() throws CloneNotSupportedException {
		CheckList checklist = new CheckList();
		checklist.copy(this);
		return checklist;
	}

	/**
	 * Copy the values of the specified CheckList into the current
	 * instance of the class.
	 * 
	 * @param _checklist
	 *            the CheckList to copy
	 */
	protected void copy(final CheckList _checklist) {
		super.copy(_checklist);
		this.getSections().addAll(_checklist.getSections());
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the CheckList.
	 * 
	 * @param obj
	 *            the Object to be compare to the CheckList
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof CheckList == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}

		CheckList checklist = (CheckList) obj;
		return new EqualsBuilder().appendSuper(super.equals(checklist)).append(
				this.getSections(), checklist.getSections()).isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of CheckList
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.toHashCode();
	}

	/**
	 * Returns the sections of the CheckList.
	 * 
	 * @return the sections
	 */
	public Set<Section> getSections() {
		return sections;
	}

	/**
	 * Sets the sections of the CheckList.
	 * 
	 * @param sections
	 *            the sections to set
	 */
	public void setSections(Set<Section> _sections) {
		this.sections = _sections;
	}

	/**
	 * Adds a Section to the CheckList
	 * 
	 * @param _section the Section to add
	 */
	public void addSection(Section _section) {
		this.getSections().add(_section);
	}

	/**
	 * Removes a Section from the CheckList
	 * 
	 * @param _section the Section to set
	 */
	public void removeSection(Section _section) {
		this.getSections().remove(_section);
	}

	/**
	 * Assigns the CheckList to all the Sections of the Set in parameter
	 * 
	 * @param _sections the Section related to the CheckList
	 */
	public void addAllSections(Set<Section> _sections) {
		for (Section s : _sections) {
			s.addCheckList(this);
		}
	}

	/**
	 * Removes the CheckList from all the Sections of the Set in parameter
	 */
	public void removeAllSections() {
		for (Section s : this.getSections()) {
			s.removeCheckList(this);
		}
		this.getSections().clear();
	}

}
