import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;
import java.io.IOException;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.File;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Pair;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import javafx.scene.control.Label;

public class JSortApp extends Application

{	

	private File sortDir;
	private JSorter jsort;
	private Stage currentStage;
	
	public HBox setupSortButtons() {
		Button chooseFile = new Button("choose file to be sorted");
		chooseFile.setStyle("-fx-font-size:14pt; -fx-font-weight:bold; -fx-base: #d3d3d3; -fx-font-family:Monaco, 'Courier New', MONOSPACE");
		chooseFile.setOnAction( new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			    final DirectoryChooser directoryChooser =
			        new DirectoryChooser();
			    final File selectedDirectory =
			            directoryChooser.showDialog(currentStage);
			    if (selectedDirectory != null) {
			        selectedDirectory.getAbsolutePath();
			    }
			    sortDir = selectedDirectory;
			    jsort = new JSorter(sortDir.getAbsolutePath().toString());
			    chooseFile.setText(sortDir.getAbsolutePath().toString());
			}
		});
		HBox hbox = new HBox();
		hbox.setSpacing(10);
		
		//sort buttons
		Button sort = new Button("sort");
		sort.setMaxSize(70, 70);
		setButtonStyle(sort);
		sort.setOnAction( new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			    if(jsort == null) {
			    	Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("<-- opps");
					alert.setHeaderText("Look, to your left");
					alert.setContentText("please choose directory to sort");
					alert.showAndWait();
			    } else {
			    	try {
			    		jsort.sort(sortDir);
			    		Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Success");
						alert.setHeaderText("JSorted");
						alert.showAndWait();
			    	} catch (IOException ex) {
			    		System.out.println("sihlulekile");
			    	}
			    }
			}
		});
		hbox.getChildren().addAll(chooseFile, sort);
		hbox.setAlignment(Pos.CENTER);
		return hbox;
	}
	
	public CheckBox setupCheckBox(String title) {
		CheckBox cb = new CheckBox(title);
		cb.setStyle("-fx-font-size:10pt; -fx-font-weight:bold; -fx-text-fill: rgb(255, 255, 255); -fx-base: #ffffff; -fx-font-family:Monaco, 'Courier New', MONOSPACE");
		cb.setAlignment(Pos.BOTTOM_LEFT);
		cb.setMaxWidth(200);
		return cb;
	}
	
	public void start(Stage stage) {
		currentStage = stage;
		VBox layout = new VBox();	
        StackPane sp = new StackPane();
		HBox hb = new HBox();
		hb.setAlignment(Pos.BOTTOM_CENTER);
		hb.setSpacing(100);
		Scene scene = new Scene(sp, 500, 500);
		layout.setSpacing(100);
        layout.setAlignment(Pos.CENTER);
		
		//sort, checkboxes 
		HBox hbox = setupSortButtons();
		CheckBox directoryFlag = setupCheckBox("sort directories");
		CheckBox deleteDuplicates = setupCheckBox("delete duplicates");
		VBox center = new VBox();
		center.setAlignment(Pos.CENTER);
        center.setSpacing(20);
        center.getChildren().addAll(hbox, directoryFlag, deleteDuplicates);
       
		//undo button	
		VBox undoBox = new VBox();
		undoBox.setAlignment(Pos.BOTTOM_RIGHT);
		Button undo = new Button("undo");
		setButtonStyle(undo);
		undoBox.getChildren().add(undo);
		undo.setOnAction( new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				if(jsort == null) {
			    	Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Error");
					alert.setHeaderText("No directory selected");
					alert.setContentText("Please choose directory to undo.");
					alert.showAndWait();
			    } else {
			    	try {
			    		Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Confirm Undo");
						alert.setHeaderText("Undo");
						alert.setContentText("Are you sure you want to undo?");
						ButtonType yes = new ButtonType("Yes");
						ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
						alert.getButtonTypes().setAll(yes, cancel);
						Optional<ButtonType> result = alert.showAndWait();
						if(result.get() == yes) {
							jsort.undo();
							Alert suc = new Alert(AlertType.INFORMATION);
							suc.setTitle("Done");
							suc.setHeaderText("Returned to last known state.");
							suc.showAndWait();
						} 
			    	} catch (Exception ex) {
			    		System.out.println("sihlulekile");
			    	}
			    }
		    	
			}
		});
		
		//add filters
		VBox addFilter = new VBox();
		Button add = new Button("add filter");
		setUpFilter(add);
		addFilter.setAlignment(Pos.BOTTOM_LEFT);
		addFilter.getChildren().add(add);
		
		//add undo and filters to pane
		setButtonStyle(add);
		hb.getChildren().addAll(addFilter,undoBox);
		layout.getChildren().addAll(center, hb);
        layout.setMargin(undoBox, new Insets(8,8,8,8));
        layout.setMargin(addFilter, new Insets(8,8,8,8));
        sp.getChildren().add(layout);
        
        sp.getStylesheets().add("style.css");
       	sp.getStyleClass().add("pane");
        scene.getStylesheets().addAll(
				JSortApp.class.getResource("style.css").toExternalForm());
		stage.setScene(scene);
        stage.show();
	} 	
	
	public void setUpFilter(Button b) {
		
		b.setOnAction( new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				Dialog<Pair<String, String>> dialog = new Dialog<>();
				dialog.setTitle("Add");
				dialog.setHeaderText("Add new file description");
				ButtonType doneButton = new ButtonType("Done", ButtonData.OK_DONE);
				dialog.getDialogPane().getButtonTypes().addAll(doneButton, ButtonType.CANCEL);
				GridPane grid = new GridPane();
				grid.setHgap(10);
				grid.setVgap(10);
				grid.setPadding(new Insets(20, 150, 10, 10));
				TextField extension = new TextField();
				extension.setPromptText("djvu");
				TextField folder = new TextField();
				folder.setPromptText("Documents/DJVU");
				grid.add(new Label("All files with the extension:"), 0, 0);
				grid.add(extension, 1, 0);
				grid.add(new Label("JSort them into:"), 0, 1);
				grid.add(folder, 1, 1);
				dialog.getDialogPane().setContent(grid);
				dialog.setResultConverter(dialogButton -> {
					if (dialogButton == doneButton) {
						return new Pair<>(extension.getText(), folder.getText());
					}
					return null;
				});
				Optional<Pair<String, String>> result = dialog.showAndWait();
				result.ifPresent(usernamePassword -> {
    				String ext = usernamePassword.getKey();
    				String path = usernamePassword.getValue();
    				try {
    					FileWriter fw = new FileWriter("filetypes.txt", true);
    					BufferedWriter bw = new BufferedWriter(fw);
    					String finalString = ext + "$" + path;
    					boolean isThere = check(finalString);
    					if(!isThere) {
    						bw.append(finalString);
    					}
    					bw.close();
					} catch (IOException le) {
						le.printStackTrace();
					}
				});
			}
		});
	}
	
	public boolean check(String wrd) throws IOException {
		FileReader fr = new FileReader("filetypes.txt");
		BufferedReader br = new BufferedReader(fr);
		for(String line = br.readLine(); line != null; line = br.readLine()) {
			if(line.equals(wrd)) return true;
		}
		br.close();
		return false;
	}
	
	public void setButtonStyle(Button b) {
		b.setStyle("-fx-font-size:12pt; -fx-font-weight:bold; -fx-base: #d3d3d3; -fx-font-family:Monaco, 'Courier New', MONOSPACE");
	}
	
	public static void main(String[] args) throws IOException{
		launch(args);
	}
}
