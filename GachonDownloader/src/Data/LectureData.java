package Data;

import java.util.ArrayList;
import java.util.List;

public class LectureData {
	private String lectureName = "";
	private String LectureID = "";
	private String eclassURL = "";
	private String lectureURL = "";
	List<String> fileList = new ArrayList<String>();
	
	public String getLectureName() {
		return lectureName;
	}
	public void setLectureName(String lectureName) {
		this.lectureName = lectureName;
	}
	public String getLectureID() {
		return LectureID;
	}
	public void setLectureID(String lectureID) {
		LectureID = lectureID;
	}
	public String getLectureURL() {
		return lectureURL;
	}
	public void setLectureURL(String lectureURL) {
		this.lectureURL = lectureURL;
	}
	public String getEclassURL() {
		return eclassURL;
	}
	public void setEclassURL(String eclassURL) {
		this.eclassURL = eclassURL;
	}
	public List<String> getFileList(){
		return fileList;
	}
	public void addFileList(String path){
		if (!fileList.contains(path)){
			fileList.add(path);
		}
	}
}
