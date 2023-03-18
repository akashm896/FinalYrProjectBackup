/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Emilien PERICO <emilien.perico@free.fr>
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

package wilos.presentation.web.template;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import wilos.presentation.web.project.ProjectAdvancementWorkProductBean;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;


public class ActionBean {

	private String manageParticipants = "manageParticipants";

	private String manageProcessManagers = "manageProcessManagers";

	private String manageProjectDirectors = "manageProjectDirectors";

	private String modifyParticipant = "modifyParticipant";

	private String affectProject = "affectProject";

	private String assignments = "Assignments";

	private String affectProjectAsManager = "affectProjectAsManager";

	private String options = "Options";

	private String importProcessFile = "importProcessFile";

	private String manageProcesses = "manageProcesses";

	private String adminMain = "admin_main";

	private String participantMain = "participant_main";

	private String projectDirectorMain = "project_director_main";

	private String processManagerMain = "process_manager_main";

	private String dashboard = "Dashboard";

	private String projectManagement = "ProjectManagement";

	private String createProject = "createProject";

	private String modifyProject = "modifyProject";
	
	private String affectProjectParticipant = "affectProjectAParticipant";

	private String affectProjectManagerParticipant = "AffectProjectAsManager";
	
	private String selectPanelForParticipant = "tabAffectProjectParticipant";
	/**
	 * Getter of importProcessFile.
	 * 
	 * @return the importProcessFile.
	 */
	public String getImportProcessFile() {
		return this.importProcessFile;
	}

	/**
	 * Setter of importProcessFile.
	 * 
	 * @param _importProcessFile
	 *            The importProcessFile to set.
	 */
	public void setImportProcessFile(String _importProcessFile) {
		this.importProcessFile = _importProcessFile;
	}

	/**
	 * Getter of manageParticipant.
	 * 
	 * @return the manageParticipant.
	 */
	public String getManageParticipants() {
		return this.manageParticipants;
	}

	/**
	 * Setter of manageParticipant.
	 * 
	 * @param _manageParticipant
	 *            The manageParticipant to set.
	 */
	public void setManageParticipants(String _manageParticipant) {
		this.manageParticipants = _manageParticipant;
	}

	public void selectNodeActionListener(ActionEvent _evt) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();

		String mainPage = (String) map.get("mainPage");
		String pageToShow = (String) map.get("pageToShow");
		MenuBean menuBean = (MenuBean) context.getExternalContext()
		.getSessionMap().get("menu");
		menuBean.getSelectedPanel().setTemplateName(mainPage);
		menuBean.getSelectedPanel().setTemplateNameForARole(pageToShow);
		ProjectAdvancementWorkProductBean pawp = (ProjectAdvancementWorkProductBean) WebCommonService.getBean("ProjectAdvancementWorkProductBean");
		pawp.refreshProjectTable();

	}
	
	public void selectNodeParticipantActionListener(ActionEvent _evt) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();

		String mainPage = (String) map.get("mainPage");
		String pageToShow = (String) map.get("pageToShow");
		MenuBean menuBean = (MenuBean) context.getExternalContext()
		.getSessionMap().get("menu");
		menuBean.getSelectedPanel().setTemplateName(mainPage);
		menuBean.getSelectedPanel().setTemplateNameForARole(pageToShow);
		ProjectAdvancementWorkProductBean pawp = (ProjectAdvancementWorkProductBean) WebCommonService.getBean("ProjectAdvancementWorkProductBean");
		pawp.refreshProjectTable();
		
		this.selectPanelForParticipant = (String) map.get("panelSelectedParticipant");
	}

	/**
	 * Getter of manageProcessManagers.
	 * 
	 * @return the manageProcessManagers.
	 */
	public String getManageProcessManagers() {
		return this.manageProcessManagers;
	}

	/**
	 * Setter of manageProcessManagers.
	 * 
	 * @param _manageProcessManagers
	 *            The manageProcessManagers to set.
	 */
	public void setManageProcessManagers(String _manageProcessManagers) {
		this.manageProcessManagers = _manageProcessManagers;
	}

	/**
	 * Getter of manageProjectDirectors.
	 * 
	 * @return the manageProjectDirectors.
	 */
	public String getManageProjectDirectors() {
		return this.manageProjectDirectors;
	}

	/**
	 * Setter of manageProjectDirectors.
	 * 
	 * @param _manageProjectDirectors
	 *            The manageProjectDirectors to set.
	 */
	public void setManageProjectDirectors(String _manageProjectDirectors) {
		this.manageProjectDirectors = _manageProjectDirectors;
	}

	/**
	 * Getter of affectProject.
	 * 
	 * @return the affectProject.
	 */
	public String getAffectProject() {
		return this.affectProject;
	}

	/**
	 * Verify if a project as selected
	 * 
	 * @return boolean
	 */
	public boolean getIsSelected() {
		if (!WebSessionService.PROJECT_ID.equals("projectId")) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Getter of Assignments
	 * 
	 * @return String
	 */
	public String getAssignments() {
		return this.assignments;
	}

	/**
	 * Setter of Assignments
	 * 
	 * @param _assignments
	 */
	public void setAssignments(String _assignments) {
		this.assignments = _assignments;
	}

	/**
	 * Setter of affectProject.
	 * 
	 * @param _affectProject
	 *            The affectProject to set.
	 */
	public void setAffectProject(String _affectProject) {
		this.affectProject = _affectProject;
	}

	/**
	 * Getter of affectProjectAsManager.
	 * 
	 * @return the affectProjectAsManager.
	 */
	public String getAffectProjectAsManager() {
		return this.affectProjectAsManager;
	}

	/**
	 * Setter of affectProjectAsManager.
	 * 
	 * @param _affectProjectAsManager
	 *            The affectProjectAsManager to set.
	 */
	public void setAffectProjectAsManager(String _affectProjectAsManager) {
		this.affectProjectAsManager = _affectProjectAsManager;
	}

	/**
	 * Getter of adminMain.
	 * 
	 * @return the adminMain.
	 */
	public String getAdminMain() {
		return this.adminMain;
	}

	/**
	 * Setter of adminMain.
	 * 
	 * @param _adminMain
	 *            The adminMain to set.
	 */
	public void setAdminMain(String _adminMain) {
		this.adminMain = _adminMain;
	}

	/**
	 * Getter of participantMain.
	 * 
	 * @return the participantMain.
	 */
	public String getParticipantMain() {
		return this.participantMain;
	}

	/**
	 * Setter of participantMain.
	 * 
	 * @param _participantMain
	 *            The participantMain to set.
	 */
	public void setParticipantMain(String _participantMain) {
		this.participantMain = _participantMain;
	}

	/**
	 * Getter of processManagerMain.
	 * 
	 * @return the processManagerMain.
	 */
	public String getProcessManagerMain() {
		return this.processManagerMain;
	}

	/**
	 * Setter of processManagerMain.
	 * 
	 * @param _processManagerMain
	 *            The processManagerMain to set.
	 */
	public void setProcessManagerMain(String _processManagerMain) {
		this.processManagerMain = _processManagerMain;
	}

	/**
	 * Getter of projectDirectorMain.
	 * 
	 * @return the projectDirectorMain.
	 */
	public String getProjectDirectorMain() {
		return this.projectDirectorMain;
	}

	/**
	 * Setter of projectDirectorMain.
	 * 
	 * @param _projectDirectorMain
	 *            The projectDirectorMain to set.
	 */
	public void setProjectDirectorMain(String _projectDirectorMain) {
		this.projectDirectorMain = _projectDirectorMain;
	}

	/**
	 * Getter of manageProcesses.
	 * 
	 * @return the manageProcesses.
	 */
	public String getManageProcesses() {	  
		return this.manageProcesses;
	}

	/**
	 * Setter of manageProcesses.
	 * 
	 * @param _manageProcesses
	 *            The manageProcesses to set.
	 */
	public void setManageProcesses(String _manageProcesses) {
		this.manageProcesses = _manageProcesses;
	}

	/**
	 * Getter of options.
	 * 
	 * @return the options.
	 */
	public String getOptions() {
		return this.options;
	}

	/**
	 * Setter of options.
	 * 
	 * @param _options
	 *            The options to set.
	 */
	public void setOptions(String _options) {
		this.options = _options;
	}

	public String getDashboard() {
		return dashboard;
	}

	public void setDashboard(String dashboard) {
		this.dashboard = dashboard;
	}

	public String getProjectManagement() {
		return projectManagement;
	}

	public void setProjectManagement(String projectManagement) {
		this.projectManagement = projectManagement;
	}

	/** Panel modify participant
	 * @return the panel to modify a participant
	 */ 
	public String getModifyParticipant() {
		return modifyParticipant;
	}

	/**
	 * Setter for modify the panel to modify a participant
	 * @param modifyParticipant the new panel
	 */
	public void setModifyParticipant(String modifyParticipant) {
		this.modifyParticipant = modifyParticipant;
	}

	public String getCreateProject(){
		return createProject;
	}
	public void setCreateProject(String cp){
		this.createProject = cp;
	}
	public String getModifyProject(){
		return modifyProject;
	}
	public void setModifyProject(String cp){
		this.modifyProject = cp;
	}

	public String getAffectProjectParticipant() {
	    return affectProjectParticipant;
	}

	public void setAffectProjectParticipant(String affectProjectParticipant) {
	    this.affectProjectParticipant = affectProjectParticipant;
	}

	public String getAffectProjectManagerParticipant() {
	    return affectProjectManagerParticipant;
	}

	public void setAffectProjectManagerParticipant(
		String affectProjectManagerParticipant) {
	    this.affectProjectManagerParticipant = affectProjectManagerParticipant;
	}

	public String getSelectPanelForParticipant() {
	    return selectPanelForParticipant;
	}

	public void setSelectPanelForParticipant(String selectPanelForParticipant) {
	    this.selectPanelForParticipant = selectPanelForParticipant;
	}
}
