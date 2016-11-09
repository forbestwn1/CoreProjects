package com.nosliw.data.datatype.importer.js;

public class HAPJSOperationInfo {

	public String m_operationId;
	
	private String m_script;
	
	private String m_resources;
	
	public HAPJSOperationInfo(String script, String resources, String operationId){
		this.m_operationId = operationId;
		this.m_script = script;
		this.m_resources = resources;
	}
	
}
