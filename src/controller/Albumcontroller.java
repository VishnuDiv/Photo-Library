package controller;

import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import data.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import controller.*;

/**
 * controls the user interface for Album Details.fxml
 *
 * @author Shiv Joshi (scj61)
 * @author Vishnu Divakaruni (vsd23)
 */
public class Albumcontroller implements Initializable{

	@FXML
	private Button quit;
	
	@FXML
	private Button create_detail;

	@FXML
	private Button delete_detail;

	@FXML
	private ListView<Albums> list_detail;

	@FXML
	private Button logout_detail;

	@FXML
	private TextField name_detail;

	@FXML
	private TextField number_detail;

	@FXML
	private Button open_detail;

	@FXML
	private ImageView preview_detail;

	@FXML
	private TextField range_detail;

	@FXML
	private Button rename_detail;

	@FXML
	private Button search_detail;

	ObservableList<Albums> obslist;
	static String users123;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) { 
		list_detail.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Albums>(){
			public void changed(ObservableValue<? extends Albums> ov, 
					Albums old_val, Albums new_val) {
				if(new_val == null){
					name_detail.setText("");
					number_detail.setText("");
					range_detail.setText("");
					preview_detail.setImage(null);
				}else{
					name_detail.setText(new_val.getName().trim());
					number_detail.setText(String.valueOf(new_val.photos.size()));

					if(new_val.photos.size()==0) {
						range_detail.setText("N/A");
						preview_detail.setImage(null);
					}else {
						Date min = new Date (Long.MAX_VALUE);
						Date max = new Date (Long.MIN_VALUE);
						for(Photo p : new_val.photos) {
							if(p.dateTaken.compareTo(min) <= 0) {
								min = p.dateTaken;
							}
							if(p.dateTaken.compareTo(max) >= 0) {
								max = p.dateTaken;
							}
						}
						
						SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
						range_detail.setText(sdf.format(min) + " - " + sdf.format(max));
						Photo photo  = new_val.photos.get(0);
						preview_detail.setImage(photo.image);
					}
				}
			}
		}); 

		name_detail.setEditable(false);
		number_detail.setEditable(false);
		range_detail.setEditable(false);
		list_detail.setItems(obslist);
		list_detail.getSelectionModel().getSelectionMode();
	}
		
	/**
     * displays all Albums for the user
     *
     * @param String user
     */
	public void show(String user) {
		for ( User users: User.users) {
			if(user.trim().toLowerCase().equals(users.name)) {
				obslist = FXCollections.observableArrayList(users.albums);
			}
		}//for loop ends
		if(!obslist.isEmpty()) {
			list_detail.setItems(obslist);
			list_detail.getSelectionModel().select(0);
		} else {
			obslist = null;
			list_detail.setItems(null);
		}
	}
		
	/*
	* sets the username of the current user
	*
	* @param String username
	*/
	public void setUsername(String username) {
		users123 = username;
	}
	
	/**
     * logs out the current user and returns back to login page
     *
     * @param MouseEvent event
	 * @throws IOException
     */
	public void Logout(MouseEvent event) throws IOException{
		Parent adminScene = FXMLLoader.load(getClass().getResource("/fxml/Photo Library Login.fxml"));
		Scene loginScene = new Scene(adminScene);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(loginScene);
		window.show();
	}
		
	/**
     * creates a new Album for user
     *
     * @param MouseEvent event
	 * @throws Exception 
     */
	public void createAlbum (MouseEvent event) throws Exception {
		TextInputDialog textInput = new TextInputDialog();
		textInput.setTitle("Create Album");
		textInput.getDialogPane().setContentText("Enter Album Name:");
		textInput.showAndWait();
		String input = textInput.getEditor().getText();

		//seeing if input is null
		if(input == null || input.length()==0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Null Input");
			alert.setContentText("Please enter album name");
			alert.showAndWait();
			return;
		}
		if(input != null && input.length()!= 0) {
			for ( User users: User.users ) {
				//find the user
				if(users123.trim().toLowerCase().equals(users.name)) {

					//check if album name alr exists
					for(Albums a : users.albums){
						if(a.getName().equals(input)) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Duplicate Album");
							alert.setContentText("New album cannot be added");
							alert.showAndWait();
							return;
						}
					}

					//add new album
					Albums album = new Albums(input.toString());
					users.albums.add(album);
					obslist = FXCollections.observableArrayList(users.albums);
					obslist.add(album);
					save(User.users);
				}
			}
		}
		
		show(users123);
	}

	/**
     * deletes the Album
     *
     * @param MouseEvent event
	 * @throws Exception 
     */
	public void deleteAlbum(MouseEvent event) throws Exception {
		for ( User users: User.users) {
			if(users123.trim().toLowerCase().equals(users.name)) {
				Albums album = list_detail.getSelectionModel().getSelectedItem();	
				users.albums.remove(album);
				obslist = FXCollections.observableArrayList(users.albums);
				obslist.remove(album);
				save(User.users);
			}
		}
		show(users123);	
	}

	/**
     * renames an existing Album
     *
     * @param MouseEvent event
	 * @throws Exception 
     */
	public void renameAlbum(MouseEvent event) throws Exception {
		TextInputDialog textInput = new TextInputDialog();
		textInput.setTitle("Rename Album");
		textInput.getDialogPane().setContentText("Enter Album Name:");
		textInput.showAndWait();
		String input = textInput.getEditor().getText();
		for ( User users: User.users) {
			if(users123.trim().toLowerCase().equals(users.name)) {

				//check if album name alr exists
				for(Albums a : users.albums){
					if(a.getName().equals(input)) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Duplicate Album Name");
						alert.setContentText("Enter in a unique album name");
						alert.showAndWait();
					}
				}

				Albums album = list_detail.getSelectionModel().getSelectedItem();
				album.setNameProperty(input);
				users.albums.remove(album);
				users.albums.add(album);
				obslist = FXCollections.observableArrayList(users.albums);
				save(User.users);
			}
		}
		
		show(users123);
	}

	/**
     * opens an existing Album
     *
     * @param MouseEvent event
	 * @throws IOException
     */
	public void openAlbum(MouseEvent event) throws IOException {
		FXMLLoader libraryLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Picture stuff.fxml"));
		Pane libraryPane = (Pane)libraryLoader.load(); 
		StuffController stuffcontroller = (StuffController) libraryLoader.getController();

		for(User user : User.users) {
			if(users123.trim().toLowerCase().equals(user.name)) {
				stuffcontroller.setStuff(user, list_detail.getSelectionModel().getSelectedItem());
			}
		}       

		Stage stage = new Stage();
		stage.setTitle("Picture");
		Scene scene = new Scene(libraryPane);

		stage.setScene(scene);
		stage.show();
				
		Node  source = (Node)  event.getSource(); 
		Stage stage2  = (Stage) source.getScene().getWindow();
		stage2.close();
	}

	/**
     * navigates to new page to search an Album with parameters
     *
     * @param MouseEvent event
	 * @throws IOException
     */
	public void searchAlbum(MouseEvent event) throws IOException {
		FXMLLoader libraryLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Picture Search.fxml"));
		Pane libraryPane = (Pane)libraryLoader.load(); 
		SearchController searchcontroller = (SearchController) libraryLoader.getController();
		for(User user : User.users) {
			if(users123.trim().toLowerCase().equals(user.name)) {
				searchcontroller.setStuff(user, list_detail.getSelectionModel().getSelectedItem());
			}
		}
				
		Stage stage = new Stage();
		stage.setTitle("Search");
		Scene scene = new Scene(libraryPane);

		stage.setScene(scene);
		stage.show();
				
		Node  source = (Node)  event.getSource(); 
		Stage stage2  = (Stage) source.getScene().getWindow();
		stage2.close();
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
     * saves data to disk
     *
     * @param List<User> users
	 * @throws Exception
     */
	public void save(List<User> users) throws Exception {
		try {
			ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream("src/data/data.dat"));
			o.writeObject(users);
			o.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
