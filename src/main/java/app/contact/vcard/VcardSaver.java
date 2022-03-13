package app.contact.vcard;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import app.contact.entities.Person;

public class VcardSaver {
	private Path path;
	
	public VcardSaver(){
		this.path= Paths.get(new File(".").getAbsolutePath()); //getting the path to the root of the project to store the vCard files
	}
	
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
					END:VCARD""".formatted(person.getNames().replace(" ", ";"), person.getFullName(),person.getNickName(),
							person.getPhoneNumber(), person.getAddress(), person.getEmailAddress(), person.getBirthDate().toString().replace("-", "")); //writing the vCard format in a string
			Files.writeString(path.resolve(person.getId()+".vcf"), messageToWrite, StandardCharsets.UTF_8); //writing vCard file named according to the id in the database
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}


