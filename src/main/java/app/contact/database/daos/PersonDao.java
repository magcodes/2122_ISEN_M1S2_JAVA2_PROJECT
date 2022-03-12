package app.contact.database.daos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.contact.database.Database;
import app.contact.entities.Person;

public class PersonDao {

	public List<Person> listPersons() {
	    List<Person> listOfPersons = new ArrayList<Person>();
	    try (Connection connection = Database.getConnection()) {
	        try (Statement statement = connection.createStatement()) {
	            try (ResultSet results = statement.executeQuery("SELECT * FROM persons")) {
	                while (results.next()) {
	                    listOfPersons.add(new Person(results.getInt("idperson"),
	                                            results.getString("lastname"),
	                                            results.getString("firstname"),
	                                            results.getString("nickname"),
	                                            results.getString("phone_number"),
	                                            results.getString("address"),
	                                            results.getString("email_address"),
	                                            results.getDate("birth_date").toLocalDate()));
	
	                }
	            }
	        }
	    } catch (SQLException e) { 
	        // Manage SQL Exception
	        e.printStackTrace();
	    }
	    catch (Exception e) {
	    	//Manage All Exceptions
	    	e.printStackTrace();
	    }
	    return listOfPersons;
	}
	
	public Person addPerson(Person person) {
	    try (Connection connection = Database.getConnection()) {
	        String sqlQuery = "INSERT INTO person(firstname, lastname, nickname, phone_number, address,"
	        		+ "email_address, birth_date) "+"VALUES(?,?,?,?,?,?,?)";
	        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
	            statement.setString(1, person.getFirstName());
	            statement.setString(2, person.getLastName());
	            statement.setString(3, person.getNickName());
	            statement.setString(4, person.getPhoneNumber());
	            statement.setString(5, person.getAddress());
	            statement.setString(6, person.getEmailAddress());
	            statement.setDate(7, Date.valueOf(person.getBirthDate()));
	            statement.executeUpdate();
	            try ( ResultSet ids = statement.getGeneratedKeys()) {
	            	if (ids.next()) {
		                return new Person(ids.getInt(1), person.getFirstName(),person.getLastName(), person.getNickName(),
		                		person.getPhoneNumber(), person.getAddress(), person.getEmailAddress(), person.getBirthDate());
		            }
	            }
	        }
	    }catch (SQLException e) {
	        // Manage Exception
	        e.printStackTrace();
	    }
	    catch (Exception e) {
	    	//Manage All Exceptions
	    	e.printStackTrace();
	    }
	    return null;
	}
	
	public void deletePerson(Integer id) {
	    try (Connection connection = Database.getConnection()) {
	        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM person WHERE idperson=?")) {
	            statement.setInt(1, id);
	            statement.executeUpdate();
	        }
	    }catch (SQLException e) {
	        // Manage Exception
	        e.printStackTrace();
	    }
	    catch (Exception e) {
	    	//Manage All Exceptions
	    	e.printStackTrace();
	    }
	}
}
