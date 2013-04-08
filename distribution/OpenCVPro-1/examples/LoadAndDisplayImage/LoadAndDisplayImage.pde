import gab.opencvpro.*;

OpenCVPro opencv;
PImage  img;
void setup() {
  opencv = new OpenCVPro(this, "test.jpg");
  size(opencv.width, opencv.height);

  img = opencv.getImage();
}

void draw() {
  image(img, 0, 0);
}
