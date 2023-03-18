package wilos.business.webservices.transfertobject;

import wilos.model.spem2.milestone.Milestone;

public class MilestoneTO extends Milestone {
	public MilestoneTO(Milestone _milestone) {
		if (_milestone != null) {
			this.setDescription(_milestone.getDescription());
			this.setName(_milestone.getName());
			this.setGuid(_milestone.getGuid());
			this.setPresentationName(_milestone.getPresentationName());
			this.setMainDescription(_milestone.getMainDescription());
			this.setKeyConsiderations(_milestone.getKeyConsiderations());
		}

	}
}
