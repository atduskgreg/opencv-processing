import gab.opencvpro.*;
import opencv.core.Mat;

OpenCVPro opencv;
void setup() {
  opencv = new OpenCVPro(this, "test.jpg");
  size(opencv.width, opencv.height);
}

void draw() {
    image(opencv.getOutput(), 0, 0);
}
