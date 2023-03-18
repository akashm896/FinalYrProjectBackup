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

package wilos.model.spem2.milestone;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.spem2.workbreakdownelement.WorkBreakdownElement;

/**
 * 
 * TODO
 */
public class Milestone extends WorkBreakdownElement implements Cloneable {

    // private Set<ConcreteMilestone> concreteMilestones;

    // private Set<WorkProductUse> outputWorkProductUses;
    
    /**
     * Default constructor.
     * 
     */
    public Milestone() {
	super();
	// this.concreteMilestones = new HashSet<ConcreteMilestone>();
	// this.outputWorkProductUses = new HashSet<WorkProductUse>();
    }

    /**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the Milestone.
	 * 
	 * @param obj
	 *            the Object to be compare to the Milestone
	 * @return true if the specified Object is the same, false otherwise
	 */
    public boolean equals(Object obj) {
	if (obj instanceof Milestone == false) {
	    return false;
	}
	if (this == obj) {
	    return true;
	}
	Milestone milestone = (Milestone) obj;
	return new EqualsBuilder().appendSuper(super.equals(milestone))
		.isEquals();
    }

    /**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of Milestone
	 */
    public int hashCode() {
	return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
		.toHashCode();
    }

    /**
	 * Returns a copy of the current instance of Milestone
	 * 
	 * @return a copy of the Milestone
	 * @throws CloneNotSupportedException
	 */
    @Override
    public Milestone clone() throws CloneNotSupportedException {
	Milestone milestone = new Milestone();
	milestone.copy(this);
	return milestone;
    }

    /**
     * Copy the values of the specified Milestone into the current instance of the
	 * class.
	 * 
     * @param _milestone
     *                The Milestone to copy.
     */
    protected void copy(final Milestone _milestone) {
	super.copy(_milestone);
	// this.concreteMilestones.addAll(_milestone.getConcreteMilestones());
    }

    // /**
    // * @return the outputWorkProductDescriptors
    // */
    // public Set<WorkProductUse> getOutputWorkProductUses() {
    // return outputWorkProductUses;
    // }
    //
    // /**
    // * @param _outputWorkProductDescriptors the outputWorkProductDescriptors
    // to set
    // */
    // public void setOutputWorkProductDescriptors(
    // Set<WorkProductUse> _outputWorkProductUses) {
    // outputWorkProductUses = _outputWorkProductUses;
    // }
    //
    // public void addOutputWorkProductDescriptor(
    // WorkProductUse _workproductuse) {
    // //TODO
    // }
    //
    // public void removeOutputWorkProductDescriptor(
    // WorkProductDescriptor _workproduct) {
    // //TODO
    // }
}
