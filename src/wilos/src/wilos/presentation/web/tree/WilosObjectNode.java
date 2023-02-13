/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

import javax.swing.tree.DefaultMutableTreeNode;

import com.icesoft.faces.component.tree.IceUserObject;

/**
 * Extends the IceUserObject in order to add object id, bean name and page Id
 * 
 * @see
 * @author garwind
 * 
 */
public class WilosObjectNode extends IceUserObject {

	// static fields
	// Project Node
	public final static String PROJECTNODE = "ProjectViewer";
	// Phase Node
	public final static String PHASENODE = "ConcretePhaseViewer";
	// Iteration Node
	public final static String ITERATIONNODE = "ConcreteIterationViewer";
	// ConcreteTask Node
	public final static String CONCRETETASKNODE = "ConcreteTaskViewer";
	// ConcreteRole Node
	public final static String CONCRETEROLENODE = "ConcreteRoleViewer";
	// ConcreteTask Node
	public final static String ACTIVITYNODE = "ConcreteActivityViewer";
	// ConcreteWorkProduct Node
	public final static String CONCRETEWORKPRODUCTNODE = "ConcreteWorkProductViewer";
	// ConcreteMilestone Node
	public final static String CONCRETEMILESTONENODE = "ConcreteMilestoneViewer";

	// properties
	private String id = "";

	private String pageId = "";

	private Boolean isSelected = false;

	private String displayOrder = "";
	
	private String title = "";

	public WilosObjectNode(DefaultMutableTreeNode arg0) {
		super(arg0);
	}

	/**
	 * Verifies if the node is selected or not
	 * @return a boolean showing if the node is selected or not 
	 */
	public Boolean getIsSelected() {
		return isSelected;
	}

	/**
	 * Changes the value showing if the node is selected or not
	 * @param isSelected the new state of the node (selected or not)
	 */
	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

	/**
	 * Gets the id of the node
	 * @return the id of the node
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id of the node
	 * @param id the new id of the node
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the pageId of the node
	 * @return the pageId of the node
	 */
	public String getPageId() {
		return pageId;
	}

	/**
	 * Sets the pageId of the node
	 * @param pageId the new pageId of the node
	 */
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	/**
	 * Gets the displayOrder of the node
	 * @return the displayOrder of the node
	 */
	public String getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * Sets the displayOrder of the node
	 * @param displayOrder the new displayOrder of the node
	 */
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	/**
	 * Sets the title displayed while passing the cursor over the current task 
	 * in the navigation tree
	 * @param title: title associated with the task name
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Gets the title displayed while passing the cursor over the current task 
	 * in the navigation tree
	 * @return String: title associated with the task name
	 */
	public String getTitle() {
		return this.title;
	}

}
