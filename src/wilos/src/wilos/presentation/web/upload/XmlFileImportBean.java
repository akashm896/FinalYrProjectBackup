/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007-2008 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

package wilos.presentation.web.upload;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wilos.business.services.spem2.process.ProcessService;
import wilos.model.spem2.process.Process;
import wilos.presentation.web.template.ActionBean;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;
import wilos.resources.LocaleBean;

import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;

public class XmlFileImportBean {

	private int percent = -1;
	private PersistentFacesState state = null;
	private File file = null;
	private ProcessService processService;
	protected final Log logger = LogFactory.getLog(this.getClass());
	private String fileName = "";
	private String contentType = "";
	private String uploadStatus = "";
	private int status;

	/**
	 * Getter of the upload status
	 * @return the upload status
	 */
	public String getUploadStatus() {
		return uploadStatus;
	}

	/**
	 * Setter of the upload status
	 * @param _uploadStatus the new upload status
	 */
	public void setUploadStatus(String _uploadStatus) {
		this.uploadStatus = _uploadStatus;
	}

	/**
	 * Constructor of the Bean
	 */
	public XmlFileImportBean() {
		state = PersistentFacesState.getInstance();
	}

	/**
	 * Setter of the percent
	 * @param percent the new percent value
	 */
	public void setPercent(int percent) {
		this.percent = percent;
	}

	/**
	 * Getter of the percent
	 * @return the percent
	 */
	public int getPercent() {
		return percent;
	}

	/**
	 * Setter of the file
	 * @param file the new file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/***
	 * Getter of the file
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Action listener of the file upload element of the page
	 * @param event event occuring
	 */
	public void uploadFileActionListener(ActionEvent event) {

		InputFile inputFile = (InputFile) event.getSource();

		if (inputFile.getFile() == null && file == null) {
			// stop the progress bar
			this.percent = -1;
			WebCommonService.addInfoMessage(LocaleBean
					.getText("XmlFileImportBean.noFile"));
		} else {
			if (inputFile.getStatus() == InputFile.SAVED) {
				fileName = inputFile.getFileInfo().getFileName();
				contentType = inputFile.getFileInfo().getContentType();
				setFile(inputFile.getFile());
			}
			if (!(contentType.equalsIgnoreCase("application/zip")
					|| contentType.equalsIgnoreCase("application/x-zip") 
					|| contentType.equalsIgnoreCase("application/x-zip-compressed"))) {
				if (!(contentType.equalsIgnoreCase("text/xml")
						|| contentType.equalsIgnoreCase("application/xml") 
						|| contentType.equalsIgnoreCase("application/x-xml"))) {
					WebCommonService.addInfoMessage(LocaleBean
							.getText("XmlFileImportBean.noGoodExtensionFile"));
					file.delete();
					this.percent = -1;
					return;
				} else {
					// xml upload ok
					uploadStatus = LocaleBean.getText("XmlFileImportBean.xmlFileUploadOk");
				}
			} else {
				// zip upload ok
				uploadStatus = LocaleBean.getText("XmlFileImportBean.zipFileUploadOk");
			}
			if (inputFile.getStatus() == InputFile.INVALID) {
				// stop the progress bar
			    	// the file is invalid
				this.percent = -1;
				// inputFile.getFileInfo().getException().printStackTrace();
				WebCommonService.addInfoMessage(LocaleBean.getText("XmlFileImportBean.noFile"));
			} else {
			    //the file is valid, we can upload it
			    this.uploadFile(inputFile);
			}
			state = PersistentFacesState.getInstance();
		}
	}

	/**
	 * The file is valid
	 * We can upload it
	 * @param _inputFile the file to upload
	 */
	private void uploadFile(InputFile _inputFile){

		if (_inputFile.getStatus() == InputFile.SIZE_LIMIT_EXCEEDED) {
		    //the size of the file is limited
			_inputFile.getFileInfo().getException().printStackTrace();
		}
		if (_inputFile.getStatus() == InputFile.UNKNOWN_SIZE) {
		    //the size of the file is not known
			_inputFile.getFileInfo().getException().printStackTrace();
		}
		ExternalContext extCtx = FacesContext.getCurrentInstance()
				.getExternalContext();
		//logger.debug("### fichier uploade = " + file.getPath() + " => "+ file.getName() + " ###");

		extCtx.getResourceAsStream("");

		// generating current date for file's dedicated directory
		Format formatter = new SimpleDateFormat("_yyyy.MM.dd.HH.mm.ss");
		String stringDateId = formatter.format(new Date());
		//logger.debug("### XmlFileImportBean ### date generation -> date="
		//				+ stringDateId);
		try {
			// creating new directory
			File targetDirForUploadedFile = new File(this.file
					.getAbsolutePath().substring(0,
							this.file.getAbsolutePath().lastIndexOf(File.separator))
					+ File.separator
					+ this.file.getName()
					+ stringDateId);
			targetDirForUploadedFile.mkdirs();

			if (targetDirForUploadedFile.isDirectory()) {

				// moving uploaded file to dedicated directory
				File targetFileInNewDir = new File(
						targetDirForUploadedFile.getAbsolutePath()
								+ File.separator + this.file.getName());

				if (!this.file.renameTo(targetFileInNewDir)) {
					throw new Exception("failed to move file");
				} else {
					this.file = targetFileInNewDir;
				}
			} else {
				logger.error("### XmlFileImportBean ### target directory -> "
								+ "directory has not been created!");
			}
		} catch (Exception e) {
			logger.error("### XmlFileImportBean ### file move -> " + e);
		}
		
		try {
			Process p = processService
					.importProcessFromEpfComposer(file);

			// check if the process already exists.
			boolean alreadyExists = false;
			List<Process> list = this.processService.getAllProcesses();
			for (Process tmp : list){
				if (p != null) {
					if (tmp.getGuid() != null) {
						if (tmp.getGuid().equals(p.getGuid()))
							alreadyExists = true;
					}
				}
			}
			// mise en place du controle du process
			if ((p != null) && (!alreadyExists)) {
				// check that the process name doesn't exist, else
				// rename it with the guid.
				for (Process tmp : list)
					if (tmp.getPresentationName().equals(p.getPresentationName())) {
						p.setPresentationName(p.getPresentationName()+ "_" + p.getGuid());
						break;
					}

				// save the process						
				/* id = */
				String managerId = (String) WebSessionService
						.getAttribute(WebSessionService.WILOS_USER_ID);
				this.processService.saveProcess(p, managerId);
				// stop the progress bar
				this.percent = -1;
				WebCommonService.addInfoMessage(LocaleBean
						.getText("XmlFileImportBean.processok"));

				/* Forwards to the list of processes */
				ActionBean actionbean = (ActionBean) WebCommonService
						.getBean("ActionBean");
				WebCommonService.getSelectedPanel().setTemplateName(
						actionbean.getProcessManagerMain());
				WebCommonService.getSelectedPanel()
						.setTemplateNameForARole(
								actionbean.getManageProcesses());
			} else {
				this.percent = -1;
				if (alreadyExists)
					WebCommonService
							.addErrorMessage(LocaleBean
									.getText("XmlFileImportBean.processAlreadyExists"));
				else
					WebCommonService.addErrorMessage(LocaleBean
							.getText("XmlFileImportBean.processNull"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("### XmlFileImportBean ### action -> " + e);
		}	
	}
	
	/**
	 * Listener of the upload progress
	 * @param event event occuring
	 */
	public void progressListener(EventObject event) {
		// start the progress bar
		this.percent = 1;
		try {
			if (state != null) {
				state.render();
			} else {
				//System.out.println("state is null");
			}
		} catch (RenderingException ee) {
			//System.out.println(ee.getMessage());
		}
	}

	/**
	 * Setter of the file name
	 * @param fileName the new file name
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Getter of the file name
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Setter of the content type
	 * @param contentType the new content type
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Getter of the content type
	 * @return the content type
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Method which purpose is unknown...
	 * @return
	 */
	public String callAction() {
		if (status == InputFile.SAVED) {
			return "saved";
		}
		return "";
	}

	/**
	 * Getter of the process service
	 * @return the process service
	 */
	public ProcessService getProcessService() {
		return processService;
	}

	/**
	 * Setter of the process service
	 * @param processService the nex process service
	 */
	public void setProcessService(ProcessService processService) {
		this.processService = processService;
	}
}
