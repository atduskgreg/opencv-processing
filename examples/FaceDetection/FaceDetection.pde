import gab.opencvpro.*;
import java.awt.Rectangle;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

OpenCVPro opencv;

void setup() {
  PImage src = loadImage("test.jpg");
  size(src.width, src.height);
  opencv = new OpenCVPro(this, src.width, src.height);
  
  println("32F: " + CvType.CV_32F + " CV_8UC3: " + CvType.CV_8UC3 + " 8u1: " + CvType.makeType(CvType.CV_8U, 1));
  
  //opencv.copy(src);
  opencv.loadImage("test.jpg");

  Mat gray = new Mat(opencv.getMat().size(), CvType.makeType(opencv.getMat().depth(), opencv.getMat().channels()));
  
  println("gray type is: " + CvType.typeToString(gray.type()));
  println("mat type is: " + CvType.typeToString(opencv.getMat().type()));
  
  
  //Imgproc.cvtColor(opencv.getMat(), gray, Imgproc.COLOR_RGB2RGBA);
  
  
 opencv.loadCascade(OpenCVPro.CASCADE_FRONTALFACE_ALT);
}
/*
void draw() {
  //image(opencv.getBuffer(), 0, 0);
  Rectangle[] faces = opencv.detect();
  
  noFill();
  stroke(255);
  for(int i = 0; i < faces.length; i++){
    rect(faces[i].x, faces[i].y, faces[i].width, faces[i].height);
  }
}
*/
