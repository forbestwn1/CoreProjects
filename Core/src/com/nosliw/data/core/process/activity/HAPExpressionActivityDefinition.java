package com.nosliw.data.core.process.activity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionActivityNormal;
import com.nosliw.data.core.script.expression.HAPDefinitionScriptEntity;

public class HAPExpressionActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String SCRIPT = "script";
	
	private HAPDefinitionScriptEntity m_script;
	
	public HAPExpressionActivityDefinition(String type) {
		super(type);
	}
	
	public HAPDefinitionScriptEntity getScript(){  return this.m_script;    }

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;

		this.m_script = new HAPDefinitionScriptEntity();
		this.m_script.buildEntityInfoByJson(jsonObj);
		this.m_script.setId(null);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SCRIPT, HAPJsonUtility.buildJson(this.m_script, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPDefinitionActivity cloneActivityDefinition() {
		HAPExpressionActivityDefinition out = new HAPExpressionActivityDefinition(this.getType());
		this.cloneToNormalActivityDefinition(out);
		out.m_script = this.m_script.cloneScriptEntityDefinition();
		return out;
	}
}
