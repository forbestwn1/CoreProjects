package com.nosliw.uiresource.page.execute;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSScriptUtility;
import com.nosliw.data.core.script.expression.HAPEmbededScriptExpression;
import com.nosliw.data.core.script.expression.HAPScriptExpression;

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

	public HAPUIEmbededScriptExpression(String uiId, HAPScriptExpression scriptExpression){
		super(scriptExpression);
		this.m_uiId = uiId;
		this.init();
	}
	
	public HAPUIEmbededScriptExpression(String uiId, List<Object> elements){
		super(elements);
		this.m_uiId = uiId;
		this.init();
	}

	public HAPUIEmbededScriptExpression(String uiId, String content){
		super(content);
		this.m_uiId = uiId;
		this.init();
	}

	private void init() {
		this.m_scriptFunction = new HAPScript(HAPRuntimeJSScriptUtility.buildMainScriptEmbededScriptExpression(this));
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
		jsonMap.put(SCRIPTFUNCTION, m_scriptFunction.toStringValue(HAPSerializationFormat.JSON_FULL));
		typeJsonMap.put(SCRIPTFUNCTION, m_scriptFunction.getClass());
		
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
