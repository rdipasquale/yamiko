package ar.edu.ungs.yamiko.test.gadomain;

import static org.junit.Assert.*;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import ar.edu.ungs.yamiko.ga.toolkit.SearchHelper;

public class TestSearchs {

	@Test
	public void test() {
	    double doubleArr[] = {5.4,5.5,9.2,9.3,35.4};

	    double searchVal = 0;
	    int retVal = SearchHelper.binaryRangeSearch(doubleArr, searchVal);
	    System.out.println("The index of element " + searchVal + " is : " + retVal + "(" + doubleArr[retVal] + ")");
	    assertEquals(retVal, 0);
	    
	    searchVal = 4;
	    retVal = SearchHelper.binaryRangeSearch(doubleArr, searchVal);
	    System.out.println("The index of element " + searchVal + " is : " + retVal + "(" + doubleArr[retVal] + ")");
	    assertEquals(retVal, 0);

	    searchVal = 5;
	    retVal = SearchHelper.binaryRangeSearch(doubleArr, searchVal);
	    System.out.println("The index of element " + searchVal + " is : " + retVal + "(" + doubleArr[retVal] + ")");
	    assertEquals(retVal, 0);

	    searchVal = 5.44;
	    retVal = SearchHelper.binaryRangeSearch(doubleArr, searchVal);
	    System.out.println("The index of element " + searchVal + " is : " + retVal + "(" + doubleArr[retVal] + ")");
	    assertEquals(retVal, 1);

	    searchVal = 5.45;
	    retVal = SearchHelper.binaryRangeSearch(doubleArr, searchVal);
	    System.out.println("The index of element " + searchVal + " is : " + retVal + "(" + doubleArr[retVal] + ")");
	    assertEquals(retVal, 1);
	    
	    searchVal = 5.46;
	    retVal = SearchHelper.binaryRangeSearch(doubleArr, searchVal);
	    System.out.println("The index of element " + searchVal + " is : " + retVal + "(" + doubleArr[retVal] + ")");
	    assertEquals(retVal, 1);

	    searchVal = 5.5;
	    retVal = SearchHelper.binaryRangeSearch(doubleArr, searchVal);
	    System.out.println("The index of element " + searchVal + " is : " + retVal + "(" + doubleArr[retVal] + ")");
	    assertEquals(retVal, 1);
	    
	    searchVal = 9;
	    retVal = SearchHelper.binaryRangeSearch(doubleArr, searchVal);
	    System.out.println("The index of element " + searchVal + " is : " + retVal + "(" + doubleArr[retVal] + ")");
	    assertEquals(retVal, 2);
	    
	    searchVal = 9.21;
	    retVal = SearchHelper.binaryRangeSearch(doubleArr, searchVal);
	    System.out.println("The index of element " + searchVal + " is : " + retVal + "(" + doubleArr[retVal] + ")");
	    assertEquals(retVal, 3);
	    
	    searchVal = 9.31;
	    retVal = SearchHelper.binaryRangeSearch(doubleArr, searchVal);
	    System.out.println("The index of element " + searchVal + " is : " + retVal + "(" + doubleArr[retVal] + ")");
	    assertEquals(retVal, 4);
	    
	    searchVal = 10;
	    retVal = SearchHelper.binaryRangeSearch(doubleArr, searchVal);
	    System.out.println("The index of element " + searchVal + " is : " + retVal + "(" + doubleArr[retVal] + ")");
	    assertEquals(retVal, 4);
	    
	    searchVal = 100;
	    retVal = SearchHelper.binaryRangeSearch(doubleArr, searchVal);
	    System.out.println("The index of element " + searchVal + " is : " + retVal + "(" + doubleArr[retVal] + ")");
	    assertEquals(retVal, 4);	    
	    
	    }
}
