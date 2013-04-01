import gab.opencvpro.*;
import java.awt.Rectangle;
import org.opencv.core.CvType;

OpenCVPro opencv;

void setup() {
  PImage src = loadImage("test.jpg");
  size(src.width, src.height);
  opencv = new OpenCVPro(this, src.width, src.height);
  
  println("32F: " + CvType.CV_32F + " 16U: " + CvType.CV_16U);
  
  opencv.copy(src);
  
  
  
  opencv.loadCascade(OpenCVPro.CASCADE_FRONTALFACE_ALT);
}

void draw() {
  image(opencv.getBuffer(), 0, 0);
  Rectangle[] faces = opencv.detect();
  
  noFill();
  stroke(255);
  for(int i = 0; i < faces.length; i++){
    rect(faces[i].x, faces[i].y, faces[i].width, faces[i].height);
  }
}
