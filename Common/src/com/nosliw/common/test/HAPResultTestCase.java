package com.nosliw.common.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPResultTestCase extends HAPResult{

	private List<Exception> m_e;
	private List<HAPResultTestItem> m_testItems;
	
	public HAPResultTestCase(HAPTestDescription testDesc) {
		super(testDesc);
		this.m_testItems = new ArrayList<HAPResultTestItem>();
		this.m_e = new ArrayList<Exception>();
	}

	public void addException(Exception e){
		this.m_e.add(e);
		this.setFail();	
	}
	
	public List<Exception> getExceptions(){  return m_e; }
	
	public void addIsScuss(boolean b){
		if(!b)  this.setFail();  
	}
	
	public void importResult(HAPResultTestCase result){
		if(!result.isSuccess())  this.setFail();  
		this.m_e.addAll(result.m_e);
		this.m_testItems.addAll(result.m_testItems);
	}

	public void addTestItemResult(HAPResultTestItem testItem){
		this.m_testItems.add(testItem);
		Boolean isSuccess = testItem.isSuccess();
		if(isSuccess!=null){
			this.addIsScuss(isSuccess);
		}
	}
	
	public List<HAPResultTestItem> getTestItems(){ return this.m_testItems; }
	
	@Override
	public String getType() {		return HAPConstantShared.TESTRESULT_TYPE_CASE;	}

	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> jsonTypeMap){
		super.buildFullJsonMap(jsonMap, jsonTypeMap);
		jsonMap.put("childTestItems", HAPJsonUtility.buildJson(this.m_testItems, HAPSerializationFormat.JSON_FULL));
		jsonMap.put("exceptions", HAPJsonUtility.buildJson(this.m_e, HAPSerializationFormat.JSON_FULL));
	}
}
