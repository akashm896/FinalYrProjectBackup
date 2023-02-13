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

import javax.faces.event.ActionEvent;

import wilos.business.services.misc.project.ProjectService;
import wilos.model.misc.project.Project;
import wilos.resources.LocaleBean;

public class CommonBean {

    /* Fields */

    private ProjectService projectService;

    private boolean isGuideVisible = true;

    private static String GUIDE_VISIBLE = "images/help_f2.gif";

    private static String GUIDE_UNVISIBLE = "images/help.gif";

    private String iconVisibleGuide = GUIDE_VISIBLE;

    /* Public Methods */

    /**
     * Get the text of the guide
     */
    public String getGuide() {
	String urlUserGuide = (String) WebSessionService
		.getAttribute(WebSessionService.USER_GUIDE);
	if (urlUserGuide == null || urlUserGuide.equals("")) {
	    // text by default
	    WebSessionService.setAttribute(WebSessionService.USER_GUIDE,
		    "guide.accueil");
	    urlUserGuide = (String) WebSessionService
		    .getAttribute(WebSessionService.USER_GUIDE);

	}
	return LocaleBean.getText(urlUserGuide);
    }

    /**
     * Return if the user guide is visible by default the user guide is visible
     * 
     * @return true or false
     */
    public boolean getIsGuideVisible() {
	return isGuideVisible;
    }

    /**
     * Set the visibility of the user guide
     * 
     * @param isGuideVisible
     */
    public void setIsGuideVisible(boolean isGuideVisible) {
	this.isGuideVisible = isGuideVisible;
	if (this.isGuideVisible) {
	    this.iconVisibleGuide = GUIDE_VISIBLE;
	} else {
	    this.iconVisibleGuide = GUIDE_UNVISIBLE;
	}
    }

    /**
     * Change the visibilty of guide
     * 
     * @param _event
     */
    public void visibilityOfGuide() {
	this.setIsGuideVisible(!this.isGuideVisible);
    }

    /**
     * Gets a boolean explaining if the project is selected
     * 
     * @return true if the project is selected else false
     */
    public boolean getIsProjectSelected() {
	String projectId = (String) WebSessionService
		.getAttribute(WebSessionService.PROJECT_ID);

	if (projectId == null || projectId.equals("default"))
	    return false;
	else
	    return true;
    }

    /**
     * Gets a boolean in order to know if the project has been instanciated
     * 
     * @return true if the project has been instanciated
     */
    public boolean getIsProjectInstanciated() {
	String projectId = (String) WebSessionService
		.getAttribute(WebSessionService.PROJECT_ID);
	if (projectId == null) {
	    return false;
	}
	Project project = projectService.getProject(projectId);
	if (project != null) {
	    if (project.getProcess() != null) {
		return true;
	    }
	}
	return false;
    }

    /**
     * Tells if a user is a project manager or not for the current project
     * 
     * @return true if the user is a project manager else false
     */
    public boolean getIsProjectManager() {
	String user_id = (String) WebSessionService
		.getAttribute(WebSessionService.WILOS_USER_ID);
	String projectId = (String) WebSessionService
		.getAttribute(WebSessionService.PROJECT_ID);

	if (projectId == null)
	    return false;

	Project project = this.projectService.getProject(projectId);
	if (project == null || project.equals("default"))
	    return false;

	if (project.getProjectManager() != null)
	    if ((project.getProjectManager().getId().equals(user_id)))
		return true;
	    else
		return false;
	else
	    return false;

    }

    public boolean getIsProjectDirector() {

	String user_id = (String) WebSessionService
		.getAttribute(WebSessionService.WILOS_USER_ID);

	String role = (String) WebSessionService
		.getAttribute(WebSessionService.ROLE_TYPE);

	String projectId = (String) WebSessionService
		.getAttribute(WebSessionService.PROJECT_ID);

	if (projectId == null)
	    return false;

	Project project = this.projectService.getProject(projectId);
	if (project == null || project.equals("default"))
	    return false;

	if (role.equals("projectDirector")) {
	    if (project.getProjectDirector() != null) {
		if ((project.getProjectDirector().equals(user_id))) {
		    return true;
		}
	    }
	}
	return false;

    }

    /* Getters & Setters */

    /**
     * Getter of the project service
     * 
     * @return the project service
     */
    public ProjectService getProjectService() {
	return projectService;
    }

    /**
     * Setter of the project service
     * 
     * @param projectService
     *                the nex project service
     */
    public void setProjectService(ProjectService projectService) {
	this.projectService = projectService;
    }

    public String getIconVisibleGuide() {
	return iconVisibleGuide;
    }

    public void setIconVisibleGuide(String iconVisibleGuide) {
	this.iconVisibleGuide = iconVisibleGuide;
    }
}
