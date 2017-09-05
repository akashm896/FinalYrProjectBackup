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

//TODO [Wilos3]MAPPING hibernate
package wilos.model.misc.concreteworkproduct;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.project.Project;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.model.spem2.workproduct.WorkProductDescriptor;
import wilos.utils.Constantes.State;

/**
 * A ConcreteRoleDescriptor is a specific {@link WorkProductDescriptor} for a
 * {@link Project}.
 * 
 */
public class ConcreteWorkProductDescriptor extends ConcreteBreakdownElement {


	private WorkProductDescriptor workProductDescriptor;

	private String state;

	private String participant;

	private ConcreteRoleDescriptor responsibleConcreteRoleDescriptor;

	private boolean isDeliverable;

	private Set<ConcreteTaskDescriptor> producerConcreteTasks;

	private Set<ConcreteTaskDescriptor> optionalUserConcreteTasks;

	private Set<ConcreteTaskDescriptor> mandatoryUserConcreteTasks;

	/**
	 * Class constructor
	 * 
	 */
	public ConcreteWorkProductDescriptor() {
		this.producerConcreteTasks = new HashSet<ConcreteTaskDescriptor>();
		this.optionalUserConcreteTasks = new HashSet<ConcreteTaskDescriptor>();
		this.mandatoryUserConcreteTasks = new HashSet<ConcreteTaskDescriptor>();
		this.state = State.CREATED;
	}

	/**
	 * Returns a copy of the current instance of ConcreteWorkProductDescriptor
	 * 
	 * @return a copy of the ConcreteWorkProductDescriptor
	 * @throws CloneNotSupportedException
	 */
	@Override
	public ConcreteWorkProductDescriptor clone()
			throws CloneNotSupportedException {
		ConcreteWorkProductDescriptor concreteWorkProductDescriptor = new ConcreteWorkProductDescriptor();
		concreteWorkProductDescriptor.copy(this);
		return concreteWorkProductDescriptor;
	}

	/**
	 * Copy the values of the specified ConcreteWorkProductDescriptor into the
	 * current instance of the class.
	 * 
	 * @param _concreteWorkProductDescriptor
	 *            the ConcreteWorkProductDescriptor to copy
	 */
	protected void copy(
			final ConcreteWorkProductDescriptor _concreteWorkProductDescriptor) {
		super.copy(_concreteWorkProductDescriptor);
		this.producerConcreteTasks.addAll(_concreteWorkProductDescriptor
				.getProducerConcreteTasks());
		this.optionalUserConcreteTasks.addAll(_concreteWorkProductDescriptor
				.getOptionalUserConcreteTasks());
		this.mandatoryUserConcreteTasks.addAll(_concreteWorkProductDescriptor
				.getMandatoryUserConcreteTasks());
		this.responsibleConcreteRoleDescriptor = _concreteWorkProductDescriptor
				.getResponsibleConcreteRoleDescriptor();
		this.state = _concreteWorkProductDescriptor.getState();
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the class.
	 * 
	 * @param obj
	 *            the Object to be compare to the ConcreteWorkProductDescriptor
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof ConcreteWorkProductDescriptor == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		ConcreteWorkProductDescriptor concreteWorkProductDescriptor = (ConcreteWorkProductDescriptor) obj;
		return new EqualsBuilder()
				.appendSuper(super.equals(concreteWorkProductDescriptor))
				.append(this.producerConcreteTasks,
						concreteWorkProductDescriptor.producerConcreteTasks)
				.append(this.optionalUserConcreteTasks,
						concreteWorkProductDescriptor.optionalUserConcreteTasks)
				.append(
						this.mandatoryUserConcreteTasks,
						concreteWorkProductDescriptor.mandatoryUserConcreteTasks)
				.append(
						this.responsibleConcreteRoleDescriptor,
						concreteWorkProductDescriptor.responsibleConcreteRoleDescriptor)
				.append(this.state, concreteWorkProductDescriptor.state)
				.isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of
	 *         ConcreteWorkProductDescriptor
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(this.workProductDescriptor).append(this.participant)
				.append(this.producerConcreteTasks).append(
						this.optionalUserConcreteTasks).append(
						this.mandatoryUserConcreteTasks).append(
						this.responsibleConcreteRoleDescriptor).append(
						this.state).toHashCode();
	}

	/*
	 * Relation between ConcreteWorkProductDescriptor and ConcreteActivity
	 * 
	 */

	/**
	 * Adds a relation between the current instance of
	 * ConcreteWorkProductDescriptor and a specified ConcreteTaskDescriptor.
	 * 
	 * @param _producer
	 *            Task which produces this workproduct
	 */
	public void addProducerConcreteTask(ConcreteTaskDescriptor _producer) {
		this.producerConcreteTasks.add(_producer);
		_producer.getOutputConcreteWorkProductDescriptors().add(this);
	}

	/**
	 * REmoves the relation between the current instance of
	 * ConcreteWorkProductDescriptor and a specified ConcreteTaskDescriptor.
	 * 
	 * @param _producer
	 *            Task which produces this workproduct
	 */
	public void removeProducerConcreteTask(ConcreteTaskDescriptor _producer) {
		_producer.getOutputConcreteWorkProductDescriptors().remove(this);
		this.producerConcreteTasks.remove(_producer);

	}

	/**
	 * Adds a relation between the current instance of
	 * ConcreteWorkProductDescriptor and a specified optional
	 * ConcreteTaskDescriptor.
	 * 
	 * @param _user
	 *            Activity which uses this workproduct
	 */
	public void addOptionalUserConcreteTask(ConcreteTaskDescriptor _user) {
		this.optionalUserConcreteTasks.add(_user);
		_user.getOptionalInputConcreteWorkProductDescriptors().add(this);
	}

	/**
	 * Removes the relation between the current instance of
	 * ConcreteWorkProductDescriptor and a specified optional
	 * ConcreteTaskDescriptor.
	 * 
	 * @param _user
	 *            Activity which uses this workproduct
	 */
	public void removeOptionalUserConcreteTask(ConcreteTaskDescriptor _user) {
		_user.getOptionalInputConcreteWorkProductDescriptors().remove(this);
		this.optionalUserConcreteTasks.remove(_user);

	}

	/**
	 * Adds a relation between the current instance of
	 * ConcreteWorkProductDescriptor and a specified mandatory user
	 * ConcreteTaskDescriptor.
	 * 
	 * @param _user
	 *            Activity which uses this workproduct
	 */
	public void addMandatoryUserConcreteTask(ConcreteTaskDescriptor _user) {
		this.mandatoryUserConcreteTasks.add(_user);
		_user.getMandatoryInputConcreteWorkProductDescriptors().add(this);
	}

	/**
	 * Removes the relation between the current instance of
	 * ConcreteWorkProductDescriptor and a specified mandatory user
	 * ConcreteTaskDescriptor.
	 * 
	 * @param _user
	 *            Activity which uses this workproduct
	 */
	public void removeMandatoryUserConcreteTask(ConcreteTaskDescriptor _user) {
		_user.getMandatoryInputConcreteWorkProductDescriptors().remove(this);
		this.mandatoryUserConcreteTasks.remove(_user);

	}

	/*
	 * Relation between ConcreteWorkProductDescriptor and ConcreteRoleDescriptor
	 * 
	 */

	/**
	 * Adds a relation between the current instance of
	 * ConcreteWorkProductDescriptor and a specified ConcreteRoleDescriptor.
	 * 
	 * @param _responsible
	 *            the ConcreteRoleDescriptor to link
	 */
	public void addResponsibleConcreteRoleDescriptor(
			ConcreteRoleDescriptor _responsible) {
		this.responsibleConcreteRoleDescriptor = _responsible;
		_responsible.getConcreteWorkProductDescriptors().add(this);
	}

	/**
	 * Removes the relation between the current instance of
	 * ConcreteWorkProductDescriptor and a specified ConcreteRoleDescriptor.
	 * 
	 * @param _responsible
	 *            the ConcreteRoleDescriptor to unlink
	 */
	public void removeResponsibleConcreteRoleDescriptor(
			ConcreteRoleDescriptor _responsible) {
		this.responsibleConcreteRoleDescriptor = null;
		_responsible.getConcreteWorkProductDescriptors().remove(this);
	}

	/*
	 * Getter and Setter
	 * 
	 */

	/**
	 * Returns the ConcreteRoleDescriptor assigned to the
	 * ConcreteWorkProductDescriptor
	 * 
	 * @return the responsibleConcreteRoleDescriptor
	 */
	public ConcreteRoleDescriptor getResponsibleConcreteRoleDescriptor() {
		return this.responsibleConcreteRoleDescriptor;
	}

	/**
	 * Sets the ConcreteRoleDescriptor assigned to the
	 * ConcreteWorkProductDescriptor
	 * 
	 * @param _responsibleConcreteRoleDescriptor
	 *            the responsibleConcreteRoleDescriptor to set
	 */
	public void setResponsibleConcreteRoleDescriptor(
			ConcreteRoleDescriptor _responsibleConcreteRoleDescriptor) {
		this.responsibleConcreteRoleDescriptor = _responsibleConcreteRoleDescriptor;
	}

	/**
	 * Defines whether a ConcreteWorkProductDescriptor is deliverable or not
	 * 
	 * @return true if the ConcreteWorkProductDescriptor is deliverable, false
	 *         otherwise
	 */
	public boolean isDeliverable() {
		return this.isDeliverable;
	}

	/**
	 * Sets the deliverable status of the ConcreteWorkProductDescriptor
	 * 
	 * @param _isDeliverable
	 *            the isDeliverable to set
	 */
	public void setDeliverable(boolean _isDeliverable) {
		this.isDeliverable = _isDeliverable;
	}

	/**
	 * Adds a relation between the current instance of
	 * ConcreteWorkProductDescriptor and a specified WorkProductDescriptor.
	 * 
	 * @param _workProductDescriptor
	 *            the WorkProductDescriptor to link
	 */
	public void addWorkProductDescriptor(
			WorkProductDescriptor _workProductDescriptor) {
		this.workProductDescriptor = _workProductDescriptor;
		_workProductDescriptor.getConcreteWorkProductDescriptors().add(this);
	}

	/**
	 * Removes the relation between the current instance of
	 * ConcreteWorkProductDescriptor and a specified WorkProductDescriptor.
	 * 
	 * @param _workProductDescriptor
	 *            the WorkProductDescriptor to unlink
	 */
	public void removeWorkProductDescriptor(
			WorkProductDescriptor _workProductDescriptor) {
		this.workProductDescriptor = null;
		_workProductDescriptor.getConcreteWorkProductDescriptors().remove(this);
	}

	/**
	 * Assigns a WorkProductDescriptor to the current instance of
	 * ConcreteWorkProductDescriptor
	 * 
	 * @param _workProductDescriptor
	 *            the WorkProductDescriptor to assigned
	 */
	public void setWorkProductDescriptor(
			WorkProductDescriptor _workProductDescriptor) {
		this.workProductDescriptor = _workProductDescriptor;
	}

	/**
	 * Returns the WorkProductDescriptor of the ConcreteWorkProductDescriptor
	 * 
	 * @param _workProductDescriptor
	 *            the WorkProductDescriptor to get
	 */
	public WorkProductDescriptor getWorkProductDescriptor() {
		return this.workProductDescriptor;
	}

	/**
	 * Returns the Participant assigned to the ConcreteWorkProductDescriptor
	 * 
	 * @return the participant
	 */
	public String getParticipant() {
		return participant;
	}

	/**
	 * Sets the Participant assigned to the ConcreteWorkProductDescriptor
	 * 
	 * @param _participant
	 *            the participant to set
	 */
	public void setParticipant(String _participant) {
		participant = _participant;
	}

	/**
	 * Returns the state of the ConcreteWorkProductDescriptor
	 * 
	 * @return the String that symbolizes the ConcreteWorkProductDescriptor
	 *         state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state of the ConcreteWorkProductDescriptor
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Returns the Collection of ConcreteTaskDescriptor related to the
	 * ConcreteWorkProductDescriptor
	 * 
	 * @return the producerConcreteTasks
	 */
	public Set<ConcreteTaskDescriptor> getProducerConcreteTasks() {
		return this.producerConcreteTasks;
	}

	/**
	 * Sets the Collection of ConcreteTaskDescriptor related to the
	 * ConcreteWorkProductDescriptor with the one in parameter
	 * 
	 * @param _producerConcreteTasks
	 *            the producerConcreteTasks to set
	 */
	public void setProducerConcreteTasks(
			Set<ConcreteTaskDescriptor> _producerConcreteTasks) {
		this.producerConcreteTasks = _producerConcreteTasks;
	}

	/**
	 * Returns the Collection of optional ConcreteTaskDescriptor related to the
	 * ConcreteWorkProductDescriptor
	 * 
	 * @return the optionalUserConcreteTasks
	 */
	public Set<ConcreteTaskDescriptor> getOptionalUserConcreteTasks() {
		return this.optionalUserConcreteTasks;
	}

	/**
	 * Sets the Collection of optional ConcreteTaskDescriptor related to the
	 * ConcreteWorkProductDescriptor with the one in parameter
	 * 
	 * @param _optionalUserConcreteTasks
	 *            the optionalUserConcreteTasks to set
	 */
	public void setOptionalUserConcreteTasks(
			Set<ConcreteTaskDescriptor> _optionalUserConcreteTasks) {
		this.optionalUserConcreteTasks = _optionalUserConcreteTasks;
	}

	/**
	 * Returns the Collection of mandatory user ConcreteTaskDescriptor related
	 * to the ConcreteWorkProductDescriptor
	 * 
	 * @return the mandatoryUserConcreteTasks
	 */
	public Set<ConcreteTaskDescriptor> getMandatoryUserConcreteTasks() {
		return this.mandatoryUserConcreteTasks;
	}

	/**
	 * Sets the Collection of mandatory user ConcreteTaskDescriptor related to
	 * the ConcreteWorkProductDescriptor with the one in parameter
	 * 
	 * @param _mandatoryUserConcreteTasks
	 *            the mandatoryUserConcreteTasks to set
	 */
	public void setMandatoryUserConcreteTasks(
			Set<ConcreteTaskDescriptor> _mandatoryUserConcreteTasks) {
		this.mandatoryUserConcreteTasks = _mandatoryUserConcreteTasks;
	}

	/**
	 * Adds a relation between the current instance of
	 * ConcreteWorkProductDescriptor, the specified Project and all the
	 * specified producer TaskDescriptor.
	 * 
	 * @param _producerTasks
	 *            the Collection of producer TaskDescriptor
	 * @param _project
	 *            the Project related to the ConcreteWorkProductDescriptor
	 */
	public void addAllProducerConcreteTasks(Set<TaskDescriptor> _producerTasks,
			Project _project) {
		for (TaskDescriptor td : _producerTasks) {
			for (ConcreteTaskDescriptor ctd : td.getConcreteTaskDescriptors()) {
				if (ctd.getProject().getId().equals(_project.getId())) {
					this.producerConcreteTasks.add(ctd);
					ctd.addOutputConcreteWorkProduct(this);
				}
			}
		}
	}

	/**
	 * Adds a relation between the current instance of
	 * ConcreteWorkProductDescriptor, the specified Project and all the
	 * specified optional TaskDescriptor.
	 * 
	 * @param _optionalUserTasks
	 *            the Collection of optional TaskDescriptor
	 * @param _project
	 *            the Project related to the ConcreteWorkProductDescriptor
	 */
	public void addAllOptionalUserConcreteTasks(
			Set<TaskDescriptor> _optionalUserTasks, Project _project) {
		for (TaskDescriptor td : _optionalUserTasks) {
			for (ConcreteTaskDescriptor ctd : td.getConcreteTaskDescriptors()) {
				if (ctd.getProject().getId().equals(_project.getId())) {
					this.optionalUserConcreteTasks.add(ctd);
					ctd.addOptionalInputConcreteWorkProduct(this);
				}
			}
		}
	}

	/**
	 * Adds a relation between the current instance of
	 * ConcreteWorkProductDescriptor, the specified Project and all the
	 * specified mandatory user TaskDescriptor.
	 * 
	 * @param _optionalUserTasks
	 *            the Collection of mandatory user TaskDescriptor
	 * @param _project
	 *            the Project related to the ConcreteWorkProductDescriptor
	 */
	public void addAllMandatoryUserConcreteTasks(
			Set<TaskDescriptor> _mandatoryUserTasks, Project _project) {
		for (TaskDescriptor td : _mandatoryUserTasks) {
			for (ConcreteTaskDescriptor ctd : td.getConcreteTaskDescriptors()) {
				if (ctd.getProject().getId().equals(_project.getId())) {
					this.mandatoryUserConcreteTasks.add(ctd);
					ctd.addMandatoryInputConcreteWorkProduct(this);
				}
			}
		}
	}

	/**
	 * remove all producer/output links between the task and the workproduct
	 * 
	 */
	public void removeAllProducerConcreteTasks() {
		for (ConcreteTaskDescriptor ctd : this.producerConcreteTasks) {
			ctd.removeOutputConcreteWorkProduct(this);
		}
		this.producerConcreteTasks.clear();
	}

	/**
	 * remove all optionaluser/optionalinput links between the task and the
	 * workproduct
	 * 
	 */
	public void removeAllOptionalUserConcreteTasks() {
		for (ConcreteTaskDescriptor ctd : this.optionalUserConcreteTasks) {
			ctd.removeOptionalInputConcreteWorkProduct(this);
		}
		this.optionalUserConcreteTasks.clear();
	}

	/**
	 * remove all mandatoryuser/mandatoryinput links between the task and the
	 * workproduct
	 * 
	 */
	public void removeAllMandatoryUserConcreteTasks() {
		for (ConcreteTaskDescriptor ctd : this.mandatoryUserConcreteTasks) {
			ctd.removeMandatoryInputConcreteWorkProduct(this);
		}
		this.mandatoryUserConcreteTasks.clear();
	}
}
