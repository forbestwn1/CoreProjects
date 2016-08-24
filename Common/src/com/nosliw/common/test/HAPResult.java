package com.nosliw.common.test;

import java.util.Map;

import com.nosliw.common.serialization.HAPStringableJson;
import com.nosliw.common.utils.HAPConstant;

public abstract class HAPResult extends HAPStringableJson{

	private Boolean m_result = true;
	private HAPTestDescription m_testDescription;
	
	public HAPResult(HAPTestDescription testDesc){
		this.m_testDescription = testDesc;
	}
	
	public abstract String getType();
	
	public HAPTestDescription getTestDescription(){
		return this.m_testDescription;
	}
	
	public String getName(){  return this.getTestDescription().getBasicAncestorValueString(HAPTestDescription.ENTITY_PROPERTY_NAME); }

	public Boolean isSuccess(){	return this.m_result;	}
	protected void setFail(){  this.m_result = false; }
	protected void setResult(Boolean result){  this.m_result = result;}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class> jsonTypeMap, String format){
		jsonMap.put("type", this.getType());
		jsonMap.put("result", String.valueOf(m_result));
		jsonMap.put("testDescription", this.m_testDescription.toStringValue(HAPConstant.CONS_SERIALIZATION_JSON));
	}
	
}
