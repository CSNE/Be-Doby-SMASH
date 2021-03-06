package ui;
import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

public class SplashWindow extends JWindow
{
	//Splash Window Class for displaying the gif while initializing
	public SplashWindow(String filename, int msDelay, Frame f)
	{
		super(f);
		JLabel l = new JLabel(new ImageIcon(filename));
		//l.setOpaque(false);
		//l.setBackground(new Color(0,0,0,0));
		setBackground(new Color(0,0,0,0));
		getContentPane().add(l, BorderLayout.CENTER);
		pack();
		Dimension screenSize =
				Toolkit.getDefaultToolkit().getScreenSize();
		Dimension labelSize = l.getPreferredSize();
		setLocation(screenSize.width/2 - (labelSize.width/2),
				screenSize.height/2 - (labelSize.height/2));
		setVisible(true);

		screenSize = null;
		labelSize = null;

		try {
			Thread.sleep(msDelay);        
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		
		setVisible(false);
		dispose();
	}
}