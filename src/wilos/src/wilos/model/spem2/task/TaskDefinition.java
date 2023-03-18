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

package wilos.model.spem2.task;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.spem2.element.Element;
import wilos.model.spem2.guide.Guidance;
import wilos.model.spem2.role.RoleDefinition;

/**
 * 
 * This class represents a task is a content element that describes work being
 * performed by Roles. It defines one default performing {@link RoleDefinition}
 * as well as many additional performers.
 * <p />
 * It's an element of the SPEM2 specification of the OMG organization
 * (http://www.omg.org/).
 * 
 */
public class TaskDefinition extends Element implements Cloneable {

	/**
	 * Collection of Step
	 */
	private SortedSet<Step> steps;

	/**
	 * Collection of TaskDescriptor
	 */
	private Set<TaskDescriptor> taskDescriptors;

	// The Project of Process

	private Set<Guidance> guidances;

	private String alternatives;

	private String purpose;

	/**
	 * Default constructor
	 */
	public TaskDefinition() {
		super();
		this.steps = new TreeSet<Step>();
		this.taskDescriptors = new HashSet<TaskDescriptor>();
		this.guidances = new HashSet<Guidance>();
		this.alternatives = "";
		this.purpose = "";
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the TaskDefinition.
	 * 
	 * @param obj
	 *            the Object to be compare to the TaskDefinition
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof TaskDefinition == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		TaskDefinition task = (TaskDefinition) obj;
		return new EqualsBuilder().appendSuper(super.equals(task)).append(
				this.steps, task.steps).append(this.taskDescriptors,
				task.taskDescriptors).append(this.guidances, task.guidances)
				.append(this.alternatives, task.alternatives).append(
						this.purpose, task.purpose).isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of TaskDefinition
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(this.alternatives).append(this.purpose).toHashCode();
	}

	/**
	 * Returns a copy of the current instance of TaskDefinition
	 * 
	 * @return a copy of the TaskDefinition
	 * @throws CloneNotSupportedException
	 */
	@Override
	public TaskDefinition clone() throws CloneNotSupportedException {
		TaskDefinition taskDefinition = new TaskDefinition();
		taskDefinition.copy(this);
		return taskDefinition;
	}

	/**
	 * Copy the values of the specified TaskDefinition into the current instance
	 * of the class.
	 * 
	 * @param _taskDefinition
	 *            The TaskDefinition to copy.
	 */
	protected void copy(final TaskDefinition _taskDefinition) {
		super.copy(_taskDefinition);
		this.steps.addAll(_taskDefinition.getSteps());
		this.taskDescriptors.addAll(_taskDefinition.getTaskDescriptors());
		this.guidances.addAll(_taskDefinition.getGuidances());
		this.alternatives = _taskDefinition.getAlternatives();
		this.purpose = _taskDefinition.getPurpose();
	}

	/**
	 * Adds a step to a taskDefinition
	 * 
	 * @param _step
	 *            the Step to relate to the TaskDefinition
	 */
	public void addStep(Step _step) {
		this.steps.add(_step);
		_step.setTaskDefinition(this);
	}

	/**
	 * Adds the TaskDefinition to all the Step from the Collection in parameter
	 * 
	 * @param _steps
	 *            the Collection of Step with which the TaskDefinition is to be
	 *            related
	 */
	public void addAllSteps(SortedSet<Step> _steps) {
		for (Step s : _steps) {
			s.addTaskDefinition(this);
		}
	}

	/**
	 * Adds the TaskDefinition to the specified TaskDescriptor
	 * 
	 * @param _taskDescriptor
	 *            the TaskDescriptor to be related with the TaskDefinition
	 */
	public void addTaskDescriptor(TaskDescriptor _taskDescriptor) {
		this.taskDescriptors.add(_taskDescriptor);
		_taskDescriptor.setTaskDefinition(this);
	}

	/**
	 * Adds the TaskDefinition to all the TaskDescriptor from the Collection in
	 * parameter
	 * 
	 * @param _taskDescriptor
	 *            the Set of TaskDescriptor to be related to the TaskDefinition
	 */
	public void addAllTaskDesciptors(Set<TaskDescriptor> _taskDescriptor) {
		for (TaskDescriptor td : _taskDescriptor) {
			td.addTaskDefinition(this);
		}
	}

	/**
	 * Removes a Step from a TaskDefinition
	 * 
	 * @param _step
	 *            the Step to be unrelated to the TaskDefinition
	 */
	public void removeStep(Step _step) {
		_step.setTaskDefinition(null);
		this.steps.remove(_step);
	}

	/**
	 * Removes all Steps from a TaskDefinition
	 */
	public void removeAllSteps() {
		for (Step tmp : this.steps) {
			tmp.setTaskDefinition(null);
		}
		this.steps.clear();
	}

	/**
	 * Removes the relation between the specified TaskDescriptor and the
	 * TaskDefinition
	 * 
	 * @param _taskDescriptor
	 *            the TaskDescriptor to be unlinked to the TaskDefinition
	 */
	public void removeTaskDescriptor(TaskDescriptor _taskDescriptor) {
		_taskDescriptor.setTaskDefinition(null);
		this.taskDescriptors.remove(_taskDescriptor);
	}

	/**
	 * Removes the TaskDescription from all its TaskDescriptor
	 */
	public void removeAllTaskDescriptors() {
		for (TaskDescriptor tmp : this.taskDescriptors) {
			tmp.setTaskDefinition(null);
		}
		this.taskDescriptors.clear();
	}

	/**
	 * Getter of steps.
	 * 
	 * @return the steps.
	 */
	public SortedSet<Step> getSteps() {
		return this.steps;
	}

	/**
	 * Setter of steps.
	 * 
	 * @param _steps
	 *            The steps to set.
	 */
	@SuppressWarnings("unused")
	public void setSteps(SortedSet<Step> _steps) {
		this.steps = _steps;
	}

	/**
	 * Getter of taskDescriptors.
	 * 
	 * @return the taskDescriptors.
	 */
	public Set<TaskDescriptor> getTaskDescriptors() {
		return this.taskDescriptors;
	}

	/**
	 * Setter of taskDescriptors.
	 * 
	 * @param _taskDescriptors
	 *            The taskDescriptors to set.
	 */
	@SuppressWarnings("unused")
	private void setTaskDescriptors(Set<TaskDescriptor> _taskDescriptors) {
		this.taskDescriptors = _taskDescriptors;
	}

	/*
	 * connection to guidances
	 */

	/**
	 * Removes the relation between the TaskDefinition and the specified
	 * Guidance
	 * 
	 * @param _guidance
	 *            the Guidance to be unlinked to the TaskDefinition
	 */
	public void removeGuidance(Guidance _guidance) {
		_guidance.getTaskDefinitions().remove(this);
		this.guidances.remove(_guidance);
	}

	/**
	 * Adds a relation between the TaskDefinition and the specified Guidance
	 * 
	 * @param _guidance
	 *            the Guidance to be related to the TaskDefinition
	 */
	public void addGuidance(Guidance _guidance) {
		this.guidances.add(_guidance);
		_guidance.getTaskDefinitions().add(this);
	}

	/**
	 * Removes the TaskDefinition from all the related Guidance
	 */
	public void removeAllGuidances() {
		for (Guidance guidance : this.guidances) {
			guidance.getTaskDefinitions().remove(this);
		}
		this.guidances.clear();
	}

	/**
	 * Adds the Collection of Guidance in parameter to the TaskDefinition
	 * 
	 * @param _guidances
	 *            the Collection of Guidance to be add
	 */
	public void addAllGuidances(Set<Guidance> _guidances) {
		for (Guidance _guid1 : _guidances) {
			this.addGuidance(_guid1);
		}
	}

	/**
	 * Returns the Collection of Guidance related to the TaskDefinition
	 * 
	 * @return The Set of the TaskDefinition guidances
	 */
	public Set<Guidance> getGuidances() {
		return this.guidances;
	}

	/**
	 * Sets the Collection of Guidances related to the TaskDefinition
	 * 
	 * @param _guidances
	 *            the Set of Guidance to assign to the TaskDefinition
	 */
	public void setGuidances(Set<Guidance> _guidances) {
		this.guidances = _guidances;
	}

	/**
	 * Getter of alternatives
	 * 
	 * @return the alternatives
	 */
	public String getAlternatives() {
		return alternatives;
	}

	/**
	 * Setter of alternatives
	 * 
	 * @param _alternatives
	 *            the alternatives to set
	 */

	public void setAlternatives(String _alternatives) {
		this.alternatives = _alternatives;
	}

	/**
	 * Getter of purpose
	 * 
	 * @return the purpose
	 */

	public String getPurpose() {
		return purpose;
	}

	/**
	 * Setter of purpose
	 * 
	 * @param _purpose
	 *            the purpose to set
	 */

	public void setPurpose(String _purpose) {
		this.purpose = _purpose;
	}
}
