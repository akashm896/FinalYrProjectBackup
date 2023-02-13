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

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import wilos.model.spem2.process.Process;
import wilos.tools.imports.epfcomposer.parser.XMLParser;

public class XMLParserTest {
	public static File pathScrum = new File("test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator
			+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "scrum.xml"); 
	public static File pathOPenUP = new File("test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "openup.xml");
	public static File pathMonTest = new File("test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "monTest.xml");
	public static File pathScrumFillerTest = new File("test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" 
			+File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "scrum_FillerTest.xml");
	public static File pathFileError = new File("noFile");
	public static File pathScrumWithArte = new File("test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"
			+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "scrum_with_ArteF.xml"); 
	public static File pathEmptyFile = new File("test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "emptyFile.xml"); 
	public static File pathItil = new File("test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "itil.xml");
	public static File crashingScrum = new File("test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"
			+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "crashingScrum.xml");
	public static File pathXP = new File("test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "xp.xml");
	public static File pathWrongFile = new File("test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "WrongFile.xml");
	public static File pathOpenUpCrashing = new File("test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "openup2_crashing.xml");
	
	private static Process  OpenUPProcess = null;
	private static Process  OpenUPProcessCrashing = null;
	private static Process  ScrumProcess = null;
	
	/**
	 * Checks that the ProcessReturned is null if the file doesnt exist
	 *
	 */
	@Test
	public void testReturnNullIfFileError() {		
		Process theProcess;
		theProcess = XMLParser.getProcess(pathFileError);
		assertTrue(theProcess == null);
	}
	
	/**
	 * Checks that the ProcessReturned is null if the file is empty
	 *
	 */
	@Test
	public void testReturnsNullIfFileEmpty() {
		Process theProcess;
		theProcess = XMLParser.getProcess(pathEmptyFile);
		assertTrue(theProcess == null);
	}
	
	/**
	 * Checks that the Regular Files (Scrum and OpenUp) contain one Process
	 *
	 */
	@Test
	public void testFindsProcessInRegularFiles() {
		Process theProcess;
		
		if (ScrumProcess == null) {
			ScrumProcess = XMLParser.getProcess(pathScrum);
		}
		assertTrue(ScrumProcess != null);
		
		if (OpenUPProcess == null) {
			OpenUPProcess = XMLParser.getProcess(pathOPenUP);
		}
		assertTrue(OpenUPProcess != null);
		
		theProcess = XMLParser.getProcess(pathScrumWithArte );
		assertTrue(theProcess != null);
		
		theProcess = XMLParser.getProcess(pathItil);
		assertTrue(theProcess != null);
	}
	
	@Test
	public void testThatXpProcessIsNull() {
		Process theProcess;
		theProcess = XMLParser.getProcess(pathXP);
		
		assertNull(theProcess);
	}
	
	@Test
	public void testThatProcessFromWrongFileIsNull() {
		Process theProcess;
		
		// Errors are Printed on the Console : IT'S NORMAL
		theProcess = XMLParser.getProcess(pathWrongFile);
		
		assertNull(theProcess);
		
	}
	
	@Test
	public void testOpenUpProcessCrashingIsNotNull() {
		if (OpenUPProcessCrashing == null) {
			OpenUPProcessCrashing = XMLParser.getProcess(pathOpenUpCrashing);
		}
		
		assertNotNull(OpenUPProcessCrashing);
	}
}
