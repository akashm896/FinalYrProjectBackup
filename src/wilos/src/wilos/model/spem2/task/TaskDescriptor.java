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

package wilos.model.spem2.task;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.model.spem2.workbreakdownelement.WorkBreakdownElement;
import wilos.model.spem2.workproduct.WorkProductDescriptor;

/**
 * 
 * A TaskDescriptor is a Descriptor and {@link WorkBreakdownElement} that
 * represents a proxy for a Task in the context of one specific {@link Activity}.
 * Every breakdown structure can define different relationships of Task
 * Descriptors to Work Product Descriptors and Role Descriptors. Therefore one
 * Task can be represented by many Task Descriptors each within the context of
 * an {@link Activity} with its own set of relationships.
 * <p />
 * It's an element of the SPEM2 specification of the OMG organization
 * (http://www.omg.org/).
 * 
 */
public class TaskDescriptor extends WorkBreakdownElement implements Cloneable {

	/**
	 * the attached taskDefinition
	 */
	private TaskDefinition taskDefinition;

	/**
	 * The additional roles of the role
	 */
	private Set<RoleDescriptor> additionalRoles;

	/**
	 * The main role of the taskDefinition
	 */
	private RoleDescriptor mainRole;

	/**
	 * The corresponding concrete task descriptors.
	 */
	private Set<ConcreteTaskDescriptor> concreteTaskDescriptors;

	/**
	 * Relations with WorkProducts
	 */

	private Set<WorkProductDescriptor> outputWorkProductDescriptors;

	private Set<WorkProductDescriptor> optionalInputWorkProductDescriptors;

	private Set<WorkProductDescriptor> mandatoryInputWorkProductDescriptors;

	/**
	 * Default constructor.
	 */
	public TaskDescriptor() {
		super();
		this.additionalRoles = new HashSet<RoleDescriptor>();
		this.outputWorkProductDescriptors = new HashSet<WorkProductDescriptor>();
		this.optionalInputWorkProductDescriptors = new HashSet<WorkProductDescriptor>();
		this.mandatoryInputWorkProductDescriptors = new HashSet<WorkProductDescriptor>();
		this.concreteTaskDescriptors = new HashSet<ConcreteTaskDescriptor>();
	}

	/**
	 * Returns a copy of the current instance of TaskDescriptor
	 * 
	 * @return a copy of the TaskDescriptor
	 * @throws CloneNotSupportedException
	 */
	@Override
	public TaskDescriptor clone() throws CloneNotSupportedException {
		TaskDescriptor taskDescriptor = new TaskDescriptor();
		taskDescriptor.copy(this);
		return taskDescriptor;
	}

	/**
	 * Copy the values of the specified TaskDescriptor into the current instance
	 * of the class.
	 * 
	 * @param _taskDescriptor
	 *            The TaskDescriptor to copy.
	 */
	protected void copy(final TaskDescriptor _taskDescriptor) {
		super.copy(_taskDescriptor);
		this.additionalRoles.addAll(_taskDescriptor.getAdditionalRoles());

		this.outputWorkProductDescriptors.addAll(_taskDescriptor
				.getOutputWorkProductDescriptors());
		this.optionalInputWorkProductDescriptors.addAll(_taskDescriptor
				.getOptionalInputWorkProductDescriptors());
		this.mandatoryInputWorkProductDescriptors.addAll(_taskDescriptor
				.getMandatoryInputWorkProductDescriptors());
		this.concreteTaskDescriptors.addAll(_taskDescriptor
				.getConcreteTaskDescriptors());
		this.taskDefinition = _taskDescriptor.getTaskDefinition();
		this.mainRole = _taskDescriptor.getMainRole();
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the TaskDescriptor.
	 * 
	 * @param obj
	 *            the Object to be compare to the TaskDescriptor
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof TaskDescriptor == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		TaskDescriptor taskDescriptor = (TaskDescriptor) obj;
		return new EqualsBuilder().appendSuper(super.equals(taskDescriptor))
				.append(this.taskDefinition, taskDescriptor.taskDefinition)
				.append(this.additionalRoles, taskDescriptor.additionalRoles)
				.append(this.outputWorkProductDescriptors,
						taskDescriptor.outputWorkProductDescriptors).append(
						this.optionalInputWorkProductDescriptors,
						taskDescriptor.optionalInputWorkProductDescriptors)
				.append(this.mandatoryInputWorkProductDescriptors,
						taskDescriptor.mandatoryInputWorkProductDescriptors)
				.append(this.mainRole, taskDescriptor.mainRole).append(
						this.concreteTaskDescriptors,
						taskDescriptor.concreteTaskDescriptors).isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of TaskDescriptor
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(this.taskDefinition).append(this.mainRole).toHashCode();
	}

	/*
	 * relation between TaskDescriptor and ConcreteTaskDescriptor.
	 * 
	 */

	/**
	 * Adds a relation between the TaskDescriptor and the specified
	 * ConcreteTaskDescriptor
	 * 
	 * @param _concreteTaskDescriptor
	 *            the ConcreteTaskDescriptor related to the TaskDescriptor
	 */
	public void addConcreteTaskDescriptor(
			ConcreteTaskDescriptor _concreteTaskDescriptor) {
		this.concreteTaskDescriptors.add(_concreteTaskDescriptor);
		_concreteTaskDescriptor.addTaskDescriptor(this);
	}

	/**
	 * Removes the relation between the TaskDescriptor and the specified
	 * ConcreteTaskDescriptor
	 * 
	 * @param _concreteTaskDescriptor
	 *            the ConcreteTaskDescriptor unlinked to the TaskDescriptor
	 */
	public void removeConcreteTaskDescriptor(
			ConcreteTaskDescriptor _concreteTaskDescriptor) {
		_concreteTaskDescriptor.removeTaskDescriptor(this);
		this.concreteTaskDescriptors.remove(_concreteTaskDescriptor);
	}

	/*
	 * relation between TaskDescriptor and TaskDefinition
	 * 
	 */

	/**
	 * Attach a TaskDescriptor to the specified TaskDefinition
	 * 
	 * @param _taskDefinition
	 *            the TaskDefinition related to the TaskDescriptor
	 */
	public void addTaskDefinition(TaskDefinition _taskDefinition) {
		this.taskDefinition = _taskDefinition;
		_taskDefinition.getTaskDescriptors().add(this);
	}

	/**
	 * Detach the TaskDescriptor from the specified TaskDefinition
	 * 
	 * @param _taskDefinition
	 */
	public void removeTaskDefinition(TaskDefinition _taskDefinition) {
		_taskDefinition.getTaskDescriptors().remove(this);
		this.taskDefinition = null;
	}

	/*
	 * relation primaryTask between TaskDescriptor and RoleDescriptor
	 */

	/**
	 * Attach the TaskDescriptor to a RoleDescriptor
	 * 
	 * @param _roleDescriptor
	 */
	public void addMainRole(RoleDescriptor _roleDescriptor) {
		this.mainRole = _roleDescriptor;
		_roleDescriptor.getPrimaryTasks().add(this);
	}

	/**
	 * Detach a taskDescriptor to its roleDescriptor
	 * 
	 * @param _roleDescriptor
	 */
	public void removeMainRole(RoleDescriptor _roleDescriptor) {
		_roleDescriptor.getPrimaryTasks().remove(this);
		this.mainRole = null;
	}

	/*
	 * relation additionalTasks between TaskDescriptor and RoleDescriptor
	 */

	/**
	 * Adds a relation between the TaskDescriptor and the specified additional
	 * RoleDescriptor
	 * 
	 * @param _roleDescriptor
	 *            the RoleDescriptor to be related to the TaskDescriptor
	 */
	public void addAdditionalRole(RoleDescriptor _roleDescriptor) {
		this.additionalRoles.add(_roleDescriptor);
		_roleDescriptor.getAdditionalTasks().add(this);
	}

	/**
	 * Removes the relation between the TaskDescriptor and the specified
	 * additional RoleDescriptor
	 * 
	 * @param _roleDescriptor
	 *            the RoleDescriptor to be unlinked to the TaskDescriptor
	 */
	public void removeAdditionalRole(RoleDescriptor _roleDescriptor) {
		_roleDescriptor.getAdditionalTasks().remove(this);
		this.additionalRoles.remove(_roleDescriptor);
	}

	/**
	 * Adds the TaskDescriptor to all the additional RoleDescriptor from the
	 * Collection in parameter
	 * 
	 * @param _role
	 *            the Set of RoleDescriptor to be in relation with the
	 *            TaskDescriptor
	 */
	public void addAllAdditionalRoles(Set<RoleDescriptor> _role) {
		for (RoleDescriptor role : _role) {
			role.addAdditionalTask(this);
		}
	}

	/**
	 * Remove from a RoleDescriptor all its additional TaskDescriptors
	 */
	public void removeAllAdditionalRoles() {
		for (RoleDescriptor tmp : this.getAdditionalRoles()) {
			tmp.getAdditionalTasks().remove(this);
		}
		this.getAdditionalRoles().clear();
	}

	/**
	 * Associates the TaskDescriptor to all the ConcreteTaskDescriptor from the
	 * Collection in parameter
	 * 
	 * @param _ctd
	 *            the Set of ConcreteTaskDescriptor to relate to the
	 *            TaskDescriptor
	 */
	public void addAllConcreteTaskDescriptors(Set<ConcreteTaskDescriptor> _ctd) {
		for (ConcreteTaskDescriptor tmp : _ctd) {
			tmp.addTaskDescriptor(this);
		}
	}

	/**
	 * Remove the TaskDescriptor from all the related RoleDescriptor
	 */
	public void removeAllConcreteTaskDescriptors() {
		for (ConcreteTaskDescriptor tmp : this.getConcreteTaskDescriptors()) {
			tmp.setTaskDescriptor(null);
		}
		this.getConcreteTaskDescriptors().clear();
	}

	/**
	 * Getter of taskDefinition.
	 * 
	 * @return the taskDefinition.
	 */
	public TaskDefinition getTaskDefinition() {
		return this.taskDefinition;
	}

	/**
	 * Setter of taskDefinition.
	 * 
	 * @param _taskDefinition
	 *            The taskDefinition to set.
	 */
	public void setTaskDefinition(TaskDefinition _taskDefinition) {
		this.taskDefinition = _taskDefinition;
	}

	/**
	 * Getter of additionalRoles.
	 * 
	 * @return the additionalRoles.
	 */
	public Set<RoleDescriptor> getAdditionalRoles() {
		return this.additionalRoles;
	}

	/**
	 * Setter of additionalRoles.
	 * 
	 * @param _additionalRoles
	 *            The additionalRoles to set.
	 */
	@SuppressWarnings("unused")
	public void setAdditionalRoles(Set<RoleDescriptor> _additionalRoles) {
		this.additionalRoles = _additionalRoles;
	}

	/**
	 * Getter of mainRole.
	 * 
	 * @return the mainRole.
	 */
	public RoleDescriptor getMainRole() {
		return this.mainRole;
	}

	/**
	 * Setter of mainRole.
	 * 
	 * @param _mainRole
	 *            The mainRole to set.
	 */
	public void setMainRole(RoleDescriptor _mainRole) {
		this.mainRole = _mainRole;
	}

	/**
	 * Getter of the Collection concreteTaskDescriptors
	 * 
	 * @return the Set of ConcreteTaskDescriptors related to the TaskDescriptor
	 */
	public Set<ConcreteTaskDescriptor> getConcreteTaskDescriptors() {
		return concreteTaskDescriptors;
	}

	/**
	 * Setter of the Collection concreteTaskDescriptors
	 * 
	 * @param concreteTaskDescriptors
	 *            the Set of ConcreteTaskDescriptor to set
	 */
	public void setConcreteTaskDescriptors(
			Set<ConcreteTaskDescriptor> concreteTaskDescriptors) {
		this.concreteTaskDescriptors = concreteTaskDescriptors;
	}

	/**
	 * Getter of the Collection outputWorkProductDescriptors
	 * 
	 * @return the Set of output WorkProductDescriptor related to the
	 *         TaskDescriptor
	 */
	public Set<WorkProductDescriptor> getOutputWorkProductDescriptors() {
		return this.outputWorkProductDescriptors;
	}

	/**
	 * Setter of the Collection outputWorkProductDescriptors
	 * 
	 * @param _outputWorkProductDescriptors
	 *            the outputWorkProductDescriptors to set
	 */
	public void setOutputWorkProductDescriptors(
			Set<WorkProductDescriptor> _outputWorkProductDescriptors) {
		this.outputWorkProductDescriptors = _outputWorkProductDescriptors;
	}

	/**
	 * Getter of the attribute optionalInputWorkProductDescriptors
	 * 
	 * @return the optionalInputWorkProductDescriptors
	 */
	public Set<WorkProductDescriptor> getOptionalInputWorkProductDescriptors() {
		return this.optionalInputWorkProductDescriptors;
	}

	/**
	 * Setter of the attribute optionalInputWorkProductDescriptors
	 * 
	 * @param _optionalInputWorkProductDescriptors
	 *            the optionalInputWorkProductDescriptors to set
	 */
	public void setOptionalInputWorkProductDescriptors(
			Set<WorkProductDescriptor> _optionalInputWorkProductDescriptors) {
		this.optionalInputWorkProductDescriptors = _optionalInputWorkProductDescriptors;
	}

	/**
	 * Getter of the attribute mandatoryInputWorkProductDescriptors
	 * 
	 * @return the mandatoryInputWorkProductDescriptors
	 */
	public Set<WorkProductDescriptor> getMandatoryInputWorkProductDescriptors() {
		return this.mandatoryInputWorkProductDescriptors;
	}

	/**
	 * Setter of the attribute mandatoryInputWorkProductDescriptors
	 * 
	 * @param _mandatoryInputWorkProductDescriptors
	 *            the mandatoryInputWorkProductDescriptors to set
	 */
	public void setMandatoryInputWorkProductDescriptors(
			Set<WorkProductDescriptor> _mandatoryInputWorkProductDescriptors) {
		this.mandatoryInputWorkProductDescriptors = _mandatoryInputWorkProductDescriptors;
	}

	/**
	 * Adds a relation between the specified WorkProductDescriptor and the
	 * TaskDescriptor
	 * 
	 * @param _workProductDescriptor
	 *            the WorkProductDescriptor to relate to the TaskDescriptor
	 */
	public void addOutputWorkProduct(
			WorkProductDescriptor _workProductDescriptor) {
		this.outputWorkProductDescriptors.add(_workProductDescriptor);
		_workProductDescriptor.getProducerTasks().add(this);
	}

	/**
	 * Adds a relation between the specified optional input
	 * WorkProductDescriptor and the TaskDescriptor
	 * 
	 * @param _workProductDescriptor
	 *            the optional input WorkProductDescriptor to relate to the
	 *            TaskDescriptor
	 */
	public void addOptionalInputWorkProduct(
			WorkProductDescriptor _workProductDescriptor) {
		this.optionalInputWorkProductDescriptors.add(_workProductDescriptor);
		_workProductDescriptor.getOptionalUserTasks().add(this);
	}

	/**
	 * Adds a relation between the specified mandatory input
	 * WorkProductDescriptor and the TaskDescriptor
	 * 
	 * @param _workProductDescriptor
	 *            the mandatory input WorkProductDescriptor to relate to the
	 *            TaskDescriptor
	 */
	public void addMandatoryInputWorkProduct(
			WorkProductDescriptor _workProductDescriptor) {
		this.mandatoryInputWorkProductDescriptors.add(_workProductDescriptor);
		_workProductDescriptor.getMandatoryUserTasks().add(this);
	}

	/**
	 * Adds the TaskDescriptor to all the specified output WorkProductDescriptor
	 * 
	 * @param _outputWorkProductDescriptors
	 *            the Collection of output WorkProductDescriptor to relate to
	 *            the TaskDescriptor
	 */
	public void addAllOutputWorkProductDescriptors(
			Set<WorkProductDescriptor> _outputWorkProductDescriptors) {
		for (WorkProductDescriptor workProduct : _outputWorkProductDescriptors) {
			workProduct.addProducerTask(this);
		}

	}

	/**
	 * Adds the TaskDescriptor to all the specified optional input
	 * WorkProductDescriptor
	 * 
	 * @param _optionalInputWorkProductDescriptors
	 *            the Collection of optional input WorkProductDescriptor to
	 *            relate to the TaskDescriptor
	 */
	public void addAllOptionalInputWorkProductDescriptors(
			Set<WorkProductDescriptor> _optionalInputWorkProductDescriptors) {
		for (WorkProductDescriptor workProduct : _optionalInputWorkProductDescriptors) {
			workProduct.addOptionalUser(this);
		}

	}

	/**
	 * Adds the TaskDescriptor to all the specified mandatory input
	 * WorkProductDescriptor
	 * 
	 * @param _mandatoryInputWorkProductDescriptors
	 *            the Collection of mandatory input WorkProductDescriptor to
	 *            relate to the TaskDescriptor
	 */
	public void addAllMandatoryInputWorkProductDescriptors(
			Set<WorkProductDescriptor> _mandatoryInputWorkProductDescriptors) {
		for (WorkProductDescriptor workProduct : _mandatoryInputWorkProductDescriptors) {
			workProduct.addMandatoryUser(this);
		}

	}
}
