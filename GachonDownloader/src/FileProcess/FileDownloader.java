package FileProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;

import System.MainSystem;

public class FileDownloader {
	final static int size = 1024;
	static String rootPath;
	static String logPath = System.getProperty("user.home")+File.separator+"GachonDownloader";
	static File dirFile = new File(logPath, "Directory.txt");
	static File articleFile = new File(logPath, "Articles.txt");
	private static BufferedWriter out;
	
	public static void writeLogFile(String log){
		// new BufferedWriter(new FileWriter(article, true));
		//==========================//
        // �ؽ�Ʈ ���� ����
        //==========================//
//        BufferedWriter bw = null;
//        try {
//        	if (!dirFile.exists()){
//    			logFile.createNewFile();
//    		}
//            bw = new BufferedWriter(new FileWriter(logFile, true));
//            bw.write(log+"\n");
//            bw.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            if(bw != null) try {
//            	bw.close(); 
//            	} catch (IOException e) {
//            		
//            	}
//        }
	}
	
	public static void writeArticleLog(String articleId){
		try {
			if (!articleFile.exists()){
				articleFile.createNewFile();
			}
			out = new BufferedWriter(new FileWriter(articleFile, true));
			out.write(articleId+" ");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> readArticleLogWithList(){
		List<String> articleList = new ArrayList<String>();
		
		try {
			if (!articleFile.exists()){
				articleFile.createNewFile();
			}
		      BufferedReader in = new BufferedReader(new FileReader(articleFile));
		      String s = in.readLine();
		     
		      in.close();
		      String[] articles = s.split("\\s");
		      for (String articleId : articles){
					articleList.add(articleId);
		      }
		} catch (Exception e) {
			return articleList;
		}
		
		return articleList;
	}
	
	// By click the anchor of variable 'link',
	// inputStream will be generated from Web Response.
	public static void downloadWithURL(HtmlAnchor link, String fileName, String parentDir, String fileDir){
		try {
			initRootPath();
			fileDir = fileDir.replaceAll("[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]", " ");
			fileDir = fileDir.replaceAll("\\s{2,}", " ");
			fileDir = fileDir.trim();
			parentDir = parentDir.replaceAll("[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]", " ");
			parentDir = parentDir.replaceAll("\\s{2,}", " ");
			parentDir = parentDir.trim();
			
			// Init File instance with local path to download the file.
			File fileDest = new File(rootPath + parentDir + File.separator + fileDir);
			fileDest.mkdirs();
			// Init Stream variables to download file with stream.
			FileOutputStream fos = new FileOutputStream(rootPath + parentDir + File.separator + fileDir + File.separator + fileName);
			InputStream is = link.click().getWebResponse().getContentAsStream();
			double size = (double)link.click().getWebResponse().getContentLength();
			byte[] buf = new byte[1024];
			double len = 0;
			double tmp = 0;
			double percent = 0;
			
			while((len = is.read(buf)) > 0){
				tmp += len;
				percent = (tmp / size)*100;
				MainSystem.myGUI.replaceLog("다운로드 중... "+Math.round(percent)+"%");
				fos.write(buf, 0, (int)len);
			}
			
			fos.close();
			is.close();
		}catch (Exception e){
			e.printStackTrace();
		}
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
		rootPath = _path+File.separator;
		
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
