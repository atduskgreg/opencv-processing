import gab.opencv.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Mat;
import java.awt.geom.Line2D;

OpenCV opencv;

ArrayList<Line2D> lines;

void setup() {
  PImage src = loadImage("film_scan.jpg");
  src.resize(0, 800);
  size(src.width, src.height, P2D);

  opencv = new OpenCV(this, src);
  opencv.findSobelEdges(1, 0);
  opencv.threshold(20);

  lines = findLines(opencv.getGray(), 200, 120, 5);
  
  for(Line2D l : lines){
    println(Math.toDegrees(angleOf(l)));
  }
  
  noLoop();
}

double angleOf(Line2D l){
   Line2D vertical = new Line2D.Double(0,0, 0,1);
   
   return angleBetween(l, vertical);
}

double angleBetween(Line2D line1, Line2D line2) {
  double angle1 = Math.atan2(line1.getY1() - line1.getY2(), 
  line1.getX1() - line1.getX2());
  double angle2 = Math.atan2(line2.getY1() - line2.getY2(), 
  line2.getX1() - line2.getX2());
  return angle1-angle2;
}

ArrayList<Line2D> findLines(Mat m, int threshold, double minLength, double maxGap) {
  ArrayList<Line2D> result = new ArrayList<Line2D>();

  Mat lineMat = new Mat();
  Imgproc.HoughLinesP(m, lineMat, 1, PI/180.0, threshold, minLength, maxGap);
  for (int i = 0; i < lineMat.width(); i++) {
    double[] coords = lineMat.get(0, i);
    result.add(new Line2D.Double(coords[0], coords[1], coords[2], coords[3]));
  }

  return result;
}

void draw() {
  image(opencv.getOutput(), 0, 0);
  stroke(0, 255, 0);
  strokeWeight(2);
  for (Line2D line : lines) {
    line((float)line.getX1(), (float)line.getY1(), (float)line.getX2(), (float)line.getY2());
  }
}

