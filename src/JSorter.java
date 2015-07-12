import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
		sort(file);		
	}
	
	private static void populateSymbolTable() throws IOException {
		File f = new File("filetypes.txt");
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		for(String line = br.readLine(); line != null; line = br.readLine()) {
			if(line != "\n") {
				String[] fileproperties = line.split("$");
				String filetype = fileproperties[0];
				String path = fileproperties[1];
				st.put(filetype, path);
			}
		}
		br.close();
		fr.close();
	}
 
	private static void sort(File file) throws IOException {
		File[] subfiles = file.listFiles();
		for(File f : subfiles) {
			if (f.isFile()) {
				String[] fileProperties = split(f.getName());
				String filetype = fileProperties[0];
				String filepath = fileProperties[1];
				Path src = Paths.get(f.toURI());
				String dest = src.toString()+filepath+f.getName();
				Path destination = new File(dest).toPath();
				Files.move(src, destination);
			}
		}
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
