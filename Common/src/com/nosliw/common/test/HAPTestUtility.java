package com.nosliw.common.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HAPTestUtility {

	public static List<HAPTestInfo> sortTestInfo(Collection<HAPTestInfo> testInfos){
		List<HAPTestInfo> infos = new ArrayList<HAPTestInfo>();
		Iterator<HAPTestInfo> it = testInfos.iterator();
		while(it.hasNext()){
			infos.add(it.next());
		}
		return sortTestInfo(infos);
	}
	
	public static List<HAPTestInfo> sortTestInfo(List<HAPTestInfo> testInfos){
		Collections.sort(testInfos, new Comparator<HAPTestInfo>() {
	        public int compare(HAPTestInfo p1, HAPTestInfo p2) {
	        	return HAPTestUtility.compare(p1, p2);
	        }
		});
		return testInfos;
	}

	/*
	 * compare logic used when sorting test info
	 */
    public static int compare(HAPTestInfo p1, HAPTestInfo p2) {
        int out =  Integer.valueOf(p1.getSequence()).compareTo(p2.getSequence());
        if(out==0){
        	out = p1.getName().compareTo(p2.getName());
        }
        return out;
     }

	public static HAPTestSuiteInfo processTestSuiteClass(String testSuiteClassName){
		try {
			Class testSuiteClass = Class.forName(testSuiteClassName);
			return processTestSuiteClass(testSuiteClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
    /*
     * process testSuiteClass and create test information (test suite and test case within it)
     * testSuiteinfo : 
     * 		if this class is configured as test suite, then a solid test suite created
     * 		otherwise, container test suite
     */
	public static HAPTestSuiteInfo processTestSuiteClass(Class testSuiteClass){
		HAPTestSuiteInfo out = null;
		//if testSuiteClass has HAPTestSuite annotation, create a solid test suite
		//otherwise, not a sold one, just a container of test case
		if(testSuiteClass.isAnnotationPresent(HAPTestSuite.class)){
			//solid test suite
			HAPTestSuite testSuite = (HAPTestSuite)testSuiteClass.getAnnotation(HAPTestSuite.class);
			out = new HAPTestSuiteInfo(new HAPTestDescription(testSuite.name(), testSuite.description()));
			out.setParentName(testSuite.parent());
		}
		else{
			//container test suite
			out = new HAPTestSuiteInfo();
		}

		//find all test cases within this test suite class
		Set<HAPTestCaseInfo> testCases = HAPTestUtility.createTestCases(testSuiteClass);
		for(HAPTestCaseInfo testCase : testCases){
			out.addTest(testCase);
		}
		
		//if there is not a sold test suite and has no test cases, then return null
		if(out.isSolid() || testCases.size()>0){
			//set base object
			try {
				out.getTestEnv().setBaseObject(testSuiteClass.newInstance());
			} catch (Exception e) {		e.printStackTrace();}
			out.resolveTestDescription();
			return out;
		}
		else return null;
	}
	
	/*
	 * process all test cases information defined within a test case class
	 */
	public static Set<HAPTestCaseInfo> createTestCases(Class testCaseClass){
		Set<HAPTestCaseInfo> testCasesInfo = new HashSet<HAPTestCaseInfo>();
		
		List<Method> beforeMethods = new ArrayList<Method>();
		List<Method> afterMethods = new ArrayList<Method>();
		
		Method[] methods = testCaseClass.getMethods();
		//get all pre and post method
		for(Method method : methods){
			if(method.isAnnotationPresent(HAPTestCaseBefore.class)){
				beforeMethods.add(method);
			}
			else if(method.isAnnotationPresent(HAPTestCaseAfter.class)){
				afterMethods.add(method);
			}
		}
		
		//get test case information
		for(Method method : methods){
			if(method.isAnnotationPresent(HAPTestCase.class)){
				HAPTestCase testCase = method.getAnnotation(HAPTestCase.class);
				HAPTestCaseRuntime testCaseRuntime = new HAPTestCaseRuntime(testCaseClass, method, beforeMethods, afterMethods, true);
				HAPTestCaseInfo testCaseInfo = new HAPTestCaseInfo(testCase, testCaseRuntime);
				testCasesInfo.add(testCaseInfo);
			}
			else if(method.isAnnotationPresent(HAPTestCaseItem.class)){
				HAPTestCaseItem testCaseItem = method.getAnnotation(HAPTestCaseItem.class);
				HAPTestCaseRuntime testCaseRuntime = new HAPTestCaseRuntime(testCaseClass, method, beforeMethods, afterMethods, false);
				HAPTestCaseInfo testCaseInfo = new HAPTestCaseInfo(testCaseItem, testCaseRuntime);
				testCasesInfo.add(testCaseInfo);
			}
		}
		return testCasesInfo;
	}
}
