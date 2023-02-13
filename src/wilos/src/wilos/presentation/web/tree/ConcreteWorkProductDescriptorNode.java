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

import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;

public class ConcreteWorkProductDescriptorNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = -1234563122119371158L;
    
    private static final int MAX_WORK_PRODUCT_NAME_LENGTH = 30;

    /**
     * Creates a node to represent a Concrete Work Product Descriptor
     * @param _concreteWorkProductDescriptor the concrete work product descriptor being represented
     * @param _treeMap the HashMap in which the concrete work product descriptor and its id will be stored (allows to acces the object easily when clicked on in the tree view)
     */
    public ConcreteWorkProductDescriptorNode(
	    ConcreteWorkProductDescriptor _concreteWorkProductDescriptor,
	    HashMap<String, Object> _treeMap) {
	super();
	WilosObjectNode iceUserObject = new WilosObjectNode(this);
	this.setUserObject(iceUserObject);

	// Adds "..." to the end of the work product name if its size is upper than 
	// MAX_WORK_PRODUCT_NAME_LENGTH
	int roleNameLength = _concreteWorkProductDescriptor.getConcreteName().length();
	if(roleNameLength > ConcreteWorkProductDescriptorNode.MAX_WORK_PRODUCT_NAME_LENGTH) {
		iceUserObject.setText(_concreteWorkProductDescriptor.getConcreteName()
				.substring(0, ConcreteWorkProductDescriptorNode.MAX_WORK_PRODUCT_NAME_LENGTH) + "...");
	}
	else {
		iceUserObject.setText(_concreteWorkProductDescriptor.getConcreteName());
	}
	iceUserObject.setLeaf(true);	
	iceUserObject.setTitle(_concreteWorkProductDescriptor.getConcreteName());

	// manage icon.
	if (_concreteWorkProductDescriptor.getResponsibleConcreteRoleDescriptor() == null && _concreteWorkProductDescriptor.getParticipant() == null)
	    iceUserObject.setLeafIcon("images/tree/icon_workproduct_free.gif");
	else
	    iceUserObject.setLeafIcon("images/tree/icon_workproduct_busy.gif");

	// node information
	iceUserObject.setId(_concreteWorkProductDescriptor.getId());
	iceUserObject.setPageId(WilosObjectNode.CONCRETEWORKPRODUCTNODE);

	// add the concreteWorkProductDescriptor object with his id in the
	// treeMap
	_treeMap.put(iceUserObject.getId(), _concreteWorkProductDescriptor);
    }

}
