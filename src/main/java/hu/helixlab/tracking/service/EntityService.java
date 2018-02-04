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
			if (filter != null && filter.size() > 0) {
				sqlBuilder.append(" where 1=1 ");
				for (Map.Entry<String, Object> entry : filter.entrySet()) {
					sqlBuilder.append(" and e.").append(entry.getKey()).append(" = :").append(entry.getKey());
				}
			}
			sqlBuilder.append(" order by id asc ");
			Query query = ENTITY_MANAGER.createQuery(sqlBuilder.toString(), clazz);
			if (filter != null && filter.size() > 0) {
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
		String userStatSql = " select u.username, u.email, u.full_name, u.nickname, counter.count from users u left join " +
				" (select username, count(*) from tracking group by username )counter on u.username = counter.username  ";
		EntityTransaction transaction = null;
		try {
			// Get a transaction
			transaction = ENTITY_MANAGER.getTransaction();
			// Begin the transaction
			transaction.begin();

			Query query = ENTITY_MANAGER.createNativeQuery(userStatSql);
			//https://www.thoughts-on-java.org/result-set-mapping-basics/
			List<Object[]> result = query.getResultList();
			Workbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet("Felhasználók");
			int rowNum = 0;
			Row r = sheet.createRow(rowNum);
			Cell usernameCell = r.createCell(0);
			usernameCell.setCellValue("Felhasználónév");
			Cell emailCell = r.createCell(1);
			emailCell.setCellValue("E-mail cím");
			Cell fullnameCell = r.createCell(2);
			fullnameCell.setCellValue("Teljes név");
			Cell nicknameCell = r.createCell(3);
			nicknameCell.setCellValue("Becenév");
			Cell trackCountCell = r.createCell(4);
			trackCountCell.setCellValue("Felhasználó által létrehozott útvonalak");
			int allColNum = 0;
			for (Object[] row : result) {
				Row dataRow = sheet.createRow(++rowNum);
				int cellNum = 0;
				for (Object o : row) {
					Cell dataCell = dataRow.createCell(cellNum++);
					if (o != null) {
						dataCell.setCellValue(o.toString());
					}
				}
				if (cellNum - 1 > allColNum) {
					allColNum = cellNum - 1;
				}
			}
			//https://stackoverflow.com/questions/4611018/apache-poi-excel-how-to-configure-columns-to-be-expanded#answer-4612239
			for (int i = 0; i <= allColNum; i++) {
				sheet.autoSizeColumn(i);
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
		String userStatSql = " select tp.length, tp.level, tp.description, counter.count from tracking_parameters tp left join\n" +
				"(select tracking_id, count(*) from pic_files group by tracking_id)counter " +
				"on counter.tracking_id = tp.tracking_id order by tp.tracking_id asc  ";
		EntityTransaction transaction = null;
		try {
			// Get a transaction
			transaction = ENTITY_MANAGER.getTransaction();
			// Begin the transaction
			transaction.begin();

			Query query = ENTITY_MANAGER.createNativeQuery(userStatSql);
			//https://www.thoughts-on-java.org/result-set-mapping-basics/
			List<Object[]> result = query.getResultList();
			Workbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet("Útvonalak");
			int rowNum = 0;
			Row r = sheet.createRow(rowNum);
			Cell usernameCell = r.createCell(0);
			usernameCell.setCellValue("Hossz");
			Cell emailCell = r.createCell(1);
			emailCell.setCellValue("Szint");
			Cell fullnameCell = r.createCell(2);
			fullnameCell.setCellValue("Leírás");
			Cell nicknameCell = r.createCell(3);
			nicknameCell.setCellValue("Fényképek száma");
			int allColNum = 0;
			for (Object[] row : result) {
				Row dataRow = sheet.createRow(++rowNum);
				int cellNum = 0;
				for (Object o : row) {
					Cell dataCell = dataRow.createCell(cellNum++);
					if (o != null) {
						dataCell.setCellValue(o.toString());
					}
				}
				if (cellNum - 1 > allColNum) {
					allColNum = cellNum - 1;
				}
			}
			//https://stackoverflow.com/questions/4611018/apache-poi-excel-how-to-configure-columns-to-be-expanded#answer-4612239
			for (int i = 0; i <= allColNum; i++) {
				sheet.autoSizeColumn(i);
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
