import gab.opencvpro.*;

PImage src, dst;
OpenCVPro opencv;

ArrayList<Contour> contours;

void setup() {
  src = loadImage("test.jpg"); 
  size(src.width, src.height/2, P2D);
  opencv = new OpenCVPro(this, src);

  opencv.gray();
  opencv.threshold(70);
  dst = opencv.getGrayImage();

  contours = opencv.findContours();
  println("found " + contours.size() + " contours");

}

void draw() {
  scale(0.5);
  image(src, 0, 0);
  image(dst, src.width, 0);
  
  noFill();
  stroke(0,255,0);
  strokeWeight(3);
  for (Contour contour : contours) {
    beginShape();
    for (PVector point : contour.getPoints()) {
      vertex(point.x, point.y);
    }
    endShape();
  }
}
