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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wilos.business.services.misc.project.ProjectService;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concreteiteration.ConcreteIteration;
import wilos.model.misc.concretemilestone.ConcreteMilestone;
import wilos.model.misc.concretephase.ConcretePhase;
import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;
import wilos.model.misc.project.Project;

public class ProjectNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = -9148715541110574441L;

	protected final Log logger = LogFactory.getLog(this.getClass());

	private Project project;

	private void createNestedNodes(ProjectService _projectService,
			int treetype, HashMap<String, Object> _treeMap) {
		for (ConcreteBreakdownElement concreteBreakdownElement : _projectService
				.getConcreteBreakdownElementsFromProject(this.project)) {
			if (concreteBreakdownElement instanceof ConcretePhase) {
				ConcretePhase cp = (ConcretePhase) concreteBreakdownElement;
				if (cp.getIsInUsed()) {
					this.add(new ConcretePhaseNode(cp, treetype, _treeMap));
				}

			} else if (concreteBreakdownElement instanceof ConcreteIteration) {
				ConcreteIteration ci = (ConcreteIteration) concreteBreakdownElement;
				if (ci.getIsInUsed()) {
					this.add(new ConcreteIterationNode(ci, treetype, _treeMap));
				}

			} else if (concreteBreakdownElement instanceof ConcreteActivity) {
				ConcreteActivity ca = (ConcreteActivity) concreteBreakdownElement;
				if (ca.getIsInUsed()) {
					this.add(new ConcreteActivityNode(ca, treetype, _treeMap));
				}

			} else if (treetype == 1) {
				if (concreteBreakdownElement instanceof ConcreteTaskDescriptor) {
					ConcreteTaskDescriptor ctd = (ConcreteTaskDescriptor) concreteBreakdownElement;

					if (ctd.getIsInUsed()) {
						this.add(new ConcreteTaskDescriptorNode(ctd, _treeMap));
					}
				} else if (concreteBreakdownElement instanceof ConcreteMilestone) {
					ConcreteMilestone cmi = (ConcreteMilestone) concreteBreakdownElement;

					if (cmi.getIsInUsed()) {
						this.add(new ConcreteMilestoneNode(cmi, _treeMap));
					}
				}

			} else if (treetype == 2) {
				if (concreteBreakdownElement instanceof ConcreteRoleDescriptor) {
					ConcreteRoleDescriptor crd = (ConcreteRoleDescriptor) concreteBreakdownElement;
					this.add(new ConcreteRoleDescriptorNode(crd, _treeMap));
				}
			} else if (concreteBreakdownElement instanceof ConcreteWorkProductDescriptor) {
				ConcreteWorkProductDescriptor cwpd = (ConcreteWorkProductDescriptor) concreteBreakdownElement;
				this.add(new ConcreteWorkProductDescriptorNode(cwpd, _treeMap));
			}
		}
	}

	/**
	 * Creates a node representing a Project
	 * 
	 * @param _project
	 *            the project being represented
	 * @param treetype
	 *            the type of the tree being generated
	 * @param _treeMap
	 *            the HashMap in which the project and its id will be stored
	 *            (allows to acces the object easily when clicked on in the tree
	 *            view)
	 */
	public ProjectNode(Project _project, int treetype,
			HashMap<String, Object> _treeMap) {
		super();
		this.project = _project;

		// Get the ProjectService.
		FacesContext context = FacesContext.getCurrentInstance();
		ProjectService projectService = (ProjectService) context
				.getApplication().getVariableResolver().resolveVariable(
						context, "ProjectService");

		WilosObjectNode iceUserObject = new WilosObjectNode(this);
		this.setUserObject(iceUserObject);

		iceUserObject.setExpanded(true);
		iceUserObject.setText(this.project.getConcreteName());
		iceUserObject.setLeaf(false);
		iceUserObject.setBranchContractedIcon("images/tree/icon_process.gif");
		iceUserObject.setBranchExpandedIcon("images/tree/icon_process.gif");
		// node information
		iceUserObject.setId(this.project.getId());
		iceUserObject.setPageId(WilosObjectNode.PROJECTNODE);
		iceUserObject.setDisplayOrder(_project.getDisplayOrder());

		// add the project object with his id in the treeMap
		_treeMap.put(iceUserObject.getId(), _project);

		if (this.project.getProcess() != null) {
			iceUserObject.setText(this.project.getConcreteName());

			// Nested nodes.
			this.createNestedNodes(projectService, treetype, _treeMap);
		}

		FacesContext ctx = FacesContext.getCurrentInstance();
		TreeBean tb = (TreeBean) context.getApplication().getVariableResolver()
				.resolveVariable(ctx, "TreeBean");
		tb.setTreeMap(_treeMap);
	}
}
