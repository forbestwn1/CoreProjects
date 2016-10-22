package com.nosliw.data1;

import java.util.Set;

/*
 * store all information related with script operation
 */
public class HAPScriptOperationInfo {

	private String m_script;

	//all data types that is operations depend on : used within implementation
	//this information is used for javascript side as data type is loaded on demand
	private Set<HAPDataTypeInfo> m_dependentDataTypeInfos;
	
	public HAPScriptOperationInfo(String script, Set<HAPDataTypeInfo> dependentDataTypeInfos){
		this.m_script = script;
		this.m_dependentDataTypeInfos = dependentDataTypeInfos;
	}
	
	public String getScript(){  return this.m_script; }
	
	public Set<HAPDataTypeInfo> getDependentDataTypeInfos(){  return this.m_dependentDataTypeInfos; }
	
}
