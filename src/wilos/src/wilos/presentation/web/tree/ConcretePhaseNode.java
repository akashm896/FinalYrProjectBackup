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
import wilos.model.misc.concretephase.ConcretePhase;
import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;
import wilos.utils.Constantes.State;

public class ConcretePhaseNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = -5277094164644525311L;

	private ConcretePhase concretePhase;

	/**
	 * Creates a node to represent a Concrete Phase
	 * @param _concretePhase the concrete phase being represented
	 * @param treetype the type of tree being generated
	 * @param _treeMap the HashMap in which the concrete phase and its id will be stored (allows to acces the object easily when clicked on in the tree view)
	 */
	public ConcretePhaseNode(ConcretePhase _concretePhase, int treetype,
			HashMap<String, Object> _treeMap) {
		super();
		this.concretePhase = _concretePhase;

		// Get the ConcretePhaseService.
		FacesContext context = FacesContext.getCurrentInstance();
		/*
		 * ConcretePhaseService concretePhaseService = (ConcretePhaseService)
		 * context.getApplication().getVariableResolver()
		 * .resolveVariable(context, "ConcretePhaseService");
		 */
		ConcreteActivityService concreteActivityService = (ConcreteActivityService) context
				.getApplication().getVariableResolver().resolveVariable(
						context, "ConcreteActivityService");

		WilosObjectNode iceUserObject = new WilosObjectNode(this);
		this.setUserObject(iceUserObject);

		iceUserObject.setExpanded(true);
		iceUserObject.setText(this.concretePhase.getConcreteName());
		iceUserObject.setLeaf(false);

		// manage icon
		String state = _concretePhase.getState();
		this.manageIcon(state, iceUserObject);
		
		// node informations
		iceUserObject.setId(this.concretePhase.getId());
		iceUserObject.setPageId(WilosObjectNode.PHASENODE);
		iceUserObject.setDisplayOrder(_concretePhase.getDisplayOrder());

		// add the concretePhase object with his id in the treeMap
		_treeMap.put(iceUserObject.getId(), _concretePhase);

		this.peruseConcreteActivityServiceList(concreteActivityService, treetype, _treeMap);
	}
	
	/**
	 * Manage icon
	 * @param _state
	 * @param _iceUserObject
	 */
	private void manageIcon(String _state, WilosObjectNode _iceUserObject)
	{
        	    if (_state.equals(State.CREATED)) {
        		_iceUserObject
        				.setBranchContractedIcon("images/tree/icon_phase_created.gif");
        		_iceUserObject
        				.setBranchExpandedIcon("images/tree/icon_phase_created.gif");
        	} else if (_state.equals(State.STARTED)) {
        		_iceUserObject
        				.setBranchContractedIcon("images/tree/icon_phase_play.gif");
        		_iceUserObject
        				.setBranchExpandedIcon("images/tree/icon_phase_play.gif");
        	} else if (_state.equals(State.FINISHED)) {
        		_iceUserObject
        				.setBranchContractedIcon("images/tree/icon_phase_over.gif");
        		_iceUserObject
        				.setBranchExpandedIcon("images/tree/icon_phase_over.gif");
        	} else {
        		_iceUserObject.setBranchContractedIcon("images/tree/icon_phase.gif");
        		_iceUserObject.setBranchExpandedIcon("images/tree/icon_phase.gif");
        	}
	}
	
	/**
	 * Add element in this phase
	 * @param _concreteActivityService
	 * @param _treetype
	 * @param _treeMap
	 */
	private void peruseConcreteActivityServiceList(ConcreteActivityService _concreteActivityService,
		int _treetype, HashMap<String, Object> _treeMap)
	{
		for (ConcreteBreakdownElement concreteBreakdownElement : _concreteActivityService
			.getConcreteBreakdownElements(this.concretePhase)) {
		if (concreteBreakdownElement instanceof ConcreteIteration) {
			ConcreteIteration ci = (ConcreteIteration) concreteBreakdownElement;
			if (ci.getIsInUsed()) {
				this.add(new ConcreteIterationNode(ci, _treetype, _treeMap));
			}
		} else if (concreteBreakdownElement instanceof ConcreteActivity) {
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
