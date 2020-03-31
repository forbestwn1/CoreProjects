package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;

/**
 * Utility to build expression process configure
 */
public class HAPExpressionProcessConfigureUtil {

	public static final String CONFIGURE_DISCOVERY = "discovery";
	
	static public Map<String, String> setDoDiscovery(Map<String, String> configure){
		return buildContextConfigure(configure, CONFIGURE_DISCOVERY, "true");
	}

	static public Map<String, String> setDontDiscovery(Map<String, String> configure){
		return buildContextConfigure(configure, CONFIGURE_DISCOVERY, "false");
	}

	static public boolean isDoDiscovery(Map<String, String> configure){
		boolean out = true;
		if(configure!=null) {
			out = "true".equals(configure.get(CONFIGURE_DISCOVERY));
		}
		return out;
	}
	
	static private Map<String, String> buildContextConfigure(Map<String, String> configure, String name, String value){
		Map<String, String> out = configure;
		if(out==null)	out = new LinkedHashMap<String, String>();
		out.put(name, value);
		return out;
	}
	
	public static HAPConfigureContextProcessor getContextProcessConfigurationForExpression() {
		HAPConfigureContextProcessor out = new HAPConfigureContextProcessor();
		return out;
	}


}
