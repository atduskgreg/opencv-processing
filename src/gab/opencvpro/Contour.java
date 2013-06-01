package gab.opencvpro;

import java.util.ArrayList;
import processing.core.*;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

public class Contour {
	private ArrayList<PVector> points;
	
	public Contour(MatOfPoint mat){
	    points = new ArrayList<PVector>();
	    Point[] inputPoints = mat.toArray();
	    for(int i = 0; i < inputPoints.length; i++){
	      points.add(new PVector((float)inputPoints[i].x, (float)inputPoints[i].y));
	    }
	}
	
	public ArrayList<PVector> getPoints(){
		return points;
	}
	
	public int numPoints(){
		return points.size();
	}
}

