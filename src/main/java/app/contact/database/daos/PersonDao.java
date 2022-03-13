package app.contact.database.daos;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import app.contact.vcard.VcardSaver;

public class PersonDao {
	private VcardSaver vcardSaver = new VcardSaver();
	
	public List<Person> listPersons() {
	    List<Person> listOfPersons = new ArrayList<Person>();
	    try (Connection connection = Database.getConnection()) {
	        try (Statement statement = connection.createStatement()) {
	            try (ResultSet results = statement.executeQuery("SELECT * FROM person")) {
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
		                return new Person(ids.getInt(1), person.getLastName(),person.getFirstName(), person.getNickName(),
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
	
	public void updatePerson(Person person) {
		try (Connection connection = Database.getConnection()) {
	        try (PreparedStatement statement = connection.prepareStatement("UPDATE person SET firstname=?, lastname=?, nickname=?, phone_number=?, "
	        		+ "address=?, email_address=?, birth_date=? WHERE idperson=?")) {
	            statement.setString(1, person.getFirstName());
	            statement.setString(2, person.getLastName());
	            statement.setString(3, person.getNickName());
	            statement.setString(4, person.getPhoneNumber());
	            statement.setString(5, person.getAddress());
	            statement.setString(6, person.getEmailAddress());
	            statement.setDate(7, Date.valueOf(person.getBirthDate()));
	            statement.setInt(8, person.getId());
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
	
	public void deletePerson(Person person) {
	    try (Connection connection = Database.getConnection()) {
	        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM person WHERE idperson=?")) {
	            statement.setInt(1, person.getId());
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
	
	public void exportPersons() {
		DirectoryStream<Path> listOfPaths;
		try {
			listOfPaths = Files.newDirectoryStream(Paths.get(new File(".").getAbsolutePath()));
			for(Path file: listOfPaths) {
				String filename = file.getFileName().toString();
				int indexOfPeriod = filename.lastIndexOf(".");
				if(filename.substring(++indexOfPeriod).equals("vcf")) {
					Files.delete(file);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new PersonDao().listPersons().stream().forEach(vcardSaver::save);
	}
}
