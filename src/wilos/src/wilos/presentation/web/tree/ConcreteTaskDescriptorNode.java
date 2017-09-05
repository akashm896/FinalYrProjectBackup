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

package wilos.presentation.web.tree;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.utils.Constantes.State;

public class ConcreteTaskDescriptorNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = -2090785940515259986L;
    
    private static final int MAX_TASK_NAME_LENGTH = 30;

    /**
     * Creates a node to represent a Concrete Task Descriptor
     * @param _concreteTaskDescriptor the concrete task descriptor being represented
     * @param _treeMap the HashMap in which the concrete task descriptor and its id will be stored (allows to acces the object easily when clicked on in the tree view)
     */
    public ConcreteTaskDescriptorNode(
	    ConcreteTaskDescriptor _concreteTaskDescriptor,
	    HashMap<String, Object> _treeMap) {
	super();
	WilosObjectNode iceUserObject = new WilosObjectNode(this);
	this.setUserObject(iceUserObject);

	// Adds "..." to the end of the task name if its size is upper than 
	// MAX_TASK_NAME_LENGTH
	int taskNameLength = _concreteTaskDescriptor.getConcreteName().length();
	if(taskNameLength > ConcreteTaskDescriptorNode.MAX_TASK_NAME_LENGTH) {
		iceUserObject.setText(_concreteTaskDescriptor.getConcreteName()
				.substring(0, ConcreteTaskDescriptorNode.MAX_TASK_NAME_LENGTH) + "...");
	}
	else {
		iceUserObject.setText(_concreteTaskDescriptor.getConcreteName());
	}
	iceUserObject.setLeaf(true);	
	iceUserObject.setTitle(_concreteTaskDescriptor.getConcreteName());

	// manage icon.
	if (_concreteTaskDescriptor.getState().equals(State.CREATED))
	    iceUserObject.setLeafIcon("images/tree/icon_task_created.gif");
	else if (_concreteTaskDescriptor.getState().equals(State.STARTED))
	    iceUserObject.setLeafIcon("images/tree/icon_task_play.gif");
	else if (_concreteTaskDescriptor.getState().equals(State.SUSPENDED))
	    iceUserObject.setLeafIcon("images/tree/icon_task_pause.gif");
	else if (_concreteTaskDescriptor.getState().equals(State.FINISHED))
	    iceUserObject.setLeafIcon("images/tree/icon_task_over.gif");
	else if (_concreteTaskDescriptor.getMainConcreteRoleDescriptor() != null)
	    iceUserObject.setLeafIcon("images/tree/icon_task_busy.gif");
	else
	    iceUserObject.setLeafIcon("images/tree/icon_task_free.gif");

	// node information
	iceUserObject.setId(_concreteTaskDescriptor.getId());
	iceUserObject.setPageId(WilosObjectNode.CONCRETETASKNODE);
	iceUserObject.setDisplayOrder(_concreteTaskDescriptor.getDisplayOrder());

	// add the concreteTaskDescriptor object with his id in the treeMap
	_treeMap.put(iceUserObject.getId(), _concreteTaskDescriptor);
    }

}
