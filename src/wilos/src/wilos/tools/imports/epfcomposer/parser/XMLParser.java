/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007-2008 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

package wilos.tools.imports.epfcomposer.parser;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
import wilos.model.spem2.task.Step;
import wilos.model.spem2.task.TaskDefinition;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.model.spem2.workbreakdownelement.WorkBreakdownElement;
import wilos.model.spem2.workbreakdownelement.WorkOrder;
import wilos.model.spem2.workbreakdownelement.WorkOrderId;
import wilos.model.spem2.workproduct.WorkProductDefinition;
import wilos.model.spem2.workproduct.WorkProductDescriptor;
import wilos.tools.imports.epfcomposer.fillers.FillerActivity;
import wilos.tools.imports.epfcomposer.fillers.FillerCheckList;
import wilos.tools.imports.epfcomposer.fillers.FillerElement;
import wilos.tools.imports.epfcomposer.fillers.FillerGuidance;
import wilos.tools.imports.epfcomposer.fillers.FillerIteration;
import wilos.tools.imports.epfcomposer.fillers.FillerMilestone;
import wilos.tools.imports.epfcomposer.fillers.FillerPhase;
import wilos.tools.imports.epfcomposer.fillers.FillerProcess;
import wilos.tools.imports.epfcomposer.fillers.FillerRole;
import wilos.tools.imports.epfcomposer.fillers.FillerRoleDescriptor;
import wilos.tools.imports.epfcomposer.fillers.FillerStep;
import wilos.tools.imports.epfcomposer.fillers.FillerTask;
import wilos.tools.imports.epfcomposer.fillers.FillerTaskDescriptor;
import wilos.tools.imports.epfcomposer.fillers.FillerWorkProduct;
import wilos.tools.imports.epfcomposer.fillers.FillerWorkProductDescriptor;
import wilos.tools.imports.epfcomposer.utils.XMLUtils;

/**
 * Class XMLParser a static class designed to Parse an XML File
 * 
 * @author SPELP Team
 */
public class XMLParser {
    /* Constants used to parse the XML File */
    // XPaths Paths
    private static final String xpath_roleDescriptor = "//BreakdownElement[@*[namespace-uri() and local-name()='type']='uma:RoleDescriptor']";

    private static final String xpath_taskDescriptor = "//BreakdownElement[@*[namespace-uri() and local-name()='type']='uma:TaskDescriptor']";

    private static final String xpath_roleDefinition = "//ContentElement[@*[namespace-uri() and local-name()='type']='uma:Role' ]";

    private static final String xpath_taskDefinition = "//ContentElement[@*[namespace-uri() and local-name()='type']='uma:Task']";

    private static final String xpath_deliveryProcess = "//Process[@*[namespace-uri() and local-name()='type']='uma:DeliveryProcess']";

    private static final String xpath_iteration = "//BreakdownElement[@*[namespace-uri() and local-name()='type']='uma:Iteration']";

    private static final String xpath_phase = "//BreakdownElement[@*[namespace-uri() and local-name()='type']='uma:Phase']";

    private static final String xpath_activity = "//BreakdownElement[@*[namespace-uri() and local-name()='type']='uma:Activity' or "
	    + "@*[namespace-uri() and local-name()='type']='uma:CapabilityPattern' "
	    + "]";

    private static final String xpath_milestone = "//BreakdownElement[@*[namespace-uri() and local-name()='type']='uma:Milestone']";

    private static final String xpath_guidance = "//ContentElement[@*[namespace-uri() and local-name()='type']='uma:Guideline' or "
	    + "@*[namespace-uri() and local-name()='type']='uma:Concept' or "
	    + "@*[namespace-uri() and local-name()='type']='uma:SupportingMaterial' or "
	    + "@*[namespace-uri() and local-name()='type']='uma:Roadmap' or "
	    + "@*[namespace-uri() and local-name()='type']='uma:TermDefinition' or "
	    + "@*[namespace-uri() and local-name()='type']='uma:ToolMentor' or "
	    + "@*[namespace-uri() and local-name()='type']='uma:Whitepaper' or "
	    + "@*[namespace-uri() and local-name()='type']='uma:ReusableAsset' or "
	    + "@*[namespace-uri() and local-name()='type']='uma:Report' or "
	    + "@*[namespace-uri() and local-name()='type']='uma:Template' or "
	    + "@*[namespace-uri() and local-name()='type']='uma:Example' or "
	    + "@*[namespace-uri() and local-name()='type']='uma:Checklist' "
	    + "]";

    private static final String xpath_workProductDescriptorFake = "//ContentElement[@*[namespace-uri() and local-name()='type']='uma:Artifact']";

    private static final String xpath_workProductDefinition = "//ContentElement[@*[namespace-uri() and local-name()='type']='uma:Artifact' ]";
    private static final String xpath_workProductDescriptor = "//BreakdownElement[@*[namespace-uri() and local-name()='type']='uma:WorkProductDescriptor']";

    // Sections
    public static final String task = "Task";

    public static final String role = "Role";

    public static final String workProduct = "WorkProduct";

    public static final String responsibleFor = "ResponsibleFor";

    public static final String performedPrimarilyBy = "PerformedPrimarilyBy";

    public static final String additionallyPerformedBy = "AdditionallyPerformedBy";

    public static final String step = "Section";

    public static final String presentation = "Presentation";

    public static final String breakdownElement = "BreakdownElement";

    public static final String predecessor = "Predecessor";

    public static final String maindescription = "MainDescription";

    // public static final String output = "Output";

    public static final String section = "Section";

    public static final String description = "Description";

    public static final String output = "Output";

    public static final String optionalInput = "OptionalInput";

    public static final String mandatoryInput = "MandatoryInput";

    private static TreeSet<String> guidancesTypes = new TreeSet<String>();

    // Types
    private static final String process = "uma:DeliveryProcess";

    private static final String phase = "uma:Phase";

    private static final String activity = "uma:Activity";

    private static final String milestone = "uma:Milestone";

    private static final String task_descriptor = "uma:TaskDescriptor";

    private static final String role_descriptor = "uma:RoleDescriptor";

    private static final String workProduct_descriptor = "uma:WorkProductDescriptor";

    private static final String iteration = "uma:Iteration";

    private static final String checklist = "uma:Checklist";

    private static final String capability_pattern = "uma:CapabilityPattern";

    // Attributes Names
    private static final String id = "id";

    private static final String attr_name_xsitype = "xsi:type";

    private static final String attr_name_variabilityBasedOnElement = "variabilityBasedOnElement";

    private static final String attr_name_linkType = "linkType";

    /* Variables recording elements */
    // Filled by fillTaskDefinitionsList and fillRoleDefinitionsList
    protected static Vector<TaskDefinition> TaskDefinitionsList = new Vector<TaskDefinition>();

    protected static Vector<RoleDefinition> RoleDefinitionsList = new Vector<RoleDefinition>();

    protected static Vector<WorkProductDefinition> WorkProductDefinitionsList = new Vector<WorkProductDefinition>();

    protected static Vector<Guidance> GuidancesList = new Vector<Guidance>();

    protected static Vector<WorkProductDescriptorFake> WorkProductDescriptorFakesList = new Vector<WorkProductDescriptorFake>();

    // this variables contain all the Elements that concern them
    protected static TreeSet<RoleDescriptor> roleDescriptorsList;

    protected static TreeSet<TaskDescriptor> taskDescriptorsList;

    protected static TreeSet<WorkProductDescriptor> workProductDescriptorsList;

    protected static TreeSet<Phase> phasesList;

    protected static TreeSet<Iteration> iterationsList;

    protected static TreeSet<Activity> activitiesList;

    protected static TreeSet<Milestone> milestonesList;

    protected static TreeSet<WorkBreakdownElement> workBreakdownElementList = new TreeSet<WorkBreakdownElement>();

    /**
     * Fills the Lists in memory To launch before everything by the parsing
     * method
     */
    private static void fillAllElementsList() {
	try {
	    FillerElement.initializeOrder();
	    initGuidancesTypesList();
	    
	    workBreakdownElementList.clear();

	    GuidancesList = fillGuidesList();

	    WorkProductDefinitionsList = fillWorkProductDefinitionsList();

	    RoleDefinitionsList = fillRoleDefinitionsList();

	    TaskDefinitionsList = fillTaskDefinitionsList();

	    WorkProductDescriptorFakesList = fillWorkProductDescriptorFakesList();

	    workProductDescriptorsList = fillWorkProductDescriptorsList();

	    // roleDescriptorsList = fillRoleDescriptorsList();
	    roleDescriptorsList = fillRoleDescriptorsList(workProductDescriptorsList);
	    taskDescriptorsList = fillTaskDescriptorsList(roleDescriptorsList,
		    workProductDescriptorsList);
	    workBreakdownElementList.addAll(taskDescriptorsList);

	    phasesList = fillPhasesList();
	    workBreakdownElementList.addAll(phasesList);

	    iterationsList = fillIterationsList();
	    workBreakdownElementList.addAll(iterationsList);

	    activitiesList = fillActivitiesList();
	    workBreakdownElementList.addAll(activitiesList);

	    milestonesList = fillMilestonesList();
	    workBreakdownElementList.addAll(milestonesList);

	    setAllTaskDescriptorsDependencies(taskDescriptorsList);
	    setAllPhasesDependencies(phasesList);
	    setAllIterationsDependencies(iterationsList);
	    setAllActivitiesDependencies(activitiesList);
	    setAllMilestonesDependencies(milestonesList);

	} catch (Exception e) {
	    // e.printStackTrace();
	}
    }

    /**
     * @return
     */
    private static TreeSet<WorkProductDescriptor> fillWorkProductDescriptorsList() {
	// List of the workProduct to be return
	TreeSet<WorkProductDescriptor> workProductList = new TreeSet<WorkProductDescriptor>();

	// get list of nodes by the XPath expression
	NodeList workProductDescriptors = (NodeList) XMLUtils.evaluate(
		xpath_workProductDescriptor, XPathConstants.NODESET);

	Node aNode;
	// for each node
	if (workProductDescriptors != null) {
	    for (int i = 0; i < workProductDescriptors.getLength(); i++) {
		aNode = workProductDescriptors.item(i);
		// Fills the WorkProductDescriptor
		WorkProductDescriptor aWorkProductDescriptor = new WorkProductDescriptor();
		FillerWorkProductDescriptor aFiller = new FillerWorkProductDescriptor(
			aWorkProductDescriptor, aNode);
		WorkProductDescriptor workProductDescriptorfilled = (WorkProductDescriptor) aFiller
			.getFilledElement();

		// affect the workProduct definition to the workproduct
		// descriptor
		setWorkProductByWorkProductDescriptor(
			workProductDescriptorfilled, aNode);

		// add the current WorkProductDescriptor Object in the list to
		// be
		// return
		workProductList.add(workProductDescriptorfilled);
	    }
	}
	return workProductList;

    }

    private static Vector<WorkProductDescriptorFake> fillWorkProductDescriptorFakesList() {
	Vector<WorkProductDescriptorFake> WorkProductDescriptorFakesList; // the
	// return
	// of
	// the
	// function

	// initializes the List
	WorkProductDescriptorFakesList = new Vector<WorkProductDescriptorFake>();
	WorkProductDescriptorFakesList.clear();

	// gets all the nodes containing roleDefinions
	NodeList nodeReturned = (NodeList) XMLUtils.evaluate(
		xpath_workProductDescriptorFake, XPathConstants.NODESET);

	// For each node...
	Node aNode;
	// Through the Artifacts...
	if (nodeReturned != null) {
	    for (int i = 0; i < nodeReturned.getLength(); i++) {
		aNode = nodeReturned.item(i);

		NodeList ArtifactChildren = aNode.getChildNodes();

		// Trough the Artifacts Children...
		for (int j = 0; j < ArtifactChildren.getLength(); j++) {
		    // Inside Presentation
		    if (ArtifactChildren.item(j).getNodeName().equals(
			    XMLParser.presentation)) {
			// System.out.println("Inside presentation");
			NodeList presentationChildren;
			presentationChildren = ArtifactChildren.item(j)
				.getChildNodes();
			// Trough the Presentation Children
			for (int k = 0; k < presentationChildren.getLength(); k++) {
			    // This Artifact contains at least a template or an
			    // example
			    if (presentationChildren.item(k).getNodeName()
				    .equals(Guidance.template)
				    || presentationChildren.item(k)
					    .getNodeName().equals(
						    Guidance.example)) {

				// We get the Id of the WorkProduct
				String artifactGuid;

				if (aNode.getAttributes().getNamedItem(
					attr_name_variabilityBasedOnElement) != null) {
				    // the current node contains the attribute:
				    // variabilityBasedOnElement in the XML file
				    artifactGuid = aNode
					    .getAttributes()
					    .getNamedItem(
						    attr_name_variabilityBasedOnElement)
					    .getNodeValue();
				} else {
				    artifactGuid = aNode.getAttributes()
					    .getNamedItem(id).getNodeValue();
				}

				Guidance theGuidance = getGuidanceById(presentationChildren
					.item(k).getTextContent());

				boolean alreadyExists = false;
				for (WorkProductDescriptorFake WPDF : WorkProductDescriptorFakesList) {
				    if (WPDF.Guid.equals(artifactGuid)) {
					WPDF.addTemplateOrExample(theGuidance);
					alreadyExists = true;
				    }
				}

				if (!alreadyExists) {
				    WorkProductDescriptorFake WPDF = (new XMLParser()).new WorkProductDescriptorFake();
				    WPDF.setGuid(artifactGuid);
				    WPDF.addTemplateOrExample(theGuidance);
				    WorkProductDescriptorFakesList.add(WPDF);
				}
			    }
			}
		    } else if (ArtifactChildren.item(j).getNodeName().equals(
			    Guidance.template)
			    || ArtifactChildren.item(j).getNodeName().equals(
				    Guidance.example)) {

			// We get the Id of the WorkProduct
			String artifactGuid;

			if (aNode.getAttributes().getNamedItem(
				attr_name_variabilityBasedOnElement) != null) {
			    // the current node contains the attribute:
			    // variabilityBasedOnElement in the XML file
			    artifactGuid = aNode.getAttributes().getNamedItem(
				    attr_name_variabilityBasedOnElement)
				    .getNodeValue();
			} else {
			    artifactGuid = aNode.getAttributes().getNamedItem(
				    id).getNodeValue();
			}

			Guidance theGuidance = getGuidanceById(ArtifactChildren
				.item(j).getTextContent());

			boolean alreadyExists = false;
			for (WorkProductDescriptorFake WPDF : WorkProductDescriptorFakesList) {
			    if (WPDF.Guid.equals(artifactGuid)) {
				WPDF.addTemplateOrExample(theGuidance);
				alreadyExists = true;
			    }
			}

			if (!alreadyExists) {
			    WorkProductDescriptorFake WPDF = (new XMLParser()).new WorkProductDescriptorFake();
			    WPDF.setGuid(artifactGuid);
			    WPDF.addTemplateOrExample(theGuidance);
			    WorkProductDescriptorFakesList.add(WPDF);
			}
		    }
		}
	    }
	}
	return WorkProductDescriptorFakesList;
    }

    /**
     * make a set with the name of each type of guidance
     */
    private static void initGuidancesTypesList() {
	guidancesTypes.add(Guidance.checklist);
	guidancesTypes.add(Guidance.concept);
	guidancesTypes.add(Guidance.estimationConsiderations);
	guidancesTypes.add(Guidance.guideline);
	guidancesTypes.add(Guidance.practice);
	guidancesTypes.add(Guidance.report);
	guidancesTypes.add(Guidance.reusableAsset);
	guidancesTypes.add(Guidance.roadMap);
	guidancesTypes.add(Guidance.supportingMaterial);
	guidancesTypes.add(Guidance.termDefinition);
	guidancesTypes.add(Guidance.toolMentor);
	guidancesTypes.add(Guidance.whitepaper);
    }

    /**
     * addDependencyToWorkBreakdownElement
     * 
     * @param _aSet
     * @param taskDescriptorfilled
     * @param node
     */
    private static void addDependencyToWorkBreakdownElement(
	    WorkBreakdownElement _successor, Node _node) {

	// predecessor
	WorkBreakdownElement wbdePredecessor = null;
	// id of the predecessor
	String predecessorId = "";
	// linkType ("FinishToFinish for example")
	String linkType = "";
	NodeList childNodes = _node.getChildNodes();

	// for each node of childNodes
	for (int i = 0; i < childNodes.getLength(); i++) {
	    if (childNodes.item(i).getNodeName().equals(predecessor)) {

		// the node contains a tag Predecessor in the XML file
		predecessorId = childNodes.item(i).getTextContent();
		// get the link of the dependency
		linkType = (childNodes.item(i).getAttributes().getNamedItem(
			attr_name_linkType).getNodeValue());

		wbdePredecessor = getWorkBreakdownElementById(
			workBreakdownElementList, predecessorId);
		if (wbdePredecessor != null) {
		    WorkOrderId woID = new WorkOrderId();
		    woID.setPredecessorId(wbdePredecessor.getId());
		    woID.setSuccessorId(_successor.getId());
		    WorkOrder wo = new WorkOrder();
		    wo.setLinkType(linkType);
		    wbdePredecessor.addSuccessor(wo);
		    _successor.addPredecessor(wo);
		    wo.setWorkOrderId(woID);
		}
	    }
	}
    }

    /**
     * fillGuidesList
     * 
     * @return a Guidance vector
     */
    private static Vector<Guidance> fillGuidesList() {
	Vector<Guidance> theGuidanceList; // the return of the function

	// initializes the List
	theGuidanceList = new Vector<Guidance>();
	theGuidanceList.clear();

	// gets all the nodes containing all guidances
	NodeList nodeReturned = (NodeList) XMLUtils.evaluate(xpath_guidance,
		XPathConstants.NODESET);
	if (nodeReturned != null && nodeReturned.getLength() != 0) {
	    // For each node...
	    Node aNode;
	    for (int i = 0; i < nodeReturned.getLength(); i++) {
		aNode = nodeReturned.item(i);
		Guidance aGuidance = null;
		CheckList aCheckList;
		// test if there is a flag "checklist" in the XML file
		if (aNode.getAttributes().getNamedItem(attr_name_xsitype)
			.getNodeValue().equals(checklist)) {
		    aCheckList = new CheckList();
		    FillerCheckList aFillerCheckList = new FillerCheckList(
			    aCheckList, aNode);
		    aCheckList = (CheckList) aFillerCheckList
			    .getFilledElement();
		    aGuidance = aCheckList;

		}
		// others guidance
		else {
		    aGuidance = new Guidance();
		    FillerGuidance aFiller = new FillerGuidance(aGuidance,
			    aNode);
		    aGuidance = (Guidance) aFiller.getFilledElement();
		}
		// add the filled guidance in the list to be return
		theGuidanceList.add(aGuidance);
	    }
	}
	return theGuidanceList;
    }

    /**
     * getAllActivities
     * 
     * @return the Set of all activity
     */
    private static TreeSet<Activity> fillActivitiesList() {
	TreeSet<Activity> activitiesList = new TreeSet<Activity>();

	/* evaluate the XPAth request and return the nodeList */
	NodeList activities = (NodeList) XMLUtils.evaluate(xpath_activity,
		XPathConstants.NODESET);

	/* For each node */
	Node aNode;
	for (int i = 0; i < activities.getLength(); i++) {
	    /* for each list element , get the list item */
	    aNode = activities.item(i);

	    Activity anActivity = new Activity();
	    /* Filler for the iteration and the item (node) */
	    FillerActivity itFiller = new FillerActivity(anActivity, aNode);
	    Activity returnedActivityFilled = (Activity) itFiller
		    .getFilledElement();

	    setGuidanceByActivity(returnedActivityFilled, aNode);

	    // We
	    if (aNode.getAttributes().getNamedItem(
		    attr_name_variabilityBasedOnElement) != null) {
		// the current node contains the attribute:
		// variabilityBasedOnElement in the XML file
		String parentElementID = aNode.getAttributes().getNamedItem(
			attr_name_variabilityBasedOnElement).getNodeValue();
		// Xpath request to get the activity list by the id:
		// variabilityBasedOnElement
		String xpath_parentElement = "//Process[@*[namespace-uri() and local-name()='type']='uma:CapabilityPattern' and @id='"
			+ parentElementID + "']";
		// get the list of the nodes by the XPath Expression
		NodeList parentElement = (NodeList) XMLUtils.evaluate(
			xpath_parentElement, XPathConstants.NODESET);
		if (parentElement.getLength() == 1) {
		    // the nodeList is not empty
		    aNode = parentElement.item(0);
		    itFiller.auxilliaryFill(aNode);
		    returnedActivityFilled = (Activity) itFiller
			    .getFilledElement();
		}
	    }

	    /* Add the filled object in the result List */
	    activitiesList.add(returnedActivityFilled);

	}

	return activitiesList;
    }

    /**
     * setGuidanceByActivity
     * 
     * @param anActivity
     * @param node
     */
    private static void setGuidanceByActivity(Activity anActivity, Node node) {
	NodeList listOfTdNodes = node.getChildNodes();
	Guidance GuideTobereturn = null;

	String idGuide = "";

	// search the nodes of the guide
	for (int i = 0; i < listOfTdNodes.getLength(); i++) {
	    if (guidancesTypes.contains(listOfTdNodes.item(i).getNodeName())) {
		// recuperation des differents id des guidelines
		idGuide = listOfTdNodes.item(i).getTextContent();

		GuideTobereturn = getGuidanceById(idGuide);
		// if the guideline doesn't exist
		if (GuideTobereturn != null) {
		    // set the guideline in the taskDefinition
		    anActivity.addGuidance(GuideTobereturn);
		}
	    }
	}
    }

    /**
     * setAllActivitiesDependencies
     * 
     * @param aSet
     */
    private static void setAllActivitiesDependencies(Set<Activity> _aSet) {

	// evaluate the XPAth request and return the nodeList
	NodeList activities = (NodeList) XMLUtils.evaluate(xpath_activity,
		XPathConstants.NODESET);

	// For each node
	Node aNode;
	for (int i = 0; i < activities.getLength(); i++) {
	    // for each list element , get the list item
	    aNode = activities.item(i);
	    Activity anActivity = new Activity();
	    // Filler for the iteration and the item (node)
	    FillerActivity itFiller = new FillerActivity(anActivity, aNode);
	    Activity returnedActivityFilled = (Activity) itFiller
		    .getFilledElement();
	    for (Activity act : _aSet) {
		if (act.getGuid().equals(returnedActivityFilled.getGuid())) {
		    returnedActivityFilled = act;
		}
	    }

	    // affect the additional dependency to the current activity
	    addDependencyToWorkBreakdownElement(returnedActivityFilled, aNode);
	}
    }

    /**
     * getAllMilestones
     * 
     * @return Set
     */
    private static TreeSet<Milestone> fillMilestonesList() {
	TreeSet<Milestone> milestoneList = new TreeSet<Milestone>();
	// evaluate the XPAth request and return the nodeList
	NodeList milestones = (NodeList) XMLUtils.evaluate(xpath_milestone,
		XPathConstants.NODESET);
	if (milestones == null) {
	    System.out.println("Pas de milestones");
	} else {
	    // there is one or several milestones
	    Node aNode;
	    for (int i = 0; i < milestones.getLength(); i++) {
		// for each list element , get the list item
		aNode = milestones.item(i);
		Milestone aMilestone = new Milestone();
		// Filler for the milestone and the item (node)
		FillerMilestone miFiller = new FillerMilestone(aMilestone,
			aNode);
		Milestone returnedMilestoneFilled = (Milestone) miFiller
			.getFilledElement();
		// TODO
		// affect the additional Guidance to the current Activity
		// setGuidanceByActivity(returnedIterationFilled, aNode);
		// Add the filled object in the result List
		milestoneList.add(returnedMilestoneFilled);
	    }
	}
	return milestoneList;
    }

    /**
     * setAllMilestonesDependencies
     * 
     * @param aSet
     */
    private static void setAllMilestonesDependencies(Set<Milestone> _aSet) {

	// evaluate the XPAth request and return the nodeList
	NodeList milestones = (NodeList) XMLUtils.evaluate(xpath_milestone,
		XPathConstants.NODESET);

	// For each node
	Node aNode;
	for (int i = 0; i < milestones.getLength(); i++) {
	    // for each list element , get the list item
	    aNode = milestones.item(i);
	    Milestone aMilestone = new Milestone();
	    // Filler for the milestone and the item (node)
	    FillerMilestone miFiller = new FillerMilestone(aMilestone, aNode);
	    Milestone returnedMilestoneFilled = (Milestone) miFiller
		    .getFilledElement();
	    for (Milestone mi : _aSet) {
		if (mi.getGuid().equals(returnedMilestoneFilled.getGuid())) {
		    returnedMilestoneFilled = mi;
		}
	    }

	    // affect the additional dependency to the current milestone
	    addDependencyToWorkBreakdownElement(returnedMilestoneFilled, aNode);
	}
    }

    /**
     * setAllPhasesDependencies
     * 
     * @param aSet
     */
    private static void setAllPhasesDependencies(Set<Phase> _aSet) {

	// evaluate the XPAth request and return the nodeList
	NodeList phases = (NodeList) XMLUtils.evaluate(xpath_phase,
		XPathConstants.NODESET);

	// For each node
	Node aNode;
	for (int i = 0; i < phases.getLength(); i++) {
	    // for each list element , get the list item
	    aNode = phases.item(i);
	    Phase aPhase = new Phase();
	    // Filler for the iteration and the item (node)
	    FillerPhase itFiller = new FillerPhase(aPhase, aNode);
	    Phase returnedPhaseFilled = (Phase) itFiller.getFilledElement();
	    for (Phase phase : _aSet) {
		if (phase.getGuid().equals(returnedPhaseFilled.getGuid())) {
		    returnedPhaseFilled = phase;
		}
	    }

	    // affect the additional dependency to the current phase
	    addDependencyToWorkBreakdownElement(returnedPhaseFilled, aNode);
	}
    }

    /**
     * setAllIterationsDependencies
     * 
     * @param aSet
     */
    private static void setAllIterationsDependencies(Set<Iteration> _aSet) {

	// evaluate the XPAth request and return the nodeList
	NodeList iterations = (NodeList) XMLUtils.evaluate(xpath_iteration,
		XPathConstants.NODESET);

	// For each node
	Node aNode;
	for (int i = 0; i < iterations.getLength(); i++) {
	    // for each list element , get the list item
	    aNode = iterations.item(i);
	    Iteration anIteration = new Iteration();
	    // Filler for the iteration and the item (node)
	    FillerIteration itFiller = new FillerIteration(anIteration, aNode);
	    Iteration returnedIterationFilled = (Iteration) itFiller
		    .getFilledElement();
	    for (Iteration iteration : _aSet) {
		if (iteration.getGuid().equals(
			returnedIterationFilled.getGuid())) {
		    returnedIterationFilled = iteration;
		}
	    }

	    // affect the additional dependency to the current phase
	    addDependencyToWorkBreakdownElement(returnedIterationFilled, aNode);
	}
    }

    /**
     * fills the tasks list with task definition
     * 
     * Used By the start method
     * 
     * @return a list of all the TaskDefinition of the XML File
     * @throws Exception
     */
    private static Vector<TaskDefinition> fillTaskDefinitionsList() {
	Vector<TaskDefinition> theTaskDefinitionsList; // the return of the
	// function

	// initializes the List
	theTaskDefinitionsList = new Vector<TaskDefinition>();
	theTaskDefinitionsList.clear();

	// gets all the nodes containing taskDefinions
	NodeList nodeReturned = (NodeList) XMLUtils.evaluate(
		xpath_taskDefinition, XPathConstants.NODESET);

	// For each node...
	Node aNode;
	if (nodeReturned != null) {
	    for (int i = 0; i < nodeReturned.getLength(); i++) {
		aNode = nodeReturned.item(i);

		// Fills the TaskDefinition from the node
		TaskDefinition aTaskDefinition = new TaskDefinition();
		FillerTask aFiller = new FillerTask(aTaskDefinition, aNode);
		aTaskDefinition = (TaskDefinition) aFiller.getFilledElement();
		// affect the additional steps to the current task
		setStepsByTaskDefinition(aTaskDefinition, aNode);
		// affect the additional guidances to the current task
		setGuidanceByTaskDefinition(aTaskDefinition, aNode);
		// add the current task to the list to be return
		theTaskDefinitionsList.add(aTaskDefinition);
	    }
	}
	return theTaskDefinitionsList;
    }

    /**
     * fills the RolesList with RoleDefinition Used by the start Method
     * 
     * @return a list of all the RoleDefinition of the XML File
     * @throws Exception
     */
    private static Vector<RoleDefinition> fillRoleDefinitionsList() {
	Vector<RoleDefinition> theRoleDefinitionsList; // the return of the
	// function

	// initializes the List
	theRoleDefinitionsList = new Vector<RoleDefinition>();
	theRoleDefinitionsList.clear();

	// gets all the nodes containing roleDefinions
	NodeList nodeReturned = (NodeList) XMLUtils.evaluate(
		xpath_roleDefinition, XPathConstants.NODESET);

	// For each node...
	Node aNode;
	if (nodeReturned != null) {
	    for (int i = 0; i < nodeReturned.getLength(); i++) {
		aNode = nodeReturned.item(i);

		// Fills the RoleDefinition from the node
		RoleDefinition aRoleDefinition = new RoleDefinition();
		FillerRole aFiller = new FillerRole(aRoleDefinition, aNode);
		aRoleDefinition = (RoleDefinition) aFiller.getFilledElement();
		// affect the additional guidance to the current role
		setGuidanceByRoleDefinition(aRoleDefinition, aNode);
		// add the filled RoleDefinition in the list to be return
		theRoleDefinitionsList.add(aRoleDefinition);
	    }
	}
	return theRoleDefinitionsList;
    }

    /**
     * fills the WorkProductsList with WorkProductDefinition Used by the start
     * Method
     * 
     * @return a list of all the WorkProductDefinition of the XML File
     * @throws Exception
     */
    private static Vector<WorkProductDefinition> fillWorkProductDefinitionsList() {
	Vector<WorkProductDefinition> theWorkProductDefinitionsList;

	// initializes the List
	theWorkProductDefinitionsList = new Vector<WorkProductDefinition>();
	theWorkProductDefinitionsList.clear();

	// gets all the nodes containing workProductDefinions
	NodeList nodeReturned = (NodeList) XMLUtils.evaluate(
		xpath_workProductDefinition, XPathConstants.NODESET);

	// For each node...
	Node aNode;
	if (nodeReturned != null) {
	    for (int i = 0; i < nodeReturned.getLength(); i++) {
		aNode = nodeReturned.item(i);

		// Fills the WorkProductDefinition from the node
		WorkProductDefinition aWorkProductDefinition = new WorkProductDefinition();
		FillerWorkProduct aFiller = new FillerWorkProduct(
			aWorkProductDefinition, aNode);
		aWorkProductDefinition = (WorkProductDefinition) aFiller
			.getFilledElement();

		// add the filled WorkProductDefinition in the list to be return
		theWorkProductDefinitionsList.add(aWorkProductDefinition);
	    }
	}
	return theWorkProductDefinitionsList;
    }

    /**
     * getProcess Return a Process from a file
     * 
     * @param f
     *                a XML file
     * @return the process
     * @throws Exception
     */
    public static Process getProcess(File XMLFilePath) {
	// The process that will be returned by the function
	Process theProcess = null;

	// If the file exists and is not empty
	if (XMLFilePath.exists() && XMLFilePath.length() > 5) {
	    // XMLUtils contains the low-level parsing functions and needs the
	    // file Path
	    XMLUtils.setDocument(XMLFilePath);

	    // fills the elements sets
	    fillAllElementsList();

	    try {
		// We get the List of all the nodes containing Processes
		NodeList processesNodeList = (NodeList) XMLUtils.evaluate(
			xpath_deliveryProcess, XPathConstants.NODESET);

		Node aNode;
		// If the file contains a process
		if (processesNodeList.getLength() != 0) {
		    theProcess = new Process();
		    // We get the Node corresponding to the process
		    aNode = processesNodeList.item(0);

		    if (aNode != null) {
			// We get the process from this recursive function
			theProcess = (Process) getBreakDownElementsFromNode(aNode);
		    }
		}
	    } catch (Exception e) {
		// e.printStackTrace();
	    }
	}
	return theProcess;
    }

    /**
     * getAllTaskDescriptors
     * 
     * @return all the tasks descriptors
     * @throws Exception
     *                 when no tasks descriptor are found
     */
    private static TreeSet<TaskDescriptor> fillTaskDescriptorsList(
	    Set<RoleDescriptor> allRoles,
	    Set<WorkProductDescriptor> allWorkProducts) {
	// List of the taskDescriptor to be return
	TreeSet<TaskDescriptor> taskList = new TreeSet<TaskDescriptor>();
	// XPath Request to get all TaskDescriptor nodes
	NodeList taskDescriptors = (NodeList) XMLUtils.evaluate(
		xpath_taskDescriptor, XPathConstants.NODESET);

	Node aNode;
	// for each node
	if (taskDescriptors != null) {
	    for (int i = 0; i < taskDescriptors.getLength(); i++) {
		aNode = taskDescriptors.item(i);
		// Fills the TaskDescriptor by the node
		TaskDescriptor aTaskDescriptor = new TaskDescriptor();
		FillerTaskDescriptor aFiller = new FillerTaskDescriptor(
			aTaskDescriptor, aNode);
		TaskDescriptor taskDescriptorfilled = (TaskDescriptor) aFiller
			.getFilledElement();

		// affect the task definition to the task descriptor
		setTaskByTaskDescriptor(taskDescriptorfilled, aNode);

		// affect the main role to the current task
		setMainRoleByTaskDescriptor(taskDescriptorfilled, aNode,
			allRoles);

		// affect the additional roles to the current task

		// Commented As AdditionalRoles are not treated
		// setAddiotionalRoleByTaskDescriptor(taskDescriptorfilled,
		// aNode, allRoles);
		// add the filled taskDescriptor in the list to be return

		// affect output and mandatory/optional input work product
		setWorkProductByTaskDescriptor(taskDescriptorfilled, aNode,
			allWorkProducts);

		taskList.add(taskDescriptorfilled);
	    }
	}
	return taskList;
    }

    /**
     * setAllTaskDescriptorsDependencies
     * 
     * @param _allTaskD
     */
    private static void setAllTaskDescriptorsDependencies(
	    Set<TaskDescriptor> _aSet) {
	// gets all the roles in the file
	NodeList taskDescriptors = (NodeList) XMLUtils.evaluate(
		xpath_taskDescriptor, XPathConstants.NODESET);

	Node aNode;
	// for each node
	for (int i = 0; i < taskDescriptors.getLength(); i++) {
	    aNode = taskDescriptors.item(i);
	    // Fills the current TaskDescriptor
	    TaskDescriptor aTaskDescriptor = new TaskDescriptor();
	    FillerTaskDescriptor aFiller = new FillerTaskDescriptor(
		    aTaskDescriptor, aNode);
	    TaskDescriptor taskDescriptorfilled = (TaskDescriptor) aFiller
		    .getFilledElement();
	    for (TaskDescriptor td : _aSet) {
		if (td.getGuid().equals(taskDescriptorfilled.getGuid())) {
		    taskDescriptorfilled = td;
		}
	    }
	    // affect the additional dependency to the current taskDescriptor
	    addDependencyToWorkBreakdownElement(taskDescriptorfilled, aNode);
	}
    }

    /**
     * setTaskByTaskDescriptor *
     * 
     * @param t
     * @return a task
     */
    private static void setTaskByTaskDescriptor(TaskDescriptor _t, Node _node) {
	TaskDefinition taskTobereturn = null;
	// getting the id of the task
	String idTask = "";
	NodeList listOfTdNodes = _node.getChildNodes();
	boolean trouve = false;
	// search the nodes of the taskDescriptor
	for (int i = 0; i < listOfTdNodes.getLength() && !trouve; i++) {
	    if (listOfTdNodes.item(i).getNodeName().equals(task)) {
		// the current node contains a tag: Task in the XML file
		trouve = true;
		// get the id of the current task
		idTask = listOfTdNodes.item(i).getTextContent();
	    }
	}
	// process if there is a task for this task desriptor
	if (trouve) {
	    taskTobereturn = getTaskDefinitionByID(idTask);
	    // if the task doesn't exist
	    if (taskTobereturn != null) {
		// set the task in the taskdescriptor
		_t.addTaskDefinition(taskTobereturn);
	    }
	}
    }

    /**
     * getTaskDefinitionByID
     * 
     * @param _id
     * @return
     */
    private static TaskDefinition getTaskDefinitionByID(String _id) {
	for (int i = 0; i < TaskDefinitionsList.size(); i++) {
	    if (TaskDefinitionsList.get(i).getGuid().equals(_id)) {
		return TaskDefinitionsList.get(i);
	    }
	}
	return null;
    }

    /**
     * setStepByTaskDefinition
     * 
     * @param _taskd
     * @param _n
     * @throws Exception
     */
    private static void setStepsByTaskDefinition(TaskDefinition _taskd, Node _n) {
	// getting the id of the role
	NodeList listOfTdNodes = _n.getChildNodes();
	boolean trouve = false;

	// search the nodes of the task
	for (int i = 0; i < listOfTdNodes.getLength() && !trouve; i++) {
	    if (listOfTdNodes.item(i).getNodeName().equals(presentation)) {
		// the current node contains the tag: Presentation

		// search the nodes of the step
		for (int j = 0; j < listOfTdNodes.item(i).getChildNodes()
			.getLength(); j++) {
		    if (listOfTdNodes.item(i).getChildNodes().item(j)
			    .getNodeName().equals(step)) {
			// the current node contains the tag: step in the XML
			// file
			Step aStep = new Step();
			// fills the step
			FillerStep fs = new FillerStep(aStep, listOfTdNodes
				.item(i).getChildNodes().item(j));
			aStep = (Step) fs.getFilledElement();
			// add the current step to the TaskDefinition
			_taskd.addStep(aStep);
		    }
		}
		trouve = true;
	    }
	}
    }

    /**
     * setGuidanceByTaskDefinition
     * 
     * @param _taskDefinition
     * @param _node
     */
    private static void setGuidanceByTaskDefinition(
	    TaskDefinition _taskDefinition, Node _node) {
	// get the child node of the taskDefinition in the list
	NodeList listOfTdNodes = _node.getChildNodes();
	Guidance GuideTobereturn = null;

	String idGuide = "";

	// search the nodes of the guidance
	for (int i = 0; i < listOfTdNodes.getLength(); i++) {
	    if (guidancesTypes.contains(listOfTdNodes.item(i).getNodeName())) {
		// recuperation des differents id des guidelines
		idGuide = listOfTdNodes.item(i).getTextContent();

		GuideTobereturn = getGuidanceById(idGuide);
		// if the guidance doesn't exist
		if (GuideTobereturn != null) {
		    // add the current guidance to the taskDefinition
		    _taskDefinition.addGuidance(GuideTobereturn);
		}
	    }
	    // We'll get The templates and examples of the Outputs of the
	    // TaskDef
	    else if (listOfTdNodes.item(i).getNodeName().equals(output)) {
		String wpdfGuid = listOfTdNodes.item(i).getTextContent();
		WorkProductDescriptorFake wpdf = getWorkProductDescriptorFakeById(wpdfGuid);
		// System.out.println(_taskDefinition.getName() + wpdfGuid);

		// For each template or guidance of the WorkProduct
		if (wpdf != null) {
		    for (Guidance aGuidance : wpdf.getTemplatesAndExamples()) {
			_taskDefinition.addGuidance(aGuidance);
		    }
		}
	    }
	}
    }

    /**
     * setGuideByTaskDefinition
     * 
     * @param _taskDefinition
     * @param _node
     */
    private static void setGuidanceByRoleDefinition(
	    RoleDefinition _roleDefinition, Node _node) {
	NodeList listOfTdNodes = _node.getChildNodes();
	Guidance GuideTobereturn = null;

	String idGuide = "";

	// search the nodes of the guide
	for (int i = 0; i < listOfTdNodes.getLength(); i++) {
	    if (guidancesTypes.contains(listOfTdNodes.item(i).getNodeName())) {
		// get the id of the current node
		idGuide = listOfTdNodes.item(i).getTextContent();
		// get the guidance object by the id
		GuideTobereturn = getGuidanceById(idGuide);
		// if the guideline doesn't exist
		if (GuideTobereturn != null) {
		    // set the guideline in the taskDefinition
		    _roleDefinition.addGuidance(GuideTobereturn);
		}
	    }
	}
    }

    /**
     * setAddiotionalRoleByTaskDescriptor
     * 
     * @param _t
     * @param _n
     * @param _s
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private static void setAddiotionalRoleByTaskDescriptor(TaskDescriptor _t,
	    Node _n, Set<RoleDescriptor> _s) {
	String idRole = "";
	NodeList listOfTdNodes = _n.getChildNodes();

	// search the additionalRole
	for (int i = 0; i < listOfTdNodes.getLength(); i++) {
	    if (listOfTdNodes.item(i).getNodeName().equals(
		    additionallyPerformedBy)) {
		// the current node contains the flag: additionallyPerformedBy
		// in the XML file
		idRole = listOfTdNodes.item(i).getTextContent();
		RoleDescriptor roleToBeset;
		// get the RoleDescriptor Object by the id
		roleToBeset = getRoleDescriptorById(_s, idRole);
		// set the role to the roledescriptor
		_t.addAdditionalRole(roleToBeset);
	    }
	}
    }

    /**
     * setWorkProductByTaskDescriptor
     * 
     * @param _t
     * @param _n
     * @param _s
     * @throws Exception
     */

    private static void setWorkProductByTaskDescriptor(TaskDescriptor _t,
	    Node _n, Set<WorkProductDescriptor> _s) {
	String wPId = "";
	NodeList listOfTdNodes = _n.getChildNodes();

	// search the output, input workproducts
	for (int i = 0; i < listOfTdNodes.getLength(); i++) {

	    if (listOfTdNodes.item(i).getNodeName().equals(output)) {
		// the current node contains the flag: output
		// in the XML file
		wPId = listOfTdNodes.item(i).getTextContent();
		WorkProductDescriptor workProductToBeset;
		// get the WorkProductDescriptor Object by the id
		workProductToBeset = getWorkProductDescriptorById(_s, wPId);
		// set the workproduct to the workproductdescriptor
		_t.addOutputWorkProduct(workProductToBeset);

	    } else if (listOfTdNodes.item(i).getNodeName()
		    .equals(optionalInput)) {
		// the current node contains the flag: optionalInput
		// in the XML file
		wPId = listOfTdNodes.item(i).getTextContent();
		WorkProductDescriptor workProductToBeset;
		// get the WorkProductDescriptor Object by the id
		workProductToBeset = getWorkProductDescriptorById(_s, wPId);
		// set the workproduct to the workproductdescriptor
		_t.addOptionalInputWorkProduct(workProductToBeset);

	    } else if (listOfTdNodes.item(i).getNodeName().equals(
		    mandatoryInput)) {
		// the current node contains the flag: mandatoryInput
		// in the XML file
		wPId = listOfTdNodes.item(i).getTextContent();
		WorkProductDescriptor workProductToBeset;
		// get the WorkProductDescriptor Object by the id
		workProductToBeset = getWorkProductDescriptorById(_s, wPId);
		// set the workproduct to the workproductdescriptor
		_t.addMandatoryInputWorkProduct(workProductToBeset);
	    }
	}
    }

    /**
     * setMainRoleByTaskDescriptor
     * 
     * @param _t
     *                the taskdescriptor executed
     * @param _n
     *                the node
     * @param _s
     *                the set of roleDescriptor available
     * @throws Exception
     */
    private static void setMainRoleByTaskDescriptor(TaskDescriptor _t, Node _n,
	    Set<RoleDescriptor> _s) {
	String idRole = "";
	NodeList listOfTdNodes = _n.getChildNodes();
	boolean trouve = false;
	// search the node of the performedPrimarilyBy
	for (int i = 0; i < listOfTdNodes.getLength() && !trouve; i++) {
	    // the current node contains the flag: performedPrimarilyBy in the
	    // XML file
	    if (listOfTdNodes.item(i).getNodeName()
		    .equals(performedPrimarilyBy)) {
		trouve = true;
		// get the id of current node
		idRole = listOfTdNodes.item(i).getTextContent();
	    }
	}
	if (trouve) {
	    RoleDescriptor roleToBeset;
	    roleToBeset = getRoleDescriptorById(_s, idRole);
	    // if the task doesn't exist
	    if (roleToBeset != null) {
		_t.addMainRole(roleToBeset);
	    }
	}
    }

    /**
     * setRoleByRoleDescriptor
     * 
     * @param r
     * @param n
     * @return
     */
    private static void setRoleByRoleDescriptor(RoleDescriptor _r, Node _n) {
	RoleDefinition roleTobereturn = null;
	// To get the id of the role
	String idRole = "";
	NodeList listOfTdNodes = _n.getChildNodes();
	boolean trouve = false;
	// search the node of the role
	for (int i = 0; i < listOfTdNodes.getLength() && !trouve; i++) {
	    if (listOfTdNodes.item(i).getNodeName().equals(role)) {
		// the current node contains the flag: Role in the XML file
		trouve = true;
		idRole = listOfTdNodes.item(i).getTextContent();
	    }
	}
	// process if there is a task for this role desriptor
	if (trouve) {
	    roleTobereturn = getRoleDefinitionByID(idRole);
	    if (roleTobereturn != null) {
		// set the role in the roledescriptor
		_r.addRoleDefinition(roleTobereturn);
	    }
	}
    }

    /**
     * setWorkProductByWorkProductDescriptor
     * 
     * @param wp
     * @param n
     * @return
     */
    private static void setWorkProductByWorkProductDescriptor(
	    WorkProductDescriptor _wp, Node _n) {
	WorkProductDefinition workProductTobereturn = null;
	// To get the id of the workProduct
	String idWorkProduct = "";
	NodeList listOfTdNodes = _n.getChildNodes();
	boolean trouve = false;
	// search the node of the workProduct
	for (int i = 0; i < listOfTdNodes.getLength() && !trouve; i++) {
	    if (listOfTdNodes.item(i).getNodeName().equals(workProduct)) {
		// the current node contains the flag: WorkProduct in the XML
		// file
		trouve = true;
		idWorkProduct = listOfTdNodes.item(i).getTextContent();
	    }
	}
	// process if there is a task for this role descriptor
	if (trouve) {
	    workProductTobereturn = getWorkProductDefinitionByID(idWorkProduct);
	    if (workProductTobereturn != null) {
		// set the workProduct in the workProductdescriptor
		_wp.addWorkProductDefinition(workProductTobereturn);
	    }
	}
    }

    /**
     * getRoleDescriptorById
     * 
     * @param _aSet
     * @param _id
     * @return RoleDescriptor
     */
    private static RoleDescriptor getRoleDescriptorById(
	    Set<RoleDescriptor> _aSet, String _id) {
	for (RoleDescriptor roleDescriptor : _aSet) {
	    if (roleDescriptor.getGuid().equals(_id)) {
		return roleDescriptor;
	    }
	}
	return null;
    }

    /**
     * getWorkProductDescriptorById
     * 
     * @param _aSet
     * @param _id
     * @return WorkProductDescriptor
     */
    private static WorkProductDescriptor getWorkProductDescriptorById(
	    Set<WorkProductDescriptor> _aSet, String _id) {
	for (WorkProductDescriptor workProductDescriptor : _aSet) {
	    if (workProductDescriptor.getGuid().equals(_id)) {
		return workProductDescriptor;
	    }
	}
	return null;
    }

    /**
     * getTaskDescriptorById
     * 
     * @param _aSet
     * @param _id
     * @return TaskDescriptor
     */
    private static TaskDescriptor getTaskDescriptorById(
	    Set<TaskDescriptor> _aSet, String _id) {
	for (TaskDescriptor taskDescriptor : _aSet) {
	    if (taskDescriptor.getGuid().equals(_id)) {
		return taskDescriptor;
	    }
	}
	return null;
    }

    /**
     * getAllIterations
     * 
     * @return Set
     */
    private static TreeSet<Iteration> fillIterationsList() {
	TreeSet<Iteration> iterationList = new TreeSet<Iteration>();
	// evaluate the XPAth request and return the nodeList
	NodeList iterations = (NodeList) XMLUtils.evaluate(xpath_iteration,
		XPathConstants.NODESET);
	if (iterations == null) {
	    //System.out.println("Pas d'iterations");
	} else {
	    // there is one or several iterations
	    Node aNode;
	    for (int i = 0; i < iterations.getLength(); i++) {
		// for each list element , get the list item
		aNode = iterations.item(i);
		Iteration aIteration = new Iteration();
		// Filler for the iteration and the item (node)
		FillerIteration itFiller = new FillerIteration(aIteration,
			aNode);
		Iteration returnedIterationFilled = (Iteration) itFiller
			.getFilledElement();
		// affect the additional Guidance to the current Activity
		setGuidanceByActivity(returnedIterationFilled, aNode);
		// Add the filled object in the result List
		iterationList.add(returnedIterationFilled);
	    }
	}
	return iterationList;
    }

    /**
     * getAllPhases
     * 
     * @return Set
     */
    private static TreeSet<Phase> fillPhasesList() {
	TreeSet<Phase> phaseList = new TreeSet<Phase>();
	/* evaluate the XPAth request and return the nodeList */
	NodeList phases = (NodeList) XMLUtils.evaluate(xpath_phase,
		XPathConstants.NODESET);

	if (phases != null) {
	    /* there is one or several phase */
	    Node aNode;
	    for (int i = 0; i < phases.getLength(); i++) {
		/* for each list element , get the list item */
		aNode = phases.item(i);
		Phase aPhase = new Phase();
		/* Filler for the phase and the item (node) */
		FillerPhase phFiller = new FillerPhase(aPhase, aNode);
		Phase returnedPhaseFilled = (Phase) phFiller.getFilledElement();

		setGuidanceByActivity(returnedPhaseFilled, aNode);

		/* Add the filled object in the result List */
		phaseList.add(returnedPhaseFilled);
	    }
	}
	return phaseList;
    }

    /**
     * getAllRoleDescriptors
     * 
     * @return all the tasks descriptors
     * @throws Exception
     *                 when no tasks descriptor are found
     */
    private static TreeSet<RoleDescriptor> fillRoleDescriptorsList(
	    Set<WorkProductDescriptor> allWorkProducts) {
	TreeSet<RoleDescriptor> roleList = new TreeSet<RoleDescriptor>();

	// get list of nodes by the XPath expression
	NodeList roleDescriptors = (NodeList) XMLUtils.evaluate(
		xpath_roleDescriptor, XPathConstants.NODESET);

	Node aNode;
	// for each node
	if (roleDescriptors != null) {
	    for (int i = 0; i < roleDescriptors.getLength(); i++) {
		aNode = roleDescriptors.item(i);
		// Fills the RoleDescriptor
		RoleDescriptor aRoleDescriptor = new RoleDescriptor();
		FillerRoleDescriptor aFiller = new FillerRoleDescriptor(
			aRoleDescriptor, aNode);
		RoleDescriptor roleDescriptorfilled = (RoleDescriptor) aFiller
			.getFilledElement();
		// affect the additionnal Role in the Descriptor
		setRoleByRoleDescriptor(roleDescriptorfilled, aNode);

		// affect the workproduct that the role is responsible for
		setWorkProductsByRoleDescriptor(roleDescriptorfilled, aNode,
			allWorkProducts);

		// add the current RoleDescriptor Object in the list to be
		// return
		roleList.add(roleDescriptorfilled);
	    }
	}
	return roleList;
    }

    /**
     * @param _roleDescriptorfilled
     * @param _node
     * @param _allWorkProducts
     */
    private static void setWorkProductsByRoleDescriptor(
	    RoleDescriptor _roleDescriptorfilled, Node _node,
	    Set<WorkProductDescriptor> _allWorkProducts) {
	String idWorkProduct = "";
	NodeList listOfTdNodes = _node.getChildNodes();
	boolean trouve = false;
	// search the node of the responsibleFor
	for (int i = 0; i < listOfTdNodes.getLength() && !trouve; i++) {
	    // the current node contains the flag: responsibleFor in the
	    // XML file
	    if (listOfTdNodes.item(i).getNodeName().equals(responsibleFor)) {
		trouve = true;
		// get the id of current node
		idWorkProduct = listOfTdNodes.item(i).getTextContent();
	    }

	    if (trouve) {
		WorkProductDescriptor workProductToBeset;
		workProductToBeset = getWorkProductDescriptorById(
			_allWorkProducts, idWorkProduct);
		trouve = false;

		if (workProductToBeset != null) {
		    _roleDescriptorfilled.addWorkProduct(workProductToBeset);
		}
	    }
	}
    }

    /**
     * getRoleDefinitionByID
     * 
     * @param _id
     * @return
     */
    private static RoleDefinition getRoleDefinitionByID(String _id) {
	for (int i = 0; i < RoleDefinitionsList.size(); i++) {
	    if (RoleDefinitionsList.get(i).getGuid().equals(_id)) {
		return RoleDefinitionsList.get(i);
	    }
	}
	return null;
    }

    /**
     * getWorkProductDefinitionByID
     * 
     * @param _id
     * @return
     */
    private static WorkProductDefinition getWorkProductDefinitionByID(String _id) {
	for (int i = 0; i < WorkProductDefinitionsList.size(); i++) {
	    if (WorkProductDefinitionsList.get(i).getGuid().equals(_id)) {
		return WorkProductDefinitionsList.get(i);
	    }
	}
	return null;
    }

    /**
     * Returns the BreakdownElement filled from the node, with all the
     * BreakdownElements that belong to it
     * 
     * @param _node
     *                The Node containing the main info of the BreakdownElement
     * @return the BreakdownELement filled
     */
    private static BreakdownElement getBreakDownElementsFromNode(Node _node) {
	BreakdownElement returnedBde = null;

	// get the filled BreakDownElement Object
	returnedBde = getBdeFromItsList(_node);

	if (_node.getAttributes().getNamedItem(attr_name_xsitype)
		.getNodeValue().equals(activity)) {
	    if (_node.getAttributes().getNamedItem(
		    attr_name_variabilityBasedOnElement) != null) {
		// the current node contains the attribute:
		// variabilityBasedOnElement in the XML file
		String parentElementID = _node.getAttributes().getNamedItem(
			attr_name_variabilityBasedOnElement).getNodeValue();
		// Xpath request to get the activity list by the id:
		// variabilityBasedOnElement
		String xpath_parentElement = "//Process[@*[namespace-uri() and local-name()='type']='uma:CapabilityPattern' and @id='"
			+ parentElementID + "']";
		// get the list of the nodes by the XPath Expression
		NodeList parentElement = (NodeList) XMLUtils.evaluate(
			xpath_parentElement, XPathConstants.NODESET);
		if (parentElement.getLength() == 1) {
		    // the nodeList is not empty
		    _node = parentElement.item(0);
		}
	    }
	}

	// We're getting with the included elements
	if ((returnedBde != null) && returnedBde instanceof Activity) {
	    BreakdownElement tmpBde; // used to receive the temp result of
	    // this function
	    // when called recursively
	    NodeList listNode = _node.getChildNodes();
	    // search the node of the breakdownElement
	    for (int i = 0; i < listNode.getLength(); i++) {
		if (listNode.item(i).getNodeName().equals(breakdownElement)) {
		    // the current node contains a flag: breakdownElement in the
		    // XML file
		    tmpBde = getBreakDownElementsFromNode(listNode.item(i));
		    // test if the BreakDownElement exists
		    if (tmpBde != null) {
			if (tmpBde instanceof Activity) {
			    ((Activity) returnedBde)
				    .addBreakdownElement(tmpBde);
			} else {
			    ((Activity) returnedBde)
				    .addBreakdownElement(tmpBde);
			}
		    }
		}
	    }
	}
	return returnedBde;
    }

    /**
     * getBdeFromItsList
     * 
     * @param _node
     * @param _nullBdeToReturn
     * @param bdeId
     *                This methods gets the Bde From its List. The List and the
     *                id of the BDe are determined using the Node given in
     *                parameter. This methods fills the BreakdownElement given
     *                in parameter.
     */
    private static BreakdownElement getBdeFromItsList(Node _node) {
	BreakdownElement _nullBdeToReturn = null;
	FillerElement BdeFiller;
	String bdeId = _node.getAttributes().getNamedItem(id).getNodeValue();

	if (_node.getAttributes().getNamedItem(attr_name_xsitype)
		.getNodeValue().equals(phase)) {
	    _nullBdeToReturn = getPhaseById(phasesList, bdeId);
	}

	if (_node.getAttributes().getNamedItem(attr_name_xsitype)
		.getNodeValue().equals(process)) {
	    _nullBdeToReturn = new Process();
	    BdeFiller = new FillerProcess(_nullBdeToReturn, _node);
	    _nullBdeToReturn = (Process) BdeFiller.getFilledElement();
	}

	if (_node.getAttributes().getNamedItem(attr_name_xsitype)
		.getNodeValue().equals(role_descriptor)) {
	    _nullBdeToReturn = getRoleDescriptorById(roleDescriptorsList, bdeId);
	}

	if (_node.getAttributes().getNamedItem(attr_name_xsitype)
		.getNodeValue().equals(workProduct_descriptor)) {
	    _nullBdeToReturn = getWorkProductDescriptorById(
		    workProductDescriptorsList, bdeId);
	}

	if (_node.getAttributes().getNamedItem(attr_name_xsitype)
		.getNodeValue().equals(task_descriptor)) {
	    _nullBdeToReturn = getTaskDescriptorById(taskDescriptorsList, bdeId);
	}

	if (_node.getAttributes().getNamedItem(attr_name_xsitype)
		.getNodeValue().equals(iteration)) {
	    _nullBdeToReturn = getIterationById(iterationsList, bdeId);
	}

	if (_node.getAttributes().getNamedItem(attr_name_xsitype)
		.getNodeValue().equals(activity)
		|| _node.getAttributes().getNamedItem(attr_name_xsitype)
			.getNodeValue().equals(capability_pattern)) {
	    _nullBdeToReturn = getActivityById(activitiesList, bdeId);
	}

	if (_node.getAttributes().getNamedItem(attr_name_xsitype)
		.getNodeValue().equals(milestone)) {
	    _nullBdeToReturn = getMilestoneById(milestonesList, bdeId);
	}

	return _nullBdeToReturn;
    }

    /**
     * getActivityById
     * 
     * @param _aSetActivity
     * @param _bdeId
     * @return a Activity
     */
    private static Activity getActivityById(Set<Activity> _aSetActivity,
	    String _bdeId) {
	for (Activity activity : _aSetActivity) {
	    if (activity.getGuid().equals(_bdeId)) {
		return activity;
	    }
	}
	return null;
    }

    /**
     * getMilestoneById
     * 
     * @param _aSetMilestone
     * @param _bdeId
     * @return a Milestone
     */
    private static Milestone getMilestoneById(Set<Milestone> _aSetMilestone,
	    String _bdeId) {
	for (Milestone milestone : _aSetMilestone) {
	    if (milestone.getGuid().equals(_bdeId)) {
		return milestone;
	    }
	}
	return null;
    }

    /**
     * getIterationById
     * 
     * @param allIterations2
     * @param _bdeId
     * @return
     */
    private static Iteration getIterationById(Set<Iteration> _aSetIteration,
	    String _bdeId) {
	for (Iteration iteration : _aSetIteration) {
	    if (iteration.getGuid().equals(_bdeId)) {
		return iteration;
	    }
	}
	return null;
    }

    /**
     * getPhaseById
     * 
     * @param _allPhases2
     * @param _bdeId
     * @return
     */
    private static Phase getPhaseById(Set<Phase> _allPhases2, String _bdeId) {
	for (Phase phase : _allPhases2) {
	    if (phase.getGuid().equals(_bdeId)) {
		return phase;
	    }
	}
	return null;
    }

    /**
     * getGuidanceById
     * 
     * @param _bdeId
     * @return a Guidance
     */
    private static Guidance getGuidanceById(String _bdeId) {
	for (Guidance guidance : GuidancesList) {
	    if (guidance.getGuid().equals(_bdeId)) {
		return guidance;
	    }
	}
	return null;
    }

    /**
     * getWorkProductDescriptorFakeById
     * 
     * @param _wpdfId
     * @return the WPDF which has the specified Id
     */
    private static WorkProductDescriptorFake getWorkProductDescriptorFakeById(
	    String _wpdfId) {
	for (WorkProductDescriptorFake wpdf : WorkProductDescriptorFakesList) {
	    if (wpdf.Guid.equals(_wpdfId)) {
		return wpdf;
	    }
	}
	return null;
    }

    /**
     * getWorkBreakdownElementById
     * 
     * @param _aSetWorkBreakdownElement
     * @param _wbdeId
     * @return
     */
    private static WorkBreakdownElement getWorkBreakdownElementById(
	    Set<WorkBreakdownElement> _aSetWorkBreakdownElement, String _wbdeId) {
	for (WorkBreakdownElement wbde : _aSetWorkBreakdownElement) {
	    if (wbde.getGuid().equals(_wbdeId)) {
		return wbde;
	    }
	}
	return null;
    }

    private class WorkProductDescriptorFake {
	private String Guid;

	private Set<Guidance> templatesAndExamples;

	public WorkProductDescriptorFake() {
	    templatesAndExamples = new LinkedHashSet<Guidance>();
	}

	public Set<Guidance> getTemplatesAndExamples() {
	    return templatesAndExamples;
	}

	/**
	 * @return the guid
	 */
	public String getGuid() {
	    return Guid;
	}

	/**
	 * @param guid
	 *                the guid to set
	 */
	public void setGuid(String _guid) {
	    Guid = _guid;
	}

	public void addTemplateOrExample(Guidance _templateOrGuidance) {
	    this.templatesAndExamples.add(_templateOrGuidance);
	}
    }
}
