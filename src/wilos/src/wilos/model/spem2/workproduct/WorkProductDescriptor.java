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

package wilos.model.spem2.workproduct;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.spem2.activity.Activity;
import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.model.spem2.breakdownelement.BreakdownElement;

/**
 * A Work Product Descriptor represents a {@link WorkProductDefinition} in the
 * context of one specific Activity. Every breakdown structure can define
 * different relationships of {@link WorkProductDefinition} Descriptors to
 * {@link TaskDefinition} Descriptors and Role Descriptors. Therefore one
 * {@link WorkProductDefinition} can be represented by many Work Product
 * Descriptors each within the context of an {@link Activity} with its own set
 * of relationships. *
 * <p />
 * It's an element of the SPEM2 specification of the OMG organization
 * (http://www.omg.org/).
 * 
 */
public class WorkProductDescriptor extends BreakdownElement implements
		Cloneable {

	/**
	 * the attached WorkProductDefinition
	 */
	private WorkProductDefinition workProductDefinition;

	/**
	 * Associated ConcreteWorkProductDescriptor.
	 */
	private Set<ConcreteWorkProductDescriptor> concreteWorkProductDescriptors;

	private boolean isDeliverable;

	private RoleDescriptor responsibleRoleDescriptor;

	private String activityEntryState = "";

	private String activityExitState = "";

	private Set<Activity> userActivities;

	private Set<Activity> producerActivities;

	private Set<TaskDescriptor> producerTasks;

	private Set<TaskDescriptor> optionalUserTasks;

	private Set<TaskDescriptor> mandatoryUserTasks;

	private boolean isOutOfProcess;

	/**
	 * Class constructor
	 */
	public WorkProductDescriptor() {
		super();
		this.concreteWorkProductDescriptors = new HashSet<ConcreteWorkProductDescriptor>();
		this.userActivities = new HashSet<Activity>();
		this.producerActivities = new HashSet<Activity>();
		this.producerTasks = new HashSet<TaskDescriptor>();
		this.optionalUserTasks = new HashSet<TaskDescriptor>();
		this.mandatoryUserTasks = new HashSet<TaskDescriptor>();
		this.isOutOfProcess = false;
	}

	/**
	 * Returns a copy of the current instance of WorkProductDescriptor
	 * 
	 * @return a copy of the WorkProductDescriptor
	 * @throws CloneNotSupportedException
	 */
	@Override
	public WorkProductDescriptor clone() throws CloneNotSupportedException {
		WorkProductDescriptor workProductDescriptor = new WorkProductDescriptor();
		workProductDescriptor.copy(this);
		return workProductDescriptor;
	}

	/**
	 * Copy the values of the specified WorkProductDescriptor into the current
	 * instance of the class.
	 * 
	 * @param _workProductDescriptor
	 *            The WorkProductDescriptor to copy.
	 */
	protected void copy(final WorkProductDescriptor _workProductDescriptor) {
		super.copy(_workProductDescriptor);
		this.workProductDefinition = _workProductDescriptor
				.getWorkProductDefinition();
		this.concreteWorkProductDescriptors.addAll(_workProductDescriptor
				.getConcreteWorkProductDescriptors());
		this.responsibleRoleDescriptor = _workProductDescriptor
				.getResponsibleRoleDescriptor();
		this.isDeliverable = _workProductDescriptor.isDeliverable();
		this.activityEntryState = _workProductDescriptor
				.getActivityEntryState();
		this.activityExitState = _workProductDescriptor.getActivityExitState();

		this.producerTasks.addAll(_workProductDescriptor.getProducerTasks());
		this.optionalUserTasks.addAll(_workProductDescriptor
				.getOptionalUserTasks());
		this.mandatoryUserTasks.addAll(_workProductDescriptor
				.getMandatoryUserTasks());
		this.isOutOfProcess = _workProductDescriptor.getIsOutOfProcess();
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the WorkProductDescriptor.
	 * 
	 * @param obj
	 *            the Object to be compare to the WorkProductDescriptor
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof WorkProductDescriptor == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}

		WorkProductDescriptor workProductDescriptor = (WorkProductDescriptor) obj;
		return new EqualsBuilder()
				.appendSuper(super.equals(workProductDescriptor))
				.append(this.workProductDefinition,
						workProductDescriptor.workProductDefinition)
				.append(this.concreteWorkProductDescriptors,
						workProductDescriptor.concreteWorkProductDescriptors)
				.append(this.isDeliverable, workProductDescriptor.isDeliverable)
				.append(this.activityEntryState,
						workProductDescriptor.activityEntryState)
				.append(this.activityExitState,
						workProductDescriptor.activityExitState)
				.append(this.producerActivities,
						workProductDescriptor.producerActivities)
				.append(this.userActivities,
						workProductDescriptor.userActivities)
				.append(this.responsibleRoleDescriptor,
						workProductDescriptor.responsibleRoleDescriptor)
				.append(this.producerTasks, workProductDescriptor.producerTasks)
				.append(this.optionalUserTasks,
						workProductDescriptor.optionalUserTasks).append(
						this.mandatoryUserTasks,
						workProductDescriptor.mandatoryUserTasks).append(
						this.isOutOfProcess,
						workProductDescriptor.isOutOfProcess).isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of WorkProductDescriptor
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(this.workProductDefinition).toHashCode();
	}

	/*
	 * relation between WOrkProductDescriptor and ConcreteWorkProductDescriptor.
	 * 
	 */

	/**
	 * Adds a relation between the WorkProductDescriptor and the specified
	 * ConcreteWorkProductDescriptor
	 * 
	 * @param _concreteWorkProductDescriptor
	 *            the ConcreteWorkProductDescriptor to relate to the
	 *            WorkProductDescriptor
	 */
	public void addConcreteWorkProductDescriptor(
			ConcreteWorkProductDescriptor _concreteWorkProductDescriptor) {
		this.concreteWorkProductDescriptors.add(_concreteWorkProductDescriptor);
		_concreteWorkProductDescriptor.addWorkProductDescriptor(this);
	}

	/**
	 * Removes the relation between the WorkProductDescriptor and the specified
	 * ConcreteWorkProductDescriptor
	 * 
	 * @param _concreteWorkProductDescriptor
	 *            the ConcreteWorkProductDescriptor to unlink to the
	 *            WorkProductDescriptor
	 */
	public void removeConcreteWorkProductDescriptor(
			ConcreteWorkProductDescriptor _concreteWorkProductDescriptor) {
		_concreteWorkProductDescriptor.removeWorkProductDescriptor(this);
		this.concreteWorkProductDescriptors
				.remove(_concreteWorkProductDescriptor);
	}

	/**
	 * Adds the WorkProductDescriptor to all the specified
	 * ConcreteWorkProductDescriptor
	 * 
	 * @param _concreteWorkProductDescriptors
	 *            the Set of ConcreteWorkProductDescriptor to relate to the
	 *            WorkProductDescriptor
	 */
	public void addAllConcreteWorkProductDescriptors(
			Set<ConcreteWorkProductDescriptor> _concreteWorkProductDescriptors) {
		for (ConcreteWorkProductDescriptor cwpd : _concreteWorkProductDescriptors) {
			cwpd.addWorkProductDescriptor(this);
		}
	}

	/**
	 * Removes the WorkProductDescriptor to all the related
	 * ConcreteWorkProductDescriptor
	 */
	public void removeAllConcreteWorkProductDescriptors() {
		for (ConcreteWorkProductDescriptor cwpd : this
				.getConcreteWorkProductDescriptors())
			cwpd.setWorkProductDescriptor(null);
		this.getConcreteWorkProductDescriptors().clear();
	}

	/*
	 * relation between WorkProductDescriptor and WorkProductDefinition
	 */

	/**
	 * Adds a WorkProductDefinition to the Set
	 * 
	 * @param _workProduct
	 */
	public void addWorkProductDefinition(WorkProductDefinition _workProduct) {
		this.workProductDefinition = _workProduct;
		_workProduct.getWorkProductDescriptors().add(this);
	}

	/**
	 * Removes a WorkProductDefinition
	 * 
	 * @param _workProduct
	 */
	public void removeWorkProductDefinition(WorkProductDefinition _workProduct) {
		_workProduct.getWorkProductDescriptors().remove(this);
		this.workProductDefinition = null;
	}

	/*
	 * Getter and Setter.
	 */

	/**
	 * Getter of workProductDefinition.
	 * 
	 * @return the workProductDefinition.
	 */
	public WorkProductDefinition getWorkProductDefinition() {
		return this.workProductDefinition;
	}

	/**
	 * Setter of workProductDefinition.
	 * 
	 * @param _workProductDefinition
	 *            The workProductDefinition to set.
	 */
	public void setWorkProductDefinition(
			WorkProductDefinition _workProductDefinition) {
		this.workProductDefinition = _workProductDefinition;
	}

	/**
	 * Returns the attribute concreteWorkProductDescriptors
	 * 
	 * @return the concreteWorkProductDescriptors
	 */
	public Set<ConcreteWorkProductDescriptor> getConcreteWorkProductDescriptors() {
		return concreteWorkProductDescriptors;
	}

	/**
	 * Set the attribute concreteWorkProductDescriptors with the specified
	 * Collection
	 * 
	 * @param concreteWorkProductDescriptors
	 *            the Collection to set
	 */
	public void setConcreteWorkProductDescriptors(
			Set<ConcreteWorkProductDescriptor> concreteWorkProductDescriptors) {
		this.concreteWorkProductDescriptors = concreteWorkProductDescriptors;
	}

	/**
	 * Defines if a WorkProductDescriptor is deliverable
	 * 
	 * @return the isDeliverable
	 */
	public boolean isDeliverable() {
		return this.isDeliverable;
	}

	/**
	 * Sets the attribute isDeliverable of the WorkProductDescriptor
	 * 
	 * @param _isDeliverable
	 *            the isDeliverable to set
	 */
	public void setDeliverable(boolean _isDeliverable) {
		this.isDeliverable = _isDeliverable;
	}

	/**
	 * Getter of the attribute activityEntryState
	 * 
	 * @return the WorkProductDescriptor's activityEntryState
	 */
	public String getActivityEntryState() {
		return this.activityEntryState;
	}

	/**
	 * Setter of the attribute activityEntryState
	 * 
	 * @param _activityEntryState
	 *            the activityEntryState to set
	 */
	public void setActivityEntryState(String _activityEntryState) {
		this.activityEntryState = _activityEntryState;
	}

	/**
	 * Gets the attribute activityExitState of the WorkProductDescriptor
	 * 
	 * @return the activityExitState
	 */
	public String getActivityExitState() {
		return this.activityExitState;
	}

	/**
	 * Sets the attribute activityExitState of the WorkProductDescriptor
	 * 
	 * @param _activityExitState
	 *            the activityExitState to set
	 */
	public void setActivityExitState(String _activityExitState) {
		this.activityExitState = _activityExitState;
	}

	/**
	 * Gets the Collection userActivities of the WorkProductDescriptor
	 * 
	 * @return the userActivities
	 */
	public Set<Activity> getUserActivities() {
		return userActivities;
	}

	/**
	 * Gets the Collection producerActivities of the WorkProductDescriptor
	 * 
	 * @return the producerActivities
	 */
	public Set<Activity> getProducerActivities() {
		return producerActivities;
	}

	/**
	 * Sets the Collection userActivities of the WorkProductDescriptor
	 * 
	 * @param _userActivities
	 *            the userActivities to set
	 */
	public void setUserActivities(Set<Activity> _userActivities) {
		userActivities = _userActivities;
	}

	/**
	 * Sets the Collection producerActivities of the WorkProductDescriptor
	 * 
	 * @param _producerActivities
	 *            the producerActivities to set
	 */
	public void setProducerActivities(Set<Activity> _producerActivities) {
		producerActivities = _producerActivities;
	}

	/**
	 * Gets the attribute responsibleRoleDescriptor of the WorkProductDescriptor
	 * 
	 * @return the responsibleRoleDescriptor
	 */
	public RoleDescriptor getResponsibleRoleDescriptor() {
		return this.responsibleRoleDescriptor;
	}

	/**
	 * Sets the attribute responsibleRoleDescriptor of the WorkProductDescriptor
	 * 
	 * @param _responsibleRoleDescriptor
	 *            the responsibleRoleDescriptor to set
	 */
	public void setResponsibleRoleDescriptor(
			RoleDescriptor _responsibleRoleDescriptor) {
		this.responsibleRoleDescriptor = _responsibleRoleDescriptor;
	}

	/**
	 * Attach a workProductDescriptor to a roleDescriptor
	 * 
	 * @param _roleDescriptor
	 */
	public void addResponsibleRole(RoleDescriptor _roleDescriptor) {
		this.responsibleRoleDescriptor = _roleDescriptor;
		_roleDescriptor.getResponsibleFor().add(this);
	}

	/**
	 * Gets the attribute producerTasks of the WorkProductDescriptor
	 * 
	 * @return the producerTasks i.e. return a list of tasks which produce this
	 *         workproduct
	 */
	public Set<TaskDescriptor> getProducerTasks() {
		return this.producerTasks;
	}

	/**
	 * Sets the attribute producerTasks of the WorkProductDescriptor
	 * 
	 * @param _producerTasks
	 *            the producerTasks to set
	 */
	public void setProducerTasks(Set<TaskDescriptor> _producerTasks) {
		this.producerTasks = _producerTasks;
	}

	/**
	 * Gets the attribute optionalUserTasks of the WorkProductDescriptor
	 * 
	 * @return the optionalUserTasks
	 */
	public Set<TaskDescriptor> getOptionalUserTasks() {
		return this.optionalUserTasks;
	}

	/**
	 * Sets the attribute optionalUserTasks of the WorkProductDescriptor
	 * 
	 * @param _optionalUserTasks
	 *            the optionalUserTasks to set
	 */
	public void setOptionalUserTasks(Set<TaskDescriptor> _optionalUserTasks) {
		this.optionalUserTasks = _optionalUserTasks;
	}

	/**
	 * Gets the attribute mandatoryUserTasks of the WorkProductDescriptor
	 * 
	 * @return the mandatoryUserTasks
	 */
	public Set<TaskDescriptor> getMandatoryUserTasks() {
		return this.mandatoryUserTasks;
	}

	/**
	 * Sets the attribute mandatoryUserTasks of the WorkProductDescriptor
	 * 
	 * @param _mandatoryUserTasks
	 *            the mandatoryUserTasks to set
	 */
	public void setMandatoryUserTasks(Set<TaskDescriptor> _mandatoryUserTasks) {
		this.mandatoryUserTasks = _mandatoryUserTasks;
	}

	/**
	 * Adds a relation between the WorkProductDescriptor and the specified
	 * producer TaskDescriptor
	 * 
	 * @param _taskDescriptor
	 *            the TaskDescriptor to relate to the WorkProductDescriptor
	 */
	public void addProducerTask(TaskDescriptor _taskDescriptor) {
		this.producerTasks.add(_taskDescriptor);
		_taskDescriptor.getOutputWorkProductDescriptors().add(this);

	}

	/**
	 * Adds a relation between the WorkProductDescriptor and the specified
	 * optional user TaskDescriptor
	 * 
	 * @param _taskDescriptor
	 */
	public void addOptionalUser(TaskDescriptor _taskDescriptor) {
		this.optionalUserTasks.add(_taskDescriptor);
		_taskDescriptor.getOptionalInputWorkProductDescriptors().add(this);

	}

	/**
	 * Adds a relation between the WorkProductDescriptor and the specified
	 * mandatory user TaskDescriptor
	 * 
	 * @param _taskDescriptor
	 */
	public void addMandatoryUser(TaskDescriptor _taskDescriptor) {
		this.mandatoryUserTasks.add(_taskDescriptor);
		_taskDescriptor.getMandatoryInputWorkProductDescriptors().add(this);

	}

	/**
	 * Adds the WorkProductDescriptor to all the producer TaskDescriptors from
	 * the Collection in parameter
	 * 
	 * @param _producerTasks
	 *            the Set of TaskDescriptors to relate to the
	 *            WorkProductDescriptor
	 */
	public void addAllProducerTasks(Set<TaskDescriptor> _producerTasks) {
		for (TaskDescriptor task : _producerTasks) {
			task.addOutputWorkProduct(this);
		}

	}

	/**
	 * Adds the WorkProductDescriptor to all the optional user TaskDescriptors
	 * from the Collection in parameter
	 * 
	 * @param _optionalUserTasks
	 */
	public void addAllOptionalUserTasks(Set<TaskDescriptor> _optionalUserTasks) {
		for (TaskDescriptor task : _optionalUserTasks) {
			task.addOptionalInputWorkProduct(this);
		}

	}

	/**
	 * Adds the WorkProductDescriptor to all the mandatory user TaskDescriptors
	 * from the Collection in parameter
	 * 
	 * @param _mandatoryUserTasks
	 */
	public void addAllMandatoryUserTasks(Set<TaskDescriptor> _mandatoryUserTasks) {
		for (TaskDescriptor task : _mandatoryUserTasks) {
			task.addMandatoryInputWorkProduct(this);
		}

	}

	/**
	 * Getter for the attribute isOutOfProcess of the WorkProductDescriptor
	 * 
	 * @return the isOutOfProcess
	 */
	public boolean getIsOutOfProcess() {
		return this.isOutOfProcess;
	}

	/**
	 * Setter for the attribute isOutOfProcess of the WorkProductDescriptor
	 * 
	 * @param _isOutOfProcess
	 *            the isOutOfProcess to set
	 */
	public void setIsOutOfProcess(boolean _isOutOfProcess) {
		this.isOutOfProcess = _isOutOfProcess;
	}
}
