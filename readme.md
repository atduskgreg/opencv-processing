## OpenCV for Processing

**A Processing library for the [OpenCV](http://opencv.org/) computer vision library.**

OpenCV for Processing is based on OpenCV's official Java bindings. It attempts to provide convenient wrappers for common OpenCV functions that are friendly to beginners and feel familiar to the Processing environment.

See the included examples below for an overview of what's possible and links to the relevant example code. Complete documentation is available here:

**[OpenCV for Processing reference](http://atduskgreg.github.io/opencv-processing/reference/)**

OpenCV for Processing is based on the officially supported [OpenCV Java API](http://docs.opencv.org/java/), currently at version 2.4.5. In addition to using the wrapped functionality, you can import OpenCV modules and use any of its documented functions: [OpenCV javadocs](http://docs.opencv.org/java/). See the advanced examples (HistogramSkinDetection, DepthFromStereo, and Marker Detection) below for details. (This style of API was inspired by Kyle McDonald's [ofxCv addon](https://github.com/kylemcdonald/ofxCv) for OpenFrameworks.) 

Contributions welcome.

### Installing

OpenCV for Processing currently supports Mac OSX, 32-bit and 64-bit Windows, 32- and 64-bit Linux. Android support is hopefully coming soon (pull requests welcome).

_NB: When running on the Mac, make sure you have Processing set to 64-bit mode in the Preferences_

See [here](https://github.com/atduskgreg/opencv-processing/releases) for the latest release.

### Examples

#### LiveCamTest

Access a live camera and do image processing on the result, specifically face detection.

Code: [LiveCamTest.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/LiveCamTest/LiveCamTest.pde)

_Note: There's a bug that prevents live camera access in current versions of Processing 2.0 on machines with a Retina display._

#### FaceDetection

Detect faces in images.

<a href="http://www.flickr.com/photos/unavoidablegrain/8634017624/" title="Screen Shot 2013-04-08 at 1.22.18 PM by atduskgreg, on Flickr"><img src="http://farm9.staticflickr.com/8543/8634017624_35f7ef05ce.jpg" width="500" height="358" alt="Screen Shot 2013-04-08 at 1.22.18 PM"></a>

Code: [FaceDetection.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/FaceDetection/FaceDetection.pde)

#### BrightnessContrast

Adjust the brightness and contrast of color and gray images.

<a href="http://www.flickr.com/photos/unavoidablegrain/9155239258/" title="brightness and contrast by atduskgreg, on Flickr"><img src="http://farm3.staticflickr.com/2841/9155239258_41a7df36c6.jpg" width="500" height="358" alt="brightness and contrast"></a>

Code: [BrightnessContrast.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/BrightnessContrast/BrightnessContrast.pde)

#### FilterImages

Basic filtering operations on images: threshold, blur, and adaptive thresholds.

<a href="http://www.flickr.com/photos/unavoidablegrain/8643666252/" title="Screen Shot 2013-04-12 at 1.42.30 PM by atduskgreg, on Flickr"><img src="http://farm9.staticflickr.com/8240/8643666252_be0da1c751.jpg" width="500" height="358" alt="Screen Shot 2013-04-12 at 1.42.30 PM"></a>

Code: [FilterImages.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/FilterImages/FilterImages.pde)

#### FindContours

Find contours in images and calculate polygon approximations of the contours (i.e., the closest straight line that fits the contour).

<a href="http://www.flickr.com/photos/unavoidablegrain/9024663015/" title="contours with polygon approximations by atduskgreg, on Flickr"><img src="http://farm4.staticflickr.com/3719/9024663015_f419b117b1.jpg" width="500" height="208" alt="contours with polygon approximations"></a>

Code: [FindContours.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/FindContours/FindContours.pde)

#### FindEdges

Three different edge-detection techniques: Canny, Scharr, and Sobel.

<a href="http://www.flickr.com/photos/unavoidablegrain/8635989723/" title="Screen Shot 2013-04-10 at 2.03.59 AM by atduskgreg, on Flickr"><img src="http://farm9.staticflickr.com/8109/8635989723_170b69dca0.jpg" width="500" height="358" alt="Screen Shot 2013-04-10 at 2.03.59 AM"></a>

Code: [FindEdges.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/FindEdges/FindEdges.pde)

#### FindLines

Find straight lines in the image using Hough line detection.

<a href="http://www.flickr.com/photos/unavoidablegrain/9263329608/" title="Hough line detection by atduskgreg, on Flickr"><img src="http://farm4.staticflickr.com/3781/9263329608_735ce228bb.jpg" width="486" height="500" alt="Hough line detection"></a>

Code: [HoughLineDetection.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/HoughLineDetection/HoughLineDetection.pde)

#### BrightestPoint

Find the brightest point in an image.

<a href="http://www.flickr.com/photos/unavoidablegrain/9199572469/" title="finding the brightest point by atduskgreg, on Flickr"><img src="http://farm8.staticflickr.com/7407/9199572469_4a25c83062.jpg" width="500" height="366" alt="finding the brightest point"></a>

Code: [BrightestPoint.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/BrightestPoint/BrightestPoint.pde)

#### RegionOfInterest

Assign a sub-section (or Region of Interest) of the image to be processed. Video of this example in action here: [Region of Interest demo on Vimeo](https://vimeo.com/69009345).

<a href="http://www.flickr.com/photos/unavoidablegrain/9077805277/" title="region of interest by atduskgreg, on Flickr"><img src="http://farm4.staticflickr.com/3795/9077805277_084d87a3a5.jpg" width="500" height="358" alt="region of interest"></a>

Code: [RegionOfInterest.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/RegionOfInterest/RegionOfInterest.pde)

#### ImageDiff

Find the difference between two images in order to subtract the background or detect a new object in a scene.

<a href="http://www.flickr.com/photos/unavoidablegrain/8640005799/" title="Screen Shot 2013-04-11 at 2.10.35 PM by atduskgreg, on Flickr"><img src="http://farm9.staticflickr.com/8114/8640005799_44b48e01ae.jpg" width="500" height="409" alt="Screen Shot 2013-04-11 at 2.10.35 PM"></a>

Code: [ImageDiff.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/ImageDiff/ImageDiff.pde)

#### DilationAndErosion

Thin (erode) and expand (dilate) an image in order to close holes. These are known as "morphological" operations.

<a href="http://www.flickr.com/photos/unavoidablegrain/9075875005/" title="dilation and erosion by atduskgreg, on Flickr"><img src="http://farm3.staticflickr.com/2818/9075875005_8f7cde3ed7.jpg" width="496" height="500" alt="dilation and erosion"></a>

Code: [DilationAndErosion.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/DilationAndErosion/DilationAndErosion.pde)

#### BackgroundSubtraction

Detect moving objects in a scene. Use background subtraction to distinguish background from foreground and contour tracking to track the foreground objects.

<a href="http://www.flickr.com/photos/unavoidablegrain/9220336868/" title="Background Subtraction by atduskgreg, on Flickr"><img src="http://farm8.staticflickr.com/7292/9220336868_bed3498528.jpg" width="500" height="369" alt="Background Subtraction"></a>

Code: [BackgroundSubtraction.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/BackgroundSubtraction/BackgroundSubtraction.pde)


#### WorkingWithColorImages

Demonstration of what you can do color images in OpenCV (threshold, blur, etc) and what you can't (lots of other operations).

<a href="http://www.flickr.com/photos/unavoidablegrain/9136033334/" title="color operations: threshold and blur by atduskgreg, on Flickr"><img src="http://farm6.staticflickr.com/5451/9136033334_3345dfa057.jpg" width="500" height="358" alt="color operations: threshold and blur"></a>

Code: [WorkingWithColorImages.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/WorkingWithColorImages/WorkingWithColorImages.pde)

#### ColorChannels ####

Separate a color image into red, green, blue or hue, saturation, and value channels in order to work with the channels individually.

<a href="http://www.flickr.com/photos/unavoidablegrain/9246157901/" title="ColorChannels by atduskgreg, on Flickr"><img src="http://farm3.staticflickr.com/2847/9246157901_08ccf19e7d.jpg" width="488" height="500" alt="ColorChannels"></a>

Code: [ColorChannels](https://github.com/atduskgreg/opencv-processing/blob/master/examples/ColorChannels/ColorChannels.pde)

#### FindHistogram

Demonstrates use of the findHistogram() function and the Histogram class to get and draw histograms for grayscale and individual color channels.

<a href="http://www.flickr.com/photos/unavoidablegrain/9174190443/" title="gray, red, green, blue histograms by atduskgreg, on Flickr"><img src="http://farm8.staticflickr.com/7287/9174190443_224a740ce8.jpg" width="500" height="355" alt="gray, red, green, blue histograms"></a>

Code: [FindHistogram.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/FindHistogram/FindHistogram.pde)

#### HueRangeSelection

Detect objects based on their color. Demonstrates the use of HSV color space as well as range-based image filtering.

<a href="http://www.flickr.com/photos/unavoidablegrain/9193745547/" title="Hue-based color detection by atduskgreg, on Flickr"><img src="http://farm4.staticflickr.com/3799/9193745547_8f09e55a39.jpg" width="500" height="397" alt="Hue-based color detection"></a>

Code: [HueRangeSelection.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/HueRangeSelection/HueRangeSelection.pde)

#### CalibrationDemo (in progress)

An example of the process involved in calibrating a camera. Currently only detects the corners in a chessboard pattern.

<a href="http://www.flickr.com/photos/unavoidablegrain/8706849024/" title="Screen Shot 2013-05-04 at 2.03.23 AM by atduskgreg, on Flickr"><img src="http://farm9.staticflickr.com/8267/8706849024_f2d938ec51.jpg" width="500" height="382" alt="Screen Shot 2013-05-04 at 2.03.23 AM"></a>

Code: [CalibrationDemo.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/CalibrationDemo/CalibrationDemo.pde)

#### HistogramSkinDetection

A more advanced example. Detecting skin in an image based on colors in a region of color space. Warning: uses un-wrapped OpenCV objects and functions.

<a href="http://www.flickr.com/photos/unavoidablegrain/8707167599/" title="Screen Shot 2013-05-04 at 2.25.18 PM by atduskgreg, on Flickr"><img src="http://farm9.staticflickr.com/8135/8707167599_d38fbdfe30.jpg" width="500" height="171" alt="Screen Shot 2013-05-04 at 2.25.18 PM"></a>

Code: [HistogramSkinDetection.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/HistogramSkinDetection/HistogramSkinDetection.pde)

#### DepthFromStereo

An advanced example. Calculates depth information from a pair of stereo images. Warning: uses un-wrapped OpenCV objects and functions.

<a href="http://www.flickr.com/photos/unavoidablegrain/8642493130/" title="Screen Shot 2013-04-12 at 2.27.30 AM by atduskgreg, on Flickr"><img src="http://farm9.staticflickr.com/8260/8642493130_f99dd76f3d.jpg" width="500" height="404" alt="Screen Shot 2013-04-12 at 2.27.30 AM"></a>

Code: [DepthFromStereo.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/DepthFromStereo/DepthFromStereo.pde)

#### WarpPerspective (in progress)

Un-distort an object that's in perspective. Coming to the real API soon.

<a href="http://www.flickr.com/photos/unavoidablegrain/9279197332/" title="Warp Perspective by atduskgreg, on Flickr"><img src="http://farm3.staticflickr.com/2861/9279197332_ca6beb3760.jpg" width="500" height="416" alt="Warp Perspective"></a>

Code: [WarpPerspective.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/WarpPerspective/WarpPerspective.pde)

#### MarkerDetection

An in-depth advanced example. Detect a CV marker in an image, warp perspective, and detect the number stored in the marker. Many steps in the code. Uses many un-wrapped OpenCV objects and functions.

<a href="http://www.flickr.com/photos/unavoidablegrain/8642309968/" title="Screen Shot 2013-04-12 at 12.20.17 AM by atduskgreg, on Flickr"><img src="http://farm9.staticflickr.com/8522/8642309968_257e397db2.jpg" width="500" height="225" alt="Screen Shot 2013-04-12 at 12.20.17 AM"></a>

Code: [MarkerDetection.pde](https://github.com/atduskgreg/opencv-processing/blob/master/examples/MarkerDetection/MarkerDetection.pde)

#### MorphologyOperations

Open and close an image, or do more complicated morphological transformations.

<a href="https://flic.kr/p/tazj7r" title="Morphology operations"><img src="https://farm6.staticflickr.com/5340/17829980821_1734e8bab8_z_d.jpg" width="640" height="393" alt="Morphology operations"></a>

Code: [MorphologyOperations.pde](examples/MorphologyOperations/MorphologyOperations.pde)
