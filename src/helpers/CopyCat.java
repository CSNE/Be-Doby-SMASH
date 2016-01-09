package helpers;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by Chan on 1/8/2016.
 */
public class CopyCat {
    public static Ellipse2D.Double copy(Ellipse2D.Double e){
        return new Ellipse2D.Double(e.getX(),e.getY(),e.getWidth(),e.getHeight());
    }
    public static Color copy(Color c){
        return new Color(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha());
    }
}
