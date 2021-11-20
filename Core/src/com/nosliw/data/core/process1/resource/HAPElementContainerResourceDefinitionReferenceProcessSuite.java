package com.nosliw.data.core.process1.resource;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPElementInContainerEntityDefinition;
import com.nosliw.data.core.complex.HAPElementInContainerEntityDefinitionReference;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPFactoryResourceId;

public class HAPElementContainerResourceDefinitionReferenceProcessSuite extends HAPEntityInfoWritableImp implements HAPElementInContainerEntityDefinitionReference{

	@HAPAttribute
	public static String REFERENCE = "reference";

	private HAPResourceId m_resourceId;

	public HAPElementContainerResourceDefinitionReferenceProcessSuite() {}

	public HAPElementContainerResourceDefinitionReferenceProcessSuite(HAPResourceId resourceId) {
		this.m_resourceId = resourceId;
	}

	@Override
	public String getType() {	return HAPConstantShared.PROCESSSUITE_ELEMENTTYPE_REFERENCE;	}

	@Override
	public HAPResourceId getResourceId() {	return this.m_resourceId;	}
	
	@Override
	protected boolean buildObjectByJson(Object obj){
		JSONObject jsonObj = (JSONObject)obj;
		this.buildEntityInfoByJson(jsonObj);
		this.m_resourceId = HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESS, jsonObj.get(REFERENCE));
		return true;  
	}

	@Override
	public HAPElementInContainerEntityDefinition cloneDefinitionEntityElementInContainer() {
		HAPElementContainerResourceDefinitionReferenceProcessSuite out = new HAPElementContainerResourceDefinitionReferenceProcessSuite();
		this.cloneToEntityInfo(out);
		out.m_resourceId = this.m_resourceId.clone();
		return out;
	}

}
