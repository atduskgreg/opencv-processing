import gab.opencvpro.*;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

OpenCVPro thresholdFilter, blurFilter, adaptiveFilter;

PImage  img, thresh, blur, adaptive;
void setup() {
  thresholdFilter = new OpenCVPro(this, "test.jpg");
  blurFilter = new OpenCVPro(this, "test.jpg");
  adaptiveFilter = new OpenCVPro(this, "test.jpg");
  
  thresholdFilter.gray();
  blurFilter.gray();
  adaptiveFilter.gray();

  size(thresholdFilter.width, thresholdFilter.height);

  thresholdFilter.threshold(80);
  blurFilter.blur(12);  
  adaptiveFilter.adaptiveThreshold(591, 1);

  // FIXME: We need a better way to get the gray image automatically 
  //        from getOutputImage() when the processing needed it (or maybe always)
  thresh = createImage(thresholdFilter.width, thresholdFilter.height, RGB);
  thresholdFilter.toPImage(thresholdFilter.getBufferGray(), thresh);

  blur = createImage(blurFilter.width, blurFilter.height, RGB);
  blurFilter.toPImage(blurFilter.getBufferGray(), blur);

  adaptive = createImage(adaptiveFilter.width, adaptiveFilter.height, RGB);
  adaptiveFilter.toPImage(adaptiveFilter.getBufferGray(), adaptive);

  img = thresholdFilter.getInputImage();
}

void draw() {
  pushMatrix();
  scale(0.5);
  image(img, 0, 0);
  image(thresh, img.width, 0);
  image(blur, 0, img.height);
  image(adaptive, img.width, img.height);
  popMatrix();

  fill(0);
  text("source", img.width/2 - 100, 20 );
  text("threshold", img.width - 100, 20 );
  text("blur", img.width/2 - 100, img.height/2 + 20 );
  text("adaptive threshold", img.width - 150, img.height/2 + 20 );
}

