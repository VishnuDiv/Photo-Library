package data;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
/**
 * This class represents a photo object 
 *
 * @author Shiv Joshi(scj61)
 * @author Vishnu Divakaruni(vsd23)
 */
public class Photo implements Serializable{

	private static final long serialVersionUID = -5122170586100200237L;

	public Date dateTaken;
	
    transient private SimpleStringProperty caption;
    transient private SimpleStringProperty path;
	transient private SimpleObjectProperty<Image> pImage;
    
	private String dataCaption;
	private String dataPath;
	
	public ArrayList<Tags> tags;
	transient public Image image;
	
	/**
	 * Sets the properties for the photo
	 *
	 */
	public void photoInit(){
		caption = new SimpleStringProperty(dataCaption);
		path = new SimpleStringProperty(dataPath);
		
		image = new Image("file:///" + dataPath, true);
		pImage = new SimpleObjectProperty<Image>(this.image);
		
	}
	
	/**
	 * returns date of photo
	 *
	 * @return
	 */
	public StringProperty dateProperty() {
	    return new SimpleStringProperty(dateTaken.toGMTString());
	}
	
	/**
	 * returns the caption of the photo
	 *
	 * @return
	 */
	public StringProperty captionProperty() {
	    return caption;
	}
	
	/**returns path of photo
	 *
	 * @return 
	 */
	public StringProperty pathProperty() {
	    return path;
	}
	
	/**
	 * sets caption of photo
	 *
	 * @param 
	 */
	public void setCaptionProperty(String caption){
		dataCaption = caption;
		this.caption.set(caption);
		
	}
	
	public SimpleObjectProperty<Image> pImageProperty(){
		return pImage;	
	}
	
	/**
	 * Initializes new photo
	 *
	 */
	public Photo(File file) {
		this.path = new SimpleStringProperty(file.getAbsolutePath());
		this.tags = new ArrayList<Tags>();
		
		dataPath = file.getAbsolutePath();
		
		this.caption = new SimpleStringProperty("");
		
		this.image = new Image("file:///" + dataPath, true);
		this.pImage = new SimpleObjectProperty<Image>(this.image);
		this.dateTaken = new Date(file.lastModified());

	}
}
