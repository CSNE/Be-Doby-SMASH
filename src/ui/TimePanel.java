package ui;

import animation.Keyframe;
import interfaces.OnModeChangeListener;
import interfaces.OnObjectSelectedListener;
import interfaces.OnObjectUpdateListener;
import interfaces.OnTimeUpdateListener;
import managers.ModeManager;
import managers.TimeManager;
import helpers.GeometricPrimitives;
import shapes.TransformableShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

/**
 * Created by Chan on 2016-01-02.
 */
public class TimePanel extends JPanel implements OnTimeUpdateListener, MouseMotionListener, MouseListener, OnModeChangeListener, OnObjectSelectedListener, OnObjectUpdateListener{
    double drawRegionX,drawRegionY;

    TimeManager tm;
    ModeManager mm;

    Rectangle2D.Double button;

    TransformableShape selected;

    double keyframeSquareSize=5;
    double keyframeYIni=7, keyframeYDelta=7;


    public TimePanel(TimeManager tm, ModeManager mm){
        this.tm=tm;
        this.mm=mm;
        setPreferredSize(new Dimension(500,50));


        addMouseMotionListener(this);
        addMouseListener(this);

    }

    public void paintComponent(Graphics g) {

        drawRegionX = getSize().getWidth();
        drawRegionY = getSize().getHeight();
        //System.out.println(drawRegionX+" / "+drawRegionY);

        //Anti-aliasing
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        super.paintComponent(g2d);








        //Time Tick
        g2d.setColor(Color.BLACK);
        double per=getPixelsPerSecond();
        for (int i = 0; i < tm.getAnimationLength(); i++) {
            g2d.fill(new Rectangle2D.Double(drawRegionY+per*i,drawRegionY/2.0,1,drawRegionY/2.0));
        }


        //Keyframes
        if (selected!=null) {
            g2d.setColor(new Color(255, 235, 113));
            for (Keyframe kf:selected.getKeyFramedX().getKeyframes()){
                g2d.fill(new Rectangle2D.Double(timeToX(kf.getTime())-keyframeSquareSize/2.0,keyframeYIni-keyframeSquareSize/2.0,keyframeSquareSize,keyframeSquareSize));
            }
            g2d.setColor(new Color(232, 138, 104));
            for (Keyframe kf:selected.getKeyFramedY().getKeyframes()){
                g2d.fill(new Rectangle2D.Double(timeToX(kf.getTime())-keyframeSquareSize/2.0,keyframeYIni+keyframeYDelta-keyframeSquareSize/2.0,keyframeSquareSize,keyframeSquareSize));
            }
            g2d.setColor(new Color(219, 127, 255));
            for (Keyframe kf:selected.getKeyFramedScaleX().getKeyframes()){
                g2d.fill(new Rectangle2D.Double(timeToX(kf.getTime())-keyframeSquareSize/2.0,keyframeYIni+keyframeYDelta*2-keyframeSquareSize/2.0,keyframeSquareSize,keyframeSquareSize));
            }
            g2d.setColor(new Color(104, 179, 232));
            for (Keyframe kf:selected.getKeyFramedScaleY().getKeyframes()){
                g2d.fill(new Rectangle2D.Double(timeToX(kf.getTime())-keyframeSquareSize/2.0,keyframeYIni+keyframeYDelta*3-keyframeSquareSize/2.0,keyframeSquareSize,keyframeSquareSize));
            }
            g2d.setColor(new Color(114, 255, 123));
            for (Keyframe kf:selected.getKeyFramedRotation().getKeyframes()){
                g2d.fill(new Rectangle2D.Double(timeToX(kf.getTime())-keyframeSquareSize/2.0,keyframeYIni+keyframeYDelta*4-keyframeSquareSize/2.0,keyframeSquareSize,keyframeSquareSize));
            }
        }


        //Current Time Indicator
        g2d.setColor(Color.RED);

        g2d.fill(new Rectangle2D.Double(timeToX(tm.getCurrentAnimationTime())-1,0,2,drawRegionY));




        //Buttons
        button=new Rectangle2D.Double(0,0,drawRegionY,drawRegionY);


        g2d.setColor(Color.BLACK);
        g2d.fill(button);
        g2d.setColor(Color.WHITE);
        if (!tm.isPlaying()) g2d.fill(new GeometricPrimitives(GeometricPrimitives.TRIANGLE).scale(0.3).rotate(90).translate(drawRegionY/2.0,drawRegionY/2.0).getPath2D());
        else {
            g2d.fill(new GeometricPrimitives(GeometricPrimitives.SQUARE).scale(0.1,0.3).translate(drawRegionY/2.0-10,drawRegionY/2.0).getPath2D());
            g2d.fill(new GeometricPrimitives(GeometricPrimitives.SQUARE).scale(0.1,0.3).translate(drawRegionY/2.0+10,drawRegionY/2.0).getPath2D());
        }



        setBackground(Color.lightGray);


    }

    private double getTimeBarWidth(){
        return drawRegionX-drawRegionY; //this may change in the future, hence the method.
    }

    private double timeToX(double time){
        return time/tm.getAnimationLength()*getTimeBarWidth()+drawRegionY;
    }
    private double getPixelsPerSecond(){
        return getTimeBarWidth()/tm.getAnimationLength();
    }

    private double coordinatesToTime(MouseEvent e){
        return coordinatesToTime(e.getX(),e.getY());
    }

    private double coordinatesToTime(int x, int y){
        if (x>drawRegionY){
            return (x-drawRegionY)/getTimeBarWidth()*tm.getAnimationLength();
        }
        else return 0.0;
    }


    @Override
    public void updateTime() {
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (button.contains(e.getX(),e.getY())){
            tm.togglePlayState();
        }else{
            tm.setTime(coordinatesToTime(e));
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (button.contains(e.getX(),e.getY())){
            //pass
        }else{
            tm.setTime(coordinatesToTime(e));
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void modeChanged(int newMode) {
        if(mm.higherThanOET(ModeManager.INTERMEDIATE)){
            setVisible(true);
        }else{
            setVisible(false);
        }
    }

    @Override
    public void onSelect(TransformableShape shape) {
        this.selected=shape;
    }

    @Override
    public void updateObject() {
        repaint();
    }
}
