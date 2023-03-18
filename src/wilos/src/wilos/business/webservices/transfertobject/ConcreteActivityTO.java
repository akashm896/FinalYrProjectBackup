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

import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concreteiteration.ConcreteIteration;
import wilos.model.misc.concretephase.ConcretePhase;
import wilos.model.misc.project.Project;

public class ConcreteActivityTO extends ConcreteActivity implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3857147546912132326L;

	/** Creates a new instance of ParticipantTO */
	public ConcreteActivityTO() {

	}

	public ConcreteActivityTO(ConcreteActivity _myConcreteActivity) {
		this.setId(_myConcreteActivity.getId());
		this.setConcreteName(_myConcreteActivity.getConcreteName());
		this.setActivity(new ActivityTO(_myConcreteActivity.getActivity()));

		Set<ConcreteActivity> concreteActivitys = new HashSet<ConcreteActivity>();
		concreteActivitys = getConcreteActivities(_myConcreteActivity);

		this.setSuperConcreteActivities(concreteActivitys);
	}

	public static HashSet<ConcreteActivity> getConcreteActivities(
			ConcreteBreakdownElement ca) {

		HashSet<ConcreteActivity> concreteActivities = new HashSet<ConcreteActivity>();
		for (ConcreteActivity cta : ca.getSuperConcreteActivities()) {
			if (cta instanceof ConcreteIteration) {
				concreteActivities.add(new ConcreteIterationTO(
						(ConcreteIteration) cta));
			} else {
				if (cta instanceof ConcretePhase) {
					concreteActivities.add(new ConcretePhaseTO(
							(ConcretePhase) cta));
				} else {
					if (cta instanceof Project) {
						concreteActivities.add(new ProjectTO((Project) cta));
					} else {
						concreteActivities.add(new ConcreteActivityTO(cta));
					}
				}
			}
		}
		return concreteActivities;
	}

	/***************************************************************************
	 * /** Creates a new instance of ParticipantTO public ConcreteActivityTO() {
	 *  }
	 * 
	 * public ConcreteActivityTO(ConcreteActivity _myConcreteActivity) {
	 * this.setId(_myConcreteActivity.getId());
	 * this.setConcreteName(_myConcreteActivity.getConcreteName());
	 * this.setActivity(new ActivityTO(_myConcreteActivity.getActivity()));
	 * 
	 * Set<ConcreteActivity> concreteActivitys = new HashSet<ConcreteActivity>();
	 * for (ConcreteActivity cta :
	 * _myConcreteActivity.getSuperConcreteActivities()) {
	 * concreteActivitys.add(new ConcreteActivityTO(cta)); }
	 * this.setSuperConcreteActivities(concreteActivitys); }
	 */

}
