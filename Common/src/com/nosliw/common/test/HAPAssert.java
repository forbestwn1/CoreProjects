package com.nosliw.common.test;

import org.junit.Assert;

public class HAPAssert{
	
	public static void assertEquals(Object expected, Object actual, HAPResultTestCase result){
		try{
			Assert.assertEquals(expected, actual);
		}
		catch(Exception e){
			result.addException(e);
		}
	}
}
