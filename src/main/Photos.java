package main;

import javafx.application.Application;
import data.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.*;
import java.util.*;
import controller.*;

public class Photos extends Application {
	Stage mainStage;
@Override
public void start(Stage stage) throws Exception {
	mainStage = stage;
	mainStage.setTitle("Photo Library");
	try {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/Photo Library Login.fxml"));
		AnchorPane pane = (AnchorPane)loader.load();
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.show();
		User user = new User("stock");
		User.users.add(user);
		save(User.users);
		Albums album = new Albums ("stock");
		user.albums.add(album);
		save(User.users);
		
		File file1 = new File("src/stock/download.jpeg");
		File file2 = new File("src/stock/downloads.jpeg");
		File file3 = new File("src/stock/jap jazz.jpeg");
		File file4 = new File("src/stock/images.jpeg");
		File file5 = new File("src/stock/murakami.jpeg");
		Photo photo1 = new Photo(file1);
		Photo photo2 = new Photo(file2);
		Photo photo3 = new Photo(file3);
		Photo photo4 = new Photo(file4);
		Photo photo5 = new Photo(file5);
		album.photos.add(photo1);
		album.photos.add(photo2);
		album.photos.add(photo3);
		album.photos.add(photo4);
		album.photos.add(photo5);
		save(User.users);
	} catch (IOException e) {
		e.printStackTrace();
	}
}
	/*
	* main class of program
	*
	* @param String[] args
	*/
	public static void main(String[] args) {
		launch(args);
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