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

import com.icesoft.faces.component.tree.IceUserObject;

import javax.faces.event.ActionEvent;

import wilos.resources.LocaleBean;

/**
 * This bean is used to build the main page content with the template
 *
 */
public class PageContentBean extends IceUserObject {

    // template, default panel to make visible in a panel stack
    private String templateName = "";
    
    private String templateNameForARole = ""; 

    // text to be displayed in navigation link
    private String menuDisplayText;
    
    // title information to be displayed
    private String menuContentTitle;

    // True indicates that there is content associated with link and as a
    // result templateName and contentPanelName can be used. Otherwise we
    // just toggle the branch visibility.
    private boolean pageContent = true;

    // Each component example needs extra information to be displayed.  These
    // variables store that information

    // message bundle for component.
    /*private static ResourceBundle messages = null;*/

    // view reference to control the visible content
    private MenuBean navigationBean;

    /**
     *Constructor of the page content bean
     */
    public PageContentBean() {
        super(null);
        init();
    }

    /**
     * Initialize the locale of the application depending on the server locale
	 * If the server locale is null it is set to English
     */
    private void init() {

        setExpanded(true);

        /*Locale locale = 
                FacesContext.getCurrentInstance().getViewRoot().getLocale();
        // assign a default locale if the faces context has none, shouldn't happen
        if (locale == null) {
            locale = Locale.ENGLISH;
        }*/
        
       /* messages = ResourceBundle.getBundle(
                "wilos.resources.messages",
                LocaleBean.getLocale());*/
        
    }

    /**
     * Gets the navigation callback.
     *
     * @return NavigationBean.
     */
    public MenuBean getNavigationSelection() {
        return navigationBean;
    }

    /**
     * Sets the navigation callback.
     *
     * @param navigationBean controls selected panel state.
     */
    public void setNavigationSelection(MenuBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    /**
     * Gets the template name to display in the showcase.jspx.  The template is
     * a panel in a panel stack which will be made visible.
     *
     * @return panel stack template name.
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * Sets the template name to be displayed when selected in tree. Selection
     * will only occur if pageContent is true.
     *
     * @param templateName valid panel name in showcase.jspx
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * Gets the menu display text.  This text will be shown in the navigation
     * tree.
     *
     * @return menu display text.
     */
    public String getMenuDisplayText() {
        // get localized string value
        return LocaleBean.getText(menuDisplayText);
    }

    /**
     * Sets the text to be displayed in the menu.  This text string must match a
     * resource property in com.icesoft.icefaces.samples.showcase.resources.messages
     *
     * @param menuDisplayText menu text to display
     */
    public void setMenuDisplayText(String menuDisplayText) {
        if (menuDisplayText != null) {
            this.menuDisplayText = menuDisplayText;
            // set tree node text value
            setText(getMenuDisplayText());
        } else {
            this.menuDisplayText = "";
        }
    }

    /**
     * Get the text to be displayed as the content title.  This text string must
     * match resource property in com.icesoft.icefaces.samples.showcase.resources.messages
     *
     * @return menu content title
     */
    public String getMenuContentTitle() {
        if (menuContentTitle != null && !menuContentTitle.equals("")) {
            return LocaleBean.getText(menuContentTitle);
        } else {
            return "";
        }
    }

    /**
     * Sets the menu content title.
     *
     * @param menuContentTitle menu content title name.
     */
    public void setMenuContentTitle(String menuContentTitle) {
        if (menuContentTitle != null) {
            this.menuContentTitle = menuContentTitle;
        } else {
            this.menuContentTitle = "";
        }
    }

    /**
     * Does the node contain content.
     *
     * @return true if the page contains content; otherwise, false.
     */
    public boolean isPageContent() {
        return pageContent;
    }

    /**
     * Sets the page content.
     *
     * @param pageContent True if the page contains content; otherwise, false.
     */
    public void setPageContent(boolean pageContent) {
        this.pageContent = pageContent;
    }

    /**
     * Sets the navigationSelectionBeans selected state
     */
    public void contentVisibleAction(ActionEvent event) {
        if (isPageContent()) {
            // only toggle the branch expansion if we have already selected the node
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
	 * Getter of templateNameForARole.
	 *
	 * @return the templateNameForARole.
	 */
	public String getTemplateNameForARole() {
		return this.templateNameForARole ;		
	}

	/**
	 * Setter of templateNameForARole.
	 *
	 * @param _templateNameForARole The templateNameForARole to set.
	 */
	public void setTemplateNameForARole(String _templateNameForARole) {
		this.templateNameForARole = _templateNameForARole ;
	}
}