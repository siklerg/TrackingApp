package hu.helixlab.tracking.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "roles", schema = "public")
public class Roles extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7796410086419764350L;
	private int roleId;
	private String roleName;
	
	public Roles() {
	}

	public Roles(int roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}

	

	@Id
	@SequenceGenerator(name="roles_role_id_seq", sequenceName="roles_role_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.AUTO, generator="roles_role_id_seq")

	@Column(name = "role_id", unique = true, nullable = false)
	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	@Column(name = "role_name", nullable = false, length = 20)
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		return "Roles [roleId=" + roleId + ", roleName=" + roleName + "]";
	}

	

}
