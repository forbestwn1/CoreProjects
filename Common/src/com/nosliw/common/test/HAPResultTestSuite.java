package com.nosliw.common.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;

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
		case HAPConstant.CONS_TESTRESULT_TYPE_CASE:
			this.m_testCaseResults.add(result);
			break;
		case HAPConstant.CONS_TESTRESULT_TYPE_SUITE:
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
		return HAPConstant.CONS_TESTRESULT_TYPE_SUITE;
	}

	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> jsonTypeMap){
		jsonMap.put("childTestSuiteResults", HAPJsonUtility.getListObjectJson(this.m_testSuiteResults));
		jsonMap.put("childTestCaseResults", HAPJsonUtility.getListObjectJson(this.m_testCaseResults));
	}
}
