import gab.opencvpro.*;

import org.opencv.core.Core;
import org.opencv.core.Scalar;
import org.opencv.core.Mat;


PImage img;
OpenCVPro opencv;

void setup(){
  img = loadImage("test.jpg");
  size(img.width, img.height, P2D);
  opencv = new OpenCVPro(this, img);
  
}

void draw(){
  opencv.loadImage(img);
  opencv.brightness((int)map(mouseX, 0, width, -255, 255));
  image(opencv.getOutput(),0,0);
}

