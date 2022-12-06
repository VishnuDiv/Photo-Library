package controller;

import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import data.*;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * controls the user interface for SlideShow.fxml
 *
 * @author Shiv Joshi (scj61)
 * @author Vishnu Divakaruni (vsd23)
 */
public class SlideShowController implements Initializable {

@FXML
private ImageView image;

@FXML
private Button next;

@FXML
private Button quit;

@FXML
private Button previous;

@FXML
private Button returnToAlbum;
static User user;
static Albums album;
static int i = 0;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	/**
     * initializes the interface
	 *
     * @param User users
	 * @param Albums a
     */
	public void setStuff(User users, Albums a) {
		album = a;
		user = users;
		
		if(album.photos!=null) {
			Photo photo = album.photos.get(i);
			image.setImage(photo.image);
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No Slideshow");
			alert.setContentText("Album has no photos");
			alert.showAndWait();
			return;
		}
	}

	/**
     * navigates to the next Photo
	 *
     * @param MouseEvent event
     */
	public void nextButton(MouseEvent event){
		if(album.photos.get(i+1) != null){
			i++;
			Photo photo = album.photos.get(i);
			image.setImage(photo.image);
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No Next Photo");
			alert.setContentText("You cannot click next");
			alert.showAndWait();
			return;
		}
	}

	/**
     * navigates to the previous Photo
	 *
     * @param MouseEvent event
     */
	public void prevButton(MouseEvent event){
		if(album.photos.get(i-1) != null){
			i--;
			Photo photo = album.photos.get(i);
			image.setImage(photo.image);
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No Previous Photo");
			alert.setContentText("You cannot click prev");
			alert.showAndWait();
			return;
		}
	}

	/**
     * quits program
     *
     * @param MouseEvent event
     */
	public void quit(MouseEvent event) {
		Platform.exit();
	}

	/**
     * goes back to previous page
     *
     * @param MouseEvent event
	 * @throws IOException
     */
	public void backButton(MouseEvent event){
		try{
			FXMLLoader libraryLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Picture stuff.fxml"));
			Pane libraryPane = (Pane)libraryLoader.load(); 
			StuffController stuffcontroller = (StuffController) libraryLoader.getController();
			stuffcontroller.setStuff(user, album);
				
			Stage stage = new Stage();
			stage.setTitle("Picture");
			Scene scene = new Scene(libraryPane);

			stage.setScene(scene);
			stage.show();
					
			Node  source = (Node)  event.getSource(); 
			Stage stage2  = (Stage) source.getScene().getWindow();
			stage2.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}