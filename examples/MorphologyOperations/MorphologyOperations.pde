import gab.opencv.*;

import org.opencv.imgproc.Imgproc;

OpenCV opencv;
PImage  img, opened, closed, tophat;

void setup() {
  img = loadImage("test.jpg");
  size(1280, 720);

  opencv = new OpenCV(this, img);
  PImage snap = opencv.getSnapshot();
 
  opencv.open(16);
  opened = opencv.getSnapshot();
  
  opencv.loadImage(snap);
  opencv.close(16);  
  closed = opencv.getSnapshot();
  
  opencv.loadImage(snap);
  opencv.morphX(Imgproc.MORPH_TOPHAT, Imgproc.MORPH_CROSS, 8, 8);
  tophat = opencv.getSnapshot();
}

void draw() {
  pushMatrix();
  scale(0.5);
  image(img, 0, 0);
  image(opened, img.width, 0);
  image(closed, 0, img.height);
  image(tophat, img.width, img.height);
  popMatrix();

  fill(0);
  text("source", img.width/2 - 100, 20 );
  text("open(16)", img.width - 100, 20 );
  text("close(16)", img.width/2 - 100, img.height/2 + 20 );
  fill(255);
  text("tophat(cross, 8, 8)", img.width - 150, img.height/2 + 20 );
}