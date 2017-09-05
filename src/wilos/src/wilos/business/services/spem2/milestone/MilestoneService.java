/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

package wilos.business.services.spem2.milestone;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.business.services.misc.concreteactivity.ConcreteActivityService;
import wilos.hibernate.misc.concretemilestone.ConcreteMilestoneDao;
import wilos.hibernate.spem2.milestone.MilestoneDao;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretemilestone.ConcreteMilestone;
import wilos.model.misc.project.Project;
import wilos.model.spem2.milestone.Milestone;

/**
 * MilestoneService is a transactional class, that manages operations about
 * milestone, requested by web pages
 * 
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class MilestoneService {

	// private ProjectDao projectDao;

	private ConcreteActivityService concreteActivityService;

	private MilestoneDao milestoneDao;

	// TODO : generate getters and setters, and update spring_services.xml
	// private ConcreteMilestoneService concreteMilestoneService;

	private ConcreteMilestoneDao concreteMilestoneDao;

	/**
	 * Instanciates a milestone for a project
	 * 
	 * @param _project
	 *            project for which the milestone shall be instanciated
	 * @param _milestone
	 *            milestone to instanciate
	 */
	public void milestoneInstanciation(Project _project, Milestone _milestone,
			ConcreteActivity _cact, int _occ, int _dispOrd) {

		if (_occ > 0) {

			this.concreteActivityService.getConcreteActivityDao()
					.getSessionFactory().getCurrentSession()
					.saveOrUpdate(_cact);

			ConcreteMilestone cmi = new ConcreteMilestone();

			if (_milestone.getPresentationName().equals(""))
				cmi.setConcreteName(_milestone.getName());
			else
				cmi.setConcreteName(_milestone.getPresentationName());

			cmi.setMilestone(_milestone);
			cmi.setProject(_project);
			cmi.setBreakdownElement(_milestone);
			cmi.setWorkBreakdownElement(_milestone);
			cmi.setInstanciationOrder(1);
			cmi.addSuperConcreteActivity(_cact);
			cmi.setDisplayOrder(cmi.getSuperConcreteActivity().getDisplayOrder()+ Integer.toString(_dispOrd));

			// this.concreteMilestoneService.saveConcreteMilestone(cmi);
			this.concreteMilestoneDao.saveOrUpdateConcreteMilestone(cmi);
			System.out.println("### ConcreteMilestone sauve");
		}
	}

	/**
	 * 
	 * @param _project
	 * @param _phase
	 * @param _cact
	 * @param _list
	 * @param _occ
	 * @param _isInstanciated
	 */
	/*
	 * public void milestoneUpdate(Project _project, Activity _act, Set<ConcreteActivity>
	 * _cacts, List<HashMap<String, Object>> _list, int _occ) { // one
	 * concretephase at least to insert in all attached // concreteactivities of
	 * the parent of _phase if (_occ > 0) { for (ConcreteActivity tmp : _cacts) {
	 * this.activityInstanciation(_project, _act, tmp, _list, _occ, true);
	 * 
	 * if (tmp instanceof Project) { Project pj = (Project) tmp;
	 * this.projectDao.saveOrUpdateProject(pj); } else { if (tmp instanceof
	 * ConcretePhase) { ConcretePhase cph = (ConcretePhase) tmp;
	 * this.concretePhaseDao.saveOrUpdateConcretePhase(cph); } else { if (tmp
	 * instanceof ConcreteIteration) { ConcreteIteration cit =
	 * (ConcreteIteration) tmp; this.concreteIterationDao
	 * .saveOrUpdateConcreteIteration(cit); } else { if (tmp instanceof
	 * ConcreteActivity) { ConcreteActivity cact = (ConcreteActivity) tmp;
	 * this.concreteActivityService .saveConcreteActivity(cact); } } } }
	 * 
	 * this.concreteActivityService.saveConcreteActivity(tmp);
	 * System.out.println("### ConcreteActivity update"); } } else { // diving
	 * in all the concreteBreakdownElements to looking for update Set<BreakdownElement>
	 * bdes = new HashSet<BreakdownElement>();
	 * bdes.addAll(this.getAllBreakdownElements(_act));
	 * 
	 * Set<ConcreteActivity> cacts = new HashSet<ConcreteActivity>();
	 * cacts.addAll(this.getAllConcreteActivitiesForAProject(_act, _project));
	 * 
	 * for (BreakdownElement bde : bdes) { if (bde instanceof Activity) {
	 * Activity act = (Activity) bde; int occ =
	 * this.giveNbOccurences(act.getId(), _list, true);
	 * this.activityUpdate(_project, act, cacts, _list, occ); } else { if (bde
	 * instanceof TaskDescriptor) { TaskDescriptor td = (TaskDescriptor) bde;
	 * int occ = this .giveNbOccurences(td.getId(), _list, true);
	 * this.taskDescriptorService.taskDescriptorUpdate( _project, td, cacts,
	 * occ); } } } } }
	 */

	/**
	 * 
	 * @param _id
	 * @param list
	 * @return
	 */
	/*
	 * private int giveNbOccurences(String _id, List<HashMap<String, Object>>
	 * list, boolean _isInstanciated) {
	 * 
	 * int nb; if (!_isInstanciated) nb = 1; else nb = 0;
	 * 
	 * for (HashMap<String, Object> hashMap : list) { if (((String)
	 * hashMap.get("id")).equals(_id)) { nb = ((Integer)
	 * hashMap.get("nbOccurences")).intValue(); break; } }
	 * 
	 * return nb; }
	 * 
	 * public Set<ConcreteActivity> getAllConcreteActivities(Activity _act) {
	 * Set<ConcreteActivity> tmp = new HashSet<ConcreteActivity>();
	 * this.activityDao.getSessionFactory().getCurrentSession().saveOrUpdate(
	 * _act); for (ConcreteActivity bde : _act.getConcreteActivities()) {
	 * tmp.add(bde); } return tmp; }
	 * 
	 * public Set<ConcreteActivity> getAllConcreteActivitiesForAProject(
	 * Activity _act, Project _project) { Set<ConcreteActivity> tmp = new
	 * HashSet<ConcreteActivity>();
	 * this.activityDao.getSessionFactory().getCurrentSession().saveOrUpdate(
	 * _act);
	 * 
	 * for (ConcreteActivity cact : _act.getConcreteActivities()) { if
	 * (cact.getProject().getId().equals(_project.getId())) tmp.add(cact); }
	 * return tmp; }
	 */

	/**
	 * @return the milestoneDao
	 */
	public MilestoneDao getMilestoneDao() {
		return this.milestoneDao;
	}

	/**
	 * @param _milestoneDao
	 *            the milestoneDao to set
	 */
	public void setMilestoneDao(MilestoneDao _milestoneDao) {
		this.milestoneDao = _milestoneDao;
	}

	/**
	 * @return the concreteActivityService
	 */
	public ConcreteActivityService getConcreteActivityService() {
		return concreteActivityService;
	}

	/**
	 * @param _concreteActivityService
	 *            the concreteActivityService to set
	 */
	public void setConcreteActivityService(
			ConcreteActivityService _concreteActivityService) {
		concreteActivityService = _concreteActivityService;
	}

	/**
	 * @return the concreteMilestoneDao
	 */
	public ConcreteMilestoneDao getConcreteMilestoneDao() {
		return concreteMilestoneDao;
	}

	/**
	 * @param _concreteMilestoneDao
	 *            the concreteMilestoneDao to set
	 */
	public void setConcreteMilestoneDao(
			ConcreteMilestoneDao _concreteMilestoneDao) {
		concreteMilestoneDao = _concreteMilestoneDao;
	}
}
