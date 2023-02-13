/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2007-2008 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

package wilos.business.services.misc.stateservice;

import wilos.hibernate.misc.concreteactivity.ConcreteActivityDao;
import wilos.hibernate.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElementDao;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concretemilestone.ConcreteMilestone;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkOrder;
import wilos.model.misc.project.Project;
import wilos.utils.Constantes.State;
import wilos.utils.Constantes.WorkOrderType;

/**
 * 
 * @author Pierre
 * @author Anael
 * 
 */
public class StateService {

    private ConcreteActivityDao concreteActivityDao;

    private ConcreteWorkBreakdownElementDao concreteWorkBreakdownElementDao;

    /**
     * Attempts to update the given CWBDE to the given state. If the
     * preconditions are not entirely fulfilled the method produces no action.
     * (see pre***() methods) According to the type of the element given, the
     * changing could cascade in order to update related elements (parent
     * elements, or elements linked to the CWBDE by a dependency)
     * 
     * @param _cwbde
     * @param _newState
     */
    public void updateStateTo(ConcreteWorkBreakdownElement _cwbde,
	    String _newState) {

	// CREATED
	if (_newState.equals(State.CREATED)) {
	    if (!this.preCreated(_cwbde))
		return;
	}
	// READY
	else if (_newState.equals(State.READY)) {
	    if (!this.preReady(_cwbde))
		return;
	}
	// STARTED
	else if (_newState.equals(State.STARTED)) {
	    if (!this.preStarted(_cwbde))
		return;
	}
	// SUSPENDED
	else if (_newState.equals(State.SUSPENDED)) {
	    if (!this.preSuspended(_cwbde))
		return;
	}
	// FINISHED
	else if (_newState.equals(State.FINISHED)) {
	    if (!this.preFinished(_cwbde))
		return;
	}
	
	// update changes.
	_cwbde.setState(_newState);
	
	// save changes.
	this.concreteWorkBreakdownElementDao
		.saveOrUpdateConcreteWorkBreakdownElement(_cwbde);

	// CREATED
	if (_newState.equals(State.CREATED)) {
	    this.postCreated(_cwbde);
	}
	// READY
	else if (_newState.equals(State.READY)) {
	    this.postReady(_cwbde);
	}
	// STARTED
	else if (_newState.equals(State.STARTED)) {
	    this.postStarted(_cwbde);
	}
	// SUSPENDED
	else if (_newState.equals(State.SUSPENDED)) {
	    this.postSuspended(_cwbde);
	}
	// FINISHED
	else if (_newState.equals(State.FINISHED)) {
	    this.postFinished(_cwbde);
	}
    }

    /**
     * Checks whether the given CWBDE is allowed to be finished or not
     * FIXME!
     * 
     * @param _cwbde
     * @return
     */
    private boolean isFinishable(ConcreteWorkBreakdownElement _cwbde) {
	// if (this.concreteActivityDao.existsConcreteActivity(_cwbde.getId()))
	// {
	if (_cwbde instanceof ConcreteActivity) {

	    // ConcreteActivity ca = this.concreteActivityDao
	    // .getConcreteActivity(_cwbde.getId());

	    if (!this
		    .isConcreteWorkBreakdownElementFinishable((ConcreteActivity) _cwbde)) {
		return false;
	    }

	    // for a ConcreteActivity, all inner cwbde must be finished
	    for (ConcreteBreakdownElement cbde : ((ConcreteActivity) _cwbde)
		    .getConcreteBreakdownElements()) {
		if (cbde instanceof ConcreteWorkBreakdownElement) {
		    ConcreteWorkBreakdownElement cwbde = (ConcreteWorkBreakdownElement) cbde;

		    if (!cwbde.getState().equals(State.FINISHED)) {
			return false;
		    }
		}
	    }
	}
	return true;
    }

    /** *********************************************************************************************************** */
    /** *********************************************************************************************************** */
    /** *********************************************************************************************************** */

    private boolean preCreated(ConcreteWorkBreakdownElement _cwbde) {
	return !_cwbde.getState().equals(State.CREATED);
    }

    private void postCreated(ConcreteWorkBreakdownElement _cwbde) {
	if (this.concreteActivityDao.existsConcreteActivity(_cwbde.getId())) {

	    ConcreteActivity ca = this.concreteActivityDao
		    .getConcreteActivity(_cwbde.getId());

	    // for a ConcreteActivity, start all inner cwbde

	    for (ConcreteBreakdownElement cbde : ca
		    .getConcreteBreakdownElements()) {
		if (cbde instanceof ConcreteWorkBreakdownElement) {
		    ConcreteWorkBreakdownElement cwbde = (ConcreteWorkBreakdownElement) cbde;

		    this.updateStateTo(cwbde, State.CREATED);
		}
	    }
	}
    }

    private boolean preReady(ConcreteWorkBreakdownElement _cwbde) {
	return !_cwbde.getState().equals(State.READY)
		&& this.isConcreteWorkBreakdownElementReadyable(_cwbde);
    }

    private void postReady(ConcreteWorkBreakdownElement _cwbde) {
	if (this.concreteActivityDao.existsConcreteActivity(_cwbde.getId())) {

	    ConcreteActivity ca = this.concreteActivityDao
		    .getConcreteActivity(_cwbde.getId());

	    // for a ConcreteActivity, set all inner cwbde to ready
	    for (ConcreteBreakdownElement cbde : ca
		    .getConcreteBreakdownElements()) {
		if (cbde instanceof ConcreteWorkBreakdownElement) {
		    ConcreteWorkBreakdownElement cwbde = (ConcreteWorkBreakdownElement) cbde;

		    this.updateStateTo(cwbde, State.READY);
		}
	    }
	}
    }

    private boolean preStarted(ConcreteWorkBreakdownElement _cwbde) {
	return !_cwbde.getState().equals(State.STARTED);
    }

    private void postStarted(ConcreteWorkBreakdownElement _cwbde) {
	// start super activity
	ConcreteActivity superConcreteActivity = _cwbde
		.getSuperConcreteActivity();
	if ((superConcreteActivity != null)
		&& !(superConcreteActivity instanceof Project)) {
	    this.updateStateTo(superConcreteActivity, State.STARTED);
	}

	// Check for dependencies
	for (ConcreteWorkOrder cwo : _cwbde.getConcreteSuccessors()) {
	    ConcreteWorkBreakdownElement successor = this.concreteWorkBreakdownElementDao
		    .getConcreteWorkBreakdownElement(cwo
			    .getConcreteWorkOrderId().getConcreteSuccessorId());
	    String linkType = cwo.getConcreteLinkType();

	    if (linkType.equals(WorkOrderType.START_TO_START)) {
		this.updateStateTo(successor, State.READY);
	    } else if ((this.concreteActivityDao
		    .existsConcreteActivity(successor.getId()))
		    && (linkType.equals(WorkOrderType.START_TO_FINISH))) {
		this.updateStateTo(successor, State.FINISHED);
	    }
	}
    }

    private boolean preSuspended(ConcreteWorkBreakdownElement _cwbde) {
	return !_cwbde.getState().equals(State.SUSPENDED);
    }

    private void postSuspended(ConcreteWorkBreakdownElement _cwbde) {
	// nothing...
    }

    private boolean preFinished(ConcreteWorkBreakdownElement _cwbde) {
	return !_cwbde.getState().equals(State.FINISHED)
		&& this.isFinishable(_cwbde);
    }

    private void postFinished(ConcreteWorkBreakdownElement _cwbde) {
	// finish super activity IF IT IS FINISHABLE
	ConcreteActivity superConcreteActivity = _cwbde
		.getSuperConcreteActivity();
	if ((superConcreteActivity != null)
		&& !(superConcreteActivity instanceof Project)) {
	    this.updateStateTo(superConcreteActivity, State.FINISHED);
	}

	// Check for dependencies
	for (ConcreteWorkOrder cwo : _cwbde.getConcreteSuccessors()) {
	    ConcreteWorkBreakdownElement successor = this.concreteWorkBreakdownElementDao
		    .getConcreteWorkBreakdownElement(cwo
			    .getConcreteWorkOrderId().getConcreteSuccessorId());
	    String linkType = cwo.getConcreteLinkType();

	    if (linkType.equals(WorkOrderType.FINISH_TO_START)) {
		this.updateStateTo(successor, State.READY);
	    } else if ((this.concreteActivityDao
		    .existsConcreteActivity(successor.getId()))
		    && (linkType.equals(WorkOrderType.FINISH_TO_FINISH))) {
		this.updateStateTo(successor, State.FINISHED);
	    }
	}
    }

    /** *********************************************************************************************************** */
    /** *********************************************************************************************************** */
    /** *********************************************************************************************************** */

    /**
     * Checks whether the CWBDE is allowed to be set to READY or not, according
     * to its current dependencies
     * 
     * @param _cwbde
     * @return
     */
    public boolean isConcreteWorkBreakdownElementReadyable(
	    ConcreteWorkBreakdownElement _cwbde) {
	this.concreteWorkBreakdownElementDao
		.saveOrUpdateConcreteWorkBreakdownElement(_cwbde);
	// check dependencies
	for (ConcreteWorkOrder cwo : _cwbde.getConcretePredecessors()) {

	    ConcreteWorkBreakdownElement predecessor = cwo
		    .getConcretePredecessor();
	    String linkType = cwo.getConcreteLinkType();

	    boolean predecessorIsAtLeastStarted = predecessor.getState()
		    .equals(State.STARTED)
		    || predecessor.getState().equals(State.SUSPENDED)
		    || predecessor.getState().equals(State.FINISHED);
	    boolean predecessorIsFinished = predecessor.getState().equals(
		    State.FINISHED);

	    if ((linkType.equals(WorkOrderType.START_TO_START) && !predecessorIsAtLeastStarted)
		    || (linkType.equals(WorkOrderType.FINISH_TO_START) && !predecessorIsFinished)) {
		return false;
	    }

	    if (_cwbde instanceof ConcreteMilestone) {
		if ((linkType.equals(WorkOrderType.START_TO_FINISH) && !predecessorIsAtLeastStarted)
			|| (linkType.equals(WorkOrderType.FINISH_TO_FINISH) && !predecessorIsFinished)) {
		    return false;
		}
	    }
	}
	return true;
    }

    /**
     * Checks whether the CWBDE is allowed to be set to FINISHED or not,
     * according to its current dependencies
     * 
     * @param _cwbde
     * @return
     */
    public boolean isConcreteWorkBreakdownElementFinishable(
	    ConcreteWorkBreakdownElement _cwbde) {
	this.concreteWorkBreakdownElementDao
		.saveOrUpdateConcreteWorkBreakdownElement(_cwbde);
	// check dependencies
	for (ConcreteWorkOrder cwo : _cwbde.getConcretePredecessors()) {

	    ConcreteWorkBreakdownElement predecessor = cwo
		    .getConcretePredecessor();
	    String linkType = cwo.getConcreteLinkType();

	    boolean predecessorIsAtLeastStarted = predecessor.getState()
		    .equals(State.STARTED)
		    || predecessor.getState().equals(State.SUSPENDED)
		    || predecessor.getState().equals(State.FINISHED);
	    boolean predecessorIsFinished = predecessor.getState().equals(
		    State.FINISHED);

	    if ((linkType.equals(WorkOrderType.START_TO_FINISH) && !predecessorIsAtLeastStarted)
		    || (linkType.equals(WorkOrderType.FINISH_TO_FINISH) && !predecessorIsFinished)) {
		return false;
	    }
	}
	return true;
    }

    /** *********************************************************************************************************** */
    /** *********************************************************************************************************** */
    /** *********************************************************************************************************** */

    /**
     * @return the concreteWorkBreakdownElementDao
     */
    public ConcreteWorkBreakdownElementDao getConcreteWorkBreakdownElementDao() {
	return this.concreteWorkBreakdownElementDao;
    }

    /**
     * @param _concreteWorkBreakdownElementDao
     *                the concreteWorkBreakdownElementDao to set
     */
    public void setConcreteWorkBreakdownElementDao(
	    ConcreteWorkBreakdownElementDao _concreteWorkBreakdownElementDao) {
	this.concreteWorkBreakdownElementDao = _concreteWorkBreakdownElementDao;
    }

    /**
     * @return the concreteActivityDao
     */
    public ConcreteActivityDao getConcreteActivityDao() {
	return this.concreteActivityDao;
    }

    /**
     * @param _concreteActivityDao
     *                the concreteActivityDao to set
     */
    public void setConcreteActivityDao(ConcreteActivityDao _concreteActivityDao) {
	this.concreteActivityDao = _concreteActivityDao;
    }
}
