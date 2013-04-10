import gab.opencvpro.*;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

OpenCVPro thresholdFilter;
PImage  img, thresh;
void setup() {
  thresholdFilter = new OpenCVPro(this, "test.jpg");
  size(thresholdFilter.width, thresholdFilter.height);
  
  

  img = opencv.getInputImage();
}

void draw() {
  scale(0.5);
  image(img, 0, 0);
  img
}
