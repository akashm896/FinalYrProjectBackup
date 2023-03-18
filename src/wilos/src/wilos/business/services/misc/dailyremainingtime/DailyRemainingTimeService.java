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

package wilos.business.services.misc.dailyremainingtime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.business.services.misc.concretetask.ConcreteTaskDescriptorService;
import wilos.hibernate.misc.dailyremainingtime.DailyRemainingTimeDao;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.dailyremainingtime.DailyRemainingTime;

@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class DailyRemainingTimeService {

	private DailyRemainingTimeDao dailyRemainingTimeDao;
	//private int i;

	/**
	 * Allows to save the daily remaining time
	 * 
	 * @param _drt
	 */
	public void saveDailyRemainingTime(DailyRemainingTime _drt) {
		this.dailyRemainingTimeDao.saveOrUpdateDailyRemainingTime(_drt);
	}

	/**
	 * Allows to get the remaining by day
	 * 
	 * @param _d
	 * @param _ctd
	 * @return the remaining by day
	 */
	public Float getRemainingByDay(Date _d, ConcreteTaskDescriptor _ctd) {

		boolean b = false;
		float res = 0;
		DailyRemainingTime drt = new DailyRemainingTime();
		Iterator ite;
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		List<DailyRemainingTime> listDrt = new ArrayList<DailyRemainingTime>();

		listDrt = this.dailyRemainingTimeDao.getDailyRemainingTime();
		cal.setTime(_d);
		cal = this.initCalendar(cal);
		if (listDrt.size() > 0) {
			ite = listDrt.iterator();
			while (ite.hasNext() && !b) {
				drt = (DailyRemainingTime) ite.next();
				this.saveDailyRemainingTime(drt);
				if (_ctd.getId()
						.equals(drt.getDailyTaskDescriptor_id().getId())) {
					cal2.setTime(drt.getDate());
					cal2 = this.initCalendar(cal2);
					if (cal.equals(cal2)) {
						res = drt.getRemainingTime();
						b = true;
					}
				}
			}
		}
		return res;
	}

	/**
	 * Allows to get the remaining time of a project for a day
	 * 
	 * @param _d
	 * @param _projectId
	 * @param listCtd
	 *            list of all concrete task descriptor of project
	 * @return the remaining time
	 */
	public Float getRemainingTimeForDay(Date _d, List<ConcreteTaskDescriptor> _listCtd) {
		float res = 0;
		List<DailyRemainingTime> listDrt = new ArrayList<DailyRemainingTime>();
		listDrt = this.dailyRemainingTimeDao.getDailyRemainingTime();
		if (listDrt.size() > 0) {
			res += this.peruseDailyRemaningTimeList(listDrt, _listCtd, _d);
		}
		return res;
	}
	
	/**
	 * Calculate the remaning time
	 * 
	 * @param _listDrt
	 * @param _listCtd
	 * @param _projectId
	 * @param _verif
	 * @param _cal
	 * @param _cal2
	 * @param _current
	 * @param _starting
	 * @param _d
	 * @return
	 */
	private float peruseDailyRemaningTimeList(
			List<DailyRemainingTime> _listDrt,
			List<ConcreteTaskDescriptor> _listCtd, Date _d) {
		List<String> lst_ctd_id = new ArrayList<String>();
		for (ConcreteTaskDescriptor ctd : _listCtd){
			if (! lst_ctd_id.contains(ctd.getId())){
				lst_ctd_id.add(ctd.getId());
			}
		}
		float remainingTime = 0;
		Date drt_date = null;
		List<String> lst_ctd_loaded = new ArrayList<String>();
		for (DailyRemainingTime drt : _listDrt) {
			drt_date = drt.getDate();
			//if the drt date is before the current Date
			if (isAfter(_d, drt_date) || isEquals(_d, drt_date)) {
				String ctd_id = drt.getDailyTaskDescriptor_id().getId();
				//if the ctdId is in the list of project'ctd
				if (lst_ctd_id.contains(ctd_id)) {
					//crt de ls_drt appartient au projet
					// si la date est la dernière avant la date t = si la modification est la dernière modif pour cette ctd
					String drt_id = getLastIdForDate(_listDrt, ctd_id, _d);
					if (drt.getId().equals(drt_id)) {
						remainingTime += drt.getRemainingTime();
						lst_ctd_loaded.add(ctd_id);
					} else {
						// pas la dernière : on ne fait rien !
					}
				}
			}
		}
		// on a la liste des id des ctd en cours à la date t
		for (ConcreteTaskDescriptor ctd : _listCtd){
			if (! lst_ctd_loaded.contains(ctd.getId())){
				Date started = ctd.getRealStartingDate();
				Date finished = ctd.getRealFinishingDate();
				//if is finished at the date _d
				if (finished != null && (isAfter(_d, finished) || isEquals(_d, finished)))
				{
					remainingTime += 0 ;
				}
				else{
					//if is not started at the date _d or not started at all 
					if (started == null || (started != null && (isBefore(_d, started))|| isEquals(_d, started)) ){
						remainingTime += ctd.getPlannedTime();
					}
				}
					//else : ctd normalement comptee ! ! !
			}
		}
		return remainingTime;
	}
	
	private String getLastIdForDate(List<DailyRemainingTime> _lst_drt,
			String ctd_id, Date t) {
		Date time = null;
		String id = null;
		Date d = null;
		for (DailyRemainingTime drt : _lst_drt) {
			//if it is the drt concerned
			if (drt.getDailyTaskDescriptor_id().getId().equals(ctd_id)) {
				//initialize
				if (time == null) {
					time = drt.getDate();
					id = drt.getId();
				} else {
					d = drt.getDate();
					// if 
					if ((isAfter(d, time)||isEquals(d, time)) 
							&& ((isBefore(d, t)||isEquals(d, t)))) {
						time = drt.getDate();
						id = drt.getId();
					}
				}
			}
		}
		return id;
	}

	/**
	 * return true if start date is after _d date
	 * @param start
	 * @param _d
	 * @return
	 */
	private boolean isAfter(Date start, Date _d) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(start);
		this.initCalendar(c1);
		c2.setTime(_d);
		this.initCalendar(c2);
		return c1.after(c2);
	}
	
	/**
	 * return true if start date is before _d date
	 * @param start
	 * @param _d
	 * @return
	 */
	private boolean isBefore(Date start, Date _d) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(start);
		this.initCalendar(c1);
		c2.setTime(_d);
		this.initCalendar(c2);
		return c1.before(c2);
	}
	
	/**
	 * return true if start date is equals _d date
	 * @param start
	 * @param _d
	 * @return
	 */
	private boolean isEquals(Date start, Date _d) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(start);
		this.initCalendar(c1);
		c2.setTime(_d);
		this.initCalendar(c2);
		return c1.equals(c2);
	}

	/**
	 * Allows to generate the slope curve
	 * 
	 * @param _beginningDate
	 * @param _endDateEstimate
	 * @param _remainingTime
	 * @return the remaining time in per cent
	 */
	public double generateSlopeCurve(Date _beginningDate,
			Date _endDateEstimate, float _remainingTime) {

		double nbDay = 0;
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();

		cal1.setTime(_beginningDate);
		cal2.setTime(_endDateEstimate);
		cal1 = this.initCalendar(cal1);
		cal2 = this.initCalendar(cal2);
		nbDay = ((cal2.getTime().getTime() - cal1.getTime().getTime()) / (24 * 60 * 60 * 1000));
		return (_remainingTime / (nbDay - 1));
	}

	/**
	 * Allows to get the remainingTimeDao
	 * 
	 * @return the remainingTimeDao
	 */
	public DailyRemainingTimeDao getDailyRemainingTimeDao() {
		return dailyRemainingTimeDao;
	}

	/**
	 * Allows to set the remainingTimeDao
	 * 
	 * @param _dailyRemainingTimeDao
	 */
	public void setDailyRemainingTimeDao(
			DailyRemainingTimeDao _dailyRemainingTimeDao) {
		dailyRemainingTimeDao = _dailyRemainingTimeDao;
	}

	/**
	 * Allows to initialize the calendar
	 * 
	 * @param _cal
	 * @return the calendar initialized
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
	 * Get the remainingTime for a task at a fixed date
	 * 
	 * @param _concreteTaskDescriptorId
	 * @param date
	 * @return the remainingTime
	 */
	public Float getAllDailyRemainingTimeForATask(Date _d,
			String _concreteTaskDescriptorId) {
		float res = 0;
		boolean flag = false;
		String concreteTaskDescriptorId;
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		Calendar cal3 = Calendar.getInstance();
		List<DailyRemainingTime> tempDrt = new ArrayList<DailyRemainingTime>();
		List<DailyRemainingTime> listDrt = new ArrayList<DailyRemainingTime>();
		// load of all line in the table dailyremainingtime
		listDrt = this.dailyRemainingTimeDao.getDailyRemainingTime();
		// use of a calendar to play with date
		cal.setTime(_d);
		cal = this.initCalendar(cal);
		// if the method getDailyRemainingTime return some rows
		if (listDrt.size() > 0) {
			// for each
			for (DailyRemainingTime drt : listDrt) {
				this.saveDailyRemainingTime(drt);
				concreteTaskDescriptorId = drt.getDailyTaskDescriptor_id()
						.getId();
				// we look if concreteTaskDescriptor are the
				// same
				if (concreteTaskDescriptorId.equals(_concreteTaskDescriptorId)) {
					tempDrt.add(drt);
					cal3.setTime(drt.getDailyTaskDescriptor_id()
							.getRealStartingDate());
					cal3 = this.initCalendar(cal3);
				}
			}
			if (tempDrt.size() > 0) {
				for (DailyRemainingTime tmpdrt : tempDrt) {
					this.saveDailyRemainingTime(tmpdrt);
					cal2.setTime(tmpdrt.getDate());
					cal2 = this.initCalendar(cal2);
					if (cal2.equals(cal)) {
						flag = true;
						res = tmpdrt.getRemainingTime();
					}
				}
				if (flag == false) {
					while (flag == false && cal.after(cal3)) {
						cal.add(Calendar.DATE, -1);
						for (DailyRemainingTime tmpdrt : tempDrt) {
							this.saveDailyRemainingTime(tmpdrt);
							cal2.setTime(tmpdrt.getDate());
							cal2 = this.initCalendar(cal2);
							if (cal2.equals(cal)) {
								flag = true;
								res = tmpdrt.getRemainingTime();
							}
						}

					}
				}

			}
			return res;

		} else {
			return null;
		}
	}
}
