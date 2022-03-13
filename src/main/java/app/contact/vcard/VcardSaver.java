package app.contact.vcard;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import app.contact.entities.Person;
import app.contact.views.App;

public class VcardSaver {
	
	public void save(Person person) {
		try {
			String messageToWrite = """
					BEGIN:VCARD
					VERSION:4.0
					N:%s
					FN:%s
					NICKNAME:%s
					TEL;TYPE=cell:%s
					ADR;TYPE=home:;;%s;;;;
					EMAIL:%s
					BDAY:%s
					END:VCARD""".formatted(person.getLastName(), person.getFirstName(),person.getNickName(),
							person.getPhoneNumber(), person.getAddress(), person.getEmailAddress(), person.getBirthDate().toString().replace("-", ""));
			Files.writeString(Paths.get(person.getId()+".vcf"), messageToWrite, StandardCharsets.UTF_8);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}


