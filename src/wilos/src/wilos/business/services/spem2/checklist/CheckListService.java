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

package wilos.business.services.spem2.checklist;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.hibernate.spem2.checklist.CheckListDao;
import wilos.model.spem2.checklist.CheckList;
import wilos.model.spem2.section.Section;

@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class CheckListService {

	private CheckListDao checkListDao;

	public Set<Section> getSections(CheckList _checklist) {
		this.checkListDao.getSessionFactory().getCurrentSession().saveOrUpdate(_checklist);
		Set<Section> result = new HashSet<Section>();
		for (Section s : checkListDao.getCheckList(_checklist.getId())
				.getSections()) {
			result.add(s);
		}
		return result;
	}

	public CheckList getCheckList(String _id) {
		return this.checkListDao.getCheckList(_id);
	}

	public List<CheckList> getAllCheckLists() {
		return this.checkListDao.getAllCheckLists();
	}

	public String saveCheckList(CheckList _checklist) {
		return this.checkListDao.saveOrUpdateCheckList(_checklist);
	}

	public void deleteCheckList(CheckList _checklist) {
		this.checkListDao.deleteCheckList(_checklist);
	}

	/**
	 * Getter of checkListDAO.
	 * 
	 * @return the checkListDAO.
	 */
	public CheckListDao getCheckListDao() {
		return this.checkListDao;
	}

	/**
	 * Setter of checkListDAO.
	 * 
	 * @param _checklistDao
	 *            The checklistDAO to set.
	 */
	public void setCheckListDao(CheckListDao checkListDao) {
		this.checkListDao = checkListDao;
	}
}
