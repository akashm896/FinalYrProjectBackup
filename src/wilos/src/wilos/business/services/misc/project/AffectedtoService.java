package wilos.business.services.misc.project;

import java.util.List;

import wilos.hibernate.misc.project.AffectedtoDao;
import wilos.model.misc.wilosuser.WilosUser;

public class AffectedtoService {

	private AffectedtoDao affectedtoDao;

	public String participantAffectedto(String id){
		return this.affectedtoDao.getAffectedToByIdParticipant(id);
	}
	
	
	public List<WilosUser> affected ( List<WilosUser> listUser){
		for(int i = 0; i < listUser.size(); i++){
			if(Integer.parseInt(listUser.get(i).getRole_id()) == 3){
				listUser.get(i).setDeleted("false");
			}else{
				listUser.get(i).setDeleted(this.participantAffectedto(listUser.get(i).getId()));
			}
		}
		return listUser;
	}
	
	public AffectedtoDao getAffectedtoDao() {
		return affectedtoDao;
	}

	public void setAffectedtoDao(AffectedtoDao affectedDao) {
		this.affectedtoDao = affectedDao;
	}
	
}
