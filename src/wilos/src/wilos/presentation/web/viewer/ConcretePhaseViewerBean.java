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

import wilos.business.services.misc.concretephase.ConcretePhaseService;
import wilos.business.services.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElementService;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concretephase.ConcretePhase;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;
import wilos.resources.LocaleBean;

public class ConcretePhaseViewerBean extends ViewerBean {

    private ConcretePhase concretePhase;

    private ConcretePhaseService concretePhaseService;

    private ConcreteWorkBreakdownElementService concreteWorkBreakdownElementService;

    private boolean isEmptyTable;

    private List<HashMap<String, Object>> concrete = new ArrayList<HashMap<String,Object>>() ;

    /* Manage the table for the visible elements in the tree. */

    public List<ConcreteBreakdownElement> getConcreteBreakdownElementsList() {
	List<ConcreteBreakdownElement> list = new ArrayList<ConcreteBreakdownElement>();
	if (this.concretePhase != null)
	    list.addAll(this.concretePhase.getConcreteBreakdownElements());

	// Filter to obtain only concreteworkbreakdownelement (without
	// concreterole).
	List<ConcreteBreakdownElement> cwbdes = new ArrayList<ConcreteBreakdownElement>();
	for (ConcreteBreakdownElement cbde : list)
	    if (cbde instanceof ConcreteWorkBreakdownElement)
		cwbdes.add(cbde);


	WebSessionService.setAttribute(
		WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT,
		this.concretePhase.getId());

	return cwbdes;
    }

    public void savePhase() {
	super.getConcreteBreakdownElementService()
		.saveAllFirstSonsConcreteBreakdownElementsForConcreteActivity(
			this.concretePhase);

	// successful message.
	WebCommonService.addInfoMessage(LocaleBean
		.getText("viewer.visibility.successMessage"));

	// Reload the treebean.
	super.rebuildProjectTree();
    }

    /* Manage the concretename field editable. */

    public void saveName() {
	if (this.concretePhase.getConcreteName().trim().length() == 0) {
	    // re-put the former concrete name.
	    ConcretePhase cp = this.concretePhaseService
		    .getConcretePhase(this.concretePhase.getId());
	    this.concretePhase.setConcreteName(cp.getConcreteName());

	    // add error message.
	    WebCommonService.addErrorMessage(LocaleBean
		    .getText("viewer.err.checkNameBySaving"));
	} else {
	    this.concretePhaseService.saveConcretePhase(this.concretePhase);

	    // Refresh the treebean.
	    super.refreshProjectTree();

	    // put the name text editor to disable.
	    super.setNameIsEditable(false);

	    // successful message.
	    WebCommonService.addInfoMessage(LocaleBean
		    .getText("viewer.visibility.successMessage"));
	}
    }

    /* Getters & Setters */

    public ConcretePhase getConcretePhase() {

	WebSessionService.setAttribute(
		WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT,
		this.concretePhase.getId());
	return this.concretePhase;
    }

    public void setConcretePhase(ConcretePhase _concretePhase) {
	this.concretePhase = _concretePhase;
    }

    /**
     * @return the concretePhaseService
     */
    public ConcretePhaseService getConcretePhaseService() {
	return concretePhaseService;
    }

    /**
     * @param concretePhaseService
     *                the concretePhaseService to set
     */
    public void setConcretePhaseService(
	    ConcretePhaseService concretePhaseService) {
	this.concretePhaseService = concretePhaseService;
    }

    public boolean getIsEmptyTable() {
	if (getConcreteBreakdownElementsList().size() == 0) {
	    isEmptyTable = true;
	} else {
	    isEmptyTable = false;
	}
	return isEmptyTable;
    }

    public void setIsEmptyTable(boolean isEmptyTable) {
	this.isEmptyTable = isEmptyTable;
    }

    public String getDisplayedState() {
	return WebCommonService
		.getDisplayedState(this.concretePhase.getState());
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

    public boolean getHasConcreteDependencies() {
	return !(this.getConcrete().isEmpty());
    }

  
    /**
     * @return the concrete Predecessors and successors
     */
    public List<HashMap<String, Object>> getConcrete() 
    {
    	this.concrete = this.concreteWorkBreakdownElementService.getConcreteSuccessorHashMap(this.concretePhase) ;
    	this.concrete.addAll(this.concreteWorkBreakdownElementService.getConcretePredecessorHashMap(this.concretePhase)) ;
    	
     	return this.concrete ;
    }

    /**
     * @param _concrete
     *                the concrete to set
     */
    public void setConcrete(
	    List<HashMap<String, Object>> _concrete) {
	concrete = _concrete;
    }
}
