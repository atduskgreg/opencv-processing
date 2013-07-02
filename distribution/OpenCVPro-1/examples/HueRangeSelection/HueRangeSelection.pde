import gab.opencvpro.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Mat;
import org.opencv.core.Core;
import org.opencv.core.Scalar;

OpenCVPro opencv;
PImage img;
Histogram histogram;

int lowerb = 50;
int upperb = 100;

void setup() {
  img = loadImage("colored_balls.jpg");
  opencv = new OpenCVPro(this, img);
  size(opencv.width, opencv.height, P2D);
  opencv.useColor(HSB);
}

void draw() {
  background(0);
  image(opencv.getOutput(), 0, 0);  


  histogram = opencv.findHistogram(opencv.getBufferH(), 180);
  noStroke(); fill(0);
  histogram.draw(10, height - 230, 400, 200);
  noFill(); stroke(0);
  line(10, height-30, 410, height-30);

  text("Hue", 10, height - (textAscent() + textDescent()));

  float lb = map(lowerb, 0, 255, 0, 400);
  float ub = map(upperb, 0, 255, 0, 400);

  stroke(255, 0, 0); fill(255, 0, 0);
  strokeWeight(2);
  line(lb + 10, height-30, ub +10, height-30);
  ellipse(lb+10, height-30, 3, 3 );
  text(lowerb, lb+10, height-15);
  ellipse(ub+10, height-30, 3, 3 );
  text(upperb, ub+10, height-15);

  //  
  //  text("range: " + lowerb + " - " + upperb, 10 + 100, hImg.height*2 + hImg.height-20 + textAscent() + textDescent());
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

  //  opencv.setBufferGray(opencv.getBufferH());
  //  opencv.inRange(lowerb, upperb);
}

void drawHistogram(Histogram hist, String label, int x, int y, int w, int h) {
  pushMatrix();

  popMatrix();
}

