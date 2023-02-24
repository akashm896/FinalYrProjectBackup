/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Mathieu BENOIT <mathieu-benoit@hotmail.fr>
 * Copyright (C) 2007-2008 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
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

package wilos.model.spem2.role;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.spem2.element.Element;
import wilos.model.spem2.guide.Guidance;

/**
 * 
 * A RoleDefinition is an {@link Element} that defines a set of related skills,
 * competencies, and responsibilities. Roles are used by Tasks to define who
 * performs them as well as define a set of work products they are responsible
 * for.
 * <p />
 * It's an element of the SPEM2 specification of the OMG organization
 * (http://www.omg.org/).
 * 
 */
public class RoleDefinition extends Element implements Cloneable {

	/**
	 * Collection of TaskDescriptor
	 */
	private Set<RoleDescriptor> roleDescriptors;

	private Set<Guidance> guidances;

	private String assignmentApproaches;

	private String skills;

	private String synonyms;

	/**
	 * Class constructor.
	 * 
	 */
	public RoleDefinition() {
		super();
		this.roleDescriptors = new HashSet<RoleDescriptor>();
		this.guidances = new HashSet<Guidance>();
		this.assignmentApproaches = "";
		this.skills = "";
		this.synonyms = "";
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the RoleDefinition.
	 * 
	 * @param obj
	 *            the Object to be compare to the RoleDefinition
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof RoleDefinition == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		RoleDefinition role = (RoleDefinition) obj;
		return new EqualsBuilder().appendSuper(super.equals(role)).append(
				this.roleDescriptors, role.roleDescriptors).append(
				this.guidances, role.guidances).append(
				this.assignmentApproaches, role.assignmentApproaches).append(
				this.skills, role.skills).append(this.synonyms, role.synonyms)
				.isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of RoleDefinition
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(this.assignmentApproaches).append(this.skills).append(
						this.synonyms).toHashCode();
	}

	/**
	 * Returns a copy of the current instance of RoleDefinition
	 * 
	 * @return a copy of the RoleDefinition
	 * @throws CloneNotSupportedException
	 */
	@Override
	public RoleDefinition clone() throws CloneNotSupportedException {
		RoleDefinition roleDefinition = new RoleDefinition();
		roleDefinition.copy(this);
		return roleDefinition;
	}

	/**
	 * Copy the values of the specified RoleDefinition into the current instance
	 * of the class.
	 * 
	 * @param _roleDefinition
	 *            The RoleDefinition to copy.
	 */
	protected void copy(final RoleDefinition _roleDefinition) {
		super.copy(_roleDefinition);
		this.roleDescriptors.addAll(_roleDefinition.getRoleDescriptors());
		this.guidances.addAll(_roleDefinition.getGuidances());
		this.assignmentApproaches = _roleDefinition.getAssignmentApproaches();
		this.skills = _roleDefinition.getSkills();
		this.synonyms = _roleDefinition.getSynonyms();
	}

	/*
	 * relation between RoleDefinition and RoleDescriptor
	 */

	/**
	 * Adds a RoleDescriptor to the Set of the RoleDefinition
	 * 
	 * @param _role
	 */
	public void addRoleDescriptor(RoleDescriptor _role) {
		this.roleDescriptors.add(_role);
		_role.setRoleDefinition(this);
	}

	/**
	 * Removes a RoleDescriptor from the RoleDefinition
	 * 
	 * @param _role
	 */
	public void removeRoleDescriptor(RoleDescriptor _role) {
		_role.setRoleDefinition(null);
		this.roleDescriptors.remove(_role);

	}

	/**
	 * Removes from a RoleDefinition all its RoleDescriptor
	 * 
	 */
	public void removeAllRoleDescriptors() {
		for (RoleDescriptor _role : this.roleDescriptors) {
			_role.setRoleDefinition(null);
		}
		this.roleDescriptors.clear();
	}

	/**
	 * Adds a RoleDescriptor collection to the RoleDescriptor collection of an
	 * RoleDefinition
	 * 
	 * @param _role
	 */
	public void addAllRoleDescriptors(Set<RoleDescriptor> _role) {
		for (RoleDescriptor _role1 : _role) {
			_role1.addRoleDefinition(this);
		}
	}

	/*
	 * connection to guidances
	 */

	/**
	 * Removes the relation between the RoleDefinition and the specified
	 * Guidance
	 * 
	 * @param _guidance
	 *            the Guidance to be unlinked to
	 */
	public void removeGuidance(Guidance _guidance) {
		_guidance.getRoleDefinitions().remove(this);
		this.guidances.remove(_guidance);
	}

	/**
	 * Adds a relation between the RoleDefinition and the specified Guidance
	 * 
	 * @param _guidance
	 *            the Guidance to be link to the RoleDefinition
	 */
	public void addGuidance(Guidance _guidance) {
		this.guidances.add(_guidance);
		_guidance.getRoleDefinitions().add(this);
	}

	/**
	 * Removes all the Guidance related to the RoleDefinition
	 */
	public void removeAllGuidances() {
		for (Guidance guidance : this.guidances) {
			guidance.getRoleDefinitions().remove(this);
		}
		this.guidances.clear();
	}

	/**
	 * Adds all the Guidance of the Collection in parameter to the
	 * RoleDefinition
	 * 
	 * @param _guidances
	 *            to be related to the RoleDefinition
	 */
	public void addAllGuidances(Set<Guidance> _guidances) {
		for (Guidance _guid1 : _guidances) {
			this.addGuidance(_guid1);
		}
	}

	/**
	 * Getter of roleDescriptors.
	 * 
	 * @return the roleDescriptors.
	 */
	public Set<RoleDescriptor> getRoleDescriptors() {
		return this.roleDescriptors;
	}

	/**
	 * Setter of roleDescriptors.
	 * 
	 * @param _roleDescriptors
	 *            The roleDescriptors to set.
	 */
	@SuppressWarnings("unused")
	public void setRoleDescriptors(Set<RoleDescriptor> _roleDescriptors) {
		this.roleDescriptors = _roleDescriptors;
	}

	/**
	 * Returns the Guidance related to the RoleDefinition
	 * 
	 * @return a Set of Guidance
	 */
	public Set<Guidance> getGuidances() {
		return guidances;
	}

	/**
	 * Sets the Collection of Guidance to the RoleDefinition
	 * 
	 * @param guidances
	 *            the Collection of Guidance to assigned to the RoleDefinition
	 */
	public void setGuidances(Set<Guidance> guidances) {
		this.guidances = guidances;
	}

	/**
	 * Returns the assignment approaches of the RoleDefinition
	 * 
	 * @return the string that symbolizes the assignment approaches
	 */
	public String getAssignmentApproaches() {
		return assignmentApproaches;
	}

	/**
	 * Sets the assignment approaches of the RoleDefinition
	 * 
	 * @param _assignmentApproaches
	 *            the assignment approaches to be set
	 */
	public void setAssignmentApproaches(String _assignmentApproaches) {
		this.assignmentApproaches = _assignmentApproaches;
	}

	/**
	 * Returns the skills for the RoleDefinition
	 * 
	 * @return the skills for the RoleDefinition
	 */
	public String getSkills() {
		return skills;
	}

	/**
	 * Sets the skills for the RoleDefinition
	 * 
	 * @param _skills
	 *            the string to set the skills
	 */
	public void setSkills(String _skills) {
		this.skills = _skills;
	}

	/**
	 * Returns the synonyms for the RoleDefinition
	 * 
	 * @return the synonyms for the RoleDefinition
	 */
	public String getSynonyms() {
		return synonyms;
	}

	/**
	 * Sets the synonyms for the RoleDefinition
	 * 
	 * @param _synonyms
	 *            the String to st the synonyms of the RoleDefinition
	 */
	public void setSynonyms(String _synonyms) {
		this.synonyms = _synonyms;
	}
}
