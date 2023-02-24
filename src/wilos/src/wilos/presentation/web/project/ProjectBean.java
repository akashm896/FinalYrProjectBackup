/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Emilien PERICO <eperico@wilos-project.org>
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
package wilos.presentation.web.project;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wilos.business.services.misc.concreteactivity.ConcreteActivityService;
import wilos.business.services.misc.concreterole.ConcreteRoleDescriptorService;
import wilos.business.services.misc.concretetask.ConcreteTaskDescriptorService;
import wilos.business.services.misc.project.ProjectService;
import wilos.business.services.misc.wilosuser.ParticipantService;
import wilos.business.services.misc.wilosuser.WilosUserService;
import wilos.business.services.spem2.process.ProcessService;
import wilos.business.services.spem2.role.RoleDescriptorService;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.project.Project;
import wilos.model.misc.wilosuser.Participant;
import wilos.model.misc.wilosuser.WilosUser;
import wilos.model.spem2.process.Process;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.presentation.web.tree.TreeBean;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;
import wilos.resources.LocaleBean;

/**
 * Managed-Bean link to project_create.jspx
 * 
 */
public class ProjectBean {

	private ProjectService projectService;

	private ProcessService processService;

	private ParticipantService participantService;

	private RoleDescriptorService roleDescriptorService;

	private ConcreteTaskDescriptorService concreteTaskDescriptorService;

	private ConcreteRoleDescriptorService concreteRoleDescriptorService;

	private ConcreteActivityService concreteActivityService;

	private Project project;

	private TreeBean treeBean;

	private String selectedProcessGuid;

	private ArrayList<SelectItem> processNamesList;

	private List<HashMap<String, Object>> projectList;

	private List<Project> projectListWithoutProcess = new ArrayList<Project>();

	private List<Project> projectListWithProcess = new ArrayList<Project>();

	public static final String MODIFY_ICON = "images/modify.gif";

	private static final String PROCESS_NULL = "projectProcesses_null";

	private static final String PROCESS_NOT_NULL = "projectProcesses_not_null";

	private boolean visiblePopup = false;

	protected final Log logger = LogFactory.getLog(this.getClass());

	@SuppressWarnings("unused")
	private SimpleDateFormat formatter;

	private String selectProcessAffectation;

	private String processName;

	private String projectListView;

	private String processesListView;

	private String projectId = "-1";

	private boolean displayCreateButton;

	private boolean newProject;

	private String selectedConcreteActivityForNewRoleId = "default";

	private String selectedTaskDescriptorId = "default";

	private boolean addRoleRendered = false;

	private String newRoleName;

	private String newRoleDescription;

	private boolean visibleTaskComboBox = false;

	private boolean isVisibleNewTaskPanel = false;

	private WilosUserService wilosUserService;

	private String name;

	private String description;

	private Date dateLancement = new Date();
	
	private String creationDate;
	
	private String owner;
	
	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public WilosUserService getWilosUserService() {
		return wilosUserService;
	}

	public void setWilosUserService(WilosUserService wilosUserService) {
		this.wilosUserService = wilosUserService;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateLancement() {
		return dateLancement;
	}

	public void setDateLancement(Date dateLancement) {
		this.dateLancement = dateLancement;
	}

	/**
	 * Constructor.
	 * 
	 */
	public ProjectBean() {
		this.project = new Project();
		this.selectedProcessGuid = "";
		this.processNamesList = new ArrayList<SelectItem>();
		this.formatter = new SimpleDateFormat("dd/MM/yyyy");
		this.displayCreateButton = true;
	}

	/* Manage the popup. */

	/**
	 * This method allow to print the right message when an user want to delete
	 * the selected project
	 * 
	 * @param event
	 */
	public void confirmDelete(ActionEvent event) {
		if (this.projectService.deleteProject(this.projectId))
			WebCommonService.addInfoMessage(LocaleBean
					.getText("component.projectcreate.deleteSuccess"));
		else
			WebCommonService.addErrorMessage(LocaleBean
					.getText("component.projectcreate.deleteError"));

		this.visiblePopup = false;
		displayCreateButton = true;
	}

	/**
	 * This method fixed the visiblePopup boolean attribute to false
	 * 
	 * @param event
	 */
	public void cancelDelete(ActionEvent event) {
		this.visiblePopup = false;
	}

	/**
	 * 
	 * Editing a project
	 * 
	 * @param e
	 *                event received when a user clicks on edit button in the
	 *                datatable
	 */
	public void editProject(ActionEvent e) {

		String projectId = (String) FacesContext.getCurrentInstance()
		.getExternalContext().getRequestParameterMap().get(
		"projectEditId");

		// project director of the selected project
		Project selectedProject = this.projectService.getProject(projectId);
		String projectDirectorId = selectedProject.getProjectDirector();

		if (isActionAllowedForThePD(projectDirectorId)) {
			for (HashMap<String, Object> projectDescription : projectList) {
				if (((String) projectDescription.get("id")).equals(projectId)) {
					projectDescription.put("isEditable", new Boolean(true));
				}
			}
		}
	}

	/**
	 * Private method to check permissions (delete and edit) for a given project
	 * director
	 * 
	 * @param projectDirectorId
	 * @return boolean true if the project director is allowed to manage the
	 *         selected project
	 */
	private boolean isActionAllowedForThePD(String projectDirectorId) {
		// user from session
		String userId = (String) WebSessionService
		.getAttribute(WebSessionService.WILOS_USER_ID);

		if (userId.equals(projectDirectorId)) {
			return true;
		} else {
			WebCommonService.addErrorMessage(LocaleBean
					.getText("component.projectcreate.notallowed"));
			return false;
		}
	}

	/**
	 * 
	 * Saving new project
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void saveProject() { 	
		this.project = new Project();
		String user_id = (String) WebSessionService
		.getAttribute(WebSessionService.WILOS_USER_ID);
		String projectName = this.getName();
		if (projectName.trim().length() == 0) {
			WebCommonService
			.addErrorMessage(LocaleBean
					.getText("component.projectList.message.invalidName"));
		} else if (this
				.projectNameAlreadyExists(projectName, projectId)) {
			WebCommonService
			.addErrorMessage(LocaleBean
					.getText("component.projectList.message.nameAlreadyExists"));
		}else if(this.getDateLancement() == null){
			WebCommonService
			.addErrorMessage(LocaleBean
					.getText("component.projectList.message.invalidlaunchingdate"));
		}
		else {
			project.setConcreteName(projectName);
			project.setLaunchingDate(this.getDateLancement());
			Date d = new Date();
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String launchingDate = formatter.format(this.getDateLancement());
			String date = formatter.format(d);
			if (date.compareTo(launchingDate) > 0) {
				WebCommonService
				.addErrorMessage(LocaleBean
						.getText("component.projectList.message.invalidlaunchingdate"));
			}else{
				project.setDescription(this.getDescription());
				project.setProjectDirector(user_id);
				projectService.getProjectDao().saveOrUpdateProject(project);
				displayCreateButton = true;
				WebCommonService.addInfoMessage(LocaleBean
						.getText("component.projectcreate.success"));
				this.newProject = false;
				this.name = "";
				this.description = "";
				this.dateLancement = null;
				//this.projectId = null;
				WebCommonService.changeContentPage("tabProjectListPage");
			}
		}
	}


	public void modifyAProject() { 
		String id = this.projectId;
		this.project = this.projectService.getProject(id);
		String user_id = (String) WebSessionService
		.getAttribute(WebSessionService.WILOS_USER_ID);
		String projectName = this.getName();
		if (projectName.trim().length() == 0) {
			WebCommonService
			.addErrorMessage(LocaleBean
					.getText("component.projectList.message.invalidName"));
		} else if (this
				.projectNameAlreadyExists(projectName, id)) {
			WebCommonService
			.addErrorMessage(LocaleBean
					.getText("component.projectList.message.nameAlreadyExists"));
		}else if(this.getDateLancement() == null){
			WebCommonService
			.addErrorMessage(LocaleBean
					.getText("component.projectList.message.invalidlaunchingdate"));
		}
		else {
			this.project.setConcreteName(projectName);
			this.project.setLaunchingDate(this.getDateLancement());
			Date d = new Date();
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String launchingDate = formatter.format(this.getDateLancement());
			String date = formatter.format(this.getDateLancement());
			if (date.compareTo(launchingDate) > 0) {
				WebCommonService
				.addErrorMessage(LocaleBean
						.getText("component.projectList.message.invalidlaunchingdate"));
			}else{
				this.project.setDescription(this.getDescription());
				this.project.setProjectDirector((user_id));
				this.projectService.getProjectDao().saveOrUpdateProject(this.project);
				displayCreateButton = true;
				this.name = "";
				this.description = "";
				this.dateLancement = null;
				this.projectId = id;
				this.newProject = false;
				WebCommonService.addInfoMessage(LocaleBean
						.getText("component.projectcreate.modificationSuccess"));
				WebCommonService.changeContentPage("tabProjectListPage");
			}
		}
	}


	/**
	 * this method allow to search if the given projectName 
	 * for the given Project is already existing in the database
	 * 
	 * @param _presentationName
	 * @param _processId
	 * @return boolean
	 */

	private boolean projectNameAlreadyExists(String _projectName,
			String _projectId) {
		for (Project project : projectService.getAllProjects()) {
			if (project.getConcreteName().equalsIgnoreCase(_projectName)
					&& !_projectId.equalsIgnoreCase(project.getId())) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Modify selected user
	 */
	public void modifyProjects() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String id = (String) map.get("idProject");
		/** Search the participant on the list */
		this.project = projectService.getProject(id);
		this.name = this.project.getConcreteName();
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		this.creationDate = formatter.format(this.project.getCreationDate());
		project.setProjectDirector(project.getProjectDirector());
		WilosUser u = this.wilosUserService.getSimpleUser(project.getProjectDirector());
		this.owner = u.getFirstname()
		+ " " + u.getName();
		this.description = this.project.getDescription();
		this.dateLancement = this.project.getLaunchingDate();
		this.projectId = id;
	}
	
	public void cancelSubscriptProject(){
		displayCreateButton = true;
		this.name = "";
		this.description = "";
		this.dateLancement = null;
		this.projectId = "";
		this.newProject = false;
		WebCommonService.changeContentPage("tabProjectListPage");
		WebCommonService.addInfoMessage(LocaleBean
			.getText("component.projectList.inscriptionProjectCancel"));
}


	// ================================================ Getters & Setters
	// ===================================================

	/**
	 * Getter of project.
	 * 
	 * @return the project.
	 */
	public Project getProject() {
		return this.project;
	}

	/**
	 * Setter of project.
	 * 
	 * @param _project
	 *                The project to set.
	 */
	public void setProject(Project _project) {
		this.project = _project;
	}

	/**
	 * Getter of projectService.
	 * 
	 * @return the projectService.
	 */
	public ProjectService getProjectService() {
		return this.projectService;
	}

	/**
	 * Setter of projectService.
	 * 
	 * @param _projectService
	 *                The projectService to set.
	 */
	public void setProjectService(ProjectService _projectService) {
		this.projectService = _projectService;
	}

	/**
	 * Return all the Projects
	 * 
	 * @return A set of Project
	 */
	public List<Project> getAllProjects() {
		return this.projectService.getAllProjects();
	}

	/**
	 * this method return all project without a Process 
	 * 
	 * @return a List of Project
	 */
	public List<Project> getProjectListWithoutProcess() {
		this.projectListWithoutProcess = new ArrayList<Project>();
		this.projectListWithoutProcess.addAll(this.projectService
				.getAllProjectsWithNoProcess());
		return projectListWithoutProcess;
	}

	/**
	 * setter of projectListWithoutProcess List<Project> attribute
	 * 
	 * @param projectListWithoutProcess
	 */
	public void setProjectListWithoutProcess(
			List<Project> projectListWithoutProcess) {
		this.projectListWithoutProcess = projectListWithoutProcess;
	}

	/**
	 * this method return all the project with a process 
	 * instantiation 
	 * 
	 * @return a List<Project> of Project
	 */
	public List<Project> getProjectListWithProcess() {
		this.projectListWithProcess = new ArrayList<Project>();
		this.projectListWithProcess.addAll(this.projectService
				.getAllProjectsWithProcess());
		return projectListWithProcess;
	}

	/**
	 * setter of setProjectListWithProcess List<Project> attribute
	 * 
	 * @param projectListWithProcess
	 */
	public void setProjectListWithProcess(List<Project> projectListWithProcess) {
		this.projectListWithProcess = projectListWithProcess;
	}

	/**
	 * Getter of processNamesList.
	 * 
	 * @return the processNamesList.
	 */
	public ArrayList<SelectItem> getProcessNamesList() {
		ArrayList<SelectItem> tmpListNames = new ArrayList<SelectItem>();
		ArrayList<wilos.model.spem2.process.Process> tmpListProcess = (ArrayList<wilos.model.spem2.process.Process>) this.processService
		.getAllProcesses();

		for (int i = 0; i < tmpListProcess.size(); i++) {
			tmpListNames.add(new SelectItem(tmpListProcess.get(i).getGuid(),
					tmpListProcess.get(i).getPresentationName()));
		}
		processNamesList = tmpListNames;
		return this.processNamesList;
	}

	/**
	 * Setter of processNamesList.
	 * 
	 * @param _processNamesList
	 *                The processNamesList to set.
	 */
	public void setProcessNamesList(ArrayList<SelectItem> _processNamesList) {
		this.processNamesList = _processNamesList;
	}

	/**
	 * Getter of processService.
	 * 
	 * @return the processService.
	 */
	public ProcessService getProcessService() {
		return this.processService;
	}

	/**
	 * Setter of processService.
	 * 
	 * @param _processService
	 *                The processService to set.
	 */
	public void setProcessService(ProcessService _processService) {
		this.processService = _processService;
	}

	/**
	 * Getter of selectedProcessGuid.
	 * 
	 * @return the selectedProcessGuid.
	 */
	public String getSelectedProcessGuid() {
		// Getting the current projet id from cession
		String tmpProjId = (String) WebSessionService
		.getAttribute(WebSessionService.PROJECT_ID);
		if (tmpProjId != null) {
			Project projTmp = projectService.getProject(tmpProjId);
			if (projTmp != null) {
				Process procTmp = projTmp.getProcess();
				if (procTmp != null) {
					this.selectedProcessGuid = projTmp.getProcess().getGuid();
				}
			}
		}
		return this.selectedProcessGuid;
	}

	/**
	 * Setter of selectedProcessGuid.
	 * 
	 * @param _selectedProcessGuid
	 *                The selectedProcessGuid to set.
	 */
	public void setSelectedProcessGuid(String _selectedProcessGuid) {
		this.selectedProcessGuid = _selectedProcessGuid;
	}

	/**
	 * this method allow to return a String.
	 * this string indicate if the selected project 
	 * has an instanciation process
	 * 
	 * @return the selectProcessAffectation
	 */
	public String getSelectProcessAffectation() {
		String tmpProjId = (String) WebSessionService
		.getAttribute(WebSessionService.PROJECT_ID);
		Project currentProject = this.projectService.getProject(tmpProjId);

		String participantId = (String) WebSessionService
		.getAttribute(WebSessionService.WILOS_USER_ID);
		Participant participant = this.participantService
		.getParticipant(participantId);
		if (currentProject.getProcess() == null) {
			if (currentProject.getProjectManager() != null) {
				if (currentProject.getProjectManager().getId().equals(
						participant.getId())) {
					this.selectProcessAffectation = "process_affectation_view";
				} else {
					this.selectProcessAffectation = "no_process_affectation_view";
				}
			} else {
				this.selectProcessAffectation = "no_process_affectation_view";
			}
		} else {
			this.setProcessName(currentProject.getProcess()
					.getPresentationName());
			this.selectProcessAffectation = "selected_process_view";
		}
		return this.selectProcessAffectation;
	}

	/**
	 * delete a participant selected
	 * 
	 */
	public void deleteProject(ActionEvent event) {

		String selectedProjectId = (String) FacesContext.getCurrentInstance()
		.getExternalContext().getRequestParameterMap().get("projectId");

		// project director of the selected project
		Project selectedProject = this.projectService
		.getProject(selectedProjectId);
		String projectDirectorId = selectedProject.getProjectDirector();

		if (isActionAllowedForThePD(projectDirectorId)) {
			this.projectId = selectedProjectId;
			this.visiblePopup = true;
		}
	}

	/**
	 * setter of selectProcessAffectation String attribute
	 * 
	 * @param selectProcessAffectation
	 *                the selectProcessAffectation to set
	 */
	public void setSelectProcessAffectation(String selectProcessAffectation) {
		this.selectProcessAffectation = selectProcessAffectation;
	}

	/**
	 * getter of processName String attribute
	 * 
	 * @return the processName
	 */
	public String getProcessName() {
		return processName;
	}

	/**
	 * setter of processName String attribute
	 * 
	 * @param processName
	 *                the processName to set
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}

	/**
	 * Setter of projectListView.
	 * 
	 * @param _projectListView
	 *                The projectListView to set.
	 */
	public void setSelectAffectedProjectView(String _projectListView) {
		this.projectListView = _projectListView;
	}

	/**
	 * this method allow to return the current instance of
	 * ParticipantService
	 * 
	 * @return the participantService
	 */
	public ParticipantService getParticipantService() {
		return this.participantService;
	}

	/**
	 * Setter of participantService.
	 * 
	 * @param _participantService
	 *                The participantService to set.
	 */
	public void setParticipantService(ParticipantService _participantService) {
		this.participantService = _participantService;
	}

	/**
	 * Getter of projectListView
	 * 
	 * @return the projectListView.
	 */
	public String getProjectListView() {
		if (this.getProjectList().size() == 0) {
			this.projectListView = "projectsListPanelGroup_null";
		} else {
			this.projectListView = "projectsListPanelGroup_not_null";
		}
		return this.projectListView;
	}

	/**
	 * Setter of projectListView.
	 * 
	 * @param _projectListView
	 *                The projectListView to set.
	 */
	public void setProjectListView(String _projectListView) {
		this.projectListView = _projectListView;
	}

	/**
	 * Getter of processesListView.
	 * 
	 * @return the processesListView.
	 */
	public String getProcessesListView() {
		if (this.getProcessNamesList().size() == 0) {
			this.processesListView = PROCESS_NULL;
		} else {
			this.processesListView = PROCESS_NOT_NULL;
		}
		return this.processesListView;
	}

	/**
	 * Setter of processesListView.
	 * 
	 * @param _processesListView
	 *                The processesListView to set.
	 */
	public void setProcessesListView(String _processesListView) {
		this.processesListView = _processesListView;
	}


	/**
	 * getter of visiblePopup boolean attribute
	 * 
	 * @return the visiblePopup
	 */
	public boolean getVisiblePopup() {
		return this.visiblePopup;
	}

	/**
	 * setter of visiblePopup boolean attribute
	 * 
	 * @param visiblePopup
	 *                the visiblePopup to set
	 */
	public void setVisiblePopup(boolean _visiblePopup) {
		this.visiblePopup = _visiblePopup;
	}

	/**
	 * this method return all the Project store in database
	 * 
	 * @return an HashMap List of project
	 */
	public List<HashMap<String, Object>> getProjectList() {
		this.project = null;
		List<Project> p =projectService.getAllSortedProjects();
		projectList = new ArrayList<HashMap<String, Object>>();

		for (Project project : p) {
			HashMap<String, Object> projectDescription = new HashMap<String, Object>();
			projectDescription.put("id", project.getId());
			projectDescription
			.put("projectName", project.getConcreteName());
			projectDescription.put("creationDate", project
					.getCreationDate());
			projectDescription.put("launchingDate", project
					.getLaunchingDate());
			projectDescription.put("description", project.getDescription());
			projectDescription.put("isEditable", new Boolean(false));

			if (project.getProjectDirector() != null) {
				project.setProjectDirector(project.getProjectDirector());
				WilosUser f =this.wilosUserService.getSimpleUser(project.getProjectDirector());
				projectDescription.put("owner", f.getFirstname()
						+ " " + f.getName());
			}
			String user_id = (String) WebSessionService
			.getAttribute(WebSessionService.WILOS_USER_ID);
			// Hides the red crosses to not be able to delete a project which 
			// is not owned by the logged pdir
			if (user_id.equals(project.getProjectDirector())) {
				projectDescription.put("isDeletable", new Boolean(true));
			} else {
				projectDescription.put("isDeletable", new Boolean(false));
			}
			
			projectList.add(projectDescription);
		}
		return projectList;
	}

	/**
	 * setter of projectList List<HashMap<String, Object>> attribute
	 * 
	 * @param _projectList
	 */
	public void setProjectList(List<HashMap<String, Object>> _projectList) {
		this.projectList = _projectList;
	}

	/**
	 * getter of displayCreateButton boolean attribute
	 * 
	 * @return boolean
	 */
	public boolean isDisplayCreateButton() {
		return displayCreateButton;
	}

	/**
	 * setter of displayCreateButton boolean attribute
	 * 
	 * @param _displayCreateButton
	 */
	public void setDisplayCreateButton(boolean _displayCreateButton) {
		this.displayCreateButton = _displayCreateButton;
	}

	/**
	 * getter of newProject boolean attribute
	 * 
	 * @return boolean
	 */
	public boolean isNewProject() {
		return newProject;
	}

	/**
	 * setter of newProject boolean attribute
	 * 
	 * @param _newProject
	 */
	public void setNewProject(boolean _newProject) {
		this.newProject = _newProject;
	}

	/**
	 * this method allow to return the current instance of
	 * RoleDescriptorService
	 * 
	 * @return RoleDescriptorService
	 */
	public RoleDescriptorService getRoleDescriptorService() {
		return roleDescriptorService;
	}

	/**
	 * this method allow to set the current instance of
	 * RoleDescriptorService
	 * 
	 * @param _roleDescriptorService
	 */
	public void setRoleDescriptorService(
			RoleDescriptorService _roleDescriptorService) {
		roleDescriptorService = _roleDescriptorService;
	}

	/**
	 * this method allow to return the current instance of
	 * ConcreteTaskDescriptorService
	 * 
	 * @return ConcreteTaskDescriptorService
	 */
	public ConcreteTaskDescriptorService getConcreteTaskDescriptorService() {
		return concreteTaskDescriptorService;
	}

	/**
	 * this method allow to set the current instance of
	 * ConcreteTaskDescriptorService
	 * 
	 * @param _concreteTaskDescriptorService
	 */
	public void setConcreteTaskDescriptorService(
			ConcreteTaskDescriptorService _concreteTaskDescriptorService) {
		concreteTaskDescriptorService = _concreteTaskDescriptorService;
	}

	/**
	 * this method allow to return the current instance of
	 * ConcreteRoleDescriptorService
	 * 
	 * @return ConcreteRoleDescriptorService
	 */
	public ConcreteRoleDescriptorService getConcreteRoleDescriptorService() {
		return concreteRoleDescriptorService;
	}

	/**
	 * this method allow to set the current instance of
	 * ConcreteRoleDescriptorService
	 * 
	 * @param concreteRoleDescriptorService
	 */
	public void setConcreteRoleDescriptorService(
			ConcreteRoleDescriptorService concreteRoleDescriptorService) {
		this.concreteRoleDescriptorService = concreteRoleDescriptorService;
	}

	/**
	 * this method allow to return the current instance of
	 * ConcreteActivityService
	 * 
	 * @return ConcreteActivityService
	 */
	public ConcreteActivityService getConcreteActivityService() {
		return concreteActivityService;
	}

	/**
	 * this method allow to set the current instance of
	 * ConcreteActivityService
	 * 
	 * @param concreteActivityService
	 */
	public void setConcreteActivityService(
			ConcreteActivityService concreteActivityService) {
		this.concreteActivityService = concreteActivityService;
	}

	/**
	 * getter of treeBean Treebean attribute
	 * 
	 * @return the treeBean
	 */
	public TreeBean getTreeBean() {
		return this.treeBean;
	}

	/**
	 * Setter of treeBean.
	 * 
	 * @param _treeBean
	 *                The treeBean to set.
	 */
	public void setTreeBean(TreeBean _treeBean) {
		this.treeBean = _treeBean;
	}

	/**
	 * ChangeListener on the Combobox including the concrete activities
	 * 
	 * @param e
	 */
	public void changeConcreteActivitiesListenerForNewRole(ValueChangeEvent evt) {
		this.selectedConcreteActivityForNewRoleId = (String) evt.getNewValue();

		this.addRoleRendered = false;

		this.visibleTaskComboBox = !(this.selectedConcreteActivityForNewRoleId
				.equals("default"));

	}

	/**
	 * Give all the tasks save in the database for the given process
	 * 
	 * @return List<SelectItem> of task
	 */
	public List<SelectItem> getTasks() {

		List<SelectItem> tasksList = new ArrayList<SelectItem>();

		SelectItem itemNoTask = new SelectItem("no_task", LocaleBean
				.getText("component.project.rolesinstanciation.noTasks"));

		SelectItem itemDefault = new SelectItem(
				"default",
				LocaleBean
				.getText("component.project.rolesinstanciation.concreteTasksComboBox"));

		tasksList.add(itemDefault);
		tasksList.add(itemNoTask);

		Project project = this.projectService
		.getProject((String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID));

		ConcreteActivity cact = this.concreteActivityService
		.getConcreteActivity(this.selectedConcreteActivityForNewRoleId);

		if (project != null) {
			Process process = project.getProcess();
			if (process != null) {

				if (!this.selectedConcreteActivityForNewRoleId
						.equals("default")) {
					SortedSet<ConcreteBreakdownElement> concreteBDE = this.concreteActivityService
					.getConcreteBreakdownElements(cact);
					for (ConcreteBreakdownElement cbde : concreteBDE) {
						if (cbde instanceof ConcreteTaskDescriptor) {
							ConcreteTaskDescriptor ctd = (ConcreteTaskDescriptor) cbde;
							if (ctd.getTaskDescriptor().getMainRole() == null) {
								tasksList.add(new SelectItem(ctd.getId(), ctd
										.getConcreteName()));
							}

						}
					}

				}
			}
		}

		return tasksList;
	}

	/**
	 * this method allow to keep the selected task in the 
	 * selectedTaskDescriptorId attribute
	 * 
	 * @param evt
	 */
	public void changeTasksListener(ValueChangeEvent evt) {
		this.selectedTaskDescriptorId = (String) evt.getNewValue();

		if (!this.selectedConcreteActivityForNewRoleId.equals("default")) {
			this.addRoleRendered = true;
		} else {
			this.addRoleRendered = false;
		}

	}

	/**
	 * Getter of selectedTaskDescriptorId.
	 * 
	 * @return the selectedTaskDescriptorId.
	 */
	public String getSelectedTaskDescriptorId() {
		return selectedTaskDescriptorId;
	}

	/**
	 * Setter of selectedTaskDescriptorId.
	 * 
	 * @param _selectedTaskDescriptorId
	 *                The selectedTaskDescriptorId to set.
	 */
	public void setSelectedTaskDescriptorId(String _selectedTaskDescriptorId) {
		this.selectedTaskDescriptorId = _selectedTaskDescriptorId;
	}

	/**
	 * getter of newRoleName String attribute
	 * 
	 * @return String
	 */
	public String getNewRoleName() {
		return this.newRoleName;
	}

	/**
	 * setter of newRoleName String attribute
	 * 
	 * @param _newRoleName
	 */
	public void setNewRoleName(String _newRoleName) {
		this.newRoleName = _newRoleName;
	}

	/**
	 * getter of newRoleDescription String attribute
	 * 
	 * @return String
	 */
	public String getNewRoleDescription() {
		return newRoleDescription;
	}

	/**
	 * setter of newRoleDescription String attribute
	 * 
	 * @param _newRoleDescription
	 */
	public void setNewRoleDescription(String _newRoleDescription) {
		this.newRoleDescription = _newRoleDescription;
	}

	/**
	 * getter of selectedConcreteActivityForNewRoleId String attribute
	 * 
	 * @return String
	 */
	public String getSelectedConcreteActivityForNewRoleId() {
		return selectedConcreteActivityForNewRoleId;
	}

	/**
	 * setter of selectedConcreteActivityForNewRoleId String attribute
	 * 
	 * @param _selectedConcreteActivityForNewRoleId
	 */
	public void setSelectedConcreteActivityForNewRoleId(
			String _selectedConcreteActivityForNewRoleId) {
		this.selectedConcreteActivityForNewRoleId = _selectedConcreteActivityForNewRoleId;
	}

	/**
	 * Give all the activities saved in the database for the given process
	 * 
	 * @return List<SelectItem> of activities
	 */
	public List<SelectItem> getConcreteActivitiesForNewRole() {

		List<SelectItem> activityList = new ArrayList<SelectItem>();

		activityList
		.add(new SelectItem(
				"default",
				LocaleBean
				.getText("component.project.rolesinstanciation.concreteActivityComboBox")));

		Project project = this.projectService
		.getProject((String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID));

		if (project != null) {
			for (ConcreteActivity cact : this.concreteActivityService
					.getConcreteActivitiesFromProject(project)) {
				activityList.add(new SelectItem(cact.getId(), cact
						.getConcreteName()));
			}
		}
		return activityList;
	}

	/**
	 * Method to add an out of process role
	 * 
	 * @param evt
	 */
	public String createNewRoleActionListener() {

		if(this.newRoleName==""){
			WebCommonService
			.addErrorMessage(LocaleBean
					.getText("component.project.rolesinstanciation.roleNameMissing"));
			return "";
		}
		
		Project project = this.projectService
		.getProject((String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID));


		if(this.selectedConcreteActivityForNewRoleId.equals("default")){
			WebCommonService
			.addErrorMessage(LocaleBean
					.getText("component.project.rolesinstanciation.roleActivityMissing"));
		}else{
			if(this.selectedTaskDescriptorId.equals("default")){
				WebCommonService
				.addErrorMessage(LocaleBean
						.getText("component.project.rolesinstanciation.taskActivityMissing"));
			}else{
				ConcreteTaskDescriptor ctd;
				TaskDescriptor td = new TaskDescriptor();
					if (this.selectedTaskDescriptorId.equals("no_task")) {
					td = null;
				} else {
					ctd = this.concreteTaskDescriptorService
					.getConcreteTaskDescriptor(this.selectedTaskDescriptorId);
						td = ctd.getTaskDescriptor();
				}
				ConcreteActivity cact = this.concreteActivityService
				.getConcreteActivity(this.selectedConcreteActivityForNewRoleId);
					if (this.projectService.createRole(this.newRoleName,
						this.newRoleDescription, project, td, cact)) {
					WebCommonService
					.addInfoMessage(LocaleBean
							.getText("component.project.rolesinstanciation.roleCreated"));
				} else {
					WebCommonService
					.addInfoMessage(LocaleBean
							.getText("component.project.rolesinstanciation.roleNotCreated"));
				}

				TreeBean treeBean = (TreeBean) WebCommonService.getBean("TreeBean");
				treeBean.rebuildProjectTree();
				this.newRoleDescription = "";
				this.newRoleName = "";
				this.selectedConcreteActivityForNewRoleId = "default";
				this.selectedTaskDescriptorId = "default";
				this.addRoleRendered = false;
				this.visibleTaskComboBox = false;
			}
		} 
		return "";
	}

	/**
	 * getter of addRoleRendered boolean attribute
	 * 
	 * @return boolean
	 */
	public boolean getAddRoleRendered() {
		return this.addRoleRendered;
	}

	/**
	 * setter of addRoleRendered boolean attribute
	 * 
	 * @param _addRoleRendered
	 */
	public void setAddRoleRendered(boolean _addRoleRendered) {
		this.addRoleRendered = _addRoleRendered;
	}

	/**
	 * getter of visibleTaskComboBox boolean attribute
	 * 
	 * @return the visibleTaskComboBox
	 */
	public boolean isVisibleTaskComboBox() {
		return visibleTaskComboBox;
	}

	/**
	 * setter of visibleTaskComboBox boolean attribute
	 * 
	 * @param _visibleTaskComboBox
	 *                the visibleTaskComboBox to set
	 */
	public void setVisibleTaskComboBox(boolean _visibleTaskComboBox) {
		visibleTaskComboBox = _visibleTaskComboBox;
	}

	/**
	 * getter of isVisibleNewTaskPanel boolean attribute
	 * 
	 * @return isVisibleNewTaskPanel
	 */

	public boolean getIsVisibleNewTaskPanel() {
		Project project = this.projectService
		.getProject((String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID));
		Process process = this.projectService.getProcessFromProject(project);
		if (process != null) {
			this.isVisibleNewTaskPanel = true;
		} else {
			this.isVisibleNewTaskPanel = false;
		}
		return this.isVisibleNewTaskPanel;
	}

	/**
	 * setter of isVisibleNewTaskPanel boolean attribute
	 * 
	 * @param _isVisibleNewTaskPanel
	 *                the isVisibleNewTaskPanel to set
	 */
	public void setIsVisibleNewTaskPanel(boolean _isVisibleNewTask) {
		this.isVisibleNewTaskPanel = _isVisibleNewTask;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

}
