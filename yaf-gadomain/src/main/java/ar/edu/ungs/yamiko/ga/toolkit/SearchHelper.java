package ar.edu.ungs.yamiko.ga.toolkit;

import java.util.Arrays;

public class SearchHelper {
	    
	    public static Integer binaryRangeSearch(final double[] array, final double value) {
	        Integer i = null;
	        int idx = binarySearch(array, value);
	        if (idx < 0) {
	            idx = -(idx) - 1;
	            if (idx == 0)
	            	return 0;
	            else if (idx >= array.length) 
	            return array.length-1;
	            else {
	                // Find nearest point
	            	if (array[idx-1]<value && array[idx]>=value)
	            		i=idx;
	            	else
	            		i=idx-1;
	            }
	        }
	        else {
	            i = idx;
	        }
	        return i;
	    }

	    private static int binarySearch(double[] a, double key) {
	        int index = -1;
	        if (a[0] <= a[1]) {
	            index = Arrays.binarySearch(a, key);
	        }
	        else {
	            index = binarySearch(a, key, 0, a.length - 1);
	        }
	        return index;
	    }

	    private static int binarySearch(double[] a, double key, int low, int high) {
	        while (low <= high) {
	            int mid = (low + high) / 2;
	            double midVal = a[mid];

	            int cmp;
	            if (midVal > key) {
	                cmp = -1; // Neither val is NaN, thisVal is smaller
	            }
	            else if (midVal < key) {
	                cmp = 1; // Neither val is NaN, thisVal is larger
	            }
	            else {
	                long midBits = Double.doubleToLongBits(midVal);
	                long keyBits = Double.doubleToLongBits(key);
	                cmp = (midBits == keyBits ? 0 : (midBits < keyBits ? -1 : 1)); // (0.0, -0.0) or (NaN, !NaN)
	            }

	            if (cmp < 0) {
	                low = mid + 1;
	            }
	            else if (cmp > 0) {
	                high = mid - 1;
	            }
	            else {
	                return mid; // key found
	            }
	        }
	        return -(low + 1); // key not found.
	    }
}

