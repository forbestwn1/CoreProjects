package com.nosliw.data.core.process.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.process.HAPActivityPluginId;
import com.nosliw.data.core.process.HAPExecutableActivityNormal;
import com.nosliw.data.core.process.resource.HAPResourceIdActivityPlugin;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPUtilityRuntimeJSScript;
import com.nosliw.data.core.script.expression.HAPContextProcessScriptExpression;
import com.nosliw.data.core.script.expression.expression.HAPScriptExpression;

public class HAPExpressionActivityExecutable extends HAPExecutableActivityNormal{

	@HAPAttribute
	public static String SCRIPTEXPRESSION = "scriptExpression";

	@HAPAttribute
	public static String SCRIPTEXPRESSIONSCRIPT = "scriptExpressionScript";

	private HAPContextProcessScriptExpression m_expressionProcessContext;

	private HAPScriptExpression m_scriptExpression;

	public HAPExpressionActivityExecutable(String id, HAPExpressionActivityDefinition activityDef) {
		super(id, activityDef);
		this.m_expressionProcessContext = new HAPContextProcessScriptExpression();
	}

	public HAPContextProcessScriptExpression getScriptExpressionProcessContext() {
		return this.m_expressionProcessContext;
	}
	
	public void setScriptExpression(HAPScriptExpression scriptExpression) {    this.m_scriptExpression = scriptExpression;    }
	public HAPScriptExpression getScriptExpression() {   return this.m_scriptExpression;  }
	
//	public HAPExpressionActivityDefinition getExpressionActivityDefinition() {   return (HAPExpressionActivityDefinition)this.getActivityDefinition();   }

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_scriptExpression = new HAPScriptExpression();
		this.m_scriptExpression.buildObject(jsonObj.getJSONObject(SCRIPTEXPRESSION), HAPSerializationFormat.JSON);
		return true;  
	}
	
	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.add(new HAPResourceDependency(new HAPResourceIdActivityPlugin(new HAPActivityPluginId(HAPConstant.ACTIVITY_TYPE_EXPRESSION))));
		out.addAll(this.m_scriptExpression.getResourceDependency(runtimeInfo, resourceManager));
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SCRIPTEXPRESSION, this.m_scriptExpression.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(SCRIPTEXPRESSIONSCRIPT, HAPUtilityRuntimeJSScript.buildScriptExpressionJSFunction(this.m_scriptExpression));
		typeJsonMap.put(SCRIPTEXPRESSIONSCRIPT, HAPJsonTypeScript.class);
	}	
	
}
