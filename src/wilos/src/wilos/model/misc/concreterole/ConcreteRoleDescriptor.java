/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Mathieu BENOIT <mathieu-benoit@hotmail.fr>
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

package wilos.model.misc.concreterole;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.concreteworkproduct.ConcreteWorkProductDescriptor;
import wilos.model.misc.project.Project;
import wilos.model.misc.wilosuser.Participant;
import wilos.model.spem2.role.RoleDescriptor;

/**
 * 
 * A ConcreteRoleDescriptor is a specific {@link RoleDescriptor} for a
 * {@link Project}.
 * 
 */
public class ConcreteRoleDescriptor extends ConcreteBreakdownElement {

	private RoleDescriptor roleDescriptor;

	private Participant participant;

	private Set<ConcreteTaskDescriptor> primaryConcreteTaskDescriptors;

	private Set<ConcreteWorkProductDescriptor> concreteWorkProductDescriptors;

	/**
	 * Class constructor
	 */
	public ConcreteRoleDescriptor() {
		this.primaryConcreteTaskDescriptors = new HashSet<ConcreteTaskDescriptor>();
		this.concreteWorkProductDescriptors = new HashSet<ConcreteWorkProductDescriptor>();
	}

	/**
	 * Returns a copy of the current instance of ConcreteRoleDescriptor
	 * 
	 * @return a copy of the ConcreteRoleDescriptor
	 * @throws CloneNotSupportedException
	 */
	@Override
	public ConcreteRoleDescriptor clone() throws CloneNotSupportedException {
		ConcreteRoleDescriptor concreteRoleDescriptor = new ConcreteRoleDescriptor();
		concreteRoleDescriptor.copy(this);
		return concreteRoleDescriptor;
	}

	/**
	 * Copy the values of the specified ConcreteRoleDescriptor into the current
	 * instance of the class.
	 * 
	 * @param _concreteRoleDescriptor
	 *            the ConcreteRoleDescriptor to copy
	 */
	protected void copy(final ConcreteRoleDescriptor _concreteRoleDescriptor) {
		super.copy(_concreteRoleDescriptor);
		this.participant = _concreteRoleDescriptor.getParticipant();
		this.roleDescriptor = _concreteRoleDescriptor.getRoleDescriptor();
		this.primaryConcreteTaskDescriptors.addAll(_concreteRoleDescriptor
				.getPrimaryConcreteTaskDescriptors());
	}

	/**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the class.
	 * 
	 * @param obj
	 *            the Object to be compare to the ConcreteRoleDescriptor
	 * @return true if the specified Object is the same, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof ConcreteRoleDescriptor == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		ConcreteRoleDescriptor concreteRoleDescriptor = (ConcreteRoleDescriptor) obj;
		return new EqualsBuilder().appendSuper(
				super.equals(concreteRoleDescriptor)).append(
				this.primaryConcreteTaskDescriptors,
				concreteRoleDescriptor.primaryConcreteTaskDescriptors).append(
				this.participant, concreteRoleDescriptor.participant).append(
				this.roleDescriptor, concreteRoleDescriptor.roleDescriptor)
				.isEquals();
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of ConcreteRoleDescriptor
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(this.participant).append(this.roleDescriptor)
				.toHashCode();
	}

	/*
	 * relation between ConcreteRoleDescriptor and Participant.
	 */

	/**
	 * Adds a relation between the current instance of ConcreteRoleDescriptor
	 * and a specified Participant.
	 * 
	 * @param _participant
	 *            the Participant to relate to the ConcreteRoleDescriptor
	 */
	public void addParticipant(Participant _participant) {
		this.participant = _participant;
		_participant.getConcreteRoleDescriptors().add(this);
	}

	/**
	 * Removes the relation between the current instance of
	 * ConcreteRoleDescriptor and the specified Participant.
	 * 
	 * @param _participant
	 *            the Participant to unlinked to the ConcreteRoleDescriptor
	 */
	public void removeParticipant(Participant _participant) {
		_participant.getConcreteRoleDescriptors().remove(this);
		this.participant = null;
	}

	/*
	 * Relation between ConcreteRoleDescriptor and RoleDescriptor.
	 * 
	 */

	/**
	 * Adds a relation between the current instance of ConcreteRoleDescriptor
	 * and a specified RoleDescriptor.
	 * 
	 * @param _roleDescriptor
	 *            the RoleDescriptor to link with the ConcreteRoleDescriptor
	 */
	public void addRoleDescriptor(RoleDescriptor _roleDescriptor) {
		this.roleDescriptor = _roleDescriptor;
		_roleDescriptor.getConcreteRoleDescriptors().add(this);
	}

	/**
	 * Removes the relation between the current instance of
	 * ConcreteRoleDescriptor and the specified RoleDescriptor.
	 * 
	 * @param _roleDescriptor
	 *            the RoleDescriptor to unlinked to the ConcreteRoleDescriptor
	 */
	public void removeRoleDescriptor(RoleDescriptor _roleDescriptor) {
		_roleDescriptor.getConcreteRoleDescriptors().remove(this);
		this.roleDescriptor = null;
	}

	/*
	 * Relation between ConcreteRoleDescriptor and ConcreteTaskDescriptor.
	 * 
	 */

	/**
	 * Adds a relation between the ConcreteRoleDescriptor and a specified
	 * ConcreteTaskDescriptor.
	 * 
	 * @param _concreteTaskDescriptor
	 *            the ConcreteTaskDescriptor to assign to the
	 *            ConcreteRoleDescriptor
	 */
	public void addPrimaryConcreteTaskDescriptor(
			ConcreteTaskDescriptor _concreteTaskDescriptor) {
		this.primaryConcreteTaskDescriptors.add(_concreteTaskDescriptor);
		_concreteTaskDescriptor.setMainConcreteRoleDescriptor(this);
	}

	/**
	 * Removes the relation between the ConcreteRoleDescriptor and the specified
	 * ConcreteTaskDescriptor.
	 * 
	 * @param _concreteTaskDescriptor
	 *            the ConcreteTaskDescriptor to unlinked to the
	 *            ConcreteTaskDescriptor
	 */
	public void removePrimaryConcreteTaskDescriptor(
			ConcreteTaskDescriptor _concreteTaskDescriptor) {
		_concreteTaskDescriptor.setMainConcreteRoleDescriptor(null);
		this.primaryConcreteTaskDescriptors.remove(_concreteTaskDescriptor);
	}

	/**
	 * Adds a relation between the ConcreteRoleDescriptor and all the specified
	 * ConcreteTaskDescriptors.
	 * 
	 * @param _concreteTaskDescriptors
	 *            the Set of ConcreteTaskDescriptors to relate to the
	 *            ConcreteRoleDescriptor
	 */
	public void addAllPrimaryConcreteTaskDescriptors(
			Set<ConcreteTaskDescriptor> _concreteTaskDescriptors) {
		this.primaryConcreteTaskDescriptors.addAll(_concreteTaskDescriptors);

		for (ConcreteTaskDescriptor ctd : _concreteTaskDescriptors) {
			ctd.addMainConcreteRoleDescriptor(this);
		}
	}

	/**
	 * Removes the relation between the ConcreteRoleDescriptor and all the
	 * related ConcreteTaskDescriptor.
	 */
	public void removeAllPrimaryConcreteTaskDescriptors() {
		for (ConcreteTaskDescriptor ctd : this
				.getPrimaryConcreteTaskDescriptors())
			ctd.setMainConcreteRoleDescriptor(null);
		this.getPrimaryConcreteTaskDescriptors().clear();
	}

	/**
	 * Adds a relation between the ConcreteRoleDescriptor and a specified
	 * ConcreteWorkProductDescriptor.
	 * 
	 * @param _product
	 *            the ConcreteWorkProductDescriptor to assign to the
	 *            ConcreteRoleDescriptor
	 */
	public void addConcreteWorkProductDescriptor(
			ConcreteWorkProductDescriptor _product) {
		_product.setResponsibleConcreteRoleDescriptor(this);
		this.concreteWorkProductDescriptors.add(_product);
	}

	/**
	 * Removes the relation between the ConcreteRoleDescriptor and a specified
	 * ConcreteWorkProductDescriptor.
	 * 
	 * @param _product
	 *            the ConcreteWorkProductDescriptor to unlinked to the
	 *            ConcreteRoleDescriptor
	 */
	public void removeConcreteWorkProductDescriptor(
			ConcreteWorkProductDescriptor _product) {
		this.concreteWorkProductDescriptors.remove(_product);
		_product.removeResponsibleConcreteRoleDescriptor(this);
	}

	/*
	 * Getter and Setter.
	 * 
	 */

	/**
	 * Returns the RoleDescriptor assigned to the ConcreteRoleDescriptor.
	 * 
	 * @return the RoleDescriptor of the ConcreteRoleDescriptor
	 */
	public RoleDescriptor getRoleDescriptor() {
		return roleDescriptor;
	}

	/**
	 * Initializes the RoleDescriptor of the ConcreteRoleDescriptor.
	 * 
	 * @param _roleDescriptor
	 *            the RoleDescriptor to set
	 */
	public void setRoleDescriptor(RoleDescriptor _roleDescriptor) {
		this.roleDescriptor = _roleDescriptor;
	}

	/**
	 * Returns the Set of ConcreteTaskDescriptor assigned to the
	 * ConcreteRoleDescriptor.
	 * 
	 * @return the Set of ConcreteTaskDescriptor
	 */
	public Set<ConcreteTaskDescriptor> getPrimaryConcreteTaskDescriptors() {
		return primaryConcreteTaskDescriptors;
	}

	/**
	 * Initializes the Collection of ConcreteTaskDescriptors of the
	 * ConcreteRoleDescriptor with the values of the Set in parameter.
	 * 
	 * @param concreteTaskDescriptors
	 *            the Set of ConcreteTaskDescriptor to assigned
	 */
	public void setPrimaryConcreteTaskDescriptors(
			Set<ConcreteTaskDescriptor> concreteTaskDescriptors) {
		this.primaryConcreteTaskDescriptors = concreteTaskDescriptors;
	}

	/**
	 * Returns the Participant assigned to the ConcreteRoleDescriptor.
	 * 
	 * @return the Participant of the ConcreteRoleDescriptor
	 */
	public Participant getParticipant() {
		return participant;
	}

	/**
	 * Initializes the Participant of the ConcreteRoleDescriptor.
	 * 
	 * @param participant
	 *            the Participant to set
	 */
	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	/**
	 * Returns the getConcreteWorkProductDescriptors assigned to the
	 * ConcreteRoleDescriptor.
	 * 
	 * @return the getConcreteWorkProductDescriptors of the
	 *         ConcreteRoleDescriptor
	 */
	public Set<ConcreteWorkProductDescriptor> getConcreteWorkProductDescriptors() {
		return this.concreteWorkProductDescriptors;
	}

	/**
	 * Initializes the Collection of ConcreteWorkProductDescriptor of the
	 * ConcreteRoleDescriptor with the values of the Set in parameter.
	 * 
	 * @param _concreteWorkProductDescriptors
	 *            the Set of ConcreteWorkProductDescriptor to assigned
	 */
	public void setConcreteWorkProductDescriptors(
			Set<ConcreteWorkProductDescriptor> _concreteWorkProductDescriptors) {
		this.concreteWorkProductDescriptors = _concreteWorkProductDescriptors;
	}

}