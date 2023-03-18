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

package wilos.presentation.web.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.icesoft.faces.component.menubar.MenuItem;

import wilos.business.services.misc.project.ProjectService;
import wilos.business.services.misc.wilosuser.LoginService;
import wilos.business.services.misc.wilosuser.ParticipantService;
import wilos.business.services.misc.wilosuser.WilosUserService;
import wilos.business.services.spem2.process.ProcessService;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concreteiteration.ConcreteIteration;
import wilos.model.misc.concretemilestone.ConcreteMilestone;
import wilos.model.misc.concretephase.ConcretePhase;
import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;
import wilos.model.misc.project.Project;
import wilos.model.misc.wilosuser.Participant;
import wilos.model.misc.wilosuser.WilosUser;
import wilos.model.spem2.process.Process;
import wilos.presentation.web.expandabletable.RolesExpTableBean;
import wilos.presentation.web.expandabletable.TasksExpTableBean;
import wilos.presentation.web.expandabletable.WorkProductsExpTableBean;
import wilos.presentation.web.process.ProcessBean;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;
import wilos.presentation.web.viewer.ConcreteActivityViewerBean;
import wilos.presentation.web.viewer.ConcreteIterationViewerBean;
import wilos.presentation.web.viewer.ConcreteMilestoneViewerBean;
import wilos.presentation.web.viewer.ConcretePhaseViewerBean;
import wilos.presentation.web.viewer.ConcreteRoleViewerBean;
import wilos.presentation.web.viewer.ConcreteTaskViewerBean;
import wilos.presentation.web.viewer.ConcreteWorkProductViewerBean;
import wilos.presentation.web.viewer.ProjectViewerBean;
import wilos.resources.LocaleBean;

/**
 * A basic backing bean for a ice:tree component. The only instance variable
 * needed is a DefaultTreeModel Object which is bound to the icefaces tree
 * component in the jspx code. The tree created by this backing bean is used to
 * control the selected panel in a ice:panelStack.
 */
public class TreeBean {

	/* Services */

	private ProjectService projectService;

	private LoginService loginService;

	private ParticipantService participantService;

	private ProcessService processService;

	/** The service for wilos User */
	private WilosUserService wilosUserService;

	/* Simple fields */

	private Project project;

	private static final String DEFAULT_PROJECT_ID = "default";

	private String projectId = DEFAULT_PROJECT_ID;

	private boolean loadTree;

	private boolean hideRadio;
	// true is the selected mode is TASKS_MODE
	private boolean showTasks = true;
	// true is the selected mode is ROLES_MODE
	private boolean showRoles;
	// true is the selected mode is WORKPRODUCTS_MODE
	private boolean showWorkProducts;

	private String selectedMode = TASKS_MODE;

	public static final String TASKS_MODE = "tasksMode";

	public static final String ROLES_MODE = "rolesMode";

	public static final String WORKPRODUCTS_MODE = "workProductsMode";

	// tree default model, used as a value for the tree component
	private DefaultTreeModel model = null;

	protected final Log logger = LogFactory.getLog(this.getClass());

	// HashMap which contains the object and his id
	private HashMap<String, Object> treeMap = new HashMap<String, Object>();

	/**
	 * Comparator used to sort the components of the treeBean
	 */
	class WilosMutableTreeNodeComparator implements
			Comparator<DefaultMutableTreeNode> {

		public int compare(DefaultMutableTreeNode o1, DefaultMutableTreeNode o2) {
			if (((WilosObjectNode) o1.getUserObject()).getDisplayOrder()
					.compareTo(
							((WilosObjectNode) o2.getUserObject())
									.getDisplayOrder()) > 0)
				// o1 preceeds o2
				return 1;
			else
				// o2 preceeds o1
				return -1;
		}

		public WilosMutableTreeNodeComparator() {
		}

	}

	public TreeBean() {
		this.model = new DefaultTreeModel(this.getDefaultTree());
	}

	/**
	 * Gets the default tree which is a node asking for the user to select a
	 * project
	 * 
	 * @return the default tree
	 */
	public DefaultMutableTreeNode getDefaultTree() {
		DefaultMutableTreeNode defaultTree = new DefaultMutableTreeNode();
		WilosObjectNode iceUserObject = new WilosObjectNode(defaultTree);

		iceUserObject.setText(LocaleBean
				.getText("navigation.tree.defaulttreenodetext"));
		defaultTree.setUserObject(iceUserObject);
		return defaultTree;
	}

	/**
	 * Refreshes the tree by calling the method buildTreeModel
	 */
	public void refreshProjectTree() {
		this.buildTreeModel();
	}

	/**
	 * Rebuilds the tree by reloading the project and calling the method
	 * buildTreeModel
	 */
	public void rebuildProjectTree() {
		String prId = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);
		this.project = this.projectService.getProject(prId);
		this.buildTreeModel();
	}

	/**
	 * Cleans the tree, it resets the PROJECT_ID in the Session to default an
	 * calls the method buildTreeModel This method has to be called on
	 * participant log out
	 */
	public void cleanTreeDisplay() {
		WebSessionService.setAttribute(WebSessionService.PROJECT_ID,
				DEFAULT_PROJECT_ID);
		this.projectId = DEFAULT_PROJECT_ID;
		this.buildTreeModel();
	}

	/**
	 * Builds the tree Model depending on the type of tree selected If the tree
	 * type is TASK_MODE the nodes are sorted before being displayed
	 */
	private void buildTreeModel() {
		if (this.projectId != null
				&& !this.projectId.equals(DEFAULT_PROJECT_ID)) {

			ProjectNode projectNode;
			if (this.selectedMode.equals(TASKS_MODE)) {
				projectNode = new ProjectNode(this.project, 1, treeMap);
				WebSessionService.setAttribute(WebSessionService.TREE_MODE,
						TASKS_MODE);
			} else {
				if (this.selectedMode.equals(ROLES_MODE)) {
					projectNode = new ProjectNode(this.project, 2, treeMap);
					WebSessionService.setAttribute(WebSessionService.TREE_MODE,
							ROLES_MODE);
				} else {
					projectNode = new ProjectNode(this.project, 3, treeMap);
					WebSessionService.setAttribute(WebSessionService.TREE_MODE,
							WORKPRODUCTS_MODE);
				}
			}
			this.sortModel(projectNode);
			this.model = new DefaultTreeModel(projectNode);
		} else {
			// Build the default tree.
			this.model = new DefaultTreeModel(this.getDefaultTree());
		}
	}

	/**
	 * Sorts the tree, it uses a WilosMutableTreeNodeComparator
	 * 
	 * @param _node
	 *            the nodes being sorted
	 */
	public void sortModel(DefaultMutableTreeNode _node) {
		TreeSet<DefaultMutableTreeNode> treeSet = new TreeSet<DefaultMutableTreeNode>(
				new WilosMutableTreeNodeComparator());
		Enumeration children = _node.children();
		while (children.hasMoreElements()) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) children
					.nextElement();
			if (child.children().hasMoreElements()) {
				this.sortModel(child);
			}
			treeSet.add(child);
		}
		for (DefaultMutableTreeNode node : treeSet) {
			_node.add(node);
		}
	}

	/**
	 * Action listener on the tree
	 * 
	 * @param evt
	 *            event occurring
	 */
	public void changeTreeActionListener(ValueChangeEvent evt) {
		String nodeTypeToShow = DEFAULT_PROJECT_ID;

		this.projectId = (String) evt.getNewValue();

		// Put into the session the current project used.
		WebSessionService.setAttribute(WebSessionService.PROJECT_ID,
				this.projectId);

		if (!this.projectId.equals(DEFAULT_PROJECT_ID)) {

			// Retrieve the entire project.
			this.project = this.projectService.getProject(this.projectId);

			nodeTypeToShow = WilosObjectNode.PROJECTNODE;

			// masquage de la exptable d'instanciation
			// String projectId = (String)
			// WebSessionService.getAttribute(WebSessionService.PROJECT_ID);
			// Project project = this.projectService.getProject(projectId);

			ProcessBean processBean = (ProcessBean) WebCommonService
					.getBean("ProcessBean");
			processBean
					.setInstanciationDependenciesView("view_instanciation_panelGroup");

			TasksExpTableBean tasksExpTableBean = (TasksExpTableBean) WebCommonService
					.getBean("TasksExpTableBean");

			RolesExpTableBean rolesExpTableBean = (RolesExpTableBean) WebCommonService
					.getBean("RolesExpTableBean");

			WorkProductsExpTableBean workProductsExpTableBean = (WorkProductsExpTableBean) WebCommonService
					.getBean("WorkProductsExpTableBean");

			String user_id = (String) WebSessionService
					.getAttribute(WebSessionService.WILOS_USER_ID);
			String projectId = (String) WebSessionService
					.getAttribute(WebSessionService.PROJECT_ID);

			String role = (String) WebSessionService
					.getAttribute(WebSessionService.ROLE_TYPE);

			boolean userIsProjectManager = false;

			Project project = this.projectService.getProject(this.projectId);

			if (project.getProjectManager() != null) {
				if ((project.getProjectManager().getId().equals(user_id))) {
					// the current user is the project manager of the selected
					// project
					userIsProjectManager = true;
				}
			}

			if (this.project.getProcess() == null) {
				// the project is not instanciated
				processBean.setSelectedProcessId(DEFAULT_PROJECT_ID);
				tasksExpTableBean.setSelectedProcessId(DEFAULT_PROJECT_ID);
				rolesExpTableBean.setSelectedProcessId(DEFAULT_PROJECT_ID);
				workProductsExpTableBean
						.setSelectedProcessId(DEFAULT_PROJECT_ID);
				processBean.setIsVisibleExpTable(false);
				tasksExpTableBean.setIsInstanciedProject(false);
				tasksExpTableBean.getExpTableContent().clear();
				if (role.equals("projectDirector")) {
					WebSessionService.setAttribute(
							WebSessionService.USER_GUIDE,
							"guide.login.pd.selected.project.not.defined");
				} else if (userIsProjectManager) {
					WebSessionService
							.setAttribute(WebSessionService.USER_GUIDE,
									"guide.login.project.manager.selected.project.not.defined");
				} else {
					WebSessionService
							.setAttribute(WebSessionService.USER_GUIDE,
									"guide.login.participant.selected.project.not.defined");
				}

			} else {
				// the project is instanciated
				Process process = this.projectService
						.getProcessFromProject(this.project);
				processBean.setSelectedProcessId(process.getId());
				processBean
						.setInstanciationDependenciesView("view_instanciation_panelGroup");
				tasksExpTableBean.setSelectedProcessId(process.getId());
				rolesExpTableBean.setSelectedProcessId(process.getId());
				workProductsExpTableBean.setSelectedProcessId(process.getId());
				processBean.setIsVisibleExpTable(true);
				tasksExpTableBean.setIsInstanciedProject(true);

				if (role.equals("projectDirector")) {
					WebSessionService.setAttribute(
							WebSessionService.USER_GUIDE,
							"guide.login.pd.selected.project.defined");
				} else if (userIsProjectManager) {
					WebSessionService
							.setAttribute(WebSessionService.USER_GUIDE,
									"guide.login.project.manager.selected.project.defined");
				} else {
					WebSessionService.setAttribute(
							WebSessionService.USER_GUIDE,
							"guide.login.participant.selected.project.defined");
				}
			}
		}

		this.buildTreeModel();

		if (this.projectId.length() > 0)
			this.selectNodeToShow(this.projectId, nodeTypeToShow);

	}

	/**
	 * Action listener on a node of the tree
	 * 
	 * @param evt
	 *            event occurring
	 */
	public void selectNodeActionListener(ActionEvent evt) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();

		String nodeId = (String) map.get("nodeId");
		String pageId = (String) map.get("pageId");

		this.selectNodeToShow(nodeId, pageId);
	}

	/**
	 * Action listener on the radio button which allows to change the type of
	 * tree ( tasks, roles, work products)
	 * 
	 * @param evt
	 *            event occurring
	 */
	public void changeModeActionListener(ValueChangeEvent evt) {
		this.selectedMode = (String) evt.getNewValue();
		this.buildTreeModel();
	}

	/**
	 * Gets a list of the types of tree ( tasks, roles, work products)
	 * 
	 * @return a list of SelectItem
	 */
	public List<SelectItem> getModesList() {
		ArrayList<SelectItem> modesList = new ArrayList<SelectItem>();

		modesList.add(new SelectItem(TASKS_MODE, LocaleBean
				.getText("navigation.tree.checkboxlabel.tasks")));
		modesList.add(new SelectItem(ROLES_MODE, LocaleBean
				.getText("navigation.tree.checkboxlabel.roles")));
		modesList.add(new SelectItem(WORKPRODUCTS_MODE, LocaleBean
				.getText("navigation.tree.checkboxlabel.workproducts")));

		return modesList;
	}

	/**
	 * Getter for the combobox representing the projects to which the
	 * participant is affected
	 * 
	 * @return a list of SelectItem
	 */

	public List<SelectItem> getProjects() {
		List<SelectItem> projectsList = new ArrayList<SelectItem>();
		// projectDirector
		String role = (String) WebSessionService
				.getAttribute(WebSessionService.ROLE_TYPE);
		String wilosUserId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);
		if (role.equals("participant")) {

			Participant participant = this.participantService
					.getParticipant(wilosUserId);

			if (participant != null) {
				HashMap<Project, Boolean> projects = this.participantService
						.getProjectsForAParticipant(participant);
				for (Project project : projects.keySet()) {
					if (projects.get(project)) {
						this.addSelectItemToList(projectsList, new SelectItem(
								project.getId(), project.getConcreteName()));
					}
				}
			}
		} else if (role.equals("projectDirector")) {

			WilosUser pd = new WilosUser();
			pd = this.wilosUserService.getSimpleUser(wilosUserId);
			if (pd != null) {
				for (Project project : projectService.getAllSortedProjects()) {
					if (project.getProjectDirector().equals(pd.getId())) {
						this.addSelectItemToList(projectsList, new SelectItem(
								project.getId(), project.getConcreteName()));
					}
				}

			}

		}
		projectsList.add(0, new SelectItem(DEFAULT_PROJECT_ID, LocaleBean
				.getText("navigation.tree.defaulttreenodetext")));
		return projectsList;
	}

	/**
	 * Inserts the SelectItem _si representing a project into the list used by
	 * the combobox
	 * 
	 * @param _projectsList
	 *            the projectsList
	 * @param _si
	 *            the SelectItem
	 */
	private void addSelectItemToList(List<SelectItem> _projectsList,
			SelectItem _si) {
		if (_projectsList.size() == 0)
			_projectsList.add(_si);
		else {
			int i;
			// inserting the project in an alphabetically ordered list
			for (i = 0; i < _projectsList.size()
					&& _si.getLabel()
							.compareTo(_projectsList.get(i).getLabel()) > 0; i++) {
			}
			_projectsList.add(i, _si);
		}
	}

	/**
	 * Selects to node to show depending on the id of the object and the page id
	 * 
	 * @param _objectId
	 *            id of the object being shown
	 * @param _pageId
	 *            id of the page to be displayed in the browser
	 */
	private void selectNodeToShow(String _objectId, String _pageId) {
		if (_objectId != null && _pageId != null) {
			if (_pageId.equals(WilosObjectNode.ACTIVITYNODE)) {
				ConcreteActivityViewerBean av = (ConcreteActivityViewerBean) WebCommonService
						.getBean(WilosObjectNode.ACTIVITYNODE + "Bean");

				// recovers the object in the HashMap for the viewer
				ConcreteActivity ca = (ConcreteActivity) treeMap.get(_objectId);
				av.setConcreteActivity(ca);
				WebSessionService.setAttribute(
						WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT, ca
								.getId());
				WebCommonService.changeContentPage(_pageId);
			} else if (_pageId.equals(WilosObjectNode.CONCRETETASKNODE)) {
				ConcreteTaskViewerBean ctv = (ConcreteTaskViewerBean) WebCommonService
						.getBean(WilosObjectNode.CONCRETETASKNODE + "Bean");

				// recovers the object in the HashMap for the viewer
				ConcreteTaskDescriptor ctd = (ConcreteTaskDescriptor) treeMap
						.get(_objectId);
				ctv.setConcreteTaskDescriptor(ctd);
				WebSessionService.setAttribute(
						WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT, ctd
								.getId());
				WebCommonService.changeContentPage(_pageId);
			} else if (_pageId.equals(WilosObjectNode.CONCRETEMILESTONENODE)) {
				ConcreteMilestoneViewerBean cmiv = (ConcreteMilestoneViewerBean) WebCommonService
						.getBean(WilosObjectNode.CONCRETEMILESTONENODE + "Bean");

				// recovers the object in the HashMap for the viewer
				ConcreteMilestone cmi = (ConcreteMilestone) treeMap
						.get(_objectId);
				cmiv.setConcreteMilestone(cmi);

				WebCommonService.changeContentPage(_pageId);
			} else if (_pageId.equals(WilosObjectNode.CONCRETEROLENODE)) {
				ConcreteRoleViewerBean crv = (ConcreteRoleViewerBean) WebCommonService
						.getBean(WilosObjectNode.CONCRETEROLENODE + "Bean");

				// recovers the object in the HashMap for the viewer
				ConcreteRoleDescriptor crd = (ConcreteRoleDescriptor) treeMap
						.get(_objectId);
				crv.setConcreteRoleDescriptor(crd);

				WebCommonService.changeContentPage(_pageId);

			} else if (_pageId.equals(WilosObjectNode.CONCRETEWORKPRODUCTNODE)) {

				ConcreteWorkProductViewerBean cwpv = (ConcreteWorkProductViewerBean) WebCommonService
						.getBean(WilosObjectNode.CONCRETEWORKPRODUCTNODE
								+ "Bean");

				// recovers the object in the HashMap for the viewer
				ConcreteWorkProductDescriptor cwpd = (ConcreteWorkProductDescriptor) treeMap
						.get(_objectId);
				cwpv.setConcreteWorkProductDescriptor(cwpd);

				WebCommonService.changeContentPage(_pageId);

			} else if (_pageId.equals(WilosObjectNode.ITERATIONNODE)) {
				ConcreteIterationViewerBean iv = (ConcreteIterationViewerBean) WebCommonService
						.getBean(WilosObjectNode.ITERATIONNODE + "Bean");

				// recovers the object in the HashMap for the viewer
				ConcreteIteration ci = (ConcreteIteration) treeMap
						.get(_objectId);
				iv.setConcreteIteration(ci);
				WebSessionService.setAttribute(
						WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT, ci
								.getId());

				WebCommonService.changeContentPage(_pageId);
			} else if (_pageId.equals(WilosObjectNode.PHASENODE)) {
				ConcretePhaseViewerBean pb = (ConcretePhaseViewerBean) WebCommonService
						.getBean(WilosObjectNode.PHASENODE + "Bean");

				// recovers the object in the HashMap for the viewer
				ConcretePhase cp = (ConcretePhase) treeMap.get(_objectId);
				pb.setConcretePhase(cp);
				WebSessionService.setAttribute(
						WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT, cp
								.getId());

				WebCommonService.changeContentPage(_pageId);
			} else if (_pageId.equals(WilosObjectNode.PROJECTNODE)) {
				ProjectViewerBean p = (ProjectViewerBean) WebCommonService
						.getBean(WilosObjectNode.PROJECTNODE + "Bean");

				// recovers the object in the HashMap for the viewer
				Project proj = (Project) treeMap.get(_objectId);
				p.setProject(proj);
				WebSessionService.setAttribute(
						WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT, proj
								.getId());
				WebCommonService.changeContentPage(_pageId);
			} else {
				// displays blank page
				WebCommonService.changeContentPage("wilos");
			}
		}
	}

	/* Getters & Setters */

	public DefaultTreeModel getModel() {
		return this.model;
	}

	public String getProjectId() {
		return this.projectId;
	}

	public void setProjectId(String _processId) {
		this.projectId = _processId;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public Boolean getLoadTree() {
		if (this.projectId != null
				&& !this.projectId.equals(DEFAULT_PROJECT_ID))
			this.loadTree = false;
		else
			this.loadTree = true;

		return this.loadTree;
	}

	public void setLoadTree(Boolean loadTree) {
		this.loadTree = loadTree;
	}

	public LoginService getLoginService() {
		return loginService;
	}

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	public ParticipantService getParticipantService() {
		return participantService;
	}

	public void setParticipantService(ParticipantService participantService) {
		this.participantService = participantService;
	}

	public ProcessService getProcessService() {
		return processService;
	}

	public void setProcessService(ProcessService processService) {
		this.processService = processService;
	}

	/**
	 * Gets the tree map
	 * 
	 * @return the HashMap
	 */
	public HashMap<String, Object> getTreeMap() {
		return treeMap;
	}

	/**
	 * Sets the tree map
	 * 
	 * @param _treeMap
	 *            the new HashMap
	 */
	public void setTreeMap(HashMap<String, Object> _treeMap) {
		this.treeMap = _treeMap;
	}

	/**
	 * Gets the selected mode
	 * 
	 * @return the selectedMode
	 */
	public String getSelectedMode() {
		return this.selectedMode;
	}

	/**
	 * Sets the select mode
	 * 
	 * @param selectedMode
	 *            the selectedMode to set
	 */
	public void setSelectedMode(String _selectedMode) {
		this.selectedMode = _selectedMode;
	}

	/**
	 * Gets the hidden state of the radio buttons
	 * 
	 * @return
	 */
	public boolean getHideRadio() {
		if (this.projectId != null
				&& !this.projectId.equals(DEFAULT_PROJECT_ID))
			this.hideRadio = true;
		else
			this.hideRadio = false;
		return this.hideRadio;
	}

	/**
	 * Hides/shows the radio buttons
	 * 
	 * @param _hideRadio
	 */
	public void setHideRadio(boolean _hideRadio) {
		this.hideRadio = _hideRadio;
	}

	/**
	 * Get the service of wilos user
	 * 
	 * @return the WilosUserService
	 */
	public WilosUserService getWilosUserService() {
		return wilosUserService;
	}

	/**
	 * Change the wilosUser service
	 * 
	 * @param wilosUserService
	 *            the new service
	 */
	public void setWilosUserService(WilosUserService wilosUserService) {
		this.wilosUserService = wilosUserService;
	}
	
	/**
	 * Test if the part Task of the tree legends must be showed
	 * 
	 * @return true is the selected mode is task
	 */
	public boolean isShowTasks() {
		return this.selectedMode.equals(TreeBean.TASKS_MODE);
	}
	
	/**
	 * Test if the part Role of the tree legends must be showed
	 * 
	 * @return true is the selected mode is roles
	 */
	public boolean isShowRoles() {
		return this.selectedMode.equals(TreeBean.ROLES_MODE);
	}
	
	/**
	 * Test if the part Product of the tree legends must be showed
	 * 
	 * @return true is the selected mode is product
	 */
	public boolean isShowWorkProducts() {
		return this.selectedMode.equals(TreeBean.WORKPRODUCTS_MODE);
	}

}