package Data;

import java.util.ArrayList;
import java.util.List;

// Class for ClassData
public class ClassData {
	private String lectureName = "";
	private String LectureID = "";
	private String eclassURL = "";
	private String lectureURL = "";
	private List<String> fileList = new ArrayList<String>();
	
	public String getName() {
		return lectureName;
	}
	public void setName(String lectureName) {
		this.lectureName = lectureName;
	}
	public String getID() {
		return LectureID;
	}
	public void setID(String lectureID) {
		LectureID = lectureID;
	}
	public String getLectureURL() {
		return lectureURL;
	}
	public void setLectureURL(String lectureURL) {
		this.lectureURL = lectureURL;
	}
	public String getURL() {
		return eclassURL;
	}
	public void setURL(String eclassURL) {
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
