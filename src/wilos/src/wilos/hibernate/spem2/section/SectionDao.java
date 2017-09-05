/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 * Copyright (C) 2007 Sebastien BALARD <sbalard@wilos-project.org>
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

package wilos.hibernate.spem2.section;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import wilos.model.spem2.section.Section;

/**
 * SectionDao manage requests from the system to store Sections into the database
 */
public class SectionDao extends HibernateDaoSupport {

    /**
     * Saves or updates a Section
     * 
     * @param _section The Section to be saved or updated
     */
    public String saveOrUpdateSection(Section _section) {
	if (_section != null) {
	    this.getHibernateTemplate().saveOrUpdate(_section);
	    return _section.getId();
	}
	return "";
    }

    /**
     * Returns a list of all the Sections
     * 
     * @return A list of all the Sections
     */
    public List<Section> getAllSections() {
	List<Section> sections = new ArrayList<Section>();
	for (Object obj : this.getHibernateTemplate().loadAll(Section.class)) {
	    Section s = (Section) obj;
	    sections.add(s);
	}
	return sections;
    }

    /**
     * Returns the Section which has the specified ID
     * 
     * @param _id The wanted Section's ID
     * @return The wanted Section
     */
    public Section getSection(String _id) {
	if (!_id.equals(""))
	    return (Section) this.getHibernateTemplate().get(Section.class, _id);
	return null;
    }

    /**
     * Deletes a Section
     * 
     * @param _section The Section to be deleted
     */
    public void deleteSection(Section _section) {
	this.getHibernateTemplate().delete(_section);
    }

}
