/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
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

package wilos.presentation.web.expandabletable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import wilos.business.services.misc.project.ProjectService;
import wilos.business.services.spem2.activity.ActivityService;
import wilos.business.services.spem2.process.ProcessService;
import wilos.model.misc.project.Project;
import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.breakdownelement.BreakdownElement;
import wilos.model.spem2.milestone.Milestone;
import wilos.model.spem2.process.Process;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.model.spem2.workbreakdownelement.WorkBreakdownElement;
import wilos.presentation.web.process.ProcessBean;
import wilos.presentation.web.project.ProjectAdvancementBean;
import wilos.presentation.web.tree.TreeBean;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;
import wilos.resources.LocaleBean;

public class TasksExpTableBean {

	public static final String EXPAND_TABLE_ARROW = "images/expandableTable/expand.gif";

	public static final String CONTRACT_TABLE_ARROW = "images/expandableTable/contract.gif";

	public static final String TABLE_LEAF = "images/expandableTable/leaf.gif";

	public static final String CHECKED = "images/expandableTable/checked.gif";

	public static final String INDENTATION_STRING = "|- - - ";

	private List<HashMap<String, Object>> expTableContent = new ArrayList<HashMap<String, Object>>();;

	protected HashMap<String, Boolean> isExpanded = new HashMap<String, Boolean>();

	private HashMap<String, String> indentationContent = new HashMap<String, String>();;

	private ProcessService processService;

	private ProjectService projectService;

	private ActivityService activityService;

	private boolean needIndentation = false;

	private boolean isVisible = true;

	private boolean isInstanciedProject = false;

	private boolean isVisibleDependenciesManagementButton = false;

	private String viewedProcessId = "";

	private String selectedProcessId = "default";

	private String instanciationBtName;

	private String expandAllBtName;

	private String viewManagement;

	// private boolean activateWorkProductDependencies = false;
	private boolean activateWorkProductDependencies = true;

	public void refreshExpTable() {
		this.isInstanciedProject = true;
	}

	/**
	 * Verify if all activities non optional haven't a number of occurences
	 * equals to 0
	 * 
	 * @return
	 */
	public boolean verifyNbOccurences() {
		for (HashMap<String, Object> hashMap : this.expTableContent) {
			if (!(Boolean) hashMap.get("isOptional")) {
				if ((Integer) hashMap.get("nbOccurences") == 0) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * this method allow to save the task instanciation for the selected project
	 * 
	 * @param Action
	 *            Event _event
	 * @return nothing
	 */
	public void saveProjectInstanciation(ActionEvent _event) {

		String projectId = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);

		boolean ok = true;

		if ((projectId != null) && (!this.selectedProcessId.equals("default"))) {
			Project project = projectService.getProject(projectId);
			project
					.setConsiderWorkProductAndTaskLinks(activateWorkProductDependencies);
			if (project != null) {
				Process process = processService.getProcessDao().getProcess(
						selectedProcessId);
				if (process != null) {
					if (!this.isInstanciedProject) {
						// verify if occurence are ok
						ok = verifyNbOccurences();
						if (ok) {
							// instanciate
							processService.projectInstanciation(project,
									process, expTableContent);
						}
					} else {
						// update
						processService.projectUpdate(project, process,
								expTableContent);
					}

					// Dependencies instanciation
					ProcessBean pb = (ProcessBean) WebCommonService
							.getBean("ProcessBean");
					if (pb.getInstanciateDependenciesWithProcess()) {
						processService.projectDependenciesInstanciation(
								project, process);
					}
					this.projectService
							.updateConcreteActivitiesStateFromProject(project);
				}
			}
			if (ok) {
				this.isInstanciedProject = true;
				this.expTableContent.clear();

				// refresh de la table d'avancement.
				ProjectAdvancementBean pab = (ProjectAdvancementBean) WebCommonService
						.getBean("ProjectAdvancementBean");
				pab.refreshProjectTable();

				/* Displays info message */
				WebCommonService
						.addInfoMessage(LocaleBean
								.getText("component.instanciation.task.instanciatedMessage"));

				TreeBean tb = (TreeBean) WebCommonService.getBean("TreeBean");
				tb.rebuildProjectTree();
			} else {
				/*
				 * All activities non optional must have a number of occurences
				 * > to 0
				 */
				WebCommonService
						.addErrorMessage(LocaleBean
								.getText("component.instanciation.number.ocurrences.error"));
			}
		} else {
			/* The wilos user has to choose a process in the processes list ! */
			WebCommonService.addErrorMessage(LocaleBean
					.getText("component.instanciation.chooseAProcessMessage"));
		}
	}

	/**
	 * getter of expTableContent HashMap List attribute
	 * 
	 * @return the expTableContent
	 */
	public List<HashMap<String, Object>> getExpTableContent() {

		if (!this.selectedProcessId.equals("default")) {
			Process process = this.processService
					.getProcess(this.selectedProcessId);
			if (!this.viewedProcessId.equals(process.getId())
					|| this.expTableContent.isEmpty()) {
				this.viewedProcessId = process.getId();
				this.isExpanded.clear();
				this.expTableContent.clear();
				this.indentationContent.clear();
				this.needIndentation = false;
				List<HashMap<String, Object>> lines = this
						.getExpTableLineContent(process);
				this.expTableContent.addAll(lines);
			}
		}
		return this.expTableContent;
	}

	/**
	 * this method allow to return a list of roleDescriptor for the given
	 * activity
	 * 
	 * @param _act
	 * @return List<HashMap<String,Object>>
	 */
	private List<HashMap<String, Object>> getExpTableLineContent(Activity _act) {

		List<HashMap<String, Object>> lines = new ArrayList<HashMap<String, Object>>();
		String indentationString = "";
		Activity act = this.activityService.getActivity(_act.getId());
		SortedSet<BreakdownElement> set = this.activityService
				.getAllBreakdownElements(act);
		act.setBreakdownElements(set);
		for (BreakdownElement bde : act.getBreakdownElements()) {
			if (bde instanceof WorkBreakdownElement) {
				WorkBreakdownElement wbde = (WorkBreakdownElement) bde;
				HashMap<String, Object> hm = new HashMap<String, Object>();
				if ((wbde instanceof TaskDescriptor)
						|| (wbde instanceof Milestone)) {
					hm.put("nodeType", "leaf");
					hm.put("expansionImage", TABLE_LEAF);
				} else {
					hm.put("nodeType", "node");
					hm.put("expansionImage", CONTRACT_TABLE_ARROW);
				}
				hm.put("id", wbde.getId());
				hm.put("name", wbde.getPresentationName());
				hm.put("isEditable", wbde.getHasMultipleOccurrences()
						|| wbde.getIsRepeatable() || wbde.getIsOptional());
				if (this.isInstanciedProject) {
					hm.put("nbOccurences", new Integer(0));
				} else {
					hm.put("nbOccurences", new Integer(1));
				}
				hm.put("parentId", act.getId());

				hm.put("checked", CHECKED);

				hm.put("hasMultipleOccurrences", wbde
						.getHasMultipleOccurrences());
				hm.put("isEvenDriven", wbde.getIsEvenDriven());
				hm.put("isOngoing", wbde.getIsOngoing());
				hm.put("isOptional", wbde.getIsOptional());
				hm.put("isPlanned", wbde.getIsPlanned());
				hm.put("isRepeatable", wbde.getIsRepeatable());

				lines.add(hm);

				if (needIndentation) {
					if (this.indentationContent.get(act.getId()) != null) {
						indentationString = this.indentationContent.get(act
								.getId());
					}
					this.indentationContent.put((String) hm.get("id"),
							indentationString.concat(INDENTATION_STRING));
				}
			}
		}
		return lines;
	}

	/**
	 * Utility method to add all child nodes to the parent dataTable list.
	 */
	private void expandNodeAction() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");

		this.needIndentation = true;

		ArrayList<HashMap<String, Object>> tmp = new ArrayList<HashMap<String, Object>>();
		tmp.addAll(this.expTableContent);
		int index;
		for (HashMap<String, Object> hashMap : tmp) {
			if (hashMap.get("id").equals(elementId)) {
				if (hashMap.get("nodeType").equals("node")) {
					hashMap.put("expansionImage", EXPAND_TABLE_ARROW);
					Activity act = this.activityService.getActivityDao()
							.getActivity((String) hashMap.get("id"));
					index = this.expTableContent.indexOf(hashMap);
					this.expTableContent.addAll(index + 1, this
							.getExpTableLineContent(act));
					return;
				}
			}
		}
	}

	/**
	 * Utility method to remove all child nodes from the parent dataTable list.
	 */
	private void contractNodeAction() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");

		ArrayList<HashMap<String, Object>> parentList = new ArrayList<HashMap<String, Object>>();
		parentList.addAll(this.expTableContent);

		/* Removes element which we want to contract from the parent list */
		for (HashMap<String, Object> currentElement : this.expTableContent) {

			if (currentElement.get("id").equals(elementId)
					&& currentElement.get("nodeType").equals("node")) {
				currentElement.put("expansionImage", CONTRACT_TABLE_ARROW);
				parentList.remove(currentElement);
			}
		}
		this.deleteChildren(elementId, parentList);
	}

	/**
	 * Hides children for a specific row of the expandable table Used to
	 * simulate contraction behaviour
	 * 
	 * @param _parentId
	 *            identifier of current row
	 * @param parentList
	 *            parent children
	 */
	private void deleteChildren(String parentId,
			ArrayList<HashMap<String, Object>> parentList) {
		for (HashMap<String, Object> child : parentList) {
			if (child.get("parentId").equals(parentId)) {
				this.expTableContent.remove(child);
				deleteChildren((String) child.get("id"), parentList);
			}
			if (child.get("id").equals(parentId)) {
				child.put("expansionImage", CONTRACT_TABLE_ARROW);
				this.isExpanded.put((String) child.get("id"), false);
			}
		}
	}

	/**
	 * Toggles the expanded state of this ConcreteBreakDownElement.
	 * 
	 * @param event
	 */
	public void toggleSubGroupAction(ActionEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");

		// toggle expanded state
		Boolean b = isExpanded.get(elementId);
		if (b == null) {
			isExpanded.put(elementId, true);
			b = isExpanded.get(elementId);
		} else {
			if (b) {
				b = false;
			} else {
				b = true;
			}
			isExpanded.put(elementId, b);
		}

		if (b) {
			expandNodeAction();
		} else {
			contractNodeAction();
		}

	}

	/**
	 * Toggles the expanded state of all ConcreteBreakDownElement.
	 * 
	 * @param event
	 */
	public void toggleAllSubGroupAction(ActionEvent _event) {
		String elementId = null;
		Boolean b = null;
		ArrayList<HashMap<String, Object>> tmp ;
		Boolean existsContractNode = true ;
		while (existsContractNode) {
			existsContractNode = false ;
			tmp = new ArrayList<HashMap<String, Object>>();
			tmp.addAll(this.expTableContent);
			for (HashMap<String, Object> hm : tmp) {
				if (((String) hm.get("nodeType")).equals("node")) {
					if (((String) hm.get("expansionImage"))
							.equals(CONTRACT_TABLE_ARROW)) {
						existsContractNode = true;
						b = null;
						elementId = (String) hm.get("id");
						// toggle expanded state
						b = isExpanded.get(elementId);
						if (b == null) {
							isExpanded.put(elementId, true);
							b = isExpanded.get(elementId);
						} else {
							if (!b) {
								b = true;
							}
							isExpanded.put(elementId, b);
						}
						if (b) {
							expandNodeAction(elementId);
						}
					}
				}
			}
		}
	}
	
	/**
	 * expand the node for the specified element
	 * @param _elementId
	 */
	private void expandNodeAction(String _elementId) {
		this.needIndentation = true;
		ArrayList<HashMap<String, Object>> tmp = new ArrayList<HashMap<String, Object>>();
		tmp.addAll(this.expTableContent);
		int index;
		for (HashMap<String, Object> hashMap : tmp) {
			if (hashMap.get("id").equals(_elementId)) {
				if (hashMap.get("nodeType").equals("node")) {
					hashMap.put("expansionImage", EXPAND_TABLE_ARROW);
					Activity act = this.activityService.getActivityDao()
							.getActivity((String) hashMap.get("id"));
					index = this.expTableContent.indexOf(hashMap);
					this.expTableContent.addAll(index + 1, this
							.getExpTableLineContent(act));
					return;
				}
			}
		}
	}

	/**
	 * setter of expTableContent HashMap List attribute
	 * 
	 * @param _expTableContent
	 *            the expTableContent to set
	 */
	public void setExpTableContent(
			ArrayList<HashMap<String, Object>> _expTableContent) {
		this.expTableContent = _expTableContent;
	}

	/**
	 * getter of isExpanded HashMap attribute
	 * 
	 * @return the isExpanded
	 */
	public HashMap<String, Boolean> getIsExpanded() {
		return this.isExpanded;
	}

	/**
	 * setter of isExpanded HashMap attribute
	 * 
	 * @param _isExpanded
	 *            the isExpanded to set
	 */
	public void setIsExpanded(HashMap<String, Boolean> _isExpanded) {
		this.isExpanded = _isExpanded;
	}

	/**
	 * this method allows to return the current instance of ActivityService
	 * 
	 * @return the activityService
	 */
	public ActivityService getActivityService() {
		return this.activityService;
	}

	/**
	 * this method allows to set the current instance of ActivityService
	 * 
	 * @param _activityService
	 *            the activityService to set
	 */
	public void setActivityService(ActivityService _activityService) {
		this.activityService = _activityService;
	}

	/**
	 * this method allows to return the current instance of ProcessService
	 * 
	 * @return the processService
	 */
	public ProcessService getProcessService() {
		return this.processService;
	}

	/**
	 * this method allows to set the current instance of ProcessService
	 * 
	 * @param _processService
	 *            the processService to set
	 */
	public void setProcessService(ProcessService _processService) {
		this.processService = _processService;
	}

	/**
	 * this method allows to return the current instance of ProjectService
	 * 
	 * @return the projectService
	 */
	public ProjectService getProjectService() {
		return this.projectService;
	}

	/**
	 * this method allows to set the current instance of ProjectService
	 * 
	 * @param _projectService
	 *            the projectService to set
	 */
	public void setProjectService(ProjectService _projectService) {
		this.projectService = _projectService;
	}

	/**
	 * getter of isVisible boolean attribute
	 * 
	 * @return the isVisible
	 */
	public boolean isVisible() {
		return this.isVisible;
	}

	/**
	 * setter of isVisible boolean attribute
	 * 
	 * @param _isVisible
	 *            the isVisible to set
	 */
	public void setVisible(boolean _isVisible) {
		this.isVisible = _isVisible;
	}

	/**
	 * getter of selectedProcessId String attribute
	 * 
	 * 
	 * @return the selectedProcessId
	 */
	public String getSelectedProcessId() {
		return this.selectedProcessId;
	}

	/**
	 * setter of selectedProcessId String attribute
	 * 
	 * @param _selectedProcessGuid
	 *            the selectedProcessId to set
	 */
	public void setSelectedProcessId(String _selectedProcessGuid) {
		this.selectedProcessId = _selectedProcessGuid;
	}

	/**
	 * getter of isInstanciedProject boolean attribute
	 * 
	 * @return the isInstanciedProject
	 */
	public boolean getIsInstanciedProject() {
		return this.isInstanciedProject;
	}

	/**
	 * setter of isInstanciedProject boolean attribute
	 * 
	 * @param _isInstanciedProject
	 *            the isInstanciedProject to set
	 */
	public void setIsInstanciedProject(boolean _isInstanciedProject) {
		this.isInstanciedProject = _isInstanciedProject;
	}

	/**
	 * getter of indentationContent HashMap attribute
	 * 
	 * @return the indentationContent
	 */
	public HashMap<String, String> getIndentationContent() {
		return this.indentationContent;
	}

	/**
	 * setter of indentationContent HashMap attribute
	 * 
	 * @param _indentationContent
	 *            the indentationContent to set
	 */
	public void setIndentationContent(
			HashMap<String, String> _indentationContent) {
		this.indentationContent = _indentationContent;
	}

	/**
	 * getter of instanciationBtName String attribute
	 * 
	 * @return the instanciationBtName
	 */
	public String getInstanciationBtName() {
		if (!this.isInstanciedProject) {
			this.instanciationBtName = LocaleBean
					.getText("component.instanciation.button.ins");
			this.isVisibleDependenciesManagementButton = false;
		} else {
			this.instanciationBtName = LocaleBean
					.getText("component.instanciation.button.up");
			this.isVisibleDependenciesManagementButton = true;
		}
		return this.instanciationBtName;
	}

	/**
	 * setter of instanciationBtName String attribute
	 * 
	 * @param _instanciationBtName
	 *            the instanciationBtName to set
	 */
	public void setInstanciationBtName(String _instanciationBtName) {
		this.instanciationBtName = _instanciationBtName;
	}

	/**
	 * getter of viewManagement String attribute
	 * 
	 * @return the viewManagement
	 */
	public String getViewManagement() {
		return this.viewManagement;
	}

	/**
	 * setter of viewManagement String attribute
	 * 
	 * @param _viewManagement
	 *            the viewManagement to set
	 */
	public void setViewManagement(String _viewManagement) {
		this.viewManagement = _viewManagement;
	}

	/**
	 * getter of isVisibleDependenciesManagementButton boolean attribute
	 * 
	 * @return the isVisibleDependenciesManagementButton
	 */
	public boolean getIsVisibleDependenciesManagementButton() {
		return this.isVisibleDependenciesManagementButton;
	}

	/**
	 * setter of isVisibleDependenciesManagementButton boolean attribute
	 * 
	 * @param _isVisibleDependenciesManagementButton
	 *            the isVisibleDependenciesManagementButton to set
	 */
	public void setVisibleDependenciesManagementButton(
			boolean _isVisibleDependenciesManagementButton) {
		this.isVisibleDependenciesManagementButton = _isVisibleDependenciesManagementButton;
	}

	/**
	 * getter of activateWorkProductDependencies boolean attribute
	 * 
	 * @return the activateWorkProductDependencies
	 */
	public boolean getActivateWorkProductDependencies() {
		String projectId = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);
		boolean b = this.projectService.getProject(projectId)
				.getConsiderWorkProductAndTaskLinks();
		if (!b && this.activateWorkProductDependencies == true) {
			return true;
		} else {
			return (b || this.activateWorkProductDependencies);
		}
	}

	/**
	 * setter of activateWorkProductDependencies boolean attribute
	 * 
	 * @param _activateWorkProductDependencies
	 *            the activateWorkProductDependencies to set
	 */
	public void setActivateWorkProductDependencies(
			boolean _activateWorkProductDependencies) {
		this.activateWorkProductDependencies = _activateWorkProductDependencies;
	}

	/**
	 * @param expandAllBtName
	 *            the expandAllBtName to set
	 */
	public void setExpandAllBtName(String _expandAllBtName) {
		this.expandAllBtName = _expandAllBtName;
	}

	/**
	 * @return the expandAllBtName
	 */
	public String getExpandAllBtName() {
		this.expandAllBtName = LocaleBean
				.getText("component.instanciation.button.expandTasks");
		return this.expandAllBtName;
	}
}
