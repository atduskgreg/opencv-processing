import gab.opencvpro.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Point;
import org.opencv.core.Size;

import org.opencv.core.Scalar;

PImage src, dst;
OpenCVPro opencv;

void setup() {
  src = loadImage("test.jpg");
  opencv = new OpenCVPro(this, src);
  size(opencv.width, opencv.height, P2D);
  opencv.gray();
  
  Mat original = opencv.getBufferGray();
  Mat roi = new Mat(original, new Rect(200, 200, 400, 400));
  Imgproc.threshold(roi, roi, 100, 255, Imgproc.THRESH_BINARY);
}

void draw() {
  background(125);

  image(opencv.getOutput(), 0, 0);
}

