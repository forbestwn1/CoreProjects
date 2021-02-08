package com.nosliw.common.test;

import com.nosliw.common.utils.HAPConstantShared;

/*
 * test item is child of test case, 
 */
public class HAPResultTestItem extends HAPResult{

	public HAPResultTestItem(HAPTestDescription description, boolean isSuccess){
		super(description);
		this.setResult(Boolean.valueOf(isSuccess));
	}

	public HAPResultTestItem(HAPTestDescription description){
		super(description);
		this.setResult(null);
	}
		
	@Override
	public String getType() {		return HAPConstantShared.TESTRESULT_TYPE_ITEM;	}
}
