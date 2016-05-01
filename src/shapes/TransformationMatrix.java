package shapes;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;

/* 00 10 20
 * 01 11 21
 * 02 12 22
 */

public class TransformationMatrix implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	double[][] element=new double[3][3];


	public TransformationMatrix(TransformationMatrix tm){
		this.element[0][0]=tm.element[0][0];
		this.element[0][1]=tm.element[0][1];
		this.element[0][2]=tm.element[0][2];
		this.element[1][0]=tm.element[1][0];
		this.element[1][1]=tm.element[1][1];
		this.element[1][2]=tm.element[1][2];
		this.element[2][0]=tm.element[2][0];
		this.element[2][1]=tm.element[2][1];
		this.element[2][2]=tm.element[2][2];
	}
	
	public TransformationMatrix(double e00, double e10, double e20, double e01, double e11, double e21, double e02, double e12, double e22){
		this.element[0][0]=e00;
		this.element[0][1]=e01;
		this.element[0][2]=e02;
		this.element[1][0]=e10;
		this.element[1][1]=e11;
		this.element[1][2]=e12;
		this.element[2][0]=e20;
		this.element[2][1]=e21;
		this.element[2][2]=e22;
	}
	
	private TransformationMatrix(){
		this(0,0,0,0,0,0,0,0,0);
	}

	transient double[][] temp=new double[3][3];

	public TransformationMatrix multiplyMatrix(TransformationMatrix t){
		/*
		TransformationMatrix res;
		res=new TransformationMatrix();

		res.element[0][0] = this.element[0][0] * t.element[0][0] + this.element[1][0] * t.element[0][1] + this.element[2][0] * t.element[0][2];
		res.element[1][0] = this.element[0][0] * t.element[1][0] + this.element[1][0] * t.element[1][1] + this.element[2][0] * t.element[1][2];
		res.element[2][0] = this.element[0][0] * t.element[2][0] + this.element[1][0] * t.element[2][1] + this.element[2][0] * t.element[2][2];

		res.element[0][1] = this.element[0][1] * t.element[0][0] + this.element[1][1] * t.element[0][1] + this.element[2][1] * t.element[0][2];
		res.element[1][1] = this.element[0][1] * t.element[1][0] + this.element[1][1] * t.element[1][1] + this.element[2][1] * t.element[1][2];
		res.element[2][1] = this.element[0][1] * t.element[2][0] + this.element[1][1] * t.element[2][1] + this.element[2][1] * t.element[2][2];

		res.element[0][2] = this.element[0][2] * t.element[0][0] + this.element[1][2] * t.element[0][1] + this.element[2][2] * t.element[0][2];
		res.element[1][2] = this.element[0][2] * t.element[1][0] + this.element[1][2] * t.element[1][1] + this.element[2][2] * t.element[1][2];
		res.element[2][2] = this.element[0][2] * t.element[2][0] + this.element[1][2] * t.element[2][1] + this.element[2][2] * t.element[2][2];
		
		return res;
		*/

		temp[0][0] = this.element[0][0] * t.element[0][0] + this.element[1][0] * t.element[0][1] + this.element[2][0] * t.element[0][2];
		temp[1][0] = this.element[0][0] * t.element[1][0] + this.element[1][0] * t.element[1][1] + this.element[2][0] * t.element[1][2];
		temp[2][0] = this.element[0][0] * t.element[2][0] + this.element[1][0] * t.element[2][1] + this.element[2][0] * t.element[2][2];

		temp[0][1] = this.element[0][1] * t.element[0][0] + this.element[1][1] * t.element[0][1] + this.element[2][1] * t.element[0][2];
		temp[1][1] = this.element[0][1] * t.element[1][0] + this.element[1][1] * t.element[1][1] + this.element[2][1] * t.element[1][2];
		temp[2][1] = this.element[0][1] * t.element[2][0] + this.element[1][1] * t.element[2][1] + this.element[2][1] * t.element[2][2];

		temp[0][2] = this.element[0][2] * t.element[0][0] + this.element[1][2] * t.element[0][1] + this.element[2][2] * t.element[0][2];
		temp[1][2] = this.element[0][2] * t.element[1][0] + this.element[1][2] * t.element[1][1] + this.element[2][2] * t.element[1][2];
		temp[2][2] = this.element[0][2] * t.element[2][0] + this.element[1][2] * t.element[2][1] + this.element[2][2] * t.element[2][2];

		element[0][0] = temp[0][0];
		element[1][0] = temp[1][0];
		element[2][0] = temp[2][0];
		element[0][1] = temp[0][1];
		element[1][1] = temp[1][1];
		element[2][1] = temp[2][1];
		element[0][2] = temp[0][2];
		element[1][2] = temp[1][2];
		element[2][2] = temp[2][2];


		return this;
	}
	
	public TransformationMatrix inverseMatrix(){
		TransformationMatrix res;
		res=new TransformationMatrix();
		double det,a,b,c,d,e,f,g,h,i;

		a = this.element[0][0];
		b = this.element[1][0];
		c = this.element[2][0];
		d = this.element[0][1];
		e = this.element[1][1];
		f = this.element[2][1];
		g = this.element[0][2];
		h = this.element[1][2];
		i = this.element[2][2];
		
		det=Math.abs(a*(e*i-f*h)-b*(d*i-f*g)+c*(d*h-e*g));
		res.element[0][0] = (e * i - f * h) / det;
		res.element[1][0] = (c * h - b * i) / det;
		res.element[2][0] = (b * f - c * e) / det;

		res.element[0][1] = (f * g - d * i) / det;
		res.element[1][1] = (a * i - c * g) / det;
		res.element[2][1] = (c * d - a * f) / det;

		res.element[0][2] = (d * h - e * g) / det;
		res.element[1][2] = (b * g - a * h) / det;
		res.element[2][2] = (a * e - b * d) / det;
		
		return res;
	}
	

	
	public double getElementAt(int x, int y){
		if (x>2 || y>2){
			System.out.println("MATRIX ERROR: Out of bounds!");
			return 404;
		}
		return this.element[x][y];
	}
	
	public void setElementAt(int x, int y, double v){
		if (x>2 || y>2){
			System.out.println("MATRIX ERROR: Out of bounds!");
		}
		this.element[x][y]=v;
	}
	
	public Point transformPoint(Point original){
		double e1,e2,e0,f1,f2,f0;
		e0=(double) original.getX();
		e1=(double) original.getY();
		e2=1;
		
		f0= this.element[0][0] * e0+ this.element[1][0] * e1+ this.element[2][0] * e2;
		f1= this.element[0][1] * e0+ this.element[1][1] * e1+ this.element[2][1] * e2;
		f2= this.element[0][2] * e0+ this.element[1][2] * e1+ this.element[2][2] * e2;
		
		return new Point((int)f0,(int)f1);
	}
	
	public Point2D.Double transformPoint2D(Point2D.Double original){
		double e1,e2,e0,f1,f2,f0;
		e0= original.getX();
		e1= original.getY();
		e2=1;
		
		f0= this.element[0][0] * e0+ this.element[1][0] * e1+ this.element[2][0] * e2;
		f1= this.element[0][1] * e0+ this.element[1][1] * e1+ this.element[2][1] * e2;
		f2= this.element[0][2] * e0+ this.element[1][2] * e1+ this.element[2][2] * e2;
		
		return new Point2D.Double(f0,f1);
	}
	
	public double transformX(double oX, double oY){
		double e1,e2,e0,f1,f2,f0;
		e0=oX;
		e1=oY;
		e2=1;
		
		f0= this.element[0][0] * e0+ this.element[1][0] * e1+ this.element[2][0] * e2;
		f1= this.element[0][1] * e0+ this.element[1][1] * e1+ this.element[2][1] * e2;
		f2= this.element[0][2] * e0+ this.element[1][2] * e1+ this.element[2][2] * e2;
		
		return f0;
	}
	
	public double transformY(double oX, double oY){
		double e1,e2,e0,f1,f2,f0;
		e0=oX;
		e1=oY;
		e2=1;
		
		f0= this.element[0][0] * e0+ this.element[1][0] * e1+ this.element[2][0] * e2;
		f1= this.element[0][1] * e0+ this.element[1][1] * e1+ this.element[2][1] * e2;
		f2= this.element[0][2] * e0+ this.element[1][2] * e1+ this.element[2][2] * e2;
		
		return f1;
	}
	
	public double getXFromMatrix(){
		return this.element[2][0];
	}
	public double getYFromMatrix(){
		return this.element[2][1];
	}
	public double getScaleXFromMatrix(){
		return Math.sqrt(Math.pow(this.element[0][0], 2)+Math.pow(this.element[0][1], 2));
	}
	public double getScaleYFromMatrix(){
		return Math.sqrt(Math.pow(this.element[1][0], 2)+Math.pow(this.element[1][1], 2));
	}
	public double getRotationFromMatrix(){
		return Math.toDegrees(Math.atan(this.element[1][0] / this.element[1][1]));
	}
	public void printMatrix(){
		System.out.println(this.element[0][0] +" "+ this.element[1][0] +" "+ this.element[2][0] +"\n"+ this.element[0][1] +" "+ this.element[1][1] +" "+ this.element[2][1] +"\n"+ this.element[0][2] +" "+ this.element[1][2] +" "+ this.element[2][2]);
	}

	public static TransformationMatrix identityMatrix(){
		TransformationMatrix res=new TransformationMatrix();
		res.element[0][0]=1;
		res.element[1][0]=0;
		res.element[2][0]=0;
		res.element[0][1]=0;
		res.element[1][1]=1;
		res.element[2][1]=0;
		res.element[0][2]=0;
		res.element[1][2]=0;
		res.element[2][2]=1;
		return res;
	}

	public static TransformationMatrix translationMatrix(double x, double y){
		TransformationMatrix res=new TransformationMatrix();
		res.element[0][0]=1;
		res.element[1][0]=0;
		res.element[2][0]=x;
		res.element[0][1]=0;
		res.element[1][1]=1;
		res.element[2][1]=y;
		res.element[0][2]=0;
		res.element[1][2]=0;
		res.element[2][2]=1;
		return res;
	}

	public static TransformationMatrix rotationMatrix(double rotation){
		TransformationMatrix res=new TransformationMatrix();
		res.element[0][0]=Math.cos(Math.toRadians(rotation));
		res.element[1][0]=	Math.sin(Math.toRadians(rotation));
		res.element[2][0]=0;
		res.element[0][1]=-Math.sin(Math.toRadians(rotation));
		res.element[1][1]=Math.cos(Math.toRadians(rotation));
		res.element[2][1]=0;
		res.element[0][2]=0;
		res.element[1][2]=0;
		res.element[2][2]=1;
		return res;
	}

	public static TransformationMatrix scaleMatrix(double sX, double sY){
		TransformationMatrix res=new TransformationMatrix();
		res.element[0][0]=sX;
		res.element[1][0]=0;
		res.element[2][0]=0;
		res.element[0][1]=0;
		res.element[1][1]=sY;
		res.element[2][1]=0;
		res.element[0][2]=0;
		res.element[1][2]=0;
		res.element[2][2]=1;
		return res;
	}
	
	public TransformationMatrix offset(double x, double y){
		TransformationMatrix offsetMatrix=TransformationMatrix.translationMatrix(x,y);
		return offsetMatrix.multiplyMatrix(this);
	}
}