/**
 * MultipleColorTracking
 * Select 4 colors to track them separately
 *
 * It uses the OpenCV for Processing library by Greg Borenstein
 * https://github.com/atduskgreg/opencv-processing
 *
 * @author: Jordi Tost (@jorditost)
 * @url: https://github.com/jorditost/ImageFiltering/tree/master/MultipleColorTracking
 *
 * University of Applied Sciences Potsdam, 2014
 *
 * Instructions:
 * Press one numerical key [1-4] and click on one color to track it
 */
 
import gab.opencv.*;
import processing.video.*;
import java.awt.Rectangle;

Capture video;
OpenCV opencv;
PImage src;
ArrayList<Contour> contours;

// <1> Set the range of Hue values for our filter
//ArrayList<Integer> colors;
int maxColors = 4;
int[] hues;
int[] colors;
int rangeWidth = 10;

PImage[] outputs;

int colorToChange = -1;

void setup() {
  video = new Capture(this, 640, 480);
  opencv = new OpenCV(this, video.width, video.height);
  contours = new ArrayList<Contour>();
  
  size(830, 480, P2D);
  
  // Array for detection colors
  colors = new int[maxColors];
  hues = new int[maxColors];
  
  outputs = new PImage[maxColors];
  
  video.start();
}

void draw() {
  
  background(150);
  
  if (video.available()) {
    video.read();
  }

  // <2> Load the new frame of our movie in to OpenCV
  opencv.loadImage(video);
  
  // Tell OpenCV to use color information
  opencv.useColor();
  src = opencv.getSnapshot();
  
  // <3> Tell OpenCV to work in HSV color space.
  opencv.useColor(HSB);
  
  detectColors();
  
  // Show images
  image(src, 0, 0);
  for (int i=0; i<outputs.length; i++) {
    if (outputs[i] != null) {
      image(outputs[i], width-src.width/4, i*src.height/4, src.width/4, src.height/4);
      
      noStroke();
      fill(colors[i]);
      rect(src.width, i*src.height/4, 30, src.height/4);
    }
  }
  
  // Print text if new color expected
  textSize(20);
  stroke(255);
  fill(255);
  
  if (colorToChange > -1) {
    text("click to change color " + colorToChange, 10, 25);
  } else {
    text("press key [1-4] to select color", 10, 25);
  }
  
  displayContoursBoundingBoxes();
}

//////////////////////
// Detect Functions
//////////////////////

void detectColors() {
    
  for (int i=0; i<hues.length; i++) {
    
    if (hues[i] <= 0) continue;
    
    opencv.loadImage(src);
    opencv.useColor(HSB);
    
    // <4> Copy the Hue channel of our image into 
    //     the gray channel, which we process.
    opencv.setGray(opencv.getH().clone());
    
    int hueToDetect = hues[i];
    //println("index " + i + " - hue to detect: " + hueToDetect);
    
    // <5> Filter the image based on the range of 
    //     hue values that match the object we want to track.
    opencv.inRange(hueToDetect-rangeWidth/2, hueToDetect+rangeWidth/2);
    
    //opencv.dilate();
    opencv.erode();
    
    // TO DO:
    // Add here some image filtering to detect blobs better
    
    // <6> Save the processed image for reference.
    outputs[i] = opencv.getSnapshot();
  }
  
  // <7> Find contours in our range image.
  //     Passing 'true' sorts them by descending area.
  if (outputs[0] != null) {
    
    opencv.loadImage(outputs[0]);
    contours = opencv.findContours(true,true);
  }
}

void displayContoursBoundingBoxes() {
  
  for (int i=0; i<contours.size(); i++) {
    
    Contour contour = contours.get(i);
    Rectangle r = contour.getBoundingBox();
    
    if (r.width < 20 || r.height < 20)
      continue;
    
    stroke(255, 0, 0);
    fill(255, 0, 0, 150);
    strokeWeight(2);
    rect(r.x, r.y, r.width, r.height);
  }
}

//////////////////////
// Keyboard / Mouse
//////////////////////

void mousePressed() {
    
  if (colorToChange > -1) {
    
    color c = get(mouseX, mouseY);
    println("r: " + red(c) + " g: " + green(c) + " b: " + blue(c));
   
    int hue = int(map(hue(c), 0, 255, 0, 180));
    
    colors[colorToChange-1] = c;
    hues[colorToChange-1] = hue;
    
    println("color index " + (colorToChange-1) + ", value: " + hue);
  }
}

void keyPressed() {
  
  if (key == '1') {
    colorToChange = 1;
    
  } else if (key == '2') {
    colorToChange = 2;
    
  } else if (key == '3') {
    colorToChange = 3;
    
  } else if (key == '4') {
    colorToChange = 4;
  }
}

void keyReleased() {
  colorToChange = -1; 
}