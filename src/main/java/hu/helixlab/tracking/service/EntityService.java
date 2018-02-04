package hu.helixlab.tracking.service;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import hu.helixlab.tracking.entity.BaseEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Transaction;

import javax.persistence.*;

public class EntityService {
	// Create an EntityManagerFactory when you start the application.
	private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
			.createEntityManagerFactory("tracking_jpa_pu");

	private static final EntityManager ENTITY_MANAGER = ENTITY_MANAGER_FACTORY.createEntityManager();

	public boolean save (BaseEntity entity){
		EntityTransaction transaction = null;
		boolean success = true;
		try {
			// Get a transaction
			transaction = ENTITY_MANAGER.getTransaction();
			// Begin the transaction
			transaction.begin();

			ENTITY_MANAGER.persist(entity);

			// Commit the transaction
			transaction.commit();

		} catch (Exception ex) {
			// If there are any exceptions, roll back the changes
			if (transaction != null) {
				transaction.rollback();
			}
			// Print the Exception
			ex.printStackTrace();
			success = false;
		} finally {
			// Close the EntityManager
			// manager.close();
		}
		return success;
	}
	
	public BaseEntity searchById (Class clazz, Object id){
		EntityTransaction transaction = null;
		BaseEntity entity = null;
		try {
			// Get a transaction
			transaction = ENTITY_MANAGER.getTransaction();
			// Begin the transaction
			transaction.begin();

			entity = (BaseEntity)ENTITY_MANAGER.find(clazz, id);

			// Commit the transaction
			transaction.commit();

		} catch (Exception ex) {
			// If there are any exceptions, roll back the changes
			if (transaction != null) {
				transaction.rollback();
			}
			// Print the Exception
			ex.printStackTrace();
		} finally {
			// Close the EntityManager
			// manager.close();
		}
		return entity;
	}

	public List<? extends BaseEntity> search (Class clazz, HashMap<String, Object> filter){
		EntityTransaction transaction = null;
		List<? extends BaseEntity> entities = null;
		try {
			// Get a transaction
			transaction = ENTITY_MANAGER.getTransaction();
			// Begin the transaction
			transaction.begin();

			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append(" select e from ").append(clazz.getName()).append(" e ");
			boolean hasParameter = false;
			if (filter != null && filter.size() > 0) {
				hasParameter = true;
				sqlBuilder.append(" where 1=1 ");
				for (Map.Entry<String, Object> entry : filter.entrySet()) {
					sqlBuilder.append(" and e.").append(entry.getKey()).append(" = :").append(entry.getKey());
				}
			}
			sqlBuilder.append(" order by id asc ");
			Query query = ENTITY_MANAGER.createQuery(sqlBuilder.toString());
			if (hasParameter) {
				for (Map.Entry<String, Object> entry : filter.entrySet()) {
					query.setParameter(entry.getKey(), entry.getValue());
				}
			}
			entities = query.getResultList();
			// Commit the transaction
			transaction.commit();

		} catch (Exception ex) {
			// If there are any exceptions, roll back the changes
			if (transaction != null) {
				transaction.rollback();
			}
			// Print the Exception
			ex.printStackTrace();
		} finally {
			// Close the EntityManager
			// manager.close();
		}
		return entities;
	}

	public List<? extends BaseEntity> searchWithSQL (String sql) {
		EntityTransaction transaction = null;
		List<? extends BaseEntity> entities = null;
		try {
			// Get a transaction
			transaction = ENTITY_MANAGER.getTransaction();
			// Begin the transaction
			transaction.begin();

			Query query = ENTITY_MANAGER.createQuery(sql);

			entities = query.getResultList();
			// Commit the transaction
			transaction.commit();

		} catch (Exception ex) {
			// If there are any exceptions, roll back the changes
			if (transaction != null) {
				transaction.rollback();
			}
			// Print the Exception
			ex.printStackTrace();
		} finally {
			// Close the EntityManager
			// manager.close();
		}
		return entities;
	}

	
	public boolean delete (BaseEntity entity){
		EntityTransaction transaction = null;
		boolean success = true;
		try {
			// Get a transaction
			transaction = ENTITY_MANAGER.getTransaction();
			// Begin the transaction
			transaction.begin();

			ENTITY_MANAGER.remove(entity);

			// Commit the transaction
			transaction.commit();

		} catch (Exception ex) {
			// If there are any exceptions, roll back the changes
			if (transaction != null) {
				transaction.rollback();
			}
			// Print the Exception
			ex.printStackTrace();
			success = false;
		} finally {
			// Close the EntityManager
			// manager.close();
		}
		return success;
	}

	public void createUserStat () {
		String userStatSql = " select u.username, u.email, u.full_name, u.nickname, count(*) from users u " +
				" inner join tracking t on u.user_id = t.user_id " +
				" group by u.username, u.email, u.full_name, u.nickname  ";
		EntityTransaction transaction = null;
		try {
			// Get a transaction
			transaction = ENTITY_MANAGER.getTransaction();
			// Begin the transaction
			transaction.begin();

			Query query = ENTITY_MANAGER.createNativeQuery(userStatSql);
			//https://www.thoughts-on-java.org/result-set-mapping-basics/
			List<Object[]> result = query.getResultList();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("Felhasználók");
			int rowNum = 0;
			XSSFRow r = sheet.createRow(rowNum);
			XSSFCell usernameCell = r.createCell(0);
			usernameCell.setCellValue("Felhasználónév");
			XSSFCell emailCell = r.createCell(1);
			emailCell.setCellValue("E-mail cím");
			XSSFCell fullnameCell = r.createCell(2);
			fullnameCell.setCellValue("Teljes név");
			XSSFCell nicknameCell = r.createCell(3);
			nicknameCell.setCellValue("Becenév");
			XSSFCell trackCountCell = r.createCell(4);
			trackCountCell.setCellValue("Felhasználó által létrehozott útvonalak");
			for (Object[] row : result) {
				XSSFRow dataRow = sheet.createRow(++rowNum);
				int cellNum = 0;
				for (Object o : row) {
					XSSFCell dataCell = dataRow.createCell(cellNum++);
					if (o != null) {
						dataCell.setCellValue(o.toString());
					}
				}
			}
			Scanner scanner = new Scanner(System.in);
			//pl a teljes név esetén csak az első szóközig olvassa be, ezért itt megadjuk, hogy mindig a sor
			//végéig olvassa
			scanner.useDelimiter("\n");
			System.out.println("Adja meg a munkafüzet mentési helyét:");
			String filePath = scanner.next();
			FileOutputStream fileOut = new FileOutputStream(filePath);
			wb.write(fileOut);
			fileOut.close();
			// Commit the transaction
			transaction.commit();
		} catch (Exception e) {
			// If there are any exceptions, roll back the changes
			if (transaction != null) {
				transaction.rollback();
			}
			// Print the Exception
			e.printStackTrace();
		}
	}

	public void createTrackingStat () {
		String userStatSql = " select tp.length, tp.level, tp.description, count(*) from tracking_parameters tp " +
				" inner join pic_files p on p.tracking_id = tp.tracking_id group by tp.length, tp.level, tp.description  ";
		EntityTransaction transaction = null;
		try {
			// Get a transaction
			transaction = ENTITY_MANAGER.getTransaction();
			// Begin the transaction
			transaction.begin();

			Query query = ENTITY_MANAGER.createNativeQuery(userStatSql);
			//https://www.thoughts-on-java.org/result-set-mapping-basics/
			List<Object[]> result = query.getResultList();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("Útvonalak");
			int rowNum = 0;
			XSSFRow r = sheet.createRow(rowNum);
			XSSFCell usernameCell = r.createCell(0);
			usernameCell.setCellValue("Hossz");
			XSSFCell emailCell = r.createCell(1);
			emailCell.setCellValue("Szint");
			XSSFCell fullnameCell = r.createCell(2);
			fullnameCell.setCellValue("Leírás");
			XSSFCell nicknameCell = r.createCell(3);
			nicknameCell.setCellValue("Fényképek száma");

			for (Object[] row : result) {
				XSSFRow dataRow = sheet.createRow(++rowNum);
				int cellNum = 0;
				for (Object o : row) {
					XSSFCell dataCell = dataRow.createCell(cellNum++);
					if (o != null) {
						dataCell.setCellValue(o.toString());
					}
				}
			}
			Scanner scanner = new Scanner(System.in);
			//pl a teljes név esetén csak az első szóközig olvassa be, ezért itt megadjuk, hogy mindig a sor
			//végéig olvassa
			scanner.useDelimiter("\n");
			System.out.println("Adja meg a munkafüzet mentési helyét:");
			String filePath = scanner.next();
			FileOutputStream fileOut = new FileOutputStream(filePath);
			wb.write(fileOut);
			fileOut.close();
			// Commit the transaction
			transaction.commit();
		} catch (Exception e) {
			// If there are any exceptions, roll back the changes
			if (transaction != null) {
				transaction.rollback();
			}
			// Print the Exception
			e.printStackTrace();
		}
	}
	public void closeEntityManager () {
		try {
			ENTITY_MANAGER.close();
			ENTITY_MANAGER_FACTORY.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
