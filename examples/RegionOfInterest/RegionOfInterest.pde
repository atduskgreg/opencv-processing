import gab.opencv.*;

PImage src;
OpenCV opencv;

int roiWidth = 150;
int roiHeight = 150;

boolean useROI = true;

void setup() {
  src = loadImage("test.jpg");
  opencv = new OpenCV(this, src);
  size(opencv.width, opencv.height);
}

void draw() {
  opencv.loadImage(src);

  if (useROI) {
    opencv.setROI(mouseX, mouseY, roiWidth, roiHeight);
  }

  opencv.findCannyEdges(20,75);
  image(opencv.getOutput(), 0, 0);
}

// toggle ROI on and off
void keyPressed() {
  useROI = !useROI;

  if (!useROI) {
    opencv.releaseROI();
  }
}

