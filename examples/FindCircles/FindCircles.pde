import gab.opencv.*;

OpenCV opencv;

void setup() {
  size(300*2, 400);
  ellipseMode(RADIUS);
  
  opencv = new OpenCV(this, 300, 400);
  
}

void draw() {
  opencv.loadImage("sample.jpg");
  image(opencv.getSnapshot(),0,0); 
  image(loadImage("sample.jpg"),300,0);
   
  ArrayList< ArrayList< Integer> > arr = opencv.findCircles();   
  
  for (ArrayList<Integer> zx : arr)
  {
    ellipse(zx.get(0),zx.get(1),zx.get(2),zx.get(2)); //x, y, radius, radius (of the circle)
  }
  
  noFill();
  stroke(0, 255, 0);
  strokeWeight(3);

}
