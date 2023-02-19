/*
Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>

This program is free software; you can redistribute it and/or modify it under the terms of the GNU
General Public License as published by the Free Software Foundation; either version 2 of the License,
or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not,
write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA. 
*/

package wilos.test.tools.imports.epfcomposer.utils;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipException;

import junit.framework.TestCase;
import wilos.tools.imports.epfcomposer.utils.ZIPUtils;

public class ZIPUtilsTest extends TestCase
{
	public static File pathEmpty = new File("test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "empty.zip");
	public static File pathMonTest = new File("test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "monTest.zip");
	public static File pathBizarre = new File("test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "bizarre.zip");
	public static File pathTest2Niveau = new File("test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "test2Niveau.zip");
	public static File pathTestCorrompu = new File("test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "corrompu.zip");
	public static File pathTest2XML = new File("test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "deuxXML.zip");
	public static File pathTestArbre = new File("test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "arbre.zip");
	public static File pathTest2XML2 = new File("test"+ File.separator +"wilos"+ File.separator +"test"+File.separator+"tools"+ File.separator+ "imports" +File.separator +  "epfcomposer" +File.separator  + "resources" +File.separator  + "test2xml.zip");
	
	public void testIsEmpty (){
		try {
			ZIPUtils monZip = new ZIPUtils (pathEmpty);
			assertTrue("TEST AVEC ARCHIVE VIDE", monZip.isEmpty());
			
			ZIPUtils monZip2 = new ZIPUtils (pathMonTest);
			assertFalse("TEST AVEC ARCHIVE NON VIDE",monZip2.isEmpty());
			
		}
		catch (Exception e){
			assertTrue(false);
		}
		try{
			ZIPUtils monZip3 = new ZIPUtils (pathBizarre);
			assertTrue("TEST AVEC ARCHIVE NON VIDE",monZip3.isEmpty());
		}
		catch (ZipException z){
			assertTrue(true);
		}
	}
	
	public void testGetXMLFile () {
		try {
			ZIPUtils monZip = new ZIPUtils(pathEmpty);
			assertNull("TEST getXML FICHIER VIDE",monZip.getXMLFile());
			
			ZIPUtils monZip2 = new ZIPUtils(pathMonTest);
			File file = monZip2.getXMLFile() ;
			assertNotNull("TEST getXML FICHIER VIDE",file);
			System.out.println(file.getAbsolutePath());
			
			ZIPUtils monZip3 = new ZIPUtils(pathTest2Niveau);
			File file2 = monZip3.getXMLFile() ;
			assertNotNull("TEST getXML FICHIER 2 NIVEAUX",file2);
			System.out.println(file2.getAbsolutePath());
		}
		catch (ZipException z){
			assertTrue(false);
		}
		
		try {
			ZIPUtils monZip4 = new ZIPUtils(pathTestCorrompu) ;
			assertNull("TEST getXML FICHIER CORROMPU",monZip4.getXMLFile());
		} catch (ZipException e) {
			//None.
		}
		
	}
	
	public void testGetXMLStream () {
		try {
			ZIPUtils monZip = new ZIPUtils(pathEmpty);
			assertNull("TEST getStream FICHIER VIDE",monZip.getXMLStream());
			
			ZIPUtils monZip2 = new ZIPUtils(pathMonTest);
			InputStream file = monZip2.getXMLStream() ;
			assertNotNull("TEST getStream FICHIER VIDE",file);
			
			ZIPUtils monZip3 = new ZIPUtils(pathTest2Niveau);
			InputStream file2 = monZip3.getXMLStream() ;
			assertNotNull("TEST getStream FICHIER 2 NIVEAUX",file2);
		}
		catch (ZipException z){
			assertTrue(false);
		}

		try {
			ZIPUtils monZip4 = new ZIPUtils(pathTestCorrompu) ;
			assertNull("TEST getStream FICHIER CORROMPU",monZip4.getXMLStream());
		} catch (ZipException e) {
			//None.
		}
		
	}
	
	public void test2XML (){
		try {
			ZIPUtils monZip = new ZIPUtils(pathTest2XML2);
			assertTrue("TEST 2 NIVEAUX",monZip.getXMLEntry().getName().endsWith("test.xml"));
		}
		catch (ZipException z){
			assertTrue(false);
		}
	}
	
	public void testExtract () {
		String dest = "." + File.separator  + "dest" + File.separator;
		String dest2 = "." + File.separator  + "dest";
		
		File f = new File(dest) ;
		
		ZIPUtils z ;
		try {
			// test avec file separator
			 z = new ZIPUtils(pathMonTest);
		
			//deleteFolder(f);
			if (!f.exists()){
				f.mkdir();
				//System.out.println("IS DIRECTORY ? " + f.isDirectory());			
			}
			
			z.extract(dest);
			assertTrue(f.listFiles().length == 1);
			
			deleteFolder(f);
			
			// test sans file separator
			z.extract(dest2);
			assertTrue(f.listFiles().length == 1);
			
			deleteFolder(f);
			
			z = new ZIPUtils(pathTest2XML) ;
			z.extract(dest);
			assertTrue(f.listFiles().length == 2);
			
			deleteFolder(f);
			
			z = new ZIPUtils(pathTest2Niveau) ;
			z.extract(dest);
			assertTrue(f.listFiles().length == 1);
			
			deleteFolder(f);
			
			z = new ZIPUtils(pathTestArbre) ;
			z.extract(dest);
			assertTrue(f.listFiles().length == 1);
			deleteFolder(f);
			
			z = new ZIPUtils(pathEmpty) ;
			z.extract(dest);
			assertTrue(f.list().length == 0);
			
			f.deleteOnExit();
			
		} catch (ZipException e) {
			System.out.println(e.getMessage());
			assertTrue(false);
		}
	}
	
	private void deleteFolder(File fi){
		File[] f = fi.listFiles() ;
		for (int i = 0 ; i < f.length ; i++){
			if (f[i].isDirectory()){
				deleteFolder(f[i]);
			}
			f[i].delete();
		}
	}
}
