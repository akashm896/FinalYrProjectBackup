/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2007 Mickaël CLAVREUL <mclavreul@wilos-project.org>
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

package wilos.presentation.web.project;

import wilos.presentation.web.tree.TreeBean;
import wilos.presentation.web.utils.WebCommonService;

import com.icesoft.faces.component.paneltabset.TabChangeEvent;
import com.icesoft.faces.component.paneltabset.TabChangeListener;


public class ProjectViewerTabChangeListener implements TabChangeListener {

	private int currentTabIndex = 0;

	/**
	 * TabChange Listener for Project main tab set
	 * 
	 * @param TabChangeEvent _tabChangeEvent
	 */
	public void processTabChange(TabChangeEvent _tabChangeEvent) {

		this.currentTabIndex = _tabChangeEvent.getNewTabIndex();		
		TreeBean treeBean = (TreeBean)WebCommonService.getBean("TreeBean");
		
		switch (this.currentTabIndex) {
		// roles instanciation tab
		case 1:
			treeBean.setSelectedMode(TreeBean.ROLES_MODE);
			break;
		// dependencies management tab
		case 2:			
		    	treeBean.setSelectedMode(TreeBean.WORKPRODUCTS_MODE);
			break;
		default:
			treeBean.setSelectedMode(TreeBean.TASKS_MODE);
			break;
		}
		treeBean.refreshProjectTree();
	}

	/**
	 * getter of currentTabIndex int attribute
	 * 
	 * @return the currentTabIndex
	 */
	public int getCurrentTabIndex() {
		return this.currentTabIndex;
	}

	/**
	 * setter of currentTabIndex int attribute
	 * 
	 * @param _currentTabIndex the currentTabIndex to set
	 */
	public void setCurrentTabIndex(int _currentTabIndex) {
		this.currentTabIndex = _currentTabIndex;
	}
}