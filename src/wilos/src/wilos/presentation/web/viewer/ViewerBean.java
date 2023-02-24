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

package wilos.presentation.web.viewer;

import wilos.business.services.misc.concretebreakdownelement.ConcreteBreakdownElementService;
import wilos.business.services.misc.project.ProjectService;
import wilos.model.misc.project.Project;
import wilos.presentation.web.project.ProjectAdvancementBean;
import wilos.presentation.web.project.ProjectAdvancementWorkProductBean;
import wilos.presentation.web.tree.TreeBean;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;

public class ViewerBean {

    private ProjectService projectService;

    private ConcreteBreakdownElementService concreteBreakdownElementService;

    private boolean nameIsEditable = false;

    /**
     * Method which refresh the project tree
     */
    protected void refreshProjectTree() {
	TreeBean treeBean = (TreeBean) WebCommonService.getBean("TreeBean");
	treeBean.refreshProjectTree();
    }

    /**
     * Method which rebuild the project tree
     */
    protected void rebuildProjectTree() {
	TreeBean treeBean = (TreeBean) WebCommonService.getBean("TreeBean");
	treeBean.rebuildProjectTree();
    }

    /**
     * Method which refresh the project table
     */
    protected void refreshProjectTable() {
	ProjectAdvancementBean pab = (ProjectAdvancementBean) WebCommonService
		.getBean("ProjectAdvancementBean");
	pab.refreshProjectTable();
	ProjectAdvancementWorkProductBean pawp = (ProjectAdvancementWorkProductBean) WebCommonService
		.getBean("ProjectAdvancementWorkProductBean");
	pawp.refreshProjectTable();
    }

    /**
     * Method which return the state of the inputName
     * True if this one can only be read, else false
     * @return
     */
    public boolean getIsInputNameReadOnly() {
	return (this.getChangeButtonIsDisabled());
    }

    /**
     * Method which set to true the attribute nameIsEditable
     */
    public void editName() {
	this.nameIsEditable = true;
    }

    /**
     * Method which return the state of the change button
     * true is this one is disable, else false
     * @return boolean
     */
    public boolean getChangeButtonIsDisabled() {
	String wilosUserId = (String) WebSessionService
		.getAttribute(WebSessionService.WILOS_USER_ID);
	String projectId = (String) WebSessionService
		.getAttribute(WebSessionService.PROJECT_ID);

	if (projectId == null)
	    return true;

	Project project = this.projectService.getProject(projectId);

	if ((project != null)
		&& ((project.getProjectManager() != null) && (project
			.getProjectManager().getId().equals(wilosUserId))))
	    return false;
	else
	    return true;
    }

    // To have the mask available only for the ProjectManager.
    // And Only for the TasksTreeMode (not for RolesTreeMode).
    /**
     * Method which return true if the Mask of the tree is available, else false
     * This mask is available if the current user is projectDirector or ProjectMAnager
     * @return boolean
     */
    public boolean getTreeMaskIsAvailable() {
	// Get the treemode.
	String treeMode = (String) WebSessionService
		.getAttribute(WebSessionService.TREE_MODE);

	if (treeMode.equals(TreeBean.TASKS_MODE)) {

	    // participant into session
	    String wilosUserId = (String) WebSessionService
		    .getAttribute(WebSessionService.WILOS_USER_ID);

	    String role = (String) WebSessionService
		    .getAttribute(WebSessionService.ROLE_TYPE);

	    Project project = this.projectService
		    .getProject((String) WebSessionService
			    .getAttribute(WebSessionService.PROJECT_ID));

	    //project manager
	    if ((project.getProjectManager() != null)
		    && (project.getProjectManager().getId().equals(wilosUserId))) {
		return true;
	    }

	    /*//project director
	    if (role.equals("projectDirector")) {
	    return true;
	    }*/
	}
	return false;
    }

    /**
     * Method which return true if the Mask of the tree is available, else false
     * This mask is available if the current user is projectDirector or ProjectMAnager
     * @return boolean
     */
    public boolean getIsTreeModeTask() {
	// Get the treemode.
	String treeMode = (String) WebSessionService
		.getAttribute(WebSessionService.TREE_MODE);

	if (treeMode.equals(TreeBean.TASKS_MODE))
	    return true;
	else
	    return false;
    }

    /* Getters & Setters */

    /**
     * @return the concreteBreakdownElementService
     */
    public ConcreteBreakdownElementService getConcreteBreakdownElementService() {
	return concreteBreakdownElementService;
    }

    /**
     * @param concreteBreakdownElementService
     *                the concreteBreakdownElementService to set
     */
    public void setConcreteBreakdownElementService(
	    ConcreteBreakdownElementService concreteBreakdownElementService) {
	this.concreteBreakdownElementService = concreteBreakdownElementService;
    }

    /**
     * @return the projectService
     */
    public ProjectService getProjectService() {
	return projectService;
    }

    /**
     * @param projectService
     *                the projectService to set
     */
    public void setProjectService(ProjectService projectService) {
	this.projectService = projectService;
    }

    /**
     * @return the nameIsEditable
     */
    public boolean getNameIsEditable() {
	return this.nameIsEditable;
    }

    /**
     * @param nameIsEditable
     *                the nameIsEditable to set
     */
    public void setNameIsEditable(boolean _nameIsEditable) {
	this.nameIsEditable = _nameIsEditable;
    }
}
