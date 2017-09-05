/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
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

package wilos.presentation.web.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wilos.business.services.misc.concreterole.ConcreteRoleDescriptorService;
import wilos.business.services.misc.concreteworkproduct.ConcreteWorkProductDescriptorService;
import wilos.business.services.misc.wilosuser.ParticipantService;
import wilos.business.services.spem2.role.RoleDescriptorService;
import wilos.business.services.spem2.workproduct.WorkProductDescriptorService;

import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;
import wilos.model.misc.wilosuser.Participant;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.model.spem2.workproduct.WorkProductDescriptor;
import wilos.presentation.web.project.ProjectAdvancementWorkProductBean;
import wilos.presentation.web.tree.TreeBean;
import wilos.presentation.web.tree.WilosObjectNode;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;
import wilos.resources.LocaleBean;
import wilos.utils.Constantes.State;

public class ConcreteWorkProductViewerBean extends ViewerBean {

	/* Services */

	private ConcreteWorkProductDescriptorService concreteWorkProductDescriptorService;

	private WorkProductDescriptorService workProductDescriptorService;

	private ConcreteRoleDescriptorService concreteRoleDescriptorService;

	private RoleDescriptorService roleDescriptorService;

	private ParticipantService participantService;

	/* Simple fields */

	private ConcreteWorkProductDescriptor concreteWorkProductDescriptor;

	private boolean displayedResponsibleRoleVisible;

	private boolean displayedParticipantVisible;

	private boolean visiblePopup = false;

	private boolean visibleDeletePopup = false;

	protected final Log logger = LogFactory.getLog(this.getClass());

	private String displayedPanelManager;

	private String selectedWorkProductState = "default";

	private String workProductAffectedView;

	private List<HashMap<String, Object>> tabWorkProductAffected;

	public static final String MY_WP_MODE = "myWorkProductMode";

	public static final String ALL_WP_MODE = "allWorkProductMode";

	private String selectedMode = ALL_WP_MODE;

	/**
	 * Method to have states in string
	 * 
	 * @return state of concreteWorkProductDescritor
	 */
	public String getDisplayedState() {
		return WebCommonService
				.getDisplayedStateForWorkProduct(this.concreteWorkProductDescriptor
						.getState());
	}

	/**
	 * Method to have the responsible role
	 * 
	 * @return string
	 */
	public String getDisplayedResponsibleRole() {
		RoleDescriptor rd = this.concreteWorkProductDescriptor
				.getWorkProductDescriptor().getResponsibleRoleDescriptor();
		if (rd != null) {
			return this.roleDescriptorService.getPresentationName(rd);
		} else {
			return LocaleBean
					.getText("concreteWorkProductViewer.noConcreteRole");
		}

	}

	/**
	 * Method to get state button
	 * 
	 * @return
	 */
	public boolean getDissociateButtonIsVisible() {

		String state = this.concreteWorkProductDescriptor.getState();

		String wilosUserId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);

		Participant current_participant = this.participantService
				.getParticipant(wilosUserId);

		String role = (String) WebSessionService
				.getAttribute(WebSessionService.ROLE_TYPE);

		if (!role.equals("projectDirector")) {

			return ((this.concreteWorkProductDescriptorService
					.isAffectableToConcreteWorkProductDescriptor(
							this.concreteWorkProductDescriptor,
							current_participant)) && (!state
					.equals(State.CREATED)));
		} else {
			return false;
		}

	}

	/**
	 * Method to dissociate workProduct listener and refresh table and tree
	 * 
	 * @param event
	 */
	public void dissociateWorkProductActionListener(ActionEvent event) {

		String wilosUserId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);

		Participant current_participant = this.participantService
				.getParticipant(wilosUserId);

		this.concreteWorkProductDescriptorService
				.dissociateConcreteWorkProductDescriptor(
						this.concreteWorkProductDescriptor, current_participant);

		WebCommonService.addInfoMessage(LocaleBean
				.getText("concreteWorkProductViewer.dissociated"));

		super.refreshProjectTable();
		ProjectAdvancementWorkProductBean pawp = (ProjectAdvancementWorkProductBean) WebCommonService
				.getBean("ProjectAdvancementWorkProductBean");
		pawp.refreshProjectTable();
		super.refreshProjectTree();
	}

	/**
	 * Method to save the concrete name
	 */
	public void saveName() {
		if (this.concreteWorkProductDescriptor.getConcreteName().trim()
				.length() == 0) {
			// re-put the former concrete name.
			ConcreteWorkProductDescriptor ctd = this.concreteWorkProductDescriptorService
					.getConcreteWorkProductDescriptor(this.concreteWorkProductDescriptor
							.getId());
			this.concreteWorkProductDescriptor.setConcreteName(ctd
					.getConcreteName());

			// add error message.
			WebCommonService.addErrorMessage(LocaleBean
					.getText("viewer.err.checkNameBySaving"));
		} else {
			this.concreteWorkProductDescriptorService
					.saveConcreteWorkProductDescriptor(this.concreteWorkProductDescriptor);

			// Refresh the treebean.
			super.refreshProjectTree();

			// put the name text editor to disable.
			super.setNameIsEditable(false);

			// successful message.
			WebCommonService.addInfoMessage(LocaleBean
					.getText("viewer.visibility.successMessage"));
		}
	}

	/**
	 * Method to affect an actionListener
	 * 
	 * @param event
	 */
	public void affectedActionListener(ActionEvent event) {
		String wilosUserId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);

		Participant participant = this.participantService
				.getParticipant(wilosUserId);
		//on essaye d'affecter la personne et on récupere le produit
		this.concreteWorkProductDescriptor = this.concreteWorkProductDescriptorService
				.affectedConcreteWorkProductDescriptor(
						this.concreteWorkProductDescriptor, participant);
		//si le produit n'est pas supprimé
		if (this.concreteWorkProductDescriptor != null) {
			//si l'utilisateur est bien affecté
			if(this.concreteWorkProductDescriptor.getParticipant()== participant.getId() && concreteWorkProductDescriptor.getParticipant() != null){
				//message de confirmation
				WebCommonService.addInfoMessage(LocaleBean
						.getText("concreteWorkProductViewer.updateAffected"));
			}else{
				//si utilisateur pas affecté
				//message indiquant que le produit est deja affecté
				WebCommonService.addErrorMessage(LocaleBean
						.getText("concreteWorkProductViewer.updateAffected.failed"));
			}
		} else {
			//si produit supprimé, on redirige vers la page de description
			WebCommonService.changeContentPage(WilosObjectNode.PROJECTNODE);
			//et message d'erreur indiquant que le produit a ete supprimé
			WebCommonService.addErrorMessage(LocaleBean
					.getText("concreteWorkProductViewer.updateAffected.delete"));
		}
		//on refresh
		super.refreshProjectTable();
		super.refreshProjectTree();
		ProjectAdvancementWorkProductBean pawp = (ProjectAdvancementWorkProductBean) WebCommonService
				.getBean("ProjectAdvancementWorkProductBean");
		pawp.refreshProjectTable();
	}

	/**
	 * @return the visibleAffected
	 */
	public boolean getVisibleAffected() {
		return ((this.concreteWorkProductDescriptor.getState().equals(
				State.CREATED) && this.visibleAffected()) || (this.concreteWorkProductDescriptor
				.getState().equals(State.CREATED) && !this.havingRole()));
	}

	/**
	 * Method to known if responsible role is affected or not
	 * 
	 * @return true if responsible role is affected
	 */
	public boolean havingRole() {
		return (this.concreteWorkProductDescriptor.getWorkProductDescriptor()
				.getResponsibleRoleDescriptor() != null);
	}

	/**
	 * Method for check if the current user can be affected to the current
	 * workproduct
	 */
	public boolean visibleAffected() {

		String wilosUserId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);
		Participant participant = this.participantService
				.getParticipant(wilosUserId);
		String role = (String) WebSessionService
				.getAttribute(WebSessionService.ROLE_TYPE);

		boolean b = false;

		if (role.equals("participant")) {
			b = concreteWorkProductDescriptorService.checkAffectation(
					this.concreteWorkProductDescriptor, participant);
		}

		if (role.equals("projectDirector")) {
			b = true;
		}

		return b;
	}

	/**
	 * Method to confim delete action on a popup
	 * 
	 * @param _event
	 */
	public void confirmDelete(ActionEvent _event) {

		if (!this.getChangeButtonIsDisabled()
				&& (this.concreteWorkProductDescriptor.getState().equals(
						State.CREATED) || this.concreteWorkProductDescriptor
						.getState().equals(State.READY))) {

			ConcreteWorkProductDescriptor cwd = this.concreteWorkProductDescriptorService
					.getConcreteWorkProductDescriptor(this.concreteWorkProductDescriptor
							.getId());

			cwd = this.concreteWorkProductDescriptorService
					.deleteConcreteWorkProductDescriptor(cwd);
			if(cwd == null){
				//la suppression c'est bien passé, 
				//on retourne sur la page du projet et on affiche un message de confirmation
				WebCommonService.changeContentPage(WilosObjectNode.PROJECTNODE);
				/* Displays info message */
				WebCommonService.addInfoMessage(LocaleBean
						.getText("concreteWorkProductViewer.removed"));
			}else{
				System.out.println("la : "+cwd.getParticipant());
				if(cwd.getParticipant() == null){
					//on affiche un message d'erreur disant qu'un tache lie
					//au produit est commencé
					WebCommonService.addErrorMessage(LocaleBean
							.getText("concreteWorkProductViewer.removed.failed.task"));
				}else{
					//on affiche un message d'erreur disant que le produit est affecte a quelqu'un
					WebCommonService.addErrorMessage(LocaleBean
						.getText("concreteWorkProductViewer.removed.failed.assign"));
				}
			}

			// Refresh components.
			//super.refreshProjectTable();
//			ProjectAdvancementWorkProductBean pawp = (ProjectAdvancementWorkProductBean) WebCommonService
	//				.getBean("ProjectAdvancementWorkProductBean");
		//	pawp.refreshProjectTable();
			// super.refreshProjectTree();
			super.rebuildProjectTree();
			this.visibleDeletePopup = false;
		}
	}

	/**
	 * Method to set not visible the popup to cancel delete action
	 * 
	 * @param _event
	 */
	public void cancelDelete(ActionEvent _event) {
		this.visibleDeletePopup = false;
	}

	/**
	 * Method to remove listener (set visible the popup)
	 * 
	 * @param event
	 */
	public void removeActionListener(ActionEvent event) {
		this.visibleDeletePopup = true;
	}

	/**
	 * Method to have the concreteWorkProductDescriptor attribute
	 * 
	 * @return concreteWorkProductDescriptor
	 */
	public ConcreteWorkProductDescriptor getConcreteWorkProductDescriptor() {
		return concreteWorkProductDescriptor;
	}

	/**
	 * Method to set concreteWorkProductDescriptor attribute
	 * 
	 * @param concreteWorkProductDescriptor
	 */
	public void setConcreteWorkProductDescriptor(
			ConcreteWorkProductDescriptor concreteWorkProductDescriptor) {
		this.concreteWorkProductDescriptor = concreteWorkProductDescriptor;
	}

	/**
	 * Method to have the concreteWorkProductDescriptorService attribute
	 * 
	 * @return concreteWorkProductDescriptorService
	 */
	public ConcreteWorkProductDescriptorService getConcreteWorkProductDescriptorService() {
		return concreteWorkProductDescriptorService;
	}

	/**
	 * Method to set the concreteWorkProductDescriptorService attribute
	 * 
	 * @param concreteWorkProductDescriptorService
	 */
	public void setConcreteWorkProductDescriptorService(
			ConcreteWorkProductDescriptorService concreteWorkProductDescriptorService) {
		this.concreteWorkProductDescriptorService = concreteWorkProductDescriptorService;
	}

	/**
	 * @return the participantService
	 */
	public ParticipantService getParticipantService() {
		return participantService;
	}

	/**
	 * Setter of participantService.
	 * 
	 * @param participantService
	 *            The participantService to set.
	 */
	public void setParticipantService(ParticipantService participantService) {
		this.participantService = participantService;
	}

	/**
	 * save the ConcreteWorkProductDescriptor
	 * 
	 * @param event
	 */
	public void updateActionListener(ActionEvent event) {
		this.concreteWorkProductDescriptorService
				.saveConcreteWorkProductDescriptor(this.concreteWorkProductDescriptor);
		WebCommonService.addInfoMessage(LocaleBean
				.getText("concreteworkproductviewer.updateMessage"));

		super.refreshProjectTable();
		ProjectAdvancementWorkProductBean pawp = (ProjectAdvancementWorkProductBean) WebCommonService
				.getBean("ProjectAdvancementWorkProductBean");
		pawp.refreshProjectTable();
	}

	/**
	 * @return the concreteRoleDescriptorService
	 */
	public ConcreteRoleDescriptorService getConcreteRoleDescriptorService() {
		return concreteRoleDescriptorService;
	}

	/**
	 * @param concreteRoleDescriptorService
	 *            the concreteRoleDescriptorService to set
	 */
	public void setConcreteRoleDescriptorService(
			ConcreteRoleDescriptorService concreteRoleDescriptorService) {
		this.concreteRoleDescriptorService = concreteRoleDescriptorService;
	}

	/**
	 * @return the roleDescriptorService
	 */
	public RoleDescriptorService getRoleDescriptorService() {
		return roleDescriptorService;
	}

	/**
	 * @param roleDescriptorService
	 *            the roleDescriptorService to set
	 */
	public void setRoleDescriptorService(
			RoleDescriptorService roleDescriptorService) {
		this.roleDescriptorService = roleDescriptorService;
	}

	/**
	 * @return the workProductDescriptorService
	 */
	public WorkProductDescriptorService getWorkProductDescriptorService() {
		return workProductDescriptorService;
	}

	/**
	 * @param workProductDescriptorService
	 *            the workProductDescriptorService to set
	 */
	public void setWorkProductDescriptorService(
			WorkProductDescriptorService workProductDescriptorService) {
		this.workProductDescriptorService = workProductDescriptorService;
	}

	/**
	 * Method to known if popup is visible or not
	 * 
	 * @return true if popup is visible
	 */
	public boolean getVisiblePopup() {
		return visiblePopup;
	}

	/**
	 * Method to set popup visible or not
	 * 
	 * @param visiblePopup
	 *            true to set visible
	 */
	public void setVisiblePopup(boolean visiblePopup) {
		this.visiblePopup = visiblePopup;
	}

	/**
	 * Method to known if delete popup is visible or not
	 * 
	 * @return true if popup is visible
	 */
	public boolean isVisibleDeletePopup() {
		return visibleDeletePopup;
	}

	/**
	 * Method to set delete popup visible or not
	 * 
	 * @param _visibleDeletePopup
	 */
	public void setVisibleDeletePopup(boolean _visibleDeletePopup) {
		this.visibleDeletePopup = _visibleDeletePopup;
	}

	/**
	 * @return the visibleRemoved
	 */
	public boolean getVisibleRemove() {
		return (!this.getChangeButtonIsDisabled() && (this.concreteWorkProductDescriptor
				.getState().equals(State.CREATED)
		/*
		 * || this.concreteWorkProductDescriptor .getState().equals(State.READY)
		 */
		));
	}

	/**
	 * @return the displayedResponsibleRoleVisible
	 */
	public boolean getDisplayedResponsibleRoleVisible() {
		this.displayedResponsibleRoleVisible = false;
		if (this.concreteWorkProductDescriptor.getState().equals(State.CREATED)) {
			this.displayedResponsibleRoleVisible = true;
		} else {
			this.displayedResponsibleRoleVisible = false;
		}
		return this.displayedResponsibleRoleVisible;
	}

	/**
	 * @param _displayedResponsibleRoleVisible
	 *            the displayedResponsibleRoleVisible to set
	 */
	public void setDisplayedResponsibleRoleVisible(
			boolean _displayedResponsibleRoleVisible) {
		this.displayedResponsibleRoleVisible = _displayedResponsibleRoleVisible;
	}

	/**
	 * @return displayedParticipantVisible
	 */
	public boolean getDisplayedParticipantVisible() {
		this.displayedParticipantVisible = this.concreteWorkProductDescriptor
				.getParticipant() != null;
		return this.displayedParticipantVisible;
	}

	/**
	 * Method to set the displayedParticipantVisible attribute
	 * 
	 * @param _displayedParticipantVisible
	 */
	public void setDisplayedParticipantVisible(
			boolean _displayedParticipantVisible) {
		this.displayedParticipantVisible = _displayedParticipantVisible;
	}

	/**
	 * Method to have Name and Firstname of participant
	 * 
	 * @return an empty string if participant is empty
	 */
	public String getDisplayedParticipant() {

		String pa = this.concreteWorkProductDescriptor.getParticipant();
		Participant participant = this.participantService.getParticipant(pa);
		if (participant == null) {
			return ("");
		} else {
			return (participant.getName() + " " + participant.getFirstname());
		}
	}

	/**
	 * Method to have entry state of activity
	 * 
	 * @return an empty string if entryState is empty
	 */
	public String getDisplayActivityEntryState() {
		String entrystate = this.concreteWorkProductDescriptor
				.getWorkProductDescriptor().getActivityEntryState();

		if (!entrystate.equals("")) {
			return entrystate;
		} else {
			return LocaleBean
					.getText("concreteWorkProductViewer.noConcreteRole");
		}
	}

	/**
	 * Method to have exit state of activity
	 * 
	 * @return an empty string if exitState is empty
	 */
	public String getDisplayActivityExitState() {
		String exitstate = this.concreteWorkProductDescriptor
				.getWorkProductDescriptor().getActivityExitState();

		if (!exitstate.equals("")) {
			return exitstate;
		} else {
			return LocaleBean
					.getText("concreteWorkProductViewer.noConcreteRole");
		}
	}

	/**
	 * Method to have a string describing the project state (selected, affected,
	 * instanciated)
	 * 
	 * @return string
	 */
	public String getDisplayedPanelManager() {
		String projectId = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);
		if ((projectId == null) || (projectId.equals("default"))) {
			this.setDisplayedPanelManager("projectNotSelected");
		} else if (this.concreteWorkProductDescriptorService
				.getAllConcreteWorkProductDescriptorsForProject(projectId)
				.size() == 0) {
			this.setDisplayedPanelManager("projectNotInstanciated");
		} else {
			// ???
			// String wilosUserId = (String) WebSessionService
			// .getAttribute(WebSessionService.WILOS_USER_ID);
			// if (this.participantService.getParticipant(wilosUserId) == null)
			// {
			// this.setDisplayedPanelManager("productNotAffected");
			// } else {
			this.setDisplayedPanelManager("productAffectedTo");
			// }
		}
		return this.displayedPanelManager;
	}

	/**
	 * Method t set displayedPanelManager attribute
	 * 
	 * @param displayedPanelManager
	 */
	public void setDisplayedPanelManager(String displayedPanelManager) {
		this.displayedPanelManager = displayedPanelManager;
	}

	/**
	 * ChangeListener on the Combobox of the workproduct states
	 * 
	 * @param e
	 */
	public void changeWorkProductStateListener(ValueChangeEvent evt) {
		this.selectedWorkProductState = (String) evt.getNewValue();
	}

	/**
	 * Give all states save in the database for the given workproduct
	 * 
	 * @return
	 */
	public List<SelectItem> getWorkProductStates() {

		List<SelectItem> workProductStatesList = new ArrayList<SelectItem>();

		workProductStatesList.add(new SelectItem("default", LocaleBean
				.getText("concreteWorkProductViewer.comboboxDefaultChoice")));

		workProductStatesList.add(new SelectItem(
				this.concreteWorkProductDescriptor.getWorkProductDescriptor()
						.getActivityEntryState(),
				this.concreteWorkProductDescriptor.getWorkProductDescriptor()
						.getActivityEntryState()));

		workProductStatesList.add(new SelectItem(
				this.concreteWorkProductDescriptor.getWorkProductDescriptor()
						.getActivityExitState(),
				this.concreteWorkProductDescriptor.getWorkProductDescriptor()
						.getActivityExitState()));

		return workProductStatesList;

	}

	/**
	 * @return the selectedWorkProductState
	 */
	public String getSelectedWorkProductState() {
		return this.selectedWorkProductState;
	}

	/**
	 * @param _selectedWorkProductState
	 *            the selectedWorkProductState to set
	 */
	public void setSelectedWorkProductState(String _selectedWorkProductState) {
		this.selectedWorkProductState = _selectedWorkProductState;
	}

	/**
	 * Method to known if combobox is visible or not
	 * 
	 * @return
	 */
	public boolean getVisibleWorkProductStateCombobox() {
		if (this.concreteWorkProductDescriptor.getWorkProductDescriptor()
				.getActivityExitState().equals("")
				&& this.concreteWorkProductDescriptor
						.getWorkProductDescriptor().getActivityEntryState()
						.equals("")) {
			return (this.displayedParticipantVisible && false);
		} else {
			return (this.displayedParticipantVisible && true);
		}
	}

	/**
	 * Method to change actionListener state of workProduct
	 * 
	 * @param event
	 */
	public void changeStateActionListener(ActionEvent event) {
		if (!this.selectedWorkProductState.equals("default")) {
			this.concreteWorkProductDescriptor
					.setState(this.selectedWorkProductState);
			this.concreteWorkProductDescriptorService
					.getConcreteWorkProductDescriptorDao()
					.saveOrUpdateConcreteWorkProductDescriptor(
							this.concreteWorkProductDescriptor);

			WebCommonService.addInfoMessage(LocaleBean
					.getText("concreteWorkProductViewer.updateState"));
			this.selectedWorkProductState = "default";
		}

	}

	/**
	 * getter of workProductAffectedView
	 * 
	 * @return String
	 */
	public String getWorkProductAffectedView() {
		String projectId = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);

		if ((projectId == null) || (projectId.equals("default"))) {
			this.setWorkProductAffectedView("noSelectedProjectPanel");
			// System.out.println("noSelectedProjectPanel");
		} else if (this.concreteWorkProductDescriptorService
				.getAllConcreteWorkProductDescriptorsForProject(projectId)
				.size() == 0) {
			this.setWorkProductAffectedView("projectNotInstanciated");
			// System.out.println("projectNotInstanciated");
		} else if (this.getAllConcreteWorkProduct().size() == 0) {
			this.setWorkProductAffectedView("NoWorkProductAffected");
			// System.out.println("NoWorkProductAffected");
		} else {
			this.setWorkProductAffectedView("allWorkProductAffected");
			// System.out.println("allWorkProductAffected");
		}
		return this.workProductAffectedView;

	}

	/**
	 * Setter of WorkProductAffectedView
	 * 
	 * @param _workProductAffectedView
	 */
	public void setWorkProductAffectedView(String _workProductAffectedView) {
		this.workProductAffectedView = _workProductAffectedView;
	}

	/**
	 * Method to have all concrete work product
	 * 
	 * @return list
	 */
	public List<HashMap<String, Object>> getAllConcreteWorkProduct() {
		List<HashMap<String, Object>> listHashMapTask = new ArrayList<HashMap<String, Object>>();
		List<ConcreteWorkProductDescriptor> tabConcreteWorkProductDescriptor;

		String projectID = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);
		String wilosUserId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);

		if (this.getSelectedMode().equals(MY_WP_MODE)) {
			listHashMapTask = getMyConcreteWorkProduct();

		} else {
			Participant parti = this.participantService
					.getParticipant(wilosUserId);

			tabConcreteWorkProductDescriptor = this.concreteWorkProductDescriptorService
					.getAllConcreteWorkProductDescriptorsForProject(projectID);

			for (ConcreteWorkProductDescriptor cwpd : tabConcreteWorkProductDescriptor) {

				HashMap<String, Object> tmpHashMap = new HashMap<String, Object>();

				Participant responsible = this.concreteWorkProductDescriptorService
						.getParticipant(cwpd);

				if (responsible != null) {
					// the workproduct is affected
					String responsibleID = responsible.getId();

					tmpHashMap.put("responsible", this.participantService
							.getParticipant(responsibleID).getName());

					tmpHashMap.put("workProductName", cwpd.getConcreteName());
					tmpHashMap.put("workProductID", cwpd.getId());
					WorkProductDescriptor work = this.concreteWorkProductDescriptorService
							.getWorkProductDescriptor(cwpd);

					String requiredRole = this.workProductDescriptorService
							.getNameRequiredRoleWorkProductDescriptor(work);
					if (requiredRole != null) {
						tmpHashMap.put("requiredRole", requiredRole);
					} else {
						tmpHashMap
								.put(
										"requiredRole",
										LocaleBean
												.getText("tabAffectWorkProduct.noRequieredRole"));
					}

					tmpHashMap.put("visibleCheckbox", true);
					tmpHashMap.put("isAffected", true);

					if (responsibleID.equals(parti.getId())) {
						// workproduct is affected to the current participant

						tmpHashMap.put("enabled", false);
					} else {
						// the workproduct is affected to an another participant
						tmpHashMap.put("enabled", true);

					}
					listHashMapTask.add(tmpHashMap);
				} else {
					// the workproduct is not affected
					tmpHashMap.put("responsible", LocaleBean
							.getText("tabAffectWorkProduct.noResponsible"));

					tmpHashMap.put("workProductName", cwpd.getConcreteName());
					tmpHashMap.put("workProductID", cwpd.getId());
					WorkProductDescriptor work = this.concreteWorkProductDescriptorService
							.getWorkProductDescriptor(cwpd);

					String requiredRole = this.workProductDescriptorService
							.getNameRequiredRoleWorkProductDescriptor(work);
					if (requiredRole != null) {
						tmpHashMap.put("requiredRole", requiredRole);

						// verify if the current participant have the required
						// role
						RoleDescriptor rd = this.workProductDescriptorService
								.getRequiredRoleWorkProductDescriptor(work);

						if (this.participantHaveRole(parti, projectID, rd)) {// the
							// participant
							// have
							// the
							// required
							// role
							tmpHashMap.put("enabled", false);
						} else {// the participant haven't the required role
							tmpHashMap.put("enabled", true);
						}
					} else {
						tmpHashMap
								.put(
										"requiredRole",
										LocaleBean
												.getText("tabAffectWorkProduct.noRequieredRole"));
						tmpHashMap.put("enabled", false);
					}

					tmpHashMap.put("visibleCheckbox", true);
					tmpHashMap.put("isAffected", false);
					listHashMapTask.add(tmpHashMap);
				}

			}
		}

		this.setTabWorkProductAffected(listHashMapTask);
		return this.tabWorkProductAffected;
	}

	/**
	 * Method to have my concrete work product
	 * 
	 * @return list
	 */
	public List<HashMap<String, Object>> getMyConcreteWorkProduct() {
		List<HashMap<String, Object>> listHashMapTask = new ArrayList<HashMap<String, Object>>();
		List<ConcreteWorkProductDescriptor> tabConcreteWorkProductDescriptor;

		String projectID = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);
		String wilosUserId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);

		Participant parti = this.participantService.getParticipant(wilosUserId);

		tabConcreteWorkProductDescriptor = this.concreteWorkProductDescriptorService
				.getAllConcreteWorkProductDescriptorsForProject(projectID);

		for (ConcreteWorkProductDescriptor cwpd : tabConcreteWorkProductDescriptor) {

			HashMap<String, Object> tmpHashMap = new HashMap<String, Object>();

			Participant responsible = this.concreteWorkProductDescriptorService
					.getParticipant(cwpd);

			if (responsible != null) {

				String responsibleID = responsible.getId();

				if (responsibleID.equals(parti.getId())) {
					// the workproduct is affected to the current participant

					tmpHashMap.put("responsible", this.participantService
							.getParticipant(responsibleID).getName());

					tmpHashMap.put("workProductName", cwpd.getConcreteName());
					tmpHashMap.put("workProductID", cwpd.getId());
					WorkProductDescriptor work = this.concreteWorkProductDescriptorService
							.getWorkProductDescriptor(cwpd);

					String requiredRole = this.workProductDescriptorService
							.getNameRequiredRoleWorkProductDescriptor(work);
					if (requiredRole != null) {
						tmpHashMap.put("requiredRole", requiredRole);
					} else {
						tmpHashMap
								.put(
										"requiredRole",
										LocaleBean
												.getText("tabAffectWorkProduct.noRequieredRole"));
					}

					tmpHashMap.put("visibleCheckbox", true);
					tmpHashMap.put("isAffected", true);

					tmpHashMap.put("enabled", false);

					listHashMapTask.add(tmpHashMap);
				}
			}

		}

		return listHashMapTask;
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
		boolean modif = false;
		String wilosUserId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);
		ParticipantService pService = this.getParticipantService();
		Participant parti = pService.getParticipant(wilosUserId);

		ConcreteWorkProductDescriptorService cwpdService = this.concreteWorkProductDescriptorService;

		for (HashMap<String, Object> ligne : this.getTabWorkProductAffected()) {

			if ((Boolean) ligne.get("visibleCheckbox")) {
				if (!(Boolean) ligne.get("enabled")) {

					/*
					 * ConcreteWorkProductDescriptor cwpd =
					 * (ConcreteWorkProductDescriptor) ligne
					 * .get("workPProduct");
					 */

					ConcreteWorkProductDescriptor cwpd = this.concreteWorkProductDescriptorService
							.getConcreteWorkProductDescriptor(ligne.get(
									"workProductID").toString());

					cwpdService.getConcreteWorkProductDescriptorDao()
							.saveOrUpdateConcreteWorkProductDescriptor(cwpd);

					if (cwpd != null) {
						if ((Boolean) ligne.get("isAffected")) {

							cwpdService.affectedConcreteWorkProductDescriptor(
									cwpd, parti);

							cwpdService.saveConcreteWorkProductDescriptor(cwpd);

							modif = true;
						} else if (!(Boolean) ligne.get("isAffected")) {

							cwpdService
									.dissociateConcreteWorkProductDescriptor(
											cwpd, parti);

							cwpdService.saveConcreteWorkProductDescriptor(cwpd);

							modif = true;
						}
					}
				}
			}
		}

		if (modif) {
			WebCommonService.addInfoMessage(LocaleBean
					.getText("tabAffectWorkProduct.updateAffected"));
			TreeBean tb = (TreeBean) WebCommonService.getBean("TreeBean");
			tb.setSelectedMode(tb.WORKPRODUCTS_MODE);
			tb.refreshProjectTree();

		}

	}

	/**
	 * Method to have tabWorkProductAffected
	 * 
	 * @return tabWorkProductAffected
	 */
	public List<HashMap<String, Object>> getTabWorkProductAffected() {
		return this.tabWorkProductAffected;
	}

	/**
	 * Method to set tabWorkProductAffected
	 * 
	 * @param _tabWorkProductAffected
	 */
	public void setTabWorkProductAffected(
			List<HashMap<String, Object>> _tabWorkProductAffected) {
		this.tabWorkProductAffected = _tabWorkProductAffected;
	}

	/**
	 * Method to have list of modes
	 * 
	 * @return list
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
	 * Method to change selected mode of tree
	 * 
	 * @param evt
	 */
	public void changeModeActionListener(ValueChangeEvent evt) {
		this.selectedMode = (String) evt.getNewValue();
		if (this.selectedMode.equals(ALL_WP_MODE)) {
			WebSessionService.setAttribute(WebSessionService.TREE_MODE,
					ALL_WP_MODE);
		} else {
			WebSessionService.setAttribute(WebSessionService.TREE_MODE,
					MY_WP_MODE);
		}
	}

	/**
	 * Method to have the selectedMode
	 * 
	 * @return selectedMode
	 */
	public String getSelectedMode() {
		return selectedMode;
	}

	/**
	 * Method to set selectedMode
	 * 
	 * @param selectedMode
	 */
	public void setSelectedMode(String selectedMode) {
		this.selectedMode = selectedMode;
	}

	/**
	 * Give all producers save in the database for the given workproduct
	 * 
	 * @return
	 */
	public List<SelectItem> getProducers() {

		List<SelectItem> producersList = new ArrayList<SelectItem>();

		for (ConcreteTaskDescriptor ctd : this.concreteWorkProductDescriptor
				.getProducerConcreteTasks()) {
			producersList.add(new SelectItem("name", ctd.getConcreteName()));
		}
		return producersList;
	}

	/**
	 * Give all optional users save in the database for the given workproduct
	 * 
	 * @return
	 */
	public List<SelectItem> getOptionalUsers() {

		List<SelectItem> optionalUersList = new ArrayList<SelectItem>();

		for (ConcreteTaskDescriptor ctd : this.concreteWorkProductDescriptor
				.getOptionalUserConcreteTasks()) {
			optionalUersList.add(new SelectItem("name", ctd.getConcreteName()));
		}
		return optionalUersList;
	}

	/**
	 * Give all mandatory users save in the database for the given workproduct
	 * 
	 * @return
	 */
	public List<SelectItem> getMandatoryUsers() {

		List<SelectItem> mandatoryUsersList = new ArrayList<SelectItem>();

		for (ConcreteTaskDescriptor ctd : this.concreteWorkProductDescriptor
				.getMandatoryUserConcreteTasks()) {
			mandatoryUsersList
					.add(new SelectItem("name", ctd.getConcreteName()));
		}
		return mandatoryUsersList;
	}

}
