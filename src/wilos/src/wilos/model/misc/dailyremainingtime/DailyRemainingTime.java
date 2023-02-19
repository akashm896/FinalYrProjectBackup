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

package wilos.model.misc.dailyremainingtime;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.misc.concretetask.ConcreteTaskDescriptor;

/**
 * 
 * A ConcreteRoleDescriptor is a specific {@link RoleDescriptor} for a
 * {@link Project}.
 * 
 */
public class DailyRemainingTime {

    private String id;

    private Float remainingTime;

    private Date date;

    private ConcreteTaskDescriptor dailyTaskDescriptor_id;

    /**
	 * Returns the identifier assigned to the DailyRemainingTime.
	 * 
	 * @return the identifier of the DailyRemainingTime
	 */
    public String getId() {
	return id;
    }

    /**
	 * Sets the identifier assigned to the DailyRemainingTime.
	 */
    public void setId(String _id) {
	id = _id;
    }

    /**
	 * Class constructor
	 */
    public DailyRemainingTime() {
    }

    /**
	 * Returns a copy of the current instance of DailyRemainingTime
	 * 
	 * @return a copy of the DailyRemainingTime
	 * @throws CloneNotSupportedException
	 */
    @Override
    public DailyRemainingTime clone() throws CloneNotSupportedException {
	DailyRemainingTime dailyRemainingTime = new DailyRemainingTime();
	dailyRemainingTime.copy(this);
	return dailyRemainingTime;
    }

    /**
	 * Copy the values of the specified DailyRemainingTime into the current
	 * instance of the class.
	 * 
	 * @param _dailyRemainingTime
	 *            the DailyRemainingTime to copy
	 */
    protected void copy(final DailyRemainingTime _dailyRemainingTime) {
	this.date = _dailyRemainingTime.getDate();
	this.remainingTime = _dailyRemainingTime.getRemainingTime();
	this.dailyTaskDescriptor_id = _dailyRemainingTime
		.getDailyTaskDescriptor_id();
    }

    /**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the class.
	 * 
	 * @param obj
	 *            the Object to be compare to the DailyRemainingTime
	 * @return true if the specified Object is the same, false otherwise
	 */
    public boolean equals(Object _obj) {
	if (_obj instanceof DailyRemainingTime == false) {
	    return false;
	}
	if (this == _obj) {
	    return true;
	}
	DailyRemainingTime dailyRemainingTime = (DailyRemainingTime) _obj;
	return new EqualsBuilder()
		.appendSuper(super.equals(dailyRemainingTime)).append(
			this.dailyTaskDescriptor_id,
			dailyRemainingTime.dailyTaskDescriptor_id).append(this.date,
			dailyRemainingTime.date).append(this.remainingTime,
			dailyRemainingTime.remainingTime).isEquals();

    }

    /**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of DailyRemainingTime
	 */
    public int hashCode() {
	return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
		.append(this.date).append(this.remainingTime).append(
			this.dailyTaskDescriptor_id).toHashCode();
    }

    /*
     * Relation between DailyRemainingTime and ConcreteTaskDescriptor.
     * 
     */
    
    /**
	 * Adds a relation between the current instance of DailyRemainingTime
	 * and a specified ConcreteTaskDescriptor.
	 * 
	 * @param _concreteTaskDescriptor
	 *            the ConcreteTaskDescriptor to relate to the DailyRemainingTime
	 */
   public void addPrimaryConcreteTaskDescriptor(
	    ConcreteTaskDescriptor _concreteTaskDescriptor) {
	this.dailyTaskDescriptor_id = _concreteTaskDescriptor;
	_concreteTaskDescriptor.getDailyRemainingTimes().add(this);
    }

   /**
	 * Removes the relation between the current instance of DailyRemainingTime
	 * and a specified ConcreteTaskDescriptor.
	 * 
	 * @param _concreteTaskDescriptor
	 *            the ConcreteTaskDescriptor to unlinked to the DailyRemainingTime
	 */
    public void removePrimaryConcreteTaskDescriptor(
	    ConcreteTaskDescriptor _concreteTaskDescriptor) {
	_concreteTaskDescriptor.getDailyRemainingTimes().remove(this);
    }

    /*
     * Getter and Setter.
     * 
     */

    /**
	 * Returns the Date assigned to the DailyRemainingTime.
	 * 
	 * @return the Date of the DailyRemainingTime
	 */
    public Date getDate() {
	return date;
    }

    /**
	 * Sets the Date assigned to the DailyRemainingTime.
	 */
    public void setDate(Date _date) {
	date = _date;
    }

    /**
	 * Returns the value of the remain time
	 * 
	 * @return the remaining time
	 */
    public Float getRemainingTime() {
	return remainingTime;
    }

    /**
	 * Sets the value of the remain time
	 * 
	 * @param the remaining time to set
	 */
    public void setRemainingTime(Float _remainingTime) {
	remainingTime = _remainingTime;
    }

    /**
	 * Returns the identifier of the ConcreteTaskDescriptor assigned to the DailyRemainingTime.
	 * 
	 * @return the identifier of the ConcreteTaskDescriptor
	 */
    public ConcreteTaskDescriptor getDailyTaskDescriptor_id() {
        return dailyTaskDescriptor_id;
    }

    /**
	 * Sets the identifier of the ConcreteTaskDescriptor assigned to the DailyRemainingTime.
	 * 
	 * @param the identifier of the ConcreteTaskDescriptor to set
	 */
    public void setDailyTaskDescriptor_id(
    	ConcreteTaskDescriptor _dailytaskdescriptor_id) {
	dailyTaskDescriptor_id = _dailytaskdescriptor_id;
    }

}