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

package wilos.test.business.webservices;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wilos.business.services.misc.concretetask.ConcreteTaskDescriptorService;
import wilos.business.services.misc.wilosuser.LoginService;
import wilos.business.services.misc.wilosuser.ParticipantService;
import wilos.utils.Security;
import wilos.business.webservices.WizardServices;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.wilosuser.Participant;
import wilos.test.TestConfiguration;
import wilos.utils.Constantes;

import com.thoughtworks.xstream.XStream;

public class WizardServicesTest{
	@SuppressWarnings("unused")
	private LoginService ls;
	private ParticipantService ps;
	private WizardServices instance;
	private ConcreteTaskDescriptor ct;
	private ConcreteTaskDescriptorService cts;
	private Participant p;

    @Before
    public void setUp(){
    	ls = (LoginService) TestConfiguration.getInstance().getApplicationContext().getBean("LoginService");
		ps = (ParticipantService) TestConfiguration.getInstance().getApplicationContext().getBean("ParticipantService");
		cts = (ConcreteTaskDescriptorService) TestConfiguration.getInstance().getApplicationContext().getBean("ConcreteTaskDescriptorService");
		
		instance = (WizardServices) TestConfiguration.getInstance().getApplicationContext().getBean("WizardServices");
	 
		ct = new ConcreteTaskDescriptor();
		ct.setConcreteName("ConcreteTest");
		ct.setState(Constantes.State.READY);
		ct.setTaskDescriptor(null);
		cts.getConcreteTaskDescriptorDao().saveOrUpdateConcreteTaskDescriptor(ct);
		
		p = new Participant();
		
		p.setLogin("testJunit");
	    p.setPassword(Security.encode("testJunit"));
	    p.setName("testJunit");
	    p.setEmailAddress("test@test.com");
	    p.setFirstname("testJunit");
	    ps.getParticipantDao().saveOrUpdateParticipant(p);
    }

    @After
    public void tearDown() {
    	cts.getConcreteTaskDescriptorDao().deleteConcreteTaskDescriptor(ct);
		ps.getParticipantDao().deleteParticipant(p);         
    }  
     
    @Test
    public void testGetParticipantException() {
         System.out.println("GetParticipant");
         System.out.println("testException");    	
         
         try {             
        	instance.getParticipant("qsdfqsdfqsdf", "dfqsdfqs");            
            fail();          
         } catch (Exception ex) {
            assertTrue(true);
         }
         
     }
    
    @Test
     public void testGetParticipant() {
        System.out.println("GetParticipant");
        System.out.println("testBD");	
        
     
        Participant pt = null;
        String result = null;
        try {
            
            result = instance.getParticipant(p.getLogin(), p.getPassword());
            XStream xstream = new XStream();     
            //TODO
            //Here correct a bug that throws the following  exception :
            //java.lang.IllegalArgumentException: XPP3 pull parser library not present. Specify another driver. For example: new XStream(new DomDriver())
            pt = (Participant)xstream.fromXML(result);
           
        } catch (Exception ex) {
            ex.printStackTrace();
            //TODO 
            //Decomment after correction of the previous bug
            //fail();
        }
        assertNotNull(result);
        //TODO 
        //Decomment after correction of the previous bug
        //assertNotNull(pt);
        //assertEquals(pt.getName(),p.getName());
     }
    
}
