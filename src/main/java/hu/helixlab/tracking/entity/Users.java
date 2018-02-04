package hu.helixlab.tracking.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


@Entity
@Table(name = "users", schema = "public")
public class Users extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8104524297753716547L;
	private String username;
	private String email;
	private String fullName;
	private String nickname;
	private Date lastLogin;
	private String password;
	private Set<UserRole> userRoles = new HashSet<UserRole>(0);
	private Set<Tracking> trackings = new HashSet<Tracking>(0);

	public Users() {
	}

	public Users(String username, String email, String fullName, String nickname, Date lastLogin, String password, Set<UserRole> userRoles) {
		this.username = username;
		this.email = email;
		this.fullName = fullName;
		this.nickname = nickname;
		this.lastLogin = lastLogin;
		this.password = password;
		this.userRoles = userRoles;
	}

	@Id
	@Column(name = "username", unique = true, nullable = false, length = 25)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "email", nullable = false, length = 100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "full_name", length = 100)
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(name = "nickname", nullable = false, length = 25)
	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_login", length = 35)
	public Date getLastLogin() {
		return this.lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	@Column(name = "password", nullable = false, length = 200)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	//https://en.wikibooks.org/wiki/Java_Persistence/OneToMany
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "users")
	public Set<UserRole> getUserRoles () {
		return this.userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "users")
	public Set<Tracking> getTrackings() {
		return this.trackings;
	}

	public void setTrackings(Set<Tracking> trackings) {
		this.trackings = trackings;
	}

	@Override
	public String toString() {
		return "Users [username=" + username + ", email=" + email + ", fullName=" + fullName + ", nickname=" + nickname
				+ ", lastLogin=" + lastLogin + ", password=" + password + "]";
	}
	
}
