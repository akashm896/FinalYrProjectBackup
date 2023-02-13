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

package wilos.presentation.web.tree;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import wilos.model.misc.concretemilestone.ConcreteMilestone;
import wilos.utils.Constantes.State;

public class ConcreteMilestoneNode extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9221104093411419536L;

	/**
	 * Creates a node to represent a Concrete Milestone
	 * @param _concreteMilestone the concrete milestone being represented
	 * @param _treeMap the HashMap in which the concrete milestone and its id will be stored (allows to acces the object easily when clicked on in the tree view) 
	 */
	public ConcreteMilestoneNode(ConcreteMilestone _concreteMilestone,
			HashMap<String, Object> _treeMap) {
		super();
		WilosObjectNode iceUserObject = new WilosObjectNode(this);
		this.setUserObject(iceUserObject);

		iceUserObject.setText(_concreteMilestone.getConcreteName());
		iceUserObject.setLeaf(true);

		// manage icon.
		if (_concreteMilestone.getState().equals(State.CREATED))
			iceUserObject.setLeafIcon("images/tree/icon_milestone_created.gif");
		else if (_concreteMilestone.getState().equals(State.READY))
			iceUserObject.setLeafIcon("images/tree/icon_milestone.gif");
		else if (_concreteMilestone.getState().equals(State.FINISHED))
			iceUserObject.setLeafIcon("images/tree/icon_milestone_over.gif");

		// node information
		iceUserObject.setId(_concreteMilestone.getId());
		iceUserObject.setPageId(WilosObjectNode.CONCRETEMILESTONENODE);
		iceUserObject.setDisplayOrder(_concreteMilestone.getDisplayOrder());

		// add the concreteMilestone object with his id in the treeMap
		_treeMap.put(iceUserObject.getId(), _concreteMilestone);
	}

}
