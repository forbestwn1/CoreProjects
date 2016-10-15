package com.nosliw.common.test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.common.utils.HAPSegmentParser;

public class HAPTestSuiteInfo extends HAPTestInfo{

	private final static String DESCRIPTION_PARENTNAME = "parentName";
	
	//all the test cases information
	private List<HAPTestInfo> m_testCasesInfos;

	//all child test suites
	private Map<String, HAPTestInfo> m_testSuitesInfos;

	//indicate if this test suite info represent a real test suite or just a container of test case
	private boolean m_isSolid = true;
	
	public HAPTestSuiteInfo(HAPTestDescription description, HAPTestEnv testEnv){
		super(description, -1, testEnv);
		this.m_testCasesInfos = new ArrayList<HAPTestInfo>();
		this.m_testSuitesInfos = new LinkedHashMap<String, HAPTestInfo>();
	}

	public HAPTestSuiteInfo(){
		this(null);
		this.m_isSolid = false;
	}
	
	public HAPTestSuiteInfo(HAPTestDescription description){
		this(description, null);
	}

	@Override
	public void beforeRunTest(){
		for(HAPTestInfo testInfo : this.m_testCasesInfos)   testInfo.beforeRunTest();
		for(String name : this.m_testSuitesInfos.keySet())  this.m_testSuitesInfos.get(name).beforeRunTest();
		this.setInited();
	}

	@Override
	public String getType(){ return HAPConstant.TEST_TYPE_SUITE; }

	public void setParentName(String name){ 
		if(HAPBasicUtility.isStringNotEmpty(name)) this.getTestDescription().updateBasicChild(DESCRIPTION_PARENTNAME, name); 
	}
	public String getParentName(){  return (String)this.getTestDescription().getBasicAncestorValue(DESCRIPTION_PARENTNAME); }
	
	public boolean isSolid(){ return this.m_isSolid; }
	
	@Override
	public HAPResult run(HAPResult parentResult){
		HAPResultTestSuite parentSuiteResult = (HAPResultTestSuite)parentResult;
		if(!this.inited())  this.beforeRunTest();
		
		HAPResultTestSuite result = new HAPResultTestSuite(this.getTestDescription());
		
		List<HAPTestInfo> suiteInfos = HAPTestUtility.sortTestInfo(this.m_testSuitesInfos.values());
		for(HAPTestInfo testSuiteInfo : suiteInfos){
			testSuiteInfo.run(result);
		}
		
		HAPTestUtility.sortTestInfo(this.m_testCasesInfos);
		for(HAPTestInfo testCaseInfo : this.m_testCasesInfos){
			testCaseInfo.run(result);
		}

		return this.addToParentResult(parentSuiteResult, result);
	}

	
	public void mergeSoft(HAPTestSuiteInfo testSuite){
		for(HAPTestInfo testInfo : testSuite.m_testCasesInfos){
			this.addTest(testInfo);
		}
		
		for(String name : testSuite.m_testSuitesInfos.keySet()){
			this.addTest(testSuite.m_testSuitesInfos.get(name));
		}
	}
	
	/*
	 * add test to test suite
	 * if test is test suite, then do merge if same test suite exits
	 */
	public void addTest(HAPTestInfo test){
		this.updateChildTestCase(test);
		String testType = test.getType();
		switch(testType){
		case HAPConstant.TEST_TYPE_CASE:
			this.m_testCasesInfos.add(test);
			break;
		case HAPConstant.TEST_TYPE_SUITE:
			HAPTestSuiteInfo testSuite = (HAPTestSuiteInfo)test;
			if(testSuite.isSolid()){
				//solid test suite
				String testSuiteName = testSuite.getName();
				String testSuiteParentName = testSuite.getParentName();
				
				HAPTestSuiteInfo currentSuiteInfo = this;
				if(HAPBasicUtility.isStringNotEmpty(testSuiteParentName)){
					HAPSegmentParser segments = new HAPSegmentParser(testSuiteParentName, HAPConstant.SEPERATOR_PATH);
					while(segments.hasNext()){
						String name = segments.next();
						HAPTestSuiteInfo subSuiteInfo = currentSuiteInfo.getChildTestSuiteInfo(name);
						if(subSuiteInfo==null){
							subSuiteInfo = new HAPTestSuiteInfo(new HAPTestDescription(name, ""));
							currentSuiteInfo.addTest(subSuiteInfo);
						}
						currentSuiteInfo = subSuiteInfo;
					}
				}

				HAPTestSuiteInfo s1 = (HAPTestSuiteInfo)currentSuiteInfo.getChildTestSuiteInfo(testSuiteName);
				if(s1==null)  currentSuiteInfo.m_testSuitesInfos.put(testSuiteName, testSuite);
				else		s1.mergeSoft(testSuite);
			}
			else{
				for(HAPTestInfo t1 : testSuite.m_testCasesInfos)				this.addTest(t1);
				for(String name : testSuite.m_testSuitesInfos.keySet())  		this.addTest(testSuite.m_testSuitesInfos.get(name));
			}
			break;
		}
	}	
	
	private void updateChildTestCase(HAPTestInfo childTestInfo){
		childTestInfo.setParentTestInfo(this);
	}
	
	private HAPTestSuiteInfo getChildTestSuiteInfo(String name){
		return (HAPTestSuiteInfo)this.m_testSuitesInfos.get(name);
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> jsonTypeMap){
		super.buildFullJsonMap(jsonMap, jsonTypeMap);
		jsonMap.put("isSolid", String.valueOf(m_isSolid));
		HAPJsonUtility.buildJson(this.m_testCasesInfos.toArray(new HAPTestInfo[0]), HAPSerializationFormat.JSON_FULL);
		HAPJsonUtility.buildJson(this.m_testSuitesInfos, HAPSerializationFormat.JSON_FULL);
	}
}
