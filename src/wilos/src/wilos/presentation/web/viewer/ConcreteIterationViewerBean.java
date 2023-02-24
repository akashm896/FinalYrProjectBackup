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

import wilos.business.services.misc.concreteiteration.ConcreteIterationService;
import wilos.business.services.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElementService;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concreteiteration.ConcreteIteration;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;
import wilos.resources.LocaleBean;

public class ConcreteIterationViewerBean extends ViewerBean {

    private ConcreteIteration concreteIteration;

    private ConcreteIterationService concreteIterationService;

    private ConcreteWorkBreakdownElementService concreteWorkBreakdownElementService;

    private boolean isEmptyTable;

    private List<HashMap<String, Object>> concretePredecessors;

    private List<HashMap<String, Object>> concreteSuccessors;

	private List<HashMap<String, Object>> concrete;

    /* Manage the table for the visible elements in the tree. */
    /**
     @return cwbdes
     * 			A list of all the ConcreteBreakdownElement contained in the concreteIteration
     */
    public List<ConcreteBreakdownElement> getConcreteBreakdownElementsList() {
	List<ConcreteBreakdownElement> list = new ArrayList<ConcreteBreakdownElement>();
	if (this.concreteIteration != null)
	    list.addAll(this.concreteIteration.getConcreteBreakdownElements());

	// Filter to obtain only concreteworkbreakdownelement (without
	// concreterole).
	List<ConcreteBreakdownElement> cwbdes = new ArrayList<ConcreteBreakdownElement>();
	for (ConcreteBreakdownElement cbde : list)
	    if (cbde instanceof ConcreteWorkBreakdownElement)
		cwbdes.add(cbde);

	WebSessionService.setAttribute(
			WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT,
			this.concreteIteration.getId());
	
	return cwbdes;
    }
    
    /**
     * Save the concreteIteration in the data base(all the concreteIteration of the activity)
	 * Also refresh the projectTree
     */
    public void saveIteration() {
	super.getConcreteBreakdownElementService()
		.saveAllFirstSonsConcreteBreakdownElementsForConcreteActivity(
			this.concreteIteration);

	// successful message.
	WebCommonService.addInfoMessage(LocaleBean
		.getText("viewer.visibility.successMessage"));

	// Reload the treebean.
	super.rebuildProjectTree();
    }

    /* Manage the concretename field editable. */
    /**
     * Set a concrete name of a concreteIteration
	 * refresh also the treebean
     */
    public void saveName() {
	if (this.concreteIteration.getConcreteName().trim().length() == 0) {
	    // re-put the former concrete name.
	    ConcreteIteration ci = this.concreteIterationService
		    .getConcreteIteration(this.concreteIteration.getId());
	    this.concreteIteration.setConcreteName(ci.getConcreteName());

	    // add error message.
	    WebCommonService.addErrorMessage(LocaleBean
		    .getText("viewer.err.checkNameBySaving"));
	} else {
	    this.concreteIterationService
		    .saveConcreteIteration(this.concreteIteration);

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
     * @return isEmptyTable
     * 			true if there is no ConcreteBreakdownElement in the ConcreteActivityViewerBean
     * 			else return false
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
     * @return the concreteIteration
     */
    public ConcreteIteration getConcreteIteration() {
	WebSessionService.setAttribute(
		WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT,
		this.concreteIteration.getId());
	return concreteIteration;
    }

    /**
     * 
     * @param _concreteIteration
     * 			set the concreteIteration
     */
    public void setConcreteIteration(ConcreteIteration _concreteIteration) {
	this.concreteIteration = _concreteIteration;
    }

    /**
     * @return the concreteIterationService
     */
    public ConcreteIterationService getConcreteIterationService() {
	return concreteIterationService;
    }

    /**
     * @param concreteIterationService
     *                the concreteIterationService to set
     */
    public void setConcreteIterationService(
	    ConcreteIterationService concreteIterationService) {
	this.concreteIterationService = concreteIterationService;
    }

    /**
     * 
     * @return the displayed state of the concreteActivity
     */
    public String getDisplayedState() {
	return WebCommonService.getDisplayedState(this.concreteIteration
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
		.getConcretePredecessorHashMap(this.concreteIteration);
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
		.getConcreteSuccessorHashMap(this.concreteIteration);
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
