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

package wilos.business.webservices.transfertobject;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import wilos.model.spem2.checklist.CheckList;
import wilos.model.spem2.guide.Guidance;
import wilos.model.spem2.phase.Phase;

public class PhaseTO extends Phase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4681548168470197482L;

	public PhaseTO() {
	}

	public PhaseTO(Phase _phase) {
		if (_phase != null) {
			this.setDescription(_phase.getDescription());
			this.setName(_phase.getName());
			this.setGuid(_phase.getGuid());
			this.setPresentationName(_phase.getPresentationName());

			this.setMainDescription(_phase.getMainDescription());
			this.setKeyConsiderations(_phase.getKeyConsiderations());

			Set<Guidance> guidances = new HashSet<Guidance>();
			for (Guidance g : _phase.getGuidances()) {
				if (g instanceof CheckList)
					guidances.add(new CheckListTO((CheckList) g));
				else
					guidances.add(new GuidanceTO(g));
			}
			this.setGuidances(guidances);
		}
	}

}
