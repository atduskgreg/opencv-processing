package org.opencv.core;

/**
 * <p>Template class for 3D points specified by its coordinates <em>x</em>,
 * <em>y</em> and <em>z</em>.
 * An instance of the class is interchangeable with the C structure
 * <code>CvPoint2D32f</code>. Similarly to <code>Point_</code>, the coordinates
 * of 3D points can be converted to another type. The vector arithmetic and
 * comparison operations are also supported.</p>
 *
 * <p>The following <code>Point3_<></code> aliases are available:</p>
 *
 * <p>typedef Point3_<int> Point3i; <code></p>
 *
 * <p>// C++ code:</p>
 *
 * <p>typedef Point3_<float> Point3f;</p>
 *
 * <p>typedef Point3_<double> Point3d;</p>
 *
 * @see <a href="http://docs.opencv.org/modules/core/doc/basic_structures.html#point3">org.opencv.core.Point3_</a>
 */
public class Point3 {

    public double x, y, z;

    public Point3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3() {
        this(0, 0, 0);
    }

    public Point3(Point p) {
        x = p.x;
        y = p.y;
        z = 0;
    }

    public Point3(double[] vals) {
        this();
        set(vals);
    }

    public void set(double[] vals) {
        if (vals != null) {
            x = vals.length > 0 ? vals[0] : 0;
            y = vals.length > 1 ? vals[1] : 0;
            z = vals.length > 2 ? vals[2] : 0;
        } else {
            x = 0;
            y = 0;
            z = 0;
        }
    }

    public Point3 clone() {
        return new Point3(x, y, z);
    }

    public double dot(Point3 p) {
        return x * p.x + y * p.y + z * p.z;
    }

    public Point3 cross(Point3 p) {
        return new Point3(y * p.z - z * p.y, z * p.x - x * p.z, x * p.y - y * p.x);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Point3)) return false;
        Point3 it = (Point3) obj;
        return x == it.x && y == it.y && z == it.z;
    }

    @Override
    public String toString() {
        return "{" + x + ", " + y + ", " + z + "}";
    }
}
