package com.nosliw.core.application.division.manual.core.definition;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.dynamic.HAPContainerInputDynamic;
import com.nosliw.core.resource.HAPResourceId;

public class HAPManualDefinitionWrapperValueReferenceResource extends HAPManualDefinitionWrapperValue{

	//resource id
	public static final String RESOURCEID = "resourceId";

	@HAPAttribute
	public static final String DYNAMICINPUT = "dynamicInput";

	//reference to external resource
	private HAPResourceId m_resourceId;

	private HAPContainerInputDynamic m_dynamicInput;
	
	public HAPManualDefinitionWrapperValueReferenceResource(HAPResourceId resourceId) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE);
		this.m_resourceId = resourceId;
		this.m_dynamicInput = new HAPContainerInputDynamic();
	}

	public HAPResourceId getResourceId() {    return this.m_resourceId;     }
	public void setResourceId(HAPResourceId resourceId) {    this.m_resourceId = resourceId;      }

	public HAPContainerInputDynamic getDyanmicInput(){   return this.m_dynamicInput;     }
	public void setDynamicInput(HAPContainerInputDynamic dynamicInput) {     this.m_dynamicInput = dynamicInput;     }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(RESOURCEID, this.m_resourceId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(DYNAMICINPUT, HAPManagerSerialize.getInstance().toStringValue(this.m_dynamicInput, HAPSerializationFormat.JSON));
	}
}
