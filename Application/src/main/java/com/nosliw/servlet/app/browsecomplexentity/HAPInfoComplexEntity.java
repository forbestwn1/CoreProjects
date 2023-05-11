package com.nosliw.servlet.app.browsecomplexentity;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

@HAPEntityWithAttribute
public class HAPInfoComplexEntity extends HAPSerializableImp{

	@HAPAttribute
	public static String RESOURCEID = "resourceId";

	@HAPAttribute
	public static String RESOURCENAME = "resourceName";

	private HAPResourceId m_resourceId;
	
	private String m_resourceName;
	
	public HAPInfoComplexEntity(HAPResourceId resourceId) {
		this.m_resourceId = resourceId;
		String resourceStructure = resourceId.getStructure();
		if(resourceStructure.equals(HAPConstantShared.RESOURCEID_TYPE_SIMPLE)) {
			this.m_resourceName = ((HAPResourceIdSimple)resourceId).getId(); 
		}
	}
	
	public HAPResourceId getResourceId() {    return this.m_resourceId;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(RESOURCEID, this.m_resourceId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(RESOURCENAME, this.m_resourceName);
	}
}
