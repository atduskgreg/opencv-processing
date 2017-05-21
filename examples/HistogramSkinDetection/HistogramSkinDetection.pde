import gab.opencv.*;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.CvType;
import org.opencv.imgproc.Imgproc;

OpenCV opencv;
PImage src,dst, hist, histMask;

Mat skinHistogram;

void setup(){
  src = loadImage("test.jpg");
  src.resize(src.width/2, 0);
  size(1336, 360);
  // third argument is: useColor
  opencv = new OpenCV(this, src, true);  

  skinHistogram = Mat.zeros(256, 256, CvType.CV_8UC1);
  Core.ellipse(skinHistogram, new Point(113.0, 155.6), new Size(40.0, 25.2), 43.0, 0.0, 360.0, new Scalar(255, 255, 255), Core.FILLED);

 histMask = createImage(256,256, ARGB);
 opencv.toPImage(skinHistogram, histMask);
 hist = loadImage("cb-cr.png");
 hist.blend(histMask, 0,0,256,256,0,0,256,256, ADD);
 
 dst = opencv.getOutput();
 dst.loadPixels();
 
 for(int i = 0; i < dst.pixels.length; i++){
    
    Mat input = new Mat(new Size(1, 1), CvType.CV_8UC3);
    input.setTo(colorToScalar(dst.pixels[i]));
    Mat output = opencv.imitate(input);
    Imgproc.cvtColor(input, output, Imgproc.COLOR_BGR2YCrCb );
    double[] inputComponents = output.get(0,0);
    if(skinHistogram.get((int)inputComponents[1], (int)inputComponents[2])[0] > 0){
      dst.pixels[i] = color(255);
    } else {
      dst.pixels[i] = color(0);
    }
 }
 
 dst.updatePixels();
}

 // in BGR
Scalar colorToScalar(color c){
  return new Scalar(blue(c), green(c), red(c));
}


void draw(){
  image(src,0,0);
  image(dst, src.width, 0);
  image(hist, src.width*2, 0);
}