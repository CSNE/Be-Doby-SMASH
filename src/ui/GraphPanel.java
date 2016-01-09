package ui;

import animation.InterpolatableProperty;
import helpers.CurveGenerator;
import interfaces.OnObjectSelectedListener;
import interfaces.OnObjectUpdateListener;
import helpers.GeometricPrimitives;
import managers.ObjectsManager;
import managers.TimeManager;
import shapes.TransformableShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;


public class GraphPanel extends JPanel implements OnObjectSelectedListener, OnObjectUpdateListener,MouseMotionListener, MouseListener {
    public static final int X=1251234;
    public static final int Y=20475;
    public static final int SCALE_X=2745837;
    public static final int SCALE_Y=6754;
    public static final int ROTATION=8582;

    private TimeManager tm;


    private Color backgroundColor = new Color(0, 0, 0, 192);
    private long lastDrawn;
    private int drawRegionX, drawRegionY;

    private double maxFPS = 120.0, currentFPS;
    private boolean active = false;

    private final double closeBtnSize=50;
    private final double timeResolution=0.1;
    private double startTime=0;
    private double endTime=10;


    private Shape closeBtn;
    private java.util.List<Shape> handles=new ArrayList<>();
    GraphTransformer graphTransformer;

    int mode;
    TransformableShape target;
    InterpolatableProperty targetProperty;


    public GraphPanel(TimeManager tm) {
        graphTransformer=new GraphTransformer();
        this.tm=tm;
        setOpaque(false);

    }

    public void activate() {
        active = true;
        addMouseListener(this);
        addMouseMotionListener(this);
        recalculateGraphTransformer();
        repaint();
    }

    public void deactivate() {
        removeMouseListener(this);
        removeMouseMotionListener(this);
        active = false;
    }

    public void setup(TransformableShape target,int mode){
        this.target=target;
        this.mode=mode;
        if (mode==X) targetProperty=target.getKeyFramedX();
        else if (mode==Y) targetProperty=target.getKeyFramedY();
        else if (mode==SCALE_X) targetProperty=target.getKeyFramedScaleX();
        else if (mode==SCALE_Y) targetProperty=target.getKeyFramedScaleY();
        else if (mode==ROTATION) targetProperty=target.getKeyFramedRotation();
        else targetProperty=null;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (active&&target!=null) {

            long currentTime = System.currentTimeMillis();

            drawRegionX = Math.round((float) getSize().getWidth());
            drawRegionY = Math.round((float) getSize().getHeight());
            //System.out.println(drawRegionX+" / "+drawRegionY);

            //Anti-aliasing
            Graphics2D g2d = (Graphics2D) g;
            RenderingHints rh = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHints(rh);

            super.paintComponent(g2d);

            //setBackground(backgroundColor);
            g2d.setColor(backgroundColor);
            g2d.fillRect(0,0,getWidth(),getHeight());



            if (targetProperty!=null){

                double bound=50;

                graphTransformer.setBounds(bound,drawRegionX-bound,bound,drawRegionY-bound);


                //Grid
                g2d.setStroke(new BasicStroke(1));
                g2d.setColor(new Color(255,255,255,128));
                FontMetrics fm=getFontMetrics(getFont());
                double xPer=1,yPer=100;
                int maxIdx, minIdx;
                double currentPos;
                minIdx=(int)Math.round(Math.ceil(graphTransformer.getMinX()/xPer));
                maxIdx=(int)Math.round(Math.floor(graphTransformer.getMaxX()/xPer));
                for (int i = minIdx; i <= maxIdx; i++) {
                    currentPos=graphTransformer.fromGraphSpaceXToDrawSpaceX(xPer*i);
                    g2d.draw(new Line2D.Double(currentPos,bound,currentPos,drawRegionY-bound));
                    g2d.drawString(""+xPer*i,(float)(currentPos+3.0),(float)(drawRegionY-bound-3.0));
                }
                minIdx=(int)Math.ceil(graphTransformer.getMinY()/yPer);
                maxIdx=(int)Math.floor(graphTransformer.getMaxY()/yPer);
                for (int i = minIdx; i <= maxIdx; i++) {
                    currentPos=graphTransformer.fromGraphSpaceYToDrawSpaceY(yPer*i);
                    g2d.draw(new Line2D.Double(bound,currentPos,drawRegionX-bound,currentPos));
                    g2d.drawString(""+yPer*i,(float)(drawRegionX-bound-fm.stringWidth(""+yPer*i)-3.0),(float)(currentPos-3.0));
                }



                //Curve itself
                g2d.setColor(Color.RED);
                g2d.draw(CurveGenerator.generate(graphTransformer.fromGraphSpaceToDrawSpace(targetProperty.getKeyframeValueCurve().getSegments())));

                g2d.setColor(Color.BLUE);
                //Handles
                handles.clear();
                for (int i = 0; i < targetProperty.getNumKeyframes(); i++) {
                    handles.add(new GeometricPrimitives(GeometricPrimitives.SQUARE).scale(0.1).translate(graphTransformer.fromGraphSpaceToDrawSpace(targetProperty.getKeyframe(i).getPoint())).getPath2D());
                    handles.add(new GeometricPrimitives(GeometricPrimitives.SQUARE).scale(0.1).translate(graphTransformer.fromGraphSpaceToDrawSpace(targetProperty.getKeyframe(i).getHandle1())).getPath2D());
                    handles.add(new GeometricPrimitives(GeometricPrimitives.SQUARE).scale(0.1).translate(graphTransformer.fromGraphSpaceToDrawSpace(targetProperty.getKeyframe(i).getHandle2())).getPath2D());
                }
                Point2D.Double point, handle1, handle2;
                for (int i = 0; i < targetProperty.getNumKeyframes(); i++) {
                    point=graphTransformer.fromGraphSpaceToDrawSpace(targetProperty.getKeyframe(i).getPoint());
                    handle1=graphTransformer.fromGraphSpaceToDrawSpace(targetProperty.getKeyframe(i).getHandle1());
                    handle2=graphTransformer.fromGraphSpaceToDrawSpace(targetProperty.getKeyframe(i).getHandle2());
                    g2d.draw(new Line2D.Double(point.getX(),point.getY(),handle1.getX(),handle1.getY()));
                    g2d.draw(new Line2D.Double(point.getX(),point.getY(),handle2.getX(),handle2.getY()));
                }
                for (int i = 0; i < handles.size(); i++) {

                    g2d.fill(handles.get(i));
                }

                //Time Indicator
                g2d.setColor(Color.GREEN);
                double timeX= graphTransformer.fromGraphSpaceToDrawSpace(new Point2D.Double(tm.getCurrentAnimationTime(),0)).getX();
                g2d.draw(new Line2D.Double(timeX,0,timeX,drawRegionY));
            }




            g2d.setColor(new Color(0,0,0));
            closeBtn=new GeometricPrimitives(GeometricPrimitives.SQUARE).scale(closeBtnSize/100.0).translate(drawRegionX-closeBtnSize/2.0,closeBtnSize/2.0).getPath2D();
            g2d.fill(closeBtn);
            g2d.setColor(new Color(255,255,255));
            g2d.fill(new GeometricPrimitives(GeometricPrimitives.SQUARE).scale(closeBtnSize/100.0*0.8,closeBtnSize/100.0*0.2).rotate(45).translate(drawRegionX-closeBtnSize/2.0,closeBtnSize/2.0).getPath2D());
            g2d.fill(new GeometricPrimitives(GeometricPrimitives.SQUARE).scale(closeBtnSize/100.0*0.8,closeBtnSize/100.0*0.2).rotate(-45).translate(drawRegionX-closeBtnSize/2.0,closeBtnSize/2.0).getPath2D());


            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.setColor(new Color(255, 255, 255));
            g2d.drawString(target.getName(), 20, 60);
            g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            g2d.setColor(new Color(255, 255, 255,180));
            g2d.drawString(modeToString(), 20, 80);


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
            g2d.drawString(currentFPS + " fps", 5, 30);

            //System.out.println(Double.toString(1.0/(System.nanoTime()-lastDrawn)*1000000000L));

            lastDrawn = System.nanoTime();

            repaint();
        }
        setBackground(new Color(0,0,0,0));
    }

    private void recalculateGraphTransformer(){

        graphTransformer.reset();

/*
        double currentDrawingTime=startTime;

        while (currentDrawingTime<=endTime){
            graphTransformer.addData(currentDrawingTime,targetProperty.getValueAtTime(currentDrawingTime));
            currentDrawingTime+=timeResolution;
        }*/
        graphTransformer.feedData(targetProperty.getKeyframes());

        graphTransformer.calculate();

    }


    @Override
    public void onSelect(TransformableShape shape) {

    }



    @Override
    public void updateObject() {

    }

    public void enter(TransformableShape shape, int type){
        setup(shape,type);
        activate();
    }

    public String modeToString(){
        if (mode==X) return "X";
        else if (mode==Y) return "Y";
        else if (mode==SCALE_X) return "Scale X";
        else if (mode==SCALE_Y) return "Scale Y";
        else if (mode==ROTATION) return "Rotation";
        else return "?";
    }


    Point lastClickPoint;
    long lastClickTime;
    @Override
    public void mouseClicked(MouseEvent e) {

        if (lastClickPoint!=null){
            if (e.getY()==lastClickPoint.getY() && e.getX()==lastClickPoint.getX()){
                if (System.currentTimeMillis()-lastClickTime<100){
                    System.out.println("Double clicking bug caught.");
                    return;
                }
            }
        }
        lastClickTime=System.currentTimeMillis();
        lastClickPoint=e.getPoint();



        System.out.println("Mouse Clicked."+e.getX()+" | "+e.getY()+" | "+System.currentTimeMillis());

        if (closeBtn.contains(e.getX(),e.getY())){
            deactivate();
        }else{
            for (int i = 0; i < handles.size(); i++) {
                if (i%3!=0) continue;
                if(handles.get(i).contains(e.getX(),e.getY())){
                    int idx=i/3;

                    targetProperty.getKeyframe(idx).toggleHandles();

                    break;
                }
            }
            targetProperty.recalculateCurve();
            recalculateGraphTransformer();
        }
    }

    java.util.List<Shape> handlesBkup;
    int iniX, iniY;
    double dX1,dY1,dX2,dY2;

    @Override
    public void mousePressed(MouseEvent e) {
        handlesBkup=new ArrayList<>(handles);
        iniX=e.getX();
        iniY=e.getY();

        for (int i = 0; i < handlesBkup.size(); i++) { //TODO VERY unelegant solution.
            if (handlesBkup.get(i).contains(iniX, iniY)){
                int idx=i/3;
                if (i%3==0){ //point
                    dX1=targetProperty.getKeyframe(idx).getPoint().getX()-targetProperty.getKeyframe(idx).getHandle1().getX();
                    dY1=targetProperty.getKeyframe(idx).getPoint().getY()-targetProperty.getKeyframe(idx).getHandle1().getY();
                    dX2=targetProperty.getKeyframe(idx).getPoint().getX()-targetProperty.getKeyframe(idx).getHandle2().getX();
                    dY2=targetProperty.getKeyframe(idx).getPoint().getY()-targetProperty.getKeyframe(idx).getHandle2().getY();
                }
                break;
            }
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        recalculateGraphTransformer();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        for (int i = 0; i < handlesBkup.size(); i++) {
            if (handlesBkup.get(i).contains(iniX, iniY)){
                int idx=i/3;
                if (i%3==0){ //point
                    targetProperty.getKeyframe(idx).setPoint(graphTransformer.fromDrawSpaceXToGraphSpaceX(e.getX()),graphTransformer.fromDrawSpaceYToGraphSpaceY(e.getY()));
                    targetProperty.getKeyframe(idx).setHandle1(targetProperty.getKeyframe(idx).getPoint().getX()-dX1,targetProperty.getKeyframe(idx).getPoint().getY()-dY1);
                    targetProperty.getKeyframe(idx).setHandle2(targetProperty.getKeyframe(idx).getPoint().getX()-dX2,targetProperty.getKeyframe(idx).getPoint().getY()-dY2);
                }else if (i%3==1){ //handle 1
                    targetProperty.getKeyframe(idx).setHandle1(graphTransformer.fromDrawSpaceXToGraphSpaceX(e.getX()),graphTransformer.fromDrawSpaceYToGraphSpaceY(e.getY()));
                }else if (i%3==2){ //handle 2
                    targetProperty.getKeyframe(idx).setHandle2(graphTransformer.fromDrawSpaceXToGraphSpaceX(e.getX()),graphTransformer.fromDrawSpaceYToGraphSpaceY(e.getY()));
                }
                break;
            }
        }
        targetProperty.recalculateCurve();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
