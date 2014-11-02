package gab.opencv;

import java.util.ArrayList;
import processing.core.*;

import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Rect;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;

import java.awt.Rectangle;


public class Contour {
	private ArrayList<PVector> points;
	private Point[] inputPoints;
	private double polygonApproximationFactor;
	private PApplet parent;
	private Rectangle boundingBox;
	public MatOfPoint pointMat;
	
	public Contour(PApplet parent, MatOfPoint mat){
		polygonApproximationFactor = mat.size().height * 0.01;
		this.parent = parent;
		this.pointMat = mat;
		
		Rect r = Imgproc.boundingRect(mat);
		boundingBox = new Rectangle(r.x, r.y, r.width, r.height);
	    loadPoints(mat.toArray());
	}
	
	public Contour(PApplet parent, MatOfPoint2f mat){
		polygonApproximationFactor = mat.size().height * 0.01;
		this.parent = parent;
		this.pointMat = new MatOfPoint(mat.toArray());
		
		Rect r = Imgproc.minAreaRect(mat).boundingRect();
		boundingBox = new Rectangle(r.x, r.y, r.width, r.height);
		loadPoints(mat.toArray());	    
	}
	
	public void loadPoints(Point[] pts){
	    points = new ArrayList<PVector>();
	    inputPoints = pts;
	    
		for(int i = 0; i < inputPoints.length; i++){
			points.add(new PVector((float)inputPoints[i].x, (float)inputPoints[i].y));
		}
	}
	
	/**
	 * Check if the Contour contains a given x-y point.
	 * Particularly, useful for interaction via mouseX and mouseY.
	 * 
	 * @param x
	 * @param y
	 * @return boolean 
	 */
	public boolean containsPoint(int x, int y){
		Point p = new Point(x,y);
		MatOfPoint2f m = new MatOfPoint2f(pointMat.toArray());
		    
		double r = Imgproc.pointPolygonTest(m,p, false);
		return r == 1;
	}
	
	/**
	 * The polygonApproximationFactor is used to determine
	 * how strictly to follow a curvy polygon when converting
	 * it into a simpler polygon with getPolygonApproximation().
	 * For advanced use only. Set to a sane value by default.	
	 * 
	 * @param polygonApproximationFactor, a double
	 */
	public void setPolygonApproximationFactor(double polygonApproximationFactor){
		this.polygonApproximationFactor = polygonApproximationFactor;
	}
	
	/**
	 * Access the current polygonApproximationFactor. The polygonApproximationFactor
	 * is used to determine how strictly to follow a curvy polygon when converting
	 * it into a simpler polygon with getPolygonApproximation().
	 * 
	 * @return polygonApproximationFactor, a double
	 */
	public double getPolygonApproximationFactor(){
		return polygonApproximationFactor;
	}
	
	/**
	 * Get a new Contour that results from calculating
	 * the polygon approximation of the current Contour.
	 * The tightness of the approximation is set by the polygonApproximationFactor,
	 * See setPolygonApproximationFactor() and getPolygonApproximationFactor().
	 * 
	 * @return
	 */
	public Contour getPolygonApproximation(){
		MatOfPoint2f approx = new MatOfPoint2f();
	    Imgproc.approxPolyDP(new MatOfPoint2f(inputPoints), approx, polygonApproximationFactor, true);
		return new Contour(parent, approx);
	}
	
	/**
	 * Calculate a convex hull from the current Contour.
	 * Returns a new Contour representing the convex hull.
	 * 
	 * @return Contour
	 */
	public Contour getConvexHull(){
		  MatOfInt hull = new MatOfInt();
		  MatOfPoint points = new MatOfPoint(pointMat);
		  
		  
		  Imgproc.convexHull(points, hull);
		  Point[] hp = new Point[hull.height()];
		  
		  for(int i = 0; i < hull.height(); i++){
			  int index = (int)hull.get(i,0)[0];
			  hp[i] = new Point(pointMat.get(index,0));
		  }
		  MatOfPoint hullPoints = new MatOfPoint();
		  hullPoints.fromArray(hp);

		  return new Contour(parent, hullPoints);
	}
	
	/**
	 * Draw the Contour as a closed shape with one vertex per-point.
	 * 
	 */
	public void draw(){
		parent.beginShape();
		for (PVector p : points) {
			parent.vertex(p.x, p.y);
		}
		parent.endShape(PConstants.CLOSE);

	}
	/**
	 * Get the points that make up the Contour.
	 * 
	 * @return ArrayList<PVector> points
	 */
	public ArrayList<PVector> getPoints(){
		return points;
	}
	
	/**
	 * The number of points in the Contour.
	 * 
	 * @return int
	 */
	public int numPoints(){
		return points.size();
	}
	
	/**
	 * Get the bounding box for the Contour.
	 * 
	 * @return A java.awt.Rectangle
	 */
	public Rectangle getBoundingBox(){
		return boundingBox;
	}
	
	/**
	 * The area of the Contour's bounding box. In most cases, this is a good approximation for the Contour's area.
	 * 
	 * @return float area
	 */
	public float area(){
		return (boundingBox.width * boundingBox.height);
	}
}



