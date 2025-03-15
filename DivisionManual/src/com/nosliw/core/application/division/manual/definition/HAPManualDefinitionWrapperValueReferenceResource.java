package com.nosliw.core.application.division.manual.definition;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dynamic.HAPInputDynamicTask;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPManualDefinitionWrapperValueReferenceResource extends HAPManualDefinitionWrapperValue{

	//resource id
	public static final String RESOURCEID = "resourceId";

	@HAPAttribute
	public static final String DYNAMICINPUT = "dynamicInput";

	//reference to external resource
	private HAPResourceId m_resourceId;

	private Map<String, HAPInputDynamicTask> m_dynamicInput;
	
	public HAPManualDefinitionWrapperValueReferenceResource(HAPResourceId resourceId) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE);
		this.m_resourceId = resourceId;
		this.m_dynamicInput = new LinkedHashMap<String, HAPInputDynamicTask>();
	}

	public HAPResourceId getResourceId() {    return this.m_resourceId;     }
	public void setResourceId(HAPResourceId resourceId) {    this.m_resourceId = resourceId;      }

	public void addDyanmicInput(HAPInputDynamicTask dyanmicInput) {
		if(dyanmicInput.getName()==null) {
			dyanmicInput.setName(HAPConstantShared.NAME_DEFAULT);
		}
		this.m_dynamicInput.put(dyanmicInput.getName(), dyanmicInput);
	}
	public Set<HAPInputDynamicTask> getDyanmicInputs(){   return new HashSet(this.m_dynamicInput.values());     }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(RESOURCEID, this.m_resourceId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(DYNAMICINPUT, HAPManagerSerialize.getInstance().toStringValue(this.m_dynamicInput, HAPSerializationFormat.JSON));
	}
}
