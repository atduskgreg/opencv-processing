import gab.opencvpro.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;
import org.opencv.core.Mat;

PImage src,dilated, eroded;
OpenCVPro opencv;

void setup() {
  src = loadImage("line_drawing.jpg"); 
  size(src.width*2, src.height*2, P2D);
  opencv = new OpenCVPro(this, src);

  opencv.gray();
  opencv.threshold(70);
  dst = createImage(src.width, src.height, ARGB);
  
  Mat dilatedMat = OpenCVPro.imitate(opencv.getBufferGray());
  
  Imgproc.dilate(opencv.getBufferGray(), dilated, new Mat());
  
  opencv.toPImage(dilated, dst);
  

}

void draw(){
  background(125);
  size(0.5)
  image(src,0,0);
  image(dilated,0, src.height);  
  image(eroded,src.width, src.height);  

}
