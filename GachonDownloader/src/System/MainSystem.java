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
import GUI.mainGUI;

public class MainSystem extends Thread {
	public static mainGUI myGUI;
	private String ID, PW;
	private String rootURL = "http://eclass.gachon.ac.kr";
	private GachonSession tmp1;
	private WebClient myClient;
	private HtmlPage myPage;
	private List<ClassData> classList = new ArrayList<ClassData>();
	private List<String> readArticles;
	
	public static void main(String[] args) {
		myGUI = new mainGUI();
	}
	
	public MainSystem(String _id, String _pw){
		ID = _id;
		PW = _pw;
		tmp1 = new GachonSession();
	}
	
	public MainSystem() {
	}
	
	public void run() {
        getLogIn();
        
    }

	@SuppressWarnings("deprecation")
	private int getLogIn(){
		int resultCode;
		myGUI.addLog("로그인 진행 중...");
		resultCode = this.tmp1.logIn(ID, PW);
		
		if (resultCode == 0){
			myGUI.addLog("로그인 성공~!");
			myGUI.addLog("필요한 정보 구성 중...");
			readArticles = FileDownloader.readArticleLogWithList();
			
			myClient = this.tmp1.getSession();
			try {
				myGUI.addLog("과목 정보 받는 중...");
				myPage = (HtmlPage)myClient.getPage(rootURL);
				getClassList();
				myGUI.addLog("과목 정보를 모두 받았습니다.");
				
				myGUI.addLog("과목 별 강의 정보 받는 중...");
				for (ClassData _lecture : classList){
					myPage = (HtmlPage)myClient.getPage(_lecture.getURL());
					getLecturesFromClassData(_lecture);
				}
				myGUI.addLog("모든 파일을 다운로드 받았습니다!");
				myGUI.setEnableDownloadWidgets();
				this.stop();
			} catch (Exception e) {
				myGUI.addLog("네트워크 오류!");
				myGUI.addLog(e.getMessage());
//				myGUI.setEnableDownloadWidgets();
			}
		}else{
			myGUI.addLog("로그인 실패!");
			myGUI.addLog("아이디와 비밀번호를 다시 확인해주세요!");
		}
		myGUI.setEnableDownloadWidgets();
		
		return resultCode;
	}
	
	// Get class list from eclass page
	private void getClassList(){
		List<DomElement> divList = myPage.getElementsByTagName("a");
		
		for (DomElement e : divList){
			String hrefValue = e.getAttribute("href");
			if (hrefValue.startsWith("/myMain.jsp?Forum_seq=")) {
				String eclassURL = rootURL+hrefValue;
				String lectureID = hrefValue.substring(22);
				String lectureName = e.getTextContent();
				
				myGUI.addLog("과목 정보 받는 중 : "+lectureName);
				ClassData newClass = new ClassData();
				newClass.setURL(eclassURL);
				newClass.setID(lectureID);
				newClass.setName(lectureName);
				classList.add(newClass);
			}
		}
	}
	
	// Get List of lecture
	private void getLecturesFromClassData(ClassData _class){
		myGUI.addLog("강의 파일 받기 시작 : "+_class.getName());
		
		// Parse every lectures
		// And put them in to ClassData's LectureURL
		List<DomElement> aList = myPage.getElementsByTagName("a");
		
		for (DomElement myMainPageAnchor : aList){
			String hrefValue = myMainPageAnchor.getAttribute("href");
			
			// If anchor has text content, "강의자료실",
			// it means the anchor has URL of lecture too.
			if(myMainPageAnchor.getTextContent().contains("강의자료실")){
				try {
					// Save Lecture URL to ClassData
					String lectureFileListURL = rootURL+hrefValue+"&pageSize=500";
					HtmlPage lecturePage = (HtmlPage)myClient.getPage(lectureFileListURL);
					
					// Get anchors list on lecture page to get article list.
					List<DomElement> articleList = lecturePage.getElementsByTagName("a");
					for (DomElement myArticle : articleList){
						String articleHref = myArticle.getAttribute("href");
						
						// If anchor's href string start with "javascript:goPage(0,",
						// it means the anchor has URL of article too.
						if (articleHref.startsWith("javascript:goPage(0,")){
							
							// Open article page to get file list.
							String articleId = articleHref.substring(20, articleHref.indexOf(')'));
							// If this System read the article already,
							// it doesn't have to read once again.
							if (readArticles.contains(articleId)){
								myGUI.addLog("이미 다운로드를 마친 강의입니다.");
								continue;
							}
							String articleURL = lectureFileListURL+"&article_no="+articleId;
							// Since we have to 'view' some content with a page,
							// change source page from list.jsp to view.jsp
							articleURL = articleURL.replace("list.jsp", "view.jsp");
							HtmlPage articlePage = (HtmlPage)myClient.getPage(articleURL);
							
							// Get anchors list on article page to get file list.
							List<DomElement> fileList = articlePage.getElementsByTagName("a");
							for (DomElement myFile : fileList){
								String fileHref = myFile.getAttribute("href");
								
								// If anchor's href string start with "../board/FileDownLoad.jsp?",
								// it means the anchor has URL of file too.
								if (fileHref.startsWith("../board/FileDownLoad.jsp?")){
									
									// Get ready to download file(s)
									// by define name and directory to save.
									String fileName = myFile.getTextContent();
									String fileDir = myArticle.getTextContent();
									String fileParentDir = _class.getName().replace(" ", "");
									HtmlAnchor currentFileAnchor = null;
									fileName = fileName.substring(0, fileName.indexOf("(")).trim();
									
									// To get inputstream with anchor clicking,
									// set currentFileAnchor to myAnchor.
									for (HtmlAnchor myAnchor : articlePage.getAnchors()){
										if (myAnchor.getTextContent().contains(fileName)){
											currentFileAnchor = myAnchor;
											break;
										}
									}
									myGUI.addLog("강좌 파일 받는 중 : "+fileDir+" - "+fileName);
									MainSystem.myGUI.addLog("다운로드 중... 0%");
									// Trigger downloading
									FileDownloader.downloadWithURL(currentFileAnchor, fileName, fileParentDir, fileDir);
								}
							}
							//One article end
							FileDownloader.writeArticleLog(articleId);
						}
					}
					
				} catch (FailingHttpStatusCodeException | IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		MainSystem.myGUI.addLog("강좌 파일 모두 받음 : "+_class.getName());
	}
}
