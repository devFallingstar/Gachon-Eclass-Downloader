package System;

import Session.*;
import Data.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import FileProcess.*;

public class MainSystem {
	private static String rootURL = "http://eclass.gachon.ac.kr/";
	private static MainSystem myMain;
	private GachonSession tmp1;
	private WebClient myClient;
	private HtmlPage myPage;
	private List<LectureData> lectureList;
	
	private List<String> fileList;
	
	public static void main(String[] args) {
		myMain = new MainSystem();
	}
	
	public MainSystem(){
		int resultCode;
		this.tmp1 = new GachonSession();
		resultCode = this.tmp1.logIn("stardung86", "Netdong12");
		
		if (resultCode == 0){
			myClient = this.tmp1.getSession();
			try {
				myPage = (HtmlPage)myClient.getPage(rootURL+"index.jsp");
				
				getLectureList();
				System.out.println(lectureList);
				//TODO : File List get and download
				
				for (String subPath : lectureList){
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("Web page Loading failed!");
		}
	}
	
	public void startDownload(String url, String name, String dir){
		FileDownloader.downloadWithURL(url, name, dir);
	}
	
	private void getLectureList(){
		List<String> resultList = new ArrayList<String>();
		List<DomElement> divList = myPage.getElementsByTagName("a");
		
		for (DomElement e : divList){
			String value = e.getAttribute("href");
			if (value.startsWith("/myMain.jsp?Forum_seq=")) {
				LectureData lecture;
				lecture.lectureURL = rootURL+value;
			}
		}
	}
	
	private List<String> getFileList(String lectureURL){
		List<String> files = null;
		
		return null;
	}

}