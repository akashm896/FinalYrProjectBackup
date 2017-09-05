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

package wilos.model.misc.project;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.wilosuser.Participant;
import wilos.model.misc.wilosuser.WilosUser;
import wilos.model.spem2.process.Process;

/**
 * This class represents a project.
 * 
 */
public class Project extends ConcreteActivity implements Cloneable {

	private String description;

	private Date creationDate;

	private Date launchingDate;

	private Boolean isFinished;

	private Process process;

	private Set<Participant> participants;

	private Participant projectManager;

	private String projectDirector;

	private boolean considerWorkProductAndTaskLinks;


	/**
	 * Class constructor
	 */
	public Project() {
		this.creationDate = new Date();
		this.launchingDate = new Date();
		this.participants = new HashSet<Participant>();
		this.isFinished = false;
		this.considerWorkProductAndTaskLinks = false;
	}

	/**
	 * Returns the Date of the Project creation
	 * 
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Adds a relation between the Project and the Process
	 * 
	 * @param _process
	 *            the process to be linked to
	 */
	public void addProcess(Process _process) {
		this.process = _process;
		_process.getProjects().add(this);
	}

	/**
	 * * Removes the relation between the Project and the Process
	 * 
	 * @param _process
	 *            the process to be unlinked to
	 */
	public void removeProcess(Process _process) {
		this.process = null;
		_process.getProjects().remove(this);
	}

	/**
	 * Sets the Project creation Date
	 * 
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Returns the description for the Project
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description for the Project
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the launching Date for the Project
	 * 
	 * @return the launchingDate
	 */
	public Date getLaunchingDate() {
		return launchingDate;
	}

	/**
	 * Sets the launching Date for the Project
	 * 
	 * @param launchingDate
	 *            the launchingDate to set
	 */
	public void setLaunchingDate(Date launchingDate) {
		this.launchingDate = launchingDate;
	}

	/**
	 * Returns a copy of the current instance of Project
	 * 
	 * @return a copy of the Project
	 * @throws CloneNotSupportedException
	 */
	public Project clone() throws CloneNotSupportedException {
		Project project = new Project();
		project.copy(this);
		return project;
	}

	/**
	 * Copy the values of the specified Project into the current instance of the
	 * class.
	 * 
	 * @param _project
	 *            the Project to copy
	 */
	protected void copy(final Project _project) {
		this.description = _project.description;
		this.creationDate = _project.creationDate;
		this.launchingDate = _project.launchingDate;
		this.considerWorkProductAndTaskLinks = _project.considerWorkProductAndTaskLinks;
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of Project
	 */
	public int hashCode() {
		return new HashCodeBuilder(17,37).append(this.getId()).toHashCode();
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the class.
	 * 
	 * @param obj
	 *            the Object to be compare to the Project
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object _obj) {
		if (_obj instanceof Project == false) {
			return false;
		}
		if (this == _obj) {
			return true;
		}
		Project project = (Project) _obj;
		return new EqualsBuilder().appendSuper(super.equals(project)).append(
				this.description, project.description).append(
				this.creationDate, project.creationDate).append(
				this.launchingDate, project.launchingDate).append(
				this.isFinished, project.isFinished).append(this.process,
				project.process)
				.append(this.participants, project.participants).append(
						this.projectManager, project.projectManager).append(
						this.considerWorkProductAndTaskLinks,
						project.considerWorkProductAndTaskLinks).isEquals();

	}

	/**
	 * Getter of process.
	 * 
	 * @return the process.
	 */
	public Process getProcess() {
		return this.process;
	}

	/**
	 * Setter of process.
	 * 
	 * @param _process
	 *            The process to set.
	 */
	public void setProcess(Process _process) {
		this.process = _process;
	}

	/**
	 * Getter of participants.
	 * 
	 * @return the participants.
	 */
	public Set<Participant> getParticipants() {
		return this.participants;
	}

	/**
	 * Setter of participants.
	 * 
	 * @param _participants
	 *            The participants to set.
	 */
	public void setParticipants(Set<Participant> _participants) {
		this.participants = _participants;
	}

	/**
	 * Getter of the project manager.
	 * 
	 * @return the project manager.
	 */
	public Participant getProjectManager() {
		return projectManager;
	}

	/**
	 * Setter of the project manager.
	 * 
	 * @param projectManager
	 *            The project manager to set.
	 */
	public void setProjectManager(Participant projectManager) {
		this.projectManager = projectManager;
	}

	/**
	 * add participant to this project
	 * 
	 * @param participant
	 *            the participant to add
	 */
	public void addParticipant(Participant participant) {
		this.participants.add(participant);
		participant.getAffectedProjectList().add(this);
	}

	/**
	 * remove a project from a participant
	 * 
	 * @param participant
	 *            the participant to remove from
	 */
	public void removeParticipant(Participant participant) {
		this.participants.remove(participant);
	}

	/**
	 * remove the project from all the participants
	 * 
	 */
	public void removeAllParticipants() {
		for (Participant participant : this.participants) {
			participant.removeAffectedProject(this);
		}
		this.participants.clear();
	}

	/**
	 * Getter of isFinished.
	 * 
	 * @return the isFinished.
	 */
	public Boolean getIsFinished() {
		return this.isFinished;
	}

	/**
	 * Setter of isFinished.
	 * 
	 * @param _isFinished
	 *            The isFinished to set.
	 */
	public void setIsFinished(Boolean _isFinished) {
		this.isFinished = _isFinished;
	}

	/**
	 * Adds a relation between the current instance of Project and a specified
	 * Participant (project manager).
	 * 
	 * @param projectManager
	 */
	public void addProjectManager(Participant projectManager) {
		this.projectManager = projectManager;
		projectManager.getManagedProjects().add(this);
	}

	/**
	 * Removes the relation between the current instance of Project and a
	 * specified Participant (project manager).
	 * 
	 * @param projectManager
	 */
	public void removeProjectManager(Participant projectManager) {
		this.projectManager = null;
	}

	/**
	 * Getter of projectDirector.
	 * 
	 * @return the projectDirector.
	 */
	public String getProjectDirector() {
		return this.projectDirector;
	}

	/**
	 * Setter of projectDirector.
	 * 
	 * @param _projectDirector
	 *            The projectDirector to set.
	 */
	public void setProjectDirector(String _projectDirector) {
		this.projectDirector = _projectDirector;
	}

	/**
	 * 
	 * Add a project director
	 * 
	 * @param project
	 */
	public void addProjectDirector(String _projectDirector) {
		this.projectDirector = _projectDirector;
	}

	/**
	 * 
	 * Remove a project director
	 * 
	 * @param project
	 */
	public void removeProjectDirector(WilosUser _projectDirector) {
		this.projectDirector = null;
	}

	/**
	 * @return the considerWorkProductAndTaskLinks
	 */
	public boolean getConsiderWorkProductAndTaskLinks() {
		return this.considerWorkProductAndTaskLinks;
	}

	/**
	 * @param _considerWorkProductAndTaskLinks
	 *            the considerWorkProductAndTaskLinks to set
	 */
	public void setConsiderWorkProductAndTaskLinks(
			boolean _considerWorkProductAndTaskLinks) {
		this.considerWorkProductAndTaskLinks = _considerWorkProductAndTaskLinks;
	}

}
