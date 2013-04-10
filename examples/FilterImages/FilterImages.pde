import gab.opencvpro.*;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.CvType;

OpenCVPro thresholdFilter;
PImage  img, thresh;
void setup() {
  thresholdFilter = new OpenCVPro(this, "test.jpg");
  size(thresholdFilter.width, thresholdFilter.height);

  thresholdFilter.threshold(80);

  thresh = createImage(thresholdFilter.width, thresholdFilter.height, RGB);
  thresholdFilter.toPImage(thresholdFilter.getBufferGray(), thresh);
  
  img = thresholdFilter.getInputImage();
}

void draw() {
  scale(0.5);
  image(img, 0, 0);
  image(thresh, img.width,0);
}
