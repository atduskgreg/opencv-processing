package gab.opencv;

import java.util.Comparator;

public class ContourComparator implements Comparator<Contour> {
	  public int compare(Contour c1, Contour c2) {
	    if(c1.area() == c2.area()){
	     return 0;
	    }
	    else if (c1.area() > c2.area()) {
	      return -1;
	    } else{
	      return 1;
	    }
	  }
}