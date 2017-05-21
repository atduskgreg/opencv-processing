import gab.opencv.*;

OpenCV opencv;

void setup() {
  PImage src = loadImage("robot_light.jpg");
  src.resize(800, 0);
  size(800, 533);
  
  opencv = new OpenCV(this, src);  
}

void draw() {
  image(opencv.getOutput(), 0, 0); 
  PVector loc = opencv.max();
  
  stroke(255, 0, 0);
  strokeWeight(4);
  noFill();
  ellipse(loc.x, loc.y, 10, 10);
}