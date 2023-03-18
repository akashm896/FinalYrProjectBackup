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

package wilos.presentation.web.template;

import wilos.presentation.web.utils.WebSessionService;

public class ConnectViewBean {

	private ConnectContentBean selectedPanel;

	private WebSessionService webSessionService;

	/**
	 * Getter of the web session service
	 * @return the webSessionService
	 */
	public WebSessionService getWebSessionService() {
		return webSessionService;
	}

	/**
	 * Setter of the web session service
	 * @param webSessionService the webSessionService to set
	 */
	public void setWebSessionService(WebSessionService webSessionService) {
		this.webSessionService = webSessionService;
	}

	/**
	 * Gets the currently selected content panel.
	 * @return currently selected content panel.
	 */
	public ConnectContentBean getSelectedPanel() {
		return selectedPanel;
	}

	/**
	 * Sets the selected panel to the specified panel.
	 * @param selectedPanel a none null page content bean.
	 */
	public void setSelectedPanel(ConnectContentBean selectedPanel) {
		if (selectedPanel != null && selectedPanel.isPageContent()) {
			this.selectedPanel = selectedPanel;
		}
	}

	/**
	 * Method used to display the appropriate screen for the connected role
	 * @param connected value showing if the user is connected or not
	 * @param _applicationRole role of the user
	 * @return an empty String (really useful.... yay \o/) 
	 */
	public String connected(boolean _connected, String _applicationRole) {
		ConnectContentBean connectContent = new ConnectContentBean();

		if (_connected) {
			connectContent.setTemplateName("connected");
			connectContent.setTemplateNameActions(_applicationRole);
			if ((_applicationRole == "participant_role") || (_applicationRole.equals("projectDirector_role"))){
				connectContent.setTemplateNameMenu("tree_group");
				connectContent.setTemplateNameSelection("selection_group");
			} else {
				connectContent.setTemplateNameMenu("no_tree_group");
				connectContent.setTemplateNameSelection("no_selection_group");
			}
		} else {
			connectContent.setTemplateName("not_connected");
			connectContent.setTemplateNameActions("none");
			connectContent.setTemplateNameMenu("no_tree_group");
			connectContent.setTemplateNameSelection("no_selection_group");
		}
		connectContent.setNavigationSelection(this);
		this.selectedPanel = connectContent;
		return "";
	}
}
