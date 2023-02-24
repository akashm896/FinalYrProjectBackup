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

package wilos.presentation.web.viewer;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.krysalis.jcharts.chartData.ChartDataException;

import com.icesoft.faces.component.outputchart.OutputChart;

import wilos.business.services.misc.concreteactivity.ConcreteActivityService;
import wilos.business.services.misc.concretebreakdownelement.ConcreteBreakdownElementService;
import wilos.business.services.misc.concretetask.ConcreteTaskDescriptorService;
import wilos.business.services.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElementService;
import wilos.business.services.misc.dailyremainingtime.DailyRemainingTimeService;
import wilos.business.services.misc.project.ProjectService;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concretemilestone.ConcreteMilestone;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.dailyremainingtime.DailyRemainingTime;
import wilos.model.misc.project.Project;
import wilos.model.misc.wilosuser.Participant;
import wilos.model.misc.wilosuser.WilosUser;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;
import wilos.resources.LocaleBean;

public class DailyRemainingTimeViewerBean {

	private String referenceRemainingTime = LocaleBean
			.getText("dailyremainingtime.noRefRemainingTime");
	private DailyRemainingTimeService dailyRemainingTimeService;
	private ConcreteTaskDescriptorService concreteTaskDescriptorService;
	private List<Object> date = new ArrayList<Object>();
	private List<Color> color = new ArrayList<Color>();
	private String chartTitle = "";
	private boolean activitySelected = false;
	private String selectedConcreteActivityId = "default";
	private ConcreteBreakdownElementService concreteBreakdownElementService;
	private ConcreteActivityService concreteActivityService;
	private ProjectService projectService;
	private String type = OutputChart.LINE_CHART_TYPE;// .POINT_CHART_TYPE;//.AREA_CHART_TYPE;//
	// OutputChart.LINE_CHART_TYPE;
	private List<String> legendLabels = new ArrayList<String>();

	private String cbdeId;

	public String getCbdeId() {
		return cbdeId;
	}

	public DailyRemainingTimeViewerBean() {
		String projectId = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);
		this.cbdeId = projectId;
	}

	public void setCbdeId(String cbdeId) {
		this.cbdeId = cbdeId;
		WebSessionService.setAttribute(
				WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT, this.cbdeId);
		/*
		 * this.getCurrentRemainingTime(); this.getRefRemainingTime();
		 */
		this.getCurrentRemainingTime();
		// this.getRemainingTimeDate();
	}

	/**
	 * Method to have the DailyRemainingTimeService attribute
	 * 
	 * @return a dayliRemainingTimeService
	 */
	public DailyRemainingTimeService getDailyRemainingTimeService() {
		return dailyRemainingTimeService;
	}

	/**
	 * Method to set the DailyRemainingTimeService attribute
	 * 
	 * @param _dailyRemainingTimeService
	 */
	public void setDailyRemainingTimeService(
			DailyRemainingTimeService _dailyRemainingTimeService) {
		dailyRemainingTimeService = _dailyRemainingTimeService;
	}

	/**
	 * Method to have ConcreteTaskDescriptorService attribute
	 * 
	 * @return
	 */
	public ConcreteTaskDescriptorService getConcreteTaskDescriptorService() {
		return concreteTaskDescriptorService;
	}

	/**
	 * Method to set the ConcreteTaskDescriptorService attribute
	 * 
	 * @param _concreteTaskDescriptorService
	 */
	public void setConcreteTaskDescriptorService(
			ConcreteTaskDescriptorService _concreteTaskDescriptorService) {
		concreteTaskDescriptorService = _concreteTaskDescriptorService;
	}

	/**
	 * Method to set the date List
	 * 
	 * @param _date
	 *            object list
	 */
	public void setDate(List<Object> _date) {
		date = _date;

	}

	/**
	 * Method to known the reference of remaining time
	 * 
	 * @return string
	 */
	public String getRefRemainingTime() {
		float plannedTime = 0;
		String mess = "";
		this.setCbdeId((String) WebSessionService
				.getAttribute(WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT));

		ConcreteBreakdownElement cbde = new ConcreteBreakdownElement();

		cbde = this.getConcreteBreakdownElementService()
				.getConcreteBreakdownElementDao().getConcreteBreakdownElement(
						this.cbdeId);

		if (cbde instanceof Project) {
			plannedTime = ((Project) cbde).getPlannedTime();
		} else if (cbde instanceof ConcreteTaskDescriptor) {
			plannedTime = ((ConcreteTaskDescriptor) cbde).getPlannedTime();
		} else if (cbde instanceof ConcreteActivity) {
			plannedTime = ((ConcreteActivity) cbde).getPlannedTime();

		}
		if (plannedTime == 0) {
			mess = LocaleBean.getText("dailyremainingtime.noRefRemainingTime");
		} else {
			mess = Float.toString(plannedTime);
			mess = mess.substring(0, mess.indexOf("."));
		}
		return mess;
	}

	/**
	 * Method to known the current Remaining time list
	 * 
	 * @return object list
	 */
	public List<Object> getCurrentRemainingTime() {
		String chartT = "";

		this.cbdeId = (String) WebSessionService
				.getAttribute(WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT);

		List<Object> d = new ArrayList<Object>();
		ConcreteBreakdownElement cbde = new ConcreteBreakdownElement();
		cbde = this.getConcreteBreakdownElementService()
				.getConcreteBreakdownElementDao().getConcreteBreakdownElement(
						this.cbdeId);
		this.activitySelected = false;
		if (cbde instanceof ConcreteTaskDescriptor) {
			chartT = LocaleBean.getText("dailyremainingtime.title3")
					+ ((ConcreteTaskDescriptor) cbde).getConcreteName();
			d = this.getRemainingTimeForTask();
		}
		if (cbde instanceof Project) {
			chartT = LocaleBean.getText("dailyremainingtime.title1")
					+ ((Project) cbde).getConcreteName();
			d = this.getAllCurrentRemaingTime();
		} else {
			if (cbde instanceof ConcreteActivity) {
				chartT = LocaleBean.getText("dailyremainingtime.title2")
						+ ((ConcreteActivity) cbde).getConcreteName();
				this.selectedConcreteActivityId = this.cbdeId;
				this.activitySelected = true;
				d = this.getRemainingTimeByActivity();
			}
		}
		this.setChartTitle(chartT);
		return d;
	}

	/**
	 * Method to known the current Remaining Date list
	 * 
	 * @return object list
	 */
	public List<Object> getRemainingTimeDate() {

		this.cbdeId = (String) WebSessionService
				.getAttribute(WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT);

		List<Object> d = new ArrayList<Object>();
		ConcreteBreakdownElement cbde = new ConcreteBreakdownElement();
		cbde = this.getConcreteBreakdownElementService()
				.getConcreteBreakdownElementDao().getConcreteBreakdownElement(
						cbdeId);
		this.activitySelected = false;

		if (cbde instanceof ConcreteTaskDescriptor) {
			return this.getDate();
		}
		if (cbde instanceof Project) {
			return this.getDate();
		} else {
			if (cbde instanceof ConcreteActivity) {
				this.selectedConcreteActivityId = cbdeId;
				this.activitySelected = true;
				//return this.getDateForActivity();
				return this.getDate();
			}
		}
		return d;
	}

	/**
	 * Method to known the all Remaining time list
	 * 
	 * @return object list
	 */
	public List<Object> getAllCurrentRemaingTime() {
		List<Object> burdonChart = new ArrayList<Object>();
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		HashMap<String, Object> tmpHashMap = new HashMap<String, Object>();
		Date projectDate = new Date();
		double tmpRemaining = 0;

		burdonChart.clear();
		this.date.clear();

		cal2.setTime(new Date());
		cal2 = this.concreteTaskDescriptorService.initCalendar(cal2);

		String projectId = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);

		List<ConcreteTaskDescriptor> listCtd = this.concreteTaskDescriptorService
				.getAllConcreteTaskDescriptorsForProject(projectId);
		// if exists some conctreTaskDescriptor
		if (listCtd.size() > 0) {
			tmpHashMap = this.concreteTaskDescriptorService
					.getCreateDate(listCtd.get(0));
			projectDate = (Date) tmpHashMap.get("createDate");
			cal.setTime(projectDate);
			this.setLegendLabels(Arrays.asList(new String[] { LocaleBean
					.getText("dailyremainingtime.legendLabel1") }));
			this.setColor(Arrays.asList(new Color[] { new Color(255, 0, 0) }));

			while (cal2.after(cal) || cal2.equals(cal)) {
				tmpRemaining = 0;
				tmpRemaining = (double) this.dailyRemainingTimeService
						.getRemainingTimeForDay(projectDate, listCtd);
				if (tmpRemaining > 0) {
					burdonChart.add(new double[] { tmpRemaining });
					date.add(dateFormat.format(projectDate));
				}
				cal.add(Calendar.DATE, 1);
				projectDate = cal.getTime();
			}
			tmpRemaining = (double) this.dailyRemainingTimeService
					.getRemainingTimeForDay(cal2.getTime(), listCtd);
			if (tmpRemaining > 0) {
				burdonChart.add(new double[] { tmpRemaining });
				date.add(dateFormat.format(cal2.getTime()));
			}
		}
		if (burdonChart.size() == 0) {
			float estimate = 0;
			for (ConcreteTaskDescriptor ctd : listCtd) {
				estimate += ctd.getPlannedTime();
			}
			burdonChart.add(new double[] { estimate });
			date.add(dateFormat.format(cal2.getTime()));
		}
		return burdonChart;
	}

	/**
	 * Method to have the date attribute
	 * 
	 * @return object list
	 */
	public List<Object> getDate() {
		return date;
	}

	/**
	 * Method to have the legendLabel attribute
	 * 
	 * @return string list
	 */
	public List<String> getLegendLabels() {
		return legendLabels;
	}

	/**
	 * Method to known type
	 * 
	 * @return string type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Method to set the type attribute
	 * 
	 * @param _type
	 *            string type
	 */
	public void setType(String _type) {
		type = _type;
	}

	/**
	 * Method to set the legendLabel attribute
	 * 
	 * @param _legendLabels
	 */
	public void setLegendLabels(List<String> _legendLabels) {
		legendLabels = _legendLabels;
	}

	/**
	 * ChangeListener on the Combobox including the concrete activities
	 * 
	 * @param e
	 */
	public void changeConcreteActivitiesListener(ValueChangeEvent evt) {
		this.selectedConcreteActivityId = (String) evt.getNewValue();
		if (this.selectedConcreteActivityId.equals("default")) {
			this.activitySelected = false;
		} else {
			this.activitySelected = true;
			this.getDateForActivity();
			// this.getRemainingTimeByActivity();
		}
	}

	/**
	 * Method to known the SelectedConcreteActivityId attribute
	 * 
	 * @return SelectedConcreteActivityId
	 */
	public String getSelectedConcreteActivityId() {
		return selectedConcreteActivityId;
	}

	/**
	 * Method to set SelectedConcreteActivityId attribute
	 * 
	 * @param selectedConcreteActivityId
	 */
	public void setSelectedConcreteActivityId(String selectedConcreteActivityId) {
		this.selectedConcreteActivityId = selectedConcreteActivityId;
	}

	/**
	 * Method to known the remaining time by activity
	 * 
	 * @return object list
	 */
	public List<Object> getRemainingTimeByActivity() {
		List<Object> remainingTimeList = new ArrayList<Object>();

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		ConcreteActivity selectedCA = new ConcreteActivity();
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		Date plannedStartingDate;
		Date plannedFinishingDate;
		double tmpRemaining = 0;
		this.date.clear();
		remainingTimeList.clear();
		// this.date.clear();
		boolean flag = false;

		// we check that an concreteActivity was selected in the combo box
		if (this.getActivitySelected()) {

			this.cbdeId = (String) WebSessionService
					.getAttribute(WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT);

			selectedCA = this.concreteActivityService
					.getConcreteActivity(this.cbdeId);

			this.concreteActivityService.saveConcreteActivity(selectedCA);
			plannedStartingDate = selectedCA.getPlannedStartingDate();
			plannedFinishingDate = selectedCA.getPlannedFinishingDate();
			
			if (plannedStartingDate != null 
					&& plannedFinishingDate != null) {

				// add one day in the finishing estimate date for the loop
				cal.setTime(plannedFinishingDate);
				cal2.setTime(new Date());
				if (cal.after(cal2)) {
					Date d = new Date();
					cal.setTime(d);

				} else {
					cal.add(Calendar.DATE, 1);
				}

				plannedFinishingDate = cal.getTime();

				// init the calendar with the planedStartingDate
				cal.setTime(plannedStartingDate);
				cal2.setTime(plannedFinishingDate);
				Date currentDate = selectedCA.getPlannedStartingDate();

				float plannedRemainingTime = selectedCA.getPlannedTime();

				while (cal.before(cal2)) {
					tmpRemaining = 0;

					tmpRemaining = (double) this.getActivityRemainingTime(selectedCA,
							currentDate);

					if (tmpRemaining != 0) {
						// ref = tmpRemaining;
						flag = true;
					}
					if (flag == true) {
						remainingTimeList.add(new double[] { tmpRemaining });
						this.date.add(dateFormat.format(cal.getTime()));
					}

					// add one day in the calendar
					cal.add(Calendar.DATE, 1);
					currentDate = cal.getTime();

				}
			} else {
				WebCommonService.addInfoMessage(LocaleBean
						.getText("dailyremainingtime.noEstimation"));
				
				//calcul des restesAFaires des différentes taches sousjacentes à partir du début du projet
				ConcreteTaskDescriptor ctd = null ;
				ConcreteBreakdownElement cbe ;
				Iterator<ConcreteBreakdownElement> ite = this.concreteActivityService.getConcreteBreakdownElements(selectedCA).iterator();
				while (ite.hasNext() && ctd == null) {
					cbe = (ConcreteBreakdownElement)ite.next();
					if (cbe instanceof ConcreteTaskDescriptor){
						ctd = (ConcreteTaskDescriptor) cbe ;
					}
				}
				if (ctd != null) {
					Date projectDate = (Date) this.concreteTaskDescriptorService
							.getCreateDate(ctd).get("createDate");
					cal.setTime(projectDate);
					cal2.setTime(new Date());
					while (cal.before(cal2)) {
						tmpRemaining = 0;
						//du début de projet jusqu'à aujourd'hui
						tmpRemaining = (double) this.getActivityRemainingTime(
								selectedCA, cal.getTime());
						remainingTimeList.add(new double[] { tmpRemaining });
						this.date.add(dateFormat.format(cal.getTime()));
						// the follow day 
						cal.add(Calendar.DATE, 1);
					}
				}
			}

			if (remainingTimeList.size() == 0) {
				remainingTimeList.add(new double[] { 0 });
				this.date.add(dateFormat.format(new Date()));
			}
			return remainingTimeList;
		}

		else {
			remainingTimeList.clear();
			remainingTimeList.add(new double[] { 0 });
			this.date.add(dateFormat.format(new Date()));
			// remainingTimeList.add(new double[] { 0, 0 });
			// date.add("0");
			return remainingTimeList;
		}

	}

	
	/**
	 * Method to known the activity remaining time
	 * 
	 * @param _concreteActivity
	 * @param _currentDate
	 * @return time
	 */
	public float getActivityRemainingTime(ConcreteActivity _concreteActivity,
			Date _currentDate) {
		float res = 0;
		float tmp = 0;
		Date finishing ;
		Calendar calFinishing = Calendar.getInstance();
		// with the concreteActivity, we load all task under it
		SortedSet<ConcreteBreakdownElement> concreteBDE = this.concreteActivityService
				.getConcreteBreakdownElements(_concreteActivity);
		for (ConcreteBreakdownElement cbe : concreteBDE) {
			if (cbe instanceof ConcreteTaskDescriptor) {
				tmp = this.dailyRemainingTimeService
								.getRemainingByDay(
										_currentDate,
										((ConcreteTaskDescriptor) cbe));
				if (tmp == (float)0){
					finishing = ((ConcreteTaskDescriptor) cbe).getRealFinishingDate();
					if (finishing != null) {
						//if the task is not finished
						calFinishing.setTime(finishing);
						if (this.concreteTaskDescriptorService.initCalendar(
								calFinishing).getTime().equals(
								((ConcreteTaskDescriptor) cbe)
										.getRealFinishingDate())) {
							//the task have a finishing date planned so get remaining time
							tmp = ((ConcreteTaskDescriptor) cbe)
									.getRemainingTime();
						}
					}
				}
				res = res + tmp;
			}
			if (cbe instanceof ConcreteActivity) {
				res = res
				+  this.getActivityRemainingTime(
						(ConcreteActivity) cbe,
						_currentDate);
			}
		}

		return res;
	}

	/**
	 * Method to known date for activity
	 * 
	 * @return object list
	 */
	public List<Object> getDateForActivity() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		ConcreteActivity selectedCA = new ConcreteActivity();
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		List<Object> d = new ArrayList<Object>();
		float tmpRemaining;
		boolean flag = false;

		if (this.activitySelected) {
			// Recuperation du CA
			selectedCA = this.concreteActivityService
					.getConcreteActivity(this.selectedConcreteActivityId);

			this.concreteActivityService.saveConcreteActivity(selectedCA);

			if ((selectedCA.getPlannedStartingDate() != null)
					|| (selectedCA.getPlannedFinishingDate() != null)) {

				Date plannedStartingDate = selectedCA.getPlannedStartingDate();
				// On recupere la date de fin estimee de l'activite selectionnee
				Date plannedFinishingDate = selectedCA
						.getPlannedFinishingDate();
				cal.setTime(plannedFinishingDate);
				cal2.setTime(new Date());
				if (cal.after(cal2)) {
					Date now = new Date();
					cal.setTime(now);
					plannedFinishingDate = cal.getTime();
				} else {

					cal.setTime(plannedStartingDate); // ???
					cal2.setTime(plannedFinishingDate);
					cal2.add(Calendar.DATE, 1);
				}
				Date currentDate = selectedCA.getPlannedStartingDate(); // DateCourante
				cal.setTime(currentDate);
				d.clear();
				tmpRemaining = 0;
				// while (currentDate.before(plannedFinishingDate)) {
				while (cal.before(cal2)) {
					currentDate = cal.getTime();
					cal.add(Calendar.DATE, 1);
					tmpRemaining = this.getActivityRemainingTime(selectedCA,
							currentDate);

					if (tmpRemaining != 0) {
						flag = true;
					}
					if (flag == true) {
						String t = sdf.format(currentDate);
						d.add(t);
					}

				}

			} else {
				WebCommonService.addInfoMessage(LocaleBean
						.getText("dailyremainingtime.noEstimation"));
				d.clear();
				d.add("0");
			}
			if (d.size() == 0) {
				d.add("0");
			}
			return d;
		} else {
			d.clear();
			d.add("0");
			return d;
		}
	}

	/**
	 * Method to have the activity selected attribute
	 * 
	 * @return activitySelected
	 */
	public boolean getActivitySelected() {
		return activitySelected;
	}

	/**
	 * Method to set the activitySelected attribute
	 * 
	 * @param activitySelected
	 */
	public void setActivitySelected(boolean activitySelected) {
		this.activitySelected = activitySelected;
	}

	/**
	 * Method to known the concreteActivityService attribute
	 * 
	 * @return concreteActivityService
	 */
	public ConcreteActivityService getConcreteActivityService() {
		return this.concreteActivityService;
	}

	/**
	 * Method to set concreteActivityService attribute
	 * 
	 * @param _concreteActivityService
	 */
	public void setConcreteActivityService(
			ConcreteActivityService _concreteActivityService) {
		this.concreteActivityService = _concreteActivityService;
	}

	/**
	 * Method to known the projectService attribute
	 * 
	 * @return projectService
	 */
	public ProjectService getProjectService() {
		return projectService;
	}

	/**
	 * Method to set the projectService attribute
	 * 
	 * @param projectService
	 */
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	/**
	 * Give all the activities saved in the database for the given process
	 * 
	 * @return SelectItem list
	 */
	public List<SelectItem> getConcreteActivities() {

		List<SelectItem> activityList = new ArrayList<SelectItem>();

		activityList
				.add(new SelectItem(
						"default",
						LocaleBean
								.getText("component.tableparticipantProcessProject.concreteActivityComboBox")));

		Project project = this.projectService
				.getProject((String) WebSessionService
						.getAttribute(WebSessionService.PROJECT_ID));

		if (project != null) {
			for (ConcreteActivity cact : this.concreteActivityService
					.getConcreteActivitiesFromProject(project)) {
				activityList.add(new SelectItem(cact.getId(), cact
						.getConcreteName()));
			}
		}
		return activityList;

	}

	/**
	 * Method to have colors
	 * 
	 * @return color list
	 */
	public List<Color> getColor() {
		return color;
	}

	/**
	 * Method to set colors
	 * 
	 * @param _color
	 */
	public void setColor(List<Color> _color) {
		color = _color;
	}

	/**
	 * Method to have referenceRemainingTime attribute
	 * 
	 * @return referenceRemainingTime
	 */
	public String getReferenceRemainingTime() {
		return this.referenceRemainingTime;
	}

	/**
	 * Method to set referenceRemainingTime attribute
	 * 
	 * @param _referenceRemainingTime
	 */
	public void setReferenceRemainingTime(String _referenceRemainingTime) {
		this.referenceRemainingTime = _referenceRemainingTime;
	}

	/**
	 * Method to known the concreteBreakdownElementService attribute
	 * 
	 * @return concreteBreakdownElementService
	 */
	public ConcreteBreakdownElementService getConcreteBreakdownElementService() {
		return concreteBreakdownElementService;
	}

	/**
	 * Method to set the concreteBreakdownElementService attribute
	 * 
	 * @param _concreteBreakdownElementService
	 */
	public void setConcreteBreakdownElementService(
			ConcreteBreakdownElementService _concreteBreakdownElementService) {
		concreteBreakdownElementService = _concreteBreakdownElementService;
	}

	/**
	 * Method to update listener
	 */
	public void updateActionListener() {
		this.getRemainingTimeDate();
		//this.getCurrentRemainingTime();
	}

	/**
	 * Method to known the remaining time for task
	 * 
	 * @return object list
	 */
	public List<Object> getRemainingTimeForTask() {
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		this.cbdeId = (String) WebSessionService
				.getAttribute(WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT);
		double data = 0;
		boolean b = false;
		ConcreteTaskDescriptor ctd = new ConcreteTaskDescriptor();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		List<Object> listData = new ArrayList<Object>();

		this.date.clear();
		ctd = this.getConcreteTaskDescriptorService()
				.getConcreteTaskDescriptor(this.cbdeId);
		// for the task : get the realStartingDate
		Date starting = ctd.getRealStartingDate();
		Date finishing = ctd.getRealFinishingDate();
		HashMap<String, Object> tmpHashMap = this.concreteTaskDescriptorService
				.getCreateDate(ctd);
		Date projectDate = (Date) tmpHashMap.get("createDate");
		float tmpRemaining = 0;
		Calendar calStarting = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		Calendar calFinishing = Calendar.getInstance();
		cal.setTime(projectDate);

		// this.concreteTaskDescriptorService.initCalendar(cal);
		// this.concreteTaskDescriptorService.initCalendar(calStarting);
		Date current = new Date();

		// if the stardedDate is not null : plannedTime before the startedDate
		if (starting != null) {
			calStarting.setTime(starting);
			// planned time between the created date of the project and the
			// starting date of the task
			while (starting.after(projectDate)) {
				tmpRemaining = ctd.getPlannedTime();
				// add to lists
				listData.add(new double[] { tmpRemaining });
				date.add(dateFormat.format(projectDate));
				// follow date
				cal.add(Calendar.DATE, 1);
				projectDate = cal.getTime();
			}
		}
		// the task is started
		if (finishing != null) {
			calFinishing.setTime(finishing);
			if (this.concreteTaskDescriptorService.initCalendar(calFinishing)
					.getTime().equals(finishing)) {
				// the task is not really finished
				if (current.after(finishing)) {
					calFinishing.setTime(current);
					calFinishing.add(Calendar.DATE, 1);
					this.concreteTaskDescriptorService
							.initCalendar(calFinishing);
					finishing = calFinishing.getTime();
				}
			}
		} else {
			calFinishing.setTime(current);
			calFinishing.add(Calendar.DATE, 1);
			this.concreteTaskDescriptorService.initCalendar(calFinishing);
			finishing = calFinishing.getTime();
		}
		while (current.after(projectDate)) {
			if (finishing.after(projectDate)) {
				tmpRemaining = this.dailyRemainingTimeService
						.getRemainingByDay(projectDate, ctd);
				// time is not finished => get remaining time if tmpRemaining =
				// 0
				if (tmpRemaining == new Float(0)) {
					tmpRemaining = ctd.getRemainingTime();
				}
				// add to lists
				listData.add(new double[] { tmpRemaining });
				date.add(dateFormat.format(projectDate));
				// follow date
				cal.add(Calendar.DATE, 1);
				projectDate = cal.getTime();
			} else {
				// add to lists
				listData.add(new double[] { 0 });
				date.add(dateFormat.format(projectDate));
				// follow date
				cal.add(Calendar.DATE, 1);
				projectDate = cal.getTime();
			}
		}
		return listData;
	}

	
	/**
	 * Method to known charTitle attribute
	 * 
	 * @return char title
	 */
	public String getChartTitle() {
		return chartTitle;
	}

	/**
	 * Method to set the char title
	 * 
	 * @param _chartTitle
	 */
	public void setChartTitle(String _chartTitle) {
		this.chartTitle = _chartTitle;
	}

	/**
	 * List of elements which have a burndown chart
	 * 
	 * @return
	 */
	public List<SelectItem> getElementsList() {

		List<SelectItem> elementsList = new ArrayList<SelectItem>();

		String projectId = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);

		Project project = this.projectService.getProject(projectId);

		ConcreteWorkBreakdownElementService concreteWorkBreakdownElementService = this.concreteTaskDescriptorService
				.getConcreteWorkBreakdownElementService();
		for (ConcreteWorkBreakdownElement cwbde : concreteWorkBreakdownElementService
				.getAllConcreteWorkBreakdownElements(this.projectService
						.getProject(projectId))) {
			ConcreteWorkBreakdownElement sup = concreteWorkBreakdownElementService
					.getSuperConcreteActivitiesFromConcreteWorkBreakdownElement(
							cwbde).get(0);

			String parentConcreteName = "";
			if (!(sup instanceof Project)) {
				parentConcreteName = sup.getConcreteName() + " / ";
			}
			if (!(cwbde instanceof ConcreteMilestone)) {
				elementsList.add(new SelectItem(cwbde.getId(),
						parentConcreteName + cwbde.getConcreteName()));
			}
		}
		elementsList.add(0,
				new SelectItem(projectId, project.getConcreteName()));

		return elementsList;
	}

}
