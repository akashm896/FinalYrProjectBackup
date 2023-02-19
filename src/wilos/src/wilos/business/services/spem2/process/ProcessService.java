/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

package wilos.business.services.spem2.process;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.business.services.misc.concretebreakdownelement.ConcreteBreakdownElementService;
import wilos.business.services.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElementService;
import wilos.business.services.misc.concreteworkbreakdownelement.ConcreteWorkOrderService;
import wilos.business.services.misc.stateservice.StateService;
import wilos.business.services.misc.wilosuser.ProcessManagerService;
import wilos.business.services.spem2.activity.ActivityService;
import wilos.business.services.spem2.breakdownelement.BreakdownElementService;
import wilos.business.services.spem2.checklist.CheckListService;
import wilos.business.services.spem2.iteration.IterationService;
import wilos.business.services.spem2.milestone.MilestoneService;
import wilos.business.services.spem2.phase.PhaseService;
import wilos.business.services.spem2.role.RoleDefinitionService;
import wilos.business.services.spem2.task.TaskDefinitionService;
import wilos.business.services.spem2.task.TaskDescriptorService;
import wilos.business.services.spem2.workbreakdownelement.WorkBreakdownElementService;
import wilos.business.services.spem2.workbreakdownelement.WorkOrderService;
import wilos.business.services.spem2.workproduct.WorkProductDefinitionService;
import wilos.hibernate.misc.project.ProjectDao;
import wilos.hibernate.spem2.element.ElementDao;
import wilos.hibernate.spem2.guide.GuidanceDao;
import wilos.hibernate.spem2.process.ProcessDao;
import wilos.hibernate.spem2.role.RoleDescriptorDao;
import wilos.hibernate.spem2.section.SectionDao;
import wilos.hibernate.spem2.task.StepDao;
import wilos.hibernate.spem2.workbreakdownelement.WorkBreakdownElementDao;
import wilos.hibernate.spem2.workbreakdownelement.WorkOrderDao;
import wilos.hibernate.spem2.workproduct.WorkProductDescriptorDao;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.project.Project;
import wilos.model.misc.wilosuser.WilosUser;
import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.breakdownelement.BreakdownElement;
import wilos.model.spem2.checklist.CheckList;
import wilos.model.spem2.guide.Guidance;
import wilos.model.spem2.iteration.Iteration;
import wilos.model.spem2.milestone.Milestone;
import wilos.model.spem2.phase.Phase;
import wilos.model.spem2.process.Process;
import wilos.model.spem2.role.RoleDefinition;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.model.spem2.section.Section;
import wilos.model.spem2.task.Step;
import wilos.model.spem2.task.TaskDefinition;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.model.spem2.workbreakdownelement.WorkBreakdownElement;
import wilos.model.spem2.workbreakdownelement.WorkOrder;
import wilos.model.spem2.workbreakdownelement.WorkOrderId;
import wilos.model.spem2.workproduct.WorkProductDefinition;
import wilos.model.spem2.workproduct.WorkProductDescriptor;
import wilos.tools.imports.epfcomposer.EpfComposerProcessImportManager;

/**
 * ProcessService is a transactional class, that manage operations about
 * process, requested by web pages (?)
 * 
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class ProcessService {

    private ConcreteBreakdownElementService concreteBreakdownElementService;
    private BreakdownElementService breakdownElementService;
    private ConcreteWorkBreakdownElementService concreteWorkBreakdownElementService;
    private WorkBreakdownElementService workBreakdownElementService;
    private PhaseService phaseService;
    private IterationService iterationService;
    private ActivityService activityService;
    private TaskDescriptorService taskDescriptorService;
    private TaskDefinitionService taskDefinitionService;
    private RoleDescriptorDao roleDescriptorDao;
    private RoleDefinitionService roleDefinitionService;
    private ProcessManagerService processManagerService;
    private CheckListService checkListService;
    private ElementDao elementDao;
    private ProcessDao processDao;
    private StepDao stepDao;
    private ProjectDao projectDao;
    private GuidanceDao guidanceDao;
    private SectionDao sectionDao;
    private WorkBreakdownElementDao workBreakdownElementDao;
    private WorkOrderDao workOrderDao;
    private WorkOrderService workOrderService;
    protected final Log logger = LogFactory.getLog(this.getClass());
    private WorkProductDescriptorDao workProductDescriptorDao;
    private WorkProductDefinitionService workProductDefinitionService;
    private ConcreteWorkOrderService concreteWorkOrderService;
    private MilestoneService milestoneService;

    public enum Classe {
	Phase, Iteration, Activity, RoleDescriptor, WorkProductDescriptor, TaskDescriptor, Milestone
    };

    public Process importProcessFromEpfComposer(File _file) {
	Process process = null;
	try {
	    // logger.debug("### ProcessService ### ParsingXML "
	    // + _file.getAbsolutePath() + " abs path = "
	    // + _file.getPath());
	    String slash = System.getProperty("file.separator");

	    String path = _file.getAbsolutePath().substring(0,
		    _file.getAbsolutePath().lastIndexOf(slash));

	    // logger.debug("### ProcessService ### ParsingXML PATH
	    // == " +
	    // path);
	    EpfComposerProcessImportManager epfComposerProcessImportManager = new EpfComposerProcessImportManager();
	    process = epfComposerProcessImportManager.importProcess(_file
		    .getAbsolutePath(), path);
	    if (process != null) {
		process.setFolderPath(path);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return process;
    }

    /**
     * 
     * Method for saving a Process
     * 
     * @param _process
     */
    public String saveProcess(Process _process, String _processManagerId) {

	Process clone = null;

	// clone creation for the save of the dependencies
	try {
	    clone = _process.clone();
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}

	// elements of collection getting
	List<BreakdownElement> bdes = new ArrayList<BreakdownElement>();
	bdes.addAll(_process.getBreakdownElements());

	// elements of collection getting
	List<Guidance> guid = new ArrayList<Guidance>();

	// dependencies erasing
	_process.getBreakdownElements().clear();
	_process.getPredecessors().clear();
	_process.getProjects().clear();
	_process.getSuccessors().clear();
	_process.getSuperActivities().clear();
	// save of the project
	this.processDao.saveOrUpdateProcess(_process);

	// in function of element type
	for (BreakdownElement bde : bdes) {
	    try {
		// parse all elements
		guid = this.parseType(guid, bde, true, true);
	    } catch (IllegalArgumentException e) {
		logger
			.warn("### saveProcess ### Unknown element found during parsing ");
	    }
	}

	// saving of the attached guidances to the process
	for (Guidance g : guid) {
	    // FIXME : FS#77 - Problem with the new OpenUp xml (1.0)
	    if (!g.getGuid().equals("__O7tAMVvEduLYZUGfgZrkQ"))
		this.parseGuidance(g);
	}

	this.saveAllWorkOrdersOfAHierarchy(_process);

	// destroy the persistance of the collections
	_process.setBreakdownElements(this.activityService
		.getAllBreakdownElements(_process));

	_process.setSuperActivities(this.breakdownElementService
		.getSuperActivities(_process));

	// clone dependencies getting
	_process.addAllBreakdownElements(clone.getBreakdownElements());
	_process.addAllSuperActivities(clone.getSuperActivities());
	WilosUser pm = this.processManagerService
		.getProcessManager(_processManagerId);
	_process.addProcessManager(pm);

	// update of the project
	String id = this.processDao.saveOrUpdateProcess(_process);
	return id;
    }

    private List<Guidance> parseType(List<Guidance> lstGuid,
	    BreakdownElement bde, boolean withPhase, boolean withIteration)
	    throws IllegalArgumentException {
	String type = bde.getClass().getSimpleName();
	// java.lang.IllegalArgumentException possible on Classe.ValueOf
	Classe typeBde = Classe.valueOf(type);
	switch (typeBde) {
	case Phase:
	    if (withPhase) {
		// only for saveProcess
		Phase ph = (Phase) bde;
		lstGuid = this.parsePhase(ph, lstGuid);
		break;
	    }
	case Iteration:
	    if (withIteration) {
		Iteration it = (Iteration) bde;
		lstGuid = this.parseIteration(it, lstGuid);
		break;
	    }
	case Activity:
	    Activity act = (Activity) bde;
	    lstGuid = this.parseActivity(act, lstGuid);
	    break;
	case RoleDescriptor:
	    RoleDescriptor rd = (RoleDescriptor) bde;
	    lstGuid = this.parseRoleDescriptor(rd, lstGuid);
	    break;
	case WorkProductDescriptor:
	    WorkProductDescriptor wpd = (WorkProductDescriptor) bde;
	    lstGuid = this.parseWorkProductDescriptor(wpd, lstGuid);
	    break;
	case TaskDescriptor:
	    TaskDescriptor td = (TaskDescriptor) bde;
	    lstGuid = this.parseTaskDescriptor(td, lstGuid);
	    break;
	case Milestone:
	    Milestone mi = (Milestone) bde;
	    lstGuid = this.parseMilestone(mi, lstGuid);
	}
	return lstGuid;
    }

    /**
     * Save all the WorkOrders of _wbde and of these BreakdownElements
     * 
     * @param _wbde
     */
    private void saveAllWorkOrdersOfAHierarchy(Activity _act) {

	this.saveAllWorkOrders(_act);

	// save all workorder link
	for (BreakdownElement bde : _act.getBreakdownElements()) {

	    try {
		String type = bde.getClass().getSimpleName();
		// java.lang.IllegalArgumentException possible
		// on Classe.ValueOf
		Classe typeBde = Classe.valueOf(type);
		switch (typeBde) {
		case Phase:
		    Phase ph = (Phase) bde;
		    this.saveAllWorkOrdersOfAHierarchy(ph);
		    break;
		case Iteration:
		    Iteration it = (Iteration) bde;
		    this.saveAllWorkOrdersOfAHierarchy(it);
		    break;
		case Activity:
		    Activity act = (Activity) bde;
		    this.saveAllWorkOrdersOfAHierarchy(act);
		    ;
		    break;
		case TaskDescriptor:
		    TaskDescriptor td = (TaskDescriptor) bde;
		    this.saveAllWorkOrders(td);
		    ;
		    break;
		case Milestone:
		    Milestone mi = (Milestone) bde;
		    this.saveAllWorkOrders(mi);
		}
	    } catch (IllegalArgumentException e) {
		logger
			.warn("### saveAllWorkOrdersOfAHierarchy ### Unknown element found during parsing ");
	    }
	}
    }

    /**
     * 
     * @param _wbde
     */
    private void saveAllWorkOrders(WorkBreakdownElement _wbde) {

	// System.out.println("$$$"+_wbde.getPresentationName());

	String s = "";
	String s2 = "";
	for (Activity act : _wbde.getSuperActivities()) {
	    s = act.getPresentationName();
	    for (Activity a : act.getSuperActivities()) {
		s2 = a.getPresentationName();
		break;
	    }
	    break;
	}

	// WorkOrders getting
	List<WorkOrder> wos = new ArrayList<WorkOrder>();
	wos.addAll(_wbde.getSuccessors());

	for (WorkOrder wo : wos) {
	    WorkOrderId workOrderId = new WorkOrderId();
	    workOrderId.setPredecessorId(wo.getPredecessor().getId());
	    workOrderId.setSuccessorId(wo.getSuccessor().getId());
	    wo.setWorkOrderId(workOrderId);
	    this.workOrderDao.saveOrUpdateWorkOrder(wo);
	    // System.out.println(("###Pred : " + _wbde.getId() + "
	    // " + _wbde.getName()));
	    // System.out.println("###WorkOrder sauve ("
	    // + wo.getWorkOrderId().getPredecessorId() + ","
	    // + wo.getWorkOrderId().getSuccessorId() + "): "
	    // + wo.getPredecessor().getPresentationName() + " -> "
	    // + wo.getSuccessor().getPresentationName() + " ###" +
	    // s + " ###" + s2);
	}
    }

    /**
     * 
     * Method to parse a Phase
     * 
     * @param _ph
     */
    private List<Guidance> parsePhase(Phase _ph, List<Guidance> guid) {

	Phase clone = null;

	List<Guidance> tmp = new ArrayList<Guidance>();
	tmp.addAll(guid);

	try {
	    clone = _ph.clone();
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}

	List<BreakdownElement> bdes = new ArrayList<BreakdownElement>();
	bdes.addAll(_ph.getBreakdownElements());

	// Guides
	Set<Guidance> guidances = new HashSet<Guidance>();
	guidances.addAll(_ph.getGuidances());

	// clean of dependancies of _ph
	_ph.getBreakdownElements().clear();
	_ph.getPredecessors().clear();
	_ph.getSuccessors().clear();
	_ph.getSuperActivities().clear();
	_ph.getGuidances().clear();
	this.phaseService.getPhaseDao().saveOrUpdatePhase(_ph);
	// System.out.println("###Phase vide sauve");

	for (Guidance g : guidances) {
	    if (!tmp.contains(g)) {
		tmp.add(g);
	    }
	}

	for (BreakdownElement bde : bdes) {
	    try {
		// parse all without Phase (never here)
		tmp = this.parseType(tmp, bde, false, true);
	    } catch (IllegalArgumentException e) {
		logger
			.warn("### parsePhase ### Unknown element found during parsing ");
	    }
	}

	_ph.setBreakdownElements(this.activityService
		.getAllBreakdownElements(_ph));
	_ph.setPredecessors(this.workBreakdownElementService
		.getPredecessors(_ph));
	_ph.setSuccessors(this.workBreakdownElementService.getSuccessors(_ph));
	_ph.setSuperActivities(this.breakdownElementService
		.getSuperActivities(_ph));
	_ph.setGuidances(this.activityService.getAllGuidances(_ph));

	// clone dependencies getting
	_ph.addAllBreakdownElements(clone.getBreakdownElements());
	_ph.addAllSuperActivities(clone.getSuperActivities());
	_ph.setGuidances(clone.getGuidances());

	// Parse for guidances
	this.phaseService.getPhaseDao().saveOrUpdatePhase(_ph);
	// System.out.println("###Phase sauve");

	_ph.addAllPredecessors(clone.getPredecessors());
	_ph.addAllSuccessors(clone.getSuccessors());

	return tmp;
    }

    /**
     * 
     * Method to parse an Iteration
     * 
     * @param _it
     */
    private List<Guidance> parseIteration(Iteration _it, List<Guidance> guid) {

	Iteration clone = null;

	List<Guidance> tmp = new ArrayList<Guidance>();
	tmp.addAll(guid);

	try {
	    clone = _it.clone();
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}

	List<BreakdownElement> bdes = new ArrayList<BreakdownElement>();
	bdes.addAll(_it.getBreakdownElements());

	Set<Guidance> guidances = new HashSet<Guidance>();
	guidances.addAll(_it.getGuidances());

	_it.getBreakdownElements().clear();
	_it.getPredecessors().clear();
	_it.getSuccessors().clear();
	_it.getSuperActivities().clear();
	_it.getGuidances().clear();
	this.iterationService.getIterationDao().saveOrUpdateIteration(_it);
	// System.out.println("###Iteration vide sauve");

	for (Guidance g : guidances) {
	    if (!tmp.contains(g)) {
		tmp.add(g);
	    }
	}
	for (BreakdownElement bde : bdes) {
	    try {
		// parse all without Phase or Iteration
		tmp = this.parseType(tmp, bde, false, false);
	    } catch (IllegalArgumentException e) {
		logger
			.warn("### parseIteration ### Unknown element found during parsing ");
	    }
	}
	_it.setBreakdownElements(this.activityService
		.getAllBreakdownElements(_it));
	_it.setPredecessors(this.workBreakdownElementService
		.getPredecessors(_it));
	_it.setSuccessors(this.workBreakdownElementService.getSuccessors(_it));
	_it.setSuperActivities(this.breakdownElementService
		.getSuperActivities(_it));
	_it.setGuidances(this.activityService.getAllGuidances(_it));

	// clone dependencies getting
	_it.addAllBreakdownElements(clone.getBreakdownElements());
	_it.addAllSuperActivities(clone.getSuperActivities());
	_it.setGuidances(clone.getGuidances());

	this.iterationService.getIterationDao().saveOrUpdateIteration(_it);
	// System.out.println("###Iteration sauve");
	_it.addAllPredecessors(clone.getPredecessors());
	_it.addAllSuccessors(clone.getSuccessors());
	return tmp;
    }

    /**
     * 
     * Method to parse an Activity
     * 
     * @param _act
     */
    private List<Guidance> parseActivity(Activity _act, List<Guidance> guid) {

	Activity clone = null;

	List<Guidance> tmp = new ArrayList<Guidance>();
	tmp.addAll(guid);

	try {
	    clone = _act.clone();
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}

	List<BreakdownElement> bdes = new ArrayList<BreakdownElement>();
	bdes.addAll(_act.getBreakdownElements());

	Set<Guidance> guidances = new HashSet<Guidance>();
	guidances.addAll(_act.getGuidances());

	_act.getBreakdownElements().clear();
	_act.getPredecessors().clear();
	_act.getSuccessors().clear();
	_act.getSuperActivities().clear();
	_act.getGuidances().clear();
	this.activityService.saveActivity(_act);
	// System.out.println("###Activity vide sauve");
	for (Guidance g : guidances) {
	    if (!tmp.contains(g)) {
		tmp.add(g);
	    }
	}
	for (BreakdownElement bde : bdes) {
	    try {
		// parse all without Phase or Iteration
		tmp = this.parseType(tmp, bde, false, false);
	    } catch (IllegalArgumentException e) {
		logger
			.warn("### parseActivity ### Unknown element found during parsing ");
	    }
	}
	_act.setBreakdownElements(this.activityService
		.getAllBreakdownElements(_act));
	_act.setPredecessors(this.workBreakdownElementService
		.getPredecessors(_act));
	_act
		.setSuccessors(this.workBreakdownElementService
			.getSuccessors(_act));
	_act.setSuperActivities(this.breakdownElementService
		.getSuperActivities(_act));
	_act.setGuidances(this.activityService.getAllGuidances(_act));

	// clone dependencies getting
	_act.addAllBreakdownElements(clone.getBreakdownElements());
	_act.addAllSuperActivities(clone.getSuperActivities());
	_act.setGuidances(clone.getGuidances());

	this.activityService.saveActivity(_act);
	// System.out.println("###Activity sauve");
	_act.addAllPredecessors(clone.getPredecessors());
	_act.addAllSuccessors(clone.getSuccessors());
	return tmp;
    }

    /**
     * 
     * Method to parse a RoleDescriptor
     * 
     * @param _rd
     */
    private List<Guidance> parseRoleDescriptor(RoleDescriptor _rd,
	    List<Guidance> guid) {

	RoleDescriptor clone = null;

	List<Guidance> tmp = new ArrayList<Guidance>();
	tmp.addAll(guid);

	try {
	    clone = _rd.clone();
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}

	RoleDefinition rdef = _rd.getRoleDefinition();

	_rd.getAdditionalTasks().clear();
	_rd.getPrimaryTasks().clear();
	_rd.getSuperActivities().clear();
	_rd.getResponsibleFor().clear();
	_rd.setRoleDefinition(null);

	this.roleDescriptorDao.saveOrUpdateRoleDescriptor(_rd);
	// System.out.println("###RoleDescriptor vide sauve");
	if (rdef != null) {
	    tmp = this.parseRoleDefinition(rdef, tmp);
	}
	_rd.addAllPrimaryTasks(clone.getPrimaryTasks());
	_rd.addAllResponsibleFor(clone.getResponsibleFor());
	_rd.addAllSuperActivities(clone.getSuperActivities());
	_rd.setRoleDefinition(clone.getRoleDefinition());

	this.roleDescriptorDao.saveOrUpdateRoleDescriptor(_rd);
	// System.out.println("###RoleDescriptor sauve");
	return tmp;
    }

    /**
     * 
     * Method to parse a RoleDefinition
     * 
     * @param _rdef
     */
    private List<Guidance> parseRoleDefinition(RoleDefinition _rdef,
	    List<Guidance> guid) {

	RoleDefinition clone = null;

	List<Guidance> tmp = new ArrayList<Guidance>();
	tmp.addAll(guid);

	try {
	    clone = _rdef.clone();
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}

	Set<Guidance> guidances = new HashSet<Guidance>();
	guidances.addAll(_rdef.getGuidances());

	_rdef.getRoleDescriptors().clear();
	_rdef.getGuidances().clear();

	this.roleDefinitionService.getRoleDefinitionDao()
		.saveOrUpdateRoleDefinition(_rdef);
	// System.out.println("###RoleDefinition vide sauve");

	for (Guidance g : guidances) {
	    if (!tmp.contains(g)) {
		tmp.add(g);
	    }
	}
	_rdef.setGuidances(this.roleDefinitionService.getGuidances(_rdef));
	_rdef.addAllGuidances(clone.getGuidances());
	this.roleDefinitionService.getRoleDefinitionDao()
		.saveOrUpdateRoleDefinition(_rdef);
	// System.out.println("###RoleDefinition sauve");
	return tmp;
    }

    /**
     * 
     * Method to parse a TaskDescriptor
     * 
     * @param _td
     */
    private List<Guidance> parseTaskDescriptor(TaskDescriptor _td,
	    List<Guidance> guid) {

	TaskDescriptor clone = null;

	List<Guidance> tmp = new ArrayList<Guidance>();
	tmp.addAll(guid);

	try {
	    clone = _td.clone();
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}

	TaskDefinition tdef = _td.getTaskDefinition();

	_td.getAdditionalRoles().clear();
	_td.getOutputWorkProductDescriptors().clear();
	_td.getOptionalInputWorkProductDescriptors().clear();
	_td.getMandatoryInputWorkProductDescriptors().clear();
	_td.getPredecessors().clear();
	_td.getSuccessors().clear();
	_td.getSuperActivities().clear();
	_td.setMainRole(null);
	_td.setTaskDefinition(null);

	this.taskDescriptorService.getTaskDescriptorDao()
		.saveOrUpdateTaskDescriptor(_td);
	// System.out.println("###TaskDescriptor vide sauve");
	if (tdef != null) {
	    tmp = this.parseTaskDefinition(tdef, tmp);
	}
	// To decomment when you want to manage additionalRoles
	_td.setAdditionalRoles(this.taskDescriptorService
		.getAdditionalRoles(_td));
	_td.setOutputWorkProductDescriptors(this.taskDescriptorService
		.getOutputWorkProducts(_td));
	_td.setOptionalInputWorkProductDescriptors(this.taskDescriptorService
		.getOptionalInputWorkProducts(_td));
	_td.setMandatoryInputWorkProductDescriptors(this.taskDescriptorService
		.getMandatoryInputWorkProducts(_td));
	_td.setPredecessors(this.workBreakdownElementService
		.getPredecessors(_td));
	_td.setSuccessors(this.workBreakdownElementService.getSuccessors(_td));
	_td.setSuperActivities(this.breakdownElementService
		.getSuperActivities(_td));

	// clone dependencies getting
	_td.addAllAdditionalRoles(clone.getAdditionalRoles());
	_td.addAllOutputWorkProductDescriptors(clone
		.getOutputWorkProductDescriptors());
	_td.addAllOptionalInputWorkProductDescriptors(clone
		.getOptionalInputWorkProductDescriptors());
	_td.addAllMandatoryInputWorkProductDescriptors(clone
		.getMandatoryInputWorkProductDescriptors());
	_td.addAllSuperActivities(clone.getSuperActivities());
	_td.setMainRole(clone.getMainRole());
	_td.setTaskDefinition(clone.getTaskDefinition());

	this.taskDescriptorService.getTaskDescriptorDao()
		.saveOrUpdateTaskDescriptor(_td);
	// System.out.println("###TaskDescriptor sauve");
	_td.addAllPredecessors(clone.getPredecessors());
	_td.addAllSuccessors(clone.getSuccessors());
	return tmp;
    }

    /**
     * 
     * Method to parse a Milestone
     * 
     * @param _td
     */
    private List<Guidance> parseMilestone(Milestone _mi, List<Guidance> guid) {

	Milestone clone = null;

	List<Guidance> tmp = new ArrayList<Guidance>();
	tmp.addAll(guid);

	try {
	    clone = _mi.clone();
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}

	_mi.getPredecessors().clear();
	_mi.getSuccessors().clear();
	_mi.getSuperActivities().clear();

	this.milestoneService.getMilestoneDao().saveOrUpdateMilestone(_mi);
	// System.out.println("###Milestone vide sauve");

	_mi.setPredecessors(this.workBreakdownElementService
		.getPredecessors(_mi));
	_mi.setSuccessors(this.workBreakdownElementService.getSuccessors(_mi));
	_mi.setSuperActivities(this.breakdownElementService
		.getSuperActivities(_mi));

	// clone dependencies getting
	_mi.addAllSuperActivities(clone.getSuperActivities());

	this.milestoneService.getMilestoneDao().saveOrUpdateMilestone(_mi);
	// System.out.println("###Milestone sauve");

	_mi.addAllPredecessors(clone.getPredecessors());
	_mi.addAllSuccessors(clone.getSuccessors());

	return tmp;
    }

    /**
     * 
     * @param _tdef
     */
    private List<Guidance> parseTaskDefinition(TaskDefinition _tdef,
	    List<Guidance> guid) {

	TaskDefinition clone = null;

	List<Guidance> tmp = new ArrayList<Guidance>();
	tmp.addAll(guid);

	try {
	    clone = _tdef.clone();
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}

	Set<Guidance> guidances = new HashSet<Guidance>();
	guidances.addAll(_tdef.getGuidances());

	List<Step> steps = new ArrayList<Step>();
	// recuperation des breakdownelements du processus
	steps.addAll(_tdef.getSteps());

	_tdef.getSteps().clear();
	_tdef.getTaskDescriptors().clear();
	_tdef.getGuidances().clear();

	this.taskDefinitionService.getTaskDefinitionDao()
		.saveOrUpdateTaskDefinition(_tdef);
	// System.out.println("###TaskDefinition vide sauve");

	for (Guidance g : guidances) {
	    if (!tmp.contains(g)) {
		tmp.add(g);
	    }
	}

	for (Step step : steps) {
	    this.parseStep(step);
	}

	_tdef.setSteps(this.taskDefinitionService.getSteps(_tdef));
	// _tdef.setTaskDescriptors(this.roleDefinitionService.getRoleDescriptors(_rdef));
	_tdef.setGuidances(this.taskDefinitionService.getGuidances(_tdef));

	_tdef.addAllSteps(clone.getSteps());
	// _tdef.addAllTaskDesciptors(clone.getTaskDescriptors());
	_tdef.addAllGuidances(clone.getGuidances());

	this.taskDefinitionService.getTaskDefinitionDao()
		.saveOrUpdateTaskDefinition(_tdef);
	// System.out.println("###TaskDefinition sauve");

	return tmp;
    }

    /**
     * 
     * Method to parse a Step
     * 
     * @param _step
     */
    private void parseStep(Step _step) {
	this.stepDao.saveOrUpdateStep(_step);
	// System.out.println("###Step sauve");
    }

    /**
     * 
     * Method to parse a Guidance
     * 
     * @param _step
     */
    private void parseGuidance(Guidance _guidance) {
	if (_guidance instanceof CheckList) {
	    CheckList cl = (CheckList) _guidance;
	    this.parseCheckList(cl);
	} else {
	    this.guidanceDao.saveOrUpdateGuidance(_guidance);
	    // System.out.println("###Guidance sauve");
	}
    }

    /**
     * Method to parse a CheckList
     * 
     * @param _checkList
     */
    private void parseCheckList(CheckList _checkList) {

	CheckList clone = null;

	try {
	    clone = _checkList.clone();
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}

	List<Section> sections = new ArrayList<Section>();
	sections.addAll(_checkList.getSections());

	_checkList.getSections().clear();

	this.checkListService.saveCheckList(_checkList);
	// System.out.println("###CheckList vide sauvee");

	for (Section section : sections) {
	    this.parseSection(section);
	}

	_checkList.setSections(this.checkListService.getSections(_checkList));

	_checkList.addAllSections(clone.getSections());

	this.checkListService.saveCheckList(_checkList);
	// System.out.println("###CheckList sauvee");
    }

    /**
     * Method to parse a section
     * 
     * @param _section
     */
    private void parseSection(Section _section) {
	this.sectionDao.saveOrUpdateSection(_section);
	// System.out.println("###Section sauvee");
    }

    /**
     * @param _wpd
     * @param _guid
     * @return
     */
    private List<Guidance> parseWorkProductDescriptor(
	    WorkProductDescriptor _wpd, List<Guidance> _guid) {

	WorkProductDescriptor clone = null;

	List<Guidance> tmp = new ArrayList<Guidance>();
	tmp.addAll(_guid);

	try {
	    clone = _wpd.clone();
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}

	WorkProductDefinition wpdef = _wpd.getWorkProductDefinition();

	_wpd.getProducerTasks().clear();
	_wpd.getOptionalUserTasks().clear();
	_wpd.getMandatoryUserTasks().clear();

	_wpd.getSuperActivities().clear();
	_wpd.setResponsibleRoleDescriptor(null);
	_wpd.setWorkProductDefinition(null);

	this.workProductDescriptorDao.saveOrUpdateWorkProductDescriptor(_wpd);
	// System.out.println("###WorkProductDescriptor vide sauve");

	if (wpdef != null) {
	    tmp = this.parseWorkProductDefinition(wpdef, tmp);
	}
	_wpd.addAllProducerTasks(clone.getProducerTasks());
	_wpd.addAllOptionalUserTasks(clone.getOptionalUserTasks());
	_wpd.addAllMandatoryUserTasks(clone.getMandatoryUserTasks());

	_wpd.addAllSuperActivities(clone.getSuperActivities());
	_wpd.setResponsibleRoleDescriptor(clone.getResponsibleRoleDescriptor());
	_wpd.setWorkProductDefinition(clone.getWorkProductDefinition());

	this.workProductDescriptorDao.saveOrUpdateWorkProductDescriptor(_wpd);
	// System.out.println("###WorkProductDescriptor sauve");

	return tmp;
    }

    /**
     * @param _wpdef
     * @param _tmp
     * @return
     */
    private List<Guidance> parseWorkProductDefinition(
	    WorkProductDefinition _wpdef, List<Guidance> _tmp) {

	WorkProductDefinition clone = null;

	List<Guidance> tmp = new ArrayList<Guidance>();
	tmp.addAll(_tmp);

	try {
	    clone = _wpdef.clone();
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}

	Set<Guidance> guidances = new HashSet<Guidance>();
	guidances.addAll(_wpdef.getGuidances());

	_wpdef.getWorkProductDescriptors().clear();
	_wpdef.getGuidances().clear();

	this.workProductDefinitionService.getWorkProductDefinitionDao()
		.saveOrUpdateWorkProductDefinition(_wpdef);
	// System.out.println("###WorkProductDefinition vide sauve");

	for (Guidance g : guidances) {
	    if (!tmp.contains(g)) {
		tmp.add(g);
	    }
	}
	_wpdef.setGuidances(this.workProductDefinitionService
		.getGuidances(_wpdef));
	_wpdef.addAllGuidances(clone.getGuidances());

	this.workProductDefinitionService.getWorkProductDefinitionDao()
		.saveOrUpdateWorkProductDefinition(_wpdef);
	
	return tmp;
    }

    /**
     * Return processes list
     * 
     * @return
     */
    public List<Process> getAllProcesses() {
	return this.processDao.getAllProcesses();
    }

    /**
     * 
     * @param _guid
     * @return
     */
    public Process getProcessFromGuid(String _guid) {
	return this.processDao.getProcessFromGuid(_guid);
    }

    /**
     * 
     * @param _id
     * @return
     */
    public Process getProcess(String _id) {
	return this.processDao.getProcess(_id);
    }

    /**
     * Project instanciation
     * 
     * @param _project
     * @param _process
     * @param list
     */
    public void projectInstanciation(Project _project, Process _process,
	    List<HashMap<String, Object>> list) {

	// elements of collection getting
	Set<BreakdownElement> forInstanciation = this.activityService
		.getAllBreakdownElements(_process);

	int dispOrd = 0;
	for (BreakdownElement bde : forInstanciation) {
	    dispOrd++;

	    if (bde instanceof Phase) {
		Phase ph = (Phase) bde;
		int occ = this.giveNbOccurences(ph.getId(), list, false);
		this.phaseService.phaseInstanciation(_project, ph, _project,
			list, occ, false, dispOrd);
	    } else if (bde instanceof Iteration) {
		Iteration it = (Iteration) bde;
		int occ = this.giveNbOccurences(it.getId(), list, false);
		this.iterationService.iterationInstanciation(_project, it,
			_project, list, occ, false, dispOrd);
	    } else if (bde instanceof Activity) {
		Activity act = (Activity) bde;
		int occ = this.giveNbOccurences(act.getId(), list, false);
		this.activityService.activityInstanciation(_project, act,
			_project, list, occ, false, dispOrd);
	    } else if (bde instanceof TaskDescriptor) {
		TaskDescriptor td = (TaskDescriptor) bde;
		int occ = this.giveNbOccurences(td.getId(), list, false);
		this.taskDescriptorService.taskDescriptorInstanciation(
			_project, td, _project, occ, false, dispOrd);
	    } else if (bde instanceof Milestone) {
		Milestone mi = (Milestone) bde;
		int occ = this.giveNbOccurences(mi.getId(), list, false);
		this.milestoneService.milestoneInstanciation(_project, mi,
			_project, occ, dispOrd);
	    } else {
		logger
			.warn("### projectInstanciation ### Unknown element found during instanciation ");
	    }
	}

	_project.setProcess(_process);

	this.projectDao.saveOrUpdateProject(_project);

    }

    /**
     * 
     * @param _project
     * @param _process
     * @param list
     */
    public void projectUpdate(Project _project, Process _process,
	    List<HashMap<String, Object>> list) {

	// elements of collection getting
	Set<BreakdownElement> forInstanciation = this.activityService
		.getAllBreakdownElements(_process);

	this.projectDao.getSessionFactory().getCurrentSession().saveOrUpdate(
		_project);

	for (BreakdownElement bde : forInstanciation) {
	    if (bde instanceof Phase) {
		Phase ph = (Phase) bde;
		Set<ConcreteActivity> cacts = new HashSet<ConcreteActivity>();
		cacts.add(_project);
		int occ = this.giveNbOccurences(ph.getId(), list, true);
		this.phaseService.phaseUpdate(_project, ph, cacts, list, occ);
	    } else if (bde instanceof Iteration) {
		Iteration it = (Iteration) bde;
		Set<ConcreteActivity> cacts = new HashSet<ConcreteActivity>();
		cacts.add(_project);
		int occ = this.giveNbOccurences(it.getId(), list, true);
		this.iterationService.iterationUpdate(_project, it, cacts,
			list, occ);
	    } else if (bde instanceof Activity) {
		Activity act = (Activity) bde;
		Set<ConcreteActivity> cacts = new HashSet<ConcreteActivity>();
		cacts.add(_project);
		int occ = this.giveNbOccurences(act.getId(), list, true);
		this.activityService.activityUpdate(_project, act, cacts, list,
			occ);

	    } else if (bde instanceof TaskDescriptor) {
		System.out.println("TaskDescriptor");
		TaskDescriptor td = (TaskDescriptor) bde;
		Set<ConcreteActivity> cacts = new HashSet<ConcreteActivity>();
		cacts.add(_project);
		int occ = this.giveNbOccurences(td.getId(), list, true);
		this.taskDescriptorService.taskDescriptorUpdate(_project, td,
			cacts, occ);

	    }

	}

	_project.setProcess(_process);

	this.projectDao.saveOrUpdateProject(_project);

    }

    /**
     * Instanciate all the dependencies defined in the process for the given
     * project
     * 
     * @param _project
     * @param _process
     */
    public void projectDependenciesInstanciation(Project _project,
	    Process _process) {
	for (ConcreteWorkBreakdownElement cwbde_pred : this.concreteWorkBreakdownElementService
		.getAllConcreteWorkBreakdownElementsWithAtLeastOneSuccessor(_project)) {
	    WorkBreakdownElement wbde_pred = cwbde_pred
		    .getWorkBreakdownElement();
	    for (WorkOrder wo : wbde_pred.getSuccessors()) {
		WorkBreakdownElement wbde_succ = wo.getSuccessor();
		this.workBreakdownElementDao.getHibernateTemplate()
			.getSessionFactory().getCurrentSession().saveOrUpdate(
				wbde_succ);
		this.workBreakdownElementDao.getHibernateTemplate()
			.getSessionFactory().getCurrentSession().refresh(
				wbde_succ);
		for (ConcreteWorkBreakdownElement cwbde_succ : wbde_succ
			.getConcreteWorkBreakdownElements()) {
		    // predecessor and successor must have
		    // the same parent
		    // activity (superActivity)
		    if (cwbde_pred.getSuperConcreteActivity().getId().equals(
			    cwbde_succ.getSuperConcreteActivity().getId())) {
			// predecessor and successor
			// must have the same parent
			// project
			if (_project.getId().equals(
				cwbde_succ.getProject().getId())) {
			    // the deduced workorder
			    // must not exist
			    if (!this.concreteWorkOrderService
				    .existsConcreteWorkOrder(
					    cwbde_pred.getId(), cwbde_succ
						    .getId(), wo.getLinkType())) {
				this.concreteWorkOrderService
					.saveConcreteWorkOrder(cwbde_pred
						.getId(), cwbde_succ.getId(),
						wo.getLinkType(), _project
							.getId());
			    }
			}
		    }
		}
	    }
	}
    }

    /**
     * 
     * @param _id
     * @param list
     * @param _isInstanciated
     * @return
     */
    private int giveNbOccurences(String _id,
	    List<HashMap<String, Object>> list, boolean _isInstanciated) {

	int nb = 0;
	if (!_isInstanciated)
	    nb = 1;

	for (HashMap<String, Object> hashMap : list) {
	    if (((String) hashMap.get("id")).equals(_id)) {

		nb = ((Integer) hashMap.get("nbOccurences")).intValue();
		break;
	    }
	}
	return nb;
    }

    /**
     * @param _process
     * @return
     */
    public List<RoleDescriptor> getRoleDescriptorsFromProcess(Activity _act) {

	List<RoleDescriptor> tmp = new ArrayList<RoleDescriptor>();
	this.activityService.getActivityDao().getSessionFactory()
		.getCurrentSession().saveOrUpdate(_act);

	for (Object obj : _act.getBreakdownElements()) {
	    if (obj instanceof RoleDescriptor) {
		RoleDescriptor rd = (RoleDescriptor) obj;
		tmp.add(rd);
	    } else if (!(obj instanceof TaskDescriptor)
		    && !(obj instanceof WorkProductDescriptor)
		    && !(obj instanceof Milestone)) {
		Activity act = (Activity) obj;
		// check if roleDescriptor with the same name is
		// already present
		// in the list
		for (RoleDescriptor roleDescriptorToAdd : this
			.getRoleDescriptorsFromProcess(act)) {
		    boolean present = false;
		    for (RoleDescriptor presentRoleDescriptor : tmp) {
			if (roleDescriptorToAdd.getPresentationName().equals(
				presentRoleDescriptor.getPresentationName())) {
			    present = true;
			    break;
			}
		    }
		    if (!present)
			tmp.add(roleDescriptorToAdd);
		}
	    }
	}

	return tmp;
    }

    /**
     * @param _process
     * @param _presentationName
     * @return
     */
    public int getRoleDescriptorsWithTheSameNameNumberInProcess(Activity _act,
	    String _presentationName) {
	int nb = 0;
	this.activityService.getActivityDao().getSessionFactory()
		.getCurrentSession().saveOrUpdate(_act);
	for (Object obj : _act.getBreakdownElements()) {
	    if (obj instanceof RoleDescriptor) {
		RoleDescriptor rd = (RoleDescriptor) obj;
		if (rd.getPresentationName().equals(_presentationName))
		    nb++;
	    } else if (!(obj instanceof TaskDescriptor)
		    && !(obj instanceof WorkProductDescriptor)
		    && !(obj instanceof Milestone)) {
		Activity act = (Activity) obj;
		// check if roleDescriptor with the same name is
		// already present
		// in the list
		nb += this.getRoleDescriptorsWithTheSameNameNumberInProcess(
			act, _presentationName);
	    }
	}
	return nb;
    }

    /**
     * @param _process
     * @param _rdName
     * @return
     */
    public List<RoleDescriptor> getRoleDescriptorsWithTheSameNameInProcess(
	    Activity _act, String _rdName) {
	List<RoleDescriptor> rds = new ArrayList<RoleDescriptor>();
	this.activityService.getActivityDao().getSessionFactory()
		.getCurrentSession().saveOrUpdate(_act);
	this.activityService.getActivityDao().getSessionFactory()
		.getCurrentSession().refresh(_act);
	for (Object obj : _act.getBreakdownElements()) {
	    if (obj instanceof RoleDescriptor) {
		RoleDescriptor rd = (RoleDescriptor) obj;
		if (rd.getPresentationName().equals(_rdName))
		    rds.add(rd);
	    } else if (!(obj instanceof TaskDescriptor)
		    && !(obj instanceof WorkProductDescriptor)
		    && !(obj instanceof Milestone)) {
		Activity act = (Activity) obj;
		// check if roleDescriptor with same name is
		// already present in
		// the list
		rds.addAll(this.getRoleDescriptorsWithTheSameNameInProcess(act,
			_rdName));
	    }
	}
	return rds;
    }

    /**
     * Getter of processDao.
     * 
     * @return the processDao.
     */
    public ProcessDao getProcessDao() {
	return processDao;
    }

    /**
     * Setter of processDao.
     * 
     * @param _processDao
     *                The processDao to set.
     */
    public void setProcessDao(ProcessDao _processDao) {
	this.processDao = _processDao;
    }

    /**
     * Getter of elementDao.
     * 
     * @return the elementDao.
     */
    public ElementDao getElementDao() {
	return elementDao;
    }

    /**
     * Setter of elementDao.
     * 
     * @param elementDao
     *                The elementDao to set.
     */
    public void setElementDao(ElementDao elementDao) {
	this.elementDao = elementDao;
    }

    /**
     * Getter of stepDao.
     * 
     * @return the stepDao.
     */
    public StepDao getStepDao() {
	return stepDao;
    }

    /**
     * Setter of stepDao.
     * 
     * @param stepDao
     *                The stepDao to set.
     */
    public void setStepDao(StepDao stepDao) {
	this.stepDao = stepDao;
    }

    public BreakdownElementService getBreakdownElementService() {
	return this.breakdownElementService;
    }

    public void setBreakdownElementService(
	    BreakdownElementService _breakdownElementService) {
	this.breakdownElementService = _breakdownElementService;
    }

    public PhaseService getPhaseService() {
	return this.phaseService;
    }

    public void setPhaseService(PhaseService _phaseService) {
	this.phaseService = _phaseService;
    }

    public ActivityService getActivityService() {
	return this.activityService;
    }

    public void setActivityService(ActivityService _activityService) {
	this.activityService = _activityService;
    }

    public IterationService getIterationService() {
	return this.iterationService;
    }

    public void setIterationService(IterationService _iterationService) {
	this.iterationService = _iterationService;
    }

    public TaskDescriptorService getTaskDescriptorService() {
	return this.taskDescriptorService;
    }

    public void setTaskDescriptorService(
	    TaskDescriptorService _taskDescriptorService) {
	this.taskDescriptorService = _taskDescriptorService;
    }

    public ProjectDao getProjectDao() {
	return projectDao;
    }

    public void setProjectDao(ProjectDao projectDao) {
	this.projectDao = projectDao;
    }

    public GuidanceDao getGuidanceDao() {
	return guidanceDao;
    }

    public void setGuidanceDao(GuidanceDao guidanceDao) {
	this.guidanceDao = guidanceDao;
    }

    /**
     * @return the concreteBreakdownElementService
     */
    public ConcreteBreakdownElementService getConcreteBreakdownElementService() {
	return concreteBreakdownElementService;
    }

    /**
     * Setter of concreteBreakdownElementService.
     * 
     * @param concreteBreakdownElementService
     *                The concreteBreakdownElementService to set.
     */
    public void setConcreteBreakdownElementService(
	    ConcreteBreakdownElementService concreteBreakdownElementService) {
	this.concreteBreakdownElementService = concreteBreakdownElementService;
    }

    /**
     * @return the workBreakdownElementService
     */
    public WorkBreakdownElementService getWorkBreakdownElementService() {
	return this.workBreakdownElementService;
    }

    /**
     * @param _workBreakdownElementService
     *                the workBreakdownElementService to set
     */
    public void setWorkBreakdownElementService(
	    WorkBreakdownElementService _workBreakdownElementService) {
	this.workBreakdownElementService = _workBreakdownElementService;
    }

    /**
     * @return the roleDefinitionService
     */
    public RoleDefinitionService getRoleDefinitionService() {
	return this.roleDefinitionService;
    }

    /**
     * @param _roleDefinitionService
     *                the roleDefinitionService to set
     */
    public void setRoleDefinitionService(
	    RoleDefinitionService _roleDefinitionService) {
	this.roleDefinitionService = _roleDefinitionService;
    }

    /**
     * @return the taskDefinitionService
     */
    public TaskDefinitionService getTaskDefinitionService() {
	return this.taskDefinitionService;
    }

    /**
     * @param _taskDefinitionService
     *                the taskDefinitionService to set
     */
    public void setTaskDefinitionService(
	    TaskDefinitionService _taskDefinitionService) {
	this.taskDefinitionService = _taskDefinitionService;
    }

    public ProcessManagerService getProcessManagerService() {
	return processManagerService;
    }

    public void setProcessManagerService(
	    ProcessManagerService _processManagerService) {
	this.processManagerService = _processManagerService;
    }

    public CheckListService getCheckListService() {
	return checkListService;
    }

    public void setCheckListService(CheckListService _checkListService) {
	this.checkListService = _checkListService;
    }

    public SectionDao getSectionDao() {
	return sectionDao;
    }

    public void setSectionDao(SectionDao _sectionDao) {
	this.sectionDao = _sectionDao;
    }

    public WorkBreakdownElementDao getWorkBreakdownElementDao() {
	return workBreakdownElementDao;
    }

    public void setWorkBreakdownElementDao(
	    WorkBreakdownElementDao workBreakdownElementDao) {
	this.workBreakdownElementDao = workBreakdownElementDao;
    }

    public WorkOrderDao getWorkOrderDao() {
	return workOrderDao;
    }

    public void setWorkOrderDao(WorkOrderDao _workOrderDao) {
	workOrderDao = _workOrderDao;
    }

    /**
     * @param _retrievedProcess
     * @return
     */
    public SortedSet<BreakdownElement> getAllBreakdownElements(Process _process) {
	SortedSet<BreakdownElement> tmp = new TreeSet<BreakdownElement>();
	this.processDao.getSessionFactory().getCurrentSession().saveOrUpdate(
		_process);
	for (BreakdownElement bde : _process.getBreakdownElements()) {
	    tmp.add(bde);
	}
	return tmp;
    }

    /**
     * @param _process
     */
    public void deleteProcess(Process _process) {
	this.processDao.deleteProcess(_process);
    }

    /**
     * @param _process
     */
    public String saveProcess(Process _process) {
	if (_process != null) {
	    this.processDao.saveOrUpdateProcess(_process);
	    return _process.getId();
	}
	return null;
    }

    /**
     * @return the workOrderService
     */
    public WorkOrderService getWorkOrderService() {
	return this.workOrderService;
    }

    /**
     * @param _workOrderService
     *                the workOrderService to set
     */
    public void setWorkOrderService(WorkOrderService _workOrderService) {
	this.workOrderService = _workOrderService;
    }

    /**
     * @return the roleDescriptorDao
     */
    public RoleDescriptorDao getRoleDescriptorDao() {
	return this.roleDescriptorDao;
    }

    /**
     * @param _roleDescriptorDao
     *                the roleDescriptorDao to set
     */
    public void setRoleDescriptorDao(RoleDescriptorDao _roleDescriptorDao) {
	this.roleDescriptorDao = _roleDescriptorDao;
    }

    public List<WorkProductDescriptor> getWorkProductDescriptorsFromProcess(
	    Activity _act) {
	List<WorkProductDescriptor> tmp = new ArrayList<WorkProductDescriptor>();
	this.activityService.getActivityDao().getSessionFactory()
		.getCurrentSession().saveOrUpdate(_act);
	for (Object obj : _act.getBreakdownElements()) {
	    if (obj instanceof WorkProductDescriptor) {
		WorkProductDescriptor wpd = (WorkProductDescriptor) obj;
		tmp.add(wpd);
	    } else if (!(obj instanceof TaskDescriptor)
		    && !(obj instanceof RoleDescriptor)
		    && !(obj instanceof Milestone)) {
		Activity act = (Activity) obj;
		// check if workProductDescriptor with the same
		// name is already
		// present
		// in the list
		for (WorkProductDescriptor workProductDescriptorToAdd : this
			.getWorkProductDescriptorsFromProcess(act)) {
		    boolean present = false;
		    for (WorkProductDescriptor presentWorkProductDescriptor : tmp) {
			if (workProductDescriptorToAdd.getPresentationName()
				.equals(
					presentWorkProductDescriptor
						.getPresentationName())) {
			    present = true;
			    break;
			}
		    }
		    if (!present)
			tmp.add(workProductDescriptorToAdd);
		}
	    }
	}
	return tmp;

    }

    public int getWorkProductDescriptorsWithTheSameNameNumberInProcess(
	    Activity _act, String _presentationName) {
	int nb = 0;
	this.activityService.getActivityDao().getSessionFactory()
		.getCurrentSession().saveOrUpdate(_act);
	for (Object obj : _act.getBreakdownElements()) {
	    if (obj instanceof WorkProductDescriptor) {
		WorkProductDescriptor wpd = (WorkProductDescriptor) obj;
		if (wpd.getPresentationName().equals(_presentationName))
		    nb++;
	    } else if (!(obj instanceof TaskDescriptor)
		    && !(obj instanceof RoleDescriptor)
		    && !(obj instanceof Milestone)) {
		Activity act = (Activity) obj;
		// check if workProductDescriptor with the same
		// name is already
		// present
		// in the list
		nb += this
			.getWorkProductDescriptorsWithTheSameNameNumberInProcess(
				act, _presentationName);
	    }
	}
	return nb;
    }

    /**
     * @return the workProductDescriptorDao
     */
    public WorkProductDescriptorDao getWorkProductDescriptorDao() {
	return workProductDescriptorDao;
    }

    /**
     * @param _workProductDescriptorDao
     *                the workProductDescriptorDao to set
     */
    public void setWorkProductDescriptorDao(
	    WorkProductDescriptorDao _workProductDescriptorDao) {
	workProductDescriptorDao = _workProductDescriptorDao;
    }

    /**
     * @return the workProductDefinitionService
     */
    public WorkProductDefinitionService getWorkProductDefinitionService() {
	return workProductDefinitionService;
    }

    /**
     * @param _workProductDefinitionService
     *                the workProductDefinitionService to set
     */
    public void setWorkProductDefinitionService(
	    WorkProductDefinitionService _workProductDefinitionService) {
	workProductDefinitionService = _workProductDefinitionService;
    }

    /**
     * @return the concreteWorkOrderService
     */
    public ConcreteWorkOrderService getConcreteWorkOrderService() {
	return concreteWorkOrderService;
    }

    /**
     * @param concreteWorkOrderService
     *                the concreteWorkOrderService to set
     */
    public void setConcreteWorkOrderService(
	    ConcreteWorkOrderService concreteWorkOrderService) {
	this.concreteWorkOrderService = concreteWorkOrderService;
    }

    /**
     * @return the concreteWorkBreakdownElementService
     */
    public ConcreteWorkBreakdownElementService getConcreteWorkBreakdownElementService() {
	return concreteWorkBreakdownElementService;
    }

    /**
     * @param concreteWorkBreakdownElementService
     *                the concreteWorkBreakdownElementService to set
     */
    public void setConcreteWorkBreakdownElementService(
	    ConcreteWorkBreakdownElementService concreteWorkBreakdownElementService) {
	this.concreteWorkBreakdownElementService = concreteWorkBreakdownElementService;
    }

    /**
     * @return the milestoneService
     */
    public MilestoneService getMilestoneService() {
	return milestoneService;
    }

    /**
     * @param _milestoneService
     *                the milestoneService to set
     */
    public void setMilestoneService(MilestoneService _milestoneService) {
	milestoneService = _milestoneService;
    }

    /**
     * @param _process
     * @return
     */
    public List<TaskDescriptor> getTaskDescriptorsFromProcess(Activity _act) {

	List<TaskDescriptor> tmp = new ArrayList<TaskDescriptor>();
	this.activityService.getActivityDao().getSessionFactory()
		.getCurrentSession().saveOrUpdate(_act);
	for (Object obj : _act.getBreakdownElements()) {

	    if (obj instanceof TaskDescriptor) {
		TaskDescriptor rd = (TaskDescriptor) obj;
		tmp.add(rd);

	    } else if (!(obj instanceof RoleDescriptor)
		    && !(obj instanceof WorkProductDescriptor)) {

		Activity act = (Activity) obj;
		// check if taskDescriptor with the same name is
		// already present
		// in the list
		for (TaskDescriptor taskDescriptorToAdd : this
			.getTaskDescriptorsFromProcess(act)) {
		    boolean present = false;
		    for (TaskDescriptor presentTaskDescriptor : tmp) {
			if (taskDescriptorToAdd.getPresentationName().equals(
				presentTaskDescriptor.getPresentationName())) {
			    present = true;
			    break;
			}
		    }
		    if (!present)
			tmp.add(taskDescriptorToAdd);
		}
	    }
	}
	return tmp;
    }
}
