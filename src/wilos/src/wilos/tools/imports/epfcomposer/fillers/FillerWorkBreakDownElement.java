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

import wilos.model.spem2.element.Element;
import wilos.model.spem2.workbreakdownelement.WorkBreakdownElement;

public class FillerWorkBreakDownElement extends FillerBreakDownElement {
    private static String isRepeatable = "isRepeatable";

    private static String isOnGoing = "isOngoing";

    private static String isEvenDriven = "isEventDriven";

    /**
     * Constructor of FillerWorkBreakDownElement
     * 
     * @param _e
     * @param _aNode
     */
    public FillerWorkBreakDownElement(Element _e, Node _aNode) {
	super(_e, _aNode);
	fill();
    }

    /**
     * Fill a WorkBreakdownElement list
     */
    private void fill() {
	// setting isRepeatable
	((WorkBreakdownElement) myElement).setIsRepeatable(Boolean
		.valueOf(myNode.getAttributes().getNamedItem(isRepeatable)
			.getNodeValue()));
	// setting isOnGoing
	((WorkBreakdownElement) myElement).setIsOngoing(Boolean.valueOf(myNode
		.getAttributes().getNamedItem(isOnGoing).getNodeValue()));
	// setting isEvenDriven
	((WorkBreakdownElement) myElement).setIsEvenDriven(Boolean
		.valueOf(myNode.getAttributes().getNamedItem(isEvenDriven)
			.getNodeValue()));
    }
}
