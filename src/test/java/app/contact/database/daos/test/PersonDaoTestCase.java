package app.contact.database.daos.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import app.contact.database.Database;
import app.contact.entities.Person;

public class PersonDaoTestCase {
	
//	private PersonDao personDao = new PersonDao();
	
	@Before
	public void initTest(){
		Database.initDb();
	}
	
	@Test
	public void createContact() {
		Person person = new Person("test", "test", "test", "0751234567", "test@test.com", "test adress", LocalDate.now());
//		personDao.addPerson(person);
//		List<Person> persons = personDao.listContacts();
//		assertT
	}

}
