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

  //Mat output = new Mat(opencv.getBufferColor().clone(), new Rect(new Point(150, 50), new Point(100, 100)));

  opencv.gray();

  Mat grayMat = opencv.getBufferGray();
  Mat roi = grayMat.submat(200, 400, 200, 400);
  opencv.setBufferGray(roi);

  //    Mat original = opencv.getBufferGray();
  //    Mat roi = new Mat(original, new Rect(200,200,400,400));
  //    Imgproc.threshold(roi, roi, 100, 255, Imgproc.THRESH_BINARY); 
  //opencv.threshold(100);
  //    Mat roi = opencv.getBufferGray().adjustROI(50,100,50,100);
  //    opencv.setBufferGray(roi);
  //opencv.threshold(100);
  //opencv.getBuffer

  //  output.setTo(new Scalar(255, 0, 0));



  dst = createImage(200, 200, ARGB);  
  opencv.toPImage(opencv.getBufferGray(), dst);
}

void draw() {
  background(125);

  int regX = mouseX;
  int regY = mouseY;

  if (regX < 1) {
    regX = 1;
  }

  if (regY < 1) {
    regY = 1;
  }

  if (regX > width - 201) {
    regX = width - 251;
  }

  if (regY > height - 251) {
    regY = height - 251;
  }

  println(regX + "," + regY + " width: " + width + " height: " + height);

  Mat grayMat = opencv.getBufferGray();
  Mat roi = grayMat.submat(regX, regY+200, regX, regY+200);
  opencv.setBufferGray(roi);

  image(dst, 0, 0);
}

