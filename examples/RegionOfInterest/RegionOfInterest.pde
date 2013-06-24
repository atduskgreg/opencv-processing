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

  if (useROI){
    opencv.setROI(mouseX, mouseY, roiWidth, roiHeight);    
  }

  // threshold applies to ROI if it is set
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

