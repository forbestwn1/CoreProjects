package com.nosliw.data.core.activity.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPExecutableActivity;
import com.nosliw.data.core.activity.resource.HAPResourceIdActivityPlugin;
import com.nosliw.data.core.process.HAPActivityPluginId;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.expression.HAPContextProcessExpressionScript;
import com.nosliw.data.core.script.expression.HAPExecutableScriptGroup;

public class HAPExpressionActivityExecutable extends HAPExecutableActivity{

	@HAPAttribute
	public static String SCRIPTEXPRESSION = "scriptExpression";

	@HAPAttribute
	public static String SCRIPTEXPRESSIONSCRIPT = "scriptExpressionScript";

	private HAPContextProcessExpressionScript m_expressionProcessContext;

	private HAPExecutableScriptGroup m_scriptExpression;

	public HAPExpressionActivityExecutable(String id, HAPExpressionActivityDefinition activityDef) {
		super(activityDef.getType(), id, activityDef);
		this.m_expressionProcessContext = new HAPContextProcessExpressionScript();
	}

	public HAPContextProcessExpressionScript getScriptExpressionProcessContext() {
		return this.m_expressionProcessContext;
	}
	
	public void setScriptExpression(HAPExecutableScriptGroup scriptExpression) {    this.m_scriptExpression = scriptExpression;    }
	public HAPExecutableScriptGroup getScriptExpression() {   return this.m_scriptExpression;  }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_scriptExpression = new HAPExecutableScriptGroup();
		this.m_scriptExpression.buildObject(jsonObj.getJSONObject(SCRIPTEXPRESSION), HAPSerializationFormat.JSON);
		return true;  
	}
	
	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.add(new HAPResourceDependency(new HAPResourceIdActivityPlugin(new HAPActivityPluginId(HAPConstantShared.ACTIVITY_TYPE_EXPRESSION))));
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
		jsonMap.put(SCRIPTEXPRESSIONSCRIPT, this.m_scriptExpression.toResourceData(runtimeInfo).toString());
		typeJsonMap.put(SCRIPTEXPRESSIONSCRIPT, HAPJsonTypeScript.class);
	}	
	
}
