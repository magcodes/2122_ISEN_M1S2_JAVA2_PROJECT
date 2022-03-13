package app.contact.views;

import javafx.scene.control.TextField;
import java.io.IOException;
import java.time.LocalDate;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import app.contact.database.Database;
import app.contact.database.daos.PersonDao;
import app.contact.entities.Person;
import app.contact.util.PersonChangeListener;
import app.contact.util.PersonValueFactory;
import javafx.fxml.FXML;

public class PrimaryController {

	@FXML
	TableView<Person> personsTable;
	
	@FXML
	TableColumn<Person, String> personsColumn;

	@FXML
	AnchorPane formPane;

	@FXML
	TextField nameField;

	@FXML
	TextField adressField;

	@FXML
	TextField mailAdressField;

	@FXML
	TextField firstNameField;
	
	@FXML
	TextField nickNameField;
	
	@FXML
	TextField phoneNumberField;

	@FXML
	DatePicker birthDateField;
	
	@FXML
	Button saveButton;
	
	@FXML
	Button deleteButton;
	

	Person currentPerson = new Person();
	private ObservableList<Person> contacts = FXCollections.observableArrayList();

	private PersonDao personDao = new PersonDao();

	
    @FXML
    private void closeApp() throws IOException {
    	Platform.exit();
    }
    
    @FXML
    private void export() throws IOException {
    	personDao.exportPersons();
    }
    
    @FXML
    private void handleDelete() throws IOException {
		System.out.println(this.currentPerson.getId());
    	personDao.deletePerson(currentPerson);
    	this.populateList();
    	this.resetView();
    }
    
    @FXML
    private void handleSave() throws IOException {
    	this.currentPerson.setLastName(this.nameField.getText());
    	this.currentPerson.setFirstName(this.firstNameField.getText());
    	this.currentPerson.setNickName(this.nickNameField.getText());
    	this.currentPerson.setPhoneNumber(this.phoneNumberField.getText());
    	this.currentPerson.setEmailAddress(this.mailAdressField.getText());
    	this.currentPerson.setAddress(this.adressField.getText());
    	this.currentPerson.setBirthDate(this.birthDateField.getValue());
    	personDao.updatePerson(this.currentPerson);
    	this.populateList();
    	this.showPersonsDetails(currentPerson);

    }
    
    @FXML
    private void handleAdd() throws IOException {
    	Person person = personDao.addPerson(new Person(this.nameField.getText(), this.firstNameField.getText(), this.nickNameField.getText(),
    			this.phoneNumberField.getText(), this.adressField.getText(),this.mailAdressField.getText(), this.birthDateField.getValue()));
		this.populateList();
    	this.personsTable.getSelectionModel().select(person);
    }
    
	private void refreshList() {
		this.personsTable.refresh();
		this.personsTable.getSelectionModel().clearSelection();
	}


	private void populateList() {
		contacts.clear();
		personDao.listPersons().stream().forEach(contacts::add);
		this.personsTable.setItems(contacts);
		this.personsTable.refresh();
	}

	@FXML
	private void initialize() {
		Database.initDb();
		this.personsColumn.setCellValueFactory(new PersonValueFactory());
		this.populateList();
		this.personsTable.getSelectionModel().selectedItemProperty().addListener(new PersonChangeListener() {
			@Override
			public void handleNewValue(Person newValue) {
				showPersonsDetails(newValue);
				
			}
		});
		this.resetView();
	}

    
    private void showPersonsDetails(Person person) {
		if (person == null) {
//			this.formPane.setVisible(false);
			this.saveButton.setDisable(true);
			this.deleteButton.setDisable(true);
			this.nameField.setText("");
	    	this.firstNameField.setText("");
	    	this.nickNameField.setText("");
	    	this.phoneNumberField.setText("");
	    	this.mailAdressField.setText("");
	    	this.adressField.setText("");
	    	this.birthDateField.setValue(LocalDate.now());
		} else {
			this.formPane.setVisible(true);
			this.saveButton.setDisable(false);
			this.deleteButton.setDisable(false);
			this.currentPerson = person;
			System.out.println(person.getId());
	    	this.nameField.setText(currentPerson.getLastName());
	    	this.firstNameField.setText(currentPerson.getFirstName());
	    	this.nickNameField.setText(currentPerson.getNickName());
	    	this.phoneNumberField.setText(currentPerson.getPhoneNumber());
	    	this.mailAdressField.setText(currentPerson.getEmailAddress());
	    	this.adressField.setText(currentPerson.getAddress());
	    	this.birthDateField.setValue(currentPerson.getBirthDate());
		}
	}

	private void resetView() {
		this.showPersonsDetails(null);
		this.refreshList();
	}

}
