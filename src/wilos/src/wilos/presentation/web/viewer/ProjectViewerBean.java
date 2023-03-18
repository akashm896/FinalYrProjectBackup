/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

import java.util.ArrayList;
import java.util.List;

import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.project.Project;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;
import wilos.resources.LocaleBean;

public class ProjectViewerBean extends ViewerBean {

    /**
     * The current project
     */
    private Project project;

    /**
     * true if the table is empty
     */
    private boolean isEmptyTable;
    
    /**
     * Method to save the project name and update the treebean
     */
    public void saveName() {
	// add the check of the fact that this name already exists in the db.
	if (this.project.getConcreteName().trim().length() == 0) {
	    // re-put the former concrete name.
	    Project p = super.getProjectService().getProject(
		    this.project.getId());
	    this.project.setConcreteName(p.getConcreteName());

	    // add error message.
	    WebCommonService.addErrorMessage(LocaleBean
		    .getText("viewer.err.checkNameBySaving"));
	} else if (this.presentationNameAlreadyExists(this.project
		.getConcreteName(), this.project.getId())) {
	    // re-put the former concrete name.
	    Project p = super.getProjectService().getProject(
		    this.project.getId());
	    this.project.setConcreteName(p.getConcreteName());

	    // add error message.
	    WebCommonService
		    .addErrorMessage(LocaleBean
			    .getText("component.projectcreate.err.projectalreadyexists"));
	} else {
	    super.getProjectService().saveProject(this.project);

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
     * Method to known if project name exists in this project id
     * @param _concreteName String nam of project
     * @param _projectId String id of project
     * @return true if _concreteName already exists in _projectId project
     */
    private boolean presentationNameAlreadyExists(String _concreteName,
	    String _projectId) {
	for (Project project : super.getProjectService().getAllProjects())
	    if ((project.getConcreteName().equals(_concreteName))
		    && (!_projectId.equals(project.getId())))
		return true;
	return false;
    }

    /**
     * Method to known concreteBreakdownElement of current project
     * @return list of concreteBreakdownElement of this project
     */
    public List<ConcreteBreakdownElement> getConcreteBreakdownElementsList() {
	List<ConcreteBreakdownElement> list = new ArrayList<ConcreteBreakdownElement>();
	if (this.project != null) {
	    // list.addAll(this.project.getConcreteBreakdownElements());
	    list.addAll(this.getProjectService()
		    .getConcreteBreakdownElementsFromProject(this.project));
	}

	// Filter to obtain only concreteworkbreakdownelement (without
	// concreterole).
	List<ConcreteBreakdownElement> cwbdes = new ArrayList<ConcreteBreakdownElement>();
	for (ConcreteBreakdownElement cbde : list) {
	    if (cbde instanceof ConcreteWorkBreakdownElement)
		cwbdes.add(cbde);
	}
	WebSessionService.setAttribute(
		WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT, this.project
			.getId());
	return cwbdes;
    }

    /**
     * Method to save project and update the treebean
     */
    public void saveProject() {
	super.getConcreteBreakdownElementService()
		.saveAllFirstSonsConcreteBreakdownElementsForConcreteActivity(
			this.project);

	// successful message.
	WebCommonService.addInfoMessage(LocaleBean
		.getText("viewer.visibility.successMessage"));

	// Reload the treebean.
	super.rebuildProjectTree();
    }

    /**
     * Method to have current project
     * @return projet 
     */
    public Project getProject() {
	return project;
    }

    /**
     * Method to set the project
     * @param project the new project to replace current project
     */
    public void setProject(Project project) {
	this.project = project;
	//modify the id of the project in the session
	WebSessionService.setAttribute(
		WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT, this.project
			.getId());
    }

    /**
     * Method to known if the concreteBreakdownElements table is empty or not
     * and set the attribute isEmptyTable
     * @return true if the concreteBreakdownElements table is empty
     */
    public boolean getIsEmptyTable() {
	if (getConcreteBreakdownElementsList().size() == 0) {
	    isEmptyTable = true;
	} else {
	    isEmptyTable = false;
	}
	return isEmptyTable;
    }

    /**
     * Method to set the attribute isEmptyTable
     * @param isEmptyTable boolean
     */
    public void setIsEmptyTable(boolean isEmptyTable) {
	this.isEmptyTable = isEmptyTable;
    }
    
    /**
     * Method to known if current project is projectDirector or not
     * @return true if current project is projectDirector.
     */
    public boolean getIsProjectDirector()
    {
    	String role = (String)WebSessionService.getAttribute(WebSessionService.ROLE_TYPE);
    	if (role.equals("projectDirector"))
    		return true;
    	else
    		return false;
    }
    
    /**
     * Method which return the state of the change button
     * true is this one is disable, else false
     * @return boolean
     */
    public boolean getChangeNameProjectButtonIsDisabled() {
	String wilosUserId = (String) WebSessionService
		.getAttribute(WebSessionService.WILOS_USER_ID);
	String projectId = (String) WebSessionService
		.getAttribute(WebSessionService.PROJECT_ID);

	if (projectId == null)
	    return true;

	Project project = super.getProjectService().getProject(projectId);

	if ((project != null)
		&& ((project.getProjectDirector() != null) && (project
			.getProjectDirector().equals(wilosUserId))))
	    return false;
	else
	    return true;
    }
    
}