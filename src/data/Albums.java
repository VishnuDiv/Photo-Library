package data;
import java.io.Serializable;


import java.util.ArrayList;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Class that represents the Album object used throughout the program
 *
 * @author Shiv Joshi(scj61)
 * @author Vishnu Divakaruni(vsd23)
 */

public class Albums implements Serializable{

	private static final long serialVersionUID = -6864095642765229087L;
	transient private SimpleStringProperty name;
    transient private SimpleStringProperty date;
    transient private SimpleStringProperty range;
    transient private SimpleStringProperty num;
    
    private String dataName, dataDate, dataRange, dataNum;
    
	public ArrayList<Photo> photos;
	
	public Albums(String name) {
		photos = new ArrayList<Photo>();
		this.dataName = name;
		this.name = new SimpleStringProperty(name);
	}
	
	/**
	 * sets name of album
	 *
	 * @param name
	 */

	public void setNameProperty(String name){
		this.name.set(name);
		this.dataName = name;
	}
	
	/**
	 * gets name of album
	 *
	 * @return
	 */
	
	public String getName(){
		return this.name.get();
	}
	
	/**
	 * gets name of album
	 *
	 * @return
	 */
	
	public StringProperty nameProperty() {
	    return name;
	}

	
	public void albumInit(){
		name = new SimpleStringProperty(dataName);
		date = new SimpleStringProperty(dataDate);
		range = new SimpleStringProperty(dataRange);
		num = new SimpleStringProperty(dataNum);
	}
	
	/**
	/**
	 * gets StringProperty for number of photos in album
	 *
	 * @return
	 */
	
	public StringProperty numProperty() {
		dataNum = ""+photos.size();
		return new SimpleStringProperty("" + photos.size());
	}
	
	@Override
	public String toString(){
		return this.getName();
	}
}
