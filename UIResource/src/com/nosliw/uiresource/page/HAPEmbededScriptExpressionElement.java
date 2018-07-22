package com.nosliw.uiresource.page;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.expressionscript.HAPEmbededScriptExpression;
import com.nosliw.data.core.expressionscript.HAPScriptExpression;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSScriptUtility;

public class HAPEmbededScriptExpressionElement extends HAPEmbededScriptExpression{

	@HAPAttribute
	public static final String UIID = "uiId";
	
	@HAPAttribute
	public static final String SCRIPTFUNCTION = "scriptFunction";

	private String m_uiId;
	
	//javascript function to execute script expression 
	private HAPScript m_scriptFunction;

	public HAPEmbededScriptExpressionElement(String uiId, HAPScriptExpression scriptExpression, HAPExpressionSuiteManager expressionManager){
		super(scriptExpression, expressionManager);
		this.m_uiId = uiId;
		this.init();
	}
	
	public HAPEmbededScriptExpressionElement(String uiId, List<Object> elements, HAPExpressionSuiteManager expressionManager){
		super(elements, expressionManager);
		this.m_uiId = uiId;
		this.init();
	}

	public HAPEmbededScriptExpressionElement(String uiId, String content, HAPExpressionSuiteManager expressionManager){
		super(content, expressionManager);
		this.m_uiId = uiId;
		this.init();
	}

	private void init() {
		this.m_scriptFunction = new HAPScript(HAPRuntimeJSScriptUtility.buildEmbedScriptExpressionJSFunction(this));
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
	}
}
