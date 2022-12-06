package data;
import java.io.Serializable;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a tag
 *
 * @author Shiv Joshi (scj61)
 * @author Vishnu Divakaruni (vsd23)
 */
public class Tags implements Serializable{
	private static final long serialVersionUID = 2027955001978488707L;
	  
	transient private SimpleStringProperty type;
	transient private SimpleStringProperty value;
	    
	private String dataType;
	private String dataValue;
	    
	/**
     * initializes Tags
     *
     * @param String type
	 * @param String value
     */
	public Tags(String type, String value){
		dataType = type;
		dataValue = value;
		this.type = new SimpleStringProperty(type);
		this.value = new SimpleStringProperty(value);
	}

	/**
     * returns value property of Tag
     */
	public StringProperty valueProperty() {
		return value;
	}
	
	/**
     * returns type property of Tag
     */
	public StringProperty typeProperty() {
		return type;
	}
	
	/**
     * intializes Tag
     */
	public void tagInit(){
		type = new SimpleStringProperty(dataType);
		value = new SimpleStringProperty(dataValue);
	}
}