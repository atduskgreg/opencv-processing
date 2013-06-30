import gab.opencvpro.*;
import java.awt.Rectangle;

OpenCVPro opencv;
Rectangle[] faces;

void setup() {
  opencv = new OpenCVPro(this, "test.jpg");
  size(opencv.width, opencv.height);

  opencv.loadCascade(OpenCVPro.CASCADE_FRONTALFACE_ALT);  
  faces = opencv.detect();
}

void draw() {
  image(opencv.getInput(), 0, 0);

  noFill();
  stroke(0, 255, 0);
  strokeWeight(3);
  for (int i = 0; i < faces.length; i++) {
    rect(faces[i].x, faces[i].y, faces[i].width, faces[i].height);
  }
}

