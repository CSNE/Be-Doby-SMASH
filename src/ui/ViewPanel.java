package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import managers.ModeManager;
import managers.ObjectsManager;
import managers.TimeManager;

public class ViewPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener,KeyListener{
	
	Color backgroundColor=Color.BLACK;
	long lastDrawn;
	int drawRegionX,drawRegionY;
	private ObjectsManager om;
	int offsetX=0,offsetY=0;
	double maxFPS=60.0, currentFPS;
	boolean renderMode=false;
	TimeManager tm;
	ModeManager mm;
	
	public ViewPanel(ObjectsManager om, TimeManager tm, ModeManager mm){
		this.om=om;
		this.tm=tm;
		this.mm=mm;
		
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
		addKeyListener(this);
	}
	
	public void enterRenderMode(){this.renderMode=true;}
	public void exitRenderMode(){this.renderMode=false;}

	public void paintComponent(Graphics g){

		long currentTime=System.currentTimeMillis();

		drawRegionX=Math.round((float)getSize().getWidth());
		drawRegionY=Math.round((float)getSize().getHeight());
		//System.out.println(drawRegionX+" / "+drawRegionY);

		//Anti-aliasing
		Graphics2D g2d = (Graphics2D)g;
		RenderingHints rh = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHints(rh);

		//if (!renderMode)
		super.paintComponent(g2d);

		g2d.setFont(new Font("Arial", Font.PLAIN, 20));

		
		tm.updateTime();

		//if (tm.isPlaying()) om.updateObjectsToTime(tm.getCurrentAnimationTime());


		if (om.getMode()==ObjectsManager.VERTEX_EDITING){
			om.getIveTarget().drawStroke(g2d,offsetX,offsetY);
			om.getIveTarget().fillVertexHandles(g2d,offsetX,offsetY);
			om.getIveTarget().drawAnchor(g2d,offsetX,offsetY);
		}else if(om.getMode()==ObjectsManager.NORMAL_EDITING || om.getMode()==ObjectsManager.NOTHING_SELECTED) {

			for (int order = -10; order <=10 ; order++) {
				for (int i = 0; i < om.getNumOfObjects(); i++) {
					if (om.getObject(i).getRenderOrder()==order) om.getObject(i).drawFill(g2d, offsetX, offsetY);
				}
			}


			for (int i = 0; i < om.getNumOfObjects(); i++) {
				if (om.getObject(i).isSelected()) {
					om.getObject(i).drawStroke(g2d,offsetX,offsetY,new Color(255,0,0,(int)(255-(System.currentTimeMillis()/4)%256)));
					om.getObject(i).drawHandles(g2d, offsetX, offsetY, currentTime);
				}
			}
		}else if (om.getMode()==ObjectsManager.FREE_FORM_EDITING){
			//System.out.println("draw");
			om.getFfTarget().drawStroke(g2d,offsetX,offsetY);
			om.getFfTarget().fillVertexHandles(g2d,offsetX,offsetY);
		}


		//if (renderMode) setBackground(new Color(0,0,0,0));

		setBackground(om.getCanvasColor());
		if (!renderMode) {
			//Time
			if (mm.higherThanOET(ModeManager.INTERMEDIATE)) {
				g2d.setColor(Color.WHITE);
				g2d.drawString(String.format("%.3f", tm.getCurrentAnimationTime()) + " Sec", 10, drawRegionY - 10);
			}


			//FPS counter
			g2d.setFont(new Font("Arial", Font.PLAIN, 10));
			g2d.setColor(new Color(255, 255, 255));

			currentFPS = Math.round(10 * 1.0 / (System.nanoTime() - lastDrawn) * 1000000000L) / 10.0;

			while (currentFPS > maxFPS) {
				currentFPS = Math.round(10 * 1.0 / (System.nanoTime() - lastDrawn) * 1000000000L) / 10.0;
				try {
					Thread.sleep(1); //TODO this is not the most elegant solution.
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			g2d.drawString(currentFPS + " fps", 5, 15);


		}
		lastDrawn=System.nanoTime();
		
		repaint();
	}



	@Override
	public void keyPressed(KeyEvent e) {

		
	}



	@Override
	public void keyReleased(KeyEvent e) {

		
	}



	@Override
	public void keyTyped(KeyEvent e) {

		
	}



	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		
	}



	@Override
	public void mouseClicked(MouseEvent e) {
		if (om.getMode()==ObjectsManager.NOTHING_SELECTED||om.getMode()==ObjectsManager.NORMAL_EDITING) om.selectObject(e.getX(),e.getY());
		else if (om.getMode()==ObjectsManager.FREE_FORM_EDITING){
			om.getFfTarget().addPoint(e.getX(),e.getY());
		}
		
	}



	@Override
	public void mouseEntered(MouseEvent arg0) {

		
	}



	@Override
	public void mouseExited(MouseEvent arg0) {

		
	}



	@Override
	public void mousePressed(MouseEvent e) {
		if (om.getMode()==ObjectsManager.VERTEX_EDITING) {
			om.getIveTarget().saveIVEInitialMousePosition(e.getX(),e.getY());
			om.getIveTarget().generateVertexHandles();
		}
		else {
			if (om.activeObject() != null) {
				om.activeObject().saveMouseInitialPosition(e.getX(), e.getY());
				om.notifyOnObjectUpdateListeners();
			}
		}
	}



	@Override
	public void mouseReleased(MouseEvent arg0) {

		
	}



	@Override
	public void mouseDragged(MouseEvent e) {
		if (om.getMode()==ObjectsManager.VERTEX_EDITING){
			om.getIveTarget().updateIVEMousePosition(e.getX(),e.getY());
			om.getIveTarget().generateVertexHandles();
		}else if(om.getMode()==ObjectsManager.NORMAL_EDITING) {
			if (om.activeObject() != null) {
				om.activeObject().updateMouseCurrentPosition(e.getX(), e.getY());
				om.notifyOnObjectUpdateListeners();
			}
		}
	}



	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}