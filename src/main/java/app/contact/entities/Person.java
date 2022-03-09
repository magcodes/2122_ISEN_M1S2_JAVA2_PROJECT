package app.contact.entities;

import java.time.LocalDate;

public class Person {
	private int id;
	private String firstName;
	private String lastName;
	private String nickName;
	private String phoneNumber;
	private String emailAddress;
	private String address;
	private LocalDate birthDate;
	
	public Person() {		
	}
	
	public Person(int id, String firstName, String lastName, String nickName, String phoneNumber, String emailAddress,
			String address, LocalDate birthDate) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nickName = nickName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.address = address;
		this.birthDate = birthDate;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	
	
}
