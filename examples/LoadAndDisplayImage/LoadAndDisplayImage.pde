import gab.opencv.*;

OpenCV opencv;

void setup() {
  opencv = new OpenCV(this, "test.jpg");
  size(opencv.width, opencv.height);
}

void draw() {
  image(opencv.getOutput(), 0, 0);
}

