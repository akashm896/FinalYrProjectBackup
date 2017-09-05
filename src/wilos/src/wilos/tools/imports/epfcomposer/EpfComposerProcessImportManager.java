/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2007 Mathieu BENOIT <mathieu-benoit@hotmail.fr>
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

package wilos.tools.imports.epfcomposer;

import java.io.File;
import java.util.zip.ZipException;

import wilos.model.spem2.process.Process;
import wilos.tools.imports.IProcessImportManager;
import wilos.tools.imports.epfcomposer.parser.XMLParser;
import wilos.tools.imports.epfcomposer.utils.XMLUtils;
import wilos.tools.imports.epfcomposer.utils.ZIPUtils;

public class EpfComposerProcessImportManager implements IProcessImportManager {

    /**
     * Getter of ToolsName
     */
    public String getToolsName() {
	return "EPF Composer";
    }

    public String getToolsVersion() {
	return "1.0.2";
    }

    /**
     * importProcess : return a Process Object and if the param is a zip file,
     * it is extracted automatically in a repertory named by the guid of the
     * Process
     * 
     * @param filePath :
     *                can be a XML File or a ZIP File
     * @return a Process Object
     */
    public Process importProcess(String filePath, String pathToExtract) {
	Process p = null;
	File f = new File(filePath);
	if (XMLUtils.isExtension(filePath, "zip")) {
	    try {
		ZIPUtils z = new ZIPUtils(f);
		if (!z.isEmpty()) {
		    p = XMLParser.getProcess(f);
		    if (p != null) {
			if (pathToExtract.length() != 0
				&& pathToExtract
					.charAt(pathToExtract.length() - 1) != File.separatorChar) {
			    pathToExtract += File.separatorChar;
			}
			z.extract(pathToExtract + p.getGuid()
				+ File.separatorChar);
		    }
		}
	    } catch (ZipException e) {

	    }
	} else {
	    p = XMLParser.getProcess(f);
	}
	return p;
    }
}
