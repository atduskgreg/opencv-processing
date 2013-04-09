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
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

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
	
	public Mat bufferBGRA;
	public Mat bufferR, bufferG, bufferB, bufferA;
	public Mat bufferGray;
	public Mat pImageMat;
	
	private PImage outputImage;
	private PImage inputImage;
	
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
    	PImage imageToLoad = parent.loadImage(pathToImg);
    	init(imageToLoad.width, imageToLoad.height);
    	loadImage(imageToLoad);
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
    	init(img.width, img.height);
    	loadImage(img);
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
		init(width, height);
	}
    
    private void init(int w, int h){
    	width = w;
    	height = h;
		welcome();
		setupWorkingImages();

		
		PApplet.println("init buffers at: " + width+"x"+height);
		
		bufferR = new Mat(height, width, CvType.CV_8UC1);
		bufferG = new Mat(height, width, CvType.CV_8UC1);
		bufferB = new Mat(height, width, CvType.CV_8UC1);
		bufferA = new Mat(height, width, CvType.CV_8UC1);
		bufferGray = new Mat(height, width, CvType.CV_8UC1);
		
		pImageMat = new Mat(height, width, CvType.CV_32SC1);
		
		bufferBGRA = new Mat(height, width, CvType.CV_8UC4);
		
    }
    
    private void setupWorkingImages(){
		outputImage = parent.createImage(width,height, PConstants.ARGB);
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
		bufferGray = gray(bufferBGRA);
		PApplet.println("bufferGray: " + matToS(bufferGray));
		classifier.detectMultiScale(bufferGray, detections);
		
		Rect[] rects = detections.toArray(); 
		PApplet.println("numRects: " + rects.length);

		Rectangle[] results = new Rectangle[rects.length];
		for(int i = 0; i < rects.length; i++){
			results[i] = new Rectangle(rects[i].x, rects[i].y, rects[i].width, rects[i].height);
		}
		
		return results;
	}
	

	
	public Mat findCannyEdges(Mat src, int lowThreshold, int highThreshold){
		Mat result = new Mat(src.height(), src.width(), src.type());
		Imgproc.Canny(src, result, lowThreshold, highThreshold);
		return result;
	}
	
	public PImage findCannyEdges(int lowThreshold, int highThreshold){
		bufferGray = findCannyEdges(bufferBGRA, lowThreshold, highThreshold);
		toPImage(bufferGray, outputImage);
		return outputImage;
	}
	
	
	public Mat findSobelEdges(Mat src, int dx, int dy){
		Mat result = new Mat(src.height(), src.width(), src.type());
		Imgproc.Sobel(src, result, -1, dx, dy); // -1 depth = use source CvType
		return result;
	}
	
	public PImage findSobelEdges(int dx, int dy){
		bufferGray = findSobelEdges(bufferBGRA, dx, dy);
		toPImage(bufferGray, outputImage);
		return outputImage;
	}
	
	public Mat findScharr(Mat src, int dx, int dy){
		Mat dst = new Mat(src.height(), src.width(), src.type());
		Imgproc.Scharr(src, dst, -1, dx, dy);
		return dst;
	}
	
	public PImage findScharrX(){
		Mat dst = findScharr(bufferBGRA,1, 0);
		toPImage(dst, outputImage);
		return outputImage;
	}
	
	public PImage findScharrY(){
		bufferGray = findScharr(bufferBGRA,0,1);
		toPImage(bufferGray, outputImage);
		return outputImage;
	}
	
	
	/**
	 * 
	 * @param src
	 * 		A Mat of type 8UC4 with channels arranged as BGRA.
	 * @return
	 * 		A Mat of type 8UC1 in grayscale.
	 */
	public Mat gray(Mat src){
		
		Mat result = new Mat(src.height(), src.width(), CvType.CV_8UC1);
		Imgproc.cvtColor(src, result, Imgproc.COLOR_BGRA2GRAY);
			
		return result;
	}
	

	/**
	 * Load an image from a path.
	 * 
	 * @param imgPath
	 * 			String with the path to the image
	 */
	public void loadImage(String imgPath){
		loadImage(parent.loadImage(imgPath));
	}
	
	public void loadImage(PImage img){
		// FIXME: is there a better way to hold onto
		// 			this?
		inputImage = img;
		
		BufferedImage image = (BufferedImage)img.getNative();
		int[] matPixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		pImageMat.put(0,0, matPixels);
		
		PApplet.println(width+"x"+height);
		
		// FIXME: maybe these should not get re-allocated?
		//		  (hopefully we're doing this at most once per-frame)
		//		  (and even more hopefully, this code won't be around long)
		byte[] rPix = new byte[getSize()];
		byte[] gPix = new byte[getSize()];
		byte[] bPix = new byte[getSize()];
		byte[] aPix = new byte[getSize()];
		
		for (int i = 0; i < getSize(); i++) {
			rPix[i] = (byte)((matPixels[i] >> 16) & 0xFF);
			gPix[i] = (byte)((matPixels[i] >> 8) & 0xFF);
			bPix[i] = (byte)((matPixels[i] >> 0) & 0xFF);
			aPix[i] = (byte)((matPixels[i] >> 24) & 0xFF);
		}
				
		bufferR.put(0,0,rPix);
		bufferG.put(0,0,gPix);
		bufferB.put(0,0,bPix);
		bufferA.put(0,0,aPix);	
		
		PApplet.println("merginging channels");
		ArrayList<Mat> channels = new ArrayList<Mat>();
		channels.add(bufferB);
		channels.add(bufferG);
		channels.add(bufferR);
		channels.add(bufferA);
		
		Core.merge(channels, bufferBGRA);	
	}
	

	
	
	public int getSize(){
		return width * height;
	}
	
	
	/**
	 * Convert an OpenCV Mat object into a PImage
	 * to be used in other Processing code.
	 * Copies the Mat's pixel data into the PImage's pixel array.
	 * Iterates over each pixel in the Mat, i.e. expensive.
	 * 
	 * (Mainly used internally by OpenCVPro. Inspired by toCv()
	 * from KyleMcDonald's ofxCv.)
	 * 
	 * @param mat
	 * 			an OpenCV Mat
	 * @return 
	 * 			a PImage created from the given Mat
	 */
	public void toPImage(Mat m, PImage img){
		  
		  PApplet.println("mat to convert: " + matToS(m));
		  img.loadPixels();
		  
		  if(m.channels() == 3){
			  byte[] matPixels = new byte[width*height*3];
			  m.get(0,0, matPixels);
			  for(int i = 0; i < m.width()*m.height()*3; i+=3){
				  img.pixels[PApplet.floor(i/3)] = parent.color(matPixels[i+2]&0xFF, matPixels[i+1]&0xFF, matPixels[i]&0xFF);
			  }
		  } else if(m.channels() == 1){
			  byte[] matPixels = new byte[width*height];
			  m.get(0,0, matPixels);
		      for(int i = 0; i < m.width()*m.height(); i++){
		    	  img.pixels[i] = parent.color(matPixels[i]&0xFF);
		      }
		  } else if(m.channels() == 4){
			  byte[] matPixels = new byte[width*height*4];
			  m.get(0,0, matPixels);
			  for(int i = 0; i < m.width()*m.height()*4; i+=4){
				  img.pixels[PApplet.floor(i/4)] = parent.color(matPixels[i+2]&0xFF, matPixels[i+1]&0xFF, matPixels[i]&0xFF, matPixels[i+3]&0xFF);
			  }
		  }
		  
		  img.updatePixels();
	}
	
	public String matToS(Mat mat){
		return CvType.typeToString(mat.type());
	}
	
	public PImage getOutputImage(){
		toPImage(bufferBGRA, outputImage);
		return outputImage;
	}
	
	public PImage getInputImage(){
		return inputImage;
	}
	
	public Mat getBufferR(){
		return bufferR;
	}
	
	public Mat getBufferG(){
		return bufferG;
	}
	public Mat getBufferB(){
		return bufferB;
	}
	
	public Mat getBufferA(){
		return bufferA;
	}
	
	public Mat getBufferGray(){
		return bufferGray;
	}
	
	public Mat getColorBuffer(){
		return bufferBGRA;
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

