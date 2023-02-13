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
import wilos.model.misc.concreteiteration.ConcreteIteration;

public class ConcreteIterationTO extends ConcreteIteration implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8530841673060460523L;

	public ConcreteIterationTO() {
	}

	public ConcreteIterationTO(ConcreteIteration _myConcreteIteration) {
		this.setId(_myConcreteIteration.getId());
		this.setConcreteName(_myConcreteIteration.getConcreteName());
		this.setIteration(new IterationTO(_myConcreteIteration.getIteration()));

		Set<ConcreteActivity> concreteActivitys = new HashSet<ConcreteActivity>();
		// for (ConcreteActivity cta :
		// _myConcretePhase.getSuperConcreteActivities()) {
		// concreteActivitys.add(new ConcreteActivityTO(cta));
		// }

		concreteActivitys = ConcreteActivityTO
				.getConcreteActivities(_myConcreteIteration);

		this.setSuperConcreteActivities(concreteActivitys);
	}

}
