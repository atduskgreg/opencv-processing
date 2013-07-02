import gab.opencvpro.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Mat;
import org.opencv.core.Core;
import org.opencv.core.Scalar;

OpenCVPro opencv;
PImage img, hImg, sImg, vImg;

Histogram hHist, sHist, vHist;

Mat filteredMat;

int lowerb = 50;
int upperb = 100;

void setup() {
  img = loadImage("rainbow.jpg");
  img.resize(400, 0);
  opencv = new OpenCVPro(this, img);
  size(opencv.width * 3, opencv.height * 3, P2D);

  // API TODO:
  opencv.useColor(HSB);
 // opencv.getBufferHue();
//
//  Imgproc.cvtColor(opencv.getBufferColor(), opencv.getBufferColor(), Imgproc.COLOR_BGR2HSV);
//
//  ArrayList<Mat> channels = new ArrayList<Mat>();
//  Core.split(opencv.getBufferColor(), channels);

  filteredMat = opencv.getBufferH().clone();

  hHist = opencv.findHistogram(opencv.getBufferH(), 255);
  sHist = opencv.findHistogram(opencv.getBufferS(), 255);
  vHist = opencv.findHistogram(opencv.getBufferV(), 255);

  hImg = createImage(opencv.width, opencv.height, ARGB);
  opencv.toPImage(opencv.getBufferH(), hImg);

  sImg = createImage(opencv.width, opencv.height, ARGB);
  opencv.toPImage(opencv.getBufferS(), sImg);

  vImg = createImage(opencv.width, opencv.height, ARGB);
  opencv.toPImage(opencv.getBufferV(), vImg);
  
}

void draw() {
  background(0);



  image(img, img.width, 0);
  image(hImg, 0, img.height);
  image(sImg, hImg.width, img.height);
  image(vImg, hImg.width*2, img.height);


  stroke(255);
  fill(255);

  drawHistogram(hHist, "Hue", 10, hImg.height*2, hImg.width-20, hImg.height-20);
  drawHistogram(sHist, "Saturation", hImg.width+10, hImg.height*2, hImg.width-20, hImg.height-20);
  drawHistogram(vHist, "Value", (hImg.width+10)*2, hImg.height*2, hImg.width-20, hImg.height-20);


  stroke(255, 0, 0);
  fill(255, 0, 0);

  float lb = map(lowerb, 0, 255, 0, hImg.width-20);
  float ub = map(upperb, 0, 255, 0, hImg.width-20);

  strokeWeight(2);
  line(lb + 10, img.height*3 - 20, ub +10, img.height*3 - 20);
  ellipse(lb+10, hImg.height*3 - 20, 3, 3 );
  ellipse(ub+10, hImg.height*3 - 20, 3, 3 );
  
  text("range: " + lowerb + " - " + upperb, 10 + 100, hImg.height*2 + hImg.height-20 + textAscent() + textDescent());

}

void mouseMoved() {
  if (keyPressed) {
    upperb += mouseX - pmouseX;
  } 
  else {
    if (upperb <255 || (mouseX - pmouseX) < 0) {
      lowerb += mouseX - pmouseX;
    }
    
    if (lowerb > 0 || (mouseX - pmouseX) > 0) {
      upperb += mouseX - pmouseX;
    }
  }

  upperb = constrain(upperb, lowerb, 255);
  lowerb = constrain(lowerb, 0, upperb);

  println("upperb: " + upperb + " lowerb: " + lowerb);

  Core.inRange(opencv.getBufferH(), new Scalar(lowerb), new Scalar(upperb), filteredMat);

  opencv.toPImage(filteredMat, hImg);
}

void drawHistogram(Histogram hist, String label, int x, int y, int w, int h) {
  pushMatrix();
  hist.draw(x, y, w, h);
  text(label, x, y + h + textAscent() + textDescent());
  popMatrix();
}

