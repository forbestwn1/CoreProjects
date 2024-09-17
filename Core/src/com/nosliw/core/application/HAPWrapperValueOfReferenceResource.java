package com.nosliw.core.application;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPWrapperValueOfReferenceResource extends HAPWrapperValue{

	@HAPAttribute
	public static final String RESOURCEID = "resourceId";

	private HAPResourceId m_resourceId;
	
	public HAPWrapperValueOfReferenceResource(HAPResourceId resourceId) {
		super(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID);
		this.m_resourceId = resourceId;
	}
	
	@Override
	public Object getValue() {     return this.m_resourceId;      }

	public HAPResourceId getResourceId() {    return this.m_resourceId;     }
	
//	public void solidate(HAPManagerApplicationBrick brickMan) {
//		this.m_referBundle = brickMan.getBrickBundle(m_normalizedResourceId.getRootResourceIdSimple());
//		this.m_pathFromRoot = this.m_normalizedResourceId.getPath();
//	}


	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(RESOURCEID, HAPManagerSerialize.getInstance().toStringValue(this.m_resourceId, HAPSerializationFormat.JSON));
	}

	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
		dependency.add(new HAPResourceDependency(this.m_resourceId));
	}

}
