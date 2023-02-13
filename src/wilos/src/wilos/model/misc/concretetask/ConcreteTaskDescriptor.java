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

package wilos.model.misc.concretetask;

import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;
import wilos.model.misc.dailyremainingtime.DailyRemainingTime;
import wilos.model.misc.project.Project;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.model.spem2.workproduct.WorkProductDescriptor;
import wilos.presentation.web.utils.WebSessionService;

/**
 * 
 * A ConcreteTaskDescriptor is a specific {@link TaskDescriptor} for a
 * {@link Project}.
 * 
 */
public class ConcreteTaskDescriptor extends ConcreteWorkBreakdownElement {

	private TaskDescriptor taskDescriptor;

	private ConcreteRoleDescriptor mainConcreteRoleDescriptor;

	private Set<DailyRemainingTime> dailyRemainingTimes;

	private Date realStartingDate;

	private Date realFinishingDate;

	private float remainingTime;

	private float accomplishedTime;

	private Set<ConcreteWorkProductDescriptor> outputConcreteWorkProductDescriptors;

	private Set<ConcreteWorkProductDescriptor> optionalInputConcreteWorkProductDescriptors;

	private Set<ConcreteWorkProductDescriptor> mandatoryInputConcreteWorkProductDescriptors;

	/**
	 * Attribute for knowing if dependancies between task and workproducts
	 * states are activated
	 */
	private boolean isActivatedWorkProductStateDependancies = false;

	/**
	 * Class constructor
	 */
	public ConcreteTaskDescriptor() {
		super();
	}

	/**
	 * Returns a copy of the current instance of ConcreteTaskDescriptor
	 * 
	 * @return a copy of the ConcreteTaskDescriptor
	 * @throws CloneNotSupportedException
	 */
	@Override
	public ConcreteTaskDescriptor clone() throws CloneNotSupportedException {
		ConcreteTaskDescriptor concreteTaskDescriptor = new ConcreteTaskDescriptor();
		concreteTaskDescriptor.copy(this);
		return concreteTaskDescriptor;
	}

	/**
	 * Copy the values of the specified ConcreteTaskDescriptor into the current
	 * instance of the class.
	 * 
	 * @param _concreteTaskDescriptor
	 *            the ConcreteTaskDescriptor to copy
	 */
	protected void copy(final ConcreteTaskDescriptor _concreteTaskDescriptor) {
		super.copy(_concreteTaskDescriptor);
		this.taskDescriptor = _concreteTaskDescriptor.getTaskDescriptor();
		this.mainConcreteRoleDescriptor = _concreteTaskDescriptor
				.getMainConcreteRoleDescriptor();
		this.accomplishedTime = _concreteTaskDescriptor.getAccomplishedTime();
		this.realFinishingDate = _concreteTaskDescriptor.getRealFinishingDate();
		this.realStartingDate = _concreteTaskDescriptor.getRealStartingDate();
		this.remainingTime = _concreteTaskDescriptor.getRemainingTime();
		this.isActivatedWorkProductStateDependancies = _concreteTaskDescriptor
				.getIsActivatedWorkProductStateDependancies();
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the class.
	 * 
	 * @param obj
	 *            the Object to be compare to the ConcreteTaskDescriptor
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof ConcreteTaskDescriptor == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		ConcreteTaskDescriptor concreteTaskDescriptor = (ConcreteTaskDescriptor) obj;
		return new EqualsBuilder()
				.appendSuper(super.equals(concreteTaskDescriptor))
				.append(this.accomplishedTime,
						concreteTaskDescriptor.accomplishedTime)
				.append(this.taskDescriptor,
						concreteTaskDescriptor.taskDescriptor)
				.append(this.mainConcreteRoleDescriptor,
						concreteTaskDescriptor.mainConcreteRoleDescriptor)
				.append(this.realFinishingDate,
						concreteTaskDescriptor.realFinishingDate)
				.append(this.realStartingDate,
						concreteTaskDescriptor.realStartingDate)
				.append(this.remainingTime,
						concreteTaskDescriptor.remainingTime)
				.append(
						this.isActivatedWorkProductStateDependancies,
						concreteTaskDescriptor.isActivatedWorkProductStateDependancies)
				.isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of ConcreteTaskDescriptor
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(this.accomplishedTime).append(this.taskDescriptor)
				.append(this.mainConcreteRoleDescriptor).append(
						this.realFinishingDate).append(this.realStartingDate)
				.append(this.remainingTime).append(
						this.isActivatedWorkProductStateDependancies)
				.toHashCode();
	}

	/*
	 * Relation between ConcreteRoleDescriptor and ConcreteTaskDescriptor.
	 * 
	 */

	/**
	 * Compares the ConcreteRoleDescriptor of the ConcreteTaskDescriptor with WILOS_USER_ID
	 * 
	 * @return true if the ConcreteTaskDescriptor role id is the same as
	 *         WILOS_USER_ID, false otherwise
	 */
	public boolean getIsParticipant() {

		try {
		    	ConcreteRoleDescriptor crd = this.getMainConcreteRoleDescriptor();
			String roleId = crd.getParticipant().getId();

			String wilosUserId = (String) WebSessionService
					.getAttribute(WebSessionService.WILOS_USER_ID);

			if (roleId.equals(wilosUserId)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

	}

	public boolean getIsParticipant(ConcreteRoleDescriptor crd) {

		try {
		       	String roleId = crd.getParticipant().getId();

			String wilosUserId = (String) WebSessionService
					.getAttribute(WebSessionService.WILOS_USER_ID);

			if (roleId.equals(wilosUserId)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * Adds a relation between the ConcreteTaskDescriptor and the specified
	 * ConcreteRoleDescriptor.
	 * 
	 * @param _concreteRoleDescriptor
	 *            the ConcreteRoleDescriptor to relate to the
	 *            ConcreteTaskDescriptor
	 */
	public void addMainConcreteRoleDescriptor(
			ConcreteRoleDescriptor _concreteRoleDescriptor) {
		this.mainConcreteRoleDescriptor = _concreteRoleDescriptor;
		_concreteRoleDescriptor.getPrimaryConcreteTaskDescriptors().add(this);
	}

	/**
	 * Removes the relation between the ConcreteTaskDescriptor and the specified
	 * ConcreteRoleDescriptor.
	 * 
	 * @param _concreteRoleDescriptor
	 *            the ConcreteRoleDescriptor to unlinked to the
	 *            ConcreteTaskDescriptor
	 */
	public void removeMainConcreteRoleDescriptor(
			ConcreteRoleDescriptor _concreteRoleDescriptor) {
		_concreteRoleDescriptor.getPrimaryConcreteTaskDescriptors()
				.remove(this);
		this.mainConcreteRoleDescriptor = null;
	}

	/*
	 * Relation between DailyRemainingTime and ConcreteTaskDescriptor.
	 * 
	 */

	/**
	 * Adds a relation between the ConcreteTaskDescriptor and the specified
	 * DailyRemainingTime.
	 * 
	 * @param _dailyRemainingTime
	 *            the DailyRemainingTime to relate to the ConcreteTaskDescriptor
	 */
	public void addDailyRemainingTime(DailyRemainingTime _dailyRemainingTime) {
		this.dailyRemainingTimes.add(_dailyRemainingTime);
		_dailyRemainingTime.setDailyTaskDescriptor_id(this);
	}

	/**
	 * Removes the relation between the ConcreteTaskDescriptor and the specified
	 * DailyRemainingTime.
	 * 
	 * @param _dailyRemainingTime
	 *            the DailyRemainingTime to unlinked to the
	 *            ConcreteTaskDescriptor
	 */
	public void removeDailyRemainingTime(DailyRemainingTime _dailyRemainingTime) {
		_dailyRemainingTime.setDailyTaskDescriptor_id(null);
		this.dailyRemainingTimes.remove(_dailyRemainingTime);
	}

	/**
	 * Adds a relation between the ConcreteTaskDescriptor and all the specified
	 * DailyRemainingTime.
	 * 
	 * @param _dailyRemainingTimes
	 *            the Set of DailyRemainingTime to relate to the
	 *            ConcreteTaskDescriptor
	 */
	public void addAllDailyRemainingTimes(
			Set<DailyRemainingTime> _dailyRemainingTimes) {
		this.getDailyRemainingTimes().addAll(_dailyRemainingTimes);

		for (DailyRemainingTime drt : _dailyRemainingTimes) {
			drt.addPrimaryConcreteTaskDescriptor(this);
		}
	}

	/**
	 * Removes the relation between the ConcreteTaskDescriptor and all the
	 * related DailyRemainingTime.
	 */
	public void removeAllDailyRemainingTimes() {
		for (DailyRemainingTime drt : this.getDailyRemainingTimes())
			drt.setDailyTaskDescriptor_id(null);
		this.getDailyRemainingTimes().clear();
	}

	/*
	 * Relation between TaskDescriptor and ConcreteTaskDescriptor.
	 * 
	 */

	/**
	 * Adds a relation between the ConcreteTaskDescriptor and the specified
	 * TaskDescriptor.
	 * 
	 * @param _taskDescriptor
	 *            the TaskDescriptor to relate to the ConcreteTaskDescriptor
	 */
	public void addTaskDescriptor(TaskDescriptor _taskDescriptor) {
		this.taskDescriptor = _taskDescriptor;
		_taskDescriptor.getConcreteTaskDescriptors().add(this);
	}

	/**
	 * Removes the relation between the ConcreteTaskDescriptor and the specified
	 * TaskDescriptor.
	 * 
	 * @param _taskDescriptor
	 *            the TaskDescriptor to unlinked to the ConcreteTaskDescriptor
	 */
	public void removeTaskDescriptor(TaskDescriptor _taskDescriptor) {
		_taskDescriptor.getConcreteTaskDescriptors().remove(this);
		this.taskDescriptor = null;
	}

	/*
	 * Getter & Setter.
	 * 
	 */

	/**
	 * Returns the time that have been accomplished for a ConcreteTaskDescriptor
	 * 
	 * @return the value of the accomplished time
	 */
	public float getAccomplishedTime() {
		return accomplishedTime;
	}

	/**
	 * Initializes the accomplished time of the ConcreteTaskDescriptor.
	 * 
	 * @param accomplishedTime
	 *            the accomplished time to set
	 */
	public void setAccomplishedTime(float accomplishedTime) {
		this.accomplishedTime = accomplishedTime;
	}

	/**
	 * Returns the TaskDescriptor assigned to the ConcreteTaskDescriptor.
	 * 
	 * @return the TaskDescriptor of the ConcreteTaskDescriptor
	 */
	public TaskDescriptor getTaskDescriptor() {
		return taskDescriptor;
	}

	/**
	 * Initializes the TaskDescriptor of the ConcreteTaskDescriptor.
	 * 
	 * @param taskDescriptor
	 *            the TaskDescriptor to set
	 */
	public void setTaskDescriptor(TaskDescriptor taskDescriptor) {
		this.taskDescriptor = taskDescriptor;
	}

	/**
	 * Returns the ConcreteRoleDescriptor assigned to the
	 * ConcreteTaskDescriptor.
	 * 
	 * @return the ConcreteRoleDescriptor of the ConcreteTaskDescriptor
	 */
	public ConcreteRoleDescriptor getMainConcreteRoleDescriptor() {
		return mainConcreteRoleDescriptor;
	}

	/**
	 * Initializes the ConcreteRoleDescriptor of the ConcreteTaskDescriptor.
	 * 
	 * @param _mainConcreteRoleDescriptor
	 *            the ConcreteRoleDescriptor to set
	 */
	public void setMainConcreteRoleDescriptor(
			ConcreteRoleDescriptor _mainConcreteRoleDescriptor) {
		this.mainConcreteRoleDescriptor = _mainConcreteRoleDescriptor;
	}

	/**
	 * Returns the date where the ConcreteTaskDescriptor really finished
	 * 
	 * @return the value of the real finish date
	 */
	public Date getRealFinishingDate() {
		return realFinishingDate;
	}

	/**
	 * Initializes the Date of the real end of the ConcreteTaskDescriptor.
	 * 
	 * @param realFinishingDate
	 *            the real finish Date to set
	 */
	public void setRealFinishingDate(Date realFinishingDate) {
		this.realFinishingDate = realFinishingDate;
	}

	/**
	 * Returns the Date where the ConcreteTaskDescriptor really started
	 * 
	 * @return the value of the real stating Date
	 */
	public Date getRealStartingDate() {
		return realStartingDate;
	}

	/**
	 * Initializes the Date of the real start of the ConcreteTaskDescriptor.
	 * 
	 * @param realStartingDate
	 *            the Date to set
	 */
	public void setRealStartingDate(Date realStartingDate) {
		this.realStartingDate = realStartingDate;
	}

	/**
	 * Returns the time that remains for a ConcreteTaskDescriptor
	 * 
	 * @return the value of the remaining time
	 */
	public float getRemainingTime() {
		return remainingTime;
	}

	/**
	 * Initializes the remaining time of the ConcreteTaskDescriptor.
	 * 
	 * @param remainingTime
	 *            the remaining time to set
	 */
	public void setRemainingTime(float remainingTime) {
		this.remainingTime = remainingTime;
	}

	/**
	 * Returns the remaining time for each day of a ConcreteTaskDescriptor
	 * 
	 * @return a Set of the daily remaining time
	 */
	public Set<DailyRemainingTime> getDailyRemainingTimes() {
		return dailyRemainingTimes;
	}

	/**
	 * Initializes the daily remaining time of the ConcreteTaskDescriptor.
	 * 
	 * @param _dailyRemainingTimes
	 *            the daily remaining time to set
	 */
	public void setDailyRemainingTimes(
			Set<DailyRemainingTime> _dailyRemainingTimes) {
		dailyRemainingTimes = _dailyRemainingTimes;
	}

	/**
	 * Returns a Collection of the output ConcreteWorkProductDescriptor assigned
	 * to the ConcreteTaskDescriptor.
	 * 
	 * @return the outputConcreteWorkProductDescriptors
	 */
	public Set<ConcreteWorkProductDescriptor> getOutputConcreteWorkProductDescriptors() {
		return this.outputConcreteWorkProductDescriptors;
	}

	/**
	 * Initializes the attribute outputConcreteWorkProductDescriptors of
	 * ConcreteTaskDescriptor with a Collection of the
	 * ConcreteWorkProductDescriptor.
	 * 
	 * @param _outputConcreteWorkProductDescriptors
	 *            the outputConcreteWorkProductDescriptors to set
	 */
	public void setOutputConcreteWorkProductDescriptors(
			Set<ConcreteWorkProductDescriptor> _outputConcreteWorkProductDescriptors) {
		this.outputConcreteWorkProductDescriptors = _outputConcreteWorkProductDescriptors;
	}

	/**
	 * Returns a Collection of optional input ConcreteWorkProductDescriptors
	 * assigned to the ConcreteTaskDescriptor.
	 * 
	 * @return the optionalInputConcreteWorkProductDescriptors
	 */
	public Set<ConcreteWorkProductDescriptor> getOptionalInputConcreteWorkProductDescriptors() {
		return this.optionalInputConcreteWorkProductDescriptors;
	}

	/**
	 * Initializes the attribute optionalInputConcreteWorkProductDescriptors of
	 * ConcreteTaskDescriptor with a Collection of the
	 * ConcreteWorkProductDescriptor.
	 * 
	 * @param _optionalInputConcreteWorkProductDescriptors
	 *            the optionalInputConcreteWorkProductDescriptors to set
	 */
	public void setOptionalInputConcreteWorkProductDescriptors(
			Set<ConcreteWorkProductDescriptor> _optionalInputConcreteWorkProductDescriptors) {
		this.optionalInputConcreteWorkProductDescriptors = _optionalInputConcreteWorkProductDescriptors;
	}

	/**
	 * @return the mandatoryInputConcreteWorkProductDescriptors
	 */
	public Set<ConcreteWorkProductDescriptor> getMandatoryInputConcreteWorkProductDescriptors() {
		return this.mandatoryInputConcreteWorkProductDescriptors;
	}

	/**
	 * @param _mandatoryInputConcreteWorkProductDescriptors
	 *            the mandatoryInputConcreteWorkProductDescriptors to set
	 */
	public void setMandatoryInputConcreteWorkProductDescriptors(
			Set<ConcreteWorkProductDescriptor> _mandatoryInputConcreteWorkProductDescriptors) {
		this.mandatoryInputConcreteWorkProductDescriptors = _mandatoryInputConcreteWorkProductDescriptors;
	}

	/**
	 * @param _concreteworkProductDescriptor
	 */
	public void addOutputConcreteWorkProduct(
			ConcreteWorkProductDescriptor _concreteworkProductDescriptor) {
		this.outputConcreteWorkProductDescriptors
				.add(_concreteworkProductDescriptor);
		_concreteworkProductDescriptor.getProducerConcreteTasks().add(this);
	}

	/**
	 * @param _workProductDescriptor
	 */
	public void addOptionalInputConcreteWorkProduct(
			ConcreteWorkProductDescriptor _concreteworkProductDescriptor) {
		this.optionalInputConcreteWorkProductDescriptors
				.add(_concreteworkProductDescriptor);
		_concreteworkProductDescriptor.getOptionalUserConcreteTasks().add(this);
	}

	/**
	 * @param _workProductDescriptor
	 */
	public void addMandatoryInputConcreteWorkProduct(
			ConcreteWorkProductDescriptor _concreteworkProductDescriptor) {
		this.mandatoryInputConcreteWorkProductDescriptors
				.add(_concreteworkProductDescriptor);
		_concreteworkProductDescriptor.getMandatoryUserConcreteTasks()
				.add(this);
	}

	/**
	 * @param _outputWorkProductDescriptors
	 */
	public void addAllOutputConcreteWorkProductDescriptors(
			Set<ConcreteWorkProductDescriptor> _outputConcreteWorkProductDescriptors) {
		for (ConcreteWorkProductDescriptor concreteworkProduct : _outputConcreteWorkProductDescriptors) {
			concreteworkProduct.addProducerConcreteTask(this);
		}

	}

	/**
	 * @param _optionalInputWorkProductDescriptors
	 */
	public void addAllOptionalInputConcreteWorkProductDescriptors(
			Set<ConcreteWorkProductDescriptor> _optionalInputConcreteWorkProductDescriptors) {
		for (ConcreteWorkProductDescriptor concreteworkProduct : _optionalInputConcreteWorkProductDescriptors) {
			concreteworkProduct.addOptionalUserConcreteTask(this);
		}

	}

	/**
	 * @param _mandatoryInputWorkProductDescriptors
	 */
	public void addAllMandatoryInputConcreteWorkProductDescriptors(
			Set<ConcreteWorkProductDescriptor> _mandatoryInputConcreteWorkProductDescriptors) {
		for (ConcreteWorkProductDescriptor concreteworkProduct : _mandatoryInputConcreteWorkProductDescriptors) {
			concreteworkProduct.addMandatoryUserConcreteTask(this);
		}

	}

	/**
	 * @param _concreteworkProductDescriptor
	 */
	public void removeOutputConcreteWorkProduct(
			ConcreteWorkProductDescriptor _concreteworkProductDescriptor) {
		this.outputConcreteWorkProductDescriptors
				.remove(_concreteworkProductDescriptor);
		// _concreteworkProductDescriptor.getProducerConcreteTasks().remove(this);
	}

	/**
	 * @param _workProductDescriptor
	 */
	public void removeOptionalInputConcreteWorkProduct(
			ConcreteWorkProductDescriptor _concreteworkProductDescriptor) {
		this.optionalInputConcreteWorkProductDescriptors
				.remove(_concreteworkProductDescriptor);
		// _concreteworkProductDescriptor.getOptionalUserConcreteTasks().remove(this);
	}

	/**
	 * @param _workProductDescriptor
	 */
	public void removeMandatoryInputConcreteWorkProduct(
			ConcreteWorkProductDescriptor _concreteworkProductDescriptor) {
		this.mandatoryInputConcreteWorkProductDescriptors
				.remove(_concreteworkProductDescriptor);
		// _concreteworkProductDescriptor.getMandatoryUserConcreteTasks().remove(this);
	}

	/**
	 * @return the isActivatedWorkProductStateDependancies
	 */
	public boolean getIsActivatedWorkProductStateDependancies() {
		return this.isActivatedWorkProductStateDependancies;
	}

	/**
	 * @param _isActivatedWorkProductStateDependancies
	 *            the isActivatedWorkProductStateDependancies to set
	 */
	public void setIsActivatedWorkProductStateDependancies(
			boolean _isActivatedWorkProductStateDependancies) {
		this.isActivatedWorkProductStateDependancies = _isActivatedWorkProductStateDependancies;
	}
	
	 
}
