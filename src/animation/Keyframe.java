package animation;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Created by Chan on 1/5/2016.
 */
public class Keyframe implements Comparable<Keyframe>, Serializable{
    private double time,value,handle1T,handle1V,handle2T,handle2V;

    private boolean handlesActivation=true;

    public void toggleHandles(){
        System.out.println("Toggling handles."+" | "+time+" | "+value+" | "+handle1T+" | "+handle1V+" | "+handle2T+" | "+handle2V+" | "+handlesActivation);
        if(isHandlesActivated()) shrinkHandles();
        else expandHandles();
    }
    public void expandHandles(){
        handlesActivation=true;
        setHandle1(time-1,value);
        setHandle2(time+1,value);
    }
    public void shrinkHandles(){
        handlesActivation=false;
        setHandle1(time,value);
        setHandle2(time,value);
    }
    public boolean isHandlesActivated(){
        return handlesActivation;
    }
    public Keyframe(double time, double value){
        setPoint(time,value);
        shrinkHandles();
    }
    public Keyframe setPoint(double time, double value){
        this.time=time;
        this.value=value;
        return this;
    }
    public Keyframe setHandle1(double time, double value){
        this.handle1T=time;
        this.handle1V=value;
        return this;
    }
    public Keyframe setHandle2(double time, double value){
        this.handle2T=time;
        this.handle2V=value;
        return this;
    }

    public Point2D.Double getHandle1(){
        return new Point2D.Double(handle1T,handle1V);
    }
    public Point2D.Double getHandle2(){
        return new Point2D.Double(handle2T,handle2V);
    }
    public Point2D.Double getPoint(){
        return new Point2D.Double(time,value);
    }

    public double getTime(){
        return time;
    }
    public double getValue(){
        return value;
    }

    @Override
    public int compareTo(Keyframe o) {
        if (time>o.time) return 1;
        else if (time<o.time) return -1;
        else return 0;
    }
}
