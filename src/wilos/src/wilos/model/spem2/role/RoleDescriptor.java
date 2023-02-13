/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Mathieu BENOIT <mathieu-benoit@hotmail.fr>
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

package wilos.model.spem2.role;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.spem2.breakdownelement.BreakdownElement;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.model.spem2.workproduct.WorkProductDescriptor;

/**
 * 
 * A RoleDescriptor represents a {@link RoleDefinition} in the context of one
 * specific Activity. Every breakdown structure can define different
 * relationships of {@link RoleDefinition} Descriptors to {@link TaskDefinition}
 * Descriptors and Work Product Descriptors. Therefore one
 * {@link RoleDefinition} can be represented by many {@link RoleDefinition}
 * Descriptors each within the context of an {@link Activity} with its own set
 * of relationships.
 * <p />
 * It's an element of the SPEM2 specification of the OMG organization
 * (http://www.omg.org/).
 * 
 */
public class RoleDescriptor extends BreakdownElement implements Cloneable {

	private RoleDefinition roleDefinition;

	/**
	 * The main tasks of the roleDefinition
	 */
	private Set<TaskDescriptor> primaryTasks;

	/**
	 * The additional tasks of the roleDefinition
	 */
	private Set<TaskDescriptor> additionalTasks;

	/**
	 * Associated ConcreteRoleDescriptors.
	 */
	private Set<ConcreteRoleDescriptor> concreteRoleDescriptors;

	private Set<WorkProductDescriptor> responsibleFor;

	private boolean isOutOfProcess;

	/**
	 * Class constructor
	 */
	public RoleDescriptor() {
		super();
		this.primaryTasks = new HashSet<TaskDescriptor>();
		this.additionalTasks = new HashSet<TaskDescriptor>();
		this.concreteRoleDescriptors = new HashSet<ConcreteRoleDescriptor>();
		this.responsibleFor = new HashSet<WorkProductDescriptor>();
		this.isOutOfProcess = false;
	}

	/**
	 * Returns a copy of the current instance of RoleDescriptor
	 * 
	 * @return a copy of the RoleDescriptor
	 * @throws CloneNotSupportedException
	 */
	@Override
	public RoleDescriptor clone() throws CloneNotSupportedException {
		RoleDescriptor roleDescriptor = new RoleDescriptor();
		roleDescriptor.copy(this);
		return roleDescriptor;
	}

	/**
	 * Copy the values of the specified RoleDescriptor into the current instance
	 * of the class.
	 * 
	 * @param _roleDescriptor
	 *            The RoleDescriptor to copy.
	 */
	protected void copy(final RoleDescriptor _roleDescriptor) {
		super.copy(_roleDescriptor);
		this.additionalTasks.addAll(_roleDescriptor.getAdditionalTasks());
		this.primaryTasks.addAll(_roleDescriptor.getPrimaryTasks());
		this.roleDefinition = _roleDescriptor.getRoleDefinition();
		this.concreteRoleDescriptors.addAll(_roleDescriptor
				.getConcreteRoleDescriptors());
		this.responsibleFor.addAll(_roleDescriptor.getResponsibleFor());
		this.isOutOfProcess = _roleDescriptor.getIsOutOfProcess();
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the RoleDescriptor.
	 * 
	 * @param obj
	 *            the Object to be compare to the RoleDescriptor
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof RoleDescriptor == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}

		RoleDescriptor roleDescriptor = (RoleDescriptor) obj;
		return new EqualsBuilder().appendSuper(super.equals(roleDescriptor))
				.append(this.roleDefinition, roleDescriptor.roleDefinition)
				.append(this.primaryTasks, roleDescriptor.primaryTasks).append(
						this.additionalTasks, roleDescriptor.additionalTasks)
				.append(this.concreteRoleDescriptors,
						roleDescriptor.concreteRoleDescriptors).append(
						this.responsibleFor, roleDescriptor.responsibleFor)
				.append(this.isOutOfProcess, roleDescriptor.isOutOfProcess)
				.isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of RoleDescriptor
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(this.roleDefinition).toHashCode();
	}

	/*
	 * relation between RoleDescriptor and ConcreteRoleDescriptor.
	 * 
	 */

	/**
	 * Adds a relation between the RoleDescriptor and the specified
	 * ConcreteRoleDescriptor
	 * 
	 * @param _concreteRoleDescriptor
	 *            the ConcreteRoleDescriptor to be linked to
	 */
	public void addConcreteRoleDescriptor(
			ConcreteRoleDescriptor _concreteRoleDescriptor) {
		this.concreteRoleDescriptors.add(_concreteRoleDescriptor);
		_concreteRoleDescriptor.addRoleDescriptor(this);
	}

	/**
	 * Removes the relation between the RoleDescriptor and the specified
	 * ConcreteRoleDescriptor
	 * 
	 * @param _concreteRoleDescriptor
	 *            the ConcreteRoleDescriptor to be unlinked to
	 */
	public void removeConcreteRoleDescriptor(
			ConcreteRoleDescriptor _concreteRoleDescriptor) {
		_concreteRoleDescriptor.removeRoleDescriptor(this);
		this.concreteRoleDescriptors.remove(_concreteRoleDescriptor);
	}

	/**
	 * Assigns the RoleDescriptor to all the ConcreteRoleDescriptor of the Set
	 * in parameter
	 * 
	 * @param _concreteRoleDescriptors
	 *            the Set of ConcreteRoleDescriptor to be assigned to
	 */
	public void addAllConcreteRoleDescriptors(
			Set<ConcreteRoleDescriptor> _concreteRoleDescriptors) {
		for (ConcreteRoleDescriptor crd : _concreteRoleDescriptors) {
			crd.addRoleDescriptor(this);
		}
	}

	/**
	 * Removes the RoleDescriptor from all the related ConcreteRoleDescriptor
	 */
	public void removeAllConcreteRoleDescriptors() {
		for (ConcreteRoleDescriptor crd : this.getConcreteRoleDescriptors())
			crd.setRoleDescriptor(null);
		this.getConcreteRoleDescriptors().clear();
	}

	/*
	 * relation between RoleDescriptor and RoleDefinition
	 */

	/**
	 * Adds a RoleDefinition to the Set
	 * 
	 * @param _role
	 *            the RoleDefinition to add
	 */
	public void addRoleDefinition(RoleDefinition _role) {
		this.roleDefinition = _role;
		_role.getRoleDescriptors().add(this);
	}

	/**
	 * Removes a RoleDefinition
	 * 
	 * @param _role
	 *            the RoleDefinition to remove
	 */
	public void removeRoleDefinition(RoleDefinition _role) {
		_role.getRoleDescriptors().remove(this);
		this.roleDefinition = null;
	}

	/*
	 * relation mainrole between RoleDescriptor and TaskDescriptor
	 */

	/**
	 * Adds a taskDescritor to the Set
	 * 
	 * @param _task
	 */
	public void addPrimaryTask(TaskDescriptor _task) {
		this.primaryTasks.add(_task);
		_task.setMainRole(this);
	}

	/**
	 * Removes a TaskDescriptor to the RoleDescriptor
	 * 
	 * @param _task
	 */
	public void removePrimaryTask(TaskDescriptor _task) {
		_task.setMainRole(null);
		this.primaryTasks.remove(_task);
	}

	/**
	 * Adds the RoleDescriptor to all the TaskDesciptor of the Set in parameter
	 * 
	 * @param _role
	 *            the Set of TaskDescriptor to be related to
	 */
	public void addAllPrimaryTasks(Set<TaskDescriptor> _role) {
		for (TaskDescriptor _role1 : _role) {
			_role1.addMainRole(this);
		}
	}

	/**
	 * Adds the RoleDescriptor to all the WorkProductDescriptor of the Set in
	 * parameter
	 * 
	 * @param _wpd
	 *            the Set of WorkProductDescriptor to be related to
	 */
	public void addAllResponsibleFor(Set<WorkProductDescriptor> _wpd) {
		for (WorkProductDescriptor _workproductdescriptor : _wpd) {
			_workproductdescriptor.addResponsibleRole(this);
		}
	}

	/**
	 * Remove from an RoleDescriptor all its primaryTask
	 * 
	 */
	public void removeAllPrimaryTasks() {
		for (TaskDescriptor _task : this.primaryTasks) {
			_task.setMainRole(null);
		}
		this.primaryTasks.clear();
	}

	/*
	 * relation additionnalRoles between RoleDescriptor and TaskDescriptor
	 */

	/**
	 * Adds a TaskDescriptor to the Set
	 * 
	 * @param _task
	 *            the TaskDescriptor to be add
	 */
	public void addAdditionalTask(TaskDescriptor _task) {
		this.additionalTasks.add(_task);
		_task.getAdditionalRoles().add(this);
	}

	/**
	 * Removes a TaskDescriptor from the RoleDescriptor Set
	 * 
	 * @param _task
	 *            the TaskDescriptor to be remove
	 */
	public void removeAdditionalTask(TaskDescriptor _task) {
		_task.getAdditionalRoles().remove(this);
		this.additionalTasks.remove(_task);
	}

	/**
	 * Adds the RoleDescriptor to all the additional TaskDescriptor of the Set
	 * in parameter
	 * 
	 * @param _task
	 *            the Set of additional TaskDescriptor to bet related to
	 */
	public void addAllAdditionalTasks(Set<TaskDescriptor> _task) {
		for (TaskDescriptor task : _task) {
			task.addAdditionalRole(this);
		}
	}

	/**
	 * Removes all the additional TaskDescriptor related to RoleDescriptors
	 */
	public void removeAllAdditionalTasks() {
		for (TaskDescriptor _task : this.additionalTasks) {
			_task.getAdditionalRoles().remove(this);
		}
		this.additionalTasks.clear();
	}

	/*
	 * Getter and Setter.
	 */

	/**
	 * Getter of roleDefinition.
	 * 
	 * @return the roleDefinition.
	 */
	public RoleDefinition getRoleDefinition() {
		return this.roleDefinition;
	}

	/**
	 * Setter of roleDefinition.
	 * 
	 * @param _roleDefinition
	 *            The roleDefinition to set.
	 */
	public void setRoleDefinition(RoleDefinition _roleDefinition) {
		this.roleDefinition = _roleDefinition;
	}

	/**
	 * Getter of primaryTasks.
	 * 
	 * @return the primaryTasks.
	 */
	@SuppressWarnings("unused")
	public Set<TaskDescriptor> getPrimaryTasks() {
		return this.primaryTasks;
	}

	/**
	 * Setter of primaryTasks.
	 * 
	 * @param _primaryTasks
	 *            The primaryTasks to set.
	 */
	@SuppressWarnings("unused")
	public void setPrimaryTasks(Set<TaskDescriptor> _primaryTasks) {
		this.primaryTasks = _primaryTasks;
	}

	/**
	 * Getter of additionalTasks.
	 * 
	 * @return the additionalTasks.
	 */
	public Set<TaskDescriptor> getAdditionalTasks() {
		return this.additionalTasks;
	}

	/**
	 * Setter of additionalTasks.
	 * 
	 * @param _additionalTasks
	 *            The additionalTasks to set.
	 */
	@SuppressWarnings("unused")
	public void setAdditionalTasks(Set<TaskDescriptor> _additionalTasks) {
		this.additionalTasks = _additionalTasks;
	}

	/**
	 * Getter of concreteRoleDescriptors.
	 * 
	 * @return the concreteRoleDescriptors.
	 */
	public Set<ConcreteRoleDescriptor> getConcreteRoleDescriptors() {
		return concreteRoleDescriptors;
	}

	/**
	 * Setter of concreteRoleDescriptors.
	 * 
	 * @param concreteRoleDescriptors
	 *            The Collection of ConcreteRoleDescriptor to set.
	 */
	public void setConcreteRoleDescriptors(
			Set<ConcreteRoleDescriptor> concreteRoleDescriptors) {
		this.concreteRoleDescriptors = concreteRoleDescriptors;
	}

	/**
	 * Getter of responsible for the RoleDescriptor.
	 * 
	 * @return the responsibleFor
	 */
	public Set<WorkProductDescriptor> getResponsibleFor() {
		return this.responsibleFor;
	}

	/**
	 * Setter of responsible for the RoleDescriptor.
	 * 
	 * @param _responsibleFor
	 *            the ResponsibleFor to set
	 */
	public void setResponsibleFor(Set<WorkProductDescriptor> _responsibleFor) {
		this.responsibleFor = _responsibleFor;
	}

	/**
	 * Adds a relation between the RoleDescriptor and the specified
	 * WorkProductDescriptor
	 * 
	 * @param _workProductToBeset
	 *            the WorkProductDescriptor related to the RoleDescriptor
	 */
	public void addWorkProduct(WorkProductDescriptor _workProductToBeset) {
		this.responsibleFor.add(_workProductToBeset);
		_workProductToBeset.setResponsibleRoleDescriptor(this);

	}

	/**
	 * Defines if a RoleDescriptor is in or out of a Process
	 * 
	 * @return true if the RoleDescriptor is out of a Process
	 */
	public boolean getIsOutOfProcess() {
		return isOutOfProcess;
	}

	/**
	 * Sets the isOutOfProcess attribute with the value in parameter
	 * 
	 * @param isOutOfProcess
	 */
	public void setIsOutOfProcess(boolean isOutOfProcess) {
		this.isOutOfProcess = isOutOfProcess;
	}
}
