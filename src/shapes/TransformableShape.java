package shapes;
import helpers.CopyCat;
import helpers.GeometricPrimitives;

import java.awt.*;
import java.awt.geom.*;
import java.io.Serializable;
import java.util.ArrayList;


public class TransformableShape extends Transformable implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	ArrayList<Point2D.Double> points=new ArrayList<>();
	ArrayList<Point2D.Double> bkupPoints=new ArrayList<>();
	Color clr;

	transient int i;

	public TransformableShape(){
		this(0,0,1,1,0,"NO_NAME");
	}

	public TransformableShape(double x, double y, double sX, double sY, double rot,String nm){
		super(x,y,sX,sY,rot,nm);
		clr=new Color(255,255,255);
	}
	public TransformableShape(TransformableShape t){
		super(t);
		this.points=new ArrayList<>(t.points);
		this.bkupPoints=new ArrayList<>(t.bkupPoints);
		this.clr=CopyCat.copy(t.clr);
	}
	public void makeSquare(double x, double y){
		points.clear();
		points.add(new Point2D.Double((-x/2),(-y/2)));
		points.add(new Point2D.Double((x/2),(-y/2)));
		points.add(new Point2D.Double((x/2),(y/2)));
		points.add(new Point2D.Double((-x/2),(y/2)));

	}

	public void makePolygon(int points, double rad){
		this.points.clear();
		double rotPer=2*Math.PI/points;
		double rot=rotPer/2;
		double x,y;
		for(i=0;i<points;i++){
			x=rad*Math.cos(rot);
			y=rad*Math.sin(rot);
			this.points.add(new Point2D.Double(x,y));
			rot=rot+rotPer;
		}
	}
	public void addPoint(double x, double y){
		this.addPoint(new Point2D.Double(x,y));
	}
	public void addPoint(Point2D.Double p){
		this.points.add(p);
	}

	public Point2D.Double calculateVertexMean(){
		double x=0, y=0;
		for (Point2D.Double p:points){
			x+=p.getX();
			y+=p.getY();
		}
		return new Point2D.Double(x/points.size(),y/points.size());
	}

	public Path2D.Double getPath(){
		Path2D.Double res;
		Point2D.Double currentPoint;
		res=new Path2D.Double();
		for (i=0;i<points.size();i++){
			currentPoint=this.getFinalMatrix().transformPoint2D(points.get(i));
			if (i==0) res.moveTo(currentPoint.getX(),currentPoint.getY());
			else res.lineTo(currentPoint.getX(),currentPoint.getY());
		}
		res.closePath();
		return res;
	}
	public Path2D.Double getLocalPath(){
		Path2D.Double res;
		Point2D.Double currentPoint;
		res=new Path2D.Double();
		for (i=0;i<points.size();i++){
			currentPoint=points.get(i);
			if (i==0) res.moveTo(currentPoint.getX(),currentPoint.getY());
			else res.lineTo(currentPoint.getX(),currentPoint.getY());
		}
		res.closePath();
		return res;
	}
	public Path2D.Double getOffsettedPath(double oX, double oY){
		Path2D.Double res;
		Point2D.Double currentPoint;
		res=new Path2D.Double();
		for (i=0;i<points.size();i++){
			currentPoint=this.getOffsettedFinalMatrix(oX,oY).transformPoint2D(points.get(i));
			if (i==0) res.moveTo(currentPoint.getX(),currentPoint.getY());
			else res.lineTo(currentPoint.getX(),currentPoint.getY());
		}
		if (points.size()>0) res.closePath();
		return res;
	}

	public void drawStroke(Graphics2D g, int offsetX, int offsetY){
		drawStroke(g,offsetX,offsetY,clr);
	}
	
	public void drawStroke(Graphics2D g, int offsetX, int offsetY, Color c){
		g.setColor(c);
		g.draw(this.getOffsettedPath(offsetX,offsetY));
	}
	public void drawFill(Graphics2D g, int offsetX, int offsetY,Color c){
		g.setColor(c);
		g.fill(this.getOffsettedPath(offsetX,offsetY));
	}
	public void drawFill(Graphics2D g, int offsetX, int offsetY){
		drawFill(g, offsetX, offsetY,clr);
	}



	public boolean isInside(double x, double y){
		return this.getPath().contains(x,y);
	}

	public Rectangle getBounds(double oX,double oY){
		return this.getOffsettedPath(oX,oY).getBounds();
	}
	public void drawBounds(Graphics2D g, int offsetX, int offsetY, long time){
		if ((Math.floor(time/500L))%2L==0) {
			g.setColor(Color.WHITE);
			g.draw(getBounds(offsetX, offsetY));
		}
	}



	//SPAGHETTI!
	transient Ellipse2D.Double xHandle;
	transient Ellipse2D.Double yHandle;
	transient Ellipse2D.Double xyHandle;
	double handlesDistance=30;
	transient Ellipse2D.Double rotationGuide, rotationHandle;
	double handleSize=10, rotationGuideRadius =100;


	public void drawHandles(Graphics2D g, int offsetX, int offsetY, long Time){
		TransformationMatrix finalMatrix=getFinalMatrix();
		Point2D.Double xHandlePoint=finalMatrix.transformPoint2D(new Point2D.Double(handlesDistance,0));
		Point2D.Double yHandlePoint=finalMatrix.transformPoint2D(new Point2D.Double(0,-handlesDistance));
		Point2D.Double xyHandlePoint=finalMatrix.transformPoint2D(new Point2D.Double(handlesDistance,-handlesDistance));
		Point2D.Double centerPoint=finalMatrix.transformPoint2D(new Point2D.Double(0,0));
		//System.out.println(""+xHandlePoint.getX()+" | "+xHandlePoint.getY()+" | "+handleSize);
		xHandle=new Ellipse2D.Double(xHandlePoint.getX()-handleSize/2.0,xHandlePoint.getY()-handleSize/2.0,handleSize,handleSize);
		yHandle=new Ellipse2D.Double(yHandlePoint.getX()-handleSize/2.0,yHandlePoint.getY()-handleSize/2.0,handleSize,handleSize);
		xyHandle=new Ellipse2D.Double(xyHandlePoint.getX()-handleSize/2.0,xyHandlePoint.getY()-handleSize/2.0,handleSize,handleSize);

		Point2D.Double center=getFinalMatrix().transformPoint2D(new Point2D.Double(anchorX,anchorY));
		rotationGuide=new Ellipse2D.Double(center.getX()- rotationGuideRadius,center.getY()- rotationGuideRadius, rotationGuideRadius*2, rotationGuideRadius*2);
		Point2D.Double rotHandleCenter=new Point2D.Double(center.getX()+rotationGuideRadius*Math.cos(Math.toRadians(getKeyFramedRotation().getValue())),center.getY()-rotationGuideRadius*Math.sin(Math.toRadians(getKeyFramedRotation().getValue())));
		rotationHandle=new Ellipse2D.Double(rotHandleCenter.getX()-handleSize/2.0,rotHandleCenter.getY()-handleSize/2.0,handleSize,handleSize);
		g.setColor(Color.RED); //TODO Blinking here

		g.fill(xHandle);
		g.fill(yHandle);
		g.fill(xyHandle);

		g.draw(new Line2D.Double(centerPoint.getX(),centerPoint.getY(),xHandlePoint.getX(),xHandlePoint.getY()));
		g.draw(new Line2D.Double(centerPoint.getX(),centerPoint.getY(),yHandlePoint.getX(),yHandlePoint.getY()));
		g.draw(new Line2D.Double(centerPoint.getX(),centerPoint.getY(),xyHandlePoint.getX(),xyHandlePoint.getY()));


		//g.setStroke(new BasicStroke(3));
		g.draw(rotationGuide);
		g.fill(rotationHandle);
	}

	transient TransformationMatrix initialMatrix;
	transient TransformationMatrix initialParentMatrix;
	transient Ellipse2D.Double xHandleInitial;
	transient Ellipse2D.Double yHandleInitial;
	transient Ellipse2D.Double xyHandleInitial;
	transient Ellipse2D.Double rotationHandleInitial;
	transient Ellipse2D.Double rotationGuideInitial;
	transient int mouseIniX,mouseIniY;
	transient double mouseIniXLocal,mouseIniYLocal;
	public void saveMouseInitialPosition(int x, int y){
		backUpTransform();
		initialMatrix=new TransformationMatrix(getFinalMatrix());
		initialParentMatrix=new TransformationMatrix(getParentMatrix());
		xHandleInitial= CopyCat.copy(xHandle);
		yHandleInitial=CopyCat.copy(yHandle);
		xyHandleInitial=CopyCat.copy(xyHandle);
		rotationHandleInitial=CopyCat.copy(rotationHandle);
		rotationGuideInitial=CopyCat.copy(rotationGuide);
		mouseIniX=x;
		mouseIniY=y;
		mouseIniXLocal=initialMatrix.inverseMatrix().transformX(x,y);
		mouseIniYLocal=initialMatrix.inverseMatrix().transformY(x,y);
	}
	public void updateMouseCurrentPosition(int x, int y){
		//System.out.println("updateTime: "+x+" | "+y);
		double xLocal=initialMatrix.inverseMatrix().transformX(x,y);
		double yLocal=initialMatrix.inverseMatrix().transformY(x,y);
		if (yHandleInitial.contains(mouseIniX,mouseIniY)){
			getKeyFramedScaleY().setValue(bscaleY*(yLocal-anchorY)/(mouseIniYLocal - anchorY));
		}else if (xHandleInitial.contains(mouseIniX,mouseIniY)){
			getKeyFramedScaleX().setValue(bscaleX*(xLocal-anchorX)/(mouseIniXLocal - anchorX));
		}else if (xyHandleInitial.contains(mouseIniX,mouseIniY)){
			double f=Math.sqrt(Math.pow(xLocal,2)+Math.pow(yLocal,2))/Math.sqrt(Math.pow(mouseIniXLocal,2)+Math.pow(mouseIniYLocal,2));
			getKeyFramedScaleX().setValue(bscaleX*f);
			getKeyFramedScaleY().setValue(bscaleY*f);
		}else if (rotationHandleInitial.contains(mouseIniX,mouseIniY)) {
			getKeyFramedRotation().setValue(brotation+Math.toDegrees(Math.atan2(-yLocal,xLocal))-Math.toDegrees(Math.atan2(-mouseIniYLocal,mouseIniXLocal))); //TODO anchor point fuckup imminent
		}else if (rotationGuideInitial.contains(mouseIniX,mouseIniY)){
			Point2D.Double iniParent=initialParentMatrix.inverseMatrix().transformPoint2D(new Point2D.Double(mouseIniX,mouseIniY));
			Point2D.Double currentParent=initialParentMatrix.inverseMatrix().transformPoint2D(new Point2D.Double(x,y));
			getKeyFramedX().setValue(bx+currentParent.getX()-iniParent.getX());
			getKeyFramedY().setValue(by+currentParent.getY()-iniParent.getY());
		}
	}




	transient Shape[] iveHandles;
	transient Shape[] iveHandlesBackup;
	double iveHandleSize=10;
	public void saveIVEInitialMousePosition(int x, int y){
		generateVertexHandles();
		backupVertex();
		mouseIniX=x;
		mouseIniY=y;
		mouseIniXLocal=getFinalMatrix().inverseMatrix().transformX(x,y);
		mouseIniYLocal=getFinalMatrix().inverseMatrix().transformY(x,y);

		iveHandlesBackup=new Shape[iveHandles.length];
		for(i=0;i<iveHandles.length;i++){
			iveHandlesBackup[i]=iveHandles[i];
		}
	}
	public void updateIVEMousePosition(int x, int y){
		rollbackVertex();
		TransformationMatrix inverseMatrix=getFinalMatrix().inverseMatrix();
		Point2D.Double currentLocal=inverseMatrix.transformPoint2D(new Point2D.Double(x,y));
		double dX=currentLocal.getX()-mouseIniXLocal;
		double dY=currentLocal.getY()-mouseIniYLocal;
		for (i=0;i<iveHandlesBackup.length;i++) {
			if(iveHandlesBackup[i].contains(mouseIniX,mouseIniY)){
				points.get(i).setLocation(points.get(i).getX()+dX,points.get(i).getY()+dY);
				return;
			}
		}
		moveAllVertex(dX,dY);
	}
	public void drawVertexHandles(Graphics2D g, int offsetX, int offsetY){
		g.setColor(clr);
		generateVertexHandles();
		for(i=0;i<iveHandles.length;i++){
			g.draw(iveHandles[i]);
		}
	}
	public void fillVertexHandles(Graphics2D g, int offsetX, int offsetY){
		g.setColor(clr);
		generateVertexHandles();
		for(i=0;i<iveHandles.length;i++){
			g.fill(iveHandles[i]);
		}
	}
	public void generateVertexHandles(){
		Point2D.Double current;
		iveHandles=new Shape[points.size()];
		for(i=0;i<points.size();i++){
			current=getFinalMatrix().transformPoint2D(points.get(i));
			iveHandles[i]=new Ellipse2D.Double(current.getX()-iveHandleSize/2.0,current.getY()-iveHandleSize/2.0,iveHandleSize,iveHandleSize);
		}
	}



	public void mouseRelease(){

	}
	public void drawAnimatedSelectionCursor(Graphics2D g, int offsetX, int offsetY,double time){

	}

	public TransformableShape duplicate(){
		TransformableShape res;
		/*
		res=new TransformableShape(this.x,this.y,this.scaleX,this.scaleY,this.rotation,this.name);
		res.setAnchor(this.getAnchorX(), this.getAnchorY());
		res.nPoints=this.nPoints;
		res.clr=this.clr;
		for(i=0;i<nPoints;i++){
			res.points[i]=new Point2D.Double(this.points[i].getX(),this.points[i].getX());
		}
		res.nPoints=this.nPoints;
		if (this.hasParent) res.setParent(this.parent);
		res.activate();
		 */
		res=new TransformableShape(this);
		res.setName(this.getName()+"_Copy");
		return res;
	}



	public double distToVertex(double x, double y, int idx){
		return Math.pow(Math.pow(points.get(idx).getX()-x,2)+Math.pow(points.get(idx).getY()-y,2),0.5);
	}

	public int nearestVertex(double x, double y){
		double minDist=1000000;
		for(i=0;i<this.points.size();i++){
			if (distToVertex(x,y,i)<minDist){
				minDist=distToVertex(x,y,i);
			}
		}
		for(i=0;i<this.points.size();i++){
			if (distToVertex(x,y,i)==minDist){
				return i;
			}
		}
		System.out.println("WHAT");
		return 404;
	}
	public void moveVertex(double dx, double dy, int idx){
		this.points.get(idx).setLocation(this.points.get(idx).getX()+dx, this.points.get(idx).getY()+dy);
	}
	public void backupVertex(){
		bkupPoints.clear();
		for(i=0;i<points.size();i++){
			bkupPoints.add(new Point2D.Double(points.get(i).getX(),points.get(i).getY()));
		}

	}
	public void rollbackVertex(){

		points.clear();
		for(i=0;i<bkupPoints.size();i++){
			points.add(new Point2D.Double(bkupPoints.get(i).getX(),bkupPoints.get(i).getY()));
		}
	}

	public void moveAllVertex(double x, double y){
		for(Point2D.Double point:points){
			point.setLocation(point.getX()+x,point.getY()+y);
		}
	}

	public void centerVertices(){
		Point2D.Double center=calculateVertexMean();
		moveAllVertex(-center.getX(),-center.getY());
		getKeyFramedX().setValue(center.getX());
		getKeyFramedY().setValue(center.getY());
	}
	public void setColor(Color c){
		clr=c;
	}
	public Color getColor(){
		return this.clr;
	}


}
