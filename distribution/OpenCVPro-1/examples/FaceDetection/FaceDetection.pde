import gab.opencvpro.*;
import java.awt.Rectangle;

OpenCVPro opencv;

PImage img;
Rectangle[] faces;

void setup() {
  opencv = new OpenCVPro(this, "test.jpg");
  size(opencv.width, opencv.height);

  img = opencv.getInput();

  opencv.loadCascade(OpenCVPro.CASCADE_FRONTALFACE_ALT);  
  faces = opencv.detect();

  noFill();
  stroke(0, 255, 0);
  strokeWeight(3);
}

void draw() {
  image(img, 0, 0);

  for (int i = 0; i < faces.length; i++) {
    rect(faces[i].x, faces[i].y, faces[i].width, faces[i].height);
  }
}

