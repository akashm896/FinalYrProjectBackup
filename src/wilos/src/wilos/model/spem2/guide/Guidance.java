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

package wilos.model.spem2.guide;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.element.Element;
import wilos.model.spem2.role.RoleDefinition;
import wilos.model.spem2.task.TaskDefinition;
import wilos.model.spem2.workproduct.WorkProductDefinition;

/**
 * 
 * Guidance is an abstract generalization of additional information related to
 * content elements such as Roles, Tasks, and Work Products. Examples for
 * Guidance are Guidelines, Templates, Checklists, Tool Mentors, Estimates,
 * Supporting Materials, Reports, Concepts, etc.
 * <p />
 * It's an element of the SPEM2 specification of the OMG organization
 * (http://www.omg.org/).
 * 
 */
public class Guidance extends Element {

	private Set<TaskDefinition> taskDefinitions;

	private Set<RoleDefinition> roleDefinitions;

	private Set<WorkProductDefinition> workProductDefinitions;

	private Set<Activity> activities;

	private String type = "";

	private String presentationName = "";

	private String attachment = "";

	/* Type guide constant */
	public static final String guideline = "Guideline";
	public static final String checklist = "Checklist";
	public static final String concept = "Concept";
	public static final String estimationConsiderations = "EstimationConsiderations";
	public static final String example = "Example";
	public static final String practice = "Practice";
	public static final String report = "Report";
	public static final String reusableAsset = "ReusableAsset";
	public static final String roadMap = "RoadMap";
	public static final String supportingMaterial = "SupportingMaterial";
	public static final String template = "Template";
	public static final String termDefinition = "TermDefinition";
	public static final String toolMentor = "ToolMentor";
	public static final String whitepaper = "Whitepaper";

	/**
	 * Class constructor
	 */
	public Guidance() {
		this.taskDefinitions = new HashSet<TaskDefinition>();
		this.roleDefinitions = new HashSet<RoleDefinition>();
		this.workProductDefinitions = new HashSet<WorkProductDefinition>();
		this.activities = new HashSet<Activity>();
	}

	/**
	 * Adds a relation between the instance of Guidance and the TaskDefinition
	 * in parameter
	 * 
	 * @param _taskdefinition
	 *            the TaskDefinition to be linked to
	 */
	public void addTaskDefinition(TaskDefinition _taskdefinition) {
		this.getTaskDefinitions().add(_taskdefinition);
		_taskdefinition.getGuidances().add(this);
	}

	/**
	 * Adds a relation between the instance of Guidance and the RoleDefinition
	 * in parameter
	 * 
	 * @param _roleDefinition
	 *            the RoleDefinition to be linked to
	 */
	public void addRoleDefinition(RoleDefinition _roleDefinition) {
		this.getRoleDefinitions().add(_roleDefinition);
		_roleDefinition.getGuidances().add(this);
	}

	/**
	 * Adds a relation between the instance of Guidance and the Activity in
	 * parameter
	 * 
	 * @param _activity
	 *            the Activity to be linked to
	 */
	public void addActivity(Activity _activity) {
		this.getActivities().add(_activity);
		_activity.getGuidances().add(this);
	}

	/**
	 * Removes the relation between the instance of Guidance and the
	 * TaskDefinition in parameter
	 * 
	 * @param _taskdefinition
	 *            the TaskDefinition to be unlinked to
	 */
	public void removeTaskDefinition(TaskDefinition _taskdefinition) {
		_taskdefinition.getGuidances().remove(this);
		this.getTaskDefinitions().remove(_taskdefinition);
	}

	/**
	 * Removes the relation between the instance of Guidance and the
	 * RoleDefinition in parameter
	 * 
	 * @param _roleDefinition
	 *            the RoleDefinition to be unlinked to
	 */
	public void removeRoleDefinition(RoleDefinition _roleDefinition) {
		_roleDefinition.getGuidances().remove(this);
		this.getRoleDefinitions().remove(_roleDefinition);
	}

	/**
	 * Removes the relation between the instance of Guidance and the Activity in
	 * parameter
	 * 
	 * @param _activity
	 *            the Activity to be unlinked to
	 */
	public void removeActivity(Activity _activity) {
		_activity.getGuidances().remove(this);
		this.getActivities().remove(_activity);
	}

	/**
	 * Returns a copy of the current instance of Guidance
	 * 
	 * @return a copy of the Guidance
	 * @throws CloneNotSupportedException
	 */
	@Override
	public Guidance clone() throws CloneNotSupportedException {
		Guidance guidance = new Guidance();
		guidance.copy(this);
		return guidance;
	}

	/**
	 * Copy the values of the specified Guidance into the current instance of the
	 * class.
	 * 
	 * @param _guidance
	 *            The Guideline to copy.
	 */
	protected void copy(final Guidance _guidance) {
		super.copy(_guidance);
		this.setType(_guidance.getType());
		this.setAttachment(_guidance.getAttachment());
		this.getTaskDefinitions().addAll(_guidance.getTaskDefinitions());
		this.getActivities().addAll(_guidance.getActivities());
		this.getRoleDefinitions().addAll(_guidance.getRoleDefinitions());
		this.getWorkProductDefinitions().addAll(
				_guidance.getWorkProductDefinitions());
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the Guidance.
	 * 
	 * @param obj
	 *            the Object to be compare to the Guidance
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Guidance == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}

		Guidance guidance = (Guidance) obj;
		return new EqualsBuilder().appendSuper(super.equals(guidance)).append(
				this.getTaskDefinitions(), guidance.getTaskDefinitions())
				.append(this.getRoleDefinitions(),
						guidance.getRoleDefinitions()).append(
						this.getActivities(), guidance.getActivities()).append(
						this.getWorkProductDefinitions(),
						guidance.getWorkProductDefinitions()).append(this.type,
						guidance.type).append(this.presentationName,
						guidance.getPresentationName()).append(this.attachment,
						guidance.getAttachment()).isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of Guidance
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(this.type).append(this.presentationName).append(
						this.attachment).toHashCode();
	}

	/**
	 * Assigns the Guidance to all the TaskDefinition of the Set in parameter
	 * 
	 * @param _taskDefinitions the TaskDefinition to be linked to
	 */
	public void addAllTaskDefinitions(Set<TaskDefinition> _taskDefinitions) {
		for (TaskDefinition td : _taskDefinitions) {
			td.addGuidance(this);
		}
	}

	/**
	 * Removes the Guidance from all the TaskDefinition it is related to
	 */
	public void removeAllTaskDefinitions() {
		for (TaskDefinition td : this.getTaskDefinitions())
			td.removeGuidance(this);
		this.getTaskDefinitions().clear();
	}

	/**
	 * Assigns the Guidance to all the RoleDefinition of the Set in parameter
	 * 
	 * @param _roleDefinitions the RoleDefinition to be linked to
	 */
	public void addAllRoleDefinitions(Set<RoleDefinition> _roleDefinitions) {
		for (RoleDefinition rd : _roleDefinitions) {
			rd.addGuidance(this);
		}
	}

	/**
	 * Removes the Guidance from all the RoleDefinition it is related to
	 */
	public void removeAllRoleDefinitions() {
		for (RoleDefinition rd : this.getRoleDefinitions())
			rd.removeGuidance(this);
		this.getRoleDefinitions().clear();
	}

	/**
	 * Assigns the Guidance to all the Activity of the Set in parameter
	 * 
	 * @param _activities the Activity to be linked to
	 */
	public void addAllActivities(Set<Activity> _activities) {
		for (Activity act : _activities) {
			act.addGuidance(this);
		}
	}

	/**
	 * Removes the Guidance from all the Activity it is related to
	 */
	public void removeAllActivities() {
		for (Activity act : this.getActivities())
			act.removeGuidance(this);
		this.getActivities().clear();
	}

	/**
	 * Getter for the Guidance type
	 * 
	 * @return the Guidance type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Setter for the Guidance type
	 * 
	 * @param _type the Guidance type to set
	 */
	public void setType(String _type) {
		this.type = _type;
	}

	/**
	 * Getter for presentationName
	 * 
	 * @return the presentation name
	 */
	public String getPresentationName() {
		return presentationName;
	}

	/**
	 * Setter for presentationName
	 * 
	 * @param the presentation name to set
	 */
	public void setPresentationName(String presentationName) {
		this.presentationName = presentationName;
	}

	/**
	 * Getter for the Activities of the Guidance
	 * 
	 * @return the Guidance's activities
	 */
	public Set<Activity> getActivities() {
		return activities;
	}

	/**
	 * Setter for the Activities of the Guidance
	 * 
	 * @param activities
	 *            the activities to set
	 */
	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}

	/**
	 * Getter for the RoleDefinitions of the Guidance
	 * 
	 * @return the roleDefinitions
	 */
	public Set<RoleDefinition> getRoleDefinitions() {
		return roleDefinitions;
	}

	/**
	 * Setter for the RoleDefinitions of the Guidance
	 * 
	 * @param roleDefinitions
	 *            the roleDefinitions to set
	 */
	public void setRoleDefinitions(Set<RoleDefinition> roleDefinitions) {
		this.roleDefinitions = roleDefinitions;
	}

	/**
	 * Getter for the TaskDefinition of the Guidance
	 * 
	 * @return the taskDefinitions
	 */
	public Set<TaskDefinition> getTaskDefinitions() {
		return taskDefinitions;
	}

	/**
	 * Setter for the TaskDefinition of the Guidance
	 * 
	 * @param taskDefinitions
	 *            the taskDefinitions to set
	 */
	public void setTaskDefinitions(Set<TaskDefinition> taskDefinitions) {
		this.taskDefinitions = taskDefinitions;
	}

	/**
	 * Getter of the associated file to the Guidance
	 * 
	 * @return the associatedFile
	 */
	public String getAttachment() {
		return attachment;
	}

	/**
	 * Setter of the associated file to the Guidance
	 * 
	 * @param _attachment
	 *            the associatedFile to set
	 */
	public void setAttachment(String _attachment) {
		this.attachment = _attachment;
	}

	/**
	 * Getter for the WorkProductDefinitions assigned to  the Guidance
	 * 
	 * @return the workProductDefinitions
	 */
	public Set<WorkProductDefinition> getWorkProductDefinitions() {
		return workProductDefinitions;
	}

	/**
	 * Setter for the WorkProductDefinitions assigned to  the Guidance
	 * 
	 * @param workProductDefinitions
	 *            the workProductDefinitions to set
	 */
	public void setWorkProductDefinitions(
			Set<WorkProductDefinition> workProductDefinitions) {
		this.workProductDefinitions = workProductDefinitions;
	}

	/**
	 * Assigns the Guidance to all the WorkProductDefinitions in parameter
	 * 
	 * @param _workProductDefinitions the WorkProductDefinitions to be related to
	 */
	public void addAllWorkProductDefinitions(
			Set<WorkProductDefinition> _workProductDefinitions) {
		for (WorkProductDefinition wpd : _workProductDefinitions) {
			wpd.addGuidance(this);
		}
	}

	/**
	 * Removes the Guidance from all the related WorkProductDefinitions
	 */
	public void removeAllWorkProductDefinitions() {
		for (WorkProductDefinition wpd : this.getWorkProductDefinitions())
			wpd.removeGuidance(this);
		this.getWorkProductDefinitions().clear();
	}

	/**
	 * Adds a relation between the Guidance and the specified WorkProductDefinition
	 * 
	 * @param _workProductDefinition the WorkProductDefinition to be linked to
	 */
	public void addWorkProductDefinition(
			WorkProductDefinition _workProductDefinition) {
		this.getWorkProductDefinitions().add(_workProductDefinition);
		_workProductDefinition.getGuidances().add(this);
	}

	/**
	 * Removes the relation between the Guidance and the specified WorkProductDefinition
	 * 
	 * @param _workProductDefinition the WorkProductDefinition to be unlinked to
	 */
	public void removeWorkProductDefinition(
			WorkProductDefinition _workProductDefinition) {
		_workProductDefinition.getGuidances().remove(this);
		this.getWorkProductDefinitions().remove(_workProductDefinition);
	}
}
