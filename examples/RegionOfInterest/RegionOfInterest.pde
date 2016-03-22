import gab.opencv.*;

PImage src;
OpenCV opencv;

int roiWidth = 150;
int roiHeight = 150;

boolean useROI = true;

void setup() {
  src = loadImage("test.jpg");
  opencv = new OpenCV(this, src);
  size(1080, 720);
  println(opencv.width, opencv.height);
}

void draw() {
  opencv.loadImage(src);

  if (useROI) {
    opencv.setROI(mouseX, mouseY, roiWidth, roiHeight);
  }

  opencv.findCannyEdges(20,75);
  image(opencv.getOutput(), 0, 0);
  
  // if an ROI is in-use then getSnapshot()
  // will return an image with the dimensions
  // and content of the ROI
  if(useROI){
    image(opencv.getSnapshot(), width-roiWidth,0);
  }
}

// toggle ROI on and off
void keyPressed() {
  useROI = !useROI;

  if (!useROI) {
    opencv.releaseROI();
  }
}