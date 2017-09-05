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

package wilos.model.spem2.process;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.misc.project.Project;
import wilos.model.misc.wilosuser.WilosUser;
import wilos.model.spem2.activity.Activity;

/**
 * 
 * A Process is a special {@link Activity} that describes a structure for
 * particular types of development projects. To perform such a development
 * project a Processes would be 'instantiated' and adapted for the specific
 * situation. Process is an abstract class and this meta-model defines different
 * special types of Processes for different process management applications and
 * different situations of process reuse.
 * <p />
 * It's an element of the SPEM2 specification of the OMG organization
 * (http://www.omg.org/).
 * 
 */
public class Process extends Activity implements Cloneable {

	// The Project of Process
	private Set<Project> projects;

	private String processManager;

	private String folderPath;

	/**
	 * Default constructor
	 * 
	 */
	public Process() {
		super();
		this.projects = new HashSet<Project>();
	}

	/**
	 * Returns a copy of the current instance of Process
	 * 
	 * @return a copy of the Process
	 * @throws CloneNotSupportedException
	 */
	@Override
	public Process clone() throws CloneNotSupportedException {
		Process process = new Process();
		process.copy(this);
		return process;
	}

	/**
	 * Copy the values of the specified Process into the current instance of the
	 * class.
	 * 
	 * @param _process
	 *            The Process to copy.
	 */
	protected void copy(final Process _process) {
		super.copy(_process);
		this.processManager = _process.getProcessManager();
		this.projects.addAll(_process.getProjects());
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the Process.
	 * 
	 * @param obj
	 *            the Object to be compare to the Process
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Process == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		Process process = (Process) obj;
		return new EqualsBuilder().appendSuper(super.equals(process)).append(
				this.projects, process.projects).append(this.processManager,
				process.processManager).isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of Process
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(this.processManager).toHashCode();
	}

	/**
	 * Removes a Project from the Process
	 * 
	 * @param _project
	 */
	public void removeProject(Project _project) {
		_project.setProcess(null);
		this.projects.remove(_project);
	}

	/**
	 * Adds a Project to the Process
	 * 
	 * @param _project
	 */
	public void addProject(Project _project) {
		this.projects.add(_project);
		_project.setProcess(this);
	}

	/**
	 * Removes all the Project related to the Process
	 */
	public void removeAllProjects() {
		for (Project project : this.projects) {
			project.setProcess(null);
		}
		this.projects.clear();
	}

	/**
	 * Add a Project Set to the Project Collection of a Process
	 * 
	 * @param _project
	 *            the Set of Project to add to the Process
	 */
	public void addAllProjects(Set<Project> _project) {
		for (Project _proj1 : _project) {
			_proj1.addProcess(this);
		}
	}

	/**
	 * Returns the Collection of Project related to the Process
	 * 
	 * @return a Set of Project
	 */
	public Set<Project> getProjects() {
		return projects;
	}

	/**
	 * Sets the Collection of Project related to the Process
	 * 
	 * @param project
	 *            the Set of Project to be set
	 */
	public void setProjects(Set<Project> project) {
		this.projects = project;
	}

	/*
	 * relation between Process et Project Manager
	 */

	/**
	 * Adds a relation between the specified ProcessManager and the Process
	 * 
	 * @param _processManager
	 *            the ProcessManager to be linked to
	 */
	public void addProcessManager(WilosUser _processManager) {
		this.processManager = _processManager.getId();
		Set<Process> p = _processManager.getProcessesManaged();
		if( p == null){
			p = new HashSet<Process>();
		}
			p.add(this);
	}

	/**
	 * Removes the relation between the specified ProcessManager and the Process
	 * 
	 * @param _processManager
	 *            the ProcessManager to be unlinked to
	 */
	public void removeProcessManager(WilosUser _processManager) {
		_processManager.getProcessesManaged().remove(this);
		_processManager.removeManagedProcess(this);
		this.processManager = null;

	}

	
	/**
	 * Returns the folder path to the Process
	 * 
	 * @return the String of the folder path
	 */
	public String getFolderPath() {
		return folderPath;
	}

	/**
	 * Sets the folder path of the Process
	 * 
	 * @param folderPath
	 *            the folder path
	 */
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public void setProcessManager(String processManager) {
		this.processManager = processManager;
	}

	public String getProcessManager() {
		return processManager;
	}

}
