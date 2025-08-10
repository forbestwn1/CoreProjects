package com.nosliw.core.application.common.scriptexpressio.definition;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPDefinitionScriptExpression extends HAPSerializableImp{

	public static String SCRIPTEXPRESSION = "scriptExpression";

	public static String SCRIPTEXPRESSIONTYPE = "scriptExpressionType";

	private String m_scriptExpressionStr;
	
	private String m_scriptExpressionType;

	public HAPDefinitionScriptExpression() {
	}

	public HAPDefinitionScriptExpression(String scriptExpressionStr) {
		this(scriptExpressionStr, null);
	}
	
	public HAPDefinitionScriptExpression(String scriptExpressionStr, String scriptExpressionType) {
		this.m_scriptExpressionStr = scriptExpressionStr;
		this.m_scriptExpressionType = scriptExpressionType;
		this.normalizeType();
	}
	
	public String getScriptExpression() {   return this.m_scriptExpressionStr;      }
	
	public String getScriptExpressionType() {    return this.m_scriptExpressionType;      }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(SCRIPTEXPRESSION, this.m_scriptExpressionStr);
		jsonMap.put(SCRIPTEXPRESSIONTYPE, this.m_scriptExpressionType);
	}

	@Override
	protected boolean buildObjectByJson(Object json){  
		if(json instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)json;
			this.m_scriptExpressionStr = jsonObj.getString(SCRIPTEXPRESSION);
			this.m_scriptExpressionType = (String)jsonObj.opt(SCRIPTEXPRESSIONTYPE);
		}
		else if(json instanceof String) {
			this.m_scriptExpressionStr = (String)json;
		}
		
		this.normalizeType();
		return false;  
	}
	
	private void normalizeType() {
		if(this.m_scriptExpressionType==null) {
			this.m_scriptExpressionType = HAPConstantShared.EXPRESSION_TYPE_LITERATE;
		}  
	}
}
