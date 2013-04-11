import gab.opencvpro.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;

OpenCVPro beforeCv, afterCv;
PImage  before, after, diff;
void setup() {
  before = loadImage("before.jpg");
  after = loadImage("after.jpg");
  size(before.width, before.height);

  beforeCv = new OpenCVPro(this, before);
  afterCv = new OpenCVPro(this, after);
    
  OpenCVPro.diff(beforeCv.getBufferColor(), afterCv.getBufferColor());

  // FIXME: same mysterious problem with getOutputImage()
  diff = createImage(before.width, before.height, RGB);
  beforeCv.toPImage(beforeCv.getBufferColor(), diff);
}

void draw() {
  pushMatrix();
  scale(0.5);
  image(before, 0, 0);
  image(after, before.width, 0);
  image(diff, before.width/2, before.height);
  popMatrix();
  
  fill(255);
  text("before", 10, 20);
  text("after", before.width/2 +10, 20);
  text("diff", before.width/4 +10, before.height/2+ 20);

}
