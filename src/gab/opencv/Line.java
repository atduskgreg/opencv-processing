package gab.opencv;

import processing.core.*;

public class Line {
	public PVector start, end;
	public double angle;
	public double x1, y1, x2, y2;
	
	public Line(double x1, double y1, double x2, double y2){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		
		start = new PVector((float)x1, (float)y1);
		end = new PVector((float)x2, (float)y2);
		
		// measure the angle between this line
		// and a vertical line oriented up
		angle = angleBetween(x1, y1, x2, y2, 0, 0, 0, -1);
	}
	
	public double angleFrom(Line other){
		return angleBetween(x1, y1, x2, y2, other.x1, other.y1, other.x2, other.y2);
	}
	
	
	public static double angleBetween(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
		double angle1 = Math.atan2(y1 - y2, x1 - x2);
		double angle2 = Math.atan2(y3 - y4, x3 - x4);
		return angle1-angle2;
	}
}
