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



package gab.opencv;

import gab.opencv.Contour;
import gab.opencv.ContourComparator;
import gab.opencv.Histogram;
import gab.opencv.Line;
import gab.opencv.Flow;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfFloat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.Point;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvException;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.video.BackgroundSubtractorMOG;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgproc.Imgproc;

import processing.core.*;

/**
 * OpenCV is the main class for using OpenCV for Processing. Most of the documentation is found here.
 * 
 * OpenCV for Processing is a computer vision library for the Processing creative coding toolkit.
 * It's based on OpenCV, which is widely used throughout industry and academic research. OpenCV for
 * Processing provides friendly, Processing-style functions for doing all of the most common tasks
 * in computer vision: loading images, filtering them, detecting faces, finding contours, background
 * subtraction, optical flow, calculating histograms etc. OpenCV also provides access to all native
 * OpenCV data types and functions. So advanced users can do anything described in the OpenCV java
 * documentation: http://docs.opencv.org/java/ 
 * 
 * A text is also underway to provide a narrative introduction to computer vision for beginners using
 * OpenCV for Processing: https://github.com/atduskgreg/opencv-processing-book/blob/master/book/toc.md
 * 
 */

public class OpenCV {
	
	PApplet parent;
	
	public int width;
	public int height;
	
	private int roiWidth;
	private int roiHeight;
		
	public Mat matBGRA;
	public Mat matR, matG, matB, matA;
	public Mat matHSV;
	public Mat matH, matS, matV;
	public Mat matGray;
	public Mat matROI;
	public Mat nonROImat; // so that releaseROI() can return to color/gray as appropriate
	
	private boolean useColor;
	private boolean useROI;
	public int colorSpace;
	
	private PImage outputImage;
	private PImage inputImage;
	
	private boolean nativeLoaded;
	private boolean isArm = false;
	
	public CascadeClassifier classifier;
	BackgroundSubtractorMOG backgroundSubtractor;
	public Flow flow;

	public final static String VERSION = "##library.prettyVersion##";
	public final static String CASCADE_FRONTALFACE = "haarcascade_frontalface_alt.xml";
	public final static String CASCADE_PEDESTRIANS = "hogcascade_pedestrians.xml";
	public final static String CASCADE_EYE = "haarcascade_eye.xml";
	public final static String CASCADE_CLOCK = "haarcascade_clock.xml";
	public final static String CASCADE_NOSE = "haarcascade_mcs_nose.xml";
	public final static String CASCADE_MOUTH = "haarcascade_mcs_mouth.xml";
	public final static String CASCADE_UPPERBODY = "haarcascade_upperbody.xml";
	public final static String CASCADE_LOWERBODY = "haarcascade_lowerbody.xml";
	public final static String CASCADE_FULLBODY = "haarcascade_fullbody.xml";
	public final static String CASCADE_PEDESTRIAN = "hogcascade_pedestrians.xml";

	public final static String CASCADE_RIGHT_EAR = "haarcascade_mcs_rightear.xml";
	public final static String CASCADE_PROFILEFACE = "haarcascade_profileface.xml";
	
	// used for both Scharr edge detection orientation
	// and flip(). Values are set for flip, arbitrary from POV of Scharr
	public final static int HORIZONTAL = 1;
	public final static int VERTICAL = 0;
	public final static int BOTH = -1;
	

    
    /**
     * Initialize OpenCV with the path to an image.
     * The image will be loaded and prepared for processing.
     * 
     * @param theParent - A PApplet representing the user sketch, i.e "this"
     * @param pathToImg - A String with a path to the image to be loaded
     */
    public OpenCV(PApplet theParent, String pathToImg){
    	initNative();
    	useColor = false;
    	loadFromString(theParent, pathToImg);
    }
    
    /**
     * Initialize OpenCV with the path to an image.
     * The image will be loaded and prepared for processing.
     * 
     * @param theParent - A PApplet representing the user sketch, i.e "this"
     * @param pathToImg - A String with a path to the image to be loaded
     * @param useColor - (Optional) Set to true if you want to use the color version of the image for processing.
     */
    public OpenCV(PApplet theParent, String pathToImg, boolean useColor){
    	initNative();
    	this.useColor = useColor;
    	if(useColor){
    		useColor(); // have to set the color space.
    	}
    	loadFromString(theParent, pathToImg);
    }
    
    private void loadFromString(PApplet theParent, String pathToImg){
    	parent = theParent;
    	PImage imageToLoad = parent.loadImage(pathToImg);
    	init(imageToLoad.width, imageToLoad.height);
    	loadImage(imageToLoad);	
    }
    
    /**
     * Initialize OpenCV with an image.
     * The image's pixels will be copied and prepared for processing.
     * 
     * @param theParent
     * 			A PApplet representing the user sketch, i.e "this"
     * @param img
     * 			A PImage to be loaded
     */
    public OpenCV(PApplet theParent, PImage img){
    	initNative();
    	useColor = false;
    	loadFromPImage(theParent, img);
    }
    
    /**
     * Initialize OpenCV with an image.
     * The image's pixels will be copiedd and prepared for processing.
     * 
     * @param theParent
     * 			A PApplet representing the user sketch, i.e "this"
     * @param img
     * 			A PImage to be loaded
     * @param useColor
     * 			(Optional) Set to true if you want to use the color version of the image for processing.
     */
    public OpenCV(PApplet theParent, PImage img, boolean useColor){
    	initNative();
    	this.useColor = useColor;
    	if(useColor){
    		useColor();  
    	}
    	loadFromPImage(theParent, img);
    }
    
    private void loadFromPImage(PApplet theParent, PImage img){
    	parent = theParent;
    	init(img.width, img.height);
    	loadImage(img);
    }
    
    /**
     * 
     * Apply subsequent image processing to
     * the color version of the loaded image.
     * 
     * Note: Many OpenCV functions require a grayscale
     *       image. Those functions will raise an exception
     *       if attempted on a color image. 
     * 
     */
    public void useColor(){
    	useColor(PApplet.RGB);
    }
    
    /**
     * 
     * Get the colorSpace of the current color image. Will be either RGB or HSB.
     * 
     * @return
     * 		
     * The color space of the color mats. Either PApplet.RGB or PApplet.HSB
     */
    public int getColorSpace(){
    	return colorSpace;
    }
    
    /**
     * 
     * Set the main working image to be the color version of the imported image.
     * Subsequent image-processing functions will be applied to the color version
     * of the image. Image is assumed to be HSB or RGB based on the argument
     * 
     * 
     * @param colorSpace
     * 		The color space of the image to be processed. Either RGB or HSB.
     */
    public void useColor(int colorSpace){
    	useColor = true;
    	if(colorSpace != PApplet.RGB && colorSpace != PApplet.HSB){
    		PApplet.println("ERROR: color space must be either RGB or HSB");
    	} else {
    		this.colorSpace = colorSpace;
    	}
    	
    	if(this.colorSpace == PApplet.HSB){
    		populateHSV();
    	}
    }
    
    private void populateHSV(){
    	matHSV = imitate(matBGRA);
    	Imgproc.cvtColor(matBGRA, matHSV, Imgproc.COLOR_BGR2HSV);
    	ArrayList<Mat> channels = new ArrayList<Mat>();
    	Core.split(matHSV, channels);
    	    	
    	matH = channels.get(0);
    	matS = channels.get(1);
    	matV = channels.get(2);
    }
    
    private void populateBGRA(){
    	ArrayList<Mat> channels = new ArrayList<Mat>();
    	Core.split(matBGRA, channels);
		matB = channels.get(0);
		matG = channels.get(1);
		matR = channels.get(2);
		matA = channels.get(3);	
    }
    
    /**
     * 
     * Set OpenCV to do image processing on the grayscale version
     * of the loaded image.
     * 
     */
    public void useGray(){
    	useColor = false;
    }
    
    /**
     * 
     * Checks whether OpenCV is currently using the color version of the image
     * or the grayscale version.
     * 
     * @return
     * 		True if OpenCV is currently using the color version of the image.
     */
    public boolean getUseColor(){
    	return useColor;
    }
    
    private Mat getCurrentMat(){
    	if(useROI){
    		return matROI;
    		
    	} else{
    	
    		if(useColor){
    			return matBGRA;
    		} else{
    			return matGray;
    		}
    	}
    }
    
    /**
     * Initialize OpenCV with a width and height.
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
	public OpenCV(PApplet theParent, int width, int height) {
		initNative();
		parent = theParent;
		init(width, height);
	}
    
    private void init(int w, int h){
    	width = w;
    	height = h;
		welcome();
		setupWorkingImages();
		setupFlow();
		
		matR = new Mat(height, width, CvType.CV_8UC1);
		matG = new Mat(height, width, CvType.CV_8UC1);
		matB = new Mat(height, width, CvType.CV_8UC1);
		matA = new Mat(height, width, CvType.CV_8UC1);
		matGray = new Mat(height, width, CvType.CV_8UC1);
		
		matBGRA = new Mat(height, width, CvType.CV_8UC4);
    }
    
    private void setupFlow(){
    	flow = new Flow(parent);
    }
    
    private void setupWorkingImages(){
		outputImage = parent.createImage(width,height, PConstants.ARGB);
    }
    
    private String getLibPath() {
        URL url = this.getClass().getResource("OpenCV.class");
        if (url != null) {
          // Convert URL to string, taking care of spaces represented by the "%20"
          // string.
          String path = url.toString().replace("%20", " ");
          int n0 = path.indexOf('/');

          int n1 = -1;
            

          n1 = path.indexOf("opencv_processing.jar");
          if (PApplet.platform == PConstants.WINDOWS) { //platform Windows
            // In Windows, path string starts with "jar file/C:/..."
            // so the substring up to the first / is removed.
            n0++;
          }


          if ((-1 < n0) && (-1 < n1)) {
            return path.substring(n0, n1);
          } else {
            return "";
          }
        }
        return "";
      }
    
    private void initNative(){
    	if(!nativeLoaded){
    		int bitsJVM = PApplet.parseInt(System.getProperty("sun.arch.data.model"));
    		
    		String osArch = System.getProperty("os.arch");
    		
	    	String nativeLibPath = getLibPath();
	    	
	    	String path = null;

	    	// determine the path to the platform-specific opencv libs
	    	if (PApplet.platform == PConstants.WINDOWS) { //platform Windows
	    		path = nativeLibPath + "windows" + bitsJVM;
	    	}
	    	if (PApplet.platform == PConstants.MACOSX) { //platform Mac
	    		path = nativeLibPath + "macosx" + bitsJVM;
	    	}
	    	if (PApplet.platform == PConstants.LINUX) { //platform Linux
	    		// attempt to detect arm architecture - is it fair to assume linux for ARM devices?
	    		isArm = osArch.contains("arm");
			// armv6hf as found on the Raspberry Pi is the lowest architecture supported by Processing
			// in the future we'll have runtime-detection of armv7 systems, and use the optimized library on those
    			path = isArm ? nativeLibPath + "linux-armv6hf" : nativeLibPath + "linux" + bitsJVM;
	    	}
	    	
	    	// ensure the determined path exists
	    	try {
	    		File libDir = new File(path);
	    		if (libDir.exists()) {
	    			nativeLibPath = path; 
	    		}
	    	} catch (NullPointerException e) {
	    		// platform couldn't be determined
	    		System.err.println("Cannot load local version of opencv_java245  : Linux 32/64, arm7, Windows 32 bits or Mac Os 64 bits are only avaible");
	    		e.printStackTrace();
	    	}
	    	
	    	// this check might be redundant now...
	    	if((PApplet.platform == PConstants.MACOSX && bitsJVM == 64) || (PApplet.platform == PConstants.WINDOWS) || (PApplet.platform == PConstants.LINUX)){
		    	try {
					addLibraryPath(nativeLibPath);
				} catch (Exception e) {
					e.printStackTrace();
				}
		    	System.loadLibrary("opencv_java245");
	    	}
	    	else{
	    		 System.err.println("Cannot load local version of opencv_java245  : Linux 32/64, Windows 32 bits or Mac Os 64 bits are only avaible");
	    	}
	    	
	    	nativeLoaded = true;
    	}
    }
    

    private void addLibraryPath(String path) throws Exception {
        String originalPath = System.getProperty("java.library.path");
        
        // If this is an arm device running linux, Processing seems to include the linux32 dirs in the path,
        // which conflict with the arm-specific libs. To fix this, we remove the linux32 segments from the path.
        //
        // Alternatively, we could do one of the following:
        // 		A) prepend to the path instead of append, forcing our libs to be used
        // 		B) rename the libopencv_java245 in the arm7 dir and add logic to load it instead above in System.loadLibrary(...)
        
        if (isArm) {
        	if (originalPath.indexOf("linux32") != -1) {
        		originalPath = originalPath.replaceAll(":[^:]*?linux32", "");
        	}
        }
        
    	System.setProperty("java.library.path", originalPath +System.getProperty("path.separator")+ path);
     
        //set sys_paths to null
        final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
        sysPathsField.setAccessible(true);
        sysPathsField.set(null, null);
    }

	/**
	 * Load a cascade file for face or object detection.
	 * Expects one of:
	 * 
	 * <pre>
	 * OpenCV.CASCADE_FRONTALFACE
	 * OpenCV.CASCADE_PEDESTRIANS
	 * OpenCV.CASCADE_EYE			
	 * OpenCV.CASCADE_CLOCK		
	 * OpenCV.CASCADE_NOSE 		
	 * OpenCV.CASCADE_MOUTH		
	 * OpenCV.CASCADE_UPPERBODY 	
	 * OpenCV.CASCADE_LOWERBODY 	
	 * OpenCV.CASCADE_FULLBODY 	
	 * OpenCV.CASCADE_PEDESTRIANS
	 * OpenCV.CASCADE_RIGHT_EAR 	
	 * OpenCV.CASCADE_PROFILEFACE
	 * </pre>
	 * 
	 * To pass your own cascade file, provide an absolute path and a second
	 * argument of true, thusly:
	 * 
	 * <pre>
	 * opencv.loadCascade("/path/to/my/custom/cascade.xml", true)
	 * </pre>
	 * 
	 * (NB: ant build scripts copy the data folder outside of the
	 * jar so that this will work.)
	 * 
	 * @param cascadeFileName
	 * 		The name of the cascade file to be loaded form within OpenCV for Processing.
	 * 		Must be one of the constants provided by this library 
	*/
	public void loadCascade(String cascadeFileName){

		// localize path to cascade file to point at the library's data folder
		String relativePath = "cascade-files/" + cascadeFileName;
		String cascadePath = getLibPath();
		cascadePath += relativePath;
				
		PApplet.println("Load cascade from: " + cascadePath);

		classifier = new CascadeClassifier(cascadePath);   
        
        if(classifier.empty()){
        	PApplet.println("Cascade failed to load"); // raise exception here?
        } else {
        	PApplet.println("Cascade loaded: " + cascadeFileName);
        }
	}

	/**
	 * Load a cascade file for face or object detection.
	 * If absolute is true, cascadeFilePath must be an
	 * absolute path to a cascade xml file. If it is false
	 * then cascadeFilePath must be one of the options provided
	 * by OpenCV for Processing as in the single-argument
	 * version of this function.
	 * 
	 * @param cascadeFilePath
	 * 		A string. Either an absolute path to a cascade XML file or 
	 * 		one of the constants provided by this library.
	 * @param absolute
	 * 		Whether or not the cascadeFilePath is an absolute path to an XML file.		
	 */
	public void loadCascade(String cascadeFilePath, boolean absolute){
		if(absolute){
			classifier = new CascadeClassifier(cascadeFilePath);   
	        
	        if(classifier.empty()){
	        	PApplet.println("Cascade failed to load"); // raise exception here?
	        } else {
	        	PApplet.println("Cascade loaded from absolute path: " + cascadeFilePath);
	        }
		} else {
			loadCascade(cascadeFilePath);
		}
	}
	
	/**
	 * Convert an array of OpenCV Rect objects into
	 * an array of java.awt.Rectangle rectangles.
	 * Especially useful when working with
	 * classifier.detectMultiScale().
	 *
	 * @param Rect[] rects
	 * 
	 * @return 
	 *  A Rectangle[] of java.awt.Rectangle
	 */
	public static Rectangle[] toProcessing(Rect[] rects){
		Rectangle[] results = new Rectangle[rects.length];
		for(int i = 0; i < rects.length; i++){
			results[i] = new Rectangle(rects[i].x, rects[i].y, rects[i].width, rects[i].height);
		}
		return results;
	}
	
	/**
	 * Detect objects using the cascade classifier. loadCascade() must already
	 * have been called to setup the classifier. See the OpenCV documentation
	 * for details on the arguments: http://docs.opencv.org/java/org/opencv/objdetect/CascadeClassifier.html#detectMultiScale(org.opencv.core.Mat, org.opencv.core.MatOfRect, double, int, int, org.opencv.core.Size, org.opencv.core.Size)
	 * 
	 * A simpler version of detect() that doesn't need these arguments is also available.
	 * 
	 * @param scaleFactor
	 * @param minNeighbors
	 * @param flags
	 * @param minSize
	 * @param maxSize
	 * @return
	 * 		An array of java.awt.Rectangle objects with the location, width, and height of each detected object.
	 */
	public Rectangle[] detect(double scaleFactor , int minNeighbors , int flags, int minSize , int maxSize){
		Size minS = new Size(minSize, minSize);
		Size maxS = new Size(maxSize, maxSize);
		
		MatOfRect detections = new MatOfRect();
		classifier.detectMultiScale(getCurrentMat(), detections, scaleFactor, minNeighbors, flags, minS, maxS );

		return OpenCV.toProcessing(detections.toArray());
	}
	
	/**
	 * Detect objects using the cascade classifier. loadCascade() must already
	 * have been called to setup the classifier.
	 * 
	 * @return
	 * 		An array of java.awt.Rectnangle objects with the location, width, and height of each detected object.
	 */
	public Rectangle[] detect(){
		MatOfRect detections = new MatOfRect();
		classifier.detectMultiScale(getCurrentMat(), detections);
		
		return OpenCV.toProcessing(detections.toArray());
	}
	
	/**
	 * Setup background subtraction. After calling this function,
	 * updateBackground() must be called with each new frame
	 * you want to add to the running background subtraction calculation.
	 * 
	 * For details on the arguments, see:
	 * http://docs.opencv.org/java/org/opencv/video/BackgroundSubtractorMOG.html#BackgroundSubtractorMOG(int, int, double)
	 * 
	 * @param history
	 * @param nMixtures
	 * @param backgroundRatio
	 */
	public void startBackgroundSubtraction(int history, int nMixtures, double backgroundRatio){
		backgroundSubtractor = new BackgroundSubtractorMOG(history, nMixtures, backgroundRatio);
	}
	
	/**
	 * Update the running background for background subtraction based on
	 * the current image loaded into OpenCV. startBackgroundSubtraction()
	 * must have been called before this to setup the background subtractor.
	 * 
	 */
	public void updateBackground(){
		Mat foreground = imitate(getCurrentMat());
		backgroundSubtractor.apply(getCurrentMat(), foreground, 0.05);
		setGray(foreground);
	}	
	
	/**
	 * Calculate the optical flow of the current image relative
	 * to a running series of images (typically frames from video).
	 * Optical flow is useful for detecting what parts of the image
	 * are moving and in what direction.
	 * 
	 */
	public void calculateOpticalFlow(){
		flow.calculateOpticalFlow(getCurrentMat());
	}
	
	/*
	 * Get the total optical flow within a region of the image.
	 * Be sure to call calculateOpticalFlow() first.
	 * 
	 */
	public PVector getTotalFlowInRegion(int x, int y, int w, int h) {
		return flow.getTotalFlowInRegion(x, y, w, h);
	}
	
	/*
	 * Get the average optical flow within a region of the image.
	 * Be sure to call calculateOpticalFlow() first.
	 * 
	 */
	public PVector getAverageFlowInRegion(int x, int y, int w, int h) {
		return flow.getAverageFlowInRegion(x,y,w,h);
	}
	
	/*
	 * Get the total optical flow for the entire image.
	 * Be sure to call calculateOpticalFlow() first. 
	 */
	public PVector getTotalFlow() {
		return flow.getTotalFlow();
	}
	
	/*
	 * Get the average optical flow for the entire image.
	 * Be sure to call calculateOpticalFlow() first.
	 */
	public PVector getAverageFlow() {
		return flow.getAverageFlow();
	}
	
	/*
	 * Get the optical flow at a single point in the image.
	 * Be sure to call calcuateOpticalFlow() first.
	 */
	public PVector getFlowAt(int x, int y){
		return flow.getFlowAt(x,y);
	}
	
	/*
	 * Draw the optical flow.
	 * Be sure to call calcuateOpticalFlow() first.
	 */
	public void drawOpticalFlow(){
		flow.draw();
	}
	
	/**
	 * Flip the current image.
	 * 
	 * @param direction
	 * 		One of: OpenCV.HORIZONTAL, OpenCV.VERTICAL, or OpenCV.BOTH
	 */
	public void flip(int direction){
		Core.flip(getCurrentMat(), getCurrentMat(), direction);
	}
	
	/**
	 * 
	 * Adjust the contrast of the image. Works on color or black and white images.
	 * 
	 * @param amt
	 * 			Amount of contrast to apply. 0-1.0 reduces contrast. Above 1.0 increases contrast.
	 * 
	 **/
	public void contrast(float amt){
		Scalar modifier;
		if(useColor){
			modifier = new Scalar(amt,amt,amt,1);

		} else{
			modifier = new Scalar(amt);
		}
		
		Core.multiply(getCurrentMat(), modifier, getCurrentMat());
	}
	
	/**
	 * Get the x-y location of the maximum value in the current image.
	 * 
	 * @return 
	 * 		A PVector with the location of the maximum value.
	 */
	public PVector max(){
		MinMaxLocResult r = Core.minMaxLoc(getCurrentMat());
		return OpenCV.pointToPVector(r.maxLoc);
	}
	
	/**
	 * Get the x-y location of the minimum value in the current image.
	 * 
	 * @return 
	 * 		A PVector with the location of the minimum value.
	 */
	public PVector min(){
		MinMaxLocResult r = Core.minMaxLoc(getCurrentMat());
		return OpenCV.pointToPVector(r.minLoc);
	}
	
	/**
	 * Helper function to convert an OpenCV Point into a Processing PVector
	 * 
	 * @param p
	 * 		A Point
	 * @return
	 * 		A PVector
	 */
	public static PVector pointToPVector(Point p){
		return new PVector((float)p.x, (float)p.y);
	}
	
	
	/**
	 * Adjust the brightness of the image. Works on color or black and white images.
	 * 
	 * @param amt
	 * 			The amount to brighten the image. Ranges -255 to 255. 
	 * 
	 **/	
	public void brightness(int amt){
		Scalar modifier;
		if(useColor){
			modifier = new Scalar(amt,amt,amt, 1);

		} else{
			modifier = new Scalar(amt);
		}
		
		Core.add(getCurrentMat(), modifier, getCurrentMat());
	}
	
	/**
	 * Helper to create a new OpenCV Mat whose channels and
	 * bit-depth mask an existing Mat.
	 * 
	 * @param m
	 * 		The Mat to match
	 * @return
	 * 		A new Mat
	 */
	public static Mat imitate(Mat m){
		return new Mat(m.height(), m.width(), m.type());
	}
	
	/**
	 * Calculate the difference between the current image
	 * loaded into OpenCV and a second image. The result is stored
	 * in the loaded image in OpenCV. Works on both color and grayscale
	 * images.
	 * 
	 * @param img
	 * 		A PImage to diff against.
	 */
	public void diff(PImage img){
		Mat imgMat = imitate(getColor());
		toCv(img, imgMat);

		Mat dst = imitate(getCurrentMat());

		if(useColor){
			ARGBtoBGRA(imgMat, imgMat);
			Core.absdiff(getCurrentMat(), imgMat, dst);
		} else {
			Core.absdiff(getCurrentMat(), OpenCV.gray(imgMat), dst);
		}
		
		dst.assignTo(getCurrentMat());
	}
	
	/**
	 * A helper function that diffs two Mats using absdiff.
	 * Places the result back into mat1 
	 * 
	 * @param mat1
	 * 		The destination Mat
	 * @param mat2
	 * 		The Mat to diff against
	 */
	public static void diff(Mat mat1, Mat mat2){
		Mat dst = imitate(mat1);
		Core.absdiff(mat1, mat2, dst);
		dst.assignTo(mat1);
	}
	
	/**
	 * Apply a global threshold to an image. Produces a binary image
	 * with white pixels where the original image was above the threshold
	 * and black where it was below.
	 * 
	 * @param threshold
	 * 		An int from 0-255.
	 */
	public void threshold(int threshold){
		Imgproc.threshold(getCurrentMat(), getCurrentMat(), threshold, 255, Imgproc.THRESH_BINARY); 
	}
	
	/**
	 * Apply a global threshold to the image. The threshold is determined by Otsu's method, which
	 * attempts to divide the image at a threshold which minimizes the variance of pixels in the black
	 * and white regions.
	 *
	 * See: https://en.wikipedia.org/wiki/Otsu's_method
	 */
	public void threshold() {
		Imgproc.threshold(getCurrentMat(), getCurrentMat(), 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU); 
	}
	
	/**
	 * Apply an adaptive threshold to an image. Produces a binary image
	 * with white pixels where the original image was above the threshold
	 * and black where it was below.
	 * 
	 * See:
	 * http://docs.opencv.org/java/org/opencv/imgproc/Imgproc.html#adaptiveThreshold(org.opencv.core.Mat, org.opencv.core.Mat, double, int, int, int, double)
	 * 
	 * @param blockSize
	 * 		The size of the pixel neighborhood to use.
	 * @param c
	 * 		A constant subtracted from the mean of each neighborhood.
	 */
	public void adaptiveThreshold(int blockSize, int c){
		try{
			Imgproc.adaptiveThreshold(getCurrentMat(), getCurrentMat(), 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, blockSize, c);
		} catch(CvException e){
			PApplet.println("ERROR: adaptiveThreshold function only works on gray images.");
		}
	}
	
	/**
	 * Normalize the histogram of the image. This will spread the image's color
	 * spectrum over the full 0-255 range. Only works on grayscale images. 
	 * 
	 * 
	 * See: http://docs.opencv.org/java/org/opencv/imgproc/Imgproc.html#equalizeHist(org.opencv.core.Mat, org.opencv.core.Mat)
	 * 
	 */
	public void equalizeHistogram(){
		try{
			Imgproc.equalizeHist(getCurrentMat(), getCurrentMat());
		} catch(CvException e){
			PApplet.println("ERROR: equalizeHistogram only works on a gray image.");
		}
	}
	
	/**
	 * Invert the image.
	 * See: http://docs.opencv.org/java/org/opencv/core/Core.html#bitwise_not(org.opencv.core.Mat, org.opencv.core.Mat)
	 * 
	 */
	public void invert(){
		Core.bitwise_not(getCurrentMat(),getCurrentMat());
	}
	
	/**
	 * Dilate the image. Dilation is a morphological operation (i.e. it affects the shape) often used to
	 * close holes in contours. It expands white areas of the image.
	 * 
	 * See:
	 * http://docs.opencv.org/java/org/opencv/imgproc/Imgproc.html#dilate(org.opencv.core.Mat, org.opencv.core.Mat, org.opencv.core.Mat)
	 * 
	 */
	public void dilate(){
		Imgproc.dilate(getCurrentMat(), getCurrentMat(), new Mat());
	}
	
	/**
	 * Erode the image. Erosion  is a morphological operation (i.e. it affects the shape) often used to
	 * close holes in contours. It contracts white areas of the image.
	 * 
	 * See:
	 * http://docs.opencv.org/java/org/opencv/imgproc/Imgproc.html#erode(org.opencv.core.Mat, org.opencv.core.Mat, org.opencv.core.Mat)
	 * 
	 */
	public void erode(){
		Imgproc.erode(getCurrentMat(), getCurrentMat(), new Mat());
	}
	
  /**
   * Apply a morphological operation (e.g., opening, closing) to the image with a given kernel element.
   *
   * See:
   * http://docs.opencv.org/doc/tutorials/imgproc/opening_closing_hats/opening_closing_hats.html
   * 
   * @param operation
   *    The morphological operation to apply: Imgproc.MORPH_CLOSE, MORPH_OPEN,
   *    MORPH_TOPHAT, MORPH_BLACKHAT, MORPH_GRADIENT.
   * @param kernelElement
   *    The shape to apply the operation with: Imgproc.MORPH_RECT, MORPH_CROSS, or MORPH_ELLIPSE.
   * @param width
   *    Width of the shape.
   * @param height
   *    Height of the shape.
   */
  public void morphX(int operation, int kernelElement, int width, int height) {
    Mat kernel = Imgproc.getStructuringElement(kernelElement, new Size(width, height));
    Imgproc.morphologyEx(getCurrentMat(), getCurrentMat(), operation, kernel);
  }
  
  /**
   * Close the image with a circle of a given size.
   *
   * See:
   * http://docs.opencv.org/doc/tutorials/imgproc/opening_closing_hats/opening_closing_hats.html#closing
   *
   * @param size
   *    Radius of the circle to close with.
   */
  public void close(int size) {
    Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(size, size));
    Imgproc.morphologyEx(getCurrentMat(), getCurrentMat(), Imgproc.MORPH_CLOSE, kernel);
  }
	
  /**
   * Open the image with a circle of a given size.
   *
   * See:
   * http://docs.opencv.org/doc/tutorials/imgproc/opening_closing_hats/opening_closing_hats.html#opening
   *
   * @param size
   *    Radius of the circle to open with.
   */
  public void open(int size) {
    Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(size, size));
    Imgproc.morphologyEx(getCurrentMat(), getCurrentMat(), Imgproc.MORPH_OPEN, kernel);
  }
  
	/**
	 * Blur an image symetrically by a given number of pixels.
	 * 
	 * @param blurSize
	 * 		int - the amount to blur by in x- and y-directions.
	 */
	public void blur(int blurSize){
		Imgproc.blur(getCurrentMat(), getCurrentMat(), new Size(blurSize, blurSize)); 
	}
	
	/**
	 * Blur an image assymetrically by a different number of pixels in x- and y-directions.
	 * 
	 * @param blurW
	 * 		amount to blur in the x-direction
	 * @param blurH
	 * 		amount to blur in the y-direction
	 */
	public void blur(int blurW, int blurH){
		Imgproc.blur(getCurrentMat(), getCurrentMat(), new Size(blurW, blurH)); 
	}
	
	/**
	 * Find edges in the image using Canny edge detection.
	 * 
	 * @param lowThreshold
	 * @param highThreshold
	 */
	public void findCannyEdges(int lowThreshold, int highThreshold){
		Imgproc.Canny(getCurrentMat(), getCurrentMat(), lowThreshold, highThreshold);
	}
	
	public void findSobelEdges(int dx, int dy){
		Mat sobeled = new Mat(getCurrentMat().height(), getCurrentMat().width(), CvType.CV_32F);
		Imgproc.Sobel(getCurrentMat(), sobeled, CvType.CV_32F, dx, dy);
		sobeled.convertTo(getCurrentMat(), getCurrentMat().type());
	}
	
	public void findScharrEdges(int direction){
		if(direction == HORIZONTAL){
			Imgproc.Scharr(getCurrentMat(), getCurrentMat(), -1, 1, 0 );
		}
		
		if(direction == VERTICAL){
			Imgproc.Scharr(getCurrentMat(), getCurrentMat(), -1, 0, 1 );
		}
		
		if(direction == BOTH){
			Mat hMat = imitate(getCurrentMat());
			Mat vMat = imitate(getCurrentMat());
			Imgproc.Scharr(getCurrentMat(), hMat, -1, 1, 0 );
			Imgproc.Scharr(getCurrentMat(), vMat, -1, 0, 1 );
			Core.add(vMat,hMat, getCurrentMat());
		}
	}
	
	public ArrayList<Contour> findContours(){
		return findContours(true, false);
	}
	
	public ArrayList<Contour> findContours(boolean findHoles, boolean sort){
		ArrayList<Contour> result = new ArrayList<Contour>();
		
		ArrayList<MatOfPoint> contourMat = new ArrayList<MatOfPoint>();
		try{
			int contourFindingMode = (findHoles ? Imgproc.RETR_LIST : Imgproc.RETR_EXTERNAL);
			
			Imgproc.findContours(getCurrentMat(), contourMat, new Mat(), contourFindingMode, Imgproc.CHAIN_APPROX_NONE);
		} catch(CvException e){
			PApplet.println("ERROR: findContours only works with a gray image.");
		}
		  for (MatOfPoint c : contourMat) {
		    result.add(new Contour(parent, c));
		  }
		
		if(sort){
			Collections.sort(result, new ContourComparator());
		}
		  
		return result;
	}
	
	public ArrayList<Line> findLines(int threshold, double minLineLength, double maxLineGap){
		ArrayList<Line> result = new ArrayList<Line>();
		
		Mat lineMat = new Mat();
		Imgproc.HoughLinesP(getCurrentMat(), lineMat, 1, PConstants.PI/180.0, threshold, minLineLength, maxLineGap);
		for (int i = 0; i < lineMat.width(); i++) {
			double[] coords = lineMat.get(0, i);
		    result.add(new Line(coords[0], coords[1], coords[2], coords[3]));
		}
		
		return result;
	}
	
	public ArrayList<PVector> findChessboardCorners(int patternWidth, int patternHeight){
		MatOfPoint2f corners = new MatOfPoint2f();
		Calib3d.findChessboardCorners(getCurrentMat(), new Size(patternWidth,patternHeight), corners);
		return matToPVectors(corners);
	}
	
	/**
	 * 
	 * @param mat 
	 * 		The mat from which to calculate the histogram. Get this from getGray(), getR(), getG(), getB(), etc..
	 * 		By default this will normalize the histogram (scale the values to 0.0-1.0). Pass false as the third argument to keep values unormalized.
	 * @param numBins 
	 * 		The number of bins into which divide the histogram should be divided.
	 * @param normalize (optional)
	 * 		Whether or not to normalize the histogram (scale the values to 0.0-1.0). Defaults to true.
	 * @return
	 * 		A Histogram object that you can call draw() on.
	 */
	public Histogram findHistogram(Mat mat, int numBins){
		return findHistogram(mat, numBins, true);
	}
	
	
	public Histogram findHistogram(Mat mat, int numBins, boolean normalize){
		
		MatOfInt channels = new MatOfInt(0);
		MatOfInt histSize = new MatOfInt(numBins);
		float[] r = {0f, 256f};
		MatOfFloat ranges = new MatOfFloat(r);
		Mat hist = new Mat();
		
		ArrayList<Mat> images = new ArrayList<Mat>();
		images.add(mat);

		Imgproc.calcHist( images, channels, new Mat(), hist, histSize, ranges);
		
		if(normalize){
			Core.normalize(hist, hist);
		}
		
		return new Histogram(parent, hist);
	}
	
	/**
	 * 
	 * Filter the image for values between a lower and upper bound.
	 * Converts the current image into a binary image with white where pixel
	 * values were within bounds and black elsewhere.
	 * 
	 * @param lowerBound
	 * @param upperBound
	 */
	public void inRange(int lowerBound, int upperBound){
		Core.inRange(getCurrentMat(), new Scalar(lowerBound), new Scalar(upperBound), getCurrentMat());
	}
	
	/**
	 * 
	 * @param src
	 * 		A Mat of type 8UC4 with channels arranged as BGRA.
	 * @return
	 * 		A Mat of type 8UC1 in grayscale.
	 */
	public static Mat gray(Mat src){
		Mat result = new Mat(src.height(), src.width(), CvType.CV_8UC1);
		Imgproc.cvtColor(src, result, Imgproc.COLOR_BGRA2GRAY);
			
		return result;
	}
	
	public void gray(){
		matGray = gray(matBGRA);
		useGray(); //???
	}
	
	/**
	 * Set a Region of Interest within the image. Subsequent image processing
	 * functions will apply to this ROI rather than the full image.
	 * Full image will display be included in output.
	 * 
	 * @return
	 * 		False if requested ROI exceed the bounds of the working image.
	 * 		True if ROI was successfully set.
	 */
	public boolean setROI(int x, int y, int w, int h){
		if(x < 0 ||
		   x + w > width ||
		   y < 0 ||
		   y + h > height){
			return false;
		} else{
			roiWidth = w;
			roiHeight = h;
		
			if(useColor){
				nonROImat = matBGRA;
				matROI = new Mat(matBGRA, new Rect(x, y, w, h));
			} else {
				nonROImat = matGray;
				matROI = new Mat(matGray, new Rect(x, y, w, h));
			}
			useROI = true;
			
			return true;
		}
	}
	
	public void releaseROI(){
		useROI = false;
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
	
	// NOTE: We're not handling the signed/unsigned
	// 		 conversion. Is that any issue?
	public void loadImage(PImage img){				
		// FIXME: is there a better way to hold onto
		// 			this?
		inputImage = img;
		
		toCv(img, matBGRA);
		ARGBtoBGRA(matBGRA,matBGRA);
		populateBGRA();
		
		if(useColor){
			useColor(this.colorSpace);
		} else {
			gray();
		}
		
	}
	
	public static void ARGBtoBGRA(Mat rgba, Mat bgra){
		ArrayList<Mat> channels = new ArrayList<Mat>();
		Core.split(rgba, channels);

		ArrayList<Mat> reordered = new ArrayList<Mat>();
		// Starts as ARGB. 
		// Make into BGRA.
		
		reordered.add(channels.get(3));
		reordered.add(channels.get(2));
		reordered.add(channels.get(1));
		reordered.add(channels.get(0));
		  
		Core.merge(reordered, bgra);	
	}
	
	
	public int getSize(){
		return width * height;
	}
    
    /**
     * 
     * Convert a 4 channel OpenCV Mat object into 
     * pixels to be shoved into a 4 channel ARGB PImage's
     * pixel array.
     * 
     * @param m
     * 		An RGBA Mat we want converted 
     * @return
     * 		An int[] formatted to be the pixels of a PImage
     */
    public int[] matToARGBPixels(Mat m){
    	 int pImageChannels = 4;
         int numPixels = m.width()*m.height();
         int[] intPixels = new int[numPixels];
         byte[] matPixels = new byte[numPixels*pImageChannels];
   
         m.get(0,0, matPixels);
         ByteBuffer.wrap(matPixels).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().get(intPixels);
         return intPixels;
    }

 
	/**
	 * Convert an OpenCV Mat object into a PImage
	 * to be used in other Processing code.
	 * Copies the Mat's pixel data into the PImage's pixel array.
	 * Iterates over each pixel in the Mat, i.e. expensive.
	 * 
	 * (Mainly used internally by OpenCV. Inspired by toCv()
	 * from KyleMcDonald's ofxCv.)
	 * 
	 * @param m
	 * 			A Mat you want converted
	 * @param img
	 * 			The PImage you want the Mat converted into.
	 */
	public void toPImage(Mat m, PImage img){	
		  img.loadPixels();

		  if(m.channels() == 3){
			  Mat m2 = new Mat();
			  Imgproc.cvtColor(m, m2, Imgproc.COLOR_RGB2RGBA);
              img.pixels = matToARGBPixels(m2);
		  } else if(m.channels() == 1){
			  Mat m2 = new Mat();
			  Imgproc.cvtColor(m, m2, Imgproc.COLOR_GRAY2RGBA);
              img.pixels = matToARGBPixels(m2);
		  } else if(m.channels() == 4){
              img.pixels = matToARGBPixels(m);
		  }
		  
		  img.updatePixels();
	}
	
	/**
	 * Convert a Processing PImage to an OpenCV Mat.
	 * (Inspired by Kyle McDonald's ofxCv's toOf())
	 * 
	 * @param img
	 * 		The PImage to convert.
	 * @param m
	 * 		The Mat to receive the image data.
	 */	
	public static void toCv(PImage img, Mat m){
		BufferedImage image = (BufferedImage)img.getNative();
		int[] matPixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		
		ByteBuffer bb = ByteBuffer.allocate(matPixels.length * 4);
		IntBuffer ib = bb.asIntBuffer();
		ib.put(matPixels);
		
		byte[] bvals = bb.array();

		m.put(0,0, bvals);
	}
	
	public static ArrayList<PVector> matToPVectors(MatOfPoint mat){
		ArrayList<PVector> result = new ArrayList<PVector>();
		Point[] points =  mat.toArray();
		for(int i = 0; i < points.length; i++){
			result.add(new PVector((float)points[i].x, (float)points[i].y));
		}
		  
		return result;
	}

	public static ArrayList<PVector> matToPVectors(MatOfPoint2f mat){
		ArrayList<PVector> result = new ArrayList<PVector>();
		Point[] points =  mat.toArray();
		for(int i = 0; i < points.length; i++){
			result.add(new PVector((float)points[i].x, (float)points[i].y));
		}
		  
		return result;
	}
	
	public String matToS(Mat mat){
		return CvType.typeToString(mat.type());
	}
			
	public PImage getInput(){
		return inputImage;
	}
	
	public PImage getOutput(){
		if(useColor){
			toPImage(matBGRA, outputImage);
		} else {
			toPImage(matGray, outputImage);
		}
		
		return outputImage;	
	}
	
	public PImage getSnapshot(){
		PImage result;
		
		if(useROI){
			result = getSnapshot(matROI);
		} else {
			if(useColor){
				if(colorSpace == PApplet.HSB){
					result = getSnapshot(matHSV);
				} else {
					result = getSnapshot(matBGRA);
				}
			} else {
				result = getSnapshot(matGray);
			}
		}
		return result;
	}
	
	public PImage getSnapshot(Mat m){
		PImage result = parent.createImage(m.width(), m.height(), PApplet.ARGB);
		toPImage(m, result);
		return result;
	}

	public Mat getR(){
		return matR;
	}
	
	public Mat getG(){
		return matG;
	}
	
	public Mat getB(){
		return matB;
	}
	
	public Mat getA(){
		return matA;
	}
	
	public Mat getH(){
		return matH;
	}
	
	public Mat getS(){
		return matS;
	}
	
	public Mat getV(){
		return matV;
	}
	
	public Mat getGray(){
		return matGray;
	}
	
	public void setGray(Mat m){
		matGray = m;
		useColor = false;
	}
	
	public void setColor(Mat m){
		matBGRA = m;
		useColor = true;
	}
	
	public Mat getColor(){
		return matBGRA;
	}
	
	public Mat getROI(){
		return matROI;
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

