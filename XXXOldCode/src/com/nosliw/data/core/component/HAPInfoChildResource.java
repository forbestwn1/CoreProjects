package com.nosliw.data.core.component;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.core.resource.HAPResourceId;

//child resource info defined in parent resource
public class HAPInfoChildResource extends HAPEntityInfoWritableImp{

	private HAPResourceId m_resourceId;

	public HAPInfoChildResource(String name, HAPResourceId resourceId, HAPInfo info) {
		this.setName(name);
		this.setResourceId(resourceId);
		this.setInfo(info);
	}
	
	public HAPResourceId getResourceId() {	return this.m_resourceId;	}

	public void setResourceId(HAPResourceId resourceId) {   this.m_resourceId = resourceId;   }
	
}
