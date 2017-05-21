import gab.opencv.*;

PImage src;
ArrayList<PVector> cornerPoints;
OpenCV opencv;

void setup() {
  src = loadImage("checkerboard.jpg");
  src.resize(500, 0);
  size(500, 333);

  opencv = new OpenCV(this, src);
  opencv.gray();
  
  cornerPoints = opencv.findChessboardCorners(9,6);
}

void draw() {
  image( opencv.getOutput(), 0, 0);
  fill(255,0,0);
  noStroke();
  for(PVector p : cornerPoints){
    ellipse(p.x, p.y, 5, 5);
  }
}