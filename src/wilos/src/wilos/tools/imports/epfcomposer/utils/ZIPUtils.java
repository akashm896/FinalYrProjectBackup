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

package wilos.tools.imports.epfcomposer.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ZIPUtils {
    static final int BUFFER = 2048;

    static final String prefix = "test_";

    private ZipFile zipfile;

    // private File file ;

    /**
     * ZIPUtils : Object containing an archive
     * 
     * @param file
     *                the archive
     * @throws ZipException
     *                 if the zip can't be read
     */
    public ZIPUtils(File file) throws ZipException {
	try {
	    zipfile = new ZipFile(file);
	    // this.file = file ;
	} catch (ZipException e) {
	    zipfile = null;
	    if (!file.exists()
		    || !e.getMessage().equals("error in opening zip file")) {
		// if the exception is not error in opening zip file we have to
		// throw
		// and if the file doesn' exist
		throw e;
	    }

	} catch (IOException e) {
	    // if there is an I/O Exception we transform this in ZIPException
	    // for the catcher
	    throw new ZipException(e.getMessage());
	}

    }

    /**
     * isEmpty check if the Zip contains files
     * 
     * @return true if the Zip is empty
     */
    public boolean isEmpty() {
	return (zipfile == null);
    }

    /**
     * getXMLStream return the XML from the ZIP File as an inputStream
     * 
     * @return the stream
     */
    public InputStream getXMLStream() {
	InputStream str = null;
	ZipEntry entry = getXMLEntry();
	if (entry != null) {
	    try {
		str = zipfile.getInputStream(entry);
	    } catch (IOException e1) {
		e1.printStackTrace();
	    }
	}
	return str;
    }

    /**
     * getXMLEntry return the entry corresponding at the ZIP file
     * 
     * @return e ZipEntry
     */
    public ZipEntry getXMLEntry() {
	ZipEntry entry = null;
	ZipEntry retour = null;
	int sizePath = Integer.MAX_VALUE;
	boolean trouve = false;
	// if the file is not empty
	if (!isEmpty()) {
	    Enumeration<?> e = zipfile.entries();
	    // searching a xml file
	    while (e.hasMoreElements()) {
		entry = (ZipEntry) e.nextElement();

		trouve = XMLUtils.isExtension(entry.getName(), "xml");
		if (trouve) {
		    String name = entry.getName();
		    int pos = name.lastIndexOf(File.separator);
		    if (pos < sizePath) {
			sizePath = pos;
			retour = entry;
		    }
		}
	    }
	}
	return retour;
    }

    /**
     * getXMLFile return the XML from the ZIP File as an inputStream => creates
     * a temporary file
     * 
     * @return the file
     */
    public File getXMLFile() {
	File fileReturned = null;
	ZipEntry entry = getXMLEntry();
	BufferedOutputStream dest = null;
	BufferedInputStream is = null;
	if (entry != null) {
	    try {
		String path = zipfile.getName().substring(0,
			zipfile.getName().lastIndexOf(File.separator) + 1)
			+ prefix + entry.getName();
		fileReturned = new File(path);

		fileReturned.deleteOnExit();
		is = new BufferedInputStream(zipfile.getInputStream(entry));

		int count;
		byte data[] = new byte[BUFFER];

		// writing the file from the zip in the repertory of the zipfile
		FileOutputStream fos = new FileOutputStream(fileReturned);
		dest = new BufferedOutputStream(fos, BUFFER);
		while ((count = is.read(data, 0, BUFFER)) != -1) {
		    dest.write(data, 0, count);
		}

		dest.flush();
		dest.close();
		is.close();
	    } catch (IOException io) {

	    }
	}
	return fileReturned;
    }

    /**
     * extract takes the files in the zip and extract then in the path specified
     * 
     * @param path
     */
    public void extract(String path) {
	if (path.length() != 0
		&& path.charAt(path.length() - 1) != File.separatorChar) {
	    path += File.separatorChar;
	}
	ZipEntry entry = null;
	// if the file is not empty
	if (!isEmpty()) {
	    Enumeration<?> e = zipfile.entries();
	    // searching a xml file
	    while (e.hasMoreElements()) {
		entry = (ZipEntry) e.nextElement();
		// save on the disk
		BufferedOutputStream dest = null;
		BufferedInputStream is = null;
		try {
		    is = new BufferedInputStream(zipfile.getInputStream(entry));
		    int count;
		    byte data[] = new byte[BUFFER];
		    // writing the file from the zip in the repertory of the
		    // zipfile

		    File f = new File(path + entry.getName()
			    + File.separatorChar);
		    //System.out.println(f.getAbsolutePath());
		    if (entry.isDirectory()) {
			f.mkdirs();
		    } else {
			FileOutputStream fos = new FileOutputStream(f);
			dest = new BufferedOutputStream(fos, BUFFER);
			while ((count = is.read(data, 0, BUFFER)) != -1) {
			    dest.write(data, 0, count);
			}
			dest.flush();
			dest.close();
			is.close();
		    }

		} catch (IOException io) {

		}
	    }
	}
    }
}
