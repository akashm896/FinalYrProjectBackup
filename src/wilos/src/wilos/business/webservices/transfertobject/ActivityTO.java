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

import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.checklist.CheckList;
import wilos.model.spem2.guide.Guidance;

public class ActivityTO extends Activity implements Serializable {

	private static final long serialVersionUID = 1531990547468325073L;

	public ActivityTO() {
	}

	public ActivityTO(Activity _activity) {
		if (_activity != null) {
			this.setDescription(_activity.getDescription());
			this.setName(_activity.getName());
			this.setGuid(_activity.getGuid());
			this.setPresentationName(_activity.getPresentationName());

			this.setAlternatives(_activity.getAlternatives());
			this.setHowToStaff(_activity.getHowToStaff());
			this.setPurpose(_activity.getPurpose());

			this.setMainDescription(_activity.getMainDescription());
			this.setKeyConsiderations(_activity.getKeyConsiderations());

			Set<Guidance> guidances = new HashSet<Guidance>();
			for (Guidance g : _activity.getGuidances()) {
				if (g instanceof CheckList)
					guidances.add(new CheckListTO((CheckList) g));
				else
					guidances.add(new GuidanceTO(g));
			}
			this.setGuidances(guidances);
		}

	}

}
