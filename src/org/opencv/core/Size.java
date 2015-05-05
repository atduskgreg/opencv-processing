package org.opencv.core;

/**
 * <p>Template class for specifying the size of an image or rectangle. The class
 * includes two members called <code>width</code> and <code>height</code>. The
 * structure can be converted to and from the old OpenCV structures
 * <code>CvSize</code> and <code>CvSize2D32f</code>. The same set of arithmetic
 * and comparison operations as for <code>Point_</code> is available.</p>
 *
 * <p>OpenCV defines the following <code>Size_<></code> aliases:</p>
 *
 * <p>typedef Size_<int> Size2i; <code></p>
 *
 * <p>// C++ code:</p>
 *
 * <p>typedef Size2i Size;</p>
 *
 * <p>typedef Size_<float> Size2f;</p>
 *
 * @see <a href="http://docs.opencv.org/modules/core/doc/basic_structures.html#size">org.opencv.core.Size_</a>
 */
public class Size {

    public double width, height;

    public Size(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public Size() {
        this(0, 0);
    }

    public Size(Point p) {
        width = p.x;
        height = p.y;
    }

    public Size(double[] vals) {
        set(vals);
    }

    public void set(double[] vals) {
        if (vals != null) {
            width = vals.length > 0 ? vals[0] : 0;
            height = vals.length > 1 ? vals[1] : 0;
        } else {
            width = 0;
            height = 0;
        }
    }

    public double area() {
        return width * height;
    }

    public Size clone() {
        return new Size(width, height);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(height);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(width);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Size)) return false;
        Size it = (Size) obj;
        return width == it.width && height == it.height;
    }

    @Override
    public String toString() {
        return (int)width + "x" + (int)height;
    }

}
