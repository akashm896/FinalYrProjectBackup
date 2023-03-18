/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007-2008 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

package wilos.presentation.web.tree;

import java.util.HashMap;

import javax.faces.context.FacesContext;
import javax.swing.tree.DefaultMutableTreeNode;

import wilos.business.services.misc.concreteactivity.ConcreteActivityService;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concreteiteration.ConcreteIteration;
import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;
import wilos.utils.Constantes.State;

public class ConcreteIterationNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = -5277094164644525311L;

    private ConcreteIteration concreteIteration;

    /**
     * Creates a node representing a Concrete Iteration
     * @param _concreteIteration  the concrete iteration being represented
     * @param treetype the type of the tree being generated
     * @param _treeMap the HashMap in which the concrete iteration and its id will be stored (allows to acces the object easily when clicked on in the tree view)
     */
    public ConcreteIterationNode(ConcreteIteration _concreteIteration,
	    int treetype, HashMap<String, Object> _treeMap) {
	super();
	this.concreteIteration = _concreteIteration;

	// Get the ConcreteIterationService.
	FacesContext context = FacesContext.getCurrentInstance();
	/*
	 * ConcreteIterationService concreteIterationService =
	 * (ConcreteIterationService)
	 * context.getApplication().getVariableResolver()
	 * .resolveVariable(context, "ConcreteIterationService");
	 */
	ConcreteActivityService concreteActivityService = (ConcreteActivityService) context
		.getApplication().getVariableResolver().resolveVariable(
			context, "ConcreteActivityService");

	WilosObjectNode iceUserObject = new WilosObjectNode(this);
	this.setUserObject(iceUserObject);

	iceUserObject.setExpanded(true);
	iceUserObject.setText(this.concreteIteration.getConcreteName());
	iceUserObject.setLeaf(false);

	// manage icon
	String state = _concreteIteration.getState();
	this.manageIcon(state, iceUserObject);
	
	// node information
	iceUserObject.setId(this.concreteIteration.getId());
	iceUserObject.setPageId(WilosObjectNode.ITERATIONNODE);
	iceUserObject.setDisplayOrder(_concreteIteration.getDisplayOrder());

	// add the concreteIteration object with his id in the treeMap
	_treeMap.put(iceUserObject.getId(), _concreteIteration);

	this.peruseConcreteActivityService(concreteActivityService, treetype,
		_treeMap);
    }

    /**
     * Manage icon
     * 
     * @param _state
     * @param _iceUserObject
     */
    private void manageIcon(String _state, WilosObjectNode _iceUserObject) {
	if (_state.equals(State.CREATED)) {
	    _iceUserObject
		    .setBranchContractedIcon("images/tree/icon_iteration_created.gif");
	    _iceUserObject
		    .setBranchExpandedIcon("images/tree/icon_iteration_created.gif");
	} else if (_state.equals(State.STARTED)) {
	    _iceUserObject
		    .setBranchContractedIcon("images/tree/icon_iteration_play.gif");
	    _iceUserObject
		    .setBranchExpandedIcon("images/tree/icon_iteration_play.gif");
	} else if (_state.equals(State.FINISHED)) {
	    _iceUserObject
		    .setBranchContractedIcon("images/tree/icon_iteration_over.gif");
	    _iceUserObject
		    .setBranchExpandedIcon("images/tree/icon_iteration_over.gif");
	} else {
	    //READY
	    _iceUserObject
		    .setBranchContractedIcon("images/tree/icon_iteration.gif");
	    _iceUserObject
		    .setBranchExpandedIcon("images/tree/icon_iteration.gif");
	}
    }

    /**
     * Add element on the iteration
     * @param _concreteActivityService
     * @param _treetype
     * @param _treeMap
     */
    private void peruseConcreteActivityService(
	    ConcreteActivityService _concreteActivityService, int _treetype,
	    HashMap<String, Object> _treeMap) {
	for (ConcreteBreakdownElement concreteBreakdownElement : _concreteActivityService
		.getConcreteBreakdownElements(this.concreteIteration)) {
	    if (concreteBreakdownElement instanceof ConcreteActivity) {
		ConcreteActivity ca = (ConcreteActivity) concreteBreakdownElement;
		if (ca.getIsInUsed()) {
		    this.add(new ConcreteActivityNode(ca, _treetype, _treeMap));
		}
	    } else if (_treetype == 1) {
		if (concreteBreakdownElement instanceof ConcreteTaskDescriptor) {
		    ConcreteTaskDescriptor ctd = (ConcreteTaskDescriptor) concreteBreakdownElement;
		    if (ctd.getIsInUsed()) {
			this.add(new ConcreteTaskDescriptorNode(ctd, _treeMap));
		    }
		}
	    } else if (_treetype == 2) {
		if (concreteBreakdownElement instanceof ConcreteRoleDescriptor) {
		    ConcreteRoleDescriptor crd = (ConcreteRoleDescriptor) concreteBreakdownElement;
		    this.add(new ConcreteRoleDescriptorNode(crd, _treeMap));
		}
	    }

	    else {
		if (concreteBreakdownElement instanceof ConcreteWorkProductDescriptor) {
		    ConcreteWorkProductDescriptor cwpd = (ConcreteWorkProductDescriptor) concreteBreakdownElement;
		    this.add(new ConcreteWorkProductDescriptorNode(cwpd,
			    _treeMap));
		}
	    }
	}
    }

}
