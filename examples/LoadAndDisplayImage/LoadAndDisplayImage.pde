import gab.opencvpro.*;

OpenCVPro opencv;

void setup() {
  PImage src = loadImage("test.png");
  size(src.width, src.height);
  opencv = new OpenCVPro(this, src.width, src.height);
  opencv.copy(src);
}

void draw() {
  image(opencv.getBuffer(), 0, 0);
}
