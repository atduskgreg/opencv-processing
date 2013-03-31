import gab.opencvpro.*;

OpenCVPro opencv;
PImage src;

void setup() {
  src = loadImage("test.png");
  size(src.width,src.height);
  opencv = new OpenCVPro(this, src.width, src.height);
  opencv.copy(src);

}

void draw() {
  background(0);
  image(opencv.getBuffer(), 0, 0);
}
