/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Mathieu BENOIT <mathieu-benoit@hotmail.fr>
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

package wilos.model.spem2.activity;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.spem2.breakdownelement.BreakdownElement;
import wilos.model.spem2.guide.Guidance;
import wilos.model.spem2.workbreakdownelement.WorkBreakdownElement;


/**
 * 
 * An Activity is a {@link WorkBreakdownElement} which supports the nesting and logical
 * grouping of related Breakdown Elements forming breakdown structures. Although
 * Activity is a concrete meta-class, other classes which represent breakdown
 * structures derive from it; such as {@link Phase}, {@link Iteration} or {@link Process}.
 * <p />
 * It's an element of the SPEM2 specification of the OMG
 *         organization (http://www.omg.org/). 
 * 
 */
public class Activity extends WorkBreakdownElement implements Cloneable {

    private SortedSet<BreakdownElement> breakdownElements;

    private Set<ConcreteActivity> concreteActivities;

    private Set<Guidance> guidances;

    private String alternatives;

    private String howToStaff;

    private String purpose;
    
    /**
     * Default constructor.
     * 
     */
    public Activity() {
	super();
	this.breakdownElements = new TreeSet<BreakdownElement>();
	this.concreteActivities = new HashSet<ConcreteActivity>();
	this.guidances = new HashSet<Guidance>();
	this.howToStaff = "";
	this.purpose = "";
	this.alternatives = "";
    }

    /**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the class.
	 * 
	 * @param _obj
	 *            the Object to be compare to the Activity
	 * @return true if the specified Object is the same, false otherwise
	 */
    public boolean equals(Object obj) {
	if (obj instanceof Activity == false) {
	    return false;
	}
	if (this == obj) {
	    return true;
	}
	Activity activity = (Activity) obj;
	return new EqualsBuilder().appendSuper(super.equals(activity)).append(
		this.breakdownElements, activity.breakdownElements).append(
		this.concreteActivities, activity.concreteActivities).append(
		this.guidances, activity.guidances).append(this.alternatives,
		activity.alternatives).append(this.howToStaff,
		activity.howToStaff).append(this.purpose, activity.purpose)
		.isEquals();
    }

    /**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of Activity
	 */
    public int hashCode() {
	return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
		.append(this.alternatives).append(this.howToStaff).append(
			this.purpose).toHashCode();
    }

    /**
	 * Returns a copy of the current instance of Activity
	 * 
	 * @return a copy of the Activity
	 * @throws CloneNotSupportedException
	 */
    @Override
    public Activity clone() throws CloneNotSupportedException {
	Activity activity = new Activity();
	activity.copy(this);
	return activity;
    }

    /**
     * Copy the _activity into the current Activity.
     * 
     * @param _activity
     *            The Activity to copy.
     */
    protected void copy(final Activity _activity) {
	super.copy(_activity);
	this.breakdownElements.addAll(_activity.getBreakdownElements());
	this.concreteActivities.addAll(_activity.getConcreteActivities());
	this.guidances.addAll(_activity.getGuidances());
	this.alternatives = _activity.getAlternatives();
	this.howToStaff = _activity.getHowToStaff();
	this.purpose = _activity.getPurpose();
    }

    /*
     * Manage BreakdownElement relation.
     */

    /**
     * Add a {@link BreakdownElement} to the BreakdownElement collection of an
     * activity.
     * 
     * @param _breakdownElement
     *            The {@link BreakdownElement} to add.
     */
    public void addBreakdownElement(BreakdownElement _breakdownElement) {
	this.getBreakdownElements().add(_breakdownElement);
	_breakdownElement.getSuperActivities().add(this);
    }

    /**
     * Add a {@link BreakdownElement} collection to the BreakdownElement
     * collection of an activity.
     * 
     * @param _breakdownElements
     *            The set of {@link BreakdownElement} to add.
     */
    public void addAllBreakdownElements(SortedSet<BreakdownElement> _breakdownElements) {
	for (BreakdownElement bde : _breakdownElements) {
	    bde.addSuperActivity(this);
	}
    }

    /**
     * Remove from an Activity all its break down elements.
     * 
     */
    public void removeAllBreakdownElements() {
	for (BreakdownElement bde : this.getBreakdownElements())
	    bde.getSuperActivities().remove(this);
	this.getBreakdownElements().clear();
    }

    /**
     * Remove from an Activity one of these break downe elements.
     * 
     * @param _breakdownElement
     *            The {@link BreakdownElement} to remove.
     */
    public void removeBreakdownElement(BreakdownElement _breakdownElement) {
	_breakdownElement.getSuperActivities().remove(this);
	this.getBreakdownElements().remove(_breakdownElement);
    }

    /*
     * Manage ConcreteActivity relation.
     */

    /**
     * Add a {@link ConcreteActivity} to the BreakdownElement collection of an
     * Activity.
     * 
     * @param _concreteActivity
     *            The {@link ConcreteActivity} to add.
     */
    public void addConcreteActivity(ConcreteActivity _concreteActivity) {
	this.concreteActivities.add(_concreteActivity);
	_concreteActivity.setActivity(this);
    }

    /**
     * Add a {@link ConcreteActivity} collection to the concrete activities
     * collection of an activity.
     * 
     * @param _concreteActivities
     *            The set of {@link ConcreteActivity} to add.
     */
    public void addAllConcreteActivities(Set<ConcreteActivity> _concreteActivities) {
	for (ConcreteActivity ca : _concreteActivities) {
	    this.addConcreteActivity(ca);
	}
    }

    /**
     * Remove from an Activity one of these concrete activities.
     * 
     * @param _concreteActivity
     *            The {@link ConcreteActivity} to remove.
     */
    public void removeConcreteActivity(ConcreteActivity _concreteActivity) {
	this.concreteActivities.remove(_concreteActivity);
	_concreteActivity.setActivity(null);
    }

    /**
     * Remove from an Activity all its concrete activities.
     * 
     */
    public void removeAllConcreteActivities() {

	for (ConcreteActivity tmp : this.concreteActivities) {
	    tmp.setActivity(null);
	}
	this.concreteActivities.clear();
    }

    /*
     * Manage Guidance relation.
     */

    /**
     * Remove from a Guidance one of these guidances.
     * 
     * @param _guidance
     *            The {@link Guidance} to remove.
     */
    public void removeGuidance(Guidance _guidance) {
	_guidance.getActivities().remove(this);
	this.guidances.remove(_guidance);
    }

    /**
     * @param _guidance
     */
    public void addGuidance(Guidance _guidance) {
	this.guidances.add(_guidance);
	_guidance.getActivities().add(this);
    }

    /**
     * Remove from an Activity all its guidances.
     * 
     */
    public void removeAllGuidances() {
	for (Guidance guidance : this.guidances) {
	    guidance.getActivities().remove(this);
	}
	this.guidances.clear();
    }

    /**
     * @param _guidances
     */
    public void addAllGuidances(Set<Guidance> _guidances) {
	for (Guidance _guid1 : _guidances) {
	    this.addGuidance(_guid1);
	}
    }

    /*
     * Getters & Setters.
     */

    /**
     * Getter of break down elements collection.
     * 
     * @return the break down elements collection.
     */
    public SortedSet<BreakdownElement> getBreakdownElements() {
	return this.breakdownElements;
    }

    /**
     * Setter of breakDownElements.
     * 
     * @param _breakDownElements
     *            The breakDownElements to set.
     */
    public void setBreakdownElements(
	    SortedSet<BreakdownElement> _breakDownElements) {
	this.breakdownElements = _breakDownElements;
    }

    /**
     * Getter of concrete activities.
     * 
     * @return
     */
    public Set<ConcreteActivity> getConcreteActivities() {
	return concreteActivities;
    }

    /**
     * Setter of concrete activities collection.
     * 
     * @param _concreteActivities
     *            The concrete activities collection to set.
     */
    public void setConcreteActivities(Set<ConcreteActivity> _concreteActivities) {
	this.concreteActivities = _concreteActivities;
    }

    /**
     * Getter of guidances.
     * 
     * @return The guidances collection to get.
     */
    public Set<Guidance> getGuidances() {
	return guidances;
    }

    /**
     * Setter of guidances collection.
     * 
     * @param _guidances
     *            The guidances collection to set.
     */
    public void setGuidances(Set<Guidance> _guidances) {
	this.guidances = _guidances;
    }

    /**
     * Getter of alternatives.
     * 
     * @return The alternatives collection.
     */
    public String getAlternatives() {
	return alternatives;
    }

    /**
     * Setter of alternatives.
     * 
     * @param _alternatives
     */
    public void setAlternatives(String _alternatives) {
	this.alternatives = _alternatives;
    }

    /**
     * Getter of howToStaff.
     * 
     * @return
     */
    public String getHowToStaff() {
	return howToStaff;
    }

    /**
     * Setter of howToStaff.
     * 
     * @param _howToStaff
     */
    public void setHowToStaff(String _howToStaff) {
	this.howToStaff = _howToStaff;
    }

    /**
     * Getter of purpose.
     * 
     * @return
     */
    public String getPurpose() {
	return purpose;
    }

    /**
     * Setter of purpose.
     * 
     * @param _purpose
     */
    public void setPurpose(String _purpose) {
	this.purpose = _purpose;
    }   
}
