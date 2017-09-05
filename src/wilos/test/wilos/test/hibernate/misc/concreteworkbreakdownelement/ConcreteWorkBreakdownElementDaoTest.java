/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Sebastien BALARD <sbalard@wilos-project.org>
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

package wilos.test.hibernate.misc.concreteworkbreakdownelement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.hibernate.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElementDao;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.test.TestConfiguration;
import wilos.utils.Constantes;

/**
 * @author Sebastien
 *
 * Unit test for WorkBreakdownElementDao
 *
 */
public class ConcreteWorkBreakdownElementDaoTest {

    private ConcreteWorkBreakdownElementDao concreteWorkBreakdownElementDao = (ConcreteWorkBreakdownElementDao) TestConfiguration
	    .getInstance().getApplicationContext().getBean("ConcreteWorkBreakdownElementDao");

    private ConcreteWorkBreakdownElement concreteWorkBreakdownElement = null;

    Date date = null;

    @Before
    public void setUp() {

	try {
	    date = Constantes.DATE_FORMAT.parse("18/01/2007 10:00");
	} catch (ParseException e) {
	    e.printStackTrace();
	}

	this.concreteWorkBreakdownElement = new ConcreteWorkBreakdownElement();
	this.concreteWorkBreakdownElement.setConcreteName("My concrete name");
	this.concreteWorkBreakdownElement.setPlannedTime(25.0f);
	this.concreteWorkBreakdownElement.setPlannedFinishingDate(date);
    }

    @After
    public void tearDown() {
	this.concreteWorkBreakdownElement = null;
    }

    @Test
    public void testStorageOfANullConcreteWorkBreakdownElement() {

	ConcreteWorkBreakdownElement concreteWorkBreakdownElement = null;

	String id = this.concreteWorkBreakdownElementDao
		.saveOrUpdateConcreteWorkBreakdownElement(concreteWorkBreakdownElement);
	ConcreteWorkBreakdownElement retrievedConcreteWorkBreakdownElement = this.concreteWorkBreakdownElementDao
		.getConcreteWorkBreakdownElement(id);

	assertNull("null", retrievedConcreteWorkBreakdownElement);
    }

    @Test
    public void testStorageOfAConcreteWorkBreakdownElement() {

	String id = this.concreteWorkBreakdownElementDao
		.saveOrUpdateConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement);
	ConcreteWorkBreakdownElement retrievedConcreteWorkBreakdownElement = this.concreteWorkBreakdownElementDao
		.getConcreteWorkBreakdownElement(id);

	assertEquals("concreteWorkBreakdownElement : concrete name", this.concreteWorkBreakdownElement
		.getConcreteName(), retrievedConcreteWorkBreakdownElement.getConcreteName());
	assertEquals("concreteWorkBreakdownElement : planned time", this.concreteWorkBreakdownElement.getPlannedTime(),
		retrievedConcreteWorkBreakdownElement.getPlannedTime());
	assertEquals("concreteWorkBreakdownElement : planned finishing date", this.concreteWorkBreakdownElement
		.getPlannedFinishingDate(), retrievedConcreteWorkBreakdownElement.getPlannedFinishingDate());
	assertNotSame("same object", this.concreteWorkBreakdownElement, retrievedConcreteWorkBreakdownElement);

	this.concreteWorkBreakdownElementDao.deleteConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement);
    }

    @Test
    public void testDeletionOfAConcreteWorkBreakdownElement() {

	String id = this.concreteWorkBreakdownElementDao
		.saveOrUpdateConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement);
	this.concreteWorkBreakdownElementDao.deleteConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement);
	ConcreteWorkBreakdownElement retrievedConcreteWorkBreakdownElement = this.concreteWorkBreakdownElementDao
		.getConcreteWorkBreakdownElement(id);

	assertNull("null", retrievedConcreteWorkBreakdownElement);
    }

    @Test
    public void testRetrievalOfAllConcreteWorkBreakdownElements() {

	int nb = this.concreteWorkBreakdownElementDao.getAllConcreteWorkBreakdownElements().size();

	ConcreteWorkBreakdownElement concreteWorkBreakdownElement1 = new ConcreteWorkBreakdownElement();
	concreteWorkBreakdownElement1.setConcreteName("My concrete name");
	concreteWorkBreakdownElement1.setPlannedTime(25.0f);
	concreteWorkBreakdownElement1.setPlannedFinishingDate(date);

	ConcreteWorkBreakdownElement concreteWorkBreakdownElement2 = new ConcreteWorkBreakdownElement();
	concreteWorkBreakdownElement2.setConcreteName("Your concrete name");
	concreteWorkBreakdownElement2.setPlannedTime(7.0f);
	concreteWorkBreakdownElement2.setPlannedFinishingDate(date);

	this.concreteWorkBreakdownElementDao
		.saveOrUpdateConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement);
	this.concreteWorkBreakdownElementDao.saveOrUpdateConcreteWorkBreakdownElement(concreteWorkBreakdownElement1);
	this.concreteWorkBreakdownElementDao.saveOrUpdateConcreteWorkBreakdownElement(concreteWorkBreakdownElement2);

	List<ConcreteWorkBreakdownElement> concreteWorkBreakdownElements = this.concreteWorkBreakdownElementDao
		.getAllConcreteWorkBreakdownElements();
	assertNotNull("list not null", concreteWorkBreakdownElements);
	assertEquals("size", 3, concreteWorkBreakdownElements.size() - nb);

	this.concreteWorkBreakdownElementDao.deleteConcreteWorkBreakdownElement(concreteWorkBreakdownElement2);
	this.concreteWorkBreakdownElementDao.deleteConcreteWorkBreakdownElement(concreteWorkBreakdownElement1);
	this.concreteWorkBreakdownElementDao.deleteConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement);

	concreteWorkBreakdownElement2 = null;
	concreteWorkBreakdownElement1 = null;

    }

}
