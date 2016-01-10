package helpers;

import animation.Keyframe;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class CurveGenerator {
    public static Path2D.Double generatePathFromPoints(Point2D.Double[] points){
        Path2D.Double res=new Path2D.Double();
        for (int i=0;i<points.length;i++){
            if (i==0) res.moveTo(points[i].getX(),points[i].getY());
            else res.lineTo(points[i].getX(),points[i].getY());
        }
        return res;
    }
    public static Ellipse2D.Double generateCircleFromCoords(Point2D.Double center, double radius){
        return new Ellipse2D.Double(center.getX()-radius,center.getY()-radius,radius*2,radius*2);
    }

}
