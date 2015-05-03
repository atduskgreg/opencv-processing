import gab.opencv.*;
//import processing.video.*;
import java.awt.*;
import org.opencv.core.*;

//Capture video;
OpenCV opencv;

void setup() {
  size(300, 400);

  //video = new Capture(this, 640, 480);
  //opencv = new OpenCV(this, 640, 480);
  //video.start();
  
  opencv = new OpenCV(this, 300, 400);
}

void draw() {
  //opencv.loadImage(video);
  opencv.loadImage("sample.jpg");
  float foo[]={0,0,0};
  Mat m;// = new Mat(0,0,0);
  m=opencv.HoughCircles();
  image(opencv.getSnapshot(),0,0);
  for(int x=0;x<20;x++)
  {
    for(int y=0;y<20;y++)
    {
      m.get(x,y,foo);
      for (int i=0;i<3;i++)
      {
        if(foo[i]!=0)
        {
          //System.out.print("X: " + x +" Y: " + y + " foo["+i+"]: "+ foo[i] + "\n");
          ellipseMode(RADIUS);
          ellipse(foo[0],foo[1],foo[2],foo[2]);
        }
      }
    }
  }
  noFill();
  stroke(0, 255, 0);
  strokeWeight(3);
  
  
  
}

/*
void captureEvent(Capture c) {
  c.read();
}
*/