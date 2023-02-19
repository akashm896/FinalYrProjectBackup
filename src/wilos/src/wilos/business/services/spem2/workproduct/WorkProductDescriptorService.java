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

package wilos.business.services.spem2.workproduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.business.services.misc.concreteactivity.ConcreteActivityService;
import wilos.business.services.misc.concretebreakdownelement.ConcreteBreakdownElementService;
import wilos.business.services.misc.concretetask.ConcreteTaskDescriptorService;
import wilos.business.services.misc.concreteworkproduct.ConcreteWorkProductDescriptorService;
import wilos.business.services.misc.project.ProjectService;
import wilos.business.services.spem2.activity.ActivityService;
import wilos.business.services.spem2.breakdownelement.BreakdownElementService;
import wilos.business.services.spem2.process.ProcessService;
import wilos.hibernate.spem2.role.RoleDescriptorDao;
import wilos.hibernate.spem2.workproduct.WorkProductDescriptorDao;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;
import wilos.model.misc.project.Project;
import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.process.Process;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.model.spem2.workproduct.WorkProductDescriptor;
import wilos.hibernate.misc.concreteactivity.ConcreteActivityDao;
import wilos.hibernate.misc.concretetask.ConcreteTaskDescriptorDao;
import wilos.hibernate.misc.concreteworkproduct.ConcreteWorkProductDescriptorDao;
import wilos.resources.LocaleBean;
import wilos.utils.Constantes.State;

@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class WorkProductDescriptorService {

    /* Attributes */

    private ConcreteWorkProductDescriptorDao concreteWorkProductDescriptorDao;

    private WorkProductDescriptorDao workProductDescriptorDao;

    private ConcreteActivityService concreteActivityService;

    private ProcessService processService;

    private ActivityService activityService;

    private ConcreteTaskDescriptorService concreteTaskDescriptorService;

    private BreakdownElementService breakdownElementService;

    private ConcreteBreakdownElementService concreteBreakdownElementService;

    private ProjectService projectService;

    private ConcreteWorkProductDescriptorService concreteWorkProductDescriptorService;

    // TODO [Wilos3]

    private ConcreteTaskDescriptorDao concreteTaskDescriptorDao;

    private RoleDescriptorDao roleDescriptorDao;

    private ConcreteActivityDao concreteActivityDao;

    /**
     * @return the concreteWorkProductDescriptorDao
     */
    public ConcreteWorkProductDescriptorDao getConcreteWorkProductDescriptorDao() {
	return concreteWorkProductDescriptorDao;
    }

    /**
     * @param _concreteWorkProductDescriptorDao
     *                the concreteWorkProductDescriptorDao to set
     */
    public void setConcreteWorkProductDescriptorDao(
	    ConcreteWorkProductDescriptorDao _concreteWorkProductDescriptorDao) {
	concreteWorkProductDescriptorDao = _concreteWorkProductDescriptorDao;
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
     * @return the concreteActivityService
     */
    public ConcreteActivityService getConcreteActivityService() {
	return concreteActivityService;
    }

    /**
     * @param _concreteActivityService
     *                the concreteActivityService to set
     */
    public void setConcreteActivityService(
	    ConcreteActivityService _concreteActivityService) {
	concreteActivityService = _concreteActivityService;
    }

    /**
     * @return the processService
     */
    public ProcessService getProcessService() {
	return processService;
    }

    /**
     * @param _processService
     *                the processService to set
     */
    public void setProcessService(ProcessService _processService) {
	processService = _processService;
    }

    /**
     * @return the activityService
     */
    public ActivityService getActivityService() {
	return activityService;
    }

    /**
     * @param _activityService
     *                the activityService to set
     */
    public void setActivityService(ActivityService _activityService) {
	activityService = _activityService;
    }

    /**
     * @return the breakdownElementService
     */
    public BreakdownElementService getBreakdownElementService() {
	return breakdownElementService;
    }

    /**
     * @param _breakdownElementService
     *                the breakdownElementService to set
     */
    public void setBreakdownElementService(
	    BreakdownElementService _breakdownElementService) {
	breakdownElementService = _breakdownElementService;
    }

    /**
     * @return the concreteBreakdownElementService
     */
    public ConcreteBreakdownElementService getConcreteBreakdownElementService() {
	return concreteBreakdownElementService;
    }

    /**
     * @param _concreteBreakdownElementService
     *                the concreteBreakdownElementService to set
     */
    public void setConcreteBreakdownElementService(
	    ConcreteBreakdownElementService _concreteBreakdownElementService) {
	concreteBreakdownElementService = _concreteBreakdownElementService;
    }

    /**
     * @return the projectService
     */
    public ProjectService getProjectService() {
	return projectService;
    }

    /**
     * @param _projectService
     *                the projectService to set
     */
    public void setProjectService(ProjectService _projectService) {
	projectService = _projectService;
    }

    /**
     * @param _project
     * @param _process
     * @param _expTableContent
     */
    public void workProductsInstanciation(Project _project, Process _process,
	    List<HashMap<String, Object>> _expTableContent) {

	this.concreteActivityService.getConcreteActivityDao()
		.getSessionFactory().getCurrentSession().saveOrUpdate(_project);
	this.activityService.getActivityDao().getSessionFactory()
		.getCurrentSession().saveOrUpdate(_process);

	for (HashMap<String, Object> hm : _expTableContent) {
	    if (hm.get("isDisabled") != null) {
		// general workproduct instanciation
		if ((Boolean) hm.get("isDisabled") == false) {
		    for (HashMap<String, Object> tmp : this.projectService
			    .getDifferentPathsOfWorkProductDescriptorInProcess(
				    _process, (String) hm.get("name"))) {
			WorkProductDescriptor wpd = this.workProductDescriptorDao
				.getWorkProductDescriptor((String) tmp
					.get("id"));
			for (Activity act : this.breakdownElementService
				.getSuperActivitiesUnderList(wpd)) {

			    if (!_process.getGuid().equals(
				    "_0uyGoMlgEdmt3adZL5Dmdw")) {
				this.generalParsing(_project, hm, wpd, act);
			    } else {
				this.openUPParsing(_project, hm, tmp, wpd, act);
			    }
			}
		    }
		}
	    } else {
		// instanciation for each occurence of a workproduct
		WorkProductDescriptor wpd = this.workProductDescriptorDao
			.getWorkProductDescriptor((String) hm.get("id"));
		for (Activity act : this.breakdownElementService
			.getSuperActivitiesUnderList(wpd)) {
		    if (!_process.getGuid().equals("_0uyGoMlgEdmt3adZL5Dmdw")) {
			generalParsing(_project, hm, wpd, act);
		    } else {
			openUPParsing(_project, hm, hm, wpd, act);
		    }
		}
	    }
	}
    }

    private void openUPParsing(Project _project, HashMap<String, Object> hm,
	    HashMap<String, Object> tmp, WorkProductDescriptor wpd,
	    Activity _act) {
	// open up process
	String path = (String) tmp.get("name");
	path = path.substring(0, path.lastIndexOf('/') - 1);
	path = path.substring(0, path.lastIndexOf('/') - 1);
	String cAncestorName = path.substring(path.lastIndexOf('/') + 2, path
		.length());
	Activity superAct = null;
	for (Activity superActTmp : _act.getSuperActivities()) {
	    if (superActTmp.getPresentationName().equals(cAncestorName)) {
		superAct = superActTmp;
		break;
	    }
	}

	if (_act instanceof Process) {
	    this.workProductDescriptorInstanciation(_project, wpd, _project,
		    (Integer) hm.get("nbOccurences"));
	} else {

	    for (ConcreteActivity ca : this.activityService
		    .getConcreteActivitiesFromActivityAndForAProject(_act,
			    _project)) {
		List<ConcreteActivity> listSuperCA = this.concreteBreakdownElementService
			.getSuperConcreteActivitiesUnderList(ca);
		if (listSuperCA.get(0).getActivity().equals(superAct))
		    this.workProductDescriptorInstanciation(_project, wpd, ca,
			    (Integer) hm.get("nbOccurences"));
	    }
	}
    }

    /**
     * 
     * @param _project
     * @param hm
     * @param wpd
     * @param _act
     */
    private void generalParsing(Project _project, HashMap<String, Object> hm,
	    WorkProductDescriptor wpd, Activity _act) {
	// scrum process
	if (_act instanceof Process) {
	    this.workProductDescriptorInstanciation(_project, wpd, _project,
		    (Integer) hm.get("nbOccurences"));
	} else {
	    for (ConcreteActivity ca : this.activityService
		    .getConcreteActivitiesFromActivityAndForAProject(_act,
			    _project)) {
		this.workProductDescriptorInstanciation(_project, wpd, ca,
			(Integer) hm.get("nbOccurences"));
	    }
	}
    }

    /**
     * 
     * @param _project
     * @param _wpd
     * @param _cact
     * @param _occ
     */
    public void workProductDescriptorInstanciation(Project _project,
	    WorkProductDescriptor _wpd, ConcreteActivity _cact, int _occ) {

	if (!(_cact.getState().equals(State.FINISHED))) {
	    if (_occ > 0) {
		int nbCwpd = 0;
		for (ConcreteBreakdownElement tmp : this.concreteActivityService
			.getConcreteBreakdownElements(_cact)) {
		    if (tmp instanceof ConcreteWorkProductDescriptor) {
			if (((ConcreteWorkProductDescriptor) tmp)
				.getWorkProductDescriptor().getId().equals(
					_wpd.getId())) {
			    nbCwpd++;
			}
		    }
		}
		for (int i = nbCwpd + 1; i <= nbCwpd + _occ; i++) {

		    ConcreteWorkProductDescriptor cwpd = new ConcreteWorkProductDescriptor();

		    if (_wpd.getPresentationName().equals(""))
			cwpd.setConcreteName(_wpd.getName() + "#" + i);
		    else
			cwpd.setConcreteName(_wpd.getPresentationName() + "#"
				+ i);

		    cwpd.addWorkProductDescriptor(_wpd);
		    cwpd.setBreakdownElement(_wpd);
		    cwpd.setProject(_project);
		    cwpd.setInstanciationOrder(i);
		    cwpd.addSuperConcreteActivity(_cact);
		    cwpd.addAllProducerConcreteTasks(_wpd.getProducerTasks(), _project);
		    cwpd.addAllOptionalUserConcreteTasks(_wpd
			    .getOptionalUserTasks(), _project);
		    cwpd.addAllMandatoryUserConcreteTasks(_wpd
			    .getMandatoryUserTasks(), _project);

		    this.concreteWorkProductDescriptorDao
			    .saveOrUpdateConcreteWorkProductDescriptor(cwpd);
		    System.out
			    .println("### ConcreteWorkProductDescriptor sauve : "
				    + cwpd.getConcreteName());

		    this.concreteActivityService.saveConcreteActivity(_cact);
		    System.out.println("### ConcreteActivity updated : "
			    + cwpd.getConcreteName());

		}
	    }
	}
    }

    /**
     * @param _workProductDescriptor
     * @return
     */
    public String saveWorkProductDescriptor(
	    WorkProductDescriptor _workProductDescriptor) {
	return this.workProductDescriptorDao
		.saveOrUpdateWorkProductDescriptor(_workProductDescriptor);
    }

    /**
     * @param _workProductDescriptorId
     * @return
     */
    public WorkProductDescriptor getWorkProductDescriptor(
	    String _workProductDescriptorId) {
	return this.workProductDescriptorDao
		.getWorkProductDescriptor(_workProductDescriptorId);
    }

    /**
     * @param _workProductDescriptor
     */
    public void deleteWorkProductDescriptor(
	    WorkProductDescriptor _workProductDescriptor) {
	this.workProductDescriptorDao
		.deleteWorkProductDescriptor(_workProductDescriptor);
    }

    /**
     * @return
     */
    public List<WorkProductDescriptor> getAllWorkProductDescriptors() {
	return this.workProductDescriptorDao.getAllWorkProductDescriptors();
    }

    /**
     * @param _id
     * @return
     */
    public WorkProductDescriptor getWorkProductDescriptorById(String _id) {
	return this.workProductDescriptorDao.getWorkProductDescriptor(_id);
    }


    public RoleDescriptor getRoleDescriptor(WorkProductDescriptor _wpd)
    {
	this.workProductDescriptorDao.getSessionFactory()
	.getCurrentSession().saveOrUpdate(
		_wpd);
	return _wpd.getResponsibleRoleDescriptor();
    }


    public boolean createWorkProduct(String _workProductName,
			String _workProductDescription, Project _project,
			RoleDescriptor _role, ConcreteActivity _cact,
			String _activityEntryState, String _activityExitState,
			ArrayList<String> producerConcreteTasksIDs,
			ArrayList<String> optionalUserConcreteTaskIDs,
			ArrayList<String> mandatoryUserConcreteTasksIDs) {

		if (_role != null) {
			this.roleDescriptorDao.getSessionFactory().getCurrentSession()
					.saveOrUpdate(_role);
		}

		if (_cact != null) {
			this.concreteActivityDao.getSessionFactory().getCurrentSession()
					.saveOrUpdate(_cact);
		} else {
			_cact = _project;
		}
		String activityEntryState = _activityEntryState;
		String activityExitState = _activityExitState;
		if (_activityEntryState == null || _activityEntryState == "") {
			activityEntryState = LocaleBean
					.getText("component.project.workproductsinstanciation.noEntryState");
		}
		if (_activityExitState == null || _activityExitState == "") {
			activityExitState = LocaleBean
					.getText("component.project.workproductsinstanciation.noExitState");
		}

		WorkProductDescriptor wpd = this.createWorkProductDescriptor(
				_workProductName, _workProductDescription, _role,
				_workProductName, activityEntryState, activityExitState);

		if (wpd == null) {
			return false;
		}
		String createConcreteWorkProductDescriptorID = this
				.createConcreteWorkProductDescriptor(_workProductName,
						_project, wpd, _cact);
		if (createConcreteWorkProductDescriptorID == null) {
			return false;
		} else {

			ConcreteWorkProductDescriptor cwpd = this.concreteWorkProductDescriptorDao
					.getConcreteWorkProductDescriptor(createConcreteWorkProductDescriptorID);

			if (producerConcreteTasksIDs.size() != 0) {
				for (String producerConcreteTasksID : producerConcreteTasksIDs) {
					ConcreteTaskDescriptor concreteTaskdescriptor = this.concreteTaskDescriptorDao
							.getConcreteTaskDescriptor(producerConcreteTasksID);
					concreteTaskdescriptor.addOutputConcreteWorkProduct(cwpd);
					this.concreteTaskDescriptorDao
							.saveOrUpdateConcreteTaskDescriptor(concreteTaskdescriptor);
				}
			}
			if (optionalUserConcreteTaskIDs.size() != 0) {
				for (String optionalUserConcreteTaskID : optionalUserConcreteTaskIDs) {
					ConcreteTaskDescriptor concreteTaskdescriptor = this.concreteTaskDescriptorDao
							.getConcreteTaskDescriptor(optionalUserConcreteTaskID);
					concreteTaskdescriptor
							.addOptionalInputConcreteWorkProduct(cwpd);
					this.concreteTaskDescriptorDao
							.saveOrUpdateConcreteTaskDescriptor(concreteTaskdescriptor);
				}
			}

			if (mandatoryUserConcreteTasksIDs.size() != 0) {
				for (String mandatoryUserConcreteTasksID : mandatoryUserConcreteTasksIDs) {
					ConcreteTaskDescriptor concreteTaskdescriptor = this.concreteTaskDescriptorDao
							.getConcreteTaskDescriptor(mandatoryUserConcreteTasksID);
					concreteTaskdescriptor
							.addMandatoryInputConcreteWorkProduct(cwpd);
					this.concreteTaskDescriptorDao
							.saveOrUpdateConcreteTaskDescriptor(concreteTaskdescriptor);
				}
			}
			return true;
		}
	}

    /**
     * @param _wPName
     * @param _wPDescription
     * @param _role
     * @param _wPName2
     * @return
     */
    private WorkProductDescriptor createWorkProductDescriptor(String _wPName,
	    String _wPDescription, RoleDescriptor _role, String _wPName2,
	    String _activityEntryState, String _activityExitState) {

	WorkProductDescriptor wpd = new WorkProductDescriptor();
	wpd.setPresentationName(_wPName);
	wpd.setDescription(_wPDescription);
	wpd.setGuid(_wPName2);
	wpd.setPrefix("");
	wpd.setIsPlanned(true);
	wpd.setHasMultipleOccurrences(false);
	wpd.setIsOptional(false);
	wpd.setResponsibleRoleDescriptor(_role);
	wpd.setIsOutOfProcess(true);
	wpd.setActivityEntryState(_activityEntryState);
	wpd.setActivityExitState(_activityExitState);

	this.workProductDescriptorDao.saveOrUpdateWorkProductDescriptor(wpd);
	System.out.println("### WorkProductDescriptor sauve");
	if (wpd.getId() != null) {
	    return wpd;
	} else {
	    return null;
	}
    }

    public String createConcreteWorkProductDescriptor(String _concreteName,
	    Project _project, WorkProductDescriptor _wpd, ConcreteActivity _cact) {

	this.concreteActivityDao.getSessionFactory().getCurrentSession()
		.saveOrUpdate(_cact);

	if (!(_project.getId().equals(_cact.getId()))) {
	    this.concreteActivityService.getConcreteActivityDao()
		    .getSessionFactory().getCurrentSession().saveOrUpdate(
			    _project);
	}

	ConcreteWorkProductDescriptor cwpd = new ConcreteWorkProductDescriptor();

	// this variable is used to generate a random number for the
	// instanciationOrder in order to correct the tree problem
	int i = Math.abs((int) System.currentTimeMillis());

	cwpd.setConcreteName(_concreteName);
	cwpd.addWorkProductDescriptor(_wpd);
	cwpd.setBreakdownElement(_wpd);
	cwpd.setProject(_project);
	cwpd.setInstanciationOrder(i);
	cwpd.addSuperConcreteActivity(_cact);

	this.concreteWorkProductDescriptorDao
		.saveOrUpdateConcreteWorkProductDescriptor(cwpd);
	System.out.println("### ConcreteWorkProductDescriptor sauve : "
		+ cwpd.getConcreteName());

	this.concreteActivityService.saveConcreteActivity(_cact);
	System.out.println("### ConcreteActivity updated : "
		+ cwpd.getConcreteName());

	System.out.println("### ConcreteWorkProductDescriptor sauve");
	return cwpd.getId();
    }

    /**
     * @return the roleDescriptorDao
     */
    public RoleDescriptorDao getRoleDescriptorDao() {
	return roleDescriptorDao;
    }

    /**
     * @param _roleDescriptorDao
     *                the roleDescriptorDao to set
     */
    public void setRoleDescriptorDao(RoleDescriptorDao _roleDescriptorDao) {
	roleDescriptorDao = _roleDescriptorDao;
    }

    /**
     * @return the concreteActivityDao
     */
    public ConcreteActivityDao getConcreteActivityDao() {
	return concreteActivityDao;
    }

    /**
     * @param _concreteActivityDao
     *                the concreteActivityDao to set
     */
    public void setConcreteActivityDao(ConcreteActivityDao _concreteActivityDao) {
	concreteActivityDao = _concreteActivityDao;
    }

    public Set<ConcreteWorkProductDescriptor> getConcreteWorkProductDescriptors(
	    WorkProductDescriptor _wpd) {
	this.workProductDescriptorDao.getSessionFactory().getCurrentSession()
		.saveOrUpdate(_wpd);
	Set<ConcreteWorkProductDescriptor> tmp = new HashSet<ConcreteWorkProductDescriptor>();
	for (ConcreteWorkProductDescriptor ctd : _wpd
		.getConcreteWorkProductDescriptors()) {
	    tmp.add(ctd);
	}
	return tmp;
    }

    
   public String getNameWorkProductDescriptor(WorkProductDescriptor _wpd)
   {
       this.workProductDescriptorDao.getSessionFactory().getCurrentSession()
	.saveOrUpdate(_wpd);
       return _wpd.getPresentationName();
   }
    
   public String getNameRequiredRoleWorkProductDescriptor(WorkProductDescriptor _wpd)
   {
       this.workProductDescriptorDao.getSessionFactory().getCurrentSession()
	.saveOrUpdate(_wpd);
       if(_wpd.getResponsibleRoleDescriptor() != null)
       {
           this.roleDescriptorDao.saveOrUpdateRoleDescriptor(_wpd.getResponsibleRoleDescriptor());
           
           return _wpd.getResponsibleRoleDescriptor().getPresentationName();
       }
       else
       {
	   return null;
       }
   }
   
   public RoleDescriptor getRequiredRoleWorkProductDescriptor(WorkProductDescriptor _wpd)
   {
       this.workProductDescriptorDao.getSessionFactory().getCurrentSession()
	.saveOrUpdate(_wpd);
       if(_wpd.getResponsibleRoleDescriptor() != null)
       {
           this.roleDescriptorDao.saveOrUpdateRoleDescriptor(_wpd.getResponsibleRoleDescriptor());
           
           return _wpd.getResponsibleRoleDescriptor();
       }
       else
       {
	   return null;
       }
   }

    public ConcreteTaskDescriptorService getConcreteTaskDescriptorService() {
	return this.concreteTaskDescriptorService;
    }

    public void setConcreteTaskDescriptorService(
	    ConcreteTaskDescriptorService _concreteTaskDescriptorService) {
	this.concreteTaskDescriptorService = _concreteTaskDescriptorService;
    }

    public ConcreteTaskDescriptorDao getConcreteTaskDescriptorDao() {
	return this.concreteTaskDescriptorDao;
    }

    public void setConcreteTaskDescriptorDao(
	    ConcreteTaskDescriptorDao _concreteTaskDescriptorDao) {
	this.concreteTaskDescriptorDao = _concreteTaskDescriptorDao;
    }

    public ConcreteWorkProductDescriptorService getConcreteWorkProductDescriptorService() {
	return this.concreteWorkProductDescriptorService;
    }

    public void setConcreteWorkProductDescriptorService(
	    ConcreteWorkProductDescriptorService _concreteWorkProductDescriptorService) {
	this.concreteWorkProductDescriptorService = _concreteWorkProductDescriptorService;
    }

}
