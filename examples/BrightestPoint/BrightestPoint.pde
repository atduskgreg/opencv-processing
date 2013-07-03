import gab.opencvpro.*;

OpenCVPro opencv;

void setup() {
  PImage src = loadImage("robot_light.jpg");
  src.resize(800, 0);
  size(src.width, src.height);
  
  opencv = new OpenCVPro(this, src);  
}

void draw() {
  image(opencv.getOutput(), 0, 0); 
  PVector loc = opencv.max();
  
  stroke(255, 0, 0);
  strokeWeight(4);
  noFill();
  ellipse(loc.x, loc.y, 10, 10);
}

