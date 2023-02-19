/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Mathieu BENOIT <mbenoit@wilos-project.org>
 * Copyright (C) 2007 Emilien PERICO <emilien.perico@free.fr>
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

import javax.faces.event.ActionEvent;

import wilos.business.services.misc.concreterole.ConcreteRoleDescriptorService;
import wilos.business.services.misc.wilosuser.ParticipantService;
import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.wilosuser.Participant;
import wilos.presentation.web.tree.WilosObjectNode;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;
import wilos.resources.LocaleBean;

public class ConcreteRoleViewerBean extends ViewerBean {

	private ConcreteRoleDescriptor concreteRoleDescriptor;

	private ConcreteRoleDescriptorService concreteRoleDescriptorService;

	private String concreteRoleDescriptorId = "";

	private ParticipantService participantService;

	private String displayedPanelManager;

	private boolean visibleDeletePopup = false;

	/**
	 * Method which set the value of the concreteRoleDescriptorId to a default value if this one is null
	 * The default value is the concreteRoleDescriptorId of the concreteRoleDescriptorService
	 */
	public void buildConcreteRoleModel() {
		this.concreteRoleDescriptor = new ConcreteRoleDescriptor();
		if (!(concreteRoleDescriptorId.equals(""))
				|| concreteRoleDescriptorId != null) {
			this.concreteRoleDescriptor = this.concreteRoleDescriptorService
			.getConcreteRoleDescriptor(this.concreteRoleDescriptorId);
		}
	}

	/**
	 * Method which affect and save in the data base the current user to a role
	 * Also refresh the project tree
	 */
	public void affectParticipantToARole() {
		
		String wilosUserId = (String) WebSessionService
		.getAttribute(WebSessionService.WILOS_USER_ID);
		Participant user = this.participantService.getParticipant(wilosUserId);
		//on récupère le rôle apres avoir essayer d'affecter l'utilisateur
		concreteRoleDescriptor = this.concreteRoleDescriptorService.addPartiConcreteRoleDescriptor(concreteRoleDescriptor, user);
		//si le rôle n'est pas supprimé
		if(concreteRoleDescriptor != null){
			//si le rôle est bien affecté à l'utilisateur
			if(concreteRoleDescriptor.getParticipant() == user && concreteRoleDescriptor.getParticipant() != null){
				//message de confirmation
				WebCommonService.addInfoMessage(LocaleBean
						.getText("concreteRoleViewer.success"));
			}else{
				//si l'utilisateur ne correspond pas 
				//message d'erreur indiquant que le role etait deja affecté 
				WebCommonService.addErrorMessage(LocaleBean
						.getText("concreteRoleViewer.failed"));
			}
		}else{
			//si il a été supprimé, redirection sur la description du projet
			WebCommonService.changeContentPage(WilosObjectNode.PROJECTNODE);
			//message d'erreur indiquant que le role a ete supprimé
			WebCommonService.addErrorMessage(LocaleBean
					.getText("concreteRoleViewer.delete"));
			
		}
		// refresh the tree
		super.refreshProjectTree();
	}

	/**
	 * @return the visibleRemove
	 */
	public boolean getVisibleRemove() {
		return (!this.getChangeButtonIsDisabled() && (this.concreteRoleDescriptor
				.getParticipant() == null));
	}

	/**
	 * Delete a concrete role when the specified event is received
	 * @param event
	 */
	public void removeActionListener(ActionEvent event) {
		this.visibleDeletePopup = true;
	}

	/**
	 * Ask a confirmation for the specified event.
	 * Also refresh the project tree
	 * @param _event
	 */
	public void confirmDelete(ActionEvent _event) {
		ConcreteRoleDescriptor crd;
		if (!this.getChangeButtonIsDisabled()
				&& (this.concreteRoleDescriptor.getParticipant() == null)) {
			//on fait appel à la fonction de suppression du rôle
			crd = this.concreteRoleDescriptorService.deleteConcreteRoleDescriptor(this.concreteRoleDescriptor);
			//si on a un retour à null
			if(crd == null){
				//la suppression c'est bien passé, 
				//on retourne sur la page du projet et on affiche un message de confirmation
				WebCommonService.changeContentPage(WilosObjectNode.PROJECTNODE);
				/* Displays info message */
				WebCommonService.addInfoMessage(LocaleBean
						.getText("concreteRoleViewer.removed"));
			}else{
				if(crd.getParticipant() != null){
					//si pas null alors quelqu'un s'est affecté, on actualise l'objet
					this.concreteRoleDescriptor.setParticipant(crd.getParticipant());
					//et on affiche un message d'erreur explicatif
					WebCommonService.addErrorMessage(LocaleBean
						.getText("concreteRoleViewer.failed.removed"));
				}else{
					WebCommonService.addErrorMessage(LocaleBean
							.getText("concreteRoleViewer.failed.removed.link"));
				}
			}
			//on refresh les arbres
			super.rebuildProjectTree();
			super.refreshProjectTree();
			//et on cache la pop-up
			this.visibleDeletePopup = false;
		}
	}

	/**
	 * Method which cancel the deletion of the specified event 
	 * @param _event
	 */
	public void cancelDelete(ActionEvent _event) {
		this.visibleDeletePopup = false;
	}

	/**
	 * action called to change the concrete name of a concrete role
	 */
	public void saveName() {
		if (this.concreteRoleDescriptor.getConcreteName().trim().length() == 0) {
			// re-put the former concrete name.
			ConcreteRoleDescriptor crd = this.concreteRoleDescriptorService
			.getConcreteRoleDescriptor(this.concreteRoleDescriptor
					.getId());
			this.concreteRoleDescriptor.setConcreteName(crd.getConcreteName());

			// add error message.
			WebCommonService.addErrorMessage(LocaleBean
					.getText("viewer.err.checkNameBySaving"));
		} else {
			// successful message.
			WebCommonService.addInfoMessage(LocaleBean
					.getText("viewer.visibility.successMessage"));
		}
		this.concreteRoleDescriptorService
		.saveConcreteRoleDescriptor(this.concreteRoleDescriptor);

		// Refresh the treebean.
		super.refreshProjectTree();

		// put the name text editor to disable.
		super.setNameIsEditable(false);
	}

	/**
	 * Method which return the concreteRoleDescriptor
	 * @return
	 */
	public ConcreteRoleDescriptor getConcreteRoleDescriptor() {
		return concreteRoleDescriptor;
	}

	/**
	 * Method which set the concreteRoleDescriptor 
	 * @param concreteRoleDescriptor
	 */
	public void setConcreteRoleDescriptor(
			ConcreteRoleDescriptor concreteRoleDescriptor) {
		this.concreteRoleDescriptor = concreteRoleDescriptor;
	}

	/**
	 * Method which return the concreteRoleDescriptorId
	 * @return
	 */
	public String getConcreteRoleDescriptorId() {
		return concreteRoleDescriptorId;
	}

	/**
	 * Method which set the concreteRoleDescriptorId
	 * @param concreteRoleDescriptorId
	 */
	public void setConcreteRoleDescriptorId(String concreteRoleDescriptorId) {
		this.concreteRoleDescriptorId = concreteRoleDescriptorId;
	}

	/**
	 * Method which return the concreteRoleDescriptorService
	 * @return concreteRoleDescriptorService
	 */
	public ConcreteRoleDescriptorService getConcreteRoleDescriptorService() {
		return concreteRoleDescriptorService;
	}

	/**
	 * Method which set the concreteRoleDescriptorService
	 * @param concreteRoleDescriptorService
	 */
	public void setConcreteRoleDescriptorService(
			ConcreteRoleDescriptorService concreteRoleDescriptorService) {
		this.concreteRoleDescriptorService = concreteRoleDescriptorService;
	}

	/**
	 * Method which display the user affected to the concreteRole
	 * 	display "projectNotselected" if no project is selected
	 * 	display "projectNotInstancied" if there is no project
	 * 	display "roleNotaffected" is no user is affected to the concreteRole
	 */
	public String getDisplayedPanelManager() {
		String projectId = (String)WebSessionService.getAttribute(WebSessionService.PROJECT_ID);
		if ((projectId == null)
				|| (projectId
						.equals("default"))) {
			this.setDisplayedPanelManager("projectNotSelected");
		} else if (this.concreteRoleDescriptorService.getAllConcreteRoleDescriptorsForProject(projectId).size() == 0) {
			this.setDisplayedPanelManager("projectNotInstanciated");
		} else {
			String wilosUserId = (String) WebSessionService
			.getAttribute(WebSessionService.WILOS_USER_ID);
			if(this.concreteRoleDescriptor==null || this.concreteRoleDescriptor.getParticipant() == null) {
				//if (this.participantService.getParticipant(wilosUserId) == null) {
				this.setDisplayedPanelManager("roleNotAffected");
			} else {
				this.setDisplayedPanelManager("roleAffectedTo");
			}
		}
		return this.displayedPanelManager;
	}

	public String getDisplayedPanelManagerForSeveralRolesAssignment() {
		String projectId = (String) WebSessionService
		.getAttribute(WebSessionService.PROJECT_ID);
		if ((projectId == null) || (projectId.equals("default"))) {
			return "projectNotSelected";
		} else if (this.concreteRoleDescriptorService
				.getAllConcreteRoleDescriptorsForProject(projectId).size() == 0) {
			return "projectNotInstanciated";
		} else {
			String wilosUserId = (String) WebSessionService
			.getAttribute(WebSessionService.WILOS_USER_ID);
			if (this.participantService.getParticipant(wilosUserId) == null) {
				return "roleNotAffected";
			} else {
				return "roleAffectedTo";
			}
		}
	}

	/**
	 * Method which display the PanelManager
	 * That is to say the panel which allow you to see the concreteRoles and the users affected to them
	 * @param displayedPanelManager
	 */
	public void setDisplayedPanelManager(String displayedPanelManager) {
		this.displayedPanelManager = displayedPanelManager;
	}

	/**
	 * Method which return the participantService
	 * @return participantService
	 */
	public ParticipantService getParticipantService() {
		return participantService;
	}

	/**
	 * Method which set the participantService
	 * @param participantService
	 */
	public void setParticipantService(ParticipantService participantService) {
		this.participantService = participantService;
	}

	/**
	 * @return the visibleDeletePopup
	 */
	public boolean getVisibleDeletePopup() {
		return this.visibleDeletePopup;
	}

	/**
	 * @param visibleDeletePopup
	 *            the visibleDeletePopup to set
	 */
	public void setVisibleDeletePopup(boolean _visibleDeletePopup) {
		this.visibleDeletePopup = _visibleDeletePopup;
	}
}