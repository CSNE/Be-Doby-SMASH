package animation;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chan on 1/6/2016.
 */
public class KeyframedValueCurve implements Serializable{

    static final long serialVersionUID = 2L;

    ArrayList<Keyframe> keyframes=new ArrayList<>();
    ArrayList<Point2D.Double> segments=new ArrayList<>();
    int iterations=100;
    public KeyframedValueCurve(){
        recalculate();
    }
    public ArrayList<Keyframe> getKeyframes(){
        return this.keyframes;
    }
    public KeyframedValueCurve(KeyframedValueCurve kvc){
        this.keyframes=new ArrayList<>(kvc.keyframes);
        this.segments=new ArrayList<>(kvc.segments);
        this.iterations=kvc.iterations;
    }
    public void recalculate(){
        Keyframe next,prev;
        segments.clear();
        for (int i = 0; i < keyframes.size()-1; i++) {
            next=keyframes.get(i+1);
            prev=keyframes.get(i);
            solveBezier(prev.getPoint(),prev.getHandle2(),next.getHandle1(),next.getPoint(),segments,i==(keyframes.size()-1));

        }
    }
    private void solveBezier(Point2D.Double p1,Point2D.Double p2,Point2D.Double p3,Point2D.Double p4,List<Point2D.Double> appendTo, boolean appendLast){
        double x,y,t;
        for (int i = 0; i < (appendLast?iterations:(iterations+1)); i++) {
            t=i/(double)iterations;
            x=p1.getX()*(1-t)*(1-t)*(1-t)
              +p2.getX()*3*(1-t)*(1-t)*t
              +p3.getX()*3*(1-t)*t*t
              +p4.getX()*t*t*t;
            y=p1.getY()*(1-t)*(1-t)*(1-t)
                    +p2.getY()*3*(1-t)*(1-t)*t
                    +p3.getY()*3*(1-t)*t*t
                    +p4.getY()*t*t*t;
            appendTo.add(new Point2D.Double(x,y));
        }
    }
    public double getValueAtTime(double time, double defaultValue){
        Point2D.Double prev,next;
        if (segments.size()<2) return defaultValue;
        if (time<=segments.get(0).getX()) return segments.get(0).getY();
        else if (time>=segments.get(segments.size()-1).getX()) return segments.get(segments.size()-1).getY();
        for (int i = 0; i < segments.size()-1; i++) {
            prev=segments.get(i);
            next=segments.get(i+1);
            if (prev.getX()<=time && next.getX()>=time){
                return prev.getY()+(next.getY()-prev.getY())*(time-prev.getX())/(next.getX()-prev.getX());
            }
        }
        return 0;//TODO catch
    }
    public List<Point2D.Double> getSegments(){
        return this.segments;
    }

}

