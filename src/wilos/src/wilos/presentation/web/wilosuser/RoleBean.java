package wilos.presentation.web.wilosuser;

import java.util.List;
import wilos.business.services.misc.wilosuser.RoleService;
import wilos.model.misc.wilosuser.Role;



public class RoleBean {

	private List<Role> roleList;
	private RoleService roleService;
	private Role role;
	private WilosUserBean user;
	
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public List<Role> getRoleList() {
		this.roleList = this.roleService.getRoleList();
		return roleList;
	}
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	public RoleService getRoleService() {
		return roleService;
	}
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	
	public WilosUserBean getUser() {
		return user;
	}
	public void setUser(WilosUserBean user) {
		this.user = user;
	}
	
	
	
}
