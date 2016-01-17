package ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import animation.Keyframe;
import managers.ModeManager;
import managers.ObjectsManager;
import managers.TimeManager;
import shapes.TransformableShape;





/*
 * TODO
 * 
 * Export to image
 * Graph Editor
 * Parenting Bug Fix
 * Move Anchor Bug Fix
 * int timing to float timings
 * Restructure UI
 * Object Properties Panel
 * 
 */

public class MainClass extends JFrame implements ActionListener{

	boolean hideSplash=false;

	ViewPanel vp;
	JLayeredPane lp;
	GraphPanel gp;

	int screenX, screenY;

	int windowX=1000, windowY=650;




	JMenuBar topBar;
	JMenu menuTop_File,menuTop_Animation,menuTop_View,menuTop_Help,menuTop_Mode;
	JMenuItem[] menu_File=new JMenuItem[6];
	JMenuItem[] menu_Animation=new JMenuItem[5];
	JMenuItem[] menu_Help=new JMenuItem[5];
	JMenuItem[] menu_View=new JMenuItem[6];
	JMenuItem[] menu_Mode=new JMenuItem[4];

	
	ImageIcon logoImage;

	
	ObjectPanel objectPanel;
	TimePanel tp;
	
	ObjectsManager om;
	TimeManager tm;
	ModeManager mm;

	public static void main(String[] args) {

		new MainClass();

	}




	public MainClass(){



		//We get the screen size.
		Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
		screenX=screenSize.width;
		screenY=screenSize.height;

		//We set the random time for the loading splash.
		Random rGen=new Random();
		int loadTime=rGen.nextInt(1000);
		System.out.println("LoadTime: "+loadTime);

		if (!hideSplash) new SplashWindow("media/splash/splash_r3.png",2000+loadTime,this);


		logoImage = new ImageIcon("media/logo/new_r1_256px.png");



		//Add top menu bar
		topBar=new JMenuBar();


		menuTop_File=new JMenu("File");


		menu_File[3] = new JMenuItem("New Project");
		menu_File[3].setToolTipText("Erase everything and start over.");
		menu_File[3].addActionListener(this);
		menuTop_File.add(menu_File[3]);

		menu_File[0] = new JMenuItem("Open Project");
		menu_File[0].setToolTipText("Open a SMash Animation file(.sma).");
		menu_File[0].addActionListener(this);
		menuTop_File.add(menu_File[0]);

		menu_File[1] = new JMenuItem("Save Project");
		menu_File[1].setToolTipText("Save a SMash Animation file(.sma).");
		menu_File[1].addActionListener(this);
		menuTop_File.add(menu_File[1]);

		menu_File[4] = new JMenuItem("Export Photo");
		menu_File[4].setToolTipText("Export to an image.");
		menu_File[4].addActionListener(this);
		menuTop_File.add(menu_File[4]);

		menu_File[5] = new JMenuItem("Export Movie");
		menu_File[5].setToolTipText("Export to a playable movie.");
		menu_File[5].addActionListener(this);
		menuTop_File.add(menu_File[5]);

		menu_File[2] = new JMenuItem("Exit");
		menu_File[2].setToolTipText("Exit Be Doby SMASH");
		menu_File[2].addActionListener(this);
		menuTop_File.add(menu_File[2]);



		menuTop_Mode=new JMenu("Mode");

		menu_Mode[0] = new JMenuItem("Beginner");
		menu_Mode[0].setToolTipText("Basic object manipulation only.");
		menu_Mode[0].addActionListener(this);
		menuTop_Mode.add(menu_Mode[0]);

		menu_Mode[1] = new JMenuItem("Intermediate");
		menu_Mode[1].setToolTipText("Added access to animation features.");
		menu_Mode[1].addActionListener(this);
		menuTop_Mode.add(menu_Mode[1]);

		menu_Mode[2] = new JMenuItem("Advanced");
		menu_Mode[2].setToolTipText("Added access tp advanced object manipulation features.");
		menu_Mode[2].addActionListener(this);
		menuTop_Mode.add(menu_Mode[2]);

		menu_Mode[3] = new JMenuItem("Expert");
		menu_Mode[3].setToolTipText("Access to every feature of Be Doby SMASH CS8.");
		menu_Mode[3].addActionListener(this);
		menuTop_Mode.add(menu_Mode[3]);



		menuTop_Animation=new JMenu("Animation");
		
		/*

		easingSelection=new JMenu("Save Transformation");
		for(int i=0;i<easingStrings.length;i++){
			easingMenus[i] = new JMenuItem(easingStrings[i]);
			easingMenus[i].addActionListener(this);
			easingSelection.add(easingMenus[i]);
		}
		menuTop_Animation.add(easingSelection);*/

		/*
		menu_Animation[0] = new JMenuItem("Save Transformation");
		menu_Animation[0].setToolTipText("Insert a keyframe int the current frame.");
		menu_Animation[0].addActionListener(this);
		menu_Top[1].add(menu_Animation[0]);*/
/*
		menu_Animation[1] = new JMenuItem("Clear Transformation");
		menu_Animation[1].setToolTipText("Clear all transformations.");
		menu_Animation[1].addActionListener(this);
		menuTop_Animation.add(menu_Animation[1]);
*/
		menu_Animation[2] = new JMenuItem("Set Length");
		menu_Animation[2].setToolTipText("Set Animation Length");
		menu_Animation[2].addActionListener(this);
		menuTop_Animation.add(menu_Animation[2]);


		menuTop_Help=new JMenu("Help");

		menu_Help[0] = new JMenuItem("Show Help");
		menu_Help[0].setToolTipText("Need Help? (Open help file in external browser)");
		menu_Help[0].addActionListener(this);
		menuTop_Help.add(menu_Help[0]);

/*
		menuTop_View=new JMenu("View");

		menu_View[0] = new JMenuItem("Reset View");
		menu_View[0].setToolTipText("Reset viewport to origin.");
		menu_View[0].addActionListener(this);
		menuTop_View.add(menu_View[0]);

		menu_View[1] = new JMenuItem("Show Trails for selected");
		menu_View[1].setToolTipText("Display object motion trails.");
		menu_View[1].addActionListener(this);
		menuTop_View.add(menu_View[1]);

		menu_View[4] = new JMenuItem("Show Trails for all");
		menu_View[4].setToolTipText("Display object motion trails.");
		menu_View[4].addActionListener(this);
		menuTop_View.add(menu_View[4]);

		menu_View[2] = new JMenuItem("Cache Trails");
		menu_View[2].setToolTipText("Display object motion trails. Uses Caching.");
		menu_View[2].addActionListener(this);
		menuTop_View.add(menu_View[2]);

		menu_View[5] = new JMenuItem("Clear Cache");
		menu_View[5].setToolTipText("Clear cached trails");
		menu_View[5].addActionListener(this);
		menuTop_View.add(menu_View[5]);

		menu_View[3] = new JMenuItem("Hide Trails");
		menu_View[3].setToolTipText("Do not display object motion trails.");
		menu_View[3].addActionListener(this);
		menuTop_View.add(menu_View[3]);
*/

		topBar.add(menuTop_File);
		topBar.add(menuTop_Mode);
		topBar.add(menuTop_Animation);
		//topBar.add(menuTop_View);
		//topBar.add(menuTop_Help);

		setJMenuBar(topBar);




		setLayout(new BorderLayout());

		//Adding frame
		lp=new FilledLayeredPane();
		//lp=getLayeredPane();
		lp.setPreferredSize(new Dimension(300,300));
		lp.setBackground(Color.GREEN);

		vp = new ViewPanel(ObjectsManager.getInstance(),TimeManager.getInstance(),ModeManager.getInstance());
		//vp.setBounds(0,0,300,300);

		lp.add(vp,JLayeredPane.DEFAULT_LAYER);


		gp=new GraphPanel(TimeManager.getInstance(),ObjectsManager.getInstance());
		lp.add(gp,JLayeredPane.MODAL_LAYER);




		add(lp,BorderLayout.CENTER);



		
		
		objectPanel=new ObjectPanel(ObjectsManager.getInstance(),TimeManager.getInstance(),ModeManager.getInstance());
		add(objectPanel,BorderLayout.LINE_END);


		tp=new TimePanel(TimeManager.getInstance(),ModeManager.getInstance());
		add(tp,BorderLayout.PAGE_END);
		


		//Misc window setup
		setTitle("Be Doby SMASH CS8+");
		setSize(windowX,windowY);
		setLocation(screenX/2-windowX/2,screenY/2-windowY/2);
		setVisible(true);
		setIconImage(logoImage.getImage());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		
		om=ObjectsManager.getInstance();
		tm=TimeManager.getInstance();
		mm=ModeManager.getInstance();


		//cross-object references
		om.addOnObjectSelectedListener(objectPanel);
		om.addOnObjectUpdateListener(objectPanel);
		om.addOnObjectSelectedListener(tp);
		om.addOnObjectUpdateListener(tp);

		tm.addOnTimeUpdateListener(tp);
		tm.addOnTimeUpdateListener(om);

		mm.addOnModeChangeListener(objectPanel);
		mm.addOnModeChangeListener(tp);

		objectPanel.setGraphPanel(gp);



/*
		TransformableShape shape=om.createShape();
		shape.makeSquare(100, 100);
		shape.transform(100, 100, 1, 1, 0);
		shape.getKeyFramedX().addKeyframe(new Keyframe().setPoint(0,0).setHandle2(1,1000));
		shape.getKeyFramedX().addKeyframe(new Keyframe().setPoint(3,500).setHandle1(1.5,500).setHandle2(6,500));
		shape.getKeyFramedX().addKeyframe(new Keyframe().setPoint(6,200).setHandle1(3,0).setHandle2(7,-200));
		shape.getKeyFramedX().addKeyframe(new Keyframe().setPoint(9,500).setHandle1(8,0));
		om.selectObject(shape);


		TransformableShape shape2=om.createShape();
		shape2.makeSquare(100, 100);
		shape2.transform(200, 200, 1, 1, 0);
		shape2.setParent(shape);
		//start animation
*/

		om.notifyOnObjectSelectedListeners(null);



		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


	}

	public BufferedImage getScreenShot(Component component) {

		BufferedImage image = new BufferedImage(
				component.getWidth(),
				component.getHeight(),
				BufferedImage.TYPE_INT_RGB
		);
		// call the Component's paint method, using
		// the Graphics object of the image.
		component.paint( image.getGraphics() ); // alternately use .printAll(..)
		return image;
	}
	public void savePNG(BufferedImage bi, File file){
		File outputfile = file;
		try {
			ImageIO.write(bi, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void savePNGSequence(double end, double framerate){
		//TODO Render files cleaning
		//for(File file: new File("media/renders").listFiles()) file.delete();

		om.deselectAll();
		String frameIdentifier;
		for (int i = 0; i*(1/framerate) <= end; i++) {
			tm.setTime(i*(1/framerate));
			frameIdentifier=""+i;
			while (frameIdentifier.length()<4) frameIdentifier="0"+frameIdentifier;
			savePNG(getScreenShot(vp),new File("media/renders/"+frameIdentifier+".png"));
		}
	}

	public void saveMP4(File file, int framerate) throws IOException, InterruptedException {

		String ffmpeg="\""+System.getProperty("user.dir")+"/media/ffmpeg"+"\"";
		String images="\""+System.getProperty("user.dir")+"/media/renders/%04d.png"+"\"";
		String out=file.getAbsolutePath();
		String command="cmd /c start \"Movie Export (ffmpeg)\" "+ffmpeg+" -framerate "+framerate+" -i "+images+" -c:v libx264 -pix_fmt yuv420p "+out;
		//String command="ping www.google.com";
		//String command=ffmpeg+" -h";

		System.out.println(command);
		Runtime.getRuntime().exec(command);
	}

	public void exportImage(){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("media/exported/"));
		fileChooser.setSelectedFile(new File("Untitled.png"));
		fileChooser.setDialogTitle("Export PNG Screenshot");
		fileChooser.setApproveButtonText("Export");
		int result = fileChooser.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			String file_name = selectedFile.toString();

			if (!file_name.endsWith(".png")) file_name += ".png";
			selectedFile = new File(file_name);

			System.out.println("Selected file: " + selectedFile.getAbsolutePath());
			vp.enterRenderMode();
			savePNG(getScreenShot(vp),selectedFile);
			vp.exitRenderMode();
		}
	}

	public void exportMovie(){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("media/exported/"));
		fileChooser.setSelectedFile(new File("Untitled.mp4"));
		fileChooser.setDialogTitle("Export MP4 Movie");
		fileChooser.setApproveButtonText("Export");
		int result = fileChooser.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			String file_name = selectedFile.toString();

			if (!file_name.endsWith(".mp4")) file_name += ".mp4";
			selectedFile = new File(file_name);

			System.out.println("Selected file: " + selectedFile.getAbsolutePath());

			try {
				JOptionPane.showMessageDialog(null, "This may take a while.");
				vp.enterRenderMode();
				savePNGSequence(tm.getAnimationLength(),30);
				vp.exitRenderMode();
				saveMP4(selectedFile,30);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource().equals(menu_File[1])){ //Button Pressed

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("media/saves/"));
			fileChooser.setSelectedFile(new File("Untitled.sma"));
			fileChooser.setDialogTitle("Save file");
			fileChooser.setApproveButtonText("Save");
			int result = fileChooser.showSaveDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				String file_name = selectedFile.toString();

				if (!file_name.endsWith(".sma"))
					file_name += ".sma";
				selectedFile = new File(file_name);

				System.out.println("Selected file: " + selectedFile.getAbsolutePath());

				om.saveShapesToFile(selectedFile);
			}



		}
		else if (e.getSource().equals(menu_File[0])){ //Button Pressed
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("media/saves/"));
			fileChooser.setDialogTitle("Open file");

			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				String file_name = selectedFile.toString();

				if (!file_name.endsWith(".sma")) file_name += ".sma";
				selectedFile = new File(file_name);

				System.out.println("Selected file: " + selectedFile.getAbsolutePath());

				om.loadShapesFromFile(selectedFile);
			}

		}
	
		else if (e.getSource().equals(menu_Help[0])){ //Button Pressed
			new Help();
		}
		else if (e.getSource().equals(menu_File[2])){ //Button Pressed
			System.exit(0);
		}
		else if (e.getSource().equals(menu_View[1])){

		}
		else if (e.getSource().equals(menu_View[2])){


			//wd.close();
		}
		else if (e.getSource().equals(menu_View[3])){

		}
		else if (e.getSource().equals(menu_View[4])){

		}
		else if (e.getSource().equals(menu_View[5])){

		}
		else if (e.getSource().equals(menu_File[3])){
			om.clearShapes();

		}
		else if (e.getSource().equals(menu_File[4])){
			exportImage();

		}
		else if (e.getSource().equals(menu_File[5])){
			exportMovie();
		}
		else if (e.getSource().equals(menu_Animation[2])){
			String s = (String)JOptionPane.showInputDialog(
					this,
					"Set Animation Length in Seconds.",
					"Set Animation Length",
					JOptionPane.PLAIN_MESSAGE,
					null,
					null,
					Double.toString(tm.getAnimationLength()));

			//If a string was returned
			if ((s != null) && (s.length() > 0)) {
				try {
					tm.setAnimationLength(Double.parseDouble(s));
				}catch(Exception asdf){}
			}
		}else if (e.getSource().equals(menu_Mode[0])){
			mm.setMode(ModeManager.BEGINNER);
		}else if (e.getSource().equals(menu_Mode[1])){
			mm.setMode(ModeManager.INTERMEDIATE);
		}else if (e.getSource().equals(menu_Mode[2])){
			mm.setMode(ModeManager.ADVANCED);
		}else if (e.getSource().equals(menu_Mode[3])){
			mm.setMode(ModeManager.EXPERT);
		}
		else{
			System.out.println("Unhandled Action."+e.getSource());
		}
	}


}
