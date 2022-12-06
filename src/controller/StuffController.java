package controller;

import java.util.*;
import javafx.scene.input.MouseEvent;
import data.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Pair;


/**
 * controls the user interface for Picture stuff.fxml
 *
 * @author Shiv Joshi (scj61)
 * @author Vishnu Divakaruni (vsd23)
 */
public class StuffController implements Initializable {
	
	@FXML
	private Button move_button;
	
	@FXML
	private Button copy;
	
    @FXML
    private TextField tname;

    @FXML
    private TextField tval;

	@FXML
    private Button add_pic;
	
	@FXML
    private Button delete_pic;

    @FXML
    private TextField album_pic;

    @FXML
    private Button back_pic;

    @FXML
    private TextField cap_pic;

    @FXML
    private Button caption;

    @FXML
    private TextField date_pic;

    @FXML
    private ImageView imageView;

    @FXML
    private ListView<Photo> list_pic;

    @FXML
    private ChoiceBox<Albums> choice;

    @FXML
    private TextField name_pic;

    @FXML
    private TableColumn<Tags, String> name_tag;

    @FXML
    private Button slideshow;

    @FXML
    private Button tag_add;

    @FXML
    private TableView<Tags> tags_pic;

    @FXML
    private TableColumn<Tags, String> value_tag;
    
    @FXML
    private Button quit;
    
    static User user;
    
    static Albums album;
    
    ObservableList<Photo> obslist;
    
    ObservableList<Tags> tags = FXCollections.observableArrayList();
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		name_tag.setCellValueFactory(new PropertyValueFactory<Tags, String>("type"));
		value_tag.setCellValueFactory(new PropertyValueFactory<Tags, String>("value"));
		tags_pic.setItems(tags);
		
		list_pic.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Photo>(){
			public void changed(ObservableValue<? extends Photo> ov, 
					Photo old_val, Photo new_val) {
						
				if(new_val == null) {
					name_pic.setText("");
					date_pic.setText("");
					cap_pic.setText("");
					album_pic.setText("");
					imageView.setImage(null);
				}else {
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					date_pic.setText(sdf.format(new_val.dateTaken));
					imageView.setImage(new_val.image);
					tags = FXCollections.observableArrayList(new_val.tags);
					tags_pic.setItems(tags);
					cap_pic.setText(album.getName());
					name_pic.setText(user.getName());
					if(new_val.captionProperty().get() == null) {
						cap_pic.setText("N/A");
					}else {
					cap_pic.setText(new_val.captionProperty().get());
					}
				}
			}
		});

		list_pic.setItems(obslist);
		list_pic.setCellFactory(param -> new ListCell<Photo>() {
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
	}
	
	/**
     * initializes the interface
	 *
     * @param User users
	 * @param Albums a
     */
	public void setStuff(User users,Albums a) {
		user = users;
		album= a;
		obslist = FXCollections.observableArrayList(album.photos);
		ObservableList<Albums> albums = FXCollections.observableArrayList(user.albums);
		choice.setItems(albums);
		if(!obslist.isEmpty()) {
			album_pic.setText(album.getName());
			list_pic.setItems(obslist);
			list_pic.getSelectionModel().select(0);
		}
	}
	
	/**
     * adds tag type-value pair to a photo
     *
     * @param MouseEvent event
	 * @throws Exception 
     */
	public void addTags(MouseEvent event) throws Exception {
		if(tname.getText().equals("") || tname.getText().equals("")){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Empty Tag Field");
			alert.setContentText("Enter valid tag type and value inputs");
			alert.showAndWait();
			return;
		}

    	for(Tags tag : tags){
    		if(tag.typeProperty().get().equals(tname.getText()) && tag.valueProperty().get().equals(tname.getText())){
				return;
			}
    	}

		Tags tag = new Tags(tname.getText(), tval.getText());
		Photo photo = list_pic.getSelectionModel().getSelectedItem();
		
		photo.tags.add(tag);
		this.tags.add(tag);
		tags_pic.setItems(tags);
		name_tag.setText("");
		value_tag.setText("");
		save(User.users);
	}
	
	/**
     * deletes a tag type-value pair from a photo
     *
     * @param MouseEvent event
	 * @throws Exception 
     */
	public void deleteTags(MouseEvent event) throws Exception {
		Tags tag = tags_pic.getSelectionModel().getSelectedItem();
		Photo photo = list_pic.getSelectionModel().getSelectedItem();
    	photo.tags.remove(tag);
    	this.tags.remove(tag);
		tags_pic.setItems(tags);
		save(User.users);
	}
	
	/**
     * adds a Photo to an Album
     *
     * @param MouseEvent event
	 * @throws Exception 
     */
	public void addPhotos(MouseEvent event) throws Exception {
		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg"));

		File selectedFile = fileChooser.showOpenDialog(stage).getAbsoluteFile();
		Photo photo = new Photo(selectedFile);

		if (selectedFile != null) {
			for(Photo p : album.photos){
        		if((selectedFile.getAbsolutePath()).equals(p.pathProperty().get())){
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Duplicate Photo");
					alert.setContentText("New photo cannot be added");
					alert.showAndWait();
        			return;
				}	
			}
			
			album.photos.add(photo);
			obslist.add(photo);
			list_pic.setItems(obslist);
			save(User.users);
		}
    }
	 
	/**
     * deletes a Photo from an Album
     *
     * @param MouseEvent event
	 * @throws Exception 
     */
	public void deletePhotos(MouseEvent event) throws Exception {
		Photo photo = list_pic.getSelectionModel().getSelectedItem();
		album.photos.remove(photo);
		obslist.remove(photo);
		save(User.users);
	}
	 
	/**
     * edits a caption of a Photo
     *
     * @param MouseEvent event
	 * @throws Exception 
     */
	public void editCaption(MouseEvent event) throws Exception {
		Photo photo = list_pic.getSelectionModel().getSelectedItem();
		TextInputDialog textInput = new TextInputDialog();
		textInput.setTitle("Edit Caption");
		textInput.getDialogPane().setContentText("Enter New Caption Name: ");
		textInput.showAndWait();
		String input = textInput.getEditor().getText();

		if(input.equals("")){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Empty Input Field");
			alert.setContentText("Enter valid caption");
			alert.showAndWait();
			return;
		}

		if(photo != null) {
			photo.setCaptionProperty(input);
		}
		save(User.users);
		cap_pic.setText(input);
	}

	/**
     * goes back to previous page
     *
     * @param MouseEvent event
	 * @throws IOException
     */
	public void backButton(MouseEvent event) throws IOException {
		FXMLLoader libraryLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Album Details.fxml"));
		Pane libraryPane = (Pane)libraryLoader.load(); 
		Albumcontroller albumcontroller = (Albumcontroller) libraryLoader.getController();
		albumcontroller.setUsername(user.getName());
		albumcontroller.show(user.getName());
				
		Stage stage = new Stage();
		stage.setTitle("Library");
		Scene scene = new Scene(libraryPane);

		stage.setScene(scene);
		stage.show();
				
		Node source = (Node)  event.getSource(); 
		Stage stage2  = (Stage) source.getScene().getWindow();
		stage2.close();
	}

	/**
     * moves an Album to another User's library of Albums
     *
     * @param MouseEvent event
     */
	public void moveAlbum(MouseEvent event) {
		Photo photo = list_pic.getSelectionModel().getSelectedItem();
		Albums newAlbum = choice.getValue();

		if(choice.getValue() != null){
			for(Photo p : newAlbum.photos){
				if(photo.pathProperty().get().equals(p.pathProperty().get()))
					return;
			}
			newAlbum.photos.add(photo);
			album.photos.remove(photo);
			obslist.remove(photo);
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Choice");
			alert.setContentText("Make a valid choice");
			alert.showAndWait();
			return;
		}
		
		list_pic.setItems(obslist);
	}
	
	/**
     * copies an Album to another User's library of Albums
     *
     * @param MouseEvent event
     */
	public void copyAlbum(MouseEvent event) {
		Photo photo = list_pic.getSelectionModel().getSelectedItem();
		Albums newAlbum = choice.getValue();

		if(choice.getValue() != null){
			for(Photo p : newAlbum.photos){
				if(photo.pathProperty().get().equals(p.pathProperty().get()))
					return;
			}
			newAlbum.photos.add(photo);
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Choice");
			alert.setContentText("Make a valid choice");
			alert.showAndWait();
			return;
		}
		
		list_pic.setItems(obslist);
	}

	/**
     * navigates to SlideShow
     *
     * @param MouseEvent event
	 * @throws IOException
     */
	public void SlideShows(MouseEvent event) throws IOException {
		if(album.photos.isEmpty()){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No Slideshow");
			alert.setContentText("Album has no photos");
			alert.showAndWait();
			return;
		}else{
			FXMLLoader libraryLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/SlideShow.fxml"));
			Pane libraryPane = (Pane)libraryLoader.load(); 
			SlideShowController slideshow = (SlideShowController) libraryLoader.getController();
			slideshow.setStuff(user, album);
			Stage stage = new Stage();
			stage.setTitle("Search");
			Scene scene = new Scene(libraryPane);

			stage.setScene(scene);
			stage.show();
					
			Node  source = (Node)  event.getSource(); 
			Stage stage2  = (Stage) source.getScene().getWindow();
			stage2.close();
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
			o.writeObject(User.users);
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
