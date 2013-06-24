import gab.opencvpro.*;


PImage src;
OpenCVPro opencv;

int roiWidth = 150;
int roiHeight = 150;

boolean useROI = true;

void setup() {
  src = loadImage("test.jpg");
  opencv = new OpenCVPro(this, src);
  size(opencv.width, opencv.height, P2D);
}

void draw() {
  opencv.loadImage(src);
  opencv.gray();

  // if we're using the ROI
  // and the ROI is wholly within the image
  // then set the ROI
  if (useROI &&
    mouseX > 0 && 
    mouseX < width-roiWidth && 
    mouseY > 0 && 
    mouseY < height-roiHeight) {
  
    opencv.setROI(mouseX, mouseY, roiWidth, roiHeight);
  }
  

  // threshold either the ROI or the full image
  opencv.threshold(75);
  image(opencv.getOutput(), 0, 0);
}

// toggle ROI on and off
void keyPressed() {
  useROI = !useROI;

  if (!useROI) {
    opencv.releaseROI();
  }
}

