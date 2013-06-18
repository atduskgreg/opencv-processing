package gab.opencvpro;

import java.util.ArrayList;
import processing.core.*;

import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.imgproc.Imgproc;

import org.opencv.core.Point;

public class Contour {
	private ArrayList<PVector> points;
	private Point[] inputPoints;
	private double polygonApproximationFactor;
	PApplet parent;
	
	public Contour(PApplet parent, MatOfPoint mat){
		polygonApproximationFactor = mat.size().height * 0.01;
		this.parent = parent;
	    loadPoints(mat.toArray());
	}
	
	public Contour(PApplet parent, MatOfPoint2f mat){
		polygonApproximationFactor = mat.size().height * 0.01;
		this.parent = parent;
	    loadPoints(mat.toArray());	    
	}
	
	public void loadPoints(Point[] pts){
	    points = new ArrayList<PVector>();
	    inputPoints = pts;
	    
		for(int i = 0; i < inputPoints.length; i++){
			points.add(new PVector((float)inputPoints[i].x, (float)inputPoints[i].y));
		}
	}
	
	// The polygonApproximationFactor is used to determine
	// how strictly to follow a curvy polygon when converting
	// it into a simpler polygon with getPolygonApproximation().
	// For advanced use only. Set to a sane value by default.	
	public void setPolygonApproximationFactor(double polygonApproximationFactor){
		this.polygonApproximationFactor = polygonApproximationFactor;
	}
	
	public double getPolygonApproximationFactor(){
		return polygonApproximationFactor;
	}
	
	public Contour getPolygonApproximation(){
		MatOfPoint2f approx = new MatOfPoint2f();
	    Imgproc.approxPolyDP(new MatOfPoint2f(inputPoints), approx, polygonApproximationFactor, true);
		return new Contour(parent, approx);
	}
	
	public void draw(){
		for (PVector p : points) {
		    parent.beginShape();
		      parent.vertex(p.x, p.y);
		    parent.endShape();
		  }
	}
	
	public ArrayList<PVector> getPoints(){
		return points;
	}
	
	public int numPoints(){
		return points.size();
	}
	
}

