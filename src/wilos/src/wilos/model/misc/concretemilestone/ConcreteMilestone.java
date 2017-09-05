/*
 * 
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

package wilos.model.misc.concretemilestone;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.project.Project;
import wilos.model.spem2.milestone.Milestone;

/**
 * 
 * A ConcreteMilestone is a specific {@link Milestone} for a {@link Project}.
 * 
 */
public class ConcreteMilestone extends ConcreteWorkBreakdownElement implements
	Cloneable {

    private Milestone milestone;

    /**
     * Class constructor.
     */
    public ConcreteMilestone() {
	super();
    }

    /**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the class.
	 * 
	 * @param obj
	 *            the Object to be compare to the ConcreteMilestone
	 * @return true if the specified Object is the same, false otherwise
	 */
    public boolean equals(Object obj) {
	if (obj instanceof ConcreteMilestone == false) {
	    return false;
	}
	if (this == obj) {
	    return true;
	}
	ConcreteMilestone concreteMilestone = (ConcreteMilestone) obj;
	return new EqualsBuilder().appendSuper(super.equals(concreteMilestone))
		.append(this.milestone, concreteMilestone.milestone).isEquals();
    }

    /**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of ConcreteMilestone
	 */
    public int hashCode() {
	return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
		.append(this.milestone).toHashCode();
    }

    /**
	 * Returns a copy of the current instance of ConcreteMilestone
	 * 
	 * @return a copy of the ConcreteMilestone
	 * @throws CloneNotSupportedException
	 */
    @Override
    public ConcreteMilestone clone() throws CloneNotSupportedException {
	ConcreteMilestone concreteMilestone = new ConcreteMilestone();
	concreteMilestone.copy(this);
	return concreteMilestone;
    }

    /**
	 * Copy the values of the specified ConcreteMilestone into the current
	 * instance.
     * 
     * @param _concreteMilestone
     *                The ConcreteMilestone to copy.
     */
    protected void copy(final ConcreteMilestone _concreteMilestone) {
	super.copy(_concreteMilestone);
	this.milestone = _concreteMilestone.getMilestone();
    }

    /**
     * Return the Milestone assigned to the ConcreteMilestone
     * 
     * @return the milestone of the ConcreteMilestone
     */
    public Milestone getMilestone() {
	return this.milestone;
    }

    /**
     * Initializes the Milestone assigned to the ConcreteMilestone.
     *  
     * @param _milestone
     *                the milestone to set
     */
    public void setMilestone(Milestone _milestone) {
	this.milestone = _milestone;
    }

}
