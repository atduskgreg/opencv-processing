import gab.opencvpro.*;

OpenCVPro thresholdFilter, blurFilter;
PImage  img, thresh, blur;
void setup() {
  thresholdFilter = new OpenCVPro(this, "test.jpg");
  blurFilter = new OpenCVPro(this, "test.jpg");
  
  size(thresholdFilter.width, thresholdFilter.height);

  thresholdFilter.threshold(80);
  blurFilter.blur(12);

  // FIXME: We need a better way to get the gray image automatically 
  //        from getOutputImage() when the processing needed it (or maybe always)
  thresh = createImage(thresholdFilter.width, thresholdFilter.height, RGB);
  thresholdFilter.toPImage(thresholdFilter.getBufferGray(), thresh);
    
  blur = createImage(blurFilter.width, thresholdFilter.height, RGB);
  blurFilter.toPImage(blurFilter.getBufferGray(), blur);

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
