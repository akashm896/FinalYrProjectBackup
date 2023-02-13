/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

package wilos.business.webservices;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.jws.WebService;

import wilos.business.services.misc.concretetask.ConcreteTaskDescriptorService;
import wilos.business.services.misc.wilosuser.LoginService;
import wilos.business.services.misc.wilosuser.ParticipantService;
import wilos.business.services.spem2.guide.GuidanceService;
import wilos.model.misc.wilosuser.Participant;
import wilos.model.misc.wilosuser.WilosUser;

import com.thoughtworks.xstream.XStream;



@WebService(serviceName = "WizardServices", endpointInterface = "wilos.business.webservices.WizardServices")
public class WizardServicesImpl implements WizardServices {

	private LoginService loginService;

	private ParticipantService participantService;

	private ConcreteTaskDescriptorService concreteTaskDescriptorService;

	private GuidanceService guidanceService;


	/* (non-Javadoc)
	 * @see wilos.business.webservices.WizardServices#getParticipant(java.lang.String, java.lang.String)
	 */
	public String getParticipant(String _login, String _password)
			throws Exception {
		String result = "";
		Participant wu = new Participant();

		wu = this.participantService
				.getParticipantTO(getAuthentifiedParticipant(_login, _password)
						.getLogin());

		XStream xstream = new XStream();

		result = xstream.toXML(wu);

		/*
		 * File xstreamFile = new File ("xstream.xml"); PrintStream out = new
		 * PrintStream(new FileOutputStream(xstreamFile), false, "UTF-8");
		 * out.println(result);
		 */

		return result;
	}

	/* (non-Javadoc)
	 * @see wilos.business.webservices.WizardServices#getGuidanceAttachment(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public byte[] getGuidanceAttachment(String _login, String _password,
			String _idGuidance, String _file) throws Exception {
		String filePath = "";
		Participant wu = new Participant();
		byte[] contenuFichier = null;

		System.out.println("APPEL DE LA METHODE getGuidanceAttachment");

		wu = getAuthentifiedParticipant(_login, _password);
		if (wu != null) {
			System.out.println("Le user " + wu.getName() + " est logge");

			filePath = this.guidanceService.getAttachmentFilePath(_idGuidance,
					_file);

			// serialisation du fichier

			File myfile = new File(filePath);
			if (myfile.exists()) {
				// ouverture du fichier
				try {
					// Ouverture du fichier passé en paramètre dans la ligne
					// de commande
					InputStream fluxFichier = new FileInputStream(filePath);

					// Lecture des n premiers octets du fichier. n est passé en
					// paramètre
					contenuFichier = new byte[(int) myfile.length()];
					fluxFichier.read(contenuFichier);

					// Fermeture du fichier
					fluxFichier.close();

				} catch (IOException e) {
					// Exception déclenchée si un problème survient pendant
					// l'accès au fichier
					System.out.println(e);
				}
			} else
				contenuFichier = null;
		}

		/*
		 * File xstreamFile = new File ("xstream.xml"); PrintStream out = new
		 * PrintStream(new FileOutputStream(xstreamFile), false, "UTF-8");
		 * out.println(result);
		 */

		return contenuFichier;
	}

	/* (non-Javadoc)
	 * @see wilos.business.webservices.WizardServices#startConcreteTaskDescriptor(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void startConcreteTaskDescriptor(String login, String password,
			String _taskId) throws Exception {
		if (getAuthentifiedParticipant(login, password) != null) {
			this.concreteTaskDescriptorService.startConcreteTaskDescriptor(_taskId);
		}
	}

	/* (non-Javadoc)
	 * @see wilos.business.webservices.WizardServices#suspendConcreteTaskDescriptor(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void suspendConcreteTaskDescriptor(String _login, String _password,
			String _taskId) throws Exception {
		if (getAuthentifiedParticipant(_login, _password) != null) {
			this.concreteTaskDescriptorService
					.suspendConcreteTaskDescriptor(_taskId);
		}
	}

	/* (non-Javadoc)
	 * @see wilos.business.webservices.WizardServices#stopConcreteTaskDescriptor(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void stopConcreteTaskDescriptor(String _login, String _password,
			String _taskId) throws Exception {
		if (getAuthentifiedParticipant(_login, _password) != null) {
			this.concreteTaskDescriptorService.finishConcreteTaskDescriptor(_taskId);
		}
	}

	/* (non-Javadoc)
	 * @see wilos.business.webservices.WizardServices#setAccomplishedTimeByTask(java.lang.String, java.lang.String, java.lang.String, float)
	 */
	public void setAccomplishedTimeByTask(String _login, String _password,
			String _taskId, float _newTime) throws Exception {
		if (getAuthentifiedParticipant(_login, _password) != null) {
			this.concreteTaskDescriptorService.setAccomplishedTimeByTask(
					_taskId, _newTime);
		}
	}

	/* (non-Javadoc)
	 * @see wilos.business.webservices.WizardServices#setRemainingTimeByTask(java.lang.String, java.lang.String, java.lang.String, float)
	 */
	public void setRemainingTimeByTask(String _login, String _password,
			String _taskId, float _newTime) throws Exception {
		if (getAuthentifiedParticipant(_login, _password) != null) {
			this.concreteTaskDescriptorService.setRemainingTimeByTask(_taskId,
					_newTime);
		}
	}

	/**
	 * return the participant with the given login and password
	 * 
	 * @param login
	 * @param password
	 * @return the participant
	 * @throws Exception if login or password are wrong
	 */
	private Participant getAuthentifiedParticipant(String _login, String _password)
			throws Exception {
		Participant result = null;
		WilosUser tmpwu = loginService.getAuthentifiedUser(_login, _password);
		if (tmpwu == null) {
			throw new Exception("le wilos user n'existe pas");
		} else {
			if (!(tmpwu instanceof Participant)) {
				throw new Exception("le wilos user n'est pas un particpant");
			} else {
				result = (Participant) tmpwu;
			}
		}
		return result;
	}

	/* Getters & Setters */

	public ParticipantService getParticipantService() {
		return participantService;
	}

	public void setParticipantService(ParticipantService _participantService) {
		this.participantService = _participantService;
	}

	public ConcreteTaskDescriptorService getConcreteTaskDescriptorService() {
		return concreteTaskDescriptorService;
	}

	public void setConcreteTaskDescriptorService(
			ConcreteTaskDescriptorService _concreteTaskDescriptorService) {
		this.concreteTaskDescriptorService = _concreteTaskDescriptorService;
	}

	public GuidanceService getGuidanceService() {
		return guidanceService;
	}

	public void setGuidanceService(GuidanceService _guidanceService) {
		this.guidanceService = _guidanceService;
	}

	public LoginService getLoginService() {
		return loginService;
	}

	public void setLoginService(LoginService _loginService) {
		this.loginService = _loginService;
	}
}
