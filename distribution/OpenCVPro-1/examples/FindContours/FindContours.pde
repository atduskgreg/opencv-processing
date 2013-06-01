import gab.opencvpro.*;

import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

PImage src, dst;
OpenCVPro opencv;

ArrayList<Contour> contours;

void setup() {
  src = loadImage("test.jpg"); 
  size(src.width, src.height/2, P2D);
  opencv = new OpenCVPro(this, src);
  
  opencv.gray();
  opencv.threshold(50);
  
  contours = findContours(opencv.getBufferGray());
  dst = opencv.getGrayImage();
  
  println("numContours: " + contours.size());
}

void draw() {
  scale(0.5);
  image(src, 0, 0);
  image(dst, src.width, 0);
  
  noFill();
  stroke(255);
  for(Contour contour : contours){
    if(contour.points.size() > 20 && contour.points.size() < 1000){
      contour.draw();
    }
  }
}

void drawContour(Contour c){
  void draw(){
    beginShape();
    for(PVector point : c.points){
      vertex(point.x, point.y);
    }
    endShape();
  }
}

// find contours works on a binary image,
// so you have to threshold it (or otherwise make it binary) first
// in order to get a good result
ArrayList<Contour> findContours(Mat buffer){
  ArrayList<MatOfPoint> cs = new ArrayList<MatOfPoint>();
  Imgproc.findContours(buffer, cs, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
    
  ArrayList<Contour> result = new ArrayList<Contour>();
  for(MatOfPoint c : cs){
    result.add(new Contour(c));
  }
  
  
  return result;
}
