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

import java.util.Locale;
import java.util.ResourceBundle;

import com.icesoft.faces.component.tree.IceUserObject;

import javax.faces.event.ActionEvent;

import wilos.resources.LocaleBean;

/**
 * This bean is used to manage the display zone for the connection area.
 * 
 * @author sadasblackmilk
 */
public class ConnectContentBean extends IceUserObject {

	private String templateName = "";

	private String templateNameActions = "";

	private String templateNameMenu = "";

	private String templateNameSelection = "";

	// True indicates that there is content associated with link and as a
	// result templateName and contentPanelName can be used
	private boolean pageContent = true;

	// view reference to control the visible content
	private ConnectViewBean navigationBean;

	private ResourceBundle messages;

	/**
	 * Constructor of the class ConnectContentBean
	 */
	public ConnectContentBean() {
		super(null);
		init();
	}

	/**
	 * Initialize the locale of the application depending on the server locale
	 * If the server locale is null it is set to English
	 */
	private void init() {

		setExpanded(true);

		/* locale = FacesContext.getCurrentInstance().getViewRoot()
				.getLocale();
		// assign a default locale if the faces context has none, shouldn't
		// happen
		if (locale == null) {
			locale = Locale.ENGLISH;
		}
		messages = ResourceBundle.getBundle("wilos.resources.messages", locale);*/
		messages = ResourceBundle.getBundle(
			"wilos.resources.messages", Locale.getDefault());
	}

	/**
	 * Gets the navigation callback.
	 * @return NavigationBean.
	 */
	public ConnectViewBean getNavigationSelection() {
		return navigationBean;
	}

	/**
	 * Sets the navigation callback.
	 * @param navigationBean controls selected panel state.
	 */
	public void setNavigationSelection(ConnectViewBean navigationBean) {
		this.navigationBean = navigationBean;
	}

	/**
	 * Getter for TemplateName
	 * @return panel stack template name.
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * Setter of templateName
	 * @param templateName valid panel name
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * This method permits to know if the page contains content or not 
	 * @return true if the page contains content; otherwise, false.
	 */
	public boolean isPageContent() {
		return pageContent;
	}

	/**
	 * Sets the page content. 
	 * @param pageContent True if the page contains content; otherwise, false.
	 */
	public void setPageContent(boolean pageContent) {
		this.pageContent = pageContent;
	}

	/**
	 * Sets the navigationSelectionBeans selected state
	 * @param event event occurring
	 */
	public void contentVisibleAction(ActionEvent event) {
		if (isPageContent()) {
			// only toggle the branch expansion if we have already selected the
			// node
			if (navigationBean.getSelectedPanel().equals(this)) {
				// toggle the branch node expansion
				setExpanded(!isExpanded());
			}
			navigationBean.setSelectedPanel(this);
		}
		// Otherwise toggle the node visibility, only changes the state
		// of the nodes with children.
		else {
			setExpanded(!isExpanded());
		}
	}

	/**
	 * Getter of templateNameActions.
	 * @return the templateNameActions.
	 */
	public String getTemplateNameActions() {
		return this.templateNameActions;
	}

	/**
	 * Setter of templateNameActions.
	 * @param _templateNameActions  The templateNameActions to set.
	 */
	public void setTemplateNameActions(String _templateNameActions) {
		this.templateNameActions = _templateNameActions;
	}

	/**
	 * Getter of templateNameMenu. 
	 * @return the templateNameMenu.
	 */
	public String getTemplateNameMenu() {
		return this.templateNameMenu;
	}

	/**
	 * Setter of templateNameMenu. 
	 * @param _templateNameMenu The templateNameMenu to set.
	 */
	public void setTemplateNameMenu(String _templateNameMenu) {
		this.templateNameMenu = _templateNameMenu;
	}

	/**
	 * Getter of templateNameSelection. 
	 * @return the templateNameSelection.
	 */
	public String getTemplateNameSelection() {
		return this.templateNameSelection;
	}

	/**
	 * Setter of templateNameSelection. 
	 * @param _templateNameSelection The templateNameSelection to set.
	 */
	public void setTemplateNameSelection(String _templateNameSelection) {
		this.templateNameSelection = _templateNameSelection;
	}
}