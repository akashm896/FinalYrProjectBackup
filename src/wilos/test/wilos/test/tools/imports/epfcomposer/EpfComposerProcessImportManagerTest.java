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

package wilos.test.tools.imports.epfcomposer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;

import org.junit.Test;

import wilos.model.spem2.process.Process;
import wilos.tools.imports.epfcomposer.EpfComposerProcessImportManager;

public class EpfComposerProcessImportManagerTest {
	public static String pathEmpty = "test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "empty.zip";
	public static String pathMonTest = "test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "monTest.zip";
	public static String pathBizarre = "test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "bizarre.zip";
	public static String pathTest2Niveau = "test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "test2Niveau.zip";
	public static String pathTestCorrompu = "test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "corrompu.zip";
	public static String pathTest2XML = "test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "deuxXML.zip";
	public static String pathTestOpenUp = "test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "openup.zip";
	
	@Test
	public void testImportProcess(){
		Process p = null ;
		EpfComposerProcessImportManager parser = new EpfComposerProcessImportManager();
		
		p = parser.importProcess(pathEmpty,"") ;
		assertNull("TEST FICHIER VIDE",p);
		
		p = parser.importProcess(pathMonTest,"") ;
		assertNotNull("TEST FICHIER SIMPLE",p) ;
		
		p = parser.importProcess(pathBizarre,"") ;
		assertNull("TEST BIZARRE",p);
		
		/*p = parser.importProcess(pathTest2Niveau,"") ;
		assertNotNull("TEST 2 NIVEAUX",p);*/
		
		p = parser.importProcess(pathTestCorrompu,"") ;
		assertNull("TEST CORROMPU",p);
	
		p = parser.importProcess(pathTest2XML,"") ;
		assertNotNull("TEST 2 XML",p);
		System.out.println(p.getName());
		
		p = parser.importProcess(pathMonTest, "");
		assertNotNull("TEST 2 XML",p);
		
		p = parser.importProcess(pathTestOpenUp, "");
		assertNotNull("TEST OPEN UP",p);
	}
}
