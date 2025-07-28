package com.nosliw.core.application.common.dataassociation;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPDataAssociation extends HAPSerializableImp{

	@HAPAttribute
	public static String TYPE = "type";

	private String m_dataAssociationType;
	
	public HAPDataAssociation(String dataAssociationType) {
		this.m_dataAssociationType = dataAssociationType;
	}
	
	public String getDataAssociationType() {   return this.m_dataAssociationType;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getDataAssociationType());
	}
}
