/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Mathieu BENOIT <mbenoit@wilos-project.org>
 * Copyright (C) 2007 Emilien PERICO <emilien.perico@free.fr>
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wilos.business.services.misc.concreteactivity.ConcreteActivityService;
import wilos.business.services.misc.concreterole.ConcreteRoleDescriptorService;
import wilos.business.services.misc.concretetask.ConcreteTaskDescriptorService;
import wilos.business.services.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElementService;
import wilos.business.services.misc.concreteworkproduct.ConcreteWorkProductDescriptorService;
import wilos.business.services.misc.dailyremainingtime.DailyRemainingTimeService;
import wilos.business.services.misc.project.ProjectService;
import wilos.business.services.misc.wilosuser.ParticipantService;
import wilos.business.services.spem2.role.RoleDescriptorService;
import wilos.business.services.spem2.task.TaskDescriptorService;
import wilos.business.services.spem2.workproduct.WorkProductDescriptorService;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concretemilestone.ConcreteMilestone;
import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;
import wilos.model.misc.dailyremainingtime.DailyRemainingTime;
import wilos.model.misc.project.Project;
import wilos.model.misc.wilosuser.Participant;
import wilos.model.spem2.process.Process;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.model.spem2.workbreakdownelement.WorkBreakdownElement;
import wilos.model.spem2.workproduct.WorkProductDescriptor;
import wilos.presentation.web.project.ProjectAdvancementBean;
import wilos.presentation.web.tree.WilosObjectNode;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;
import wilos.resources.LocaleBean;
import wilos.utils.Constantes.State;

public class ConcreteTaskViewerBean extends ViewerBean {

	public static final String EXPAND_TABLE_ARROW = "images/expandableTable/expand.gif";

	public static final String CONTRACT_TABLE_ARROW = "images/expandableTable/contract.gif";

	public static final String TABLE_LEAF = "images/expandableTable/leaf.gif";

	public static final String INDENTATION_STRING = "|- - - ";

	/* Services */

	private ConcreteTaskDescriptorService concreteTaskDescriptorService;

	private TaskDescriptorService taskDescriptorService;

	private ConcreteRoleDescriptorService concreteRoleDescriptorService;

	private RoleDescriptorService roleDescriptorService;

	private ParticipantService participantService;

	private DailyRemainingTimeService dailyRemainingTimeService;

	private ConcreteWorkBreakdownElementService concreteWorkBreakdownElementService;

	private WorkProductDescriptorService workProductDescriptorService;

	private ConcreteWorkProductDescriptorService concreteWorkProductDescriptorService;

	/* Simple fields */

	private ConcreteTaskDescriptor concreteTaskDescriptor;

	private boolean accomplishedTimeVisible;

	private boolean accomplishedTimeModifiable;

	private boolean remainingTimeVisible;

	private boolean remainingTimeModifiable;

	private boolean visibleSaveButton;

	private boolean plannedUserTimeVisible;

	private boolean plannedUserTimeModifiable;

	private boolean plannedUserFinishingDateVisible;

	private boolean plannedUserFinishingDateModifiable;

	private boolean printBundle;

	private boolean plannedFinishingDateVisible;

	private boolean plannedFinishingDateModifiable;

	private Date plannedFinishingDateValue;

	private boolean visiblePopup = false;

	private boolean visibleDeletePopup = false;

	private boolean visibleDeleteTasksPopup = false;

	private boolean visibleMandatoryWPPopup = false;

	private boolean visibleOutputWPPopup = false;

	private boolean visibleWrongStateMandatoryWPPopup = false;

	private boolean visibleWrongStateOutputWPPopup = false;
	
	private boolean visibleNameParti = false;

	protected final Log logger = LogFactory.getLog(this.getClass());

	private String concreteA;

	private List<HashMap<String, Object>> tabTasksAffected;

	private List<HashMap<String, Object>> tabTasksToDelete;

	private String taskAffectedView;

	private String taskLateAffectView;

	private String selectedMode = START_MODE;

	private List<HashMap<String, Object>> concretePredecessors;

	private List<HashMap<String, Object>> concreteSuccessors;

	private boolean displayWorkProductTable = true;

	public static final String START_MODE = "startMode";

	public static final String FINISH_MODE = "finishMode";

	private Project project;

	private String projectViewedId;

	private ArrayList<HashMap<String, Object>> displayContent;

	private ArrayList<HashMap<String, Object>> displayContentToDelete;

	private HashMap<String, String> indentationContent;

	private boolean needIndentation = false;

	protected HashMap<String, Boolean> isExpanded = new HashMap<String, Boolean>();

	private boolean projectModified;

	private int pos = 0;

	private Set<String> listRoleOfParti = new HashSet<String>();

	public static final String MY_TASK_MODE = "myTaskMode";

	public static final String ALL_TASK_MODE = "allTaskMode";

	private String selectedModeFilter = ALL_TASK_MODE;

	/**
	 * Constructor
	 */
	public ConcreteTaskViewerBean() {

		this.project = new Project();
		this.displayContent = new ArrayList<HashMap<String, Object>>();
		this.displayContentToDelete = new ArrayList<HashMap<String, Object>>();
		this.indentationContent = new HashMap<String, String>();

	}

	public boolean isProjectModified() {
		return projectModified;
	}

	public void setProjectModified(boolean projectModified) {
		this.projectModified = projectModified;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getProjectViewedId() {
		return projectViewedId;
	}

	public void setProjectViewedId(String projectViewedId) {
		this.projectViewedId = projectViewedId;
	}

	public HashMap<String, String> getIndentationContent() {
		return indentationContent;
	}

	public void setIndentationContent(HashMap<String, String> indentationContent) {
		this.indentationContent = indentationContent;
	}

	public boolean isNeedIndentation() {
		return needIndentation;
	}

	public void setNeedIndentation(boolean needIndentation) {
		this.needIndentation = needIndentation;
	}

	public HashMap<String, Boolean> getIsExpanded() {
		return isExpanded;
	}

	public void setIsExpanded(HashMap<String, Boolean> isExpanded) {
		this.isExpanded = isExpanded;
	}
	
	/**
	 * Méthode pour récupérer le rôle responsable de la tâche
	 * 
	 * @return string
	 */
	public String getDisplayedMainRole() {
		RoleDescriptor rd = this.concreteTaskDescriptor
				.getTaskDescriptor().getMainRole();
		if (rd != null) {
			return this.roleDescriptorService.getPresentationName(rd);
		} else {
			return LocaleBean
					.getText("concreteWorkProductViewer.noConcreteRole");
		}
	}
	
	/**
	 * Setter of displayContent.
	 * 
	 * @param _displayContent
	 *            The displayContent to set.
	 */
	public void setDisplayContent(
			ArrayList<HashMap<String, Object>> _displayContent) {
		this.displayContent = _displayContent;
	}

	/**
	 * Setter of displayContentToDelete.
	 * 
	 * @param _displayContent
	 *            The displayContent to set.
	 */
	public void setDisplayContentToDelete(
			ArrayList<HashMap<String, Object>> _displayContent) {
		this.displayContentToDelete = _displayContent;
	}

	public String getConcreteA() {
		return concreteA;
	}

	/**
	 * getter of taskAffectedView
	 * 
	 * @return String
	 */
	public String getTaskAffectedView() {
		String projectId = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);
		if ((projectId == null) || (projectId.equals("default")))
			this.setTaskAffectedView("noSelectedProjectPanel");
		else if (this.concreteTaskDescriptorService
				.getAllConcreteTaskDescriptorsForProject(projectId).size() == 0)
			this.setTaskAffectedView("projectNotInstanciated");
		/*
		 * else if (this.getAllConcreteNotAffectedTask().size() == 0)
		 * this.setTaskAffectedView("noAffectedTasksPanel");
		 */
		else
			this.setTaskAffectedView("affectedTasksPanel");
		return this.taskAffectedView;
	}

	/**
	 * Setter of taskAffectedView
	 * 
	 * @param taskAffectedView
	 */
	public void setTaskAffectedView(String taskAffectedView) {
		this.taskAffectedView = taskAffectedView;
	}

	/**
	 * Method which set the attribute concreteA
	 * 
	 * @param concreteA
	 */
	public void setConcreteA(String concreteA) {
		this.concreteA = concreteA;
	}

	/**
	 * Method which return the state of the WebCommonService
	 * 
	 * @return String
	 */
	public String getDisplayedState() {
		return WebCommonService.getDisplayedState(this.concreteTaskDescriptor
				.getState());
	}

	/**
	 * Method which return true if the Dissociate Button can be visible, else
	 * false This button is visible when a concreteRoleDescriptor is not null
	 * and it state is created or ready
	 * 
	 * @return boolean
	 */
	public boolean getDissociateButtonIsVisible() {
		String state = this.concreteTaskDescriptor.getState();

		// String wilosUserId = (String) WebSessionService
		// .getAttribute(WebSessionService.WILOS_USER_ID);

		// Participant participant = this.participantService
		// .getParticipant(wilosUserId);
		ConcreteRoleDescriptor mcrd = this.concreteTaskDescriptor
				.getMainConcreteRoleDescriptor();

		if ((mcrd != null)
				&& ((state == State.CREATED) || (state == State.READY))
		/* && participant.getConcreteRoleDescriptors().contains(mcrd) */) {
			setVisibleNameParti(true);
			return true;
		} else {
			setVisibleNameParti(false);
			return false;
		}
	}

	/**
	 * Method which dissociate the concreteTaskDescriptor of the
	 * ConcreteTaskDescriptor used for display the lower tasks to one Also
	 * refresh the project tree and the project table
	 * 
	 * @param event
	 */
	public void dissociateTaskActionListener(ActionEvent event) {
		this.concreteTaskDescriptorService
				.dissociateConcreteTaskDescriptor(this.concreteTaskDescriptor);

		WebCommonService.addInfoMessage(LocaleBean
				.getText("concretetaskviewer.dissociated"));

		super.refreshProjectTable();
		super.refreshProjectTree();
	}

	/**
	 * Method which set a default value to the ConcreteName of the
	 * concreteTaskDescriptor if this one don't have any The default value is
	 * the Id of the concreteTaskDescriptorService
	 */
	public void saveName() {
		if (this.concreteTaskDescriptor.getConcreteName().trim().length() == 0) {
			// re-put the former concrete name.
			ConcreteTaskDescriptor ctd = this.concreteTaskDescriptorService
					.getConcreteTaskDescriptor(this.concreteTaskDescriptor
							.getId());
			this.concreteTaskDescriptor.setConcreteName(ctd.getConcreteName());

			// add error message.
			WebCommonService.addErrorMessage(LocaleBean
					.getText("viewer.err.checkNameBySaving"));
		} else {
			this.concreteTaskDescriptorService
					.saveConcreteTaskDescriptor(this.concreteTaskDescriptor);

			// Refresh the treebean.
			super.refreshProjectTree();

			// put the name text editor to disable.
			super.setNameIsEditable(false);

			// successful message.
			WebCommonService.addInfoMessage(LocaleBean
					.getText("viewer.visibility.successMessage"));
		}
	}

	/**
	 * soosuske method for the button affected and more precisely... method
	 * which
	 */
	public void affectedActionListener(ActionEvent event) {
		String wilosUserId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);
		Participant participant = this.participantService
				.getParticipant(wilosUserId);
		//on essaye d'affecter la personne et on récupere la tâche
		this.concreteTaskDescriptor = this.concreteTaskDescriptorService.affectedConcreteTaskDescriptor(
				this.concreteTaskDescriptor, participant);
		//si la tâche n'est pas supprimé
		if (this.concreteTaskDescriptor != null) {
			//si l'utilisateur est bien affecté
			if(this.concreteTaskDescriptor.getIsParticipant()){
				//message de confirmation
				WebCommonService.addInfoMessage(LocaleBean
						.getText("concretetaskviewer.updateAffected"));
			}else{
				//si utilisateur pas affecté
				//message indiquant que la tâche est deja affecté
				WebCommonService.addErrorMessage(LocaleBean
						.getText("concretetaskviewer.updateAffected.failed"));
			}
		} else {
			//si tache supprimé, on redirige vers la page de description
			WebCommonService.changeContentPage(WilosObjectNode.PROJECTNODE);
			//et message d'erreur indiquant que la tache a ete supprimé
			WebCommonService.addErrorMessage(LocaleBean
					.getText("concretetaskviewer.updateAffected.delete"));
		}
		//on refresh
		super.refreshProjectTable();
		super.refreshProjectTree();
	}

	/**
	 * Method which return the visibleAffected
	 * 
	 * @return boolean
	 */
	public boolean getVisibleAffected() {
		return this.isParticipantAffectedToConcreteRole()
				&& !this.isParticipantAffectedToConcreteTask()
				&& !this.isConcreteTaskHasAParticipant();
	}
	
	/**
	 * Method which return permits to now if a participant is affected to the task
	 * 
	 * @return boolean
	 */
	public boolean getIsAffected() {
		return this.isConcreteTaskHasAParticipant();
	}

	/**
	 * Checks if the participant can be affected to a concreteTask (the
	 * participant must be affected to the corresponding concrete role)
	 */
	public boolean isParticipantAffectedToConcreteRole() {
		boolean flag = true;
		String wilosUserId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);
		String role = (String) WebSessionService
				.getAttribute(WebSessionService.ROLE_TYPE);
		WebSessionService.setAttribute(
				WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT,
				this.concreteTaskDescriptor.getId());
		if (role.equals("participant")) {
			Participant participant = this.participantService
					.getParticipant(wilosUserId);
			if(this.concreteTaskDescriptorService != null){
				flag = concreteTaskDescriptorService.checkAffectationToRole(
						this.concreteTaskDescriptor, participant);
			}
		}
		if (role.equals("projectDirector")) {
			flag = true;

		}
		return flag;
	}

	/**
	 * Method to known if participant is affected to the concrete task
	 * 
	 * @return true if participant is affected to the concrete task
	 */
	public boolean isParticipantAffectedToConcreteTask() {
		boolean flag = false;
		String wilosUserId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);
		String role = (String) WebSessionService
				.getAttribute(WebSessionService.ROLE_TYPE);
		WebSessionService.setAttribute(
				WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT,
				this.concreteTaskDescriptor.getId());
		if (role.equals("participant")) {
			Participant participant = this.participantService
					.getParticipant(wilosUserId);
			flag = concreteTaskDescriptorService.checkAffectationToTask(
					this.concreteTaskDescriptor, participant);
		}
		if (role.equals("projectDirector")) {
			flag = true;

		}
		return flag;
	}
	
	/**
	 * Method to known if participant is affected to the concrete task
	 * 
	 * @return true if participant is affected to the concrete task
	 */
	public boolean isConcreteTaskHasAParticipant() {
		boolean flag = false;
		String role = (String) WebSessionService
				.getAttribute(WebSessionService.ROLE_TYPE);
		WebSessionService.setAttribute(
				WebSessionService.CONCRETE_BREAK_DOWN_ELEMENT,
				this.concreteTaskDescriptor.getId());
		if (role.equals("participant")) {
			Participant p = this.concreteTaskDescriptorService.getParticipant(this.concreteTaskDescriptor);
			flag = (p != null);
		}
		if (role.equals("projectDirector")) {
			flag = true;
		}
		return flag;
	}

	/**
	 * action for button start
	 * 
	 * @param event
	 */
	public void startActionListener(ActionEvent event) {

		TaskDescriptor _td = this.concreteTaskDescriptor.getTaskDescriptor();

		Project project = super.getProjectService().getProject(
				(String) WebSessionService
						.getAttribute(WebSessionService.PROJECT_ID));

		// First, checking if there is at least one mandatory concrete work
		// product
		// created for the task
		if (!(this.concreteTaskDescriptorService
				.atLeastOneConcreteForEachMandatoryInputWorkProductOfATask(this.concreteTaskDescriptor))
				&& (this.taskDescriptorService.getMandatoryInputWorkProducts(
						_td).size() != 0)) {
			this.visibleMandatoryWPPopup = true;
		} else {

			// Checking if the project have to consider workproducts and tasks
			// state
			// links
			if (project.getConsiderWorkProductAndTaskLinks()) {

				// Checking if the mandatories products are in the desired state
				if (this.concreteTaskDescriptorService
						.checkMandatoryWorkProductState(this.concreteTaskDescriptor)) {
					Date d = new Date();
					DailyRemainingTime drt = new DailyRemainingTime();

					this.concreteTaskDescriptor.setRealStartingDate(d);

					this.concreteTaskDescriptorService
							.startConcreteTaskDescriptor(this.concreteTaskDescriptor);

					// this.saveOrUpdateDaily(this.concreteTaskDescriptor);
					drt.setDailyTaskDescriptor_id(this.concreteTaskDescriptor);
					drt.setDate(d);
					drt.setRemainingTime(this.concreteTaskDescriptor
							.getPlannedUserTime());
					this.getDailyRemainingTimeService().saveDailyRemainingTime(
							drt);

					super.refreshProjectTree();
					super.refreshProjectTable();
				} else {
					this.visibleWrongStateMandatoryWPPopup = true;
				}

			} else {
				Date d = new Date();
				DailyRemainingTime drt = new DailyRemainingTime();

				this.concreteTaskDescriptor.setRealStartingDate(d);

				this.concreteTaskDescriptorService
						.startConcreteTaskDescriptor(this.concreteTaskDescriptor);

				// this.saveOrUpdateDaily(this.concreteTaskDescriptor);
				drt.setDailyTaskDescriptor_id(this.concreteTaskDescriptor);
				drt.setDate(d);
				drt.setRemainingTime(this.concreteTaskDescriptor
						.getPlannedUserTime());
				this.getDailyRemainingTimeService().saveDailyRemainingTime(drt);

				super.refreshProjectTree();
				super.refreshProjectTable();
			}
		}
	}

	/**
	 * Method to display the modal popup. The stop action will be applied or
	 * aborted according to the user's choice through the 'Confirm' or 'Cancel'
	 * buttons
	 * 
	 * @param _event
	 */
	public void stopActionListener(ActionEvent _event) {
		// 

		Project project = super.getProjectService().getProject(
				(String) WebSessionService
						.getAttribute(WebSessionService.PROJECT_ID));

		// First, checking if there is at least one output concrete work product
		// created for the task
		TaskDescriptor _td = this.concreteTaskDescriptor.getTaskDescriptor();
		if (!(this.concreteTaskDescriptorService
				.atLeastOneConcreteForEachOutputWorkProductOfATask(this.concreteTaskDescriptor))
				&& (this.taskDescriptorService.getOutputWorkProducts(_td)
						.size() != 0)) {
			this.visibleOutputWPPopup = true;
		} else {

			// Checking if the project have to consider workproducts and tasks
			// state
			// links
			if (project.getConsiderWorkProductAndTaskLinks()) {
				if (this.concreteTaskDescriptorService
						.checkOutputWorkProductState(this.concreteTaskDescriptor)) {
					if (this.concreteTaskDescriptor.getRemainingTime() == 0) {
						this.confirmStop(_event);
						// saveOrUpdateDaily(this.concreteTaskDescriptor);
					} else {
						this.visiblePopup = true;
					}
				} else {
					this.visibleWrongStateOutputWPPopup = true;
				}

			} else {
				if (this.concreteTaskDescriptor.getRemainingTime() == 0) {
					this.confirmStop(_event);
					// saveOrUpdateDaily(this.concreteTaskDescriptor);
				} else {
					this.visiblePopup = true;
				}
			}
		}
	}

	/**
	 * Method to confirm the delete action
	 * 
	 * @param _event
	 */
	public void confirmDelete(ActionEvent _event) {

		if (!this.getChangeButtonIsDisabled()
				&& (this.concreteTaskDescriptor.getState()
						.equals(State.CREATED) || this.concreteTaskDescriptor
						.getState().equals(State.READY))) {

			ConcreteTaskDescriptor ctd = this.concreteTaskDescriptorService
					.getConcreteTaskDescriptor(this.concreteTaskDescriptor
							.getId());

			ctd = this.concreteTaskDescriptorService.deleteConcreteTaskDescriptor(ctd);
			if(ctd == null){
				//la suppression c'est bien passé, 
				//on retourne sur la page du projet et on affiche un message de confirmation
				WebCommonService.changeContentPage(WilosObjectNode.PROJECTNODE);
				/* Displays info message */
				WebCommonService.addInfoMessage(LocaleBean
						.getText("concretetaskviewer.removed"));
			}else{
				//on affiche un message d'erreur explicatif
				WebCommonService.addErrorMessage(LocaleBean
						.getText("concretetaskviewer.removed.failed"));
			}
			super.rebuildProjectTree();
			this.visibleDeletePopup = false;
		}
	}

	/**
	 * Method to cancel the delete action
	 * 
	 * @param _event
	 */
	public void cancelDelete(ActionEvent _event) {
		this.visibleDeletePopup = false;
	}

	/**
	 * Method to cancel the delete action
	 * 
	 * @param _event
	 */
	public void cancelDeleteTasks(ActionEvent _event) {
		this.visibleDeleteTasksPopup = false;
	}

	/**
	 * Mï¿½thod to confirm stop action
	 * 
	 * @param _event
	 */
	public void confirmStop(ActionEvent _event) {
		Date d = new Date();
		this.concreteTaskDescriptor.setRemainingTime(0);
		this.concreteTaskDescriptor.setRealFinishingDate(d);

		this.concreteTaskDescriptorService
				.finishConcreteTaskDescriptor(this.concreteTaskDescriptor);
		saveOrUpdateDaily(this.concreteTaskDescriptor);
		// Refresh components.
		super.refreshProjectTree();
		super.refreshProjectTable();

		// once the treatment done, hide the popup
		this.visiblePopup = false;
	}

	/**
	 * Method to cancel stop action
	 * 
	 * @param _event
	 */
	public void cancelStop(ActionEvent _event) {
		this.visiblePopup = false;
	}

	/**
	 * Method to suspend action listener and refresh tree and table
	 * 
	 * @param event
	 */
	public void suspendedActionListener(ActionEvent event) {
		this.concreteTaskDescriptorService
				.suspendConcreteTaskDescriptor(this.concreteTaskDescriptor);

		// Refresh components.
		super.refreshProjectTree();
		super.refreshProjectTable();
	}

	/**
	 * Method to set visible the delete popup
	 * 
	 * @param event
	 */
	public void removeActionListener(ActionEvent event) {
		this.visibleDeletePopup = true;
	}

	/**
	 * Method to set visible the delete popup
	 * 
	 * @param event
	 */
	public void removeTasksActionListener(ActionEvent event) {
		this.visibleDeleteTasksPopup = true;
	}

	/**
	 * Method which return concreteTaskDescriptor
	 * 
	 * @return
	 */
	public ConcreteTaskDescriptor getConcreteTaskDescriptor() {
		return concreteTaskDescriptor;
	}

	/**
	 * Method which set the attribute concreteTaskDescriptor
	 * 
	 * @param concreteTaskDescriptor
	 */
	public void setConcreteTaskDescriptor(
			ConcreteTaskDescriptor concreteTaskDescriptor) {
		this.concreteTaskDescriptor = concreteTaskDescriptor;
	}

	/**
	 * Method which return the ConcreteTaskDescriptorService
	 * 
	 * @return ConcreteTaskDescriptorService
	 */
	public ConcreteTaskDescriptorService getConcreteTaskDescriptorService() {
		return concreteTaskDescriptorService;
	}

	/**
	 * Method which set the attribute concreteTaskDescriptorService
	 * 
	 * @param concreteTaskDescriptorService
	 */
	public void setConcreteTaskDescriptorService(
			ConcreteTaskDescriptorService concreteTaskDescriptorService) {
		this.concreteTaskDescriptorService = concreteTaskDescriptorService;
	}

	/**
	 * @return the participantService
	 */
	public ParticipantService getParticipantService() {
		return participantService;
	}

	/**
	 * Setter of participantService.
	 * 
	 * @param participantService
	 *            The participantService to set.
	 */
	public void setParticipantService(ParticipantService participantService) {
		this.participantService = participantService;
	}

	/**
	 * Getter of accomplishedTimeModifiable.
	 * 
	 * @return the accomplishedTimeModifiable.
	 */
	public boolean getAccomplishedTimeModifiable() {
		this.accomplishedTimeModifiable = false;
		if (this.getAccomplishedTimeVisible()
				&& !this.concreteTaskDescriptor.getState().equals(
						State.SUSPENDED)
				&& !this.concreteTaskDescriptor.getState().equals(
						State.FINISHED)
				&& this.isParticipantAffectedToConcreteRole()) {
			this.accomplishedTimeModifiable = false;
		} else {
			this.accomplishedTimeModifiable = true;
		}
		return this.accomplishedTimeModifiable;
	}

	/**
	 * Setter of accomplishedTimeModifiable.
	 * 
	 * @param _accomplishedTimeModifiable
	 *            The accomplishedTimeModifiable to set.
	 */
	public void setAccomplishedTimeModifiable(
			boolean _accomplishedTimeModifiable) {
		this.accomplishedTimeModifiable = _accomplishedTimeModifiable;
	}

	/**
	 * Getter of accomplishedTimeVisible.
	 * 
	 * @return the accomplishedTimeVisible.
	 */
	public boolean getAccomplishedTimeVisible() {
		this.accomplishedTimeVisible = false;
		if (!this.getVisibleAffected()
				&& !this.concreteTaskDescriptor.getState()
						.equals(State.CREATED)
				&& !this.concreteTaskDescriptor.getState().equals(State.READY)
				&& this.isParticipantAffectedToConcreteRole()) {
			this.accomplishedTimeVisible = true;
		} else {
			this.accomplishedTimeVisible = false;
		}
		return this.accomplishedTimeVisible;
	}

	/**
	 * Setter of accomplishedTimeVisible.
	 * 
	 * @param _accomplishedTimeVisible
	 *            The accomplishedTimeVisible to set.
	 */
	public void setAccomplishedTimeVisible(boolean _accomplishedTimeVisible) {
		this.accomplishedTimeVisible = _accomplishedTimeVisible;
	}

	/**
	 * Getter of remainingTimeModifiable.
	 * 
	 * @return the remainingTimeModifiable.
	 */
	public boolean getRemainingTimeModifiable() {
		this.remainingTimeModifiable = false;
		if (getRemainingTimeVisible()) {
			this.remainingTimeModifiable = false;
		} else {
			this.remainingTimeModifiable = true;
		}
		return this.remainingTimeModifiable;
	}

	/**
	 * Setter of remainingTimeModifiable.
	 * 
	 * @param _remainingTimeModifiable
	 *            The remainingTimeModifiable to set.
	 */
	public void setRemainingTimeModifiable(boolean _remainingTimeModifiable) {
		this.remainingTimeModifiable = _remainingTimeModifiable;
	}

	/**
	 * Getter of remainingTimeVisible.
	 * 
	 * @return the remainingTimeVisible.
	 */
	public boolean getRemainingTimeVisible() {
		this.remainingTimeVisible = false;
		if (!this.getVisibleAffected()
				&& !this.concreteTaskDescriptor.getState()
						.equals(State.CREATED)
				&& !this.concreteTaskDescriptor.getState().equals(
						State.FINISHED)
				&& this.isParticipantAffectedToConcreteRole()) {
			this.remainingTimeVisible = true;
		} else {
			this.remainingTimeVisible = false;
		}
		return this.remainingTimeVisible;
	}

	/**
	 * Setter of remainingTimeVisible.
	 * 
	 * @param _remainingTimeVisible
	 *            The remainingTimeVisible to set.
	 */
	public void setRemainingTimeVisible(boolean _remainingTimeVisible) {
		this.remainingTimeVisible = _remainingTimeVisible;
	}

	/**
	 * Getter of plannedUserTimeVisible.
	 * 
	 * @return the plannedUserTimeVisible.
	 */
	public boolean getPlannedUserTimeVisible() {
		this.plannedUserTimeVisible = false;
		if (!this.getVisibleAffected()
				&& this.isParticipantAffectedToConcreteRole()) {
			if (this.getVisibleStart() && this.getVisibleSaveButton()) {
				this.plannedUserTimeVisible = true;
			} else {
				this.plannedUserTimeVisible = false;
			}
		} else {
			this.plannedUserTimeVisible = false;
		}
		return this.plannedUserTimeVisible;
	}

	/**
	 * Setter of plannedUserTimeVisible.
	 * 
	 * @param _plannedUserTimeVisible
	 *            The plannedUserTimeVisible to set.
	 */
	public void setPlannedUserTimeVisible(boolean _plannedUserTimeVisible) {
		this.plannedUserTimeVisible = _plannedUserTimeVisible;
	}

	/**
	 * Getter of plannedUserTimeModifiable.
	 * 
	 * @return the plannedUserTimeModifiable.
	 */
	public boolean getPlannedUserTimeModifiable() {
		this.plannedUserTimeModifiable = false;
		if (!this.getVisibleAffected()
				&& this.isParticipantAffectedToConcreteRole()) {
			if (!this.getVisibleStart() && !this.getVisibleSaveButton()
					&& !this.getVisibleRemove()) {
				this.plannedUserTimeModifiable = false;
			} else {
				// Start
				if (!this.getPlannedUserTimeVisible()
						&& this.getVisibleSaveButton() && this.getVisibleStop()
						&& this.getVisibleSuspended()
						&& !this.getVisibleStart()
						&& !this.getVisibleContinue()
						&& !this.getVisibleRemove()) {
					this.plannedUserTimeModifiable = true;
				}
				// Suspended
				else if (!this.getPlannedUserTimeVisible()
						&& this.getVisibleContinue()
						&& this.getVisibleSaveButton()
						&& !this.getVisibleRemove() && !this.getVisibleStart()
						&& !this.getVisibleStop()
						&& !this.getVisibleSuspended()) {
					this.plannedUserTimeModifiable = true;
				}
				// Stop
				else if (!this.getPlannedUserTimeVisible()
						&& this.getVisibleSaveButton()
						&& !this.getVisibleStart() && !this.getVisibleRemove()
						&& !this.getVisibleContinue() && !this.getVisibleStop()
						&& !this.getVisibleSuspended()) {
					this.plannedUserTimeModifiable = true;
				} else {
					this.plannedUserTimeModifiable = false;
				}
			}
		} else {
			this.plannedUserTimeModifiable = false;
		}
		return this.plannedUserTimeModifiable;
	}

	/**
	 * Setter of plannedUserTimeModifiable.
	 * 
	 * @param _plannedUserTimeModifiable
	 *            The plannedUserTimeModifiable to set.
	 */
	public void setPlannedUserTimeModifiable(boolean _plannedUserTimeModifiable) {
		this.plannedUserTimeModifiable = _plannedUserTimeModifiable;
	}

	/**
	 * Getter of plannedUserFinishingDateVisible.
	 * 
	 * @return the plannedUserFinishingDateVisible.
	 */
	public boolean getPlannedUserFinishingDateVisible() {
		this.plannedUserFinishingDateVisible = false;
		if (!this.getVisibleAffected()
				&& this.isParticipantAffectedToConcreteRole()) {
			if (this.getVisibleStart() && this.getVisibleSaveButton()) {
				this.plannedUserFinishingDateVisible = true;
			} else {
				this.plannedUserFinishingDateVisible = false;
			}
		} else {
			this.plannedUserFinishingDateVisible = false;
		}
		return plannedUserFinishingDateVisible;
	}

	/**
	 * Setter of plannedUserFinishingDateVisible.
	 * 
	 * @param _plannedUserFinishingDateVisible
	 *            The plannedUserFinishingDateVisible to set.
	 */
	public void setPlannedUserFinishingDateVisible(
			boolean _plannedUserFinishingDateVisible) {
		this.plannedUserFinishingDateVisible = _plannedUserFinishingDateVisible;
	}

	/**
	 * Getter of plannedUserFinishingDateModifiable.
	 * 
	 * @return the plannedUserFinishingDateModifiable.
	 */
	public boolean getPlannedUserFinishingDateModifiable() {
		this.plannedUserFinishingDateModifiable = false;
		if (!this.getVisibleAffected()
				&& this.isParticipantAffectedToConcreteRole()) {
			if (!this.getVisibleStart() && !this.getVisibleSaveButton()
					&& !this.getVisibleRemove()) {
				this.plannedUserFinishingDateModifiable = false;
			} else {
				// Start
				if (!this.getPlannedUserTimeVisible()
						&& this.getVisibleSaveButton() && this.getVisibleStop()
						&& this.getVisibleSuspended()
						&& !this.getVisibleStart()
						&& !this.getVisibleContinue()
						&& !this.getVisibleRemove()) {
					this.plannedUserFinishingDateModifiable = true;
				}
				// Suspended
				else if (!this.getPlannedUserTimeVisible()
						&& this.getVisibleContinue()
						&& this.getVisibleSaveButton()
						&& !this.getVisibleRemove() && !this.getVisibleStart()
						&& !this.getVisibleStop()
						&& !this.getVisibleSuspended()) {
					this.plannedUserFinishingDateModifiable = true;
				}
				// Stop
				else if (!this.getPlannedUserTimeVisible()
						&& this.getVisibleSaveButton()
						&& !this.getVisibleStart() && !this.getVisibleRemove()
						&& !this.getVisibleContinue() && !this.getVisibleStop()
						&& !this.getVisibleSuspended()) {
					this.plannedUserFinishingDateModifiable = true;
				} else {
					this.plannedUserFinishingDateModifiable = false;
				}
			}
		} else {
			this.plannedUserFinishingDateModifiable = false;
		}
		return this.plannedUserFinishingDateModifiable;
	}

	/**
	 * Setter of plannedUserFinishingDateModifiable.
	 * 
	 * @param _plannedUserFinishingDateModifiable
	 *            The plannedUserFinishingDateModifiable to set.
	 */
	public void setPlannedUserFinishingDateModifiable(
			boolean _plannedUserFinishingDateModifiable) {
		this.plannedUserFinishingDateModifiable = _plannedUserFinishingDateModifiable;
	}

	/**
	 * Getter of plannedStartingDate.
	 * 
	 * @return the value of the plannedStartingDate.
	 */
	public Date getPlannedStartingDate() {
		return this.concreteTaskDescriptor.getPlannedStartingDate();
	}

	/**
	 * Getter of plannedFinishingDateVisible.
	 * 
	 * @return the value of the plannedFinishingDateVisible.
	 */
	public boolean getPlannedFinishingDateVisible() {
		this.plannedFinishingDateVisible = false;
		if (!this.getVisibleAffected()
				&& this.isParticipantAffectedToConcreteRole()) {
			if (this.getVisibleStart() && this.getVisibleSaveButton()) {
				this.plannedFinishingDateVisible = false;
			} else if (this.getPlannedFinishingDateModifiable()) {
				this.plannedFinishingDateVisible = false;
			} else {
				this.plannedFinishingDateVisible = true;
			}
		} else {
			this.plannedFinishingDateVisible = false;
		}
		return this.plannedFinishingDateVisible;
	}

	/**
	 * Setter of plannedFinishingDateVisible.
	 * 
	 * @param _plannedFinishingDateVisible
	 *            The plannedFinishingDateVisible to set.
	 */
	public void setPlannedFinishingDateVisible(
			boolean _plannedFinishingDateVisible) {
		this.plannedFinishingDateVisible = _plannedFinishingDateVisible;
	}

	/**
	 * Getter of plannedFinishingDateModifiable.
	 * 
	 * @return the value of the plannedFinishingDateModifiable.
	 */
	public boolean getPlannedFinishingDateModifiable() {
		this.plannedFinishingDateModifiable = false;
		if (!this.getVisibleAffected()
				&& this.isParticipantAffectedToConcreteRole()) {
			if (this.getVisibleSaveButton() && this.getVisibleStop()
					&& this.getVisibleSuspended()) {
				this.plannedFinishingDateModifiable = true;
			}
		}
		return this.plannedFinishingDateModifiable;
	}

	/**
	 * Setter of plannedFinishingDateModifiable.
	 * 
	 * @param _plannedFinishingDateModifiable
	 *            The plannedFinishingDateModifiable to set.
	 */
	public void setPlannedFinishingDateModifiable(
			boolean _plannedFinishingDateModifiable) {
		this.plannedFinishingDateModifiable = _plannedFinishingDateModifiable;
	}

	/**
	 * Getter of plannedFinishingDateValue.
	 * 
	 * @return the value of the plannedFinishingDateValue.
	 */
	public Date getPlannedFinishingDateValue() {
		if (this.concreteTaskDescriptor.getRealFinishingDate() == null) {
			this.concreteTaskDescriptor
					.setRealFinishingDate(this.concreteTaskDescriptor
							.getPlannedUserFinishingDate());
		}
		this.plannedFinishingDateValue = this.concreteTaskDescriptor
				.getRealFinishingDate();

		return this.plannedFinishingDateValue;
	}

	/**
	 * Setter of plannedFinishingDateValue.
	 * 
	 * @param _plannedFinishingDateValue
	 *            The plannedFinishingDateValue to set.
	 */
	public void setPlannedFinishingDateValue(Date _plannedFinishingDateValue) {
		this.concreteTaskDescriptor
				.setRealFinishingDate(_plannedFinishingDateValue);
		this.concreteTaskDescriptor.getPlannedUserFinishingDate();
	}

	/**
	 * Permit to know if the bundle is print or not
	 * 
	 * @return the printBundle
	 */
	public boolean getPrintBundle() {
		this.printBundle = true;
		if (this.getPlannedUserTimeVisible()
				|| this.getPlannedUserTimeModifiable()) {
			this.printBundle = true;
		} else {
			this.printBundle = false;
		}
		return this.printBundle;
	}

	/**
	 * save the ConcreteTaskDescriptor
	 * 
	 * @param event
	 */
	public void updateActionListener(ActionEvent event) {

		saveOrUpdateDaily(this.concreteTaskDescriptor);

		this.concreteTaskDescriptorService
				.saveConcreteTaskDescriptor(this.concreteTaskDescriptor);
		WebCommonService.addInfoMessage(LocaleBean
				.getText("concretetaskviewer.updateMessage"));

		super.refreshProjectTable();
	}

	/**
	 * Method which set the value of the attribute visibleSaveButton
	 * 
	 * @param _visibleSaveButton
	 */
	public void setVisibleSaveButton(boolean _visibleSaveButton) {
		this.visibleSaveButton = _visibleSaveButton;
	}

	/**
	 * return the value of visible buton start
	 * 
	 * @return
	 */
	public boolean getVisibleStart() {
		return this.concreteTaskDescriptor.getState().equals(State.READY)
				&& this.isParticipantAffectedToConcreteTask();
	}

	/**
	 * Getter of visibleSaveButton.
	 * 
	 * @return the visibleSaveButton.
	 */
	public boolean getVisibleSaveButton() {
		this.visibleSaveButton = (!accomplishedTimeModifiable || !remainingTimeModifiable)
				&& this.isParticipantAffectedToConcreteTask();
		return this.visibleSaveButton;
	}

	/**
	 * @return the visibleStop
	 */
	public boolean getVisibleStop() {
		return (this.concreteTaskDescriptor.getState().equals(State.STARTED)
				&& this.isParticipantAffectedToConcreteTask() && this.concreteTaskDescriptorService
				.isConcreteTaskDescriptorFinishable(this.concreteTaskDescriptor));
	}

	/**
	 * @return the visibleSuspended
	 */
	public boolean getVisibleSuspended() {
		return (this.concreteTaskDescriptor.getState().equals(State.STARTED) && this
				.isParticipantAffectedToConcreteTask());
	}

	/**
	 * @return the visibleRemoved
	 */
	public boolean getVisibleRemove() {
		return (!this.getChangeButtonIsDisabled() && (this.concreteTaskDescriptor
				.getState().equals(State.CREATED) || this.concreteTaskDescriptor
				.getState().equals(State.READY)));
	}

	/**
	 * @return the visibleRemoved
	 */
	public boolean getVisibleRemove(ConcreteTaskDescriptor _ctd) {

		if (_ctd.getState().equals(State.STARTED)
				|| _ctd.getState().equals(State.SUSPENDED)
				|| _ctd.getState().equals(State.FINISHED)) {
			return false;
		}
		
		return true;
	}

	/**
	 * @return the visibleContinue
	 */
	public boolean getVisibleContinue() {
		return (this.concreteTaskDescriptor.getState().equals(State.SUSPENDED) && this
				.isParticipantAffectedToConcreteTask());
	}

	/**
	 * @return the concreteRoleDescriptorService
	 */
	public ConcreteRoleDescriptorService getConcreteRoleDescriptorService() {
		return concreteRoleDescriptorService;
	}

	/**
	 * @param concreteRoleDescriptorService
	 *            the concreteRoleDescriptorService to set
	 */
	public void setConcreteRoleDescriptorService(
			ConcreteRoleDescriptorService concreteRoleDescriptorService) {
		this.concreteRoleDescriptorService = concreteRoleDescriptorService;
	}

	/**
	 * @return the roleDescriptorService
	 */
	public RoleDescriptorService getRoleDescriptorService() {
		return roleDescriptorService;
	}

	/**
	 * @param roleDescriptorService
	 *            the roleDescriptorService to set
	 */
	public void setRoleDescriptorService(
			RoleDescriptorService roleDescriptorService) {
		this.roleDescriptorService = roleDescriptorService;
	}

	/**
	 * @return the taskDescriptorService
	 */
	public TaskDescriptorService getTaskDescriptorService() {
		return taskDescriptorService;
	}

	/**
	 * @param taskDescriptorService
	 *            the taskDescriptorService to set
	 */
	public void setTaskDescriptorService(
			TaskDescriptorService taskDescriptorService) {
		this.taskDescriptorService = taskDescriptorService;
	}

	/**
	 * Method which return the value of the visiblePopup
	 * 
	 * @return boolean
	 */
	public boolean getVisiblePopup() {
		return visiblePopup;
	}

	/**
	 * Method which set the visibility of the attribute visiblePopup
	 * 
	 * @param visiblePopup
	 */
	public void setVisiblePopup(boolean visiblePopup) {
		this.visiblePopup = visiblePopup;
	}

	/**
	 * Method which return the value of the visibleDeletePopup
	 * 
	 * @return boolean
	 */
	public boolean isVisibleDeletePopup() {
		return visibleDeletePopup;
	}

	/**
	 * Method which shows or hides the deletePop-up
	 * 
	 * @param _visibleDeletePopup
	 */
	public void setVisibleDeletePopup(boolean _visibleDeletePopup) {
		this.visibleDeletePopup = _visibleDeletePopup;
	}

	/**
	 * Method which return the value of the visibleDeletePopup
	 * 
	 * @return boolean
	 */
	public boolean isVisibleDeleteTasksPopup() {
		return visibleDeleteTasksPopup;
	}

	/**
	 * Method which shows or hides the deletePop-up
	 * 
	 * @param _visibleDeletePopup
	 */
	public void setVisibleDeleteTasksPopup(boolean _visibleDeleteTasksPopup) {
		this.visibleDeleteTasksPopup = _visibleDeleteTasksPopup;
	}

	/**
	 * Method used in order to test the value of the session project and the
	 * project attached to the concreteTask
	 * 
	 * @return boolean
	 */
	public boolean gettest() {
		if (!WebSessionService.PROJECT_ID.equals("projectId")) {

			return true;
		} else {

			return false;
		}
	}

	/**
	 * Getter of tabTasksAffected
	 * 
	 * @return List of HashMap
	 */
	public List<HashMap<String, Object>> getTabTasksAffected() {
		return this.tabTasksAffected;
	}

	/**
	 * Setter of tabTasksAffected
	 * 
	 * @param tabTasksAffected
	 */
	public void setTabTasksAffected(
			List<HashMap<String, Object>> tabTasksAffected) {
		this.tabTasksAffected = tabTasksAffected;
	}

	/**
	 * Getter of tabTasksAffected
	 * 
	 * @return List of HashMap
	 */
	public List<HashMap<String, Object>> getTabTasksToDelete() {
		return this.tabTasksToDelete;
	}

	/**
	 * Setter of tabTasksAffected
	 * 
	 * @param tabTasksAffected
	 */
	public void setTabTasksToDelete(
			List<HashMap<String, Object>> tabTasksToDelete) {
		this.tabTasksToDelete = tabTasksToDelete;
	}

	/**
	 * Return a collection of concrete task who don't have any affectation
	 * 
	 * @return List of HashMap
	 */
	/*
	 * public List<HashMap<String, Object>> getAllConcreteNotAffectedTask() {
	 * List<HashMap<String, Object>> listHashMapTask = new
	 * ArrayList<HashMap<String, Object>>(); List<ConcreteTaskDescriptor>
	 * tabConcreteTaskDescriptor;
	 * 
	 * ConcreteTaskDescriptorService ctService = this
	 * .getConcreteTaskDescriptorService(); String projectID = (String)
	 * WebSessionService .getAttribute(WebSessionService.PROJECT_ID); String
	 * wilosUserId = (String) WebSessionService
	 * .getAttribute(WebSessionService.WILOS_USER_ID); Participant parti =
	 * this.participantService.getParticipant(wilosUserId);
	 * 
	 * tabConcreteTaskDescriptor = ctService
	 * .getAllConcreteTaskDescriptorsForProject(projectID); for
	 * (ConcreteTaskDescriptor ctd : tabConcreteTaskDescriptor) { if
	 * (ctService.checkAffectationToRole(ctd, parti)) { HashMap<String, Object>
	 * tmpHashMap = new HashMap<String, Object>(); tmpHashMap.put("taskName",
	 * ctd.getConcreteName()); TaskDescriptor tmp = ctd.getTaskDescriptor(); //
	 * RoleDescriptor tmpRoleDescriptor; TaskDescriptor tmp2 =
	 * this.taskDescriptorService .getTaskDescriptorById(tmp.getId());
	 * 
	 * if (tmp2.getMainRole() == null) { if (ctd.getState().equals("Created")) {
	 * tmpHashMap.put("affected", new Boolean(false)); } else {
	 * tmpHashMap.put("affected", new Boolean(true)); } } else {
	 * tmpHashMap.put("affected", ctd.getIsParticipant()); }
	 * 
	 * tmpHashMap.put("taskId", ctd.getId()); if
	 * ((ctd.getState().equals("Created")) || (ctd.getState().equals("Ready")))
	 * { tmpHashMap.put("taskState", new Boolean(false)); } else {
	 * tmpHashMap.put("taskState", new Boolean(true)); } tmpHashMap .put(
	 * "activitie", this.concreteTaskDescriptorService
	 * .getSuperConcreteActivityFromConcreteTaskDescriptor(
	 * ctd).getConcreteName()); listHashMapTask.add(tmpHashMap); } }
	 * this.setTabTasksAffected(listHashMapTask); return listHashMapTask; }
	 */

	/**
	 * Getter of displayContent.
	 * 
	 * @return the displayContent.
	 */
	public ArrayList<HashMap<String, Object>> getDisplayContent() {
		String projectId = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);

		this.listRoleOfParti = null;
		if (projectId != null && !projectId.equals("default")) {
			ConcreteTaskDescriptorService ctService = this
					.getConcreteTaskDescriptorService();

			this.project = ctService.getProject(projectId);
			this.pos = 0;
			// if the project is another then the last selected or if it has
			// been
			// modified
			if (project != null) {
				if (this.projectViewedId == null
						|| !(this.projectViewedId.equals(projectId))
						|| this.projectModified) {
					// reseting the table parameters
					projectViewedId = projectId;
					this.displayContent.clear();
					this.indentationContent.clear();
					this.isExpanded.clear();
					this.needIndentation = false;
					List<HashMap<String, Object>> list = this
							.retrieveHierarchicalItems(this.project);
					if (list != null) {
						this.displayContent.addAll(list);
					} else
						this.displayContent.clear();
				}

			} else {
				this.displayContent.clear();
			}
		} else
			this.displayContent.clear();

		this.projectModified = false;
		return this.displayContent;
	}

	/**
	 * Getter of displayContentToDelete.
	 * 
	 * @return the displayContentToDelete.
	 */
	public ArrayList<HashMap<String, Object>> getDisplayContentToDelete() {
		String projectId = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);

		this.listRoleOfParti = null;
		if (projectId != null && !projectId.equals("default")) {
			ConcreteTaskDescriptorService ctService = this
					.getConcreteTaskDescriptorService();

			this.project = ctService.getProject(projectId);
			this.pos = 0;
			// if the project is another then the last selected or if it has
			// been
			// modified
			if (project != null) {
				if (this.projectViewedId == null
						|| !(this.projectViewedId.equals(projectId))
						|| this.projectModified) {
					// reseting the table parameters
					projectViewedId = projectId;
					this.displayContentToDelete.clear();
					this.indentationContent.clear();
					this.isExpanded.clear();
					this.needIndentation = false;
					List<HashMap<String, Object>> list = this
							.retrieveHierarchicalItemsForDelete(this.project);
					if (list != null) {
						this.displayContentToDelete.addAll(list);
					} else
						this.displayContentToDelete.clear();
				}

			} else {
				this.displayContentToDelete.clear();
			}
		} else
			this.displayContentToDelete.clear();

		this.projectModified = false;
		return this.displayContentToDelete;
	}

	/**
	 * Utility method to add all child nodes to the parent dataTable list.
	 */
	@SuppressWarnings("unchecked")
	private void expandNodeAction(String elementId, ArrayList<HashMap<String,Object>> nom_liste) {
		// the table will be expanded so, indentation string
		this.needIndentation = true;
		
		ArrayList<Object> tmp = new ArrayList<Object>();
		tmp.addAll(nom_liste);
		int index;
		ConcreteTaskDescriptorService ctService = this
				.getConcreteTaskDescriptorService();
		ConcreteActivityService concreteActivityService = ctService
				.getConcreteActivityService();
		for (Iterator iter = tmp.iterator(); iter.hasNext();) {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm = (HashMap<String, Object>) iter.next();

			if (hm.get("id").equals(elementId)) {
				if (hm.get("nodeType").equals("node")) {
					hm.put("expansionImage", EXPAND_TABLE_ARROW);
					ConcreteActivity ca = concreteActivityService
							.getConcreteActivity((String) hm.get("id"));
					index = nom_liste.indexOf(hm);
					if (this.retrieveHierarchicalItems(ca) != null) {
						nom_liste.addAll(index + 1, this
								.retrieveHierarchicalItems(ca));
					} else {
						nom_liste.clear();
					}
					return;
				}
			}
		}
	}
	
	
	
	/**
	 * Utility method to add all child nodes to the parent dataTable list.
	 */
	@SuppressWarnings("unchecked")
	private void expandNodeAction() {
		ArrayList<HashMap<String,Object>> nom_liste = displayContent;
		
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");

		this.expandNodeAction(elementId,nom_liste);
	}

	
	/**
	 * Utility method to add all child nodes to the parent dataTable list.
	 */
	@SuppressWarnings("unchecked")
	private void expandNodeActionForTasksToDelete() {
		ArrayList<HashMap<String,Object>> tmp = this.displayContentToDelete;
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");
		
		this.expandNodeActionForTasksToDelete(elementId,tmp);
	}

	/**
	 * Utility method to add all child nodes to the parent dataTable list.
	 */
	@SuppressWarnings("unchecked")
	private void expandNodeActionForTasksToDelete(String elementId, ArrayList<HashMap<String,Object>> nom_liste) {
		// the table will be expanded so, indentation string
		this.needIndentation = true;
		
		ArrayList<Object> tmp = new ArrayList<Object>();
		tmp.addAll(nom_liste);
		int index;
		ConcreteTaskDescriptorService ctService = this
				.getConcreteTaskDescriptorService();
		ConcreteActivityService concreteActivityService = ctService
				.getConcreteActivityService();
		for (Iterator iter = tmp.iterator(); iter.hasNext();) {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm = (HashMap<String, Object>) iter.next();

			if (hm.get("id").equals(elementId)) {
				if (hm.get("nodeType").equals("node")) {
					hm.put("expansionImage", EXPAND_TABLE_ARROW);
					ConcreteActivity ca = concreteActivityService
							.getConcreteActivity((String) hm.get("id"));
					index = this.displayContentToDelete.indexOf(hm);
					if (this.retrieveHierarchicalItemsForDelete(ca) != null) {
						this.displayContentToDelete.addAll(index + 1, this
								.retrieveHierarchicalItemsForDelete(ca));
					} else {
						this.displayContentToDelete.clear();
					}
				}
			}
		}
	}

	
	
	public ConcreteRoleDescriptor getRoleForTask(ConcreteTaskDescriptor _ctd) {
		if (_ctd != null) {
			// List<HashMap<String, Object>> listHashMapRoleForTask = new
			// ArrayList<HashMap<String, Object>>();

			ConcreteTaskDescriptorService ctdService = this
					.getConcreteTaskDescriptorService();
			TaskDescriptorService tdService = ctdService
					.getTaskDescriptorService();
			RoleDescriptorService rdService = ctdService
					.getRoleDescriptorService();
			ConcreteRoleDescriptorService crdService = ctdService
					.getConcreteRoleDescriptorService();

			TaskDescriptor tmp = _ctd.getTaskDescriptor();
			RoleDescriptor tmpRoleDescriptor;
			TaskDescriptor tmp2 = tdService.getTaskDescriptorById(tmp.getId());

			tmpRoleDescriptor = tmp2.getMainRole();

			if (tmpRoleDescriptor != null) {
				RoleDescriptor rd = rdService
						.getRoleDescriptor(tmpRoleDescriptor.getId());
				List<ConcreteRoleDescriptor> listeRd = crdService
						.getAllConcreteRoleDescriptorForARoleDescriptor(rd
								.getId());
				if (listeRd.size() > 0) {
					Iterator<ConcreteRoleDescriptor> ite = listeRd.iterator();
					ConcreteRoleDescriptor crd = ite.next();
					// if(crd==null)
					while (crd == null || ite.hasNext()) {
						crd = ite.next();
					}
					return crd;
				}
			}
		}
		return new ConcreteRoleDescriptor();
	}

	public RoleDescriptor getRoleDescriptorForTask(ConcreteTaskDescriptor _ctd) {
		if (_ctd != null) {
			
			ConcreteTaskDescriptorService ctdService = this
					.getConcreteTaskDescriptorService();
			TaskDescriptorService tdService = ctdService
					.getTaskDescriptorService();
			RoleDescriptorService rdService = ctdService
					.getRoleDescriptorService();
			
			TaskDescriptor tmp = _ctd.getTaskDescriptor();
			TaskDescriptor tmp2 = tdService.getTaskDescriptorById(tmp.getId());
			RoleDescriptor tmpRoleDescriptor = tmp2.getMainRole();
			
			if (tmpRoleDescriptor != null) {
				RoleDescriptor rd = rdService
						.getRoleDescriptor(tmpRoleDescriptor.getId());
				return rd;
			}
		}
		return null;
	}

	/**
	 * The concrete breakdown element is a task We must display a leaf
	 * 
	 * @param _hm
	 * @param _concreteBreakdownElement
	 * @param _role
	 * @param _affected
	 * @param _b
	 * @param _parti
	 */
	private void displayALeaf(HashMap<String, Object> _hm,
			ConcreteBreakdownElement _concreteBreakdownElement, String _role,
			String _affected, boolean _b, Participant _parti, String _projectId) {
		ConcreteTaskDescriptorService ctService = this
				.getConcreteTaskDescriptorService();
		ConcreteTaskDescriptor ctd = (ConcreteTaskDescriptor) _concreteBreakdownElement;
		RoleDescriptor rd = getRoleDescriptorForTask(ctd);
		
		String requiredRoleDescriptor = "";
		if (rd != null) {
			requiredRoleDescriptor = rd.getPresentationName();
			_hm.put("requiredRoleDescriptor", requiredRoleDescriptor);
			_hm.put("requiredRole", requiredRoleDescriptor);
		} else {
			_hm.put("requiredRole", LocaleBean
					.getText("tabAffectWorkProduct.noRequieredRole"));
			_hm.put("enabled", false);
		}
		// Set as a leaf
		_hm.put("nodeType", "leaf");
		_hm.put("expansionImage", TABLE_LEAF);

		_hm.put("id", _concreteBreakdownElement.getId());
		if (ctd.getConcreteName() != "") {
			boolean b1 = false;
			// responsible of the task
			Participant responsible;
			if (ctd.getIsParticipant()) 
			{
				b1 = true;
				responsible = ctService.getParticipant(ctd);
				if (responsible != null) {
					_hm.put("responsible", responsible.getName() + " "
							+ responsible.getFirstname());
					if (!responsible.getId().equals(_parti.getId())) {
						_affected = "OtherAffected";
					}
				}
			}
			else
			{
				ConcreteRoleDescriptor crd = ctd.getMainConcreteRoleDescriptor();
				if (crd != null) {
					responsible = crd.getParticipant();
					if (responsible != null) {
						_hm.put("responsible", responsible.getName() 
								+ " "+ responsible.getFirstname());
						_affected = "OtherAffected";
					}
				}
			}
			
			_hm.put("isAffected", b1);
			_hm.put("flagAffect", _hm.get("isAffected"));
			_hm.put("visibleCheckbox", true);
		} else {
			_hm.put("visibleCheckbox", false);
		}
		//if the task is not started or not finished
		if (ctd.getState().equals("Created")|| ctd.getState().equals("Ready")) {
			//if an other participant is affected to the task
			if (_affected.equals("OtherAffected")) {
				// hide check box
				_hm.put("enabled", true);
				_hm.put("visibleCheckbox", false);
			} else {
				// if no required role
				if (requiredRoleDescriptor.equals("")) {
					// visible
					_hm.put("enabled", false);
					_hm.put("visibleCheckbox", true);
				} else { 
					// if _parti have the required role 
					boolean affected = this.isAffected(_parti, rd);
					if (affected) {
						// visible
						_hm.put("enabled", false);
						_hm.put("visibleCheckbox", true);
					} else {
						// hide
						_hm.put("enabled", true);
						_hm.put("visibleCheckbox", false);
					}
				}
			}
			_hm.put("taskState", new Boolean(false));
		} 
		// the task is started or finished
		else {
			_hm.put("enabled", true);
			_hm.put("visibleCheckbox", true);
			_hm.put("taskState", new Boolean(true));
			//if an other participant is affected to the task
			if (_affected.equals("OtherAffected")) {
				_hm.put("visibleCheckbox", false);
			}
		}
	}

	public boolean isAffected(Participant _parti, RoleDescriptor _rd){
		ConcreteRoleDescriptorService crdService = this
				.getConcreteTaskDescriptorService().getConcreteRoleDescriptorService();
		List<ConcreteRoleDescriptor> listeCrd = crdService
				.getAllConcreteRoleDescriptorForARoleDescriptor(_rd.getId());
		for (ConcreteRoleDescriptor crd_list : listeCrd)
		{
			Participant p = crd_list.getParticipant();
			if (p != null)
				if (p.getId().equals(_parti.getId()))
					return true;
		}
		return false;
	}
	
	
	/**
	 * Update the table when the user arrived by the menu
	 */
	public void updateTable() {
		this.projectModified = true;
	}

	/**
	 * this method allow to return a string. the string indicate if the given
	 * participant is already affected or not to the given
	 * concreteRoleDescriptor this method also indicate if another participant
	 * than the given participant is affected to the given
	 * concreteRoleDescriptor
	 * 
	 * @param _parti
	 * @param _concreteRoleDescriptor
	 * @return String
	 */

	public String isAffected(Participant _parti,
			ConcreteRoleDescriptor _concreteRoleDescriptor) {
		ConcreteTaskDescriptorService ctdService = this
				.getConcreteTaskDescriptorService();
		ConcreteRoleDescriptorService crdService = ctdService
				.getConcreteRoleDescriptorService();
		Participant parti = crdService.getParticipant(_concreteRoleDescriptor);
		if (parti != null) {
			if (parti.getId().equals(_parti.getId())) {
				return "Affected";
			} else {
				return "OtherAffected";
			}

		} else {
			return "NotAffected";
		}
	}
	
	
	/**
	 * Recursive method returning a hashmap data model which contains the
	 * elements required to display the project advancement table Give a
	 * ConcreteActivity to this method, return its first hierarchical children
	 * described into the hashmap
	 * 
	 */
	private List<HashMap<String, Object>> retrieveHierarchicalItems(
			ConcreteActivity _concreteActivity) {

		String indentationString = "";
		String affected = "";

		boolean b = false;

		boolean screen = true;
		List<HashMap<String, Object>> subConcretesContent = new ArrayList<HashMap<String, Object>>();

		String wilosUserId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);

		Participant parti = this.participantService.getParticipant(wilosUserId);

		String role = (String) WebSessionService
				.getAttribute(WebSessionService.ROLE_TYPE);

		String projectId = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);

		// for every child of the activity
		if (_concreteActivity == null) {
			return null;
		}
		ConcreteTaskDescriptorService ctService = this
				.getConcreteTaskDescriptorService();
		ConcreteActivityService concreteActivityService = ctService
				.getConcreteActivityService();

		SortedSet<ConcreteBreakdownElement> concreteBDE = concreteActivityService
				.getConcreteBreakdownElements(_concreteActivity);
		for (ConcreteBreakdownElement concreteBreakdownElement : concreteBDE) {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			if (!(concreteBreakdownElement instanceof ConcreteMilestone)) {
				if (concreteBreakdownElement instanceof ConcreteWorkBreakdownElement) {
					affected = "";
					// if this is a task -> display a leaf
					if (concreteBreakdownElement instanceof ConcreteTaskDescriptor) {

						if (this.selectedModeFilter.equals(MY_TASK_MODE)) {
							// verify if the user is the responsible
							Participant responsible;
							ConcreteTaskDescriptor ctd = (ConcreteTaskDescriptor) concreteBreakdownElement;
							if (ctd.getIsParticipant()) {
								responsible = ctService.getParticipant(ctd);
								if (responsible.getId().equals(parti.getId())) {
									// display only tasks which are affected to
									// the connected participant
									this.displayALeaf(hm,
											concreteBreakdownElement, role,
											affected, b, parti, projectId);
									screen = true;
								} else {
									screen = false;
								}
							} else {
								screen = false;
							}
						} else {// display all tasks
							this.displayALeaf(hm, concreteBreakdownElement,
									role, affected, b, parti, projectId);
							screen = true;
						}
					}
					// if this is not a task -> display a node
					else {

						hm.put("visibleCheckbox", false);
						hm.put("nodeType", "node");
						hm.put("expansionImage", CONTRACT_TABLE_ARROW);
						screen = true;
					}

					hm.put("id", concreteBreakdownElement.getId());
					hm.put("concreteName", concreteBreakdownElement
							.getConcreteName());
					hm.put("parentId", _concreteActivity.getId());
					if (screen)
						subConcretesContent.add(hm);

					// if this is not the root node -> needIndentation == true
					if (needIndentation) {
						if (this.indentationContent.get(_concreteActivity
								.getId()) != null) {
							indentationString = this.indentationContent
									.get(_concreteActivity.getId());
						}
						this.indentationContent
								.put(
										(String) hm.get("id"),
										indentationString
												.concat(ProjectAdvancementBean.INDENTATION_STRING));
					}
					hm.put("isEditable", new Boolean(false));
				}
			}
		}
		this.setTabTasksAffected(subConcretesContent);
		return subConcretesContent;
	}

	/**
	 * The concrete breakdown element is a task We must display a leaf
	 * 
	 * @param _hm
	 * @param _concreteBreakdownElement
	 * @param _role
	 * @param _affected
	 * @param _b
	 * @param _parti
	 */
	private void displayALeafTaskToDelete(HashMap<String, Object> _hm,
			ConcreteBreakdownElement _concreteBreakdownElement, String _role,
			String _affected, boolean _b, Participant _parti, String _projectId) {
		ConcreteTaskDescriptorService ctService = this
				.getConcreteTaskDescriptorService();

		ConcreteRoleDescriptorService crds = ctService
				.getConcreteRoleDescriptorService();

		ConcreteTaskDescriptor ctd = (ConcreteTaskDescriptor) _concreteBreakdownElement;
		Participant responsible;
		responsible = ctService.getParticipant(ctd);
		if (responsible != null) {
			_hm.put("responsible", responsible.getName() + " " + responsible.getFirstname());
			_hm.put("toDelete", new Boolean(false));
			_hm.put("visibleCheckbox", false);
		}else{
			_hm.put("toDelete", new Boolean(false));
			_hm.put("visibleCheckbox", true);
		}
		// Set as a leaf
		_hm.put("nodeType", "leaf");
		_hm.put("expansionImage", TABLE_LEAF);

		_hm.put("id", _concreteBreakdownElement.getId());
	}

	/**
	 * Recursive method returning a hashmap data model which contains the
	 * elements required to display the project advancement table Give a
	 * ConcreteActivity to this method, return its first hierarchical children
	 * described into the hashmap
	 * 
	 */
	private List<HashMap<String, Object>> retrieveHierarchicalItemsForDelete(
			ConcreteActivity _concreteActivity) {
		
		String indentationString = "";
		String affected = "";

		boolean b = false;

		boolean screen = true;
		List<HashMap<String, Object>> subConcretesContent = new ArrayList<HashMap<String, Object>>();

		String wilosUserId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);

		Participant parti = this.participantService.getParticipant(wilosUserId);

		String role = (String) WebSessionService
				.getAttribute(WebSessionService.ROLE_TYPE);

		String projectId = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);

		// for every child of the activity
		if (_concreteActivity == null) {
			return null;
		}
		ConcreteTaskDescriptorService ctService = this
				.getConcreteTaskDescriptorService();
		ConcreteActivityService concreteActivityService = ctService
				.getConcreteActivityService();

		SortedSet<ConcreteBreakdownElement> concreteBDE = concreteActivityService
				.getConcreteBreakdownElements(_concreteActivity);
		for (ConcreteBreakdownElement concreteBreakdownElement : concreteBDE) {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			if (!(concreteBreakdownElement instanceof ConcreteMilestone)) {
				if (concreteBreakdownElement instanceof ConcreteWorkBreakdownElement) {
					affected = "";
					// if this is a task -> display a leaf
					if (concreteBreakdownElement instanceof ConcreteTaskDescriptor) {

						if (this.selectedModeFilter.equals(MY_TASK_MODE)) {
							// verify if the user is the responsible
							Participant responsible;
							ConcreteTaskDescriptor ctd = (ConcreteTaskDescriptor) concreteBreakdownElement;
							if (ctd.getIsParticipant()) {
								responsible = ctService.getParticipant(ctd);
								if (responsible.getId().equals(parti.getId())) {
									// display only tasks which are affected to
									// the connected participant
									this.displayALeafTaskToDelete(hm,
											concreteBreakdownElement, role,
											affected, b, parti, projectId);
									screen = true;
								} else {
									screen = false;
								}
							} else {
								screen = false;
							}
						} else {// display all tasks
							this.displayALeafTaskToDelete(hm, concreteBreakdownElement,
									role, affected, b, parti, projectId);
							screen = true;
						}
					}
					// if this is not a task -> display a node
					else {

						hm.put("visibleCheckbox", false);
						hm.put("nodeType", "node");
						hm.put("expansionImage", CONTRACT_TABLE_ARROW);
						screen = true;
					}

					hm.put("id", concreteBreakdownElement.getId());
					hm.put("concreteName", concreteBreakdownElement
							.getConcreteName());
					hm.put("parentId", _concreteActivity.getId());
					if (screen)
						subConcretesContent.add(hm);

					// if this is not the root node -> needIndentation == true
					if (needIndentation) {
						if (this.indentationContent.get(_concreteActivity
								.getId()) != null) {
							indentationString = this.indentationContent
									.get(_concreteActivity.getId());
						}
						this.indentationContent
								.put(
										(String) hm.get("id"),
										indentationString
												.concat(ProjectAdvancementBean.INDENTATION_STRING));
					}
					hm.put("isEditable", new Boolean(false));
				}
			}
		}
		this.setTabTasksAffected(subConcretesContent);
		return subConcretesContent;
		
	}

	/**
	 * Utility method to remove all child nodes from the parent dataTable list.
	 */
	private void contractNodeAction() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");

		ArrayList<HashMap<String, Object>> parentList = new ArrayList<HashMap<String, Object>>();
		parentList.addAll(this.displayContent);

		/* Removes element which we want to contract from the parent list */
		for (HashMap<String, Object> currentElement : this.displayContent) {

			if (currentElement.get("id").equals(elementId)
					&& currentElement.get("nodeType").equals("node")) {
				currentElement.put("expansionImage",
						ProjectAdvancementBean.CONTRACT_TABLE_ARROW);
				parentList.remove(currentElement);
			}
		}
		this.deleteChildren(elementId, parentList);
	}

	/**
	 * Utility method to remove all child nodes from the parent dataTable list.
	 */
	private void contractNodeActionForTasksToDelete() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");

		ArrayList<HashMap<String, Object>> parentList = new ArrayList<HashMap<String, Object>>();
		parentList.addAll(this.displayContentToDelete);

		/* Removes element which we want to contract from the parent list */
		for (HashMap<String, Object> currentElement : this.displayContentToDelete) {

			if (currentElement.get("id").equals(elementId)
					&& currentElement.get("nodeType").equals("node")) {
				currentElement.put("expansionImage",
						ProjectAdvancementBean.CONTRACT_TABLE_ARROW);
				parentList.remove(currentElement);
			}
		}
		this.deleteChildren(elementId, parentList);
	}

	/**
	 * Hides children for a specific row of the expandable table Used to
	 * simulate contraction behaviour
	 * 
	 * @param _parentId
	 *            identifier of current row
	 * @param parentList
	 *            parent children
	 */
	public void deleteChildren(String _parentId,
			ArrayList<HashMap<String, Object>> parentList) {
		for (HashMap<String, Object> child : parentList) {
			if (child.get("parentId").equals(_parentId)) {
				this.displayContent.remove(child);
				deleteChildren((String) child.get("id"), parentList);
			}
			if (child.get("id").equals(_parentId)) {
				child.put("expansionImage",
						ProjectAdvancementBean.CONTRACT_TABLE_ARROW);
				this.isExpanded.put((String) child.get("id"), false);
			}
		}
	}

	/**
	 * Hides children for a specific row of the expandable table Used to
	 * simulate contraction behaviour
	 * 
	 * @param _parentId
	 *            identifier of current row
	 * @param parentList
	 *            parent children
	 */
	public void deleteChildrenForTasksToDelete(String _parentId,
			ArrayList<HashMap<String, Object>> parentList) {
		for (HashMap<String, Object> child : parentList) {
			if (child.get("parentId").equals(_parentId)) {
				this.displayContentToDelete.remove(child);
				deleteChildren((String) child.get("id"), parentList);
			}
			if (child.get("id").equals(_parentId)) {
				child.put("expansionImage",
						ProjectAdvancementBean.CONTRACT_TABLE_ARROW);
				this.isExpanded.put((String) child.get("id"), false);
			}
		}
	}

	/**
	 * Save the modification on the page tabAffectTask.jspx
	 */
	public void saveTasksAffectation() {
		String taskId;
		ConcreteTaskDescriptor ctd;
		//booleen servent a savoir si les affectations et suppressions se sont bien passé
		boolean reg = true;

		ConcreteTaskDescriptorService ctService = this
				.getConcreteTaskDescriptorService();

		String wilosUserId = (String) WebSessionService
				.getAttribute(WebSessionService.WILOS_USER_ID);

		Participant parti = this.participantService.getParticipant(wilosUserId);

		// for (HashMap<String, Object> ligne : this.tabTasksAffected) {
		for (HashMap<String, Object> ligne : this.displayContent) {
			if ((Boolean) ligne.get("visibleCheckbox")) {
				if (!(Boolean) ligne.get("enabled")) {
					Boolean state = (Boolean) ligne.get("taskState");
					// if task state is created
					if (!state) {
						taskId = (String) ligne.get("id");
						ctd = ctService.getConcreteTaskDescriptor(taskId);
						if (ctd != null) {
							// if current user want to be affected for this task
							if ((Boolean) ligne.get("isAffected")) {
								ctd = ctService.affectedConcreteTaskDescriptor(ctd,parti);
								//verifiaction que l'affectation c'est bien passé
								if(ctd == null || !ctd.getIsParticipant()){
									reg = false;
								}
							}
							// if the current user want to be dissociate from
							// the task
							// (chekbox not check)
							else {
								ctd.setMainConcreteRoleDescriptor(null);
								ctService.saveConcreteTaskDescriptor(ctd);
							}
						}else{
							reg = false;
						}
					}
				}
			}
		}
		if(reg){
			WebCommonService.addInfoMessage(LocaleBean
					.getText("concretetaskviewer.updateAffectedtab"));
		}else{
			WebCommonService.addErrorMessage(LocaleBean
					.getText("concretetaskviewer.updateAffectedtab.failed"));
		}
		
		this.rebuildProjectTree();
		this.refreshProjectTable();
	}

	/**
	 * Delete tasks selected
	 */
	public void deleteSelectedTasks(ActionEvent event) {
		String taskId;

		ConcreteTaskDescriptor ctd;

		ConcreteTaskDescriptorService ctService = this
				.getConcreteTaskDescriptorService();
		
		//permet de savoir si les suppressions se sont bien passées
		boolean reg = true;
		
		for (HashMap<String, Object> ligne : this.displayContentToDelete) {
			if ((Boolean) ligne.get("visibleCheckbox")) {

				taskId = (String) ligne.get("id");
				ctd = ctService.getConcreteTaskDescriptor(taskId);
				if (ctd != null) {
					// if the task is selected to delete it
					if ((Boolean) ligne.get("toDelete")) {
						//on essaye de supprimer le rôle
						ctd = ctService.deleteConcreteTaskDescriptor(ctd);
						//on vérifie que ca c'est bien passé
						if(ctd != null){
							reg = false;
						}
					}
				}

			}
		}
		if(reg){
			WebCommonService.addInfoMessage(LocaleBean
					.getText("concretetaskviewer.delete.task.done"));
		}else{
			WebCommonService.addErrorMessage(LocaleBean
					.getText("concretetaskviewer.delete.task.done.failed"));
		}
		this.rebuildProjectTree();
		this.refreshProjectTable();
		this.visibleDeleteTasksPopup = false;
	}

	/**
	 * Return a collection of task not started or not finished
	 * 
	 * @return List of HashMap
	 */
	public List<HashMap<String, Object>> getLateTasks() {
		List<HashMap<String, Object>> listHashMapLateTask = new ArrayList<HashMap<String, Object>>();
		List<ConcreteTaskDescriptor> tabConcreteTaskDescriptor;
		Date ladate = new Date();
		boolean testDate = false;
		boolean testState = false;
		boolean dateIsNull = false;

		ConcreteTaskDescriptorService ctService = this
				.getConcreteTaskDescriptorService();
		String projectID = (String) WebSessionService
				.getAttribute(WebSessionService.PROJECT_ID);
		tabConcreteTaskDescriptor = ctService
				.getAllConcreteTaskDescriptorsForProject(projectID);

		for (ConcreteTaskDescriptor ctd : tabConcreteTaskDescriptor) {
			if (this.getSelectedMode().equals(START_MODE)) {
				dateIsNull = (ctd.getPlannedStartingDate() != null);
			} else {
				dateIsNull = (ctd.getPlannedFinishingDate() != null);
			}
			if (dateIsNull) {

				if (this.getSelectedMode().equals(START_MODE)) {
					testDate = ctd.getPlannedStartingDate().before(ladate);
					testState = (ctd.getState().equals("Created"))
							|| (ctd.getState().equals("Ready"));
				} else if (this.getSelectedMode().equals(FINISH_MODE)) {
					testState = (ctd.getState().equals("Created"))
							|| (ctd.getState().equals("Ready"))
							|| (ctd.getState().equals("Started"));
					testDate = ctd.getPlannedFinishingDate().before(ladate);
				}
				if (testState) {
					if (testDate) {
						HashMap<String, Object> tmpHashMap = new HashMap<String, Object>();
						tmpHashMap.put("taskName", ctd.getConcreteName());
						// date planned for a project director
						tmpHashMap.put("taskPlannedStart", ctd
								.getPlannedStartingDate());
						tmpHashMap.put("taskPlannedFinish", ctd
								.getPlannedFinishingDate());
						tmpHashMap.put("taskPlannedUserFinish", ctd
								.getPlannedUserFinishingDate());
						tmpHashMap.put("taskRealStartingDate", ctd
								.getRealStartingDate());
						ParticipantService ps = this.participantService;
						List<Participant> lp = ps.getParticipants();
						for (Participant p : lp) {
							if (ctService.checkAffectationToRole(ctd, p)) {
								tmpHashMap.put("ParticipantName", p.getName());
							}
						}
						listHashMapLateTask.add(tmpHashMap);
					}
				}
			}
		}
		return listHashMapLateTask;
	}

	/**
	 * Method which return the selectedMode
	 * 
	 * @return String
	 */
	public String getSelectedMode() {
		return selectedMode;
	}

	/**
	 * Method which set the selectedMode
	 * 
	 * @param selectedMode
	 */
	public void setSelectedMode(String selectedMode) {
		this.selectedMode = selectedMode;
	}

	/**
	 * Method which return the list of all the possible mode
	 * 
	 * @return List<SelectItem>
	 */
	public List<SelectItem> getModesList() {
		ArrayList<SelectItem> modesList = new ArrayList<SelectItem>();

		modesList.add(new SelectItem(START_MODE, LocaleBean
				.getText("tablatetask.checkboxlabel.start")));
		modesList.add(new SelectItem(FINISH_MODE, LocaleBean
				.getText("tablatetask.checkboxlabel.finish")));

		return modesList;
	}

	/**
	 * Method which change the current mode of the concreteTask and set it to
	 * the specified value
	 * 
	 * @param evt
	 */
	public void changeModeActionListener(ValueChangeEvent evt) {
		this.selectedMode = (String) evt.getNewValue();
		if (this.selectedMode.equals(START_MODE)) {
			WebSessionService.setAttribute(WebSessionService.TREE_MODE,
					START_MODE);
		} else {
			WebSessionService.setAttribute(WebSessionService.TREE_MODE,
					FINISH_MODE);
		}
	}

	/**
	 * Method which return the TaskLateAffectView
	 * 
	 * @return String
	 */
	public String getTaskLateAffectView() {
		List<HashMap<String, Object>> temp = getLateTasks();
		if (temp.size() == 0) {
			this.setTaskLateAffectView("affected_no_records_view");
		} else {
			this.setTaskLateAffectView("affected_records_view");
		}
		return taskLateAffectView;
	}

	/**
	 * Method which set the taskLateAffecteView
	 * 
	 * @param taskLateAffectView
	 */
	public void setTaskLateAffectView(String taskLateAffectView) {
		this.taskLateAffectView = taskLateAffectView;
	}

	/**
	 * Method which return the dailyRemainingTimeService
	 * 
	 * @return DailyRemainingTimeService
	 */
	public DailyRemainingTimeService getDailyRemainingTimeService() {
		return dailyRemainingTimeService;
	}

	/**
	 * Method which set the dailyRemainingTimeService
	 * 
	 * @param _dailyRemainingTimeService
	 */
	public void setDailyRemainingTimeService(
			DailyRemainingTimeService _dailyRemainingTimeService) {
		dailyRemainingTimeService = _dailyRemainingTimeService;
	}

	/**
	 * Method to update or save
	 * 
	 * @param _ctd
	 */
	public void saveOrUpdateDaily(ConcreteTaskDescriptor _ctd) {
		DailyRemainingTime drt;

		drt = this.concreteTaskDescriptorService.getDailyRemainingTime(_ctd);
		if (drt != null) {
			drt
					.setRemainingTime(this.concreteTaskDescriptor
							.getRemainingTime());
		} else {
			drt = new DailyRemainingTime();
			drt.setDailyTaskDescriptor_id(_ctd);
			drt.setDate(new Date());
			// if (_ctd.getState().equals("Started"))
			// {
			// drt.setRemainingTime(_ctd.getPlannedUserTime());
			// }
			// else
			// {
			drt.setRemainingTime(_ctd.getRemainingTime());
			// }
		}
		this.dailyRemainingTimeService.saveDailyRemainingTime(drt);
	}

	/**
	 * Method which return true if the concreteTask has dependencies, that is to
	 * say predecessors or successors
	 * 
	 * @return boolean
	 */
	public boolean getHasConcreteDependencies() {
		return !(this.getConcretePredecessors().isEmpty() && this
				.getConcreteSuccessors().isEmpty());
	}

	/**
	 * Method which return true if the concreteTask has predecessors, else false
	 * 
	 * @return boolean
	 */
	public boolean getHasConcretePredecessors() {
		return !this.getConcretePredecessors().isEmpty();
	}

	/**
	 * Method which return true if the concreteTask has successors, else false
	 * 
	 * @return boolean
	 */
	public boolean getHasConcreteSuccessors() {
		return !this.getConcreteSuccessors().isEmpty();
	}

	/**
	 * @return the concretePredecessors
	 */
	public List<HashMap<String, Object>> getConcretePredecessors() {
		return this.concreteWorkBreakdownElementService
				.getConcretePredecessorHashMap(this.concreteTaskDescriptor);
	}

	/**
	 * @param _concretePredecessors
	 *            the concretePredecessors to set
	 */
	public void setConcretePredecessors(
			List<HashMap<String, Object>> _concretePredecessors) {
		concretePredecessors = _concretePredecessors;
	}

	/**
	 * @return the concreteSuccessors
	 */
	public List<HashMap<String, Object>> getConcreteSuccessors() {
		return this.concreteWorkBreakdownElementService
				.getConcreteSuccessorHashMap(this.concreteTaskDescriptor);
	}

	/**
	 * @param _concreteSuccessors
	 *            the concreteSuccessors to set
	 */
	public void setConcreteSuccessors(
			List<HashMap<String, Object>> _concreteSuccessors) {
		concreteSuccessors = _concreteSuccessors;
	}

	/**
	 * @return the concreteWorkBreakdownElementService
	 */
	public ConcreteWorkBreakdownElementService getConcreteWorkBreakdownElementService() {
		return concreteWorkBreakdownElementService;
	}

	/**
	 * @param _concreteWorkBreakdownElementService
	 *            the concreteWorkBreakdownElementService to set
	 */
	public void setConcreteWorkBreakdownElementService(
			ConcreteWorkBreakdownElementService _concreteWorkBreakdownElementService) {
		concreteWorkBreakdownElementService = _concreteWorkBreakdownElementService;
	}

	public List<HashMap<String, Object>> getWorkProducts() {
		Project project = super.getProjectService().getProject(
				(String) WebSessionService
						.getAttribute(WebSessionService.PROJECT_ID));
		
		return this.concreteTaskDescriptorService
				.getWorkProductsForAConcreteTask(this.concreteTaskDescriptor,
						project.getConsiderWorkProductAndTaskLinks());

	}

	/**
	 * @return the workProductDescriptorService
	 */
	public WorkProductDescriptorService getWorkProductDescriptorService() {
		return this.workProductDescriptorService;
	}

	/**
	 * @param _workProductDescriptorService
	 *            the workProductDescriptorService to set
	 */
	public void setWorkProductDescriptorService(
			WorkProductDescriptorService _workProductDescriptorService) {
		this.workProductDescriptorService = _workProductDescriptorService;
	}

	/**
	 * @return the concreteWorkProductDescriptorService
	 */
	public ConcreteWorkProductDescriptorService getConcreteWorkProductDescriptorService() {
		return this.concreteWorkProductDescriptorService;
	}

	/**
	 * @param _concreteWorkProductDescriptorService
	 *            the concreteWorkProductDescriptorService to set
	 */
	public void setConcreteWorkProductDescriptorService(
			ConcreteWorkProductDescriptorService _concreteWorkProductDescriptorService) {
		this.concreteWorkProductDescriptorService = _concreteWorkProductDescriptorService;
	}

	/**
	 * @return true if workProduct is empty
	 */
	public boolean isEmptyWorkProductTable() {
		return this.getWorkProducts().isEmpty();
	}
	
	/**
	 * @return the displayWorkProductTable
	 */
	public boolean isDisplayWorkProductTable() {
		return this.displayWorkProductTable && !this.isEmptyWorkProductTable();
	}

	/**
	 * @param _displayWorkProductTable
	 *            the displayWorkProductTable to set
	 */
	public void setDisplayWorkProductTable(boolean _displayWorkProductTable) {
		this.displayWorkProductTable = _displayWorkProductTable;
	}

	/**
	 * Method which hide or show the workProductTable by changing the concerned
	 * attribute
	 * 
	 * @param evt
	 */
	public void changeWorkProductTableListener(ValueChangeEvent evt) {
		this.displayWorkProductTable = !(this.displayWorkProductTable);
	}

	/**
	 * @return the visibleMandatoryWPPopup
	 */
	public boolean getVisibleMandatoryWPPopup() {
		return this.visibleMandatoryWPPopup;
	}

	/**
	 * @param _visibleMandatoryWPPopup
	 *            the visibleMandatoryWPPopup to set
	 */
	public void setVisibleMandatoryWPPopup(boolean _visibleMandatoryWPPopup) {
		this.visibleMandatoryWPPopup = _visibleMandatoryWPPopup;
	}

	/**
	 * Method which hide the pop-up by setting the concerned attributes to false
	 * when a specified event is received
	 * 
	 * @param _event
	 */
	public void confirmWorkProductButton(ActionEvent _event) {
		this.visibleMandatoryWPPopup = false;
		this.visibleWrongStateMandatoryWPPopup = false;
		this.visibleOutputWPPopup = false;
		this.visibleWrongStateOutputWPPopup = false;
	}

	/**
	 * @return the visibleWrongStateMandatoryWPPopup
	 */
	public boolean isVisibleWrongStateMandatoryWPPopup() {
		return this.visibleWrongStateMandatoryWPPopup;
	}

	/**
	 * @param _visibleWrongStateMandatoryWPPopup
	 *            the visibleWrongStateMandatoryWPPopup to set
	 */
	public void setVisibleWrongStateMandatoryWPPopup(
			boolean _visibleWrongStateMandatoryWPPopup) {
		this.visibleWrongStateMandatoryWPPopup = _visibleWrongStateMandatoryWPPopup;
	}

	/**
	 * @return the visibleWrongStateOutputWPPopup
	 */
	public boolean isVisibleWrongStateOutputWPPopup() {
		return this.visibleWrongStateOutputWPPopup;
	}

	/**
	 * @param _visibleWrongStateOutputWPPopup
	 *            the visibleWrongStateOutputWPPopup to set
	 */
	public void setVisibleWrongStateOutputWPPopup(
			boolean _visibleWrongStateOutputWPPopup) {
		this.visibleWrongStateOutputWPPopup = _visibleWrongStateOutputWPPopup;
	}

	/**
	 * @return the visibleOutputWPPopup
	 */
	public boolean isVisibleOutputWPPopup() {
		return this.visibleOutputWPPopup;
	}

	/**
	 * @param _visibleOutputWPPopup
	 *            the visibleOutputWPPopup to set
	 */
	public void setVisibleOutputWPPopup(boolean _visibleOutputWPPopup) {
		this.visibleOutputWPPopup = _visibleOutputWPPopup;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	/**
	 * put the attribut project modified to true which force the project table
	 * to be recalculated
	 * 
	 */
	public void refreshProjectTable() {
		this.projectModified = true;
	}

	/**
	 * Toggles the expanded state of this ConcreteBreakDownElement.
	 * 
	 * @param event
	 */
	public void toggleSubGroupAction(ActionEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");

		// toggle expanded state
		Boolean b = isExpanded.get(elementId);
		if (b == null) {
			isExpanded.put(elementId, false);
			b = isExpanded.get(elementId);
		}
		b = !b;
		isExpanded.put(elementId, b);

		// add sub elements to list
		if (b) {
			expandNodeAction();
		}
		// remove items from list
		else {
			contractNodeAction();
		}
	}
	
	/**
	 * Toggles the expanded state of all ConcreteActivity.
	 * 
	 * @param _event ActionEvent wich activate the action of expand button
	 */
	public void toggleAllSubGroupAction(ActionEvent _event) {
		String elementId = null;							// id of the current task
        Boolean taskExpanded = null;						// test if the current task is expanded or no
        ArrayList<HashMap<String, Object>> taskList ;		// list of all task
        Boolean existsContractNode = true ;					// true if a node exist and then expand this node
       
        ArrayList<HashMap<String,Object>> nom_liste = displayContent;
        
        taskList = new ArrayList<HashMap<String, Object>>();
        while (existsContractNode) {
        	// expand the currently
            existsContractNode = false ;
            taskList.clear();	//TODO : a optimiser
            taskList.addAll(this.displayContent);
            // expand all item present in the current node
            for (HashMap<String, Object> hm : taskList) {
                if (((String) hm.get("nodeType")).equals("node")) {
                	//if the item is a node expand all item present in the current node
                    if (((String) hm.get("expansionImage")).equals(CONTRACT_TABLE_ARROW)) {
                        existsContractNode = true;
                        taskExpanded = null;
                        elementId = (String) hm.get("id");
                        // toggle expanded state
                        taskExpanded = isExpanded.get(elementId);
                        if (taskExpanded == null) {
                            isExpanded.put(elementId, true);
                            taskExpanded = isExpanded.get(elementId);
                        } else {
                            if (!taskExpanded) {
                                taskExpanded = true;
                            }
                            isExpanded.put(elementId, taskExpanded);
                        }
                        if (taskExpanded) {
                            expandNodeAction(elementId,nom_liste);
                        }
                    }
                }
            }
        }
	}

	
	/**
	 * Toggles the expanded state of all ConcreteActivity.
	 * 
	 * @param _event ActionEvent wich activate the action of expand button
	 */
	public void toggleAllSubGroupActionToDelete(ActionEvent _event) {
		String elementId = null;							// id of the current task
        Boolean taskExpanded = null;						// test if the current task is expanded or no
        ArrayList<HashMap<String, Object>> taskList ;		// list of all task
        Boolean existsContractNode = true ;					// true if a node exist and then expand this node
       
        ArrayList<HashMap<String,Object>> nom_liste = displayContentToDelete;
        
        taskList = new ArrayList<HashMap<String, Object>>();
        while (existsContractNode) {
        	// expand the currently
            existsContractNode = false ;
            taskList.clear();	//TODO : a optimiser
            taskList.addAll(this.displayContentToDelete);
            // expand all item present in the current node
            for (HashMap<String, Object> hm : taskList) {
                if (((String) hm.get("nodeType")).equals("node")) {
                	//if the item is a node expand all item present in the current node
                    if (((String) hm.get("expansionImage")).equals(CONTRACT_TABLE_ARROW)) {
                        existsContractNode = true;
                        taskExpanded = null;
                        elementId = (String) hm.get("id");
                        // toggle expanded state
                        taskExpanded = isExpanded.get(elementId);
                        if (taskExpanded == null) {
                            isExpanded.put(elementId, true);
                            taskExpanded = isExpanded.get(elementId);
                        } else {
                            if (!taskExpanded) {
                                taskExpanded = true;
                            }
                            isExpanded.put(elementId, taskExpanded);
                        }
                        if (taskExpanded) {
                        	expandNodeActionForTasksToDelete(elementId,nom_liste);
                        }
                    }
                }
            }
        }
	}
	
	
	/**
	 * Toggles the expanded state of this ConcreteBreakDownElement.
	 * 
	 * @param event
	 */
	public void toggleSubGroupActionToDelete(ActionEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String elementId = (String) map.get("elementId");

		// toggle expanded state
		Boolean b = isExpanded.get(elementId);
		if (b == null) {
			isExpanded.put(elementId, false);
			b = isExpanded.get(elementId);
		}
		b = !b;
		isExpanded.put(elementId, b);

		// add sub elements to list
		if (b) {
			expandNodeActionForTasksToDelete();
		}
		// remove items from list
		else {
			contractNodeActionForTasksToDelete();
		}
	}

	/**
	 * Get the selected mode filter
	 * 
	 * @return the mode filter
	 */
	public String getSelectedModeFilter() {
		return selectedModeFilter;
	}

	/**
	 * Set the current selected mode filter
	 * 
	 * @param selectedModeFilter
	 *            the new selected mode filter
	 */
	public void setSelectedModeFilter(String selectedModeFilter) {
		this.selectedModeFilter = selectedModeFilter;
	}

	/**
	 * this method allow to change the current task mode
	 * 
	 * @param evt
	 */
	public void changeModeFilterActionListener(ValueChangeEvent evt) {

		this.selectedModeFilter = (String) evt.getNewValue();
		if (this.selectedModeFilter.equals(ALL_TASK_MODE)) {
			WebSessionService.setAttribute(WebSessionService.TREE_MODE,
					ALL_TASK_MODE);
			refreshProjectTable();
		} else {
			WebSessionService.setAttribute(WebSessionService.TREE_MODE,
					MY_TASK_MODE);
			refreshProjectTable();
		}
	}

	/**
	 * this method allow to return all the available mode for the workProduct
	 * 
	 * @return List<SelectItem> of mode
	 */
	public List<SelectItem> getModesFilterList() {
		ArrayList<SelectItem> modesFilterList = new ArrayList<SelectItem>();

		modesFilterList.add(new SelectItem(ALL_TASK_MODE, LocaleBean
				.getText("tabAffectTask.checkboxlabel.allTask")));
		modesFilterList.add(new SelectItem(MY_TASK_MODE, LocaleBean
				.getText("tabAffectTask.checkboxlabel.myTask")));

		return modesFilterList;
	}

	public void setVisibleNameParti(boolean visibleNameParti) {
		this.visibleNameParti = visibleNameParti;
	}

	public boolean getVisibleNameParti() {
		if (this.concreteTaskDescriptor.getMainConcreteRoleDescriptor()==null){
			visibleNameParti = false;
		}else{
			visibleNameParti = true;
		}
		return visibleNameParti;
	}

}
