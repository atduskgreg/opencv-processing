import gab.opencvpro.*;

OpenCV opencv;
PImage src, r, g, b;

void setup() {
  src = loadImage("testImage.png");
  opencv = new OpenCV(this, src);  
  size(int(opencv.width*1.5), opencv.height, P2D);
  
  opencv.setBufferGray(opencv.getBufferR());
  r = opencv.getSnapshot();
  
  opencv.setBufferGray(opencv.getBufferG());
  g = opencv.getSnapshot();
  
  opencv.setBufferGray(opencv.getBufferB());
  b = opencv.getSnapshot();  
}

void draw() {
  background(0);
  noTint();
  image(src, width/3,0, width/3, height/2);
  
  tint(255,0,0);
  image(r, 0, height/2, width/3, height/2);
  
  tint(0,255,0);
  image(g, width/3, height/2, width/3, height/2);
  
  tint(0,0,255);
  image(b, 2*width/3, height/2, width/3, height/2);
}
