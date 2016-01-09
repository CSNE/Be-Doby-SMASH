package ui;

import animation.Keyframe;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chan on 2016-01-04.
 */
public class GraphTransformer {
    public GraphTransformer(){

    }
    public void reset(){
        dataX.clear();
        dataY.clear();
    }

    ArrayList<Double> dataX=new ArrayList<>();
    ArrayList<Double> dataY=new ArrayList<>();

    double boundMinX,boundMinY,boundMaxX,boundMaxY,spacingX=0,spacingY=0,maxX,maxY,minX,minY;

    /* @Deprecated
     * Instead of data, add keyframes.
     */
    @Deprecated
    public void addData(double x, double y){
        dataX.add(x);
        dataY.add(y);
    }
    public void feedData(List<Keyframe> kf){
        for (int i = 0; i < kf.size(); i++) {
            dataX.add(kf.get(i).getPoint().getX());
            dataX.add(kf.get(i).getHandle1().getX());
            dataX.add(kf.get(i).getHandle2().getX());
            dataY.add(kf.get(i).getPoint().getY());
            dataY.add(kf.get(i).getHandle1().getY());
            dataY.add(kf.get(i).getHandle2().getY());
        }

    }
    public void setBounds(double minX,double maxX,double minY,double maxY){
        this.boundMinX=minX;
        this.boundMaxY=maxY;
        this.boundMaxX=maxX;
        this.boundMinY=minY;
    }
    public void setSpacing(double x, double y){
        this.spacingX=x;
        this.spacingY=y;
    }
    public void calculate(){
        maxX=-1000000;
        maxY=-1000000;
        minX=1000000;
        minY=1000000;
        assert dataY.size()==dataX.size();//Just tp be sure
        for (int i = 0; i < dataY.size(); i++) {
            if (dataX.get(i)>maxX) maxX=dataX.get(i);
            if (dataX.get(i)<minX) minX=dataX.get(i);
            if (dataY.get(i)>maxY) maxY=dataY.get(i);
            if (dataY.get(i)<minY) minY=dataY.get(i);
        }
    }

    public double fromGraphSpaceXToDrawSpaceX(double x){
        return (x-minX)*(boundMaxX-boundMinX-spacingX)/(maxX-minX)+boundMinX+spacingX/2.0;
    }
    public double fromGraphSpaceYToDrawSpaceY(double y){
        return (y-minY)*(boundMaxY-boundMinY-spacingY)/(maxY-minY)+boundMinY+spacingY/2.0;
    }
    public double fromDrawSpaceXToGraphSpaceX(double x){
        return (x-boundMinX-spacingX/2.0)*(maxX-minX)/(boundMaxX-boundMinX-spacingX)+minX;
    }
    public double fromDrawSpaceYToGraphSpaceY(double y){
        return (y-boundMinY-spacingY/2.0)*(maxY-minY)/(boundMaxY-boundMinY-spacingY)+minY;
    }
    public Point2D.Double fromGraphSpaceToDrawSpace(Point2D.Double p){
        return new Point2D.Double(fromGraphSpaceXToDrawSpaceX(p.getX()),fromGraphSpaceYToDrawSpaceY(p.getY()));
    }
    public Point2D.Double fromDrawSpaceToGraphSpace(Point2D.Double p){
        return new Point2D.Double(fromDrawSpaceXToGraphSpaceX(p.getX()),fromDrawSpaceYToGraphSpaceY(p.getY()));
    }
    public Point2D.Double[] fromGraphSpaceToDrawSpace(List<Point2D.Double> p) {
        Point2D.Double[] res=new Point2D.Double[p.size()];
        for (int i = 0; i < p.size(); i++) {
            res[i]=fromGraphSpaceToDrawSpace(p.get(i));
        }
        return res;
    }
    public Point2D.Double[] fromDrawSpaceToGraphSpace(List<Point2D.Double> p) {
        Point2D.Double[] res=new Point2D.Double[p.size()];
        for (int i = 0; i < p.size(); i++) {
            res[i]=fromDrawSpaceToGraphSpace(p.get(i));
        }
        return res;
    }

    public double pxPerSecond(){
        return getBoundsSizeX()/(getMaxX()- getMinX());
    }
    public double pxPerY(){
        return getBoundsSizeY()/(getMaxY()- getMinY());
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }
    public double getBoundsSizeX(){
        return boundMaxX-boundMinX;
    }
    public double getBoundsSizeY(){
        return boundMaxY-boundMinY;
    }
    public double getOriginX(){
        return (-minX)*(boundMaxX-boundMinX-spacingX)/(maxX-minX)+boundMinX+spacingX/2.0;
    }
    public double getOriginY(){
        return (-minY)*(boundMaxY-boundMinY-spacingY)/(maxY-minY)+boundMinY+spacingY/2.0;
    }
}
