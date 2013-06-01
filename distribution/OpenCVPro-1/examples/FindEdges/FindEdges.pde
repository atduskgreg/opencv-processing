import gab.opencvpro.*;

OpenCVPro cannyFilter, scharrFilter, sobelFilter;
PImage src, canny, scharr, sobel;

void setup() {
  src = loadImage("test.jpg");
  size(src.width, src.height, P2D);

  cannyFilter = new OpenCVPro(this, src);
  scharrFilter = new OpenCVPro(this, src);
  sobelFilter = new OpenCVPro(this, src);
  
  sobelFilter.gray();
  
  cannyFilter.findCannyEdges(20, 75);
  scharrFilter.findScharrX(); // also try: scharrFilter.findScharrY()
  sobelFilter.findSobelEdges(1,0);

  canny = cannyFilter.getColorImage();
  sobel = sobelFilter.getGrayImage();

  // FIXME: Why doesn't this work?
  //scharr = scharrFilter.getColorImage();
  scharr = createImage(src.width, src.height, RGB);
  scharrFilter.toPImage(scharrFilter.getBufferColor(), scharr);
}


void draw() {

  pushMatrix();
  scale(0.5);
  image(src, 0, 0);
  image(canny, src.width, 0);
  image(scharr, 0, src.height);
  image(sobel, src.width, src.height);

  popMatrix();

  text("Source", 10, 25); 
  text("Canny", src.width/2 + 10, 25); 
  text("Scharr", 10, src.height/2 + 25); 
  text("Sobel", src.width/2 + 10, src.height/2 + 25);
}

