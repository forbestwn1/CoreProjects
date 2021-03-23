package com.nosliw.data.core.component;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPFactoryResourceId;

public class HAPResourceDefinitionContainerElementReferenceImp extends HAPEntityInfoWritableImp implements HAPResourceDefinitionContainerElementReference{

	@HAPAttribute
	public static String REFERENCE = "reference";

	private HAPResourceId m_resourceId;

	private String m_defaultType;
	
	public HAPResourceDefinitionContainerElementReferenceImp() {}

	public HAPResourceDefinitionContainerElementReferenceImp(String defaultType) {
		this.m_defaultType = defaultType;
	}

	public HAPResourceDefinitionContainerElementReferenceImp(HAPResourceId resourceId) {
		this.m_resourceId = resourceId;
	}

	@Override
	public String getType() {	return HAPResourceDefinitionContainerElement.TYPE_REFERENCE;	}

	@Override
	public HAPResourceId getResourceId() {	return this.m_resourceId;	}
	 
	@Override
	protected boolean buildObjectByJson(Object obj){
		JSONObject jsonObj = (JSONObject)obj;
		this.buildEntityInfoByJson(jsonObj);
		this.m_resourceId = HAPFactoryResourceId.newInstance(this.m_defaultType, jsonObj.get(REFERENCE));
		return true;  
	}

	protected void cloneToResourceDefinitionContainerElementReference(HAPResourceDefinitionContainerElementReferenceImp to) {
		this.cloneToEntityInfo(to);
		to.m_resourceId = this.m_resourceId.clone();
	}
	
	@Override
	public HAPResourceDefinitionContainerElement cloneResourceDefinitionContainerElement() {
		HAPResourceDefinitionContainerElementReferenceImp out = new HAPResourceDefinitionContainerElementReferenceImp();
		this.cloneToResourceDefinitionContainerElementReference(out);
		return out;
	}

}
