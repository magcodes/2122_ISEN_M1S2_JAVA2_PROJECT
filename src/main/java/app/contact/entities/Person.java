package app.contact.entities;

import java.time.LocalDate;

public class Person {
	private int id;
	private String firstName;
	private String lastName;
	private String nickName;
	private String phoneNumber;
	private String address;
	private String emailAddress;
	private LocalDate birthDate;
	
	public Person() {} //contructor to create empty person
	
	public Person(String lastName, String firstName, String nickName, String phoneNumber, String address, String emailAddress, // constructor to create person without id
			LocalDate birthDate) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.nickName = nickName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.address = address;
		this.birthDate = birthDate;
	}
	
	public Person(int id, String lastName, String firstName, String nickName, String phoneNumber, String address, String emailAddress, //constructor to create person with id
			LocalDate birthDate) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nickName = nickName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.address = address;
		this.birthDate = birthDate;
	}
	
	public int getId() { // getter for id
		return id;
	}
	public void setId(int id) { //setter for id
		this.id = id;
	}
	public String getNames() { // return lastName and firstName separated by space. It is used in the vCardSaver for the Full name
		return (lastName+" "+firstName);
	}
	public String getFullName() { //return firstName and lastName separated by space. It is used in the vCardSaver for the name
		return (firstName+" "+lastName);
	}
	public String getFirstName() { //getter for firstName
		return firstName;
	}
	public void setFirstName(String firstName) { //setter for firstName
		this.firstName = firstName;
	}
	public String getLastName() { //getter for lastName
		return lastName;
	}
	public void setLastName(String lastName) { //setter for lastName
		this.lastName = lastName;
	}
	public String getNickName() { //getter for nickName
		return nickName;
	}
	public void setNickName(String nickName) { //setter for nickName
		this.nickName = nickName;
	}
	public String getPhoneNumber() { //getter for phoneNumber
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) { //setter for  phoneNumber
		this.phoneNumber = phoneNumber;
	}
	public String getEmailAddress() { //getter for emailAddress
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) { //setter for emailAddress
		this.emailAddress = emailAddress;
	}
	public String getAddress() { //getter for address
		return address;
	}
	public void setAddress(String address) { //setter for address
		this.address = address;
	}
	public LocalDate getBirthDate() { //getter for birthDate
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) { //setter for birthDate
		this.birthDate = birthDate;
	}
	
	
}
