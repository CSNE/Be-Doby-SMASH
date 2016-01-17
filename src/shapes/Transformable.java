package shapes;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;
import java.util.Arrays;

import animation.InterpolatableProperty;


public class Transformable implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;

	String name=new String();
	double anchorX,anchorY;
	double bx, by, bscaleX, bscaleY, brotation,banchorX,banchorY; //Backup values
	InterpolatableProperty keyFramedX,keyFramedY,keyFramedScaleX,keyFramedScaleY,keyFramedRotation;
	Transformable parent;
	int renderOrder=0;
	boolean hasParent=false,active=false,selected=false;

	Point[] anchorCache=new Point[1003];
	int selectionPriority=0;


	public Transformable(double x, double y, double sX, double sY, double rot, String nm){
		this.name=nm;

		this.hasParent=false;
		this.selected=false;
		this.active=false;
		this.anchorX=0;
		this.anchorY=0;
		this.keyFramedX=new InterpolatableProperty(x);
		this.keyFramedY=new InterpolatableProperty(y);
		this.keyFramedRotation=new InterpolatableProperty(rot);
		this.keyFramedScaleX=new InterpolatableProperty(sX);
		this.keyFramedScaleY=new InterpolatableProperty(sY);
	}

	public Transformable(){
		this(0,0,1,1,0,"NO_NAME");
	}

	public Transformable(Transformable t){
		this.name = new String(t.name);
		this.anchorX = t.anchorX;
		this.anchorY = t.anchorY;
		this.keyFramedX = new InterpolatableProperty(t.keyFramedX);
		this.keyFramedY = new InterpolatableProperty(t.keyFramedY);
		this.keyFramedScaleX = new InterpolatableProperty(t.keyFramedScaleX);
		this.keyFramedScaleY = new InterpolatableProperty(t.keyFramedScaleY);
		this.keyFramedRotation = new InterpolatableProperty(t.keyFramedRotation);
		this.parent = t.parent;
		this.renderOrder = t.renderOrder;
		this.hasParent = t.hasParent;
		this.active = t.active;
		this.selected = t.selected;
	}
	/*
	abstract void drawStroke(Graphics2D g, int offsetX, int offsetY);

	abstract void drawFill(Graphics2D g, int offsetX, int offsetY);

	abstract void drawVertex(Graphics2D g, int offsetX, int offsetY);
	
	abstract boolean isInside(double x, double y);

	abstract Rectangle getBounds(double oX,double oY);

	abstract TransformableShape duplicate();
*/

	public void transform(double x, double y, double sX, double sY, double rot){
		this.keyFramedX.setValue(x);
		this.keyFramedY.setValue(y);
		this.keyFramedScaleX.setValue(sX);
		this.keyFramedScaleY.setValue(sY);
		this.keyFramedRotation.setValue(rot);
	}


	public void setParent(Transformable t){
		if (t==parent){
			System.out.println("Transformable-setParent(): setParent Called but the target is the same! Ignoring.");
			return;
		}
		if (hasParent) {
			System.out.println("Transformable-setParent(): setParent Called but already has parent! Clearing Parent.");
			clearParent();
		}
		if (t==null) {
			clearParent();
			return;
		}
		Transformable current=t;
		while (current.hasAParent()){
			current=current.getParent();
			if (current==this){
				System.out.println("Parenting Loop.");
				return;
			}
		}

		parent=t;
		hasParent=true;
		TransformationMatrix itm;
		itm=t.getFinalMatrix().inverseMatrix().multiplyMatrix(this.getThisMatrix());
		this.keyFramedX.setValue(itm.getXFromMatrix());
		this.keyFramedY.setValue(itm.getYFromMatrix());
		this.keyFramedScaleX.setValue(itm.getScaleXFromMatrix());
		this.keyFramedScaleY.setValue(itm.getScaleYFromMatrix());
		this.keyFramedRotation.setValue(itm.getRotationFromMatrix());
	}

	public void clearParent(){
		if (!hasParent) return; //it already doesn't have a parent, skip!
		TransformationMatrix ftm=this.getFinalMatrix();
		hasParent=false;
		this.keyFramedX.setValue(ftm.getXFromMatrix());
		this.keyFramedY.setValue(ftm.getYFromMatrix());
		this.keyFramedScaleX.setValue(ftm.getScaleXFromMatrix());
		this.keyFramedScaleY.setValue(ftm.getScaleYFromMatrix());
		this.keyFramedRotation.setValue(ftm.getRotationFromMatrix());
	}

	public TransformationMatrix anchorToOrigin(){
		TransformationMatrix tm;
		tm=new TransformationMatrix(1,0,-this.anchorX,0,1,-this.anchorY,0,0,1);
		return tm;
	}
	public TransformationMatrix originToAnchor(){
		TransformationMatrix tm;
		tm=new TransformationMatrix(1,0,this.anchorX,0,1,this.anchorY,0,0,1);
		return tm;
	}
	public TransformationMatrix translationMatrix(){
		TransformationMatrix tm,a2o,o2a,res;
		a2o=this.anchorToOrigin();
		o2a=this.originToAnchor();
		tm=new TransformationMatrix(1,0,this.keyFramedX.getValue(),0,1,this.keyFramedY.getValue(),0,0,1);
		res=o2a.multiplyMatrix(tm.multiplyMatrix(a2o));
		return res;
	}

	public TransformationMatrix rotationMatrix(){
		TransformationMatrix a2o,o2a,rm,res;
		a2o=this.anchorToOrigin();
		o2a=this.originToAnchor();
		rm=new TransformationMatrix(Math.cos(Math.toRadians(this.keyFramedRotation.getValue())),Math.sin(Math.toRadians(this.keyFramedRotation.getValue())),0,-Math.sin(Math.toRadians(this.keyFramedRotation.getValue())),Math.cos(Math.toRadians(this.keyFramedRotation.getValue())),0,0,0,1);
		res=o2a.multiplyMatrix(rm.multiplyMatrix(a2o));
		return res;
	}

	public TransformationMatrix scaleMatrix(){
		TransformationMatrix a2o,o2a,sm,res;
		a2o=this.anchorToOrigin();
		o2a=this.originToAnchor();
		sm=new TransformationMatrix(this.keyFramedScaleX.getValue(),0,0,0,this.keyFramedScaleY.getValue(),0,0,0,1);
		res=o2a.multiplyMatrix(sm.multiplyMatrix(a2o));
		return res;
	}

	public TransformationMatrix getThisMatrix(){
		TransformationMatrix tm;
		tm=this.translationMatrix().multiplyMatrix(this.rotationMatrix().multiplyMatrix(this.scaleMatrix()));
		return tm;
	}

	public TransformationMatrix getThisMatrixAtTime(int time){
		Transformable to=this.getObjectAtTime(time);
		return to.getThisMatrix();
	}

	public TransformationMatrix getParentMatrix(){
		if (hasParent) return getParent().getFinalMatrix();
		else return TransformationMatrix.identityMatrix();
	}
	public TransformationMatrix getFinalMatrix(){
		if(this.hasParent==true){
			return parent.getFinalMatrix().multiplyMatrix(this.getThisMatrix());
		}
		else{
			return this.getThisMatrix();
		}
	}

	public TransformationMatrix getFinalMatrixAtTime(int time){
		if(this.hasParent==true){
			return parent.getFinalMatrixAtTime(time).multiplyMatrix(this.getThisMatrixAtTime(time));
		}
		else{
			return this.getThisMatrixAtTime(time);
		}
	}

	public TransformationMatrix getOffsettedFinalMatrix(double offsetX, double offsetY){
		return this.getFinalMatrix().offset(offsetX, offsetY);
	}

	public TransformationMatrix getOffsettedFinalMatrixAtTime(int time, double offsetX, double offsetY){
		return this.getFinalMatrixAtTime(time).offset(offsetX, offsetY);
	}

	public void printTransform(){
		System.out.println(this.name+" : "+"Px:"+this.keyFramedX.getValue()+" / Py:"+this.keyFramedY.getValue()+" / Sx:"+this.keyFramedScaleX.getValue()+" / Sy:"+this.keyFramedScaleY.getValue()+" / Rot:"+this.keyFramedRotation.getValue());
	}
	public void setRenderOrder(int order){
		this.renderOrder=order;
		if (renderOrder>10) renderOrder=10;
		else if (renderOrder<-10) renderOrder=-10;
	}
	public void incrementRenderOrder(){
		this.renderOrder++;
		if (renderOrder>10) renderOrder=10;
	}
	public void decrementRenderOrder(){
		this.renderOrder--;
		if (renderOrder<-10) renderOrder=-10;
	}
	public int getRenderOrder(){
		return this.renderOrder;
	}
	public Point anchorToGlobal(){
		return this.getFinalMatrix().transformPoint(new Point((int)this.anchorX,(int)this.anchorY));
	}

	public void drawParentLine(Graphics2D g, double oX, double oY,boolean invert){
		TransformationMatrix offsetMatrix=TransformationMatrix.translationMatrix(oX,oY);
		//offsetMatrix.printMatrix();
		
		if (invert) g.setColor(new Color(0,0,0));
		else g.setColor(new Color(255,255,255));
		if (this.hasParent==true){
			Point parentP,thisP;
			parentP=offsetMatrix.transformPoint(this.parent.anchorToGlobal());
			thisP=offsetMatrix.transformPoint(this.anchorToGlobal());
			g.drawLine((int)parentP.getX(),(int)parentP.getY(),(int)thisP.getX(),(int)thisP.getY());
		}
	}

	public void drawAnchor(Graphics2D g,double oX,double oY){
		g.setColor(new Color(255,255,255));
		int anchorX,anchorY,iR=4,oR=8;
		anchorX=(int)this.getOffsettedFinalMatrix(oX,oY).transformX(this.anchorX,this.anchorY);
		anchorY=(int)this.getOffsettedFinalMatrix(oX,oY).transformY(this.anchorX,this.anchorY);
		g.drawLine(anchorX+iR, anchorY, anchorX+oR, anchorY);
		g.drawLine(anchorX-iR, anchorY, anchorX-oR, anchorY);
		g.drawLine(anchorX, anchorY+iR, anchorX, anchorY+oR);
		g.drawLine(anchorX, anchorY-iR, anchorX, anchorY-oR);
	}

	public void drawAnchorDot(Graphics2D g, double oX, double oY,int alpha){
		g.setColor(new Color(255,255,255,alpha));
		int anchorXt,anchorYt;
		anchorXt=(int)this.getOffsettedFinalMatrix(oX,oY).transformX(this.anchorX,this.anchorY);
		anchorYt=(int)this.getOffsettedFinalMatrix(oX,oY).transformY(this.anchorX,this.anchorY);
		g.fillOval(anchorXt-1,anchorYt-1,3,3);
	}

	public void drawAnchorDotAtTime(int time, Graphics2D g, double oX, double oY,Color c){
		g.setColor(c);
		int anchorXt,anchorYt;
		anchorXt=(int)this.getOffsettedFinalMatrixAtTime(time,oX,oY).transformX(this.anchorX,this.anchorY);
		anchorYt=(int)this.getOffsettedFinalMatrixAtTime(time,oX,oY).transformY(this.anchorX,this.anchorY);
		g.fillOval(anchorXt-1,anchorYt-1,3,3);
	}

	public void drawAnchorTrail(Graphics2D g, double oX, double oY, int currentTime, int startTime, int endTime, int fade){

		int alpha=0;

		for(int i=startTime;i<=endTime;i++){
			if (Math.abs(i-currentTime)<=7) alpha=100+20*(7-Math.abs(i-currentTime));
			else alpha=100-fade*Math.abs(i-currentTime);

			if (alpha<0) continue;

			if ((i-currentTime)<0) this.drawAnchorDotAtTime(i,g, oX, oY, new Color(255,0,255,alpha));
			else if ((i-currentTime)>0) this.drawAnchorDotAtTime(i,g, oX, oY, new Color(0,255,255,alpha));
			else this.drawAnchorDotAtTime(i,g, oX, oY, new Color(255,255,255,alpha));
		}
	}


	public void cacheAnchorTrail(int startTime, int endTime){
		for(int i=startTime;i<=endTime;i++){
			this.cacheAnchorDotAtTime(i);
		}
	}
	public void cacheAnchorDotAtTime(int time){

		int anchorXt,anchorYt;
		anchorXt=(int)this.getFinalMatrixAtTime(time).transformX(this.anchorX,this.anchorY);
		anchorYt=(int)this.getFinalMatrixAtTime(time).transformY(this.anchorX,this.anchorY);

		//System.out.println(time);

		anchorCache[time]=new Point(anchorXt,anchorYt);
		//System.out.println(time+" : "+anchorCache[time]);
	}
	public void drawAnchorFromCacheAtTime (int time, Graphics2D g, double oX, double oY,Color c) throws NullPointerException {
		TransformationMatrix offsetMatrix=TransformationMatrix.translationMatrix(oX,oY);

		g.setColor(c);

		int anchorXt,anchorYt;
		anchorXt=(int) offsetMatrix.transformX(anchorCache[time].getX(), anchorCache[time].getY());
		anchorYt=(int) offsetMatrix.transformY(anchorCache[time].getX(), anchorCache[time].getY());
		//System.out.println(time+" : "+anchorCache[time].getX()+" / "+anchorCache[time].getY()+" / "+anchorXt+" / "+anchorYt);
		g.fillOval(anchorXt-1,anchorYt-1,3,3);
	}
	public void drawAnchorTrailFromCache(Graphics2D g, double oX, double oY, int currentTime, int startTime, int endTime, int fade){
		int alpha=0;
		for(int i=startTime;i<=endTime;i++){
			if (Math.abs(i-currentTime)<=7) alpha=100+20*(7-Math.abs(i-currentTime));
			else alpha=100-fade*Math.abs(i-currentTime);

			if (alpha<0) continue;
			try{
				if ((i-currentTime)<0) this.drawAnchorFromCacheAtTime(i,g, oX, oY, new Color(255,0,255,alpha));
				else if ((i-currentTime)>0) this.drawAnchorFromCacheAtTime(i,g, oX, oY, new Color(0,255,255,alpha));
				else this.drawAnchorFromCacheAtTime(i,g, oX, oY, new Color(255,255,255,alpha));
			}catch(NullPointerException e){

			}
		}
	}


	public void activate(){
		this.active=true;
	}
	public void deactivate(){
		this.active=false;
		this.deselect();
	}
	public boolean isActive(){
		return this.active;
	}

	public void select(){
		this.selected=true;
	}
	public void deselect(){
		this.selected=false;
	}
	public boolean isSelected(){
		return this.selected;
	}



	public void backUpTransform(){
		this.bx=this.keyFramedX.getValue();
		this.by=this.keyFramedY.getValue();
		this.bscaleX=this.keyFramedScaleX.getValue();
		this.bscaleY=this.keyFramedScaleY.getValue();
		this.brotation=this.keyFramedRotation.getValue();
		this.banchorX=this.anchorX;
		this.banchorY=this.anchorY;
	}

	public void rollbackTransform(){
		this.keyFramedX.setValue(this.bx);
		this.keyFramedY.setValue(this.by);
		this.keyFramedScaleX.setValue(this.bscaleX);
		this.keyFramedScaleY.setValue(this.bscaleY);
		this.keyFramedRotation.setValue(this.brotation);
		this.anchorX=this.banchorX;
		this.anchorY=this.banchorY;
	}

	public Transformable getParent(){
		return this.parent;
	}

	public boolean hasAParent(){
		return this.hasParent;
	}


	public void setValueFromTime(double time){
		this.keyFramedX.setTime(time);

		this.keyFramedY.setTime(time);

		this.keyFramedScaleX.setTime(time);

		this.keyFramedScaleY.setTime(time);

		this.keyFramedRotation.setTime(time);

	}
	public Transformable getObjectAtTime(double time){
		//Transformable res=new Transformable(this);
		Transformable res=new Transformable();
		res.setValueFromTime(time);
		return res;
	}

	public void printFinalTransformation(){
		System.out.println(">>Px: "+this.getFinalMatrix().getXFromMatrix()
				+" / Py: "+this.getFinalMatrix().getYFromMatrix()
				+" / Sx: "+this.getFinalMatrix().getScaleXFromMatrix()
				+" / Sy: "+this.getFinalMatrix().getScaleYFromMatrix()
				+" / Rot: "+this.getFinalMatrix().getRotationFromMatrix());
		this.getFinalMatrix().printMatrix();
	}






	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InterpolatableProperty getKeyFramedX() {
		return keyFramedX;
	}

	public void setKeyFramedX(InterpolatableProperty keyFramedX) {
		this.keyFramedX = keyFramedX;
	}

	public InterpolatableProperty getKeyFramedY() {
		return keyFramedY;
	}

	public void setKeyFramedY(InterpolatableProperty keyFramedY) {
		this.keyFramedY = keyFramedY;
	}

	public InterpolatableProperty getKeyFramedScaleX() {
		return keyFramedScaleX;
	}

	public void setKeyFramedScaleX(InterpolatableProperty keyFramedScaleX) {
		this.keyFramedScaleX = keyFramedScaleX;
	}

	public InterpolatableProperty getKeyFramedScaleY() {
		return keyFramedScaleY;
	}

	public void setKeyFramedScaleY(InterpolatableProperty keyFramedScaleY) {
		this.keyFramedScaleY = keyFramedScaleY;
	}

	public InterpolatableProperty getKeyFramedRotation() {
		return keyFramedRotation;
	}

	public void setKeyFramedRotation(InterpolatableProperty keyFramedRotation) {
		this.keyFramedRotation = keyFramedRotation;
	}

	public int getSelectionPriority(){
		return this.selectionPriority;
	}
	public void resetSelectionPriority(){
		this.selectionPriority=0;
	}
	public void incrementSelectionPriority(){
		this.selectionPriority++;
	}
}
