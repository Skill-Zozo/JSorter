import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JSorter {
	
	private ST<String, String> st = new ST<String, String>();
	private LinkedList<String> directoriesMade = new LinkedList<>();
	private Path path;
	static private class NotDirectory extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = -4922699640393449859L;

		public NotDirectory(String message) {
			super(message);
		}
	}

	public JSorter(String f) {
		path = Paths.get(f);
		File file = new File(f);
		if(!file.isDirectory()) {
			System.out.println(String.format("%s is not a directory", path));
			System.exit(1);
		}
		try {
			this.populateSymbolTable();
		} catch (IOException e) {
			System.out.println("Failed to populate table");
		}
	}

	public static void main(String[] args) throws NotDirectory, IOException {
		/*
		 * receives arg[0] = directory_to_be_sorted
		 * receives arg[1] = flag for undo
		 */
		JSorter jsort = new JSorter(args[0]);
		jsort.sort(new File(args[0]));
	}
	
	public void populateSymbolTable() throws IOException {
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
			line = line.trim();
			if(line.length() > 0) {
				int breakingpoint = line.indexOf('$');
				String filetype = line.substring(0, breakingpoint);
				String path = line.substring(breakingpoint+1, line.length());
				writeFolderName(path);
				st.put(filetype, path);
			}
		}
		st.put("unknown", "Other" + File.separator + "Unknown");
		br.close();
		fr.close();
	}
 
	public void sort(File file) throws IOException {	//sort out the files
		File[] subfiles = file.listFiles();
		for(File f : subfiles) {
			boolean isDotFile = f.getName().charAt(0) == '.';
			if (f.isFile() && !isDotFile) {
				String[] fileProperties = split(f.getName());
				if(fileProperties == null) {
					
				}
				String filepath = fileProperties[1];
				Path src = Paths.get(f.toURI());
				String folder = "";
				if(!st.containsItem(filepath)) {
					folder = "Other" + File.separator + filepath;
					if(!directoriesMade.contains(filepath)) {
						directoriesMade.add(filepath);
					}
				} 
				else {
					folder = st.get(filepath);
					writeFolderName(folder);
				}
				makeDirectories(path.toAbsolutePath()+File.separator+ folder);
				String dest = path.toAbsolutePath()+File.separator+folder+File.separator+f.getName();
				Path destination = new File(dest).toPath();
				if(new File(dest).exists()) continue;
				Files.move(src, destination);
				logMovement(src.toAbsolutePath().toString(), dest);
			}
		}
		logFilesMade();
	}
	
	public void logFilesMade() throws IOException {
		File log = new File(path.toAbsolutePath()+File.separator+".jsorthistory");
		if(!log.exists()) {
			log.createNewFile();
		}
		FileWriter fw = new FileWriter(log, true);
		BufferedWriter bw = new BufferedWriter(fw);
		for(int i = 0; i < directoriesMade.getSize(); i++) {
			String dir = directoriesMade.getItem(i);
			bw.write("[folder]" + dir);
			bw.newLine();
		}
		bw.close();
		fw.close();
	}
	
	private void writeFolderName(String folder) {
		String[] folders = folder.split(File.separator);
		if(!directoriesMade.contains(folders[folders.length-1]))
			directoriesMade.add(folders[folders.length-1]);
	}
	
	public void logMovement(String from, String to) throws IOException {
		File log = new File(path.toAbsolutePath()+File.separator+".jsorthistory");
		if(!log.exists()) {
			log.createNewFile();
		}
		FileWriter fw = new FileWriter(log, true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(from+"->"+to+"\n");
		bw.close();
		fw.close();
	}
	
	public void addDescription(String filetype, String path) {
		st.put(filetype, path);
	}
	
	public void undo() throws FileNotFoundException, IOException {
		//read movements from history and undo
		File f = new File(path.toAbsolutePath()+File.separator+".jsorthistory");
		if(!f.exists()) {
			System.out.println("No previous history of JSorter.");
			return;
		}
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		for(String line = br.readLine(); line != null; line = br.readLine()) {
			if(line.startsWith("[folder]")) {
				directoriesMade.add(line.substring(8,line.length()));
				continue;
			}
			if(!line.contains("->")) continue;
			String[] places = line.split("->");
			if(new File(places[0]).exists()) continue;
			File src = new File(places[1]);
			File dest = new File(places[0]);
			if(src.exists() == false || dest.exists() == true) continue; 
			Files.move (new File(places[1]).toPath(), new File(places[0]).toPath());
		}
		System.out.println("Undo Complete. Removing directories now.");
		removeEmptyDirectories(path.toAbsolutePath().toString());
		f.delete();
		br.close();
	}
	
	public void removeEmptyDirectories(String newFile) {
		File file = new File(newFile);
		File[] dirs = file.listFiles();
		for(File f : dirs) {
			if(f.exists() && f.isDirectory()) {
				boolean knownEmptyDirectory = directoriesMade.contains(f.getName());
				if(f.length() == 0  && knownEmptyDirectory) { //file is not empty
					removeFile(f);
				} else {
					removeEmptyDirectories(f.toString());
				}
				removeFile(f);
			}
		}
	}

	private static void removeFile(File f) {
		if(f.exists() && f.listFiles().length == 0) { 
			f.delete();
			System.out.println("Removed " + f.getName());
		}
	}
	
	private void makeDirectories(String directory) throws IOException {	//make parent directories
		FileSystem fs = FileSystems.getDefault();
		Path tmp = fs.getPath(directory);
		if(!Files.exists(tmp)) {
			Files.createDirectories(tmp);
			if(!directoriesMade.contains(directory)) {
				directoriesMade.add(directory);
			}
		}
	}

		/** 
		 * returns an array with the first element as the 
		 * actual name and the second element being the
		 * extension
		 **/
	private static String[] split(String filename) {
		/**
		*	returns the an array [filename, extension]
		**/
		if(!filename.contains(".")) {
			String ext = "unknown";
			String[] giveback = {filename, ext};
			return giveback;
		} else {
			int periodIndex = filename.lastIndexOf('.');
			String name = filename.substring(0, periodIndex);
			String extension = filename.substring(periodIndex+1, filename.length());
			String[] giveback = {name, extension};
			return giveback;
		}
	}

}
