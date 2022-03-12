package app.contact.database.daos.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import app.contact.database.Database;
import app.contact.database.daos.PersonDao;
import app.contact.entities.Person;

public class PersonDaoTestCase {
	
	private PersonDao personDao = new PersonDao();
	
	@Before
	public void initTest(){
		Database.initDb();
		try(Connection connection = Database.getConnection()){
			try(Statement stmt = connection.createStatement()){
				stmt.executeUpdate("DELETE FROM person");
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
	
	@Test
	public void shouldListContacts(){
		 //WHEN
		 List<Person> persons = personDao.listPersons();
		 assertThat(persons).hasSize(2);
		 assertThat(persons).extracting("id", "lastName", "firstName", "nickName",  "phoneNumber", "address", "emailAddress",
					 "birthDate").containsOnly(tuple(1, "Curie", "Marie", "MC", "+33751122334", "vie apres la mort", "N/A", LocalDate.of(1867,11,7)),
							 tuple(2, "Dupont", "Jean", "JDu", "+33757894561", "partout", "jeandupont@gmail.com", LocalDate.of(1789,7,14))
							 );
	 }
	
	@Test
	public void shouldAddPerson() {
		//WHEN
		Person person = new Person("Doe", "John", "JD", "0751234567", "0 rue quelquepart, monde", "johndoe@test.com", LocalDate.of(2000,1,1));
		personDao.addPerson(person);
		//THEN
		try(Connection connection = Database.getConnection()){
			try(Statement statement = connection.createStatement()){
				try(ResultSet result = statement.executeQuery("SELECT * FROM person WHERE lastname='Doe'")){
					assertThat(result.next()).isTrue();
					assertThat(result.getInt("idperson")).isNotNull();
					assertThat(result.getString("lastname")).isEqualTo("Doe");
					assertThat(result.getString("firstname")).isEqualTo("John");
					assertThat(result.getString("nickname")).isEqualTo("JD");
					assertThat(result.getString("phone_number")).isEqualTo("0751234567");
					assertThat(result.getString("address")).isEqualTo("0 rue quelquepart, monde");
					assertThat(result.getString("email_address")).isEqualTo("johndoe@test.com");
					assertThat(result.getDate("birth_date").toLocalDate()).isEqualTo(LocalDate.of(2000,1,1));
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
	public void updateAddPerson() {
		//WHEN
		personDao.updatePerson(new Person(1, "Curie", "Pierre", "PC", "+33756332211", "en France", "pierrecurie@pierrecurie.com", LocalDate.of(1922,7,2)));
		//THEN
		try(Connection connection = Database.getConnection()){
			try(Statement statement = connection.createStatement()){
				try(ResultSet result = statement.executeQuery("SELECT * FROM person WHERE lastname='Curie'")){
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
	public void deleteAddPerson() {
		//WHEN
		personDao.deletePerson(new Person(2, "Dupont", "Jean", "JDu", "+33757894561", "partout", "jeandupont@gmail.com", LocalDate.of(1789,7,14)));
		//THEN
		try(Connection connection = Database.getConnection()){
			try(Statement statement = connection.createStatement()){
				try(ResultSet result = statement.executeQuery("SELECT * FROM person WHERE lastname='Dupont'")){
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

}
