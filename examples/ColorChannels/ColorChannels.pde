import gab.opencvpro.*;

OpenCV opencv;
PImage src, r, g, b, h, s, v;

int imgH, imgW;

void setup() {
  src = loadImage("testImage.png");
  opencv = new OpenCV(this, src);  
  size(int(opencv.width*1.5), int(opencv.height * 1.5), P2D);
  
  imgH = src.height/2;
  imgW = src.width/2;
  
  opencv.setBufferGray(opencv.getBufferR());
  r = opencv.getSnapshot();
  
  opencv.setBufferGray(opencv.getBufferG());
  g = opencv.getSnapshot();
  
  opencv.setBufferGray(opencv.getBufferB());
  b = opencv.getSnapshot();  
  
  opencv.useColor(HSB);
  opencv.setBufferGray(opencv.getBufferH());
  h = opencv.getSnapshot();
  
  opencv.setBufferGray(opencv.getBufferS());
  s = opencv.getSnapshot();
  
  opencv.setBufferGray(opencv.getBufferV());
  v = opencv.getSnapshot();
}

void draw() {
  background(0);
  noTint();
  image(src, imgW,0, imgW, imgH);
  
  tint(255,0,0);
  image(r, 0, imgH, imgW, imgH);
  
  tint(0,255,0);
  image(g, imgW, imgH, imgW, imgH);
  
  tint(0,0,255);
  image(b, 2*imgW, imgH, imgW, imgH);
  
  noTint();
  image(h, 0, 2*imgH, imgW, imgH);
  image(s, imgW, 2*imgH, imgW, imgH);
  image(v, 2*imgW, 2*imgH, imgW, imgH);

}
