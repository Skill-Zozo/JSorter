import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JSorter {
	
	static ST<String, String> st = new ST<String, String>();
	static Path path;
	static private class NotDirectory extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = -4922699640393449859L;

		public NotDirectory(String message) {
			super(message);
		}
	}

	public static void main(String[] args) throws NotDirectory, IOException {
		/*
		 * recieves arg[0] = directory_to_be_sorted
		 */
		
		path = Paths.get(args[0]);
		File file = new File(args[0]);
		if(!file.isDirectory()) {
			NotDirectory e= new NotDirectory(String.format("%s is not a directory", path));
			throw e;
		}
		populateSymbolTable();
		sort(file);
		System.out.println("JSorter completed");
	}
	
	private static void populateSymbolTable() throws IOException {
		File f =null;
		try {
			f = new File("filetypes.txt");
		} catch(Exception e) {
			System.out.println("Please move the file named 'filetypes.txt' to the same directory as this program.");
			System.exit(0);
		}
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		for(String line = br.readLine(); line != null; line = br.readLine()) {
			if(line != "\n") {
				int breakingpoint = line.indexOf('$');
				String filetype = line.substring(0, breakingpoint);
				String path = line.substring(breakingpoint+1, line.length());
				st.put(filetype, path);
			}
		}
		br.close();
		fr.close();
	}
 
	private static void sort(File file) throws IOException {	//sort out the files
		File[] subfiles = file.listFiles();
		for(File f : subfiles) {
			if (f.isFile()) {
				String[] fileProperties = split(f.getName());
				String filepath = fileProperties[1];
				Path src = Paths.get(f.toURI());
				makeDirectories(path.toAbsolutePath()+File.separator+st.get(filepath));
				String dest = path.toAbsolutePath()+File.separator+st.get(filepath)+File.separator	+f.getName();
				Path destination = new File(dest).toPath();
				Files.move(src, destination);
			}
		}
	}

	private static void makeDirectories(String directory) throws IOException {	//make parent directories
		FileSystem fs = FileSystems.getDefault();
		Path tmp = fs.getPath(directory);
		if(!Files.exists(tmp)) 
			Files.createDirectories(tmp);
}

	private static String[] split(String filename) {
		/* 
		 * returs an array with the first element as the 
		 * actual name and the second element being the
		 * extension
		 */
		int periodIndex = filename.lastIndexOf('.');
		String name = filename.substring(0, periodIndex);
		String extension = filename.substring(periodIndex+1, filename.length());
		String[] giveback = {name, extension};
		return giveback;
	}

}
