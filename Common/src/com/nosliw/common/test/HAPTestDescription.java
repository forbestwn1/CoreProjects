package com.nosliw.common.test;

import com.nosliw.common.strvalue.HAPStringableValueEntityBasic;

public class HAPTestDescription extends HAPStringableValueEntityBasic{
	public HAPTestDescription(){}
	
	public HAPTestDescription(String name, String description){
		super(name, description);
	}

	public HAPTestDescription(HAPTestSuite testSuite){
		super(testSuite.name(), testSuite.description());
	}
	
	public HAPTestDescription(HAPTestCase testCase){
		super(testCase.name(), testCase.description());
	}

	public HAPTestDescription(HAPTestCaseItem testCaseItem){
		super(testCaseItem.name(), testCaseItem.description());
	}
}
