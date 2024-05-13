package com.nosliw.core.application.brick.adapter.dataassociation;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public abstract class HAPEndpointInTunnel extends HAPSerializableImp{

	@HAPAttribute
	public static String TYPE = "type";

	private String m_endPointType;
	
	public HAPEndpointInTunnel(String endPointType) {
		this.m_endPointType = endPointType;
	}
	
	public String getEndPointType() {   return this.m_endPointType;    }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.m_endPointType);
	}
}