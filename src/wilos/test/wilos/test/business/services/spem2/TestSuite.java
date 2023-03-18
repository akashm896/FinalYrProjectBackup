/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Sebastien BALARD <sbalard@wilos-project.org>
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
package wilos.test.business.services.spem2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
@RunWith(Suite.class)
@Suite.SuiteClasses( {
	wilos.test.business.services.spem2.activity.TestSuite.class,
	wilos.test.business.services.spem2.process.TestSuite.class,
	wilos.test.business.services.spem2.role.TestSuite.class,
	wilos.test.business.services.spem2.section.TestSuite.class,
	wilos.test.business.services.spem2.guide.TestSuite.class,
	wilos.test.business.services.spem2.checklist.TestSuite.class,
	wilos.test.business.services.spem2.workbreakdownelement.TestSuite.class,
	wilos.test.business.services.spem2.workproduct.TestSuite.class,

	})
public class TestSuite {
	// None.
}
