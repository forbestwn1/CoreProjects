package com.nosliw.uiresource.page.execute;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSScriptUtility;
import com.nosliw.data.core.script.expression.HAPEmbededScriptExpression;
import com.nosliw.data.core.script.expression.HAPScriptExpression;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEmbededScriptExpression;

public class HAPUIEmbededScriptExpression extends HAPEmbededScriptExpression{

	@HAPAttribute
	public static final String UIID = "uiId";
	
	@HAPAttribute
	public static final String SCRIPTFUNCTION = "scriptFunction";

	@HAPAttribute
	public static final String SCRIPTEXPRESSIONSCRIPTFUNCTION = "scriptExpressionScriptFunction";
	
	
	private String m_uiId;
	
	//javascript function to execute script expression 
	private HAPScript m_scriptFunction;

	public HAPUIEmbededScriptExpression(HAPDefinitionUIEmbededScriptExpression uiEmbededScriptExpression){
		super(uiEmbededScriptExpression);
		this.m_uiId = uiEmbededScriptExpression.getUIId();
	}
	
	public HAPScript getScriptFunction() {
		if(this.m_scriptFunction==null) {
			this.m_scriptFunction = new HAPScript(HAPRuntimeJSScriptUtility.buildMainScriptEmbededScriptExpression(this));
		}
		return this.m_scriptFunction;
	}
	
	public String getUIId(){   return this.m_uiId;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(UIID, this.m_uiId);
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(UIID, this.m_uiId);
		jsonMap.put(SCRIPTFUNCTION, this.getScriptFunction().toStringValue(HAPSerializationFormat.JSON_FULL));
		typeJsonMap.put(SCRIPTFUNCTION, this.getScriptFunction().getClass());
		
		Map<String, String> scriptJsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> scriptTypeJsonMap = new LinkedHashMap<String, Class<?>>();
		for(String name : this.getScriptExpressions().keySet()) {
			HAPScriptExpression scriptExpression = this.getScriptExpressions().get(name);
			scriptJsonMap.put(name, HAPRuntimeJSScriptUtility.buildScriptExpressionJSFunction(scriptExpression));
			scriptTypeJsonMap.put(name, HAPScript.class);
		}
		jsonMap.put(SCRIPTEXPRESSIONSCRIPTFUNCTION, HAPJsonUtility.buildMapJson(scriptJsonMap, scriptTypeJsonMap));
	}
}
