package com.nosliw.data.core.runtime.js;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.runtime.HAPResourceData;

public class HAPResourceDataJSGateway extends HAPSerializableImp implements HAPResourceData, HAPResourceDataJSValue{

	@HAPAttribute
	public static String GATEWAY = "gateway";

	private String m_name;
	
	public HAPResourceDataJSGateway(String name){
		this.m_name = name;
	}
	
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
	}
	
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildFullJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	public String getValue() {
		
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("gatewayId", this.m_name);
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPResourceDataJSGateway.class, "GatewayResource.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		return script;
	}

}
