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

import java.awt.Rectangle;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

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
	
	public int width;
	public int height;
	
	Mat buffer1;
	Mat grayBuffer;
	Mat workingRGB;
	private PImage outputImage;
	
	CascadeClassifier classifier;

	public final static String VERSION = "##library.prettyVersion##";
	public final static String CASCADE_FRONTALFACE_ALT = "haarcascade_frontalface_alt.xml";
	public final static String CASCADE_PEDESTRIANS = "hogcascade_pedestrians.xml";


	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the library.
	 * 
	 * @example Hello
	 * @param theParent
	 */
	
    static{ System.loadLibrary("opencv_java244"); }
	
    
    /**
     * Initialize OpenCVPro with the path to an image.
     * The image will be loaded and prepared for processing.
     * 
     * @param theParent - A PApplet representing the user sketch, i.e "this"
     * @param pathToImg - A String with a path to the image to be loaded
     */
    public OpenCVPro(PApplet theParent, String pathToImg){
    	parent = theParent;
    	init();
    	loadImage(pathToImg);
    	setupWorkingImages();
    }
    
    /**
     * Initialize OpenCVPro with an image.
     * The image's pixels will be copied and prepared for processing.
     * 
     * @param theParent
     * 			A PApplet representing the user sketch, i.e "this"
     * @param img
     * 			A PImage to be loaded
     */
    public OpenCVPro(PApplet theParent, PImage img){
    	parent = theParent;
    	width = img.width;
    	height = img.height;
    	init();
    	buffer1 = toMat(img);
    	setupWorkingImages();
    }
    
    private void init(){
		welcome();
		
		int rgbType = CvType.makeType(CvType.CV_32S, 3); // this is probably not always right
		int grayType = CvType.makeType(CvType.CV_8U, 1);
		
		buffer1 = new Mat(height, width, rgbType); 		
		workingRGB = new Mat(height, width, rgbType); 		
		grayBuffer = new Mat(height, width, grayType);
    }
    
    private void setupWorkingImages(){
		outputImage = parent.createImage(width,height, PConstants.RGB);
    }
    
    /**
     * Initialize OpenCVPro with a width and height.
     * You will need to load an image in before processing.
     * See copy(PImage img).
     * 
     * @param theParent
     * 			A PApplet representing the user sketch, i.e "this"
     * @param width
     * 			int
     * @param height
     * 			int
     */
	public OpenCVPro(PApplet theParent, int width, int height) {
		parent = theParent;
		this.width = width;
		this.height = height;
		init();
		setupWorkingImages();
	}
	
	/**
	 * Copy the pixel data from a PImage
	 * into OpenCVPro for further processing.
	 * 
	 * @param img 
	 */
	public void copy(PImage img){
		BufferedImage image = (BufferedImage)img.getNative();
		int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		buffer1.put(0, 0, pixels);
	}
	
	/**
	 * load a cascade xml file from the data folder
	 * NB: ant build scripts copy the data folder outside of the
	 * jar so that this will work.
	 * 
	 * @param cascadeFileName
	*/
	public void loadCascade(String cascadeFileName){

		// localize path to cascade file to point at the library's data folder
		String relativePath = "data/" + cascadeFileName;		
		String jarPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

		String[] parts = jarPath.split("/");
		
		String cascadePath = "";
		for(int i = 0; i < parts.length-2; i++){
			cascadePath += parts[i] + "/";
		}
		cascadePath += relativePath;
		
		classifier = new CascadeClassifier(cascadePath);   
        
        if(classifier.empty()){
        	PApplet.println("Cascade failed to load"); // raise exception here?
        } else {
        	PApplet.println("Cascade loaded: " + cascadeFileName);
        }
	}
	
	public Rectangle[] detect(){
		MatOfRect detections = new MatOfRect();
		gray(buffer1);
		classifier.detectMultiScale(grayBuffer, detections);
		
		Rect[] rects = detections.toArray(); 
		Rectangle[] results = new Rectangle[rects.length];
		for(int i = 0; i < rects.length; i++){
			results[i] = new Rectangle(rects[i].x, rects[i].y, rects[i].width, rects[i].height);
		}
		
		return results;
	}
	
	public void gray(){
		gray(buffer1);
	}
	
	public void gray(Mat src){
		Mat floatBuffer = new Mat(src.height(), src.width(), CvType.CV_32F);
		src.convertTo(floatBuffer, CvType.CV_32F);
		Imgproc.cvtColor(floatBuffer, grayBuffer, Imgproc.COLOR_RGB2GRAY);
	}
	
	/**
	 * Load an image from a path to 
	 * 
	 * @param imgPath
	 * 			String with the path to the image
	 */
	public void loadImage(String imgPath){
		PImage img = parent.loadImage(imgPath);
		width = img.width;
		height = img.height;
		buffer1 = toMat(img);
	}
	
	public Mat toMat(PImage img){
		Mat result = new Mat(img.height, img.width, buffer1.type());
		BufferedImage image = (BufferedImage)img.getNative();
		int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		result.put(0, 0, pixels);	
		return result;
	}
	
	/**
	 * Convert an OpenCV Mat object into a PImage
	 * to be used in other Processing code.
	 * Copies the Mat's pixel data into the PImage's pixel array.
	 * 
	 * (Mainly used internally by OpenCVPro. Inspired by toCv()
	 * from KyleMcDonald's ofxCv.)
	 * 
	 * @param mat
	 * 			an OpenCV Mat
	 * @return 
	 * 			a PImage created from the given Mat
	 */
	public PImage toPImage(Mat mat){		
		mat.convertTo(workingRGB, buffer1.type());
		outputImage.loadPixels();
		workingRGB.get(0,0, outputImage.pixels);
		outputImage.updatePixels();

		return outputImage;
	}
	
	public PImage getBuffer(){
		return toPImage(buffer1);
	}
	
	public PImage getGrayBuffer(){
		return toPImage(grayBuffer);
	}
	
	public Mat getMat(){
		return buffer1;
	}
	
	private void welcome() {
		System.out.println("##library.name## ##library.prettyVersion## by ##author##");
		System.out.println("Using Java OpenCV " + Core.VERSION);

	}
	
	/**
	 * return the version of the library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}
}

