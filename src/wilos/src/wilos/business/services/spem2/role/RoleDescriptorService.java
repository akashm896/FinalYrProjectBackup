/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Mathieu BENOIT <mathieu-benoit@hotmail.fr>
 * Copyright (C) 2007 Sebastien BALARD <sbalard@wilos-project.org>
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

package wilos.business.services.spem2.role;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.business.services.misc.concreteactivity.ConcreteActivityService;
import wilos.business.services.misc.concretebreakdownelement.ConcreteBreakdownElementService;
import wilos.business.services.misc.project.ProjectService;
import wilos.business.services.spem2.activity.ActivityService;
import wilos.business.services.spem2.breakdownelement.BreakdownElementService;
import wilos.business.services.spem2.process.ProcessService;
import wilos.hibernate.misc.concreterole.ConcreteRoleDescriptorDao;
import wilos.hibernate.spem2.role.RoleDescriptorDao;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.project.Project;
import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.process.Process;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.utils.Constantes.State;

@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class RoleDescriptorService {

    /* Attributes */

    private ConcreteRoleDescriptorDao concreteRoleDescriptorDao;

    private RoleDescriptorDao roleDescriptorDao;

    private ConcreteActivityService concreteActivityService;

    private ProcessService processService;

    private ActivityService activityService;

    private BreakdownElementService breakdownElementService;

    private ConcreteBreakdownElementService concreteBreakdownElementService;

    private ProjectService projectService;

    /* Public Methods */

    public RoleDescriptor getRoleDescriptor(String _id) {
    	
	return this.roleDescriptorDao.getRoleDescriptor(_id);
    }

    public String saveRoleDescriptor(RoleDescriptor _roleDescriptor) {
	return this.roleDescriptorDao
		.saveOrUpdateRoleDescriptor(_roleDescriptor);
    }

    /**
     * 
     * @param _project
     * @param _rd
     * @param _cact
     * @param _occ
     */
    public void roleDescriptorInstanciation(Project _project,
	    RoleDescriptor _rd, ConcreteActivity _cact, int _occ) {

	if (_occ > 0) {
	    int nbCrd = 0;
	    for (ConcreteBreakdownElement tmp : this.concreteActivityService
		    .getConcreteBreakdownElements(_cact)) {
		if (tmp instanceof ConcreteRoleDescriptor) {
		    if (((ConcreteRoleDescriptor) tmp).getRoleDescriptor()
			    .getId().equals(_rd.getId())) {
			nbCrd++;
		    }
		}
	    }
	    for (int i = nbCrd + 1; i <= nbCrd + _occ; i++) {

		ConcreteRoleDescriptor crd = new ConcreteRoleDescriptor();

		if (_rd.getPresentationName().equals(""))
		    crd.setConcreteName(_rd.getName() + "#" + i);
		else
		    crd.setConcreteName(_rd.getPresentationName() + "#" + i);

		crd.addRoleDescriptor(_rd);
		crd.setBreakdownElement(_rd);
		crd.setProject(_project);
		crd.setInstanciationOrder(i);
		crd.addSuperConcreteActivity(_cact);

		this.concreteRoleDescriptorDao
			.saveOrUpdateConcreteRoleDescriptor(crd);
		if (_project != _cact) {
		}
	    }
	}
    }

    /* Public Methods to resolve the lazy loading problem */

    public Set<TaskDescriptor> getAdditionalTasks(RoleDescriptor _rd) {
	Set<TaskDescriptor> tmp = new HashSet<TaskDescriptor>();
	this.roleDescriptorDao.getSessionFactory().getCurrentSession()
		.saveOrUpdate(_rd);
	for (TaskDescriptor td : _rd.getAdditionalTasks()) {
	    tmp.add(td);
	}
	return tmp;
    }

    /**
     * 
     * @param _rd
     * @return
     */
    public Set<TaskDescriptor> getPrimaryTasks(RoleDescriptor _rd) {
	Set<TaskDescriptor> tmp = new HashSet<TaskDescriptor>();
	this.roleDescriptorDao.getSessionFactory().getCurrentSession()
		.saveOrUpdate(_rd);
	for (TaskDescriptor td : _rd.getPrimaryTasks()) {
	    tmp.add(td);
	}
	return tmp;
    }

    /**
     * 
     * @param _rd
     * @return
     */
    public Set<ConcreteRoleDescriptor> getConcreteRoleDescriptors(
	    RoleDescriptor _rd) {
	Set<ConcreteRoleDescriptor> tmp = new HashSet<ConcreteRoleDescriptor>();
	this.roleDescriptorDao.getSessionFactory().getCurrentSession()
		.saveOrUpdate(_rd);
	for (ConcreteRoleDescriptor td : _rd.getConcreteRoleDescriptors()) {
	    tmp.add(td);
	}
	return tmp;
    }

    /**
     * 
     * @param _project
     * @param _process
     * @param _list
     */
    public void rolesInstanciation(Project _project, Process _process,
	    List<HashMap<String, Object>> _list) {

	this.concreteActivityService.getConcreteActivityDao()
		.getSessionFactory().getCurrentSession().saveOrUpdate(_project);
	this.activityService.getActivityDao().getSessionFactory()
		.getCurrentSession().saveOrUpdate(_process);

	for (HashMap<String, Object> hm : _list) {
	    if (hm.get("isDisabled") != null) {
		// general role instanciation
		if ((Boolean) hm.get("isDisabled") == false) {
		    for (HashMap<String, Object> tmp : this.projectService
			    .getDifferentPathsOfRoleDescriptorInProcess(
				    _process, (String) hm.get("name"))) {
			RoleDescriptor rd = this.roleDescriptorDao
				.getRoleDescriptor((String) tmp.get("id"));
			for (Activity act : this.breakdownElementService
				.getSuperActivitiesUnderList(rd)) {

			    if (!_process.getGuid().equals(
				    "_0uyGoMlgEdmt3adZL5Dmdw")) {
				this.generalParsing(_project, hm, rd, act);
			    } else {
				this.openUPParsing(_project, hm, tmp, rd, act);
			    }
			}
		    }
		}
	    } else {
		// instanciation for each occurence of a role
		RoleDescriptor rd = this.roleDescriptorDao
			.getRoleDescriptor((String) hm.get("id"));
		for (Activity act : this.breakdownElementService
			.getSuperActivitiesUnderList(rd)) {
		    if (!_process.getGuid().equals("_0uyGoMlgEdmt3adZL5Dmdw")) {
			generalParsing(_project, hm, rd, act);
		    } else {
			openUPParsing(_project, hm, hm, rd, act);
		    }
		}
	    }
	}

    }

    /**
     * 
     * @param _project
     * @param hm
     * @param tmp
     * @param rd
     * @param act
     */
    private void openUPParsing(Project _project, HashMap<String, Object> hm,
	    HashMap<String, Object> tmp, RoleDescriptor rd, Activity act) {
	// open up process
	String path = (String) tmp.get("name");
	path = path.substring(0, path.lastIndexOf('/') - 1);
	path = path.substring(0, path.lastIndexOf('/') - 1);
	String cAncestorName = path.substring(path.lastIndexOf('/') + 2, path
		.length());
	Activity superAct = null;
	for (Activity superActTmp : act.getSuperActivities()) {
	    if (superActTmp.getPresentationName().equals(cAncestorName)) {
		superAct = superActTmp;
		break;
	    }
	}
	// first level role in hierarchy
	if (act instanceof Process) {
	    this.roleDescriptorInstanciation(_project, rd, _project,
		    (Integer) hm.get("nbOccurences"));
	} else {
	    // others levels
	    for (ConcreteActivity ca : this.activityService
		    .getConcreteActivitiesFromActivityAndForAProject(act,
			    _project)) {
		if (!ca.getState().equals(State.FINISHED)) {
		    List<ConcreteActivity> listSuperCA = this.concreteBreakdownElementService
			    .getSuperConcreteActivitiesUnderList(ca);
		    if (listSuperCA.get(0).getActivity().equals(superAct))
			this.roleDescriptorInstanciation(_project, rd, ca,
				(Integer) hm.get("nbOccurences"));
		}
	    }
	}
    }

    /**
     * 
     * @param _project
     * @param hm
     * @param rd
     * @param act
     */
    private void generalParsing(Project _project, HashMap<String, Object> hm,
	    RoleDescriptor rd, Activity act) {
	// scrum process
	if (act instanceof Process) {
	    this.roleDescriptorInstanciation(_project, rd, _project,
		    (Integer) hm.get("nbOccurences"));
	} else {
	    for (ConcreteActivity ca : this.activityService
		    .getConcreteActivitiesFromActivityAndForAProject(act,
			    _project)) {
		if (!ca.getState().equals(State.FINISHED)) {
		    this.roleDescriptorInstanciation(_project, rd, ca,
			    (Integer) hm.get("nbOccurences"));
		}
	    }
	}
    }

    /* Getters & Setters */

    public RoleDescriptorDao getRoleDescriptorDao() {
	return roleDescriptorDao;
    }

    public void setRoleDescriptorDao(RoleDescriptorDao roleDescriptorDao) {
	this.roleDescriptorDao = roleDescriptorDao;
    }

    public ConcreteRoleDescriptorDao getConcreteRoleDescriptorDao() {
	return concreteRoleDescriptorDao;
    }

    public void setConcreteRoleDescriptorDao(
	    ConcreteRoleDescriptorDao concreteRoleDescriptorDao) {
	this.concreteRoleDescriptorDao = concreteRoleDescriptorDao;
    }

    public ConcreteActivityService getConcreteActivityService() {
	return concreteActivityService;
    }

    public void setConcreteActivityService(
	    ConcreteActivityService concreteActivityService) {
	this.concreteActivityService = concreteActivityService;
    }

    /**
     * @param _roleDescriptor
     */
    public void deleteRoleDescriptor(RoleDescriptor _roleDescriptor) {
	this.roleDescriptorDao.deleteRoleDescriptor(_roleDescriptor);
    }

    /**
     * @return
     */
    public List<RoleDescriptor> getAllRoleDescriptors() {
	return this.roleDescriptorDao.getAllRoleDescriptors();
    }

    /**
     * @return the processService
     */
    public ProcessService getProcessService() {
	return this.processService;
    }

    /**
     * @param _processService
     *                the processService to set
     */
    public void setProcessService(ProcessService _processService) {
	this.processService = _processService;
    }

    /**
     * @return the activityService
     */
    public ActivityService getActivityService() {
	return this.activityService;
    }

    /**
     * @param _activityService
     *                the activityService to set
     */
    public void setActivityService(ActivityService _activityService) {
	this.activityService = _activityService;
    }

    /**
     * @return the breakdownElementService
     */
    public BreakdownElementService getBreakdownElementService() {
	return this.breakdownElementService;
    }

    /**
     * @param _breakdownElementService
     *                the breakdownElementService to set
     */
    public void setBreakdownElementService(
	    BreakdownElementService _breakdownElementService) {
	this.breakdownElementService = _breakdownElementService;
    }

    public ProjectService getProjectService() {
	return projectService;
    }

    public void setProjectService(ProjectService projectService) {
	this.projectService = projectService;
    }

    public ConcreteBreakdownElementService getConcreteBreakdownElementService() {
	return concreteBreakdownElementService;
    }

    public void setConcreteBreakdownElementService(
	    ConcreteBreakdownElementService concreteBreakdownElementService) {
	this.concreteBreakdownElementService = concreteBreakdownElementService;
    }

    public String getPresentationName(RoleDescriptor _rd) {
	this.roleDescriptorDao.saveOrUpdateRoleDescriptor(_rd);
	return _rd.getPresentationName();
    }
}
