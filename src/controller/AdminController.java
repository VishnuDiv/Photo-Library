package controller;

import java.util.*;
import java.io.*;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import data.*;

/**
 * controls the user interface for Admin Page.fxml
 *
 * @author Shiv Joshi (scj61)
 * @author Vishnu Divakaruni (vsd23)
 */
public class AdminController implements Initializable {

	@FXML
	private Button quit;
	
	@FXML
    private Button back_Admin;

    @FXML
    private Button create_admin;

    @FXML
    private Button delete_admin;

    @FXML
    private TextField enter_admin;

    @FXML
    private ListView<User> list_admin;
    ObservableList<User> obslist;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		list_admin.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>(){
			public void changed(ObservableValue<? extends User> ov, 
					User old_val, User new_val) {
				if(new_val == null){
					enter_admin.setText("");
				}else{
					enter_admin.setText("");
				}
			}
		}); 
		
		obslist = FXCollections.observableArrayList(User.users);
		list_admin.setItems(obslist);
		list_admin.getSelectionModel().getSelectedItem();
	}
	
	/**
     * determines button actions for create_admin, back_admin, and delete_admin
	 *
     * @param ActionEvent e
	 * @throws IOException
     */
	public void ButtonAct(ActionEvent e) throws IOException {
		 Button clicked = (Button)e.getSource();
		if (clicked == back_Admin) {
			Parent afterAdminLogin = FXMLLoader.load(getClass().getResource("/fxml/Photo Library Login.fxml"));
	        Scene adminScene = new Scene(afterAdminLogin);
	        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
	        window.setScene(adminScene);
	        window.show();
		} else if (clicked == create_admin) {
			String input = enter_admin.getText().trim().toLowerCase();
			if(("").equals(input)){
				//nothing happens
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid Username");
				alert.setContentText("Please enter in a username");
				alert.showAndWait();
				return;
			}

			if(!User.users.isEmpty()){
				for(User u : User.users){
					if(u.name.equals(input)){
						//duplicates not allowed
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Duplicate Admin");
						alert.setContentText("Cannnot add another admin");
						alert.showAndWait();
						return;
					}
				}
			}
					
			User user = new User(input);
			User.users.add(user);
			obslist.add(user);
			list_admin.setItems(obslist);
			list_admin.getSelectionModel().selectFirst();
			save();
			return;
		}else if (clicked == delete_admin) {
			String temp = enter_admin.getText().trim().toLowerCase();

	        if(temp.equals("")){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid Username");
				alert.setContentText("Please enter in a username");
				alert.showAndWait();
				return;
			}

	        for(int i = 0; i < User.users.size(); i++){
				if(User.users.get(i).name.equals(temp)){
					//User found!
					User.users.remove(User.users.get(i));
					obslist.remove(obslist.get(i));
					list_admin.setItems(obslist);
					list_admin.getSelectionModel().selectFirst();
					save();
					return;
				}
			}

	        //User does not exist 
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("User Doesn't Exist");
			alert.setContentText("Please enter in a valid username");
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
     * saves data to disk
     *
     * @param List<User> users
	 * @throws Exception
     */
	public void save() throws IOException{
		//writes to file
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("src/data/data.dat"));
		out.writeObject(new ArrayList<> (Arrays.asList(list_admin.getItems().toArray())));
		out.close();	
    }
}