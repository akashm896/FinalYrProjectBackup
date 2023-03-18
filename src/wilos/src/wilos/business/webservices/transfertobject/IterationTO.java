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
import wilos.model.spem2.iteration.Iteration;

public class IterationTO extends Iteration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6845546096183827985L;

	public IterationTO() {
	}

	public IterationTO(Iteration _iteration) {
		if (_iteration != null) {
			this.setDescription(_iteration.getDescription());
			this.setName(_iteration.getName());
			this.setGuid(_iteration.getGuid());
			this.setPresentationName(_iteration.getPresentationName());

			this.setMainDescription(_iteration.getMainDescription());
			this.setKeyConsiderations(_iteration.getKeyConsiderations());

			Set<Guidance> guidances = new HashSet<Guidance>();
			for (Guidance g : _iteration.getGuidances()) {
				if (g instanceof CheckList)
					guidances.add(new CheckListTO((CheckList) g));
				else
					guidances.add(new GuidanceTO(g));
			}
			this.setGuidances(guidances);
		}
	}

}
