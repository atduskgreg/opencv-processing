import gab.opencvpro.*;
import org.opencv.core.Mat;
import org.opencv.calib3d.StereoBM;
import org.opencv.core.CvType;
import org.opencv.calib3d.StereoSGBM;

OpenCVPro opencv;
PImage  img, depth1, depth2;

void setup() {
  opencv = new OpenCVPro(this, "stereo_pair.jpg");
  size(opencv.width * 2, opencv.height);


  opencv.gray();

  Mat left = opencv.getBufferGray().submat(0, opencv.height, 0, opencv.width/2);
  Mat right = opencv.getBufferGray().submat(0, opencv.height, opencv.width/2, opencv.width - 1);

  Mat disparity = OpenCVPro.imitate(left);

  StereoSGBM stereo =  new StereoSGBM(0, 32, 3, 128, 256, 20, 16, 1, 100, 20, true);
  stereo.compute(left, right, disparity );

  Mat depthMat = OpenCVPro.imitate(left);
  disparity.convertTo(depthMat, depthMat.type());

  img = opencv.getOutputImage();

  depth1 = createImage(depthMat.width(), depthMat.height(), RGB);
  opencv.toPImage(depthMat, depth1);
  
  StereoBM stereo2 = new StereoBM();
  stereo2.compute(left, right, disparity );
  disparity.convertTo(depthMat, depthMat.type());

  
  depth2 = createImage(depthMat.width(), depthMat.height(), RGB);
  opencv.toPImage(depthMat, depth2);
}

void draw() {
  image(img, 0, 0);
  image(depth1, img.width, 0);
  image(depth2, img.width + depth1.width, 0);


  noStroke();
  fill(0);
  rect(10 + img.width - 5, 5, 100, 20);
  rect(10 + img.width + img.width/2 - 5, 5, 100, 20);
  
  fill(255,0,0);
  text("left", 10, 20);
  text("right", 10 + img.width/2, 20);
  text("stereo SGBM", 10 + img.width, 20);
  text("stereo BM", 10 + img.width + img.width/2, 20);
  
}

