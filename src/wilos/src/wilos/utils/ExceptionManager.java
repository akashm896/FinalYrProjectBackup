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

package wilos.utils ;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.StaleObjectStateException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * @author Sebastien
 * 
 * Cette classe permet de g�rer les exceptions
 * 
 */
public class ExceptionManager {

	private static ExceptionManager instance = null ;
	
	protected final Log logger = LogFactory.getLog(this.getClass()) ;

	/**
	 * Default constructor
	 */
	private ExceptionManager() {
	}

	/**
	 * Get the current instance of the singleton
	 * 
	 * @return Exception manager instance
	 */
	public static ExceptionManager getInstance() {
		if(ExceptionManager.instance == null){
			ExceptionManager.instance = new ExceptionManager() ;
		}
		return ExceptionManager.instance ;
	}
	
	// DAO
	
	/**
	 * 
	 * Manage the DataIntegrityViolationException type
	 *
	 * @param _s Name of the class which raise the exception
	 * @param _e Exception
	 */
	public void manageDataIntegrityViolationException(String _s, String _fonction, DataIntegrityViolationException _e) {
		logger.error("### ExceptionManager -> "+ _s +" raises DataIntegrityViolationException in "+ _fonction +" ###");
		_e.printStackTrace();
	}
	
	/**
	 * 
	 * Manage the DataIntegrityViolationException type
	 *
	 * @param _s Name of the class which raise the exception
	 * @param _e Exception
	 */
	public void manageStaleObjectStateException(String _s, String _fonction, StaleObjectStateException _e) {
		logger.error("### ExceptionManager -> "+ _s +" raises StaleObjectStateException in "+ _fonction +" ###");
		//_e.printStackTrace();
	}
	
	/**
	 * 
	 * Manage the DataIntegrityViolationException type
	 *
	 * @param _s Name of the class which raise the exception
	 * @param _e Exception
	 */
	public void manageConstraintViolationException(String _s, String _fonction, ConstraintViolationException _e) {
		logger.error("### ExceptionManager -> "+ _s +" raises ConstraintViolationException in "+ _fonction +" ###");
		_e.printStackTrace();
	}
	
	/**
	 * 
	 * Manage the DataAccessException type
	 *
	 * @param _s Name of the class which raise the exception
	 * @param _e Exception
	 */
	public void manageDataAccessException(String _s, String _fonction, DataAccessException _e) {
		logger.fatal("### ExceptionManager -> "+ _s +" raises DataAccessException in "+ _fonction +" ###");
		_e.printStackTrace();
	}
}
