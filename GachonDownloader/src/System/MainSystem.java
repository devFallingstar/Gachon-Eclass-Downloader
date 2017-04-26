package System;

import Session.*;
import Data.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import FileProcess.*;

public class MainSystem {
	private static String rootURL = "http://eclass.gachon.ac.kr";
	private static MainSystem myMain;
	private GachonSession tmp1;
	private WebClient myClient;
	private HtmlPage myPage;
	private List<LectureData> lectureList = new ArrayList<LectureData>();
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
				myPage = (HtmlPage)myClient.getPage(rootURL);
				
				getEclassList();
				//TODO : File List get and download
				
				for (LectureData _lecture : lectureList){
					System.out.println(_lecture.getEclassURL());
					myPage = (HtmlPage)myClient.getPage(_lecture.getEclassURL());
					this.getLectureList(_lecture);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("Web page Loading failed!");
		}
	}
	
	public void startDownload(String url, String name, String dir){
//		FileDownloader.downloadWithURL(url, name, dir);
	}
	
	private void getEclassList(){
		List<DomElement> divList = myPage.getElementsByTagName("a");
		
		for (DomElement e : divList){
			String hrefValue = e.getAttribute("href");
			if (hrefValue.startsWith("/myMain.jsp?Forum_seq=")) {
				LectureData lecture = new LectureData();
				lecture.setEclassURL(rootURL+hrefValue);
				lecture.setLectureID(hrefValue.substring(22));
				lecture.setLectureName(e.getTextContent());
				lectureList.add(lecture);
			}
		}
	}
	
	private void getLectureList(LectureData _lect){
		List<DomElement> aList = myPage.getElementsByTagName("a");
		
		for (DomElement myLecture : aList){
			String hrefValue = myLecture.getAttribute("href");
			if(myLecture.getTextContent().contains("강의자료실")){
				try {
					System.out.println("Hello1");
					String lectureFileListURL = rootURL+hrefValue+"&pageSize=500";
					HtmlPage lecturePage = (HtmlPage)myClient.getPage(lectureFileListURL);
					List<DomElement> articleList = lecturePage.getElementsByTagName("a");
					for (DomElement myArticle : articleList){
						String articleHref = myArticle.getAttribute("href");
						if (articleHref.startsWith("javascript:goPage(0,")){
							
							String articleId = articleHref.substring(20, articleHref.indexOf(')'));
							String articleURL = lectureFileListURL+"&article_no="+articleId;
							articleURL = articleURL.replace("list.jsp", "view.jsp");
							System.out.println(articleURL);
							HtmlPage articlePage = (HtmlPage)myClient.getPage(articleURL);
							
							
							List<DomElement> fileList = articlePage.getElementsByTagName("a");
							for (DomElement myFile : fileList){
								String fileHref = myFile.getAttribute("href");
								
								if (fileHref.startsWith("../board/FileDownLoad.jsp?")){
									String fileURL = rootURL+fileHref.substring(2);
									String fileName = myFile.getTextContent();
									String fileDir = _lect.getLectureName().replace(" ", "") +"/"+ myArticle.getTextContent();
									HtmlAnchor currentFileAnchor = null;
									fileName = fileName.substring(0, fileName.indexOf("(")).trim();
									
									for (HtmlAnchor myAnchor : articlePage.getAnchors()){
										if (myAnchor.getTextContent().contains(fileName)){
											currentFileAnchor = myAnchor;
											System.out.println(myAnchor.getTextContent());
											break;
										}
									}
									
									FileDownloader.downloadWithURL(currentFileAnchor, fileName, fileDir);
								}
								
							}
						}
					}
					
				} catch (FailingHttpStatusCodeException | IOException e1) {
					e1.printStackTrace();
				}
				
//				_lect.setLectureURL(rootURL+hrefValue+"&pageSize=500");
			}
		}
	}
	
	private void getFileList(LectureData myLecture){
		
	}

}
