import gab.opencvpro.*;

PImage src;
ArrayList<PVector> cornerPoints;
OpenCVPro opencv;
void setup() {
  src = loadImage("checkerboard.jpg");
  src.resize(500, 0);
  size(src.width, src.height, P2D);

  opencv = new OpenCVPro(this, src);
  opencv.gray();
  
  cornerPoints = opencv.findChessboardCorners(9,6);
}

void draw() {
  image( opencv.getGrayImage(), 0, 0);
  fill(255,0,0);
  noStroke();
  for(PVector p : cornerPoints){
    ellipse(p.x, p.y, 5, 5);
  }
}
