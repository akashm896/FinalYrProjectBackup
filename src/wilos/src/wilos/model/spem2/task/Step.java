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

package wilos.model.spem2.task ;

import org.apache.commons.lang.builder.EqualsBuilder ;
import org.apache.commons.lang.builder.HashCodeBuilder ;

import wilos.model.spem2.element.Element;

/**
 * 
 * This class represents a section which represents structural subsections of a taskDefinition.
 * 
 */
public class Step extends Element implements Cloneable, Comparable<Step> {

	/**
	 * the attached taskDefinition
	 */
	private TaskDefinition taskDefinition ;

	/**
	 * Default constructor
	 */
	public Step() {
		super() ;
	}

	/**
	 * Returns a copy of the current instance of Step
	 * 
	 * @return a copy of the Step
	 * @throws CloneNotSupportedException
	 */
	@ Override
	public Step clone() throws CloneNotSupportedException {
		Step step = new Step() ;
		step.copy(this) ;
		return step ;
	}

	/**
	 * Copy the values of the specified Step into the current instance
	 * of the class.
	 * 
	 * @param _step
	 *            The Step to copy.
	 */
	protected void copy(final Step _step) {
		super.copy(_step) ;
		this.taskDefinition =_step.getTaskDefinition() ;
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the Step.
	 * 
	 * @param obj
	 *            the Object to be compare to the Step
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if(obj instanceof Step == false){
			return false ;
		}
		if(this == obj){
			return true ;
		}
		Step step = (Step) obj ;
		return new EqualsBuilder().appendSuper(super.equals(step)).append(this.taskDefinition, step.taskDefinition).isEquals() ;
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of Step
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(this.taskDefinition).toHashCode() ;
	}

	/**
	 * Compares the Step in parameter with the current instance of the class.
	 * 
	 * @return 0 if both the Step are equals, -1 otherwise
	 */
	public int compareTo(Step _step) {
		// Return 0, if this is equal to _step.
		if(this.equals(_step))
			return 0 ;
		else
			return -1 ;
	}

	/**
	 * Add a step to a TaskDefiniton
	 * 
	 * @param _taskDefinition
	 */
	public void addTaskDefinition(TaskDefinition _taskDefinition) {
		this.setTaskDefinition(_taskDefinition) ;
		_taskDefinition.getSteps().add(this) ;
	}

	/**
	 * Detach a step to its TaskDefinition
	 * 
	 * @param _taskDefinition
	 */
	public void removeTaskDefinition(TaskDefinition _taskDefinition) {
		_taskDefinition.getSteps().remove(this) ;
		this.taskDefinition = null ;
	}

	/**
	 * Getter of taskDefinition.
	 * 
	 * @return the taskDefinition.
	 */
	public TaskDefinition getTaskDefinition() {
		return this.taskDefinition ;
	}

	/**
	 * Setter of taskDefinition.
	 * 
	 * @param _taskDefinition
	 *            The taskDefinition to set.
	 */
	public void setTaskDefinition(TaskDefinition _taskDefinition) {
		this.taskDefinition = _taskDefinition ;
	}
}
