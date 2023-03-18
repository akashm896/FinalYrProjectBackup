package wilos.model.misc.project;

import java.io.Serializable;

public class Affectedto implements Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String project_id;
	private String participant_id;
	
	
	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public String getParticipant_id() {
		return participant_id;
	}
	public void setParticipant_id(String participant_id) {
		this.participant_id = participant_id;
	}
}
