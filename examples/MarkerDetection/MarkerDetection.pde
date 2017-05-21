import gab.opencv.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.CvType;

import org.opencv.core.Point;
import org.opencv.core.Size;

//import java.util.list;

OpenCV opencv;
PImage  src, dst, markerImg;
ArrayList<MatOfPoint> contours;
ArrayList<MatOfPoint2f> approximations;
ArrayList<MatOfPoint2f> markers;

boolean[][] markerCells;

void setup() {
  opencv = new OpenCV(this, "marker_test.jpg");
  size(1000, 365);
  src = opencv.getInput();

  // hold on to this for later, since adaptiveThreshold is destructive
  Mat gray = OpenCV.imitate(opencv.getGray());
  opencv.getGray().copyTo(gray);

  Mat thresholdMat = OpenCV.imitate(opencv.getGray());

  opencv.blur(5);
  
  Imgproc.adaptiveThreshold(opencv.getGray(), thresholdMat, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 451, -65);

  contours = new ArrayList<MatOfPoint>();
  Imgproc.findContours(thresholdMat, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);

  approximations = createPolygonApproximations(contours);

  markers = new ArrayList<MatOfPoint2f>();
  markers = selectMarkers(approximations);

  //// Mat markerMat = grat.submat();
  //  Mat warped = OpenCVPro.imitate(gray);
  //  
  MatOfPoint2f canonicalMarker = new MatOfPoint2f();
  Point[] canonicalPoints = new Point[4];
  canonicalPoints[0] = new Point(0, 350);
  canonicalPoints[1] = new Point(0, 0);
  canonicalPoints[2] = new Point(350, 0);
  canonicalPoints[3] = new Point(350, 350);
  canonicalMarker.fromArray(canonicalPoints);

  println("num points: " + markers.get(0).height());

  Mat transform = Imgproc.getPerspectiveTransform(markers.get(0), canonicalMarker);
  Mat unWarpedMarker = new Mat(50, 50, CvType.CV_8UC1);  
  Imgproc.warpPerspective(gray, unWarpedMarker, transform, new Size(350, 350));


  Imgproc.threshold(unWarpedMarker, unWarpedMarker, 125, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

  float cellSize = 350/7.0;

  markerCells = new boolean[7][7];

  for (int row = 0; row < 7; row++) {
    for (int col = 0; col < 7; col++) {
      int cellX = int(col*cellSize);
      int cellY = int(row*cellSize);

      Mat cell = unWarpedMarker.submat(cellX, cellX +(int)cellSize, cellY, cellY+ (int)cellSize); 
      markerCells[row][col] = (Core.countNonZero(cell) > (cellSize*cellSize)/2);
    }
  }

  for (int col = 0; col < 7; col++) {
    for (int row = 0; row < 7; row++) {
      if (markerCells[row][col]) {
        print(1);
      } 
      else {
        print(0);
      }
    }
    println();
  }

  dst  = createImage(350, 350, RGB);
  opencv.toPImage(unWarpedMarker, dst);
}



ArrayList<MatOfPoint2f> selectMarkers(ArrayList<MatOfPoint2f> candidates) {
  float minAllowedContourSide = 50;
  minAllowedContourSide = minAllowedContourSide * minAllowedContourSide;

  ArrayList<MatOfPoint2f> result = new ArrayList<MatOfPoint2f>();

  for (MatOfPoint2f candidate : candidates) {

    if (candidate.size().height != 4) {
      continue;
    } 

    if (!Imgproc.isContourConvex(new MatOfPoint(candidate.toArray()))) {
      continue;
    }

    // eliminate markers where consecutive
    // points are too close together
    float minDist = src.width * src.width;
    Point[] points = candidate.toArray();
    for (int i = 0; i < points.length; i++) {
      Point side = new Point(points[i].x - points[(i+1)%4].x, points[i].y - points[(i+1)%4].y);
      float squaredLength = (float)side.dot(side);
      // println("minDist: " + minDist  + " squaredLength: " +squaredLength);
      minDist = min(minDist, squaredLength);
    }

    //  println(minDist);


    if (minDist < minAllowedContourSide) {
      continue;
    }

    result.add(candidate);
  }

  return result;
}

ArrayList<MatOfPoint2f> createPolygonApproximations(ArrayList<MatOfPoint> cntrs) {
  ArrayList<MatOfPoint2f> result = new ArrayList<MatOfPoint2f>();

  double epsilon = cntrs.get(0).size().height * 0.01;
  println(epsilon);

  for (MatOfPoint contour : cntrs) {
    MatOfPoint2f approx = new MatOfPoint2f();
    Imgproc.approxPolyDP(new MatOfPoint2f(contour.toArray()), approx, epsilon, true);
    result.add(approx);
  }

  return result;
}

void drawContours(ArrayList<MatOfPoint> cntrs) {
  for (MatOfPoint contour : cntrs) {
    beginShape();
    Point[] points = contour.toArray();
    for (int i = 0; i < points.length; i++) {
      vertex((float)points[i].x, (float)points[i].y);
    }
    endShape();
  }
}

void drawContours2f(ArrayList<MatOfPoint2f> cntrs) {
  for (MatOfPoint2f contour : cntrs) {
    beginShape();
    Point[] points = contour.toArray();

    for (int i = 0; i < points.length; i++) {
      vertex((float)points[i].x, (float)points[i].y);
    }
    endShape(CLOSE);
  }
}

void draw() {
  pushMatrix();
  background(125);
  scale(0.5);
  image(src, 0, 0);

  noFill();
  smooth();
  strokeWeight(5);
  stroke(0, 255, 0);
  drawContours2f(markers);  
  popMatrix();

  pushMatrix();
  translate(src.width/2, 0);
  strokeWeight(1);
  image(dst, 0, 0);

  float cellSize = dst.width/7.0;
  for (int col = 0; col < 7; col++) {
    for (int row = 0; row < 7; row++) {
      if(markerCells[row][col]){
        fill(255);
      } else {
        fill(0);
      }
      stroke(0,255,0);
      rect(col*cellSize, row*cellSize, cellSize, cellSize);
      //line(i*cellSize, 0, i*cellSize, dst.width);
    //line(0, i*cellSize, dst.width, i*cellSize);
    }
  }

  popMatrix();
}