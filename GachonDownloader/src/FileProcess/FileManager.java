package FileProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
	static String rootPath;
	static String logPath = System.getProperty("user.home")+File.separator+"GachonDownloader";
	static File dirFile = new File(logPath, "Directory.txt");
	private static BufferedWriter out;
	
	// By click the anchor of variable 'link',
		// inputStream will be generated from Web Response.
		public static boolean checkIfFileisExists(String fileName, String parentDir, String fileDir){
			initRootPath();
			fileDir = fileDir.replaceAll("[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]", " ");
			fileDir = fileDir.replaceAll("\\s{2,}", " ");
			fileDir = fileDir.trim();
			parentDir = parentDir.replaceAll("[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]", " ");
			parentDir = parentDir.replaceAll("\\s{2,}", " ");
			parentDir = parentDir.trim();
			
			// Init File instance with local path to download the file.
			File fileDest = new File(rootPath + parentDir + File.separator + fileDir);
			if (fileDest.exists()){
				return true;
			}else{
				return false;
			}
		}
		
		public static String getRootPath(){
			return rootPath;
		}
		
		public static void initRootPath(){
			String path = getRootPathFromLog();
			try{
				if (path.equals("WTFNOPATH")){
					setRootPath(System.getProperty("user.home"));
				}else{
					setRootPath(path);
				}
			}catch(Exception e){
				setRootPath(System.getProperty("user.home"));
			}
		}
		
		public static String getRootPathFromLog(){
			if (dirFile.exists()){
				try {
					BufferedReader in = new BufferedReader(new FileReader(dirFile));
					String s = in.readLine();
					in.close();
					return s;
				} catch (IOException e) {
					e.printStackTrace();
					return "WTFNOPATH";
				}
			}else{
				try {
					File logFile = new File(logPath);
					if(!logFile.exists()){
						logFile.mkdirs();
					}
					dirFile.createNewFile();
					setRootPath(System.getProperty("user.home"));
					
					return getRootPathFromLog();
				} catch (IOException e) {
					e.printStackTrace();
					return "WTFNOPATH";
				}
			}
		}
		
		public static void setRootPath(String _path){
			rootPath = _path;
			
			try {
				if(!dirFile.exists()){
					if(!dirFile.getParentFile().exists()){
						dirFile.getParentFile().mkdirs();
					}
					dirFile.createNewFile();
				}
				 out = new BufferedWriter(new FileWriter(dirFile, false));
				 out.write(rootPath);
				 out.newLine();
				 out.flush();
				 out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}
