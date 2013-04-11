import gab.opencvpro.*;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.CvType;
import org.opencv.core.Size;

OpenCVPro thresholdFilter, blurFilter;
PImage  img, thresh, blur;
void setup() {
  thresholdFilter = new OpenCVPro(this, "test.jpg");
  blurFilter = new OpenCVPro(this, "test.jpg");
  
  size(thresholdFilter.width, thresholdFilter.height);

  thresholdFilter.threshold(80);
  blurFilter.blur(12);

  thresh = createImage(thresholdFilter.width, thresholdFilter.height, RGB);
  thresholdFilter.toPImage(thresholdFilter.getBufferGray(), thresh);
  
  
  
  blur = createImage(blurFilter.width, thresholdFilter.height, RGB);
  blurFilter.toPImage(blurFilter.getBufferGray, blur);
  
//  blurFilter.gray();
//  Mat gray = blurFilter.getBufferGray();
//  
//  Mat dst = new Mat(gray.height(), gray.width(), gray.type());
//  
//  Imgproc.blur(gray, dst, new Size((double)12, (double)12));
//  
//  blurFilter.toPImage(dst, blur);
//  
  img = thresholdFilter.getInputImage();
}

void draw() {
  pushMatrix();
  scale(0.5);
  image(img, 0, 0);
  image(thresh, img.width,0);
  image(blur, 0,img.height);
  popMatrix();
  
  fill(0);
  text("source", img.width/2 - 100, 20 );
  text("threshold", img.width - 100, 20 );
  text("blur", img.width/2 - 100, img.height/2 + 20 );
}
