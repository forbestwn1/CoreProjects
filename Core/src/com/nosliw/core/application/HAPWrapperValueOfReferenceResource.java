package com.nosliw.core.application;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.dynamic.HAPContainerInputDynamic;
import com.nosliw.core.resource.HAPResourceDependency;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public class HAPWrapperValueOfReferenceResource extends HAPWrapperValue{

	@HAPAttribute
	public static final String RESOURCEID = "resourceId";

	@HAPAttribute
	public static final String DYNAMICINPUT = "dynamicInput";

	private HAPResourceId m_resourceId;
	
	//for resource need dynamic input
	private HAPContainerInputDynamic m_dynamicInput;
	
	public HAPWrapperValueOfReferenceResource(HAPResourceId resourceId) {
		super(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID);
		this.m_resourceId = resourceId;
		this.m_dynamicInput = new HAPContainerInputDynamic();
	}
	
	@Override
	public Object getValue() {     return this.m_resourceId;      }

	public HAPResourceId getResourceId() {    return this.m_resourceId;     }
	
	public HAPContainerInputDynamic getDynamicTaskInput(){    return this.m_dynamicInput;        }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(RESOURCEID, HAPManagerSerialize.getInstance().toStringValue(this.m_resourceId, HAPSerializationFormat.JSON));
		jsonMap.put(DYNAMICINPUT, HAPManagerSerialize.getInstance().toStringValue(this.m_dynamicInput, HAPSerializationFormat.JSON));
	}

	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
		dependency.add(new HAPResourceDependency(this.m_resourceId));
	}

}
