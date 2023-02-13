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

package wilos.presentation.web.utils;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Allow to manage the web session and its attributes.
 * 
 */
public class WebSessionService {

	/**
	 * The session attribute WilosUser Id.
	 */
	public static final String WILOS_USER_ID = "wilosUserId";

	/**
	 * The session attribute Project Id.
	 */
	public static final String PROJECT_ID = "projectId";

	/**
	 * The session attribute Role Type.
	 */
	public static final String ROLE_TYPE = "roleType";

	/**
	 * The session attribute Tree Mode.
	 */
	public static final String TREE_MODE = "treeMode";

	/**
	 * The session attribute Concrete Break Down Element.
	 */
	public static final String CONCRETE_BREAK_DOWN_ELEMENT = "concreteBreakDownElement";
	
	/**
	 * The session attribute for the user guide
	 */
	public static final String USER_GUIDE = "";
	
	/**
	 * Gets the value of the attribute
	 * @param _attributeName the name of the attribute to get
	 * @return the value of the attribute
	 */
	public static Object getAttribute(String _attributeName) {
		return getHttpSession().getAttribute(_attributeName);
	}

	/**
	 * Sets an attribute and its value in the session
	 * @param _attributeName the name of the attribute
	 * @param _value the value of the attribute
	 */
	public static void setAttribute(String _attributeName, Object _value) {
		getHttpSession().setAttribute(_attributeName, _value);
	}

	/**
	 * Removes all the attributes in the current session
	 * 
	 */
	public static void cleanSesssion() {
		getHttpSession().removeAttribute(WILOS_USER_ID);
		getHttpSession().removeAttribute(PROJECT_ID);
		getHttpSession().removeAttribute(ROLE_TYPE);
		getHttpSession().removeAttribute(TREE_MODE);
		getHttpSession().removeAttribute(CONCRETE_BREAK_DOWN_ELEMENT);
		getHttpSession().removeAttribute(USER_GUIDE);
	}

	/**
	 * Returns the current session
	 * @return the http session
	 */
	private static HttpSession getHttpSession() {
		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		return httpServletRequest.getSession();
	}
}
