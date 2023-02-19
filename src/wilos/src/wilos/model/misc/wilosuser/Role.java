package wilos.model.misc.wilosuser;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

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

public class Role implements Serializable{

		private String role_id;

		private String name;

		
		/**
		 * Default constructor.
		 * 
		 */
		public Role() {
			// None.
		}

		/**
		 * Constructor.
		 * 
		 * @param _name the user name
		 * @param _fName the user first name
		 * @param _email the user email address
		 * @param _login the user login
		 * @param _password the user password
		 */
		public Role(String _name) {
			this.name = "";
		}

		/**
		 * Getter of name.
		 * 
		 * @return the name.
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * Setter of name.
		 * 
		 * @param _name
		 *            The name to set.
		 */
		public void setName(String _name) {
			this.name = _name;
		}

		/**
		 * Defines if the specified Object is the same or has the same values as the
		 * current instance of the class.
		 * 
		 * @param _obj
		 *            the Object to be compare to the WilosUser
		 * @return true if the specified Object is the same, false otherwise
		 */
		public boolean equals(Object obj) {
			if (obj instanceof Role == false) {
				return false;
			}
			if (this == obj) {
				return true;
			}
			Role role = (Role) obj;
			return new EqualsBuilder().append(this.name, role.name).isEquals();
		}

		/**
		 * Returns a hash code value for the object. This method is supported for
		 * the benefit of hash tables.
		 * 
		 * @return the hash code of the current instance of WilosUser
		 */
		public int hashCode() {
			return new HashCodeBuilder(17, 37).append(this.name).toHashCode();
		}

		/**
		 * Getter of the WilosUser identifier.
		 * 
		 * @return the WilosUser identifier.
		 */
		public String getRole_id() {
			return this.role_id;
		}

		/**
		 * Setter of the WilosUser identifier.
		 * 
		 * @param the WilosUser identifier to set
		 */
		public void setRole_id(String _id) {
			this.role_id = _id;
		}

}
