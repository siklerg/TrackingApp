package hu.helixlab.tracking.menu;

import hu.helixlab.tracking.entity.*;
import hu.helixlab.tracking.enums.LevelEnum;
import hu.helixlab.tracking.service.EntityService;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

public class Menu {

	private static Menu INSTANCE;
	
	private Menu () {
		
	}
	
	public static Menu getInstance () {
		if (INSTANCE == null) {
			INSTANCE = new Menu();
		}
		return INSTANCE;
	}
	
	public void run() {
		int flag = 0;
		EntityService es = new EntityService();
		Scanner scanner = new Scanner(System.in);
		//pl a teljes név esetén csak az első szóközig olvassa be, ezért itt megadjuk, hogy mindig Enter-ig olvassa
		scanner.useDelimiter("\n");
		StringBuilder menu = new StringBuilder();
		menu.append("Menüpont kiválasztása:\n");
		menu.append("1. Bejelentkezés\n");
		menu.append("2. Regisztráció\n");
		menu.append("3. Útvonal keresése tájegység alapján\n");
		menu.append("4. Útvonal keresése hossz alapján\n");
		menu.append("5. Útvonal keresése nehézségi szint alapján\n");
		menu.append("6. Új útvonal felvitele\n");
		menu.append("7. Saját útvonal lekérése\n");
		menu.append("8. Saját útvonal törlése\n");
		menu.append("9. Admin menü\n");
		menu.append("-1. Kilépés\n");
		Users signedInUser = null;
		while (flag != -1) {

			System.out.println(menu.toString());
			String menupont = scanner.next();
			try {
				flag = Integer.parseInt(menupont);
			} catch (Exception e) {
				flag = 0;
				System.out.println("Kérem a menüpont kiválasztásakor csak számot adjon meg.");
				continue;
			}
			try {
				switch (flag) {
					case 1:
						System.out.println("Felhasználónév:\n");
						String userName = scanner.next();
						System.out.println("Jelszó:\n");
						String pwd = scanner.next();
						signedInUser = (Users)es.searchById(Users.class, userName);
						boolean pwdOK = false;
						if (signedInUser != null) {
							//Összehasonlítom a tárolt és kódolt jelszót a beírttal
							pwdOK = BCrypt.checkpw(pwd, signedInUser.getPassword());
						}
						if (signedInUser == null || !pwdOK) {
							System.out.println("Nincs ilyen felhasználó, vagy hibás jelszó.");
							signedInUser = null;
						} else {
							System.out.println("Sikeres bejelentkezés.\nÜdvözlöm kedves " + signedInUser.getNickname() + "!\n" +
									"Utoljára ekkor jártál itt: " + signedInUser.getLastLogin() + "\n");
							signedInUser.setLastLogin(new Date());
							es.save(signedInUser);
						}
						break;
					case 2:
						System.out.println("Kérem adja meg a regisztrációhoz szükséges adatokat.");
						System.out.println("Felhasználónév:\n");
						String newUserName = scanner.next();
						System.out.println("E-mail cím:\n");
						String email = scanner.next();
						System.out.println("Teljes név:\n");
						String fullName = scanner.next();
						System.out.println("Becenév:\n");
						String nickName = scanner.next();
						System.out.println("Jelszó:\n");
						String newPassword = scanner.next();
						Users newUser = new Users();
						newUser.setUsername(newUserName);
						newUser.setEmail(email);
						newUser.setFullName(fullName);
						newUser.setNickname(nickName);
						newUser.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
						Users existed = (Users)es.searchById(Users.class, newUserName);
						if (existed != null) {
							System.out.println("A megadott felhasználónév már foglalt.");
						} else {
							es.save(newUser);
							List<Roles> roles = (List<Roles>)es.search(Roles.class, null);
							for (Roles r : roles) {
								if (r.getRoleName().equalsIgnoreCase("writer")) {
									UserRole ur = new UserRole();
									ur.setUsers(newUser);
									ur.setRoles(r);
									es.save(ur);
									break;
								}
							}
						}

						break;
					case 3:
						//Tájegységek lekérése
						List<Regions> regions = (List<Regions>)es.search(Regions.class, null);
						if (regions != null) {
							System.out.println("Tájegységek:");
							for (int i = 0; i < regions.size(); i++) {
								Regions region = regions.get(i);
								System.out.println("\t" + (i + 1) + " - " + region.getRegionName());
							}
							System.out.println("Válasszon egy tájegységet\n");
						}
						int regionNum = scanner.nextInt();
						Regions selected = regions.get(regionNum - 1);
						for (TrackingRegions tr : selected.getTrackingRegionses()) {
							for (TrackingParameters tp : tr.getTracking().getTrackingParameterses()) {
								System.out.println("\t" + tp.toString());
							}
						}
						break;
					case 4:
						//Választás hossz szerint
						Map<Integer, String> lengths = new HashMap<>();
						lengths.put(1, "0 km - 5 km");
						lengths.put(2, "5.1 km - 10 km");
						lengths.put(3, "10.1 km - 15 km");
						lengths.put(4, "15.1 km >");
						for (Map.Entry<Integer, String> e : lengths.entrySet()) {
							System.out.println("\t" + e.getKey() + ": " + e.getValue());
						}
						int length = scanner.nextInt();
						StringBuilder sqlBuilder = new StringBuilder();
						sqlBuilder.append(" select tp from TrackingParameters tp where ");
						switch (length) {
							case 1:
								sqlBuilder.append(" length < 5 ");
								break;
							case 2:
								sqlBuilder.append(" length > 5 and length <= 10 ");
								break;
							case 3:
								sqlBuilder.append(" length > 10 and length <= 15 ");
								break;
							case 4:
								sqlBuilder.append(" length > 15 ");
								break;
							default:
								System.out.println("Nem létező paraméter.");
								break;
						}
						List<TrackingParameters> parameters = (List<TrackingParameters>)es.searchWithSQL(sqlBuilder.toString());
						if (parameters != null) {
							for (TrackingParameters tp : parameters) {
								System.out.println("\t" + tp.toString());
							}
						}
						break;
					case 5:
						//Választás nehézségi szint szerint
						Map<Integer, String> levels = new HashMap<>();
						levels.put(1, LevelEnum.getLevelByNumber(1).getHunName());
						levels.put(2, LevelEnum.getLevelByNumber(2).getHunName());
						levels.put(3, LevelEnum.getLevelByNumber(3).getHunName());
						levels.put(4, LevelEnum.getLevelByNumber(4).getHunName());
						levels.put(5, LevelEnum.getLevelByNumber(5).getHunName());
						for (Map.Entry<Integer, String> e : levels.entrySet()) {
							System.out.println("\t" + e.getKey() + ": " + e.getValue());
						}
						int level = scanner.nextInt();
						if (level >= 1 && level <= 5) {
							HashMap<String, Object> filter = new HashMap<>();
							filter.put("level", level);
							List<TrackingParameters> parameters2 = (List<TrackingParameters>)es.search(TrackingParameters.class, filter);
							if (parameters2 != null) {
								for (TrackingParameters tp : parameters2) {
									System.out.println("\t" + tp.toString());
								}
							}
						} else {
							System.out.println("Nem létező paraméter.");
						}
						break;
					case 6:
						if (signedInUser == null) {
							System.out.println("A menüpont használatához be kell jelentkezni.");
						} else {
							TrackingParameters tp = new TrackingParameters();
							Tracking t = new Tracking();
							t.setUsers(signedInUser);
							System.out.println("\tÚtvonal hossza:");
							tp.setLength(scanner.nextDouble());
							System.out.println("\tÚtvonal nehézségi szintje (1-5):");
							tp.setLevel(scanner.nextInt());
							System.out.println("\tÚtvonal leírását tartalmazó fájl:");
							String descriptionFilePath = scanner.next();
							File description = new File(descriptionFilePath);
							if (description.exists()) {
								List<String> lines = Files.readAllLines(description.toPath());
								StringBuilder desc = new StringBuilder();
								for (String s : lines) {
									desc.append(s).append("\n");
								}
								tp.setDescription(desc.toString());
							} else {
								System.out.println("Nem található ilyen fájl.");
							}
							es.save(t);
							tp.setTracking(t);
							es.save(tp);
							t.getTrackingParameterses().add(tp);
							//Tájegységek lekérése
							List<Regions> regionsForTracking = (List<Regions>)es.search(Regions.class, null);
							if (regionsForTracking != null) {
								System.out.println("\tTájegységek:");
								for (int i = 0; i < regionsForTracking.size(); i++) {
									Regions region = regionsForTracking.get(i);
									System.out.println("\t" + (i + 1) + " - " + region.getRegionName());
								}
								System.out.println("\tKérem adja meg, mely tájegység(ek)hez tartozik az útvonal (','-vel elválasztva):");
								String tajegysegek = scanner.next();
								String[] splitted = tajegysegek.split(",");
								for (String s : splitted) {
									int rNum = Integer.parseInt(s);
									Regions r = regionsForTracking.get(rNum - 1);
									TrackingRegions tr = new TrackingRegions();
									tr.setRegions(r);
									tr.setTracking(t);
									es.save(tr);
									t.getTrackingRegionses().add(tr);
								}
							}
							System.out.println("Kérem adja meg a feltölteni kívánt KML fájl elérési útját:");
							String kmlPath = scanner.next();
							File kmlFile = new File(kmlPath);
							if (kmlFile.exists()) {
								KmlFiles newKmlFile = new KmlFiles();
								newKmlFile.setFilename(kmlFile.getName());
								newKmlFile.setFile(Files.readAllBytes(kmlFile.toPath()));
								es.save(newKmlFile);
								//Az új kml file objeektumot hozzáadjuk a tracking objektumhoz és ismét lementjük
								t.setKmlFiles(newKmlFile);
								es.save(t);
								signedInUser.getTrackings().add(t);
							} else {
								System.out.println("A megadott elérési úton nem található fájl.");
							}
							System.out.println("Képek feltöltése? (I/N)");
							String valasz = scanner.next();
							if (valasz.equalsIgnoreCase("i")) {
								System.out.println("Kérem adja meg a feltölteni kívánt képeket tartalmazó mappa helyét:");
								String imgDirPath = scanner.next();
								File imgDir = new File(imgDirPath);
								if (imgDir.exists() && imgDir.isDirectory()) {
									for (File f : imgDir.listFiles()) {
										PicFiles pf = new PicFiles();
										pf.setTracking(t);
										pf.setFilename(f.getName());
										pf.setFile(Files.readAllBytes(f.toPath()));
										es.save(pf);
										t.getPicFileses().add(pf);
									}
								} else {
									System.out.println("A megadott elérési út nem létezik, vagy nem mappa.");
								}
							}
						}
						break;
					case 7:
						if (signedInUser == null) {
							System.out.println("A menüpont használatához be kell jelentkezni.");
						} else {
							Set<Tracking> myTrackings = signedInUser.getTrackings();
							for (Tracking t : myTrackings) {
								for (TrackingParameters tp : t.getTrackingParameterses()) {
									System.out.println("\t" + tp.toString());
								}
								System.out.println("\tKML file neve: " + t.getKmlFiles().getFilename());
								System.out.println("\tKépek: ");
								for (PicFiles pf : t.getPicFileses()) {
									System.out.println("\t\t" + pf.getFilename());
								}
							}
						}
						break;
					case 8:
						if (signedInUser == null) {
							System.out.println("A menüpont használatához be kell jelentkezni.");
						} else {
							Set<Tracking> myTrackings = signedInUser.getTrackings();
							System.out.println("Válasszon egy azonosítót:");
							for (Tracking t : myTrackings) {
								System.out.println("\tAzonosító: " + t.getTrackingId());
								for (TrackingParameters tp : t.getTrackingParameterses()) {
									System.out.println("\t" + tp.toString());
								}
								System.out.println("\tKML file neve: " + t.getKmlFiles().getFilename());
								System.out.println("\tKépek: ");
								for (PicFiles pf : t.getPicFileses()) {
									System.out.println("\t\t" + pf.getFilename());
								}
							}
							int torolId = scanner.nextInt();
							Tracking torol = (Tracking)es.searchById(Tracking.class, torolId);
							for (TrackingParameters tp : torol.getTrackingParameterses()) {
								es.delete(tp);
							}
							for (TrackingRegions tr : torol.getTrackingRegionses()) {
								es.delete(tr);
							}
							for (PicFiles pf : torol.getPicFileses()) {
								es.delete(pf);
							}
							KmlFiles torolni = torol.getKmlFiles();
							es.delete(torol);
							es.delete(torolni);
							signedInUser = (Users)es.searchById(Users.class, signedInUser.getUsername());
						}
						break;
					case 9:
						if (signedInUser == null) {
							System.out.println("A menüpont használatához be kell jelentkezni.");
						} else {
							boolean hasAdminRole = false;
							for (UserRole ur : signedInUser.getUserRoles()) {
								if (ur.getRoles().getRoleName().equalsIgnoreCase("admin")) {
									hasAdminRole = true;
									break;
								}
							}
							if (hasAdminRole) {
								StringBuilder adminMenu = new StringBuilder();
								adminMenu.append("\t1: Felhasználó törlés\n");
								adminMenu.append("\t2: Útonal törlés\n");
								adminMenu.append("\t3: Felhasználók statisztika\n");
								adminMenu.append("\t4: Útvonalak statisztika\n");
								adminMenu.append("\t-1: Kilépés a főmenübe");
								int adminMenuFlag = 0;
								while (adminMenuFlag != -1) {
									System.out.println(adminMenu.toString());
									adminMenuFlag = scanner.nextInt();
									switch (adminMenuFlag) {
										case 1:
											System.out.println("Adja meg a törölni kívánt felhasználó nevét:\n");
											String torol = scanner.next();
											Users user = (Users)es.searchById(Users.class, torol);
											if (user == null) {
												System.out.println("Nincs ilyen felhasználó.");
											} else {
												//user_role-ok törlése
												for (UserRole ur : user.getUserRoles()) {
													es.delete(ur);
												}
												//user-hez tartozó trackingek törlése
												for (Tracking t : user.getTrackings()) {
													//képek törlése
													for (PicFiles pf : t.getPicFileses()) {
														es.delete(pf);
													}
													for (TrackingParameters tp : t.getTrackingParameterses()) {
														es.delete(tp);
													}
													for (TrackingRegions tr : t.getTrackingRegionses()) {
														es.delete(tr);
													}
													KmlFiles kml = t.getKmlFiles();
													es.delete(t);
													es.delete(kml);
												}
												es.delete(user);
											}
											break;
										case 2:
											System.out.println("Adja meg a törölni kívánt útvonal azonosítóját:\n");
											int trackingId = scanner.nextInt();
											Tracking tracking = (Tracking)es.searchById(Tracking.class, trackingId);
											if (tracking == null) {
												System.out.println("Nincs ilyen azonosítójú útvonal.");
											} else {
												//képek törlése
												for (PicFiles pf : tracking.getPicFileses()) {
													es.delete(pf);
												}
												for (TrackingParameters tp : tracking.getTrackingParameterses()) {
													es.delete(tp);
												}
												for (TrackingRegions tr : tracking.getTrackingRegionses()) {
													es.delete(tr);
												}
												KmlFiles kml = tracking.getKmlFiles();
												es.delete(tracking);
												es.delete(kml);
											}
											break;
										case 3:
											es.createUserStat();
											break;
										case 4:
											es.createTrackingStat();
											break;
										default:
											System.out.println("Nincs ilyen menüpont.");
											break;
									}

								}
							} else {
								System.out.println("A menüpontot csak admin jogú felhasználó használhatja.");
							}
						}
						break;
					case -1:
						es.closeEntityManager();
						return;
					default:
						System.out.println("Nincs ilyen menüpont.");
						break;
				}

			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}
}
