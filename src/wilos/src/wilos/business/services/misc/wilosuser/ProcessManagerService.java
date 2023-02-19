/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Sebastien BALARD <sbalard@wilos-project.org>
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

package wilos.business.services.misc.wilosuser;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.hibernate.misc.wilosuser.WilosUserDao;
import wilos.model.misc.wilosuser.WilosUser;
import wilos.utils.Security;

/**
 * The services associated to the ProcessManager
 * 
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class ProcessManagerService {

	private WilosUserDao wilosUserDao;

	protected final Log logger = LogFactory.getLog(this.getClass());

	/**
	 * Allows to save the processManager
	 * 
	 * @param _processmanager
	 */
	public String saveProcessManager(WilosUser _processManager) {
		_processManager.setPassword(Security.encode(_processManager
				.getPassword()));
		return this.wilosUserDao.saveOrUpdateWilosUser(_processManager);
	}


	/**
	 * Allows to get the list of processManager
	 * 
	 * @return the list of Process Managers
	 */
	public List<WilosUser> getProcessManagers() {
		return this.wilosUserDao.getWilosUserByRole("2");
	}

	/**
	 * Allows to get the processManager with its id
	 * 
	 * @return the list of Process Managers
	 */
	public WilosUser getProcessManager(String _id) {
		return this.wilosUserDao.getWilosUserById(_id);
	}

	/**
	 * Allows to delete a processManager
	 * 
	 * @param processManager
	 * 
	 */
	public void deleteProcessManager(WilosUser processManager) {
		this.wilosUserDao.deleteWilosUser(processManager);
	}

	public WilosUserDao getWilosUserDao() {
		return wilosUserDao;
	}

	public void setWilosUserDao(WilosUserDao wilosUserDao) {
		this.wilosUserDao = wilosUserDao;
	}
}
