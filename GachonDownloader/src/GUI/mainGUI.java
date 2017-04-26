package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.text.BadLocationException;

import FileProcess.FileDownloader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import System.MainSystem;

import javax.swing.JTextArea;

import java.awt.Desktop;
import java.awt.Font;

@SuppressWarnings("serial")
public class mainGUI extends JFrame {
	private MainSystem user;
	private int lastLineFirstIdx=0, lastLineLastIdx=0;
	
	private JFileChooser jfc = new JFileChooser();
	private JPanel contentPane;
	private JTextField IDTxt;
	private JPasswordField PWTxt;
	private JButton DownloadBtn;
	private JScrollPane statusScroll;
	private JTextArea statusTxtLog =  new JTextArea();
	private JLabel IDLbl, PWLbl;
	private JButton CancelBtn;
	private JButton pathSetBtn;
	private JButton openPathBtn;
	/**
	 * Create the frame.
	 */
	public mainGUI() {
		setTitle("Eclass Downloader - version 0.1 - By. Dev.Fallingstar");
		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 691, 399);
		setVisible(true);
		
		IDTxt = new JTextField();
		PWTxt = new JPasswordField();
		IDLbl = new JLabel("\uC544\uC774\uB514 :");
		PWLbl = new JLabel("\uBE44\uBC00\uBC88\uD638 :");
		DownloadBtn = new JButton("\uB2E4\uC6B4\uB85C\uB4DC");
		pathSetBtn = new JButton("\uACBD\uB85C \uC124\uC815");
		CancelBtn = new JButton("\uCDE8\uC18C");
		openPathBtn = new JButton("\uACBD\uB85C \uC5F4\uAE30");
		statusScroll = new JScrollPane(statusTxtLog);
		
		IDTxt.setBounds(78, 10, 145, 21);
		IDTxt.setColumns(10);
		contentPane.add(IDTxt);
		PWTxt.setBounds(78, 52, 145, 21);
		PWTxt.setColumns(10);
		contentPane.add(PWTxt);
		IDLbl.setBounds(12, 13, 57, 15);
		contentPane.add(IDLbl);
		PWLbl.setBounds(12, 55, 57, 15);
		contentPane.add(PWLbl);
		DownloadBtn.setBounds(235, 10, 97, 62);
		contentPane.add(DownloadBtn);
		CancelBtn.setBounds(344, 9, 97, 62);
		CancelBtn.setEnabled(false);
		contentPane.add(CancelBtn);
		pathSetBtn.setBounds(453, 9, 97, 30);
		contentPane.add(pathSetBtn);
		openPathBtn.setBounds(453, 41, 97, 30);
		contentPane.add(openPathBtn);
		statusTxtLog.setEditable(false);
		statusTxtLog.setLineWrap(true);
		statusTxtLog.setFont(new Font("Monospaced", Font.PLAIN, 11));
		statusScroll.setBounds(12, 106, 651, 244);
		contentPane.add(statusScroll);
		
		
		
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setMultiSelectionEnabled(false);
		
		MyActionListener btnListener = new MyActionListener();
		DownloadBtn.addActionListener(btnListener);
		CancelBtn.addActionListener(btnListener);
		pathSetBtn.addActionListener(btnListener);
		openPathBtn.addActionListener(btnListener);
		
		this.update(this.getGraphics());
		IDTxt.requestFocus();
	}
	
	private class MyActionListener implements ActionListener{
		@SuppressWarnings("deprecation")
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton clicked = (JButton)e.getSource();
			
			if(clicked.equals(DownloadBtn)){
				clearLog();
				user = new MainSystem(IDTxt.getText(), new String(PWTxt.getPassword()));
				user.start();
				
				setEnableCancelWidgets();
			}else if(clicked.equals(CancelBtn)){
				user.stop();
				addLog("다운로드가 취소되었습니다!");
				setEnableDownloadWidgets();
			}else if(clicked.equals(pathSetBtn)){
				if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
					String path = jfc.getSelectedFile().getAbsolutePath();
					FileDownloader.setRootPath(path);
				}
			}else if(clicked.equals(openPathBtn)){
				try {
					Desktop.getDesktop().open(new File(FileDownloader.getRootPathFromLog()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	public void setEnableDownloadWidgets(){
		IDTxt.setEnabled(true);
		PWTxt.setEnabled(true);
		DownloadBtn.setEnabled(true);
		CancelBtn.setEnabled(false);
	}
	public void setEnableCancelWidgets(){
		IDTxt.setEnabled(false);
		PWTxt.setEnabled(false);
		DownloadBtn.setEnabled(false);
		CancelBtn.setEnabled(true);
	}
	@SuppressWarnings("unused")
	private void alertWithErrorCode(int code){
		if (code >= 400 && code <= 499){
			JOptionPane.showMessageDialog(this, "네트워크 연결을 확인해주세요!", "연결 오류!", 0);
		}
	}
	private void clearLog(){
		statusTxtLog.setText("");
		lastLineFirstIdx = 0;
		lastLineLastIdx = 0;
	}
	public void replaceLog(String log) throws BadLocationException{
		statusTxtLog.getDocument().remove(lastLineFirstIdx, lastLineLastIdx-lastLineFirstIdx);

		lastLineFirstIdx = (lastLineLastIdx)-(lastLineLastIdx-lastLineFirstIdx);
		statusTxtLog.append(log+"\n");
		statusTxtLog.setCaretPosition(statusTxtLog.getText().length());
		statusTxtLog.requestFocus();
		lastLineLastIdx = statusTxtLog.getCaretPosition();
	}
	
	public void addLog(String log){
		lastLineFirstIdx = statusTxtLog.getCaretPosition();
		
		statusTxtLog.append(log+"\n");
		statusTxtLog.setCaretPosition(statusTxtLog.getText().length());
		
		lastLineLastIdx = statusTxtLog.getCaretPosition();
	}
}
