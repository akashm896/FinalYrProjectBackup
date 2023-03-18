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

package wilos.business.services.spem2.process;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.business.services.spem2.process.ProcessService.Classe;
import wilos.business.services.spem2.workbreakdownelement.WorkOrderService;
import wilos.hibernate.misc.project.ProjectDao;
import wilos.hibernate.spem2.activity.ActivityDao;
import wilos.hibernate.spem2.breakdownelement.BreakdownElementDao;
import wilos.hibernate.spem2.checklist.CheckListDao;
import wilos.hibernate.spem2.element.ElementDao;
import wilos.hibernate.spem2.guide.GuidanceDao;
import wilos.hibernate.spem2.iteration.IterationDao;
import wilos.hibernate.spem2.milestone.MilestoneDao;
import wilos.hibernate.spem2.phase.PhaseDao;
import wilos.hibernate.spem2.process.ProcessDao;
import wilos.hibernate.spem2.role.RoleDefinitionDao;
import wilos.hibernate.spem2.role.RoleDescriptorDao;
import wilos.hibernate.spem2.section.SectionDao;
import wilos.hibernate.spem2.task.StepDao;
import wilos.hibernate.spem2.task.TaskDefinitionDao;
import wilos.hibernate.spem2.task.TaskDescriptorDao;
import wilos.hibernate.spem2.workbreakdownelement.WorkBreakdownElementDao;
import wilos.hibernate.spem2.workproduct.WorkProductDefinitionDao;
import wilos.hibernate.spem2.workproduct.WorkProductDescriptorDao;
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
import wilos.model.spem2.workproduct.WorkProductDefinition;
import wilos.model.spem2.workproduct.WorkProductDescriptor;

/**
 * ProcessManagementService is a transactional class, that handle removing of
 * processes, requested by web pages
 * 
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class ProcessManagementService {

	private ActivityDao activityDao;

	private PhaseDao phaseDao;

	private IterationDao iterationDao;

	private BreakdownElementDao breakdownElementDao;

	private ElementDao elementDao;

	private ProcessDao processDao;

	private RoleDefinitionDao roleDefinitionDao;

	private RoleDescriptorDao roleDescriptorDao;

	private WorkProductDefinitionDao workProductDefinitionDao;

	private WorkProductDescriptorDao workProductDescriptorDao;

	private StepDao stepDao;

	private TaskDefinitionDao taskDefinitionDao;

	private TaskDescriptorDao taskDescriptorDao;

	private WorkBreakdownElementDao workBreakdownElementDao;

	private ProjectDao projectDao;

	private GuidanceDao guidanceDao;

	private CheckListDao checkListDao;

	private SectionDao sectionDao;

	private MilestoneDao milestoneDao;

	private WorkOrderService workOrderService;

	private Set<Object> objectsToRemoveLast = new HashSet<Object>();

	private Set<WorkOrder> workOrderToRemove = new HashSet<WorkOrder>();

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * 
	 * Method to remove a process that has not been instanciated yet
	 * 
	 * @param _process
	 *            a process to remove
	 */
	public void removeProcess(String _processId) {
		if (!_processId.equals("")) {
			this.objectsToRemoveLast.clear();
			// loads process from database
			Process p = this.processDao.getProcess(_processId);
			// retrieve subelements of process
			List<BreakdownElement> bdes = new ArrayList<BreakdownElement>();
			bdes.addAll(p.getBreakdownElements());
			// retrieve guidances of the process
			Set<Guidance> guid = new HashSet<Guidance>();
			// depending on element type, calls specific method to remove current
			// element
			for (BreakdownElement bde : bdes) {
				guid = this.deleteType(guid, bde);
			}
			// suppress all guidance objects
			for (Guidance g : guid) {
				this.removeGuidance(g);
			}
			// remove workOrders
			this.removeWorkOrders();
			// deletion of objects that can't be removed directly because of
			// dependencies
			for (Object obj : this.objectsToRemoveLast) {
				if (obj instanceof Activity)
					this.activityDao.deleteActivity((Activity) obj);
				if (obj instanceof Phase)
					this.phaseDao.deletePhase((Phase) obj);
				if (obj instanceof Iteration)
					this.iterationDao.deleteIteration((Iteration) obj);
				if (obj instanceof RoleDescriptor)
					this.roleDescriptorDao
							.deleteRoleDescriptor((RoleDescriptor) obj);
				if (obj instanceof TaskDescriptor)
					this.taskDescriptorDao
							.deleteTaskDescriptor((TaskDescriptor) obj);
				if (obj instanceof WorkProductDescriptor)
					this.workProductDescriptorDao
							.deleteWorkProductDescriptor((WorkProductDescriptor) obj);
				if (obj instanceof Step)
					this.stepDao.deleteStep((Step) obj);
				if (obj instanceof Milestone)
					this.milestoneDao.deleteMilestone((Milestone) obj);
			}
			for (Object def : this.objectsToRemoveLast) {
				if (def instanceof RoleDefinition)
					this.roleDefinitionDao
							.deleteRoleDefinition((RoleDefinition) def);
				if (def instanceof TaskDefinition)
					this.taskDefinitionDao
							.deleteTaskDefinition((TaskDefinition) def);
				if (def instanceof WorkProductDefinition)
					this.workProductDefinitionDao
							.deleteWorkProductDefinition((WorkProductDefinition) def);
			}
			// delete the process
			this.processDao.deleteProcess(p);
		}
		else
			logger.error("ProcessManagementService.removeProcess(String) : _processId is null");
	}

	private Set<Guidance> deleteType(Set<Guidance> lstGuid, BreakdownElement bde)
			throws IllegalArgumentException {
		String type = bde.getClass().getSimpleName();
		// java.lang.IllegalArgumentException possible on Classe.ValueOf
		Classe typeBde = Classe.valueOf(type);
		switch (typeBde) {
		case Phase:
			// only for removeProcess
			Phase ph = (Phase) bde;
			lstGuid = this.removePhase(ph, lstGuid);
			break;
		case Iteration:
			Iteration it = (Iteration) bde;
			lstGuid = this.removeIteration(it, lstGuid);
			break;
		case Activity:
			Activity act = (Activity) bde;
			lstGuid = this.removeActivity(act, lstGuid);
			break;
		case RoleDescriptor:
			RoleDescriptor rd = (RoleDescriptor) bde;
			lstGuid = this.removeRoleDescriptor(rd, lstGuid);
			break;
		case WorkProductDescriptor:
			WorkProductDescriptor wpd = (WorkProductDescriptor) bde;
			lstGuid = this.removeWorkProductDescriptor(wpd, lstGuid);
			break;
		case TaskDescriptor:
			TaskDescriptor td = (TaskDescriptor) bde;
			lstGuid = this.removeTaskDescriptor(td, lstGuid);
			break;
		case Milestone:
			Milestone mi = (Milestone) bde;
			lstGuid = this.removeMilestone(mi, lstGuid);
		}
		return lstGuid;
	}

	/**
	 * @param _wpd
	 * @param _guid
	 * @return
	 */
	private Set<Guidance> removeWorkProductDescriptor(
			WorkProductDescriptor _wpd, Set<Guidance> _guid) {
		// loads workProductDescriptor from database
		_wpd = this.workProductDescriptorDao.getWorkProductDescriptor(_wpd
				.getId());

		// temporary list of guidances to delete
		Set<Guidance> tmp = new HashSet<Guidance>();
		tmp.addAll(_guid);

		WorkProductDefinition wpdef = _wpd.getWorkProductDefinition();

		// removes workProductDefinition related to current
		// WorkProductDescriptor
		if (wpdef != null) {
			tmp = this.removeWorkProductDefinition(wpdef, tmp);
		}

		// preparing to delete current WorkProductDescriptor
		this.objectsToRemoveLast.add(_wpd);
		return tmp;
	}

	/**
	 * @param _wpdef
	 * @param _tmp
	 * @return
	 */
	private Set<Guidance> removeWorkProductDefinition(
			WorkProductDefinition _wpdef, Set<Guidance> _tmp) {
		// loads workProductDefinition from database
		_wpdef = this.workProductDefinitionDao.getWorkProductDefinition(_wpdef
				.getId());
		Set<Guidance> tmp = new HashSet<Guidance>();
		tmp.addAll(_tmp);

		// temporary list of guidances to delete
		tmp.addAll(_wpdef.getGuidances());

		// preparing to delete current WorkProductDefinition
		this.objectsToRemoveLast.add(_wpdef);

		return tmp;
	}

	/**
	 * Remove all workOrders object of the process *
	 */
	private void removeWorkOrders() {
		for (WorkOrder wo : this.workOrderToRemove) {
			if (wo.getPredecessor() != null) {
				TaskDescriptor prec = this.taskDescriptorDao
						.getTaskDescriptor(wo.getPredecessor().getId());
				wo.setPredecessor(null);
				this.taskDescriptorDao.saveOrUpdateTaskDescriptor(prec);
			}
			if (wo.getSuccessor() != null) {
				TaskDescriptor succ = this.taskDescriptorDao
						.getTaskDescriptor(wo.getSuccessor().getId());
				wo.setSuccessor(null);
				this.taskDescriptorDao.saveOrUpdateTaskDescriptor(succ);
			}
			this.workOrderService.deleteWorkOrder(wo);
		}
	}

	/**
	 * 
	 * Method to remove a phase from a process
	 * 
	 * @param _phase
	 *            a phase of the current process
	 * @param _guid
	 *            global guidances to remove from the database
	 * 
	 * @return global guidance updated with guidances of the current phase
	 */
	private Set<Guidance> removePhase(Phase _phase, Set<Guidance> _guid) {

		// loads phase from database
		_phase = this.phaseDao.getPhase(_phase.getId());

		// retrieve guidances of the phase
		Set<Guidance> tmp = new HashSet<Guidance>();
		tmp.addAll(_guid);

		// retrieve subelements of phase
		List<BreakdownElement> bdes = new ArrayList<BreakdownElement>();
		bdes.addAll(_phase.getBreakdownElements());

		// temporary list of guidances to delete
		tmp.addAll(_phase.getGuidances());

		// depending on element type, calls specific method to remove current element
		for (BreakdownElement bde : bdes) {
			tmp = deleteType(tmp, bde);
		}

		this.findWorkOrders(_phase);
		this.objectsToRemoveLast.add(_phase);
		return tmp;
	}

	/**
	 * 
	 * Method to remove an iteration from a process
	 * 
	 * @param _phase
	 *            an iteration of the current process
	 * @param _guid
	 *            global guidances to remove from the database
	 * 
	 * @return global guidance updated with guidances of the current iteration
	 */
	private Set<Guidance> removeIteration(Iteration _it, Set<Guidance> guid) {

		// loads iteration from database
		_it = this.iterationDao.getIteration(_it.getId());
		Set<Guidance> tmp = new HashSet<Guidance>();
		tmp.addAll(guid);

		// retrieve subelements of iteration
		List<BreakdownElement> bdes = new ArrayList<BreakdownElement>();
		bdes.addAll(_it.getBreakdownElements());

		// temporary list of guidances to delete
		tmp.addAll(_it.getGuidances());

		// depending on element type, calls specific method to remove current
		// element
		for (BreakdownElement bde : bdes) {
			tmp = deleteType(tmp, bde);
		}

		this.findWorkOrders(_it);

		this.objectsToRemoveLast.add(_it);

		return tmp;
	}

	/**
	 * 
	 * Method to remove an activity from a process
	 * 
	 * @param _it
	 *            an activity of the current process
	 * @param _guid
	 *            global guidances to remove from the database
	 * 
	 * @return Set<Guidance> global guidance updated with guidances of the
	 *         current activity
	 */
	private Set<Guidance> removeActivity(Activity _act, Set<Guidance> guid) {

		// loads activity from database
		_act = this.activityDao.getActivity(_act.getId());

		Set<Guidance> tmp = new HashSet<Guidance>();
		tmp.addAll(guid);

		// retrieve subelements of activity
		List<BreakdownElement> bdes = new ArrayList<BreakdownElement>();
		bdes.addAll(_act.getBreakdownElements());

		// temporary list of guidances to delete
		tmp.addAll(_act.getGuidances());

		// depending on element type, calls specific method to remove current
		// element
		for (BreakdownElement bde : bdes) {
			tmp = deleteType(tmp, bde);
		}

		this.findWorkOrders(_act);

		this.objectsToRemoveLast.add(_act);

		return tmp;
	}

	/**
	 * 
	 * Method to remove a roleDescriptor from a process
	 * 
	 * @param _rd
	 *            a roleRoleDescriptor of the current process
	 * @param _guid
	 *            global guidances to remove from the database
	 * 
	 * @return Set<Guidance> global guidance updated with guidances of the
	 *         current roleDescriptor
	 */
	private Set<Guidance> removeRoleDescriptor(RoleDescriptor _rd,
			Set<Guidance> guid) {

		// loads roleDescriptor from database
		_rd = this.roleDescriptorDao.getRoleDescriptor(_rd.getId());

		// temporary list of guidances to delete
		Set<Guidance> tmp = new HashSet<Guidance>();
		tmp.addAll(guid);

		RoleDefinition rdef = _rd.getRoleDefinition();

		// removes roleDefinition related to current RoleDescriptor
		if (rdef != null) {
			tmp = this.removeRoleDefinition(rdef, tmp);
		}

		// preparing to delete current RoleDescriptor
		this.objectsToRemoveLast.add(_rd);
		return tmp;
	}

	/**
	 * 
	 * Method to remove a roleDefinition from a process
	 * 
	 * @param _rdef
	 *            a roleDefinition of the current process
	 * @param _guid
	 *            global guidances to remove from the database
	 * 
	 * @return Set<Guidance> global guidance updated with guidances of the
	 *         current roleDefinition
	 */
	private Set<Guidance> removeRoleDefinition(RoleDefinition _rdef,
			Set<Guidance> guid) {

		// loads roleDefinition from database
		_rdef = this.roleDefinitionDao.getRoleDefinition(_rdef.getId());
		Set<Guidance> tmp = new HashSet<Guidance>();
		tmp.addAll(guid);

		// temporary list of guidances to delete
		tmp.addAll(_rdef.getGuidances());

		// preparing to delete current RoleDefinition
		this.objectsToRemoveLast.add(_rdef);

		return tmp;
	}

	/**
	 * 
	 * Method to remove a tasDescriptor from a process
	 * 
	 * @param _td
	 *            a taskDescriptor of the current process
	 * @param _guid
	 *            global guidances to remove from the database
	 * 
	 * @return Set<Guidance> global guidance updated with guidances of the
	 *         current taskDescriptor
	 */
	private Set<Guidance> removeTaskDescriptor(TaskDescriptor _td,
			Set<Guidance> guid) {

		// loads taskDescriptor from database
		_td = this.taskDescriptorDao.getTaskDescriptor(_td.getId());

		// temporary list of guidances to delete
		Set<Guidance> tmp = new HashSet<Guidance>();
		tmp.addAll(guid);

		TaskDefinition tdef = _td.getTaskDefinition();

		// removes taskDefinition related to current TaskDescriptor
		if (tdef != null) {
			tmp = this.removeTaskDefinition(tdef, tmp);
		}

		this.findWorkOrders(_td);

		// preparing to delete current TaskDescriptor
		this.objectsToRemoveLast.add(_td);
		return tmp;
	}

	/**
	 * 
	 * Method to remove a milestone from a process
	 * 
	 * @param _mi
	 *            a milestone of the current process
	 * @param _guid
	 *            global guidances to remove from the database
	 * 
	 * @return Set<Guidance> global guidance updated with guidances of the
	 *         current milestone
	 */
	private Set<Guidance> removeMilestone(Milestone _mi, Set<Guidance> guid) {

		// loads milestone from database
		_mi = this.milestoneDao.getMilestone(_mi.getId());

		// temporary list of guidances to delete
		Set<Guidance> tmp = new HashSet<Guidance>();
		tmp.addAll(guid);

		this.findWorkOrders(_mi);

		// preparing to delete current Milestone
		this.objectsToRemoveLast.add(_mi);
		return tmp;
	}

	/**
	 * Detect all workOrder objects related to workBreakdownElement
	 * 
	 * @param _wbde
	 *            WorkBreakdownElement object
	 */
	private void findWorkOrders(WorkBreakdownElement _wbde) {

		// Removes WorkOrder predecessors objects
		for (WorkOrder wo : _wbde.getPredecessors()) {
			this.workOrderToRemove.add(wo);
		}

		// Removes WorkOrder predecessors objects
		for (WorkOrder wo : _wbde.getSuccessors()) {
			this.workOrderToRemove.add(wo);
		}
	}

	/**
	 * 
	 * Method to remove a taskDefinition from a process
	 * 
	 * @param _tdef
	 *            a taskDefinition of the current process
	 * @param _guid
	 *            global guidances to remove from the database
	 * 
	 * @return Set<Guidance> global guidance updated with guidances of the
	 *         current taskDefinition
	 */
	private Set<Guidance> removeTaskDefinition(TaskDefinition _tdef,
			Set<Guidance> guid) {

		// loads taskDefinition from database
		_tdef = this.taskDefinitionDao.getTaskDefinition(_tdef.getId());

		// temporary list of guidances to delete
		Set<Guidance> tmp = new HashSet<Guidance>();
		tmp.addAll(guid);
		tmp.addAll(_tdef.getGuidances());

		List<Step> steps = new ArrayList<Step>();

		// retrieve steps to delete
		steps.addAll(_tdef.getSteps());

		// preparing to delete all steps
		for (Step step : steps) {
			step = this.stepDao.getStep(step.getId());
			this.objectsToRemoveLast.add(step);
		}

		// preparing to delete current taskDefinition
		this.objectsToRemoveLast.add(_tdef);

		return tmp;
	}

	/**
	 * 
	 * Method to remove a guidance from a process
	 * 
	 * @param _guidance
	 *            a guidance of the current process
	 */
	private void removeGuidance(Guidance _guidance) {

		if (_guidance instanceof CheckList) {
			CheckList cl = (CheckList) _guidance;
			// removes current checkList
			this.removeCheckList(cl);
		} else {
			// loads guidance from database
			_guidance = this.guidanceDao.getGuidance(_guidance.getId());

			// delete current guidance
			this.guidanceDao.deleteGuidance(_guidance);
		}
	}

	/**
	 * 
	 * Method to remove a checkList from a process
	 * 
	 * @param _checkList
	 *            a checkList of the current process
	 */
	private void removeCheckList(CheckList _checkList) {

		// loads checkList from database
		_checkList = this.checkListDao.getCheckList(_checkList.getId());

		// retrieve all sections to delete
		List<Section> sections = new ArrayList<Section>();
		sections.addAll(_checkList.getSections());

		// delete sections of this checkList
		for (Section section : sections) {
			this.removeSection(section);
		}

		// delete current checkList
		this.checkListDao.deleteCheckList(_checkList);
	}

	/**
	 * 
	 * Method to remove a section from a process
	 * 
	 * @param _section
	 *            a section of the current process
	 */
	private void removeSection(Section _section) {
		// loads section from database
		_section = this.sectionDao.getSection(_section.getId());

		// delete current section
		this.sectionDao.deleteSection(_section);
	}

	/**
	 * Tests if a process has been already instanciated.
	 * 
	 * @param _processId
	 *            identificator of the process to be tested
	 * @return true if process has a non-null list projects, false otherwise
	 */
	public boolean hasBeenInstanciated(String _processId) {
		Process currentProcess = this.processDao.getProcess(_processId);
		if (currentProcess != null && currentProcess.getProjects().isEmpty())
			return false;
		else
			return true;
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
	 *            The processDao to set.
	 */
	public void setProcessDao(ProcessDao _processDao) {
		this.processDao = _processDao;
	}

	/**
	 * Getter of activityDao.
	 * 
	 * @return the activityDao.
	 */
	public ActivityDao getActivityDao() {
		return activityDao;
	}

	/**
	 * Setter of activityDao.
	 * 
	 * @param activityDao
	 *            The activityDao to set.
	 */
	public void setActivityDao(ActivityDao activityDao) {
		this.activityDao = activityDao;
	}

	/**
	 * Getter of breakdownElementDao.
	 * 
	 * @return the breakdownElementDao.
	 */
	public BreakdownElementDao getBreakdownElementDao() {
		return breakdownElementDao;
	}

	/**
	 * Setter of breakdownElementDao.
	 * 
	 * @param breakdownElementDao
	 *            The breakdownElementDao to set.
	 */
	public void setBreakdownElementDao(BreakdownElementDao breakdownElementDao) {
		this.breakdownElementDao = breakdownElementDao;
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
	 *            The elementDao to set.
	 */
	public void setElementDao(ElementDao elementDao) {
		this.elementDao = elementDao;
	}

	/**
	 * Getter of roleDefinitionDao.
	 * 
	 * @return the roleDefinitionDao.
	 */
	public RoleDefinitionDao getRoleDefinitionDao() {
		return roleDefinitionDao;
	}

	/**
	 * Setter of roleDefinitionDao.
	 * 
	 * @param roleDefinitionDao
	 *            The roleDefinitionDao to set.
	 */
	public void setRoleDefinitionDao(RoleDefinitionDao roleDefinitionDao) {
		this.roleDefinitionDao = roleDefinitionDao;
	}

	/**
	 * Getter of roleDescriptorDao.
	 * 
	 * @return the roleDescriptorDao.
	 */
	public RoleDescriptorDao getRoleDescriptorDao() {
		return roleDescriptorDao;
	}

	/**
	 * Setter of roleDescriptorDao.
	 * 
	 * @param roleDescriptorDao
	 *            The roleDescriptorDao to set.
	 */
	public void setRoleDescriptorDao(RoleDescriptorDao roleDescriptorDao) {
		this.roleDescriptorDao = roleDescriptorDao;
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
	 *            The stepDao to set.
	 */
	public void setStepDao(StepDao stepDao) {
		this.stepDao = stepDao;
	}

	/**
	 * Getter of taskDefinitionDao.
	 * 
	 * @return the taskDefinitionDao.
	 */
	public TaskDefinitionDao getTaskDefinitionDao() {
		return taskDefinitionDao;
	}

	/**
	 * Setter of taskDefinitionDao.
	 * 
	 * @param taskDefinitionDao
	 *            The taskDefinitionDao to set.
	 */
	public void setTaskDefinitionDao(TaskDefinitionDao taskDefinitionDao) {
		this.taskDefinitionDao = taskDefinitionDao;
	}

	/**
	 * Getter of taskDescriptorDao.
	 * 
	 * @return the taskDescriptorDao.
	 */
	public TaskDescriptorDao getTaskDescriptorDao() {
		return taskDescriptorDao;
	}

	/**
	 * Setter of taskDescriptorDao.
	 * 
	 * @param taskDescriptorDao
	 *            The taskDescriptorDao to set.
	 */
	public void setTaskDescriptorDao(TaskDescriptorDao taskDescriptorDao) {
		this.taskDescriptorDao = taskDescriptorDao;
	}

	/**
	 * Getter of workBreakdownElementDao.
	 * 
	 * @return the workBreakdownElementDao.
	 */
	public WorkBreakdownElementDao getWorkBreakdownElementDao() {
		return workBreakdownElementDao;
	}

	/**
	 * Setter of workBreakdownElementDao.
	 * 
	 * @param workBreakdownElementDao
	 *            The workBreakdownElementDao to set.
	 */
	public void setWorkBreakdownElementDao(
			WorkBreakdownElementDao workBreakdownElementDao) {
		this.workBreakdownElementDao = workBreakdownElementDao;
	}

	/**
	 * Getter of checkListDao.
	 * 
	 * @return the checkListDao.
	 */
	public CheckListDao getCheckListDao() {
		return this.checkListDao;
	}

	/**
	 * Setter of checkListDao.
	 * 
	 * @param checkListDao
	 *            The checkListDao to set.
	 */
	public void setCheckListDao(CheckListDao _checkListDao) {
		this.checkListDao = _checkListDao;
	}

	/**
	 * Getter of guidanceDao.
	 * 
	 * @return the guidanceDao.
	 */
	public GuidanceDao getGuidanceDao() {
		return this.guidanceDao;
	}

	/**
	 * Setter of guidanceDao.
	 * 
	 * @param guidanceDao
	 *            The guidanceDao to set.
	 */
	public void setGuidanceDao(GuidanceDao _guidanceDao) {
		this.guidanceDao = _guidanceDao;
	}

	/**
	 * Getter of iterationDao.
	 * 
	 * @return the iterationDao.
	 */
	public IterationDao getIterationDao() {
		return this.iterationDao;
	}

	/**
	 * Setter of iterationDao.
	 * 
	 * @param iterationDao
	 *            The iterationDao to set.
	 */
	public void setIterationDao(IterationDao _iterationDao) {
		this.iterationDao = _iterationDao;
	}

	/**
	 * Getter of phaseDao.
	 * 
	 * @return the phaseDao.
	 */
	public PhaseDao getPhaseDao() {
		return this.phaseDao;
	}

	/**
	 * Setter of phaseDao.
	 * 
	 * @param phaseDao
	 *            The phaseDao to set.
	 */
	public void setPhaseDao(PhaseDao _phaseDao) {
		this.phaseDao = _phaseDao;
	}

	/**
	 * Getter of projectDao.
	 * 
	 * @return the projectDao.
	 */
	public ProjectDao getProjectDao() {
		return this.projectDao;
	}

	/**
	 * Setter of projectDao.
	 * 
	 * @param projectDao
	 *            The projectDao to set.
	 */
	public void setProjectDao(ProjectDao _projectDao) {
		this.projectDao = _projectDao;
	}

	/**
	 * Getter of sectionDao.
	 * 
	 * @return the sectionDao.
	 */
	public SectionDao getSectionDao() {
		return this.sectionDao;
	}

	/**
	 * Setter of sectionDao.
	 * 
	 * @param sectionDao
	 *            The sectionDao to set.
	 */
	public void setSectionDao(SectionDao _sectionDao) {
		this.sectionDao = _sectionDao;
	}

	public WorkOrderService getWorkOrderService() {
		return workOrderService;
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	/**
	 * @return the workProductDefinitionDao
	 */
	public WorkProductDefinitionDao getWorkProductDefinitionDao() {
		return workProductDefinitionDao;
	}

	/**
	 * @param _workProductDefinitionDao
	 *            the workProductDefinitionDao to set
	 */
	public void setWorkProductDefinitionDao(
			WorkProductDefinitionDao _workProductDefinitionDao) {
		workProductDefinitionDao = _workProductDefinitionDao;
	}

	/**
	 * @return the workProductDescriptorDao
	 */
	public WorkProductDescriptorDao getWorkProductDescriptorDao() {
		return workProductDescriptorDao;
	}

	/**
	 * @param _workProductDescriptorDao
	 *            the workProductDescriptorDao to set
	 */
	public void setWorkProductDescriptorDao(
			WorkProductDescriptorDao _workProductDescriptorDao) {
		workProductDescriptorDao = _workProductDescriptorDao;
	}

	/**
	 * @return the milestoneDao
	 */
	public MilestoneDao getMilestoneDao() {
		return milestoneDao;
	}

	/**
	 * @param _milestoneDao
	 *            the milestoneDao to set
	 */
	public void setMilestoneDao(MilestoneDao _milestoneDao) {
		milestoneDao = _milestoneDao;
	}
}
