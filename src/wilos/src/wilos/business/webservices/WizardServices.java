/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2007 Nicolas CASTEL <ncastel@wilos-project.org>
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

package wilos.business.webservices;

import javax.jws.WebService;

@WebService
public interface WizardServices {

	/**
	 * WebServices to recover all the informations related to a participant
	 * 
	 * @param login
	 * @param password
	 * @return information about participant
	 * @throws Exception
	 */
	public abstract String getParticipant(String _login, String _password)
			throws Exception;

	/**
	 * WebService to download a file attached on a guidance
	 * 
	 * @param login
	 * @param password
	 * @param idGuidance
	 * @param file
	 * @return the content of the file
	 * @throws Exception
	 */
	public abstract byte[] getGuidanceAttachment(String _login, String _password,
			String _idGuidance, String _file) throws Exception;

	/**
	 * WebService starting a task with given id
	 * 
	 * @param login
	 * @param password
	 * @param taskId
	 * @throws Exception
	 */
	public abstract void startConcreteTaskDescriptor(String _login,
			String _password, String _id) throws Exception;

	/**
	 * WebService suspending a task with given id
	 * 
	 * @param login
	 * @param password
	 * @param taskId
	 * @throws Exception
	 */
	public abstract void suspendConcreteTaskDescriptor(String _login,
			String _password, String _taskId) throws Exception;

	/**
	 * WebService stopping a task with given id
	 * 
	 * @param login
	 * @param password
	 * @param taskId
	 * @throws Exception
	 */
	public abstract void stopConcreteTaskDescriptor(String _login,
			String _password, String _taskId) throws Exception;

	/**
	 * WebService to set the time accomplished of a task with given id
	 * 
	 * @param login
	 * @param password
	 * @param taskId
	 * @param newTime
	 * @throws Exception
	 */
	public abstract void setAccomplishedTimeByTask(String _login,
			String _password, String _taskId, float _newTime) throws Exception;

	/**
	 * WebService to set the time remaining of a task with given id
	 * 
	 * @param login
	 * @param password
	 * @param taskId
	 * @param newTime
	 * @throws Exception
	 */
	public abstract void setRemainingTimeByTask(String _login, String _password,
			String _taskId, float _newTime) throws Exception;
}