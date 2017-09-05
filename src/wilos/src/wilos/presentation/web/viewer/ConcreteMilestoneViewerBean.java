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

import java.util.HashMap;
import java.util.List;

import javax.faces.event.ActionEvent;

import wilos.business.services.misc.concretemilestone.ConcreteMilestoneService;
import wilos.business.services.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElementService;
import wilos.model.misc.concretemilestone.ConcreteMilestone;
import wilos.presentation.web.utils.WebCommonService;
import wilos.resources.LocaleBean;
import wilos.utils.Constantes;

public class ConcreteMilestoneViewerBean extends ViewerBean {

    private ConcreteMilestone concreteMilestone = null;

    private ConcreteMilestoneService concreteMilestoneService;

    private ConcreteWorkBreakdownElementService concreteWorkBreakdownElementService;

    private List<HashMap<String, Object>> concretePredecessors;

    private List<HashMap<String, Object>> concreteSuccessors;

    /**
     * Set a concrete name of a concreteMilestone
	 * refresh also the treebean
     */
    public void saveName() {
	if (this.concreteMilestone.getConcreteName().trim().length() == 0) {
	    // re-put the form concrete name.
	    ConcreteMilestone cm = this.concreteMilestoneService
		    .getConcreteMilestone(this.concreteMilestone.getId());
	    this.concreteMilestone.setConcreteName(cm.getConcreteName());

	    // add error message.
	    WebCommonService.addErrorMessage(LocaleBean
		    .getText("viewer.err.checkNameBySaving"));
	} else {
	    this.concreteMilestoneService
		    .saveConcreteMilestone(this.concreteMilestone);

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
     * 
     * @param event add a cross on the concreteMilestoneService
     * 		
     * refresh also the ProjectTree and the ProjectTable
     */
    public void crossActionListener(ActionEvent event) {

	this.concreteMilestoneService
		.crossConcreteMilestone(this.concreteMilestone);

	super.refreshProjectTree();
	super.refreshProjectTable();
    }

    /**
     * 
     * @return true if the state of the cross is READY
     */
    public boolean getVisibleCross() {
	return (this.concreteMilestone.getState()
		.equals(Constantes.State.READY));
    }

    /**
     * 
     * @return the attribute concreteMilestone
     */
    public ConcreteMilestone getConcreteMilestone() {
	return concreteMilestone;
    }

    /**
     * 
     * @param _concreteMilestone
     * 		Set the attribute concreteMilestone
     */
    public void setConcreteMilestone(ConcreteMilestone _concreteMilestone) {
	this.concreteMilestone = _concreteMilestone;
    }

    /**
     * 
     * @return the attribute concreteMilestoneService
     */
    public ConcreteMilestoneService getConcreteMilestoneService() {
	return this.concreteMilestoneService;
    }

    /**
     * 
     * @param _concreteMilestoneService
     * 		Set the attribute concreteMilestoneService
     */			
    public void setConcreteMilestoneService(
	    ConcreteMilestoneService _concreteMilestoneService) {
	this.concreteMilestoneService = _concreteMilestoneService;
    }
    
    /**
     * @return the displayed state of the concreteActivity
     */
    public String getDisplayedState() {
	return WebCommonService
		.getDisplayedStateForMilestone(this.concreteMilestone
			.getState());
    }

    /**
     * @return the concreteWorkBreakdownElementService
     */
    public ConcreteWorkBreakdownElementService getConcreteWorkBreakdownElementService() {
	return concreteWorkBreakdownElementService;
    }

    /**
     * @param _concreteWorkBreakdownElementService
     *                the concreteWorkBreakdownElementService to set
     */
    public void setConcreteWorkBreakdownElementService(
	    ConcreteWorkBreakdownElementService _concreteWorkBreakdownElementService) {
	concreteWorkBreakdownElementService = _concreteWorkBreakdownElementService;
    }

    /**
     * 
     * @return true if there are ConcretePredecessors AND ConcreteSuccessors
     * else return false
     */
    public boolean getHasConcreteDependencies() {
	return !(this.getConcretePredecessors().isEmpty() && this
		.getConcreteSuccessors().isEmpty());
    }

    /**
     * 
     * @return true if there are ConcretePredecessors
     * else return false
     */
    public boolean getHasConcretePredecessors() {
	return !this.getConcretePredecessors().isEmpty();
    }

    /**
     * 
     * @return true if there are ConcreteSuccessors
     * else return false
     */
    public boolean getHasConcreteSuccessors() {
	return !this.getConcreteSuccessors().isEmpty();
    }

    /**
     * @return the concretePredecessors
     */
    public List<HashMap<String, Object>> getConcretePredecessors() {
	return this.concreteWorkBreakdownElementService
		.getConcretePredecessorHashMap(this.concreteMilestone);
    }

    /**
     * @param _concretePredecessors
     *                the concretePredecessors to set
     */
    public void setConcretePredecessors(
	    List<HashMap<String, Object>> _concretePredecessors) {
	concretePredecessors = _concretePredecessors;
    }

    /**
     * @return the concreteSuccessors
     */
    public List<HashMap<String, Object>> getConcreteSuccessors() {
	return this.concreteWorkBreakdownElementService
		.getConcreteSuccessorHashMap(this.concreteMilestone);
    }

    /**
     * @param _concreteSuccessors
     *                the concreteSuccessors to set
     */
    public void setConcreteSuccessors(
	    List<HashMap<String, Object>> _concreteSuccessors) {
	concreteSuccessors = _concreteSuccessors;
    }
}
