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
	TableView<Person> personsTable; //Declare TableView
	
	@FXML
	TableColumn<Person, String> personsColumn; //Declare TableColumn

	@FXML
	AnchorPane formPane; //Declare FormPane

	@FXML
	TextField nameField; //Declare TextField for the last name

	@FXML
	TextField adressField; //Declare TextField

	@FXML
	TextField mailAdressField; //Declare TextField

	@FXML
	TextField firstNameField; //Declare TextField
	
	@FXML
	TextField nickNameField; //Declare TextField
	
	@FXML
	TextField phoneNumberField; //Declare TextField

	@FXML
	DatePicker birthDateField; //Declare DatePicker
	
	@FXML
	Button newButton; //Declare Button
	
	@FXML
	Button saveButton; //Declare Button
	
	@FXML
	Button deleteButton; //Declare Button
	
	

	Person currentPerson = new Person(); //Declare attribute to hold current selected person
	private ObservableList<Person> contacts = FXCollections.observableArrayList(); //list in the TableView

	private PersonDao personDao = new PersonDao(); //Data access object for the persons

	
    @FXML
    private void closeApp() throws IOException {  //to close the application
    	Platform.exit();
    }
    
    @FXML
    private void export() throws IOException { //to export all contacts to the root location of the project
    	personDao.exportPersons();
    }
    
    @FXML
    private void handleDelete() throws IOException { // to delete selected person
		System.out.println(this.currentPerson.getId());
    	personDao.deletePerson(currentPerson);
    	this.populateList();
    	this.resetView();
    }
    
    @FXML
    private void handleSave() throws IOException { //to save modifications to selected person
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
    private void handleAdd() throws IOException { //to add new person
    	Person person = personDao.addPerson(new Person(this.nameField.getText(), this.firstNameField.getText(), this.nickNameField.getText(),
    			this.phoneNumberField.getText(), this.adressField.getText(),this.mailAdressField.getText(), this.birthDateField.getValue()));
		this.populateList();
		this.personsTable.getSelectionModel().clearSelection();
    	this.personsTable.getSelectionModel().select(person);
    }
    
	private void refreshList() { //to refresh the TableView and clear selection
		this.personsTable.refresh();
		this.personsTable.getSelectionModel().clearSelection();
	}


	private void populateList() { //to fill TableView with updated contents of the database
		contacts.clear();
		personDao.listPersons().stream().forEach(contacts::add);
		this.personsTable.setItems(contacts);
		this.personsTable.refresh();
	}

	@FXML
	private void initialize() { //to initialize the page
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
	
	@FXML
    public void handleUnselect() throws IOException{ //to clear selection in table view and enable New button to add new person
        this.resetView();
    }
    
    private void showPersonsDetails(Person person) { //to fill the form with the details of the selected person
		if (person == null) {
//			this.formPane.setVisible(false)
			this.newButton.setDisable(false);
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
			this.newButton.setDisable(true);
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

	private void resetView() { //to reset the view
		this.showPersonsDetails(null);
		this.refreshList();
	}

}
