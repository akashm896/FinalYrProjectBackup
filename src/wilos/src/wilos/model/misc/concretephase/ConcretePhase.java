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

package wilos.model.misc.concretephase;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.project.Project;
import wilos.model.spem2.phase.Phase;

/**
 * 
 * A ConcretePhase is a specific {@link Phase} for a {@link Project}.
 * 
 */
public class ConcretePhase extends ConcreteActivity implements Cloneable {

	private Phase phase;

	/**
	 * Default class constructor
	 */
	public ConcretePhase() {
		super();
	}

	/**
	 * Returns a copy of the current instance of ConcretePhase
	 * 
	 * @return a copy of the ConcretePhase
	 * @throws CloneNotSupportedException
	 */
	@Override
	public ConcretePhase clone() throws CloneNotSupportedException {
		ConcretePhase concretePhase = new ConcretePhase();
		concretePhase.copy(this);
		return concretePhase;
	}

	/**
	 * Copy the values of the specified ConcretePhase into the current instance.
	 * 
	 * @param _cnocretePhase
	 *            The concretePhase to copy.
	 */
	protected void copy(final ConcretePhase _concretePhase) {
		super.copy(_concretePhase);
		this.phase = _concretePhase.getPhase();
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the class.
	 * 
	 * @param obj
	 *            the Object to be compare to the ConcretePhase
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof ConcretePhase == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		ConcretePhase concretePhase = (ConcretePhase) obj;
		return new EqualsBuilder().appendSuper(super.equals(concretePhase))
				.append(this.phase, concretePhase.phase).isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of ConcretePhase
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(this.phase).toHashCode();
	}

	/**
	 * Adds a relation between the current instance of ConcretePhase and the
	 * specified Phase.
	 * 
	 * @param _phase
	 *            the Phase to relate to the ConcretePhase
	 */
	public void addPhase(Phase _phase) {
		this.phase = _phase;
		_phase.getConcretePhases().add(this);
	}

	/**
	 * Removes the relation between the current instance of ConcretePhase and
	 * the specified Phase.
	 * 
	 * @param _phase
	 *            the Phase to unlinked from the ConcretePhase
	 */
	public void removePhase(Phase _phase) {
		_phase.getConcretePhases().remove(this);
		this.phase = null;

	}

	/**
	 * Returns the Phase assigned to the ConcretePhase.
	 * 
	 * @return the Phase of the ConcretePhase
	 */
	public Phase getPhase() {
		return phase;
	}

	/**
	 * Initializes the Phase of the ConcretePhase.
	 * 
	 * @param phase
	 *            the phase to set
	 */
	public void setPhase(Phase phase) {
		this.phase = phase;
	}

}
