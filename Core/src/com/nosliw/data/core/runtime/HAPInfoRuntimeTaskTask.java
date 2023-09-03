package com.nosliw.data.core.runtime;

public class HAPInfoRuntimeTaskTask{

	private String m_resourceType;
	
	private String m_resourceId;
	
	private Class m_outputClass;
	
	public HAPInfoRuntimeTaskTask(String resourceType, String resourceId, Class outputClass){
		this.m_resourceType = resourceType;
		this.m_resourceId = resourceId;
		this.m_outputClass = outputClass;
	}
	
	public String getResourceType() {     return this.m_resourceType;     }
	
	public String getResourceId() {     return this.m_resourceId;     }
	
	public Class getOutputClass() {    return this.m_outputClass;     }
	
}
