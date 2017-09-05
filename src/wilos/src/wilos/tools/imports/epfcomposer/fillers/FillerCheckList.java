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

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import wilos.model.spem2.checklist.CheckList;
import wilos.model.spem2.section.Section;
import wilos.tools.imports.epfcomposer.parser.XMLParser;
import wilos.tools.imports.epfcomposer.utils.EncodingProcessor;

public class FillerCheckList extends FillerGuidance {

    private static String Name = "name";

    private static String Id = "id";

    /**
     * Constructor of FillerCheckList
     * 
     * @param _checklist
     * @param aNode
     */
    public FillerCheckList(CheckList _checklist, Node aNode) {
	super(_checklist, aNode);
	fill();
    }

    /**
     * Fill a CheckList list
     */
    private void fill() {
	Set<Section> sections = new HashSet<Section>();
	NodeList l = myNode.getChildNodes();

	// ((Guidance) myElement).setType(type);
	String aName = "";
	String aDescriptionSection = "";
	String mainDescription = "";
	String aId = "";

	// for each node by the list
	for (int i = 0; i < l.getLength(); i++) {
	    if (l.item(i).getNodeName().equals(XMLParser.presentation)) {
		NodeList listItems = l.item(i).getChildNodes();
		for (int j = 0; j < listItems.getLength(); j++) {
		    // case where there is a MainDescription
		    if (listItems.item(j).getNodeName().equals(
			    XMLParser.maindescription)) {
			mainDescription = EncodingProcessor
				.cleanString(listItems.item(j).getTextContent());
		    }
		    // case where there are different sections
		    if (listItems.item(j).getNodeName().equals(
			    XMLParser.section)) {
			aName = listItems.item(j).getAttributes().getNamedItem(
				Name).getNodeValue();
			aId = listItems.item(j).getAttributes()
				.getNamedItem(Id).getNodeValue();
			// for the description
			NodeList listDescription = listItems.item(j)
				.getChildNodes();
			// for each node: get the description by the XML file
			for (int k = 0; k < listDescription.getLength(); k++) {
			    if (listDescription.item(k).getNodeName().equals(
				    XMLParser.description)) {
				aDescriptionSection = listDescription.item(k)
					.getTextContent();
			    }
			}
			// create a new instance of Section to fill it
			Section aSection = new Section();
			// set the different attributs
			aSection.setName(aName);
			aSection.setGuid(aId);
			aSection.setDescription(EncodingProcessor
				.cleanString(aDescriptionSection));

			// add the fill element in the list of sections
			sections.add(aSection);
		    }
		}
	    }
	}
	// set the list of sections by the element
	((CheckList) myElement).setSections(sections);
	((CheckList) myElement).setDescription(EncodingProcessor
		.cleanString(mainDescription));
    }
}
