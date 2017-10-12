package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility to build expression process configure
 */
public class HAPExpressionProcessConfigureUtil {

	static public Map<String, String> setDoDiscovery(Map<String, String> configure){
		return buildContextConfigure(configure, HAPProcessExpressionDefinitionContext.CONFIGURE_DISCOVERY, "true");
	}

	static public Map<String, String> setDontDiscovery(Map<String, String> configure){
		return buildContextConfigure(configure, HAPProcessExpressionDefinitionContext.CONFIGURE_DISCOVERY, "false");
	}

	static public boolean isDoDiscovery(HAPProcessExpressionDefinitionContext context){
		return "true".equals(context.getConfiguration().getStringValue(HAPProcessExpressionDefinitionContext.CONFIGURE_DISCOVERY));
	}
	
	static private Map<String, String> buildContextConfigure(Map<String, String> configure, String name, String value){
		Map<String, String> out = configure;
		if(out==null)	out = new LinkedHashMap<String, String>();
		out.put(name, value);
		return out;
	}
	
}
