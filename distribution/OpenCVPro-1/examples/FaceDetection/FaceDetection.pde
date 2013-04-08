import gab.opencvpro.*;
import java.awt.Rectangle;
import org.opencv.core.Mat;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.imgproc.Imgproc;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;


OpenCVPro opencv;
PImage img, gray;
void setup() {
  opencv = new OpenCVPro(this, "test.jpg");
  size(opencv.width, opencv.height);

  opencv.loadCascade(OpenCVPro.CASCADE_FRONTALFACE_ALT);  
  img = opencv.getImage();

  gray = createImage(img.width, img.height, RGB); 
  
  Mat grayscale = opencv.gray(opencv.getColorBuffer());

  opencv.toPImage(grayscale, gray);

}

void draw() {
  scale(0.5, 0.5);
  image(img, 0, 0);
  image(gray, img.width, 0);
}

