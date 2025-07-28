package com.nosliw.core.application.common.dataassociation;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.matcher.HAPMatchers;

@HAPEntityWithAttribute
public class HAPTunnel extends HAPSerializableImp{

	@HAPAttribute
	public static String FROMENDPOINT = "fromEndPoint";

	@HAPAttribute
	public static String TOENDPOINT = "toEndPoint";

	@HAPAttribute
	public static String MATCHERS = "matchers";
	
	private HAPEndpointInTunnel m_fromEndPoint;
	
	private HAPEndpointInTunnel m_toEndPoint;
	
	private HAPMatchers m_matchers;
	
	public HAPTunnel(HAPEndpointInTunnel fromEndPoint, HAPEndpointInTunnel toEndPoint, HAPMatchers matchers) {
		this.m_fromEndPoint = fromEndPoint;
		this.m_toEndPoint = toEndPoint;
		this.m_matchers = matchers;
	}

	public HAPEndpointInTunnel getFromEndPoint() {   return this.m_fromEndPoint;     }
	public HAPEndpointInTunnel getToEndPoint() {    return this.m_toEndPoint;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(FROMENDPOINT, HAPUtilityJson.buildJson(m_fromEndPoint, HAPSerializationFormat.JSON));
		jsonMap.put(TOENDPOINT, HAPUtilityJson.buildJson(m_toEndPoint, HAPSerializationFormat.JSON));
		jsonMap.put(MATCHERS, HAPUtilityJson.buildJson(m_matchers, HAPSerializationFormat.JSON));
	}
}
