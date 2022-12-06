package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import data.*;

import java.io.*;

import javafx.application.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * controls the user interface for Photo Library Login.fxml
 *
 * @author Shiv Joshi (scj61)
 * @author Vishnu Divakaruni (vsd23)
 */
public class LibraryController { 

    @FXML
    private Button quitBut;

    @FXML
    private TextField enter_login;

    @FXML
    private Button loginBut;
    
	/**
     * determines button actions for login and quit
	 *
     * @param ActionEvent e
	 * @throws IOException
     */
    public void ButtonActions (ActionEvent e) throws IOException{
		Button clicked = (Button)e.getSource();
		String input = enter_login.getText();

		if (clicked == loginBut ) {
			if(input.trim().toLowerCase().equals("")){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Empty Input Field");
				alert.setContentText("Enter username");
				alert.showAndWait();
			}
			if (input.trim().toLowerCase().equals("admin")) {
				Parent afterAdminLogin = FXMLLoader.load(getClass().getResource("/fxml/Admin Page.fxml"));
				Scene adminScene = new Scene(afterAdminLogin);
				Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
				window.setScene(adminScene);
				window.show();
			}else {
				for(User user : User.users){
					if(input.trim().toLowerCase().equals(user.name)){
						for(Albums a: user.albums){
							a.albumInit();
							for(Photo p: a.photos){
								p.photoInit();
								for(Tags t: p.tags){
									t.tagInit();
								}
							}
						}

						FXMLLoader libraryLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Album Details.fxml"));
						Pane libraryPane = (Pane)libraryLoader.load(); 
						Albumcontroller albumcontroller = (Albumcontroller) libraryLoader.getController();
						albumcontroller.setUsername(enter_login.getText());
						albumcontroller.show(enter_login.getText());
								
						Stage stage = new Stage();
						stage.setTitle("Library");
						Scene scene = new Scene(libraryPane);
			
						stage.setScene(scene);
						stage.show();
								
						Node  source = (Node)  e.getSource(); 
						Stage stage2  = (Stage) source.getScene().getWindow();
						stage2.close();
					}
				}	
			}
		}else if (clicked == quitBut) {
			save(User.users);
			Platform.exit();
		}
	}

	/**
     * saves data to disk
     *
     * @param List<User> users
	 * @throws Exception
     */
	public void save(List<User> users) {
    	//writes to file
		try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("src/data/data.dat"));
    		out.writeObject(users);
    		out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
    }
}
