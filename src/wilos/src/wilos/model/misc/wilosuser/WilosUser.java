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

package wilos.model.misc.wilosuser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.misc.project.Project;
import wilos.model.spem2.process.Process;

/**
 * This class represents a user of Wilos.
 * 
 */
public class WilosUser implements Cloneable, Serializable {

	private String id;

	private String name;

	private String firstname;

	private String emailAddress;

	private String login;

	private String password;
	
	private String newPassword;
	
	private String  role_id = "0";
	
	private String role_name;
	
	private String deleted = "True";
	
	private String keyPassword = "";
	
	private Set<Project> projectMonitored;
	
	private Set<Process> processesManaged;
	
	public Set<Project> getProjectMonitored() {
		return projectMonitored;
	}

	public void setProjectMonitored(Set<Project> projectMonitored) {
		this.projectMonitored = projectMonitored;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	/**
	 * Default constructor.
	 * 
	 */
	public WilosUser() {
		// None.
	}

	/**
	 * Constructor.
	 * 
	 * @param _name the user name
	 * @param _fName the user first name
	 * @param _email the user email address
	 * @param _login the user login
	 * @param _password the user password
	 */
	public WilosUser(String _name, String _fName, String _email, String _login,
			String _password) {
		this.name = "";
		this.firstname = "";
		this.emailAddress = "";
		this.login = "";
		this.password = "";
	}

	/**
	 * create random password and encrypt it in the participant password
	 * 
	 * @return : new password not encrypted
	 */
	public String generateNewPassword() {
		String notCryptedPassword = "";
		String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		Random rand = new Random();
		for (int i = 0; i < 8; i++) {
			// add one alphabet element to the string (here 8 elements)
			notCryptedPassword += alphabet.charAt(rand.nextInt(alphabet
					.length()));
		}
		return notCryptedPassword;
	}

	/**
	 * Getter of emailAddress.
	 * 
	 * @return the emailAddress.
	 */
	public String getEmailAddress() {
		return this.emailAddress;
	}

	/**
	 * Setter of emailAddress.
	 * 
	 * @param _emailAddress
	 *            The emailAddress to set.
	 */
	public void setEmailAddress(String _emailAddress) {
		this.emailAddress = _emailAddress;
	}

	/**
	 * Getter of firstname.
	 * 
	 * @return the firstname.
	 */
	public String getFirstname() {
		return this.firstname;
	}

	/**
	 * Setter of firstname.
	 * 
	 * @param _firstname
	 *            The firstname to set.
	 */
	public void setFirstname(String _firstname) {
		this.firstname = _firstname;
	}

	/**
	 * Getter of login.
	 * 
	 * @return the login.
	 */
	public String getLogin() {
		return this.login;
	}

	/**
	 * Setter of login.
	 * 
	 * @param _login
	 *            The login to set.
	 */
	public void setLogin(String _login) {
		this.login = _login;
	}

	/**
	 * Getter of name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter of name.
	 * 
	 * @param _name
	 *            The name to set.
	 */
	public void setName(String _name) {
		this.name = _name;
	}

	/**
	 * Getter of password.
	 * 
	 * @return the password.
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Setter of password.
	 * 
	 * @param _password
	 *            The password to set.
	 */
	public void setPassword(String _password) {
		this.password = _password;
	}

	/**
	 * Copy the object.
	 * 
	 * @param _element
	 *            The element to copy.
	 */
	protected void copy(final WilosUser _wilosUser) {
		this.name = _wilosUser.name;
		this.firstname = _wilosUser.firstname;
		this.emailAddress = _wilosUser.emailAddress;
		this.login = _wilosUser.login;
		this.password = _wilosUser.password;
		this.role_id = _wilosUser.role_id;
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the class.
	 * 
	 * @param _obj
	 *            the Object to be compare to the WilosUser
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof WilosUser == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		WilosUser wilosUser = (WilosUser) obj;
		return new EqualsBuilder().append(this.name, wilosUser.name).append(
				this.firstname, wilosUser.firstname).append(this.emailAddress,
				wilosUser.emailAddress).append(this.login, wilosUser.login)
				.append(this.password, wilosUser.password).isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of WilosUser
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.name).append(
				this.firstname).append(this.emailAddress).append(this.login)
				.append(this.password).toHashCode();
	}

	/**
	 * Getter of the WilosUser identifier.
	 * 
	 * @return the WilosUser identifier.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Setter of the WilosUser identifier.
	 * 
	 * @param the WilosUser identifier to set
	 */
	public void setId(String _id) {
		this.id = _id;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getKeyPassword() {
	    return keyPassword;
	}

	public void setKeyPassword(String keyPassword) {
	    this.keyPassword = keyPassword;
	}
	

	/**
	 * Getter of processManaged.
	 * @return the processManaged.
	 */
	public Set<Process> getProcessesManaged() {
		if(this.processesManaged == null){
			this.processesManaged = new HashSet<Process>();
		}
		return this.processesManaged ;
	}

	/**
	 * Setter of processManaged.
	 * @param _processManaged The processManaged to set.
	 */
	public void setProcessesManaged(Set<Process> _processManaged) {
		this.processesManaged = _processManaged ;
	}
	
	/**
	 * 
	 * Add a managed process to the process set
	 *
	 * @param project
	 */
	public void addManagedProcess(Process _process) {
		this.processesManaged.add(_process) ;
		_process.setProcessManager(this.getId()) ;
		//this.processesManaged.add(_process) ;
	}

	/**
	 * 
	 * Remove a managed process
	 *
	 * @param project
	 */
	public void removeManagedProcess(Process _process) {
		_process.setProcessManager(null) ;
		this.processesManaged.remove(_process) ;		
	}

	/**
	 * 
	 * Remove all managed processes
	 *
	 */
	public void removeAllManagedProcess() {
		ArrayList<Process> list = new ArrayList<Process>();
		for(Process process : this.processesManaged){
			list.add(process);
		}
		for(Process p : list)
			p.removeProcessManager(this) ;
	}

}
