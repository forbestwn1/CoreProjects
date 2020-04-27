package com.nosliw.data.core.script.expression;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

public class HAPScript extends HAPSerializableImp{

	@HAPAttribute
	public static String SCRIPT = "script";

	@HAPAttribute
	public static String TYPE = "type";

	private String m_type;
	
	private String m_script;

	public static HAPScript newScriptExpression(String script) {	return new HAPScript(script, HAPConstant.SCRIPT_TYPE_EXPRESSION);	}
	public static HAPScript newScriptLiterate(String script) {	return new HAPScript(script, HAPConstant.SCRIPT_TYPE_LITERATE);	}
	public static HAPScript newScript(String script, String type) {    return new HAPScript(script, type);     }
	public static HAPScript newScript(JSONObject jsonObject) {
		HAPScript out = new HAPScript();
		out.buildObject(jsonObject, HAPSerializationFormat.JSON);
		return out;
	}
	
	private HAPScript() {}
	
	protected HAPScript(String script, String type) {
		this.m_script = script;
		this.m_type = type;
	}
	
	public String getType() {   return this.m_type;    }
	
	public String getScript() {   return this.m_script;     }
	public void setScript(String script) {   this.m_script = script;   }

	public HAPScript cloneScript() {
		HAPScript out = new HAPScript(this.m_script, this.m_type);
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_script = jsonObj.getString(SCRIPT);
		this.m_type = (String)jsonObj.opt(TYPE);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.m_type);
		jsonMap.put(SCRIPT, this.m_script);
	}
}
