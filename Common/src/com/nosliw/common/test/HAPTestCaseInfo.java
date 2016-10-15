package com.nosliw.common.test;

import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPTestCaseInfo extends HAPTestInfo{
	//information for executing the test case: class, before method, test case method, after method
	private HAPTestCaseRuntime m_testCaseRuntime;
	
	/*
	 * for single test method
	 */
	public HAPTestCaseInfo(HAPTestCase testCase, HAPTestCaseRuntime testCaseRuntime){
		super(new HAPTestDescription(testCase), testCase.sequence());
		this.m_testCaseRuntime = testCaseRuntime;
		this.afterInit();
	}

	/*
	 * for test case with mutiple test case items
	 */
	public HAPTestCaseInfo(HAPTestCaseItem testCase, HAPTestCaseRuntime testCaseRuntime){
		super(new HAPTestDescription(testCase), testCase.sequence());
		this.m_testCaseRuntime = testCaseRuntime;
		
		this.afterInit();
	}

	private void afterInit(){
		if(HAPBasicUtility.isStringEmpty(this.getName())){
			//for empty name, use method name instead
			this.setName(this.m_testCaseRuntime.getMethodName());
		}
	}
	
	@Override
	public void beforeRunTest(){
		if(this.getTestEnv()!=null){
			//set base object to used when updating document info
			Object testCaseObj = this.m_testCaseRuntime.getTestCaseObject(this.getTestEnv());
			this.getTestEnv().setBaseObject(testCaseObj);

			//update description info: update variable place holder
			this.resolveTestDescription();
		}
		this.setInited();
	}

	@Override
	public String getType(){ return HAPConstant.TEST_TYPE_CASE; }

	public HAPTestCaseRuntime getTestCaseRuntime(){
		return this.m_testCaseRuntime;
	}
	
	@Override
	public HAPResult run(HAPResult parentResult){
		HAPResultTestSuite parentSuiteResult = (HAPResultTestSuite)parentResult;
		if(!this.inited())  this.beforeRunTest();
		HAPResultTestCase result = new HAPResultTestCase(this.getTestDescription());
		result = this.m_testCaseRuntime.run(result, this.getTestEnv());
		return this.addToParentResult(parentSuiteResult, result);
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> jsonTypeMap){
		super.buildFullJsonMap(jsonMap, jsonTypeMap);
		jsonMap.put("runtimeInfo", this.m_testCaseRuntime.toStringValue(null));
	}
}
