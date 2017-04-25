package FileProcess;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloader {
	final static int size = 1024;
	
	public static void downloadWithURL(String fileURLStr, String fileName, String fileDir){
		
		String rootPath = System.getProperty("user.home");
		try {
			URL fileURL = new URL(fileURLStr);
			File filePath = new File(rootPath);
			File fileDest = new File(rootPath + "/" + fileDir);
			
			fileDest.mkdirs();
			FileOutputStream fos = new FileOutputStream(rootPath + "/" + fileDir + "/" + fileName);
			InputStream is = fileURL.openStream();
			
			System.out.println("Download to "+rootPath + "/" + fileDir + "/" + fileName);
			
			byte[] buf = new byte[1024];
			double len = 0;
			double tmp = 0;
			double percent = 0;
			
			while((len = is.read(buf)) > 0){
				tmp += len / 1024 / 1024;
				percent = tmp / 1229*100;
				System.out.printf("%.1f\n", percent);
				fos.write(buf, 0, (int)len);
			}
			fos.close();
			is.close();
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
