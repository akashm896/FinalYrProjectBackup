/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Sebastien BALARD <sbalard@wilos-project.org>
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

package wilos.hibernate.spem2.process;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wilos.model.spem2.process.Process;

/**
 * ProcessDao manage requests from the system to store Processes into the
 * database
 */
public class ProcessDao extends HibernateDaoSupport {

	/**
	 * Saves or updates a Process
	 * 
	 * @param _process
	 *            The Process to be saved or updated
	 */

	public String saveOrUpdateProcess(Process _process) {
		if (_process != null) {
			this.getHibernateTemplate().saveOrUpdate(_process);
			return _process.getId();
		}
		return "";
	}

	/**
	 * Returns a list of all the Processes
	 * 
	 * @return A list of all the Processes
	 */
	public List<Process> getAllProcesses() {
		List<Process> processes = new ArrayList<Process>();
		for (Object obj : this.getHibernateTemplate().loadAll(Process.class)) {
			Process u = (Process) obj;
			processes.add(u);
		}
		return processes;
	}

	/**
	 * Returns the Process which has the specified ID
	 * 
	 * @param _id
	 *            The wanted Process's ID
	 * @return The wanted Process
	 */
	public Process getProcess(String _id) {
		if (!_id.equals(""))
			return (Process) this.getHibernateTemplate()
					.get(Process.class, _id);
		return null;
	}

	/**
	 * Returns a process with the given guid If there are many process with the
	 * same guid, it returns the first
	 * 
	 * @param _guid
	 *            the given guidance id
	 * @return the wanted Process
	 */
	public Process getProcessFromGuid(String _guid) {
		if (!_guid.equals("")) {
			List processes = this.getHibernateTemplate().find(
					"from Process p where p.guid=?", _guid);
			if (processes.size() > 0)
				return (Process) processes.get(0);
		}
		return null;
	}

	/**
	 * Deletes the Process
	 * 
	 * @param _process
	 *            The Process to be deleted
	 */
	public void deleteProcess(Process _process) {
		this.getHibernateTemplate().delete(_process);
	}
}
