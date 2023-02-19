/*
Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;

import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.breakdownelement.BreakdownElement;
import wilos.model.spem2.process.Process;
import wilos.tools.imports.epfcomposer.parser.XMLParser;

public class XMLParserFS59Test {
	public static File pathOPenUP =new File("processes"+ File.separator  + "openUP.xml");
	
	private static Process  OpenUPProcess = null;
	
	@Test
	public void testTwoSuperactivities() {
		if (OpenUPProcess == null) {
			OpenUPProcess = XMLParser.getProcess(pathOPenUP);
		}
		assertNotNull(OpenUPProcess);
		assertFalse(checkTwoSuperActivities(OpenUPProcess));
	}
	
	// check if a activity get two or more super activities
	private boolean checkTwoSuperActivities(BreakdownElement bde) {
		boolean result = false;
		if (bde instanceof Activity) {
			for (BreakdownElement bdle : ((Activity)bde).getBreakdownElements()) {
				result |= checkTwoSuperActivities(bdle);
			}
		}
		if(bde.getSuperActivities() != null && bde.getSuperActivities().size() > 1 && bde instanceof Activity) {
			System.out.println("Problem two superactivities : "+bde.getName());
		}
		return result || bde.getSuperActivities().size() > 1 && bde instanceof Activity;
	}
}
