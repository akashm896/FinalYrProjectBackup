package wilos.business.services.misc.wilosuser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.model.misc.wilosuser.WilosUser;
import wilos.utils.Security;
import wilos.hibernate.misc.wilosuser.WilosUserDao;

@Transactional(propagation = Propagation.REQUIRED)
public class WilosUserService {

	private WilosUserDao wilosUserDao;
	private List<WilosUser> list;

	/**
	 * Allows to get the list of user
	 * 
	 * @return the list of user
	 */
	public List<WilosUser> getUser() {
		this.list = new ArrayList<WilosUser>();
		this.list = this.wilosUserDao.getAllWilosUsers();
		return this.list;
	}

	/**
	 * Find a user by a login
	 * 
	 * @param _login
	 *            the login
	 * @return the associated user
	 */
	public WilosUser getUserByLogin(String _login) {
		return this.wilosUserDao.getUserByLogin(_login);
	}

	/**
	 * Find a user by a email
	 * 
	 * @param _email
	 *            the email
	 * @return the associated user
	 */
	public WilosUser getUserByEmail(String _email) {
		return this.wilosUserDao.getUserByEmail(_email);
	}

	/**
	 * Save a user
	 * 
	 * @param _user
	 *            the user to save
	 */
	public void saveWilosUser(WilosUser _user) {
		if (_user.getNewPassword() != null
				&& !_user.getNewPassword().trim().equalsIgnoreCase("")) {
			_user.setPassword(Security.encode(_user.getNewPassword()));
		}
		this.wilosUserDao.saveOrUpdateWilosUser(_user);
	}

	/**
	 * Save a user
	 * 
	 * @param _user
	 *            the user to save
	 */
	public void saveWilosUserWithoutEncryption(WilosUser _user) {
		this.wilosUserDao.saveOrUpdateWilosUser(_user);
	}

	/**
	 * Allows to delete a participant
	 * 
	 * @param participantId
	 */
	public Boolean deleteWilosuser(String userId) {
		WilosUser user = this.getSimpleUser(userId);
		if (user != null) {
			this.wilosUserDao.deleteWilosUser(user);
			return true;
		}
		return false;
	}

	public WilosUser getSimpleUser(String id) {
		return this.wilosUserDao.getWilosUserById(id);
	}

	public WilosUserDao getWilosUserDao() {
		return wilosUserDao;
	}

	public void setWilosUserDao(WilosUserDao wilosUserDao) {
		this.wilosUserDao = wilosUserDao;
	}

	public List<WilosUser> getUserByRole(String idRole) {
		if (idRole.equalsIgnoreCase("*") || idRole.equalsIgnoreCase("99"))
			return this.wilosUserDao.getAllWilosUsers();
		else
			return this.wilosUserDao.getWilosUserByRole(idRole);
	}
}
