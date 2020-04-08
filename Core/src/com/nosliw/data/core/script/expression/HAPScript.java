package com.nosliw.data.core.script.expression;

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
	
	private HAPScript(String script, String type) {
		this.m_script = script;
		this.m_type = type;
	}
	
	public String getType() {   return this.m_type;    }
	
	public String getScript() {   return this.m_script;     }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_script = jsonObj.getString(SCRIPT);
		this.m_type = (String)jsonObj.opt(TYPE);
		return true;  
	}

}
