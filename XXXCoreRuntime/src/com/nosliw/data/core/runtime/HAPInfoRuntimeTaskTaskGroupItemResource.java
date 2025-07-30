package com.nosliw.data.core.runtime;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;

public class HAPInfoRuntimeTaskTaskGroupItemResource{

	private String m_resourceType;
	
	private String m_resourceId;
	
	private String m_itemName;
	
	private Class m_outputClass;
	
	public HAPInfoRuntimeTaskTaskGroupItemResource(String resourceType, String resourceId, String itemName, Class outputClass){
		this.m_resourceType = resourceType;
		this.m_resourceId = resourceId;
		this.m_itemName = itemName;
		if(HAPUtilityBasic.isStringEmpty(this.m_itemName))   this.m_itemName = HAPConstantShared.NAME_DEFAULT;
		this.m_outputClass = outputClass;
	}
	
	public String getResourceType() {     return this.m_resourceType;     }
	
	public String getResourceId() {     return this.m_resourceId;     }
	
	public String getItemName() {    return this.m_itemName;   }
	
	public Class getOutputClass() {    return this.m_outputClass;     }
	
}
