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

import java.awt.Rectangle;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

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
	
	public CascadeClassifier classifier;
	BackgroundSubtractorMOG backgroundSubtractor;

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
	
	
	public final static int HORIZONTAL = 0;
	public final static int VERTICAL = 1;
	
	
		
    
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
    
    public int getColorSpace(){
    	return colorSpace;
    }
    
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
    
    public void useGray(){
    	useColor = false;
    }
    
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
		
		matR = new Mat(height, width, CvType.CV_8UC1);
		matG = new Mat(height, width, CvType.CV_8UC1);
		matB = new Mat(height, width, CvType.CV_8UC1);
		matA = new Mat(height, width, CvType.CV_8UC1);
		matGray = new Mat(height, width, CvType.CV_8UC1);
		
		matBGRA = new Mat(height, width, CvType.CV_8UC4);
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
	    	String nativeLibPath = getLibPath() ;
	    	if (PApplet.platform == PConstants.WINDOWS) { //platform Windows
	          nativeLibPath = nativeLibPath + "windows" + bitsJVM; 
	    	}
	    	if (PApplet.platform == PConstants.MACOSX) { //platform Mac
	    		nativeLibPath = nativeLibPath + "macosx" + bitsJVM;
	    	}
	    	if (PApplet.platform == PConstants.LINUX) { //platform Linux
	    		nativeLibPath = nativeLibPath + "linux" + bitsJVM;
	    	}
	    	
	    	if((PApplet.platform == PConstants.MACOSX && bitsJVM == 64) || (PApplet.platform == PConstants.WINDOWS && bitsJVM == 32) || (PApplet.platform == PConstants.LINUX)){
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
    	}
    }
    

    private void addLibraryPath(String path) throws Exception {
        String originalPath = System.getProperty("java.library.path");
    	System.setProperty("java.library.path", originalPath +System.getProperty("path.separator")+ path);
     
        //set sys_paths to null
        final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
        sysPathsField.setAccessible(true);
        sysPathsField.set(null, null);
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
	
	public Rectangle[] detect(){
		MatOfRect detections = new MatOfRect();
		classifier.detectMultiScale(getCurrentMat(), detections);
		
		Rect[] rects = detections.toArray(); 

		Rectangle[] results = new Rectangle[rects.length];
		for(int i = 0; i < rects.length; i++){
			results[i] = new Rectangle(rects[i].x, rects[i].y, rects[i].width, rects[i].height);
		}

		return results;
	}
	
	public void startBackgroundSubtraction(int history, int nMixtures, double backgroundRatio){
		backgroundSubtractor = new BackgroundSubtractorMOG(history, nMixtures, backgroundRatio);
	}
	
	public void updateBackground(){
		Mat foreground = imitate(getCurrentMat());
		backgroundSubtractor.apply(getCurrentMat(), foreground, 0.05);
		setGray(foreground);
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
	
	public static Mat imitate(Mat m){
		return new Mat(m.height(), m.width(), m.type());
	}
	
	
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
	
	public static void diff(Mat mat1, Mat mat2){
		Mat dst = imitate(mat1);
		Core.absdiff(mat1, mat2, dst);
		dst.assignTo(mat1);
	}
	
	public void threshold(int threshold){
		Imgproc.threshold(getCurrentMat(), getCurrentMat(), threshold, 255, Imgproc.THRESH_BINARY); 
	}

	public void adaptiveThreshold(int blockSize, int c){
		try{
			Imgproc.adaptiveThreshold(getCurrentMat(), getCurrentMat(), 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, blockSize, c);
		} catch(CvException e){
			PApplet.println("ERROR: adaptiveThreshold function only works on gray images.");
		}
	}
	
	public void equalizeHistogram(){
		try{
			Imgproc.equalizeHist(getCurrentMat(), getCurrentMat());
		} catch(CvException e){
			PApplet.println("ERROR: equalizeHistogram only works on a gray image.");
		}
	}
	
	public void invert(){
		Core.bitwise_not(getCurrentMat(),getCurrentMat());
	}
	
	public void dilate(){
		Imgproc.dilate(getCurrentMat(), getCurrentMat(), new Mat());
	}
	
	public void erode(){
		Imgproc.erode(getCurrentMat(), getCurrentMat(), new Mat());
	}
	
	public void blur(int blurSize){
		Imgproc.blur(getCurrentMat(), getCurrentMat(), new Size(blurSize, blurSize)); 
	}
	
	public void blur(int blurW, int blurH){
		Imgproc.blur(getCurrentMat(), getCurrentMat(), new Size(blurW, blurH)); 
	}
	
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
	
	/*
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
		
		if(useColor){
			if(colorSpace == PApplet.HSB){
				result = getSnapshot(matHSV);
			} else {
				result = getSnapshot(matBGRA);
			}
		} else {
			result = getSnapshot(matGray);
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

