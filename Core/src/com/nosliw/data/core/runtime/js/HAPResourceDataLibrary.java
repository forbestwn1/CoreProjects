package com.nosliw.data.core.runtime.js;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.runtime.HAPResourceData;

@HAPEntityWithAttribute
public class HAPResourceDataLibrary extends HAPSerializableImp implements HAPResourceData{

	@HAPAttribute
	public static String SCRIPT = "script";

	private String m_script;
	
	public HAPResourceDataLibrary(String script){
		this.m_script = script;
	}
	
	public String getScript(){
		return this.m_script;
	}

	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(SCRIPT, HAPSerializeManager.getInstance().toStringValue(this.getScript(), HAPSerializationFormat.LITERATE));
	}
	
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildFullJsonMap(jsonMap, typeJsonMap);
	}

}
