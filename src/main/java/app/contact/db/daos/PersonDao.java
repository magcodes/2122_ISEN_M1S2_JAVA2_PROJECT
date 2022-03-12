package app.contact.db.daos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import app.contact.database.Database;

public class PersonDao {
	
public PersonDao(int id, String firstname, String lastname, String nickname, String phone_number, String email_address,
		String address, LocalDate birth_date) {
}

	public List<PersonDao> listPersons() {
	    List<PersonDao> listOfPersons = new ArrayList<>();
	    try (Connection connection = Database.getConnection()) {
	        try (Statement statement = connection.createStatement()) {
	            try (ResultSet results = statement.executeQuery("select * from persons")) {
	                while (results.next()) {
	                    PersonDao person = new PersonDao(results.getInt("idperson"),
	                                            results.getString("lastname"),
	                                            results.getString("firstname"),
	                                            results.getString("nickname"),
	                                            results.getString("phone_number"),
	                                            results.getString("address"),
	                                            results.getString("email_address"),
	                                            results.getDate("birth_date").toLocalDate());
	                    listOfPersons.add(person);
	                }
	            }
	        }
	    } catch (SQLException e) { 
	        // Manage Exception
	        e.printStackTrace();
	    }
	    return listOfPersons;
	}
	
	public PersonDao add(int id, String firstname, String lastname, String nickname, String phone_number, 
			String address, String email_address,  LocalDate birth_date) {
	    try (Connection connection = Database.getConnection()) {
	        String sqlQuery = "insert into person(firstname, lastname, nickname, phone_number, address,"
	        		+ "email_address, birth_date) "+"VALUES(?,?,?)";
	        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
	            statement.setString(1, firstname);
	            statement.setString(2, lastname);
	            statement.setString(3, nickname);
	            statement.setString(4, phone_number);
	            statement.setString(5, address);
	            statement.setString(6, email_address);
	            statement.setDate(7, Date.valueOf(birth_date));
	            statement.executeUpdate();
	            ResultSet ids = statement.getGeneratedKeys();
	            if (ids.next()) {
	                return new PersonDao(ids.getInt(1), firstname, lastname, nickname, phone_number, address,
	                		email_address, birth_date);
	            }
	        }
	    }catch (SQLException e) {
	        // Manage Exception
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public void delete(Integer id) {
	    try (Connection connection = Database.getConnection()) {
	        try (PreparedStatement statement = connection.prepareStatement("delete from person where person_id=?")) {
	            statement.setInt(1, id);
	            statement.executeUpdate();
	        }
	    }catch (SQLException e) {
	        // Manage Exception
	        e.printStackTrace();
	    }
	}
}
