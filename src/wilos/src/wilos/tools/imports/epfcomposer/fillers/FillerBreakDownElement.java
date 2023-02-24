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

import wilos.model.spem2.breakdownelement.BreakdownElement;
import wilos.model.spem2.element.Element;
import wilos.tools.imports.epfcomposer.utils.EncodingProcessor;

public class FillerBreakDownElement extends FillerElement {
    private static String prefix = "prefix";

    private static String isPlanned = "isPlanned";

    private static String hasMultipleOccurrences = "hasMultipleOccurrences";

    private static String isOptional = "isOptional";

    private static String presentationName = "presentationName";

    /**
     * Constructor of the FillerBreakDownElement
     * 
     * @param _e
     * @param _aNode
     */
    public FillerBreakDownElement(Element _e, Node _aNode) {
	super(_e, _aNode);
	fill();
    }

    /**
     * Fill a BreakDownElement list
     */
    private void fill() {
	((BreakdownElement) myElement).setPrefix(EncodingProcessor
		.cleanString(myNode.getAttributes().getNamedItem(prefix)
			.getNodeValue()));
	// setting the isPlanned
	((BreakdownElement) myElement).setIsPlanned(Boolean.valueOf(myNode
		.getAttributes().getNamedItem(isPlanned).getNodeValue()));
	// setting the hasMultipleOccurrences
	((BreakdownElement) myElement).setHasMultipleOccurrences((Boolean
		.valueOf(myNode.getAttributes().getNamedItem(
			hasMultipleOccurrences).getNodeValue())));
	// setting the isOptional
	((BreakdownElement) myElement).setIsOptional((Boolean.valueOf(myNode
		.getAttributes().getNamedItem(isOptional).getNodeValue())));
	// Sets the Presentation name
	((BreakdownElement) myElement).setPresentationName(EncodingProcessor
		.cleanString(myNode.getAttributes().getNamedItem(
			presentationName).getNodeValue()));
    }
}
