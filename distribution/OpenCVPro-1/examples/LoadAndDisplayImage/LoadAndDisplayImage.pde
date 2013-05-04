import gab.opencvpro.*;

OpenCVPro opencv;
void setup() {
  opencv = new OpenCVPro(this, "test.jpg");
  size(opencv.width, opencv.height, P2D);
}

void draw() {
    image(opencv.getOutputImage(), 0, 0);
}
