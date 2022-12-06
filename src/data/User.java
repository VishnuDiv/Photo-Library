package data;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Represents a user, and the data associated with that user
 *
 * @author Shiv Joshi (scj61)
 * @author Vishnu Divakaruni (vsd23)
 */
public class User implements Serializable{
	
	private static final long serialVersionUID = -2413644287201467272L;
	
	public static ArrayList<User> users = new ArrayList<User>();
	public String name;
	public ArrayList<Albums> albums;
	
	/**
	 * Instantiates a user object with a name
	 * 
	 * @param name of the user
	 */
	public User(String name){
		this.name = name;
		albums = new ArrayList<Albums>();
	}

	public String toString(){
		return this.name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Restores objects from a previous serialized session via a file "users.ser"
	 * 
	 * 
	 */
	/*public static void addFromFile() {

		boolean newLib = true;

		try{
			File f = new File("src/data/users.dat");
			if(!f.exists()){
				f.createNewFile();
				System.out.println("file created");
			}else{
				newLib = false;
			}
		}catch(IOException e){
			System.out.println("Could not create file!");
			e.printStackTrace();
		}

		//reads existing file and puts it into this song library if the file exists
		if(newLib == false){
			try{
				FileInputStream fileIn = new FileInputStream("src/data/users.dat");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				System.out.println("found file");
				try{
					while(true){
						User temp = (User)in.readObject();					
						User.users.add(temp);
					}
				}catch(EOFException e){
					System.out.println("Songlist successfully loaded");
				}finally{
					in.close();
					fileIn.close();
				}
			}catch(IOException | ClassNotFoundException c){
				System.out.println("Corrupted file, overwriting this one.");
				corruptedLibrary();
			}
		}		
	}*/
	
	/**
	 * Handles instances of a corrupted or empty "users.ser" file 
	 * 
	 * 
	 */
	/*public static void corruptedLibrary() {
		try {
			Files.delete(Paths.get("src/data/users.dat"));
			File f = new File("src/data/users.dat");
			if(!f.exists()){
				f.createNewFile();
				System.out.println("New library created.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	/**
	 * Serializes all data in the current instance of the program to a file "users.ser" 
	 * 
	 * 
	 */
	/*public static void serialize(){
		try{
			FileOutputStream fileOut = new FileOutputStream("src/data/users.dat");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			for(User u : User.users){
				out.writeObject(u);
			}
			out.close();
			fileOut.close();
		}catch(IOException i){
			i.printStackTrace();
		}
	}*/
}
