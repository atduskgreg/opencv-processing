/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */



package gab.opencvpro;

import java.awt.Image;
import java.awt.Rectangle;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgproc.Imgproc;

import processing.core.*;

/**
 * This is a template class and can be used to start a new processing library or tool.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own library or tool naming convention.
 * 
 * @example Hello 
 * 
 * (the tag @example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 */

public class OpenCVPro {
	
	// myParent is a reference to the parent sketch
	PApplet parent;

	int myVariable = 0;
	
	public final static String VERSION = "##library.prettyVersion##";
	Mat buffer1;
	Mat grayBuffer;
	
	public final static String CASCADE_FRONTALFACE_ALT = "haarcascade_frontalface_alt.xml";
	public final static String CASCADE_PEDESTRIANS = "hogcascade_pedestrians.xml";
	CascadeClassifier faceDetector;

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the library.
	 * 
	 * @example Hello
	 * @param theParent
	 */
	
    static{ System.loadLibrary("opencv_java244"); }
	
	public OpenCVPro(PApplet theParent, int width, int height) {
		parent = theParent;
		welcome();
		System.out.println("Welcome to Java OpenCV " + Core.VERSION);
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		//CvType.makeType(CvType.CV_8U, 1);
		
		int rgbType = CvType.makeType(CvType.CV_32S, 4);
		int grayType = CvType.makeType(CvType.CV_32S, 1);
		
		buffer1 = new Mat(height, width, rgbType); // this is probably not always right CvType.CV_32S
		
		
		PApplet.println("buffer1 Mat type should be: " +  CvType.CV_32S);
		PApplet.println("buffer1 Mat type calculated as: " +  rgbType);
		PApplet.println("gray type: " +  grayType);
		
		grayBuffer = new Mat(height, width, CvType.CV_8U); // 1channel gray

	}
	
	public void copy(PImage img){
		BufferedImage image = (BufferedImage)img.getNative();
		
		int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		buffer1.put(0, 0, pixels);
	}
	
	
	public void loadCascade(String cascadeFileName){
		PApplet.println(cascadeFileName);
        faceDetector = new CascadeClassifier(cascadeFileName);   
	}
	
	public Rectangle[] detect(){
		MatOfRect faceDetections = new MatOfRect();
		gray(buffer1);
		faceDetector.detectMultiScale(grayBuffer, faceDetections);
		
		Rect[] detections = faceDetections.toArray(); 
		Rectangle[] results = new Rectangle[detections.length];
		for(int i = 0; i < detections.length; i++){
			results[i] = new Rectangle(detections[i].x, detections[i].y, detections[i].width, detections[i].height);
		}
		
		return results;
	}
	
	public void gray(Mat src){
		Imgproc.cvtColor(src, grayBuffer, Imgproc.COLOR_RGB2GRAY);
	}
	
	// FIXME: This seems to cause a problem with toPImage()
	//        where the Mat coming from imread()
	//		  is not the right CvType
	public void loadImage(String imgPath){
		PApplet.println(imgPath);
		buffer1 = Highgui.imread(imgPath);
	}
	
	public PImage toPImage(Mat mat){
		PImage result = parent.createImage(mat.width(), mat.height(), PConstants.ARGB);
		result.loadPixels();
		mat.get(0,0,result.pixels);
		result.updatePixels();

		return result;
	}
	
	public PImage getBuffer(){
		return toPImage(buffer1);
	}
	
	private void welcome() {
		System.out.println("##library.name## ##library.prettyVersion## by ##author##");
	}
	
	
	public String sayHello() {
		return "hello library.";
	}
	/**
	 * return the version of the library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}

	/**
	 * 
	 * @param theA
	 *          the width of test
	 * @param theB
	 *          the height of test
	 */
	public void setVariable(int theA, int theB) {
		myVariable = theA + theB;
	}

	/**
	 * 
	 * @return int
	 */
	public int getVariable() {
		return myVariable;
	}
}

