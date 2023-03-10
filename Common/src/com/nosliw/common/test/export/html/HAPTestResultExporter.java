package com.nosliw.common.test.export.html;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.test.HAPResult;
import com.nosliw.common.test.HAPResultTestCase;
import com.nosliw.common.test.HAPResultTestSuite;
import com.nosliw.common.test.HAPTestDescription;
import com.nosliw.common.test.HAPResultTestItem;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;

public class HAPTestResultExporter {
	public static void export(HAPResult result, String file){
		String resultStr = export(result);

		InputStream templateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPTestResultExporter.class, "TestResult.txt");
		Map<String, String> parms = new LinkedHashMap<String, String>();
		parms.put("result", resultStr);
		String outStr = HAPStringTemplateUtil.getStringValue(templateStream, parms);
		
		HAPUtilityFile.writeFile(file, outStr);
	}
	
	private static String export(HAPResult result){
		String out = null;
		switch(result.getType()){
		case HAPConstantShared.TESTRESULT_TYPE_CASE:
			out = exportTestCaseResult((HAPResultTestCase)result);
			break;
		case HAPConstantShared.TESTRESULT_TYPE_SUITE:
			out = exportTestSuiteResult((HAPResultTestSuite)result);
			break;
		}
		return out;
	}
	
	private static String exportTestCaseResult(HAPResultTestCase testCaseResult){
		InputStream templateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPTestResultExporter.class, "TestCaseResult.txt");
		Map<String, String> parms = new LinkedHashMap<String, String>();
		parms.put("testName", testCaseResult.getName());
		parms.put("testDescription", testCaseResult.getTestDescription().getDescription());
		parms.put("testResult", String.valueOf(testCaseResult.isSuccess()));
		
		//exceptions
		if(testCaseResult.getExceptions()==null || testCaseResult.getExceptions().size()==0) 		parms.put("testExceptions", "");
		else{
			StringBuffer exceptionsStr = new StringBuffer();
			for(Exception e : testCaseResult.getExceptions()){
				InputStream exceptionStream = HAPUtilityFile.getInputStreamOnClassPath(HAPTestResultExporter.class, "TestException.txt");
				Map<String, String> exceptionParms = new LinkedHashMap<String, String>();
				exceptionParms.put("testException", HAPErrorUtility.log(e));
				exceptionsStr.append(HAPStringTemplateUtil.getStringValue(exceptionStream, exceptionParms));
			}
			parms.put("testExceptions", exceptionsStr.toString());
		}
		
		//test items
		if(testCaseResult.getTestItems()==null || testCaseResult.getTestItems().size()==0)   parms.put("testItems", "");
		else{
			StringBuffer itemsStr = new StringBuffer();
			for(HAPResultTestItem testItem : testCaseResult.getTestItems()){
				InputStream itemStream = HAPUtilityFile.getInputStreamOnClassPath(HAPTestResultExporter.class, "TestItem.txt");
				Map<String, String> testItemParms = new LinkedHashMap<String, String>();
				testItemParms.put("testItemResult", String.valueOf(testItem.isSuccess()));
				testItemParms.put("testItemDescription", testItem.getTestDescription().getDescription());
				itemsStr.append(HAPStringTemplateUtil.getStringValue(itemStream, testItemParms));
			}
			parms.put("testItems", itemsStr.toString());
		}
		
		return HAPStringTemplateUtil.getStringValue(templateStream, parms);
	}
	
	private static String exportTestSuiteResult(HAPResultTestSuite testSuiteResult){
		InputStream templateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPTestResultExporter.class, "TestSuiteResult.txt");
		Map<String, String> parms = new LinkedHashMap<String, String>();
		parms.put("testName", testSuiteResult.getName());
		parms.put("testDescription", testSuiteResult.getTestDescription().getDescription());
		parms.put("testResult", String.valueOf(testSuiteResult.isSuccess()));

		StringBuffer childSuiteStr = new StringBuffer();
		List<HAPResult> childSuiteResults = testSuiteResult.getChildTestSuiteResults();
		for(HAPResult result : childSuiteResults){
			childSuiteStr.append(export(result));
		}
		parms.put("childSuites", childSuiteStr.toString());
		
		StringBuffer childCaseStr = new StringBuffer();
		List<HAPResult> childCaseResults = testSuiteResult.getChildTestCaseResults();
		for(HAPResult result : childCaseResults){
			childCaseStr.append(export(result));
		}
		parms.put("childCases", childCaseStr.toString());
		
		return HAPStringTemplateUtil.getStringValue(templateStream, parms);
	}
}
