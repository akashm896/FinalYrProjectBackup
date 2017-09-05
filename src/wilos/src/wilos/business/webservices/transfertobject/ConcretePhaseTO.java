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
import wilos.model.misc.concretephase.ConcretePhase;

public class ConcretePhaseTO extends ConcretePhase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6706906773605019978L;

	public ConcretePhaseTO() {
	}

	public ConcretePhaseTO(ConcretePhase _myConcretePhase) {
		this.setId(_myConcretePhase.getId());
		this.setConcreteName(_myConcretePhase.getConcreteName());
		this.setPhase(new PhaseTO(_myConcretePhase.getPhase()));

		Set<ConcreteActivity> concreteActivitys = new HashSet<ConcreteActivity>();
		// for (ConcreteActivity cta :
		// _myConcretePhase.getSuperConcreteActivities()) {
		// concreteActivitys.add(new ConcreteActivityTO(cta));
		// }

		concreteActivitys = ConcreteActivityTO
				.getConcreteActivities(_myConcretePhase);

		this.setSuperConcreteActivities(concreteActivitys);
	}

}
