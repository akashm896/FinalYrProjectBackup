/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

package wilos.tools.imports.epfcomposer.fillers;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.element.Element;
import wilos.tools.imports.epfcomposer.utils.EncodingProcessor;

public class FillerActivity extends FillerWorkBreakDownElement {
    private static String NodePresentation = "Presentation";

    private static String NodeAlternatives = "Alternatives";

    private static String NodeHowToStaff = "HowToStaff";

    private static String NodePurpose = "Purpose";

    private static String NodeMainDescription = "MainDescription";

    private static String NodeKeyConsiderations = "KeyConsiderations";

    private static String NodeDescription = "briefDescription";

    /**
     * Constructor of the FillerActivity
     * 
     * @param _e
     * @param _aNode
     */
    public FillerActivity(Element _e, Node _aNode) {
	super(_e, _aNode);
	fill();
    }

    /**
     * Fill an activity list
     */
    private void fill() {
	// searching and setting the Alternatives, HowToStaff, and Purpose
	NodeList myNodeList = myNode.getChildNodes();
	NodeList nodePresentationList = null;
	Node nodePresentation = null;
	String alternatives = "";
	String howToStaff = "";
	String purposes = "";
	String mainDescription = "";
	String keyConsiderations = "";

	// searching for the Presentation node
	for (int i = 0; i < myNodeList.getLength() && nodePresentation == null; i++) {
	    if (myNodeList.item(i).getNodeName().equals(NodePresentation)) {
		nodePresentation = myNodeList.item(i);
	    }
	}
	if (nodePresentation != null) {
	    // If the presentation node has been founded
	    nodePresentationList = nodePresentation.getChildNodes();
	    for (int i = 0; i < nodePresentationList.getLength()
		    && (alternatives.equals("") || howToStaff.equals("") || purposes
			    .equals("")); i++) {
		// Search for the Alternatives node
		if (nodePresentationList.item(i).getNodeName().equals(
			NodeAlternatives)) {
		    alternatives = EncodingProcessor
			    .cleanString(nodePresentationList.item(i)
				    .getTextContent());
		}
		// Search for the HowToStaff node
		if (nodePresentationList.item(i).getNodeName().equals(
			NodeHowToStaff)) {
		    howToStaff = EncodingProcessor
			    .cleanString(nodePresentationList.item(i)
				    .getTextContent());
		}
		// Search for the Purpose node
		if (nodePresentationList.item(i).getNodeName().equals(
			NodePurpose)) {
		    purposes = EncodingProcessor
			    .cleanString(nodePresentationList.item(i)
				    .getTextContent());
		}

		// We need it in the case that this fill is called by
		// auxiliaryFill

		if (nodePresentationList.item(i).getNodeName().equals(
			NodeMainDescription)) {
		    mainDescription = EncodingProcessor
			    .cleanString(nodePresentationList.item(i)
				    .getTextContent());
		}
		// Search for the KeyConsiderations node
		if (nodePresentationList.item(i).getNodeName().equals(
			NodeKeyConsiderations)) {
		    keyConsiderations = EncodingProcessor
			    .cleanString(nodePresentationList.item(i)
				    .getTextContent());
		}
	    }
	}

	((Activity) myElement).setAlternatives(alternatives);
	((Activity) myElement).setHowToStaff(howToStaff);
	((Activity) myElement).setPurpose(purposes);
	((Activity) myElement).setMainDescription(mainDescription);
	((Activity) myElement).setKeyConsiderations(keyConsiderations);
    }

    /**
     * Method used to fill an activity list from a node
     * 
     * @param node
     */
    public void auxilliaryFill(Node node) {
	this.myNode = node;
	this.fill();

	myElement.setDescription(EncodingProcessor.cleanString(myNode
		.getAttributes().getNamedItem(NodeDescription).getNodeValue()));
    }
}
