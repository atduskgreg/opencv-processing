import gab.opencvpro.*;
import java.awt.Rectangle;
import org.opencv.core.Mat;
import org.opencv.core.CvType;

import org.opencv.imgproc.Imgproc;

OpenCVPro cannyFilter, sobelFilter;

PImage src, canny, scharrX, scharrY;

void setup() {
  src = loadImage("test.jpg");
  cannyFilter = new OpenCVPro(this, src);
  sobelFilter = new OpenCVPro(this, src);

  size(cannyFilter.width, cannyFilter.height);


  scharrX = createImage(src.width, src.height, RGB);
  scharrY = createImage(src.width, src.height, RGB);


  canny = cannyFilter.findCannyEdges(20, 75);

  Mat src = sobelFilter.getColorBuffer();
  Mat dstX = new Mat(src.height(), src.width(), src.type());
  Mat dstY = new Mat(src.height(), src.width(), src.type());

  //  
  // Imgproc.Sobel(src, dst, -1, 1, 1); 

  Imgproc.Scharr(src, dstX, -1, 1, 0);
  Imgproc.Scharr(src, dstY, -1, 0, 1);

  sobelFilter.toPImage(dstX, scharrX);
  sobelFilter.toPImage(dstY, scharrY);


  noFill();
  stroke(0, 255, 0);
  strokeWeight(3);
}

void draw() {
  scale(0.5);
  image(src, 0, 0);
  image(canny, src.width, 0);
  image(scharrX, 0, src.height);
  image(scharrY, src.width, src.height);
}

