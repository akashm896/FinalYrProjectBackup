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

package wilos.tools.imports.epfcomposer.fillers;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import wilos.model.spem2.workproduct.WorkProductDefinition;
import wilos.tools.imports.epfcomposer.utils.EncodingProcessor;

public class FillerWorkProduct extends FillerElement {

    private static String NodePresentation = "Presentation";

    private static String NodeImpactOfNotHaving = "ImpactOfNotHaving";

    private static String NodePurpose = "Purpose";

    private static String NodeReasonsForNotNeeding = "ReasonsForNotNeeding";

    private static String NodeBriefOutline = "BriefOutline";

    private static String NodeRepresentationOptions = "RepresentationOptions";

    /**
     * Constructor of FillerWorkProduct
     * 
     * @param _e
     * @param _aNode
     */
    public FillerWorkProduct(WorkProductDefinition _e, Node _aNode) {
	super(_e, _aNode);
	fill();
    }

    /**
     * Fill a WorkProduct list
     */
    public void fill() {
	// searching and setting the Alternatives, HowToStaff, and
	// Purpose
	NodeList myNodeList = myNode.getChildNodes();
	NodeList nodePresentationList = null;
	Node nodePresentation = null;

	String impactOfNotHaving = "";
	String purpose = "";
	String reasonsForNotNeeding = "";
	String briefOutline = "";
	String representationOptions = "";

	// Searching for the Presentation node
	for (int i = 0; i < myNodeList.getLength() && nodePresentation == null; i++) {
	    if (myNodeList.item(i).getNodeName().equals(NodePresentation)) {
		nodePresentation = myNodeList.item(i);
	    }
	}
	if (nodePresentation != null) {
	    // If the presentation node has been founded
	    nodePresentationList = nodePresentation.getChildNodes();
	    for (int i = 0; i < nodePresentationList.getLength()
		    && (impactOfNotHaving.equals("") || purpose.equals("")
			    || reasonsForNotNeeding.equals("")
			    || briefOutline.equals("") || representationOptions
			    .equals("")); i++) {
		// Search for the ImpactOfNotHaving node
		if (nodePresentationList.item(i).getNodeName().equals(
			NodeImpactOfNotHaving)) {
		    impactOfNotHaving = EncodingProcessor
			    .cleanString(nodePresentationList.item(i)
				    .getTextContent());
		}
		// Search for the Purpose node
		if (nodePresentationList.item(i).getNodeName().equals(
			NodePurpose)) {
		    purpose = EncodingProcessor
			    .cleanString(nodePresentationList.item(i)
				    .getTextContent());
		}
		// Search for the ReasonsForNotNeeding node
		if (nodePresentationList.item(i).getNodeName().equals(
			NodeReasonsForNotNeeding)) {
		    reasonsForNotNeeding = EncodingProcessor
			    .cleanString(nodePresentationList.item(i)
				    .getTextContent());
		}

		// Search for the BriefOutline node
		if (nodePresentationList.item(i).getNodeName().equals(
			NodeBriefOutline)) {
		    briefOutline = EncodingProcessor
			    .cleanString(nodePresentationList.item(i)
				    .getTextContent());
		}

		// Search for the RepresentationOptions node
		if (nodePresentationList.item(i).getNodeName().equals(
			NodeRepresentationOptions)) {
		    representationOptions = EncodingProcessor
			    .cleanString(nodePresentationList.item(i)
				    .getTextContent());
		}
	    }
	}

	((WorkProductDefinition) myElement)
		.setImpactOfNotHaving(impactOfNotHaving);
	((WorkProductDefinition) myElement).setPurpose(purpose);
	((WorkProductDefinition) myElement)
		.setReasonsForNotNeeding(reasonsForNotNeeding);
	((WorkProductDefinition) myElement).setBriefOutline(briefOutline);
	((WorkProductDefinition) myElement)
		.setRepresentationOptions(representationOptions);
    }
}
