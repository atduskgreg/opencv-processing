import gab.opencvpro.*;

OpenCVPro opencv;
PImage threshold, blur;

void setup() {
  PImage img = loadImage("test.jpg");
  size(img.width, img.height/2);

  // By default, OpenCVPro works with a gray
  // version of the source image
  opencv = new OpenCVPro(this, img);
  // but you can tell it explicitly to use color instead:
  opencv.useColor();  

  // A lot of OpenCV operations only work on gray.
  // But some do work in color, like threshold, blur, findCannyEdges, findChessboardCorners, etc.:
  opencv.threshold(75);
  threshold = opencv.getSnapshot();

  opencv.blur(30);
  blur = opencv.getSnapshot();

  // If you try an operation that does not work in color
  // it will raise an exception and print out a message:
  try{
    opencv.adaptiveThreshold(591, 1);
  } catch (Exception e){
  }

}

void draw() {
  scale(0.5);
  image(threshold, 0, 0);
  image(blur, threshold.width,0);
}

