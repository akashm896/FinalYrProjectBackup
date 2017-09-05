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

package wilos.presentation.web.wilosuser;

import javax.faces.context.FacesContext;

import wilos.business.services.misc.wilosuser.LoginService;
import wilos.business.services.misc.wilosuser.RoleService;
import wilos.utils.Security;
import wilos.model.misc.wilosuser.WilosUser;
import wilos.presentation.web.template.ConnectViewBean;
import wilos.presentation.web.tree.TreeBean;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;
import wilos.resources.LocaleBean;


/**
 * This class permit to manage the phase of login
 * Managed-Bean link to participantSubscribe.jspx
 */
public class LoginBean {

    	/** login of the participant */
	private String login;

	/** password of the participant*/
	private String password;

	/** service who concerne the login*/
	private LoginService loginService;
	
	/** servie who concernte the role of an user **/
	private RoleService roleService;
	
	/** 'utilisateur **/
	private WilosUser user;

	
	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * Getter of login
	 * @return the login
	 */
	public String getLogin() {
	    return this.login;
	}

	/**
	 * Setter of login
	 * @param _login the new login
	 */
	public void setLogin(String _login) {
		this.login = _login;
	}

	/**
	 * Getter of login service
	 * @return the login service
	 */
	public LoginService getLoginService() {
		return this.loginService;
	}

	/**
	 * Setter of login service
	 * @param _loginService the new login service
	 */
	public void setLoginService(LoginService _loginService) {
		this.loginService = _loginService;
	}

	/**
	 * Getter of password
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Setter of password
	 * @param _password the new password
	 */
	public void setPassword(String _password) {
		this.password = _password;
	}

	/**
	 * Redirect to the page corresponding to the type of the user
	 */
	public void authentificationAction() {
		String url = "";
		String applicationRole = "";
		try{
		user = this.loginService.getAuthentifiedUser(this.login,
				Security.encode(this.password));

		if (user != null) {
			WebSessionService.setAttribute(
					WebSessionService.WILOS_USER_ID, user.getId());

			String role = this.roleService.getARoleForAnUser(user.getRole_id());
			WebSessionService.setAttribute(
					WebSessionService.ROLE_TYPE, role);
			
			if (role.equalsIgnoreCase("participant")) {
				url = "participant_main";
				applicationRole = "participant_role";
				WebSessionService
				.setAttribute(WebSessionService.USER_GUIDE, "guide.login.participant");
			} else if (role.equalsIgnoreCase("processManager")) {
				url = "process_manager_main";
				applicationRole = "processManager_role";
				WebSessionService
				.setAttribute(WebSessionService.USER_GUIDE, "guide.login.pm");
			} else if (role.equalsIgnoreCase("projectDirector")) {
				url = "project_director_main";
				applicationRole = "projectDirector_role";
				WebSessionService
				.setAttribute(WebSessionService.USER_GUIDE, "guide.login.pd");
			} else if (role.equalsIgnoreCase("admin")) {
				url = "admin_main";
				applicationRole = "admin_role";				
			}
			
			/* Test of the navigation */
			WebCommonService.changeContentPage(url);
			changeConnectView(true, applicationRole);
		}else{
			WebCommonService
			.addErrorMessage(LocaleBean.getText("component.authentificationerror.loginError"));
		}
		}catch(Exception e){
			WebCommonService
					.addErrorMessage(LocaleBean.getText("component.authentificationerror.loginError"));
		}
	}
	
	/**
	 * Method used to display the appropriate screen for the connected role, calling connected method
	 * @param _b connected true or false 
	 * @param _applicationRole type of application role
	 */
	public void changeConnectView(boolean _b, String _applicationRole) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Object connectObject = facesContext.getApplication()
				.createValueBinding("#{connect}").getValue(facesContext);
		if (connectObject != null && connectObject instanceof ConnectViewBean) {
			ConnectViewBean connectBean = (ConnectViewBean) connectObject;
			connectBean.connected(_b, _applicationRole);
		}
	}

	/**
	 * Method designed to describe the action to do when the wilosuser is trying
	 * to disconnect.
	 */
	public void logoutAction() {
	    	//Réinit le guide utlisateur
		WebSessionService
		.setAttribute(WebSessionService.USER_GUIDE, "guide.accueil");
		WebCommonService.changeContentPage("wilos");
		changeConnectView(false, "none");

		WebSessionService.cleanSesssion();

		TreeBean tb = (TreeBean) WebCommonService.getBean("TreeBean");
		tb.cleanTreeDisplay();
	}
	
	/**
	 * Get the user
	 * 
	 * @return the user
	 */
	public WilosUser getUser() {
		return user;
	}

	/**
	 * Set the user
	 * 
	 * @param user
	 *                the new user
	 */
	public void setUser(WilosUser user) {
		this.user = user;
	}
}
