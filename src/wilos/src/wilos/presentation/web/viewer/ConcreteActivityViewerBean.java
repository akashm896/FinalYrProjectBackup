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
import java.util.HashMap;
import java.util.List;

import wilos.business.services.misc.concreteactivity.ConcreteActivityService;
import wilos.business.services.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElementService;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.presentation.web.utils.WebCommonService;
import wilos.resources.LocaleBean;

public class ConcreteActivityViewerBean extends ViewerBean {

    private ConcreteActivity concreteActivity = null;

    private ConcreteActivityService concreteActivityService;

    private ConcreteWorkBreakdownElementService concreteWorkBreakdownElementService;

    private boolean isEmptyTable;

    private List<HashMap<String, Object>> concretePredecessors;

    private List<HashMap<String, Object>> concreteSuccessors;

	private List<HashMap<String, Object>> concrete;
	/**
	 * Set a concrete name of a concreteActivity
	 * refresh also the treebean
	 */
    public void saveName() {
	if (this.concreteActivity.getConcreteName().trim().length() == 0) {
	    // re-put the form concrete name.
	    ConcreteActivity ca = this.concreteActivityService
		    .getConcreteActivity(this.concreteActivity.getId());
	    this.concreteActivity.setConcreteName(ca.getConcreteName());

	    // add error message.
	    WebCommonService.addErrorMessage(LocaleBean
		    .getText("viewer.err.checkNameBySaving"));
	} else {
	    this.concreteActivityService
		    .saveConcreteActivity(this.concreteActivity);

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
	 * @return cwbdes 
	 * 			A list of concreteBreakdownElement of the current concreteActivity
	 */
    public List<ConcreteBreakdownElement> getConcreteBreakdownElementsList() {
	List<ConcreteBreakdownElement> list = new ArrayList<ConcreteBreakdownElement>();
	if (this.concreteActivity != null)
	    list.addAll(this.concreteActivity.getConcreteBreakdownElements());

	// Filter to obtain only concreteworkbreakdownelement (without
	// concreterole).
	List<ConcreteBreakdownElement> cwbdes = new ArrayList<ConcreteBreakdownElement>();
	for (ConcreteBreakdownElement cbde : list)
	    if (cbde instanceof ConcreteWorkBreakdownElement)
		cwbdes.add(cbde);

	return cwbdes;
    }
	/**
	 * Save the activity (all the ConcreteBreakdownElement of the activity)
	 * Also refresh the projectTree
	 */
    public void saveActivity() {
	super.getConcreteBreakdownElementService()
		.saveAllFirstSonsConcreteBreakdownElementsForConcreteActivity(
			this.concreteActivity);

	// successful message.
	WebCommonService.addInfoMessage(LocaleBean
		.getText("viewer.visibility.successMessage"));

	// Refresh the treebean.
	super.rebuildProjectTree();
    }
	/**
	 * 
	 * @return the concreteActivity
	 */
    public ConcreteActivity getConcreteActivity() {
	return concreteActivity;
    }

	 /**
	 * 
	 * @param _concreteActivity
	 * 			set a ConcreteActivity
	 */
    public void setConcreteActivity(ConcreteActivity _concreteActivity) {
	this.concreteActivity = _concreteActivity;
    }
    
	/**
	 * 
	 * @return the concreteActivityService of the ConcreteActivityViewerBean
	 */
    public ConcreteActivityService getConcreteActivityService() {
	return this.concreteActivityService;
    }
	/**
	 * 
	 * @param _concreteActivityService
	 * 			set a concreteActivityService in the ConcreteActivityViewerBean
	 */
    public void setConcreteActivityService(
	    ConcreteActivityService _concreteActivityService) {
	this.concreteActivityService = _concreteActivityService;
    }
    /**
     * 
     * @return isEmptyTable
     * 			true if there is no ConcreteBreakdownElement in the ConcreteActivityViewerBean
     * 			else return false
     * And also change the value of isEmptyTable
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
     * 
     * @param isEmptyTable
     * 		set the attribute isEmptyTable
     */
    public void setIsEmptyTable(boolean isEmptyTable) {
	this.isEmptyTable = isEmptyTable;
    }

    /**
     * 
     * @return the displayed state of the concreteActivity
     */
    public String getDisplayedState() {
	return WebCommonService.getDisplayedState(this.concreteActivity
		.getState());
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
		.getConcretePredecessorHashMap(this.concreteActivity);
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
		.getConcreteSuccessorHashMap(this.concreteActivity);
    }

    /**
     * @param _concreteSuccessors
     *                the concreteSuccessors to set
     */
    public void setConcreteSuccessors(
	    List<HashMap<String, Object>> _concreteSuccessors) {
	concreteSuccessors = _concreteSuccessors;
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
     * @return the concrete Predecessors and successors
     */
    public List<HashMap<String, Object>> getConcrete() 
    {
    	this.concrete = this.getConcretePredecessors() ;
    	this.concrete.addAll(this.getConcreteSuccessors()) ;
    	
     	return this.concrete ;
    }
}
