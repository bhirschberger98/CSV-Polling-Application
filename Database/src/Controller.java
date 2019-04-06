import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {
	private final static String[] prefixes = extractPrefixFromFile();	
	private final static String[] suffixes = extractSufixFromFile();	
	
	private File file;	
	
	private TableView<Candidate> table;	
    
	@FXML
    private Rectangle dropArea;
    	
    @FXML
	public void selectFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
        		new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("CSV", "*.csv")
            );
		file=new File(fileChooser.showOpenDialog(null).getAbsolutePath());
		tallyNames(file);
	}
    
    @FXML
    private void handleDragOver(DragEvent event) {
    	if(event.getDragboard().hasFiles()) {
    		event.acceptTransferModes(TransferMode.ANY);
    		dropArea.setFill(Color.LAWNGREEN);
    	}
    	else {
    		dropArea.setFill(Color.RED);
    	}
    }
    
    @FXML
    private void getDroppedFiles(DragEvent event) {
    	for(File file:event.getDragboard().getFiles()) {
    		tallyNames(file);
    	}
    	dropArea.setFill(Paint.valueOf("#ebeced"));
    }
    
    @FXML
    private void resetColor() {
    	dropArea.setFill(Paint.valueOf("#ebeced"));
    }
    
    
	public void  displayResults(ArrayList<Candidate> candidates) throws Exception {
		Stage window = new Stage();
        window.setTitle("Results");

        //Name column
        TableColumn<Candidate, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        //Votes column
        TableColumn<Candidate, Integer> votesColumn = new TableColumn<>("Votes");
        votesColumn.setMinWidth(100);
        votesColumn.setCellValueFactory(new PropertyValueFactory<>("Votes"));


        table = new TableView<>();
        table.setItems(getCandidates(candidates));
        table.getColumns().addAll(nameColumn, votesColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
	}
	 public ObservableList<Candidate> getCandidates(ArrayList<Candidate> candidates){
		 ObservableList<Candidate> observableListCandidates = FXCollections.observableArrayList();
		 for(Candidate c: candidates) {
			 observableListCandidates.add(c);
		 }
		 return observableListCandidates;
	        
	 }
	 public void tallyNames(File file) {
			//file object retrieves the file
			ArrayList<Candidate> candidates=new ArrayList<>(); //an arraylist for holding all of the candidate objects
			Scanner sc;
			try {
			sc = new Scanner(file);
			//for reading the file
			sc.useDelimiter(","); //sets the delimiter to a comma
			while(sc.hasNextLine()) { //loops once per line
				String line = sc.nextLine(); //retrieves a line from the file
				String[] names = line.split(","); //splits the string based on the comma
				for(int i = 0; i < names.length; i++) { //runs at the same amount as the length of the string array
					boolean abundant = false; //a boolean variable for checking if there's a match
					String name = names[i].replaceAll("﻿", "").trim(); //a specific element from the array that removes the utf-8 characters and any extra white spaces
					if(name.contains(" ")){ //runs if there's a space in the name
						name=trimSufixAndPrefix(name); //removes the suffix and/or prefix
					}
					if(name.contains(" ")){
						for(Candidate c: candidates){ //an enhanced for loop that declares a variable to access a specific element in the arraylist
							if(c.getName().contains(name.substring(0, name.indexOf(" "))) && c.getName().contains(name.substring(name.indexOf(" ") + 1, name.length()))){ //runs if the first initials and the last names matches
								abundant = true; //sets abundant to true
								c.incrementVotes(); //adds 1 votes variable of the candidate object
								break; //breaks the loop
							}
						}
					}
					if(abundant == false){ //runs if abundant is false
						candidates.add(new Candidate(name)); //adds a new candidate object
					}
				}

			}
			sc.close(); //closes the scanner
			boolean noMatches = false; //a boolean variable to check if there's anymore matches
			do {
				noMatches = true; //sets the variable to true
				for(int i = 0; i < candidates.size(); i++){ //runs at the same amount as the size of the arraylist
					boolean abundant = false;
					int possibilities = 0; //an int variable to record the number of possible matches
					Candidate c1 = candidates.get(i), candidate = new Candidate(""); //2 candidate objects

					if(c1.getName().contains(" ")){
						if(c1.getName().contains(".") || c1.getName().indexOf(" ") == 1 || c1.getName().indexOf(" ") == c1.getName().length() - 2){ //checks if there's an initial in the name
							for(Candidate c2: candidates){
								if(!c1.getName().equals(c2.getName()) && isInitials(c2, " ")){ //checks if both names aren't the same and if there's an initial
									if(compareNames(c1, c2)){ //compares both objects based on their names
										abundant = true;
										possibilities++; //adds 1 to the possibilities variable
										candidate = c2; //sets the candidate object to the c2 object
									}
								}
							}
						}
					}
					else {
						for(Candidate c2: candidates){
							if(!c1.getName().equals(c2.getName()) && !isInitials(c2,".")){
								if(c1.getName().contains(".")){ //checks if there's a period
									if(c2.getName().contains(" ") && c1.getName().substring(0, 1).equals(c2.getName().substring(0, 1)) &&
											c1.getName().substring(c1.getName().indexOf(".") + 1, c1.getName().indexOf(".") + 2).equals(c2.getName().substring(c2.getName().indexOf(" ") + 1, c2.getName().indexOf(" ") + 2))){ //checks if there's any initials
										abundant = true;
										possibilities++;
										candidate = c2;
									}
								}
								else{
									if(c2.getName().contains(c1.getName())){ //checks if the c2 object's name contains the c1 object's name in it
										abundant = true;
										possibilities++;
										candidate = c2;
									}
								}
							}
						}
					}
					if(abundant == true && possibilities == 1){ //runs if abundant is set to true and possibilities is set to 1
						noMatches = false; //sets noMatches to false
						candidate.addVotes(c1.getVotes()); //adds votes from the c1 object to the candidate object's vote variable
						candidates.remove(c1); //removes the c1 object
					}
				}
			}while(noMatches == false); //do...while loop that runs as long as noMatches is false
			//compares first names some might be nick names
			compareNames(candidates);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
			
		try {
			displayResults(candidates);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public  boolean isInitials(final Candidate c1, String s) {
		if(c1.getName().contains(s) && !(c1.getName().contains(".") || c1.getName().indexOf(" ") == 1 || c1.getName().indexOf(" ") == c1.getName().length() - 2))
			return true;
		return false;
	}
	
	public  String trimSufixAndPrefix(String name) {
		//checks if theres any prefixes or suffixes and removes them and any extra white spaces
		for(String s: prefixes){
			if(name.substring(0, name.indexOf(" ")).equalsIgnoreCase(s)){
				name = name.replaceAll(name.substring(0, name.indexOf(" ")), "");
			}
		}
		for(String s: suffixes){
			if(name.substring(name.lastIndexOf(" ") + 1, name.length()).equalsIgnoreCase(s)){
				name = name.replaceAll(name.substring(name.lastIndexOf(" ") + 1, name.length()), "");
				return name.trim();
			}
		}
		return name.trim();
	}
	public void displayAbout() {
		Stage primaryStage=new Stage();
		StackPane layout =new StackPane();
		layout.getChildren().add(new Text(150,100,"Made by Brett Hirschberger and Ivan Williams."));
		Scene scene = new Scene(layout,300,200);
        primaryStage.setTitle("About");
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	public void compareNames(ArrayList<Candidate> candidates) {
		for(int i=0;i<candidates.size();i++) {
			String[] s1=candidates.get(i).getName().split(" ");
			for(Candidate c2: candidates){
				String[] s2=c2.getName().split(" ");
				if((s1[0].contains(s2[0])||s2[0].contains(s1[0]))&&!s1[0].equalsIgnoreCase(s2[0])) {
					candidates.get(i).addVotes(c2.getVotes()); //adds votes from the c1 object to the candidate object's vote variable
					candidates.remove(c2);
					i--;
					break;
				}
			}
		}
	}
	
	
	private static String[] extractPrefixFromFile() {
		//retrieves the prefixes from the file that holds all of the prefixes, this helps for if we miss any possible prefixes that the user can add to the file
		File prefixFile = new File("prefixes.csv");
		try {
			Scanner scanner =new Scanner(prefixFile);
			String prefixes="";
			while(scanner.hasNext()) {
				prefixes=prefixes.concat(scanner.nextLine().replace("\n", ",").trim()+",");
			}
			scanner.close();
			String[] prefixArray = prefixes.trim().split(",");
			for(int i = 0;i<prefixArray.length;i++)
				prefixArray[i]=prefixArray[i].trim();
			return prefixArray;
		} catch (FileNotFoundException e) {
			System.out.println("Prefix File Not Found");
			return new String[]{"dr.", "dr", "mr.", "mr", "mrs.", "mrs", "ms.", "ms"};
		}	
		
	}
	
	private static String[] extractSufixFromFile() {
		//retrieves the suffixes from the file that holds all of the suffixes, this helps for if we miss any possible suffixes that the user can add to the file
		File suffixFile = new File("sufixes.csv");
		try {
			Scanner scanner =new Scanner(suffixFile);
			String suffixes="";
			while(scanner.hasNext()) {
				suffixes=suffixes.concat(scanner.nextLine().replace("\n", ",").trim()+",");
			}
			scanner.close();
			String[] suffixArray = suffixes.trim().split(",");
			for(int i = 0;i<suffixArray.length;i++)
				suffixArray[i]=suffixArray[i].trim();
			return suffixArray;
		} catch (FileNotFoundException e) {
			System.out.println("Sufix File Not Found");
			return new String[]{"jr.", "jr", "sr.", "sr", "II", "III", "md", "phd"};
		}
	}
	
	public boolean compareNames(final Candidate c1, final Candidate c2) {
		if((c2.getName().contains(c1.getName().substring(c1.getName().indexOf(" ") + 1, c1.getName().length())) &&
			c1.getName().substring(0, 1).equals(c2.getName().substring(0, 1))) ||
			(c2.getName().contains(c1.getName().substring(0, c1.getName().indexOf(" "))) &&
			c1.getName().substring(c1.getName().indexOf(" ") + 1, c1.getName().indexOf(" ") + 2).equals(c2.getName().substring(c2.getName().indexOf(" ") + 1, c2.getName().indexOf(" ") + 2)))){ //performs comparisons to check if there's similarities between the 2 names based on first and last initials and/or first and last names
			return true;
		}
		return false;
	}
}
