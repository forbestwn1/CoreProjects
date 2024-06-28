package com.nosliw.core.application.common.scriptexpression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeAsItIs;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.domain.entity.expression.script.resource.js.HAPScriptFunctionInfo;
import com.nosliw.data.core.domain.entity.expression.script.resource.js.HAPUtilityScriptForExecuteJSScript;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;

@HAPEntityWithAttribute
public class HAPExpressionScript extends HAPExecutableImpEntityInfo{

	@HAPAttribute
	public final static String TYPE = "type";

	@HAPAttribute
	public final static String SEGMENT = "segment";

	@HAPAttribute
	public static final String SCRIPTFUNCTION = "scriptFunction";

	@HAPAttribute
	public static final String SUPPORTFUNCTION = "supportFunction";

	@HAPAttribute
	public static final String VARIABLESINFO = "variablesInfo";

	@HAPAttribute
	public static final String VARIABLEKEYS = "variableKeys";

	@HAPAttribute
	public static final String DATAEXPRESSIONIDS = "dataExpressionIds";

	private String m_type;
	
	private List<HAPSegmentScriptExpression> m_segments;
	
	private Set<String> m_varKeys = new HashSet<String>();

	private Set<String> m_dataExpressionId = new HashSet<String>();
	
	public HAPExpressionScript(String type) {
		this.m_segments = new ArrayList<HAPSegmentScriptExpression>();
		this.m_type = type;
	}
	
	public String getType() {     return this.m_type;      }

	protected void addSegment(HAPSegmentScriptExpression segment) {	this.m_segments.add(segment);	}
	public List<HAPSegmentScriptExpression> getSegments(){    return this.m_segments;     }
	
	public Set<String> getVariableKeys(){   return this.m_varKeys;    }
	public void addVariableKey(String key) {   this.m_varKeys.add(key);    }

	public Set<String> getDataExpressionIds(){   return this.m_dataExpressionId;    }
	public void addDataExpressionId(String id) {   this.m_dataExpressionId.add(id);    }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(VARIABLEKEYS, HAPUtilityJson.buildJson(this.m_varKeys, HAPSerializationFormat.JSON));
		jsonMap.put(DATAEXPRESSIONIDS, HAPUtilityJson.buildJson(this.m_dataExpressionId, HAPSerializationFormat.JSON));
		
		List<String> segmentArrayStr = new ArrayList<String>();
		for(HAPSegmentScriptExpression segment : this.m_segments) {
			segmentArrayStr.add(segment.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(SEGMENT, HAPUtilityJson.buildArrayJson(segmentArrayStr.toArray(new String[0])));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
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
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager) {
		
	}
}
