import gab.opencvpro.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;

OpenCVPro beforeCv, afterCv;
PImage  before, after, colorDiff, grayDiff;
void setup() {
  before = loadImage("before.jpg");
  after = loadImage("after.jpg");
  size(before.width, before.height, P2D);

  beforeCv = new OpenCVPro(this, before);
  afterCv = new OpenCVPro(this, after);

  // NOTE: important to not do the color diff 
  //       before calling these, as this makes
  //       the gray buffer from the current color buffer.
  beforeCv.gray();
  afterCv.gray();
  

  OpenCVPro.diff(beforeCv.getBufferColor(), afterCv.getBufferColor());
  OpenCVPro.diff(beforeCv.getBufferGray(), afterCv.getBufferGray());

  colorDiff = beforeCv.getColorImage();

  // FIXME: same mysterious problem with getOutputImage()
//  colorDiff = createImage(before.width, before.height, RGB);
//  beforeCv.toPImage(beforeCv.getBufferColor(), colorDiff);
//  colorDiff = beforeCv.getColorImage();

  grayDiff = beforeCv.getGrayImage();
}

void draw() {
  pushMatrix();
  scale(0.5);
  image(before, 0, 0);
  image(after, before.width, 0);
  image(colorDiff, 0, before.height);
  image(grayDiff, before.width, before.height);
  popMatrix();

  fill(255);
  text("before", 10, 20);
  text("after", before.width/2 +10, 20);
  text("color diff", 10, before.height/2+ 20);
  text("gray diff", before.width/2 + 10, before.height/2+ 20);
}

