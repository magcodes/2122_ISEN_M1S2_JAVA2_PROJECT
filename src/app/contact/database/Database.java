package app.contact.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database{
	public static void initDb(){
		try(Connection connection = Database.getConnection()){
			try(Statement statement = connection.createStatement()){
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS person (\r\n"
						+ "    idperson INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \r\n"
						+ "    lastname VARCHAR(45) NOT NULL,  \r\n"
						+ "    firstname VARCHAR(45) NOT NULL,\r\n"
						+ "    nickname VARCHAR(45) NOT NULL,\r\n"
						+ "    phone_number VARCHAR(15) NULL,\r\n"
						+ "    address VARCHAR(200) NULL,\r\n"
						+ "    email_address VARCHAR(150) NULL,\r\n"
						+ "    birth_date DATE NULL);");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		try {
			return DriverManager.getConnection("jdbc:sqlite:sqlite.db");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


}
