package com.nosliw.core.application;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPUtilityResourceId;

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
	public HAPInfoResourceIdNormalize getNormalizedResourceId() {   return HAPUtilityResourceId.normalizeResourceId(m_resourceId);    }
	
//	public void solidate(HAPManagerApplicationBrick brickMan) {
//		this.m_referBundle = brickMan.getBrickBundle(m_normalizedResourceId.getRootResourceIdSimple());
//		this.m_pathFromRoot = this.m_normalizedResourceId.getPath();
//	}


	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(RESOURCEID, HAPSerializeManager.getInstance().toStringValue(this.m_resourceId, HAPSerializationFormat.JSON));
	}

}
