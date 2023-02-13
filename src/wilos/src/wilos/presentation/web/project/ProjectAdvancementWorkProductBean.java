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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import wilos.business.services.misc.concretebreakdownelement.ConcreteBreakdownElementService;
import wilos.business.services.misc.concreterole.ConcreteRoleDescriptorService;
import wilos.business.services.misc.concretetask.ConcreteTaskDescriptorService;
import wilos.business.services.misc.concreteworkproduct.ConcreteWorkProductDescriptorService;
import wilos.business.services.misc.project.ProjectService;
import wilos.business.services.misc.wilosuser.ParticipantService;
import wilos.business.services.spem2.role.RoleDescriptorService;
import wilos.business.services.spem2.task.TaskDescriptorService;
import wilos.business.services.spem2.workproduct.WorkProductDescriptorService;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concretemilestone.ConcreteMilestone;
import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;
import wilos.model.misc.project.Project;
import wilos.model.misc.wilosuser.Participant;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.model.spem2.workproduct.WorkProductDescriptor;
import wilos.presentation.web.tree.TreeBean;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;
import wilos.resources.LocaleBean;
import wilos.utils.Constantes.State;

public class ProjectAdvancementWorkProductBean {

	public static final String EXPAND_TABLE_ARROW = "images/expandableTable/expand.gif";

	public static final String CONTRACT_TABLE_ARROW = "images/expandableTable/contract.gif";

	public static final String TABLE_LEAF = "images/expandableTable/leaf.gif";

	public static final String INDENTATION_STRING = "|- - - ";

	private ProjectService projectService;

	private ConcreteActivityService concreteActivityService;

	private ParticipantService participantService;

	private Project project;

	private String projectViewedId;

	private ArrayList<HashMap<String, Object>> displayContent;

	private ArrayList<HashMap<String, Object>> displayContentToDelete;

	private HashMap<String, String> indentationContent;

	private boolean needIndentation = false;

	protected HashMap<String, Boolean> isExpanded = new HashMap<String, Boolean>();

	private boolean selectedProjectAdvancementView;

	private boolean projectModified;

	private String projectInstanciated;

	protected final Log logger = LogFactory.getLog(this.getClass());

	private int pos = 0;

	private ConcreteTaskDescriptorService concreteTaskDescriptor;

	private ConcreteBreakdownElementService concreteBreakdownElementService;

	private ConcreteWorkProductDescriptorService concreteWorkProductDescriptorService;

	private WorkProductDescriptorService workProductDescriptorService;

	public static final String MY_WP_MODE = "myWorkProductMode";

	public static final String ALL_WP_MODE = "allWorkProductMode";

	private String selectedMode = ALL_WP_MODE;

	private boolean visibleDeleteWorkProductsPopup = false;

	private List<HashMap<String, Object>> tabWorkProductsToDelete;

	/**
	 * Constructor.
	 * 
	 */
	public ProjectAdvancementWorkProductBean() {
		this.project = new Project();
		this.displayContent = new ArrayList<HashMap<String, Object>>();
		this
				.setDisplayContentToDelete(new ArrayList<HashMap<String, Object>>());
		this.indentationContent = new HashMap<String, String>();
	}

	/**
	 * Getter of concreteBreakdownElementService.
	 * 
	 * @return the concreteBreakdownElementService.
	 */
	public boolean getVisibleDeleteWorkProductsPopup() {
		return this.visibleDeleteWorkProductsPopup;
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
	 * Getter of concreteTaskDescriptor.
	 * 
	 * @return the concreteTaskDescriptor.
	 */
	public ConcreteTaskDescriptorService getConcreteTaskDescriptor() {
		return concreteTaskDescriptor;
	}

	/**
	 * Setter of concreteTaskDescriptor.
	 * 
	 * @param the
	 *            concreteTaskDescriptor.
	 */
	public void setConcreteTaskDescriptor(
			ConcreteTaskDescriptorService _concreteTaskDescriptor) {
		concreteTaskDescriptor = _concreteTaskDescriptor;
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
				// if the project is another then the last selected or if it has
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
			this.expandNodeActionForWorkProductToDelete();
		}
		// remove items from list
		else {
			contractNodeActionForWorkProductToDelete();
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

	private void contractNodeActionForWorkProductToDelete() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");

		ArrayList<HashMap<String, Object>> parentList = new ArrayList<HashMap<String, Object>>();
		parentList.addAll(this.displayContentToDelete);

		/* Removes element which we want to contract from the parent list */
		for (HashMap<String, Object> currentElement : this.displayContentToDelete) {

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
	public void deleteChildrenToDelete(String _parentId,
			ArrayList<HashMap<String, Object>> parentList) {
		for (HashMap<String, Object> child : parentList) {
			if (child.get("parentId").equals(_parentId)) {
				this.displayContentToDelete.remove(child);
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
	 * Trait the work product the workproduct has a responsible
	 * 
	 * @param _hm
	 * @param _screen
	 * @param _parti
	 * @param _cwpd
	 * @param _responsible
	 */
	private void retrieveHierarchicalItemsWithAResponsible(
			HashMap<String, Object> _hm, boolean _screen, Participant _parti,
			ConcreteWorkProductDescriptor _cwpd, Participant _responsible) {

		_hm.put("isAffected", true);
		if (this.getSelectedMode().equals(ALL_WP_MODE)
				|| (this.getSelectedMode().equals(MY_WP_MODE) && (_responsible
						.getId().equals(_parti.getId())))) {

			_screen = true;
			// the workproduct is affected
			String responsibleID = _responsible.getId();

			_hm.put("responsible", this.participantService.getParticipant(
					responsibleID).getName());

			_hm.put("workProductID", _cwpd.getId());
			WorkProductDescriptor work = this.concreteWorkProductDescriptorService
					.getWorkProductDescriptor(_cwpd);

			String requiredRole = this.workProductDescriptorService
					.getNameRequiredRoleWorkProductDescriptor(work);
			if (requiredRole != null) {
				_hm.put("requiredRole", requiredRole);
			} else {
				_hm.put("requiredRole", LocaleBean
						.getText("tabAffectWorkProduct.noRequieredRole"));
			}

			if (responsibleID.equals(_parti.getId())) {
				// workproduct is affected to the current
				// participant

				_hm.put("enabled", false);
			} else {
				// the workproduct is affected to an another
				// participant
				_hm.put("enabled", true);

			}
			_hm.put("visibleCheckbox", true);
			_hm.put("nodeType", "leaf");
			_hm.put("expansionImage", TABLE_LEAF);

		}
	}

	/**
	 * Recursive method returning a hashmap data model which contains the
	 * elements required to display the project advancement table Give a
	 * ConcreteActivity to this method, return its firs hierarchical childs
	 * described into the hashmap
	 */
	private List<HashMap<String, Object>> retrieveHierarchicalItems(
			ConcreteActivity _concreteActivity) {

		String indentationString = "";
		boolean screen = false;

		List<HashMap<String, Object>> subConcretesContent = new ArrayList<HashMap<String, Object>>();

		String projectID = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);
		String userId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);
		Participant parti = this.getParticipantService().getParticipant(userId);
		// for every child of the activity

		if (_concreteActivity == null) {
			return null;
		}
		SortedSet<ConcreteBreakdownElement> concreteBDE = this.concreteActivityService
				.getConcreteBreakdownElements(_concreteActivity);

		for (ConcreteBreakdownElement concreteBreakdownElement : concreteBDE) {
			HashMap<String, Object> hm = new HashMap<String, Object>();

			screen = false;
			if (!(concreteBreakdownElement instanceof ConcreteMilestone)
					&& !(concreteBreakdownElement instanceof ConcreteRoleDescriptor)
					&& !(concreteBreakdownElement instanceof ConcreteTaskDescriptor)) {

				if (concreteBreakdownElement instanceof ConcreteWorkProductDescriptor) {
					// workProduct
					ConcreteWorkProductDescriptor cwpd = (ConcreteWorkProductDescriptor) concreteBreakdownElement;
					Participant responsible = this.concreteWorkProductDescriptorService
							.getParticipant(cwpd);
					WorkProductDescriptor work = this.concreteWorkProductDescriptorService
							.getWorkProductDescriptor(cwpd);
					RoleDescriptor rd = this.workProductDescriptorService
							.getRoleDescriptor(work);
					String requiredRole = "";

					if (responsible != null) {

						// this workproduct has a responsible
						hm.put("isAffected", true);
						if (this.getSelectedMode().equals(ALL_WP_MODE)
								|| (this.getSelectedMode().equals(MY_WP_MODE) && (responsible
										.getId().equals(parti.getId())))) {

							screen = true;
							// the workproduct is affected
							String responsibleID = responsible.getId();
							Participant p = this.participantService
									.getParticipant(responsibleID);
							hm.put("responsible", p.getName() + " "
									+ p.getFirstname());
							if (responsibleID.equals(parti.getId())) {
								// workproduct is affected to the current
								// participant
								hm.put("visibleCheckbox", true);
								hm.put("enabled", false);
							} else {
								// the workproduct is affected to an another
								// participant
								hm.put("visibleCheckbox", false);
								hm.put("enabled", true);
							}
							hm.put("workProductID", cwpd.getId());
							if (rd != null) {
								requiredRole = rd.getPresentationName();
								hm.put("requiredRole", requiredRole);
							} else {
								hm
										.put(
												"requiredRole",
												LocaleBean
														.getText("tabAffectWorkProduct.noRequieredRole"));
							}
							// hm.put("visibleCheckbox", true);
							hm.put("nodeType", "leaf");
							hm.put("expansionImage", TABLE_LEAF);
						}
					} else if (this.getSelectedMode().equals(ALL_WP_MODE)
							&& responsible == null) {

						screen = true;
						// the workproduct is not affected
						hm.put("responsible", LocaleBean
								.getText("tabAffectWorkProduct.noResponsible"));

						hm.put("workProductID", cwpd.getId());

						if (rd != null) {
							requiredRole = rd.getPresentationName();
							hm.put("requiredRole", requiredRole);

							// verify if the current participant have the
							// required role
							if (this.isAffected(parti, rd, projectID)) {
								// the participant have the required role
								hm.put("visibleCheckbox", true);
								hm.put("enabled", false);
							} else {
								// the participant haven't the required role
								hm.put("visibleCheckbox", false);
								hm.put("enabled", true);
							}
						} else {
							hm
									.put(
											"requiredRole",
											LocaleBean
													.getText("tabAffectWorkProduct.noRequieredRole"));
							hm.put("visibleCheckbox", true);
							hm.put("enabled", false);
						}
						// hm.put("visibleCheckbox", true);
						hm.put("nodeType", "leaf");
						hm.put("expansionImage", TABLE_LEAF);
						hm.put("isAffected", false);
					} else {
						hm.put("isAffected", false);
					}

				}
				// if this is not a workproduct -> display a node
				else {
					screen = true;
					hm.put("visibleCheckbox", false);
					hm.put("nodeType", "node");
					hm.put("expansionImage", CONTRACT_TABLE_ARROW);
				}

				hm.put("id", concreteBreakdownElement.getId());

				hm.put("concreteName", concreteBreakdownElement
						.getConcreteName());

				hm.put("parentId", _concreteActivity.getId());

				// if this is not the root node -> needIndentation == true
				if (needIndentation) {
					if (this.indentationContent.get(_concreteActivity.getId()) != null) {
						indentationString = this.indentationContent
								.get(_concreteActivity.getId());
					}
					this.indentationContent
							.put(
									(String) hm.get("id"),
									indentationString
											.concat(ProjectAdvancementBean.INDENTATION_STRING));
				}
				if (screen) {
					subConcretesContent.add(hm);
				}
			}
		}
		return subConcretesContent;
	}

	/**
	 * Recursive method returning a hashmap data model which contains the
	 * elements required to display the project advancement table Give a
	 * ConcreteActivity to this method, return its firs hierarchical childs
	 * described into the hashmap
	 */
	private List<HashMap<String, Object>> retrieveHierarchicalItemsToDelete(
			ConcreteActivity _concreteActivity) {

		String indentationString = "";
		boolean screen = false;

		List<HashMap<String, Object>> subConcretesContent = new ArrayList<HashMap<String, Object>>();

		String projectID = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);
		String userId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);
		Participant parti = this.getParticipantService().getParticipant(userId);
		// for every child of the activity

		if (_concreteActivity == null) {
			return null;
		}
		SortedSet<ConcreteBreakdownElement> concreteBDE = this.concreteActivityService
				.getConcreteBreakdownElements(_concreteActivity);

		for (ConcreteBreakdownElement concreteBreakdownElement : concreteBDE) {
			HashMap<String, Object> hm = new HashMap<String, Object>();

			screen = false;
			if (!(concreteBreakdownElement instanceof ConcreteMilestone)
					&& !(concreteBreakdownElement instanceof ConcreteRoleDescriptor)
					&& !(concreteBreakdownElement instanceof ConcreteTaskDescriptor)) {

				if (concreteBreakdownElement instanceof ConcreteWorkProductDescriptor) {
					// workProduct
					ConcreteWorkProductDescriptor cwpd = (ConcreteWorkProductDescriptor) concreteBreakdownElement;
					Participant responsible = this.concreteWorkProductDescriptorService
							.getParticipant(cwpd);
					WorkProductDescriptor work = this.concreteWorkProductDescriptorService
							.getWorkProductDescriptor(cwpd);
					RoleDescriptor rd = this.workProductDescriptorService
							.getRoleDescriptor(work);
					String requiredRole = "";

					if (responsible != null) {
						// this workproduct has a responsible
						hm.put("isAffected", true);
						screen = true;
						// the workproduct is affected
						String responsibleID = responsible.getId();
						Participant p = this.participantService
								.getParticipant(responsibleID);
						hm.put("responsible", p.getName() + " "
								+ p.getFirstname());
						hm.put("visibleCheckbox", false);
						hm.put("enabled", true);
						hm.put("workProductID", cwpd.getId());
						// hm.put("visibleCheckbox", true);
						hm.put("nodeType", "leaf");
						hm.put("expansionImage", TABLE_LEAF);
					} else if (responsible == null) {
						screen = true;
						// the workproduct is not affected
						hm.put("responsible", "");
						hm.put("workProductID", cwpd.getId());
						hm.put("visibleCheckbox", true);
						hm.put("enabled", false);
						// hm.put("visibleCheckbox", true);
						hm.put("nodeType", "leaf");
						hm.put("expansionImage", TABLE_LEAF);
						hm.put("isAffected", false);
					} else {
						hm.put("isAffected", false);
					}

				}
				// if this is not a workproduct -> display a node
				else {
					screen = true;
					hm.put("visibleCheckbox", false);
					hm.put("nodeType", "node");
					hm.put("expansionImage", CONTRACT_TABLE_ARROW);
				}

				hm.put("id", concreteBreakdownElement.getId());

				hm.put("concreteName", concreteBreakdownElement
						.getConcreteName());

				hm.put("parentId", _concreteActivity.getId());

				// if this is not the root node -> needIndentation == true
				if (needIndentation) {
					if (this.indentationContent.get(_concreteActivity.getId()) != null) {
						indentationString = this.indentationContent
								.get(_concreteActivity.getId());
					}
					this.indentationContent
							.put(
									(String) hm.get("id"),
									indentationString
											.concat(ProjectAdvancementBean.INDENTATION_STRING));
				}
				if (screen) {
					subConcretesContent.add(hm);
				}
			}
		}
		return subConcretesContent;
	}

	/**
	 * Update the table when the user arrived by the menu
	 */
	public void updateTable() {
		this.projectModified = true;
	}

	public RoleDescriptor getRoleForWorkProduct(
			ConcreteWorkProductDescriptor _cwpd) {
		if (_cwpd != null) {
			ConcreteWorkProductDescriptorService ctdService = this
					.getConcreteWorkProductDescriptorService();
			WorkProductDescriptorService tdService = ctdService
					.getWorkProductDescriptorService();
			RoleDescriptorService rdService = ctdService
					.getRoleDescriptorService();
			WorkProductDescriptor tmp = _cwpd.getWorkProductDescriptor();
			RoleDescriptor tmpRoleDescriptor;
			WorkProductDescriptor tmp2 = tdService
					.getWorkProductDescriptorById(tmp.getId());

			tmpRoleDescriptor = tmp2.getResponsibleRoleDescriptor();

			if (tmpRoleDescriptor != null) {
				RoleDescriptor rd = rdService
						.getRoleDescriptor(tmpRoleDescriptor.getId());
				return rd;
			}
		}
		return null;
	}

	/**
	 * Return the advancement in percents of a Concrete breakdown element
	 * 
	 * @param cbe
	 * @return
	 */
	public Double activityAdvancementCalculation(ConcreteBreakdownElement cbe) {
		Double result = 0.0;
		HashMap<String, Double> couple = this.taskAdvancementCalculation(cbe);
		double advancementRatio = couple.get("advancementRatio");
		if (advancementRatio > 0) {
			result = advancementRatio * 100;

			if (cbe instanceof ConcreteActivity) {
				ConcreteActivity ca = (ConcreteActivity) cbe;
				int i = 0;
				for (ConcreteBreakdownElement concreteBreakdownElement : this.concreteActivityService
						.getConcreteBreakdownElements(ca)) {
					if (concreteBreakdownElement instanceof ConcreteTaskDescriptor) {
						++i;
					}
				}
				result = result / i;
			}
		} else {
			result = null;
		}
		return result;
	}

	/**
	 * Know if a participant have a role
	 * 
	 * @param _parti
	 * @param _projectId
	 * @param _rd
	 * @return
	 */
	private boolean participantHaveRole(Participant _parti, String _projectId,
			RoleDescriptor _rd) {
		for (ConcreteRoleDescriptor crd : this.participantService
				.getConcreteRoleDescriptorsForAParticipantAndForAProject(_parti
						.getId(), _projectId)) {
			if (crd.getRoleDescriptor().getPresentationName().equals(
					_rd.getPresentationName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Save affectation of workproducts
	 */
	public void saveAffectWorkProduct() {
		boolean reg = true;
		String wilosUserId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);
		ParticipantService pService = this.getParticipantService();
		Participant parti = pService.getParticipant(wilosUserId);

		ConcreteWorkProductDescriptorService cwpdService = this.concreteWorkProductDescriptorService;

		for (HashMap<String, Object> ligne : this.getDisplayContent()) {

			if ((Boolean) ligne.get("visibleCheckbox")) {
				if (!(Boolean) ligne.get("enabled")) {

					ConcreteWorkProductDescriptor cwpd = this.concreteWorkProductDescriptorService
							.getConcreteWorkProductDescriptor(ligne.get(
									"workProductID").toString());
					if (cwpd != null) {
						if ((Boolean) ligne.get("isAffected")) {

							cwpd = cwpdService
									.affectedConcreteWorkProductDescriptor(
											cwpd, parti);
							if (cwpd == null
									|| !(parti.getId()).equals(cwpd.getParticipant())) {
								reg = false;
							}
						} else if (!(Boolean) ligne.get("isAffected")) {
							cwpdService
									.dissociateConcreteWorkProductDescriptor(
											cwpd, parti);
						}
					} else {
						System.out.println("la");
						reg = false;
					}
				}
			}

		}
		if (reg) {
			WebCommonService.addInfoMessage(LocaleBean
					.getText("tabAffectWorkProduct.updateAffected"));
		} else {
			WebCommonService.addErrorMessage(LocaleBean
					.getText("tabAffectWorkProduct.updateAffectedFailed"));
		}
		TreeBean tb = (TreeBean) WebCommonService.getBean("TreeBean");
		tb.setSelectedMode(tb.WORKPRODUCTS_MODE);
		tb.refreshProjectTree();
		this.refreshProjectTable();
	}

	/**
	 * Return the consomme in percents of a Concrete breakdown element
	 * 
	 * @param cbe
	 * @return
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
	 * @return
	 */
	private HashMap<String, Double> taskAdvancementCalculation(
			ConcreteBreakdownElement cbe) {
		HashMap<String, Double> coupletmp = new HashMap<String, Double>();
		HashMap<String, Double> couple = new HashMap<String, Double>();
		couple.put("advancementRatio", 0.0);

		// if the current element is an activity, parse the sub concrete
		// breakdown elements
		if (cbe instanceof ConcreteActivity) {
			ConcreteActivity ca = (ConcreteActivity) cbe;

			for (ConcreteBreakdownElement concreteBreakdownElement : this.concreteActivityService
					.getConcreteBreakdownElements(ca)) {
				coupletmp = this
						.taskAdvancementCalculation(concreteBreakdownElement);
				couple.put("advancementRatio", couple.get("advancementRatio")
						+ coupletmp.get("advancementRatio"));
			}
		}
		// else if it's a concrete task get the values
		else {
			if (cbe instanceof ConcreteTaskDescriptor) {
				ConcreteTaskDescriptor ctd = (ConcreteTaskDescriptor) cbe;
				if (ctd.getRemainingTime() != 0
						|| ctd.getAccomplishedTime() != 0) {
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
	 * @return the isExpanded
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
	 * getter of projectService String attribute
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
			if (role.equals("projectDirector")) {
				this.selectedProjectAdvancementView = true;
			}
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
		if (this.project == null)
			this.projectInstanciated = "projectNotSelected";
		else if (this.project.getProcess() == null) {
			this.projectInstanciated = "projectNotInstanciated";
		} else {
			this.projectInstanciated = "projectInstanciated";
		}
		return this.projectInstanciated;
	}

	/**
	 * setter of projectInstanciated String attribute
	 * 
	 * @param projectInstanciated
	 */
	public void setProjectInstanciated(String projectInstanciated) {
		this.projectInstanciated = projectInstanciated;
	}

	/**
	 * 
	 * Editing an estimate
	 * 
	 * @param e
	 *            event received when a user clicks on edit button in the
	 *            datatable
	 */
	public void editEstimate(ActionEvent e) {
		String taskPosition = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get(
						"taskPosition");
		this.displayContent.get(Integer.parseInt(taskPosition)).put(
				"isEditable", true);
	}

	public int getIndentationPosition() {
		int tmppos = this.pos;
		this.pos = this.pos + 1;
		return tmppos;
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
			// System.out.println("nom de la tache : " + ctd.getConcreteName()
			// + "\n");
			// System.out.println("etat : ");
			if ((ctd.getState().equals("Created"))
					|| (ctd.getState().equals("Ready"))) {
				// System.out.println("creer ou en cours \n");
				return false;
			} else {
				// System.out.println("pas commencer ou fini \n");
				return true;
			}
		}
		if (_concreteBreakdownElement instanceof ConcreteActivity) {
			ca = (ConcreteActivity) _concreteBreakdownElement;
			// System.out.println("nom de la tache : " + ca.getConcreteName()
			// + "\n");
			// System.out.println("etat : ");
			if (ca.getState().equals("Finished")) {
				// System.out.println("fini \n");
				return true;
			} else {
				// System.out.println("pas commencer, creer ou en cours \n");
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

	/**
	 * 
	 * Save an estimate
	 * 
	 * @param e
	 *            event received when a user clicks on save button in the
	 *            datatable
	 */
	public void saveEstimate(ActionEvent e) {
		int position = 0;
		String taskPosition = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get(
						"taskPosition");

		ConcreteActivity ca = new ConcreteActivity();
		ConcreteWorkBreakdownElement cz = new ConcreteWorkBreakdownElement();
		ConcreteTaskDescriptor ctd = new ConcreteTaskDescriptor();

		List<ConcreteBreakdownElement> concreteBDE = this.concreteBreakdownElementService
				.getConcreteBreakdownElementDao()
				.getAllConcreteBreakdownElements();

		while (!concreteBDE.get(position).getId().equals(
				this.displayContent.get(Integer.parseInt(taskPosition)).get(
						"id"))) {
			position = position + 1;
		}
		cz = (ConcreteWorkBreakdownElement) concreteBDE.get(position);
		if (cz instanceof ConcreteTaskDescriptor) {

			ctd = (ConcreteTaskDescriptor) cz;
			ctd
					.setPlannedFinishingDate((Date) this.displayContent.get(
							Integer.parseInt(taskPosition)).get(
							"plannedFinishingDate"));
			ctd.setPlannedStartingDate((Date) this.displayContent.get(
					Integer.parseInt(taskPosition)).get("plannedStartingDate"));
			ctd.setPlannedTime((Float) this.displayContent.get(
					Integer.parseInt(taskPosition)).get("plannedTime"));

			this.concreteTaskDescriptor.saveConcreteTaskDescriptor(ctd);

		}
		if (cz instanceof ConcreteActivity) {
			ca = (ConcreteActivity) cz;
			ca
					.setPlannedFinishingDate((Date) this.displayContent.get(
							Integer.parseInt(taskPosition)).get(
							"plannedFinishingDate"));
			ca.setPlannedStartingDate((Date) this.displayContent.get(
					Integer.parseInt(taskPosition)).get("plannedStartingDate"));
			ca.setPlannedTime((Float) this.displayContent.get(
					Integer.parseInt(taskPosition)).get("plannedTime"));
			this.concreteActivityService.saveConcreteActivity(ca);
			System.out.println(ca.getId() + "\n");
		}

		this.displayContent.get(Integer.parseInt(taskPosition)).put(
				"isEditable", false);
		WebCommonService.addInfoMessage(LocaleBean
				.getText("component.project.projectEstimate.updateAffected"));

	}

	/**
	 * this method return a concreteRoledescriptor link to the given
	 * concreteTaskDescriptor
	 * 
	 * @param _ctd
	 *            the given concreteTaskDescriptor
	 * @return the wanted ConcreteRoleDescriptor
	 */
	// public List<HashMap<String, Object>> getAllRoleForTask(
	public ConcreteRoleDescriptor getRoleForTask(ConcreteTaskDescriptor _ctd) {
		if (_ctd != null) {
			// List<HashMap<String, Object>> listHashMapRoleForTask = new
			// ArrayList<HashMap<String, Object>>();

			ConcreteTaskDescriptorService ctdService = this
					.getConcreteTaskDescriptor();
			TaskDescriptorService tdService = ctdService
					.getTaskDescriptorService();
			RoleDescriptorService rdService = ctdService
					.getRoleDescriptorService();
			ConcreteRoleDescriptorService crdService = ctdService
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
		ConcreteTaskDescriptorService ctdService = this
				.getConcreteTaskDescriptor();
		ConcreteRoleDescriptorService crdService = ctdService
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

	public boolean isAffected(Participant _parti, RoleDescriptor _rd,
			String _projectId) {
		ConcreteRoleDescriptorService crdService = this.concreteWorkProductDescriptorService
				.getConcreteRoleDescriptorService();
		List<ConcreteRoleDescriptor> listeCrd = crdService
				.getAllConcreteRoleDescriptorsForProject(_projectId);
		for (ConcreteRoleDescriptor crd_list : listeCrd) {
			if (crd_list.getRoleDescriptor().getId().equals(_rd.getId())) {
				Participant p = crd_list.getParticipant();

				if (p != null) {
					if (p.getId().equals(_parti.getId()))
						return true;
				}
			}
		}
		return false;
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
		boolean modif = false;
		String wilosUserId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);
		ParticipantService pService = this.getParticipantService();
		Participant parti = pService.getParticipant(wilosUserId);
		ConcreteTaskDescriptorService ctdService = this
				.getConcreteTaskDescriptor();
		ConcreteRoleDescriptorService crdService = ctdService
				.getConcreteRoleDescriptorService();
		for (HashMap<String, Object> ligne : this.displayContent) {
			if ((Boolean) ligne.get("visibleCheckbox")) {
				if (!(Boolean) ligne.get("enabled")) {
					ConcreteRoleDescriptor crd = (ConcreteRoleDescriptor) ligne
							.get("role");

					if (crd != null) {
						if ((Boolean) ligne.get("isAffected")
								&& !(Boolean) ligne.get("flagAffect")) {
							crd.setParticipant(parti);
							crdService.getConcreteRoleDescriptorDao()
									.saveOrUpdateConcreteRoleDescriptor(crd);
							modif = true;
						} else if (!(Boolean) ligne.get("isAffected")
								&& (Boolean) ligne.get("flagAffect")) {
							crd.setParticipant(null);
							crdService.getConcreteRoleDescriptorDao()
									.saveOrUpdateConcreteRoleDescriptor(crd);
							modif = true;
						}
					}
				}
			}
		}
		if (modif) {
			WebCommonService.addInfoMessage(LocaleBean
					.getText("tabAffectRole.updateAffected"));
			TreeBean tb = (TreeBean) WebCommonService.getBean("TreeBean");
			tb.setSelectedMode(tb.ROLES_MODE);
			tb.refreshProjectTree();

		}
		this.refreshProjectTable();

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
		Date ladate = new Date();
		String style = "";
		if (_mode.equals("Start")) {
			if ((_ctd.getState().equals("Created"))
					|| (_ctd.getState().equals("Ready"))) {
				if (_ctd.getPlannedStartingDate() != null) {
					if (_ctd.getPlannedStartingDate().before(ladate)) {
						style = "color:red;font-weight: bold;";
					} else {
						style = "color:green;font-weight: bold;";
					}
				} else {
					style = "";
				}
			} else {
				style = "";
			}
		} else {
			if ((_ctd.getState().equals("Created"))
					|| (_ctd.getState().equals("Ready"))
					|| (_ctd.getState().equals("Started"))) {
				if (_ctd.getPlannedFinishingDate() != null) {
					if (_ctd.getPlannedFinishingDate().before(ladate)) {
						style = "color:red;font-weight: bold;";
					} else {
						style = "";
					}
				} else {
					style = "";
				}
			} else if (_ctd.getState().equals("Finished")) {
				if (_ctd.getPlannedFinishingDate() != null) {
					if (_ctd.getPlannedFinishingDate().before(
							_ctd.getRealFinishingDate())) {
						style = "color:red;font-weight: bold;";
					} else {
						style = "foncolor:green;font-weight: bold;";
					}
				} else {
					style = "";
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
		} else if (_ctd.getState().equals("Finished")) {
			couleur = "color:orange;font-weight: bold;";
		}
		return couleur;
	}

	/**
	 * this method allow to return the current instance of
	 * WorkProductDescriptorService
	 * 
	 * @return WorkProductDescriptorService
	 */
	public WorkProductDescriptorService getWorkProductDescriptorService() {
		return workProductDescriptorService;
	}

	/**
	 * this method allow to set the current instance of
	 * WorkProductDescriptorService
	 * 
	 * @param workProductDescriptorService
	 */
	public void setWorkProductDescriptorService(
			WorkProductDescriptorService workProductDescriptorService) {
		this.workProductDescriptorService = workProductDescriptorService;
	}

	/**
	 * this method allow to return the current instance of
	 * ConcreteWorkProductDescriptorService
	 * 
	 * @return ConcreteWorkProductDescriptorService
	 */
	public ConcreteWorkProductDescriptorService getConcreteWorkProductDescriptorService() {
		return concreteWorkProductDescriptorService;
	}

	/**
	 * this method allow to set the current instance of
	 * ConcreteWorkProductDescriptorService
	 * 
	 * @param concreteWorkProductDescriptorService
	 */
	public void setConcreteWorkProductDescriptorService(
			ConcreteWorkProductDescriptorService concreteWorkProductDescriptorService) {
		this.concreteWorkProductDescriptorService = concreteWorkProductDescriptorService;
	}

	/**
	 * this method allow to return all the available mode for the workProduct
	 * 
	 * @return List<SelectItem> of mode
	 */
	public List<SelectItem> getModesList() {
		ArrayList<SelectItem> modesList = new ArrayList<SelectItem>();

		modesList.add(new SelectItem(ALL_WP_MODE, LocaleBean
				.getText("tabAffectWorkProduct.checkboxlabel.allWorkProduct")));
		modesList.add(new SelectItem(MY_WP_MODE, LocaleBean
				.getText("tabAffectWorkProduct.checkboxlabel.myWorkProduct")));

		return modesList;
	}

	/**
	 * this method allow to change the current concreteWorkProduct mode
	 * 
	 * @param evt
	 */
	public void changeModeActionListener(ValueChangeEvent evt) {

		this.selectedMode = (String) evt.getNewValue();
		if (this.selectedMode.equals(ALL_WP_MODE)) {
			WebSessionService.setAttribute(WebSessionService.TREE_MODE,
					ALL_WP_MODE);
			refreshProjectTable();
		} else {
			WebSessionService.setAttribute(WebSessionService.TREE_MODE,
					MY_WP_MODE);
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

	public void setDisplayContentToDelete(
			ArrayList<HashMap<String, Object>> displayContentToDelete) {
		this.displayContentToDelete = displayContentToDelete;
	}

	/**
	 * Getter of displayContentToDelete.
	 * 
	 * @return the displayContentToDelete.
	 */
	public ArrayList<HashMap<String, Object>> getDisplayContentToDelete() {
		String projectId = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);

		// this.listRoleOfParti = null;
		if (projectId != null && !projectId.equals("default")) {

			this.project = this.projectService.getProject(projectId);
			this.pos = 0;
			// if the project is another then the last selected or if it has
			// been
			// modified
			if (project != null) {
				if (this.projectViewedId == null
						|| !(this.projectViewedId.equals(projectId))
						|| this.projectModified) {
					// reseting the table parameters
					projectViewedId = projectId;
					this.displayContentToDelete.clear();
					this.indentationContent.clear();
					this.isExpanded.clear();
					this.needIndentation = false;
					List<HashMap<String, Object>> list = this
							.retrieveHierarchicalItemsToDelete(this.project);
					if (list != null) {
						this.displayContentToDelete.addAll(list);
					} else
						this.displayContentToDelete.clear();
				}

			} else {
				this.displayContentToDelete.clear();
			}
		} else
			this.displayContentToDelete.clear();

		this.projectModified = false;
		return this.displayContentToDelete;
	}

	public void cancelDeleteWorkProduct(ActionEvent e) {
		this.visibleDeleteWorkProductsPopup = false;
	}

	public void removeWorkProductsActionListener() {
		this.visibleDeleteWorkProductsPopup = true;
	}

	public boolean isVisibleDeleteWorkProductsPopup() {
		return visibleDeleteWorkProductsPopup;
	}

	public void serVisibleDeleteWorkProductsPopup(boolean bool) {
		this.visibleDeleteWorkProductsPopup = bool;
	}

	public void setTabWorkProductsToDelete(
			List<HashMap<String, Object>> tabWorkProductsToDelete) {
		this.tabWorkProductsToDelete = tabWorkProductsToDelete;
	}

	public List<HashMap<String, Object>> getTabWorkProductsToDelete() {
		return tabWorkProductsToDelete;
	}

	/**
	 * 
	 * @return
	 */
	public boolean getVisibleRemove(ConcreteWorkProductDescriptor _cwpd) {
		return (_cwpd.getState().equals(State.CREATED));
	}

	/**
	 * 
	 */
	public void deleteSelectedWorkProducts(ActionEvent e) {

		ConcreteWorkProductDescriptorService cwpdService = this.concreteWorkProductDescriptorService;
		// permet de savoir si les suppressions se sont bien passées
		boolean reg = true;

		for (HashMap<String, Object> ligne : this.getDisplayContentToDelete()) {

			if ((Boolean) ligne.get("visibleCheckbox")) {
				if (!(Boolean) ligne.get("enabled")) {

					ConcreteWorkProductDescriptor cwpd = this.concreteWorkProductDescriptorService
							.getConcreteWorkProductDescriptor(ligne.get(
									"workProductID").toString());

					if (cwpd != null) {
						if ((Boolean) ligne.get("isAffected")) {
							// is selected
							cwpd = cwpdService
									.deleteConcreteWorkProductDescriptor(cwpd);
							if (cwpd != null) {
								reg = false;
							}
						}
					}
				}
			}

		}
		if (reg) {
			WebCommonService.addInfoMessage(LocaleBean
					.getText("tabDeleteWorkProduct.delete.done"));
		} else {
			WebCommonService.addErrorMessage(LocaleBean
					.getText("tabDeleteWorkProduct.delete.done.failed"));
		}
		TreeBean tb = (TreeBean) WebCommonService.getBean("TreeBean");
		tb.setSelectedMode(tb.WORKPRODUCTS_MODE);
		tb.refreshProjectTree();
		this.refreshProjectTable();
		this.visibleDeleteWorkProductsPopup = false;
	}

	/**
	 * Toggles the expanded state of all products.
	 * 
	 * @param event
	 */
	public void toggleAllSubGroupAction(ActionEvent _event) {
		ArrayList<HashMap<String, Object>> nom_liste = displayContent;

		String elementId = null;
		Boolean b = null;
		ArrayList<HashMap<String, Object>> tmp;
		Boolean existsContractNode = true;
		while (existsContractNode) {
			existsContractNode = false;
			tmp = new ArrayList<HashMap<String, Object>>();
			tmp.addAll(this.displayContent);
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
							expandNodeAction(elementId, nom_liste);
						}
					}
				}
			}
		}
	}

	/**
	 * Toggles the expanded state of all products.
	 * 
	 * @param event
	 */
	public void toggleAllSubGroupActionToDelete(ActionEvent _event) {
		ArrayList<HashMap<String, Object>> nom_liste = displayContentToDelete;

		String elementId = null;
		Boolean b = null;
		ArrayList<HashMap<String, Object>> tmp;
		Boolean existsContractNode = true;
		while (existsContractNode) {
			existsContractNode = false;
			tmp = new ArrayList<HashMap<String, Object>>();
			tmp.addAll(this.displayContentToDelete);
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
							expandNodeActionForWorkProductToDelete(elementId,
									nom_liste);
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
	private void expandNodeAction() {

		ArrayList<HashMap<String, Object>> nom_liste = displayContent;

		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");

		this.expandNodeAction(elementId, nom_liste);
	}

	/**
	 * Utility method to add all child nodes to the parent dataTable list.
	 */
	@SuppressWarnings("unchecked")
	private void expandNodeAction(String _elementId,
			ArrayList<HashMap<String, Object>> nom_liste) {
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
							.retrieveHierarchicalItems(ca);
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
	 * Utility method to add all child nodes to the parent dataTable list.
	 */
	@SuppressWarnings("unchecked")
	private void expandNodeActionForWorkProductToDelete() {
		ArrayList<HashMap<String, Object>> tmp = this.displayContentToDelete;
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");

		this.expandNodeActionForWorkProductToDelete(elementId, tmp);
	}

	private void expandNodeActionForWorkProductToDelete(String _elementId,
			ArrayList<HashMap<String, Object>> nom_liste) {
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
							.retrieveHierarchicalItemsToDelete(ca);
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
}
