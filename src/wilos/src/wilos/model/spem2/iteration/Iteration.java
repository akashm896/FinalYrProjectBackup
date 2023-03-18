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

package wilos.model.spem2.iteration;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.misc.concreteiteration.ConcreteIteration;
import wilos.model.spem2.activity.Activity;

/**
 * Iteration is a special {@link Activity}, which prescribes pre-defined values
 * for its instances for the attributes prefix ('Iteration') and isRepeatable
 * ('True'). It has been included into the meta-model for convenience and to
 * provide a special stereotype, because it represents a very commonly used
 * {@link Activity} type. Iteration groups a set of nested Activities that are
 * repeated more than once. It represents an important structuring element to
 * organize work in repetitive cycles
 * <p />
 * It's an element of the SPEM2 specification of the OMG organization
 * (http://www.omg.org/).
 * 
 */
public class Iteration extends Activity implements Cloneable {

	private Set<ConcreteIteration> concreteIterations;

	/**
	 * Default constructor
	 * 
	 */
	public Iteration() {
		super();
		this.concreteIterations = new HashSet<ConcreteIteration>();
	}

	/**
	 * Returns a copy of the current instance of Iteration
	 * 
	 * @return a copy of the Iteration
	 * @throws CloneNotSupportedException
	 */
	@Override
	public Iteration clone() throws CloneNotSupportedException {
		Iteration iteration = new Iteration();
		iteration.copy(this);
		return iteration;
	}

	/**
	 * Copy the values of the specified Iteration into the current instance of
	 * the class.
	 * 
	 * @param _iteration
	 *            The iteration to copy.
	 */
	protected void copy(final Iteration _iteration) {
		super.copy(_iteration);
		this.concreteIterations.addAll(_iteration.getConcreteIterations());
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the Iteration.
	 * 
	 * @param obj
	 *            the Object to be compare to the Iteration
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Iteration == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		Iteration iteration = (Iteration) obj;
		return new EqualsBuilder().appendSuper(super.equals(iteration))
				.isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of Iteration
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.toHashCode();
	}

	/**
	 * Add a concreteIteration to an iteration
	 * 
	 * @param _concreteIteration
	 */
	public void addConcreteIteration(ConcreteIteration _concreteIteration) {
		this.concreteIterations.add(_concreteIteration);
		_concreteIteration.setIteration(this);
	}

	/**
	 * Add a concreteIteration collection to the concreteIteration collection of
	 * a Iteration
	 * 
	 * @param _concreteIteration
	 */
	public void addAllConcreteIterations(
			Set<ConcreteIteration> _concreteIterations) {
		for (ConcreteIteration ci : _concreteIterations) {
			ci.addIteration(this);
		}
	}

	/**
	 * Remove a concreteIteration to its iteration
	 * 
	 * @param _concreteIteration
	 */
	public void removeConcreteIteration(ConcreteIteration _concreteIteration) {
		_concreteIteration.setIteration(null);
		this.concreteIterations.remove(_concreteIteration);
	}

	/**
	 * Remove all concreteIterations to its iteration
	 */
	public void removeAllConcreteIterations() {
		for (ConcreteIteration tmp : this.concreteIterations) {
			tmp.setIteration(null);
		}
		this.concreteIterations.clear();
	}

	/**
	 * Returns the ConcreteIterations related to the Iteration
	 * 
	 * @return a Set of ConcreteIteration
	 */
	public Set<ConcreteIteration> getConcreteIterations() {
		return concreteIterations;
	}

	/**
	 * Sets the Collection of ConcreteIterations related to the Iteration
	 * 
	 * @param _concreteIterations the Collection of ConcreteIterations to set
	 */
	public void setConcreteIterations(Set<ConcreteIteration> _concreteIterations) {
		this.concreteIterations = _concreteIterations;
	}

}
