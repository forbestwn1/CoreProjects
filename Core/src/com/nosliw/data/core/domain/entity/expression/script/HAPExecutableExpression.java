package com.nosliw.data.core.domain.entity.expression.script;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonTypeAsItIs;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.domain.entity.expression.script.resource.js.HAPScriptFunctionInfo;
import com.nosliw.data.core.domain.entity.expression.script.resource.js.HAPUtilityScriptForExecuteJSScript;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;

public class HAPExecutableExpression extends HAPExecutableImpEntityInfo{

	@HAPAttribute
	public final static String TYPE = "type";

	@HAPAttribute
	public static final String SCRIPTFUNCTION = "scriptFunction";

	@HAPAttribute
	public static final String SUPPORTFUNCTION = "supportFunction";

	@HAPAttribute
	public static final String VARIABLESINFO = "variablesInfo";

	@HAPAttribute
	public static final String VARIABLES = "variables";

	@HAPAttribute
	public static final String EXPRESSIONREF = "expressionRef";

	
	private List<HAPExecutableSegmentExpression> m_segments;
	
	private String m_type;
	
	public HAPExecutableExpression(String type) {
		this.m_segments = new ArrayList<HAPExecutableSegmentExpression>();
		this.m_type = type;
	}
	
	public String getType() {     return this.m_type;      }

	protected void addSegment(HAPExecutableSegmentExpression segment) {	this.m_segments.add(segment);	}
	
	public List<HAPExecutableSegmentExpression> getSegments(){    return this.m_segments;     }
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		HAPScriptFunctionInfo scriptFunctionInfo = HAPUtilityScriptForExecuteJSScript.buildExpressionFunctionInfo(this);
		
		String functionParmValue = "{}";
		List<HAPJSScriptInfo> childrenFun = scriptFunctionInfo.getChildren();
		if(!childrenFun.isEmpty()) {
			Map<String, String> funScriptMap = new LinkedHashMap<String, String>();
			Map<String, Class<?>> funScriptTypeMap = new LinkedHashMap<String, Class<?>>();
			for(HAPJSScriptInfo childFun : childrenFun) {
				funScriptMap.put(childFun.getName(), childFun.getScript());
				funScriptTypeMap.put(childFun.getName(), HAPJsonTypeAsItIs.class);
			}
			functionParmValue = HAPUtilityJson.buildMapJson(funScriptMap, funScriptTypeMap);
		}
		jsonMap.put(SUPPORTFUNCTION, functionParmValue);
		typeJsonMap.put(SUPPORTFUNCTION, HAPJsonTypeScript.class);

		jsonMap.put(SCRIPTFUNCTION, new HAPJsonTypeScript(scriptFunctionInfo.getMainScript().getScript()).toStringValue(HAPSerializationFormat.JSON_FULL));
		typeJsonMap.put(SCRIPTFUNCTION, HAPJsonTypeScript.class);

		jsonMap.put(EXPRESSIONREF, HAPUtilityJson.buildJson(this.discoverExpressionReference(null), HAPSerializationFormat.JSON));
		
		Set<String> vars = this.discoverVariables(null);
		jsonMap.put(VARIABLES, HAPUtilityJson.buildJson(vars, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		
	}

}
