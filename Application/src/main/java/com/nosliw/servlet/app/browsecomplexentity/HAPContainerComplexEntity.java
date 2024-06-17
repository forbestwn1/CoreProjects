package com.nosliw.servlet.app.browsecomplexentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;

@HAPEntityWithAttribute
public class HAPContainerComplexEntity extends HAPSerializableImp{

	@HAPAttribute
	public static String ENTITYTYPE = "entityType";

	@HAPAttribute
	public static String COMPLEXENTITYINFO = "complexEntityInfo";

	private String m_entityType;

	private List<HAPInfoComplexEntity> m_complexEntityInfo = new ArrayList<HAPInfoComplexEntity>();
	
	public HAPContainerComplexEntity(String entityType) {
		this.m_entityType = entityType;
	}
	
	public void addComplexEntityInfo(HAPInfoComplexEntity complexEntityInfo) {
		this.m_complexEntityInfo.add(complexEntityInfo);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITYTYPE, this.m_entityType);
		jsonMap.put(COMPLEXENTITYINFO, HAPManagerSerialize.getInstance().toStringValue(m_complexEntityInfo, HAPSerializationFormat.JSON));
	}
	
}
