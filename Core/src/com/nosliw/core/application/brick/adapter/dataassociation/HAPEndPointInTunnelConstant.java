package com.nosliw.core.application.brick.adapter.dataassociation;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public class HAPEndPointInTunnelConstant extends HAPEndpointInTunnel{

	@HAPAttribute
	public static String VALUE = "value";

	private Object m_value;
	
	public HAPEndPointInTunnelConstant(Object value) {
		super(HAPConstantShared.TUNNELENDPOINT_TYPE_CONSTANT);
		this.m_value = value;
	}
	
	public Object getValue() {   return this.m_value;   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUE, HAPSerializeManager.getInstance().toStringValue(m_value, HAPSerializationFormat.JSON));
	}
}
