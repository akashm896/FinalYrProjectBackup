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
import java.util.SortedSet;
import java.util.TreeSet;

import wilos.model.spem2.checklist.CheckList;
import wilos.model.spem2.guide.Guidance;
import wilos.model.spem2.task.Step;
import wilos.model.spem2.task.TaskDefinition;

/**
 * 
 * @author toine
 */
public class TaskDefinitionTO extends TaskDefinition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 428935428254462072L;

	/** Creates a new instance of TaskDefinitionTO */
	public TaskDefinitionTO() {

	}

	public TaskDefinitionTO(TaskDefinition myTD) {
		this.setName(myTD.getName());
		this.setGuid(myTD.getGuid());
		this.setDescription(myTD.getDescription());

		this.setAlternatives(myTD.getAlternatives());
		this.setPurpose(myTD.getPurpose());

		this.setMainDescription(myTD.getMainDescription());
		this.setKeyConsiderations(myTD.getKeyConsiderations());

		SortedSet<Step> steps = new TreeSet<Step>();
		for (Step s : myTD.getSteps()) {
			steps.add(new StepTO(s));
		}
		this.addAllSteps(steps);
		for (Step s : steps) {
			s.setTaskDefinition(null);
		}

		Set<Guidance> guidances = new HashSet<Guidance>();
		for (Guidance g : myTD.getGuidances()) {
			if (g instanceof CheckList)
				guidances.add(new CheckListTO((CheckList) g));
			else
				guidances.add(new GuidanceTO(g));
		}
		this.setGuidances(guidances);
	}

}
