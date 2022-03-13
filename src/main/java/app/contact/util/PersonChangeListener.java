package app.contact.util;

import app.contact.entities.Person;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public abstract class PersonChangeListener implements ChangeListener<Person>  { //listening for changes on the person objects in the TableBiew
	@Override
	public void changed(ObservableValue<? extends Person> observable, Person oldValue, Person newValue) {
		handleNewValue(newValue); //handling the change

	}

	public abstract void handleNewValue(Person newValue); //abstract method to be overrided to handle the change

}
