package com.nosliw.data.core.process;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPResourceDefinitionContainerElement;
import com.nosliw.data.core.component.HAPResourceDefinitionContainerElementReference;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdFactory;

public class HAPDefinitionProcessSuiteElementReference extends HAPEntityInfoWritableImp implements HAPResourceDefinitionContainerElementReference{

	@HAPAttribute
	public static String REFERENCE = "reference";

	private HAPResourceId m_resourceId;

	public HAPDefinitionProcessSuiteElementReference() {}

	public HAPDefinitionProcessSuiteElementReference(HAPResourceId resourceId) {
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
		this.m_resourceId = HAPResourceIdFactory.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESS, jsonObj.get(REFERENCE));
		return true;  
	}

	@Override
	public HAPResourceDefinitionContainerElement cloneResourceDefinitionContainerElement() {
		HAPDefinitionProcessSuiteElementReference out = new HAPDefinitionProcessSuiteElementReference();
		this.cloneToEntityInfo(out);
		out.m_resourceId = this.m_resourceId.clone();
		return out;
	}

}
