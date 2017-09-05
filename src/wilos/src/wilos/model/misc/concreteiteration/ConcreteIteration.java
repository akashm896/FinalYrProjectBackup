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

package wilos.model.misc.concreteiteration;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.project.Project;
import wilos.model.spem2.iteration.Iteration;

/**
 * 
 * A ConcreteIteration is a specific {@link Iteration} for a {@link Project}.
 * 
 */
public class ConcreteIteration extends ConcreteActivity implements Cloneable {

	private Iteration iteration;

	/**
	 * Default class constructor.
	 */
	public ConcreteIteration() {
		super();
	}

	/**
	 * Returns a copy of the current instance of ConcreteIteration
	 * 
	 * @return a copy of the ConcreteIteration
	 * @throws CloneNotSupportedException
	 */
	@Override
	public ConcreteIteration clone() throws CloneNotSupportedException {
		ConcreteIteration concreteIteration = new ConcreteIteration();
		concreteIteration.copy(this);
		return concreteIteration;
	}

	/**
	 * Copy the values of the specified ConcreteIteration into the current
	 * instance.
	 * 
	 * @param _iteration
	 *            the ConcreteIteration to copy.
	 */
	protected void copy(final ConcreteIteration _concreteIteration) {
		super.copy(_concreteIteration);
		this.iteration = _concreteIteration.getIteration();
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the class.
	 * 
	 * @param obj
	 *            the Object to be compare to the ConcreteIteration
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof ConcreteIteration == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		ConcreteIteration concreteIteration = (ConcreteIteration) obj;
		return new EqualsBuilder().appendSuper(super.equals(concreteIteration))
				.append(this.iteration, concreteIteration.iteration).isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of ConcreteIteration
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(this.iteration).toHashCode();
	}

	/**
	 * Attach an Iteration to the ConcreteIteration
	 * 
	 * @param _iteration
	 *            the Iteration to be attached
	 */
	public void addIteration(Iteration _iteration) {
		this.iteration = _iteration;
		_iteration.getConcreteIterations().add(this);
	}

	/**
	 * Detach an Iteration from the Collection of the ConcreteIteration
	 * 
	 * @param _iteration
	 *            the Iteration to be detached
	 */
	public void removeIteration(Iteration _iteration) {
		_iteration.getConcreteIterations().remove(this);
		this.iteration = null;
	}

	/**
	 * Get the Iteration reattached to the ConcreteIteration
	 * 
	 * @return the Iteration of the ConcreteIteration
	 */
	public Iteration getIteration() {
		return iteration;
	}

	/**
	 * Set the Iteration reattached to the ConcreteIteration
	 * 
	 * @param iteration
	 *            the Iteration to be reattached to the ConcreteIteration
	 */
	public void setIteration(Iteration iteration) {
		this.iteration = iteration;
	}
}
