/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2007-2008 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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
import wilos.model.spem2.workproduct.WorkProductDefinition;

/**
 * @author Thomas Teissedre
 * 
 */
public class WorkProductDefinitionTO extends WorkProductDefinition implements
	Serializable {

    private static final long serialVersionUID = -786734744962727760L;

    /** Creates a new instance of WorkProductDefinitionTO */
    public WorkProductDefinitionTO() {
    }

    public WorkProductDefinitionTO(WorkProductDefinition myWorkProductDefinition) {

	this.setName(myWorkProductDefinition.getName());
	this.setGuid(myWorkProductDefinition.getGuid());
	this.setDescription(myWorkProductDefinition.getDescription());
	this.setMainDescription(myWorkProductDefinition.getMainDescription());
	this.setKeyConsiderations(myWorkProductDefinition
		.getKeyConsiderations());

	this.setImpactOfNotHaving(myWorkProductDefinition
		.getImpactOfNotHaving());
	this.setPurpose(myWorkProductDefinition.getPurpose());
	this.setReasonsForNotNeeding(myWorkProductDefinition
		.getReasonsForNotNeeding());
	this.setBriefOutline(myWorkProductDefinition.getBriefOutline());
	this.setRepresentationOptions(myWorkProductDefinition
		.getRepresentationOptions());

	Set<Guidance> guidances = new HashSet<Guidance>();
	for (Guidance g : myWorkProductDefinition.getGuidances()) {
	    if (g instanceof CheckList)
		guidances.add(new CheckListTO((CheckList) g));
	    else
		guidances.add(new GuidanceTO(g));
	}
	this.setGuidances(guidances);

    }
}
