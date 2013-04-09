import gab.opencvpro.*;
import java.awt.Rectangle;
import org.opencv.core.Mat;
import org.opencv.core.CvType;

import org.opencv.imgproc.Imgproc;

OpenCVPro cannyFilter, sobelFilter;

PImage src, canny, sobel;

void setup() {
  src = loadImage("test.jpg");
  cannyFilter = new OpenCVPro(this, src);
  sobelFilter = new OpenCVPro(this, src);

  size(cannyFilter.width, cannyFilter.height);

  
  //sobel = createImage(src.width, src.height, RGB);
  //sobel = sobelFilter.findSobelEdges(2,2);
  sobel = sobelFilter.findScharrX();
  canny = cannyFilter.findCannyEdges(20, 75);
  
 // Mat src = sobelFilter.getColorBuffer();
 // Mat dst = new Mat(src.height(), src.width(), src.type());
//  
 // Imgproc.Sobel(src, dst, -1, 1, 1); 
 
 //Imgproc.Scharr(src, dst, -1, 1, 0);
 
//  
  //sobelFilter.toPImage(dst, sobel);
  
  //opencv.toPImage(opencv.findSobelEdges(20, 75), canny);

  noFill();
  stroke(0, 255, 0);
  strokeWeight(3);
}

void draw() {
  scale(0.5);
  image(src, 0, 0);
  image(canny,src.width,0);
  image(sobel,src.width, src.height);
}

