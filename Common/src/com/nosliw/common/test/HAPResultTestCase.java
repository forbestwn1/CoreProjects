package com.nosliw.common.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;

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
	public String getType() {		return HAPConstant.TESTRESULT_TYPE_CASE;	}

	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class> jsonTypeMap, String format){
		super.buildFullJsonMap(jsonMap, jsonTypeMap, format);
		jsonMap.put("childTestItems", HAPJsonUtility.getListObjectJson(this.m_testItems, format));
		jsonMap.put("exceptions", HAPJsonUtility.getListObjectJson(this.m_e, format));
	}
}
