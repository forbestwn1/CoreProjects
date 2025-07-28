package com.nosliw.data.core.component;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.core.resource.HAPFactoryResourceId;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.data.core.domain.complexentity.HAPElementInContainerEntityDefinition;
import com.nosliw.data.core.domain.complexentity.HAPElementInContainerEntityDefinitionReference;

public class HAPElementInContainerEntityDefinitionReferenceImp extends HAPEntityInfoWritableImp implements HAPElementInContainerEntityDefinitionReference{

	@HAPAttribute
	public static String REFERENCE = "reference";

	private HAPResourceId m_resourceId;

	private String m_defaultType;
	
	public HAPElementInContainerEntityDefinitionReferenceImp() {}

	public HAPElementInContainerEntityDefinitionReferenceImp(String defaultType) {
		this.m_defaultType = defaultType;
	}

	public HAPElementInContainerEntityDefinitionReferenceImp(HAPResourceId resourceId) {
		this.m_resourceId = resourceId;
	}

	@Override
	public String getElementType() {	return HAPElementInContainerEntityDefinition.TYPE_REFERENCE;	}

	@Override
	public HAPResourceId getResourceId() {	return this.m_resourceId;	}
	 
	@Override
	protected boolean buildObjectByJson(Object obj){
		JSONObject jsonObj = (JSONObject)obj;
		this.buildEntityInfoByJson(jsonObj);
		this.m_resourceId = HAPFactoryResourceId.newInstance(this.m_defaultType, jsonObj.get(REFERENCE));
		return true;  
	}

	protected void cloneToResourceDefinitionContainerElementReference(HAPElementInContainerEntityDefinitionReferenceImp to) {
		this.cloneToEntityInfo(to);
		to.m_resourceId = this.m_resourceId.clone();
	}
	
	@Override
	public HAPElementInContainerEntityDefinition cloneDefinitionEntityElementInContainer() {
		HAPElementInContainerEntityDefinitionReferenceImp out = new HAPElementInContainerEntityDefinitionReferenceImp();
		this.cloneToResourceDefinitionContainerElementReference(out);
		return out;
	}

}
