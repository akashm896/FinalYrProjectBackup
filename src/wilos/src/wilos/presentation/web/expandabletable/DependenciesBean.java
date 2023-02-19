/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2007 Sebastien BALARD <sbalard@wilos-project.org>
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

package wilos.presentation.web.expandabletable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import wilos.business.services.misc.concreteactivity.ConcreteActivityService;
import wilos.business.services.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElementService;
import wilos.business.services.misc.concreteworkbreakdownelement.ConcreteWorkOrderService;
import wilos.business.services.misc.project.ProjectService;
import wilos.business.services.spem2.workbreakdownelement.WorkBreakdownElementService;
import wilos.business.services.spem2.workbreakdownelement.WorkOrderService;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkOrder;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkOrderId;
import wilos.model.misc.project.Project;
import wilos.model.spem2.process.Process;
import wilos.presentation.web.process.ProcessBean;
import wilos.presentation.web.tree.TreeBean;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;
import wilos.resources.LocaleBean;
import wilos.utils.Constantes;

/**
 * @author Sebastien
 * 
 */
public class DependenciesBean {

    private ConcreteWorkBreakdownElementService concreteWorkBreakdownElementService;

    private WorkBreakdownElementService workBreakdownElementService;

    private ConcreteWorkOrderService concreteWorkOrderService;

    private ConcreteActivityService concreteActivityService;

    private WorkOrderService workOrderService;

    private ProjectService projectService;

    private String selectedPredecessor = "default";

    private String selectedLinkType = "default";

    private String selectedSuccessor = "default";

    private String originalEditedLinkType;

    private String editedLinkType;

    private int editedLine;

    private boolean isDisableLinkTypeCombo = true;

    private boolean isDisableSuccessorCombo = true;

    private boolean isRendererConcreteWorkOrderCreationButton = false;

    private boolean isVisibleDependenciesPanel = false;

    private List<HashMap<String, Object>> dependencies;

    private String currentProjectId = "";

    /**
     * Create a dependency on the current project with the value of the three
     * different comboBox
     * 
     * @return an empty String
     */
    public String createDependency() {
	String projectId = (String) WebSessionService
		.getAttribute(WebSessionService.PROJECT_ID);

	this.concreteWorkOrderService.saveConcreteWorkOrder(
		this.selectedPredecessor, this.selectedSuccessor,
		this.selectedLinkType, projectId);

	this.selectedPredecessor = "default";
	this.selectedLinkType = "default";
	this.selectedSuccessor = "default";
	this.isDisableLinkTypeCombo = true;
	this.isDisableSuccessorCombo = true;
	this.isRendererConcreteWorkOrderCreationButton = false;

	TreeBean tb = (TreeBean) WebCommonService.getBean("TreeBean");
	tb.refreshProjectTree();

	return "";

    }

    /**
     * Remove a created dependency
     * 
     * @return an empty string
     */
    public String removeDependency() {

	FacesContext context = FacesContext.getCurrentInstance();
	Map map = context.getExternalContext().getRequestParameterMap();
	int line = new Integer((String) map.get("line"));

	ConcreteWorkOrderId cwoi = new ConcreteWorkOrderId();
	cwoi.setConcretePredecessorId((String) this.dependencies.get(line).get(
		"pred_id"));
	cwoi.setConcreteSuccessorId((String) this.dependencies.get(line).get(
		"succ_id"));
	cwoi.setConcreteLinkTypeId((String) this.dependencies.get(line).get(
		"linkType"));

	ConcreteWorkOrder cwo = this.concreteWorkOrderService
		.getConcreteWorkOrder(cwoi);

	this.concreteWorkOrderService.deleteConcreteWorkOrder(cwo);

	TreeBean tb = (TreeBean) WebCommonService.getBean("TreeBean");
	tb.refreshProjectTree();

	return "";
    }

    /**
     * Editing a dependency
     * 
     * @param e
     *                event received when a user clicks on edit button in the
     *                ice:datatable component
     */
    public void editDependency(ActionEvent e) {

	FacesContext context = FacesContext.getCurrentInstance();
	Map map = context.getExternalContext().getRequestParameterMap();
	this.editedLine = new Integer((String) map.get("editLine"));

	for (HashMap<String, Object> hm : this.dependencies) {
	    int currentLine = (Integer) hm.get("line");
	    if (currentLine == this.editedLine) {
		hm.put("isEditable", new Boolean(false));
		hm.put("isEdited", new Boolean(true));
		this.editedLinkType = (String) hm.get("linkType");
		this.originalEditedLinkType = (String) hm.get("linkType");
	    } else {
		hm.put("isEditable", new Boolean(false));
	    }
	}
    }

    /**
     * Saving a dependency
     * 
     * @param e
     *                event received when a user clicks on edit button in the
     *                ice:datatable component
     */
    public void saveDependency(ActionEvent e) {

	FacesContext context = FacesContext.getCurrentInstance();
	Map map = context.getExternalContext().getRequestParameterMap();

	ConcreteWorkOrderId cwoi = new ConcreteWorkOrderId();
	cwoi.setConcretePredecessorId((String) this.dependencies.get(
		this.editedLine).get("pred_id"));
	cwoi.setConcreteSuccessorId((String) this.dependencies.get(
		this.editedLine).get("succ_id"));
	cwoi.setConcreteLinkTypeId(this.originalEditedLinkType);
	ConcreteWorkOrder cwo = this.concreteWorkOrderService
		.getConcreteWorkOrder(cwoi);

	this.concreteWorkOrderService.updateWorkOrderLinkType(cwo,
		this.editedLinkType);

	for (HashMap<String, Object> hm : this.dependencies) {
	    int currentLine = (Integer) hm.get("line");
	    if (currentLine == this.editedLine) {
		hm.put("isEditable", new Boolean(true));
		hm.put("isEdited", new Boolean(false));
	    } else {
		hm.put("isEditable", new Boolean(true));
	    }
	}

	this.editedLine = -1;

	TreeBean tb = (TreeBean) WebCommonService.getBean("TreeBean");
	tb.refreshProjectTree();
    }

    /**
     * This method allows to return a list of concrete predecessors thanks to
     * the changePredecessorListener value
     * 
     * @return the concretePredecessors
     */
    public List<SelectItem> getPredecessors() {

	List<SelectItem> concretePredecessors = new ArrayList<SelectItem>();

	concretePredecessors.add(new SelectItem("default", LocaleBean
		.getText("dependencies.predecessor.combobox.default.label")));

	for (ConcreteWorkBreakdownElement cwbde : this.concreteWorkBreakdownElementService
		.getAllConcreteWorkBreakdownElements(this.projectService
			.getProject((String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID)))) {
	    ConcreteWorkBreakdownElement sup = this.concreteWorkBreakdownElementService
		    .getSuperConcreteActivitiesFromConcreteWorkBreakdownElement(
			    cwbde).get(0);

	    String parentConcreteName = "";
	    if (!(sup instanceof Project)) {
		parentConcreteName = sup.getConcreteName() + " / ";
	    }
	    concretePredecessors.add(new SelectItem(cwbde.getId(),
		    parentConcreteName + cwbde.getConcreteName()));
	}

	return concretePredecessors;
    }

    /**
     * This method allows to return a list of concrete link type thanks to the
     * changePredecessorListener value
     * 
     * @return the concreteLinkTypes
     */
    public List<SelectItem> getLinkTypes() {

	List<SelectItem> concreteLinkTypes = new ArrayList<SelectItem>();

	concreteLinkTypes.add(new SelectItem("default", LocaleBean
		.getText("dependencies.linktype.combobox.default.label")));

	for (String linkType : Constantes.WorkOrderTypes) {
	    if (!this.concreteWorkOrderService.existsConcreteWorkOrder(
		    this.selectedPredecessor, this.selectedSuccessor, linkType)) {
		concreteLinkTypes.add(new SelectItem(linkType));
	    }
	}

	return concreteLinkTypes;

    }

    /**
     * This method allow to return a list of concrete successors thanks to the
     * changePredecessorListener value
     * 
     * @return the concrete successors
     */
    public List<SelectItem> getSuccessors() {

	List<SelectItem> concreteSuccessors = new ArrayList<SelectItem>();

	concreteSuccessors.add(new SelectItem("default", LocaleBean
		.getText("dependencies.successor.combobox.default.label")));

	if (!this.selectedPredecessor.equals("default")) {
	    ConcreteWorkBreakdownElement cwbdePredecessor = this.concreteWorkBreakdownElementService
		    .getConcreteWorkBreakdownElementDao()
		    .getConcreteWorkBreakdownElement(this.selectedPredecessor);

	    // predecessor parent(s)
	    List<ConcreteWorkBreakdownElement> superConcreteActivities = this.concreteWorkBreakdownElementService
		    .getSuperConcreteActivitiesFromConcreteWorkBreakdownElement(cwbdePredecessor);

	    // for all parent activities (generally only one)
	    for (ConcreteWorkBreakdownElement supercact : superConcreteActivities) {

		if (supercact instanceof ConcreteActivity) {
		    // for all children of the parent activity
		    for (ConcreteBreakdownElement nestedCbde : this.concreteActivityService
			    .getConcreteBreakdownElements((ConcreteActivity) supercact)) {

			// no roles or products, just CWBDE
			if (nestedCbde instanceof ConcreteWorkBreakdownElement) {
			    // avoid dependency between an activity an itself
			    if (!nestedCbde.getId().equals(
				    this.selectedPredecessor)) {
				String parentConcreteName = "";
				if (!(supercact instanceof Project)) {
				    parentConcreteName = supercact
					    .getConcreteName()
					    + " / ";
				}
				concreteSuccessors
					.add(new SelectItem(
						nestedCbde.getId(),
						parentConcreteName
							+ nestedCbde
								.getConcreteName()));
			    }
			}
		    }
		}

	    }
	}
	return concreteSuccessors;
    }

    /**
     * listener on the processes selection combobox this listener allows to put
     * a value on the private attribute selectedPredecessor
     */
    public void changePredecessorListener(ValueChangeEvent evt) {
	this.selectedPredecessor = (String) evt.getNewValue();

	if (!this.selectedPredecessor.equals("default")) {
	    this.isDisableSuccessorCombo = false;

	} else {
	    this.isDisableSuccessorCombo = true;
	    this.isDisableLinkTypeCombo = true;
	    this.isRendererConcreteWorkOrderCreationButton = false;
	}
    }

    /**
     * listener on the processes selection combobox this listener allows to put
     * a value on the private attribute selectedSuccessor
     */
    public void changeSuccessorListener(ValueChangeEvent evt) {

	this.selectedSuccessor = (String) evt.getNewValue();

	if (!this.selectedSuccessor.equals("default")) {
	    this.isDisableLinkTypeCombo = false;
	} else {
	    this.isDisableLinkTypeCombo = true;
	}
    }

    /**
     * listener on the link type selection combobox this listener allows to put
     * a value on the private attribute selectedLinkType
     */
    public void changeLinkTypeListener(ValueChangeEvent evt) {

	this.selectedLinkType = (String) evt.getNewValue();

	if (!this.selectedLinkType.equals("default")) {
	    this.isRendererConcreteWorkOrderCreationButton = true;
	} else {
	    this.isRendererConcreteWorkOrderCreationButton = false;
	}
    }

    /**
     * This method allow to return a SelectItem list of Link
     * 
     * @return a SelectItem List of link
     */
    public List<SelectItem> getEditLinkTypes() {
	List<SelectItem> linkTypes = new ArrayList<SelectItem>();
	FacesContext context = FacesContext.getCurrentInstance();
	Map map = context.getExternalContext().getRequestParameterMap();

	String concretePredecessorId = (String) this.dependencies.get(
		this.editedLine).get("pred_id");
	String concreteSuccessorId = (String) this.dependencies.get(
		this.editedLine).get("succ_id");
	String currentLinkType = (String) this.dependencies
		.get(this.editedLine).get("linkType");

	for (String linkType : Constantes.WorkOrderTypes) {
	    if (!this.concreteWorkOrderService.existsConcreteWorkOrder(
		    concretePredecessorId, concreteSuccessorId, linkType)
		    || linkType.equals(currentLinkType)) {
		linkTypes.add(new SelectItem(linkType));
	    }
	}
	return linkTypes;
    }

    /**
     * listener on the link type selection combobox in the dependencies table
     */
    public void changeEditLinkTypeListener(ValueChangeEvent evt) {

	this.editedLinkType = evt.getNewValue().toString();
    }

    // TODO utile ?
    /**
     * Watch dependencies management view order
     * 
     * @param evt
     */
    public void dependenciesManagementActionListener(ActionEvent evt) {

	ProcessBean processBean = (ProcessBean) WebCommonService
		.getBean("ProcessBean");
	processBean
		.setInstanciationDependenciesView("view_dependencies_panelGroup");
    }

    // TODO utile ?
    /**
     * Watch back to instanciation view order
     * 
     * @param evt
     */
    public void backActionListener(ActionEvent evt) {

	ProcessBean processBean = (ProcessBean) WebCommonService
		.getBean("ProcessBean");
	processBean
		.setInstanciationDependenciesView("view_instanciation_panelGroup");
    }

    /**
     * This method allow to return the current instance of
     * ConcreteWorkBreakdownElementService
     * 
     * @return the concreteWorkBreakdownElementService
     */
    public ConcreteWorkBreakdownElementService getConcreteWorkBreakdownElementService() {
	return this.concreteWorkBreakdownElementService;
    }

    /**
     * This method allow to set the current instance of
     * ConcreteWorkBreakdownElementService
     * 
     * @param _concreteWorkBreakdownElementService
     *                the concreteWorkBreakdownElementService to set
     */
    public void setConcreteWorkBreakdownElementService(
	    ConcreteWorkBreakdownElementService _concreteWorkBreakdownElementService) {
	this.concreteWorkBreakdownElementService = _concreteWorkBreakdownElementService;
    }

    /**
     * getter of selectedPredecessor attribute
     * 
     * @return the selectedPredecessor
     */
    public String getSelectedPredecessor() {
	return this.selectedPredecessor;
    }

    /**
     * setter of selectedPredecessor attribute
     * 
     * @param _predecessorSelected
     *                the selectedPredecessor to set
     */
    public void setSelectedPredecessor(String _predecessorSelected) {
	this.selectedPredecessor = _predecessorSelected;
    }

    /**
     * This method allow to return the current instance of ProjectService
     * 
     * @return the projectService
     */
    public ProjectService getProjectService() {
	return this.projectService;
    }

    /**
     * This method allow to set the current instance of ProjectService
     * 
     * @param _projectService
     *                the projectService to set
     */
    public void setProjectService(ProjectService _projectService) {
	this.projectService = _projectService;
    }

    /**
     * getter of isDisableLinkTypecombo Boolean attribute
     * 
     * @return the visibleLinkCombo
     */
    public boolean getIsDisableLinkTypeCombo() {
	return this.isDisableLinkTypeCombo;
    }

    /**
     * setter of isDisableLinkTypecombo Boolean attribute
     * 
     * @param _visibleLinkCombo
     *                the visibleLinkCombo to set
     */
    public void setDisableLinkTypeCombo(boolean _visibleLinkCombo) {
	this.isDisableLinkTypeCombo = _visibleLinkCombo;
    }

    /**
     * getter of selectedLinkType String attribute
     * 
     * @return the selectedLinkType
     */
    public String getSelectedLinkType() {
	return this.selectedLinkType;
    }

    /**
     * setter of selectedLinkType String attribute
     * 
     * @param _linkTypeSelected
     *                the selectedLinkType to set
     */
    public void setSelectedLinkType(String _linkTypeSelected) {
	this.selectedLinkType = _linkTypeSelected;
    }

    /**
     * This method allow to return the current instance of workBreakdownElementService
     * 
     * @return the workBreakdownElementService
     */
    public WorkBreakdownElementService getWorkBreakdownElementService() {
	return this.workBreakdownElementService;
    }

    /**
     * This method allow to set the current instance of
     * workBreakdownElementService
     * 
     * @param _workBreakdownElementService
     *                the workBreakdownElementService to set
     */
    public void setWorkBreakdownElementService(
	    WorkBreakdownElementService _workBreakdownElementService) {
	this.workBreakdownElementService = _workBreakdownElementService;
    }

    /**
     * This method allow to return the current instance of workOrderService
     * 
     * @return the workOrderService
     */
    public WorkOrderService getWorkOrderService() {
	return this.workOrderService;
    }

    /**
     * This method allow to set the current instance of workOrderService
     * 
     * @param _workOrderService
     *                the workOrderService to set
     */
    public void setWorkOrderService(WorkOrderService _workOrderService) {
	this.workOrderService = _workOrderService;
    }

    /**
     * getter of selectedSuccessor String attribute
     * 
     * @return the selectedSuccessor
     */
    public String getSelectedSuccessor() {
	return this.selectedSuccessor;
    }

    /**
     * setter of selectedSuccessor String attribute
     * 
     * @param _selectedSuccessor
     *                the selectedSuccessor to set
     */
    public void setSelectedSuccessor(String _selectedSuccessor) {
	this.selectedSuccessor = _selectedSuccessor;
    }

    /**
     * getter of isDisableSuccessorCombo boolean attribute
     * 
     * @return the isDisableSuccessorCombo
     */
    public boolean getIsDisableSuccessorCombo() {
	return this.isDisableSuccessorCombo;
    }

    /**
     * setter of isDisableSuccessorCombo boolean attribute
     * 
     * @param _isDisableSuccessorCombo
     *                the isDisableSuccessorCombo to set
     */
    public void setDisableSuccessorCombo(boolean _isDisableSuccessorCombo) {
	this.isDisableSuccessorCombo = _isDisableSuccessorCombo;
    }

    /**
     * getter of rendererConcreteWorkOrderCreationButton boolean attribute
     * 
     * @return the isRendererConcreteWorkOrderCreationButton
     */
    public boolean getIsRendererConcreteWorkOrderCreationButton() {
	return this.isRendererConcreteWorkOrderCreationButton;
    }

    /**
     * setter of rendererConcreteWorkOrderCreationButton boolean attribute
     * 
     * @param _isDisableConcreteWorkOrderCreationButton
     *                the isRendererConcreteWorkOrderCreationButton to set
     */
    public void setRendererConcreteWorkOrderCreationButton(
	    boolean _isDisableConcreteWorkOrderCreationButton) {
	this.isRendererConcreteWorkOrderCreationButton = _isDisableConcreteWorkOrderCreationButton;
    }

    /**
     * getter of dependencies HashMap List attribute
     * 
     * @return a HashMap List of dependencies
     */
    public List<HashMap<String, Object>> getDependencies() {

	String projectId = (String) WebSessionService
		.getAttribute(WebSessionService.PROJECT_ID);
	List<ConcreteWorkOrder> dependencyList = this.concreteWorkOrderService
		.getAllConcreteWorkOrdersFromProject(projectId);

	if (this.dependencies == null
		|| this.dependencies.size() != dependencyList.size()
		|| !this.currentProjectId.equals(projectId)) {

	    this.currentProjectId = projectId;

	    this.dependencies = new ArrayList<HashMap<String, Object>>();
	    int i = 0;

	    for (ConcreteWorkOrder cwo : dependencyList) {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("line", i);
		ConcreteWorkBreakdownElement cPred = this.concreteWorkBreakdownElementService
			.getConcreteWorkBreakdownElementDao()
			.getConcreteWorkBreakdownElement(
				cwo.getConcreteWorkOrderId()
					.getConcretePredecessorId());
		hm.put("pred_id", cPred.getId());
		hm.put("pred", cPred.getConcreteName());
		hm.put("linkType", cwo.getConcreteLinkType());
		ConcreteWorkBreakdownElement cSucc = this.concreteWorkBreakdownElementService
			.getConcreteWorkBreakdownElementDao()
			.getConcreteWorkBreakdownElement(
				cwo.getConcreteWorkOrderId()
					.getConcreteSuccessorId());
		hm.put("succ_id", cSucc.getId());
		hm.put("succ", cSucc.getConcreteName());
		hm.put("isEditable", new Boolean(true));
		hm.put("isEdited", new Boolean(false));

		this.dependencies.add(hm);
		i++;
	    }
	}
	return this.dependencies;
    }

    /**
     * setter of dependencies HashMap List attribute
     * 
     * @param _dependencies
     *                a dependencies List to set
     */
    public void setDependencies(List<HashMap<String, Object>> _dependencies) {
	this.dependencies = _dependencies;
    }

    /**
     * This method allow to return the current instance of concreteWorkOrderService
     * 
     * @return the concreteWorkOrderService
     */
    public ConcreteWorkOrderService getConcreteWorkOrderService() {
	return this.concreteWorkOrderService;
    }

    /**
     * This method allow to set the current instance of concreteWorkOrderService
     * 
     * @param _concreteWorkOrderService
     *                the concreteWorkOrderService to set
     */
    public void setConcreteWorkOrderService(
	    ConcreteWorkOrderService _concreteWorkOrderService) {
	this.concreteWorkOrderService = _concreteWorkOrderService;
    }

    /**
     * getter of isVisibleDependenciesPanel boolean attribute
     * 
     * @return the isVisibleDependenciesPanel
     */
    public boolean getIsVisibleDependenciesPanel() {
	Project project = this.projectService
		.getProject((String) WebSessionService
			.getAttribute(WebSessionService.PROJECT_ID));
	Process process = this.projectService.getProcessFromProject(project);
	if (process != null) {
	    this.isVisibleDependenciesPanel = true;
	} else {
	    this.isVisibleDependenciesPanel = false;
	}
	return this.isVisibleDependenciesPanel;
    }

    /**
     * setter of IsVisibleDependenciesPanel boolean attribute
     * 
     * @param isVisibleDependenciesPanel
     *                the isVisibleDependenciesPanel to set
     */
    public void setIsVisibleDependenciesPanel(boolean isVisibleDependenciesPanel) {
	this.isVisibleDependenciesPanel = isVisibleDependenciesPanel;
    }

    /**
     * getter of isVisibleAddDependenciesForm boolean attribute
     * 
     * @return boolean
     */

    public boolean getIsVisibleAddDependenciesForm() {
	return this.getPredecessors().size() > 1;
    }

    /**
     * This method allow to return the current instance of concreteActivityService
     * 
     * @return the concreteActivityService
     */
    public ConcreteActivityService getConcreteActivityService() {
	return this.concreteActivityService;
    }

    /**
     * This method allow to set the current instance of concreteActivityService
     * 
     * @param _concreteActivityService
     *                the concreteActivityService to set
     */
    public void setConcreteActivityService(
	    ConcreteActivityService _concreteActivityService) {
	this.concreteActivityService = _concreteActivityService;
    }
}
