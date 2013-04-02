import gab.opencvpro.*;
import java.awt.Rectangle;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Size;

OpenCVPro opencv;
Mat blurMat;
PImage blurred;
void setup() {
  opencv = new OpenCVPro(this, "test.jpg");  
  size(opencv.width, opencv.height);
  //

  //Imgproc.cvtColor(opencv.getMat(), gray, Imgproc.COLOR_RGB2RGBA);
  
   //blurMat = new Mat(src.height, src.width,CvType.makeType(CvType.CV_32S, 3));
   //Imgproc.blur(opencv.getMat(), blurMat, new Size(3,3));
    //opencv.gray(opencv.getMat());
   
  // opencv.loadCascade(OpenCVPro.CASCADE_FRONTALFACE_ALT);
   //blurred = opencv.toPImage(blurMat);
 //  blurred = opencv.getGrayBuffer();
   blurred = opencv.getGrayBuffer();
}

void draw() {
  image(blurred, 0, 0);
  //image(blurred, 0,0);
//  image(opencv.getBuffer(), 0, 0);
//  Rectangle[] faces = opencv.detect();
//  
//  noFill();
//  stroke(255);
//  for(int i = 0; i < faces.length; i++){
//    rect(faces[i].x, faces[i].y, faces[i].width, faces[i].height);
//  }
}

