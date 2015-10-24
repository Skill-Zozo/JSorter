import javafx.application.Application;
public class JSortApp extends Application
{	

	private File sortDir;
	
	public void start(Stage stage) {
		Button chooseFile = new Button("choose file to be sorted");
		chooseFile.setStyle("-fx-font-size:14pt; -fx-font-weight:bold; -fx-font-family:Monaco, 'Courier New', MONOSPACE");
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
			    System.out.println(sortDir.getName());
			}
		});
		
	} 	
	
	public static void main(String[] args) {
		launch(args)
	}
}
