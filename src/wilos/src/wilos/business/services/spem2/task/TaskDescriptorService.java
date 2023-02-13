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

package wilos.business.services.spem2.task;

import java.util.HashSet;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.business.services.misc.concreteactivity.ConcreteActivityService;
import wilos.business.services.misc.stateservice.StateService;
import wilos.hibernate.misc.concretetask.ConcreteTaskDescriptorDao;
import wilos.hibernate.spem2.task.TaskDescriptorDao;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.project.Project;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.model.spem2.workproduct.WorkProductDescriptor;
import wilos.utils.Constantes.State;

@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class TaskDescriptorService {

    private ConcreteTaskDescriptorDao concreteTaskDescriptorDao;

    private TaskDescriptorDao taskDescriptorDao;

    private ConcreteActivityService concreteActivityService;


    /**
     * 
     * Process the descriptor of a task
     * 
     * @param _project
     * @param _td
     * @param _cact
     * @param _occ
     * @param _isInstanciated
     * @param _dispOrd
     */
    public void taskDescriptorInstanciation(Project _project,
	    TaskDescriptor _td, ConcreteActivity _cact, int _occ,
	    boolean _isInstanciated, int _dispOrd) {
	// System.out.println(_td.getPresentationName()+ " " + _occ);
	if (_occ > 0) {
	    int nbCtd = 0;
	    for (ConcreteBreakdownElement tmp : this.concreteActivityService
		    .getConcreteBreakdownElements(_cact)) {
		if (tmp instanceof ConcreteTaskDescriptor) {
		    if (((ConcreteTaskDescriptor) tmp).getTaskDescriptor()
			    .getId().equals(_td.getId())) {
			nbCtd++;
		    }
		}
	    }
	    for (int i = nbCtd + 1; i <= nbCtd + _occ; i++) {

		ConcreteTaskDescriptor ctd = new ConcreteTaskDescriptor();
		ctd.setIsActivatedWorkProductStateDependancies(_project
			.getConsiderWorkProductAndTaskLinks());

		if (_occ != 1 || nbCtd != 0) {
		    if (_td.getPresentationName().equals(""))
			ctd.setConcreteName(_td.getName() + "#" + i);
		    else
			ctd
				.setConcreteName(_td.getPresentationName()
					+ "#" + i);
		} else {

		    if (_td.getPresentationName().equals(""))
			ctd.setConcreteName(_td.getName());
		    else
			ctd.setConcreteName(_td.getPresentationName());
		}
		_td.setConcreteTaskDescriptors(this
			.getConcreteTaskDescriptors(_td));

		ctd.addTaskDescriptor(_td);
		ctd.setProject(_project);
		ctd.setBreakdownElement(_td);
		ctd.setWorkBreakdownElement(_td);
		ctd.setInstanciationOrder(i);
		ctd.addSuperConcreteActivity(_cact);
		ctd.setDisplayOrder(ctd.getSuperConcreteActivity()
			.getDisplayOrder()
			+ Integer.toString(_dispOrd + i));
		System.out.println(ctd.getConcreteName() + " " + _cact.getConcreteName() + " " + _cact.getState());
		if(_cact.getState().equals(State.READY)){
		    ctd.setState(State.READY);
		}
		else if(_cact.getState().equals(State.CREATED)){
		    ctd.setState(State.CREATED);
		}
		
		this.concreteTaskDescriptorDao
			.saveOrUpdateConcreteTaskDescriptor(ctd);
	    }
	}
    }

    /**
     * 
     * Update a task of a descriptor
     * 
     * @param _project
     * @param _td
     * @param _cacts
     * @param _occ
     */
    public void taskDescriptorUpdate(Project _project, TaskDescriptor _td,
	    Set<ConcreteActivity> _cacts, int _occ) {
	// one concretephase at least to insert in all attached
	// concreteactivities of the parent of _phase
	if (_occ > 0) {
	    for (ConcreteActivity tmp : _cacts) {		
		
		if (!tmp.getState().equals(State.FINISHED)) {
		    String strDispOrd = this.concreteActivityService
			    .getMaxDisplayOrder(tmp);
		    int dispOrd = Integer.parseInt(strDispOrd) + 1;
		    this.taskDescriptorInstanciation(_project, _td, tmp, _occ,
			    true, dispOrd);
		}

	    }
	}
    }

    /**
     * 
     * Return the TaskDescriptor with his id
     * 
     * @param _id
     * @return TaskDescriptor
     */
    public TaskDescriptor getTaskDescriptorById(String _id) {
	return this.taskDescriptorDao.getTaskDescriptor(_id);
    }

    /**
     * 
     * save the taskDescriptor
     * 
     * @param _taskDescriptor
     */
    public void saveTaskDescriptor(TaskDescriptor _taskDescriptor) {
	this.taskDescriptorDao.saveOrUpdateTaskDescriptor(_taskDescriptor);
    }

    /**
     * 
     * return some additionnalRoles
     * 
     * @param _td
     * @returnSet<RoleDescriptor>
     */
    public Set<RoleDescriptor> getAdditionalRoles(TaskDescriptor _td) {
	Set<RoleDescriptor> tmp = new HashSet<RoleDescriptor>();
	for (RoleDescriptor rd : _td.getAdditionalRoles()) {
	    tmp.add(rd);
	}
	return tmp;
    }

    /**
     * 
     * Return the ConcreteTaskDescriptor
     * 
     * @param _td
     * @return Set<ConcreteTaskDescriptor>
     */
    public Set<ConcreteTaskDescriptor> getConcreteTaskDescriptors(
	    TaskDescriptor _td) {
	this.taskDescriptorDao.getSessionFactory().getCurrentSession()
		.saveOrUpdate(_td);
	Set<ConcreteTaskDescriptor> tmp = new HashSet<ConcreteTaskDescriptor>();
	for (ConcreteTaskDescriptor ctd : _td.getConcreteTaskDescriptors()) {
	    tmp.add(ctd);
	}
	return tmp;
    }

    /**
     * 
     * Getter of TaskDescriptorDao
     * 
     * @return TaskDescriptorDao
     */
    public TaskDescriptorDao getTaskDescriptorDao() {
	return taskDescriptorDao;
    }

    /**
     * 
     * Setter of TaskDescriptorDao
     * 
     * @param _taskDescriptorDao
     */
    public void setTaskDescriptorDao(TaskDescriptorDao _taskDescriptorDao) {
	this.taskDescriptorDao = _taskDescriptorDao;
    }

    /**
     * 
     * Getter of ConcreteActivityService
     * 
     * @return ConcreteActivityService
     */
    public ConcreteActivityService getConcreteActivityService() {
	return concreteActivityService;
    }

    /**
     * 
     * Setter of ConcreteActivityService
     * 
     * @param _concreteActivityService
     */
    public void setConcreteActivityService(
	    ConcreteActivityService _concreteActivityService) {
	this.concreteActivityService = _concreteActivityService;
    }

    /**
     * 
     * Getter of ConcreteTaskDescriptorDao
     * 
     * @return ConcreteTaskDescriptorDao
     */
    public ConcreteTaskDescriptorDao getConcreteTaskDescriptorDao() {
	return concreteTaskDescriptorDao;
    }

    /**
     * 
     * Setter of ConcreteTaskDescriptorDao
     * 
     * @param concreteTaskDescriptorDao
     */
    public void setConcreteTaskDescriptorDao(
	    ConcreteTaskDescriptorDao concreteTaskDescriptorDao) {
	this.concreteTaskDescriptorDao = concreteTaskDescriptorDao;
    }

    /**
     * 
     * Getter of WorkProductDescriptor
     * 
     * @param _td
     * @return Set<WorkProductDescriptor>
     */
    public Set<WorkProductDescriptor> getOutputWorkProducts(TaskDescriptor _td) {
	this.taskDescriptorDao.getSessionFactory().getCurrentSession()
		.saveOrUpdate(_td);
	Set<WorkProductDescriptor> tmp = new HashSet<WorkProductDescriptor>();
	for (WorkProductDescriptor wpd : _td.getOutputWorkProductDescriptors()) {
	    tmp.add(wpd);
	}
	return tmp;
    }

    /**
     * 
     * Getter of WorkProductDescriptor
     * 
     * @param _td
     * @return Set<WorkProductDescriptor>
     */
    public Set<WorkProductDescriptor> getOptionalInputWorkProducts(
	    TaskDescriptor _td) {
	this.taskDescriptorDao.getSessionFactory().getCurrentSession()
		.saveOrUpdate(_td);
	Set<WorkProductDescriptor> tmp = new HashSet<WorkProductDescriptor>();
	for (WorkProductDescriptor wpd : _td
		.getOptionalInputWorkProductDescriptors()) {
	    tmp.add(wpd);
	}
	return tmp;
    }

    /**
     * 
     * Getter of WorkProductDescriptor
     * 
     * @param _td
     * @return Set<WorkProductDescriptor>
     */
    public Set<WorkProductDescriptor> getMandatoryInputWorkProducts(
	    TaskDescriptor _td) {
	this.taskDescriptorDao.getSessionFactory().getCurrentSession()
		.saveOrUpdate(_td);
	Set<WorkProductDescriptor> tmp = new HashSet<WorkProductDescriptor>();
	for (WorkProductDescriptor wpd : _td
		.getMandatoryInputWorkProductDescriptors()) {
	    tmp.add(wpd);
	}
	return tmp;
    }

}
