package hu.helixlab.tracking;

import hu.helixlab.tracking.entity.KmlFiles;
import hu.helixlab.tracking.entity.Roles;
import hu.helixlab.tracking.entity.UserRole;
import hu.helixlab.tracking.entity.Users;
import hu.helixlab.tracking.menu.Menu;
import hu.helixlab.tracking.service.EntityService;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Application {

	public static void main(String[] args) throws Exception {
		//Léterhozzuk az admin felhasználót
/*		Users admin = new Users();
		admin.setUsername("admin");
		admin.setNickname("AdminAndi");
		admin.setFullName("Wágenhoffer Andrea");
		admin.setEmail("wagenhofferandrea@gmail.com");
		admin.setPassword(BCrypt.hashpw("kiscica", BCrypt.gensalt()));
		EntityService es = new EntityService();
		es.save(admin);
		//Léterhozzuk az admin role-okat, az admin mindent megkap
		List<Roles> roles = (List<Roles>)es.search(Roles.class, null);
		Set<UserRole> userRoles = new HashSet<>();
		for (Roles role : roles) {
			UserRole ur = new UserRole();
			ur.setRoles(role);
			ur.setUsers(admin);
			userRoles.add(ur);
			es.save(ur);
		}
		admin.setUserRoles(userRoles);
		es.closeEntityManager();*/

		Menu.getInstance().run();
	}

}
