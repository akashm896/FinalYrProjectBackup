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

package wilos.business.services.misc.concretetask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.business.services.misc.concreteactivity.ConcreteActivityService;
import wilos.business.services.misc.concreterole.ConcreteRoleDescriptorService;
import wilos.business.services.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElementService;
import wilos.business.services.misc.concreteworkbreakdownelement.ConcreteWorkOrderService;
import wilos.business.services.misc.stateservice.StateService;
import wilos.business.services.spem2.role.RoleDescriptorService;
import wilos.business.services.spem2.task.TaskDescriptorService;
import wilos.hibernate.misc.concreteactivity.ConcreteActivityDao;
import wilos.hibernate.misc.concretetask.ConcreteTaskDescriptorDao;
import wilos.hibernate.spem2.workproduct.WorkProductDescriptorDao;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkOrder;
import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;
import wilos.model.misc.dailyremainingtime.DailyRemainingTime;
import wilos.model.misc.project.Project;
import wilos.model.misc.wilosuser.Participant;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.model.spem2.workproduct.WorkProductDescriptor;
import wilos.presentation.web.utils.WebSessionService;
import wilos.utils.Constantes.State;

/**
 * 
 * @author Soosuske
 * @author deder
 * @author nicastel
 * @author Almiriad
 * 
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class ConcreteTaskDescriptorService {

	private ConcreteTaskDescriptorDao concreteTaskDescriptorDao;

	private ConcreteActivityDao concreteActivityDao;

	private WorkProductDescriptorDao workProductDescriptorDao;

	private StateService stateService;

	private ConcreteWorkBreakdownElementService concreteWorkBreakdownElementService;

	private TaskDescriptorService taskDescriptorService;

	private RoleDescriptorService roleDescriptorService;

	private ConcreteRoleDescriptorService concreteRoleDescriptorService;

	private ConcreteWorkOrderService concreteWorkOrderService;

	private ConcreteActivityService concreteActivityService;

	public static final String CHECKED = "images/expandableTable/checked.gif";

	public static final String UNCHECKED = "images/expandableTable/unchecked.gif";

	protected final Log logger = LogFactory.getLog(this.getClass());

	/**
	 * Return the list of concreteTaskDescriptors for a project
	 * 
	 * @return list of concreteTaskDescriptors
	 */
	public List<ConcreteTaskDescriptor> getAllConcreteTaskDescriptorsForProject(
			String _projectId) {
		return this.getConcreteTaskDescriptorDao()
				.getAllConcreteTaskDescriptorsForProject(_projectId);
	}

	/**
	 * Allows to get the concreteTaskDescriptor with its id
	 * 
	 * @param _concreteTaskDescriptorId
	 * @return the concreteTaskDescriptor
	 */
	public ConcreteTaskDescriptor getConcreteTaskDescriptor(
			String _concreteTaskDescriptorId) {
		return this.getConcreteTaskDescriptorDao().getConcreteTaskDescriptor(
				_concreteTaskDescriptorId);
	}

	/**
	 * Save the ConcreteTaskDescriptor modifications into database
	 * 
	 * @param _concreteTaskDescriptor
	 */
	public void updateConcreteTaskDescriptor(
			ConcreteTaskDescriptor _concreteTaskDescriptor) {
		this.concreteTaskDescriptorDao
				.saveOrUpdateConcreteTaskDescriptor(_concreteTaskDescriptor);
	}

	/**
	 * Start the ConcreteTaskDescriptor and save into the data base changing
	 * (i.e. State, realStartingDate).
	 * 
	 * @param _concreteTaskDescriptor
	 */
	public void startConcreteTaskDescriptor(
			ConcreteTaskDescriptor _concreteTaskDescriptor) {

		this.stateService.updateStateTo(_concreteTaskDescriptor, State.STARTED);
	}

	/**
	 * Allows to start a concreteTaskDescriptor with its id
	 * 
	 * @param id
	 */
	public void startConcreteTaskDescriptor(String id) {
		ConcreteTaskDescriptor ct = this.getConcreteTaskDescriptorDao()
				.getConcreteTaskDescriptor(id);
		this.startConcreteTaskDescriptor(ct);
	}

	/**
	 * Allows to change the accomplished time of a task
	 * 
	 * @param taskId
	 * @param newTime
	 */
	public void setAccomplishedTimeByTask(String taskId, float newTime) {
		ConcreteTaskDescriptor ct = this.getConcreteTaskDescriptorDao()
				.getConcreteTaskDescriptor(taskId);
		ct.setAccomplishedTime(newTime);
		this.getConcreteTaskDescriptorDao().saveOrUpdateConcreteTaskDescriptor(
				ct);
	}

	/**
	 * Allows to change the remaining time of a tsk
	 * 
	 * @param taskId
	 * @param newTime
	 */
	public void setRemainingTimeByTask(String taskId, float newTime) {
		ConcreteTaskDescriptor ct = this.getConcreteTaskDescriptorDao()
				.getConcreteTaskDescriptor(taskId);
		ct.setRemainingTime(newTime);
		this.getConcreteTaskDescriptorDao().saveOrUpdateConcreteTaskDescriptor(
				ct);
	}

	/**
	 * Allows to remove a concreteTaskDescriptor
	 * 
	 * @param _concreteTaskDescriptor
	 */
	public void removeConcreteTaskDescriptor(
			ConcreteTaskDescriptor _concreteTaskDescriptor) {
		this.concreteTaskDescriptorDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_concreteTaskDescriptor);
		this.concreteTaskDescriptorDao.getSessionFactory().getCurrentSession()
				.refresh(_concreteTaskDescriptor);

		// remove dependencies linked to the task descriptor

		// delete all predecessors
		for (ConcreteWorkOrder cwo : _concreteTaskDescriptor
				.getConcretePredecessors()) {
			this.concreteWorkOrderService.deleteConcreteWorkOrder(cwo);
		}

		// delete all successors
		for (ConcreteWorkOrder cwo : _concreteTaskDescriptor
				.getConcreteSuccessors()) {
			this.concreteWorkOrderService.deleteConcreteWorkOrder(cwo);
		}

		// update concrete activity
		for (ConcreteActivity sca : _concreteTaskDescriptor
				.getSuperConcreteActivities()) {
			this.concreteActivityDao.getSessionFactory().getCurrentSession()
					.saveOrUpdate(sca);
			sca.getConcreteBreakdownElements().remove(_concreteTaskDescriptor);
			this.concreteActivityDao.saveOrUpdateConcreteActivity(sca);
		}

		ConcreteRoleDescriptor tmpConcreteRoleDescriptor;
		if (_concreteTaskDescriptor.getMainConcreteRoleDescriptor() != null) {
			tmpConcreteRoleDescriptor = _concreteTaskDescriptor
					.getMainConcreteRoleDescriptor();
			this.concreteRoleDescriptorService.getConcreteRoleDescriptorDao()
					.getSessionFactory().getCurrentSession().saveOrUpdate(
							tmpConcreteRoleDescriptor);
			tmpConcreteRoleDescriptor.getPrimaryConcreteTaskDescriptors()
					.remove(_concreteTaskDescriptor);
			this.concreteRoleDescriptorService
					.saveConcreteRoleDescriptor(tmpConcreteRoleDescriptor);
		}

		TaskDescriptor td = _concreteTaskDescriptor.getTaskDescriptor();
		this.taskDescriptorService.getTaskDescriptorDao().getSessionFactory()
				.getCurrentSession().saveOrUpdate(td);
		td.removeConcreteTaskDescriptor(_concreteTaskDescriptor);
		this.taskDescriptorService.saveTaskDescriptor(td);
		this.taskDescriptorService.getTaskDescriptorDao().getSessionFactory()
				.getCurrentSession().refresh(td);
		this.saveConcreteTaskDescriptor(_concreteTaskDescriptor);
		this.getConcreteTaskDescriptorDao().deleteConcreteTaskDescriptor(
				_concreteTaskDescriptor);
	}
	
	/**
	 * Lance la suppression d'une tâche apres verification que cela est possible
	 * 
	 * @param _concreteTaskDescriptor
	 * @return
	 */
	public ConcreteTaskDescriptor deleteConcreteTaskDescriptor(ConcreteTaskDescriptor _concreteTaskDescriptor){
		//on récupère la tache au cas ou il y a eu des modifications
		_concreteTaskDescriptor = this.getConcreteTaskDescriptor(_concreteTaskDescriptor.getId());
		//s'il n'a pas ete efface et si il n'y a pas de participant affecté a  la tache
		if(_concreteTaskDescriptor != null && getParticipant(_concreteTaskDescriptor) == null){
			//on supprime la tache et on renvoie null
			this.removeConcreteTaskDescriptor(_concreteTaskDescriptor);		
		}else if(getParticipant(_concreteTaskDescriptor) != null){
			//bug : si on ne le fait pas plantage de l'application car il ne trouve pas le nom du participant ...
			getParticipant(_concreteTaskDescriptor).getName();
			//si un participant est affecte, on ne fait rien et on renvoie la nouvelle instance de tache
			return _concreteTaskDescriptor;
		}
		return null;
	}

	/**
	 * Checks if the participant can be affected to a concreteTask (the
	 * participant must be affected to the corresponding concrete role)
	 * 
	 * @param _concreteTaskDescriptor
	 * @param _participant
	 * @return true if the participant can be affected to a concreteTask, false
	 *         in the other case
	 */
	public boolean checkAffectationToRole(
			ConcreteTaskDescriptor _concreteTaskDescriptor,
			Participant _participant) {

		boolean afficher = false;
		_concreteTaskDescriptor = this.getConcreteTaskDescriptor(_concreteTaskDescriptor.getId());
		if (_concreteTaskDescriptor != null) {
			TaskDescriptor tmp = _concreteTaskDescriptor.getTaskDescriptor();
			RoleDescriptor tmpRoleDescriptor;
			// ConcreteTaskDescriptor ctd = new ConcreteTaskDescriptor();
			TaskDescriptor tmp2 = this.taskDescriptorService
					.getTaskDescriptorById(tmp.getId());

			if (tmp2.getMainRole() == null) {
				return true;
			}
			tmpRoleDescriptor = tmp2.getMainRole();
			RoleDescriptor rd = this.roleDescriptorService
					.getRoleDescriptor(tmpRoleDescriptor.getId());
			// recuperation des deux listes.
			// this.roleDescriptorService.
			List<ConcreteRoleDescriptor> crdList = this.concreteRoleDescriptorService
					.getAllConcreteRoleDescriptorForARoleDescriptor(rd.getId());

			// on parcours les deux liste afin de trouver le bon
			// concreteRoledescriptor
			for (ConcreteRoleDescriptor currentCrd : crdList) {

				ConcreteRoleDescriptor crd = this.concreteRoleDescriptorService
						.getConcreteRoleDescriptor(currentCrd.getId());
				if (crd.getParticipant() == null) {
					afficher = false;
				} else {
					if (crd.getParticipant().getId().equals(
							_participant.getId())) {
						// check if the concreteRole and the concreteTask form
						// part
						// of the same concreteActivity
						for (ConcreteActivity cact1 : crd
								.getSuperConcreteActivities()) {
							for (ConcreteActivity cact2 : _concreteTaskDescriptor
									.getSuperConcreteActivities()) {
								if (cact1.getId().equals(cact2.getId())) {
									return true;
								}
							}
						}
					}
				}

			}
		}
		return afficher;

	}

	/**
	 * Checks if the participant IS affected to a concreteTask (the participant
	 * must be affected to the corresponding concrete role)
	 * 
	 * @param _concreteTaskDescriptor
	 * @param _participant
	 * @return true if the participant IS affected to a concreteTask, false in
	 *         the other case
	 */
	public boolean checkAffectationToTask(
			ConcreteTaskDescriptor _concreteTaskDescriptor,
			Participant _participant) {
		this.concreteTaskDescriptorDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_concreteTaskDescriptor);
		return _concreteTaskDescriptor.getIsParticipant();
	}

	/**
	 * When the user click on the button affected.
	 * 
	 * @param _concreteTaskDescriptor
	 */
	public ConcreteTaskDescriptor affectedConcreteTaskDescriptor(
			ConcreteTaskDescriptor _concreteTaskDescriptor, Participant _user) {

		// on récupère la dernière version de la tâche
		_concreteTaskDescriptor = this.getConcreteTaskDescriptor(_concreteTaskDescriptor.getId());
		// on vérifie qu'elle n'a pas ete supprimé
		if (_concreteTaskDescriptor != null) {
			// on vérifie qu'elle n'est pas déjà affecté à quelqu'un
			if (_concreteTaskDescriptor.getMainConcreteRoleDescriptor() == null) {
				RoleDescriptor mainRole = _concreteTaskDescriptor
						.getTaskDescriptor().getMainRole();
				if (mainRole == null) {
					mainRole = this.getConcreteRoleDescriptorService()
							.createOutOfProcessConcreteRoleDescriptor(_user,
									_concreteTaskDescriptor);
				}
				this.roleDescriptorService.getRoleDescriptorDao()
						.saveOrUpdateRoleDescriptor(mainRole);
				Set<ConcreteRoleDescriptor> listeRd = mainRole
						.getConcreteRoleDescriptors();

				// on parcours les deux liste afin de trouver le bon
				// concreteRoledescriptor
				for (ConcreteRoleDescriptor tmpListeRd : listeRd) {
					if (tmpListeRd.getParticipant() != null) {
						if (tmpListeRd.getParticipant().getId().equals(
								_user.getId())) {
							// on fait le lien entre la tache et le role
							_concreteTaskDescriptor.addMainConcreteRoleDescriptor(tmpListeRd);
							// on sauve la tache
							this.saveConcreteTaskDescriptor(_concreteTaskDescriptor);
							// on renvoie la tâche avec l'affectation de la
							// personne
							return _concreteTaskDescriptor;
						}
					}
				}
			} else {
				// bug : si on ne le fait pas plantage de l'application car il
				// ne trouve pas le role responsable ...
				_concreteTaskDescriptor.getTaskDescriptor().getMainRole();
				_concreteTaskDescriptor.getIsParticipant();
				// déjà affecté à quelqu'un on renvoie la tache avec
				// l'affectation de la personne
				return _concreteTaskDescriptor;
			}
		}
		// a été supprimé on renvoie donc null
		return null;
	}

	/**
	 * Suspend a concreteTaskDescriptor and save it into the data base (i.e.
	 * State).
	 * 
	 * @param _concreteTaskDescriptor
	 */
	public void suspendConcreteTaskDescriptor(
			ConcreteTaskDescriptor _concreteTaskDescriptor) {
		this.stateService.updateStateTo(_concreteTaskDescriptor,
				State.SUSPENDED);
	}

	/**
	 * Suspend a concreteTaskDescriptor with its id and save it into the data
	 * base
	 * 
	 * @param id
	 */
	public void suspendConcreteTaskDescriptor(String id) {
		ConcreteTaskDescriptor ct = this.getConcreteTaskDescriptorDao()
				.getConcreteTaskDescriptor(id);
		this.suspendConcreteTaskDescriptor(ct);
	}

	/**
	 * Finish a ConcreteTaskDescriptor and save it into the data base (i.e.
	 * State, realFinishingDate).
	 * 
	 * @param _concreteTaskDescriptor
	 */
	public void finishConcreteTaskDescriptor(
			ConcreteTaskDescriptor _concreteTaskDescriptor) {
		this.stateService
				.updateStateTo(_concreteTaskDescriptor, State.FINISHED);
	}

	/**
	 * Finish a ConcreteTaskDescriptor with its id and save it into the data
	 * base
	 * 
	 * @param id
	 */
	public void finishConcreteTaskDescriptor(String id) {
		ConcreteTaskDescriptor ct = this.getConcreteTaskDescriptorDao()
				.getConcreteTaskDescriptor(id);
		ct.setRemainingTime((float) 0.0);
		this.finishConcreteTaskDescriptor(ct);
	}

	/**
	 * Allows to check if a concreteTaskDescriptor is ready to finish
	 * 
	 * @param _concreteTaskDescriptor
	 * @return true if the concreteTaskDescriptor is ready to finish, false in
	 *         the other case
	 */
	public boolean isConcreteTaskDescriptorFinishable(
			ConcreteTaskDescriptor _concreteTaskDescriptor) {
		return this.stateService
				.isConcreteWorkBreakdownElementFinishable(_concreteTaskDescriptor);
	}

	/**
	 * Allows to save a concreteTaskDescriptor
	 * 
	 * @param _concreteTaskDescriptor
	 */
	public void saveConcreteTaskDescriptor(
			ConcreteTaskDescriptor _concreteTaskDescriptor) {
		this.concreteTaskDescriptorDao
				.saveOrUpdateConcreteTaskDescriptor(_concreteTaskDescriptor);
	}

	/**
	 * Dissociates a concreteTask
	 * 
	 * @param _concreteTaskDescriptor
	 * 
	 */
	public void dissociateConcreteTaskDescriptor(
			ConcreteTaskDescriptor _concreteTaskDescriptor) {
		ConcreteRoleDescriptor cmrd = _concreteTaskDescriptor
				.getMainConcreteRoleDescriptor();

		cmrd.removePrimaryConcreteTaskDescriptor(_concreteTaskDescriptor);
		this.concreteTaskDescriptorDao
				.saveOrUpdateConcreteTaskDescriptor(_concreteTaskDescriptor);
		this.concreteRoleDescriptorService.saveConcreteRoleDescriptor(cmrd);
	}

	/**
	 * Getter of taskDescriptorDao.
	 * 
	 * @return the taskDescriptorDao.
	 */
	public ConcreteTaskDescriptorDao getConcreteTaskDescriptorDao() {
		return this.concreteTaskDescriptorDao;
	}

	/**
	 * Setter of concreteTaskDescriptorDao.
	 * 
	 * @param _concreteTaskDescriptorDao
	 */
	public void setConcreteTaskDescriptorDao(
			ConcreteTaskDescriptorDao _concreteTaskDescriptorDao) {
		this.concreteTaskDescriptorDao = _concreteTaskDescriptorDao;
	}

	/**
	 * Allows to get the roleDescriptorService
	 * 
	 * @return the roleDescriptorService
	 */
	public RoleDescriptorService getRoleDescriptorService() {
		return roleDescriptorService;
	}

	/**
	 * Allows to set the roleDecriptorService
	 * 
	 * @param roleDescriptorService
	 */
	public void setRoleDescriptorService(
			RoleDescriptorService roleDescriptorService) {
		this.roleDescriptorService = roleDescriptorService;
	}

	/**
	 * Allows to get the taskDescriptorService
	 * 
	 * @return the taskDescriptorService
	 */
	public TaskDescriptorService getTaskDescriptorService() {
		return taskDescriptorService;
	}

	/**
	 * Allows to set the taskDescriptorService
	 * 
	 * @param taskDescriptorService
	 */
	public void setTaskDescriptorService(
			TaskDescriptorService taskDescriptorService) {
		this.taskDescriptorService = taskDescriptorService;
	}

	/**
	 * Allows to get the concreteRoleDescriptorService
	 * 
	 * @return the concreteRoleDescriptorService
	 */
	public ConcreteRoleDescriptorService getConcreteRoleDescriptorService() {
		return concreteRoleDescriptorService;
	}

	/**
	 * Allows to set the concreteRoleDescriptorService
	 * 
	 * @param concreteRoleDescriptorService
	 */
	public void setConcreteRoleDescriptorService(
			ConcreteRoleDescriptorService concreteRoleDescriptorService) {
		this.concreteRoleDescriptorService = concreteRoleDescriptorService;
	}

	/**
	 * Allows to get the concreteActivityDao
	 * 
	 * @return the concreteActivityDao
	 */
	public ConcreteActivityDao getConcreteActivityDao() {
		return concreteActivityDao;
	}

	/**
	 * Allows to set the concreteActivityDao
	 * 
	 * @param concreteActivityDao
	 */
	public void setConcreteActivityDao(ConcreteActivityDao concreteActivityDao) {
		this.concreteActivityDao = concreteActivityDao;
	}

	/**
	 * Allows to get the concreteWorkOrderService
	 * 
	 * @return the concreteWorkOrderService
	 */
	public ConcreteWorkOrderService getConcreteWorkOrderService() {
		return concreteWorkOrderService;
	}

	/**
	 * Allows to set the concreteWorkOrderService
	 * 
	 * @param concreteWorkOrderService
	 */
	public void setConcreteWorkOrderService(
			ConcreteWorkOrderService concreteWorkOrderService) {
		this.concreteWorkOrderService = concreteWorkOrderService;
	}

	/**
	 * Allows to get the concreteActivityService
	 * 
	 * @return the concreteActivityService
	 */
	public ConcreteActivityService getConcreteActivityService() {
		return this.concreteActivityService;
	}

	/**
	 * Allows to set the concreteActivityService
	 * 
	 * @param _concreteActivityService
	 */
	public void setConcreteActivityService(
			ConcreteActivityService _concreteActivityService) {
		this.concreteActivityService = _concreteActivityService;
	}

	/**
	 * Allows to get a super concreteActivity for a concreteTaskDescriptor
	 * 
	 * @param _concreteTaskDescriptor
	 * @return concreteActivity
	 */
	public ConcreteActivity getSuperConcreteActivityFromConcreteTaskDescriptor(
			ConcreteTaskDescriptor _concreteTaskDescriptor) {
		this.concreteTaskDescriptorDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_concreteTaskDescriptor);
		return _concreteTaskDescriptor.getSuperConcreteActivity();
	}

	/**
	 * Allows to get the daily remaining time for a concreteTaskDescriptor
	 * 
	 * @param _ctd
	 * @return the daily remaining time
	 */
	public DailyRemainingTime getDailyRemainingTime(ConcreteTaskDescriptor _ctd) {
		Set<DailyRemainingTime> setDailyRemainingTimes = new HashSet<DailyRemainingTime>();
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		Boolean verif = false;
		DailyRemainingTime tempDrt = new DailyRemainingTime();
		Iterator ite;

		this.concreteTaskDescriptorDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_ctd);

		cal2.setTime(new Date());
		cal2 = this.initCalendar(cal2);

		setDailyRemainingTimes = _ctd.getDailyRemainingTimes();
		if (setDailyRemainingTimes != null) {
			ite = setDailyRemainingTimes.iterator();
			while (ite.hasNext() && !verif) {
				tempDrt = (DailyRemainingTime) ite.next();
				cal.setTime(tempDrt.getDate());
				cal = this.initCalendar(cal);
				if (cal.equals(cal2)) {
					verif = true;
				}
			}
			ite = null;
		}
		if (verif) {
			return tempDrt;
		} else {
			return null;
		}
	}

	/**
	 * Allows to initialize a calendar
	 * 
	 * @param _cal
	 * @return the initialized calendar
	 */
	public Calendar initCalendar(Calendar _cal) {
		Calendar cal = _cal;
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	/**
	 * Allows to create a planning for a concreteTaskDescriptor
	 * 
	 * @param _ctd
	 * @return an hashMap planning
	 */
	public HashMap<String, Object> getCreateDate(ConcreteTaskDescriptor _ctd) {
		HashMap<String, Object> tmpHashMap = new HashMap<String, Object>();
		Project project = new Project();

		this.concreteTaskDescriptorDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_ctd);

		project = _ctd.getProject();
		tmpHashMap.put("createDate", project.getCreationDate());
		tmpHashMap.put("finishingDate", project.getPlannedFinishingDate());
		tmpHashMap.put("plannedTime", (float) project.getPlannedTime());
		return tmpHashMap;
	}

	/**
	 * Allows to get the concreteWorkBreakdownElementService
	 * 
	 * @return the concreteWorkBreakdownElementService
	 */
	public ConcreteWorkBreakdownElementService getConcreteWorkBreakdownElementService() {
		return this.concreteWorkBreakdownElementService;
	}

	/**
	 * Allows to set the concreteWorkBreakdownElementService
	 * 
	 * @param _concreteWorkBreakdownElementService
	 */
	public void setConcreteWorkBreakdownElementService(
			ConcreteWorkBreakdownElementService _concreteWorkBreakdownElementService) {
		this.concreteWorkBreakdownElementService = _concreteWorkBreakdownElementService;
	}

	/**
	 * Allows to get the service's state
	 * 
	 * @return the stateService
	 */
	public StateService getStateService() {
		return this.stateService;
	}

	/**
	 * Allows to set the service's state
	 * 
	 * @param _stateService
	 */
	public void setStateService(StateService _stateService) {
		this.stateService = _stateService;
	}

	/**
	 * Allows to check if there is at least one concreteTaskDescriptor for each
	 * mandatory input workProductDescriptors of a task
	 * 
	 * @param _concreteTaskDescriptor
	 * @return true if there is at least one concrete, false in the other case
	 */
	public boolean atLeastOneConcreteForEachMandatoryInputWorkProductOfATask(
			ConcreteTaskDescriptor _concreteTaskDescriptor) {
		this.concreteTaskDescriptorDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_concreteTaskDescriptor);
		boolean b = true;
		Set<WorkProductDescriptor> wpds = _concreteTaskDescriptor
				.getTaskDescriptor().getMandatoryInputWorkProductDescriptors();
		for (WorkProductDescriptor wpd : wpds) {
			if (!this.atLeastOneConcreteForAMandatoryInputWorkProductOfATask(
					_concreteTaskDescriptor, wpd)) {
				b = false;
			}
		}
		return b;
	}

	/**
	 * Allows to check if there is at least one concreteTaskDescriptor for each
	 * mandatory output workProductDescriptors of a task
	 * 
	 * @param _concreteTaskDescriptor
	 * @return true if there is at least one concrete, false in the other case
	 */
	public boolean atLeastOneConcreteForEachOutputWorkProductOfATask(
			ConcreteTaskDescriptor _concreteTaskDescriptor) {
		this.concreteTaskDescriptorDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_concreteTaskDescriptor);
		boolean b = true;
		Set<WorkProductDescriptor> wpds = _concreteTaskDescriptor
				.getTaskDescriptor().getOutputWorkProductDescriptors();
		for (WorkProductDescriptor wpd : wpds) {
			if (!this.atLeastOneConcreteForAOutputWorkProductOfATask(
					_concreteTaskDescriptor, wpd)) {
				b = false;
			}
		}
		return b;
	}

	/** Create the map correspond to a workproduct list emptied */
	private HashMap<String, Object> workProductsOutputListEmptyWork(
			HashMap<String, Object> _hm, boolean _considerState,
			WorkProductDescriptor _outputwpd,
			ConcreteTaskDescriptor _concreteTaskDescriptor) {
		_hm.put("id", _outputwpd.getId());
		_hm.put("isOutput", true);
		String s = _outputwpd.getActivityExitState();
		if (s.equals("")) {
			s = "Aucun";
		}
		_hm.put("desiredOutputState", s);
		_hm.put("isOptionalInput", false);
		_hm.put("isMandatoryInput", false);
		_hm.put("concretename", _outputwpd.getPresentationName());

		if (_considerState) {
			// checking if there is at least one concrete created for
			// outputwpd and if all concrete output of outputwpd
			// are in the good
			// state in order to know if we can finish the task

			boolean bool = ((this
					.checkOutputWorkProductState(_concreteTaskDescriptor)) && (this
					.atLeastOneConcreteForAOutputWorkProductOfATask(
							_concreteTaskDescriptor, _outputwpd)));

			if (bool) {
				_hm.put("readyOutput", CHECKED);
			} else {
				_hm.put("readyOutput", UNCHECKED);
			}
		} else {
			// checking if there is at least one concrete created for
			// outputwpd in order to know if we can finish the task
			if (this.atLeastOneConcreteForAOutputWorkProductOfATask(
					_concreteTaskDescriptor, _outputwpd)) {
				_hm.put("readyOutput", CHECKED);
			}
		}
		return _hm;
	}

	/**
	 * Peruse the list of output workproducts
	 * 
	 * @param _list
	 *            the list of output workproducts
	 * @param _workProducts
	 *            workProducts
	 * @param _concreteTaskDescriptor
	 *            concrete task descriptor
	 * @param _considerState
	 *            the state
	 */
	private void peruseOutputWorkProducts(Set<WorkProductDescriptor> _list,
			List<HashMap<String, Object>> _workProducts,
			ConcreteTaskDescriptor _concreteTaskDescriptor,
			boolean _considerState) {
		for (WorkProductDescriptor outputwpd : _list) {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("checked", CHECKED);
			hm.put("readyManda", UNCHECKED);
			hm.put("readyOutput", UNCHECKED);

			if (_workProducts.size() == 0) {

				_workProducts.add(this.workProductsOutputListEmptyWork(hm,
						_considerState, outputwpd, _concreteTaskDescriptor));

			} else {
				// checking if the product "outputwpd" is not already in the
				// list

				boolean b = false;
				// the variable "i" is use in order to know the position of the
				// object to remove in the list
				int i = 0;
				HashMap<String, Object> hm2 = new HashMap<String, Object>();
				for (HashMap<String, Object> h : _workProducts) {
					if ((h.get("id").equals(outputwpd.getId()))) {
						b = true;
						hm2 = h;
						break;
					}
					++i;
				}

				if (!b) {
					hm.put("id", outputwpd.getId());
					hm.put("isOutput", true);
					String s = outputwpd.getActivityExitState();
					if (s.equals("")) {
						s = "Aucun";
					}
					hm.put("desiredOutputState", s);
					hm.put("isOptionalInput", false);
					hm.put("isMandatoryInput", false);
					hm.put("concretename", outputwpd.getPresentationName());
					if (_considerState) {
						// checking if there is at least one concrete created
						// for
						// outputwpd and if all concrete mandatories of
						// outputwpd
						// are in the good
						// state in order to know if we can start the task

						boolean bool = ((this
								.checkOutputWorkProductState(_concreteTaskDescriptor)) && (this
								.atLeastOneConcreteForAOutputWorkProductOfATask(
										_concreteTaskDescriptor, outputwpd)));

						if (bool) {
							hm.put("readyOutput", CHECKED);
						} else {
							hm.put("readyOutput", UNCHECKED);
						}
					} else {
						// checking if there is at least one concrete created
						// for
						// outputwpd in order to know if we can start the task
						if (this
								.atLeastOneConcreteForAOutputWorkProductOfATask(
										_concreteTaskDescriptor, outputwpd)) {
							hm.put("readyOutput", CHECKED);
						}
					}
					_workProducts.add(hm);
				} else {
					_workProducts.remove(_workProducts.get(i));
					hm2.remove("isOutput");
					hm2.put("isOutput", true);
					String s = outputwpd.getActivityExitState();
					if (s.equals("")) {
						s = "Aucun";
					}
					hm2.put("desiredOutputState", s);
					if (_considerState) {
						// checking if there is at least one concrete created
						// for
						// outputwpd and if all concrete mandatories of
						// outputwpd
						// are in the good
						// state in order to know if we can start the task

						boolean bool = ((this
								.checkOutputWorkProductState(_concreteTaskDescriptor)) && (this
								.atLeastOneConcreteForAOutputWorkProductOfATask(
										_concreteTaskDescriptor, outputwpd)));

						if (bool) {
							hm2.put("readyOutput", CHECKED);
						} else {
							hm2.put("readyOutput", UNCHECKED);
						}
					} else {
						// checking if there is at least one concrete created
						// for
						// outputwpd in order to know if we can start the task
						if (this
								.atLeastOneConcreteForAOutputWorkProductOfATask(
										_concreteTaskDescriptor, outputwpd)) {
							hm2.put("readyOutput", CHECKED);
						}
					}
					_workProducts.add(hm2);
				}
			}
		}
	}

	/**
	 * Peruse the list of optional input workproducts
	 * 
	 * @param _list
	 *            the list of optional input workproducts
	 * @param _workProducts
	 *            workProducts
	 * @param _concreteTaskDescriptor
	 *            concrete task descriptor
	 * @param _considerState
	 *            the state
	 */
	private void peruseOptionalInputWorkProducts(
			Set<WorkProductDescriptor> _list,
			List<HashMap<String, Object>> _workProducts,
			ConcreteTaskDescriptor _concreteTaskDescriptor,
			boolean _considerState) {

		for (WorkProductDescriptor optionalinputwpd : _list) {

			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("checked", CHECKED);

			if (_workProducts.size() == 0) {
				hm.put("id", optionalinputwpd.getId());
				hm.put("isOutput", true);
				hm.put("isOptionalInput", false);
				hm.put("isMandatoryInput", false);
				hm.put("concretename", optionalinputwpd.getPresentationName());
				_workProducts.add(hm);
			} else {
				// checking if the product "optionalinputwpd" is not already in
				// the
				// list
				boolean b = false;
				// the variable "i" is use in order to know the position of the
				// object to remove in the list
				int i = 0;
				HashMap<String, Object> hm2 = new HashMap<String, Object>();
				for (HashMap<String, Object> h : _workProducts) {
					if ((h.get("id").equals(optionalinputwpd.getId()))) {
						b = true;
						hm2 = h;
						break;
					}
					++i;
				}

				if (!b) {
					hm.put("id", optionalinputwpd.getId());
					hm.put("isOutput", false);
					hm.put("isOptionalInput", true);
					hm.put("isMandatoryInput", false);
					hm.put("concretename", optionalinputwpd
							.getPresentationName());
					_workProducts.add(hm);
				} else {
					_workProducts.remove(_workProducts.get(i));
					hm2.remove("isOptionalInput");
					hm2.put("isOptionalInput", true);
					_workProducts.add(hm2);

				}
			}
		}
	}

	/** Create the map correspond to a workproduct list emptied */
	private HashMap<String, Object> workProductsMandatoryInputListEmptyWork(
			HashMap<String, Object> _hm, boolean _considerState,
			WorkProductDescriptor _mandatoryinputwpd,
			ConcreteTaskDescriptor _concreteTaskDescriptor) {

		_hm.put("id", _mandatoryinputwpd.getId());
		_hm.put("isOutput", false);
		_hm.put("isOptionalInput", false);
		_hm.put("isMandatoryInput", true);
		String s = _mandatoryinputwpd.getActivityEntryState();
		if (s.equals("")) {
			s = "Aucun";
		}
		_hm.put("desiredMandaState", s);
		_hm.put("concretename", _mandatoryinputwpd.getPresentationName());
		if (_considerState) {
			// checking if there is at least one concrete created
			// for
			// mandatoryinputwpd and if all concrete mandatories of
			// mandatoryinputwpd
			// are in the good
			// state in order to know if we can start the task

			boolean bool = ((this
					.checkMandatoryWorkProductState(_concreteTaskDescriptor)) && (this
					.atLeastOneConcreteForAMandatoryInputWorkProductOfATask(
							_concreteTaskDescriptor, _mandatoryinputwpd)));

			if (bool) {
				_hm.put("readyManda", CHECKED);
			} else {
				_hm.put("readyManda", UNCHECKED);
			}
		} else {
			// checking if there is at least one concrete created
			// for
			// mandatoryinputwpd in order to know if we can start the
			// task
			if (this.atLeastOneConcreteForAMandatoryInputWorkProductOfATask(
					_concreteTaskDescriptor, _mandatoryinputwpd)) {
				_hm.put("readyManda", CHECKED);
			}
		}

		return _hm;
	}

	{

	}

	/**
	 * Peruse the list of mandatory input workproducts
	 * 
	 * @param _list
	 *            the list of mandatory input workproducts
	 * @param _workProducts
	 *            workProducts
	 * @param _concreteTaskDescriptor
	 *            concrete task descriptor
	 * @param _considerState
	 *            the state
	 */
	private void peruseMandatoryInputWorkProducts(
			Set<WorkProductDescriptor> _list,
			List<HashMap<String, Object>> _workProducts,
			ConcreteTaskDescriptor _concreteTaskDescriptor,
			boolean _considerState) {
		for (WorkProductDescriptor mandatoryinputwpd : _list) {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("readyManda", UNCHECKED);
			hm.put("readyOutput", UNCHECKED);
			hm.put("checked", CHECKED);

			if (_workProducts.size() == 0) {
				hm = this.workProductsMandatoryInputListEmptyWork(hm,
						_considerState, mandatoryinputwpd,
						_concreteTaskDescriptor);
				_workProducts.add(hm);

			} else {
				// checking if the product "mandatoryinputwpd" is not already in
				// the
				// list

				boolean b = false;
				// the variable "i" is use in order to know the position of the
				// object to remove in the list
				int i = 0;
				HashMap<String, Object> hm2 = new HashMap<String, Object>();

				for (HashMap<String, Object> h : _workProducts) {
					if ((h.get("id").equals(mandatoryinputwpd.getId()))) {
						b = true;
						hm2 = h;
						break;
					}
					++i;
				}

				if (!b) {
					hm.put("id", mandatoryinputwpd.getId());
					hm.put("isOutput", false);
					hm.put("isOptionalInput", false);
					hm.put("isMandatoryInput", true);
					String s = mandatoryinputwpd.getActivityEntryState();
					if (s.equals("")) {
						s = "Aucun";
					}
					hm.put("desiredMandaState", s);
					hm.put("concretename", mandatoryinputwpd
							.getPresentationName());

					if (_considerState) {
						// checking if there is at least one concrete created
						// for
						// mandatoryinputwpd and if all concrete mandatories of
						// mandatoryinputwpd
						// are in the good
						// state in order to know if we can start the task

						boolean bool = ((this
								.checkMandatoryWorkProductState(_concreteTaskDescriptor)) && (this
								.atLeastOneConcreteForAMandatoryInputWorkProductOfATask(
										_concreteTaskDescriptor,
										mandatoryinputwpd)));

						if (bool) {
							hm.put("readyManda", CHECKED);
						} else {
							hm.put("readyManda", UNCHECKED);
						}
					} else {
						// checking if there is at least one concrete created
						// for
						// mandatoryinputwpd in order to know if we can start
						// the task
						if (this
								.atLeastOneConcreteForAMandatoryInputWorkProductOfATask(
										_concreteTaskDescriptor,
										mandatoryinputwpd)) {
							hm.put("readyManda", CHECKED);
						}
					}
					_workProducts.add(hm);

				} else {
					_workProducts.remove(_workProducts.get(i));
					hm2.remove("isMandatoryInput");
					hm2.put("isMandatoryInput", true);
					String s = mandatoryinputwpd.getActivityEntryState();
					if (s.equals("")) {
						s = "Aucun";
					}
					hm2.put("desiredMandaState", s);
					if (_considerState) {
						// checking if there is at least one concrete created
						// for
						// mandatoryinputwpd and if all concrete mandatories of
						// mandatoryinputwpd
						// are in the good
						// state in order to know if we can start the task

						boolean bool = ((this
								.checkMandatoryWorkProductState(_concreteTaskDescriptor)) && (this
								.atLeastOneConcreteForAMandatoryInputWorkProductOfATask(
										_concreteTaskDescriptor,
										mandatoryinputwpd)));

						if (bool) {
							hm2.put("readyManda", CHECKED);
						} else {
							hm2.put("readyManda", UNCHECKED);
						}

					} else {
						// checking if there is at least one concrete created
						// for
						// mandatoryinputwpd in order to know if we can start
						// the task
						if (this
								.atLeastOneConcreteForAMandatoryInputWorkProductOfATask(
										_concreteTaskDescriptor,
										mandatoryinputwpd)) {
							hm2.put("readyManda", CHECKED);
						}
					}
					_workProducts.add(hm2);
				}
			}
		}

	}

	/**
	 * Allows to get the list of workProducts for a concreteTask
	 * 
	 * @param _concreteTaskDescriptor
	 * @param _considerState
	 * @return the list of workProducts by hashMap
	 */
	public List<HashMap<String, Object>> getWorkProductsForAConcreteTask(
			ConcreteTaskDescriptor _concreteTaskDescriptor,
			boolean _considerState) {

		TaskDescriptor _td = _concreteTaskDescriptor.getTaskDescriptor();

		List<HashMap<String, Object>> _workProducts = new ArrayList<HashMap<String, Object>>();

		// peruse the list of output work products
		this.peruseOutputWorkProducts(this.taskDescriptorService
				.getOutputWorkProducts(_td), _workProducts,
				_concreteTaskDescriptor, _considerState);

		// peruse the list of optional input workproduct
		this.peruseOptionalInputWorkProducts(this.taskDescriptorService
				.getOptionalInputWorkProducts(_td), _workProducts,
				_concreteTaskDescriptor, _considerState);

		// peruse the list of mandatory input workproduct
		this.peruseMandatoryInputWorkProducts(this.taskDescriptorService
				.getMandatoryInputWorkProducts(_td), _workProducts,
				_concreteTaskDescriptor, _considerState);

		return _workProducts;
	}

	/**
	 * Allows to check the mandatory state of a workProduct for a
	 * concreteTaskDescriptor
	 * 
	 * @param _concreteWorkProductDescriptors
	 * @return true if it's right, false in the other case
	 * 
	 */
	public boolean checkMandatoryWorkProductState(
			ConcreteTaskDescriptor _concreteTaskDescriptor) {
		this.concreteTaskDescriptorDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_concreteTaskDescriptor);
		Set<ConcreteWorkProductDescriptor> _concreteWorkProductDescriptors = _concreteTaskDescriptor
				.getMandatoryInputConcreteWorkProductDescriptors();
		for (ConcreteWorkProductDescriptor cwpd : _concreteWorkProductDescriptors) {
			String cwpdState = cwpd.getState();
			String cwpdAcitvityEntryState = cwpd.getWorkProductDescriptor()
					.getActivityEntryState();
			if (!(cwpdAcitvityEntryState.equals("") || cwpdState
					.equals(cwpdAcitvityEntryState))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Allows to check the output workProduct state
	 * 
	 * @param _concreteWorkProductDescriptors
	 * @return true if it's right, false in the other case
	 * 
	 */
	public boolean checkOutputWorkProductState(
			ConcreteTaskDescriptor _concreteTaskDescriptor) {
		this.concreteTaskDescriptorDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_concreteTaskDescriptor);
		Set<ConcreteWorkProductDescriptor> cwpds = _concreteTaskDescriptor
				.getOutputConcreteWorkProductDescriptors();
		for (ConcreteWorkProductDescriptor cwpd : cwpds) {
			String cwpdState = cwpd.getState();
			String cwpdActivityExitState = cwpd.getWorkProductDescriptor()
					.getActivityExitState();
			if (!(cwpdActivityExitState.equals("") || cwpdState
					.equals(cwpdActivityExitState))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Allows to reload a conreteTaskDescriptor
	 * 
	 * @param _ctd
	 * @return the new hashMap
	 */
	public HashMap<String, Object> reloadCtd(ConcreteTaskDescriptor _ctd) {
		HashMap<String, Object> hashmap = new HashMap<String, Object>();

		this.saveConcreteTaskDescriptor(_ctd);
		hashmap.put("Finishing", _ctd.getRealFinishingDate());
		hashmap.put("Starting", _ctd.getRealStartingDate());
		return hashmap;
	}

	/**
	 * Allows to check if there is at least one concreteTaskDescriptor for an
	 * input workProductDescriptors of a task
	 * 
	 * @param _concreteTaskDescriptor
	 * @return true if there is at least one concrete, false in the other case
	 */
	public boolean atLeastOneConcreteForAMandatoryInputWorkProductOfATask(
			ConcreteTaskDescriptor _concreteTaskDescriptor,
			WorkProductDescriptor _workProductDescriptor) {
		this.concreteTaskDescriptorDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_concreteTaskDescriptor);
		this.workProductDescriptorDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_workProductDescriptor);

		Set<ConcreteWorkProductDescriptor> cwpds = _workProductDescriptor
				.getConcreteWorkProductDescriptors();
		for (ConcreteWorkProductDescriptor cwpd : cwpds) {
			for (ConcreteTaskDescriptor ctd : cwpd
					.getMandatoryUserConcreteTasks()) {
				if (ctd.getId().equals(_concreteTaskDescriptor.getId())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Allows to check if there is at least one concreteTaskDescriptor for an
	 * output workProductDescriptors of a task
	 * 
	 * @param _concreteTaskDescriptor
	 * @return true if there is at least one concrete, false in the other case
	 */
	public boolean atLeastOneConcreteForAOutputWorkProductOfATask(
			ConcreteTaskDescriptor _concreteTaskDescriptor,
			WorkProductDescriptor _workProductDescriptor) {
		this.concreteTaskDescriptorDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_concreteTaskDescriptor);
		this.workProductDescriptorDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_workProductDescriptor);
		Set<ConcreteWorkProductDescriptor> cwpds = _workProductDescriptor
				.getConcreteWorkProductDescriptors();
		for (ConcreteWorkProductDescriptor cwpd : cwpds) {
			for (ConcreteTaskDescriptor ctd : cwpd.getProducerConcreteTasks()) {
				if (ctd.getId().equals(_concreteTaskDescriptor.getId())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Allows to get the workProductDescriptorDao
	 * 
	 * @return the workProductDescriptorDao
	 */
	public WorkProductDescriptorDao getWorkProductDescriptorDao() {
		return this.workProductDescriptorDao;
	}

	/**
	 * Allows to set the workProductDescriptorDao
	 * 
	 * @param _workProductDescriptorDao
	 */
	public void setWorkProductDescriptorDao(
			WorkProductDescriptorDao _workProductDescriptorDao) {
		this.workProductDescriptorDao = _workProductDescriptorDao;
	}

	/**
	 * Return the project associated to the id
	 * 
	 * @param _id
	 *            the id of the project
	 * @return the project
	 */
	public Project getProject(String _id) {
		return this.concreteWorkBreakdownElementService.getProject(_id);
	}

	/**
	 * Allows to get the participant for a concreteTaskDescriptor
	 * 
	 * @param _concreteTaskDescriptor
	 * @return the participant
	 */
	public Participant getParticipant(
			ConcreteTaskDescriptor _concreteTaskDescriptor) {
		this.concreteTaskDescriptorDao.getSessionFactory().getCurrentSession()
				.saveOrUpdate(_concreteTaskDescriptor);
		Participant parti = null;
		if (_concreteTaskDescriptor.getMainConcreteRoleDescriptor() != null) {
			parti = _concreteTaskDescriptor.getMainConcreteRoleDescriptor()
					.getParticipant();
		}

		return (parti);
	}
}
