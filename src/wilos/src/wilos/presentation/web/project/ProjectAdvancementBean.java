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

package wilos.presentation.web.project;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wilos.business.services.misc.concreteactivity.ConcreteActivityService;
import wilos.business.services.misc.concretebreakdownelement.ConcreteBreakdownElementService;
import wilos.business.services.misc.concreterole.ConcreteRoleDescriptorService;
import wilos.business.services.misc.concretetask.ConcreteTaskDescriptorService;
import wilos.business.services.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElementService;
import wilos.business.services.misc.concreteworkproduct.ConcreteWorkProductDescriptorService;
import wilos.business.services.misc.project.ProjectService;
import wilos.business.services.misc.wilosuser.ParticipantService;
import wilos.business.services.spem2.role.RoleDescriptorService;
import wilos.business.services.spem2.task.TaskDescriptorService;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concretemilestone.ConcreteMilestone;
import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;
import wilos.model.misc.project.Project;
import wilos.model.misc.wilosuser.Participant;
import wilos.model.misc.wilosuser.WilosUser;
import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.breakdownelement.BreakdownElement;
import wilos.model.spem2.milestone.Milestone;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.model.spem2.workbreakdownelement.WorkBreakdownElement;
import wilos.presentation.web.tree.TreeBean;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;
import wilos.presentation.web.viewer.ConcreteRoleViewerBean;
import wilos.presentation.web.viewer.ConcreteTaskViewerBean;
import wilos.resources.LocaleBean;
import wilos.utils.Constantes;
import wilos.utils.Constantes.State;

public class ProjectAdvancementBean {

	public static final String EXPAND_TABLE_ARROW = "images/expandableTable/expand.gif";

	public static final String CONTRACT_TABLE_ARROW = "images/expandableTable/contract.gif";

	public static final String TABLE_LEAF = "images/expandableTable/leaf.gif";

	public static final String INDENTATION_STRING = "|- - - ";

	private static final String ALL_ROLE_MODE = "allRoleMode";

	private static final String MY_ROLE_MODE = "myRoleMode";

	private String selectedMode = ALL_ROLE_MODE;

	private ProjectService projectService;

	private ConcreteActivityService concreteActivityService;

	private ParticipantService participantService;

	private Project project;

	private String projectViewedId;

	private ArrayList<HashMap<String, Object>> displayContent;

	private ArrayList<HashMap<String, Object>> displayRoleContent;

	private ArrayList<HashMap<String, Object>> displayRoleContentToDelete;

	private HashMap<String, String> indentationContent;

	private boolean needIndentation = false;

	protected HashMap<String, Boolean> isExpanded = new HashMap<String, Boolean>();

	private boolean selectedProjectAdvancementView;

	private boolean projectModified;

	private String projectInstanciated;

	protected final Log logger = LogFactory.getLog(this.getClass());

	private ConcreteTaskDescriptorService concreteTaskDescriptorService;
	
	private ConcreteRoleDescriptorService concreteRoleDescriptorService;

	private ConcreteWorkProductDescriptorService concreteWorkProductDescriptorService;

	private int pos = 0;

	private Date startingDate;

	private String idActivity;

	private Date finishingDate;

	private Float plannedTime;

	private String concreteName;

	private String cbdeId;

	private ConcreteBreakdownElementService concreteBreakdownElementService;

	private boolean visibleDeleteRolePopup = false;

	/**
	 * Constructor.
	 * 
	 */
	public ProjectAdvancementBean() {
		this.project = new Project();
		this.displayContent = new ArrayList<HashMap<String, Object>>();
		this.displayRoleContent = new ArrayList<HashMap<String, Object>>();
		this.displayRoleContentToDelete = new ArrayList<HashMap<String, Object>>();
		this.indentationContent = new HashMap<String, String>();
	}

	/**
	 * Method to cancel the delete action
	 * 
	 * @param _event
	 */
	public void cancelDeleteRole(ActionEvent _event) {
		this.visibleDeleteRolePopup = false;
	}

	/**
	 * Getter of concreteBreakdownElementService.
	 * 
	 * @return the concreteBreakdownElementService.
	 */
	public ConcreteBreakdownElementService getConcreteBreakdownElementService() {
		return concreteBreakdownElementService;
	}

	/**
	 * Setter of concreteBreakdownElementService.
	 * 
	 * @param the
	 *            concreteBreakdownElementService.
	 */
	public void setConcreteBreakdownElementService(
			ConcreteBreakdownElementService _concreteBreakdownElementService) {
		concreteBreakdownElementService = _concreteBreakdownElementService;
	}

	/**
	 * Setter of concreteTaskDescriptor.
	 * 
	 * @param the
	 *            concreteTaskDescriptor.
	 */
	public void setConcreteTaskDescriptorService(
			ConcreteTaskDescriptorService _concreteTaskDescriptor) {
		concreteTaskDescriptorService = _concreteTaskDescriptor;
	}

	/**
	 * Setter of concreteTaskDescriptor.
	 * 
	 * @param the
	 *            concreteTaskDescriptor.
	 */
	public void setConcreteTaskDescriptor(
			ConcreteTaskDescriptorService _concreteTaskDescriptor) {
		concreteTaskDescriptorService = _concreteTaskDescriptor;
	}

	/**
	 * Getter of concreteTaskDescriptor.
	 * 
	 * @return the concreteTaskDescriptor.
	 */
	public ConcreteTaskDescriptorService getConcreteTaskDescriptorService() {
		return concreteTaskDescriptorService;
	}

	/**
	 * Getter of concreteTaskDescriptor.
	 * 
	 * @return the concreteTaskDescriptor.
	 */
	public ConcreteTaskDescriptorService getConcreteTaskDescriptor() {
		return concreteTaskDescriptorService;
	}

	/**
	 * Getter of displayContent.
	 * 
	 * @return the displayContent.
	 */
	public ArrayList<HashMap<String, Object>> getDisplayContent() {
		String projectId = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);

		if (projectId != null && !projectId.equals("default")) {
			this.project = this.projectService.getProject(projectId);
			this.pos = 0;
			if (project != null) {
				// if the project is another then the last
				// selected or if it has
				// been
				// modified
				if (this.projectViewedId == null
						|| !(this.projectViewedId.equals(projectId))
						|| this.projectModified) {
					// reseting the table parameters
					projectViewedId = projectId;
					this.displayContent.clear();
					this.indentationContent.clear();
					this.isExpanded.clear();
					this.needIndentation = false;
					List<HashMap<String, Object>> list = this
							.retrieveHierarchicalItems(this.project);
					if (list != null) {
						this.displayContent.addAll(list);
					} else
						this.displayContent.clear();

				}
			} else {
				this.displayContent.clear();
			}
		} else
			this.displayContent.clear();

		this.projectModified = false;
		return this.displayContent;
	}

	/**
	 * Getter of displayRoleContent.
	 * 
	 * @return the displayRoleContent.
	 */
	public ArrayList<HashMap<String, Object>> getDisplayRoleContent() {
		String projectId = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);

		if (projectId != null && !projectId.equals("default")) {
			this.project = this.projectService.getProject(projectId);
			this.pos = 0;
			if (project != null) {
				// if the project is another then the last
				// selected or if it has
				// been
				// modified
				if (this.projectViewedId == null
						|| !(this.projectViewedId.equals(projectId))
						|| this.projectModified) {
					// reseting the table parameters
					projectViewedId = projectId;
					this.displayRoleContent.clear();
					this.indentationContent.clear();
					this.isExpanded.clear();
					this.needIndentation = false;
					List<HashMap<String, Object>> list = this
							.retrieveHierarchicalRoleItems(this.project);
					if (list != null) {
						this.displayRoleContent.addAll(list);
					} else
						this.displayRoleContent.clear();

				}
			} else {
				this.displayRoleContent.clear();
			}
		} else
			this.displayRoleContent.clear();

		this.projectModified = false;
		return this.displayRoleContent;
	}

	/**
	 * Getter of displayRoleContentToDelete.
	 * 
	 * @return the displayRoleContentToDelete.
	 */
	public ArrayList<HashMap<String, Object>> getDisplayRoleContentToDelete() {
		String projectId = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);

		if (projectId != null && !projectId.equals("default")) {
			this.project = this.projectService.getProject(projectId);
			this.pos = 0;
			if (project != null) {
				// if the project is another then the last
				// selected or if it has
				// been
				// modified
				if (this.projectViewedId == null
						|| !(this.projectViewedId.equals(projectId))
						|| this.projectModified) {
					// reseting the table parameters
					projectViewedId = projectId;
					this.displayRoleContentToDelete.clear();
					this.indentationContent.clear();
					this.isExpanded.clear();
					this.needIndentation = false;
					List<HashMap<String, Object>> list = this
							.retrieveHierarchicalRoleItems(this.project);
					if (list != null) {
						this.displayRoleContentToDelete.addAll(list);
					} else
						this.displayRoleContentToDelete.clear();

				}
			} else {
				this.displayRoleContentToDelete.clear();
			}
		} else
			this.displayRoleContentToDelete.clear();

		this.projectModified = false;
		return this.displayRoleContentToDelete;
	}

	private List<HashMap<String, Object>> retrieveHierarchicalRoleItems(
			ConcreteActivity _concreteActivity) {
		String indentationString = "";
		String affected = "";
		boolean b = false;
		List<HashMap<String, Object>> subConcretesContent = new ArrayList<HashMap<String, Object>>();
		String userId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);
		String role = (String) WebSessionService
				.getAttribute(WebSessionService.ROLE_TYPE);
		Participant parti;
		// ConcreteRoleDescriptor crd = null;

		if (!role.equals("projectDirector")) {
			parti = this.getParticipantService().getParticipant(userId);
		} else {
			parti = null;
		}
		// for every child of the activity
		if (_concreteActivity == null) {
			return null;
		}
		SortedSet<ConcreteBreakdownElement> concreteBDE = this.concreteActivityService
				.getConcreteBreakdownElements(_concreteActivity);
		for (ConcreteBreakdownElement cbe : concreteBDE) {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			b = false;
			if (!(cbe instanceof ConcreteMilestone)) {
				// all roles
				if (cbe instanceof ConcreteRoleDescriptor) {
					b = true;
					if (!role.equals("projectDirector")) {
						affected = isAffected(parti,
								(ConcreteRoleDescriptor) cbe);
					}
					if (this.getSelectedMode().equals(ALL_ROLE_MODE)
							|| this.getSelectedMode().equals(MY_ROLE_MODE)
							&& affected.equals("Affected")) {
						this.displayARoleLeaf(hm, cbe, parti, role, affected,
								_concreteActivity);
					}
				} else if (cbe instanceof ConcreteWorkBreakdownElement
						&& !(cbe instanceof ConcreteTaskDescriptor)) {
					// if this is not a role an d not a task
					// -> display a node
					b = true;
					hm.put("visibleCheckbox", false);
					hm.put("nodeType", "node");
					hm.put("expansionImage", CONTRACT_TABLE_ARROW);
					hm.put("participant", "");
				}
				// if is a line for the table (activity or
				// concrete role
				// descriptor)
				if (b) {
					if (this.getSelectedMode().equals(ALL_ROLE_MODE)
							|| this.getSelectedMode().equals(MY_ROLE_MODE)
							&& (affected.equals("Affected") || affected
									.equals(""))) {
						// advancement consolidation
						// time calculation
						hm.put("id", cbe.getId());
						hm.put("concreteName", cbe.getConcreteName());
						hm.put("parentId", _concreteActivity.getId());
						subConcretesContent.add(hm);

						// if this is not the root node
						// -> needIndentation ==
						// true
						if (needIndentation) {
							if (this.indentationContent.get(_concreteActivity
									.getId()) != null) {
								indentationString = this.indentationContent
										.get(_concreteActivity.getId());
							}
							this.indentationContent
									.put(
											(String) hm.get("id"),
											indentationString
													.concat(ProjectAdvancementBean.INDENTATION_STRING));
						}
						hm.put("isEditable", new Boolean(false));
						hm.put("isStarted",
								isConcreteBreakdownElementStarted(cbe));
					}
				}
			}
		}
		return subConcretesContent;
	}

	/**
	 * @return the visibleRemove
	 */
	public boolean getVisibleRemove(ConcreteRoleDescriptor _crd) {
		return (_crd.getParticipant() == null);
	}

	private List<HashMap<String, Object>> retrieveHierarchicalRoleItemsToDelete(
			ConcreteActivity _concreteActivity) {
		
		String indentationString = "";
		String affected = "";
		boolean b = false;
		List<HashMap<String, Object>> subConcretesContent = new ArrayList<HashMap<String, Object>>();

		String userId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);
		String role = (String) WebSessionService
				.getAttribute(WebSessionService.ROLE_TYPE);
		Participant parti;
		// ConcreteRoleDescriptor crd = null;

		if (!role.equals("projectDirector")) {
			parti = this.getParticipantService().getParticipant(userId);
		} else {
			parti = null;
		}
		// for every child of the activity
		if (_concreteActivity == null) {
			return null;
		}
		SortedSet<ConcreteBreakdownElement> concreteBDE = this.concreteActivityService
				.getConcreteBreakdownElements(_concreteActivity);
		for (ConcreteBreakdownElement cbe : concreteBDE) {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			b = false;
			if (!(cbe instanceof ConcreteMilestone)) {
				// all roles
				if (cbe instanceof ConcreteRoleDescriptor) {
					b = true;
					if (!role.equals("projectDirector")) {
						affected = isAffected(parti,
								(ConcreteRoleDescriptor) cbe);
					}
					if (this.getSelectedMode().equals(ALL_ROLE_MODE)
							|| this.getSelectedMode().equals(MY_ROLE_MODE)
							&& affected.equals("Affected")) {
						this.displayARoleLeafToDelete(hm, cbe, parti, role, affected,
								_concreteActivity);
					}
				} else if (cbe instanceof ConcreteWorkBreakdownElement
						&& !(cbe instanceof ConcreteTaskDescriptor)) {
					// if this is not a role an d not a task
					// -> display a node
					b = true;
					hm.put("visibleCheckbox", false);
					hm.put("nodeType", "node");
					hm.put("expansionImage", CONTRACT_TABLE_ARROW);
					hm.put("participant", "");
				}
				// if is a line for the table (activity or
				// concrete role
				// descriptor)
				if (b) {
					if (this.getSelectedMode().equals(ALL_ROLE_MODE)
							|| this.getSelectedMode().equals(MY_ROLE_MODE)
							&& (affected.equals("Affected") || affected
									.equals(""))) {
						// advancement consolidation
						// time calculation
						hm.put("id", cbe.getId());
						hm.put("concreteName", cbe.getConcreteName());
						hm.put("parentId", _concreteActivity.getId());
						subConcretesContent.add(hm);

						// if this is not the root node
						// -> needIndentation ==
						// true
						if (needIndentation) {
							if (this.indentationContent.get(_concreteActivity
									.getId()) != null) {
								indentationString = this.indentationContent
										.get(_concreteActivity.getId());
							}
							this.indentationContent
									.put(
											(String) hm.get("id"),
											indentationString
													.concat(ProjectAdvancementBean.INDENTATION_STRING));
						}
						hm.put("isEditable", new Boolean(false));
						hm.put("isStarted",
								isConcreteBreakdownElementStarted(cbe));
					}
				}
			}
		}
		return subConcretesContent;
		
	}

	private void displayARoleLeaf(HashMap<String, Object> _hm,
			ConcreteBreakdownElement _cbe, Participant _parti, String _role,
			String _affected, ConcreteActivity _activity) {
		// Set as a leaf
		_hm.put("nodeType", "leaf");
		_hm.put("expansionImage", TABLE_LEAF);

		if (_cbe.getConcreteName() != "") {
			_hm.put("role", _cbe);
			_hm.put("roleName", _cbe.getConcreteName());
			boolean b1 = false;
			if (!_role.equals("projectDirector")) {
				if (_affected.equals("Affected")
						|| _affected.equals("OtherAffected")) {
					b1 = true;
				} else if (_affected.equals("NotAffected")) {
					b1 = false;
				}
			}
			_hm.put("isAffected", b1);
			_hm.put("flagAffect", _hm.get("isAffected"));
			_hm.put("visibleCheckbox", true);
		} else {
			_hm.put("roleName", LocaleBean.getText("tabAffectRole.anyRole"));
			_hm.put("visibleCheckbox", false);
		}
		// cocher la checkbox si le parti est affecté à ce role
		// cacher la case si
		// une des taches de ce role est démarrée
		// quelqu'un d'autre est affecté à ce role => pas cochée

		int nb = getNbTaskStartedOrFinishedForAConcreteRole(_activity,
				(ConcreteRoleDescriptor) _cbe);

		if (nb > 0){
			//_hm.put("enabled", true);
			_hm.put("visibleCheckbox", true);
		}else{
			_hm.put("enabled", false);
		}
		if (_affected.equals("Affected")) {
			_hm.put("isAffected", true);
			_hm.put("participant", _parti.getFirstname() + " "
					+ _parti.getName());
			_hm.put("visibleCheckbox", true);
		} else if (_affected.equals("OtherAffected")) {
			_hm.put("enabled", true);
			_hm.put("visibleCheckbox", false);
			_hm.put("isAffected", false);
			Participant p = ((ConcreteRoleDescriptor) _cbe).getParticipant();
			_hm.put("participant", p.getFirstname() + " " + p.getName());
		} else {
			_hm.put("visibleCheckbox", true);
			_hm.put("isAffected", false);
		}
	}
	
	private void displayARoleLeafToDelete(HashMap<String, Object> _hm,
			ConcreteBreakdownElement _cbe, Participant _parti, String _role,
			String _affected, ConcreteActivity _activity) {
		// Set as a leaf
		_hm.put("nodeType", "leaf");
		_hm.put("expansionImage", TABLE_LEAF);

		if (_cbe.getConcreteName() != "") {
			_hm.put("role", _cbe);
			_hm.put("roleName", _cbe.getConcreteName());
			boolean b1 = false;
			if (!_role.equals("projectDirector")) {
				if (_affected.equals("Affected")
						|| _affected.equals("OtherAffected")) {
					b1 = true;
				} else if (_affected.equals("NotAffected")) {
					b1 = false;
				}
			}
			_hm.put("isAffected", b1);
			_hm.put("flagAffect", _hm.get("isAffected"));
			_hm.put("visibleCheckbox", true);
		} else {
			_hm.put("roleName", LocaleBean.getText("tabAffectRole.anyRole"));
			_hm.put("visibleCheckbox", false);
		}
		// cocher la checkbox si le parti est affecté à ce role
		// cacher la case si
		// une des taches de ce role est démarrée
		// quelqu'un d'autre est affecté à ce role => pas cochée

		int nb = getNbTaskStartedOrFinishedForAConcreteRole(_activity,
				(ConcreteRoleDescriptor) _cbe);

		if (nb > 0){
			//_hm.put("enabled", true);
			_hm.put("visibleCheckbox", true);
		}else{
			_hm.put("enabled", false);
		}
		if (_affected.equals("Affected") || _affected.equals("OtherAffected")) {
			_hm.put("enabled", true);
			_hm.put("visibleCheckbox", false);
			_hm.put("isAffected", false);
			Participant p = ((ConcreteRoleDescriptor) _cbe).getParticipant();
			_hm.put("participant", p.getFirstname() + " " + p.getName());
		} else {
			_hm.put("visibleCheckbox", true);
			_hm.put("isAffected", false);
		}
	}

	private int getNbTaskStartedOrFinishedForAConcreteRole(
			ConcreteActivity _activity,
			ConcreteRoleDescriptor _concreteRoleDescriptor) {
		String state = null;
		int nb = 0;
		SortedSet<ConcreteBreakdownElement> concreteBDE = this.concreteActivityService
				.getConcreteBreakdownElements(_activity);
		for (ConcreteBreakdownElement cbe : concreteBDE) {
			if (!(cbe instanceof ConcreteMilestone)) {
				if (cbe instanceof ConcreteWorkBreakdownElement) {
					// if this is a task -> compare affected
					// role
					if (cbe instanceof ConcreteTaskDescriptor) {

						// if the task is affected to a
						// role
						ConcreteRoleDescriptor crd = ((ConcreteTaskDescriptor) cbe)
								.getMainConcreteRoleDescriptor();
						if (crd != null) {
							// if it is the same
							// role
							if (crd.getId().equals(
									_concreteRoleDescriptor.getId())) {
								state = ((ConcreteTaskDescriptor) cbe)
										.getState();
								if (!state.equals("Created")
										&& !state.equals("Ready")) {
									nb++;
								}
							}
						}
					}
				}
			}
		}
		return nb;
	}

	/**
	 * put the attribut project modified to true which force the project table
	 * to be recalculated
	 * 
	 */
	public void refreshProjectTable() {
		this.projectModified = true;
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
			isExpanded.put(elementId, false);
			b = isExpanded.get(elementId);
		}
		b = !b;
		isExpanded.put(elementId, b);

		// add sub elements to list
		if (b) {
			expandNodeAction();
		}
		// remove items from list
		else {
			contractNodeAction();
		}
	}

	/**
	 * Toggles the expanded state of this ConcreteBreakDownElement.
	 * 
	 * @param event
	 */
	public void toggleSubGroupActionToDelete(ActionEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");

		// toggle expanded state
		Boolean b = isExpanded.get(elementId);
		if (b == null) {
			isExpanded.put(elementId, false);
			b = isExpanded.get(elementId);
		}
		b = !b;
		isExpanded.put(elementId, b);

		// add sub elements to list
		if (b) {
			expandNodeActionToDelete();
		}
		// remove items from list
		else {
			contractRoleNodeActionToDelete();
		}
	}

	/**
	 * Toggles the expanded state of this ConcreteBreakDownElement.
	 * 
	 * @param event
	 */
	public void toggleRoleSubGroupAction(ActionEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");

		// toggle expanded state
		Boolean b = isExpanded.get(elementId);
		if (b == null) {
			isExpanded.put(elementId, false);
			b = isExpanded.get(elementId);
		}
		b = !b;
		isExpanded.put(elementId, b);

		// add sub elements to list
		if (b) {
			expandRoleNodeAction();
		}
		// remove items from list
		else {
			contractRoleNodeAction();
		}
	}

	/**
	 * Utility method to add all child nodes to the parent dataTable list.
	 */
	@SuppressWarnings("unchecked")
	private void expandNodeAction() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");

		// the table will be expanded so, indentation string
		this.needIndentation = true;

		ArrayList<Object> tmp = new ArrayList<Object>();
		tmp.addAll(this.displayContent);
		int index;

		for (Iterator iter = tmp.iterator(); iter.hasNext();) {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm = (HashMap<String, Object>) iter.next();

			if (hm.get("id").equals(elementId)) {
				if (hm.get("nodeType").equals("node")) {
					hm.put("expansionImage", EXPAND_TABLE_ARROW);
					ConcreteActivity ca = this.concreteActivityService
							.getConcreteActivity((String) hm.get("id"));
					index = this.displayContent.indexOf(hm);
					List<HashMap<String, Object>> lst = this
							.retrieveHierarchicalItems(ca);
					if (lst != null) {
						this.displayContent.addAll(index + 1, lst);
					} else {
						this.displayContent.clear();
					}
					return;
				}
			}
		}
	}

	/**
	 * Utility method to add all child nodes to the parent dataTable list.
	 */
	@SuppressWarnings("unchecked")
	private void expandNodeActionToDelete() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");

		this.expandRoleNodeActionToDelete(elementId, displayRoleContentToDelete);
	}

	/**
	 * Utility method to remove all child nodes from the parent dataTable list.
	 */
	private void contractNodeAction() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");

		ArrayList<HashMap<String, Object>> parentList = new ArrayList<HashMap<String, Object>>();
		parentList.addAll(this.displayContent);

		/*
		 * Removes element which we want to contract from the parent list
		 */
		for (HashMap<String, Object> currentElement : this.displayContent) {

			if (currentElement.get("id").equals(elementId)
					&& currentElement.get("nodeType").equals("node")) {
				currentElement.put("expansionImage",
						ProjectAdvancementBean.CONTRACT_TABLE_ARROW);
				parentList.remove(currentElement);
			}
		}
		this.deleteChildren(elementId, parentList);
	}

	/**
	 * Utility method to add all child nodes to the parent dataTable list.
	 */
	@SuppressWarnings("unchecked")
	private void expandRoleNodeAction() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");
		this.expandRoleNodeAction(elementId,displayRoleContent);
	}

	/**
	 * Utility method to remove all child nodes from the parent dataTable list.
	 */
	private void contractRoleNodeActionToDelete() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");

		ArrayList<HashMap<String, Object>> parentList = new ArrayList<HashMap<String, Object>>();
		parentList.addAll(this.displayRoleContentToDelete);

		/*
		 * Removes element which we want to contract from the parent list
		 */
		for (HashMap<String, Object> currentElement : this.displayRoleContentToDelete) {

			if (currentElement.get("id").equals(elementId)
					&& currentElement.get("nodeType").equals("node")) {
				currentElement.put("expansionImage",
						ProjectAdvancementBean.CONTRACT_TABLE_ARROW);
				parentList.remove(currentElement);
			}
		}
		this.deleteChildrenToDelete(elementId, parentList);
	}

	/**
	 * Utility method to remove all child nodes from the parent dataTable list.
	 */
	private void contractRoleNodeAction() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");

		ArrayList<HashMap<String, Object>> parentList = new ArrayList<HashMap<String, Object>>();
		parentList.addAll(this.displayRoleContent);

		/*
		 * Removes element which we want to contract from the parent list
		 */
		for (HashMap<String, Object> currentElement : this.displayRoleContent) {

			if (currentElement.get("id").equals(elementId)
					&& currentElement.get("nodeType").equals("node")) {
				currentElement.put("expansionImage",
						ProjectAdvancementBean.CONTRACT_TABLE_ARROW);
				parentList.remove(currentElement);
			}
		}
		this.deleteRoleChildren(elementId, parentList);
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
	public void deleteChildren(String _parentId,
			ArrayList<HashMap<String, Object>> parentList) {
		for (HashMap<String, Object> child : parentList) {
			if (child.get("parentId").equals(_parentId)) {
				this.displayContent.remove(child);
				deleteChildren((String) child.get("id"), parentList);
			}
			if (child.get("id").equals(_parentId)) {
				child.put("expansionImage",
						ProjectAdvancementBean.CONTRACT_TABLE_ARROW);
				this.isExpanded.put((String) child.get("id"), false);
			}
		}
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
	public void deleteRoleChildren(String _parentId,
			ArrayList<HashMap<String, Object>> parentList) {
		for (HashMap<String, Object> child : parentList) {
			if (child.get("parentId").equals(_parentId)) {
				this.displayRoleContent.remove(child);
				deleteRoleChildren((String) child.get("id"), parentList);
			}
			if (child.get("id").equals(_parentId)) {
				child.put("expansionImage",
						ProjectAdvancementBean.CONTRACT_TABLE_ARROW);
				this.isExpanded.put((String) child.get("id"), false);
			}
		}
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
	public void deleteChildrenToDelete(String _parentId,
			ArrayList<HashMap<String, Object>> parentList) {
		for (HashMap<String, Object> child : parentList) {
			if (child.get("parentId").equals(_parentId)) {
				this.displayRoleContentToDelete.remove(child);
				deleteChildrenToDelete((String) child.get("id"), parentList);
			}
			if (child.get("id").equals(_parentId)) {
				child.put("expansionImage",
						ProjectAdvancementBean.CONTRACT_TABLE_ARROW);
				this.isExpanded.put((String) child.get("id"), false);
			}
		}
	}

	/**
	 * Add advancement details of the concrete breakdown element The concrete
	 * breakdown element is a task
	 * 
	 * @param _hm
	 * @param _concreteBreakdownElement
	 */
	private void advancementDetailsOfATask(HashMap<String, Object> _hm,
			ConcreteBreakdownElement _concreteBreakdownElement) {
		_hm.put("expansionImage", TABLE_LEAF);

		_hm
				.put(
						"styleAdvancement",
						getStyleAdvancement((ConcreteTaskDescriptor) _concreteBreakdownElement));

		_hm.put("plannedStartingDate",
				((ConcreteWorkBreakdownElement) _concreteBreakdownElement)
						.getPlannedStartingDate());
		_hm.put("plannedFinishingDate",
				((ConcreteWorkBreakdownElement) _concreteBreakdownElement)
						.getPlannedFinishingDate());

		_hm.put("taskPlannedUserFinish",
				((ConcreteWorkBreakdownElement) _concreteBreakdownElement)
						.getPlannedUserFinishingDate());

		_hm.put("taskRealStartingDate",
				((ConcreteTaskDescriptor) _concreteBreakdownElement)
						.getRealStartingDate());
		_hm.put("taskRealFinishingDate",
				((ConcreteTaskDescriptor) _concreteBreakdownElement)
						.getRealFinishingDate());

		_hm.put("styleStarting", getStyle(
				(ConcreteTaskDescriptor) _concreteBreakdownElement, "Start"));

		if ((((ConcreteTaskDescriptor) _concreteBreakdownElement)
				.getRealFinishingDate()) != null) {
			_hm.put("styleisFinish", getStyle(
					(ConcreteTaskDescriptor) _concreteBreakdownElement,
					"Finish"));
		} else {
			_hm.put("styleFinish", getStyle(
					(ConcreteTaskDescriptor) _concreteBreakdownElement,
					"Finish"));
		}
		_hm.put("plannedTime",
				((ConcreteWorkBreakdownElement) _concreteBreakdownElement)
						.getPlannedTime());
		_hm.put("accomplishedTime",
				((ConcreteTaskDescriptor) _concreteBreakdownElement)
						.getAccomplishedTime());

		// remainign time : if not started, plannedTime
		if (((ConcreteTaskDescriptor) _concreteBreakdownElement)
				.getRealStartingDate() != null)
			_hm.put("remainingTime",
					((ConcreteTaskDescriptor) _concreteBreakdownElement)
							.getRemainingTime());
		else
			_hm.put("remainingTime",
					((ConcreteWorkBreakdownElement) _concreteBreakdownElement)
							.getPlannedTime());

		_hm
				.put(
						"consomme",
						Math
								.round(activityConsommeCalculation((ConcreteTaskDescriptor) _concreteBreakdownElement)));

		_hm
				.put(
						"style",
						outputStyleForConsomme(activityConsommeCalculation((ConcreteTaskDescriptor) _concreteBreakdownElement)));
	}

	/**
	 * The concrete breakdown element is a task We must display a leaf
	 * 
	 * @param _hm
	 * @param _concreteBreakdownElement
	 * @param _role
	 * @param _affected
	 * @param _b
	 * @param _parti
	 * @param _crd
	 * @param _selectedMode
	 */
	private void displayALeaf(HashMap<String, Object> _hm,
			ConcreteBreakdownElement _concreteBreakdownElement, String _role,
			String _affected, boolean _b, Participant _parti,
			ConcreteRoleDescriptor _crd) {
		// Set as a leaf
		_hm.put("nodeType", "leaf");
		_hm.put("expansionImage", TABLE_LEAF);

		// Set advancement information
		this.advancementDetailsOfATask(_hm, _concreteBreakdownElement);
		if (_crd.getConcreteName() != "") {
			_hm.put("role", _crd);
			_hm.put("roleName", _crd.getConcreteName());
			boolean b1 = false;
			if (!_role.equals("projectDirector")) {
				_affected = isAffected(_parti, _crd);
				if (_affected.equals("Affected")
						|| _affected.equals("OtherAffected")) {
					_b = true;
				} else if (_affected.equals("NotAffected")) {
					_b = false;
				}
				b1 = _b;
			}
			_hm.put("isAffected", b1);
			_hm.put("flagAffect", _hm.get("isAffected"));
			//_hm.put("visibleCheckbox", true);
		} else {
			_hm.put("roleName", LocaleBean.getText("tabAffectRole.anyRole"));
			//_hm.put("visibleCheckbox", false);
		}

		if (((ConcreteTaskDescriptor) _concreteBreakdownElement).getState()
				.equals("Created")
				|| ((ConcreteTaskDescriptor) _concreteBreakdownElement)
						.getState().equals("Ready")) {
			if (_affected.equals("OtherAffected")) {
				_hm.put("enabled", true);
				_hm.put("visibleCheckbox", false);
			} else {
				_hm.put("visibleCheckbox", true);
				_hm.put("enabled", false);
			}
		} else {
			// task started ou finished
			if (_affected.equals("OtherAffected")) {
				_hm.put("enabled", false);
				_hm.put("visibleCheckbox", false);
			} else {
				_hm.put("enabled", true);
				_hm.put("visibleCheckbox", true);
				_hm.put("isAffected", true);
			}
			
		}

		// if the task is affected to a role
		if (((ConcreteTaskDescriptor) _concreteBreakdownElement)
				.getMainConcreteRoleDescriptor() != null) {
			// if the role is already affected to a participant
			if (((ConcreteTaskDescriptor) _concreteBreakdownElement)
					.getMainConcreteRoleDescriptor().getParticipant() != null) {
				_hm
						.put(
								"participant",
								((ConcreteTaskDescriptor) _concreteBreakdownElement)
										.getMainConcreteRoleDescriptor()
										.getParticipant().getFirstname()
										+ " "
										+ ((ConcreteTaskDescriptor) _concreteBreakdownElement)
												.getMainConcreteRoleDescriptor()
												.getParticipant().getName());
			} else {
				_hm
						.put(
								"participant",
								LocaleBean
										.getText("component.project.projectadvancement.nobody"));
			}
		} else {
			_hm.put("participant", LocaleBean
					.getText("component.project.projectadvancement.nobody"));
		}
	}

	/**
	 * Recursive method returning a hashmap data model which contains the
	 * elements required to display the project advancement table Give a
	 * ConcreteActivity to this method, return its first hierarchical children
	 * described into the hashmap
	 * 
	 */
	private List<HashMap<String, Object>> retrieveHierarchicalItems(
			ConcreteActivity _concreteActivity) {
		Double currentAdvancedTime;
		String indentationString = "";
		String affected = "";
		boolean b = false;
		List<HashMap<String, Object>> subConcretesContent = new ArrayList<HashMap<String, Object>>();
		String userId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);
		String role = (String) WebSessionService
				.getAttribute(WebSessionService.ROLE_TYPE);
		Participant parti;
		ConcreteRoleDescriptor crd = null;

		if (!role.equals("projectDirector")) {
			parti = this.getParticipantService().getParticipant(userId);
		} else {
			parti = null;
		}
		// for every child of the activity
		if (_concreteActivity == null) {
			return null;
		}
		SortedSet<ConcreteBreakdownElement> concreteBDE = this.concreteActivityService
				.getConcreteBreakdownElements(_concreteActivity);

		for (ConcreteBreakdownElement concreteBreakdownElement : concreteBDE) {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			if (!(concreteBreakdownElement instanceof ConcreteMilestone)) {
				if (concreteBreakdownElement instanceof ConcreteWorkBreakdownElement) {
					// if this is a task -> display a leaf
					if (concreteBreakdownElement instanceof ConcreteTaskDescriptor) {
						crd = getRoleForTask((ConcreteTaskDescriptor) concreteBreakdownElement);
						if (crd.getConcreteName() != ""
								&& !role.equals("projectDirector"))
							affected = isAffected(parti, crd);
						if (this.getSelectedMode().equals(ALL_ROLE_MODE)
								|| crd.getConcreteName().equals("")
								|| this.getSelectedMode().equals(MY_ROLE_MODE)
								&& affected.equals("Affected")) {
							this.displayALeaf(hm, concreteBreakdownElement,
									role, affected, b, parti, crd);
						}
					}
					// if this is not a task -> display a
					// node
					else {
						hm.put("visibleCheckbox", false);
						hm.put("nodeType", "node");
						hm.put("expansionImage", CONTRACT_TABLE_ARROW);
						hm.put("accomplishedTime", "");
						hm.put("remainingTime", "");
						hm.put("participant", "");
						hm.put("plannedStartingDate",
								((ConcreteActivity) concreteBreakdownElement)
										.getPlannedStartingDate());
						hm.put("plannedFinishingDate",
								((ConcreteActivity) concreteBreakdownElement)
										.getPlannedFinishingDate());
						hm.put("plannedTime",
								((ConcreteActivity) concreteBreakdownElement)
										.getPlannedTime());
					}
					if (this.getSelectedMode().equals(ALL_ROLE_MODE)
							|| this.getSelectedMode().equals(MY_ROLE_MODE)
							&& (affected.equals("Affected") || affected
									.equals(""))) {
						// advancement consolidation
						// time calculation
						currentAdvancedTime = this
								.activityAdvancementCalculation(concreteBreakdownElement);
						if (currentAdvancedTime == null) {
							hm.put("advancementTime", 0);
						} else {
							hm.put("advancementTime", Math
									.round(currentAdvancedTime));
						}
						hm.put("id", concreteBreakdownElement.getId());
						hm.put("concreteName", concreteBreakdownElement
								.getConcreteName());
						hm.put("parentId", _concreteActivity.getId());
						subConcretesContent.add(hm);

						// if this is not the root node
						// -> needIndentation ==
						// true
						if (needIndentation) {
							if (this.indentationContent.get(_concreteActivity
									.getId()) != null) {
								indentationString = this.indentationContent
										.get(_concreteActivity.getId());
							}
							this.indentationContent
									.put(
											(String) hm.get("id"),
											indentationString
													.concat(ProjectAdvancementBean.INDENTATION_STRING));
						}
						hm.put("isEditable", new Boolean(false));
						hm
								.put(
										"isStarted",
										isConcreteBreakdownElementStarted(concreteBreakdownElement));
					}
				}
			}
		}
		return subConcretesContent;
	}

	/**
	 * Return the advancement in percents of a Concrete breakdown element
	 * 
	 * @param cbe
	 * @return Double the percents advancement
	 */
	public Double activityAdvancementCalculation(ConcreteBreakdownElement cbe) {
		Double result = 0.0;
		HashMap<String, Double> couple = this.taskAdvancementCalculation(cbe);
		double advancementRatio = couple.get("advancementRatio");
		if (advancementRatio > 0) {
			result = advancementRatio * 100;
			if (cbe instanceof ConcreteActivity) {
				ConcreteActivity ca = (ConcreteActivity) cbe;
				result = result / couple.get("nbConcreteTaskDescriptor");
			}
		} else {
			result = null;
		}
		return result;
	}

	/**
	 * Return the consume in percents of a Concrete breakdown element
	 * 
	 * @param cbe
	 * @return Double the percents consume
	 */
	public Double activityConsommeCalculation(ConcreteTaskDescriptor ctd) {
		Double result = 0.0;
		double EstimateTimes = 0.0;
		double accomplishedTimes = 0.0;
		accomplishedTimes = ctd.getAccomplishedTime();
		EstimateTimes = ctd.getPlannedTime();

		if (EstimateTimes == 0.0) {
			result = 0.0;
		} else {
			result = accomplishedTimes / EstimateTimes;
			result = result * 100;
		}
		return result;
	}

	/**
	 * Sub recursive method used for the Concrete breakdown element advancement
	 * calculation
	 * 
	 * @param ctd
	 * @return HashMap
	 */
	private HashMap<String, Double> taskAdvancementCalculation(
			ConcreteBreakdownElement cbe) {
		HashMap<String, Double> coupletmp = new HashMap<String, Double>();
		HashMap<String, Double> couple = new HashMap<String, Double>();
		couple.put("advancementRatio", 0.0);
		couple.put("nbConcreteTaskDescriptor", 0.0);
		// if the current element is an activity, parse the sub concrete
		// breakdown elements
		if (cbe instanceof ConcreteActivity) {
			ConcreteActivity ca = (ConcreteActivity) cbe;
			int i = 0;
			for (ConcreteBreakdownElement concreteBreakdownElement : this.concreteActivityService
					.getConcreteBreakdownElements(ca)) {
				if (concreteBreakdownElement instanceof ConcreteTaskDescriptor) {
					i++;
				}
				coupletmp = this
						.taskAdvancementCalculation(concreteBreakdownElement);
				couple.put("advancementRatio", couple.get("advancementRatio")
						+ coupletmp.get("advancementRatio"));
			}
			if (coupletmp.get("nbConcreteTaskDescriptor") != null) {
				couple.put("nbConcreteTaskDescriptor", i
						+ coupletmp.get("nbConcreteTaskDescriptor"));
			} else {
				couple.put("nbConcreteTaskDescriptor", Double
						.parseDouble(String.valueOf(i)));
			}
		}
		// else if it's a concrete task get the values
		else {
			if (cbe instanceof ConcreteTaskDescriptor) {
				ConcreteTaskDescriptor ctd = (ConcreteTaskDescriptor) cbe;
				float remainingTime = ctd.getRemainingTime();
				float accomplishedTime = ctd.getAccomplishedTime();
				if (remainingTime != 0 || accomplishedTime != 0) {
					couple.put("advancementRatio",
							(double) (ctd.getAccomplishedTime() / (ctd
									.getRemainingTime() + ctd
									.getAccomplishedTime())));
				}
			}
		}
		return couple;
	}

	/**
	 * Setter of displayContent.
	 * 
	 * @param _displayContent
	 *            The displayContent to set.
	 */
	public void setDisplayContent(
			ArrayList<HashMap<String, Object>> _displayContent) {
		this.displayContent = _displayContent;
	}

	/**
	 * Setter of displayRoleContent.
	 * 
	 * @param _displayContent
	 *            The displayContent to set.
	 */
	public void setDisplayRoleContent(
			ArrayList<HashMap<String, Object>> _displayRoleContent) {
		this.displayRoleContent = _displayRoleContent;
	}

	/**
	 * getter of isExpanded HashMap attribute
	 * 
	 * @return the isExpanded value attribute
	 */
	public HashMap<String, Boolean> getIsExpanded() {
		return this.isExpanded;
	}

	/**
	 * Setter of isExpanded.
	 * 
	 * @param _isExpanded
	 *            The isExpanded to set.
	 */
	public void setIsExpanded(HashMap<String, Boolean> _isExpanded) {
		this.isExpanded = _isExpanded;
	}

	/**
	 * getter of project attribute this attribute is an Project type attribute
	 * 
	 * @return the project
	 */
	public Project getProject() {
		return this.project;
	}

	/**
	 * Setter of project.
	 * 
	 * @param _project
	 *            The project to set.
	 */
	public void setProject(Project _project) {
		this.project = _project;
	}

	/**
	 * this method allow to return the current instance of projectService
	 * 
	 * @return the projectService
	 */
	public ProjectService getProjectService() {
		return this.projectService;
	}

	/**
	 * Setter of projectService.
	 * 
	 * @param _projectService
	 *            The projectService to set.
	 */
	public void setProjectService(ProjectService _projectService) {
		this.projectService = _projectService;
	}

	/**
	 * getter of projectViewerdId String attribute
	 * 
	 * @return the projectViewedId
	 */
	public String getProjectViewedId() {
		return this.projectViewedId;
	}

	/**
	 * Setter of projectViewedId.
	 * 
	 * @param _projectViewedId
	 *            The projectViewedId to set.
	 */
	public void setProjectViewedId(String _projectViewedId) {
		this.projectViewedId = _projectViewedId;
	}

	/**
	 * getter of identationContent HashMap attribute
	 * 
	 * @return the indentationContent
	 */
	public HashMap<String, String> getIndentationContent() {
		return this.indentationContent;
	}

	/**
	 * Setter of indentationContent.
	 * 
	 * @param _indentationContent
	 *            The indentationContent to set.
	 */
	public void setIndentationContent(
			HashMap<String, String> _indentationContent) {
		this.indentationContent = _indentationContent;
	}

	/**
	 * Getter of concreteActivityService.
	 * 
	 * @return the concreteActivityService.
	 */
	public ConcreteActivityService getConcreteActivityService() {
		return this.concreteActivityService;
	}

	/**
	 * Setter of concreteActivityService.
	 * 
	 * @param _concreteActivityService
	 *            The concreteActivityService to set.
	 */
	public void setConcreteActivityService(
			ConcreteActivityService _concreteActivityService) {
		this.concreteActivityService = _concreteActivityService;
	}

	/**
	 * getter of selectedProjectAdvancementView boolean attribute
	 * 
	 * @return the selectedProjectAdvancementView
	 */
	public boolean getSelectedProjectAdvancementView() {
		String user_id = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);

		String projectId = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);

		String role = (String) WebSessionService
				.getAttribute(WebSessionService.ROLE_TYPE);

		if (projectId == null) {
			this.selectedProjectAdvancementView = false;
			return this.selectedProjectAdvancementView;
		}

		this.project = this.projectService.getProject(projectId);
		if (project == null) {
			this.selectedProjectAdvancementView = false;
			return this.selectedProjectAdvancementView;
		}

		this.selectedProjectAdvancementView = false;
		if (this.project.getProjectManager() != null) {
			if (this.project.getProjectManager().getId().equals(user_id)) {
				this.selectedProjectAdvancementView = true;
			}
		}
		if (role.equals("projectDirector")) {
			this.selectedProjectAdvancementView = true;
		}

		return this.selectedProjectAdvancementView;
	}

	/**
	 * Setter of selectedProjectAdvancementView.
	 * 
	 * @param _selectedProjectAdvancementView
	 *            The selectedProjectAdvancementView to set.
	 */
	public void setSelectedProjectAdvancementView(
			boolean _selectedProjectAdvancementView) {
		this.selectedProjectAdvancementView = _selectedProjectAdvancementView;
	}

	/**
	 * The method return information to know if a project is instanciated or
	 * not.
	 * 
	 * @return String
	 */
	public String getProjectInstanciated() {
		if (this.project == null) {
			this.projectInstanciated = "projectNotSelected";
		} else if (this.project.getProcess() == null) {
			this.projectInstanciated = "projectNotInstanciated";
		} else {
			this.projectInstanciated = "projectInstanciated";
		}
		return this.projectInstanciated;
	}

	/**
	 * setter of projectInstanciated String attribute
	 * 
	 * @param String
	 *            projectInstanciated
	 */
	public void setProjectInstanciated(String projectInstanciated) {
		this.projectInstanciated = projectInstanciated;
	}

	/**
	 * methods who return a boolean. this method allow to now if a task is
	 * already started or not in order to blocking edit of estimate
	 * 
	 * @param _concreteBreakdownElement
	 * @return boolean
	 */
	public int getIndentationPosition() {
		int tmppos = this.pos;
		this.pos = this.pos + 1;
		return tmppos;
	}

	/**
	 * 
	 * Editing an estimate
	 * 
	 * @param e
	 *            event received when a user clicks on edit button in the
	 *            datatable
	 */
	public void editEstimate() {
		/*
		 * String taskPosition = (String) FacesContext.getCurrentInstance()
		 * .getExternalContext().getRequestParameterMap().get( "taskPosition");
		 * this.displayContent.get(Integer.parseInt(taskPosition)).put(
		 * "isEditable", true);
		 */

		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String id = (String) map.get("idCbde");
		ConcreteBreakdownElement cbde = this.getConcreteActivityService()
				.getConcreteActivity(id);
		this.cbdeId = id;
		if (cbde != null) {
			// it's an ativity
			ConcreteActivity ca = (ConcreteActivity) cbde;
			this.idActivity = ca.getId();
			this.concreteName = ca.getConcreteName();
			this.startingDate = ca.getPlannedStartingDate();
			this.finishingDate = ca.getPlannedFinishingDate();
			this.plannedTime = ca.getPlannedTime();
		} else {
			// maybe it's a concreteTaskDescriptor
			cbde = this.concreteTaskDescriptorService
					.getConcreteTaskDescriptor(id);
			if (cbde != null) {
				// it's a concrete task descriptor
				ConcreteTaskDescriptor ctd = (ConcreteTaskDescriptor) cbde;
				this.idActivity = ctd.getId();
				this.concreteName = ctd.getConcreteName();
				this.startingDate = ctd.getPlannedStartingDate();
				this.finishingDate = ctd.getPlannedFinishingDate();
				this.plannedTime = ctd.getPlannedTime();
			}
		}
	}

	/**
	 * methods who return a boolean. this method allow to now if a task is
	 * already started or not in order to blocking edit of estimate
	 * 
	 * @param _concreteBreakdownElement
	 * @return boolean
	 */
	public boolean isConcreteBreakdownElementStarted(
			ConcreteBreakdownElement _concreteBreakdownElement) {
		ConcreteTaskDescriptor ctd = new ConcreteTaskDescriptor();
		ConcreteActivity ca = new ConcreteActivity();
		if (_concreteBreakdownElement instanceof ConcreteTaskDescriptor) {
			ctd = (ConcreteTaskDescriptor) _concreteBreakdownElement;
			// System.out.println("nom de la tache : " +
			// ctd.getConcreteName()
			// + "\n");
			// System.out.println("etat : ");
			if ((ctd.getState().equals("Created"))
					|| (ctd.getState().equals("Ready"))) {
				// System.out.println("creer ou en cours \n");
				return false;
			} else {
				// System.out.println("pas commencer ou fini
				// \n");
				return true;
			}
		}
		if (_concreteBreakdownElement instanceof ConcreteActivity) {
			ca = (ConcreteActivity) _concreteBreakdownElement;
			// System.out.println("nom de la tache : " +
			// ca.getConcreteName()
			// + "\n");
			// System.out.println("etat : ");
			if (ca.getState().equals("Finished")) {
				// System.out.println("fini \n");
				return true;
			} else {
				// System.out.println("pas commencer, creer ou
				// en cours \n");
				return false;
			}
		}
		// System.out.println("etat : probleme");

		return false;
	}

	/**
	 * 
	 * method returning a formated string to apply a specific style in an
	 * ice:outputText tag
	 * 
	 * @param _consomme
	 * @return String
	 */

	public String outputStyleForConsomme(Double _consomme) {
		String result = "";
		if (_consomme > 100) {
			result = "color:red;font-weight: bold;";
		} else {
			result = "";
		}
		return result;
	}

	public void cancelModifyEstimate() {
		// go to the page with all estimate
		WebCommonService.changeContentPage("tabProjectEstimate");
	}

	/**
	 * 
	 * Save an estimate
	 * 
	 * @param e
	 *            event received when a user clicks on save button in the
	 *            datatable
	 */
	public void saveEstimate() {
		if (this.plannedTime != null && this.plannedTime < 0) {
			// error the estimate must be > 0
			WebCommonService
					.addErrorMessage(LocaleBean
							.getText("component.project.projectEstimate.plannedTimeError"));
		} else if (this.finishingDate != null && this.startingDate == null
				&& !verifyPlannedFinishingDate()) {
			// error date are not ok in function of task dependencies
			WebCommonService
					.addErrorMessage(LocaleBean
							.getText("component.project.projectEstimate.plannedFinishedDateError"));
		} else if (this.startingDate != null && this.finishingDate == null
				&& !verifyPlannedStartingDate()) {
			// error date are not ok in function of task dependencies
			WebCommonService
					.addErrorMessage(LocaleBean
							.getText("component.project.projectEstimate.plannedStartedDateError"));
		} else if (this.startingDate != null && this.finishingDate != null
				&& !verifyPlannedStartingDate() && verifyPlannedFinishingDate()) {
			// error date are not ok in function of task dependencies
			WebCommonService
					.addErrorMessage(LocaleBean
							.getText("component.project.projectEstimate.plannedStartedDateError"));
		} else if (this.startingDate != null && this.finishingDate != null
				&& verifyPlannedStartingDate() && !verifyPlannedFinishingDate()) {
			// error date are not ok in function of task dependencies
			WebCommonService
					.addErrorMessage(LocaleBean
							.getText("component.project.projectEstimate.plannedFinishedDateError"));
		} else if (this.finishingDate != null && this.startingDate != null) {
			if (!verifyPlannedFinishingDate() || !verifyPlannedStartingDate()) {
				// error date are not ok in function of task dependencies
				WebCommonService
						.addErrorMessage(LocaleBean
								.getText("component.project.projectEstimate.plannedDateError"));
			} else if (!this.finishingDate.before(this.startingDate)) {
				ConcreteBreakdownElement cbde = this.concreteActivityService
						.getConcreteActivity(this.cbdeId);
				if (cbde != null) {
					// it's a concreteActivity
					ConcreteActivity ca = (ConcreteActivity) cbde;
					ca.setPlannedStartingDate(this.startingDate);
					ca.setPlannedFinishingDate(this.finishingDate);
					ca.setPlannedTime(this.plannedTime);
					this.concreteActivityService.saveConcreteActivity(ca);

				} else {
					cbde = this.concreteTaskDescriptorService
							.getConcreteTaskDescriptor(this.cbdeId);
					if (cbde != null) {
						// it's a concreteTaskDescriptor
						ConcreteTaskDescriptor ctd = (ConcreteTaskDescriptor) cbde;
						ctd.setPlannedStartingDate(this.startingDate);
						ctd.setPlannedFinishingDate(this.finishingDate);
						ctd.setPlannedTime(this.plannedTime);
						this.concreteTaskDescriptorService
								.saveConcreteTaskDescriptor(ctd);
					}
				}

				// refresh the tab
				this.projectModified = true;
				WebCommonService
						.addInfoMessage(LocaleBean
								.getText("component.project.projectEstimate.updateAffected"));
				// go to the page with all estimate
				WebCommonService.changeContentPage("tabProjectEstimate");
			} else {
				// error the finishing date must be after the started date
				WebCommonService
						.addErrorMessage(LocaleBean
								.getText("component.project.projectEstimate.updateError"));
			}
		} else {
			ConcreteBreakdownElement cbde = this.concreteActivityService
					.getConcreteActivity(this.cbdeId);
			if (cbde != null) {
				// it's a concreteActivity
				ConcreteActivity ca = (ConcreteActivity) cbde;
				ca.setPlannedStartingDate(this.startingDate);
				ca.setPlannedFinishingDate(this.finishingDate);
				ca.setPlannedTime(this.plannedTime);
				this.concreteActivityService.saveConcreteActivity(ca);

			} else {
				cbde = this.concreteTaskDescriptorService
						.getConcreteTaskDescriptor(this.cbdeId);
				if (cbde != null) {
					// it's a concreteTaskDescriptor
					ConcreteTaskDescriptor ctd = (ConcreteTaskDescriptor) cbde;
					ctd.setPlannedStartingDate(this.startingDate);
					ctd.setPlannedFinishingDate(this.finishingDate);
					ctd.setPlannedTime(this.plannedTime);
					this.concreteTaskDescriptorService
							.saveConcreteTaskDescriptor(ctd);
				}
			}

			// refresh the tab
			this.projectModified = true;
			WebCommonService
					.addInfoMessage(LocaleBean
							.getText("component.project.projectEstimate.updateAffected"));
			// go to the page with all estimate
			WebCommonService.changeContentPage("tabProjectEstimate");
		}

	}

	/**
	 * this method return a concreteRoledescriptor link to the given
	 * concreteTaskDescriptor
	 * 
	 * @param _ctd
	 *            the given concreteTaskDescriptor
	 * @return the wanted ConcreteRoleDescriptor
	 */
	public ConcreteRoleDescriptor getRoleForTask(ConcreteTaskDescriptor _ctd) {
		if (_ctd != null) {

			TaskDescriptorService tdService = this.concreteTaskDescriptorService
					.getTaskDescriptorService();
			RoleDescriptorService rdService = this.concreteTaskDescriptorService
					.getRoleDescriptorService();
			ConcreteRoleDescriptorService crdService = this.concreteTaskDescriptorService
					.getConcreteRoleDescriptorService();

			TaskDescriptor tmp = _ctd.getTaskDescriptor();
			RoleDescriptor tmpRoleDescriptor;
			TaskDescriptor tmp2 = tdService.getTaskDescriptorById(tmp.getId());

			tmpRoleDescriptor = tmp2.getMainRole();

			if (tmpRoleDescriptor != null) {
				RoleDescriptor rd = rdService
						.getRoleDescriptor(tmpRoleDescriptor.getId());
				List<ConcreteRoleDescriptor> listeRd = crdService
						.getAllConcreteRoleDescriptorForARoleDescriptor(rd
								.getId());
				if (listeRd.size() > 0) {
					Iterator<ConcreteRoleDescriptor> ite = listeRd.iterator();
					ConcreteRoleDescriptor crd = ite.next();
					// if(crd==null)
					while (crd == null || ite.hasNext()) {
						crd = ite.next();
					}
					return crd;
				}
			}
		}
		return new ConcreteRoleDescriptor();
	}

	/**
	 * this method allow to return a string. the string indicate if the given
	 * participant is already affected or not to the given
	 * concreteRoleDescriptor this method also indicate if another participant
	 * than the given participant is affected to the given
	 * concreteRoleDescriptor
	 * 
	 * @param _parti
	 * @param _concreteRoleDescriptor
	 * @return String
	 */

	public String isAffected(Participant _parti,
			ConcreteRoleDescriptor _concreteRoleDescriptor) {

		ConcreteRoleDescriptorService crdService = this.concreteTaskDescriptorService
				.getConcreteRoleDescriptorService();
		Participant parti = crdService.getParticipant(_concreteRoleDescriptor);
		if (parti != null) {
			if (parti.getId().equals(_parti.getId())) {
				return "Affected";
			} else {
				return "OtherAffected";
			}

		} else {
			return "NotAffected";
		}

	}

	/**
	 * this method allow to return the current instance of participantService
	 * 
	 * @return ParticipantService
	 */
	public ParticipantService getParticipantService() {
		return participantService;
	}

	/**
	 * this method allow to set the current instance of participantService
	 * 
	 * @param _participantService
	 */
	public void setParticipantService(ParticipantService _participantService) {
		participantService = _participantService;
	}

	/**
	 * this method allow to save the current user role affectation through the
	 * role affectation panel
	 */

	public void saveAffectRole() {
		String wilosUserId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);

		ParticipantService pService = this.getParticipantService();
		Participant parti = pService.getParticipant(wilosUserId);

		ConcreteRoleDescriptorService crdService = this.concreteTaskDescriptorService
				.getConcreteRoleDescriptorService();
		//permet de savoir si tout les enregistrements se sont bien passés
		boolean reg = true;
		for (HashMap<String, Object> ligne : this.displayRoleContent) {
			if ((Boolean) ligne.get("visibleCheckbox")) {
				if (!(Boolean) ligne.get("enabled")) {
					ConcreteRoleDescriptor crd = (ConcreteRoleDescriptor) ligne
							.get("role");
					if (crd != null) {
						// affectation
						if ((Boolean) ligne.get("isAffected")
								&& !(Boolean) ligne.get("flagAffect")) {
							crd = crdService.addPartiConcreteRoleDescriptor(crd, parti);
							//si le parti est différent ou que le rôle a été supprimé alors message d'erreur 
							if(crd == null || crd.getParticipant() != parti ){
								reg = false;
							}
						}
						// desaffectation
						else if (!(Boolean) ligne.get("isAffected")
								&& (Boolean) ligne.get("flagAffect")) {
							this.unaffectTaskAndWorkProducts(parti, crd);
							crd.setParticipant(null);
							crdService.getConcreteRoleDescriptorDao()
									.saveOrUpdateConcreteRoleDescriptor(crd);
						}
					}else{
						reg = false;
					}
				}
			}
		}
		if(reg){
			WebCommonService.addInfoMessage(LocaleBean
				.getText("tabAffectRole.updateAffected"));
		}else{
			WebCommonService.addErrorMessage(LocaleBean
					.getText("tabAffectRole.updateAffectedFailed"));
		}
		TreeBean tb = (TreeBean) WebCommonService.getBean("TreeBean");
		tb.setSelectedMode(TreeBean.ROLES_MODE);
		tb.refreshProjectTree();
		this.refreshProjectTable();

	}

	/**
	 * Method to set visible the delete popup
	 * 
	 * @param event
	 */
	public void deleteRoles() {
		this.visibleDeleteRolePopup = true;
	}

	/**
	 * this method allow to save the current user role affectation through the
	 * role affectation panel
	 */
	public void deleteAffectRole(ActionEvent _event) {
		// permet de savoir si les suppressions se sont bien passées
		boolean reg = true;
		
		ConcreteRoleDescriptorService crdService = this.concreteTaskDescriptorService
				.getConcreteRoleDescriptorService();
		for (HashMap<String, Object> ligne : this.displayRoleContentToDelete) {
			if ((Boolean) ligne.get("visibleCheckbox")) {
				if (!(Boolean) ligne.get("enabled")) {
					ConcreteRoleDescriptor crd = (ConcreteRoleDescriptor) ligne
							.get("role");

					if (crd != null) {
						// delete selected roles
						if ((Boolean) ligne.get("isAffected")) {
							crd = crdService.deleteConcreteRoleDescriptor(crd);
							if (crd != null) {
								reg = false;
							}
						}
					}
				}
			}
		}
		if (reg) {
			WebCommonService.addInfoMessage(LocaleBean
					.getText("tabDeleteRole.delete.done"));
		} else {
			WebCommonService.addErrorMessage(LocaleBean
					.getText("tabDeleteRole.delete.done.failed"));
		}
		TreeBean tb = (TreeBean) WebCommonService.getBean("TreeBean");
		tb.setSelectedMode(TreeBean.ROLES_MODE);
		tb.refreshProjectTree();
		this.refreshProjectTable();
		this.visibleDeleteRolePopup = false;

	}

	private void unaffectTaskAndWorkProducts(Participant _parti,
			ConcreteRoleDescriptor _crd) {

		List<ConcreteTaskDescriptor> lst_ctd = this.concreteTaskDescriptorService
				.getAllConcreteTaskDescriptorsForProject(this.project.getId());
		// for all task -> compare affected role
		for (ConcreteTaskDescriptor ctd : lst_ctd) {
			// if the task is affected to a
			// role
			ConcreteRoleDescriptor crd = ((ConcreteTaskDescriptor) ctd)
					.getMainConcreteRoleDescriptor();
			if (crd != null) {
				// if it is the same role
				if (crd.getId().equals(_crd.getId())) {
					// the actual task have the concrete role descriptor _crd
					((ConcreteTaskDescriptor) ctd)
							.setMainConcreteRoleDescriptor(null);
					this.concreteTaskDescriptorService
							.saveConcreteTaskDescriptor(((ConcreteTaskDescriptor) ctd));
				}
			}
		}
		List<ConcreteWorkProductDescriptor> lst_cwpd = this.concreteWorkProductDescriptorService
				.getAllConcreteWorkProductDescriptorsForProject((String) WebSessionService
						.getAttribute(WebSessionService.PROJECT_ID));
		// for all work product -> compare affected role
		for (ConcreteWorkProductDescriptor cwpd : lst_cwpd) {
			if (cwpd instanceof ConcreteWorkProductDescriptor) {
				String pa = cwpd.getParticipant();
				Participant p = this.participantService.getParticipant(pa);
				ConcreteRoleDescriptor crd = cwpd
						.getResponsibleConcreteRoleDescriptor();
				if (crd != null) {
					if (p != null && crd.getId().equals(_crd.getId())) {
						if (p.getId().equals(_parti.getId())) {
							cwpd.setParticipant(null);
							this.concreteWorkProductDescriptorService
									.saveConcreteWorkProductDescriptor(cwpd);
						}
					}
				}
			}
		}
	}

	/**
	 * Update the table when the user arrived by the menu
	 */
	public void updateTable() {
		this.projectModified = true;
	}

	/**
	 * getter of projectSelected boolean attribute
	 * 
	 * @return boolean
	 */
	public boolean getProjectSelected() {
		String projectid = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);
		if (projectid == null || projectid.equals("default")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * this method allow to change the concreteName font style of the given
	 * concreteTaskDescriptor for the given mode parameter
	 * 
	 * @param _ctd
	 * @param _mode
	 * @return String
	 */
	public String getStyle(ConcreteTaskDescriptor _ctd, String _mode) {
		// la date = now
		Date ladate = new Date();
		String style = "";
		if (_mode.equals("Start")) {
			if ((_ctd.getState().equals("Created"))
					|| (_ctd.getState().equals("Ready"))) {
				// the task is created or ready
				if (_ctd.getPlannedStartingDate() != null) {
					if (_ctd.getPlannedStartingDate().before(ladate)) {
						// the task must be started
						style = "color:orange;font-weight: bold;";
					}
					/*
					 * else { //the planned starting date is after now() style =
					 * "color:green;font-weight: bold;"; }
					 */
					if (_ctd.getRealStartingDate() != null) {
						if (_ctd.getRealStartingDate().after(
								_ctd.getPlannedStartingDate())) {
							// the real starting date is after the planned
							// starting date
							// so the task is started in late
							style = "color:red;font-weight: bold;";
						}
					}
				}
			}
		} else {
			// mode != Start
			if ((_ctd.getState().equals("Created"))
					|| (_ctd.getState().equals("Ready"))
					|| (_ctd.getState().equals("Started"))) {
				if (_ctd.getPlannedFinishingDate() != null) {
					if (_ctd.getPlannedFinishingDate().before(ladate)) {
						// the task must be finishing
						style = "color:orange;font-weight: bold;";
					}
				}
			} else if (_ctd.getState().equals("Finished")) {
				if (_ctd.getPlannedFinishingDate() != null
						&& _ctd.getRealFinishingDate() != null) {
					if (_ctd.getPlannedFinishingDate().before(
							_ctd.getRealFinishingDate())) {
						// the planned finishing date is before the real
						// finishing date
						// the task is in late
						style = "color:red;font-weight: bold;";
					}/*
					 * else { style = "foncolor:green;font-weight: bold;"; }
					 */
				}
			}
		}
		return style;
	}

	/**
	 * this method allow to return the Advancement percent column font style
	 * 
	 * @param _ctd
	 *            the given concreteTaskDescriptor
	 * @return String
	 */

	public String getStyleAdvancement(ConcreteTaskDescriptor _ctd) {
		String couleur = new String();
		if (_ctd.getState().equals("Created")) {
			couleur = "color:black;font-weight: bold;";
		} else if (_ctd.getState().equals("Ready")) {
			couleur = "color:green;font-weight: bold;";
		} else if (_ctd.getState().equals("Started")) {
			couleur = "color:orange;font-weight: bold;";
		} else if (_ctd.getState().equals("Finished")) {
			couleur = "color:red;font-weight: bold;";
		}
		return couleur;
	}

	/**
	 * this method allow to return all the available mode for the role
	 * 
	 * @return List<SelectItem> of mode
	 */
	public List<SelectItem> getModesList() {
		ArrayList<SelectItem> modesList = new ArrayList<SelectItem>();
		modesList.add(new SelectItem(ALL_ROLE_MODE, LocaleBean
				.getText("tabAffectRole.checkboxlabel.allRole")));
		modesList.add(new SelectItem(MY_ROLE_MODE, LocaleBean
				.getText("tabAffectRole.checkboxlabel.myRole")));
		return modesList;
	}

	/**
	 * this method allow to change the current concreteWorkProduct mode
	 * 
	 * @param evt
	 */
	public void changeModeActionListener(ValueChangeEvent evt) {
		this.selectedMode = (String) evt.getNewValue();
		if (this.selectedMode.equals(ALL_ROLE_MODE)) {
			WebSessionService.setAttribute(WebSessionService.TREE_MODE,
					ALL_ROLE_MODE);
			refreshProjectTable();
		} else {
			WebSessionService.setAttribute(WebSessionService.TREE_MODE,
					MY_ROLE_MODE);
			refreshProjectTable();
		}
	}

	/**
	 * getter of selectedMode String attribute
	 * 
	 * @return String
	 */
	public String getSelectedMode() {
		return selectedMode;
	}

	/**
	 * setter of selectedMode String attribute
	 * 
	 * @param selectedMode
	 */
	public void setSelectedMode(String selectedMode) {
		this.selectedMode = selectedMode;
	}

	public Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

	public Date getFinishingDate() {
		return finishingDate;
	}

	public void setFinishingDate(Date finishingDate) {
		this.finishingDate = finishingDate;
	}

	public Float getPlannedTime() {
		return plannedTime;
	}

	public void setPlannedTime(Float plannedTime) {
		this.plannedTime = plannedTime;
	}

	public String getConcreteName() {
		return concreteName;
	}

	public void setConcreteName(String concreteName) {
		this.concreteName = concreteName;
	}

	/**
	 * @param _concreteWorkProductDescriptorService
	 *            the concreteWorkProductDescriptorService to set
	 */
	public void setConcreteWorkProductDescriptorService(
			ConcreteWorkProductDescriptorService _concreteWorkProductDescriptorService) {
		this.concreteWorkProductDescriptorService = _concreteWorkProductDescriptorService;
	}

	/**
	 * @return the concreteWorkProductDescriptorService
	 */
	public ConcreteWorkProductDescriptorService getConcreteWorkProductDescriptorService() {
		return concreteWorkProductDescriptorService;
	}

	public boolean isVisibleDeleteRolePopup() {
		return visibleDeleteRolePopup;
	}

	public void setVisibleDeleteRolePopup(boolean visibleDeleteRolePopup) {
		this.visibleDeleteRolePopup = visibleDeleteRolePopup;
	}

	/**
	 * 
	 * @return true if there are ConcretePredecessors AND ConcreteSuccessors
	 *         else return false
	 */
	public boolean getHasConcreteDependencies() {
		return !(this.getConcretePredecessors().isEmpty() && this
				.getConcreteSuccessors().isEmpty());
	}

	/**
	 * 
	 * @return true if there are ConcretePredecessors else return false
	 */
	public boolean getHasConcretePredecessors() {
		return !this.getConcretePredecessors().isEmpty();
	}

	/**
	 * 
	 * @return true if there are ConcreteSuccessors else return false
	 */
	public boolean getHasConcreteSuccessors() {
		return !this.getConcreteSuccessors().isEmpty();
	}

	/**
	 * ConcretePredecessors of the current activity
	 * 
	 * @return the concretePredecessors
	 */
	public List<HashMap<String, Object>> getConcretePredecessors() {
		ConcreteBreakdownElement cbde = this.concreteActivityService
				.getConcreteActivity(this.cbdeId);
		if (cbde != null) {
			// it's a concreteActivity
			ConcreteActivity ca = (ConcreteActivity) cbde;
			return this.projectService.getConcreteWorkBreakdownElementService()
					.getConcretePredecessorHashMap(ca);
		} else {
			ConcreteTaskDescriptor ctd = this.concreteTaskDescriptorService
					.getConcreteTaskDescriptor(this.cbdeId);
			return this.projectService.getConcreteWorkBreakdownElementService()
					.getConcretePredecessorHashMap(ctd);
		}
	}

	/**
	 * ConcreteSucessor of the current activity
	 * 
	 * @return the concreteSuccessors
	 */
	public List<HashMap<String, Object>> getConcreteSuccessors() {
		ConcreteBreakdownElement cbde = this.concreteActivityService
				.getConcreteActivity(this.cbdeId);
		if (cbde != null) {
			// it's a concreteActivity
			ConcreteActivity ca = (ConcreteActivity) cbde;
			return this.projectService.getConcreteWorkBreakdownElementService()
					.getConcreteSuccessorHashMap(ca);
		} else {
			ConcreteTaskDescriptor ctd = this.concreteTaskDescriptorService
					.getConcreteTaskDescriptor(this.cbdeId);
			return this.projectService.getConcreteWorkBreakdownElementService()
					.getConcreteSuccessorHashMap(ctd);
		}
	}

	private boolean verifyPlannedStartingDate() {

		// the planned starting date must be after the planned staring date of
		// all predecessors
		Date psd = new Date();
		Date pfd = new Date();
		String linkType;
		for (HashMap<String, Object> cp : this.getConcretePredecessors()) {
			psd = (Date) cp.get("plannedStartingDate");
			pfd = (Date) cp.get("plannedFinishingDate");

			linkType = (String) cp.get("linkType");
			if (linkType.equals(Constantes.WorkOrderType.FINISH_TO_START)) {
				if (pfd != null && !pfd.equals("")) {
					if (!pfd.before(this.startingDate)) {
						return false;
					}
				}
			}
		}
		for (HashMap<String, Object> cp : this.getConcreteSuccessors()) {
			psd = (Date) cp.get("plannedStartingDate");
			pfd = (Date) cp.get("plannedFinishingDate");
			linkType = (String) cp.get("linkType");

			if (linkType.equals(Constantes.WorkOrderType.START_TO_FINISH)) {
				if (pfd != null && !pfd.equals("")) {
					if (!pfd.equals(this.startingDate)) {
						return false;
					}
				}
			} else if (linkType.equals(Constantes.WorkOrderType.START_TO_START)) {
				if (psd != null && !psd.equals("")) {
					if (!psd.equals(this.startingDate)) {
						return false;
					}
				}
			}

		}

		return true;
	}

	private boolean verifyPlannedFinishingDate() {
		// the planned starting date must be after the planned staring date of
		// all predecessors
		Date psd = new Date();
		String linkType;
		for (HashMap<String, Object> cp : this.getConcreteSuccessors()) {
			psd = (Date) cp.get("plannedFinishingDate");
			linkType = (String) cp.get("linkType");

			if (linkType.equals(Constantes.WorkOrderType.FINISH_TO_FINISH)) {
				if (psd != null && !psd.equals("")) {
					if (psd.before(this.finishingDate)) {
						return false;
					}
				}
			}

		}

		return true;
	}

	public String getIdActivity() {
		return idActivity;
	}

	public void setIdActivity(String _idActivity) {
		this.idActivity = _idActivity;
	}
	
	
	
	
	/**
	 * Toggles the expanded state of all roles.
	 * 
	 * @param event
	 */
	public void toggleAllSubGroupAction(ActionEvent _event) {
		String elementId = null;
		Boolean b = null;
		ArrayList<HashMap<String, Object>> tmp ;
		Boolean existsContractNode = true ;
		
		ArrayList<HashMap<String,Object>> nom_liste = displayRoleContent;
		
		while (existsContractNode) {
			existsContractNode = false ;
			tmp = new ArrayList<HashMap<String, Object>>();
			tmp.addAll(this.displayRoleContent);
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
							expandRoleNodeAction(elementId,nom_liste);
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Toggles the expanded state of all roles.
	 * 
	 * @param event
	 */
	public void toggleAllSubGroupActionToDelete(ActionEvent _event) {
		String elementId = null;
		Boolean b = null;
		ArrayList<HashMap<String, Object>> tmp ;
		Boolean existsContractNode = true ;
		
		ArrayList<HashMap<String,Object>> nom_liste = displayRoleContentToDelete;
		
		while (existsContractNode) {
			existsContractNode = false ;
			tmp = new ArrayList<HashMap<String, Object>>();
			tmp.addAll(this.displayRoleContentToDelete);
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
							expandRoleNodeActionToDelete(elementId,nom_liste);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Utility method to add all child nodes to the parent dataTable list for delete the roles.
	 */
	@SuppressWarnings("unchecked")
	private void expandRoleNodeActionToDelete(String _elementId,ArrayList<HashMap<String,Object>> nom_liste) {
		// the table will be expanded so, indentation string
		this.needIndentation = true;

		ArrayList<Object> tmp = new ArrayList<Object>();
		tmp.addAll(nom_liste);
		int index;

		for (Iterator iter = tmp.iterator(); iter.hasNext();) {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm = (HashMap<String, Object>) iter.next();

			if (hm.get("id").equals(_elementId)) {
				if (hm.get("nodeType").equals("node")) {
					hm.put("expansionImage", EXPAND_TABLE_ARROW);
					ConcreteActivity ca = this.concreteActivityService
							.getConcreteActivity((String) hm.get("id"));
					index = nom_liste.indexOf(hm);
					List<HashMap<String, Object>> lst = this
							.retrieveHierarchicalRoleItemsToDelete(ca);
					if (lst != null) {
						nom_liste.addAll(index + 1, lst);
					} else {
						nom_liste.clear();
					}
					return;
				}
			}
		}
	}
	
	/**
	 * Utility method to add all child nodes to the parent dataTable list for effect the role.
	 */
	@SuppressWarnings("unchecked")
	private void expandRoleNodeAction(String _elementId,ArrayList<HashMap<String,Object>> nom_liste) {
		// the table will be expanded so, indentation string
		this.needIndentation = true;

		ArrayList<Object> tmp = new ArrayList<Object>();
		tmp.addAll(nom_liste);
		int index;

		for (Iterator iter = tmp.iterator(); iter.hasNext();) {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm = (HashMap<String, Object>) iter.next();

			if (hm.get("id").equals(_elementId)) {
				if (hm.get("nodeType").equals("node")) {
					hm.put("expansionImage", EXPAND_TABLE_ARROW);
					ConcreteActivity ca = this.concreteActivityService
							.getConcreteActivity((String) hm.get("id"));
					index = nom_liste.indexOf(hm);
					List<HashMap<String, Object>> lst = this
							.retrieveHierarchicalRoleItems(ca);
					if (lst != null) {
						nom_liste.addAll(index + 1, lst);
					} else {
						nom_liste.clear();
					}
					return;
				}
			}
		}
	}
	
	
	/**
	 * Toggles the expanded advanced project. 
	 * @param _event ActionEvent which activate the action of expand button
	 */
	public void toggleAllSubGroupActionAdvancement(ActionEvent _event) {
		String elementId = null;							// id of the current task
        Boolean taskExpanded = null;						// test if the current task is expanded or no
        ArrayList<HashMap<String, Object>> taskList ;		// list of all task
        Boolean existsContractNode = true ;					// true if a node exist and then expand this node
        
        taskList = new ArrayList<HashMap<String, Object>>();
        while (existsContractNode) {
        	// expand the currently
            existsContractNode = false ;
            taskList.clear();	//TODO : a optimiser
            taskList.addAll(this.displayContent);
            // expand all item present in the current node
            for (HashMap<String, Object> hm : taskList) {
                if (((String) hm.get("nodeType")).equals("node")) {
                	//if the item is a node expand all item present in the current node
                    if (((String) hm.get("expansionImage")).equals(CONTRACT_TABLE_ARROW)) {
                        existsContractNode = true;
                        taskExpanded = null;
                        elementId = (String) hm.get("id");
                        // toggle expanded state
                        taskExpanded = isExpanded.get(elementId);
                        if (taskExpanded == null) {
                            isExpanded.put(elementId, true);
                            taskExpanded = isExpanded.get(elementId);
                        } else {
                            if (!taskExpanded) {
                                taskExpanded = true;
                            }
                            isExpanded.put(elementId, taskExpanded);
                        }
                        if (taskExpanded) {
                            expandNodeAction(elementId);
                        }
                    }
                }
            }
        }
	}
	
	/**
	 * Utility method to add all child nodes to the parent dataTable list.
	 */
	@SuppressWarnings("unchecked")
	private void expandNodeAction(String elementId) {
		// the table will be expanded so, indentation string
		this.needIndentation = true;

		ArrayList<Object> tmp = new ArrayList<Object>();
		tmp.addAll(this.displayContent);
		int index;
		ConcreteTaskDescriptorService ctService = this
				.getConcreteTaskDescriptorService();
		ConcreteActivityService concreteActivityService = ctService
				.getConcreteActivityService();
		for (Iterator iter = tmp.iterator(); iter.hasNext();) {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm = (HashMap<String, Object>) iter.next();

			if (hm.get("id").equals(elementId)) {
				if (hm.get("nodeType").equals("node")) {
					hm.put("expansionImage", EXPAND_TABLE_ARROW);
					ConcreteActivity ca = concreteActivityService
							.getConcreteActivity((String) hm.get("id"));
					index = this.displayContent.indexOf(hm);
					if (this.retrieveHierarchicalItems(ca) != null) {
						this.displayContent.addAll(index + 1, this
								.retrieveHierarchicalItems(ca));
					} else {
						this.displayContent.clear();
					}
					return;
				}
			}
		}
	}
	
}
