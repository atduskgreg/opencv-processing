import gab.opencvpro.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;

import org.opencv.core.Point;

//import java.util.list;

OpenCVPro opencv;
PImage  src, dst;
ArrayList<MatOfPoint> contours;
ArrayList<MatOfPoint2f> approximations;
ArrayList<MatOfPoint2f> markers;

void setup() {
  opencv = new OpenCVPro(this, "marker_test.jpg");
  size(opencv.width, opencv.height/2);
  src = opencv.getInputImage();

  opencv.gray();

  Mat thresholdMat = OpenCVPro.imitate(opencv.getBufferGray());

  opencv.blur(5);
  Imgproc.adaptiveThreshold(opencv.getBufferGray(), thresholdMat, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 451, -65);

  contours = new ArrayList<MatOfPoint>();
  Imgproc.findContours(thresholdMat, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);

  approximations = createPolygonApproximations(contours);
  
  markers = new ArrayList<MatOfPoint2f>();
  markers = selectMarkers(approximations);

  dst  = createImage(src.width, src.height, RGB);
  opencv.toPImage(thresholdMat, dst);
}

ArrayList<MatOfPoint2f> selectMarkers(ArrayList<MatOfPoint2f> candidates){
  float minAllowedContourSide = 50;
  minAllowedContourSide = minAllowedContourSide * minAllowedContourSide;
  
  ArrayList<MatOfPoint2f> result = new ArrayList<MatOfPoint2f>();
  
  for(MatOfPoint2f candidate : candidates){
    
    if(candidate.size().height != 4){
      continue;
    } 
    
    if(!Imgproc.isContourConvex(new MatOfPoint(candidate.toArray()))){
      continue;
    }
    
    // eliminate markers where consecutive
    // points are too close together
    float minDist = src.width * src.width;
    Point[] points = candidate.toArray();
    for(int i = 0; i < points.length; i++){
      Point side = new Point(points[i].x - points[(i+1)%4].x, points[i].y - points[(i+1)%4].y);
      float squaredLength = (float)side.dot(side);
      println("minDist: " + minDist  + " squaredLength: " +squaredLength);
      minDist = min(minDist, squaredLength);
    }
    
     println(minDist);

    
    if(minDist < minAllowedContourSide){
      continue;
    }
    
    
    
    
    result.add(candidate);
    
  }
  
  return result;
}

ArrayList<MatOfPoint2f> createPolygonApproximations(ArrayList<MatOfPoint> cntrs) {
  ArrayList<MatOfPoint2f> result = new ArrayList<MatOfPoint2f>();
  
  double epsilon = cntrs.get(0).size().height * 0.01;
  println(epsilon);
  
  for (MatOfPoint contour : cntrs) {
      MatOfPoint2f approx = new MatOfPoint2f();
      Imgproc.approxPolyDP(new MatOfPoint2f(contour.toArray()), approx, epsilon, true);
      result.add(approx);
  }
  
  return result;
}

void drawContours(ArrayList<MatOfPoint> cntrs) {
  for (MatOfPoint contour : cntrs) {
    beginShape();
    Point[] points = contour.toArray();
    for (int i = 0; i < points.length; i++) {
      vertex((float)points[i].x, (float)points[i].y);
    }
    endShape();
  }
}

void drawContours2f(ArrayList<MatOfPoint2f> cntrs) {
  for (MatOfPoint2f contour : cntrs) {
    beginShape();
    Point[] points = contour.toArray();
    
    pushStyle();
    pushMatrix();
    scale(2);
    fill(255);
    text(points.length, (float)points[0].x/2, (float)points[0].y/2);
    popMatrix();
    popStyle();
    for (int i = 0; i < points.length; i++) {
      vertex((float)points[i].x, (float)points[i].y);
    }
    endShape(CLOSE);
  }
}

void draw() {
  pushMatrix();
  scale(0.5);
  image(src, 0, 0);
  image(dst, src.width, 0);
  noFill();
  stroke(0, 255, 0);
  smooth();
  strokeWeight(3);
  translate(src.width, 0);
  drawContours(contours);
  
  stroke(255,0, 0);
  drawContours2f(approximations);
  
  stroke(0,0,255);
  drawContours2f(markers);
  
  popMatrix();
  
    fill(0,255,0);
  text("contours", src.width/2 + 10, 20);
  
  fill(255,0,0);
  text("polygon approximations", src.width/2 + 10, 40);
  
  fill(255);
  text("per-polygon point count", src.width/2 + 10, 60);
 
  fill(0,0,255);
  text("marker candidates after filtering",  src.width/2 + 10, 80);
  
  
}

