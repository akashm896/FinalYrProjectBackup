/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007-2008 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

package wilos.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author deder
 * 
 * This class represents the configuration for unit tests of this project (by
 * using spring framework). Each TestCase must use this class to use its
 * protected properties.
 * 
 */
public class TestConfiguration {

    private static final String CONFIG_FILES_LOCATIONS[] = {
	    "spring_datasource.xml", "spring_services.xml", "spring_dao.xml" };

    // The application context from the spring configuration file
    private ApplicationContext applicationContext = null;

    private static TestConfiguration testConfiguration = null;

    private TestConfiguration() {
	this.applicationContext = new ClassPathXmlApplicationContext(
		CONFIG_FILES_LOCATIONS);
    }

    public static TestConfiguration getInstance() {
	if (testConfiguration == null)
	    testConfiguration = new TestConfiguration();
	return testConfiguration;
    }

    public ApplicationContext getApplicationContext() {
	return this.applicationContext;
    }
}
