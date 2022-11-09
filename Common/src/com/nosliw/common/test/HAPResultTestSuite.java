package com.nosliw.common.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPResultTestSuite extends HAPResult{
	//results for suites
	private List<HAPResult> m_testSuiteResults;
	//results for test cases 
	private List<HAPResult> m_testCaseResults;
	
	public HAPResultTestSuite(HAPTestDescription testDesc) {
		super(testDesc);
		this.m_testSuiteResults = new ArrayList<HAPResult>();
		this.m_testCaseResults = new ArrayList<HAPResult>();
	}

	public List<HAPResult> getChildTestSuiteResults(){
		return this.m_testSuiteResults;
	}
	
	public List<HAPResult> getChildTestCaseResults(){
		return this.m_testCaseResults;
	}
	
	public void addTestResult(HAPResult result){
		switch(result.getType()){
		case HAPConstantShared.TESTRESULT_TYPE_CASE:
			this.m_testCaseResults.add(result);
			break;
		case HAPConstantShared.TESTRESULT_TYPE_SUITE:
			this.m_testSuiteResults.add(result);
			break;
		}
		if(!result.isSuccess()){
			this.setFail();
		}
	}
	
	public Iterator<HAPResult> iterator(){
		List<HAPResult> all = new ArrayList<HAPResult>();
		all.addAll(m_testSuiteResults);
		all.addAll(m_testCaseResults);
		return all.iterator();
	}
	
	@Override
	public String getType() {
		return HAPConstantShared.TESTRESULT_TYPE_SUITE;
	}

	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> jsonTypeMap){
		super.buildFullJsonMap(jsonMap, jsonTypeMap);
		jsonMap.put("childTestSuiteResults", HAPUtilityJson.buildJson(this.m_testSuiteResults, HAPSerializationFormat.JSON_FULL));
		jsonMap.put("childTestCaseResults", HAPUtilityJson.buildJson(this.m_testCaseResults, HAPSerializationFormat.JSON_FULL));
	}
}
