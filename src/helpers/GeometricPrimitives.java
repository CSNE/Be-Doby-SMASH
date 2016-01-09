package helpers;

import shapes.TransformationMatrix;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/**
 * Created by Chan on 2016-01-03.
 */
public class GeometricPrimitives {
    public static final int TRIANGLE=89765;
    public static final int SQUARE=1512346;

    int type;
    TransformationMatrix tm;

    public GeometricPrimitives(int type){
        this.type=type;
        tm=TransformationMatrix.identityMatrix();
    }
    public GeometricPrimitives translate(Point2D.Double position){
        return translate(position.getX(),position.getY());
    }
    public GeometricPrimitives translate(double x,double y){
        tm=TransformationMatrix.translationMatrix(x,y).multiplyMatrix(tm);
        return this;
    }
    public GeometricPrimitives scale(double factor){
        tm=TransformationMatrix.scaleMatrix(factor,factor).multiplyMatrix(tm);
        return this;
    }
    public GeometricPrimitives scale(double x, double y){
        tm=TransformationMatrix.scaleMatrix(x,y).multiplyMatrix(tm);
        return this;
    }
    public GeometricPrimitives rotate(double degrees){
        tm=TransformationMatrix.rotationMatrix(degrees).multiplyMatrix(tm);
        return this;
    }
    public GeometricPrimitives appendMatrix(TransformationMatrix matrix){
        tm=matrix.multiplyMatrix(tm);
        return this;
    }
    public Path2D.Double getPath2D(){
        Point2D.Double[] points;
        int nPoints;
        if (type==TRIANGLE){
            points=new Point2D.Double[3];
            nPoints=3;
            points[0]=new Point2D.Double(0,50);
            points[1]=new Point2D.Double(-43.30127019,-25);
            points[2]=new Point2D.Double(43.30127019,-25);
        }else if (type==SQUARE){
            points=new Point2D.Double[4];
            nPoints=4;
            points[0]=new Point2D.Double(50.0,50.0);
            points[1]=new Point2D.Double(-50.0,50.0);
            points[2]=new Point2D.Double(-50.0,-50.0);
            points[3]=new Point2D.Double(50.0,-50.0);

        }else {
            points=new Point2D.Double[0];
            nPoints=0;
        }

        Path2D.Double res;
        Point2D.Double currentPoints;
        res=new Path2D.Double();
        for (int i=0;i<nPoints;i++){
            currentPoints=tm.transformPoint2D(points[i]);
            if (i==0) res.moveTo(currentPoints.getX(),currentPoints.getY());
            else res.lineTo(currentPoints.getX(),currentPoints.getY());
            //System.out.println(currentPoints.getX()+"|"+currentPoints.getY());
        }
        res.closePath();
        return res;
    }

}
