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

package wilos.business.services.spem2.section;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.hibernate.spem2.section.SectionDao;
import wilos.model.spem2.section.Section;

@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class SectionService {

	/* Attributes */

	private SectionDao sectionDao;

	/* Element Methods */

	public Section getSection(String _id) {
		return this.sectionDao.getSection(_id);
	}

	public List<Section> getAllSections() {
		return this.sectionDao.getAllSections();
	}

	public String saveSection(Section _section) {
		if (_section != null) {
			this.sectionDao.saveOrUpdateSection(_section);
			return _section.getId();
		}
		return null;
	}

	public void deleteSection(Section _section) {
		this.sectionDao.deleteSection(_section);
	}

	/* Getters & Setters */

	public SectionDao getSectionDao() {
		return this.sectionDao;
	}

	public void setSectionDao(SectionDao _sectionDAO) {
		this.sectionDao = _sectionDAO;
	}
}
