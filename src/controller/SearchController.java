package controller;

import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import data.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * controls the user interface for Picture Search.fxml
 *
 * @author Shiv Joshi (scj61)
 * @author Vishnu Divakaruni (vsd23)
 */
public class SearchController implements Initializable{

	 @FXML
	    private Button back_search;

	    @FXML
	    private Button createTag;

	    @FXML
	    private Button create_range;

	    @FXML
	    private DatePicker end_date;

	    @FXML
	    private ListView<Photo> list_search;

	    @FXML
	    private Button range;

	    @FXML
	    private AnchorPane rangebody;

	    @FXML
	    private Tab rangetab;

	    @FXML
	    private Button searchA;

	    @FXML
	    private Button searchO;

	    @FXML
	    private TabPane search_search;

	    @FXML
	    private AnchorPane searchpane;

	    @FXML
	    private TextField secname;

	    @FXML
	    private TextField secval;

	    @FXML
	    private DatePicker start_date;

	    @FXML
	    private Button tag;

	    @FXML
	    private AnchorPane tagbody;

	    @FXML
	    private TextField tagname;

	    @FXML
	    private Tab tagsearch;

	    @FXML
	    private TextField tagval;
	    
	    @FXML
	    private Button quit;

    static User user;
    static Albums album;

    ObservableList<Photo> obslist;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		list_search.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Photo>(){
			public void changed(ObservableValue<? extends Photo> ov, 
					Photo old_val, Photo new_val) {
				
			}
		});

		list_search.setItems(obslist);
		list_search.setCellFactory(param -> new ListCell<Photo>() {
			ImageView imageV = new ImageView();
			VBox vb = new VBox();
			{
				vb.setAlignment(Pos.CENTER);
				imageV.setFitHeight(50);
				imageV.setFitWidth(50);
				vb.getChildren().addAll(imageV);
				setGraphic(vb);
			}
			public void updateItem(Photo name, boolean empty) {
                super.updateItem(name, empty);
                if(name != null){
                	imageV.setImage(name.image);
				}
				else{
					imageV.setImage(null);
				}
			}
		});
		
		list_search.setItems(obslist);
		list_search.getSelectionModel().getSelectionMode();
	}


	/**
     * initializes the interface
	 *
     * @param User users
	 * @param Albums a
     */
	public void setStuff(User users, Albums albums) {
		album = albums;
		user = users;
		obslist = FXCollections.observableArrayList();
		list_search.setItems(obslist);
		//list_search.getSelectionModel().select(0);
	}

	/**
     * searches for Photos in between specific date range
	 *
     * @param MouseEvent event
     */
	public void searchRange(MouseEvent event) {
		LocalDate start = start_date.getValue();
		LocalDate end = end_date.getValue();
		if(!obslist.isEmpty()) {
			obslist.removeAll(obslist);
			list_search.setItems(obslist);
		}
		
		
		if(start.compareTo(end) < 0) {
			for(Albums a: user.albums) {
				if(a!=null) {
					for(Photo p : a.photos) {
						LocalDate h = p.dateTaken.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						if(h.compareTo(start) >= 0 && h.compareTo(end)<= 0) {
							obslist.add(p);
						}
					}
				}
			}
		}
		list_search.setItems(obslist);
	}
	
	/**
     * creates a new Album with the results of the searchRange(date range search)
	 *
     * @param MouseEvent event
	 * @throws Exception 
     */
	public void createDateAlbum(MouseEvent event) throws Exception {
		TextInputDialog textInput = new TextInputDialog();
		textInput.setTitle("Create New Album");
		textInput.getDialogPane().setContentText("Enter Album Name:");
		textInput.showAndWait();
		String input = textInput.getEditor().getText();

		if(input.equals("")){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Empty Input Field");
			alert.setContentText("Enter album name");
			alert.showAndWait();
		}


		Albums album = new Albums(input);
		
		album.photos.addAll(obslist);
		user.albums.add(album);
		save(User.users);
	}
	
	/**
     * creates a new Album with the results of the tag search
	 *
     * @param MouseEvent event
	 * @throws Exception 
     */
	public void createTagAlbum(MouseEvent event) throws Exception {
		TextInputDialog textInput = new TextInputDialog();
		textInput.setTitle("Create New Album");
		textInput.getDialogPane().setContentText("Enter Album Name:");
		textInput.showAndWait();
		String input = textInput.getEditor().getText();

		Albums album = new Albums(input);
		
		album.photos.addAll(obslist);
		user.albums.add(album);
		save(User.users);
	}
	
	/**
     * goes back to previous page
     *
     * @param MouseEvent event
	 * @throws IOException
     */
	public void back(MouseEvent event) throws IOException {
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
	}
	
	/**
     * searches for Photos with a specific tag 
	 *
     * @param MouseEvent event
     */
	public void SearchSingleTag(MouseEvent event){
		String name =  tagname.getText().trim().toLowerCase();
		String val =  tagval.getText().trim().toLowerCase();
		if(name != null && val != null) {
			Tags tag = new Tags(name, val);
			
			if(!obslist.isEmpty()) {
				obslist.removeAll(obslist);
				list_search.setItems(obslist);
			}

			for(Albums a : user.albums) {
				for(Photo p : a.photos) {
					for( Tags t : p.tags) {
						if(t.typeProperty().getValue().equals(tag.typeProperty().getValue()) && t.valueProperty().getValue().equals(tag.valueProperty().getValue())) {
							obslist.add(p);
						}
					}
				}
			}
			list_search.setItems(obslist);
		}
	}
	
	/*
	* Performs Conjunctive Search
	*
	*@param MouseEvent event
	*/
	public void SearchAnd(MouseEvent event){
		String firstname =  tagname.getText().trim().toLowerCase();
		String firstval =  tagval.getText().trim().toLowerCase();
		String secondname =  secname.getText().trim().toLowerCase();
		String secondval =  secval.getText().trim().toLowerCase();
		if(firstname != null && firstval != null && secondname != null && secondval != null ) {
			Tags firsttag = new Tags(firstname, firstval);
			Tags secondtag = new Tags(secondname, secondval);

			Tags[] arr = new Tags[2];
			arr[0] = firsttag;
			arr[1] = secondtag;
			int i = -1;

			if(!obslist.isEmpty()) {
				obslist.removeAll(obslist);
				list_search.setItems(obslist);
			}

			for(Albums a : user.albums) {
				for(Photo p : a.photos) {
					for( Tags t : p.tags) {
						if(i==-1){
							if(t.typeProperty().getValue().equals(arr[0].typeProperty().getValue()) && t.valueProperty().getValue().equals(arr[0].valueProperty().getValue())){
								i = 1;
							}else if(t.typeProperty().getValue().equals(arr[1].typeProperty().getValue()) && t.valueProperty().getValue().equals(arr[1].valueProperty().getValue())){
								i = 0;
							}
						}else if(i==1){
							if(t.typeProperty().getValue().equals(arr[1].typeProperty().getValue()) && t.valueProperty().getValue().equals(arr[1].valueProperty().getValue())){
								obslist.add(p);
							}
						}else if(i==0){
							if(t.typeProperty().getValue().equals(arr[0].typeProperty().getValue()) && t.valueProperty().getValue().equals(arr[0].valueProperty().getValue())){
								obslist.add(p);
							}
						}
					}
					i=-1;
				}
			}
			list_search.setItems(obslist);
		}
	}
	
	/*
	* Performs Disjunctive Search
	*
	*@param MouseEvent event
	*/
	public void SearchOr(MouseEvent event){
		String firstname =  tagname.getText().trim().toLowerCase();
		String firstval =  tagval.getText().trim().toLowerCase();
		String secondname =  secname.getText().trim().toLowerCase();
		String secondval =  secval.getText().trim().toLowerCase();
		if(firstname != null && firstval != null && secondname != null && secondval != null ) {
			Tags firsttag = new Tags(firstname, firstval);
			Tags secondtag = new Tags(secondname, secondval);

			if(!obslist.isEmpty()) {
				obslist.removeAll(obslist);
				list_search.setItems(obslist);
			}
			
			for(Albums a : user.albums) {
				for(Photo p : a.photos) {
					for( Tags t : p.tags) {
						if(t.typeProperty().getValue().equals(firsttag.typeProperty().getValue()) && t.valueProperty().getValue().equals(firsttag.valueProperty().getValue())
							||	(t.typeProperty().getValue().equals(secondtag.typeProperty().getValue()) &&  t.valueProperty().getValue().equals(secondtag.valueProperty().getValue()))) {
							obslist.add(p);
						}
					}
				}
			}
			list_search.setItems(obslist);
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
