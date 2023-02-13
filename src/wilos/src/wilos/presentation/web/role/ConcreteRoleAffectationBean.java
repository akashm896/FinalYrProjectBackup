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

package wilos.presentation.web.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wilos.business.services.misc.concreterole.ConcreteRoleAffectationService;
import wilos.business.services.misc.wilosuser.ParticipantService;
import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.presentation.web.project.ProjectAdvancementBean;
import wilos.presentation.web.tree.TreeBean;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;
import wilos.resources.LocaleBean;

/**
 * Managed-Bean link to participant_logging.jsp
 * 
 */
public class ConcreteRoleAffectationBean {

	private ConcreteRoleAffectationService concreteRoleAffectationService;

	private ParticipantService participantService;

	private List<HashMap<String, Object>> concreteRolesDescriptorsList;

	private String nodeId;

	private String oldNodeId;

	private String selectRolesView;

	protected final Log logger = LogFactory.getLog(this.getClass());

	public ConcreteRoleAffectationBean() {
		this.concreteRolesDescriptorsList = new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * Getter of selectAffectedProjectView.
	 * 
	 * @return the selectAffectedProjectView.
	 */
	public String getSelectRolesView() {
		if (this.getConcreteRolesDescriptorsList().size() == 0) {
			this.selectRolesView = "no_roles_view";
		} else {
			this.selectRolesView = "roles_view";
		}
		return this.selectRolesView;
	}

	/**
	 * Setter of selectAffectedProjectView.
	 * 
	 * @param _selectAffectedProjectView
	 *            The selectAffectedProjectView to set.
	 */
	public void setSelectRolesView(String _selectRolesView) {
		this.selectRolesView = _selectRolesView;
	}

	/**
	 * Getter of participantService.
	 * 
	 * @return the participantService.
	 */
	public ParticipantService getParticipantService() {
		return this.participantService;
	}

	/**
	 * Setter of participantService.
	 * 
	 * @param _participantService
	 *            The participantService to set.
	 */
	public void setParticipantService(ParticipantService _participantService) {
		this.participantService = _participantService;
	}

	/**
	 * Getter of concreteRoleAffectationService.
	 * 
	 * @return the concreteRoleAffectationService.
	 */
	public ConcreteRoleAffectationService getConcreteRoleAffectationService() {
		return this.concreteRoleAffectationService;
	}

	/**
	 * Setter of concreteRoleAffectationService.
	 * 
	 * @param _concreteRoleAffectationService
	 *            The concreteRoleAffectationService to set.
	 */
	public void setConcreteRoleAffectationService(
			ConcreteRoleAffectationService _concreteRoleAffectationService) {
		this.concreteRoleAffectationService = _concreteRoleAffectationService;
	}

	/**
	 * Getter of concreteRolesDescriptorsList.
	 * 
	 * @return the concreteRolesDescriptorsList.
	 */
	public List<HashMap<String, Object>> getConcreteRolesDescriptorsList() {
		this.concreteRolesDescriptorsList.clear();
		List<ConcreteRoleDescriptor> globalCRD = this.concreteRoleAffectationService
				.getAllConcreteRolesDescriptorsForActivity(this.nodeId,
						(String) WebSessionService
								.getAttribute(WebSessionService.PROJECT_ID));
		for (ConcreteRoleDescriptor concreteRoleDescriptor : globalCRD) {

			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("concreteId", concreteRoleDescriptor.getId());
			hm.put("concreteName", concreteRoleDescriptor.getConcreteName());
			hm
					.put(
							"affected",
							((HashMap<String, Boolean>) this
									.getParticipantAffectationForConcreteRoleDescriptor(
											(String) WebSessionService
													.getAttribute(WebSessionService.WILOS_USER_ID),
											concreteRoleDescriptor.getId()))
									.get("affected"));
			hm
					.put(
							"not_allowed",
							((HashMap<String, Boolean>) this
									.getParticipantAffectationForConcreteRoleDescriptor(
											(String) WebSessionService
													.getAttribute(WebSessionService.WILOS_USER_ID),
											concreteRoleDescriptor.getId()))
									.get("not_allowed"));
			this.concreteRolesDescriptorsList.add(hm);
		}
		return this.concreteRolesDescriptorsList;
	}

	/**
	 * this method allows to save the role affectation
	 * of the current user through the value of
	 * concreteRolesDescriptorsList Attribute
	 * 
	 * @return an empty String
	 */
	public String saveConcreteRoleAffectation() {
		for (HashMap<String, Object> concreteRoleInfo : this.concreteRolesDescriptorsList) {
			this.concreteRoleAffectationService.saveParticipantConcreteRoles(
					concreteRoleInfo, (String) WebSessionService
							.getAttribute(WebSessionService.WILOS_USER_ID));
		}
		WebCommonService.addErrorMessage(LocaleBean
				.getText("component.project.projectroles.validationMessage"));

		// refresh the tree
		TreeBean tb = (TreeBean) WebCommonService.getBean("TreeBean");
		tb.refreshProjectTree();

		// refresh the project advancement table
		ProjectAdvancementBean pab = (ProjectAdvancementBean) WebCommonService
				.getBean("ProjectAdvancementBean");
		pab.refreshProjectTable();

		return "";
	}

	/**
	 * Method for having the participants list with the affected role
	 * 
	 * @param _wilosUserId
	 * @param _concreteId
	 * @return a hashMap with the role and a boolean to know if the participant is affected or not
	 */

	private HashMap<String, Boolean> getParticipantAffectationForConcreteRoleDescriptor(
			String _wilosUserId, String _concreteId) {
		return this.concreteRoleAffectationService
				.getParticipantAffectationForConcreteRoleDescriptor(
						_wilosUserId, _concreteId);
	}

	/**
	 * Setter of concreteRolesDescriptorsList.
	 * 
	 * @param _concreteRolesDescriptorsList
	 *            The concreteRolesDescriptorsList to set.
	 */
	public void setConcreteRolesDescriptorsList(
			List<HashMap<String, Object>> _concreteRolesDescriptorsList) {
		this.concreteRolesDescriptorsList = _concreteRolesDescriptorsList;
	}

	/**
	 * Getter of nodeId.
	 * 
	 * @return the nodeId.
	 */
	public String getNodeId() {
		return this.nodeId;
	}

	/**
	 * Setter of nodeId.
	 * 
	 * @param _nodeId
	 *            The nodeId to set.
	 */
	public void setNodeId(String _nodeId) {
		this.oldNodeId = this.nodeId;
		this.nodeId = _nodeId;
	}

	/**
	 * Getter of oldNodeId.
	 * 
	 * @return the oldNodeId.
	 */
	public String getOldNodeId() {
		return this.oldNodeId;
	}

	/**
	 * Setter of oldNodeId.
	 * 
	 * @param _oldNodeId
	 *            The oldNodeId to set.
	 */
	public void setOldNodeId(String _oldNodeId) {
		this.oldNodeId = _oldNodeId;
	}
}
