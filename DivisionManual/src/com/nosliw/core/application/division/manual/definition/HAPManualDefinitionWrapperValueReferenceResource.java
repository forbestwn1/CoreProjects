package com.nosliw.core.application.division.manual.definition;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPManualDefinitionWrapperValueReferenceResource extends HAPManualDefinitionWrapperValue{

	//resource id
	public static final String RESOURCEID = "resourceId";

	//reference to external resource
	private HAPResourceId m_resourceId;

	public HAPManualDefinitionWrapperValueReferenceResource(HAPResourceId resourceId) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE);
		this.m_resourceId = resourceId;
	}

	public HAPResourceId getResourceId() {    return this.m_resourceId;     }
	public void setResourceId(HAPResourceId resourceId) {    this.m_resourceId = resourceId;      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(RESOURCEID, this.m_resourceId.toStringValue(HAPSerializationFormat.JSON));
	}
}
