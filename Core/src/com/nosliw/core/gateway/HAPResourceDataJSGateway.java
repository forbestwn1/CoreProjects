package com.nosliw.core.gateway;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.resource.imp.js.HAPResourceDataJSValueImp;

public class HAPResourceDataJSGateway extends HAPResourceDataJSValueImp{

	@HAPAttribute
	public static String GATEWAY = "gateway";

	private String m_name;
	
	public HAPResourceDataJSGateway(String name){
		this.m_name = name;
	}
	
	@Override
	public String getValue() {
		
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("gatewayId", this.m_name);
		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPResourceDataJSGateway.class, "GatewayResource.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		return script;
	}

}
