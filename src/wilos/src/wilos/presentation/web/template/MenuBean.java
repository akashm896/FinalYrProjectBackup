/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Emilien PERICO <eperico@wilos-project.org>
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

/**
 * Bean for managing informations of the menu
 */
public class MenuBean {

	private PageContentBean selectedPanel;

	/**
	 * Gets the currently selected content panel.
	 * @return currently selected content panel.
	 */
	public PageContentBean getSelectedPanel() {
		return selectedPanel;
	}

	/**
	 * Sets the selected panel to the specified panel.
	 * @param selectedPanel a none null page content bean.
	 */
	public void setSelectedPanel(PageContentBean selectedPanel) {
		if (selectedPanel != null && selectedPanel.isPageContent()) {
			this.selectedPanel = selectedPanel;
		}
	}

	/**
	 * Method for a new user subscription
	 */
	public void subscribe() {
		PageContentBean pcb = new PageContentBean();
		pcb.setTemplateName("subscribe");
		pcb.setNavigationSelection(this);
		this.selectedPanel = pcb;
	}
	
	/**
	 * Method for a user who has forgotten his/her password
	 */
	public void forgottenPassword() {
		PageContentBean pcb = new PageContentBean();
		pcb.setTemplateName("forgottenPasswordAskKey");
		pcb.setNavigationSelection(this);
		this.selectedPanel = pcb;
	}
	
	/**
	 * This method redirects to the url which is passed in parameter
	 * 
	 * @param url navigation url for the page to be displayed
	 */
	public void changePage(String url) {
		PageContentBean pcb = new PageContentBean();
		pcb.setTemplateName(url);
		pcb.setNavigationSelection(this);
		this.selectedPanel = pcb;
	}

}
