/*
Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
Copyright (C) 2007 Nicolas CASTEL <ncastel@wilos-project.org>

This program is free software; you can redistribute it and/or modify it under the terms of the GNU
General Public License as published by the Free Software Foundation; either version 2 of the License,
or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not,
write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA. 
 */

package wilos.test.tools.imports.epfcomposer.parser;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.xpath.XPathConstants;

import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.breakdownelement.BreakdownElement;
import wilos.model.spem2.process.Process;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.tools.imports.epfcomposer.parser.XMLParser;
import wilos.tools.imports.epfcomposer.utils.XMLUtils;

public class XMLParserTestAnalyst {
	public static File pathOPenUP = new File("test" + File.separator + "wilos"
			+ File.separator + "test" + File.separator + "tools"
			+ File.separator + "imports" + File.separator + "epfcomposer"
			+ File.separator + "resources" + File.separator + "openup.xml");

	@Test
	public void testOpenUPInitiateProjectContainsExpectedRoleDescriptors() {
		Iterator<BreakdownElement> itTopLevelAct;
		Activity secondLevelActivity;
		Iterator<BreakdownElement> BdeIterator;
		BreakdownElement tmpBde;

		HashSet<String> expectedResults = new HashSet<String>();
		expectedResults.add("Stakeholder");
		expectedResults.add("Project Manager");
		expectedResults.add("Analyst");
		expectedResults.add("Tester");
		expectedResults.add("Architect");

		Process openUPProcess = XMLParser.getProcess(pathOPenUP);

		// Iterator on the set of the four Phases of OpenUP
		itTopLevelAct = openUPProcess.getBreakdownElements().iterator();

		// We want the third Phase : Construction Iteration
		while (itTopLevelAct.hasNext()) {
			Object object = itTopLevelAct.next();
			if (object instanceof Activity) {
				secondLevelActivity = (Activity) object;

				BdeIterator = secondLevelActivity.getBreakdownElements()
						.iterator();
				while (BdeIterator.hasNext()) {
					tmpBde = BdeIterator.next();
					if (tmpBde instanceof Activity) {
						Activity a = (Activity) tmpBde;
						Iterator<BreakdownElement> Actiterator = a
								.getBreakdownElements().iterator();
						while (Actiterator.hasNext()) {
							BreakdownElement bdee = Actiterator.next();
							if (bdee instanceof RoleDescriptor
									&& bdee.getPresentationName().equals(
											"Analyst")) {
								RoleDescriptor roro = (RoleDescriptor) bdee;
								
								Iterator<TaskDescriptor> iter = roro
										.getPrimaryTasks().iterator();
								while (iter.hasNext()) {
									TaskDescriptor td = iter.next();
								}
							}
						}
					}
				}
			}
		}

	}

	/**
	 * It seems that OpenUP may contains multiple role descriptor that has the
	 * same id We are gonna check this
	 * 
	 */
	@Test
	public void testToDeterminesIfOpenUpContainsMultipleRoleDescWithSameId() {
		String xpath_roleDescriptor = "//BreakdownElement[@*[namespace-uri() and local-name()='type']='uma:RoleDescriptor']";
		String AttributeId = "id";

		String guid;

		Vector<String> allRoleDescGuid = new Vector<String>();
		Vector<String> duplicatedDescGuid = new Vector<String>();

		XMLUtils.setDocument(pathOPenUP);

		NodeList roleDescriptorsNodeList = (NodeList) XMLUtils.evaluate(
				xpath_roleDescriptor, XPathConstants.NODESET);

		Node aNode;
		for (int i = 0; i < roleDescriptorsNodeList.getLength(); i++) {
			aNode = roleDescriptorsNodeList.item(i);

			guid = aNode.getAttributes().getNamedItem(AttributeId)
					.getNodeValue();

			if (allRoleDescGuid.contains(guid)) {
				duplicatedDescGuid.add(guid);
			}

			allRoleDescGuid.add(guid);
		}

		assertTrue("RoleDescriptor duplicated", duplicatedDescGuid.isEmpty());
	}

	/**
	 * No we go further : Are there two elements with the same id ?? We are
	 * gonna check this
	 * 
	 */
	@Test
	public void testToDeterminesIfOpenUpContainsMultipleElementsWithSameId() {
		String xpath_roleDescriptor = "//*";
		String AttributeId = "id";

		String guid;

		Vector<String> allRoleDescGuid = new Vector<String>();
		Vector<String> duplicatedDescGuid = new Vector<String>();

		XMLUtils.setDocument(pathOPenUP);

		NodeList elementsNodeList = (NodeList) XMLUtils.evaluate(
				xpath_roleDescriptor, XPathConstants.NODESET);

		Node aNode;
		for (int i = 0; i < elementsNodeList.getLength(); i++) {
			aNode = elementsNodeList.item(i);

			if (aNode != null
					&& aNode.getAttributes().getNamedItem(AttributeId) != null) {
				guid = aNode.getAttributes().getNamedItem(AttributeId)
						.getNodeValue();

				if (allRoleDescGuid.contains(guid)) {
					duplicatedDescGuid.add(guid);
				}

				allRoleDescGuid.add(guid);
			}
		}

		assertTrue("Element duplicated", duplicatedDescGuid.isEmpty());
	}
}
