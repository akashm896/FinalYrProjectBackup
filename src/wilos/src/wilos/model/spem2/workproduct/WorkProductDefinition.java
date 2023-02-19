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

package wilos.model.spem2.workproduct;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;

import wilos.model.spem2.element.Element;
import wilos.model.spem2.guide.Guidance;

import wilos.model.spem2.workproduct.WorkProductDescriptor;

public class WorkProductDefinition extends Element {

	/**
	 * Collection of WorkProductDescriptor
	 */
	private Set<WorkProductDescriptor> workProductDescriptors;

	private String impactOfNotHaving;
	private String purpose;
	private String reasonsForNotNeeding;
	private String briefOutline;
	private String representationOptions;

	private Set<Guidance> guidances;

	/**
	 * Default constructor
	 */
	public WorkProductDefinition() {
		super();
		this.guidances = new HashSet<Guidance>();
		this.workProductDescriptors = new HashSet<WorkProductDescriptor>();
		this.impactOfNotHaving = "";
		this.purpose = "";
		this.reasonsForNotNeeding = "";
		this.briefOutline = "";
		this.representationOptions = "";
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the WorkProductDefinition.
	 * 
	 * @param obj
	 *            the Object to be compare to the WorkProductDefinition
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof WorkProductDefinition == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		WorkProductDefinition workProduct = (WorkProductDefinition) obj;
		return new EqualsBuilder().appendSuper(super.equals(workProduct))
				.isEquals();
	}

	/**
	 * Returns a copy of the current instance of WorkProductDefinition
	 * 
	 * @return a copy of the WorkProductDefinition
	 * @throws CloneNotSupportedException
	 */
	public WorkProductDefinition clone() throws CloneNotSupportedException {
		WorkProductDefinition workProductDefinition = new WorkProductDefinition();
		workProductDefinition.copy(this);
		return workProductDefinition;
	}

	/**
	 * Copy the values of the specified WorkProductDefinition into the current
	 * instance of the class.
	 * 
	 * @param _workProductDefinition
	 *            The WorkProductDefinition to copy.
	 */
	protected void copy(final WorkProductDefinition _workProductDefinition) {
		super.copy(_workProductDefinition);
		this.workProductDescriptors.addAll(_workProductDefinition
				.getWorkProductDescriptors());
		this.guidances.addAll(_workProductDefinition.getGuidances());
	}

	/*
	 * connection to guidances
	 */
	/**
	 * Removes the relation between the WorkProductDefinition and the specified
	 * Guidance
	 * 
	 * @param _guidance
	 *            the Guidance to unlink to the WorkProductDefinition
	 */
	public void removeGuidance(Guidance _guidance) {
		_guidance.getWorkProductDefinitions().remove(this);
		this.guidances.remove(_guidance);
	}

	/**
	 * Adds a relation between the WorkProductDefinition and the specified
	 * Guidance
	 * 
	 * @param _guidance
	 *            the Guidance to relate to the WorkProductDefinition
	 */
	public void addGuidance(Guidance _guidance) {
		this.guidances.add(_guidance);
		_guidance.getWorkProductDefinitions().add(this);
	}

	/**
	 * Removes the WorkProductDefinition from all the related Guidance
	 */
	public void removeAllGuidances() {
		for (Guidance guidance : this.guidances) {
			guidance.getWorkProductDefinitions().remove(this);
		}
		this.guidances.clear();
	}

	/**
	 * Adds all the Guidance from the Collection in parameter to the WorkProductDefinition
	 * 
	 * @param _guidances
	 *            the Guidances to add to the WorkProductDefinition
	 */
	public void addAllGuidances(Set<Guidance> _guidances) {
		for (Guidance _guid1 : _guidances) {
			this.addGuidance(_guid1);
		}
	}

	/**
	 * Returns the Collection of Guidance related to the WorkProductDefinition
	 * 
	 * @return the Set of the Guidance related to the WorkProductDefinition
	 */
	public Set<Guidance> getGuidances() {
		return this.guidances;
	}

	/**
	 * Sets the Collection of Guidance related to the WorkProductDefinition
	 * 
	 * @param _guidances the Set of Guidance to set
	 */
	public void setGuidances(Set<Guidance> _guidances) {
		this.guidances = _guidances;
	}

	/**
	 * Getter of workProductDescriptors.
	 * 
	 * @return the workProductDescriptors.
	 */
	public Set<WorkProductDescriptor> getWorkProductDescriptors() {
		return workProductDescriptors;
	}

	/**
	 * Setter of workProductDescriptors.
	 * 
	 * @param _workProductDescriptors
	 *            The workProductDescriptors to set.
	 */
	public void setWorkProductDescriptors(
			Set<WorkProductDescriptor> workProductDescriptors) {
		this.workProductDescriptors = workProductDescriptors;
	}

	/*
	 * relation between WorkProductDefinition and WorkProductDescriptor
	 */
	
	/**
	 * Adds a WorkProductDescriptor to the set
	 * 
	 * @param _workProductDescriptor
	 */
	public void addWorkProductDescriptor(
			WorkProductDescriptor _workProductDescriptor) {
		this.workProductDescriptors.add(_workProductDescriptor);
		_workProductDescriptor.setWorkProductDefinition(this);
	}

	/**
	 * Removes a WorkProductDescriptor
	 * 
	 * @param _workProductDescriptor
	 */
	public void removeWorkProductDescriptor(
			WorkProductDescriptor _workProductDescriptor) {
		_workProductDescriptor.setWorkProductDefinition(null);
		this.workProductDescriptors.remove(_workProductDescriptor);

	}

	/**
	 * Removes from an WorkProductDefinition all its WorkProductDescriptor
	 * 
	 */
	public void removeAllWorkProductDescriptors() {
		for (WorkProductDescriptor _workProduct : this.workProductDescriptors) {
			_workProduct.setWorkProductDefinition(null);
		}
		this.workProductDescriptors.clear();
	}

	/**
	 * Adds a WorkProductDescriptor collection to the WorkProductDescriptor
	 * collection of an WorkProductDefinition
	 * 
	 * @param _workProduct
	 */
	public void addAllWorkProductDescriptors(
			Set<WorkProductDescriptor> _workProduct) {
		for (WorkProductDescriptor _workProduct1 : _workProduct) {
			_workProduct1.addWorkProductDefinition(this);
		}
	}

	/**
	 * Getter of the attribute impactOfNotHaving
	 * 
	 * @return the impactOfNotHaving
	 */
	public String getImpactOfNotHaving() {
		return impactOfNotHaving;
	}

	/**
	 * Setter of the attribute impactOfNotHaving
	 * 
	 * @param _impactOfNotHaving
	 *            the impactOfNotHaving to set
	 */
	public void setImpactOfNotHaving(String _impactOfNotHaving) {
		impactOfNotHaving = _impactOfNotHaving;
	}

	/**
	 * Getter of the attribute purpose
	 * 
	 * @return the purpose
	 */
	public String getPurpose() {
		return purpose;
	}

	/**
	 * Setter of the attribute purpose
	 * 
	 * @param _purpose
	 *            the purpose to set
	 */
	public void setPurpose(String _purpose) {
		purpose = _purpose;
	}

	/**
	 * Getter of the attribute reasonsForNotNeeding
	 * 
	 * @return the reasonsForNotNeeding
	 */
	public String getReasonsForNotNeeding() {
		return reasonsForNotNeeding;
	}

	/**
	 * Setter of the attribute reasonsForNotNeeding
	 * 
	 * @param _reasonsForNotNeeding
	 *            the reasonsForNotNeeding to set
	 */
	public void setReasonsForNotNeeding(String _reasonsForNotNeeding) {
		reasonsForNotNeeding = _reasonsForNotNeeding;
	}

	/**
	 * Getter of the attribute briefOutline
	 * 
	 * @return the briefOutline
	 */
	public String getBriefOutline() {
		return briefOutline;
	}

	/**
	 * Setter of the attribute briefOutline
	 * 
	 * @param _briefOutline
	 *            the briefOutline to set
	 */
	public void setBriefOutline(String _briefOutline) {
		briefOutline = _briefOutline;
	}

	/**
	 * Getter of the attribute representationOptions
	 * 
	 * @return the representationOptions
	 */
	public String getRepresentationOptions() {
		return representationOptions;
	}

	/**
	 * Setter of the attribute representationOptions
	 * 
	 * @param _representationOptions
	 *            the representationOptions to set
	 */
	public void setRepresentationOptions(String _representationOptions) {
		representationOptions = _representationOptions;
	}
}
