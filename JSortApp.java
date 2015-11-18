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
import javafx.scene.layout.StackPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class JSortApp extends Application

{	

	private File sortDir;
	JSorter jsort;
	
	public void start(Stage stage) {
		Button chooseFile = new Button("choose file to be sorted");
		chooseFile.setStyle("-fx-font-size:14pt; -fx-font-weight:bold; -fx-base: #d3d3d3; -fx-font-family:Monaco, 'Courier New', MONOSPACE");
		chooseFile.setOnAction( new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			    final DirectoryChooser directoryChooser =
			        new DirectoryChooser();
			    final File selectedDirectory =
			            directoryChooser.showDialog(stage);
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
						alert.setTitle("yey");
						alert.setHeaderText("success");
						alert.showAndWait();
			    	} catch (IOException ex) {
			    		System.out.println("sihlulekile");
			    	}
			    }
			}
		});
		hbox.getChildren().addAll(chooseFile, sort);
		hbox.setAlignment(Pos.CENTER);
		CheckBox directoryFlag = new CheckBox("sort directories");
		CheckBox deleteDuplicates = new CheckBox("delete duplicates");
		deleteDuplicates.setStyle("-fx-font-size:10pt; -fx-font-weight:bold; -fx-text-fill: rgb(255, 255, 255); -fx-base: #ffffff; -fx-font-family:Monaco, 'Courier New', MONOSPACE");
		directoryFlag.setStyle("-fx-font-size:10pt; -fx-font-weight:bold; -fx-text-fill: rgb(255, 255, 255); -fx-font-family:Monaco, 'Courier New', MONOSPACE");
		deleteDuplicates.setAlignment(Pos.BOTTOM_LEFT);
		directoryFlag.setAlignment(Pos.BOTTOM_LEFT);
		directoryFlag.setMaxWidth(200);
		deleteDuplicates.setMaxWidth(200);
		VBox center = new VBox();
		center.setAlignment(Pos.CENTER);
        center.setSpacing(20);
        center.getChildren().addAll(hbox, directoryFlag, deleteDuplicates);
        VBox layout = new VBox();
        StackPane sp = new StackPane();
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
						} 
			    	} catch (Exception ex) {
			    		System.out.println("sihlulekile");
			    	}
			    }
		    	
			}
		});
		HBox hb = new HBox();
		hb.setSpacing(100);
		//add filters
		VBox addFilter = new VBox();
		Button add = new Button("add filter");
		addFilter.setAlignment(Pos.BOTTOM_LEFT);
		addFilter.getChildren().add(add);
		setButtonStyle(add);
		hb.getChildren().addAll(addFilter,undoBox);
		hb.setAlignment(Pos.BOTTOM_CENTER);
        layout.getChildren().addAll(center, hb);
        Scene scene = new Scene(sp, 500, 500);
        layout.setSpacing(100);
        layout.setAlignment(Pos.CENTER);
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
	
	public void setButtonStyle(Button b) {
		b.setStyle("-fx-font-size:12pt; -fx-font-weight:bold; -fx-base: #d3d3d3; -fx-font-family:Monaco, 'Courier New', MONOSPACE");
	}
	
	public static void main(String[] args) throws IOException{
		launch(args);
	}
}
