package ui;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;


public class Help extends JFrame{
	
	void open(File document) throws IOException {
	    Desktop dt = Desktop.getDesktop();
	    dt.open(document);
	}
	
	public Help(){
		
		try {
			open(new File("media/doc/BDS7.1xManual.html"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
}
