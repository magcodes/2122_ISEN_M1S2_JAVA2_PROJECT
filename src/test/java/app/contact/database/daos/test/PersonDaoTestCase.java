package app.contact.database.daos.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import app.contact.database.Database;
import app.contact.database.daos.PersonDao;
import app.contact.entities.Person;

public class PersonDaoTestCase {
	
	private PersonDao personDao = new PersonDao();
	@Before
	public void initTest(){
		Database.initDb(); //making sure table is created
		try(Connection connection = Database.getConnection()){
			try(Statement stmt = connection.createStatement()){
				stmt.executeUpdate("DELETE FROM person"); //clearing table 
				//insert dummy data for test
				stmt.executeUpdate("INSERT INTO person(idperson,lastname,firstname,nickname,phone_number,address,email_address,birth_date) "
						+ "VALUES (1, 'Curie', 'Marie', 'MC', '+33751122334', 'vie apres la mort', 'N/A', '1867-11-7 12:00:00.000')");
				stmt.executeUpdate("INSERT INTO person(idperson,lastname,firstname,nickname,phone_number,address,email_address,birth_date) "
						+ "VALUES (2, 'Dupont', 'Jean', 'JDu', '+33757894561', 'partout', 'jeandupont@gmail.com', '1789-07-14 12:00:00.000')");			
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void cleanupTest() {
		//delete dummy data from database after test
		try(Connection connection = Database.getConnection()){
			try(Statement stmt = connection.createStatement()){
				stmt.executeUpdate("DELETE FROM person");			
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void shouldListContacts(){
		 //WHEN
		 List<Person> persons = personDao.listPersons(); //get list of persons using DAO
		 //THEN
		 assertThat(persons).hasSize(2);  //make sure the size of the list is the same as the number of dummy data
		 assertThat(persons).extracting("id", "lastName", "firstName", "nickName",  "phoneNumber", "address", "emailAddress", //make sure the results is the same as the dummy data
					 "birthDate").containsOnly(tuple(1, "Curie", "Marie", "MC", "+33751122334", "vie apres la mort", "N/A", LocalDate.of(1867,11,7)),
							 tuple(2, "Dupont", "Jean", "JDu", "+33757894561", "partout", "jeandupont@gmail.com", LocalDate.of(1789,7,14))
							 );
	 }
	
	@Test
	public void shouldAddPerson() {
		//WHEN
		Person person = new Person("Doe", "John", "JD", "0751234567", "0 rue quelquepart, monde", "johndoe@test.com", LocalDate.of(2000,1,1)); //create a person object to be added
		personDao.addPerson(person); //add person to database using DAO
		//THEN
		try(Connection connection = Database.getConnection()){
			try(Statement statement = connection.createStatement()){
				try(ResultSet result = statement.executeQuery("SELECT * FROM person WHERE lastname='Doe'")){
					assertThat(result.next()).isTrue(); //make sure there is a result of the added person
					assertThat(result.getInt("idperson")).isNotNull(); //make sure the id is set
					assertThat(result.getString("lastname")).isEqualTo("Doe"); //make sure the last name is the same as person object
					assertThat(result.getString("firstname")).isEqualTo("John"); //make sure first name is the same as person object
					assertThat(result.getString("nickname")).isEqualTo("JD"); //make sure nickname is the same as person object
					assertThat(result.getString("phone_number")).isEqualTo("0751234567"); //make sure phone number is the same as the person object
					assertThat(result.getString("address")).isEqualTo("0 rue quelquepart, monde"); //make sure address is the same as the person object
					assertThat(result.getString("email_address")).isEqualTo("johndoe@test.com"); //make sure the email address is the same as person object
					assertThat(result.getDate("birth_date").toLocalDate()).isEqualTo(LocalDate.of(2000,1,1)); //make sure birth date is the same as person object
					assertThat(result.next()).isFalse(); //make sure there is no other result
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void shouldUpdatePerson() {
		//WHEN
		personDao.updatePerson(new Person(1, "Curie", "Pierre", "PC", "+33756332211", "en France", "pierrecurie@pierrecurie.com", LocalDate.of(1922,7,2))); // update one dummy entry using the DAO
		//THEN
		try(Connection connection = Database.getConnection()){
			try(Statement statement = connection.createStatement()){
				try(ResultSet result = statement.executeQuery("SELECT * FROM person WHERE lastname='Curie'")){
					//make sure the changes have been effected
					assertThat(result.next()).isTrue();
					assertThat(result.getInt("idperson")).isNotNull();
					assertThat(result.getString("lastname")).isEqualTo("Curie");
					assertThat(result.getString("firstname")).isEqualTo("Pierre");
					assertThat(result.getString("nickname")).isEqualTo("PC");
					assertThat(result.getString("phone_number")).isEqualTo("+33756332211");
					assertThat(result.getString("address")).isEqualTo("en France");
					assertThat(result.getString("email_address")).isEqualTo("pierrecurie@pierrecurie.com");
					assertThat(result.getDate("birth_date").toLocalDate()).isEqualTo(LocalDate.of(1922,7,2));
					assertThat(result.next()).isFalse();
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void shouldDeletePerson() {
		//WHEN
		personDao.deletePerson(new Person(2, "Dupont", "Jean", "JDu", "+33757894561", "partout", "jeandupont@gmail.com", LocalDate.of(1789,7,14))); //delete a dummy entry using the DAO
		//THEN
		try(Connection connection = Database.getConnection()){
			try(Statement statement = connection.createStatement()){
				try(ResultSet result = statement.executeQuery("SELECT * FROM person WHERE lastname='Dupont'")){
					//make sure the entry does not exist any more
					assertThat(result.next()).isFalse();
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void shouldExportPersons() {
		//WHEN 
		personDao.exportPersons(); //export persons to vCard using DAO
		//THEN
		//count the number of vCard files in the repository
		int count = 0;
		DirectoryStream<Path> listOfPaths;
		try {
			listOfPaths = Files.newDirectoryStream(Paths.get(new File(".").getAbsolutePath()));
			for(Path file: listOfPaths) {
				String filename = file.getFileName().toString();
				int indexOfPeriod = filename.lastIndexOf(".");
				if(filename.substring(++indexOfPeriod).equals("vcf")) {
					count++;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		//make sure the count is the same as the number of dummy data
		assertThat(count).isEqualTo(2);
	}
	
	

}
